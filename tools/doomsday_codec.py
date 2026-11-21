#!/usr/bin/env python3
"""DoomsDay arithmetic-coder decoder, ported from net/java/l of Grim_Client_1.0.jar.

STATIC ANALYSIS TOOL - it only reads the jar. Nothing from the sample is executed; the
codec below was transcribed by hand from `javap -c -p net/java/l.class` plus the
decompiled source (work/deobf/l.java), because the class overloads field names and the
decompiler output alone is ambiguous.

Status: the XOR layer and container headers decode correctly for all three resources;
the range-coder bit stream still desyncs partway through, so no payloads come out yet.
Help wanted - the model indexing in net/java/l is the tricky part.

Usage:
    python3 doomsday_codec.py [jar] [-o outdir] [--only a|b|c] [--report]

Container format (see tools/doomsday_unpack.py):
    byte 0        model id  -> j = id%9, k = (id/9)%5, pow2 = (id/9)/5
    bytes 1..4    window size, little endian int
    bytes 5..12   decoded length, little endian long
    bytes 13..    arithmetic-coded stream

The coder is a context-mixing / PPM-style model:
  * binary decision l.a(Object,int):  bound = (range>>>11) * prob;
        unsigned(code) < bound -> bit 0, range = bound, prob += (2048-prob)>>>5
        else                       code -= bound, range -= bound, prob -= prob>>>5
        renormalise when (range & 0xFF000000)==0: code = code<<8 | nextByte(); range <<= 8
  * value decode l.b(Object): binary tree walk over a model array
  * output is written into a circular history window; symbols are either literals
    (binarised, optionally match-model guided) or LZ77-style matches whose position is
    coded through several order-N byte contexts.

Java integer semantics matter here: every arithmetic step is wrapped back to signed
32-bit / signed 16-bit exactly as the JVM would, and the shifts that javap shows as
iushr are done unsigned. That is what makes the port reproduce bit for bit.
"""
import argparse
import os
import struct
import sys
import zipfile

# ---------------------------------------------------------------- Java integer semantics
MASK32 = 0xFFFFFFFF
TRACE = bool(os.environ.get("DD_TRACE"))


def s32(x):
    x &= MASK32
    return x - 0x100000000 if x >= 0x80000000 else x


def u32(x):
    return x & MASK32


def s16(x):
    x &= 0xFFFF
    return x - 0x10000 if x >= 0x8000 else x


def s8(x):
    x &= 0xFF
    return x - 0x100 if x >= 0x80 else x


def nlz32(x):
    """Integer.numberOfLeadingZeros for a non-negative int."""
    return 32 if x == 0 else 32 - x.bit_length()


# ---------------------------------------------------------------- the decoder (net/java/l)
class DoomsDayDecoder:
    def __init__(self):
        # constructor initialisers, in bytecode order
        self.M_a192 = [1024] * 192          # l.a:[S   (literal decision, ctx = state*16 + pos&q)
        self.M_b12 = [1024] * 12           # l.b:[S
        self.M_c12 = [1024] * 12           # l.c:[S
        self.M_d12 = [1024] * 12           # l.d:[S
        self.M_e12 = [1024] * 12           # l.e:[S
        self.M_f192 = [1024] * 192         # l.f:[S   (match/extend decision)
        self.M_b64 = [[1024] * 64 for _ in range(4)]   # l.b:[[S  match position length class
        self.M_g114 = [1024] * 114         # l.g:[S   (match position bits)
        self.M_h16 = [1024] * 16           # l.h:[S   (match position tail value)
        self.M_i2 = [1024] * 2             # l.i:[S
        self.M_c8 = [None] * 16            # l.c:[[S
        self.M_d8 = [None] * 16            # l.d:[[S
        self.M_j256 = [1024] * 256         # l.j:[S
        self.m = 0                         # l.m:I   number of allocated c/d sub-models
        self.M_k2 = [1024] * 2             # l.k:[S
        self.M_e8 = [None] * 16            # l.e:[[S
        self.M_f8 = [None] * 16            # l.f:[[S
        self.M_l256 = [1024] * 256         # l.l:[S
        self.n = 0                         # l.n:I   number of allocated e/f sub-models
        self.o = -1                        # l.o:I   requested window size
        self.p = -1                        # l.p:I
        self.q = 0                         # l.q:I   position mask (1<<pow2)-1

        self.M_main = None                 # l.a:[[S  literal binarisation models, 768 each
        self.win = None                    # l.a:[B   circular history window
        self.wsize = 0                     # l.d:I
        self.wp = 0                        # l.c:I   write position
        self.rp = 0                        # l.e:I   read/flush position
        self.out = None                    # l.b:[B
        self.outpos = 0                    # l.f:I
        self.inbuf = None                  # l.c:[B
        self.ipos = 0                      # l.i:I
        self.range_ = 0                    # l.g:I   range
        self.code_ = 0                     # l.h:I   code
        self.j = 0                         # l.j:I   order of the byte-history context
        self.k = 0                         # l.k:I
        self.lmask = 0                     # l.l:I

    # ------------------------------------------------------------- range coder primitives
    def next_byte(self):
        b = self.inbuf[self.ipos] & 0xFF       # l.a()I : c:[B[i:I++] & 255
        self.ipos += 1
        return b

    def decode_bit(self, model, idx):
        """l.a(Object,int) - binary arithmetic decode + probability update."""
        m = model[idx]
        bound = s32((u32(self.range_) >> 11) * m)
        if u32(self.code_) < u32(bound):
            self.range_ = bound
            model[idx] = s16(m + (u32(2048 - m) >> 5))
            bit = 0
        else:
            self.code_ = s32(self.code_ - bound)
            self.range_ = s32(self.range_ - bound)
            model[idx] = s16(m - (u32(m) >> 5))
            bit = 1
        if (self.range_ & 0xFF000000) == 0:
            self.code_ = s32((self.code_ << 8) | self.next_byte())
            self.range_ = s32(self.range_ << 8)
        return bit

    def decode_value(self, model):
        """l.b(Object) - binary tree value decode."""
        bits = 31 - nlz32(len(model))
        v = 1
        for _ in range(bits):
            v = (v << 1) | self.decode_bit(model, v)
        return v - (1 << bits)

    def reset_model(self, model):
        """l.c(Object)."""
        for i in range(len(model)):
            model[i] = 1024

    # ------------------------------------------------------------- window helpers
    def win_byte(self, dist):
        """l.a(int) - history byte at distance `dist` behind the write position."""
        idx = self.wp - dist - 1
        if idx < 0:
            idx += self.wsize
        return s8(self.win[idx])

    def flush(self):
        """l.b()V - copy decoded window content into the output buffer."""
        n = self.wp - self.rp
        if n == 0:
            return
        self.out[self.outpos:self.outpos + n] = self.win[self.rp:self.wp]
        self.outpos += n
        if self.wp >= self.wsize:
            self.wp = 0
        self.rp = self.wp

    # ------------------------------------------------------------- container setup (l.b([B))
    def configure(self, header):
        """header = bytes 0..12 of the decrypted stream. Returns window size."""
        mid = header[0] & 0xFF
        jj = mid % 9
        kk = (mid // 9) % 5
        pow2 = (mid // 9) // 5
        if not (jj <= 8 and kk <= 4 and pow2 <= 4):
            raise ValueError("bad model byte %d" % mid)
        window = struct.unpack_from("<I", header, 1)[0]

        if self.M_main is None or self.j != jj or self.k != kk:
            self.k = kk
            self.lmask = (1 << kk) - 1
            self.j = jj
            cnt = 1 << (self.j + self.k)
            self.M_main = [[1024] * 768 for _ in range(cnt)]

        want = 1 << pow2
        while self.m < want:
            self.M_c8[self.m] = [1024] * 8
            self.M_d8[self.m] = [1024] * 8
            self.m += 1
        while self.n < want:
            self.M_e8[self.n] = [1024] * 8
            self.M_f8[self.n] = [1024] * 8
            self.n += 1
        self.q = want - 1

        if window < 0:
            raise ValueError("negative window size")
        if self.o != window:
            self.o = window
            self.p = max(self.o, 1)
            wsize = max(self.p, 4096)
            if self.win is None or self.wsize != wsize:
                self.win = bytearray(wsize)
            self.wsize = wsize
            self.wp = 0
            self.rp = 0
        return window

    # ------------------------------------------------------------- main decode (l.a([B,[B))
    def decode(self, data, outlen):
        self.inbuf = data
        self.ipos = 13                       # skip the container header
        self.flush()                         # l.a()V : flush + out = null
        self.out = bytearray(outlen)
        self.outpos = 0
        self.rp = 0
        self.wp = 0

        # every model is reset to 1024 (probability 1/2) at the start of a stream
        for mdl in (self.M_a192, self.M_f192, self.M_b12, self.M_c12, self.M_d12,
                    self.M_e12, self.M_g114):
            self.reset_model(mdl)
        for sub in self.M_main:
            self.reset_model(sub)
        for sub in self.M_b64:
            self.reset_model(sub)
        self.reset_model(self.M_i2)
        for i in range(self.m):
            self.reset_model(self.M_c8[i])
            self.reset_model(self.M_d8[i])
        self.reset_model(self.M_j256)
        self.reset_model(self.M_k2)
        for i in range(self.n):
            self.reset_model(self.M_e8[i])
            self.reset_model(self.M_f8[i])
        self.reset_model(self.M_l256)
        self.reset_model(self.M_h16)

        # range coder init: range = -1, then five bytes shifted into code (32-bit wrap)
        self.code_ = 0
        self.range_ = -1
        for _ in range(5):
            self.code_ = s32((self.code_ << 8) | self.next_byte())

        state = 0            # var17:B  model/order state, 0..11
        mpos = 0             # var20:I  current match position
        h1 = h2 = h3 = 0     # var5/var6/var7 : match-position history
        produced = 0         # var10:J
        lastbyte = 0         # var8:I (assigned through (byte) casts, so signed)

        while produced < outlen:
            pos_q = produced & self.q

            # ---------------------------------------------------------- literal
            if self.decode_bit(self.M_a192, (state << 4) + pos_q) == 0:
                sub = self.M_main[((produced & self.lmask) << self.j)
                                  + ((lastbyte & 0xFF) >> (8 - self.j))]
                if state >= 7:
                    pred = self.win_byte(mpos)          # predicted byte from the match position
                    acc = 1
                    while True:
                        pb = (pred >> 7) & 1
                        pred = s8(pred << 1)
                        dec = self.decode_bit(sub, ((pb + 1) << 8) + acc)
                        acc = acc << 1 | dec
                        if pb != dec:
                            while acc < 256:
                                acc = acc << 1 | self.decode_bit(sub, acc)
                            break
                        if acc >= 256:
                            break
                    lastbyte = s8(acc)
                else:
                    v = 1
                    while True:
                        v = v << 1 | self.decode_bit(sub, v)
                        if v >= 256:
                            break
                    lastbyte = s8(v)

                self.win[self.wp] = lastbyte & 0xFF
                self.wp += 1
                if self.wp >= self.wsize:
                    self.flush()

                state = 0 if state < 4 else (state - 3 if state < 10 else state - 6)
                produced += 1
                continue

            # ---------------------------------------------------------- match
            if self.decode_bit(self.M_b12, state) == 1:
                lastbyte = 0
                if self.decode_bit(self.M_c12, state) == 0:
                    if self.decode_bit(self.M_f192, (state << 4) + pos_q) == 0:
                        state = 9 if state < 7 else 11
                        lastbyte = 1
                else:
                    # pick one of the three remembered match positions
                    if self.decode_bit(self.M_d12, state) == 0:
                        chosen = h1
                    else:
                        if self.decode_bit(self.M_e12, state) == 0:
                            chosen = h2
                        else:
                            chosen = h3
                            h2 = h1
                        h1 = mpos
                    mpos = chosen

                if lastbyte == 0:
                    if self.decode_bit(self.M_k2, 0) == 0:
                        length = self.decode_value(self.M_e8[pos_q])
                    elif self.decode_bit(self.M_k2, 1) == 0:
                        length = 8 + self.decode_value(self.M_f8[pos_q])
                    else:
                        length = 16 + self.decode_value(self.M_l256)
                    lastbyte = length + 2
                    state = 8 if state < 7 else 11
            else:
                h3, h2, h1 = h2, h1, mpos
                if self.decode_bit(self.M_i2, 0) == 0:
                    length = self.decode_value(self.M_c8[pos_q])
                elif self.decode_bit(self.M_i2, 1) == 0:
                    length = 8 + self.decode_value(self.M_d8[pos_q])
                else:
                    length = 16 + self.decode_value(self.M_j256)
                lastbyte = length + 2
                state = 7 if state < 7 else 10

                # ---- decode the new match position
                cls = self.decode_value(self.M_b64[length if length < 4 else 3])
                if cls >= 4:
                    e_ = (cls >> 1) - 1
                    base = s32((2 | (cls & 1)) << e_)
                    if cls < 14:
                        acc = 1
                        bits = 0
                        for t in range(e_):
                            d = self.decode_bit(self.M_g114, s32(base - cls - 1 + acc))
                            acc = (acc << 1) + d
                            bits |= d << t
                        mpos = s32(base + bits)
                    else:
                        cnt = e_ - 4
                        acc = 0
                        while cnt != 0:
                            self.range_ = u32(self.range_) >> 1
                            flag = u32(self.code_ - self.range_) >> 31     # 0 or 1
                            self.code_ = s32(self.code_ - (self.range_ & (flag - 1)))
                            acc = acc << 1 | (1 - flag)
                            if (self.range_ & 0xFF000000) == 0:
                                self.code_ = s32((self.code_ << 8) | self.next_byte())
                                self.range_ = s32(self.range_ << 8)
                            cnt -= 1
                        pos = s32(base + (acc << 4))
                        bits = 0
                        v = 1
                        for t in range(31 - nlz32(len(self.M_h16))):
                            d = self.decode_bit(self.M_h16, v)
                            v = (v << 1) + d
                            bits |= d << t
                        mpos = s32(pos + bits)
                        if mpos < 0:
                            if mpos != -1:
                                if TRACE:
                                    print("   FAIL negative mpos=%d at produced=%d cls=%d"
                                          % (mpos, produced, cls))
                                return None
                            break
                else:
                    mpos = cls

            if mpos >= produced or mpos >= self.p:
                if TRACE:
                    print("   FAIL match-check at produced=%d state=%d mpos=%d len=%d p=%d"
                          % (produced, state, mpos, lastbyte, self.p))
                    print("   window[0:120]:", bytes(self.win[:min(self.wp, 120)]))
                return None

            # ---- copy the match out of the circular window
            src = self.wp - mpos - 1
            if src < 0:
                src += self.wsize
            for _ in range(lastbyte):
                if src >= self.wsize:
                    src = 0
                self.win[self.wp] = self.win[src]
                src += 1
                self.wp += 1
                if self.wp >= self.wsize:
                    self.flush()

            produced += lastbyte
            lastbyte = self.win_byte(0)

        self.flush()
        self.flush()
        if TRACE:
            print("   stream ended early at produced=%d / %d" % (produced, outlen))
        return bytes(self.out[:self.outpos])


# ---------------------------------------------------------------- jar level helpers
RES = {"a": ("/net/java/a", -8461988079585481081),
       "b": ("/net/java/b", -4062297973245990737),
       "c": ("/net/java/c", -1083759330220665782)}


def xor(data, key_long):
    kb = struct.pack(">q", key_long)
    return bytes(b ^ kb[i % 8] for i, b in enumerate(data))


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("jar", nargs="?", default="../Grim_Client_1.0.jar")
    ap.add_argument("-o", "--outdir", default="work/doom/decoded")
    ap.add_argument("--only", choices="abc")
    args = ap.parse_args()

    zf = zipfile.ZipFile(args.jar)
    os.makedirs(args.outdir, exist_ok=True)
    targets = [args.only] if args.only else list("abc")

    for tag in targets:
        name, key = RES[tag]
        raw = zf.read(name.lstrip("/"))
        dec = xor(raw, key)
        outlen = struct.unpack_from("<Q", dec, 5)[0]
        d = DoomsDayDecoder()
        d.configure(dec[:13])
        print("net/java/%s : model=%d (j=%d k=%d pow2ctx=%d) window=%d outlen=%d"
              % (tag, dec[0], d.j, d.k, d.q + 1, d.wsize, outlen), flush=True)
        out = d.decode(dec, outlen)
        if out is None:
            print("   DECODE FAILED (desync)")
            continue
        path = os.path.join(args.outdir, "net_java_%s.bin" % tag)
        open(path, "wb").write(out)
        print("   -> %d bytes (%s)" % (len(out), "OK" if len(out) == outlen else "SIZE MISMATCH"),
              flush=True)
    return 0


if __name__ == "__main__":
    sys.exit(main())

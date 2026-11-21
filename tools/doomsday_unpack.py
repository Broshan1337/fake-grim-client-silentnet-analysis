#!/usr/bin/env python3
"""Unpacker for the "DoomsDay" Java protector layer.

XOR keys and container-header layout transcribed from net/java/n and net/java/l
(see ../protector/DoomsDay.md).

Usage:
    python3 doomsday_unpack.py <jar> [-o outdir]

What it does:
  * extracts the five DoomsDay resources  net/java/{a,b,c,d,e}  and  /64FV7OQUIMC8I
  * XOR-decrypts them with the three 8-byte keys stored in net/java/n
  * parses the arithmetic-coder container header (model byte, window size,
    declared output length) and reports whether the decryption validated

The bodies themselves are compressed with a context-mixing range coder in
net/java/l; this script stops at the header check, which proves the keys.
"""
import struct
import sys
import zipfile

# --- constants recovered from net/java/n -------------------------------------------------
RES_A, RES_B, RES_C, RES_D, RES_E = ("/net/java/a", "/net/java/b", "/net/java/c",
                                     "/net/java/d", "/net/java/e")
BLOB = "/64FV7OQUIMC8I"

KEY_A = -1083759330220665782   # net/java/n.a (long)
KEY_B = -4062297973245990737   # net/java/n.b (long)
KEY_C = -8461988079585481081   # net/java/n.c (long)

# pairing used by net/java/l.a()/b()/c()  (resource name -> key field)
PAIRS = {
    "a": (RES_A, KEY_C),   # l.a(): name = l.c ("/net/java/a"), key = l.c
    "b": (RES_B, KEY_B),   # l.b(): name = l.b ("/net/java/b"), key = l.b
    "c": (RES_C, KEY_A),   # l.c(): name = l.a ("/net/java/c"), key = l.a
}


def to_signed64(v):
    v &= (1 << 64) - 1
    return v - (1 << 64) if v >= (1 << 63) else v


def key_bytes(key_long):
    """net/java/l.a(long): the long written big-endian, used as an 8 byte XOR key."""
    return struct.pack(">q", key_long)


def xor(data, kb):
    return bytes(b ^ kb[i % 8] for i, b in enumerate(data))


def parse_header(dec):
    """Container written by the DoomsDay compressor.

    byte 0        model id (j,k,pow2 packed as ((pow2*5 + k)*9 + j))
    bytes 1..4    window size, little endian int
    bytes 5..12   decoded output length, little endian long
    """
    if len(dec) < 13:
        return None
    model = dec[0]
    window = struct.unpack_from("<I", dec, 1)[0]
    outlen = struct.unpack_from("<Q", dec, 5)[0]
    j = model % 9
    k = (model // 9) % 5
    pow2 = (model // 9) // 5
    return dict(model=model, j=j, k=k, pow2=pow2, window=window, outlen=outlen)


def main():
    if len(sys.argv) < 2:
        print(__doc__)
        return 1
    jar = sys.argv[1]
    outdir = None
    if "-o" in sys.argv:
        outdir = sys.argv[sys.argv.index("-o") + 1]

    zf = zipfile.ZipFile(jar)
    names = set(zf.namelist())

    print("== DoomsDay resources ==")
    for tag, (res, key) in PAIRS.items():
        raw = zf.read(res.lstrip("/"))
        dec = xor(raw, key_bytes(key))
        h = parse_header(dec)
        ok = h and 1 <= h["pow2"] <= 8 and h["window"] > 0 and 0 < h["outlen"] < 512 * 1024 * 1024
        print("  net/java/%s : %9d bytes stored -> key %-22d model=%3d (j=%d k=%d pow2=%d) "
              "window=0x%08x decoded_size=%d  %s"
              % (tag, len(raw), key, h["model"], h["j"], h["k"], h["pow2"],
                 h["window"], h["outlen"], "OK" if ok else "INVALID"))
        if outdir:
            with open("%s/net_java_%s.xor" % (outdir, tag), "wb") as fh:
                fh.write(dec)

    print("\n== native payload blob ==")
    raw = zf.read(BLOB.lstrip("/"))
    print("  %-16s : %9d bytes   tail(4096) first bytes: %s"
          % (BLOB, len(raw), raw[-4096:-4080].hex()))
    print("  net/java/g patches 8 bytes at offset len-4096+48 with a value taken from")
    print("  the /net/java/c container before handing the blob to native l.n(Object).")

    for extra in (RES_D, RES_E):
        print("  %-16s : %9d bytes (passed as an argument to the hidden main class)"
              % (extra, len(zf.read(extra.lstrip("/")))))
    return 0


if __name__ == "__main__":
    sys.exit(main())

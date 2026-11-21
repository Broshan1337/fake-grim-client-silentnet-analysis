#!/usr/bin/env python3
"""Decrypt the RT_RCDATA blob inside Grim_Client's app.pyd (stage 2).

The module reads one PE resource - RT_RCDATA id 3 - and "decrypts" it with a small
obfuscated loop: an S-box substitution driven by a rolling key byte that advances by
the loop index plus a per-position constant from a jump table. No real crypto, no key
material beyond the two tables inside .rdata.

    out[i-16] = SBOX[((i-8 + bl) & 0xff) ^ res[i]]      i = 8 .. size-1
    bl        = SBOX[...] then bl += / -= CONST[i & 7]   (bl feeds the next round)
    out[0:16] = res[0:16]                                (header copied verbatim)

Layout after decryption: u32 checksum, u32 length, then a Nuitka ".bytecode"
constants blob - all Python-level strings, names and code-object metadata of the
compiled stealer.

The S-box address and the jump table are located by their instruction patterns so the
tool keeps working if the module is rebuilt at a different offset.

    python3 app_pyd_decrypt.py app.pyd [-o out.bin] [--strings]
"""
import argparse, re, struct, sys

# movzx ebx, byte ptr [rbx + r9 + disp32]  -> S-box base offset
SBOX_INSN = bytes.fromhex('420fb69c0b')
# mov eax, dword ptr [r9 + rax*4 + disp32] -> jump-table base offset
JT_INSN   = bytes.fromhex('418b8481')
# the loop body anchor: lea ecx,[rdx-8]; add cl,bl; movzx ebx,cl; xor rbx,rax
ANCHOR    = bytes.fromhex('8d4af802cb0fb6d94833d8')


class PE:
    def __init__(self, data):
        self.d = data
        self.image_base = struct.unpack_from('<Q', data,
            struct.unpack_from('<I', data, 0x3c)[0] + 0x18 + 0x18)[0]
        nt = struct.unpack_from('<I', data, 0x3c)[0]
        nsec = struct.unpack_from('<H', data, nt + 6)[0]
        opt  = struct.unpack_from('<H', data, nt + 20)[0]
        self.sections = []
        for i in range(nsec):
            o = nt + 24 + opt + i * 40
            name = data[o:o+8].rstrip(b'\0').decode('latin1')
            vsz, va, rawsz, raw = struct.unpack_from('<IIII', data, o + 8)
            self.sections.append((name, va, vsz, raw, rawsz))

    def v2f(self, v):
        v -= self.image_base
        for _, va, vsz, raw, rawsz in self.sections:
            if va <= v < va + max(vsz, rawsz):
                return raw + (v - va)
        raise ValueError('bad vaddr %#x' % v)

    def rva_to_exec_data(self):
        """Raw bytes of every executable section (where the code lives)."""
        out = b''
        for name, va, vsz, raw, rawsz in self.sections:
            if name == '.text':
                out += self.d[raw:raw + rawsz]
        return out

    def resource(self, type_id, name_id):
        """Raw bytes of a resource by numeric type/name ids (RT_RCDATA=10, name 3)."""
        import pefile
        pe = pefile.PE(data=self.d, fast_load=True)
        pe.parse_data_directories(directories=[pefile.DIRECTORY_ENTRY['IMAGE_DIRECTORY_ENTRY_RESOURCE']])
        for t in pe.DIRECTORY_ENTRY_RESOURCE.entries:
            if t.id != type_id:
                continue
            for n in t.directory.entries:
                if n.id != name_id:
                    continue
                lang = n.directory.entries[0]
                ds = lang.data.struct
                return self.d[pe.get_offset_from_rva(ds.OffsetToData):][:ds.Size]
        raise ValueError('resource %d/%d not found' % (type_id, name_id))


def find_tables(pe):
    code = pe.rva_to_exec_data()
    i = code.find(ANCHOR)
    if i < 0:
        raise SystemExit('decrypt loop not found - is this the same app.pyd?')
    j = code.find(SBOX_INSN, i)
    sbox_rva = struct.unpack_from('<I', code, j + len(SBOX_INSN))[0]
    k = code.find(JT_INSN, i)
    jt_rva = struct.unpack_from('<I', code, k + len(JT_INSN))[0]
    # jump table entries are image-relative offsets into .text
    consts = []
    text = next(s for s in pe.sections if s[0] == '.text')
    raw_jt = text[3] + (jt_rva - text[1])
    for n in range(8):
        off = struct.unpack_from('<I', pe.d, raw_jt + n * 4)[0]
        insn = pe.d[pe.v2f(pe.image_base + off)]
        imm = pe.d[pe.v2f(pe.image_base + off) + 2]
        consts.append(('add' if insn == 0x80 and pe.d[pe.v2f(pe.image_base + off) + 1] == 0xc3 else 'sub', imm))
    sbox = pe.d[pe.v2f(pe.image_base + sbox_rva):][:256]
    return sbox, consts


def decrypt(res, sbox, consts):
    out = bytearray(len(res))
    out[:16] = res[:16]
    bl = 0
    for i in range(8, len(res)):
        val = sbox[((i - 8 + bl) & 0xff) ^ res[i]]
        if i >= 0x18:
            out[i - 16] = val
        bl = val
        op, k = consts[i & 7]
        bl = (bl + k) & 0xff if op == 'add' else (bl - k) & 0xff
    return bytes(out)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('pyd')
    ap.add_argument('-o', '--out', default='app_constants.bin')
    ap.add_argument('--strings', action='store_true', help='also dump readable constants')
    a = ap.parse_args()

    pe = PE(open(a.pyd, 'rb').read())
    res = pe.resource(10, 3)                      # RT_RCDATA = 10, name id = 3
    sbox, consts = find_tables(pe)
    print('resource %d bytes; S-box is a permutation: %s' % (len(res), sorted(sbox) == list(range(256))))
    print('key schedule per (i&7):', ', '.join('%s%d' % (o, k) for o, k in consts))

    out = decrypt(res, sbox, consts)
    checksum, length = struct.unpack_from('<II', out, 0)
    body = out[8:8 + length]
    print('stored checksum %#010x  declared length %d  -> magic %r' % (checksum, length, body[:9]))
    open(a.out, 'wb').write(body)
    print('wrote', a.out)

    if a.strings:
        txt = '\n'.join(s.decode('latin1') for s in re.findall(rb'[\x20-\x7e]{4,}', body))
        open(a.out + '.strings.txt', 'w').write(txt)
        print('wrote', a.out + '.strings.txt')


if __name__ == '__main__':
    main()

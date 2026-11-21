#!/usr/bin/env python3
"""Minimal Java class-file reader: field/method tables + resolved bytecode refs.

Handy when a class abuses duplicate field names (the DoomsDay layer does): the descriptor
next to each ref says which field is really meant.
"""
import struct, sys


def u1(b, i): return b[i]
def u2(b, i): return struct.unpack_from('>H', b, i)[0]
def u4(b, i): return struct.unpack_from('>I', b, i)[0]


class ClassFile:
    def __init__(self, data):
        self.b = data
        assert self.b[:4] == b'\xca\xfe\xba\xbe', "not a class file"
        self.cp_count = u2(self.b, 8)
        self.cp = [None] * self.cp_count
        i = 10
        idx = 1
        while idx < self.cp_count:
            tag = self.b[i]; i += 1
            if tag in (1,):
                n = u2(self.b, i); i += 2
                self.cp[idx] = ('Utf8', self.b[i:i + n].decode('utf-8', 'replace')); i += n
            elif tag == 7:
                self.cp[idx] = ('Class', u2(self.b, i)); i += 2
            elif tag in (8, 16, 19, 20):
                self.cp[idx] = ({8: 'String', 16: 'MT', 19: 'IM', 20: 'CMI'}[tag], u2(self.b, i)); i += 2
            elif tag in (15,):
                i += 3
            elif tag in (3, 4, 9, 10, 11, 12, 17, 18):
                self.cp[idx] = ({3: 'Int', 4: 'Float', 9: 'Fieldref', 10: 'Methodref', 11: 'IFref',
                                 12: 'NameType', 17: 'Dyn', 18: 'InvDyn'}[tag], u2(self.b, i), u2(self.b, i + 2)); i += 4
            elif tag in (5, 6):
                self.cp[idx] = ('Long' if tag == 5 else 'Double', struct.unpack_from('>q' if tag == 5 else '>d', self.b, i)[0])
                i += 8; idx += 1
            else:
                raise ValueError('bad cp tag %d at %d' % (tag, i))
            idx += 1
        self.cp_end = i
        i += 2  # access flags
        self.this_class = u2(self.b, i); i += 4  # this + super
        ifd = u2(self.b, i); i += 2
        self.interfaces = [u2(self.b, i + 2 * k) for k in range(ifd)]
        i += 2 * ifd
        self.fields = []
        nf = u2(self.b, i); i += 2
        for _ in range(nf):
            acc = u2(self.b, i); ni = u2(self.b, i + 2); di = u2(self.b, i + 4)
            attrn = u2(self.b, i + 6); i += 8
            for _a in range(attrn):
                ln = u4(self.b, i + 2); i += 6 + ln
            self.fields.append((self.utf8(ni), self.utf8(di), acc))
        self.methods = []
        nm = u2(self.b, i); i += 2
        for _ in range(nm):
            acc = u2(self.b, i); ni = u2(self.b, i + 2); di = u2(self.b, i + 4)
            attrn = u2(self.b, i + 6); j = i + 8
            code = None
            for _a in range(attrn):
                an = self.utf8(u2(self.b, j))
                ln = u4(self.b, j + 2)
                if an == 'Code':
                    clen = u4(self.b, j + 8)
                    code = self.b[j + 12:j + 12 + clen]
                j += 6 + ln
            self.methods.append((self.utf8(ni), self.utf8(di), acc, code))
            i = j

    def utf8(self, i):
        v = self.cp[i]
        return v[1] if v and v[0] == 'Utf8' else '?'

    def const(self, i):
        v = self.cp[i]
        if v is None:
            return '?'
        if v[0] == 'Fieldref':
            cls = self.utf8(self.cp[v[1]][1])
            nt = self.cp[v[2]]
            return '%s.%s:%s' % (cls, self.utf8(nt[1]), self.utf8(nt[2]))
        if v[0] in ('Methodref', 'IFref'):
            cls = self.utf8(self.cp[v[1]][1])
            nt = self.cp[v[2]]
            return '%s.%s%s' % (cls, self.utf8(nt[1]), self.utf8(nt[2]))
        if v[0] == 'Class':
            return self.utf8(v[1])
        if v[0] == 'String':
            return repr(self.utf8(v[1]))
        if v[0] == 'Utf8':
            return repr(v[1])
        return '%s(%r)' % (v[0], v[1])


OPS = {0x00:('nop',0),0x01:('aconst_null',0),0x02:('iconst_m1',0),0x03:('iconst_0',0),0x04:('iconst_1',0),
0x05:('iconst_2',0),0x06:('iconst_3',0),0x07:('iconst_4',0),0x08:('iconst_5',0),0x09:('lconst_0',0),
0x0a:('lconst_1',0),0x0b:('fconst_0',0),0x0c:('fconst_1',0),0x0d:('dconst_0',0),0x0e:('dconst_1',0),
0x0f:('bipush',1),0x10:('sipush',2),0x11:('ldc',1),0x12:('ldc_w',2),0x13:('ldc2_w',2),0x14:('iload',1),
0x15:('lload',1),0x16:('fload',1),0x17:('dload',1),0x18:('aload',1),0x19:('iload_0',0),0x1a:('iload_1',0),
0x1b:('iload_2',0),0x1c:('iload_3',0),0x1d:('lload_0',0),0x1e:('lload_1',0),0x1f:('lload_2',0),0x20:('lload_3',0),
0x21:('fload_0',0),0x22:('fload_1',0),0x23:('fload_2',0),0x24:('fload_3',0),0x25:('dload_0',0),0x26:('dload_1',0),
0x27:('dload_2',0),0x28:('dload_3',0),0x29:('aload_0',0),0x2a:('aload_1',0),0x2b:('aload_2',0),0x2c:('aload_3',0),
0x2d:('iaload',0),0x2e:('laload',0),0x33:('baload',0),0x34:('caload',0),0x35:('saload',0),0x36:('istore',1),
0x37:('lstore',1),0x3a:('astore',1),0x3b:('istore_0',0),0x3c:('istore_1',0),0x3d:('istore_2',0),0x3e:('istore_3',0),
0x3f:('lstore_0',0),0x40:('lstore_1',0),0x41:('lstore_2',0),0x42:('lstore_3',0),0x47:('astore_0',0),
0x48:('astore_1',0),0x49:('astore_2',0),0x4a:('astore_3',0),0x4b:('iastore',0),0x4c:('lastore',0),
0x4f:('bastore',0),0x50:('castore',0),0x51:('sastore',0),0x52:('pop',0),0x57:('dup',0),0x59:('dup_x1',0),
0x5a:('dup_x2',0),0x5f:('swap',0),0x60:('iadd',0),0x61:('ladd',0),0x64:('isub',0),0x65:('lsub',0),
0x68:('imul',0),0x69:('lmul',0),0x6c:('idiv',0),0x6d:('ldiv',0),0x70:('irem',0),0x74:('ineg',0),
0x75:('lneg',0),0x78:('ishl',0),0x79:('lshl',0),0x7a:('ishr',0),0x7b:('lshr',0),0x7c:('iushr',0),
0x7d:('lushr',0),0x7e:('iand',0),0x7f:('land',0),0x80:('ior',0),0x81:('lor',0),0x82:('ixor',0),0x83:('lxor',0),
0x84:('iinc',2),0x85:('i2l',0),0x87:('i2b',0),0x88:('i2c',0),0x89:('i2s',0),0x8a:('l2i',0),0x8b:('l2f',0),
0x8c:('l2d',0),0x91:('fcmpl',0),0x93:('dcmpl',0),0x94:('ifeq',2),0x95:('ifne',2),0x96:('iflt',2),0x97:('ifge',2),
0x98:('ifgt',2),0x99:('ifle',2),0x9a:('if_icmplt',2),0x9b:('if_icmpge',2),0x9c:('if_icmpgt',2),0x9d:('if_icmple',2),
0x9e:('if_acmpeq',2),0x9f:('if_acmpne',2),0xa0:('goto',2),0xa7:('areturn',0),0xaa:('tableswitch',-1),
0xab:('lookupswitch',-1),0xac:('ireturn',0),0xb1:('return',0),0xb2:('getstatic',2),0xb3:('putstatic',2),
0xb4:('getfield',2),0xb5:('putfield',2),0xb6:('invokevirtual',2),0xb7:('invokespecial',2),0xb8:('invokestatic',2),
0xbb:('new',2),0xbd:('anewarray',2),0xbf:('checkcast',2),0xc0:('instanceof',2),0xc1:('monitorenter',0),
0xc2:('monitorexit',0),0xc3:('wide',-1),0xb9:('invokeinterface',4),0xba:('invokedynamic',4),
0xbe:('newarray',1),0xc6:('ifnull',2),0xc7:('ifnonnull',2),0xc8:('goto_w',4),0xa9:('ret',1)}


def disasm(cf, code, label=''):
    out = []
    i = 0
    n = len(code)
    while i < n:
        op = code[i]
        name, ln = OPS.get(op, ('op%02x' % op, 0))
        if name in ('tableswitch', 'lookupswitch'):
            j = (i + 4) & ~3
            default = struct.unpack_from('>i', code, j)[0]
            if name == 'tableswitch':
                low = struct.unpack_from('>i', code, j + 4)[0]
                high = struct.unpack_from('>i', code, j + 8)[0]
                out.append('%5d: tableswitch %d..%d default=%d' % (i, low, high, default))
                i = j + 12 + 4 * (high - low + 1)
            else:
                npairs = struct.unpack_from('>i', code, j + 4)[0]
                out.append('%5d: lookupswitch %d pairs default=%d' % (i, npairs, default))
                i = j + 8 + 8 * npairs
            continue
        if name == 'wide':
            wop = code[i + 1]
            idx = struct.unpack_from('>H', code, i + 2)[0]
            out.append('%5d: wide %s %d' % (i, OPS.get(wop, ('?', 0))[0], idx))
            i += 4 if wop != 0x84 else 6
            continue
        args = code[i + 1:i + 1 + ln]
        txt = '%5d: %s' % (i, name)
        if name in ('getstatic', 'putstatic', 'getfield', 'putfield'):
            txt += '   #' + str(u2(args, 0)) + '  ' + cf.const(u2(args, 0))
        elif name in ('invokevirtual', 'invokespecial', 'invokestatic', 'invokeinterface', 'invokedynamic'):
            txt += '   #' + str(u2(args, 0)) + '  ' + cf.const(u2(args, 0))
        elif name in ('ldc', 'ldc_w', 'ldc2_w'):
            ci = args[0] if name == 'ldc' else u2(args, 0)
            txt += '   ' + cf.const(ci)
        elif name in ('new', 'anewarray', 'checkcast', 'instanceof'):
            txt += '   ' + cf.const(u2(args, 0))
        elif name == 'bipush':
            txt += '   %d' % struct.unpack('b', args)[0]
        elif name == 'sipush':
            txt += '   %d' % struct.unpack('>h', args)[0]
        elif ln:
            txt += '   ' + ' '.join(str(x) for x in args)
        out.append(txt)
        i += 1 + ln
    return out


def main():
    path = sys.argv[1]
    cf = ClassFile(open(path, 'rb').read())
    print('class', cf.utf8(cf.cp[cf.this_class][1]))
    print('--- fields (declaration order) ---')
    for k, (nm, ds, acc) in enumerate(cf.fields):
        print('  [%02d] %s : %s   flags=0x%x' % (k, nm, ds, acc))
    if len(sys.argv) > 2:
        want = sys.argv[2]
        for nm, ds, acc, code in cf.methods:
            if nm == want or want == '*':
                print('--- method %s%s ---' % (nm, ds))
                if code:
                    print('\n'.join(disasm(cf, code)))


if __name__ == '__main__':
    main()

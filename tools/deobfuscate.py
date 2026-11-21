#!/usr/bin/env python3
"""
deobfuscate.py -- source level deobfuscator for the string layer of Grim_Client_1.0.jar

The sample builds every interesting string at runtime out of constant integer /
char arithmetic, in three shapes:

  1. StringBuilder chains
       StringBuilder v = new StringBuilder();
       v.append((char)(8814 ^ 8771));            // 'C'
       ... v.toString()
  2. char[] arrays
       char[] a = new char[]{(char)(6901 - 6797), (char)('뮙' - '묥'), ...};
       ... new String(a)
  3. inline (char)(expr) casts anywhere

The script folds all of them back into real string literals and removes the now
dead bookkeeping statements. Pure text rewriting over vineflower output.

usage: deobfuscate.py <file.java> [-o out.java] [--stats]
"""
import re
import sys

CHAR_LIT = r"'(?:\\'|\\[rntbfn0]|\\u[0-9a-fA-F]{4}|\\[0-7]{1,3}|[^'\\])'"


def parse_char_literal(tok: str) -> int:
    """Evaluate a Java char literal as emitted by vineflower."""
    body = tok[1:-1]
    if body.startswith("\\u"):
        return int(body[2:6], 16)
    if body.startswith("\\"):
        c = body[1]
        simple = {"n": 10, "r": 13, "t": 9, "b": 8, "f": 12, "\\": 92, "'": 39, '"': 34}
        if c in simple:
            return simple[c]
        if c.isdigit():
            return int(body[1:], 8)
        raise ValueError("bad escape " + body)
    return ord(body)


TOKEN_RE = re.compile(
    r"""
      (?P<char>{ch})
    | (?P<hex>0[xX][0-9a-fA-F]+)
    | (?P<int>\d+)
    | (?P<op>[\^\-\*/%\+])
    | (?P<paren>[()])
    | (?P<ws>\s+)
    """.format(ch=CHAR_LIT),
    re.VERBOSE,
)


def eval_expr(s: str):
    """Evaluate constant int/char arithmetic; None when not fully constant."""
    toks = []
    i = 0
    while i < len(s):
        m = TOKEN_RE.match(s, i)
        if not m:
            return None
        i = m.end()
        k, v = m.lastgroup, m.group()
        if k == "ws":
            continue
        if k == "char":
            try:
                toks.append(("num", parse_char_literal(v)))
            except ValueError:
                return None
        elif k == "int":
            toks.append(("num", int(v)))
        elif k == "hex":
            toks.append(("num", int(v, 16)))
        else:
            toks.append((k, v))
    if not toks:
        return None
    pos = 0

    def peek():
        return toks[pos] if pos < len(toks) else (None, None)

    def primary():
        nonlocal pos
        k, v = peek()
        if k == "num":
            pos += 1
            return v
        if k == "op" and v in "+-":
            pos += 1
            r = primary()
            return None if r is None else (r if v == "+" else -r)
        if k == "paren":
            pos += 1
            r = expr(0)
            if peek()[0] != "paren" or peek()[1] != ")":
                return None
            pos += 1
            return r
        return None

    def expr(minprec):
        nonlocal pos
        left = primary()
        if left is None:
            return None
        while True:
            k, v = peek()
            if k != "op":
                break
            prec = {"*": 3, "/": 3, "%": 3, "+": 2, "-": 2, "^": 1}[v]
            if prec < minprec:
                break
            pos += 1
            right = expr(prec + 1)
            if right is None:
                return None
            if v == "+":
                left += right
            elif v == "-":
                left -= right
            elif v == "*":
                left *= right
            elif v == "/":
                if right == 0:
                    return None
                left = int(left / right)
            elif v == "%":
                if right == 0:
                    return None
                left %= right
            elif v == "^":
                left ^= right
        return left

    r = expr(0)
    return r if pos == len(toks) else None


def esc_char(cp: int) -> str:
    cp &= 0xFFFF
    table = {39: "'\\''", 92: "'\\\\'", 10: "'\\n'", 13: "'\\r'", 9: "'\\t'"}
    if cp in table:
        return table[cp]
    if 32 <= cp < 127:
        return "'%s'" % chr(cp)
    return "'\\u%04x'" % cp


def esc_str(s: str) -> str:
    out = []
    for ch in s:
        o = ord(ch)
        if ch == '"':
            out.append('\\"')
        elif ch == "\\":
            out.append("\\\\")
        elif ch == "\n":
            out.append("\\n")
        elif ch == "\r":
            out.append("\\r")
        elif ch == "\t":
            out.append("\\t")
        elif 32 <= o < 127:
            out.append(ch)
        else:
            out.append("\\u%04x" % o)
    return '"' + "".join(out) + '"'


# ---------------------------------------------------------------- pass 1: (char)(expr)

CHARCAST_RE = re.compile(r"\(\s*char\s*\)\s*(\(?[^();{}=]*?\))", re.S)
CHARCAST_BARE_RE = re.compile(r"\(\s*char\s*\)\s*(" + CHAR_LIT + r"|" + r"\d+)")


def fold_char_casts(text: str, stats: dict) -> str:
    def sub(m):
        v = eval_expr(m.group(1))
        return m.group(0) if v is None else esc_char(v)

    prev = None
    while prev != text:
        prev = text
        text = CHARCAST_BARE_RE.sub(
            lambda m: esc_char(eval_expr(m.group(1))) if eval_expr(m.group(1)) is not None else m.group(0),
            text,
        )
        text = CHARCAST_RE.sub(sub, text)
    return text


# ---------------------------------------------------------------- pass 2: string assembly

CHARRUN = "(?:[^{}']|'(?:\\\\'|\\\\.|[^'\\\\])*')*"


DECL_RE = re.compile(
    r"^[ \t]*(?:(?:final|public|private|protected|static)\s+)?[\w<>,\.\[\]]*[ \t]*"
    r"(?P<var>\w+)[ \t]*=[ \t]*(?:(?P<other>\w+)[ \t]*=[ \t]*)?new\s+StringBuilder\s*\(\s*\)"
    r"(?P<chain>(?:\.append\([^\n]*\))*)[ \t]*;[ \t]*$", re.M)
PAREN_DECL_RE = re.compile(
    r"^[ \t]*\([ \t]*(?P<var>\w+)[ \t]*=[ \t]*new\s+StringBuilder\s*\(\s*\)[ \t]*\)"
    r"(?P<chain>(?:\.append\([^\n]*\))*)[ \t]*;[ \t]*$", re.M)
APPEND_HEAD_RE = re.compile(r"^[ \t]*(?P<var>\w+)(?P<dot>\.append\s*\()", re.M)
ARRAY_DECL_RE = re.compile(
    r"^[ \t]*char\[\][ \t]+(?P<var>\w+)[ \t]*=[ \t]*new[ \t]+char\[\][ \t]*\{(?P<items>" + CHARRUN + r")\}[ \t]*;[ \t]*$",
    re.M | re.S)

# "char[] v = new char[10];"  (filled index by index below it)
IARRAY_DECL_RE = re.compile(
    r"^[ \t]*char\[\][ \t]+(?P<var>\w+)[ \t]*=[ \t]*new[ \t]+char\[[ \t]*(?P<n>\d+)[ \t]*\][ \t]*;[ \t]*$", re.M)

# "v[3] = (char)(180 - 126);"
ISET_RE = re.compile(
    r"^[ \t]*(?P<var>\w+)\[[ \t]*(?P<idx>\d+)[ \t]*\][ \t]*=[ \t]*(?P<arg>[^;\n]+?)[ \t]*;[ \t]*$", re.M)

# Any typed local declaration resets the name: without this a builder declared in
# one method would be reused for a same-named variable in the next method.
RESET_RE = re.compile(
    r"^[ \t]*(?:(?:final|public|private|protected|static)[ \t]+)*"
    r"[A-Za-z_$][\w$\[\]]*[ \t]+(?P<var>[A-Za-z_$]\w*)[ \t]*(?:=[ \t]*[^;]*)?;[ \t]*$", re.M)
USE_RE = re.compile(r"\b(?P<var>\w+)\.toString\(\)|new\s+String\(\s*(?P<arr>\w+)\s*\)")
INLINE_ARRAY_RE = re.compile(r"new\s+String\(\s*new\s+char\[\]\s*\{(" + CHARRUN + r")\}\s*\)", re.S)


def match_call_chain(text: str, start: int):
    """From `.append(` at start walk the whole chain.

    Returns (index just past the terminating ';', [argument texts]) or
    (None, None) when the chain is not a plain statement.
    """
    i = start
    args = []
    n = len(text)
    while True:
        m = re.compile(r"\.append\s*\(").match(text, i)
        if not m:
            break
        i = m.end()
        dp = 1
        begin = i
        while i < n and dp:
            c = text[i]
            if c == '"' or c == "'":
                q = c
                i += 1
                while i < n:
                    if text[i] == "\\":
                        i += 2
                        continue
                    if text[i] == q:
                        break
                    i += 1
            elif c == "(":
                dp += 1
            elif c == ")":
                dp -= 1
            i += 1
        args.append(text[begin:i - 1])
    m = re.compile(r"[ \t]*;[ \t]*(\n|$)").match(text, i)
    if not m:
        return None, None
    return m.end(), args


class Box:
    __slots__ = ("chars", "tainted", "uses", "decl", "appends", "size", "idxmap")

    def __init__(self):
        self.chars = []
        self.tainted = False      # something non constant was appended
        self.uses = 0
        self.decl = None          # (start, end) of the declaration statement
        self.appends = []         # (start, end) of constant append statements
        self.size = None          # indexed char[] arrays: declared length
        self.idxmap = None        # indexed char[] arrays: index -> char

    def literal(self):
        """The folded string, or None when the box is not fully known."""
        if self.tainted:
            return None
        if self.idxmap is not None:
            if self.size is None or len(self.idxmap) != self.size:
                return None
            return "".join(self.idxmap.get(i, "\0") for i in range(self.size))
        return "".join(self.chars) if self.chars else None


STR_LIT_RE = re.compile(r'^"(?:\\.|[^"\\])*"$', re.S)


def unquote(s: str):
    """Decode a Java string literal into its characters."""
    body = s[1:-1]
    out, i = [], 0
    while i < len(body):
        c = body[i]
        if c != "\\":
            out.append(c)
            i += 1
            continue
        i += 1
        e = body[i]
        i += 1
        simple = {"n": "\n", "r": "\r", "t": "\t", "b": "\b", "f": "\f", "\\": "\\", '"': '"', "'": "'"}
        if e in simple:
            out.append(simple[e])
        elif e == "u":
            out.append(chr(int(body[i:i + 4], 16)))
            i += 4
        else:
            out.append(e)
    return "".join(out)


def _char_arg(arg):
    v = eval_expr((arg or "").strip())
    return None if v is None else chr(v & 0xFFFF)


def _feed_arg(box, arg):
    """Append one append() argument to the box; False when it is not constant."""
    a = (arg or "").strip()
    ch = _char_arg(a)
    if ch is not None:
        box.chars.append(ch)
        return True
    if STR_LIT_RE.match(a):
        try:
            box.chars.extend(unquote(a))
            return True
        except Exception:
            pass
    return False


def _split_args(s: str):
    out, buf, dp, db, i = [], [], 0, 0, 0
    while i < len(s):
        c = s[i]
        if c == '"' or c == "'":
            q = c
            buf.append(c)
            i += 1
            while i < len(s):
                buf.append(s[i])
                if s[i] == "\\":
                    buf.append(s[i + 1])
                    i += 2
                    continue
                if s[i] == q:
                    break
                i += 1
            i += 1
            continue
        if c in "([":
            dp += c == "("
            db += c == "["
        elif c in ")]":
            dp -= c == ")"
            db -= c == "]"
        if c == "," and dp == 0 and db == 0:
            out.append("".join(buf))
            buf = []
        else:
            buf.append(c)
        i += 1
    if buf:
        out.append("".join(buf))
    return [a.strip() for a in out]


def _feed(box, args):
    """Append constant chars to box; taint it as soon as anything is not constant."""
    if isinstance(args, str):
        items = []
        rest = args
        while rest.strip():
            m = re.compile(r"\.append\s*\(").match(rest)
            if not m:
                box.tainted = True
                return
            i = m.end()
            dp = 1
            while i < len(rest) and dp:
                c = rest[i]
                if c == '"' or c == "'":
                    q = c
                    i += 1
                    while i < len(rest):
                        if rest[i] == "\\":
                            i += 2
                            continue
                        if rest[i] == q:
                            break
                        i += 1
                elif c == "(":
                    dp += 1
                elif c == ")":
                    dp -= 1
                i += 1
            items.append(rest[m.end():i - 1])
            rest = rest[i:]
    else:
        items = args
    for a in items:
        if not _feed_arg(box, a):
            box.tainted = True
            return


def _still_referenced(text: str, names, spans, replaced, region_end) -> bool:
    """True when a name is used somewhere outside the statements we would delete.

    The search is limited to the region that belongs to this declaration (up to
    the next declaration of the same variable name), so identical obfuscated
    names in different methods do not keep each other alive.
    """
    def covered(p):
        for s, e in spans:
            if s <= p < e:
                return True
        for s, e in replaced:
            if s <= p < e:
                return True
        return False

    start = min(s for s, _ in spans)
    for nm in names:
        for m in re.finditer(r"\b" + re.escape(nm) + r"\b", text):
            if not (start <= m.start() < region_end):
                continue
            if not covered(m.start()):
                return True
    return False


def fold_strings(text: str, stats: dict) -> str:
    """Fold StringBuilder chains and char[] arrays into literals.

    Events are collected in source order and replayed, so every read of a
    builder is resolved against the exact state at that point.  A builder that
    ever receives a non constant value is left completely untouched.
    """
    events = []

    for m in DECL_RE.finditer(text):
        kind = "alias" if m.group("other") else "decl"
        events.append((m.start(), 1, kind, m.group("var"), m.group("other"), m.group("chain"), (m.start(), m.end())))

    for m in PAREN_DECL_RE.finditer(text):
        events.append((m.start(), 1, "decl", m.group("var"), None, m.group("chain"), (m.start(), m.end())))

    for m in APPEND_HEAD_RE.finditer(text):
        end, args = match_call_chain(text, m.start("dot"))
        if args is None:
            continue
        events.append((m.start(), 1, "append", m.group("var"), None, args, (m.start(), end)))

    for m in ARRAY_DECL_RE.finditer(text):
        events.append((m.start(), 1, "array", m.group("var"), None, m.group("items"), (m.start(), m.end())))

    for m in IARRAY_DECL_RE.finditer(text):
        events.append((m.start(), 1, "iarray", m.group("var"), None, int(m.group("n")), (m.start(), m.end())))

    for m in ISET_RE.finditer(text):
        events.append((m.start(), 1, "iset", m.group("var"), None, (int(m.group("idx")), m.group("arg")),
                       (m.start(), m.end())))

    for m in RESET_RE.finditer(text):
        events.append((m.start(), 0, "reset", m.group("var"), None, None, (m.start(), m.end())))

    for m in USE_RE.finditer(text):
        name = m.group("var") or m.group("arr")
        events.append((m.start(), 1, "use", name, None, None, (m.start(), m.end())))

    events.sort(key=lambda e: (e[0], e[1]))

    active = {}
    boxes = []
    box_names = {}
    decl_pos = {}   # variable name -> sorted list of declaration offsets
    edits = []      # (start, end, replacement)

    for pos, _ord, kind, var, other, payload, span in events:
        if kind == "reset":
            active.pop(var, None)
        elif kind == "decl":
            box = Box()
            box.decl = span
            active[var] = box
            boxes.append(box)
            box_names[id(box)] = [var]
            decl_pos.setdefault(var, []).append(span[0])
            _feed(box, payload or "")
        elif kind == "alias":
            box = Box()
            box.decl = span
            active[var] = active[other] = box
            boxes.append(box)
            box_names[id(box)] = [var, other]
            decl_pos.setdefault(var, []).append(span[0])
            decl_pos.setdefault(other, []).append(span[0])
            _feed(box, payload or "")
        elif kind == "array":
            box = Box()
            box.decl = span
            for it in _split_args(payload):
                ch = _char_arg(it)
                if ch is None:
                    box.tainted = True
                    break
                box.chars.append(ch)
            active[var] = box
            boxes.append(box)
            box_names[id(box)] = [var]
            decl_pos.setdefault(var, []).append(span[0])
        elif kind == "iarray":
            box = Box()
            box.decl = span
            box.size = payload
            box.idxmap = {}
            active[var] = box
            boxes.append(box)
            box_names[id(box)] = [var]
            decl_pos.setdefault(var, []).append(span[0])
        elif kind == "iset":
            box = active.get(var)
            if box is None or box.idxmap is None:
                continue
            idx, arg = payload
            ch = _char_arg(arg)
            if ch is None or idx < 0 or (box.size is not None and idx >= box.size) or idx in box.idxmap:
                box.tainted = True
                continue
            box.idxmap[idx] = ch
            box.appends.append(span)
        elif kind == "append":
            box = active.get(var)
            if box is not None:
                before = len(box.chars)
                _feed(box, payload)
                if len(box.chars) > before and not box.tainted:
                    box.appends.append(span)
        elif kind == "use":
            box = active.get(var)
            if box is None:
                continue
            lit = box.literal()
            if lit is None:
                continue
            box.uses += 1
            edits.append((span[0], span[1], esc_str(lit)))

    # drop the bookkeeping of every builder that was fully folded, but only when
    # no reference to its variable name survives the folding
    replaced = [(s, e) for s, e, _ in edits]
    for b in boxes:
        if b.tainted or not b.uses or b.literal() is None:
            continue
        spans = ([b.decl] if b.decl else []) + list(b.appends)
        nm = box_names.get(id(b))
        if nm:
            first = min(s for s, _ in spans)
            limit = len(text)
            for n2 in nm:
                for p in decl_pos.get(n2, ()):
                    if first < p < limit:
                        limit = p
                # any later declaration of the same name starts a new scope
                for m in re.finditer(
                        r"\n[ \t]*(?:final[ \t]+)?[\w<>,\.\[\]]+[ \t]+" + re.escape(n2) + r"[ \t]*=[ \t]*(?!=)",
                        text):
                    if first < m.start() < limit:
                        limit = m.start()
            if _still_referenced(text, nm, spans, replaced, limit):
                continue
        if b.decl:
            edits.append((b.decl[0], b.decl[1], ""))
        for a in b.appends:
            edits.append((a[0], a[1], ""))

    out = text
    for s, e, rep in sorted(edits, key=lambda x: -x[0]):
        if s > len(out):
            continue
        out = out[:s] + rep + out[e:]
        stats["strings"] += 1

    def sub_inline_array(m):
        acc = []
        for it in _split_args(m.group(1)):
            ch = _char_arg(it)
            if ch is None:
                return m.group(0)
            acc.append(ch)
        stats["arrays"] += 1
        return esc_str("".join(acc))

    out = INLINE_ARRAY_RE.sub(sub_inline_array, out)
    out = re.sub(r"\n[ \t]*\n[ \t]*\n+", "\n\n", out)
    return out


# ---------------------------------------------------------------- pass 3: junk

def strip_dead(text: str, stats: dict) -> str:
    """Remove opaque predicate noise: int v = C; if ((v ^ C) == 0) { ... }."""
    pat = re.compile(
        r"\n[ \t]*(?:int|long)\s+(\w+)\s*=\s*(-?\d+)L?;\s*\n"
        r"[ \t]*if\s*\(\(\s*\1\s*\^\s*(-?\d+)L?\s*\)\s*==\s*0\s*\)\s*\{[^{}]*\}", re.S)

    def sub(m):
        if m.group(2) != m.group(3):
            return m.group(0)
        stats["dead"] += 1
        return ""

    prev = None
    while prev != text:
        prev = text
        text = pat.sub(sub, text)
    return text


def deobfuscate(src: str, stats: dict) -> str:
    t = fold_char_casts(src, stats)
    t = strip_dead(t, stats)      # junk first: it otherwise keeps names alive
    prev = None
    for _ in range(4):
        if t == prev:
            break
        prev = t
        t = fold_strings(t, stats)
    return t


def main():
    args = [a for a in sys.argv[1:] if not a.startswith("--")]
    stats_on = "--stats" in sys.argv
    out = None
    if "-o" in args:
        i = args.index("-o")
        out = args[i + 1]
        del args[i:i + 2]
    src = open(args[0], encoding="utf-8").read()
    stats = dict(chars=0, arrays=0, dead=0, strings=0)
    t = deobfuscate(src, stats)
    if out:
        open(out, "w", encoding="utf-8").write(t)
    else:
        sys.stdout.write(t)
    if stats_on:
        sys.stderr.write("folded chars=%d strings=%d inline char arrays=%d dead blocks=%d\n"
                         % (stats["chars"], stats["strings"], stats["arrays"], stats["dead"]))


if __name__ == "__main__":
    main()

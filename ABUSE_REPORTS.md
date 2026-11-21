# Abuse reports and notifications

Drafts, ready to paste. Send these **before** publishing the writeup if you want any chance of the
infrastructure coming down — a public post tells the operator to rotate the contract address and the
payload path.

Handling: never send the sample or the payload unpacked. Use the AES-256 zip
(`stage2/app.pyd.infected.zip`, password `infected`) and lead with hashes so recipients can verify
without touching anything.

---

## 1. Cloudflare (the payload host is Cloudflare-fronted)

Report at <https://abuse.cloudflare.com> — choose "Hosting/Malware". Cloudflare fronts the origin, so
they cannot disable the domain but they can take down the zone/proxy for malware distribution and can
identify the customer behind it.

```
Subject: Malware distribution via Cloudflare-proxied host - thisisafalsepositive.st

The following URL is served through Cloudflare and delivers a malware second stage:

  https://thisisafalsepositive.st/cdn/v2/9f4e7a2c1b8d.png

Observed behaviour (static analysis, full writeup available on request):
- The URL returns a valid PNG image (720x720) that additionally carries a non-standard chunk
  named "hIds". Inflating that chunk yields a ZIP archive containing a complete portable
  Python 3.12 runtime plus a compiled stealer module (AppHost/app.pyd).
- That module is the second stage of a malicious Minecraft mod which steals the player's
  session token and uploads it, along with game logs, to this same host:
      POST https://thisisafalsepositive.st/shard/prefireMc
      POST https://thisisafalsepositive.st/shard/submitMinecraftLog
- The domain is Cloudflare-proxied (A record 185.178.208.191, NS dean/luciane.ns.cloudflare.com).

Evidence / hashes:
  PNG carrier   16,956,757 bytes
                SHA-256 fb8100225454c882831f7b53b0f7dcacc4d41cb9b3abd46255695100d88331f7
  app.pyd        1,852,928 bytes
                SHA-256 4d7ac018e4a2697ca0d4d6864c7f7bc9569bbbead6e32f3506484f490435a410
  main.py              353 bytes
                SHA-256 bc87ec291523785fd9f8b1925e92dbe5aa71af4a9dd631c794fc14efd9e5afb1
  Initial dropper    Grim_Client_1.0.jar, 20,164,948 bytes
                SHA-256 7de29255762284b7236ba7fdd00881220178c192fbbd124befad1d3995d0f79c

Captured on 2026-11-21. Sample available as a password-protected archive on request.
```

## 2. Domain registry / registrar (`.st`)

WHOIS for `thisisafalsepositive.st` (queried 2026-11-21 via whois.nic.st):

```
Registrar:            ST Registry
Creation Date:        2026-06-17 12:54:31
Expiration Date:      2027-06-17
Registrant:           Lena Neumann (STR6A32990691A8D)
Registrant address:   Rua de Mocambique, Nº 47, Sao Tome, Agua Grande
Name servers:         dean.ns.cloudflare.com / luciane.ns.cloudflare.com
```

Registered five months ago, registered directly at the registry, registrant details look throwaway.
Abuse contact for the `.st` registry is on <https://www.nic.st/> — send the same evidence block as above
plus this ask: **suspend the domain and disclose the registrant payment/contact details to law
enforcement.** Note that the C2 value itself lives in a smart contract, so the actor can repoint the
malware to a new domain by one transaction; suspending the domain only closes today's endpoint.

## 3. The impersonated addon author (do this regardless of takedown)

The jar's LabyMod manifests are copied verbatim from the legitimate open-source addon **"Full Bright"
by Lennart Löschel (`L3nnart_`)** — real name, real UUIDs (`bf6c3f3c-0c70-46b6-8e0d-16601f6e2850`,
`40a4991b-2d73-46d1-9dba-f38b88997163`), real icon URL on `dl.lennartloesche.de` — with only
`mainClass` replaced by the malware's bootstrap classes. He is being impersonated and his name is what
a victim sees.

```
Subject: Your "Full Bright" addon identity is being used to distribute malware

Someone shipped a malicious jar that reuses your LabyMod addon manifests verbatim - same addon
name, author string, UUIDs and icon URL - with only the mainClass swapped to their own code. The
file is Grim_Client_1.0.jar (SHA-256 7de29255...f79c), which steals the Minecraft session token
and downloads a Python stealer.

Two practical points for you: blocklists that key on "Full Bright" or those UUIDs will hit your
legitimate addon too, and users who find writeups of this may confuse the two. Worth stating
publicly that your addon's real jar differs, and worth checking whether your distribution channel
has copies of the fake one.

Analysis (deobfuscated source, IOCs): <link to your repo>
```

## 4. Optional / lower value

* **Polygon contract `0x9c0a507300fd902787bb193d80fca5ce6e1bff9a`** — no takedown path, but worth
  submitting to blockchain-threat feeds so the address gets blocklisted by wallet/security tooling.
  Every read of it is public, so monitoring that contract for changes is a cheap early-warning signal
  for new C2 domains.
* **The RPC providers listed in the malware** (`drpc.org`, `publicnode.com`, `1rpc.io`, `zan.top`,
  `quiknode.pro`) are innocent third parties — do **not** report them.
* **Wherever the jar was distributed** (download site, Discord, YouTube comment chain): fill in from
  your own source and send the same evidence block. That is the only report that removes the actual
  delivery vector.

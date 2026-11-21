# Sample inventory

> Documentation only — a reverse-engineering record of one malicious jar.

## The jar

| Field | Value |
|---|---|
| File | `Grim_Client_1.0.jar` |
| Size | 20,164,948 bytes |
| SHA-256 | `7de29255762284b7236ba7fdd00881220178c192fbbd124befad1d3995d0f79c` |
| SHA-1 | `c240141fe98de5a34ed6b558d9dc17920ac70880` |
| MD5 | `3cdcc8da0d096d24e71f9d0a76f74409` |
| Format | zip (Java SE jar), 35 entries, **unsigned** (no `.SF`/`.RSA`/`.DSA`) |

## Manifest — the build environment leaks through

```
Manifest-Version: 1.0
Main-Class: net.fabric.reo_qrox_ebgg_jhss
Fabric-Jar-Type: classes
Fabric-Loom-Mixin-Remap-Type: static
Fabric-Gradle-Version: 9.7.1
Fabric-Loom-Version: 1.18.2
Fabric-Mixin-Compile-Extensions-Version: 0.6.0
Fabric-Minecraft-Version: 1.21
Fabric-Tiny-Remapper-Version: 0.14.1
Fabric-Loader-Version: 0.19.5
Fabric-Mixin-Version: 0.17.4+mixin.0.8.7
Fabric-Mapping-Namespace: intermediary
```

So this was produced with a genuine Fabric Mod Development Kit (Loom 1.18.2, Gradle 9.7.1) targeting
**Minecraft 1.21** with intermediary mappings — i.e. the author is not hand-rolling a fake jar, they
built a real mod and then wrapped/obfuscated it. `Main-Class` being set on top of that is what makes
the same file work as a standalone executable.

## Entry points declared in the jar

| Mechanism | Declaration | Handler |
|---|---|---|
| `java -jar` | `Main-Class: net.fabric.reo_qrox_ebgg_jhss` | re-launches itself detached, env tag `DoubleClick` |
| Fabric mod load | `fabric.mod.json` → `"id": "dd"`, entrypoints `["net.java.h", "net.fabric.hkonjkz"]` | DoomsDay bootstrap + session stealer (env `Fabric`) |
| Forge mod load | `META-INF/mods.toml`, `mcmod.info`, `pack.mcmeta` | `net/java/i.class`, `mod_d.class` → DoomsDay bootstrap |
| LabyMod addon | `addon3.json`, `addon4.json` — "Full Bright" by `L3nnart_` | `net/java/r.class`, `net/java/s.class` → DoomsDay bootstrap |

## Zip listing (note the two timestamp groups)

```
    Length  Date        Name
       458  11-14-2026  META-INF/MANIFEST.MF
     54467  11-14-2026  net/fabric/btcValue.class
       711  11-14-2026  net/fabric/tmpSikxuafu$uvt_gjana.class      (trust-all X509TrustManager)
      1383  11-14-2026  net/fabric/tmpSikxuafu$panopkr_F8Tu.class   (response holder)
       949  11-14-2026  net/fabric/tmpSikxuafu$bufTxjwh.class
     23014  11-14-2026  net/fabric/tmpSikxuafu.class                (raw HTTPS client + DoH)
     12179  11-14-2026  net/fabric/hkonjkz.class                    (Fabric ModInitializer)
      7665  11-14-2026  net/fabric/reo_qrox_ebgg_jhss.class         (Main-Class)
     25479  11-14-2026  net/fabric/znIHc.class                      (session reflection helper)
      1055  02-01-2016  net/java/f.class
     15135  02-01-2016  net/java/g.class                            (native loader / patcher)
       475  02-01-2016  net/java/h.class
       539  02-01-2016  net/java/i.class
       602  02-01-2016  net/java/r.class
       681  02-01-2016  net/java/s.class
       513  02-01-2016  net/java/t.class
      2108  02-01-2016  net/java/n.class                            (custom ClassLoader + XOR keys)
      2058  02-01-2016  net/java/y.class
      1984  02-01-2016  net/java/k.class                            (native lib container reader)
     15809  02-01-2016  net/java/l.class                            (range coder + native l.n + updater)
       613  02-01-2016  mod_d.class
     17474  02-01-2016  net/java/b                                (8 native libraries, encoded)
    398089  02-01-2016  net/java/c                                (per-arch patch blocks, encoded)
      7103  02-01-2016  l.png                                       (mod icon, clean)
       151  02-01-2016  fabric.mod.json
       125  02-01-2016  META-INF/mods.toml
        45  02-01-2016  pack.mcmeta
        42  02-01-2016  mcmod.info
       275  02-01-2016  addon3.json
       372  02-01-2016  addon4.json
    214419  02-01-2016  net/java/a                                (hidden class map, encoded)
     14152  02-01-2016  net/java/e
   2278288  02-01-2016  64FV7OQUIMC8I                               (encrypted native payload)
  17039360  11-14-2026  assets/mnqmuqdh.cache                       (17 MB high-entropy blob)
```

The 2016 timestamps are not real: they are stamped by the DoomsDay packer, which writes its own
entries with a fixed date. The 11-14-2026 entries are what the author's build actually added —
including the 17 MB `assets/mnqmuqdh.cache`.

## What is in the two big blobs

* **`64FV7OQUIMC8I` (2.28 MB)** — handed to the native function `l.n(Object)` after the DoomsDay
  native library is loaded from `/net/java/b`. On Windows, 8 bytes at offset
  `len - 4096 + 48` are patched first with a per-architecture value taken from `/net/java/c`, which
  means the blob has a fixed 4 KB trailer holding pointers/hook addresses. Not decrypted: the key
  and the algorithm live in the native code.
* **`assets/mnqmuqdh.cache` (17 MB)** — entropy 7.997 bits/byte, no repeating XOR key (periods
  1–64 tested), no duplicate 16-byte blocks, no zip/class magic anywhere in the first megabytes.
  No reference to this path appears in any deobfuscated class. Two plausible readings: an encrypted
  pre-staged copy of the "portable runtime" that a newer loader version reads, or deliberate filler
  to push the file size/entropy up. Flagged as unresolved rather than guessed.

## Disguise metadata (verbatim)

```jsonc
// fabric.mod.json  — note: no name/description/author at all, just the id
{ "schemaVersion": 1, "id": "dd", "version": "1.0.0",
  "entrypoints": { "main": ["net.java.h", "net.fabric.hkonjkz"] } }
```

```toml
# META-INF/mods.toml — Forge, everything left blank except the id
modLoader="javafml"
loaderVersion="[25,)"
license=" "
[[mods]]
modId="dd"
version="1.0"
displayName="dd"
description='''
'''
```

```jsonc
// addon3.json (old LabyMod addon API)
{"uuid":"bf6c3f3c-0c70-46b6-8e0d-16601f6e2850","name":"Full Bright","mainClass":"net.java.r",
 "description":"Allows you to change the brightness of the game to full bright","version":1,
 "author":"L3nnart_","category":4,"icon":"https://dl.lennartloesche.de/fullbright/logo.png"}

// addon4.json (new LabyMod addon API)
{"compatibleMinecraftVersions":"*","namespace":"fullbright","displayName":"Full Bright",
 "uuid":"40a4991b-2d73-46d1-9dba-f38b88997163","name":"Full Bright","mainClass":"net.java.s", …}
```

The two addon manifests are **copied verbatim from the legitimate open-source LabyMod addon
"Full Bright" by Lennart Löschel** (`L3nnart_`), including its real UUIDs and its real icon URL on
`dl.lennartloesche.de`. Only `mainClass` was swapped to the malware's bootstrap classes. That is
identity cloning, not just a fake name: blocklisting "Full Bright" or those UUIDs would also hit the
genuine addon, so hunt on the jar hash, the class names and the network indicators instead.


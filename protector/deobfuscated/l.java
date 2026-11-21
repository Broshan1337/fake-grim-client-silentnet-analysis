package net.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.CRC32;

public class l {
   private static String d;
   private static final int a;
   private static final int b;
   public static Map a = System.getProperties();
   public static String a;
   public static String b;
   public static String c;
   public static long a;
   public static long b;
   public static long c;
   private static volatile boolean a;
   private byte[] a;
   private int c;
   private int d = 0;
   private int e;
   private byte[] b;
   private int f;
   private int g;
   private int h;
   private byte[] c;
   private int i;
   private int j;
   private int k;
   private int l;
   private short[][] a;
   private short[] a = new short[192];
   private short[] b = new short[12];
   private short[] c = new short[12];
   private short[] d = new short[12];
   private short[] e = new short[12];
   private short[] f = new short[192];
   private short[][] b = new short[4][];
   private short[] g = new short[114];
   private short[] h = new short[16];
   private short[] i = new short[2];
   private short[][] c = new short[16][];
   private short[][] d = new short[16][];
   private short[] j = new short[256];
   private int m = 0;
   private short[] k = new short[2];
   private short[][] e = new short[16][];
   private short[][] f = new short[16][];
   private short[] l = new short[256];
   private int n = 0;
   private int o = -1;
   private int p = -1;
   private int q;

   static {
      StringBuilder var2;

      d = "UTF-8";
      byte var0 = -1;
      String var1 = null;
      String var7 = null;
      String var3 = null;
      Iterator var5 = a.entrySet().iterator();

      while (var5.hasNext()) {
         Entry var4;
         int var6 = (var4 = (Entry)var5.next()).getKey().hashCode();
         Object var14 = var4.getValue();
         if (var6 == -1228098475) {
            var1 = var14.toString().toLowerCase();
         } else if (var6 == 1174476494) {
            var7 = var14.toString().toLowerCase();
         } else if (var6 == -1228469728) {
            var3 = var14.toString().toLowerCase();
         }
      }

      label66: {
         if (var7 != null) {

            if (var7.contains("android")) {
               var0 = 3;
               break label66;
            }
         }

         if (var1 != null) {

            if (var1.contains("win")) {
               var0 = 0;
            } else {

               if (var1.contains("linux")) {
                  var0 = 1;
               } else {
                  label59: {

                     if (!var1.contains("mac")) {

                        if (!var1.contains("osx")) {

                           if (!var1.contains("os x")) {
                              var0 = -1;
                              break label59;
                           }
                        }
                     }

                     var0 = 2;
                  }
               }
            }
         }
      }

      b = var0;
      int var15;
      if ((var15 = var3.hashCode()) == 93084186 || var15 == -1221096139) {
         a = 2;
      } else if (var15 == -806050265 || var15 == 92926582) {
         a = 0;
      } else if (var15 != 117110 && var15 != -806050360 && var15 != 3178856 && var15 != 3179817 && var15 != 3180778 && var15 != 3181739) {
         a = -1;
      } else {
         a = 1;
      }
   }

   public static native Object n(Object var0);

   public static int a(Object var0) {
      Object[] var5;
      byte[] var1 = (byte[])(var5 = var0)[0];
      int var2 = (Integer)var5[1];
      if ((var0 = (byte[])var5[2]).length == 0) {
         return -1;
      }

      var2 -= var0.length;

      label27:
      for (int var3 = 0; var3 <= var2; var3++) {
         for (int var4 = 0; var4 < var0.length; var4++) {
            if (var0[var4] != var1[var3 + var4]) {
               continue label27;
            }
         }

         return var3;
      }

      return -1;
   }

   public static int a(Object var0, Object var1) {
      var0 = var0;
      var1 = var1;
      int var2 = var0.length;
      if (var1.length == 0) {
         return -1;
      }

      var2 -= var1.length;

      label27:
      for (int var3 = 0; var3 <= var2; var3++) {
         for (int var4 = 0; var4 < var1.length; var4++) {
            if (var1[var4] != var0[var3 + var4]) {
               continue label27;
            }
         }

         return var3;
      }

      return -1;
   }

   private static boolean a(char var0) {
      if (var0 >= '0' && var0 <= '9') {
         return true;
      } else {
         return var0 >= 'A' && var0 <= 'F' ? true : var0 >= 'a' && var0 <= 'f';
      }
   }

   private static byte[] a(byte[] var0, byte[] var1) {
      byte[] var2 = null;
      int var3;
      if ((var3 = a(var0, var1)) >= 0) {
         int var4 = var0.length;

         for (int var5 = var3; var5 < var0.length; var5++) {
            if (var0[var5] == 32) {
               var4 = var5;
               break;
            }
         }

         if (var4 > 0) {
            byte[] var9;
            var1 = new byte[(var9 = var2 = a(var0, var3 + var1.length, var4)).length];
            var3 = 0;

            for (int var14 = 0; var14 < var9.length; var14++) {
               char var16;
               if ((var16 = (char)var9[var14]) == '\\') {
                  if (var14 + 3 >= var9.length) {
                     break;
                  }

                  if (var9[var14 + 1] == 120 && a((char)var9[var14 + 2]) && a((char)var9[var14 + 3])) {
                     int var10001 = var3++;
                     byte var10002 = var9[var14 + 2];
                     byte var6 = var9[var14 + 3];
                     byte var17 = var10002;
                     char var7 = 0;
                     char var8 = 0;
                     if (var17 >= 48 && var17 <= 57) {
                        var7 = (char)(var17 - 48);
                     }

                     if (var17 >= 65 && var17 <= 70) {
                        var7 = (char)(var17 - 65 + 10);
                     }

                     if (var17 >= 97 && var17 <= 102) {
                        var7 = (char)(var17 - 97 + 10);
                     }

                     if (var6 >= 48 && var6 <= 57) {
                        var8 = (char)(var6 - 48);
                     }

                     if (var6 >= 65 && var6 <= 70) {
                        var8 = (char)(var6 - 65 + 10);
                     }

                     if (var6 >= 97 && var6 <= 102) {
                        var8 = (char)(var6 - 97 + 10);
                     }

                     var1[var10001] = (byte)(var7 << 4 | var8);
                  }

                  var14 += 3;
               } else {
                  var1[var3++] = (byte)var16;
               }
            }

            byte[] var15 = a(var1, var3);

            for (int var18 = 0; var18 < var1.length; var18++) {
               var1[var18] = 0;
            }

            byte[] var19 = var15;

            for (int var10 = 0; var10 < var2.length; var10++) {
               var2[var10] = 0;
            }

            var2 = var19;
         }
      }

      return var2;
   }

   public static void a(Object var0) {
      if (!a) {
         a = true;
         Object[] var11;
         byte[] var1 = (byte[])(var11 = (Object[])var0)[0];
         Object var2 = var11[1];
         int var3 = (Integer)var11[2];
         ClassLoader var4 = (ClassLoader)var11[3];
         Object var5 = var11[4];
         if ((var0 = (String)var11[5]) != null && var0.length() == 0) {
            var0 = null;
         }

         Object var6 = null;
         byte[] var7 = null;
         if (var1 != null) {
            var7 = var1;
            byte[] var8 = new byte[]{45, 45, 100, 111, 111, 109, 115, 100, 97, 121, 118, 101, 114, 115, 105, 111, 110, 61};
            var7 = a(var7, var8);

            for (int var9 = 0; var9 < 18; var9++) {
               var8[var9] = 0;
            }

            var6 = var7;
            var7 = var1;
            var8 = new byte[]{45, 45, 99, 111, 110, 102, 105, 103, 115, 116, 111, 114, 97, 103, 101, 61};
            var7 = a(var7, var8);

            for (int var22 = 0; var22 < 16; var22++) {
               var8[var22] = 0;
            }
         }

         if (d() && var7 == null && (var3 == 6 || var3 == 5)) {
            var7 = new byte[]{36, 100, 101, 102, 97, 117, 108, 116, 36};
            if (var1 == null) {
               var1 = new byte[]{45, 45, 99, 111, 110, 102, 105, 103, 115, 116, 111, 114, 97, 103, 101, 61, 36, 100, 101, 102, 97, 117, 108, 116, 36};
            }
         }

         byte[] var13;
         if ((var13 = (byte[])a(new Object[]{var0, var3, var6, var7})) != null) {
            try {
               if (!net.java.g.a()) {
                  throw new RuntimeException();
               }

               ((Object[])(var6 = new Object[8]))[0] = new long[1];
               ((Object[])var6)[1] = new long[]{var3};
               ((Object[])var6)[2] = var1;
               ((Object[])var6)[3] = var2;
               ((Object[])var6)[4] = var13;
               ((Object[])var6)[5] = null;
               ((Object[])var6)[6] = var4;
               ((Object[])var6)[7] = var5;
               n(var6);

               for (int var14 = 0; var14 < var13.length; var14++) {
                  var13[var14] = 0;
               }
            } catch (Throwable var10) {
               PrintStream var23 = System.out;
               StringBuilder var20;
               (var20 = new StringBuilder()).append('E');
               var20.append('r');
               var20.append('r');
               var20.append('o');
               var20.append('r');
               var20.append(' ');
               var20.append('l');
               var20.append('o');
               var20.append('a');
               var20.append('d');
               var20.append(' ');
               var20.append('D');
               var20.append('o');
               var20.append('o');
               var20.append('m');
               var20.append('s');
               var20.append('D');
               var20.append('a');
               var20.append('y');
               var23.println("Error load DoomsDay");
               var10.printStackTrace();
            }
         } else {
            PrintStream var10000 = System.out;
            StringBuilder var19;

            var10000.println("Error load DoomsDay - update not found");
         }
      }
   }

   private static Object a(Object var0) {
      Object[] var10;
      String var1 = (String)(var10 = var0)[0];
      int var2 = (Integer)var10[1];
      byte[] var3 = (byte[])var10[2];
      var0 = (byte[])var10[3];
      if (var1 != null && var3 == null) {
         try {
            byte[] var4;
            if ((var4 = a(var1, null)) != null) {
               return var4;
            }
         } catch (Throwable var9) {
            PrintStream var10000 = System.out;
            StringBuilder var6;

            var10000.println("Stored 1");
            var9.printStackTrace();
         }
      }

      byte[] var14;
      if (Arrays.equals(var14 = new byte[]{36, 108, 97, 116, 101, 115, 116, 36}, var3)) {
         var3 = null;
      }

      for (int var5 = 0; var5 < 8; var5++) {
         var14[var5] = 0;
      }

      byte[] var15;
      if ((c() && g() || d()) && (var2 == 4 || var2 == 12)) {
         if ((var15 = (byte[])a(a(var0), var3)) == null) {
            var15 = (byte[])b(var3);
         }
      } else if ((var15 = (byte[])b(var3)) == null) {
         var15 = (byte[])a(a(var0), var3);
      }

      if (var15 == null && var1 != null) {
         try {
            if ((var0 = a(var1, null)) != null) {
               var15 = var0;
            }
         } catch (Throwable var8) {
            PrintStream var18 = System.out;
            StringBuilder var16;

            var18.println("Stored 2");
            var8.printStackTrace();
         }
      }

      if (var2 == 1 || var2 == 12) {
         try {
            if (var1 != null && (var0 = a(var1, null)) != null && var15 != null && a(var0) > a(var15)) {
               var15 = var0;
            }
         } catch (Throwable var7) {
            PrintStream var19 = System.out;
            StringBuilder var17;
            (var17 = new StringBuilder()).append('S');
            var17.append('t');
            var17.append('o');
            var17.append('r');
            var17.append('e');
            var17.append('d');
            var17.append(' ');
            var17.append('3');
            var19.println("Stored 3");
            var7.printStackTrace();
         }
      }

      return var15;
   }

   private static String a(Object var0) {
      String var1 = null;
      if (var0 != null && var0 instanceof byte[]) {
         byte[] var2;
         if (!Arrays.equals(var2 = new byte[]{36, 100, 101, 102, 97, 117, 108, 116, 36}, (byte[])var0)) {
            try {
               var1 = new String((byte[])var0, d);
            } catch (Throwable var3) {
            }
         }

         for (int var4 = 0; var4 < 9; var4++) {
            var2[var4] = 0;
         }
      }

      if (var1 == null) {
         StringBuilder var5;
         (var5 = new StringBuilder()).append('d');
         var5.append('o');
         var5.append('o');
         var5.append('m');
         var5.append('s');
         var5.append('d');
         var5.append('a');
         var5.append('y');
         var1 = new File("doomsday").getAbsolutePath();
      }

      return var1;
   }

   private static Object b(Object var0) {
      byte[] var1 = new byte[]{-91, 22, -60, 0};
      byte[] var2 = new byte[]{42, 3, -80, -64, 0, 2, 0, -48, 0, 0, 0, 0, 20, 44, -32, 1};
      byte[] var17;
      if (var0 != null) {
         byte[] var3 = new byte[0];
         if (var0 instanceof byte[]) {
            var3 = (byte[])((byte[])var0).clone();
         } else if (var0 instanceof String) {
            try {
               var3 = ((String)var0).getBytes(d);
            } catch (Throwable var14) {
            }
         }

         (var17 = new byte[8 + var3.length])[0] = 22;
         var17[1] = 3;
         var17[2] = 100;
         var17[3] = (byte)var3.length;

         for (int var4 = 0; var4 < var3.length; var4++) {
            var17[var4 + 8] = var3[var4];
         }
      } else {
         var17 = new byte[]{22, 3, 100, 0, 0, 0, 0, 0};
      }

      try {
         SocketChannel var18 = SocketChannel.open();

         try {
            var18.connect(new InetSocketAddress(InetAddress.getByAddress(var1), 443));
         } catch (Throwable var13) {
            var18.connect(new InetSocketAddress(InetAddress.getByAddress(var2), 443));
         }

         ByteBuffer var22;
         (var22 = ByteBuffer.allocateDirect(var17.length)).put(var17);
         ((Buffer)var22).position(0);
         var18.write(var22);
         ((Buffer)var22).position(0);
         var22.put(new byte[var22.limit()]);
         var22 = ByteBuffer.allocateDirect(4);
         var18.read(var22);
         int var5;
         if ((var5 = 0 | (var22.get(0) & 255) << 24 | (var22.get(1) & 255) << 16 | (var22.get(2) & 255) << 8 | var22.get(3) & 255) > 0) {
            ((Buffer)var22).position(0);
            var22.put(new byte[var22.limit()]);
            byte[] var24 = new byte[var5];
            ByteBuffer var6 = ByteBuffer.allocateDirect(16384);
            int var7 = 0;

            int var8;
            while (var7 < var5 && (var8 = var18.read(var6)) >= 0) {
               ((Buffer)var6).position(0);
               var6.get(var24, var7, var8);
               ((Buffer)var6).clear();
               var7 += var8;
            }

            var18.close();
            ((Buffer)var6).position(0);
            var6.put(new byte[var6.limit()]);
            return var24;
         }

         var18.close();
      } catch (Exception var15) {
         var15.printStackTrace();
         return null;
      } finally {
         for (int var19 = 0; var19 < var17.length; var19++) {
            var17[var19] = 0;
         }

         for (int var20 = 0; var20 < 4; var20++) {
            var1[var20] = 0;
         }

         for (int var21 = 0; var21 < 16; var21++) {
            var2[var21] = 0;
         }
      }

      return null;
   }

   private static byte[] a(String var0, String var1) {
      try {
         InputStream var2;
         if ((var2 = l.class.getResourceAsStream(var0)) == null) {
            return var1 != null ? (byte[])b(var1) : null;
         }

         byte[] var6 = new byte[16384];
         ByteArrayOutputStream var5 = new ByteArrayOutputStream(var2.available());

         int var3;
         while ((var3 = var2.read(var6)) != -1) {
            var5.write(var6, 0, var3);
         }

         var2.close();
         return var5.toByteArray();
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   private static Object a(Object var0, Object var1) {
      File var11;
      if (var0 instanceof String) {
         var11 = new File((String)var0);
      } else {
         var11 = (File)var0;
      }

      byte[] var2 = null;
      byte[] var3 = null;
      ByteArrayOutputStream var4 = null;
      int var5 = 0;

      try {
         File[] var12;
         if ((var12 = var11.listFiles()) == null) {
            return null;
         }

         for (int var6 = 0; var6 < var12.length; var6++) {
            String var7;
            int var13;
            if ((var7 = var12[var6].getName()).length() > 8 && var7.length() < 20 && (var13 = a(var7)) > 0) {
               if (var4 == null) {
                  var4 = new ByteArrayOutputStream(1310720);
               }

               if (var2 == null) {
                  var2 = new byte[65536];
               }

               RandomAccessFile var14;
               label76: {
                  var4.reset();
                  File var10002 = var12[var6];
                  StringBuilder var8;

                  var14 = new RandomAccessFile(var10002, "r");
                  if (var1 != null) {
                     byte[] var9 = new byte[64];
                     var14.readFully(var9);
                     if (!Arrays.equals(c(var9), (byte[])var1)) {
                        break label76;
                     }

                     var4.write(var9);
                  }

                  if (var13 > var5) {
                     int var15;
                     while ((var15 = var14.read(var2)) >= 0) {
                        var4.write(var2, 0, var15);
                     }

                     var3 = var4.toByteArray();
                     var5 = var13;
                  }
               }

               var14.close();
            }
         }
      } catch (Throwable var10) {
         var10.printStackTrace();
      }

      return var3;
   }

   public static void b(Object var0) {
      try {
         Object[] var1;
         var0 = (File)(var1 = (byte[])((Object[])var0))[0];
         var1 = (byte[])var1[1];
         StringBuilder var4;

         RandomAccessFile var5;
         (var5 = new RandomAccessFile(var0, "rw")).write(var1);
         var5.close();
      } catch (Throwable var2) {
      }
   }

   private static byte[] c(byte[] var0) {
      var0 = (byte[])var0.clone();
      int var1 = 0;

      for (int var2 = 6; var2 < 64; var2++) {
         var0[var2] = (byte)(var0[var2] ^ 71);
         if (var0[var2] == 0 && var1 == 0) {
            var1 = var2;
         }
      }

      if (var1 == 0) {
         var1 = 64;
      }

      return a(var0, 6, var1);
   }

   private static int a(byte[] var0) {
      return 0 + ((var0[0] & 0xFF) << 24) + ((var0[1] & 0xFF) << 16) + ((var0[2] & 0xFF) << 8) + (var0[3] & 0xFF);
   }

   private static int a(String var0) {
      try {
         if (var0 == null) {
            StringBuilder var20;

            throw new NumberFormatException("null");
         }

         int var1;
         if ((var1 = var0.length()) > 0) {
            if (var0.charAt(0) == '-') {
               StringBuilder var19;
               (var19 = new StringBuilder()).append('I');
               var19.append('l');
               var19.append('l');
               var19.append('e');
               var19.append('g');
               var19.append('a');
               var19.append('l');
               var19.append(' ');
               var19.append('l');
               var19.append('e');
               var19.append('a');
               var19.append('d');
               var19.append('i');
               var19.append('n');
               var19.append('g');
               var19.append(' ');
               var19.append('m');
               var19.append('i');
               var19.append('n');
               var19.append('u');
               var19.append('s');
               var19.append(' ');
               var19.append('s');
               var19.append('i');
               var19.append('g');
               var19.append('n');
               var19.append(' ');
               var19.append('o');
               var19.append('n');
               var19.append(' ');
               var19.append('u');
               var19.append('n');
               var19.append('s');
               var19.append('i');
               var19.append('g');
               var19.append('n');
               var19.append('e');
               var19.append('d');
               var19.append(' ');
               var19.append('s');
               var19.append('t');
               var19.append('r');
               var19.append('i');
               var19.append('n');
               var19.append('g');
               var19.append(' ');
               var19.append('%');
               var19.append('s');
               var19.append('.');
               throw new NumberFormatException(String.format("Illegal leading minus sign on unsigned string %s.", var0));
            }

            long var10000;
            if (var1 <= 12) {
               var10000 = Long.parseLong(var0, 32);
            } else {
               long var3 = Long.parseLong(var0.substring(0, var1 - 1), 32);
               if ((var1 = Character.digit(var0.charAt(var1 - 1), 32)) < 0) {
                  StringBuilder var18;

                  throw new NumberFormatException("Bad digit at end of " + var0);
               }

               long var5;
               var10000 = (var5 = (var3 << 5) + var1) + Long.MIN_VALUE;
               long var9 = var3 + Long.MIN_VALUE;
               long var7 = var10000;
               if ((var10000 < var9 ? -1 : (var7 == var9 ? 0 : 1)) < 0) {
                  StringBuilder var17;

                  throw new NumberFormatException(String.format("String value %s exceeds range of unsigned long.", var0));
               }

               var10000 = var5;
            }

            long var15 = var10000 ^ 4773789072989332985L;
            byte[] var13;
            (var13 = new byte[8])[0] = (byte)(var15 >>> 56 & 255L);
            var13[1] = (byte)(var15 >>> 48 & 255L);
            var13[2] = (byte)(var15 >>> 40 & 255L);
            var13[3] = (byte)(var15 >>> 32 & 255L);
            var13[4] = (byte)(var15 >>> 24 & 255L);
            var13[5] = (byte)(var15 >>> 16 & 255L);
            var13[6] = (byte)(var15 >>> 8 & 255L);
            var13[7] = (byte)(var15 & 255L);
            short var16 = (short)((short)(0 + ((var13[6] & 255) << 8)) + (var13[7] & 255));
            CRC32 var2;
            (var2 = new CRC32()).update(var13, 0, 6);
            if ((short)var2.getValue() != var16) {
               return -2;
            }

            return 0 + ((var13[1] & 0xFF) << 24) + ((var13[2] & 0xFF) << 16) + ((var13[3] & 0xFF) << 8) + (var13[4] & 0xFF);
         }

         throw new IllegalArgumentException();
      } catch (NumberFormatException var11) {
      } catch (Exception var12) {
         var12.printStackTrace();
      }

      return -1;
   }

   public static final byte[] a(byte[] var0) {
      byte[] var3 = new byte[var0.length];
      int var2 = var0.length;

      while (var2 - 1 > 0 && var0[var2 - 1] == 61) {
         var2--;
      }

      if (var2 - 1 == 0) {
         return null;
      }

      byte[] var4 = new byte[var2 * 3 / 4];

      for (int var1 = 0; var1 < var2; var1++) {
         if (var0[var1] == 43) {
            var3[var1] = 62;
         } else if (var0[var1] == 47) {
            var3[var1] = 63;
         } else if (var0[var1] < 58) {
            var3[var1] = (byte)(var0[var1] + 52 - 48);
         } else if (var0[var1] < 91) {
            var3[var1] = (byte)(var0[var1] - 65);
         } else if (var0[var1] < 123) {
            var3[var1] = (byte)(var0[var1] + 26 - 97);
         }
      }

      byte var9 = 0;

      for (var5 = 0; var9 < var2 && var5 < var4.length / 3 * 3; var9 += 4) {
         var4[var5++] = (byte)(var3[var9] << 2 & 252 | var3[var9 + 1] >>> 4 & 3);
         var4[var5++] = (byte)(var3[var9 + 1] << 4 & 240 | var3[var9 + 2] >>> 2 & 15);
         var4[var5++] = (byte)(var3[var9 + 2] << 6 & 192 | var3[var9 + 3] & 63);
      }

      if (var9 < var2) {
         if (var9 < var2 - 2) {
            var4[var5++] = (byte)(var3[var9] << 2 & 252 | var3[var9 + 1] >>> 4 & 3);
            var4[var5] = (byte)(var3[var9 + 1] << 4 & 240 | var3[var9 + 2] >>> 2 & 15);
         } else if (var9 < var2 - 1) {
            var4[var5] = (byte)(var3[var9] << 2 & 252 | var3[var9 + 1] >>> 4 & 3);
         }
      }

      return var4;
   }

   public static boolean a() {
      return b == 0;
   }

   public static boolean b() {
      return b == 2;
   }

   public static boolean c() {
      return b == 1;
   }

   public static boolean d() {
      return b == 3;
   }

   public static boolean e() {
      return a == 1;
   }

   public static boolean f() {
      return a == 0;
   }

   public static boolean g() {
      return a == 2;
   }

   private void a() {
      this.b();
      this.b = null;
   }

   private void b() {
      int var1;
      if ((var1 = this.c - this.e) != 0) {
         for (int var2 = 0; var2 < var1; var2++) {
            this.b[var2 + this.f] = this.a[this.e + var2];
         }

         this.f += var1;
         if (this.c >= this.d) {
            this.c = 0;
         }

         this.e = this.c;
      }
   }

   private byte a(int var1) {
      if ((var1 = this.c - var1 - 1) < 0) {
         var1 += this.d;
      }

      return this.a[var1];
   }

   private int a() {
      return this.c[this.i++] & 0xFF;
   }

   private int a(Object var1, int var2) {
      short[] var5;
      short var3 = (var5 = (short[])var1)[var2];
      int var4 = (this.g >>> 11) * var3;
      if ((this.h ^ Integer.MIN_VALUE) < (var4 ^ Integer.MIN_VALUE)) {
         this.g = var4;
         var5[var2] = (short)(var3 + (2048 - var3 >>> 5));
         if ((this.g & 0xFF000000) == 0) {
            this.h = this.h << 8 | this.a();
            this.g <<= 8;
         }

         return 0;
      } else {
         this.g -= var4;
         this.h -= var4;
         var5[var2] = (short)(var3 - (var3 >>> 5));
         if ((this.g & 0xFF000000) == 0) {
            this.h = this.h << 8 | this.a();
            this.g <<= 8;
         }

         return 1;
      }
   }

   private static void c(Object var0) {
      var0 = var0;

      for (int var1 = 0; var1 < var0.length; var1++) {
         var0[var1] = 1024;
      }
   }

   public l() {
      for (int var1 = 0; var1 < 4; var1++) {
         this.b[var1] = new short[64];
      }
   }

   private int b(Object var1) {
      short[] var5 = (short[])var1;
      int var2 = 31 - a(var5.length);
      int var3 = 1;

      for (int var4 = var2; var4 != 0; var4--) {
         var3 = (var3 << 1) + this.a(var5, var3);
      }

      return var3 - (1 << var2);
   }

   private static int a(int var0) {
      if (var0 == 0) {
         return 32;
      }

      byte var1 = 1;
      if (var0 >>> 16 == 0) {
         var1 += 16;
         var0 <<= 16;
      }

      if (var0 >>> 24 == 0) {
         var1 += 8;
         var0 <<= 8;
      }

      if (var0 >>> 28 == 0) {
         var1 += 4;
         var0 <<= 4;
      }

      if (var0 >>> 30 == 0) {
         var1 += 2;
         var0 <<= 2;
      }

      return var1 - (var0 >>> 31);
   }

   private boolean a(byte[] var1, byte[] var2) {
      long var3 = var2.length;
      short[] var9 = var1;
      l var12 = this;
      this.c = (byte[])var9;
      var12.i = 13;
      var9 = var2;
      var12 = this;
      this.a();
      var12.b = var9;
      var12 = this;
      l var29 = this;
      this.e = 0;
      var29.c = 0;
      c(var12.a);
      c(var12.f);
      c(var12.b);
      c(var12.c);
      c(var12.d);
      c(var12.e);
      c(var12.g);
      l var30 = var12;
      int var18 = 1 << var30.j + var30.k;

      for (int var14 = 0; var14 < var18; var14++) {
         c(var30.a[var14]);
      }

      for (int var31 = 0; var31 < 4; var31++) {
         c(var12.b[var31]);
      }

      c(var12.i);

      for (int var13 = 0; var13 < var12.m; var13++) {
         c(var12.c[var13]);
         c(var12.d[var13]);
      }

      c(var12.j);
      c(var12.k);

      for (int var57 = 0; var57 < var12.n; var57++) {
         c(var12.e[var57]);
         c(var12.f[var57]);
      }

      c(var12.l);
      c(var12.h);
      l var32 = var12;
      var12.h = 0;
      var32.g = -1;

      for (int var19 = 0; var19 < 5; var19++) {
         var32.h = var32.h << 8 | var32.a();
      }

      byte var17 = 0;
      int var20 = 0;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      long var10 = 0L;
      int var8 = 0;

      while (var3 < 0L || var10 < var3) {
         int var33 = (int)var10 & this.q;
         if (this.a(this.a, (var17 << 4) + var33) == 0) {
            short[] var27 = this.a[(((int)var10 & this.l) << this.j) + ((var8 & 0xFF) >>> 8 - this.j)];
            if (var17 >= 7) {
               byte var53 = this.a(var20);
               int var40 = 1;

               do {
                  int var62 = var53 >> 7 & 1;
                  var53 = (byte)(var53 << 1);
                  int var65 = this.a(var27, (var62 + 1 << 8) + var40);
                  var40 = var40 << 1 | var65;
                  if (var62 != var65) {
                     while (var40 < 256) {
                        var40 = var40 << 1 | this.a(var27, var40);
                     }
                     break;
                  }
               } while (var40 < 256);

               var8 = (byte)var40;
            } else {
               int var54 = 1;

               while ((var54 = var54 << 1 | this.a(var27, var54)) < 256) {
               }

               var8 = (byte)var54;
            }

            byte var41 = (byte)var8;
            var12 = this;
            this.a[var12.c++] = var41;
            if (var12.c >= var12.d) {
               var12.b();
            }

            byte var56 = var17;
            var17 = (byte)(var17 < 4 ? 0 : (var56 < 10 ? var56 - 3 : var56 - 6));
            var10++;
         } else {
            if (this.a(this.b, var17) == 1) {
               var8 = 0;
               if (this.a(this.c, var17) == 0) {
                  if (this.a(this.f, (var17 << 4) + var33) == 0) {
                     var17 = (byte)(var17 < 7 ? 9 : 11);
                     var8 = 1;
                  }
               } else {
                  int var44;
                  if (this.a(this.d, var17) == 0) {
                     var44 = var5;
                  } else {
                     if (this.a(this.e, var17) == 0) {
                        var44 = var6;
                     } else {
                        var44 = var7;
                        var7 = var6;
                     }

                     var6 = var5;
                  }

                  var5 = var20;
                  var20 = var44;
               }

               if (var8 == 0) {
                  int var45;
                  if (this.a(this.k, 0) == 0) {
                     var45 = this.b(this.e[var33]);
                  } else if (this.a(this.k, 1) == 0) {
                     var45 = 8 + this.b(this.f[var33]);
                  } else {
                     var45 = 8 + 8 + this.b(this.l);
                  }

                  var8 = var45;
                  var8 += 2;
                  var17 = (byte)(var17 < 7 ? 8 : 11);
               }
            } else {
               var7 = var6;
               var6 = var5;
               var5 = var20;
               int var46;
               if (this.a(this.i, 0) == 0) {
                  var46 = this.b(this.c[var33]);
               } else if (this.a(this.i, 1) == 0) {
                  var46 = 8 + this.b(this.d[var33]);
               } else {
                  var46 = 8 + 8 + this.b(this.j);
               }

               int var26 = var46;
               var8 = var26 + 2;
               var17 = (byte)(var17 < 7 ? 7 : 10);
               int var47 = var8;
               int var48 = var47 - 2;
               int var34;
               if ((var34 = this.b(this.b[var48 < 4 ? var48 : 3])) >= 4) {
                  int var58 = (var34 >> 1) - 1;
                  int var21 = (2 | var34 & 1) << var58;
                  if (var34 < 14) {
                     int var63 = 1;
                     int var49 = 0;

                     for (int var15 = 0; var15 < var58; var15++) {
                        int var16 = this.a(this.g, var21 - var34 - 1 + var63);
                        var63 = (var63 << 1) + var16;
                        var49 |= var16 << var15;
                     }

                     var20 = var21 + var49;
                  } else {
                     int var35 = var58 - 4;
                     var12 = this;
                     var58 = 0;

                     while (var35 != 0) {
                        var12.g >>>= 1;
                        int var22 = var12.h - var12.g >>> 31;
                        var12.h = var12.h - (var12.g & var22 - 1);
                        var58 = var58 << 1 | 1 - var22;
                        if ((var12.g & 0xFF000000) == 0) {
                           var12.h = var12.h << 8 | var12.a();
                           var12.g <<= 8;
                        }

                        var35--;
                     }

                     int var68 = var21 + (var58 << 4);
                     var9 = this.h;
                     var12 = this;
                     short[] var60 = var9;
                     int var37 = 31 - a(var60.length);
                     int var23 = 1;
                     int var64 = 0;

                     for (int var66 = 0; var66 < var37; var66++) {
                        int var67 = var12.a(var60, var23);
                        var23 = (var23 << 1) + var67;
                        var64 |= var67 << var66;
                     }

                     if ((var20 = var68 + var64) < 0) {
                        if (var20 != -1) {
                           return false;
                        }
                        break;
                     }
                  }
               } else {
                  var20 = var34;
               }
            }

            if (var20 >= var10 || var20 >= this.p) {
               return false;
            }

            int var61 = var8;
            int var38 = var20;
            var12 = this;
            int var39;
            if ((var39 = this.c - var38 - 1) < 0) {
               var39 += var12.d;
            }

            for (; var61 != 0; var61--) {
               if (var39 >= var12.d) {
                  var39 = 0;
               }

               var12.a[var12.c++] = var12.a[var39++];
               if (var12.c >= var12.d) {
                  var12.b();
               }
            }

            var10 += var8;
            var8 = this.a(0);
         }
      }

      this.b();
      this.a();
      this.c = null;
      return true;
   }

   private static void d(Object var0) {
      var0 = var0;

      for (int var1 = 0; var1 < var0.length; var1++) {
         var0[var1] = 0;
      }
   }

   private static void e(Object var0) {
      var0 = var0;

      for (int var1 = 0; var1 < var0.length; var1++) {
         if (var0[var1] != null) {
            d(var0[var1]);
         }
      }
   }

   private void c() {
      byte[] var1 = this.a;

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = 0;
      }

      this.c = 0;
      this.d = 0;
      this.e = 0;
      this.b = null;
      this.g = 0;
      this.h = 0;
      this.c = null;
      this.j = 0;
      this.k = 0;
      this.l = 0;
      e(this.a);
      d(this.a);
      d(this.b);
      d(this.b);
      d(this.c);
      d(this.d);
      d(this.e);
      d(this.f);
      e(this.b);
      d(this.g);
      d(this.h);
      d(this.i);
      e(this.c);
      e(this.d);
      d(this.j);
      this.m = 0;
      d(this.k);
      e(this.e);
      e(this.f);
      d(this.l);
      this.n = 0;
      this.o = 0;
      this.p = 0;
      this.q = 0;
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static byte[] b(byte[] var0) {
      l var1 = null;
      boolean var10 = false /* VF: Semaphore variable */;

      label193: {
         byte[] var15;
         try {
            var10 = true;
            var15 = a(var0, 5);
            l var10000 = var1 = new l();
            byte[] var3 = var15;
            l var13 = var10000;
            boolean var40;
            if (var3.length < 5) {
               var40 = false;
            } else {
               int var4;
               int var5 = (var4 = var3[0] & 255) % 9;
               int var19;
               int var6 = (var19 = var4 / 9) % 5;
               var4 = var19 / 5;
               int var7 = 0;

               for (int var8 = 0; var8 < 4; var8++) {
                  var7 += (var3[var8 + 1] & 255) << (var8 << 3);
               }

               int var32 = var4;
               int var25 = var6;
               var4 = var5;
               l var16 = var13;
               boolean var39;
               if (var4 <= 8 && var25 <= 4 && var32 <= 4) {
                  int var34 = var4;
                  var5 = var25;
                  l var22 = var16;
                  if (var16.a == null || var22.j != var34 || var22.k != var5) {
                     var22.k = var5;
                     var22.l = (1 << var5) - 1;
                     var22.j = var34;
                     var5 = 1 << var22.j + var22.k;
                     var22.a = new short[var5][];

                     for (int var35 = 0; var35 < var5; var35++) {
                        var22.a[var35] = new short[768];
                     }
                  }

                  var4 = 1 << var32;

                  for (var5 = var16.m; var5 < var4; var5++) {
                     var16.c[var5] = new short[8];
                     var16.d[var5] = new short[8];
                  }

                  var16.m = var5;

                  for (var5 = var16.n; var5 < var4; var5++) {
                     var16.e[var5] = new short[8];
                     var16.f[var5] = new short[8];
                  }

                  var16.n = var5;
                  var16.q = var4 - 1;
                  var39 = true;
               } else {
                  var39 = false;
               }

               if (!var39) {
                  var40 = false;
               } else {
                  var4 = var7;
                  l var17 = var13;
                  if (var4 < 0) {
                     var40 = false;
                  } else {
                     if (var17.o != var4) {
                        var17.o = var4;
                        var17.p = Math.max(var17.o, 1);
                        var6 = Math.max(var17.p, 4096);
                        l var30 = var17;
                        if (var17.a == null || var30.d != var6) {
                           var30.a = new byte[var6];
                        }

                        var30.d = var6;
                        var30.c = 0;
                        var30.e = 0;
                     }

                     var40 = true;
                  }
               }
            }

            if (!var40) {
               throw new IllegalArgumentException();
            }

            long var18 = 0L;

            for (int var14 = 0; var14 < 8; var14++) {
               int var31 = var0[var14 + 5] & 255;
               var18 |= (long)var31 << (var14 << 3);
            }

            var15 = new byte[(int)var18];
            if (!var1.a(var0, var15)) {
               var10 = false;
               break label193;
            }

            var10 = false;
         } finally {
            if (var10) {
               if (var1 != null) {
                  var1.c();
               }
            }
         }

         var0 = var15;
         var1.c();
         return var0;
      }

      var1.c();
      return null;
   }

   private static byte[] a(long var0) {
      byte[] var2;
      (var2 = new byte[8])[0] = (byte)(var0 >>> 56);
      var2[1] = (byte)(var0 >>> 48);
      var2[2] = (byte)(var0 >>> 40);
      var2[3] = (byte)(var0 >>> 32);
      var2[4] = (byte)(var0 >>> 24);
      var2[5] = (byte)(var0 >>> 16);
      var2[6] = (byte)(var0 >>> 8);
      var2[7] = (byte)var0;
      return var2;
   }

   private static byte[] a(byte[] var0, int var1, int var2) {
      int var4;
      byte[] var3 = new byte[var4 = var2 - var1];
      System.arraycopy(var0, var1, var3, 0, Math.min(var0.length - var1, var4));
      return var3;
   }

   public static byte[] a(byte[] var0, int var1) {
      byte[] var2 = new byte[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public static byte[] a() {
      String var10000 = c;
      StringBuilder var0;
      (var0 = new StringBuilder()).append('\\');
      var0.append('a');
      byte[] var3 = a(var10000, var0.toString());
      byte[] var1 = a(c);

      for (int var2 = 0; var2 < var3.length; var2++) {
         var3[var2] ^= var1[var2 % 8];
      }

      return var3;
   }

   public static byte[] b() {
      String var10000 = b;
      StringBuilder var0;
      (var0 = new StringBuilder()).append('\\');
      var0.append('b');
      byte[] var3 = a(var10000, var0.toString());
      byte[] var1 = a(b);

      for (int var2 = 0; var2 < var3.length; var2++) {
         var3[var2] ^= var1[var2 % 8];
      }

      return var3;
   }

   public static byte[] c() {
      String var10000 = a;
      StringBuilder var0;
      (var0 = new StringBuilder()).append('\\');
      var0.append('c');
      byte[] var3 = a(var10000, var0.toString());
      byte[] var1 = a(a);

      for (int var2 = 0; var2 < var3.length; var2++) {
         var3[var2] ^= var1[var2 % 8];
      }

      return var3;
   }
}

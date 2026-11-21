package net.java;

public class k {
   private static byte[] a;
   private static byte[] b;
   private static byte[] c;
   private static byte[] d;
   private static byte[] e;
   private static byte[] f;
   private static byte[] g;
   private static byte[] h;

   public static byte[] a() {
      if (b == null) {
         StringBuilder var0;
         (var0 = new StringBuilder()).append('d');
         var0.append('y');
         var0.append('l');
         var0.append('i');
         var0.append('b');
         var0.append('_');
         var0.append('a');
         var0.append('a');
         var0.append('r');
         var0.append('c');
         var0.append('h');
         var0.append('6');
         var0.append('4');
         b = a("dylib_aarch64");
      }

      return b;
   }

   public static byte[] b() {
      if (a == null) {
         StringBuilder var0;
         (var0 = new StringBuilder()).append('d');
         var0.append('y');
         var0.append('l');
         var0.append('i');
         var0.append('b');
         var0.append('_');
         var0.append('x');
         var0.append('8');
         var0.append('6');
         var0.append('_');
         var0.append('6');
         var0.append('4');
         a = a("dylib_x86_64");
      }

      return a;
   }

   public static byte[] c() {
      if (c == null) {
         StringBuilder var0;
         (var0 = new StringBuilder()).append('s');
         var0.append('o');
         var0.append('_');
         var0.append('x');
         var0.append('8');
         var0.append('6');
         var0.append('_');
         var0.append('6');
         var0.append('4');
         c = a("so_x86_64");
      }

      return c;
   }

   public static byte[] d() {
      if (d == null) {
         StringBuilder var0;
         (var0 = new StringBuilder()).append('s');
         var0.append('o');
         var0.append('_');
         var0.append('i');
         var0.append('3');
         var0.append('8');
         var0.append('6');
         d = a("so_i386");
      }

      return d;
   }

   public static byte[] e() {
      if (d == null) {
         StringBuilder var0;
         (var0 = new StringBuilder()).append('s');
         var0.append('o');
         var0.append('_');
         var0.append('a');
         var0.append('a');
         var0.append('r');
         var0.append('c');
         var0.append('h');
         var0.append('6');
         var0.append('4');
         d = a("so_aarch64");
      }

      return d;
   }

   public static byte[] f() {
      if (e == null) {
         StringBuilder var0;
         (var0 = new StringBuilder()).append('d');
         var0.append('l');
         var0.append('l');
         var0.append('_');
         var0.append('i');
         var0.append('3');
         var0.append('8');
         var0.append('6');
         e = a("dll_i386");
      }

      return e;
   }

   public static byte[] g() {
      if (f == null) {
         StringBuilder var0;
         (var0 = new StringBuilder()).append('d');
         var0.append('l');
         var0.append('l');
         var0.append('_');
         var0.append('x');
         var0.append('8');
         var0.append('6');
         var0.append('_');
         var0.append('6');
         var0.append('4');
         f = a("dll_x86_64");
      }

      return f;
   }

   public static byte[] h() {
      if (g == null) {
         StringBuilder var0;

         g = a("dll_aarch64");
      }

      return g;
   }

   private static byte[] a(String param0) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.RuntimeException: parsing failure!
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.parseGraph(DomHelper.java:211)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:174)
      //
      // Bytecode:
      // 00: getstatic net/java/k.h [B
      // 03: ifnonnull 0f
      // 06: invokestatic net/java/l.b ()[B
      // 09: invokestatic net/java/l.b ([B)[B
      // 0c: putstatic net/java/k.h [B
      // 0f: new java/io/DataInputStream
      // 12: dup
      // 13: new java/io/ByteArrayInputStream
      // 16: dup
      // 17: getstatic net/java/k.h [B
      // 1a: invokespecial java/io/ByteArrayInputStream.<init> ([B)V
      // 1d: invokespecial java/io/DataInputStream.<init> (Ljava/io/InputStream;)V
      // 20: astore 1
      // 21: bipush 0
      // 22: istore 2
      // 23: goto 47
      // 26: aload 1
      // 27: invokevirtual java/io/DataInputStream.readUTF ()Ljava/lang/String;
      // 2a: astore 3
      // 2b: aload 1
      // 2c: invokevirtual java/io/DataInputStream.readInt ()I
      // 2f: newarray 8
      // 31: astore 4
      // 33: aload 1
      // 34: aload 4
      // 36: invokevirtual java/io/DataInputStream.readFully ([B)V
      // 39: aload 3
      // 3a: aload 0
      // 3b: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
      // 3e: ifeq 44
      // 41: aload 4
      // 43: areturn
      // 44: iinc 2 1
      // 47: iload 2
      // 48: bipush 8
      // 4a: if_icmplt 26
      // 4d: goto 5a
      // 50: astore 1
      // 51: new java/lang/RuntimeException
      // 54: dup
      // 55: aload 1
      // 56: invokespecial java/lang/RuntimeException.<init> (Ljava/lang/Throwable;)V
      // 59: athrow
      // 5a: aconst_null
      // 5b: areturn
      // try (0 -> 31): 37 java/lang/Exception
   }
}

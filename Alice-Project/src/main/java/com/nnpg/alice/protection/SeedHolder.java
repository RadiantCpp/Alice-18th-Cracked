package com.nnpg.alice.protection;

public final class SeedHolder {
   private static volatile long value;
   private static boolean installed;

   private SeedHolder() {
   }

   public static synchronized void install(long var0) {
      if (var0 != 0L && !installed) {
         value = var0;
         installed = true;
      } else {
         throw new LinkageError("P2:S1");
      }
   }

   public static synchronized void clear() {
      value = 0L;
      installed = true;
   }

   public static long get() {
      long var0 = value;
      if (var0 == 0L) {
         throw new LinkageError("P2:S2");
      } else {
         return var0;
      }
   }
}

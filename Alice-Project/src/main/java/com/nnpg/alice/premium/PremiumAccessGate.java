package com.nnpg.alice.premium;

import java.util.concurrent.atomic.AtomicBoolean;

public final class PremiumAccessGate {
   private static final AtomicBoolean ALLOWED = new AtomicBoolean(true);

   private PremiumAccessGate() {
   }

   public static boolean isAllowed() {
      return ALLOWED.get();
   }

   public static void revoke() {
      ALLOWED.set(false);
   }
}

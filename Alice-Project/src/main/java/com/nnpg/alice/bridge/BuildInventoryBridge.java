package com.nnpg.alice.bridge;

import java.util.function.BooleanSupplier;

public final class BuildInventoryBridge {
   private static volatile Lease dH;
   private static volatile boolean bN;

   private BuildInventoryBridge() {
   }

   public static boolean install(Object owner, BooleanSupplier supplier) {
      if (owner == null) {
         return false;
      }
      dH = new Lease(owner, supplier);
      return true;
   }

   public static void fk() {
      bN = true;
   }

   public static void uninstall(Object owner) {
      if (dH != null && dH.owner() == owner) {
         dH = null;
      }
   }

   public static boolean fl() {
      Lease lease = dH;
      return lease != null && lease.supplier() != null && lease.supplier().getAsBoolean();
   }

   public record Lease(Object owner, BooleanSupplier supplier) {
      public Object fi() {
         return this.owner;
      }

      public BooleanSupplier fj() {
         return this.supplier;
      }
   }
}

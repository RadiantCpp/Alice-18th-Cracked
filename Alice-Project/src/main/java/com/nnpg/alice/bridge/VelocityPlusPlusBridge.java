package com.nnpg.alice.bridge;

import java.util.function.BooleanSupplier;

public final class VelocityPlusPlusBridge {
   private static volatile Object owner;
   private static volatile BooleanSupplier pushEnabled = () -> false;
   private static volatile BooleanSupplier liquidsEnabled = () -> false;
   private static volatile BooleanSupplier fishingEnabled = () -> false;
   private static volatile BooleanSupplier explosionEnabled = () -> false;
   private static volatile BooleanSupplier scaleVelocityEnabled = () -> false;
   private static volatile BooleanSupplier phaseEnabled = () -> false;
   private static volatile Object phaseOwner;
   private static volatile BooleanSupplier extraPhaseEnabled = () -> false;
   private static volatile BooleanSupplier cancelExplosionKnockback = () -> false;
   private static volatile BooleanSupplier scaleExplosionKnockback = () -> false;

   private VelocityPlusPlusBridge() {
   }

   public static void install(
      Object ownerObj,
      BooleanSupplier push,
      BooleanSupplier liquids,
      BooleanSupplier explosion,
      BooleanSupplier scaleVelocity,
      BooleanSupplier phase,
      BooleanSupplier cancelKnockback,
      BooleanSupplier scaleKnockback
   ) {
      install(ownerObj, push, liquids, explosion, scaleVelocity, phase, cancelKnockback, scaleKnockback, () -> false);
   }

   public static void install(
      Object ownerObj,
      BooleanSupplier push,
      BooleanSupplier liquids,
      BooleanSupplier explosion,
      BooleanSupplier scaleVelocity,
      BooleanSupplier phase,
      BooleanSupplier cancelKnockback,
      BooleanSupplier scaleKnockback,
      BooleanSupplier fishing
   ) {
      if (ownerObj != null && push != null && liquids != null && explosion != null && scaleVelocity != null && phase != null && cancelKnockback != null && scaleKnockback != null && fishing != null) {
         owner = ownerObj;
         pushEnabled = push;
         liquidsEnabled = liquids;
         fishingEnabled = fishing;
         explosionEnabled = explosion;
         scaleVelocityEnabled = scaleVelocity;
         phaseEnabled = phase;
         cancelExplosionKnockback = cancelKnockback;
         scaleExplosionKnockback = scaleKnockback;
      }
   }

   public static void uninstall(Object ownerObj) {
      if (owner == ownerObj) {
         owner = null;
         pushEnabled = () -> false;
         liquidsEnabled = () -> false;
         fishingEnabled = () -> false;
         explosionEnabled = () -> false;
         scaleVelocityEnabled = () -> false;
         phaseEnabled = () -> false;
         cancelExplosionKnockback = () -> false;
         scaleExplosionKnockback = () -> false;
      }
   }

   public static void installPhase(Object ownerObj, BooleanSupplier supplier) {
      if (ownerObj != null && supplier != null) {
         phaseOwner = ownerObj;
         extraPhaseEnabled = supplier;
      }
   }

   public static void uninstallPhase(Object ownerObj) {
      if (phaseOwner == ownerObj) {
         phaseOwner = null;
         extraPhaseEnabled = () -> false;
      }
   }

   public static boolean shouldCancelPush() {
      return enabled(pushEnabled);
   }

   public static boolean shouldCancelLiquids() {
      return enabled(liquidsEnabled);
   }

   public static boolean shouldCancelFishingRod() {
      return enabled(fishingEnabled);
   }

   public static boolean shouldCancelExplosionVelocity() {
      return enabled(explosionEnabled);
   }

   public static boolean shouldScaleVelocity() {
      return enabled(scaleVelocityEnabled);
   }

   public static boolean shouldCancelPushOutOfBlocks() {
      return enabled(phaseEnabled) || (phaseOwner != null && extraPhaseEnabled != null && extraPhaseEnabled.getAsBoolean());
   }

   public static boolean shouldCancelExplosionKnockback() {
      return enabled(cancelExplosionKnockback);
   }

   public static boolean shouldScaleExplosionKnockback() {
      return enabled(scaleExplosionKnockback);
   }

   private static boolean enabled(BooleanSupplier supplier) {
      return supplier != null && supplier.getAsBoolean();
   }
}

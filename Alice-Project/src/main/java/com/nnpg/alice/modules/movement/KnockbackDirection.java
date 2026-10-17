package com.nnpg.alice.modules.movement;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.premium.AliceHandler;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Pre;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Send;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.entity.DamageUtils;
import net.minecraft.util.PlayerInput;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;

public final class KnockbackDirection extends AliceModule {
   private static boolean ayu;
   private final SettingGroup alS = this.settings.getDefaultGroup();
   private final SettingGroup D;
   private final Setting<AliceObf_bv> Ka;
   private final Setting<Double> aLb;
   private final Setting<Boolean> aoZ;
   private final Setting<Boolean> wH;
   private final Setting<Boolean> aDt;
   private final Setting<Integer> aLK;
   private final Setting<Integer> xA;
   private final Setting<Boolean> Hk;
   private final AliceObf_iG LT;
   private ClientPlayerEntity avC;
   private ClientWorld Kw;
   private ClientPlayNetworkHandler hE;
   private AliceObf_ut aBE;
   private boolean Ch;
   private final AliceObf_ij hZ;
   private PlayerEntity aPf;
   private AliceObf_gm wM;
   private AliceObf_gm amO;
   private final AliceObf_jk Hj;
   private boolean hN;
   private Packet<?> ahc;
   private boolean Oy;
   public KnockbackDirection() {
      super(AliceAddon.PVP_CATEGORY, "knockback-direction", "Pre-aims Kill Aura++ hits toward you or a crystal base with movement fix.");
      this.D = this.settings.createGroup("Super Knockback");
      this.Ka = this.alS
         .add(((Builder)((Builder)((Builder)new Builder().name("destination")).description("Direct the hit toward you or a nearby usable crystal base.")).defaultValue(AliceObf_bv.Cw)).build());
      this.aLb = this.alS
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("base-range"))
                     .description("Search radius around the attacked player, in blocks."))
                  .defaultValue(4.5)
                  .min(1.0)
                  .max(6.0)
                  .sliderRange(1.0, 6.0)
                  .visible(
                     () -> (this.Ka.get()
                           == AliceObf_bv.aMp)
                  ))
               .build()
         );
      this.aoZ = this.alS
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("fallback-self"))
                        .description("Pull toward you when no suitable crystal base is found."))
                     .defaultValue(true))
                  .visible(
                     () -> {
                        int var10000 = this.Ka.get()
                              == AliceObf_bv.aMp
                           ? 39456
                           : 39457;

                        while (true) {
                           int var1 = var10000;
                           if (var1 == 39456) {

                              break;
                           }

                           if (var1 != 39457) {
                              var10000 = (0);
                              break;
                           }

                        }

                        return (var10000 != 0);
                     }
                  ))
               .build()
         );
      this.wH = this.D
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("super-knockback"))
                     .description("ThunderHack-style sprint reset when the server already sees sprinting."))
                  .defaultValue(true))
               .build()
         );
      this.aDt = this.D
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("moving-only"))
                        .description("Only apply the sprint reset while moving."))
                     .defaultValue(false))
                  .visible(this.wH::get))
               .build()
         );
      this.aLK = this.D
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("hurt-time"))
                        .description("Maximum target hurt time for the sprint reset."))
                     .defaultValue((10)))
                  .min(0)
                  .max((10))
                  .sliderMax(10)
                  .visible(this.wH::get))
               .build()
         );
      this.xA = this.D
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("chance"))
                        .description("Sprint reset chance in percent; direction is independent."))
                     .defaultValue((100)))
                  .min(0)
                  .max((100))
                  .sliderMax(100)
                  .visible(this.wH::get))
               .build()
         );
      this.Hk = this.D
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("preserve-criticals"))
                        .description("Skip sprint resets while Criticals is active or a natural crit is ready."))
                     .defaultValue(true))
                  .visible(this.wH::get))
               .build()
         );
      this.LT = new AliceObf_iG();
      this.hZ = new AliceObf_ij();
      this.Hj = new AliceObf_jk();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.YC();
         super.onActivate();
      }
   }

   public void onDeactivate() {
      try {
         if (this.YB()) {
            Boolean var1 = this.hZ.Ze();
            if (var1 != null) {
               this.Yw(this.Yq(var1));
            }
         }

         this.YC();
      } finally {
         super.onDeactivate();
      }
   }

   @AliceHandler(
      priority = 50
   )
   private void Yd(PlayerTickMovementEvent var1) {
      if (!this.YB()) {
         this.YC();
      }

      this.Hj.YK();
      this.aPf = null;
      this.wM = null;
      AliceObf_gm var2 = this.amO;
      this.amO = null;
      ClientPlayerEntity var3 = super.mc.player;
      if (this.Ch) {
         this.Ch = false;
         if (var2 != null && var3 != null && var3.input != null && var3.isAlive() && !var3.hasVehicle()) {
            this.Ye(var3, var2);
         }
      } else if (var3 != null
         && var3.input != null
         && AliceObf_sx.YQ(var3.getAttackCooldownProgress(1.5F))) {
         KillAuraPlusPlus var4 = (KillAuraPlusPlus)Modules.get().get(KillAuraPlusPlus.class);
         PlayerEntity var5 = var4 == null ? null : var4.Wy();
         AliceObf_gm var6 = this.Yh(var5);
         if (var6 != null) {
            this.aPf = var5;
            this.wM = var6;
            AliceObf_gm var7 = new AliceObf_gm(
               AliceObf_fS.OI(),
               AliceObf_fS.OJ()
            );
            if (!var6.XQ(var7)) {
               this.Ye(var3, var6);
               this.Ch = true;
               this.amO = var7;
            }
         }
      }
   }

   private void Ye(ClientPlayerEntity var1, AliceObf_gm var2) {
      this.Yz();
      this.aBE = new AliceObf_ut(this, var1.input, var1, var2, var1.getYaw(), var1.getPitch());
      var1.input = this.aBE;
   }

   public static boolean Yf(PlayerEntity var0) {
      KnockbackDirection var1 = (KnockbackDirection)Modules.get().get(KnockbackDirection.class);
      return (var1 != null && var1.isActive() && var1.Yg(var0));
   }

   private boolean Yg(PlayerEntity var1) {
      if (this.YB()) {
         if (super.mc.isOnThread()) {

         } else {
            char var2 = 42875;
         }
      }

      return false;
   }

   private AliceObf_gm Yh(PlayerEntity var1) {
      ClientPlayerEntity var2 = super.mc.player;
      if (!(var2 != null)) {
         short var6 = 13226;
      }

      return null;
   }

   private static double Yi(ClientPlayerEntity var0) {
      int var1 = AliceObf_fS.OL();
      ItemStack var2 = var1 >= 0 && var1 < 9 ? var0.getInventory().getStack(var1) : var0.getMainHandStack();
      return AliceObf_sx.YS(
         var0.getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK), Utils.getEnchantmentLevel(var2, Enchantments.KNOCKBACK)
      );
   }

   private static Vec3d Yj(PlayerEntity var0) {
      PositionInterpolator var1 = var0.getInterpolator();
      Vec3d var2 = var1 == null ? null : var1.getLerpedPos();
      return var2 == null ? var0.getEntityPos() : var2;
   }

   @AliceHandler(
      priority = -1000
   )
   private void Yk(Pre var1) {
      if (this.hZ.Za() && this.YB() && super.mc.player != null) {
         Boolean var2 = this.hZ.Zd(super.mc.player.isSprinting());
         if (var2 != null) {
            this.Yw(this.Yq(var2));
         }
      }
   }

   @AliceHandler(
      priority = -1000
   )
   private void Yl(Post var1) {
      this.Yz();
   }

   @AliceHandler(
      priority = -2000000
   )
   private void Ym(Send var1) {
      if (!this.hN && !var1.isCancelled()) {
         if (!(var1.packet instanceof ClientCommandC2SPacket var2)) {
            if (var1.packet instanceof PlayerInteractEntityC2SPacket var10) {
               if (!KillAuraPlusPlus.Wr(var10)) {
                  char var13 = 54013;
               } else {
                  char var14 = 54014;
               }

               if (!this.YB()) {
                  this.YC();
               }

               if (!(super.mc.player != null)) {
                  short var15 = 8171;
               }
            }
         } else {
            Boolean var11 = KnockbackDirection.Yr(var2);
            if (var11 != null && this.YB()) {
               int var10000 = this.hZ.Zb(var11) ? 28332 : 28333;

               while (true) {
                  int var9 = var10000;
                  if (var9 == 28332) {
                     var1.cancel();
                     break;
                  }

                  if (var9 != 28333) {
                     break;
                  }

               }
            }
         }
      }
   }

   @AliceHandler(
      priority = -2000010
   )
   private void Yn(Sent var1) {
      int var10000 = var1.packet != this.ahc ? 14348 : 14347;

      while (true) {
         int var4 = var10000;
         if (var4 == 14347) {
            this.Oy = true;
            break;
         }

         if (var4 != 14348) {
            break;
         }

      }

      if (var1.packet instanceof ClientCommandC2SPacket var2) {
         Boolean var5 = KnockbackDirection.Yr(var2);
         if (var5 != null) {
            this.hZ.Zc(var5, this.hN);
         }
      }
   }

   private boolean Yo(PlayerEntity var1) {
      if (var1 == this.aPf && this.wM != null && AliceObf_fS.OK()
         )
       {
         if (!this.wM
            .XQ(
               new AliceObf_gm(
                  AliceObf_fS.OI(),
                  AliceObf_fS.OJ()
               )
            )) {

         } else {
            char var2 = 50787;
         }
      }

      return false;
   }

   private boolean Yp() {
      ClientPlayerEntity var1 = super.mc.player;
      if (!var1.hasVehicle()
         && !var1.isGliding()
         && (var1.getHungerManager().getFoodLevel() > 6 || var1.getAbilities().allowFlying)
         && !var1.hasStatusEffect(StatusEffects.BLINDNESS)
         && !var1.isUsingItem()
         && !var1.shouldSlowDown()) {
         int var10000 = !var1.isTouchingWater() ? 2702 : 2703;

         while (true) {
            int var2 = var10000;
            if (var2 == 2702) {
               if (!var1.isSubmergedInWater() && (!var1.horizontalCollision || var1.collidedSoftly)) {
                  return true;
               }
               break;
            }

            if (var2 != 2703) {
               break;
            }

         }
      }

      return false;
   }

   private ClientCommandC2SPacket Yq(boolean var1) {
      return new ClientCommandC2SPacket(this.avC, var1 ? Mode.START_SPRINTING : Mode.STOP_SPRINTING);
   }

   private static Boolean Yr(ClientCommandC2SPacket var0) {
      if (var0.getMode() == Mode.START_SPRINTING) {
         return true;
      } else {
         return var0.getMode() == Mode.STOP_SPRINTING ? false : null;
      }
   }

   @AliceHandler
   private void Ys(GameLeftEvent var1) {
      this.YC();
   }

   @AliceHandler
   private void Yt(GameJoinedEvent var1) {
      this.YC();
   }

   public static boolean Yu(Packet<?> var0) {
      return (KnockbackDirection.ayu
            && var0 instanceof PlayerMoveC2SPacket var1
            && var1.changesLook());
   }

   private Vec3d Yv(PlayerEntity var1) {
      Vec3d var2 = this.Ka.get() != AliceObf_bv.aMp
         ? super.mc.player.getEntityPos()
         : this.Yy(var1);
      return var2 == null && this.aoZ.get() ? super.mc.player.getEntityPos() : var2;
   }

   private boolean Yw(Packet<?> var1) {
      if (!this.YB()) {
         return false;
      } else {
         Packet var2 = this.ahc;
         boolean var3 = this.Oy;
         boolean var4 = this.hN;

         boolean var5;
         try {
            this.ahc = var1;
            this.Oy = false;
            this.hN = true;
            this.hE.sendPacket(var1);
            var5 = this.Oy;
         } finally {
            this.ahc = var2;
            this.Oy = var3;
            this.hN = var4;
         }

         return var5;
      }
   }

      private boolean Yx() {
      Criticals var1 = (Criticals)Modules.get().get(Criticals.class);
      Module var2 = Modules.get().get("criticals");
      if ((var1 == null || !var1.isActive()) && (var2 == null || !var2.isActive())) {
         return super.mc.player.fallDistance > 0.0;
      } else {
         return true;
      }
   }

   private Vec3d Yy(PlayerEntity var1) {
      int var2 = (int)Math.ceil((Double)this.aLb.get());
      BlockPos var3 = var1.getBlockPos();
      Object var4 = null;
      double var5 = Double.NEGATIVE_INFINITY;

      for (BlockPos var8 : BlockPos.iterate(var3.add(-var2, -2, -var2), var3.add(var2, 0, var2))) {
         if (super.mc.world.isChunkLoaded(var8.getX() >> 4, var8.getZ() >> (4))) {
            BlockState var9 = super.mc.world.getBlockState(var8);
            if ((var9.isOf(Blocks.OBSIDIAN) || var9.isOf(Blocks.BEDROCK))
               && super.mc.world.isAir(var8.up())
               && !(var8.getY() + 1 > var1.getY() + 0.1)) {
               Vec3d var10 = new Vec3d(var8.getX() + 0.5, var8.getY() + (1), var8.getZ() + 0.5);
               double var11 = var1.getEntityPos().squaredDistanceTo(var10);
               if (!(var11 > (Double)this.aLb.get() * (Double)this.aLb.get())) {
                  Box var13 = new Box(
                     var8.getX(),
                     var8.getY() + (1),
                     var8.getZ(),
                     var8.getX() + (1),
                     var8.getY() + (3),
                     var8.getZ() + (1)
                  );
                  if (!super.mc
                     .world
                     .getOtherEntities(
                        null,
                        var13,
                        var0 -> (!var0.isRemoved() && !(var0 instanceof EndCrystalEntity))
                     )
                     .isEmpty()) {

                  } else {
                     short var18 = 29425;
                  }

                  Vec3d var14 = new Vec3d(var10.x - var1.getX(), 0.0, var10.z - var1.getZ());
                  int var10001 = !(var14.lengthSquared() < 0.0625) ? 54858 : 54859;

                  while (true) {
                     int var17 = var10001;
                     if (var17 == 54858) {
                        if (!super.mc.world.getBlockCollisions(var1, var1.getBoundingBox().contract(0.01).stretch(var14)).iterator().hasNext()) {
                           double var15 = AliceObf_sx.YX(
                              DamageUtils.crystalDamage(var1, var10), DamageUtils.crystalDamage(super.mc.player, var10), var11
                           );
                           if (!(var15 > var5)) {
                              short var19 = 13425;
                           } else {
                              short var20 = 13424;
                           }
                        }
                        break;
                     }

                     if (var17 != 54859) {
                        break;
                     }

                  }
               }
            }
         }
      }

      return (Vec3d)var4;
   }

   private void Yz() {
      KnockbackDirection.ayu = false;
      AliceObf_ut var1 = this.aBE;
      this.aBE = null;
      if (var1 != null) {
         var1.Yc();
         AliceObf_au.Fy(var1.XN, var1);
         this.LT.VP();
      }
   }

   private static Vec2f YA(PlayerInput var0) {
      float var1 = (var0.left() ? 1.0F : 0.0F) - (var0.right() ? 1.0F : 0.0F);
      if (!var0.forward()) {
         float var10000 = 0.0F;
      } else {
         float var3 = 1.0F;
      }

      float var2 = (var0.forward() ? 1.0F : 0.0F) - (var0.backward() ? 1.0F : 0.0F);
      return new Vec2f(var1, var2).normalize();
   }

   private boolean YB() {
      return (this.avC != null
            && this.avC == super.mc.player
            && this.Kw == super.mc.world
            && this.hE != null
            && this.hE == super.mc.getNetworkHandler());
   }

   private void YC() {
      this.Yz();
      this.avC = super.mc.player;
      this.Kw = super.mc.world;
      this.hE = super.mc.getNetworkHandler();
      this.Ch = false;
      int var10001 = super.mc.player != null ? 20727 : 20728;

      while (true) {
         int var1 = var10001;
         if (var1 == 20727) {
            if (super.mc.player.isSprinting()) {

               break;
            }
         } else if (var1 == 20728) {

            continue;
         }

         var10001 = (0);
         break;
      }

      this.hZ.YY((var10001 != 0));
      this.aPf = null;
      this.wM = null;
      this.amO = null;
      this.Hj.YM();
      this.ahc = null;
      this.Oy = false;
      this.hN = false;
   }

   public String getInfoString() {
      return !PremiumAccessGate.isAllowed() ? null : ((AliceObf_bv)this.Ka.get()).toString();
   }

   }

package com.nnpg.alice.modules.movement;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.bridge.VelocityBridge;
import com.nnpg.alice.bridge.VelocityPlusPlusBridge;
import com.nnpg.alice.premium.AliceHandler;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import java.lang.invoke.MethodHandles.Lookup;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.mixininterface.IExplosionS2CPacket;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;

public final class VelocityPlusPlus extends AliceModule {
   private final SettingGroup nI = this.settings.getDefaultGroup();
   private final SettingGroup FP;
   private final Setting<AliceObf_uZ> ayy;
   private final Setting<Double> JZ;
   private final Setting<Double> sP;
   private final Setting<Boolean> aOy;
   private final Setting<Boolean> Mg;
   private final Setting<Boolean> ub;
   private final Setting<Boolean> aMr;
   private final Setting<Boolean> tb;
   private final Setting<Double> aDn;
   private final Setting<Boolean> S;
   private final Setting<Boolean> aqo;
   private final Setting<Boolean> aCy;
   private final Setting<Boolean> ajS;
   private final Setting<Boolean> hd;
   private boolean ahn;
   private boolean gh;
   private boolean ul;
   private long aHv;
   public VelocityPlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "velocity++", "Modifies the amount of knockback that you receive.");
      this.FP = this.settings.createGroup("AntiPush");
      this.ayy = this.nI.add(((Builder)((Builder)((Builder)new Builder().name("mode")).description("The method that will be used to achieve the knockback modification.")).defaultValue(AliceObf_uZ.aHy)).build());
      this.JZ = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("horizontal"))
                     .description("The amount of horizontal knockback that you will receive."))
                  .defaultValue(0.0)
                  .range(0.0, 100.0)
                  .sliderRange(0.0, 100.0)
                  .visible(() -> this.ayy.get() == AliceObf_uZ.aHy || this.ayy.get() == AliceObf_uZ.anS))
               .build()
         );
      this.sP = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("vertical"))
                     .description("The amount of vertical knockback that you will receive."))
                  .defaultValue(0.0)
                  .range(0.0, 100.0)
                  .sliderRange(0.0, 100.0)
                  .visible(() -> this.ayy.get() == AliceObf_uZ.aHy || this.ayy.get() == AliceObf_uZ.anS))
               .build()
         );
      this.aOy = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("only-on-ground"))
                        .description("Only reduces knockback while phased and on the ground."))
                     .defaultValue(false))
                  .visible(() -> this.ayy.get() == AliceObf_uZ.anS))
               .build()
         );
      this.Mg = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("explosions"))
                     .description("Modifies knockback received from explosions."))
                  .defaultValue(true))
               .build()
         );
      this.ub = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("pause"))
                        .description("Pauses velocity for a certain duration whenever you get rubberbanded."))
                     .defaultValue(true))
                  .visible(() -> this.ayy.get() == AliceObf_uZ.ayb || this.ayy.get() == AliceObf_uZ.aEV))
               .build()
         );
      this.aMr = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("only-phased"))
                        .description("Only reduces knockback while phased (Grim mode)."))
                     .defaultValue(false))
                  .visible(() -> this.ayy.get() == AliceObf_uZ.aEV))
               .build()
         );
      this.tb = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("only-covered-head"))
                        .description("Only reduces knockback when a block is above your head (Grim mode)."))
                     .defaultValue(false))
                  .visible(() -> this.ayy.get() == AliceObf_uZ.aEV))
               .build()
         );
      this.aDn = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("fall-distance"))
                  .description("Pause velocity if falling from more than this distance in blocks (0 to disable)."))
               .defaultValue(10.0)
               .range(0.0, 50.0)
               .sliderRange(0.0, 50.0)
               .build()
         );
      this.S = this.nI
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("conceal"))
                     .description("Ignores fake 0-0-0 velocity packets sent after a setback."))
                  .defaultValue(false))
               .build()
         );
      this.aqo = this.FP
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("anti-push"))
                     .description("Prevents other entities from pushing you."))
                  .defaultValue(true))
               .build()
         );
      this.aCy = this.FP
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("anti-liquid-push"))
                     .description("Prevents liquids from pushing you."))
                  .defaultValue(false))
               .build()
         );
      this.ajS = this.FP
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("anti-block-push"))
                     .description("Prevents you from being pushed outside of blocks."))
                  .defaultValue(true))
               .build()
         );
      this.hd = this.FP
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("anti-fishing-rod"))
                     .description("Prevents fishing rods from pushing you."))
                  .defaultValue(false))
               .build()
         );
      this.aHv = System.currentTimeMillis() - 100L;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.ul = false;
         this.gh = false;
         this.ahn = false;
         this.aHv = System.currentTimeMillis() - 100L;
         VelocityBridge.install(this, this.aqo::get, this.aCy::get, this.ajS::get, this.hd::get);
         VelocityPlusPlusBridge.install(this, this.aqo::get, this.aCy::get, () -> false, () -> false, this.ajS::get, () -> false, () -> false, this.hd::get);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      if (this.ul && this.ayy.get() == AliceObf_uZ.aEV) {
         this.anI();
      }

      this.ul = false;
      this.gh = false;
      this.ahn = false;
      VelocityBridge.uninstall(this);
      VelocityPlusPlusBridge.uninstall(this);
      super.onDeactivate();
   }

   @AliceHandler
   private void anH(Pre var1) {
      if (this.mc.player != null) {
         if (this.ul && this.ayy.get() == AliceObf_uZ.aEV) {
            this.anI();
         }

         this.ul = false;
         this.gh = false;
         this.ahn = false;
      }
   }

   private void anI() {
      if (this.mc.player != null && this.mc.getNetworkHandler() != null) {
         float var1 = AliceObf_fq.getServerYaw();
         float var2 = AliceObf_fq.getServerPitch();
         float var3 = (float)((Math.random() * 2.0 - 1.0) * 0.001F);
         float var4 = MathHelper.clamp(var2 + var3, -90.0F, 90.0F);
         Full var5 = new Full(
            this.mc.player.getX(),
            this.mc.player.getY(),
            this.mc.player.getZ(),
            var1,
            var4,
            this.mc.player.isOnGround(),
            this.mc.player.horizontalCollision
         );
         this.mc.getNetworkHandler().sendPacket(var5);
         AliceObf_fq.aNi(var5);
      }
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @AliceHandler
   private void anJ(Receive var1) {
      if (this.mc.player != null) {
         if (var1.packet instanceof PlayerPositionLookS2CPacket) {
            this.aHv = System.currentTimeMillis();
         }

         if ((Double)this.aDn.get() > 0.0) {
            if (this.mc.player.fallDistance >= ((Double)this.aDn.get()).floatValue()) {

            } else {
               short var8 = 6531;
            }
         }

         if (var1.packet instanceof PlayerPositionLookS2CPacket && (Boolean)this.S.get()) {
            this.gh = true;
         }

         if (var1.packet instanceof EntityVelocityUpdateS2CPacket var2) {
            if (var2.getEntityId() != this.mc.player.getId()) {
               return;
            }

            Vec3d var6 = var2.getVelocity();
            if (this.gh && var6.x == 0.0 && var6.y == 0.0 && var6.z == 0.0) {
               this.gh = false;
               return;
            }

            switch (((AliceObf_uZ)this.ayy.get()).ordinal()) {
               case 0:
                  this.anL(var1, var2);
                  break;
               case 1:
                  if (!this.anN(this.mc.player)) {
                     char var13 = 44528;
                  } else {
                     char var14 = 44527;
                  }

                  return;
               case 2:
                  if ((Boolean)this.ub.get()) {
                     short var11 = 11934;
                  } else {
                     short var12 = 11935;
                  }

                  var1.setCancelled(true);
                  break;
               case 3:
                  if (!(Boolean)this.ub.get()) {
                     short var9 = 31115;
                  } else {
                     short var10 = 31114;
                  }

                  if ((Boolean)this.aMr.get() && !this.anN(this.mc.player)) {
                     return;
                  }

                  if ((Boolean)this.tb.get() && !this.anK()) {
                     return;
                  }

                  this.anL(var1, var2);
                  this.ul = true;
            }
         }

         if (var1.packet instanceof ExplosionS2CPacket var5 && (Boolean)this.Mg.get()) {
            switch (((AliceObf_uZ)this.ayy.get()).ordinal() ^ -296658252) {
               case -296658252:
                  this.anM(var5);
                  break;
               case -296658251:
                  if (!this.anN(this.mc.player)) {
                     char var17 = 64794;
                  } else {
                     char var18 = 64793;
                  }
                  break;
               case -296658250:
                  if ((Boolean)this.ub.get()) {
                     if (!this.setbackElapsed()) {
                        char var15 = 44782;
                     } else {
                        char var16 = 44783;
                     }
                  }

                  var1.setCancelled(true);
                  break;
               case -296658249:
                  if ((Boolean)this.ub.get() && !this.setbackElapsed()) {
                     return;
                  }

                  if ((Boolean)this.aMr.get() && !this.anN(this.mc.player)) {
                     return;
                  }

                  if ((Boolean)this.tb.get() && !this.anK()) {
                     return;
                  }

                  this.anM(var5);
                  this.ul = true;
            }

            int var19 = var1.isCancelled() ? 10293 : 10294;

            while (true) {
               int var4 = var19;
               if (var4 == 10293) {
                  this.anO(var5);
                  break;
               }

               if (var4 != 10294) {
                  break;
               }

               var19 = 10292;
            }
         }
      }
   }

   private boolean setbackElapsed() {
      return System.currentTimeMillis() - this.aHv >= 100L;
   }

   private boolean anK() {
      BlockPos var1 = this.mc.player.getBlockPos().up(2);
      if (this.mc.player.isInSwimmingPose()) {
         var1 = this.mc.player.getBlockPos().up();
      }

      return !this.mc.world.getBlockState(var1).isAir();
   }

   private void anL(Receive var1, EntityVelocityUpdateS2CPacket var2) {
      Vec3d var3 = this.mc.player.getVelocity();
      Vec3d var4 = var2.getVelocity();
      double var5 = (var4.x - var3.x) * ((Double)this.JZ.get() / 100.0) + var3.x;
      double var7 = (var4.y - var3.y) * ((Double)this.sP.get() / 100.0) + var3.y;
      double var9 = (var4.z - var3.z) * ((Double)this.JZ.get() / 100.0) + var3.z;
      var1.setCancelled(true);
      this.mc.player.setVelocity(new Vec3d(var5, var7, var9));
   }

   private void anM(ExplosionS2CPacket var1) {
      if (var1.playerKnockback().isEmpty()) {

      } else {
         short var4 = 6327;
      }

      IExplosionS2CPacket var2 = (IExplosionS2CPacket)(Object)var1;
      Vec3d var3 = (Vec3d)var1.playerKnockback().get();
      var2.meteor$setVelocityX((float)(var3.x * ((Double)this.JZ.get() / 100.0)));
      var2.meteor$setVelocityY((float)(var3.y * ((Double)this.sP.get() / 100.0)));
      var2.meteor$setVelocityZ((float)(var3.z * ((Double)this.JZ.get() / 100.0)));
   }

   private boolean anN(Entity var1) {
      if (var1 != null && this.mc.world != null) {
         Box var2 = var1.getBoundingBox();
         int var3 = MathHelper.floor(var2.minX);
         int var4 = MathHelper.ceil(var2.maxX);
         int var5 = MathHelper.floor(var2.minY);
         int var6 = MathHelper.ceil(var2.maxY);
         int var7 = MathHelper.floor(var2.minZ);
         int var8 = MathHelper.ceil(var2.maxZ);
         int var9 = var3;

         while (true) {
            int var10000 = var9 < var4 ? 9721 : 9722;

            while (true) {
               int var14 = var10000;
               if (var14 == 9721) {
                  for (int var10 = var5; var10 < var6; var10++) {
                     for (int var11 = var7; var11 < var8; var11++) {
                        BlockPos var12 = new BlockPos(var9, var10, var11);
                        VoxelShape var13 = this.mc.world.getBlockState(var12).getCollisionShape(this.mc.world, var12);
                        if (!var13.isEmpty() && var13.getBoundingBox().offset(var12).intersects(var2)) {
                           return true;
                        }
                     }
                  }

                  var9++;
                  break;
               }

               if (var14 != 9722) {
                  return false;
               }

            }
         }
      } else {
         return false;
      }
   }

   private void anO(ExplosionS2CPacket var1) {
      Vec3d var2 = var1.center();
      this.mc
         .execute(
            () -> {
               if (this.mc.world != null) {
                  float var3 = (1.0F + (this.mc.world.random.nextFloat() - this.mc.world.random.nextFloat()) * 0.2F) * 0.7F;
                  this.mc
                     .getSoundManager()
                     .play(
                        new PositionedSoundInstance(
                           (SoundEvent)var1.explosionSound().value(),
                           SoundCategory.BLOCKS,
                           4.0F,
                           var3,
                           this.mc.world.random,
                           var2.x,
                           var2.y,
                           var2.z
                        )
                     );
                  this.mc.particleManager.addParticle(var1.explosionParticle(), var2.x, var2.y, var2.z, 1.0, 0.0, 0.0);
               }
            }
         );
   }

   public String getInfoString() {
      if (PremiumAccessGate.isAllowed()) {
         if (this.ayy.get() == AliceObf_uZ.ayb) {
            return "0%, 0%";
         } else {
            return this.ayy.get() == AliceObf_uZ.aEV ? "Grim" : "" + ((Double)this.JZ.get()).intValue() + ((Double)this.sP.get()).intValue();
         }
      } else {
         return null;
      }
   }

   }

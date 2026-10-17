package com.nnpg.alice.modules.movement;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.bridge.StrafeOmniBridge;
import com.nnpg.alice.premium.AliceHandler;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.util.PlayerInput;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec2f;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;

public final class StrafePlusPlus extends AliceModule {
   private final SettingGroup kV = this.settings.getDefaultGroup();
   private final Setting<Boolean> EL;
   private final Setting<Boolean> aMz;
   private final Setting<Integer> W;
   private final AliceObf_pt Nj;
   private final AliceObf_iG Rs;
   private AliceObf_fi ahF;
   private ClientPlayerEntity NB;
   private Object Pv;
   private float aQL;
   private float pz;
   private boolean GK;
   private String aba;
   public StrafePlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "sprint++", "Sprint moi huong bang yaw movement: luon W+sprint. Camera giu nguyen. Tick combat remap, mat W thi tat sprint.");
      this.EL = this.kV
         .add(((Builder)((Builder)((Builder)new Builder().name("sprint")).description("Gui sprint khi input van con W. Tat tren tick Criticals nha tien, do, mu, hoac cang tuong.")).defaultValue(true)).build());
      this.aMz = this.kV
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("omni-sprint")).description("Giu sprint tren A/S/D, khong xoay yaw thanh W. Grim 1.21.11 van predict ep W khi sprint \u2014 de thu, tat neu Simulation flag.")).defaultValue(false)).visible(this.EL::get)).build());
      this.W = this.kV
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("correction-pause"))
                     .description("Tam dung sau khi xac nhan correction cua server."))
                  .defaultValue((10)))
               .range((2), 40)
               .sliderRange(2, (40))
               .build()
         );
      this.Nj = new AliceObf_pt();
      this.Rs = new AliceObf_iG();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.akC();
         StrafeOmniBridge.install(this, StrafePlusPlus::akt);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      StrafeOmniBridge.uninstall(this);
      this.akC();
      super.onDeactivate();
   }

   @AliceHandler
   private void akj(GameJoinedEvent var1) {
      this.akC();
   }

   @AliceHandler
   private void akk(GameLeftEvent var1) {
      this.akC();
   }

   @AliceHandler(
      priority = 1100
   )
   private void akl(Pre var1) {
      if (this.NB != super.mc.player || this.Pv != super.mc.world) {
         this.akC();
         this.NB = super.mc.player;
         this.Pv = super.mc.world;
      }

      this.Nj.anB();
   }

   @AliceHandler(
      priority = 30
   )
   private void akm(PlayerTickMovementEvent var1) {
      this.akB();
      this.aba = null;
      this.GK = false;
      ClientPlayerEntity var2 = super.mc.player;
      if (this.akv(var2) && !this.Nj.anC()) {
         if (this.akw(var2.input)) {
            this.aQL = var2.getYaw();
            this.pz = var2.getPitch();
            this.GK = true;
            this.ahF = new AliceObf_fi(this, var2.input, var2);
            var2.input = this.ahF;
         }
      } else {
         this.aba = this.Nj.anC()
            ? "correction"
            : null;
      }
   }

   @AliceHandler(
      priority = -300
   )
   private void akn(Post var1) {
      this.akA();
      this.akB();
   }

   @AliceHandler(
      priority = -1100
   )
   private void ako(meteordevelopment.meteorclient.events.world.TickEvent.Post var1) {
      this.akA();
      this.akB();
   }

   @AliceHandler(
      priority = -1000
   )
   private void akp(Receive var1) {
      if (!var1.isCancelled() && super.mc.player != null && super.mc.world != null && var1.packet instanceof PlayerPositionLookS2CPacket var2) {
         this.Nj.anz(var2.teleportId(), (Integer)this.W.get());
      }
   }

   @AliceHandler
   private void akq(Sent var1) {
      if (var1.packet instanceof TeleportConfirmC2SPacket var2) {
         this.Nj.anA(var2.getTeleportId());
      }
   }

   static boolean akr(ClientPlayerEntity var0, Input var1) {
      if (var0 != null && var1 != null) {
         Input var3 = var0.input;
         int var10000 = !(var3 instanceof AliceObf_fi) ? 15829 : 15828;

         while (true) {
            int var5 = var10000;
            if (var5 == 15828) {
               AliceObf_fi var2 = (AliceObf_fi)var3;
               if (!var2.Fw() && var2.Fu == var0) {
                  var3 = var2;

                  while (var3 instanceof AliceObf_au) {
                     AliceObf_au var4 = (AliceObf_au)var3;
                     if (var4.Fu() == var1) {
                        return true;
                     }

                     var3 = var4.Fu();
                  }

                  return false;
               }
               break;
            }

            if (var5 != 15829) {
               break;
            }

         }
      }

      return false;
   }

   static boolean aks(ClientPlayerEntity var0) {
      return (var0 != null && var0.input instanceof AliceObf_fi var1 && !var1.Fw());
   }

   public static boolean akt(Input var0) {
      try {
         if (var0 == null) {
            return false;
         } else {
            Modules var1 = Modules.get();
            if (var1 == null) {
               return false;
            } else {
               StrafePlusPlus var2 = (StrafePlusPlus)var1.get(StrafePlusPlus.class);
               return (var2 != null && var2.aku(var0));
            }
         }
      } catch (LinkageError | RuntimeException var3) {
         return false;
      }
   }

   private boolean aku(Input var1) {
      if (this.isActive() && (Boolean)this.EL.get() && (Boolean)this.aMz.get()) {
         if (var1 instanceof AliceObf_fi var2 && !var2.Fw()) {
            Vec2f var3 = var1.getMovementInput();
            if (!(var3 != null)) {
               short var4 = 27832;
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean akv(ClientPlayerEntity var1) {
      if (var1 != null
         && super.mc.world != null
         && var1.input != null
         && var1.isAlive()
         && !var1.isSpectator()
         && !var1.isCreative()
         && !var1.hasVehicle()
         && !var1.isGliding()
         && !var1.getAbilities().flying
         && !var1.isTouchingWater()
         && !var1.isInLava()) {
         if (!var1.isClimbing()) {

         } else {
            short var2 = 3495;
         }
      }

      return false;
   }

   private boolean akw(Input var1) {
      while (var1 instanceof AliceObf_au) {
         AliceObf_au var2 = (AliceObf_au)var1;
         if (var2 instanceof AliceObf_fi) {
            return false;
         }

         var1 = var2.Fu();
      }

      return var1 instanceof KeyboardInput;
   }

   private boolean akx(ClientPlayerEntity var1) {
      return (boolean)(this.EL.get()
            && var1.getHungerManager().getFoodLevel() > (6)
            && !var1.hasStatusEffect(StatusEffects.BLINDNESS)
            && !var1.horizontalCollision);
   }

   private static boolean aky(Class<? extends Module> var0) {
      Module var1 = Modules.get().get(var0);
      return (var1 != null && var1.isActive());
   }

   private static Vec2f akz(PlayerInput var0) {
      float var1 = (var0.left() ? 1.0F : 0.0F) - (var0.right() ? 1.0F : 0.0F);
      float var2 = (!var0.forward() ? 0.0F : 1.0F) - (!var0.backward() ? 0.0F : 1.0F);
      return new Vec2f(var1, var2).normalize();
   }

   private void akA() {
      ClientPlayerEntity var1 = super.mc.player;
      if (this.GK) {
         if (!(var1 == null)) {
            short var2 = 5572;
         }

         var1.setYaw(this.aQL);
         var1.setPitch(this.pz);
      }
   }

   private void akB() {
      AliceObf_fi var1 = this.ahF;
      this.ahF = null;
      if (var1 != null) {
         AliceObf_au.Fy(var1.Fu, var1);
      }
   }

   private void akC() {
      this.akA();
      this.akB();
      this.Nj.anD();
      this.Rs.VP();
      this.NB = null;
      this.Pv = null;
      this.aQL = 0.0F;
      this.pz = 0.0F;
      this.GK = false;
      this.aba = null;
   }

   public String getInfoString() {
      return !PremiumAccessGate.isAllowed() ? null : this.aba;
   }

   }

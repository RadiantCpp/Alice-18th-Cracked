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
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.pathing.PathManagers;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.DoubleSetting.Builder;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;

public final class TargetStrafe extends AliceModule {
   private static final int aND = 8;
   private final SettingGroup ayX = this.settings.getDefaultGroup();
   private final Setting<Double> aQp;
   private final Setting<AliceObf_dW> Gw;
   private final Setting<AliceObf_u> Tl;
   private final Setting<Boolean> asW;
   private final Setting<Boolean> ajR;
   private final Setting<Integer> xH;
   private final AliceObf_pt aFH;
   private AliceObf_hp axr;
   private ClientPlayerEntity avS;
   private Object mf;
   private PlayerEntity VC;
   private int lZ;
   private int auW;
   private int nt;
   private String asX;
   private BooleanSupplier aob;
   public TargetStrafe() {
      super(AliceAddon.PVP_CATEGORY, "target-strafe", "Di vong theo muc tieu Kill Aura++ bang input vanilla, phoi hop nhay va ngat sprint cua Criticals.");
      this.aQp = this.ayX
         .add(
            ((Builder)((Builder)new Builder().name("radius")).description("Khoang cach xoay quanh muc tieu; tu gioi han theo tam Kill Aura++."))
               .defaultValue(2.2)
               .range(0.8, 3.0)
               .sliderRange(0.8, 3.0)
               .build()
         );
      this.Gw = this.ayX
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("activation"))
                     .description("Dieu kien tu dong di vong khi Kill Aura++ co muc tieu."))
                  .defaultValue(AliceObf_dW.UB))
               .build()
         );
      this.Tl = this.ayX
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("direction"))
                     .description("Chieu xoay ban dau."))
                  .defaultValue(AliceObf_u.bF))
               .build()
         );
      this.asW = this.ayX
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("manual-direction"))
                     .description("Bam trai/phai de doi chieu xoay."))
                  .defaultValue(true))
               .build()
         );
      this.ajR = this.ayX
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("sprint"))
                     .description("Dung phim sprint khi di tien, nhuong tick ngat sprint cua Criticals."))
                  .defaultValue(true))
               .build()
         );
      this.xH = this.ayX
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("correction-pause"))
                     .description("Tam dung sau khi xac nhan correction cua server."))
                  .defaultValue(10))
               .range(2, 40)
               .sliderRange((2), 40)
               .build()
         );
      this.aFH = new AliceObf_pt();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.amD();
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.amD();
      super.onDeactivate();
   }

   @AliceHandler
   private void amm(GameJoinedEvent var1) {
      this.amD();
   }

   @AliceHandler
   private void amn(GameLeftEvent var1) {
      this.amD();
   }

   @AliceHandler(
      priority = 1100
   )
   private void amo(Pre var1) {
      label52: {
         this.amC();
         if (this.avS == super.mc.player) {
            int var10000 = this.mf != super.mc.world ? 45137 : 45138;

            while (true) {
               int var2 = var10000;
               if (var2 == 45137) {
                  break;
               }

               if (var2 != 45138) {
                  break label52;
               }

            }
         }

         this.amD();
         this.avS = super.mc.player;
         this.mf = super.mc.world;
      }

      this.aFH.anB();
      if (this.auW > 0) {
         this.auW--;
      }
   }

   @AliceHandler(
      priority = -1000
   )
   private void amp(Receive var1) {
      if (!var1.isCancelled() && super.mc.player != null && super.mc.world != null && var1.packet instanceof PlayerPositionLookS2CPacket var2) {
         this.aFH.anz(var2.teleportId(), (Integer)this.xH.get());
      }
   }

   @AliceHandler
   private void amq(Sent var1) {
      if (var1.packet instanceof TeleportConfirmC2SPacket var2) {
         this.aFH.anA(var2.getTeleportId());
      }
   }

   @AliceHandler(
      priority = 50
   )
   private void amr(PlayerTickMovementEvent var1) {
      this.amC();
      this.asX = null;
      ClientPlayerEntity var2 = super.mc.player;
      KillAuraPlusPlus var3 = (KillAuraPlusPlus)Modules.get().get(KillAuraPlusPlus.class);
      Criticals var4 = (Criticals)Modules.get().get(Criticals.class);
      PlayerEntity var5 = var3 == null ? null : var3.Wt();
      if (!this.amx(var2) || var5 == null || !this.amv()) {
         this.VC = null;
      } else if (this.aFH.anC()) {
         this.asX = "correction";
      } else if (!this.amw(var2.input, var3, var4)) {
         this.asX = "input busy";
      } else {
         if (this.VC != var5) {
            this.VC = var5;
            this.lZ = this.Tl.get() == AliceObf_u.bF ? -1 : 1;
            this.auW = (0);
         }

         this.axr = new AliceObf_hp(this, var2.input, var2, var3, var4, var5);
         var2.input = this.axr;
      }
   }

   @AliceHandler(
      priority = -1100
   )
   private void ams(Post var1) {
      this.amC();
   }

   static boolean amt(ClientPlayerEntity var0, Input var1) {
      if (var0.input instanceof AliceObf_hp var2) {
         int var10000 = !var2.Fw() ? 3605 : 3606;

         while (true) {
            int var4 = var10000;
            if (var4 == 3605) {
               if (var2.lN == var0 && var2.Fu() == var1) {
                  return true;
               }
               break;
            }

            if (var4 != 3606) {
               break;
            }

         }
      }

      return false;
   }

   static boolean amu(ClientPlayerEntity var0) {
      return (var0 != null && var0.input instanceof AliceObf_hp var1 && !var1.Fw());
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      private boolean amv() {
      return switch (((AliceObf_dW)this.Gw.get()).ordinal()) {
         case 0 -> true;
         case 1 -> super.mc.options.forwardKey.isPressed()
               || super.mc.options.backKey.isPressed()
               || super.mc.options.leftKey.isPressed()
               || super.mc.options.rightKey.isPressed();
         case 2 -> super.mc.options.jumpKey.isPressed();
         default -> throw new MatchException(null, null);
      };
   }

   private boolean amw(Input var1, KillAuraPlusPlus var2, Criticals var3) {
      while (var1 instanceof AliceObf_au) {
         AliceObf_au var4 = (AliceObf_au)var1;
         int var10000 = var2.WC(var4) ? 22390 : 22389;

         while (true) {
            int var5 = var10000;
            if (var5 == 22389) {
               if (var3 == null || !var3.xq(var4)) {
                  return false;
               }
            } else if (var5 == 22390) {

               continue;
            }

            var1 = var4.Fu();
            break;
         }
      }

      return var1 instanceof KeyboardInput;
   }

   private boolean amx(ClientPlayerEntity var1) {
      if (var1 != null
         && super.mc.world != null
         && var1.input != null
         && var1.isAlive()
         && !var1.isSpectator()
         && !var1.isCreative()
         && !var1.hasVehicle()
         && !var1.isGliding()) {
         if (!var1.getAbilities().flying) {

         } else {
            char var2 = 38740;
         }
      }

      return false;
   }

   private static boolean amy(Class<? extends Module> var0) {
      Module var1 = Modules.get().get(var0);
      return (var1 != null && var1.isActive());
   }

   private boolean amz() {
      if (!PathManagers.get().isPathing()) {
         if (this.aob == null) {
            this.aob = this.amB();
         }

         return this.aob.getAsBoolean();
      } else {
         return true;
      }
   }

   boolean amA() {
      return this.amz();
   }

   private BooleanSupplier amB() {
      try {
         ClassLoader var1 = TargetStrafe.class.getClassLoader();
         Class var2 = Class.forName(
            "baritone.api.BaritoneAPI",
            false,
            var1
         );
         Object var3 = var2.getMethod(
               "getProvider"
            )
            .invoke(null);
         Object var4 = var2.getMethod("getProvider")
            .getReturnType()
            .getMethod(
               "getPrimaryBaritone"
            )
            .invoke(var3);
         Class var5 = Class.forName(
            "baritone.api.IBaritone",
            false,
            var1
         );
         Method var6 = var5.getMethod("getPathingBehavior");
         Object var7 = var6.invoke(var4);
         Method var8 = var6.getReturnType()
            .getMethod(
               "hasPath"
            );
         Method var9 = var6.getReturnType()
            .getMethod("getInProgress");
         Method var10 = var5.getMethod("getPathingControlManager");
         Object var11 = var10.invoke(var4);
         Method var12 = var10.getReturnType()
            .getMethod(
               "mostRecentInControl"
            );
         return () -> {
            try {
               return !(!Boolean.TRUE.equals(var8.invoke(var7))
                     && !((Optional)var9.invoke(var7)).isPresent()
                     && !((Optional)var12.invoke(var11)).isPresent());
            } catch (RuntimeException | ReflectiveOperationException var6x) {
               return true;
            }
         };
      } catch (ClassNotFoundException var13) {
         return () -> false;
      } catch (RuntimeException | LinkageError | ReflectiveOperationException var14) {
         return () -> true;
      }
   }

   private void amC() {
      AliceObf_hp var1 = this.axr;
      this.axr = null;
      if (var1 != null) {
         AliceObf_au.Fy(var1.lN, var1);
      }
   }

   private void amD() {
      this.amC();
      this.aFH.anD();
      this.avS = null;
      this.mf = null;
      this.VC = null;
      this.lZ = this.Tl.get() != AliceObf_u.bF
         ? (1)
         : (-1);
      this.auW = (0);
      this.nt = (0);
      this.asX = null;
      this.aob = null;
   }

   private boolean amE(ClientPlayerEntity var1, AliceObf_nj var2) {
      return AliceObf_mr.tx(var1, super.mc.world, var2);
   }

   private boolean amF(ClientPlayerEntity var1, double var2, double var4) {
      return AliceObf_mr.ty(var1, super.mc.world, var2, var4);
   }

   static int amG(Box var0, Box var1, double var2) {
      return AliceObf_mr.tB(var0, var1, var2);
   }

   public String getInfoString() {
      return PremiumAccessGate.isAllowed() ? this.asX : null;
   }

   }

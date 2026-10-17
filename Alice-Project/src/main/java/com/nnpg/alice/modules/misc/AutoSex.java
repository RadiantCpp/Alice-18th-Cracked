package com.nnpg.alice.modules.misc;

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
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.IntSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.entity.EntityPose;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;

public final class AutoSex extends AliceModule {
   private static final String[] Hs;
   private static final AliceObf_nj Nf = new AliceObf_nj(0, (0), 0.0, 0.0);
   private final SettingGroup afA = this.settings.getDefaultGroup();
   private final Setting<Integer> alr;
   private final Setting<Double> iW;
   private final Setting<AliceObf_ci> jy;
   private final Setting<Integer> zi;
   private final Setting<Integer> aMe;
   private final AliceObf_pt aig;
   private ClientPlayerEntity gm;
   private Object avg;
   private PlayerEntity bP;
   private AliceObf_rs VE;
   private boolean mJ;
   private long oc;
   private boolean aMl;
   private boolean asM;
   private EntityPose rq;
   public AutoSex() {
      super(AliceAddon.SEX_CATEGORY, "AutoSex", "Bam sat sau dit target, sneak va gui /msg theo nhip.");
      this.alr = this.afA
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("target-range")).description("Khoang cach toi da toi muc tieu.")).defaultValue(5))
               .min(1)
               .sliderRange((1), 10)
               .build()
         );
      this.iW = this.afA
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("follow-distance"))
                  .description("Khoang cach bam sat sau dit. Nho hon = di sat hon."))
               .defaultValue(0.48)
               .range(0.28, 1.2)
               .sliderRange(0.28, 1.0)
               .build()
         );
      this.jy = this.afA
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("sex-mode"))
                     .description("Active bam sneak theo nhip, Passive giu sneak lien tuc."))
                  .defaultValue(AliceObf_ci.Ho))
               .build()
         );
      this.zi = this.afA
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("sneak-speed")).description("So lan sneak moi giay. Toi da 10 lan gui that; 11-20 chi ve phia client."))
                     .defaultValue((3)))
                  .range(1, 20)
                  .sliderRange((1), (20))
                  .visible(
                     () -> (this.jy.get()
                           == AliceObf_ci.Ho)
                  ))
               .build()
         );
      this.aMe = this.afA
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("message-delay")).description("Thoi gian cho giua cac /msg, tinh bang giay."))
                  .defaultValue(1))
               .min(0)
               .sliderRange(0, 50)
               .build()
         );
      this.aig = new AliceObf_pt();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.mE();
         this.oc = 0L;
         this.aMl = false;
         this.asM = false;
         this.rq = null;
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.mz();
      this.mE();
      super.onDeactivate();
   }

   @AliceHandler
   private void mk(GameJoinedEvent var1) {
      this.mE();
   }

   @AliceHandler
   private void ml(GameLeftEvent var1) {
      this.mE();
   }

   @AliceHandler
   private void mm(Pre var1) {
      this.my();
      this.mD();
      if (this.gm != super.mc.player || this.avg != super.mc.world) {
         this.mE();
         this.gm = super.mc.player;
         this.avg = super.mc.world;
      }

      this.aig.anB();
      if (this.ms()) {
         ClientPlayerEntity var2 = super.mc.player;
         if (this.bP == null || !this.mt(var2, this.bP)) {
            this.bP = this.mF(var2);
            this.mJ = false;
         }

         if (this.bP != null) {
            boolean var3 = AliceObf_gU.mU(this.mu(var2, this.bP));
            this.mw((boolean)(!var3 && this.mv(var2.age) && !this.aig.anC()));
            long var4 = System.currentTimeMillis();
            if (var4 - this.oc >= ((Integer)this.aMe.get()).intValue() * (1000)) {
               this.oc = var4;
               String var6 = AutoSex.Hs[(int)(
                  Math.random() * AutoSex.Hs.length
               )];
               if (super.mc.getNetworkHandler() != null) {
                  super.mc.getNetworkHandler().sendChatCommand(this.bP.getName().getString() + var6);
               }
            }
         }
      }
   }

   @AliceHandler(
      priority = 35
   )
   private void mn(PlayerTickMovementEvent var1) {
      this.mD();
      ClientPlayerEntity var2 = super.mc.player;
      if (this.ms()) {
         int var10000 = this.bP != null ? 24931 : 24932;

         while (true) {
            int var3 = var10000;
            if (var3 == 24931) {
               if (this.mt(var2, this.bP)) {
                  short var4 = 5719;
               } else {
                  short var5 = 5720;
               }
               break;
            }

            if (var3 != 24932) {
               break;
            }

         }
      }
   }

   @AliceHandler(
      priority = -1100
   )
   private void mo(Post var1) {
      this.mD();
   }

   @AliceHandler
   private void mp(Render3DEvent var1) {
      if (this.ms()
         && this.bP != null
         && this.jy.get() == AliceObf_ci.Ho) {
         if (!AliceObf_gU.mU(this.mu(super.mc.player, this.bP))
            )
          {
            int var2 = (Integer)this.zi.get();
            if (AliceObf_jV.mW(var2)
               > AliceObf_jV.mV(var2)) {
               this.mx(
                  AliceObf_jV.mX(
                     super.mc.player.age + var1.tickDelta,
                     AliceObf_jV.mW(var2)
                  )
               );
            }
         }
      }
   }

   @AliceHandler(
      priority = -1000
   )
   private void mq(Receive var1) {
      if (!var1.isCancelled() && super.mc.player != null && super.mc.world != null && var1.packet instanceof PlayerPositionLookS2CPacket var2) {
         this.aig.anz(var2.teleportId(), 10);
      }
   }

   @AliceHandler
   private void mr(Sent var1) {
      if (var1.packet instanceof TeleportConfirmC2SPacket var2) {
         this.aig.anA(var2.getTeleportId());
      }
   }

   private boolean ms() {
      return (super.mc.player != null && super.mc.world != null && super.mc.currentScreen == null && super.mc.options != null);
   }

   private boolean mt(ClientPlayerEntity var1, PlayerEntity var2) {
      if (var2 != null && var2 != var1) {
         if (var2.isAlive()) {

         } else {
            short var6 = 26114;
         }
      }

      return false;
   }

   private double mu(ClientPlayerEntity var1, PlayerEntity var2) {
      return AliceObf_gU.mT(
         var1.getX(), var1.getZ(), var2.getX(), var2.getZ(), var2.getYaw(), (Double)this.iW.get()
      );
   }

   private boolean mv(int var1) {
      return (boolean)(this.jy.get() != AliceObf_ci.aMO
         ? AliceObf_jV.mX(
            var1, AliceObf_jV.mV((Integer)this.zi.get())
         )
         : 1);
   }

   private void mw(boolean var1) {
      if (super.mc.options.sneakKey.isPressed() != var1) {
         super.mc.options.sneakKey.setPressed(var1);
      }

      this.aMl = true;
   }

   private void mx(boolean var1) {
      ClientPlayerEntity var2 = super.mc.player;
      if (var2 != null) {
         int var10000 = !this.asM ? 13255 : 13256;

         while (true) {
            int var3 = var10000;
            if (var3 == 13255) {
               this.rq = var2.getPose();
               this.asM = true;
               break;
            }

            if (var3 != 13256) {
               break;
            }

         }

         var2.setPose(!var1 ? EntityPose.STANDING : EntityPose.CROUCHING);
      }
   }

   private void my() {
      ClientPlayerEntity var1 = super.mc.player;
      if (this.asM && var1 != null) {
         if (!(this.rq == null)) {
            char var2 = 45319;
         }

         var1.setPose(this.rq);
         this.asM = false;
         this.rq = null;
      } else {
         this.asM = false;
         this.rq = null;
      }
   }

   private void mz() {
      this.my();
      if (this.aMl && super.mc.options != null) {
         super.mc.options.sneakKey.setPressed(false);
         this.aMl = false;
      }
   }

   private boolean mA(ClientPlayerEntity var1) {
      if (var1.isAlive()
         && !var1.isSpectator()
         && !var1.hasVehicle()
         && !var1.isGliding()
         && !var1.getAbilities().flying
         && !var1.isTouchingWater()
         && !var1.isInLava()
         && !var1.isClimbing()
         && !AutoSex.mB(ScaffoldPlusPlus.class)) {
         if (!AutoSex.mB(FreezePlayer.class)) {

         } else {
            short var2 = 12993;
         }
      }

      return false;
   }

   private static boolean mB(Class<? extends Module> var0) {
      Module var1 = Modules.get().get(var0);
      if (!(var1 != null)) {
         short var2 = 28653;
      }

      return false;
   }

   private boolean mC(Input var1) {
      while (var1 instanceof AliceObf_au) {
         AliceObf_au var2 = (AliceObf_au)var1;
         if (!(var2 instanceof AliceObf_rs)) {

         } else {
            char var3 = 60705;
         }

         var1 = var2.Fu();
      }

      return var1 instanceof KeyboardInput;
   }

   private void mD() {
      AliceObf_rs var1 = this.VE;
      this.VE = null;
      if (var1 != null) {
         AliceObf_au.Fy(var1.nN, var1);
      }
   }

   private void mE() {
      this.mD();
      this.aig.anD();
      this.gm = null;
      this.avg = null;
      this.bP = null;
      this.mJ = false;
   }

   private PlayerEntity mF(ClientPlayerEntity var1) {
      double var2 = (double)((Integer)this.alr.get()).intValue() * ((Integer)this.alr.get()).intValue();
      List var4 = super.mc
         .world
         .getPlayers()
         .stream()
         .filter(var1x -> var1x != var1 && var1x.isAlive() && !var1x.isSpectator())
         .filter(var0 -> !(Friends.get().isFriend(var0)))
         .filter(var3 -> (var1.getEntityPos().squaredDistanceTo(var3.getEntityPos()) < var2))
         .sorted(Comparator.comparingDouble(var1x -> var1.getEntityPos().squaredDistanceTo(var1x.getEntityPos())))
         .collect(Collectors.toList());
      if (var4.isEmpty()) {

      } else {
         short var5 = 18781;
      }

      return (PlayerEntity)var4.getFirst();
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         return this.bP == null ? null : this.bP.getName().getString();
      }
   }

   static {

      String[] var10000 = new String[(7)];
      var10000[(0)] = "\u1ed0i \u0111\u1ecbt m\u1eb9 s\u01b0\u1edbng qu\u00e1, c\u1eb7c m\u00e0y nh\u00e9t s\u00e2u v\u00e0o l\u1ed3n tao r\u1ed3i";
      var10000[1] = "A a yamateee, \u0111\u1ecbt m\u1eb9 d\u1eebng l\u1ea1i \u0111i, \u0111ang \u0111\u1ea5m th\u1ee7ng l\u1ed7 tao r\u1ed3i";
      var10000[2] = "Tha taoooo, s\u1eafp b\u1eafn h\u1ebft tinh v\u00e0o trong r\u1ed3i";
      var10000[3] = "\u0110\u1eebng b\u1eafn v\u00e0o trong l\u1ed3n tao nha aaaaa";
      var10000[4] = "B\u01a1m h\u1ebft v\u00e0o t\u1eed cung \u0111i, \u0111\u1ec3 tao \u0111\u1ebb ra \u0111\u00fang m\u1ed9t con ham gi\u1ed1ng m\u00e0y";
      var10000[5] = "\u0110\u1eebng r\u00fat, \u0111\u1ec3 tinh m\u00e0y \u0111\u00f3ng c\u1ee9ng trong x\u00e1c tao";
      var10000[6] = "\u0110\u1ea5m c\u1ea3 khi tao ch\u1ebft, kh\u00f4ng \u0111\u01b0\u1ee3c r\u00fat cho \u0111\u1ebfn l\u00fac ch\u00f4n xu\u1ed1ng \u0111\u1ea5t";
      Hs = var10000;
   }

   }

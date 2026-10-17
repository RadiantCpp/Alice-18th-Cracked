package com.nnpg.alice.modules.combat;

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
import java.util.Iterator;
import meteordevelopment.meteorclient.events.entity.player.AttackEntityEvent;
import meteordevelopment.meteorclient.events.entity.player.DoAttackEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Send;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.mixininterface.IPlayerInteractEntityC2SPacket;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public final class Criticals extends AliceModule {
   private static final float agc = 0.9F;
   private static final double ou = 1.0;
   private static final int aaq = 6;
   private static final int KR = 2;
   private static final int Js = 20;
   private static final int arw = 2;
   private static final int Hw = 3;
   private static final double gS = 0.0625;
   private static final int akp = 3;
   private static final int aEt = 32;
   private final SettingGroup hz = this.settings.getDefaultGroup();
   private final SettingGroup awi;
   private final SettingGroup yN;
   private final Setting<AliceObf_rb> Nt;
   private final Setting<Double> xB;
   private final Setting<Boolean> cA;
   private final Setting<Boolean> arN;
   private final Setting<Integer> dQ;
   private final Setting<Boolean> ji;
   private final Setting<Integer> oz;
   private final Setting<Boolean> aEB;
   private final Setting<Boolean> Wb;
   private final Setting<Boolean> uj;
   private AliceObf_mS Ay;
   private final AliceObf_lz aGo;
   private ClientPlayerEntity WF;
   private Object JI;
   private Entity aGO;
   private long aV;
   private long DT;
   private long fi;
   private long eg;
   private long axf;
   private int PQ;
   private boolean VQ;
   private boolean bx;
   private boolean Gk;
   private int aBZ;
   private boolean iS;
   private int uP;
   private int Eg;
   public Criticals() {
      super(AliceAddon.PVP_CATEGORY, "criticals", "Canh nhay, cooldown va ngat sprint de danh chi mang trong pha roi.");
      this.awi = this.settings.createGroup("Jump");
      this.yN = this.settings.createGroup("Pause");
      this.Nt = this.hz
         .add(((Builder)((Builder)((Builder)new Builder().name("mode")).description("jump: canh nhay vanilla theo nhip danh va cho pha roi. packet: gui cap packet vi tri truoc don danh, co the bi anticheat flag.")).defaultValue(AliceObf_rb.alP)).build());
      this.xB = this.hz
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("range"))
                  .description("chi chuan bi crit khi co muc tieu song trong tam nay, tinh tu mat den hitbox"))
               .defaultValue(4.0)
               .range(1.0, 8.0)
               .sliderRange(1.0, 8.0)
               .build()
         );
      this.cA = this.hz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("require-killaura"))
                     .description("chi chay khi kill-aura++ dang bat. Tat di thi module cung chuan bi crit cho don danh tay va cho trigger-bot."))
                  .defaultValue(true))
               .build()
         );
      this.arN = this.hz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("players-only"))
                     .description("chi chuan bi crit cho nguoi choi, bo qua mob"))
                  .defaultValue(false))
               .build()
         );
      this.dQ = this.awi
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("lead-bias"))
                        .description("nham don danh vao tick thu may cua pha roi. 0 la tick roi dau tien, 2 la giua cua so nen chiu duoc sai so hai tick ve ca hai phia."))
                     .defaultValue(2))
                  .range(0, (4))
                  .sliderRange(0, 4)
                  .visible(this::xn))
               .build()
         );
      this.ji = this.awi
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("hold-attack"))
                        .description("doi them mot so tick khi dang tren khong de danh vao pha roi va du cooldown."))
                     .defaultValue(true))
                  .visible(this::xn))
               .build()
         );
      this.oz = this.awi
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("max-hold"))
                        .description("so tick toi da duoc giu mot don danh. 0 la khong bao gio giu, chi du doan."))
                     .defaultValue((4)))
                  .range((0), (12))
                  .sliderRange(0, 12)
                  .visible(() -> {
                     if (this.xn()) {
                        if ((Boolean)this.ji.get()) {

                        } else {
                           short var1 = 30877;
                        }
                     }

                     return false;
                  }))
               .build()
         );
      this.aEB = this.hz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("sprint-reset"))
                     .description("tam tha phim tien va sprint truoc don danh, giu den khi danh hoac het co hoi. Chi mang can trang thai khong sprint."))
                  .defaultValue(true))
               .build()
         );
      this.Wb = this.yN
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pause-on-use"))
                     .description("khong can thiep khi dang dung item (an tao, gio khien, keo cung)"))
                  .defaultValue(true))
               .build()
         );
      this.uj = this.yN
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pause-on-sneak"))
                     .description("khong nhay khi dang ngoi, tranh roi khoi via hoac hole"))
                  .defaultValue(true))
               .build()
         );
      this.aGo = new AliceObf_lz();
      this.DT = -9223372036854775808L;
      this.fi = -9223372036854775808L;
      this.axf = -9223372036854775808L;
      this.PQ = (6);
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.xt();
         this.xA();
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.xt();
      this.xA();
      super.onDeactivate();
   }

   @AliceHandler(
      priority = 1000
   )
   private void wR(Pre var1) {
      ClientPlayerEntity var2 = super.mc.player;
      if (this.WF != var2 || this.JI != super.mc.world) {
         this.xt();
         this.xA();
         this.WF = var2;
         this.JI = super.mc.world;
      }

      this.aV += 1L;
      this.bx = false;
      this.Gk = false;
      if (var2 != null && super.mc.world != null) {
         this.xb(var2);
         this.xa(var2);
         if (!this.xk(var2)) {
            this.xy();
         } else {
            int var3 = this.xd(var2);
            if (var3 > this.xc() + 3) {
               this.xy();
            } else if (!this.xl(var2)) {
               this.xy();
            } else {
               this.Gk = this.aGo
                  .xJ(
                     true,
                     (Boolean)this.aEB.get(),
                     !(this.xn() && var2.isOnGround() || var3 > 2 && !this.aGo.xI()),
                     var2.isSprinting()
                  );
               if (this.xn()
                  && var2.isOnGround()
                  && var3 > 0
                  && var3 <= this.xc()
                  && this.fi == (0)
                  && this.aV >= this.eg
                  && this.aBZ < (3)) {
                  if (this.xh(var2)) {

                  } else {
                     char var4 = 50746;
                  }
               }
            }
         }
      } else {
         this.xz();
      }
   }

   @AliceHandler
   private void wS(GameJoinedEvent var1) {
      this.xt();
      this.xA();
   }

   @AliceHandler
   private void wT(GameLeftEvent var1) {
      this.xt();
      this.xA();
   }

   @AliceHandler(
      priority = 300
   )
   private void wU(PlayerTickMovementEvent var1) {
      ClientPlayerEntity var2 = super.mc.player;
      if (!(var2 != null)) {
         char var3 = 44802;
      }

      this.xt();
   }

   @AliceHandler(
      priority = 1000
   )
   private void wV(DoAttackEvent var1) {
      this.axf = this.aV;
   }

   @AliceHandler(
      priority = 200
   )
   private void wW(AttackEntityEvent var1) {
      if (!var1.isCancelled()) {
         ClientPlayerEntity var2 = super.mc.player;
         if (!(var2 == null)) {
            char var6 = 56448;
         }

         if (var1.entity instanceof LivingEntity) {
            if (this.xk(var2)) {
               if (!this.xn()) {
                  this.xo(var2);
               } else {
                  int var3 = this.ji.get() && this.axf != this.aV && !var2.isOnGround() ? 1 : (0);
                  if (this.aGo.xH(this.aV, var1.entity.getId(), (Integer)this.oz.get(), this.xi(var2), (var3 != 0))) {
                                          this.iS = !(this.xe() instanceof KillAuraPlusPlus var5 && var5.Wq(var1.entity));
                     var1.cancel();
                  }
               }
            }
         }
      }
   }

   @AliceHandler
   private void wX(Sent var1) {
      if (var1.packet instanceof PlayerInteractEntityC2SPacket var2
         && KillAuraPlusPlus.Wr(var2)
         && super.mc.player != null) {
         if (!(super.mc.world == null)) {
            char var6 = 38276;
         }

         Entity var5 = ((IPlayerInteractEntityC2SPacket)var2).meteor$getEntity();
         if (var5 instanceof LivingEntity) {
            if (this.xk(super.mc.player)) {
                              boolean var4 = this.xn()
                  ? this.xi(super.mc.player)
                  : (Criticals.xj(super.mc.player.getAttackCooldownProgress(0.5F)) && !super.mc.player.isSprinting());
               this.xx(var4);
            }

            this.aGo.xK();
            this.aBZ = 0;
            this.Gk = false;
            this.iS = false;
         }
      }
   }

   @AliceHandler(
      priority = -2000001
   )
   private void wY(Send var1) {
      if (var1.packet instanceof PlayerInteractEntityC2SPacket var2) {
         int var10000 = !KillAuraPlusPlus.Wr(var2) ? 11768 : 11767;

         while (true) {
            int var4 = var10000;
            if (var4 == 11767) {
               if (var1.isCancelled()) {
                  this.aGo.xL();
                  this.Gk = false;
                  this.iS = false;
                  return;
               }
               break;
            }

            if (var4 != 11768) {
               break;
            }

         }
      }

      if (this.iS) {
         if (var1.packet instanceof HandSwingC2SPacket var5) {
            if (var5.getHand() == Hand.MAIN_HAND) {
               this.iS = false;
               if (!var1.isCancelled()) {
                  var1.cancel();
               }
            }
         }
      }
   }

   @AliceHandler(
      priority = -1000
   )
   private void wZ(Post var1) {
      this.bx = false;
      this.Gk = false;
      this.iS = false;
   }

   private void xa(ClientPlayerEntity var1) {
      if (!(!this.VQ)) {
         char var3 = 33977;
      }
   }

   private void xb(ClientPlayerEntity var1) {
      if (this.fi != (0)) {
         if (!var1.isOnGround()) {
            this.DT = this.fi;
            this.fi = -9223372036854775808L;
            this.VQ = false;
            this.aBZ++;
         } else if (this.aV - this.fi >= (3)) {
            this.fi = -9223372036854775808L;
            this.eg = this.aV + (10);
            this.aBZ++;
         }
      }
   }

   private int xc() {
      return this.PQ + (1) + (Integer)this.dQ.get();
   }

   private int xd(ClientPlayerEntity var1) {
      if (this.xe() instanceof KillAuraPlusPlus var9) {
         return var9.Ww();
      } else {
         float var3 = var1.getAttackCooldownProgressPerTick();
         if (var3 <= 0.0F) {
            return 0;
         } else {
            double var4 = this.xf();
            double var6 = var1.getAttackCooldownProgress(0.5F);
            if (var6 >= var4) {
               return (0);
            } else {
               int var8 = (int)Math.ceil((var4 - var6) * var3 - 1.0E-6);
               return Math.max((0), var8);
            }
         }
      }
   }

   private Module xe() {
      Module var1 = Modules.get().get(KillAuraPlusPlus.class);
      if (var1 != null) {
         if (var1.isActive()) {

         } else {
            char var2 = 60063;
         }
      }

      return null;
   }

   private double xf() {
      Module var1 = this.xe();
      if (!(var1 == null)) {
         short var4 = 1253;
      }

      if (Boolean.TRUE
         .equals(
            Criticals.settingValue(
               var1, "bypass-ac"
            )
         )) {
         return 1.0;
      } else {
         return Criticals.settingValue(
               var1, "cooldown-threshold"
            ) instanceof Double var3
            ? Math.max(0.9F, var3)
            : 1.0;
      }
   }

   private boolean xg() {
      Module var1 = this.xe();
      if (var1 == null) {
         return false;
      } else {
         if (Boolean.TRUE
            .equals(
               Criticals.settingValue(
                  var1,
                  "bypass-ac"
               )
            )) {

         } else {
            short var4 = 12825;
         }

         return (Criticals.settingValue(
                  var1,
                  "cooldown-threshold"
               ) instanceof Double var3
               && var3 <= 0.9F);
      }
   }

   private static Object settingValue(Module var0, String var1) {
      try {
         Setting var2 = var0.settings.get(var1);
         return var2 == null ? null : var2.get();
      } catch (RuntimeException var3) {
         return null;
      }
   }

   private boolean xh(ClientPlayerEntity var1) {
      if (!(var1.fallDistance > 0.0)) {
         short var2 = 18831;
      }

      return false;
   }

   private boolean xi(ClientPlayerEntity var1) {
      return (this.xh(var1)
            && Criticals.xj(var1.getAttackCooldownProgress(0.5F)));
   }

   static boolean xj(float var0) {
      return (var0 > 0.9F);
   }

   private boolean xk(ClientPlayerEntity var1) {
      if ((Boolean)this.cA.get() && this.xe() == null) {
         return false;
      } else if (super.mc.world != null && super.mc.currentScreen == null) {
         if (this.xe() instanceof KillAuraPlusPlus var3 && !var3.Wx()) {
            return false;
         } else if (this.xn() && this.xB()) {
            return false;
         } else if (!var1.isAlive() || var1.isSpectator()) {
            return false;
         } else if (var1.hasVehicle() || var1.isGliding()) {
            return false;
         } else if (var1.getAbilities().flying) {
            return false;
         } else if (var1.isTouchingWater() || var1.isInLava()) {
            return false;
         } else if (var1.isClimbing()) {
            return false;
         } else if (var1.hasStatusEffect(StatusEffects.BLINDNESS)) {
            return false;
         } else {
            if ((Boolean)this.Wb.get()) {
               if (var1.isUsingItem()) {
                  return false;
               }

               int var10000 = super.mc.options.useKey.isPressed() ? 47776 : 47777;

               while (true) {
                  int var4 = var10000;
                  if (var4 == 47776) {
                     return false;
                  }

                  if (var4 != 47777) {
                     break;
                  }

               }
            }

            return !(this.uj.get() && (var1.isSneaking() || super.mc.options.sneakKey.isPressed()));
         }
      } else {
         return false;
      }
   }

   private boolean xl(ClientPlayerEntity var1) {
      double var2 = (Double)this.xB.get();
      double var4 = var2 * var2;
      Module var6 = this.xe();
      Object var7 = null;
      if (var6 instanceof KillAuraPlusPlus var8) {
         PlayerEntity var9 = var8.Wv();
         if (var9 != null && this.xm(var9)) {
            if (KillAuraPlusPlus.Xv(
                  var1.getEyePos(), var9.getBoundingBox()
               )
               <= var4) {

            } else {
               char var13 = 47250;
            }
         }
      } else {
         Box var12 = var1.getBoundingBox().expand(var2);
         Iterator var10 = super.mc.world.getOtherEntities(var1, var12, this::xm).iterator();
         if (var10.hasNext()) {
            char var14 = 55744;
         } else {
            char var15 = 55745;
         }
      }

      if (this.aGO != var7) {
         this.aGO = (Entity)var7;
         this.aGo.xM();
         this.aBZ = 0;
      }

      return (var7 != null);
   }

   private boolean xm(Entity var1) {
      if (!(var1 instanceof LivingEntity var2)) {
         return false;
      } else if (!var2.isAlive() || var2.isRemoved()) {
         return false;
      } else if (!(var2 instanceof PlayerEntity var3)) {
         return (!this.arN.get());
      } else {
         if (!var3.isCreative()) {
            int var10000 = !var3.isSpectator() ? 8312 : 8313;

            while (true) {
               int var4 = var10000;
               if (var4 == 8312) {
                  if (Friends.get().shouldAttack(var3)) {
                     return true;
                  }
                  break;
               }

               if (var4 != 8313) {
                  break;
               }

            }
         }

         return false;
      }
   }

   private boolean xn() {
      return !(this.Nt.get() != AliceObf_rb.alP);
   }

   private void xo(ClientPlayerEntity var1) {
      if (super.mc.getNetworkHandler() != null) {
         if (var1.isOnGround()) {
            double var2 = var1.getX();
            double var4 = var1.getY();
            double var6 = var1.getZ();
            boolean var8 = var1.horizontalCollision;
            super.mc.getNetworkHandler().sendPacket(new PositionAndOnGround(var2, var4 + 0.0625, var6, false, var8));
            super.mc.getNetworkHandler().sendPacket(new PositionAndOnGround(var2, var4, var6, false, var8));
         }
      }
   }

   private boolean xp(ClientPlayerEntity var1) {
      int var10000 = this.Ay != null ? 4214 : 4215;

      while (true) {
         int var4 = var10000;
         if (var4 == 4214) {
            if (!this.Ay.Fw()) {
               Input var2 = var1.input;

               while (var2 instanceof AliceObf_au) {
                  AliceObf_au var3 = (AliceObf_au)var2;
                  if (var3 == this.Ay) {
                     return true;
                  }

                  var2 = var3.Fu();
               }

               return false;
            }
            break;
         }

         if (var4 != 4215) {
            break;
         }

      }

      return false;
   }

   public boolean xq(Input var1) {
      return (var1 == this.Ay && this.Ay != null && !this.Ay.Fw());
   }

   boolean xr() {
      return (boolean)(this.isActive() && this.Gk);
   }

   static PlayerInput xs(PlayerInput var0) {
      Criticals var1 = (Criticals)Modules.get().get(Criticals.class);
      return var1 != null && var1.xr()
         ? Criticals.xv(var0)
         : var0;
   }

   private void xt() {
      AliceObf_mS var1 = this.Ay;
      this.Ay = null;
      if (var1 != null) {
         AliceObf_au.Fy(var1.FI, var1);
      }
   }

   private static PlayerInput xu(PlayerInput var0) {
      return var0.jump()
         ? var0
         : new PlayerInput(
            var0.forward(),
            var0.backward(),
            var0.left(),
            var0.right(),
            true,
            var0.sneak(),
            var0.sprint()
         );
   }

   private static PlayerInput xv(PlayerInput var0) {
      return !var0.forward() && !var0.sprint()
         ? var0
         : new PlayerInput(
            false, var0.backward(), var0.left(), var0.right(), var0.jump(), var0.sneak(), false
         );
   }

   private static Vec2f xw(PlayerInput var0) {
      float var1 = (!var0.left() ? 0.0F : 1.0F) - (var0.right() ? 1.0F : 0.0F);
      float var2 = (!var0.forward() ? 0.0F : 1.0F) - (var0.backward() ? 1.0F : 0.0F);
      return new Vec2f(var1, var2).normalize();
   }

   private void xx(boolean var1) {
      if (this.uP >= (32)) {
         this.uP /= (2);
         this.Eg /= (2);
      }

      this.uP++;
      if (var1) {
         this.Eg++;
      }
   }

   private void xy() {
      this.bx = false;
      this.Gk = false;
      this.aGo.xM();
      this.aGO = null;
      this.aBZ = (0);
   }

   private void xz() {
      this.DT = -9223372036854775808L;
      this.fi = -9223372036854775808L;
      this.eg = 0L;
      this.axf = -9223372036854775808L;
      this.VQ = false;
      this.aGo.xM();
      this.aGO = null;
      this.aBZ = (0);
      this.iS = false;
   }

   private void xA() {
      this.xz();
      this.WF = null;
      this.JI = null;
      this.aV = 0L;
      this.PQ = (6);
      this.bx = false;
      this.Gk = false;
      this.uP = (0);
      this.Eg = 0;
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else if ((Boolean)this.cA.get() && this.xe() == null) {
         return "aura off";
      } else if (this.xn() && this.xB()) {
         return "Sprint Rage";
      } else if (this.xg() && !(Boolean)this.ji.get()) {
         return "aura cd<0.9";
      } else if (this.uP == 0) {
         return !this.xn()
            ? "packet"
            : "jump";
      } else {
         int var1 = this.Eg * (100) / this.uP;
         return (
               this.xn()
                  ? "jump ready "
                  : "packet ready "
            )
            + var1;
      }
   }

      private boolean xB() {
      Module var1 = Modules.get().get("sprint");
      return (var1 != null && var1.isActive() ? Criticals.settingValue(var1, "sprint-mode") : null) instanceof Enum var3
         && var3.name().equals("Rage");
   }

   }

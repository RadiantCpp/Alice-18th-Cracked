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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.mixininterface.IPlayerInteractEntityC2SPacket;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.utils.entity.DamageUtils;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerEntity;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import net.minecraft.util.PlayerInput;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class KillAuraPlusPlus extends AliceModule implements AliceObf_jQ {
   private static final double nK = 1.0E-4;
   private static final float Pz = 1.0F;
   private static final double yD = 3.0;
   private static final int axU = 3;
   private static final double Oo = 0.07;
   private static final int adP = 2;
   private static final int aiR = 5;
   private static final double Wi = 0.9;
   private final SettingGroup Gu = this.settings.getDefaultGroup();
   private final SettingGroup abC;
   private final SettingGroup nc;
   private final SettingGroup awh;
   private final SettingGroup ayj;
   private final Setting<AliceObf_fu> te;
   private final Setting<AliceObf_dF> Tq;
   private final Setting<Double> Ra;
   private final Setting<AliceObf_tV> aKn;
   private final Setting<Boolean> XD;
   private final Setting<Double> xR;
   private final Setting<Double> CJ;
   private final Setting<Boolean> Xv;
   private final Setting<Boolean> aaQ;
   private final Setting<Double> Pg;
   private final Setting<Double> Mc;
   private final Setting<Boolean> anY;
   private final AliceObf_pn NK;
   private final Random Dj;
   private final AliceObf_uT SB;
   private final AliceObf_n Dd;
   private final AliceObf_sD ahy;
   private final AliceObf_iG dV;
   private final AliceObf_ln aGl;
   private final AliceObf_gH ds;
   private PlayerEntity aHO;
   private boolean Tp;
   private PlayerEntity pG;
   private boolean aBx;
   private float amd;
   private float cG;
   private boolean FR;
   private Vec3d wq;
   private Vec3d anZ;
   private ClientWorld kd;
   private ClientPlayerEntity Ax;
   private AliceObf_om apU;
   private AliceObf_lH aud;
   private long aBu;
   private AliceObf_uF aAy;
   private boolean adB;
   private boolean dD;
   private boolean kU;
   private boolean aGv;
   public KillAuraPlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "kill-aura++", "Tu dong tan cong theo hoi chieu phia may khach voi tam danh, goc xoay va phoi hop Crystal Aura++.");
      this.abC = this.settings.createGroup("Targeting");
      this.nc = this.settings.createGroup("Rotation");
      this.awh = this.settings.createGroup("Bypass");
      this.ayj = this.settings.createGroup("Pause");
      this.te = this.Gu.add(((Builder)((Builder)((Builder)new Builder().name("weapon-swap")).description("none: danh bang do dang cam, pick: cam do ngon nhat, silent: chi doi slot tren server")).defaultValue(AliceObf_fu.QQ)).build());
      this.Tq = this.Gu.add(((Builder)((Builder)((Builder)new Builder().name("raycast-mode")).description("grim chi yeu cau tia chieu vao hitbox cua thuc: the. visible con yeu cau tia khong bi chan boi the gioi")).defaultValue(AliceObf_dF.aul)).build());
      this.Ra = this.abC
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("attack-range"))
                  .description("gioi han tam danh cuc bo ap dung cung voi he thong cua minecraft 1.21.11, khong bu tru"))
               .defaultValue(3.0)
               .range(1.0, 6.0)
               .sliderRange(1.0, 6.0)
               .build()
         );
      this.aKn = this.abC.add(((Builder)((Builder)((Builder)new Builder().name("target-mode")).description("chon mot che do uu tien danh muc tieu nhat dinh")).defaultValue(AliceObf_tV.axt)).build());
      this.XD = this.abC
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("lock-target"))
                     .description("giu nguyen muc tieu da chon cho den khi khong con hop le"))
                  .defaultValue(true))
               .build()
         );
      this.xR = this.nc
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("yaw-step"))
                  .description("goc yaw thay doi toi da tren server moi chu ky"))
               .defaultValue(180.0)
               .range(1.0, 180.0)
               .sliderRange(1.0, 180.0)
               .build()
         );
      this.CJ = this.nc
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("pitch-step"))
                  .description("goc pitch thay doi toi da tren server moi chu ky"))
               .defaultValue(90.0)
               .range(1.0, 90.0)
               .sliderRange(1.0, 90.0)
               .build()
         );
      this.Xv = this.ayj
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pause-on-use"))
                     .description("tam dung khi dang dung item de tranh xung dot packet"))
                  .defaultValue(true))
               .build()
         );
      this.aaQ = this.ayj
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pause-on-mine"))
                     .description("tam dung khi giu nut danh de tranh xung dot"))
                  .defaultValue(true))
               .build()
         );
      this.Pg = this.Gu
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("cooldown-threshold"))
                  .description("Nguong hoi chieu theo toc do tan cong cua vu khi. 1.0 la hoi day du; gia tri thap hon danh som hon voi sat thuong moi don thap hon."))
               .defaultValue(0.85)
               .range(0.5, 1.0)
               .sliderRange(0.5, 1.0)
               .build()
         );
      this.Mc = this.abC
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("walls-range"))
                  .description("tam danh xuyen tuong toi da (gioi han truoc khi vao raycast gate)"))
               .defaultValue(3.0)
               .range(0.0, 6.0)
               .sliderRange(0.0, 6.0)
               .build()
         );
      this.anY = this.awh
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("bypass-ac"))
                     .description("Quay muot, doi diem ngam tren co the, gioi han tam 3.0 va kiem tra tia ngam theo Minecraft. Cho hoi chieu day du va them tre ngan. Khong dam bao qua moi anticheat."))
                  .defaultValue(false))
               .build()
         );
      this.NK = new AliceObf_pn();
      this.Dj = new Random();
      this.SB = new AliceObf_uT();
      this.Dd = new AliceObf_n();
      this.ahy = new AliceObf_sD(this.Dj);
      this.dV = new AliceObf_iG();
      this.aGl = new AliceObf_ln();
      this.ds = new AliceObf_gH();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.Xn();
         this.ds.install();
         AliceObf_fS.Od(this);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.Xi();
      AliceObf_fS.Oe(this);
      this.Xn();
      super.onDeactivate();
   }

   @AliceHandler(
      priority = 2000
   )
   private void Wf(Pre var1) {
      label59: {
         if (this.kd == this.mc.world) {
            int var10000 = this.Ax != this.mc.player ? 60234 : 60235;

            while (true) {
               int var2 = var10000;
               if (var2 == 60234) {
                  break;
               }

               if (var2 != 60235) {
                  break label59;
               }

            }
         }

         this.Xn();
      }

      if (this.kU != this.Xo()) {
         this.Xn();
      }
   }

   @AliceHandler(
      priority = 100
   )
   private void Wg(PlayerTickMovementEvent var1) {
      if (!AliceObf_fS.OA(this)) {
         if (!AliceObf_fS.OD(this)) {
            this.suspendForItemUse();
         }
      } else {
         if (this.Xo()) {
            if (!(!this.dD)) {
               short var2 = 21086;
            }
         }

         if (this.Xo() && this.apU == null) {
            this.WT();
         }

         this.Xh();
      }
   }

   @AliceHandler(
      priority = -200
   )
   private void Wh(Post var1) {
      this.Xj();
      this.Wk();
      this.aBu++;
      this.Xi();
      this.NK.Rk();
   }

   @AliceHandler(
      priority = -1000
   )
   private void Wi(Sent var1) {
      if (KnockbackDirection.Yu(var1.packet)) {
         this.aGv = true;
         this.Wj((PlayerMoveC2SPacket)var1.packet);
      } else {
         if (this.aHO != null
            && var1.packet instanceof PlayerInteractEntityC2SPacket var2
            && ((IPlayerInteractEntityC2SPacket)var2).meteor$getEntity() == this.aHO
            && KillAuraPlusPlus.Wr(var2)) {
            this.Tp = true;
            CrystalAuraPlusPlus.zr(this.aHO);
         }

         if (var1.packet instanceof PlayerMoveC2SPacket var4) {
            this.Wj(var4);
            if (!var4.changesLook()) {

            } else {
               char var7 = 59704;
            }

            boolean var6 = this.aAy != null
               && AliceObf_fS.Qr(
                  this.amd, this.cG, this.aAy.XF(), this.aAy.XG()
               );
            this.amd = var4.getYaw(this.amd);
            this.cG = var4.getPitch(this.cG);
            this.FR = true;
            if (this.aud != null && this.aud.CP) {
               AliceObf_fS.Oz(this, this.amd, this.cG);
               this.adB = var6
                  && this.aAy != null
                  && AliceObf_fS.Qr(
                     this.amd, this.cG, this.aAy.XH(), this.aAy.XI()
                  );
            } else {
               this.Dd.XM();
               this.adB = false;
               this.ds.VK();
            }
         }
      }
   }

   private void Wj(PlayerMoveC2SPacket var1) {
      if (var1.changesPosition()) {
         this.wq = new Vec3d(var1.getX(0.0), var1.getY(0.0), var1.getZ(0.0));
      }

      if (this.wq != null && this.mc.player != null) {
         double var2 = this.mc.player.getEyePos().y - this.mc.player.getEntityPos().y;
         this.anZ = this.wq.add(0.0, var2, 0.0);
      }
   }

   private void Wk() {
      if (this.aGv) {
         this.aGv = false;
         this.aAy = null;
         this.adB = false;
      } else {
         if (this.dD
            && this.aAy != null
            && !this.aAy.XE()
            && this.aud != null
            && this.aud.CP
            && AliceObf_fS.Qr(this.amd, this.cG, this.aAy.XH(), this.aAy.XI())) {
            this.dD = false;
         }

         if (this.aAy != null && this.adB) {
            if (AliceObf_fS.Qr(
               this.amd, this.cG, this.aAy.XH(), this.aAy.XI()
            )) {

            } else {
               short var1 = 26266;
            }
         }

         if (this.Xo() && this.aAy != null && this.aAy.XE() && this.aud != null && this.aud.CP) {
            this.dD = true;
         }

         this.Dd.XM();
         this.aAy = null;
         this.adB = false;
      }
   }

   private void Wl() {
      this.Dd.XM();
      this.aAy = new AliceObf_uF(this.amd, this.cG, this.amd, this.cG);
      this.apU = new AliceObf_om(this.amd, this.cG);
   }

   public AliceObf_y prepareCombatIntent() {
      this.aBx = false;
      this.Xi();
      this.apU = null;
      if (this.te.get() != AliceObf_fu.nx && AliceObf_fS.Oy(this)) {
         return this.Wo();
      } else if (!this.Ws()) {
         this.Xm();
         return !AliceObf_fS.Oy(this) ? null : this.Wo();
      } else {
         this.pG = this.WD();
         CrystalAuraPlusPlus.zq(this.pG);
         int var10000 = this.pG == null ? 12181 : 12182;

         while (true) {
            int var4 = var10000;
            if (var4 == 12181) {
               this.NK.Rn();
               return AliceObf_fS.Oy(this) ? this.Wo() : null;
            }

            if (var4 != 12182) {
               this.aBx = true;
               int var1 = this.WJ(this.pG);
               if (this.WK(var1)) {
                  return this.Wm(var1);
               }

               if (this.Xq()) {
                  Vec3d var2 = this.WV(this.pG);
                  double[] var3 = Xw(var2);
                  return this.Wn(this.pG, var1, var3[0], var3[1]);
               }

               this.aBx = this.aAy != null || this.dD;
               this.NK.Rn();
               return null;
            }

         }
      }
   }

   public void suspendForItemUse() {
      this.aBx = false;
      this.Xi();
      this.Xl();
      this.apU = null;
      this.NK.Rn();
      this.Dd.XM();
      this.aAy = null;
      this.adB = false;
   }

   public boolean supportsMiningHandoff() {
      return true;
   }

   public boolean canYieldForMining() {
      if (!(this.aHO == null)) {
         char var1 = 36972;
      }

      return false;
   }

   public void yieldForMining() {
      this.aBx = false;
      this.Xi();
      this.Xl();
      this.apU = null;
      this.NK.Rn();
      this.Dd.XM();
      this.aAy = null;
      this.adB = false;
      this.aGv = false;
   }

   public boolean canYieldAfterExecution() {
      return this.apU == null && this.aAy == null && this.aHO == null;
   }

   public boolean usesExclusiveCombatTurn() {
      return false;
   }

   public boolean sharesParallelCombatEpoch() {
      return true;
   }

   public boolean hasExclusiveCombatWork() {
      return this.aBx;
   }

   public int combatPriority() {
      return 150;
   }

   private AliceObf_y Wm(int var1) {
      return new AliceObf_od(this, var1);
   }

   private AliceObf_y Wn(PlayerEntity var1, int var2, double var3, double var5) {
      return new AliceObf_x(this, var1, var2, var3, var5);
   }

   private AliceObf_y Wo() {
      return new AliceObf_dG(this);
   }

   private void Wp(PlayerEntity var1, int var2, double var3, double var5) {
      if (this.pG != var1 || !this.WE(var1) || this.WK(var2) || !this.Xq()) {
         this.NK.Rn();
      } else if (this.WM(var1, var3, var5)) {
         if (!KnockbackDirection.Yf(var1)) {
            if (AliceObf_fS.OE(this)) {
               this.aHO = var1;
               this.Tp = false;

               try {
                  this.mc.interactionManager.attackEntity(this.mc.player, var1);
               } finally {
                  this.aHO = null;
               }

               if (this.Tp) {
                  this.mc.player.swingHand(Hand.MAIN_HAND);
                  this.Xr();
               }
            }
         }
      }
   }

   public boolean Wq(Entity var1) {
      return this.aHO == var1 && this.aHO != null;
   }

   public static boolean Wr(PlayerInteractEntityC2SPacket var0) {
      boolean[] var1 = new boolean[]{false};
      var0.handle(new AliceObf_sS(var1));
      return var1[0];
   }

   private boolean Ws() {
      if (this.mc.player != null && this.mc.world != null) {
         int var10000 = this.mc.interactionManager != null ? 40057 : 40058;

         while (true) {
            int var1 = var10000;
            if (var1 == 40057) {
               if (this.mc.getNetworkHandler() != null && this.mc.player.isAlive() && !this.mc.player.isCreative() && !this.mc.player.isSpectator()
                  )
                {
                  if (!this.mc.player.hasVehicle()) {
                     short var2 = 9532;
                  } else {
                     short var3 = 9533;
                  }
               }
               break;
            }

            if (var1 != 40058) {
               break;
            }

         }
      }

      return false;
   }

   public PlayerEntity Wt() {
      return this.isActive() && this.Ws() && this.WE(this.pG) ? this.pG : null;
   }

   double Wu() {
      return this.Xp();
   }

   PlayerEntity Wv() {
      return this.isActive() && this.Ws() ? this.WD() : null;
   }

   int Ww() {
      if (this.mc.player == null) {
         return Integer.MAX_VALUE;
      } else {
         double var1 = this.mc.player.getAttackCooldownProgressPerTick();
         double var3 = this.Xs();
         int var5 = var1 > 0.0 ? (int)Math.max(0.0, Math.ceil((1.0 - var3) * var1 - 1.0E-6)) : 0;
         return this.Xo() ? this.SB.Vw(this.aBu, var5) : var5;
      }
   }

   boolean Wx() {
      return this.isActive() && this.Ws();
   }

   public PlayerEntity Wy() {
      if (this.isActive() && this.Ws() && AliceObf_fS.OA(this)) {
         int var10000 = !this.WE(this.pG) ? 65371 : 65370;

         while (true) {
            int var3 = var10000;
            if (var3 == 65370) {
               if (this.Xb(this.pG) && !this.WK(this.WJ(this.pG))) {
                  float var1 = this.mc.player.getAttackCooldownProgress(1.5F) + 1.0E-4F;
                  boolean var2 = !this.Xo() ? var1 >= ((Double)this.Pg.get()).floatValue() : var1 >= 1.0F && this.SB.Vv(this.aBu);
                  return var2 ? this.pG : null;
               }
               break;
            }

            if (var3 != 65371) {
               break;
            }

         }
      }

      return null;
   }

   PlayerEntity Wz(PlayerEntity var1, double var2) {
      if (!this.Wx()) {
         return null;
      } else {
         double var4 = KillAuraPlusPlus.WA(var2);
         if (var4 <= 0.0) {
            return null;
         } else if (this.WB(this.pG, var4) && this.WE(this.pG)) {
            return this.pG;
         } else if (this.WB(var1, var4)) {
            return var1;
         } else {
            PlayerEntity var6 = null;

            for (PlayerEntity var8 : this.mc.world.getPlayers()) {
               if (this.WB(var8, var4)
                  && (!this.Xo() && this.Tq.get() != AliceObf_dF.lY || this.Xg(this.WZ(var8)))
                  && (var6 == null || this.WG(var8, var6) < 0)) {
                  var6 = var8;
               }
            }

            return var6;
         }
      }
   }

   static double WA(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, 0.0, 8.0) : 0.0;
   }

   private boolean WB(PlayerEntity var1, double var2) {
      if (this.isAllowedTarget(var1)) {

      } else {
         char var4 = 54869;
      }

      return false;
   }

   public boolean WC(Input var1) {
      if (var1 instanceof AliceObf_lH var2) {
         int var10000 = !var2.Fw() ? 29750 : 29751;

         while (true) {
            int var3 = var10000;
            if (var3 == 29750) {
               return true;
            }

            if (var3 != 29751) {
               break;
            }

         }
      }

      return false;
   }

   private PlayerEntity WD() {
      if ((Boolean)this.XD.get() && this.WE(this.pG) && this.WF(this.pG)) {
         return this.pG;
      } else {
         ArrayList var1 = new ArrayList();

         for (PlayerEntity var3 : this.mc.world.getPlayers()) {
            if (this.WE(var3) && this.WF(var3)) {
               var1.add(var3);
            }
         }

         ((List<PlayerEntity>)(List<?>)var1).sort(this::WG);
         return var1.isEmpty() ? null : (PlayerEntity)var1.getFirst();
      }
   }

   private boolean isAllowedTarget(PlayerEntity var1) {
      return var1 != null
            && var1 != this.mc.player
            && var1.isAlive()
            && !var1.isRemoved()
            && !var1.isCreative()
            && !var1.isSpectator()
            && Friends.get().shouldAttack(var1)
         ? !(var1 instanceof FakePlayerEntity var2 && var2.noHit)
         : false;
   }

   private boolean WE(PlayerEntity var1) {
      if (this.isAllowedTarget(var1)) {
         Box var2 = this.WZ(var1);
         if (this.mc.player.canAttackEntityIn(var2, 0.0)) {
            double var3 = this.Xp();
            double var5 = Xv(this.mc.player.getEyePos(), var2);
            if (var5 > var3 * var3) {
               return false;
            } else if (this.Xo()) {
               return Xv(this.mc.player.getEyePos(), this.Xa(var1)) <= var3 * var3;
            } else {
               if (this.Tq.get() == AliceObf_dF.lY) {
                  boolean var7 = this.Xg(var2);
                  if (!var7 && var5 > (Double)this.Mc.get() * (Double)this.Mc.get()) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean WF(PlayerEntity var1) {
      if (this.Xo()) {
         int var10000 = this.WW(var1, null) != null ? 63315 : 63316;

         while (true) {
            int var2 = var10000;
            if (var2 == 63315) {
               break;
            }

            if (var2 != 63316) {
               return false;
            }

         }
      }

      return true;
   }

   private int WG(PlayerEntity var1, PlayerEntity var2) {
      int var3 = switch (((AliceObf_tV)this.aKn.get()).ordinal() ^ 1071029223) {
         case 1071029219 -> Integer.compare(var2.getArmor(), var1.getArmor());
         case 1071029220 -> Integer.compare(var1.getArmor(), var2.getArmor());
         case 1071029221 -> Double.compare(this.WI(var1), this.WI(var2));
         case 1071029222 -> Double.compare(this.WH(var1), this.WH(var2));
         case 1071029223 -> Float.compare(
            Xx(var1), KillAuraPlusPlus.Xx(var2)
         );
         default -> throw new MatchException(null, null);
      };
      if (var3 != 0) {
         short var6 = 14586;
      } else {
         short var7 = 14587;
      }

      var3 = Double.compare(this.WH(var1), this.WH(var2));
      int var10001 = var3 != 0 ? 60380 : 60381;

      while (true) {
         int var4 = var10001;
         if (var4 == 60380) {
            var10001 = var3;
            break;
         }

         if (var4 != 60381) {
            var10001 = Integer.compare(var1.getId(), var2.getId());
            break;
         }

      }

      return var10001;
   }

   private double WH(PlayerEntity var1) {
      return Xv(this.mc.player.getEyePos(), this.WZ(var1));
   }

   private double WI(PlayerEntity var1) {
      double[] var2 = KillAuraPlusPlus.Xw(this.WZ(var1).getCenter());
      float var3 = this.FR
         ? this.amd
         : (
            AliceObf_fS.OK()
               ? AliceObf_fS.OI()
               : this.mc.player.getYaw()
         );
      float var4 = this.FR
         ? this.cG
         : (
            AliceObf_fS.OK()
               ? AliceObf_fS.OJ()
               : this.mc.player.getPitch()
         );
      double var5 = MathHelper.wrapDegrees(var2[0] - var3);
      double var7 = var2[1] - var4;
      return var5 * var5 + var7 * var7;
   }

   private int WJ(PlayerEntity var1) {
      if (this.te.get() == AliceObf_fu.QQ) {
         return this.mc.player.getInventory().getSelectedSlot();
      } else {
         int var2 = this.mc.player.getInventory().getSelectedSlot();
         if (CrystalAuraPlusPlus.zn()) {
            int var3 = -1;
            float var4 = Float.NEGATIVE_INFINITY;

            for (int var5 = 0; var5 < 9; var5++) {
               ItemStack var6 = this.mc.player.getInventory().getStack(var5);
               if (var6.isIn(ItemTags.SWORDS)) {
                  float var7 = DamageUtils.getAttackDamage(this.mc.player, var1, var6);
                  if (var7 > var4 || var7 == var4 && var5 == var2) {
                     var3 = var5;
                     var4 = var7;
                  }
               }
            }

            if (var3 >= 0) {
               return var3;
            }
         }

         int var10 = var2;
         float var11 = DamageUtils.getAttackDamage(this.mc.player, var1, this.mc.player.getInventory().getStack(var2));
         int var12 = 0;

         while (true) {
            int var10000 = var12 < 9 ? 22856 : 22857;

            while (true) {
               int var8 = var10000;
               if (var8 == 22856) {
                  ItemStack var13 = this.mc.player.getInventory().getStack(var12);
                  float var14 = DamageUtils.getAttackDamage(this.mc.player, var1, var13);
                  var10000 = var14 > var11 + 1.0E-4F ? 4065 : 4066;

                  while (true) {
                     int var9 = var10000;
                     if (var9 == 4065) {
                        var11 = var14;
                        var10 = var12;
                        break;
                     }

                     if (var9 != 4066) {
                        break;
                     }

                  }

                  var12++;
                  break;
               }

               if (var8 != 22857) {
                  return var10;
               }

            }
         }
      }
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      private boolean WK(int var1) {
      if (var1 >= 0 && var1 <= 8) {
         int var2 = this.mc.player.getInventory().getSelectedSlot();
         int var3 = AliceObf_fS.OL();
         switch (((AliceObf_fu)this.te.get()).ordinal()) {
            case 0:
               if (AliceObf_fS.Oy(this)) {
                  return false;
               }
               return var3 != var2;
            case 1:
               return !(var2 == var1 && var3 == var1);
            case 2:
               return var3 != var1;
            default:
               throw new MatchException(null, null);
         }
      } else {
         return true;
      }
   }

   private void WL(int var1) {
      if (var1 >= 0 && var1 <= 8) {
         int var10000 = this.mc.player == null ? 64935 : 64936;

         while (true) {
            int var3 = var10000;
            if (var3 == 64935) {
               break;
            }

            if (var3 != 64936) {
               int var2 = this.mc.player.getInventory().getSelectedSlot();
               int var4 = ((AliceObf_fu)this.te.get()).ordinal() ^ -1845897580;
               if (var4 == -1845897580) {
                  AliceObf_fS.Of(this, var2);
               } else if (var4 == -1845897579) {
                  if (!AliceObf_fS.Of(this, var1) && var2 != var1) {
                     InvUtils.swap(var1, false);
                  }
               } else if (var4 == -1845897578) {
                  AliceObf_fS.Of(this, var1);
               }

               this.NK.Rn();
               return;
            }

         }
      }
   }

   private boolean WM(PlayerEntity var1, double var2, double var4) {
      if (!this.Xo()) {
         if (this.Xb(var1)) {
            return true;
         } else if (!this.WN(var2, var4)) {
            this.NK.Rn();
            return false;
         } else if (this.NK.Rm(var1.getId())) {
            boolean var7 = this.Xb(var1);
            if (!var7) {
               this.NK.Rn();
            }

            return var7;
         } else {
            return false;
         }
      } else {
         boolean var6 = this.Xb(var1);
         this.WN(var2, var4);
         if (!var6) {
            this.NK.Rn();
            return false;
         } else {
            return true;
         }
      }
   }

   private boolean WN(double var1, double var3) {
      double var5 = this.FR
         ? this.amd
         : (
            AliceObf_fS.OK()
               ? AliceObf_fS.OI()
               : Rotations.serverYaw
         );
      double var7 = !this.FR
         ? (
            AliceObf_fS.OK()
               ? AliceObf_fS.OJ()
               : Rotations.serverPitch
         )
         : this.cG;
      double var9 = MathHelper.wrapDegrees(var1 - var5);
      double var11 = var3 - var7;
      if (this.Xo()) {
         return this.WO(var5, var7, var9, var11);
      } else {
         if (Math.abs(var9) < 0.5) {

         } else {
            char var18 = 33059;
         }

         double var13 = CrystalAuraPlusPlus.CQ(var5, var1, (Double)this.xR.get());
         double var15 = MathHelper.clamp(var7 + MathHelper.clamp(var11, -(Double)this.CJ.get(), (Double)this.CJ.get()), -90.0, 90.0);
         boolean var17 = Math.abs(var9) <= (Double)this.xR.get() + 1.0E-4 && Math.abs(var11) <= (Double)this.CJ.get() + 1.0E-4;
         this.apU = new AliceObf_om((float)var13, (float)var15);
         return var17;
      }
   }

   private boolean WO(double var1, double var3, double var5, double var7) {
      double var9 = this.WP();
      this.aAy = this.Dd.XJ(var1, var3, var1 + var5, var3 + var7, var9, (Double)this.xR.get(), (Double)this.CJ.get());
      this.apU = new AliceObf_om(this.aAy.XH(), this.aAy.XI());
      return Math.abs(MathHelper.wrapDegrees(var1 + var5 - this.aAy.XH())) <= var9 * 0.5 + 1.0E-4
         && Math.abs(var3 + var7 - this.aAy.XI()) <= var9 * 0.5 + 1.0E-4;
   }

      private double WP() {
      double var2 = this.mc.options == null ? 0.5 : (Double)this.mc.options.getMouseSensitivity().getValue();
      return WQ(var2);
   }

   static double WQ(double var0) {
      double var2 = var0 * 0.6 + 0.2;
      return Math.max(1.0E-4, var2 * var2 * var2 * 8.0 * 0.15);
   }

   static double WR(double var0, double var2) {
      return Math.round(var0 / var2) * var2;
   }

   static double WS(double var0, double var2, double var4) {
      double var6 = Math.floor(var4 / var2);
      return MathHelper.clamp(Math.round(var0 / var2), -var6, var6) * var2;
   }

   private void WT() {
      if (!this.Ws()) {
         this.WU();
      } else {
         PlayerEntity var1 = this.WE(this.pG) && this.WF(this.pG) ? this.pG : this.WD();
         int var10000 = var1 == null ? 12874 : 12875;

         while (true) {
            int var3 = var10000;
            if (var3 == 12874) {
               this.WU();
               return;
            }

            if (var3 != 12875) {
               this.pG = var1;
               double[] var2 = KillAuraPlusPlus.Xw(this.WV(var1));
               this.WN(var2[0], var2[1]);
               return;
            }

         }
      }
   }

   private void WU() {
      ClientPlayerEntity var1 = this.mc.player;
      if (this.FR && var1 != null) {
         double var2 = MathHelper.wrapDegrees(var1.getYaw() - this.amd);
         double var4 = var1.getPitch() - this.cG;
         double var6 = this.WP();
         if (Math.abs(var2) <= var6 * 0.5 + 1.0E-4 && Math.abs(var4) <= var6 * 0.5 + 1.0E-4) {
            if (this.Dd.XL(var6)) {

            } else {
               short var8 = 3346;
            }
         }

         this.WN(var1.getYaw(), var1.getPitch());
      }
   }

   private Vec3d WV(PlayerEntity var1) {
      Box var2 = this.WZ(var1);
      if (!this.Xo()) {
         return var2.getCenter();
      } else {
         AliceObf_ht var3 = this.ahy
            .Vp(var1.getId(), this.aBu);
         Vec3d var4 = new Vec3d(
            MathHelper.lerp(var3.Vm(), var2.minX, var2.maxX),
            MathHelper.lerp(var3.Vn(), var2.minY, var2.maxY),
            MathHelper.lerp(var3.Vo(), var2.minZ, var2.maxZ)
         );
         Vec3d var5 = this.WW(var1, var4);
         return var5 == null ? var2.getCenter() : var5;
      }
   }

   private Vec3d WW(PlayerEntity var1, Vec3d var2) {
      Vec3d var3 = this.mc.player.getEyePos();
      if (var2 != null && this.WY(var1, var3, var2)) {
         return var2;
      } else {
         Box var4 = this.WZ(var1);
         Vec3d[] var5 = KillAuraPlusPlus.WX(var4);
         int var6 = var5.length;
         byte var7 = 0;
         if (!(var7 >= var6)) {
            short var9 = 2776;
         }

         return null;
      }
   }

   static Vec3d[] WX(Box var0) {
      Vec3d var1 = var0.getCenter();
      double var2 = MathHelper.lerp(0.8, var0.minY, var0.maxY);
      return new Vec3d[]{
         var1,
         new Vec3d(var1.x, var2, var1.z),
         new Vec3d(var1.x, MathHelper.lerp(0.2, var0.minY, var0.maxY), var1.z),
         new Vec3d(MathHelper.lerp(0.15, var0.minX, var0.maxX), var2, var1.z),
         new Vec3d(MathHelper.lerp(0.85, var0.minX, var0.maxX), var2, var1.z),
         new Vec3d(var1.x, var2, MathHelper.lerp(0.15, var0.minZ, var0.maxZ)),
         new Vec3d(var1.x, var2, MathHelper.lerp(0.85, var0.minZ, var0.maxZ))
      };
   }

   private boolean WY(PlayerEntity var1, Vec3d var2, Vec3d var3) {
      Vec3d var4 = var2.add(var3.subtract(var2).normalize().multiply(this.Xp()));
      return this.Xd(var1, var2, var4);
   }

   private Box WZ(PlayerEntity var1) {
      return var1.getBoundingBox();
   }

   private Box Xa(PlayerEntity var1) {
      Box var2 = var1.getBoundingBox();
      PositionInterpolator var3 = var1.getInterpolator();
      Vec3d var4 = var3 == null ? null : var3.getLerpedPos();
      if (var4 == null) {
         return var2;
      } else {
         Vec3d var5 = var1.getEntityPos();
         return var2.offset(var4.x - var5.x, var4.y - var5.y, var4.z - var5.z);
      }
   }

   private boolean Xb(PlayerEntity var1) {
      if (this.FR && var1 != null) {
         if (!(this.mc.player != null)) {
            char var10 = 48743;
         }
      }

      return false;
   }

   private boolean Xc(PlayerEntity var1, Vec3d var2) {
      Vec3d var3 = var2.add(Vec3d.fromPolar(this.cG, this.amd).multiply(this.Xp()));
      return this.Xd(var1, var2, var3);
   }

   private boolean Xd(PlayerEntity var1, Vec3d var2, Vec3d var3) {
      Box var4 = var1.getBoundingBox();
      Box var5 = this.Xa(var1);
      Vec3d var6 = KillAuraPlusPlus.Xe(var4, var2, var3);
      if (var6 == null || Xv(var2, var5) > this.Xp() * this.Xp()) {
         return false;
      } else if (this.Xf(var2, var3) != var1) {
         return false;
      } else {
         BlockHitResult var7 = this.mc.world.raycast(new RaycastContext(var2, var3, ShapeType.OUTLINE, FluidHandling.NONE, this.mc.player));
         return var7.getType() == HitResult.Type.MISS || var2.squaredDistanceTo(var7.getPos()) + 1.0E-4 >= var2.squaredDistanceTo(var6);
      }
   }

   static Vec3d Xe(Box var0, Vec3d var1, Vec3d var2) {
      return var0.contains(var1) ? var1 : (Vec3d)var0.raycast(var1, var2).orElse(null);
   }

   private Entity Xf(Vec3d var1, Vec3d var2) {
      EntityHitResult var3 = ProjectileUtil.raycast(
         this.mc.player, var1, var2, new Box(var1, var2).expand(1.0), EntityPredicates.CAN_HIT, var1.squaredDistanceTo(var2)
      );
      return var3 == null ? null : var3.getEntity();
   }

   private boolean Xg(Box var1) {
      if (this.mc.player != null && this.mc.world != null) {
         Vec3d var2 = this.mc.player.getEyePos();
         Vec3d var3 = var1.getCenter();
         BlockHitResult var4 = this.mc
            .world
            .raycast(new RaycastContext(var2, var3, this.Xo() ? ShapeType.OUTLINE : ShapeType.COLLIDER, FluidHandling.NONE, this.mc.player));
         return var4.getType() == HitResult.Type.MISS || var2.squaredDistanceTo(var4.getPos()) + 1.0E-4 >= var2.squaredDistanceTo(var3);
      } else {
         return false;
      }
   }

   private void Xh() {
      AliceObf_om var1 = this.apU;
      this.apU = null;
      ClientPlayerEntity var2 = this.mc.player;
      if (var1 != null && var2 != null) {
         int var10000 = !var2.hasVehicle() ? 63953 : 63954;

         while (true) {
            int var3 = var10000;
            if (var3 == 63953) {
               if (var2.input != null) {
                  this.Xi();
                  this.aud = new AliceObf_lH(this, var2.input, var2, var1);
                  var2.input = this.aud;
                  return;
               }
               break;
            }

            if (var3 != 63954) {
               break;
            }

         }
      }

      this.Xl();
   }

   private void Xi() {
      AliceObf_lH var1 = this.aud;
      if (var1 != null) {
         var1.VT();
         AliceObf_au.Fy(var1.WW, var1);
         this.aud = null;
      }
   }

   private void Xj() {
      AliceObf_lH var1 = this.aud;
      ClientPlayerEntity var2 = this.mc.player;
      if (this.Xo()) {
         if (!(var1 != null)) {
            short var3 = 18732;
         }
      }

      this.Xl();
   }

   private static Input Xk(Input var0) {
      while (var0 instanceof AliceObf_au) {
         AliceObf_au var1 = (AliceObf_au)var0;
         var0 = var1.Fu();
      }

      return var0;
   }

   private void Xl() {
      this.aGl.VD();
      this.dV.VP();
      this.ds.VK();
   }

   private void Xm() {
      this.pG = null;
      this.NK.Rn();
      this.apU = null;
   }

   private void Xn() {
      CrystalAuraPlusPlus.zp();
      this.Xi();
      this.Xl();
      this.Xm();
      this.aBx = false;
      this.amd = 0.0F;
      this.cG = 0.0F;
      this.FR = false;
      this.wq = null;
      this.anZ = null;
      this.kd = this.mc.world;
      this.Ax = this.mc.player;
      this.aBu = 0L;
      this.SB.Vy();
      this.aHO = null;
      this.Tp = false;
      this.Dd.XM();
      this.ahy.Vq();
      this.aAy = null;
      this.adB = false;
      this.dD = false;
      this.aGv = false;
      this.kU = this.Xo();
   }

   private boolean Xo() {
      return (Boolean)this.anY.get();
   }

   private double Xp() {
      int var10000 = !this.Xo() ? 59063 : 59062;

      while (true) {
         int var1 = var10000;
         if (var1 == 59062) {
            return Math.min((Double)this.Ra.get(), 3.0);
         }

         if (var1 != 59063) {
            return (Double)this.Ra.get();
         }

      }
   }

   private boolean Xq() {
      return !this.Xo() ? !(this.Xs() + 1.0E-4F < ((Double)this.Pg.get()).floatValue()) : this.SB.Vu(this.aBu, this.Xs() + 1.0E-4F >= 1.0F);
   }

   private void Xr() {
      if (this.Xo()) {
         double var1 = -Math.log(1.0 - this.Dj.nextDouble()) * 0.9;
         int var3 = Math.min((int)Math.floor(var1), 3);
         int var10000 = var3 >= 0 ? 61023 : 61022;

         while (true) {
            int var4 = var10000;
            if (var4 == 61022) {
               var3 = 0;
               break;
            }

            if (var4 != 61023) {
               break;
            }

         }

         if (this.Dj.nextDouble() < 0.07) {
            short var5 = 31736;
         } else {
            short var6 = 31737;
         }

         this.SB.Vx(var3);
      }
   }

   private float Xs() {
      return this.mc.player == null ? 0.0F : this.mc.player.getAttackCooldownProgress(0.5F);
   }

   @Deprecated
   private float Xt() {
      return this.Xs();
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   static int Xu(AliceObf_tV var0, float var1, float var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      return switch (var0.ordinal()) {
         case 0 -> Float.compare(var1, var2);
         case 1 -> Double.compare(var3, var5);
         case 2 -> Double.compare(var7, var9);
         case 3 -> Integer.compare(var11, var12);
         case 4 -> Integer.compare(var12, var11);
         default -> throw new MatchException(null, null);
      };
   }

   static double Xv(Vec3d var0, Box var1) {
      double var2 = Math.max(var1.minX - var0.x, Math.max(0.0, var0.x - var1.maxX));
      double var4 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var6 = Math.max(var1.minZ - var0.z, Math.max(0.0, var0.z - var1.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   private static double[] Xw(Vec3d var0) {
      ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
      Vec3d var2 = var1.getEyePos();
      double var3 = var0.x - var2.x;
      double var5 = var0.y - var2.y;
      double var7 = var0.z - var2.z;
      double var9 = Math.sqrt(var3 * var3 + var7 * var7);
      return new double[]{Math.toDegrees(Math.atan2(var7, var3)) - 90.0, -Math.toDegrees(Math.atan2(var5, var9))};
   }

   private static float Xx(PlayerEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   private static Vec2f Xy(PlayerInput var0) {
      float var1 = (var0.left() ? 1.0F : 0.0F) - (var0.right() ? 1.0F : 0.0F);
      float var2 = (var0.forward() ? 1.0F : 0.0F) - (var0.backward() ? 1.0F : 0.0F);
      return new Vec2f(var1, var2).normalize();
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         return this.pG == null ? null : this.pG.getName().getString();
      }
   }

   }

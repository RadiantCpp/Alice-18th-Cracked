package com.nnpg.alice.modules.player;

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
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction.Type;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class AutoMinePlusPlus extends AliceModule {
   private static final double aqc = 0.0625;
   private static final double gn = 0.05;
   private static final float Hp = 600.0F;
   private static final int PL = 1500;
   private static final int oj = 700;
   private static final int to = 600;
   private static final int aGH = 550;
   private static final int FV = 500;
   private static final int em = 400;
   private static final int aPh = 100;
   private static final int auY = 400;
   private static final Direction[] nq;
   private static final EquipmentSlot[] aKl;
   private final SettingGroup azz = this.settings.getDefaultGroup();
   private final SettingGroup rs;
   private final SettingGroup aAF;
   private final Setting<AliceObf_uQ> aAC;
   private final Setting<Boolean> aEi;
   private final Setting<Boolean> qZ;
   private final Setting<Boolean> Tu;
   private final Setting<Boolean> akl;
   private final Setting<Integer> afU;
   private final Setting<Boolean> pH;
   private final Setting<Boolean> CI;
   private final Setting<Boolean> aAp;
   private final Setting<Boolean> amC;
   private final Setting<Boolean> aaN;
   private final Setting<Boolean> anm;
   private final Setting<Boolean> axc;
   private final Setting<Boolean> afR;
   private final Setting<Boolean> aOg;
   private final Setting<Boolean> adI;
   private final Setting<Boolean> akS;
   private int Me;
   private int aEm;
   private long NH;
   private AliceObf_bI ajz;
   private AliceObf_bI OA;
   private boolean Ye;
   public AutoMinePlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "auto-mine++", "Bo lap ke hoach ALICE toi uu hai lane cho Speed Mine++");
      this.rs = this.settings.createGroup("Rules");
      this.aAF = this.settings.createGroup("Self");
      this.aAC = this.azz
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("logic")).description("Che do chon muc tieu: Plain, Grim hoac NoVelocity."))
                  .defaultValue(AliceObf_uQ.yK))
               .build()
         );
      this.aEi = this.azz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("strict-direction"))
                     .description("Chi nhan block co mat dao hop le tu vi tri mat hien tai."))
                  .defaultValue(false))
               .build()
         );
      this.qZ = this.azz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("raytrace"))
                     .description("Yeu cau tia nhin cham duoc block muc tieu."))
                  .defaultValue(false))
               .build()
         );
      this.Tu = this.azz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("rotate"))
                     .description("Gui look trong movement packet truoc khi giao plan cho Speed Mine++."))
                  .defaultValue(false))
               .build()
         );
      this.akl = this.azz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("ignore-naked"))
                     .description("Bo qua nguoi choi khong mac mon giap nao."))
                  .defaultValue(false))
               .build()
         );
      this.afU = this.azz
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("grim-delay"))
                     .description("So tick giu nguyen plan moi truoc khi giao cho Speed Mine++."))
                  .defaultValue(1))
               .range(0, (5))
               .sliderRange((0), 5)
               .build()
         );
      this.pH = this.rs
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("burrow"))
                     .description("Dao block chong no ma muc tieu dang dung ben trong."))
                  .defaultValue(true))
               .build()
         );
      this.CI = this.rs
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("fish"))
                     .description("Dao block ben duoi muc tieu dang dung tren cot bedrock mot o."))
                  .defaultValue(false))
               .build()
         );
      this.aAp = this.rs
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("crystal-base"))
                     .description("Dao block cach nen dang dung cua muc tieu 1-2 o de mo base dat crystal."))
                  .defaultValue(true))
               .build()
         );
      this.amC = this.rs
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("head"))
                     .description("Dao nap va vong block tren dau muc tieu."))
                  .defaultValue(false))
               .build()
         );
      this.aaN = this.rs
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("trap-override"))
                        .description("Nang uu tien nap dau len 650 khi gap trap hop le."))
                     .defaultValue(false))
                  .visible(this.amC::get))
               .build()
         );
      this.anm = this.rs
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("feet"))
                     .description("City vong surround quanh chan muc tieu."))
                  .defaultValue(true))
               .build()
         );
      this.axc = this.rs
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("safe"))
                        .description("Khong city block thuoc surround cua chinh ban."))
                     .defaultValue(true))
                  .visible(this.anm::get))
               .build()
         );
      this.afR = this.aAF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("self"))
                     .description("Tu dao block dang ghim dau, mat hoac chan cua ban."))
                  .defaultValue(true))
               .build()
         );
      this.aOg = this.aAF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("face"))
                        .description("Uu tien block chong no tai eye position cua ban."))
                     .defaultValue(false))
                  .visible(this.afR::get))
               .build()
         );
      this.adI = this.aAF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("only-crawl"))
                        .description("Chi tu dao khi dang crawl, tru block face hop le."))
                     .defaultValue(false))
                  .visible(this.afR::get))
               .build()
         );
      this.akS = this.aAF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("down-priority"))
                        .description("Cho phep block duoi chan thang uu tien ma khong can hole an toan."))
                     .defaultValue(false))
                  .visible(this.afR::get))
               .build()
         );
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.it();
         SpeedMinePlusPlus var1 = this.is();
         if (var1 == null || !var1.isActive()) {
            this.warning("Speed Mine++ is off, so Auto Mine++ cannot submit targets.", new Object[0]);
         }

         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.NH += 1L;
      SpeedMinePlusPlus var1 = this.is();
      if (var1 != null) {
         var1.aib();
      }

      this.it();
      super.onDeactivate();
   }

   @AliceHandler(
      priority = 150
   )
   private void hK(Pre var1) {
      this.Me++;
      SpeedMinePlusPlus var2 = this.is();
      if (var2 != null
         && var2.isActive()
         && this.mc.player != null
         && this.mc.world != null
         && !this.mc.player.isCreative()
         && !this.mc.player.isSpectator()) {
         PlayerEntity var3 = this.hU(var2.aij());
         AliceObf_bu var4 = new AliceObf_bu(this);
         this.hN(var2, var3, var4);
         this.hO(var2, var3, var4);
         this.hP(var2, var3, var4);
         this.hR(var2, var3, var4);
         this.hS(var2, var3, var4);
         this.hT(var2, var3, var4);
         AliceObf_bI var5 = var4.hJ();
         if (var5 == null) {
            this.iu();
         } else {
            if (!this.hW(var2, var5)) {

            } else {
               char var7 = 46882;
            }

            int var10001 = iv(var5, this.ajz) ? 61901 : 61900;

            while (true) {
               int var6 = var10001;
               if (var6 == 61900) {
                  this.ajz = var5;
                  this.aEm = this.Me + (Integer)this.afU.get();
                  this.Ye = false;
                  this.OA = null;
                  this.NH += 1L;
                  break;
               }

               if (var6 != 61901) {
                  break;
               }

            }

            if (this.Me >= this.aEm) {
               if (!var2.aif(var5.hB(), var5.hD()) && !var2.aig()) {
                  if ((Boolean)this.Tu.get() && !iv(var5, this.OA)) {
                     this.hL(var5);
                  } else {
                     this.hM(var2, var5);
                  }
               }
            }
         }
      } else {
         this.iu();
      }
   }

   private void hL(AliceObf_bI var1) {
      if (!this.Ye && this.mc.player != null) {
         this.Ye = true;
         long var2 = this.NH += 1L;
         Vec3d var4 = this.hY(var1.hB(), var1.hC());
         double var5 = Rotations.getYaw(var4);
         double var7 = Rotations.getPitch(var4);
         Rotations.rotate(
            var5,
            var7,
            100,
            () -> {
               this.Ye = false;
               if (var2 == this.NH
                  && this.isActive()
                  && AutoMinePlusPlus.iv(var1, this.ajz)) {
                  SpeedMinePlusPlus var4x = this.is();
                  if (var4x != null && var4x.isActive() && !var4x.aig() && this.hW(var4x, var1)) {
                     this.OA = var1;
                     this.hM(var4x, var1);
                  }
               }
            }
         );
      }
   }

   private boolean hM(SpeedMinePlusPlus var1, AliceObf_bI var2) {
      return var1.aia(var2.hB(), var2.hC(), var2.hD(), var2.hE(), var2.hF(), var2.hF() >= (1500));
   }

      private void hN(SpeedMinePlusPlus var1, PlayerEntity var2, AliceObf_bu var3) {
      if ((Boolean)this.pH.get() && var2 != null) {
         List var4 = BlockPos.stream(var2.getBoundingBox().expand(-0.1, 0.0, -0.1).withMaxY(var2.getY()))
            .<BlockPos>map(BlockPos::toImmutable)
            .filter(
               var2x -> this.checkBlock(var2x)
                     && !this.mc.world.getBlockState(var2x).isOf(Blocks.COBWEB)
                     && !this.mc.world.getBlockState(var2x).isOf(Blocks.ANVIL)
                     && this.hV(var1, var2x)
            )
            .sorted(this.ih(var1, var2))
            .toList();
         if (var4.size() >= 2) {
            var3.hH(700, "BURROW", (BlockPos)var4.get(0), (BlockPos)var4.get(1));
         } else if (var4.size() == 1) {
            var3.hH(500, "BURROW", (BlockPos)var4.get(0), null);
         }
      }
   }

   private void hO(SpeedMinePlusPlus var1, PlayerEntity var2, AliceObf_bu var3) {
      if ((Boolean)this.CI.get() && var2 != null && ip(var2).size() == (1)) {
         BlockPos var4 = AutoMinePlusPlus.io(var2);
         if (!this.mc.world.isAir(var4) && !BlockUtils.canBreak(var4, this.mc.world.getBlockState(var4))) {
            BlockPos var5 = var4.down();
            if (this.hV(var1, var5)) {

            } else {
               char var6 = 58900;
            }
         }
      }
   }

   private void hP(SpeedMinePlusPlus var1, PlayerEntity var2, AliceObf_bu var3) {
      if ((Boolean)this.aAp.get() && var2 != null && this.ia(var2)) {
         BlockPos var4 = io(var2);
         LinkedHashSet var5 = new LinkedHashSet();

         for (BlockPos var7 : AutoMinePlusPlus.iq(
            var2.getBoundingBox(), 0.0625
         )) {
            for (int var8 = 1; var8 <= 2; var8++) {
               BlockPos var9 = new BlockPos(var7.getX(), var4.getY() - 1 - var8, var7.getZ());
               BlockState var10 = this.mc.world.getBlockState(var9.down());
               if (!var10.isOf(Blocks.OBSIDIAN)) {
                  if (var10.isOf(Blocks.BEDROCK)) {

                  } else {
                     char var12 = 59964;
                  }
               } else if (this.hV(var1, var9) && this.hQ(var9, var2)) {
                  var5.add(var9.toImmutable());
               }
            }
         }

         if (var5.isEmpty()) {
            char var13 = 64957;
         } else {
            char var14 = 64958;
         }

         ArrayList var11 = new ArrayList(var5);
         var11.sort(this.ih(var1, var2));
         var3.hH(
            (550),
            "CRYSTAL_BASE",
            (BlockPos)var11.get(0),
            var11.size() <= 1 ? null : (BlockPos)var11.get(1)
         );
      }
   }

   private boolean hQ(BlockPos var1, PlayerEntity var2) {
      Box var3 = new Box(var1);

      for (Entity var5 : this.mc.world.getEntities()) {
         if (var5 != var2 && !var5.isRemoved() && var5.isAlive() && var5.getBoundingBox().intersects(var3)) {
            return false;
         }
      }

      return true;
   }

   private void hR(SpeedMinePlusPlus var1, PlayerEntity var2, AliceObf_bu var3) {
      if ((Boolean)this.anm.get() && var2 != null && this.ia(var2)) {
         if (!this.in(var2).stream().allMatch(var1x -> this.mc.world.getBlockState(var1x).isOf(Blocks.BEDROCK))) {
            BlockPos var4 = this.im(var2);
            int var5 = var4.getY()
               - AutoMinePlusPlus.io(var2).getY();
            boolean var6 = this.checkBlock(var4);
            Set var7 = AutoMinePlusPlus.ir(this.mc.player);
            ArrayList var8 = new ArrayList();

            for (BlockPos var10 : AutoMinePlusPlus.ir(var2)) {
               BlockPos var11 = var10.up(var5);
               BlockPos var12 = var6 ? var11.down() : var11;
               if ((Boolean)this.axc.get()) {

               } else {
                  short var13 = 32006;
               }

               if (!this.mc.world.getBlockState(var12).isOf(Blocks.COBWEB)) {
                  if (this.hV(var1, var12)) {

                  } else {
                     short var14 = 2823;
                  }

                  if (this.aAC.get() != AliceObf_uQ.yK || var6 || this.ig(var12)) {
                     var8.add(var12);
                  }
               }
            }

            var8.sort(this.ii(var1, var2));
            if (!var8.isEmpty()) {
               var3.hH(
                  600,
                  "FEET",
                  (BlockPos)var8.get(0),
                  var8.size() <= (1) ? null : (BlockPos)var8.get(1)
               );
            }
         }
      }
   }

   private void hS(SpeedMinePlusPlus var1, PlayerEntity var2, AliceObf_bu var3) {
      if ((Boolean)this.afR.get() && !this.mc.player.isGliding()) {
         BlockPos var4 = this.ic(var1);
         if (var2 != null || var4 != null) {
            boolean var5 = this.mc.player.isCrawling();
            if (!(Boolean)this.adI.get() || var5 || var4 != null) {
               int var6 = !var5 && var4 == null ? 100 : 1500;
               BlockPos var7 = null;
               BlockPos var8 = io(this.mc.player).down();
               if (this.hV(var1, var8) && (this.ij(var1, var8) || var5) && this.ie()) {
                  var7 = var8;
               }

               if (var7 == null) {
                  if (!(var4 != null)) {
                     char var9 = 64322;
                  }

                  var7 = this.id(var1);
               }

               var3.hH(var6, "SELF", var7, null);
            }
         }
      }
   }

   private void hT(SpeedMinePlusPlus var1, PlayerEntity var2, AliceObf_bu var3) {
      int var10000 = !this.amC.get() ? 44706 : 44705;

      while (true) {
         int var13 = var10000;
         if (var13 == 44705) {
            if (var2 != null) {
               char var14 = 39941;
            } else {
               char var15 = 39942;
            }
            break;
         }

         if (var13 != 44706) {
            break;
         }

      }
   }

   private PlayerEntity hU(double var1) {
      PlayerEntity var3 = null;
      double var4 = Double.MAX_VALUE;

      for (PlayerEntity var7 : this.mc.world.getPlayers()) {
         if (var7 != this.mc.player
            && var7.isAlive()
            && !var7.isSpectator()
            && Friends.get().shouldAttack(var7)
            && !(this.mc.player.squaredDistanceTo(var7) > var1 * var1)
            && (!(Boolean)this.akl.get() || ik(var7))) {
            double var8 = Math.abs(MathHelper.wrapDegrees((float)(Rotations.getYaw(var7) - this.mc.player.getYaw())));
            if (var8 < var4) {
               var4 = var8;
               var3 = var7;
            }
         }
      }

      return var3;
   }

   private boolean hV(SpeedMinePlusPlus var1, BlockPos var2) {
      if (var2 != null) {
         if (!var1.ail(var2)) {

         } else {
            char var5 = 60435;
         }

         BlockState var3 = this.mc.world.getBlockState(var2);
         if (!var3.isAir() && BlockUtils.canBreak(var2, var3)) {
            Direction var4 = this.hX(var2);
            if ((Boolean)this.aEi.get() && var4 == null) {
               return false;
            } else {
               return !(this.qZ.get() && !this.hZ(var2));
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean hW(SpeedMinePlusPlus var1, AliceObf_bI var2) {
      if (var2 != null && this.hV(var1, var2.hB())) {
         return !(var2.hD() != null && !this.hV(var1, var2.hD()));
      } else {
         return false;
      }
   }

   private Direction hX(BlockPos var1) {
      return !this.aEi.get() ? Direction.UP : CrystalAuraPlusPlus.CO(var1, this.mc.player.getEyePos());
   }

   private Vec3d hY(BlockPos var1, Direction var2) {
      Vec3d var3 = Vec3d.ofCenter(var1);
      Direction var4 = var2 == null ? Direction.UP : var2;
      return var3.add(var4.getOffsetX() * 0.5, var4.getOffsetY() * 0.5, var4.getOffsetZ() * 0.5);
   }

   private boolean hZ(BlockPos var1) {
      BlockHitResult var2 = this.mc
         .world
         .raycast(
            new RaycastContext(this.mc.player.getEyePos(), Vec3d.ofCenter(var1), ShapeType.COLLIDER, FluidHandling.NONE, this.mc.player)
         );
      return !(var2.getType() != HitResult.Type.MISS && !var2.getBlockPos().equals(var1));
   }

   private boolean ia(Entity var1) {
      return (!this.mc.world.isSpaceEmpty(var1, var1.getBoundingBox().stretch(0.0, -0.1, 0.0)));
   }

   private boolean ib(SpeedMinePlusPlus var1, BlockPos var2) {
      if (!this.hV(var1, var2)) {
         return false;
      } else if (!this.ij(var1, var2)) {
         if (this.aAC.get() == AliceObf_uQ.lL) {
            return true;
         } else {
            return (this.mc.world.getBlockState(var2).isOf(Blocks.OBSIDIAN) && this.mc.world.isAir(var2.up()));
         }
      } else {
         return true;
      }
   }

   private BlockPos ic(SpeedMinePlusPlus var1) {
      if ((Boolean)this.aOg.get() && !this.mc.player.isCrawling()) {
         BlockPos var2 = BlockPos.ofFloored(this.mc.player.getEyePos());
         if (this.checkBlock(var2)) {
            int var10000 = this.hV(var1, var2) ? 53000 : 53001;

            while (true) {
               int var3 = var10000;
               if (var3 == 53000) {
                  return var2;
               }

               if (var3 != 53001) {
                  break;
               }

            }
         }

         return null;
      } else {
         return null;
      }
   }

   private BlockPos id(SpeedMinePlusPlus var1) {
      int var2 = (int)Math.floor(this.mc.player.getBoundingBox().maxY) + (1);

      for (BlockPos var4 : iq(this.mc.player.getBoundingBox(), 0.05)) {
         BlockPos var5 = new BlockPos(var4.getX(), var2, var4.getZ());
         if (this.hV(var1, var5)) {
            return var5;
         }
      }

      return null;
   }

   private boolean ie() {
      if ((Boolean)this.akS.get()) {

      } else {
         short var3 = 20259;
      }

      if (this.mc
         .world
         .isAir(
            AutoMinePlusPlus.io(this.mc.player)
               .down((2))
         )) {
         return false;
      } else {
         for (BlockPos var2 : AutoMinePlusPlus.ir(this.mc.player)) {
            if (!this.checkBlock(var2.down())) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean checkBlock(BlockPos var1) {
      BlockState var2 = this.mc.world.getBlockState(var1);
      if (!var2.isAir()) {
         if (!(var2.getBlock().getBlastResistance() >= 600.0F)) {

         } else {
            short var3 = 18880;
         }
      }

      return false;
   }

   private boolean ig(BlockPos var1) {
      Direction[] var2 = nq;
      int var3 = var2.length;

      for (int var4 = (0); var4 < var3; var4++) {
         Direction var5 = var2[var4];
         BlockPos var6 = var1.offset(var5).down();
         BlockState var7 = this.mc.world.getBlockState(var6);
         if (var7.isOf(Blocks.OBSIDIAN) || var7.isOf(Blocks.BEDROCK)) {
            BlockPos var8 = var6.up();
            if (this.mc.world.isAir(var8)) {

            } else {
               short var13 = 31216;
            }

            new Box(var8);
            byte var10 = 0;

            for (Entity var12 : this.mc.world.getEntities()) {
               if (!var12.isRemoved()) {
                  if (var12.isAlive()) {

                  } else {
                     char var14 = 56400;
                  }
               }
            }

            if (var10 == 0) {
               return true;
            }
         }
      }

      return false;
   }

   private Comparator<BlockPos> ih(SpeedMinePlusPlus var1, PlayerEntity var2) {
      return Comparator.<BlockPos, Boolean>comparing(
            var2x -> Boolean.valueOf((!this.ij(var1, var2x)))
         )
         .thenComparingDouble(var1x -> var2.squaredDistanceTo(Vec3d.ofCenter(var1x)));
   }

   private Comparator<BlockPos> ii(SpeedMinePlusPlus var1, PlayerEntity var2) {
      return Comparator.<BlockPos, Boolean>comparing(
            var2x -> Boolean.valueOf((!this.ij(var1, var2x)))
         )
         .thenComparing(
            var1x -> Boolean.valueOf((!this.mc.player.getBoundingBox().intersects(new Box(var1x))))
         )
         .thenComparing(var1x -> Boolean.valueOf(!(this.ig(var1x))))
         .thenComparingDouble(var1x -> var2.squaredDistanceTo(Vec3d.ofCenter(var1x)));
   }

   private boolean ij(SpeedMinePlusPlus var1, BlockPos var2) {
      return !(var2 == null || !var2.equals(var1.aic()) && !var2.equals(var1.aid()));
   }

   private static boolean ik(PlayerEntity var0) {
      EquipmentSlot[] var1 = aKl;
      int var2 = var1.length;

      for (int var3 = (0); var3 < var2; var3++) {
         EquipmentSlot var4 = var1[var3];
         if (var0.hasStackEquipped(var4)) {
            return true;
         }
      }

      return false;
   }

   private int il(Entity var1) {
      BlockPos var2 = io(var1);
      int var3 = (0);
      if (var3 <= (2)) {

      } else {
         char var5 = 64110;
      }

      return var2.getY();
   }

   private BlockPos im(Entity var1) {
      BlockPos var2 = io(var1);
      return new BlockPos(var2.getX(), this.il(var1), var2.getZ());
   }

   private Set<BlockPos> in(Entity var1) {
      int var2 = this.il(var1);
      LinkedHashSet var3 = new LinkedHashSet();

      for (BlockPos var5 : AutoMinePlusPlus.iq(
         var1.getBoundingBox(), 0.0625
      )) {
         var3.add(new BlockPos(var5.getX(), var2, var5.getZ()));
      }

      return var3;
   }

   private static BlockPos io(Entity var0) {
      return BlockPos.ofFloored(var0.getX(), var0.getY(), var0.getZ());
   }

   private static Set<BlockPos> ip(Entity var0) {
      int var1 = AutoMinePlusPlus.io(var0).getY();
      LinkedHashSet var2 = new LinkedHashSet();
      Iterator var3 = AutoMinePlusPlus.iq(var0.getBoundingBox(), 0.0625)
         .iterator();
      if (var3.hasNext()) {

      } else {
         char var5 = 38619;
      }

      return var2;
   }

   private static Set<BlockPos> iq(Box var0, double var1) {
      Box var3 = var0.expand(-var1, 0.0, -var1);
      LinkedHashSet var4 = new LinkedHashSet();
      var4.add(BlockPos.ofFloored(var3.minX, 0.0, var3.minZ));
      var4.add(BlockPos.ofFloored(var3.minX, 0.0, var3.maxZ));
      var4.add(BlockPos.ofFloored(var3.maxX, 0.0, var3.minZ));
      var4.add(BlockPos.ofFloored(var3.maxX, 0.0, var3.maxZ));
      return var4;
   }

   private static Set<BlockPos> ir(Entity var0) {
      Set<BlockPos> var1 = ip(var0);
      LinkedHashSet var2 = new LinkedHashSet();

      for (BlockPos var4 : var1) {
         for (Direction var6 : Direction.Type.HORIZONTAL) {
            BlockPos var7 = var4.offset(var6);
            if (!var1.contains(var7)) {
               var2.add(var7);
            }
         }
      }

      return var2;
   }

   private SpeedMinePlusPlus is() {
      return (SpeedMinePlusPlus)Modules.get().get(SpeedMinePlusPlus.class);
   }

   private void it() {
      this.Me = (0);
      this.iu();
   }

   private void iu() {
      this.ajz = null;
      this.OA = null;
      this.Ye = false;
      this.aEm = 0;
      this.NH += 1L;
   }

   private static boolean iv(AliceObf_bI var0, AliceObf_bI var1) {
      if (var0 != var1) {
         if (!(var0 != null)) {
            short var2 = 25263;
         }

         return false;
      } else {
         return true;
      }
   }

   static {

      Direction[] var10000 = new Direction[(5)];
      var10000[0] = Direction.EAST;
      var10000[1] = Direction.SOUTH;
      var10000[(2)] = Direction.UP;
      var10000[3] = Direction.NORTH;
      var10000[4] = Direction.WEST;
      nq = var10000;
      EquipmentSlot[] var0 = new EquipmentSlot[4];
      var0[(0)] = EquipmentSlot.HEAD;
      var0[1] = EquipmentSlot.CHEST;
      var0[(2)] = EquipmentSlot.LEGS;
      var0[(3)] = EquipmentSlot.FEET;
      aKl = var0;
   }

   }

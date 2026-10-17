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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import net.minecraft.util.PlayerInput;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.util.Hand;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.network.ClientPlayerEntity;

public final class AntiPhasePlusPlus extends AliceModule implements AliceObf_jQ {
   private static final double atq = 0.0625;
   private static final double aDm = 0.1875;
   private static final double pD = 0.0625;
   private static final double aMg = 0.6;
   private static final double df = 4.5;
   private static final double iM = 0.08;
   private static final int Jr = 2;
   private static final int nY = 4;
   private static final double auX = 0.3;
   private static final int No = 20;
   private static final int aan = 3;
   private static final int xW = 120;
   private static final Direction[] aPI;
   private static final Direction[] arz;
   private static final EquipmentSlot[] nR;
   private final SettingGroup agg = this.settings.getDefaultGroup();
   private final SettingGroup fe;
   private final SettingGroup ahE;
   private final Setting<AliceObf_hZ> sL;
   private final Setting<Double> aQm;
   private final Setting<Boolean> aFJ;
   private final Setting<Boolean> s;
   private final Setting<Boolean> aiQ;
   private final Setting<SettingColor> jx;
   private final Setting<SettingColor> Gh;
   private final Setting<SettingColor> pM;
   private final Map<BlockPos, Integer> aHN;
   private final Map<Integer, Vec3d> ayJ;
   private final Map<Integer, Vec3d> vV;
   private final Map<Integer, Integer> aOj;
   private final Random agx;
   private AliceObf_bz aBr;
   private AliceObf_bz HU;
   private int acz;
   private int Fl;
   private boolean cW;
   private boolean uu;
   private int avV;
   private float abd;
   private float aop;
   private AliceObf_mO adi;
   private AliceObf_sQ aJD;
   private BlockPos Si;
   private BlockPos fG;
   private Direction aPt;
   private float afC;
   private float jV;
   private int AX;
   private boolean qc;
   private boolean arE;
   public AntiPhasePlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "anti-phase++", "Bit o dat cua doi thu bang scaffolding, vine, ladder hoac item frame de chan pearl phase. Grim-safe.");
      this.fe = this.settings.createGroup("Targeting");
      this.ahE = this.settings.createGroup("ESP");
      this.sL = this.agg
         .add(((Builder)((Builder)((Builder)new Builder().name("seal-item")).description("Vat pham dung de bit o dat. Scaffold bit chac nhat, Frame khong can block dac hai ben.")).defaultValue(AliceObf_hZ.apE)).build());
      this.aQm = this.agg
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("player-range"))
                  .description("Khoang cach toi da tu ban den doi thu can bit."))
               .defaultValue(5.0)
               .min(1.0)
               .sliderRange(1.0, 8.0)
               .build()
         );
      this.aFJ = this.fe
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("ignore-friends"))
                     .description("Khong bit o dat cua friend."))
                  .defaultValue(true))
               .build()
         );
      this.s = this.fe
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("armoured-only"))
                     .description("Chi bit doi thu dang mac du bon manh giap."))
                  .defaultValue(false))
               .build()
         );
      this.aiQ = this.ahE
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("esp"))
                     .description("Ve tam bit sap dat va tam vua dat."))
                  .defaultValue(true))
               .build()
         );
      this.jx = this.ahE
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                     .name("plan-color"))
                  .description("Mau o dat vua tim ra, goc chua arm."))
               .defaultValue(new SettingColor((120), 220, (255), 220))
               .build()
         );
      this.Gh = this.ahE
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                     .name("arm-color"))
                  .description("Mau khi goc da arm va chi cho handshake khop de ban packet."))
               .defaultValue(new SettingColor(180, 255, 140, (230)))
               .build()
         );
      this.pM = this.ahE
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                     .name("sealed-color"))
                  .description("Mau tam vua gui packet dat."))
               .defaultValue(new SettingColor(255, (170), (60), 220))
               .build()
         );
      this.aHN = new HashMap<>();
      this.ayJ = new HashMap<>();
      this.vV = new HashMap<>();
      this.aOj = new HashMap<>();
      this.agx = new Random();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.eR();
         AliceObf_fS.Od(this);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      AliceObf_fS.Oe(this);
      this.eR();
      super.onDeactivate();
   }

   private void eR() {
      this.aBr = null;
      this.HU = null;
      this.acz = (0);
      this.cW = false;
      this.aHN.clear();
      this.ayJ.clear();
      this.vV.clear();
      this.aOj.clear();
      this.fG();
   }

   @AliceHandler(
      priority = 100
   )
   private void eS(Pre var1) {
      if (!(super.mc.player != null)) {
         short var4 = 18200;
      }

      this.eR();
   }

   @AliceHandler(
      priority = 90
   )
   private void eT(PlayerTickMovementEvent var1) {
      if (this.uu && this.adi != null && this.aJD == null) {
         ClientPlayerEntity var2 = super.mc.player;
         if (!(var2 != null)) {
            short var4 = 22540;
         }

         this.fG();
      }
   }

   @AliceHandler(
      priority = -50
   )
   private void eU(Post var1) {
      AliceObf_sQ var2 = this.aJD;
      if (var2 != null) {
         var2.eQ(false);
         AliceObf_au.Fy(super.mc.player, var2);
         this.aJD = null;
         this.uu = false;
      }
   }

   @AliceHandler(
      priority = -1000
   )
   private void eV(Sent var1) {
      if (!(var1.packet instanceof PlayerInteractBlockC2SPacket)) {
         if (var1.packet instanceof PlayerMoveC2SPacket var2 && var2.changesLook()) {
            if (this.uu) {
               float var5 = var2.getYaw(this.abd);
               float var4 = var2.getPitch(this.aop);
               if (!AliceObf_fS.Qr(var5, var4, this.abd, this.aop)
                  )
                {
                  this.adi = null;
               }

               this.uu = false;
            }
         }
      } else {
         if (this.qc) {
            this.arE = true;
         }
      }
   }

   public AliceObf_y prepareCombatIntent() {
      if (super.mc.player != null && super.mc.world != null) {
         AliceObf_bz var1 = this.aBr;
         if (var1 == null) {
            return null;
         } else {
            AliceObf_hZ var2 = (AliceObf_hZ)this.sL.get();
            Item[] var10000 = new Item[(1)];
            var10000[(0)] = var2.eD();
            if (!InvUtils.findInHotbar(var10000).found()) {
               this.aBr = null;
               this.fG();
               return null;
            } else {
               if (this.eX(var2)) {
                  char var5 = 38661;
               } else {
                  char var6 = 38662;
               }

               float[] var3 = this.fA(var1);
               if (var3 == null) {
                  return null;
               } else {
                  if (this.fz(var3)) {
                     if (this.fD(var1, var3)) {

                     } else {
                        char var7 = 50421;
                     }

                     this.fC();
                     var3 = this.fx(var1.eN());
                     if (var3 == null) {
                        return null;
                     }
                  }

                  if (!this.uu && this.adi == null && this.aJD == null) {
                     float[] var4 = var3;
                     return this.eW(() -> this.fE(var4, var1));
                  } else {
                     return this.eW(() -> {});
                  }
               }
            }
         }
      } else {
         return null;
      }
   }

   public void suspendForItemUse() {
      this.fG();
   }

   public int combatPriority() {
      return (120);
   }

   private AliceObf_y eW(Runnable var1) {
      return new AliceObf_oi(this, var1);
   }

   private boolean eX(AliceObf_hZ var1) {
      return (boolean)(var1 == AliceObf_hZ.apE);
   }

   private AliceObf_bz eY(AliceObf_hZ var1) {
      double var2 = (Double)this.aQm.get();
      double var4 = var2 * var2;
      AliceObf_bz var6 = null;
      double var7 = Double.MAX_VALUE;
      int var9 = 0;

      for (PlayerEntity var11 : super.mc.world.getPlayers()) {
         this.eZ(var11);
         if (this.fe(var11, var4)) {
            Vec3d var12 = this.fa(var11);
            BlockPos var13 = this.fh(var11);
            AliceObf_bz var14 = var13 == null
               ? null
               : this.fk(
                  var1,
                  var11,
                  AntiPhasePlusPlus.fc(var11, null, 0),
                  var13
               );
            int var15 = (0);
            if (var14 == null) {
               var14 = this.fj(var1, var11, var12, var13);
               var15 = var14 != null ? (1) : (0);
            }

            if (var14 != null) {
               double var16 = this.Si != null && this.Si.equals(var14.eK()) ? -1.0 : super.mc.player.squaredDistanceTo(var11);
               if (var16 < var7) {
                  var7 = var16;
                  var6 = var14;
                  var9 = var15;
               }
            }
         }
      }

      this.cW = (var6 != null && var9 != 0);
      return var6;
   }

   private void eZ(PlayerEntity var1) {
      int var2 = var1.getId();
      Vec3d var3 = AntiPhasePlusPlus.fb(var1);
      Vec3d var4 = this.ayJ.put(var2, var3);
      Integer var5 = this.aOj.put(var2, this.Fl);
      if (var4 != null && var5 != null && this.Fl - var5 == 1) {
         Vec3d var6 = var3.subtract(var4);
         Vec3d var7 = (Vec3d)this.vV.get(var2);
         this.vV.put(var2, var7 == null ? var6 : var7.multiply(0.4).add(var6.multiply(0.6)));
      } else {
         this.vV.remove(var2);
      }
   }

   private Vec3d fa(PlayerEntity var1) {
      Vec3d var2 = (Vec3d)this.vV.get(var1.getId());
      return var2 == null ? Vec3d.ZERO : var2;
   }

   private static Vec3d fb(PlayerEntity var0) {
      PositionInterpolator var1 = var0.getInterpolator();
      Vec3d var2 = var1 == null ? null : var1.getLerpedPos();
      return var2 == null ? var0.getEntityPos() : var2;
   }

   private static Box fc(PlayerEntity var0, Vec3d var1, int var2) {
      Vec3d var3 = AntiPhasePlusPlus.fb(var0);
      Vec3d var4 = var0.getEntityPos();
      Box var5 = var0.getBoundingBox().offset(var3.x - var4.x, var3.y - var4.y, var3.z - var4.z);
      if (!(var2 > 0)) {
         short var6 = 6767;
      }

      return var5;
   }

   private AliceObf_bz fd(AliceObf_hZ var1) {
      if (this.Si != null) {
         if (!(this.fG != null)) {
            short var3 = 4080;
         }
      }

      return null;
   }

   private boolean fe(PlayerEntity var1, double var2) {
      if (var1 != super.mc.player && var1.isAlive() && !var1.isSpectator()) {
         if (var1.isSwimming()) {

         } else {
            char var4 = 33743;
         }

         if ((Boolean)this.aFJ.get()) {

         } else {
            short var5 = 28189;
         }

         if ((Boolean)this.s.get() && !this.ff(var1)) {
            return false;
         } else if (!(var1.getBoundingBox().getLengthY() <= 1.0)) {
            return (super.mc.player.squaredDistanceTo(var1) <= var2);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean ff(PlayerEntity var1) {
      EquipmentSlot[] var2 = AntiPhasePlusPlus.nR;
      int var3 = var2.length;

      for (int var4 = (0); var4 < var3; var4++) {
         EquipmentSlot var5 = var2[var4];
         if (!var1.hasStackEquipped(var5)) {
            return false;
         }
      }

      return true;
   }

   private BlockPos fg(Box var1, double var2) {
      Box var4 = var1.expand(-0.0625, 0.0, -0.0625);
      double var5 = Math.round(var2);
      BlockPos var7 = BlockPos.ofFloored(var4.minX, var5, var4.minZ);
      if (!BlockPos.ofFloored(var4.minX, var5, var4.maxZ).equals(var7)) {
         return null;
      } else if (!BlockPos.ofFloored(var4.maxX, var5, var4.minZ).equals(var7)) {
         return null;
      } else {
         return BlockPos.ofFloored(var4.maxX, var5, var4.maxZ).equals(var7) ? var7 : null;
      }
   }

   private BlockPos fh(PlayerEntity var1) {
      return this.fg(
         AntiPhasePlusPlus.fc(var1, null, 0),
         AntiPhasePlusPlus.fb(var1).y
      );
   }

   private BlockPos fi(PlayerEntity var1, Vec3d var2, int var3) {
      if (var3 > 0 && var2 != null) {
         return var2.horizontalLengthSquared() < 1.0E-6
            ? null
            : this.fg(
               AntiPhasePlusPlus.fc(var1, var2, var3),
               AntiPhasePlusPlus.fb(var1).y
                  + var2.y * var3
            );
      } else {
         return null;
      }
   }

   private AliceObf_bz fj(AliceObf_hZ var1, PlayerEntity var2, Vec3d var3, BlockPos var4) {
      for (int var5 = (1); var5 <= 2; var5++) {
         BlockPos var6 = this.fi(var2, var3, var5);
         if (var6 != null && !var6.equals(var4)) {
            AliceObf_bz var7 = this.fk(
               var1, var2, AntiPhasePlusPlus.fc(var2, var3, var5), var6
            );
            if (var7 != null) {
               return var7;
            }
         }
      }

      return null;
   }

   private AliceObf_bz fk(AliceObf_hZ var1, PlayerEntity var2, Box var3, BlockPos var4) {
      if (super.mc.world.isOutOfHeightLimit(var4)) {
         return null;
      } else {
         if (!super.mc.world.getBlockState(var4).isAir()) {

         } else {
            short var20 = 5573;
         }

         if (this.aHN.containsKey(var4)) {

         } else {
            char var21 = 62286;
         }

         if (this.fn(var4)) {
            return null;
         } else {
            Box var5 = var1.eF() ? var3 : null;
            Direction[] var6 = !var1.eE()
               ? AntiPhasePlusPlus.arz
               : AntiPhasePlusPlus.aPI;
            Object var7 = null;
            double var8 = Double.MAX_VALUE;

            for (int var10 = 0; var10 < var6.length; var10++) {
               Direction var11 = var6[var10];
               Box var12 = AntiPhasePlusPlus.fo(
                  var4, var11, var1.eG()
               );
               if (var5 == null || !var12.intersects(var5)) {
                  BlockPos var13 = var4.offset(var11);
                  Direction var14 = var11.getOpposite();
                  if (!this.fl(var13, var14)) {

                  } else {
                     char var22 = 46041;
                  }

                  Vec3d var15 = AntiPhasePlusPlus.fp(var13, var14);
                  if (this.fq(var15) && this.ft(var14, var15) && this.fs(var4, var13, var14, var15)) {
                     double var16 = super.mc.player.getEyePos().squaredDistanceTo(var15);
                     if (!(var16 < var8)) {

                     } else {
                        char var23 = 57806;
                     }
                  }
               }
            }

            if (var7 == null) {
               return null;
            } else {
               BlockPos var18 = var4.offset((Direction)var7);
               Direction var19 = ((Direction)var7).getOpposite();
               return new AliceObf_bz(
                  var4,
                  var18,
                  var19,
                  AntiPhasePlusPlus.fp(var18, var19),
                  AntiPhasePlusPlus.fo(var4, (Direction)var7, var1.eG()),
                  var2.getId()
               );
            }
         }
      }
   }

   private boolean fl(BlockPos var1, Direction var2) {
      if (super.mc.world.isOutOfHeightLimit(var1)) {
         return false;
      } else {
         BlockState var3 = super.mc.world.getBlockState(var1);
         if (var3.isAir() || var3.isReplaceable()) {
            return false;
         } else {
            return !(BlockUtils.isClickable(var3.getBlock()));
         }
      }
   }

   private boolean fm(BlockPos var1) {
      BlockPos var2 = var1.down();
      if (super.mc.world.getBlockState(var2).isSideSolidFullSquare(super.mc.world, var2, Direction.UP)) {
         return true;
      } else {
         for (Direction var6 : AntiPhasePlusPlus.aPI) {
            if (super.mc.world.getBlockState(var1.offset(var6)).isOf(Blocks.SCAFFOLDING)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean fn(BlockPos var1) {
      return (!super.mc.world.getEntitiesByClass(ItemFrameEntity.class, new Box(var1), var0 -> true).isEmpty());
   }

   private static Box fo(BlockPos var0, Direction var1, double var2) {
      double var4 = var0.getX();
      double var6 = var0.getY();
      double var8 = var0.getZ();
      double var10 = Math.min(Math.max(var2, 0.0), 1.0);
      int var12 = AliceObf_kd.jH[var1.ordinal()] ^ 1557183485;
      Box var10000;
      if (var12 == 1557183480) {
         var10000 = new Box(var4, var6, var8, var4 + var10, var6 + 1.0, var8 + 1.0);
      } else if (var12 == 1557183481) {
         var10000 = new Box(var4, var6, var8 + 1.0 - var10, var4 + 1.0, var6 + 1.0, var8 + 1.0);
      } else if (var12 != 1557183483) {
         if (var12 != 1557183484) {
            if (var12 == 1557183486) {
               var10000 = new Box(var4, var6, var8, var4 + 1.0, var6 + 1.0, var8 + var10);
            } else {
               if (var12 != 1557183487) {
                  throw new MatchException(null, null);
               }

               var10000 = new Box(var4, var6 + 1.0 - var10, var8, var4 + 1.0, var6 + 1.0, var8 + 1.0);
            }
         } else {
            var10000 = new Box(var4, var6, var8, var4 + 1.0, var6 + var10, var8 + 1.0);
         }
      } else {
         var10000 = new Box(var4 + 1.0 - var10, var6, var8, var4 + 1.0, var6 + 1.0, var8 + 1.0);
      }

      return var10000;
   }

   private static Vec3d fp(BlockPos var0, Direction var1) {
      return var0.toCenterPos().offset(var1, 0.5);
   }

   private boolean fq(Vec3d var1) {
      double var2 = this.fr();
      return (super.mc.player.getEyePos().squaredDistanceTo(var1) <= var2 * var2);
   }

   private double fr() {
      return Math.min(4.5, super.mc.player.getBlockInteractionRange());
   }

   private boolean fs(BlockPos var1, BlockPos var2, Direction var3, Vec3d var4) {
      Item var5 = ((AliceObf_hZ)this.sL.get()).eD();
      if (var5 instanceof BlockItem var6) {
         ItemPlacementContext var7 = new ItemPlacementContext(super.mc.player, Hand.MAIN_HAND, new ItemStack(var5), new BlockHitResult(var4, var3, var2, false));
         if (var7.canPlace()) {

         } else {
            char var9 = 44554;
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean ft(Direction var1, Vec3d var2) {
      return !(!(var1.getDoubleVector().dotProduct(super.mc.player.getEyePos().subtract(var2)) > 0.0));
   }

   private void fu(AliceObf_bz var1, float[] var2) {
      if (AliceObf_fS.OE(this)) {
         if (!(super.mc.player != null)) {
            short var5 = 2615;
         }
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean fv(FindItemResult var1, BlockHitResult var2, float[] var3) {
      if (!this.fw(var1)) {
         return false;
      } else {
         this.qc = true;
         this.arE = false;
         boolean var7 = false /* VF: Semaphore variable */;

         byte var10;
         label72: {
            label71: {
               try {
                  var7 = true;
                  if (var3 != null && !this.fz(var3)) {
                     var10 = 0;
                     var7 = false;
                     break label72;
                  }

                  super.mc.interactionManager.interactBlock(super.mc.player, Hand.MAIN_HAND, var2);
                  if (!this.arE) {
                     var10 = 0;
                     var7 = false;
                     break label71;
                  }

                  super.mc.player.swingHand(Hand.MAIN_HAND);
                  var10 = 1;
                  var7 = false;
               } finally {
                  if (var7) {
                     this.qc = false;
                     AliceObf_fS.Ov(this);
                  }
               }

               this.qc = false;
               AliceObf_fS.Ov(this);
               return (var10 != 0);
            }

            this.qc = false;
            AliceObf_fS.Ov(this);
            return (var10 != 0);
         }

         this.qc = false;
         AliceObf_fS.Ov(this);
         return (var10 != 0);
      }
   }

   private boolean fw(FindItemResult var1) {
      return (boolean)(!var1.found()
         ? 0
         : AliceObf_fS.Of(this, var1.slot()));
   }

   private float[] fx(Vec3d var1) {
      if (var1 != null && super.mc.player != null) {
         Vec3d var2 = super.mc.player.getEyePos();
         double var3 = var1.x - var2.x;
         double var5 = var1.y - var2.y;
         double var7 = var1.z - var2.z;
         double var9 = Math.sqrt(var3 * var3 + var7 * var7);
         double var11 = 0.08;
         float[] var10000 = new float[2];
         var10000[(0)] = (float)(Math.toDegrees(Math.atan2(var7, var3)) - 90.0 + this.fy(var11));
         var10000[(1)] = (float)(-Math.toDegrees(Math.atan2(var5, var9)) + this.fy(var11));
         return var10000;
      } else {
         return null;
      }
   }

   private double fy(double var1) {
      if (!(var1 <= 0.0)) {
         char var3 = 55121;
      }

      return (this.agx.nextDouble() * 2.0 - 1.0) * var1;
   }

   private boolean fz(float[] var1) {
      return (var1 != null
            && AliceObf_fS.OK()
            && AliceObf_fS.Qr(
               AliceObf_fS.OI(),
               AliceObf_fS.OJ(),
               var1[(0)],
               var1[1]
            ));
   }

   private float[] fA(AliceObf_bz var1) {
      if (this.Si != null && this.aPt == var1.eM() && this.Si.equals(var1.eK()) && this.fG != null && this.fG.equals(var1.eL())) {
         float[] var10000 = new float[2];
         var10000[(0)] = this.afC;
         var10000[1] = this.jV;
         return var10000;
      } else {
         return this.fx(var1.eN());
      }
   }

   private void fB(AliceObf_bz var1, float[] var2) {
      if (var1 != null && var2 != null) {
         this.Si = var1.eK();
         this.fG = var1.eL();
         this.aPt = var1.eM();
         this.afC = var2[(0)];
         this.jV = var2[1];
         this.AX = this.Fl;
      }
   }

   private void fC() {
      this.Si = null;
      this.fG = null;
      this.aPt = null;
   }

   private boolean fD(AliceObf_bz var1, float[] var2) {
      if (super.mc.player != null && super.mc.world != null && var2 != null) {
         double var3 = Math.toRadians(var2[0]);
         double var5 = Math.toRadians(var2[1]);
         double var7 = Math.cos(var5);
         Vec3d var9 = new Vec3d(-Math.sin(var3) * var7, -Math.sin(var5), Math.cos(var3) * var7);
         Vec3d var10 = super.mc.player.getEyePos();
         BlockHitResult var11 = VoxelShapes.fullCube().raycast(var10, var10.add(var9.multiply(this.fr())), var1.eL());
         if (var11 != null && var11.getSide() == var1.eM()) {
            if (var11.getBlockPos().equals(var1.eL())) {

            } else {
               char var12 = 44425;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void fE(float[] var1, AliceObf_bz var2) {
      if (!this.uu && var1 != null && AliceObf_fS.OE(this)) {
         this.fF(var1);
         if (this.uu) {
            this.fB(var2, var1);
         }
      }
   }

   private void fF(float[] var1) {
      int var10000 = this.uu ? 34817 : 34816;

      while (true) {
         int var2 = var10000;
         if (var2 == 34816) {
            if (this.adi == null && var1 != null) {
               this.abd = var1[0];
               this.aop = var1[1];
               this.uu = true;
               this.avV = this.Fl;
               this.adi = new AliceObf_mO(this.abd, this.aop);
               return;
            }
            break;
         }

         if (var2 != 34817) {
            break;
         }

      }
   }

   private void fG() {
      this.fC();
      this.adi = null;
      this.uu = false;
      AliceObf_sQ var1 = this.aJD;
      if (var1 != null) {
         var1.eQ(true);
         AliceObf_au.Fy(super.mc.player, var1);
         this.aJD = null;
      }
   }

   private void fH() {
      this.aOj.entrySet().removeIf(var1 -> {
         if (this.Fl - (Integer)var1.getValue() > 40) {

         } else {
            char var2 = 54309;
         }

         return false;
      });
      this.ayJ.keySet().removeIf(var1 -> (!this.aOj.containsKey(var1)));
      this.vV.keySet().removeIf(var1 -> !this.aOj.containsKey(var1));
      if (!this.aHN.isEmpty()) {
         this.aHN.entrySet().removeIf(var1 -> !(this.Fl < var1.getValue() && super.mc.world.getBlockState((BlockPos)var1.getKey()).isAir()));
      }
   }

   @AliceHandler
   private void fI(Render3DEvent var1) {
      if ((Boolean)this.aiQ.get() && super.mc.world != null) {
         AliceObf_bz var2 = this.aBr;
         if (var2 != null) {
            if (!(this.Si != null)) {
               char var6 = 48011;
            }

            int var3 = (0);
            Color var4 = var3 != 0 ? (Color)this.Gh.get() : (Color)this.jx.get();
            this.fJ(
               var1, var2, this.cW ? AntiPhasePlusPlus.fK(var4) : var4
            );
         }

         AliceObf_bz var5 = this.HU;
         if (var5 != null && this.Fl - this.acz <= 20) {
            this.fJ(var1, var5, (Color)this.pM.get());
         }
      }
   }

   private void fJ(Render3DEvent var1, AliceObf_bz var2, Color var3) {
      Box var4 = var2.eO().expand(0.002);
      Direction var5 = var2.eM().getOpposite();
      Vec3d[] var6 = AntiPhasePlusPlus.fN(var4, var5);
      Vec3d var7 = var4.getCenter();
      double var8 = 0.3;

      for (int var10 = (0); var10 < (4); var10++) {
         Vec3d var11 = var6[var10];
         this.fM(
            var1,
            var11,
            AntiPhasePlusPlus.fL(
               var11, var6[(var10 + (1)) % 4], var8
            ),
            var3
         );
         this.fM(
            var1,
            var11,
            AntiPhasePlusPlus.fL(var11, var6[(var10 + 3) % 4], var8),
            var3
         );
      }

      Vec3d var17 = var6[(1)].subtract(var6[0]).multiply(var8 * 0.5);
      Vec3d var18 = var6[(3)].subtract(var6[0]).multiply(var8 * 0.5);
      this.fM(var1, var7.subtract(var17), var7.add(var17), var3);
      this.fM(var1, var7.subtract(var18), var7.add(var18), var3);
      Vec3d var12 = var5.getDoubleVector().multiply(0.18);

      for (Vec3d var16 : var6) {
         this.fM(var1, var16, var16.add(var12), var3);
      }
   }

   private static Color fK(Color var0) {
      return new Color(var0.r, var0.g, var0.b, Math.max(var0.a / 2, 40));
   }

   private static Vec3d fL(Vec3d var0, Vec3d var1, double var2) {
      return var0.add(var1.subtract(var0).multiply(var2));
   }

   private void fM(Render3DEvent var1, Vec3d var2, Vec3d var3, Color var4) {
      var1.renderer.line(var2.x, var2.y, var2.z, var3.x, var3.y, var3.z, var4);
   }

   private static Vec3d[] fN(Box var0, Direction var1) {
      int var2 = AliceObf_kd.jH[var1.ordinal()];
      Vec3d[] var10000;
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 3) {
               if (var2 != 4) {
                  if (var2 != 5) {
                     if (var2 != 6) {
                        throw new MatchException(null, null);
                     }

                     var10000 = new Vec3d[]{
                        new Vec3d(var0.maxX, var0.minY, var0.minZ),
                        new Vec3d(var0.maxX, var0.minY, var0.maxZ),
                        null,
                        null
                     };
                     var10000[(2)] = new Vec3d(var0.maxX, var0.maxY, var0.maxZ);
                     var10000[3] = new Vec3d(var0.maxX, var0.maxY, var0.minZ);
                  } else {
                     var10000 = new Vec3d[4];
                     var10000[(0)] = new Vec3d(var0.minX, var0.minY, var0.minZ);
                     var10000[(1)] = new Vec3d(var0.minX, var0.minY, var0.maxZ);
                     var10000[(2)] = new Vec3d(var0.minX, var0.maxY, var0.maxZ);
                     var10000[(3)] = new Vec3d(var0.minX, var0.maxY, var0.minZ);
                  }
               } else {
                  var10000 = new Vec3d[4];
                  var10000[(0)] = new Vec3d(var0.minX, var0.minY, var0.maxZ);
                  var10000[(1)] = new Vec3d(var0.maxX, var0.minY, var0.maxZ);
                  var10000[(2)] = new Vec3d(var0.maxX, var0.maxY, var0.maxZ);
                  var10000[(3)] = new Vec3d(var0.minX, var0.maxY, var0.maxZ);
               }
            } else {
               var10000 = new Vec3d[4];
               var10000[(0)] = new Vec3d(var0.minX, var0.minY, var0.minZ);
               var10000[1] = new Vec3d(var0.maxX, var0.minY, var0.minZ);
               var10000[(2)] = new Vec3d(var0.maxX, var0.maxY, var0.minZ);
               var10000[3] = new Vec3d(var0.minX, var0.maxY, var0.minZ);
            }
         } else {
            var10000 = new Vec3d[(4)];
            var10000[(0)] = new Vec3d(var0.minX, var0.maxY, var0.minZ);
            var10000[(1)] = new Vec3d(var0.maxX, var0.maxY, var0.minZ);
            var10000[(2)] = new Vec3d(var0.maxX, var0.maxY, var0.maxZ);
            var10000[(3)] = new Vec3d(var0.minX, var0.maxY, var0.maxZ);
         }
      } else {
         var10000 = new Vec3d[4];
         var10000[(0)] = new Vec3d(var0.minX, var0.minY, var0.minZ);
         var10000[1] = new Vec3d(var0.maxX, var0.minY, var0.minZ);
         var10000[2] = new Vec3d(var0.maxX, var0.minY, var0.maxZ);
         var10000[(3)] = new Vec3d(var0.minX, var0.minY, var0.maxZ);
      }

      return var10000;
   }

      private static Vec2f fO(PlayerInput var0) {
      float var1 = (var0.left() ? 1.0F : 0.0F) - (var0.right() ? 1.0F : 0.0F);
      float var2 = (var0.forward() ? 1.0F : 0.0F) - (var0.backward() ? 1.0F : 0.0F);
      return new Vec2f(var1, var2).normalize();
   }

   static {

      Direction[] var10000 = new Direction[(4)];
      var10000[0] = Direction.NORTH;
      var10000[1] = Direction.EAST;
      var10000[2] = Direction.SOUTH;
      var10000[(3)] = Direction.WEST;
      aPI = var10000;
      var10000 = new Direction[(6)];
      var10000[0] = Direction.DOWN;
      var10000[(1)] = Direction.NORTH;
      var10000[(2)] = Direction.EAST;
      var10000[(3)] = Direction.SOUTH;
      var10000[(4)] = Direction.WEST;
      var10000[(5)] = Direction.UP;
      arz = var10000;
      EquipmentSlot[] var1 = new EquipmentSlot[(4)];
      var1[(0)] = EquipmentSlot.HEAD;
      var1[1] = EquipmentSlot.CHEST;
      var1[2] = EquipmentSlot.LEGS;
      var1[(3)] = EquipmentSlot.FEET;
      nR = var1;
   }

   }

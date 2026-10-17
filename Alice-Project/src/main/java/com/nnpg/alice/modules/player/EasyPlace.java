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
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BooleanSupplier;
import meteordevelopment.meteorclient.events.entity.player.InteractBlockEvent;
import meteordevelopment.meteorclient.events.entity.player.InteractItemEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.SlotUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.PlayerInput;
import net.minecraft.network.packet.s2c.play.PlayerRotationS2CPacket;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.util.math.Direction;
import net.minecraft.block.HopperBlock;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.util.hit.HitResult;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.LoomBlock;
import net.minecraft.util.math.Vec2f;
import net.minecraft.block.ObserverBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.text.Text;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.ChestType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.IntProperty;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.block.CartographyTableBlock;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.block.SmithingTableBlock;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.block.enums.Orientation;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.block.CrafterBlock;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Direction.Type;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class EasyPlace extends AliceModule {
   private static final int QM = 40;
   private static final int ajB = 40;
   private static final int aoT = 120;
   private static final int DL = 2;
   private static final double aCt = 30.25;
   private static final int aKR = 80;
   private static final int ajW = 4;
   private static final int Yv = 20;
   private static final int aME = 4;
   private static final int aOw = 100;
   private static final int arb = 40;
   private static final int aJZ = 40;
   private static final int Vc = 3;
   private static final int amH = 60;
   private static EasyPlace UG;
   private static final Block[] fS = new Block[]{
      Blocks.HOPPER,
      Blocks.DROPPER,
      Blocks.DISPENSER,
      Blocks.CHEST,
      Blocks.TRAPPED_CHEST,
      Blocks.ENDER_CHEST,
      Blocks.BARREL,
      Blocks.SHULKER_BOX,
      Blocks.WHITE_SHULKER_BOX,
      Blocks.ORANGE_SHULKER_BOX,
      Blocks.MAGENTA_SHULKER_BOX,
      Blocks.LIGHT_BLUE_SHULKER_BOX,
      Blocks.YELLOW_SHULKER_BOX,
      Blocks.LIME_SHULKER_BOX,
      Blocks.PINK_SHULKER_BOX,
      Blocks.GRAY_SHULKER_BOX,
      Blocks.LIGHT_GRAY_SHULKER_BOX,
      Blocks.CYAN_SHULKER_BOX,
      Blocks.PURPLE_SHULKER_BOX,
      Blocks.BLUE_SHULKER_BOX,
      Blocks.BROWN_SHULKER_BOX,
      Blocks.GREEN_SHULKER_BOX,
      Blocks.RED_SHULKER_BOX,
      Blocks.BLACK_SHULKER_BOX,
      Blocks.COMPARATOR,
      Blocks.REPEATER,
      Blocks.OBSERVER,
      Blocks.REDSTONE_WIRE,
      Blocks.PISTON,
      Blocks.STICKY_PISTON
   };
   private final SettingGroup sG = this.settings.getDefaultGroup();
   private final SettingGroup aIq;
   private final SettingGroup aJy;
   private final Setting<Boolean> anM;
   private final Setting<Boolean> aBD;
   private final Setting<Boolean> aI;
   private final Setting<List<Block>> fg;
   private final Setting<Boolean> TL;
   private final Setting<Boolean> aJE;
   private final Setting<AliceObf_mH> md;
   private final Setting<Boolean> be;
   private final Setting<AliceObf_hL> ZG;
   private final Setting<AliceObf_mD> aIO;
   private final Setting<AliceObf_hi> aOG;
   private final Setting<Boolean> Cf;
   private final Setting<Boolean> aIU;
   private final Setting<Boolean> aoJ;
   private final Setting<Double> aEK;
   private final Setting<Integer> Gr;
   private final Setting<Boolean> Lv;
   private final Setting<Boolean> Bs;
   private boolean axK;
   private boolean amq;
   private int lM;
   private AliceObf_qK aht;
   private final AliceObf_mX ee;
   private AliceObf_kZ Pj;
   private boolean ky;
   private AliceObf_qK Mh;
   private AliceObf_uR aFU;
   private AliceObf_km iX;
   private final Map<BlockPos, AliceObf_hy> adJ;
   private final Map<BlockPos, AliceObf_nF> axE;
   private final ConcurrentLinkedQueue<AliceObf_dc> abb;
   private int alT;
   private boolean OR;
   private int aLV;
   private int aaz;
   private int rA;
   private int aQV;
   private AliceObf_pA ahI;
   private AliceObf_kk aoD;
   private boolean aPu;
   private AliceObf_tC apB;
   private int aEk;
   private int aaH;
   private boolean Ko;
   private final Map<Item, Boolean> avW;
   private boolean apP;
   private float FN;
   private float nz;
   private ClientPlayerEntity NT;
   private final Map<BlockPos, Integer> akX;
   private int alh;
   private String yV;
   private boolean aHL;
   private AliceObf_dt agC;
   private AliceObf_cu MS;
   private boolean aIk;
   private Object DQ;
   private AliceObf_dd JN;
   private Method acW;
   private Method yw;
   private boolean ZE;
   private boolean gB;
   private Input avl;
   private AliceObf_em jB;
   public EasyPlace() {
      super(AliceAddon.CATEGORY, "easy-place", "Dat redstone va khoi len cac khoi co tuong tac ma khong can cui, kem ho tro dat thong minh cho Litematica.");
      this.aIq = this.settings.createGroup("Rotation");
      this.aJy = this.settings.createGroup("Smart Place");
      this.anM = this.sG.add(((Builder)((Builder)((Builder)new Builder().name("all-blocks")).description("cho phep easy place hoat dong voi moi loai block")).defaultValue(true)).build());
      this.aBD = this.sG.add(((Builder)((Builder)((Builder)new Builder().name("auto-bucket")).description("tu dong chon xo va dat nguon nuoc/lava theo schematic")).defaultValue(false)).build());
      this.aI = this.sG.add(((Builder)((Builder)((Builder)new Builder().name("material-override")).description("cho phep thay cac loai kinh cho nhau, giu rieng kinh khoi va kinh tam")).defaultValue(false)).build());
      this.fg = this.sG
         .add(
            ((meteordevelopment.meteorclient.settings.BlockListSetting.Builder)((meteordevelopment.meteorclient.settings.BlockListSetting.Builder)((meteordevelopment.meteorclient.settings.BlockListSetting.Builder)new meteordevelopment.meteorclient.settings.BlockListSetting.Builder()
                        .name("blocks"))
                     .description("cac block ma easy place duoc phep dat khi tat che do All Blocks"))
                  .defaultValue(fS)
                  .filter(EasyPlace::MB)
                  .visible(() -> !(Boolean)this.anM.get()))
               .build()
         );
      this.TL = this.sG.add(((Builder)((Builder)((Builder)new Builder().name("show-status-message")).description("hien thi bat/tat Easy Place o action bar")).defaultValue(true)).build());
      this.aJE = this.aIq.add(((Builder)((Builder)((Builder)new Builder().name("rotation-spoof")).description("lang le gia mao huong xoay khi dat cac block co huong")).defaultValue(true)).build());
      this.md = this.aIq
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("direction"))
                     .description("huong dat block. tu dong doc huong tu litematica. van ap dung cho Schematic Aura va hopper khi tat Rotation Spoof"))
                  .defaultValue(AliceObf_mH.Lb))
               .build()
         );
      this.be = this.aJy.add(((Builder)((Builder)((Builder)new Builder().name("smart-placement")).description("chon block dung yeu cau cua litematica truoc khi dat")).defaultValue(true)).build());
      this.ZG = this.aJy
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("inventory-pick-source"))
                        .description("chon cac block xac nhan tren the gioi de chuyen item vao inventory"))
                     .defaultValue(AliceObf_hL.kT))
                  .visible(this.be::get))
               .build()
         );
      this.aIO = this.aJy
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("inventory-pick-sync"))
                        .description("ServerAck se cho server xac nhan chuyen tui do. SameTick du doan ngay lap tuc nhung co the bi server grim phat hien"))
                     .defaultValue(AliceObf_mD.aHJ))
                  .visible(this.be::get))
               .build()
         );
      this.aOG = this.aJy
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("dependency-sync"))
                        .description("ServerAck cho server xac nhan tung block. Immediate cho Schematic Aura noi chuoi tren block vua du doan de nhanh hon nhung de bi server Grim phat hien hon"))
                     .defaultValue(AliceObf_hi.Zz))
                  .visible(this.be::get))
               .build()
         );
      this.Cf = this.aJy.add(((Builder)((Builder)((Builder)new Builder().name("schematic-only")).description("ngan dat block khac vao vung litematica da chon")).defaultValue(false)).build());
      this.aIU = this.aJy
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("schematic-lock")).description("chi dat block trong o schematic Litematica dang render va khong phai air. chi ap dung ben trong vung schematic da chon, bo qua o dang bi an theo layer")).defaultValue(false)).visible(this.Cf::get)).build());
      this.aoJ = this.aJy
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("schematic-aura")).description("tu dong dat cac block litematica o gan ma khong can giu chuot phai. can bat Smart Placement hoac Schematic Only")).defaultValue(false))
                  .visible(() -> (Boolean)this.be.get() || (Boolean)this.Cf.get()))
               .build()
         );
      this.aEK = this.aJy
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("aura-range"))
                     .description("khoang cach toi da de quet block schematic. van ap dung tam voi vanilla"))
                  .defaultValue(4.0)
                  .min(1.0)
                  .max(6.0)
                  .sliderMax(5.0)
                  .visible(() -> (Boolean)this.aoJ.get() && ((Boolean)this.be.get() || (Boolean)this.Cf.get())))
               .build()
         );
      this.Gr = this.aJy
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("aura-delay"))
                        .description("so tick toi thieu giua hai lan tu dong dat schematic"))
                     .defaultValue(4))
                  .range(1, 20)
                  .sliderRange(1, 10)
                  .visible(() -> (Boolean)this.aoJ.get() && ((Boolean)this.be.get() || (Boolean)this.Cf.get())))
               .build()
         );
      this.Lv = this.aJy.add(((Builder)((Builder)((Builder)new Builder().name("hopper-force-facing")).description("ep hopper huong dung theo schematic Litematica thay vi theo huong dang nhin")).defaultValue(true)).build());
      this.Bs = this.aJy.add(((Builder)((Builder)((Builder)new Builder().name("edge-creep")).description("dung sneak di sat ria block do de dat duoc block nho ra ngoai (mai/san cong). Chi la input vanilla: sneak + tien, khong sua velocity, va vanilla tu chan khong cho roi.")).defaultValue(true)).build());
      this.ee = new AliceObf_mX();
      this.adJ = new ConcurrentHashMap<>();
      this.axE = new ConcurrentHashMap<>();
      this.abb = new ConcurrentLinkedQueue<>();
      this.aEk = -1;
      this.aaH = Integer.MAX_VALUE;
      this.avW = new ConcurrentHashMap<>();
      this.akX = new ConcurrentHashMap<>();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.ME();
         this.DQ = this.mc.world;
         this.Kg();
         if (this.IM()) {
            if (!FabricLoader.getInstance().isModLoaded("litematica")) {
               this.error("Easy Place requires Litematica for Smart Placement, Schematic Only, Schematic Lock and Schematic Aura.", new Object[0]);
               this.IN();
               return;
            }

            int var10000 = !AliceObf_gZ.initialize()
               ? 59508
               : 59509;

            while (true) {
               int var1 = var10000;
               if (var1 == 59508) {
                  this.error("The installed Litematica version is not compatible with Easy Place.", new Object[0]);
                  this.IN();
                  return;
               }

               if (var1 != 59509) {
                  break;
               }

            }
         }

         if (!this.MF()) {
            this.error("Disable or remove the standalone Easy Redstone mod before using Easy Place.", new Object[0]);
            this.IN();
         } else {
            UG = this;
            super.onActivate();
            this.MH(true);
            this.HT();
         }
      }
   }

   private void HT() {
      AliceObf_oN var1 = AliceObf_oN.activeCompanionBuilder();
      if (var1 != null) {
         int var2 = var1.companionLayerCeiling();
         if (var2 != Integer.MAX_VALUE) {
            this.setCompanionLayerCeiling(var2);
         }
      }
   }

   public boolean HU(double var1) {
      if (!this.isActive()) {
         try {
            this.toggle();
         } catch (RuntimeException var10) {
         }

         if (!this.isActive()) {
            return false;
         }
      }

      if (!AliceObf_gZ.initialize()) {
         return false;
      } else {
         if (!(Boolean)this.be.get()) {
            int var10000 = !this.Cf.get() ? 4183 : 4184;

            while (true) {
               int var6 = var10000;
               if (var6 == 4183) {
                  try {
                     this.be.set(true);
                  } catch (RuntimeException var9) {
                  }
                  break;
               }

               if (var6 != 4184) {
                  break;
               }

            }
         }

         if (!(Boolean)this.aoJ.get()) {
            try {
               this.aoJ.set(true);
            } catch (RuntimeException var8) {
            }

            this.aaz = 0;
         }

         if (var1 > 0.0) {
            double var3 = Math.min(var1, 6.0);
            if ((Double)this.aEK.get() < var3) {
               try {
                  this.aEK.set(var3);
               } catch (RuntimeException var7) {
               }
            }
         }

         return (Boolean)this.aoJ.get() && ((Boolean)this.be.get() || (Boolean)this.Cf.get());
      }
   }

   public boolean HV() {
      return this.isActive() && (Boolean)this.aoJ.get() && ((Boolean)this.be.get() || (Boolean)this.Cf.get());
   }

   public boolean HW() {
      if (!this.axK && !this.amq && this.aht == null) {
         int var10000 = this.iX == null ? 57924 : 57925;

         while (true) {
            int var1 = var10000;
            if (var1 == 57924) {
               if (this.aFU == null && (this.agC == null || this.agC.aQc) && this.JN == null && this.apB == null) {
                  return false;
               }
               break;
            }

            if (var1 != 57925) {
               break;
            }

         }
      }

      return true;
   }

   public void setCompanionMovementLock(boolean var1) {
      this.aQV = var1 ? 3 : 0;
   }

   private boolean HX() {
      int var10000 = this.aQV <= 0 ? 33064 : 33065;

      while (true) {
         int var1 = var10000;
         if (var1 != 33064) {
            if (var1 == 33065) {

               continue;
            }
         } else {
            var10000 = AliceObf_eW.gi() ? 30377 : 30378;

            while (true) {
               int var2 = var10000;
               if (var2 == 30377) {
                  break;
               }

               if (var2 != 30378) {
                  return false;
               }

            }
         }

         return true;
      }
   }

   public void setCompanionLayerCeiling(int var1) {
      this.aaH = var1;
      this.Ko = true;
      AliceObf_qK var2 = this.aht;
      if (var2 != null && var2.akJ == null && !this.Ia(var2.sc)) {
         this.Lc();
      }

      AliceObf_uR var3 = this.aFU;
      if (var3 != null && (!this.Ia(var3.agJ) || !this.Ia(var3.at))) {
         this.aFU = null;
      }

      AliceObf_tC var4 = this.apB;
      if (var4 != null && !this.Ia(var4.Fm)) {
         this.JK();
      }
   }

   public boolean HY() {
      return this.isActive() && (this.apB != null || this.mc.player != null && this.mc.player.age <= this.aEk);
   }

   public void HZ() {
      this.aaH = Integer.MAX_VALUE;
      this.Ko = false;
   }

   private boolean Ia(BlockPos var1) {
      int var10000 = this.Ko ? 62005 : 62006;

      while (true) {
         int var2 = var10000;
         if (var2 == 62005) {
            if (var1 != null) {
               return var1.getY() <= this.aaH;
            }
            break;
         }

         if (var2 != 62006) {
            break;
         }

      }

      return true;
   }

   private boolean Ib(BlockPos var1) {
      AliceObf_oN var2 = AliceObf_oN.activeCompanionBuilder();
      return var2 == null || var2.companionHeightAllows(var1);
   }

   public void setCompanionTemporarySupport(BlockPos var1, BlockState var2, BooleanSupplier var3) {
      this.setCompanionTemporarySupport(var1, var2, var3, () -> {});
   }

   public void setCompanionTemporarySupport(BlockPos var1, BlockState var2, BooleanSupplier var3, Runnable var4) {
      if (var1 != null
         && var2 != null
         && var3 != null
         && var4 != null
         && this.mc.player != null
         && this.mc.world != null
         && (var2.isOf(Blocks.GLASS) || var2.getBlock() instanceof StainedGlassBlock)) {
         AliceObf_kZ var5 = this.Pj;
         if (var5 == null || var5.zW != this.mc.player || var5.AI != this.mc.world || !var5.aBU.equals(var1) || !var5.yT.equals(var2)) {
            this.Ic();
            this.Pj = new AliceObf_kZ(var1.toImmutable(), var2, this.mc.player, this.mc.world, var3, var4);
         }
      } else {
         this.Ic();
      }
   }

   public void Ic() {
      AliceObf_kZ var1 = this.Pj;
      this.Pj = null;
      if (var1 != null) {
         if (this.aht != null && this.aht.VY == var1) {
            this.Lc();
         }

         AliceObf_dt var2 = this.agC;
         int var10000 = var2 != null ? 29786 : 29787;

         while (true) {
            int var3 = var10000;
            if (var3 == 29786) {
               if (var2.XY == var1 && var2.An != AliceObf_rV.Mf && var2.An != AliceObf_rV.aLN) {
                  this.Lo();
               }
               break;
            }

            if (var3 != 29787) {
               break;
            }

         }
      }
   }

   private boolean Id(AliceObf_kZ var1) {
      if (var1 != null
         && this.Pj == var1
         && this.isActive()
         && var1.zW == this.mc.player
         && var1.AI == this.mc.world
         && this.Ia(var1.aBU)
         && ((Boolean)this.anM.get() || ((List)this.fg.get()).contains(var1.yT.getBlock()))) {
         try {
            BlockState var2 = AliceObf_nc.getUnfilteredSchematicState(
               var1.aBU
            );
            return var2 != null && var2.isAir() && var1.xt.getAsBoolean();
         } catch (RuntimeException var3) {
            return false;
         }
      } else {
         return false;
      }
   }

   private AliceObf_kZ Ie(BlockPos var1) {
      AliceObf_kZ var2 = this.Pj;
      return var2 != null && var2.aBU.equals(var1) && this.Id(var2) ? var2 : null;
   }

   private BlockState If(BlockPos var1) {
      AliceObf_kZ var2 = this.Ie(var1);
      return var2 != null ? var2.yT : AliceObf_gZ.getState(var1);
   }

   private boolean Ig(BlockPos var1) {
      AliceObf_kZ var2 = this.Ie(var1);
      return var2 != null && var2.Ur
         ? false
         : AliceObf_gZ.Hh(this.mc, var1, AliceObf_gZ.getState(var1), this::Me);
   }

   public boolean Ih() {
      AliceObf_kZ var1 = this.Pj;
      return var1 != null && this.Ii(var1.aBU);
   }

   public boolean Ii(BlockPos var1) {
      AliceObf_kZ var2 = this.Ie(var1);
      if (var2 != null
         && !var2.Ur
         && this.HV()
         && !AliceObf_bT.atL(this.mc)
         && !this.HX()
         && !this.HW()
         && this.rA <= 0
         && !this.amq) {
         this.ky = true;

         boolean var3;
         try {
            this.IR(Hand.MAIN_HAND, var2.aBU);
            var3 = this.aht != null && this.aht.VY == var2 || this.agC != null && this.agC.XY == var2;
         } finally {
            this.ky = false;
         }

         return var3;
      } else {
         return false;
      }
   }

      private boolean Ij(BlockPos var1, BlockState var2) {
      AliceObf_oN var3 = AliceObf_oN.activeCompanionBuilder();
      if (var3 == null || var1 == null || var2 == null) {
         return true;
      } else if (!var3.companionEscapeAllows(var1, var2)) {
         return false;
      } else {
         BlockPos var4 = In(var1, var2);
         return var4 == null || var3.companionEscapeAllows(var4, EasyPlace.Io(var2));
      }
   }

   private boolean Ik(BlockPos var1, BlockState var2) {
      if (this.Ij(var1, var2)) {
         return true;
      } else {
         this.aPu = true;
         this.aFU = null;
         this.Il(var1, var2);
         return false;
      }
   }

   private void Il(BlockPos var1, BlockState var2) {
      if (this.Ia(var1)) {
         AliceObf_oN var3 = AliceObf_oN.activeCompanionBuilder();
         if (var3 != null && var1 != null && var2 != null) {
            var3.requestCompanionEscape(var1, var2);
         }
      }
   }

   private void Im(BlockPos var1, BlockState var2) {
      AliceObf_oN var3 = AliceObf_oN.activeCompanionBuilder();
      if (var3 != null && var1 != null && var2 != null) {
         var3.onCompanionPlacementSent(var1, var2);
      }
   }

   private static BlockPos In(BlockPos var0, BlockState var1) {
      if (!var1.contains(Properties.DOUBLE_BLOCK_HALF)) {
         if (var1.contains(Properties.BED_PART) && var1.contains(Properties.HORIZONTAL_FACING)) {
            Direction var2 = (Direction)var1.get(Properties.HORIZONTAL_FACING);
            return var0.offset(var1.get(Properties.BED_PART) == BedPart.FOOT ? var2 : var2.getOpposite());
         } else {
            return null;
         }
      } else {
         return var1.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? var0.up() : var0.down();
      }
   }

   private static BlockState Io(BlockState var0) {
      return var0.contains(Properties.DOUBLE_BLOCK_HALF)
         ? (BlockState)var0.with(
            Properties.DOUBLE_BLOCK_HALF, var0.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER
         )
         : (BlockState)var0.with(
            Properties.BED_PART, var0.get(Properties.BED_PART) == BedPart.FOOT ? BedPart.HEAD : BedPart.FOOT
         );
   }

   public void Ip() {
      AliceObf_qK var1 = this.aht;
      if (var1 != null) {
         AliceObf_nF var2 = var1.akJ == null ? null : this.axE.get(var1.akJ);
         if (!(var1.akJ == null)) {
            short var8 = 26387;
         }

         BlockState var3 = var2 != null && this.LT(var1.akJ, var2.yH) ? this.LU(var1.akJ) : null;
         if (!this.Ik(var1.sc, var3)) {
            this.Lc();
         }
      }

      AliceObf_tC var6 = this.apB;
      if (var6 != null) {
         int var9 = !this.Ik(var6.Fm, AliceObf_gZ.getState(var6.Fm))
            ? 12983
            : 12984;

         while (true) {
            int var4 = var9;
            if (var4 == 12983) {
               this.JK();
               break;
            }

            if (var4 != 12984) {
               break;
            }

            var9 = 12982;
         }
      }

      AliceObf_uR var7 = this.aFU;
      label139:
      if (var7 != null) {
         int var10 = !this.Ik(var7.agJ, AliceObf_gZ.getState(var7.agJ)) ? 4081 : 4080;

         while (true) {
            int var5 = var10;
            if (var5 == 4080) {
               if (var7.at == null
                  || this.Ik(var7.at, AliceObf_gZ.getState(var7.at))
                  )
                {
                  break label139;
               }
               break;
            }

            if (var5 != 4081) {
               break;
            }

            var10 = 4079;
         }

         this.aFU = null;
      }

      this.IH();
   }

   public boolean Iq(BlockPos var1) {
      if (var1 == null || this.mc.player == null || this.mc.world == null) {
         return false;
      } else if (!this.HV()) {
         return false;
      } else if (!this.Ig(var1)) {
         return false;
      } else {
         AliceObf_dp var2 = this.IT(var1);
         if (var2.Eq != AliceObf_ou.ar) {
            return false;
         } else {
            BlockPos var3 = var2.Zs;
            if (var3 == null) {
               return false;
            } else {
               double var4 = this.Jx();
               if (!Jz(var3, this.mc.player.getEyePos(), var4)) {
                  return false;
               } else {
                  return this.Jt(var3) ? true : this.It(var3);
               }
            }
         }
      }
   }

   public boolean Ir() {
      return (Boolean)this.Bs.get();
   }

   public boolean Is(BlockPos var1) {
      if (var1 != null && this.mc.player != null && this.mc.world != null && this.HV()) {
         int var10000 = this.Ig(var1) ? 29219 : 29218;

         while (true) {
            int var5 = var10000;
            if (var5 == 29218) {
               return false;
            }

            if (var5 != 29219) {
               AliceObf_dp var2 = this.IT(var1);
               if (var2.Eq == AliceObf_ou.ar && var2.Zs != null) {
                  double var3 = this.Jx();
                  return EasyPlace.Jz(
                        var2.Zs, this.mc.player.getEyePos(), var3
                     )
                     && this.Jt(var2.Zs);
               }

               return false;
            }

         }
      } else {
         return false;
      }
   }

   private boolean It(BlockPos var1) {
      if (var1 == null || this.mc.player == null || this.mc.world == null) {
         return false;
      } else if (!this.JG(var1)) {
         return false;
      } else {
         AliceObf_tC var2 = this.apB;
         if (var2 != null) {
            return var1.equals(var2.Fm) && this.Ij(var1, AliceObf_gZ.getState(var1));
         } else if ((Boolean)this.Bs.get()) {
            if (this.mc.player.isOnGround() && !this.mc.player.hasVehicle()) {
               BlockPos var3 = AliceObf_oS.aub(
                  this.mc, var1, this::LJ
               );
               if (var3 == null) {
                  return false;
               } else {
                  for (Direction var5 : Type.HORIZONTAL) {
                     if (var3.offset(var5).equals(var1)) {
                        return this.JF(var1, var3, var5);
                     }
                  }

                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean Iu(BlockPos var1) {
      return var1 == null ? false : this.Iv(this.If(var1));
   }

   public boolean Iv(BlockState var1) {
      Item var2 = this.getRequiredPlacementItem(var1);
      if (!(var2 != null)) {
         short var3 = 180;
      }

      return true;
   }

   public boolean Iw(BlockPos var1) {
      if (var1 == null) {
         return false;
      } else if (this.adJ.containsKey(var1)) {
         return true;
      } else {
         AliceObf_nF var2 = this.axE.get(var1);
         return var2 != null && var2.FT;
      }
   }

   public boolean Ix(BlockPos var1, BlockState var2) {
      if (!this.Ia(var1)) {
         return false;
      } else if (var1 != null && var2 != null && this.mc.player != null && this.mc.world != null) {
         BlockState var3 = this.If(var1);
         if (!var2.equals(var3)) {
            return false;
         } else if (AliceObf_qo.aDY(var3, this.mc.world.getBlockState(var1)) == AliceObf_qo.uG) {
            return true;
         } else {
            AliceObf_nF var4 = this.axE.get(var1);
            return var4 != null && var2.equals(var4.yH) && this.LT(var1, var4.yH) ? true : this.Ig(var1) && this.IJ(var3);
         }
      } else {
         return false;
      }
   }

   private boolean Iy(BlockPos var1) {
      if (var1 != null && this.mc.player != null) {
         if (!(this.mc.world != null)) {
            short var2 = 1342;
         }
      }

      return false;
   }

   public boolean Iz(BlockPos var1) {
      if (!this.Iy(var1)) {
         return false;
      } else {
         AliceObf_dd var2 = this.JN;
         return var2 == null || var2.FG().equals(var1) && this.mc.player.getInventory().getSelectedSlot() == var2.FK()
            ? this.LQ(var1) != null || this.IA(var1) >= 0
            : false;
      }
   }

   private int IA(BlockPos var1) {
      Block var2 = this.mc.world.getBlockState(var1).getBlock();
      int var3 = -1;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = this.mc.player.getInventory().getStack(var4);
         if (!var5.isEmpty() && MA(var5.getItem()) == var2) {
            return var4;
         }

         if (var3 < 0 && var5.isEmpty()) {
            var3 = var4;
         }
      }

      return var3;
   }

   private BlockHitResult IB(BlockPos var1) {
      if (!this.Iz(var1)) {
         return null;
      } else {
         BlockHitResult var2 = this.LR(var1);
         double var3 = this.IK();
         return var2 != null && this.mc.player.getEyePos().squaredDistanceTo(var2.getPos()) <= var3 * var3 ? var2 : null;
      }
   }

   public boolean IC(BlockPos var1) {
      return this.IB(var1) != null && this.Ij(var1, this.LU(var1));
   }

   public boolean ID(BlockPos var1) {
      if (this.HX() || this.IB(var1) == null) {
         return false;
      } else if (!this.Ib(var1)) {
         return false;
      } else if (!this.Ik(var1, this.LU(var1))) {
         return false;
      } else {
         if (this.JN == null && this.LQ(var1) == null) {
            if (this.HW()) {
               return false;
            }

            int var2 = this.IA(var1);
            if (var2 < 0) {
               return false;
            }

            this.JN = new AliceObf_dd(var1.toImmutable(), this.mc.player, this.mc.world, this.mc.player.getInventory().getSelectedSlot(), var2);
            this.mc.player.getInventory().setSelectedSlot(var2);
         }

         if (!this.axE.containsKey(var1)) {
            this.LZ(var1, AliceObf_gZ.getState(var1));
         }

         AliceObf_nF var3 = this.axE.get(var1);
         if (var3 != null) {
            var3.dl = true;
         }

         this.LP();
         this.IF();
         return this.axE.containsKey(var1);
      }
   }

   private boolean IE(AliceObf_nF var1) {
      if (!var1.akU) {
         if (!var1.dl) {
            return true;
         } else {
            AliceObf_dd var2 = this.JN;
            if (var2 == null
               || !var2.FG().equals(var1.Df)
               || this.mc.player == var2.FH() && this.mc.world == var2.FI() && this.mc.player.getInventory().getSelectedSlot() == var2.FK()) {
               return AliceObf_oN.activeCompanionBuilder() != null
                     && this.Ia(var1.Df)
                     && this.mc.world != null
                     && AliceObf_qo.aDZ(
                        var1.yH, AliceObf_gZ.getState(var1.Df)
                     )
                  ? var1.FT
                     || AliceObf_qo.aDY(
                           var1.yH, this.mc.world.getBlockState(var1.Df)
                        )
                        == AliceObf_qo.uG
                  : false;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private void IF() {
      AliceObf_dd var1 = this.JN;
      if (var1 != null) {
         AliceObf_nF var2 = this.axE.get(var1.FG());
         if (this.mc.player == var1.FH()) {
            if (this.mc.world != var1.FI()) {

            } else {
               short var4 = 9995;
            }
         }

         boolean var3 = true;
         if (var3 && var2 != null) {
            var2.akU = true;
         }

         if (var3 || var2 == null || !var2.FT && !this.IE(var2)) {
            if (this.aht != null && var1.FG().equals(this.aht.akJ)) {
               this.Lc();
            }

            if (var2 != null) {
               short var5 = 24790;
            } else {
               short var6 = 24791;
            }

            this.IG();
         }
      }
   }

   private void IG() {
      AliceObf_dd var1 = this.JN;
      this.JN = null;
      if (var1 != null && this.mc.player == var1.FH() && this.mc.world == var1.FI() && this.mc.player.getInventory().getSelectedSlot() == var1.FK()) {
         this.mc.player.getInventory().setSelectedSlot(var1.FJ());
      }
   }

   private void IH() {
      AliceObf_dd var1 = this.JN;
      if (var1 != null) {
         AliceObf_nF var2 = this.axE.get(var1.FG());
         if (var2 == null || !var2.FT) {
            if (!this.Ik(var1.FG(), this.LU(var1.FG()))) {
               if (this.aht != null && var1.FG().equals(this.aht.akJ)) {
                  this.Lc();
               }

               if (var2 != null) {
                  this.axE.remove(var1.FG(), var2);
               }

               this.IG();
            }
         }
      }
   }

   public boolean II(BlockPos var1) {
      return var1 == null ? false : this.IJ(this.If(var1));
   }

   public boolean IJ(BlockState var1) {
      Item var2 = this.getRequiredPlacementItem(var1);
      Block var3 = EasyPlace.MA(var2);
      if (var2 != null && var2 != Items.AIR) {
         if (!(var3 == Blocks.AIR)) {
            short var6 = 8126;
         }

         if (!(Boolean)this.anM.get() && !((List)this.fg.get()).contains(var3)) {
            return false;
         } else {
            Boolean var4 = this.avW.get(var2);
            if (var4 != null) {
               return var4;
            } else {
               boolean var5 = this.JN(var2);
               this.avW.put(var2, var5);
               return var5;
            }
         }
      } else {
         return false;
      }
   }

   public double IK() {
      return this.mc.player == null ? (Double)this.aEK.get() : Math.min((Double)this.aEK.get(), this.mc.player.getBlockInteractionRange());
   }

   public boolean IL() {
      return (Boolean)this.Lv.get() && ((Boolean)this.be.get() || (Boolean)this.Cf.get());
   }

   public void onDeactivate() {
      int var10000 = UG == this ? 15232 : 15233;

      while (true) {
         int var1 = var10000;
         if (var1 == 15232) {
            UG = null;
            break;
         }

         if (var1 != 15233) {
            break;
         }

      }

      this.ME();
      this.DQ = null;
      this.Kh();
      this.MH(false);
      super.onDeactivate();
      this.MG();
   }

   private boolean IM() {
      return (Boolean)this.be.get() || (Boolean)this.Cf.get() || (Boolean)this.aIU.get() || (Boolean)this.aoJ.get();
   }

   private void IN() {
      this.ME();
      this.DQ = null;
      this.Kh();
      this.disable();
   }

   @AliceHandler
   public void IO(InteractBlockEvent var1) {
      if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null) {
         if (!this.amq) {
            AliceObf_oN var2 = AliceObf_oN.activeCompanionBuilder();
            BlockHitResult var3 = var1.result;
            if (var3 == null || !this.LO(var3.getBlockPos()) && !this.LO(this.getPlacePos(var3))) {
               if (var2 != null) {
                  if (!var2.isCompanionClimbing()) {

                  } else {
                     short var4 = 24017;
                  }
               }

               if (!this.amq && this.IQ(var1.hand, var1.result)) {
                  var1.cancel();
               } else {
                  if (this.JR(var1.hand, var1.result, false)) {
                     short var5 = 18772;
                  } else {
                     short var6 = 18773;
                  }
               }
            } else {
               var1.cancel();
            }
         }
      }
   }

   @AliceHandler
   public void IP(InteractItemEvent var1) {
      int var10000 = this.IQ(var1.hand, null) ? 1313 : 1314;

      while (true) {
         int var2 = var10000;
         if (var2 == 1313) {
            var1.toReturn = ActionResult.SUCCESS;
            break;
         }

         if (var2 != 1314) {
            break;
         }

      }
   }

   private boolean IQ(Hand var1, BlockHitResult var2) {
      if (this.JN != null || this.mc.player == null || this.mc.world == null || this.mc.interactionManager == null || var1 == null || this.amq) {
         return false;
      } else if (!(Boolean)this.be.get() && !(Boolean)this.Cf.get()) {
         return false;
      } else {
         BlockPos var3;
         if (var2 != null && var2.getType() == HitResult.Type.BLOCK) {
            var3 = this.getPlacePos(var2);
            if (!AliceObf_gZ.Hg(this.mc, var3, this::Me)) {

            } else {
               char var5 = 39565;
            }
         } else {
            BlockHitResult var4 = AliceObf_gZ.Hi(this.mc, this::Me);
            var3 = var4 == null ? null : var4.getBlockPos();
         }

         return var3 == null ? false : this.IR(var1, var3);
      }
   }

   private boolean IR(Hand var1, BlockPos var2) {
      if (this.mc.player == null || this.mc.world == null || this.mc.interactionManager == null || var1 == null || var2 == null || this.amq) {
         return false;
      } else if (!this.Ia(var2)) {
         return true;
      } else {
         AliceObf_uR var3 = this.aFU;
         if (var3 != null && !var3.agJ.equals(var2)) {
            return true;
         } else if (!this.Ig(var2)) {
            return false;
         } else {
            AliceObf_dp var4 = this.IT(var2);
            if (!(var4.Eq == AliceObf_ou.lc)) {
               char var8 = 48096;
            }

            int var10001 = var4.Eq != AliceObf_ou.hX ? 62800 : 62799;

            while (true) {
               int var5 = var10001;
               if (var5 == 62799) {
                  this.IW(var1, var2);
                  return true;
               }

               if (var5 != 62800) {
                  if (this.Ib(var4.Zs)) {
                     var10001 = !this.Ik(var4.Zs, this.If(var4.Zs)) ? 45456 : 45457;

                     while (true) {
                        int var6 = var10001;
                        if (var6 == 45456) {
                           this.aFU = null;
                           return true;
                        }

                        if (var6 != 45457) {
                           var3 = this.aFU;
                           if (var3 != null && var3.agJ.equals(var2) && !var4.Zs.equals(var3.at)) {
                              var3.at = var4.Zs;
                              var3.Lt = 0;
                           }

                           if (!var4.Zs.equals(var2)) {
                              this.IW(var1, var2);
                           }

                           return this.IS(var1, var4.Zs);
                        }

                     }
                  }

                  this.aFU = null;
                  return true;
               }

            }
         }
      }
   }

   private boolean IS(Hand var1, BlockPos var2) {
      if (this.mc.player == null || this.mc.world == null || this.mc.interactionManager == null || var1 == null || var2 == null || this.amq || !this.Ig(var2)) {
         return false;
      } else if (!this.Ia(var2)) {
         return true;
      } else if (this.Ib(var2)) {
         Item var3 = this.getRequiredPlacementItem(this.If(var2));
         Block var4 = EasyPlace.MA(var3);
         boolean var5 = false;
         BlockHitResult var6;
         if (AliceObf_ga.Nv(var3) != Blocks.AIR) {
            var5 = true;
            var6 = this.JO(var2, var3);
         } else if (var4 instanceof HopperBlock) {
            Direction var7 = this.getWantedHopperFacing(var2);
            if (var7 != null) {
               var5 = true;
               var6 = this.Mq(var2, var7);
            } else {
               var6 = this.LA(var2);
            }
         } else if (var4 instanceof ShulkerBoxBlock) {
            var5 = true;
            var6 = this.Lw(var2, this.getWantedShulkerFacing(var2));
         } else if (!(var4 instanceof ChestBlock)) {
            List var8 = AliceObf_gZ.getPlacementSupportDirections(
               var2
            );
            var6 = this.LC(var2, var8);
            if (var6 == null) {
               if (var8.isEmpty()) {

               } else {
                  char var10 = 60481;
               }
            }

            if (!var8.isEmpty()) {
               var5 = true;
            }
         } else {
            ChestType var9 = AliceObf_gZ.getChestType(var2);
            var5 = var9 != null;
            if (var9 == null) {
               short var11 = 25315;
            } else {
               short var12 = 25316;
            }

            var6 = this.LD(var2, var9, var4);
         }

         if (var6 != null) {
            return this.JR(var1, var6, true);
         } else {
            if (var5 || !this.LI(var2)) {
               this.LL(var2, var5 ? "thieu mat do phia can thiet" : "khong co block do");
            }

            return true;
         }
      } else {
         return true;
      }
   }

   private AliceObf_dp IT(BlockPos var1) {
      return this.IU(var1, new HashSet());
   }

   private AliceObf_dp IU(BlockPos var1, Set<BlockPos> var2) {
      if (!(var1 != null)) {
         short var9 = 28405;
      }

      return AliceObf_dp.FU();
   }

   private List<BlockPos> getSchematicDependencies(BlockPos var1) {
      ArrayList var2 = new ArrayList();

      for (Direction var4 : AliceObf_gZ.getPlacementSupportDirections(
         var1
      )) {
         var2.add(var1.offset(var4).toImmutable());
      }

      BlockState var6 = this.If(var1);
      if (var6 != null && var6.getBlock() instanceof HopperBlock) {
         Direction var7 = this.getWantedHopperFacing(var1);
         if (var7 != null) {
            BlockPos var5 = AliceObf_eh.axz(var1, var7, this::LJ, var0 -> {
               BlockState var1x = AliceObf_gZ.getState(var0);
               if (var1x != null) {
                  if (!var1x.isAir()) {

                  } else {
                     char var2x = 58784;
                  }
               }

               return false;
            });
            if (var5 != null && !var2.contains(var5)) {
               var2.add(var5);
            }
         }
      }

      var2.sort(Comparator.comparingInt(Vec3i::getY).thenComparingDouble(var1x -> var1x.getSquaredDistance(var1)));
      return var2;
   }

      private boolean IV(BlockPos var1) {
      if (var1 != null && this.mc.world != null) {
         BlockState var2 = AliceObf_gZ.getState(var1);
         if (!this.LJ(var1)) {
            if (this.aOG.get() != AliceObf_hi.aFc) {
               return false;
            } else {
               AliceObf_hy var3 = this.adJ.get(var1);
               return var3 != null && var3.ZC != null && var3.ZC != Blocks.AIR;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private void IW(Hand var1, BlockPos var2) {
      if (var1 != null && var2 != null) {
         AliceObf_uR var3 = this.aFU;
         if (var3 != null) {
            if (!var3.agJ.equals(var2)) {

            } else {
               short var4 = 5787;
            }
         } else {
            this.aFU = new AliceObf_uR(var1, var2.toImmutable());
         }
      }
   }

   private void IX() {
      if (this.JN == null) {
         AliceObf_uR var1 = this.aFU;
         if (var1 != null && this.mc.player != null && this.mc.world != null) {
            if (!(this.mc.interactionManager == null)) {
               short var4 = 6642;
            }

            var1.Lt++;
            if (var1.Lt > 40) {
               this.aFU = null;
               this.LL(var1.agJ, "chuoi block phu thuoc qua thoi gian cho");
            } else if (!this.adJ.containsKey(var1.agJ) && this.mc.world.getBlockState(var1.agJ).isReplaceable()) {
               BlockState var2 = AliceObf_gZ.getState(var1.agJ);
               if (var2 == null || var2.isAir()) {
                  this.aFU = null;
               } else if ((Boolean)this.aoJ.get()) {
                  if (!this.HX()) {
                     char var6 = 38093;
                  } else {
                     char var7 = 38094;
                  }
               } else {
                  if (AliceObf_bT.atM(this.mc)) {

                  } else {
                     short var5 = 502;
                  }

                  this.aFU = null;
               }
            } else {
               this.aFU = null;
            }
         }
      }
   }

   @AliceHandler(
      priority = -1000
   )
   public void IY(Sent var1) {
      if (var1.packet instanceof PlayerMoveC2SPacket var2) {
         ClientPlayerEntity var4 = this.mc.player;
         if (var4 != null && this.mc.world != null && var4.networkHandler != null && var1.connection == var4.networkHandler.getConnection()) {
            if (this.mc.isOnThread()) {

            } else {
               short var5 = 14969;
            }

            this.ee.acv();
         }
      }
   }

   private AliceObf_ru IZ() {
      ClientPlayerEntity var1 = this.mc.player;
      if (var1 != null && this.mc.world != null && var1.networkHandler != null && !var1.hasVehicle()) {
         AliceObf_ru var2 = this.ee
            .acu(var1, this.mc.world, var1.networkHandler.getConnection());
         return var2 != null && var2.acq() == var1.getEyeHeight(var1.getPose()) ? var2 : null;
      } else {
         return null;
      }
   }

   private boolean Ja(AliceObf_qK var1) {
      var1.aKj = null;
      var1.kE = null;
      AliceObf_ru var2 = this.IZ();
      if (var2 != null && var2.ack() == var1.ama && var1.Wd == this.ee.acw() && var2.acj(var1.ju, var1.adA)) {
         var1.aKj = var2;
         var1.kE = new Vec3d(var2.acn(), var2.aco() + var2.acq(), var2.acp());
         var1.sE = var2.acr();
         var1.fZ = var2.acs();
         return true;
      } else {
         return false;
      }
   }

   @AliceHandler
   public void Jb(Sent var1) {
      AliceObf_qK var2 = this.Mh;
      if (var2 != null) {
         Packet var4 = var1.packet;
         if (!(var4 instanceof PlayerInteractBlockC2SPacket)) {
            short var6 = 27001;
         }
      }
   }

   @AliceHandler
   public void Jc(Receive var1) {
      if (var1.packet instanceof PlayerPositionLookS2CPacket || var1.packet instanceof PlayerRotationS2CPacket) {
         ClientPlayerEntity var2 = this.mc.player;
         if (var2 != null && var2.networkHandler != null) {
            if (var1.connection == var2.networkHandler.getConnection()) {

            } else {
               short var3 = 26692;
            }
         }
      }
   }

   @AliceHandler
   public void Jd(Receive var1) {
      if (var1.packet instanceof BlockUpdateS2CPacket var2) {
         this.Mn(var2.getPos(), var2.getState());
      } else if (var1.packet instanceof ChunkDeltaUpdateS2CPacket var3 && this.Mm()) {
         var3.visitUpdates((var1x, var2x) -> this.Mn(var1x, var2x));
      }

      AliceObf_dt var5 = this.agC;
      if (var5 != null) {
         if (!(var1.packet instanceof InventoryS2CPacket)) {
            if (!(var1.packet instanceof ScreenHandlerSlotUpdateS2CPacket)) {
               char var7 = 56646;
            }
         }

         this.mc.execute(() -> this.mc.execute(() -> {
            if (this.agC == var5) {
               var5.aAS = true;
               if (var5.An == AliceObf_rV.Ku) {
                  var5.Qz = 0;
               }
            }
         }));
      }
   }

   @AliceHandler(
      priority = 100
   )
   public void Je(PlayerTickMovementEvent var1) {
      this.Kg();
   }

   @AliceHandler(
      priority = -100
   )
   public void Jf(Post var1) {
      this.aHL = this.Lv();
      this.Ll();
      AliceObf_qK var2 = this.aht;
      if (var2 != null && var2.aKP) {
         boolean var3 = var2.OJ;
         boolean var4 = this.Ja(var2);
         this.KL(var2);
         var2.OJ = false;
         if (var4) {
            if (var3) {
               var2.aDx = this.Ky(var2.kE, var2.sE, var2.fZ, var2.ajf.getBlockPos());
               var2.rv = !var2.aDx ? null : var2.aKj;
               if (var2.aDx) {
                  var2.iR = 0;
               }
            } else if (this.Ko(var2)) {
               this.KR(var2.aMd);
            }
         } else {
            var2.aDx = false;
         }
      } else {
         this.KQ();
      }
   }

   @AliceHandler
   public void Jg(Pre var1) {
      AliceObf_gZ.GN();
      if (this.DQ != this.mc.world) {
         this.ME();
         this.DQ = this.mc.world;
         this.HT();
      }

      if (!AliceObf_eW.gi()) {
         this.IF();
         this.aIk = false;
         this.Kg();
         this.Ls();
         this.Le();
         this.KS();
         this.LP();
         this.IX();
         this.Jh();
         this.Ji();
      }
   }

   private void Jh() {
      int var10000 = AliceObf_eW.gj() ? 9243 : 9244;

      while (true) {
         int var4 = var10000;
         if (var4 == 9243) {
            return;
         }

         if (var4 != 9244) {
            if (this.JN == null) {
               char var5 = 52855;
            } else {
               char var6 = 52856;
            }

            return;
         }

      }
   }

   private void Ji() {
      if (!AliceObf_eW.gj()) {
         if (this.JN == null) {
            if ((Boolean)this.aoJ.get()) {
               if (!this.HX()) {
                  if (!(Boolean)this.be.get() && !(Boolean)this.Cf.get()) {
                     this.MI("Schematic Aura can bat Smart Placement hoac Schematic Only");
                  } else if (this.aaz <= 0
                     && this.mc.player != null
                     && this.mc.world != null
                     && this.mc.interactionManager != null
                     && !AliceObf_bT.atL(this.mc)
                     && !this.mc.player.isSneaking()
                     && !this.axK
                     && this.aht == null
                     && (this.agC == null || this.agC.aQc)
                     && !this.aIk
                     && this.aFU == null
                     && !this.amq) {
                     if (!(this.rA > 0)) {
                        char var6 = 61120;
                     }

                     AliceObf_oN var1 = AliceObf_oN.activeCompanionBuilder();
                     Integer var2 = var1 == null ? null : var1.companionContinuousAuraLayer();
                     AliceObf_vk var3 = this.Jp(var2);
                     BlockPos var4 = var3.FD();
                     if (var4 == null) {
                        this.JE();
                        if (this.apB == null && var3.FE() != null) {
                           this.Il(var3.FE(), var3.FF());
                        }
                     } else {
                        this.aPu = false;
                        this.OR = false;
                        this.IR(Hand.MAIN_HAND, var4);
                        if (!(this.aPu)) {
                           char var7 = 35286;
                        }

                        this.Jq(var4);
                        if (!(this.aht == null)) {
                           short var8 = 5106;
                        }

                        this.aaz = (Integer)this.Gr.get();
                     }
                  }
               }
            }
         }
      }
   }

   public BlockPos Jj(int var1) {
      if (AliceObf_oN.activeCompanionBuilder() != null && this.HV()
         )
       {
         int var10000 = this.mc.player != null ? 20379 : 20380;

         while (true) {
            int var2 = var10000;
            if (var2 == 20379) {
               if (this.mc.world != null && this.mc.interactionManager != null) {
                  if (!AliceObf_bT.atL(this.mc)) {
                     char var3 = 53924;
                  } else {
                     char var4 = 53925;
                  }
               }
               break;
            }

            if (var2 != 20380) {
               break;
            }

         }
      }

      return null;
   }

   public BlockPos Jk(int var1) {
      AliceObf_oN var2 = AliceObf_oN.activeCompanionBuilder();
      if (var2 != null
         && this.HV()
         && this.mc.player != null
         && this.mc.world != null
         && this.mc.interactionManager != null
         && !AliceObf_bT.atL(this.mc)
         && !this.mc.player.isSpectator()
         && this.mc.player.isAlive()
         && (!this.Ko || var1 <= this.aaH)) {
         for (BlockPos var4 : this.Jl(var1, this.Jx(), this.mc.player.getEyePos())) {
            if (this.Jm(var4) && this.Ju(var4)) {
               BlockState var5 = this.If(var4);
               if (!var2.companionHopperPlacementProbeAllows(var4, var5)) {

               } else {
                  short var7 = 11619;
               }

               BlockPos var6 = EasyPlace.In(var4, var5);
               if (var6 == null
                  || var2.companionHopperPlacementProbeAllows(
                     var6, EasyPlace.Io(var5)
                  )) {
                  return var4;
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private List<BlockPos> Jl(int var1, double var2, Vec3d var4) {
      AliceObf_kk var5 = this.aoD;
      if (var5 != null && var5.HP() == var1 && var5.HQ() == var2 && var5.HR().equals(var4)) {
         return var5.HS();
      } else if (!(var2 <= 0.0) && Double.isFinite(var2)) {
         BlockPos var6 = BlockPos.ofFloored(var4);
         int var7 = (int)Math.ceil(var2) + 1;
         ArrayList<BlockPos> var8 = new ArrayList<>();

         for (BlockPos var10 : BlockPos.iterate(
            new BlockPos(var6.getX() - var7, var1, var6.getZ() - var7),
            new BlockPos(var6.getX() + var7, var1, var6.getZ() + var7)
         )) {
            if (EasyPlace.Jz(var10, var4, var2)) {
               var8.add(var10.toImmutable());
            }
         }

         var8.sort(Comparator.comparingDouble(var1x -> var4.squaredDistanceTo(Vec3d.ofCenter(var1x))));
         List var11 = List.copyOf(var8);
         this.aoD = new AliceObf_kk(var1, var2, var4, var11);
         return var11;
      } else {
         return List.of();
      }
   }

   private boolean Jm(BlockPos var1) {
      AliceObf_oN var2 = AliceObf_oN.activeCompanionBuilder();
      if (var2 != null && this.Ia(var1) && !this.akX.containsKey(var1) && !this.Iw(var1)) {
         if (!var2.companionCellReserved(var1)) {

         } else {
            char var4 = 34855;
         }
      }

      return false;
   }

   private AliceObf_vk Jn(int var1, double var2, Vec3d var4) {
      if (this.Ko && var1 > this.aaH) {
         return new AliceObf_vk(null, null, null);
      } else {
         Object var5 = null;
         Object var6 = null;

         for (BlockPos var8 : this.Jl(var1, var2, var4)) {
            if (this.Jm(var8) && this.Ju(var8)) {
               BlockState var9 = this.If(var8);
               if (this.Ij(var8, var9)) {

               } else {
                  short var10 = 1823;
               }

               this.ahI = new AliceObf_pA(this.mc.world, this.mc.player, this.mc.player.age, var1, var2, var4, var8);
               return new AliceObf_vk(var8, null, null);
            }
         }

         return new AliceObf_vk(null, (BlockPos)var5, (BlockState)var6);
      }
   }

   private BlockPos Jo(Integer var1, double var2, Vec3d var4) {
      AliceObf_pA var5 = this.ahI;
      if (var1 != null && var5 != null) {
         if (var5.HI() == this.mc.world) {

         } else {
            char var8 = 41851;
         }
      }

      return null;
   }

   private AliceObf_vk Jp(Integer var1) {
      double var2 = this.Jx();
      Vec3d var4 = this.mc.player.getEyePos();
      BlockPos var5 = this.Jo(var1, var2, var4);
      if (var5 != null) {
         return new AliceObf_vk(var5, null, null);
      } else if (var1 != null) {
         return this.Jn(var1, var2, var4);
      } else {
         BlockPos var6 = BlockPos.ofFloored(var4);
         int var7 = (int)Math.ceil(var2) + 1;
         BlockPos var8 = null;
         BlockState var9 = null;
         ArrayList<AliceObf_rL> var10 = new ArrayList<>();
         int var11 = var6.getY() - var7;
         int var12 = var6.getY() + var7;
         if (!(this.Ko)) {
            short var25 = 21567;
         }

         if (var11 > var12) {
            return new AliceObf_vk(null, null, null);
         } else {
            for (BlockPos var14 : BlockPos.iterate(
               new BlockPos(var6.getX() - var7, var11, var6.getZ() - var7),
               new BlockPos(var6.getX() + var7, var12, var6.getZ() + var7)
            )) {
               int var10001 = !Jz(var14, var4, var2) ? 6928 : 6929;

               while (true) {
                  int var19 = var10001;
                  if (var19 == 6928) {
                     break;
                  }

                  if (var19 != 6929) {
                     if (this.Ia(var14)
                        && !this.akX.containsKey(var14)
                        && AliceObf_gZ.Hg(this.mc, var14, this::Me)
                        )
                      {
                        AliceObf_dp var15 = this.IT(var14);
                        if (var15.Eq == AliceObf_ou.ar) {
                           BlockPos var16 = var15.Zs;
                           if (var16 != null && this.Ib(var16)) {
                              double var17 = var4.squaredDistanceTo(Vec3d.ofCenter(var16));
                              if (EasyPlace.Jz(var16, var4, var2)
                                 )
                               {
                                 var10.add(new AliceObf_rL(var14.toImmutable(), var16.toImmutable(), var17));
                              }
                           }
                        }
                     }
                     break;
                  }

               }
            }

            var10.sort(Comparator.<AliceObf_rL>comparingInt(var0 -> var0.FB().getY()).thenComparingDouble(AliceObf_rL::FC));

            for (AliceObf_rL var22 : var10) {
               BlockPos var23 = var22.FB();
               int var26 = !this.Ju(var23) ? 13611 : 13612;

               while (true) {
                  int var20 = var26;
                  if (var20 == 13611) {
                     break;
                  }

                  if (var20 != 13612) {
                     BlockState var24 = this.If(var23);
                     if (this.Ij(var23, var24)) {
                        return new AliceObf_vk(var22.FA(), null, null);
                     }

                     if (var8 == null) {
                        var8 = var23.toImmutable();
                        var9 = var24;
                     }
                     break;
                  }

                  var26 = 13610;
               }
            }

            return new AliceObf_vk(null, var8, var9);
         }
      }
   }

   private void Jq(BlockPos var1) {
      AliceObf_qK var2 = this.aht;
      AliceObf_oN var3 = AliceObf_oN.activeCompanionBuilder();
      if (!(var2 != null)) {
         char var5 = 54464;
      }
   }

   private void Jr(AliceObf_qK var1) {
      AliceObf_iO var2 = var1.fC;
      var1.fC = null;
      if (var2 != null
         && var1.aMd == this.lM
         && this.HV()
         && this.mc.player == var2.FM()
         && this.mc.world == var2.FN()
         && this.mc.interactionManager != null
         && this.mc.player.networkHandler != null
         && var2.FL() == AliceObf_oN.activeCompanionBuilder()
         && (!var2.FR() || var2.FO().equals(var2.FL().companionBuildTarget()))
         && !AliceObf_bT.atL(this.mc)
         && !AliceObf_bT.atM(this.mc)
         && !this.HX()) {
         int var10000 = !this.mc.player.isSpectator() ? 11325 : 11326;

         while (true) {
            int var4 = var10000;
            if (var4 == 11325) {
               if (this.mc.player.isAlive()
                  && !this.mc.player.isSneaking()
                  && !this.mc.player.isUsingItem()
                  && this.mc.options != null
                  && !this.mc.options.sneakKey.isPressed()
                  && !this.mc.options.useKey.isPressed()
                  && this.agC == null
                  && !this.aIk) {
                  if (this.aFU == null) {
                     char var6 = 46517;
                  } else {
                     char var7 = 46518;
                  }
               }
               break;
            }

            if (var4 != 11326) {
               break;
            }

         }
      }
   }

   private void Js() {
      if (!this.akX.isEmpty()) {
         this.akX.replaceAll((var0, var1) -> var1 - 1);
         this.akX.values().removeIf(var0 -> var0 <= 0);
      }
   }

   private boolean Jt(BlockPos var1) {
      if (!this.Ia(var1)) {
         return false;
      } else if (this.Ju(var1)) {
         if (!this.Ij(var1, this.If(var1))) {

         } else {
            char var2 = 35687;
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean Ju(BlockPos var1) {
      double var2 = this.Jx();
      if (AliceObf_oN.activeCompanionBuilder() != null) {
         if (this.Jv(var1, var2) != null) {

         } else {
            char var5 = 59057;
         }

         return false;
      } else {
         BlockHitResult var4 = this.Jw(var1);
         return var4 != null && this.mc.player.getEyePos().squaredDistanceTo(var4.getPos()) <= var2 * var2;
      }
   }

   private BlockHitResult Jv(BlockPos var1, double var2) {
      BlockHitResult var4 = this.Jw(var1);
      if (var4 != null && this.LJ(var4.getBlockPos())) {
         return this.getPlacePos(var4).equals(var1) && this.mc.player.getEyePos().squaredDistanceTo(var4.getPos()) <= var2 * var2 ? var4 : null;
      } else {
         return null;
      }
   }

   private BlockHitResult Jw(BlockPos var1) {
      if (!this.Ia(var1)) {
         return null;
      } else if (this.mc.world == null || !this.mc.world.isPosLoaded(var1)) {
         return null;
      } else if (!this.JA(var1, this.If(var1))) {
         Item var2 = this.getRequiredPlacementItem(this.If(var1));
         Block var3 = MA(var2);
         if (var2 != null && var2 != Items.AIR) {
            if (!(var3 != Blocks.AIR)) {
               short var5 = 20313;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private double Jx() {
      double var1 = this.IK();
      return AliceObf_oN.activeCompanionBuilder() == null ? var1 : Math.min(var1, 4.5);
   }

   private double Jy() {
      return AliceObf_oN.activeCompanionBuilder() == null ? this.mc.player.getBlockInteractionRange() : this.Jx();
   }

   private static boolean Jz(BlockPos var0, Vec3d var1, double var2) {
      if (!(var0 != null)) {
         char var4 = 39566;
      }

      return false;
   }

   private boolean JA(BlockPos var1, BlockState var2) {
      if (this.mc.player != null && var1 != null) {
         int var10000 = var2 == null ? 51776 : 51777;

         while (true) {
            int var3 = var10000;
            if (var3 == 51776) {
               break;
            }

            if (var3 != 51777) {
               return AliceObf_nc.aDq(
                  this.mc, var1, var2, this.mc.player.getBoundingBox()
               );
            }

         }
      }

      return false;
   }

   private boolean JB(BlockPos var1, BlockState var2) {
      if (var1 == null || var2 == null) {
         return false;
      } else if (this.JA(var1, var2)) {
         return true;
      } else {
         BlockPos var3 = EasyPlace.In(var1, var2);
         return var3 != null && this.JA(var3, Io(var2));
      }
   }

   private BlockState JC(BlockPos var1, Hand var2, BlockHitResult var3) {
      BlockState var4 = this.If(var1);
      return var4 != null && !var4.isAir() ? var4 : this.JD(var1, var2, var3);
   }

   private BlockState JD(BlockPos var1, Hand var2, BlockHitResult var3) {
      if (this.mc.player != null && var2 != null && var3 != null) {
         ItemStack var4 = this.mc.player.getStackInHand(var2);
         Block var5 = AliceObf_ga.Nv(var4.getItem());
         if ((Boolean)this.aBD.get() && var5 != Blocks.AIR) {
            return var5.getDefaultState();
         } else {
            Block var6 = !(var4.getItem() instanceof BlockItem var7) ? null : var7.getBlock();
            if (var6 == null) {
               return null;
            } else {
               try {
                  ItemPlacementContext var10 = new ItemPlacementContext(this.mc.player, var2, var4, var3);
                  return var10.getBlockPos().equals(var1) ? this.getPlacementStateForHit(var4, var6, var10) : null;
               } catch (RuntimeException var9) {
                  return null;
               }
            }
         }
      } else {
         return null;
      }
   }

   private void JE() {
      if ((Boolean)this.Bs.get() && this.apB == null) {
         if (!(this.mc.player != null)) {
            char var11 = 45164;
         }
      }
   }

   private boolean JF(BlockPos var1, BlockPos var2, Direction var3) {
      if (!this.Ia(var1)) {
         return false;
      } else {
         return !this.Ij(var1, AliceObf_gZ.getState(var1))
            ? false
            : this.JI(var1, var2, var3);
      }
   }

   private boolean JG(BlockPos var1) {
      AliceObf_oN var2 = AliceObf_oN.activeCompanionBuilder();
      return var2 == null || var1 != null && var1.equals(var2.companionBuildTarget()) && var2.companionCreepAllowed(var1);
   }

   private void JH(BlockPos var1, BlockPos var2, Direction var3) {
      if (!this.Ib(var1)) {

      } else {
         char var5 = 40460;
      }

      if (this.JF(var1, var2, var3)) {
         AliceObf_gD var4 = AliceObf_oS.aul(
            this.mc, var1, this.mc.player.getEntityPos(), var2, this.IK(), this::LJ
         );
         if (var4 != null) {
            this.apB = new AliceObf_tC(
               var1,
               var2,
               var3,
               this.mc.player.getY(),
               this.mc.player.getX(),
               this.mc.player.getZ(),
               var4.atU(),
               this.mc.world.getBlockState(var2),
               AliceObf_gZ.getState(var1)
            );
         }
      }
   }

   private boolean JI(BlockPos var1, BlockPos var2, Direction var3) {
      if (this.Ia(var1) && this.JG(var1)) {
         if (this.akX.containsKey(var1.toImmutable())) {
            return false;
         } else if (!AliceObf_gZ.Hg(this.mc, var1, this::Me)) {
            return false;
         } else {
            AliceObf_dp var4 = this.IT(var1);
            if (var4.Eq != AliceObf_ou.ar || !var1.equals(var4.Zs)) {
               return false;
            } else if (this.JA(var1, AliceObf_gZ.getState(var1))) {
               return false;
            } else if (!this.II(var1)) {
               return false;
            } else {
               BlockState var5 = AliceObf_gZ.getState(var1);
               Direction var6 = var5 != null && var5.getBlock() instanceof HopperBlock ? this.getWantedHopperFacing(var1) : null;
               if (var6 != null && !var2.equals(var1.offset(var6))) {
                  return false;
               } else {
                  if (var5 != null && var5.getBlock() instanceof ShulkerBoxBlock) {
                     Direction var7 = this.getWantedShulkerFacing(var1);
                     if (var7 == null) {
                        return false;
                     }

                     int var10000 = var3 == var7 ? 17203 : 17204;

                     while (true) {
                        int var9 = var10000;
                        if (var9 == 17203) {
                           if (!var2.equals(var1.offset(var7.getOpposite()))) {
                              return false;
                           }
                           break;
                        }

                        if (var9 != 17204) {
                           return false;
                        }

                     }
                  }

                  AliceObf_gD var10 = AliceObf_oS.aul(
                     this.mc, var1, this.mc.player.getEntityPos(), var2, this.IK(), this::LJ
                  );
                  AliceObf_oN var8 = AliceObf_oN.activeCompanionBuilder();
                  return var10 != null && (var8 == null || var8.companionCreepPathAllows(var10.atU()));
               }
            }
         }
      } else {
         return false;
      }
   }

   private static float JJ(Direction var0) {
      return (float)Math.toDegrees(Math.atan2(-var0.getOffsetX(), var0.getOffsetZ()));
   }

   private void JK() {
      if (this.apB != null && this.mc.player != null) {
         this.aEk = this.mc.player.age + 2;
      }

      this.apB = null;
   }

   private void JL(BlockPos var1) {
      this.akX.put(var1.toImmutable(), 40);
      AliceObf_oN var2 = AliceObf_oN.activeCompanionBuilder();
      if (var2 != null) {
         var2.companionPlacementFailed(var1);
      }

      this.JK();
   }

   private boolean JM(AliceObf_em var1, PlayerInput var2, Vec2f var3) {
      AliceObf_tC var4 = this.apB;
      ClientPlayerEntity var5 = this.mc.player;
      if (var4 == null) {
         return false;
      } else if (AliceObf_bT.atL(this.mc)) {
         this.JK();
         return false;
      } else if (this.Ib(var4.Fm)) {
         if (this.Ik(var4.Fm, AliceObf_gZ.getState(var4.Fm))) {

         } else {
            short var13 = 19615;
         }

         if (!(Boolean)this.Bs.get()
            || var5 == null
            || this.mc.world == null
            || var5.hasVehicle()
            || !var5.isOnGround()
            || var5.getY() < var4.RO - 0.1
            || this.axK
            || this.aht != null
            || this.agC != null
            || this.iX != null
            || !this.Ia(var4.Fm)
            || !this.JG(var4.Fm)
            || !var4.Pt
               .equals(AliceObf_oS.aub(this.mc, var4.Fm, this::LJ))
            || this.mc.world.getBlockState(var4.Pt) != var4.rZ
            || !AliceObf_qo.aDZ(
               var4.js, AliceObf_gZ.getState(var4.Fm)
            )
            || !this.LJ(var4.Pt)
            || !AliceObf_gZ.Hg(this.mc, var4.Fm, this::Me)) {
            this.JK();
            return false;
         } else if (++var4.aIA > 40) {
            this.JL(var4.Fm);
            return false;
         } else if (AliceObf_oS.aun(
               this.mc, var4.Pt, var4.amh, var5.getEntityPos().add(0.0, 1.62, 0.0), this.IK()
            )
            != null) {
            boolean var11 = var5.getVelocity().horizontalLengthSquared() > 9.0E-6;
            var1.setState(new PlayerInput(false, false, false, false, false, var11, false), Vec2f.ZERO);
            var5.setSprinting(false);
            if (!var11) {
               this.JK();
            }

            return true;
         } else {
            AliceObf_oN var6 = AliceObf_oN.activeCompanionBuilder();
            if (!(var6 != null)) {
               char var14 = 59398;
            }

            if (!var4.Qr) {
               var1.setState(new PlayerInput(false, false, false, false, false, true, false), Vec2f.ZERO);
               boolean var7 = var5.isSprinting();
               var5.setSprinting(false);
               if (!var5.isSneaking() || var7 || var5.getVelocity().horizontalLengthSquared() > 9.0E-6) {
                  return true;
               }

               var4.Qr = true;
            }

            double var12 = var5.getX() - var4.YE;
            double var9 = var5.getZ() - var4.aaa;
            var4.YE = var5.getX();
            var4.aaa = var5.getZ();
            if (var12 * var12 + var9 * var9 < 1.0E-4) {
               var4.NX++;
            } else {
               var4.NX = 0;
            }

            if (var4.aIA >= 8 && var4.NX >= 6) {
               this.JL(var4.Fm);
               return false;
            } else {
               this.KJ(var5, var5.getYaw(), var5.getPitch());
               var5.setYaw(
                  KG(EasyPlace.JJ(var4.amh), var5.getYaw())
               );
               var5.setSprinting(false);
               var1.setState(new PlayerInput(true, false, false, false, false, true, false), new Vec2f(0.0F, 1.0F));
               return true;
            }
         }
      } else {
         this.JK();
         return false;
      }
   }

   private boolean JN(Item var1) {
      if (var1 != null && var1 != Items.AIR && this.mc.player != null) {
         if (!this.mc.player.getMainHandStack().isOf(var1) && !this.mc.player.getOffHandStack().isOf(var1)) {
            if ((Boolean)this.be.get()
               || (Boolean)this.aBD.get()
                  && AliceObf_ga.Nv(var1) != Blocks.AIR
               || (Boolean)this.aI.get()
                  && AliceObf_ga.Ns(
                        EasyPlace.MA(var1)
                     )
                     != 0) {
               FindItemResult var2 = InvUtils.find(new Item[]{var1});
               if (var2.found()) {

               } else {
                  short var3 = 3640;
               }

               return false;
            } else {
               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private Item getRequiredPlacementItem(BlockState var1) {
      if (AliceObf_ga.Nt(var1)) {
         return AliceObf_ga.Nu(var1, (Boolean)this.aBD.get());
      } else {
         Item var2 = AliceObf_gZ.getRequiredItem(var1);
         if ((Boolean)this.aI.get() && var1 != null && this.mc.player != null && AliceObf_ga.Ns(var1.getBlock()) != 0 && !this.JN(var2)) {
            for (Hand var6 : Hand.values()) {
               ItemStack var7 = this.mc.player.getStackInHand(var6);
               if (this.Mw(var7) && this.Mz(var7, var2)) {
                  return var7.getItem();
               }
            }

            for (int var8 = 0; var8 < this.mc.player.getInventory().size(); var8++) {
               ItemStack var9 = this.mc.player.getInventory().getStack(var8);
               if (this.Mw(var9) && this.Mz(var9, var2)) {
                  return var9.getItem();
               }
            }

            return var2;
         } else {
            return var2;
         }
      }
   }

   private BlockHitResult JO(BlockPos var1, Item var2) {
      if (this.mc.player != null) {
         int var10000 = this.mc.world != null ? 54804 : 54805;

         while (true) {
            int var14 = var10000;
            if (var14 == 54804) {
               if ((Boolean)this.aBD.get()
                  && AliceObf_ga.Nu(this.If(var1), true) == var2
                  && AliceObf_ga.Nw(
                     this.If(var1), this.mc.world.getBlockState(var1), true
                  )) {
                  if (var2 == Items.WATER_BUCKET) {
                     char var15 = 56870;
                  } else {
                     char var16 = 56871;
                  }

                  BlockHitResult var3 = null;
                  double var4 = Double.MAX_VALUE;

                  for (Direction var9 : Direction.values()) {
                     BlockPos var10 = var1.offset(var9);
                     if (this.LJ(var10) && (var2 != Items.WATER_BUCKET || !(this.mc.world.getBlockState(var10).getBlock() instanceof FluidFillable))) {
                        BlockHitResult var11 = this.LF(var1, var9);
                        if (var11 != null && var11.getBlockPos().offset(var11.getSide()).equals(var1)) {
                           double var12 = this.mc.player.getEyePos().squaredDistanceTo(var11.getPos());
                           if (var12 < var4) {
                              var3 = var11;
                              var4 = var12;
                           }
                        }
                     }
                  }

                  return var3;
               }
               break;
            }

            if (var14 != 54805) {
               break;
            }

         }
      }

      return null;
   }

   private boolean JP(AliceObf_qK var1) {
      if ((Boolean)this.aBD.get()
         && this.mc.player != null
         && this.mc.world != null
         && this.mc.interactionManager != null
         && this.rA <= 0
         && var1.mr == this.mc.player.getStackInHand(var1.Nu).getItem()) {
         int var10000 = AliceObf_ga.Nu(this.If(var1.sc), true)
               == var1.mr
            ? 51943
            : 51944;

         while (true) {
            int var7 = var10000;
            if (var7 == 51943) {
               if (AliceObf_ga.Nw(
                  this.If(var1.sc), this.mc.world.getBlockState(var1.sc), true
               )) {
                  if (var1.mr == Items.WATER_BUCKET && (Boolean)this.mc.world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.WATER_EVAPORATES_GAMEPLAY, var1.sc)) {
                     return false;
                  }

                  HitResult var2 = this.mc.player.raycast(this.Jy(), 1.0F, false);
                  if (var2 instanceof BlockHitResult var3
                     && var2.getType() == HitResult.Type.BLOCK
                     && var3.getBlockPos().equals(var1.ajf.getBlockPos())
                     && var3.getSide() == var1.ajf.getSide()
                     && var3.getBlockPos().offset(var3.getSide()).equals(var1.sc)) {
                     if (this.LJ(var3.getBlockPos())) {
                        char var8 = 59345;
                     } else {
                        char var9 = 59346;
                     }
                  }

                  return false;
               }
               break;
            }

            if (var7 != 51944) {
               break;
            }

         }
      }

      return false;
   }

   @AliceHandler
   public void JQ(meteordevelopment.meteorclient.events.world.TickEvent.Post var1) {
      this.KQ();
      this.KK();
      this.Mo();
      this.Mf();
      this.Mb();
      this.IF();
      this.Js();
      if (!(this.alT > 0)) {
         short var2 = 29799;
      }

      if (this.aLV > 0) {
         this.aLV--;
      }

      if (this.aaz > 0) {
         this.aaz--;
      }

      if (this.rA > 0) {
         this.rA--;
      }

      if (this.aQV > 0) {
         this.aQV--;
      }

      this.avW.clear();
      if (this.alh > 0) {
         this.alh--;
      }
   }

   private boolean JR(Hand var1, BlockHitResult var2, boolean var3) {
      if (this.JN == null && !AliceObf_bT.atL(this.mc)) {
         BlockPos var4 = this.getPlacePos(var2);
         if (this.Pj != null && this.Pj.aBU.equals(var4)) {
            int var10000 = this.ky ? 62512 : 62513;

            while (true) {
               int var19 = var10000;
               if (var19 == 62512) {
                  if (!this.Id(this.Pj)) {
                     return true;
                  }
                  break;
               }

               if (var19 != 62513) {
                  return true;
               }

            }
         }

         Item var5 = !this.be.get() && !this.Cf.get() && !this.aBD.get() ? null : this.getRequiredPlacementItem(this.If(var4));
         if (this.Ia(var4) && this.Ib(var4)) {
            if (!this.Ik(var4, this.JC(var4, var1, var2))) {
               return true;
            } else {
               if (this.JA(var4, this.JC(var4, var1, var2))) {
                  short var35 = 16992;
               } else {
                  short var36 = 16993;
               }

               Hand var6 = var1;
               if ((Boolean)this.be.get()
                  || (Boolean)this.aBD.get() && AliceObf_ga.Nv(var5) != Blocks.AIR
                  || (Boolean)this.aI.get()
                     && AliceObf_ga.Ns(MA(var5)) != 0) {
                  if (!(var5 != null)) {
                     short var37 = 23434;
                  }
               }

               ItemStack var7 = this.mc.player.getStackInHand(var1);
               if (!this.Mw(var7)) {
                  if (this.Mw(this.mc.player.getMainHandStack())) {
                     var6 = Hand.MAIN_HAND;
                  } else if (!this.Mw(this.mc.player.getOffHandStack())) {

                  } else {
                     char var38 = 52139;
                  }

                  var7 = this.mc.player.getStackInHand(var6);
               }

               if (this.Mx(var7, var4)) {
                  return true;
               } else if ((Boolean)this.Cf.get()
                  && AliceObf_gZ.GJ()
                  && AliceObf_gZ.GW(var4)
                  && My(var7)
                  && !this.Mz(var7, var5)) {
                  this.MI("Schematic Only: block trong tay khong dung o schematic nay");
                  return true;
               } else {
                  boolean var8 = this.Mw(var7);
                  int var39;
                  if (!var8 && !this.Mw(this.mc.player.getMainHandStack())) {
                     var39 = this.Mw(this.mc.player.getOffHandStack()) ? 6684 : 6685;
                     boolean var10003 = false;
                  } else {
                     var39 = 1;
                  }

                  int var9 = var39;
                  if (var9 == 0) {
                     return !this.Ik(var4, this.JD(var4, var1, var2));
                  } else if (AliceObf_ga.Nv(var7.getItem()) == Blocks.AIR) {
                     BlockHitResult var22 = var2;
                     boolean var11 = false;
                     boolean var12 = false;
                     Block var13 = var7.getItem() instanceof BlockItem var14 ? var14.getBlock() : null;
                     if (var13 instanceof ChestBlock) {
                        ChestType var23 = AliceObf_gZ.getChestType(
                           var4
                        );
                        if (var23 != null) {
                           BlockHitResult var27 = this.LD(var4, var23, var13);
                           if (var27 == null) {
                              char var41 = 54552;
                           } else {
                              char var42 = 54553;
                           }

                           var22 = var27;
                        }
                     }

                     if (!(var13 instanceof HopperBlock)) {
                        if (var13 instanceof ShulkerBoxBlock) {
                           char var43 = 33034;
                        } else {
                           char var44 = 33035;
                        }

                        if ((Boolean)this.be.get()
                           && AliceObf_gZ.Hf(var4)) {
                           BlockHitResult var24 = this.Lz(var4);
                           if (var24 != null) {
                              var22 = var24;
                           }
                        }
                     } else {
                        Direction var25 = this.getWantedHopperFacing(var4);
                        if (var25 != null) {
                           char var45 = 59991;
                        } else {
                           char var46 = 59992;
                        }
                     }

                     BlockState var26 = this.If(var4);
                     if (this.JV(var7, var26, var4)) {
                        BlockHitResult var28 = this.JW(var4, var6, var13, var26, var22);
                        if (var28 == null) {
                           this.LL(var4, "khong thay mat cho state dung theo schematic");
                           return true;
                        }

                        var22 = var28;
                        var11 = true;
                     }

                     if (!this.adJ.containsKey(var22.getBlockPos())) {
                        char var47 = 58787;
                     } else {
                        char var48 = 58786;
                     }

                     if (var8 && !this.amq && ((Boolean)this.aJE.get() || var3 || var11)) {
                        if (this.axK) {
                           return true;
                        }

                        if (this.ky) {
                           this.Kb(var6, var22, var4, false);
                           return true;
                        }

                        if (var11) {
                           BlockHitResult var31 = this.JS(var22, var4);
                           if (var31 != null) {
                              if (this.JT(var6, var31, var4, var13)) {
                                 short var51 = 21483;
                              } else {
                                 short var52 = 21484;
                              }
                           }

                           this.Kb(var6, var22, var4, var12);
                           return true;
                        }

                        AliceObf_mH var29 = (AliceObf_mH)this.md.get();
                        Orientation var16 = null;
                        if (var29 == AliceObf_mH.Lb) {
                           if (var13 instanceof CrafterBlock) {
                              var16 = AliceObf_gZ.getOrientation(var4);
                           }

                           var29 = AliceObf_gZ.getRotation(var4);
                        }

                        int var10004 = var29 != null ? 34012 : 34013;

                        while (true) {
                           int var21 = var10004;
                           if (var21 == 34012) {
                              if (var29 != AliceObf_mH.Lb) {
                                 BlockHitResult var17 = this.JS(var22, var4);
                                 if (var17 != null && this.JT(var6, var17, var4, var13)) {
                                    this.JZ(var6, var17, var4);
                                    return true;
                                 }

                                 AliceObf_mH var30 = EasyPlace.MJ(
                                    var29, var13
                                 );
                                 BlockHitResult var18 = this.Lx(var4, var22, var30, var13, var7.getItem(), var16, var3);
                                 if (var18 == null) {
                                    this.LL(var4, ("" + var30.name()));
                                    return true;
                                 }

                                 this.Ka(var6, var18, var4, var30, var13, var16, var12);
                                 return true;
                              }
                              break;
                           }

                           if (var21 != 34013) {
                              break;
                           }

                        }

                        if (var3) {
                           char var49 = 43944;
                        } else {
                           char var50 = 43945;
                        }
                     }

                     if (var3) {
                        return true;
                     } else if (!this.amq && this.Mh(var4, var22, var7.getItem())) {
                        this.Kb(var6, var22, var4, false);
                        return true;
                     } else {
                        return !this.Ik(var4, this.JD(var4, var1, var2));
                     }
                  } else if ((Boolean)this.aBD.get()
                     && var7.getItem() == var5
                     && AliceObf_ga.Nw(
                        this.If(var4), this.mc.world.getBlockState(var4), true
                     )) {
                     BlockHitResult var10 = this.JO(var4, var7.getItem());
                     if (var10 != null && !this.axK) {
                        this.Kb(var6, var10, var4, false);
                     }

                     return true;
                  } else {
                     return false;
                  }
               }
            }
         } else {
            if (!var3) {
               int var32 = var5 != null ? 8353 : 8354;

               while (true) {
                  int var20 = var32;
                  if (var20 == 8353) {
                     if (var5 != Items.AIR) {
                        return true;
                     }
                     break;
                  }

                  if (var20 != 8354) {
                     break;
                  }

                  var32 = 8352;
               }

               if (!this.Mw(this.mc.player.getStackInHand(var1))) {
                  if (this.Mw(this.mc.player.getMainHandStack())) {
                     short var33 = 32248;
                  } else {
                     short var34 = 32247;
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private BlockHitResult JS(BlockHitResult var1, BlockPos var2) {
      int var10000 = this.mc.player != null ? 23796 : 23797;

      while (true) {
         int var7 = var10000;
         if (var7 == 23796) {
            if (this.mc.world != null) {
               char var8 = 52049;
            } else {
               char var9 = 52050;
            }
            break;
         }

         if (var7 != 23797) {
            break;
         }

      }

      return null;
   }

   private boolean JT(Hand var1, BlockHitResult var2, BlockPos var3, Block var4) {
      if (this.mc.player != null && var1 != null && var2 != null && var3 != null && var4 != null) {
         ItemStack var5 = this.mc.player.getStackInHand(var1);
         if (var5.isEmpty()) {
            return false;
         } else {
            ItemPlacementContext var6 = new ItemPlacementContext(this.mc.player, var1, var5, var2);
            if (!var6.getBlockPos().equals(var3)) {

            } else {
               short var10 = 23980;
            }

            BlockState var7 = this.getPlacementStateForHit(var5, var4, var6);
            AliceObf_mH var8 = (AliceObf_mH)this.md.get();
            if (!(var8 != AliceObf_mH.Lb)) {
               char var11 = 59416;
            }

            if (!(var7 != null)) {
               char var12 = 44622;
            }

            return false;
         }
      } else {
         return false;
      }
   }

      private boolean JU(ItemStack var1, BlockState var2, BlockPos var3) {
      return this.JV(var1, var2, var3) || (var2 != null && !var2.isAir());
   }

   private boolean JV(ItemStack var1, BlockState var2, BlockPos var3) {
      if (var1 != null && !var1.isEmpty() && var2 != null && !var2.isAir()) {
         if (!AliceObf_sW.azf(var2)) {
            if (!var2.contains(SlabBlock.TYPE)) {

            } else {
               char var6 = 39705;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private BlockHitResult JW(BlockPos var1, Hand var2, Block var3, BlockState var4, BlockHitResult var5) {
      if (this.mc.player != null && this.mc.world != null && var1 != null && var2 != null && var3 != null && var4 != null) {
         if (this.JX(var1, var2, var3, var4, var5)) {
            return var5;
         } else {
            Vec3d var6 = this.mc.player.getEyePos();
            float var7 = this.mc.player.getYaw();
            float var8 = this.mc.player.getPitch();
            BlockHitResult var9 = null;
            double var10 = Double.MAX_VALUE;

            for (Direction var15 : Direction.values()) {
               BlockPos var16 = var1.offset(var15);
               if (this.LK(var16)) {
                  Direction var17 = var15.getOpposite();
                  if (AliceObf_sW.azf(var4) && this.LJ(var16)) {
                     BlockHitResult var25 = AliceObf_oS.auo(
                        this.mc, var16, var17, var6, this.Jy()
                     );
                     if (this.JX(var1, var2, var3, var4, var25)) {
                        double var26 = var6.squaredDistanceTo(var25.getPos());
                        if (var26 < var10) {
                           var10 = var26;
                           var9 = var25;
                        }
                     }
                  } else {
                     for (Vec3d var21 : this.Ms(var16, var17)) {
                        if (this.Mt(var16, var21)) {
                           BlockHitResult var22 = new BlockHitResult(var21, var17, var16, false);
                           if (this.JX(var1, var2, var3, var4, var22)) {
                              double var23 = var6.squaredDistanceTo(var21);
                              if (this.Ky(var6, var7, var8, var16)) {
                                 var23 -= 1000.0;
                              }

                              if (var23 < var10) {
                                 var10 = var23;
                                 var9 = var22;
                              }
                           }
                        }
                     }
                  }
               }
            }

            return var9;
         }
      } else {
         return null;
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean JX(BlockPos var1, Hand var2, Block var3, BlockState var4, BlockHitResult var5) {
      int var10000 = var5 != null ? 50132 : 50133;

      while (true) {
         int var14 = var10000;
         if (var14 == 50132) {
            if ((!this.adJ.containsKey(var5.getBlockPos()) || this.aOG.get() != AliceObf_hi.Zz)
               && this.getPlacePos(var5).equals(var1)
               && this.Mt(var5.getBlockPos(), var5.getPos())) {
               ItemStack var6 = this.mc.player.getStackInHand(var2);
               if (!AliceObf_sW.azf(var4)) {
                  if (var6.getItem() instanceof VerticallyAttachableBlockItem) {
                     short var23 = 1394;
                  } else {
                     short var24 = 1393;
                  }
               }

               boolean var7 = true;
               float var8 = this.mc.player.getYaw();
               float var9 = this.mc.player.getPitch();
               if (var7) {
                  float[] var10 = Kc(this.mc.player.getEyePos(), var5.getPos());
                  this.mc.player.setYaw(KG(var10[0], var8));
                  this.mc.player.setPitch(var10[1]);
               }

               boolean var16 = false /* VF: Semaphore variable */;

               boolean var12;
               label217: {
                  boolean var11;
                  try {
                     var16 = true;
                     ItemPlacementContext var18 = new ItemPlacementContext(this.mc.player, var2, var6, var5);
                     if (var18.getBlockPos().equals(var1)) {
                        BlockState var19 = this.getPlacementStateForHit(var6, var3, var18);
                        var12 = var19 != null
                           && AliceObf_gZ.Hd(var4, var19);
                        var16 = false;
                        break label217;
                     }

                     var11 = false;
                     var16 = false;
                  } finally {
                     if (var16) {
                        if (var7) {
                           this.mc.player.setYaw(var8);
                           this.mc.player.setPitch(var9);
                        }
                     }
                  }

                  if (var7) {
                     char var25 = 42128;
                  } else {
                     char var26 = 42129;
                  }

                  return var11;
               }

               if (var7) {
                  this.mc.player.setYaw(var8);
                  this.mc.player.setPitch(var9);
               }

               return var12;
            }
            break;
         }

         if (var14 != 50133) {
            break;
         }

      }

      return false;
   }

   private BlockState getPlacementStateForHit(ItemStack var1, Block var2, ItemPlacementContext var3) {
      if (var1 != null && !var1.isEmpty() && var2 != null && var3 != null) {
         BlockPos var4 = var3.getBlockPos();
         BlockPos var5 = var4.offset(var3.getSide().getOpposite());
         boolean var6 = this.aOG.get() == AliceObf_hi.aFc && this.adJ.containsKey(var5);
         BlockState var7 = this.If(var4);
         if (!(var1.getItem() instanceof VerticallyAttachableBlockItem)) {
            BlockState var17 = var2.getPlacementState(var3);
            return var17 == null && var6 && var7 != null && var7.getBlock() == var2 ? var7 : var17;
         } else {
            Block var8 = EasyPlace.JY(var2);
            if (var7 != null && var7.getBlock() != var2 && var7.getBlock().asItem() == var1.getItem()) {
               var8 = var7.getBlock();
            }

            BlockState var9 = var2.getPlacementState(var3);
            BlockState var10 = var8 == null ? null : var8.getPlacementState(var3);
            if (var6 && var7 != null) {
               if (var7.getBlock() == var2 && var9 == null) {
                  var9 = var7;
               } else {
                  int var10000 = var7.getBlock() == var8 ? 61324 : 61325;

                  while (true) {
                     int var16 = var10000;
                     if (var16 == 61324) {
                        if (var10 == null) {
                           var10 = var7;
                        }
                        break;
                     }

                     if (var16 != 61325) {
                        break;
                     }

                  }
               }
            }

            for (Direction var14 : var3.getPlacementDirections()) {
               if (var14 == Direction.DOWN) {
                  char var18 = 60784;
               } else {
                  char var19 = 60785;
               }

               BlockState var15 = var14 == Direction.UP ? var9 : var10;
               if (var15 != null && (var15.canPlaceAt(this.mc.world, var4) || var6 && var4.offset(var14.getOpposite()).equals(var5))) {
                  return var15;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   private static Block JY(Block var0) {
      if (var0 != Blocks.TORCH) {
         if (var0 == Blocks.SOUL_TORCH) {
            return Blocks.SOUL_WALL_TORCH;
         } else {
            return var0 != Blocks.REDSTONE_TORCH ? null : Blocks.REDSTONE_WALL_TORCH;
         }
      } else {
         return Blocks.WALL_TORCH;
      }
   }

   private void JZ(Hand var1, BlockHitResult var2, BlockPos var3) {
      if (this.mc.player != null) {
         if (this.Ib(var3)) {
            if (this.Pj != null && this.Pj.aBU.equals(var3)) {
               if (this.ky && this.Id(this.Pj)) {
                  this.Kb(var1, var2, var3, false);
               }
            } else {
               ItemStack var4 = this.mc.player.getStackInHand(var1);
               if (!var4.isEmpty()) {
                  Item var5 = var4.getItem();
                  if (!this.Mh(var3, var2, var5)) {

                  } else {
                     char var9 = 54242;
                  }

                  BlockState var6 = this.JD(var3, var1, var2);
                  if (var6 != null || AliceObf_oN.activeCompanionBuilder() == null) {
                     if (!this.Ik(var3, var6)) {

                     } else {
                        char var10 = 40544;
                     }
                  }
               }
            }
         }
      }
   }

   private BlockPos getPlacePos(BlockHitResult var1) {
      BlockPos var2 = var1.getBlockPos();
      BlockState var3 = this.mc.world.getBlockState(var2);
      return var3.isReplaceable() && !this.Me(var2) ? var2 : var2.offset(var1.getSide());
   }

   private void Ka(Hand var1, BlockHitResult var2, BlockPos var3, AliceObf_mH var4, Block var5, Orientation var6, boolean var7) {
      if (this.mc.player != null && var4.pQ != null) {
         this.Ke(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   private void Kb(Hand var1, BlockHitResult var2, BlockPos var3, boolean var4) {
      if (this.mc.player != null) {
         ItemStack var5 = this.mc.player.getStackInHand(var1);
         Block var6 = !(var5.getItem() instanceof BlockItem var7) ? null : var7.getBlock();
         this.Ke(var1, var2, var3, null, var6, null, var4);
      }
   }

   private static float[] Kc(Vec3d var0, Vec3d var1) {
      double var2 = var1.x - var0.x;
      double var4 = var1.y - var0.y;
      double var6 = var1.z - var0.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var6, var2)) - 90.0F;
      float var11 = (float)(-Math.toDegrees(Math.atan2(var4, var8)));
      return new float[]{var10, var11};
   }

   private static boolean Kd(Block var0) {
      if (!(var0 != null)) {
         char var1 = 38636;
      }

      return false;
   }

   private void Ke(Hand var1, BlockHitResult var2, BlockPos var3, AliceObf_mH var4, Block var5, Orientation var6, boolean var7) {
      if (this.mc.player != null) {
         if (this.Ib(var3)) {
            ItemStack var8 = this.mc.player.getStackInHand(var1);
            if (!var8.isEmpty()) {
               boolean var9 = this.Mh(var3, var2, var8.getItem());
               int var10 = ++this.lM;
               this.axK = true;
               AliceObf_qK var11 = new AliceObf_qK(var10, var1, var2, var3, var8.getItem(), var7, var4, var5, var6, var9);
               var11.VY = !this.ky ? null : this.Ie(var3);
               if (!(var4 != null)) {
                  short var12 = 7075;
               }

               this.aht = var11;
               this.Kg();
            }
         }
      }
   }

   private boolean Kf(BlockHitResult var1) {
      ClientPlayerEntity var2 = this.mc.player;
      if (var2 != null && var1 != null) {
         AliceObf_ru var3 = this.IZ();
         return var3 != null
            && var3.acj(var2.getYaw(), var2.getPitch())
            && this.Ky(new Vec3d(var3.acn(), var3.aco() + var3.acq(), var3.acp()), var3.acr(), var3.acs(), var1.getBlockPos());
      } else {
         return false;
      }
   }

   private void Kg() {
      ClientPlayerEntity var1 = this.mc.player;
      if (var1 != null) {
         if (this.jB != null && !this.jB.Fw()) {
            Input var3 = var1.input;

            while (var3 instanceof AliceObf_au) {
               AliceObf_au var2 = (AliceObf_au)var3;
               if (var3 == this.jB) {
                  return;
               }

               var3 = var2.Fu();
            }

            this.jB.Fv();
         }

         this.avl = var1.input;
         this.jB = new AliceObf_em(this, this.avl);
         var1.input = this.jB;
      }
   }

   private void Kh() {
      if (this.jB != null) {
         AliceObf_au.Fy(this.mc.player, this.jB);
      }

      this.jB = null;
      this.avl = null;
   }

   private void Ki(AliceObf_em var1, PlayerInput var2, Vec2f var3) {
      AliceObf_qK var4 = this.aht;
      ClientPlayerEntity var5 = this.mc.player;
      if (!(var4 != null)) {
         short var12 = 31763;
      }
   }

   private AliceObf_fo Kj(BlockHitResult var1, boolean var2, boolean var3, boolean var4, PlayerInput var5, Vec2f var6) {
      ClientPlayerEntity var7 = this.mc.player;
      if (var7 == null) {
         return null;
      } else {
         float var8 = var7.getYaw();
         float var9 = var7.getPitch();
         Vec3d var10 = var7.getEyePos();
         Vec3d var11 = var2 ? var1.getPos() : Vec3d.ofCenter(var1.getBlockPos());
         boolean var12 = !var2 && this.KA(var1, var10);
         int var10000 = var12 ? 49158 : 49159;

         while (true) {
            int var29 = var10000;
            if (var29 == 49158) {
               var11 = var1.getPos();
               break;
            }

            if (var29 != 49159) {
               break;
            }

         }

         float[] var13 = EasyPlace.Kc(var10, var11);
         var13[0] = EasyPlace.KG(var13[0], var8);
         if (var12 && !this.KB(var10, var13[0], var13[1], var1)) {
            return null;
         } else {
            boolean var14 = var6 != null && var6.lengthSquared() > 1.0E-5F;
            if (var3) {
               char var31 = 36389;
            } else {
               char var32 = 36390;
            }

            AliceObf_fo var15 = null;
            double var16 = Double.MAX_VALUE;
            if (var14) {
               double var18 = Math.toDegrees(Math.atan2(var6.y, var6.x));

               for (AliceObf_rY var23 : AliceObf_rY.aMq) {
                  float var24 = KG((float)(var8 + var18 - var23.Gj()), var8);
                  float var25 = var13[1];
                  boolean var26 = var12
                     ? this.KB(var10, var24, var25, var1)
                     : (var2 ? this.Kz(var10, var24, var25, var1.getBlockPos()) : this.Ky(var10, var24, var25, var1.getBlockPos()));
                  if (var26) {
                     double var27 = EasyPlace.KD(
                        var7, var5, var23, var24, var25, var8, var13
                     );
                     if (var27 < var16) {
                        var16 = var27;
                        var15 = new AliceObf_fo(var24, var25, var23.Gl(var5), var23.Gk());
                     }
                  }
               }
            }

            if (var15 != null) {
               return var15;
            } else if (var14 && var4) {
               return null;
            } else {
               float[] var30 = EasyPlace.Kc(var10, var11);
               var30[0] = EasyPlace.KG(var30[0], var8);
               return new AliceObf_fo(
                  var30[0],
                  var30[1],
                  EasyPlace.KF(var5),
                  Vec2f.ZERO
               );
            }
         }
      }
   }

   private AliceObf_fo Kk(AliceObf_qK var1, PlayerInput var2, Vec2f var3) {
      ClientPlayerEntity var4 = this.mc.player;
      if (var4 == null) {
         return null;
      } else {
         float var5 = var4.getYaw();
         float var6 = var4.getPitch();
         Vec3d var7 = var4.getEyePos();
         Vec3d var8 = Vec3d.ofCenter(var1.ajf.getBlockPos());
         float[] var9 = EasyPlace.Kc(var7, var8);
         boolean var10 = var3 != null && var3.lengthSquared() > 1.0E-5F;
         AliceObf_fo var11 = null;
         Object var12 = null;
         double var13 = Double.MAX_VALUE;
         double var15 = Double.MAX_VALUE;
         if (var10) {
            double var17 = Math.toDegrees(Math.atan2(var3.y, var3.x));

            for (AliceObf_rY var22 : AliceObf_rY.aMq) {
               float var23 = KG((float)(var5 + var17 - var22.Gj()), var5);
               float var24 = this.Kp(var1, var23, var9[1], var6);
               if (Float.isFinite(var24) && this.Kq(var1, var23, var24)) {
                  PlayerInput var25 = var22.Gl(var2);
                  Vec2f var26 = var22.Gk();
                  boolean var27 = this.Ky(var7, var23, var24, var1.ajf.getBlockPos());
                  double var28 = EasyPlace.KD(
                     var4, var2, var22, var23, var24, var5, var9
                  );
                  AliceObf_fo var30 = new AliceObf_fo(var23, var24, var25, var26);
                  if (!(var28 < var15)) {

                  } else {
                     char var35 = 53740;
                  }

                  if (var27 && var28 < var13) {
                     var13 = var28;
                     var11 = var30;
                  }
               }
            }
         }

         if (var11 != null) {
            return var11;
         } else {
            float[] var31 = this.Km(var1, var7, var5, var6);
            if (var31 != null) {
               PlayerInput var32 = EasyPlace.KF(var2);
               return new AliceObf_fo(var31[0], var31[1], var32, Vec2f.ZERO);
            } else if (var12 == null || !this.Kl(var1, var7, (AliceObf_fo)var12) && !var1.RK) {
               if (var1.aho != null) {
                  short var36 = 17018;
               } else {
                  short var37 = 17019;
               }

               float var18 = EasyPlace.KG(var1.aNe.Hx(var5), var5);
               float var33 = this.Kp(var1, var18, var9[1], var6);
               if (!Float.isFinite(var33)) {
                  var33 = var1.aNe.Hy(var6);
               }

               AliceObf_fo var34 = new AliceObf_fo(var18, var33, KF(var2), Vec2f.ZERO);
               if (!this.Kq(var1, var18, var33)) {
                  return null;
               } else {
                  return !this.Kl(var1, var7, var34) && !var1.RK ? null : var34;
               }
            } else {
               return (AliceObf_fo)var12;
            }
         }
      }
   }

      private boolean Kl(AliceObf_qK var1, Vec3d var2, AliceObf_fo var3) {
      return var1.aLW
         ? this.Kz(var2, var3.HE(), var3.HF(), var1.ajf.getBlockPos())
         : this.Ky(var2, var3.HE(), var3.HF(), var1.ajf.getBlockPos());
   }

   private float[] Km(AliceObf_qK var1, Vec3d var2, float var3, float var4) {
      BlockPos var5 = var1.ajf.getBlockPos();
      double[] var6 = new double[]{0.1, 0.3, 0.5, 0.7, 0.9};
      float[] var7 = null;
      double var8 = Double.MAX_VALUE;
      Vec3d[] var10 = new Vec3d[]{
         var1.ajf.getPos(),
         Vec3d.ofCenter(var5),
         EasyPlace.Mv(var5, var1.ajf.getSide())
      };

      for (Vec3d var14 : var10) {
         float[] var15 = Kc(var2, var14);
         var15[0] = EasyPlace.KG(var15[0], var3);
         if (this.Kq(var1, var15[0], var15[1]) && this.Kn(var1, var2, var15)) {
            double var16 = KI(var15[0], var3) + Math.abs(var15[1] - var4);
            if (var16 < var8) {
               var8 = var16;
               var7 = var15;
            }
         }
      }

      if (var7 != null) {
         return var7;
      } else {
         for (double var33 : var6) {
            for (double var19 : var6) {
               for (double var24 : var6) {
                  Vec3d var26 = new Vec3d(var5.getX() + var33, var5.getY() + var19, var5.getZ() + var24);
                  float[] var27 = Kc(var2, var26);
                  var27[0] = EasyPlace.KG(var27[0], var3);
                  if (this.Kq(var1, var27[0], var27[1])) {
                     double var28 = EasyPlace.KI(var27[0], var3)
                        + Math.abs(var27[1] - var4);
                     if (!(var28 >= var8)) {
                        if (!this.Kn(var1, var2, var27)) {

                        } else {
                           char var35 = 51600;
                        }

                        var8 = var28;
                        var7 = var27;
                     }
                  }
               }
            }
         }

         return var7;
      }
   }

   private boolean Kn(AliceObf_qK var1, Vec3d var2, float[] var3) {
      return var1.aLW ? this.Kz(var2, var3[0], var3[1], var1.ajf.getBlockPos()) : this.Ky(var2, var3[0], var3[1], var1.ajf.getBlockPos());
   }

   private boolean Ko(AliceObf_qK var1) {
      if (var1.kE == null) {
         return false;
      } else if (var1.akJ != null) {
         return true;
      } else if (var1.aNe != null && !this.Kq(var1, var1.sE, var1.fZ)) {
         return false;
      } else {
         if (!(!var1.aLW)) {
            char var2 = 48421;
         }

         return this.Ky(var1.kE, var1.sE, var1.fZ, var1.ajf.getBlockPos()) ? true : var1.RK;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private float Kp(AliceObf_qK var1, float var2, float var3, float var4) {
      int var10000 = var1.aNe != null ? 994 : 995;

      while (true) {
         int var10 = var10000;
         if (var10 == 994) {
            if (Kd(var1.aKH)) {
               if (this.Kq(var1, var2, var3)) {
                  return var3;
               }

               Direction var5 = var1.aNe.pQ.getOpposite();

               float var6 = switch (AliceObf_nY.avN[var5.ordinal()]) {
                  case 1 -> -89.9F;
                  case 2 -> 89.9F;
                  default -> 0.0F;
               };

               for (int var7 = 1; var7 <= 360; var7++) {
                  float var8 = var7 / 360.0F;
                  float var9 = var3 + (var6 - var3) * var8;
                  if (this.Kq(var1, var2, var9)) {
                     return var9;
                  }
               }

               if (this.Kq(var1, var2, var4)) {
                  return var4;
               }

               return Float.NaN;
            }
            break;
         }

         if (var10 != 995) {
            break;
         }

      }

      return var3;
   }

   private boolean Kq(AliceObf_qK var1, float var2, float var3) {
      return var1.aho != null
         ? EasyPlace.Kv(var2, var3) == var1.aho
         : EasyPlace.Ku(var1.aNe, var1.aKH, var2, var3);
   }

   private static boolean Kr(AliceObf_mH var0, Block var1, Orientation var2, float var3, float var4) {
      return var2 != null
         ? EasyPlace.Kv(var3, var4) == var2
         : EasyPlace.Ku(var0, var1, var3, var4);
   }

      private static boolean Ks(Block var0, Item var1, BlockPos var2) {
      if (var0 instanceof SlabBlock || var0 instanceof ComparatorBlock || var0 instanceof RepeaterBlock || var1 instanceof VerticallyAttachableBlockItem) {
         return true;
      }
      return !AliceObf_gZ.getPlacementSupportDirections(var2).isEmpty();
   }

      private static boolean Kt(AliceObf_mH var0, Block var1, boolean var2) {
      return var0 != null && var0.pQ != null && !var2 && !(var1 instanceof HopperBlock) && !(var1 instanceof ShulkerBoxBlock);
   }

   private static boolean Ku(AliceObf_mH var0, Block var1, float var2, float var3) {
      if (var0 != null && var0.pQ != null) {
         Direction var4 = var0.pQ.getOpposite();
         if (Kd(var1)) {
            if (Direction.getFacing(Vec3d.fromPolar(var3, var2)) != var4) {

            } else {
               short var5 = 30762;
            }

            return false;
         } else {
            return var4.getAxis() != Axis.Y && Direction.fromHorizontalDegrees(var2) == var4;
         }
      } else {
         return true;
      }
   }

   private static Orientation Kv(float var0, float var1) {
      Direction var2 = Direction.getFacing(Vec3d.fromPolar(var1, var0));
      Direction var3 = var2.getOpposite();
      Direction var4 = Direction.fromHorizontalDegrees(var0);
      int var6 = AliceObf_nY.avN[var3.ordinal()] ^ -804205233;
      Direction var5 = var6 != -804205235 ? (var6 != -804205234 ? Direction.UP : var4) : var4.getOpposite();
      return Orientation.byDirections(var3, var5);
   }

   private static float Kw(Orientation var0, float var1) {
      int var4 = AliceObf_nY.avN[var0.getFacing().ordinal()];
      Direction var2 = var4 == 1 ? var0.getRotation() : (var4 != 2 ? var0.getFacing().getOpposite() : var0.getRotation().getOpposite());
      int var5 = AliceObf_nY.avN[var2.ordinal()];
      float var3 = var5 == 3 ? 180.0F : (var5 == 4 ? 0.0F : (var5 == 5 ? 90.0F : (var5 != 6 ? var1 : -90.0F)));
      return EasyPlace.KG(var3, var1);
   }

   private static float Kx(Orientation var0) {
      int var1 = AliceObf_nY.avN[var0.getFacing().getOpposite().ordinal()] ^ 1625724032;
      return var1 != 1625724033 ? (var1 == 1625724034 ? 89.9F : 0.0F) : -89.9F;
   }

   private boolean Ky(Vec3d var1, float var2, float var3, BlockPos var4) {
      if (this.mc.player == null) {
         return false;
      } else {
         double var5 = this.mc.player.getBlockInteractionRange();
         Vec3d var7 = var1.add(Vec3d.fromPolar(var3, var2).multiply(var5));
         return new Box(var4).raycast(var1, var7).isPresent();
      }
   }

   private boolean Kz(Vec3d var1, float var2, float var3, BlockPos var4) {
      return this.KC(var1, var2, var3, var4) != null;
   }

   private boolean KA(BlockHitResult var1, Vec3d var2) {
      if (!this.LJ(var1.getBlockPos())) {
         return false;
      } else {
         float[] var3 = Kc(var2, Vec3d.ofCenter(var1.getBlockPos()));
         return !this.KB(var2, var3[0], var3[1], var1);
      }
   }

   private boolean KB(Vec3d var1, float var2, float var3, BlockHitResult var4) {
      BlockHitResult var5 = this.KC(var1, var2, var3, var4.getBlockPos());
      return var5 != null && var5.getSide() == var4.getSide();
   }

   private BlockHitResult KC(Vec3d var1, float var2, float var3, BlockPos var4) {
      if (this.mc.player != null && this.mc.world != null) {
         double var5 = this.mc.player.getBlockInteractionRange();
         Vec3d var7 = var1.add(Vec3d.fromPolar(var3, var2).multiply(var5));
         BlockHitResult var8 = this.mc.world.raycast(new RaycastContext(var1, var7, ShapeType.OUTLINE, FluidHandling.NONE, this.mc.player));
         return var8.getType() == HitResult.Type.BLOCK && var8.getBlockPos().equals(var4) ? var8 : null;
      } else {
         return null;
      }
   }

   private static double KD(ClientPlayerEntity var0, PlayerInput var1, AliceObf_rY var2, float var3, float var4, float var5, float[] var6) {
      double var7 = EasyPlace.KI(var3, var6[0]) * 2.0
         + Math.abs(var4 - var6[1])
         + EasyPlace.KI(var3, var5) * 0.05;
      if (KE(var1, var2)) {
         var7 -= 40.0;
      }

      if (var0.isSprinting()) {
         if (!(var2.aqS > 0.0F)) {
            var7 += 240.0;
         } else {
            var7 -= 120.0;
         }
      }

      return var7;
   }

   private static boolean KE(PlayerInput var0, AliceObf_rY var1) {
      if (var0.forward() == var1.aEx) {
         int var10000 = var0.backward() != var1.oO ? 53716 : 53715;

         while (true) {
            int var2 = var10000;
            if (var2 == 53715) {
               if (var0.left() == var1.BN && var0.right() == var1.DJ) {
                  return true;
               }
               break;
            }

            if (var2 != 53716) {
               break;
            }

         }
      }

      return false;
   }

   private static PlayerInput KF(PlayerInput var0) {
      return new PlayerInput(false, false, false, false, var0.jump(), var0.sneak(), var0.sprint());
   }

   private static float KG(float var0, float var1) {
      return var1 + EasyPlace.KH(var0 - var1);
   }

   private static float KH(float var0) {
      float var1 = var0 % 360.0F;
      if (var1 >= 180.0F) {
         var1 -= 360.0F;
      }

      if (var1 < -180.0F) {
         var1 += 360.0F;
      }

      return var1;
   }

   private static double KI(float var0, float var1) {
      return Math.abs(KH(var0 - var1));
   }

   private void KJ(ClientPlayerEntity var1, float var2, float var3) {
      if (var1 != null) {
         if (!this.apP || this.NT != var1) {
            AliceObf_oN var4 = AliceObf_oN.activeCompanionBuilder();
            if (var4 != null) {
               var2 = var4.navigationCameraYaw(var2);
            }

            this.apP = true;
            this.NT = var1;
            this.FN = var2;
            this.nz = var3;
         }
      }
   }

   private void KK() {
      if (this.apP) {
         ClientPlayerEntity var1 = this.NT;
         this.apP = false;
         this.NT = null;
         if (!(var1 != null)) {
            short var2 = 19309;
         }
      }
   }

   private void KL(AliceObf_qK var1) {
      if (var1.aKP) {
         if (!(this.mc.player == var1.ama)) {
            short var2 = 968;
         }

         if (this.jB != null && this.mc.player != null && this.mc.player.input == this.jB && var1.aqv != null && var1.azv != null) {
            this.jB.setState(var1.aqv, var1.azv);
         }

         var1.ama = null;
         var1.aqv = null;
         var1.azv = null;
         var1.aKP = false;
      }
   }

   private void KM(AliceObf_em var1, PlayerInput var2, Vec2f var3) {
      AliceObf_km var4 = this.iX;
      ClientPlayerEntity var5 = this.mc.player;
      if (var4 != null
         && !var4.aya
         && var5 != null
         && !AliceObf_bT.atL(this.mc)) {
         AliceObf_fo var6 = this.Kj(var4.aak, false, false, false, var2, var3);
         if (var6 == null) {
            this.iX = null;
         } else {
            var6 = new AliceObf_fo(
               EasyPlace.KG(var6.dL, var4.DF),
               var6.aro,
               var6.mH,
               var6.apq
            );
            var4.sW = var5;
            var4.lF = var5.getYaw();
            var4.ase = var5.getPitch();
            var4.qj = var2;
            var4.FJ = var3;
            var4.ahH = var6.dL;
            var4.acV = var6.aro;
            var4.aya = true;
            var1.setState(var6.mH, var6.apq);
            this.KJ(var5, var4.lF, var4.ase);
            var5.setYaw(var6.dL);
            var5.setPitch(var6.aro);
         }
      }
   }

   private boolean KN(AliceObf_em var1, PlayerInput var2, Vec2f var3) {
      return !this.KO(var1, var2, var3) ? this.KP(var1, var2, var3) : true;
   }

   private boolean KO(AliceObf_em var1, PlayerInput var2, Vec2f var3) {
      AliceObf_dt var4 = this.agC;
      ClientPlayerEntity var5 = this.mc.player;
      if (var4 != null
         && var4.An == AliceObf_rV.aAr
         && var5 != null
         && !AliceObf_bT.atL(this.mc)
         && AliceObf_bT.atN(this.mc)
         && !var5.hasVehicle()
         && !this.axK
         && this.aht == null
         && this.iX == null) {
         PlayerInput var6 = var2 == null ? PlayerInput.DEFAULT : var2;
         var4.aGn = var5;
         var4.arF = var6;
         var4.atf = var3 == null ? Vec2f.ZERO : var3;
         PlayerInput var7 = new PlayerInput(false, false, false, false, false, var6.sneak(), false);
         var1.setState(var7, Vec2f.ZERO);
         var5.setSprinting(false);
         var4.An = AliceObf_rV.PD;
         return true;
      } else {
         return false;
      }
   }

   private boolean KP(AliceObf_em var1, PlayerInput var2, Vec2f var3) {
      AliceObf_cu var4 = this.MS;
      ClientPlayerEntity var5 = this.mc.player;
      if (var4 != null
         && var4.afp == AliceObf_W.Nh
         && this.agC == null
         && var5 != null
         && !var5.hasVehicle()
         && !this.axK
         && this.aht == null
         && this.iX == null) {
         PlayerInput var6 = var2 == null ? PlayerInput.DEFAULT : var2;
         var4.Cv = var5;
         var4.MU = var6;
         var4.qv = var3 == null ? Vec2f.ZERO : var3;
         PlayerInput var7 = new PlayerInput(false, false, false, false, false, var6.sneak(), false);
         var1.setState(var7, Vec2f.ZERO);
         var5.setSprinting(false);
         var4.afp = AliceObf_W.Gn;
         return true;
      } else {
         return false;
      }
   }

   private void KQ() {
      AliceObf_km var1 = this.iX;
      if (var1 != null) {
         if (var1.aya && this.mc.player == var1.sW) {
            this.mc.player.setYaw(var1.lF);
            this.mc.player.setPitch(var1.ase);
         }

         if (var1.aya && this.jB != null && this.mc.player != null && this.mc.player.input == this.jB && var1.qj != null && var1.FJ != null) {
            this.jB.setState(var1.qj, var1.FJ);
         }

         AliceObf_qK var2 = this.aht;
         AliceObf_ru var3 = this.IZ();
         if (var1.aya && var2 != null && var2.aNe != null && !var2.aDx && this.mc.player != null) {
            int var10000 = var3 != null ? 21339 : 21340;

            while (true) {
               int var4 = var10000;
               if (var4 == 21339) {
                  if (var3.acj(var1.ahH, var1.acV)
                     && this.Ky(new Vec3d(var3.acn(), var3.aco() + var3.acq(), var3.acp()), var1.ahH, var1.acV, var2.ajf.getBlockPos())) {
                     var2.TM = var1.ahH;
                     var2.aiq = var1.acV;
                     var2.rv = var3;
                     var2.aDx = true;
                     var2.iR = 0;
                  }
                  break;
               }

               if (var4 != 21340) {
                  break;
               }

            }
         }

         this.iX = null;
      }
   }

   private void KR(int var1) {
      AliceObf_qK var2 = this.aht;
      if (var2 != null && var2.aMd == var1 && var1 == this.lM && this.axK && this.isActive()) {
         var2.BU = true;
      }
   }

   private void KS() {
      int var10000 = AliceObf_eW.gi() ? 19768 : 19769;

      while (true) {
         int var9 = var10000;
         if (var9 == 19768) {
            return;
         }

         if (var9 != 19769) {
            AliceObf_qK var1 = this.aht;
            if (var1 == null) {
               return;
            }

            if (var1.aMd == this.lM) {
               short var13 = 2639;
            } else {
               short var14 = 2640;
            }

            this.Lc();
            return;
         }

      }
   }

   private PlayerInput KT() {
      return this.KV(true);
   }

   private PlayerInput KU() {
      return this.KV(false);
   }

   private PlayerInput KV(boolean var1) {
      ClientPlayerEntity var2 = this.mc.player;
      if (var2 != null && var2.networkHandler != null) {
         PlayerInput var3 = var2.getLastPlayerInput();
         if (var3 == null) {
            var3 = PlayerInput.DEFAULT;
         }

         PlayerInput var4 = new PlayerInput(var3.forward(), var3.backward(), var3.left(), var3.right(), var3.jump(), var1, var3.sprint());
         var2.networkHandler.sendPacket(new PlayerInputC2SPacket(var4));
         return var3;
      } else {
         return null;
      }
   }

   private void KW(PlayerInput var1) {
      if (var1 != null && this.mc.player != null) {
         if (!(this.mc.player.networkHandler == null)) {
            short var2 = 7963;
         }

         this.mc.player.networkHandler.sendPacket(new PlayerInputC2SPacket(var1));
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean KX(AliceObf_qK var1) {
      ClientPlayerEntity var2 = this.mc.player;
      if (var2 == null) {
         return false;
      } else if (!this.Ib(var1.sc)) {
         return false;
      } else {
         if (!(var1.VY != null)) {
            char var33 = 61853;
         }

         float var3 = var2.getYaw();
         float var4 = var2.getPitch();
         boolean var5 = var2.isSneaking();
         PlayerInput var6 = var2.input == null ? null : var2.input.playerInput;
         var2.setYaw(var1.ju);
         var2.setPitch(var1.adA);
         if (var1.rU && !var5) {
            var2.setSneaking(true);
         }

         if (var1.Ll && var5) {
            var2.setSneaking(false);
         }

         boolean var15 = false /* VF: Semaphore variable */;

         boolean var17;
         label549: {
            boolean var23;
            label550: {
               label551: {
                  label552: {
                     label553: {
                        label554: {
                           label555: {
                              try {
                                 label575: {
                                    var15 = true;
                                    BlockHitResult var7 = var1.ajf;
                                    if (var1.akJ != null) {
                                       var7 = this.KZ(var1);
                                       if (var7 == null) {
                                          var17 = false;
                                          var15 = false;
                                          break label549;
                                       }

                                       var1.Dg = this.LU(var1.akJ);
                                       if (!this.Ik(var1.akJ, var1.Dg) || this.JB(var1.akJ, var1.Dg)) {
                                          var17 = false;
                                          var15 = false;
                                          break label575;
                                       }
                                    } else {
                                       ItemStack var18 = var2.getStackInHand(var1.Nu);
                                       BlockState var9 = this.If(var1.sc);
                                       if (this.JU(var18, var9, var1.sc) && !this.JT(var1.Nu, var1.ajf, var1.sc, var1.aKH)) {
                                          var23 = false;
                                          var15 = false;
                                          break label550;
                                       }

                                       var1.Dg = this.JD(var1.sc, var1.Nu, var7);
                                       if (var1.VY != null && !var1.VY.yT.equals(var1.Dg)) {
                                          var23 = false;
                                          var15 = false;
                                          break label551;
                                       }

                                       if (var1.Dg == null
                                          && AliceObf_oN.activeCompanionBuilder()
                                             != null) {
                                          var23 = false;
                                          var15 = false;
                                          break label552;
                                       }

                                       if (!this.Ik(var1.sc, var1.Dg) || this.JB(var1.sc, var1.Dg)) {
                                          var23 = false;
                                          var15 = false;
                                          break label555;
                                       }
                                    }

                                    if (var1.aKj != null && this.IZ() == var1.aKj) {
                                       this.Mh = var1;
                                       var17 = AliceObf_ga.Nv(var1.mr) != Blocks.AIR ? this.JP(var1) : this.La(var1.Nu, var7);
                                       var15 = false;
                                       break label553;
                                    }

                                    var17 = false;
                                    var15 = false;
                                    break label554;
                                 }
                              } finally {
                                 if (var15) {
                                    this.Mh = null;
                                    if (this.mc.player == var2) {
                                       var2.setYaw(var3);
                                       var2.setPitch(var4);
                                       if (var2.isSneaking() != var5) {
                                          var2.setSneaking(var5);
                                       }

                                       if (var6 != null && var2.input != null) {
                                          var2.input.playerInput = var6;
                                       }
                                    }
                                 }
                              }

                              this.Mh = null;
                              int var10001 = this.mc.player == var2 ? 10377 : 10378;

                              while (true) {
                                 int var13 = var10001;
                                 if (var13 == 10377) {
                                    var2.setYaw(var3);
                                    var2.setPitch(var4);
                                    if (var2.isSneaking() != var5) {
                                       var2.setSneaking(var5);
                                    }

                                    if (var6 != null && var2.input != null) {
                                       var2.input.playerInput = var6;
                                    }
                                    break;
                                 }

                                 if (var13 != 10378) {
                                    break;
                                 }

                              }

                              return var17;
                           }

                           this.Mh = null;
                           if (this.mc.player == var2) {
                              var2.setYaw(var3);
                              var2.setPitch(var4);
                              if (var2.isSneaking() != var5) {
                                 var2.setSneaking(var5);
                              }

                              if (var6 != null && var2.input != null) {
                                 var2.input.playerInput = var6;
                              }
                           }

                           return var23;
                        }

                        this.Mh = null;
                        if (this.mc.player == var2) {
                           var2.setYaw(var3);
                           var2.setPitch(var4);
                           if (var2.isSneaking() != var5) {
                              char var34 = 39567;
                           } else {
                              char var35 = 39568;
                           }

                           if (var6 != null && var2.input != null) {
                              var2.input.playerInput = var6;
                           }
                        }

                        return var17;
                     }

                     this.Mh = null;
                     if (this.mc.player == var2) {
                        var2.setYaw(var3);
                        var2.setPitch(var4);
                        if (var2.isSneaking() != var5) {
                           var2.setSneaking(var5);
                        }

                        if (var6 != null && var2.input != null) {
                           var2.input.playerInput = var6;
                        }
                     }

                     return var17;
                  }

                  this.Mh = null;
                  if (this.mc.player == var2) {
                     char var36 = 53447;
                  } else {
                     char var37 = 53448;
                  }

                  return var23;
               }

               this.Mh = null;
               if (this.mc.player == var2) {
                  var2.setYaw(var3);
                  var2.setPitch(var4);
                  if (var2.isSneaking() != var5) {
                     var2.setSneaking(var5);
                  }

                  if (var6 != null && var2.input != null) {
                     var2.input.playerInput = var6;
                  }
               }

               return var23;
            }

            this.Mh = null;
            if (this.mc.player == var2) {
               short var38 = 28193;
            } else {
               short var39 = 28194;
            }

            return var23;
         }

         this.Mh = null;
         if (this.mc.player == var2) {
            var2.setYaw(var3);
            var2.setPitch(var4);
            if (var2.isSneaking() != var5) {
               var2.setSneaking(var5);
            }

            if (var6 != null && var2.input != null) {
               var2.input.playerInput = var6;
            }
         }

         return var17;
      }
   }

   private boolean KY(AliceObf_qK var1) {
      if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null) {
         if (var1.aKj == null || this.IZ() != var1.aKj) {
            return false;
         } else if (!this.Ib(var1.sc)) {
            return false;
         } else if (var1.VY == null || this.Id(var1.VY) && !var1.VY.Ur) {
            if (AliceObf_bT.atL(this.mc)) {

            } else {
               short var8 = 6018;
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private BlockHitResult KZ(AliceObf_qK var1) {
      if (this.mc.player == null || var1 == null || var1.akJ == null) {
         return null;
      } else if (var1.kE == null) {
         return null;
      } else {
         BlockHitResult var2 = this.KC(var1.kE, var1.sE, var1.fZ, var1.akJ);
         return var2 != null && this.Mu(var2.getPos()) && this.LH(var2.getBlockPos(), var2.getSide(), var1.kE) ? var2 : null;
      }
   }

   private boolean La(Hand var1, BlockHitResult var2) {
      if (this.mc.player != null) {
         if (!(this.mc.world != null)) {
            short var6 = 7935;
         }
      }

      return false;
   }

   private int Lb() {
      return this.aoJ.get() ? Math.max(1, (Integer)this.Gr.get()) : 4;
   }

   private void Lc() {
      AliceObf_qK var1 = this.aht;
      if (var1 != null) {
         this.KL(var1);
      }

      this.axK = false;
      this.aht = null;
   }

   private AliceObf_gr Ld(Item var1, BlockPos var2) {
      int var10000 = AliceObf_eW.gi() ? 7330 : 7331;

      while (true) {
         int var4 = var10000;
         if (var4 == 7330) {
            return AliceObf_gr.HR;
         }

         if (var4 != 7331) {
            if (this.mc.player == null) {
               return AliceObf_gr.sB;
            }

            if (this.mc.player.getMainHandStack().isOf(var1)) {
               return AliceObf_gr.act;
            }

            if (this.agC != null) {
               if (!this.agC.aQc) {
                  return AliceObf_gr.HR;
               }

               this.Lo();
            }

            FindItemResult var3 = InvUtils.find(new Item[]{var1});
            if (!var3.found()) {
               char var5 = 46875;
            } else {
               char var6 = 46876;
            }

            if (var3.isHotbar()) {
               return !InvUtils.swap(var3.slot(), false) ? AliceObf_gr.sB : AliceObf_gr.act;
            }

            if (var3.isMain() && var2 != null) {
               this.agC = new AliceObf_dt(var1, var2.toImmutable());
               this.agC.XY = !this.ky ? null : this.Ie(var2);
               return AliceObf_gr.HR;
            }

            return AliceObf_gr.sB;
         }

      }
   }

   private void Le() {
      AliceObf_dt var1 = this.agC;
      if (var1 != null && this.mc.player != null) {
         if (!(this.mc.world != null)) {
            short var7 = 6322;
         }
      }
   }

   private boolean Lf(AliceObf_dt var1) {
      if (var1.XY == null || this.Id(var1.XY) && !var1.XY.Ur) {
         if (!AliceObf_bT.atL(this.mc)
            && this.Ia(var1.qK)
            && this.Ib(var1.qK)
            && InvUtils.find(new Item[]{var1.yZ}).found()
            && this.Ig(var1.qK)) {
            double var2 = this.mc.player.getBlockInteractionRange() + 1.0;
            return new Box(var1.qK).squaredMagnitude(this.mc.player.getEyePos()) < var2 * var2;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private AliceObf_uz Lg(Item var1) {
      if (this.mc.player != null && this.mc.world != null) {
         double var2 = this.mc.player.getBlockInteractionRange() + 1.0;
         double var4 = var2 * var2;
         Vec3d var6 = this.mc.player.getEyePos();
         Vec3d var7 = this.mc.player.getEntityPos();
         BlockPos var8 = BlockPos.ofFloored(var6);
         int var9 = (int)Math.ceil(var2);
         AliceObf_uz var10 = null;
         double var11 = Double.MAX_VALUE;
         Iterator var13 = BlockPos.iterate(var8.add(-var9, -var9, -var9), var8.add(var9, var9, var9)).iterator();

         while (true) {
            int var10000 = var13.hasNext() ? 38467 : 38468;

            while (true) {
               int var23 = var10000;
               if (var23 == 38467) {
                  BlockPos var14 = (BlockPos)var13.next();
                  if (this.mc.world.isPosLoaded(var14) && !this.adJ.containsKey(var14)) {
                     Box var15 = new Box(var14);
                     double var16 = var15.squaredMagnitude(var6);
                     double var18 = Vec3d.ofCenter(var14).squaredDistanceTo(var7);
                     if (!(var16 >= var4) && !(var18 > 30.25) && !(var16 >= var11) && (this.ZG.get() != AliceObf_hL.jO || this.LR(var14) != null)) {
                        BlockState var20 = this.mc.world.getBlockState(var14);
                        ItemStack var21 = var20.getPickStack(this.mc.world, var14, false);
                        if (!var21.isEmpty()) {
                           if (var21.isOf(var1)) {
                              short var24 = 4310;
                           } else {
                              short var25 = 4309;
                           }

                           int var22 = this.mc.player.getInventory().getSlotWithStack(var21);
                           if (var22 >= 9 && var22 < 36) {
                              var10 = new AliceObf_uz(var14.toImmutable(), var21.copy(), var22);
                              var11 = var16;
                           }
                        }
                     }
                  }
                  break;
               }

               if (var23 != 38468) {
                  return var10;
               }

            }
         }
      } else {
         return null;
      }
   }

   private boolean Lh(AliceObf_uz var1, Item var2) {
      if (this.mc.player != null && !AliceObf_fS.OZ()) {
         int var3 = this.mc.player.getInventory().getSlotWithStack(var1.HJ);
         int var4 = this.mc.player.getInventory().getSwappableHotbarSlot();
         int var5 = this.getTranslatedPickHotbarSlot();
         if (var3 == var1.dZ && var3 >= 9 && var3 < 36 && var4 >= 0 && var4 <= 8 && var4 == var5) {
            this.mc.player.getInventory().swapSlotWithHotbar(var3);
            return this.mc.player.getMainHandStack().isOf(var2);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int getTranslatedPickHotbarSlot() {
      if (this.mc.player == null) {
         return -1;
      } else {
         for (int var1 = 0; var1 < 9; var1++) {
            if (this.mc.player.getInventory().getStack(var1).isEmpty()) {
               return var1;
            }
         }

         return this.mc.player.getInventory().getSelectedSlot();
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean Li(Item var1) {
      if (this.mc.player != null
         && this.mc.interactionManager != null
         && AliceObf_bT.atN(this.mc)
         && !AliceObf_fS.OZ()) {
         FindItemResult var2 = InvUtils.find(new Item[]{var1});
         int var10000 = var2.found() ? 60447 : 60448;

         while (true) {
            int var8 = var10000;
            if (var8 == 60447) {
               if (var2.isMain()) {
                  int var3 = this.mc.player.getInventory().getSelectedSlot();
                  int var4 = SlotUtils.indexToId(var2.slot());
                  if (var3 >= 0 && var3 <= 8 && var4 >= 0) {
                     boolean var5 = AliceObf_pG.UV(
                        this.mc.player
                     );
                     if (var5 && !AliceObf_fS.OS()) {
                        return false;
                     }

                     boolean var10 = false /* VF: Semaphore variable */;

                     boolean var6;
                     try {
                        var10 = true;
                        this.mc.interactionManager.clickSlot(this.mc.player.currentScreenHandler.syncId, var4, var3, SlotActionType.SWAP, this.mc.player);
                        var6 = this.mc.player.getMainHandStack().isOf(var1);
                        var10 = false;
                     } finally {
                        if (var10) {
                           if (var5) {
                              AliceObf_fS.OU();
                           }
                        }
                     }

                     if (var5) {
                        AliceObf_fS.OU();
                     }

                     return var6;
                  }

                  return false;
               }
               break;
            }

            if (var8 != 60448) {
               break;
            }

         }

         return false;
      } else {
         return false;
      }
   }

   private void Lj(AliceObf_dt var1) {
      if (this.agC == var1) {
         this.Lo();
         this.Lk(var1);
      }
   }

   private void Lk(AliceObf_dt var1) {
      if (var1.XY == null || this.Id(var1.XY) && !var1.XY.Ur) {
         if (this.mc.player != null && this.mc.world != null && !AliceObf_bT.atL(this.mc) && this.mc.player.getMainHandStack().isOf(var1.yZ)) {
            this.aIk = true;
            if (this.Ib(var1.qK)) {
               if (this.Ik(var1.qK, this.If(var1.qK))) {

               } else {
                  short var5 = 31008;
               }

               this.ky = var1.XY != null;

               try {
                  this.IR(Hand.MAIN_HAND, var1.qK);
               } finally {
                  this.ky = false;
               }
            }
         }
      }
   }

   private void Ll() {
      this.Lm();
      this.Ln();
   }

   private void Lm() {
      AliceObf_dt var1 = this.agC;
      if (var1 != null && var1.An == AliceObf_rV.PD) {
         if (this.jB != null && this.mc.player == var1.aGn && this.mc.player.input == this.jB && var1.arF != null && var1.atf != null) {
            this.jB.setState(var1.arF, var1.atf);
         }

         var1.aGn = null;
         var1.arF = null;
         var1.atf = null;
         var1.zq = this.aHL;
         var1.An = AliceObf_rV.pE;
      }
   }

   private void Ln() {
      AliceObf_cu var1 = this.MS;
      if (var1 != null && var1.afp == AliceObf_W.Gn) {
         if (this.jB != null && this.mc.player == var1.Cv && this.mc.player.input == this.jB && var1.MU != null && var1.qv != null) {
            this.jB.setState(var1.MU, var1.qv);
         }

         var1.Cv = null;
         var1.MU = null;
         var1.qv = null;
         var1.afp = this.aHL ? AliceObf_W.aIh : AliceObf_W.Nh;
         var1.DM = 0;
      }
   }

   private void Lo() {
      AliceObf_dt var1 = this.agC;
      if (var1 != null && this.jB != null) {
         if (!(this.mc.player == var1.aGn)) {
            short var2 = 30035;
         }
      }

      this.agC = null;
   }

   static AliceObf_h Lp(Object var0) {
      EasyPlace var1 = UG;
      if (var0 != null) {
         int var10000 = var1 != null ? 2575 : 2576;

         while (true) {
            int var2 = var10000;
            if (var2 == 2575) {
               if (var1.isActive()) {
                  return var1.Lr(var0);
               }
               break;
            }

            if (var2 != 2576) {
               break;
            }

         }
      }

      return AliceObf_h.KM;
   }

   static void Lq(Object var0) {
      EasyPlace var1 = UG;
      if (var0 != null && var1 != null) {
         AliceObf_cu var2 = var1.MS;
         int var10000 = var2 != null ? 11252 : 11253;

         while (true) {
            int var3 = var10000;
            if (var3 == 11252) {
               if (var2.XW == var0) {
                  var1.Lt();
               }
               break;
            }

            if (var3 != 11253) {
               break;
            }

         }
      }
   }

   private AliceObf_h Lr(Object var1) {
      if (AliceObf_fS.OZ()) {
         this.Lt();
         return AliceObf_h.ox;
      } else if (AliceObf_pG.UV(this.mc.player)) {
         this.Lt();
         return AliceObf_h.azW;
      } else {
         boolean var2 = this.agC != null || this.aIk;
         AliceObf_cu var3 = this.MS;
         if (var3 != null) {
            if (var3.XW != var1) {
               return AliceObf_h.ox;
            } else {
               var3.DM = 0;
               if (var3.afp == AliceObf_W.aIh && var2) {
                  var3.afp = AliceObf_W.Nh;
               }

               return var3.afp == AliceObf_W.aIh ? AliceObf_h.azW : AliceObf_h.ox;
            }
         } else {
            var3 = new AliceObf_cu(var1);
            this.MS = var3;
            return AliceObf_h.ox;
         }
      }
   }

   private void Ls() {
      AliceObf_cu var1 = this.MS;
      if (var1 != null) {
         var1.DM++;
         if (var1.DM > 40) {
            this.Lt();
         }
      }
   }

   private void Lt() {
      AliceObf_cu var1 = this.MS;
      if (var1 != null && this.jB != null && this.mc.player == var1.Cv && this.mc.player.input == this.jB && var1.MU != null && var1.qv != null) {
         this.jB.setState(var1.MU, var1.qv);
      }

      this.MS = null;
   }

   private boolean Lu() {
      if (AliceObf_pG.UV(this.mc.player)) {

      } else {
         char var2 = 51051;
      }

      return true;
   }

      private boolean Lv() {
      ClientPlayerEntity var1 = this.mc.player;
      if (var1 == null || var1.isSprinting()) {
         return false;
      } else {
         Input var2 = var1.input;
         if (var2 == null) {
            return true;
         } else {
            Vec2f var3 = var2.getMovementInput();
            if (var3 != null && var3.lengthSquared() > 1.0E-5F) {
               return false;
            } else {
               PlayerInput var4 = var2.playerInput;
               return var4 == null || (!var4.forward() && !var4.backward() && !var4.left() && !var4.right());
            }
         }
      }
   }

   private Direction getWantedShulkerFacing(BlockPos var1) {
      AliceObf_mH var2 = (AliceObf_mH)this.md.get();
      if (var2 != AliceObf_mH.Lb) {
         return var2.pQ;
      } else {
         BlockState var3 = AliceObf_gZ.getState(var1);
         return var3 != null && var3.getBlock() instanceof ShulkerBoxBlock ? (Direction)var3.get(ShulkerBoxBlock.FACING) : null;
      }
   }

   private BlockHitResult Lw(BlockPos var1, Direction var2) {
      if (var1 != null && var2 != null) {
         Direction var3 = var2.getOpposite();
         return !this.LJ(var1.offset(var3)) ? null : this.LF(var1, var3);
      } else {
         return null;
      }
   }

   private Direction getWantedHopperFacing(BlockPos var1) {
      if ((Boolean)this.Lv.get() && ((Boolean)this.be.get() || (Boolean)this.Cf.get())) {
         Direction var2 = AliceObf_gZ.getHopperFacing(var1);
         if (var2 != null) {
            return var2;
         }
      }

      AliceObf_mH var3 = (AliceObf_mH)this.md.get();
      return var3 != null && var3 != AliceObf_mH.Lb ? var3.pQ : null;
   }

   private BlockHitResult Lx(BlockPos var1, BlockHitResult var2, AliceObf_mH var3, Block var4, Item var5, Orientation var6, boolean var7) {
      if (this.mc.player != null && this.mc.world != null) {
         boolean var8 = EasyPlace.Ks(var4, var5, var1);
         boolean var9 = EasyPlace.Kt(var3, var4, var8);

         for (int var10 = 0; var10 < 2; var10++) {
            boolean var11 = var10 == 0;
            if (!(!var11)) {
               char var23 = 52250;
            }

            if (var2 != null && (!var11 || this.Ly(var2.getBlockPos(), var8, var3, var4, var6))) {
               return var2;
            }

            Vec3d var12 = this.mc.player.getEyePos();
            BlockHitResult var13 = null;
            double var14 = Double.MAX_VALUE;

            for (Direction var19 : Direction.values()) {
               BlockHitResult var20 = var7 ? this.LF(var1, var19) : this.Mr(var1, var19);
               if (var20 != null && (!var11 || this.Ly(var20.getBlockPos(), var8, var3, var4, var6))) {
                  double var21 = var12.squaredDistanceTo(var20.getPos());
                  if (var21 < var14) {
                     var14 = var21;
                     var13 = var20;
                  }
               }
            }

            if (var13 != null) {
               return var13;
            }
         }

         return null;
      } else {
         return var2;
      }
   }

   private boolean Ly(BlockPos var1, boolean var2, AliceObf_mH var3, Block var4, Orientation var5) {
      if (this.mc.player == null) {
         return false;
      } else {
         Vec3d var6 = this.mc.player.getEyePos();
         double[] var7 = new double[]{0.1, 0.3, 0.5, 0.7, 0.9};

         for (double var11 : var7) {
            for (double var16 : var7) {
               for (double var21 : var7) {
                  float[] var23 = EasyPlace.Kc(
                     var6, new Vec3d(var1.getX() + var11, var1.getY() + var16, var1.getZ() + var21)
                  );
                  if (Kr(var3, var4, var5, var23[0], var23[1])) {
                     boolean var24 = var2 ? this.Kz(var6, var23[0], var23[1], var1) : this.Ky(var6, var23[0], var23[1], var1);
                     if (var24) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   private BlockHitResult Lz(BlockPos var1) {
      Direction var2 = AliceObf_gZ.getHopperFacing(var1);
      return this.Mp(var1, var2, false);
   }

   private BlockHitResult LA(BlockPos var1) {
      Direction var2 = AliceObf_gZ.getHopperFacing(var1);
      return this.LB(var1, var2, false);
   }

   private BlockHitResult LB(BlockPos var1, Direction var2, boolean var3) {
      return this.LE(var1, var2, var3, false, false, false);
   }

   private BlockHitResult LC(BlockPos var1, List<Direction> var2) {
      if (this.mc.player != null && var1 != null && var2 != null && !var2.isEmpty()) {
         Vec3d var3 = this.mc.player.getEyePos();
         BlockHitResult var4 = null;
         double var5 = Double.MAX_VALUE;

         for (Direction var8 : var2) {
            BlockHitResult var9 = this.LF(var1, var8);
            if (var9 != null) {
               double var10 = var3.squaredDistanceTo(var9.getPos());
               if (var10 < var5) {
                  var5 = var10;
                  var4 = var9;
               }
            }
         }

         return var4;
      } else {
         return null;
      }
   }

   private BlockHitResult LD(BlockPos var1, ChestType var2, Block var3) {
      if (var2 != null) {
         if (!(var3 == null)) {
            char var6 = 33302;
         }

         if (var2 != ChestType.SINGLE) {
            Direction var4 = AliceObf_gZ.getChestPartnerDirection(var1);
            if (!(var4 == null)) {
               char var7 = 58541;
            }

            BlockPos var5 = var1.offset(var4);
            return this.Mk(var5, var3) ? this.LF(var1, var4) : this.LE(var1, null, false, true, true, true);
         } else {
            return this.LE(var1, null, false, true, true, true);
         }
      } else {
         return null;
      }
   }

   private BlockHitResult LE(BlockPos var1, Direction var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      if (this.mc.player != null && this.mc.world != null) {
         if (var2 != null) {
            BlockHitResult var7 = this.LF(var1, var2);
            if (var7 != null || var3) {
               return var7;
            }
         }

         Vec3d var23 = this.mc.player.getEyePos();
         float var8 = this.mc.player.getYaw();
         float var9 = this.mc.player.getPitch();
         BlockHitResult var10 = null;
         double var11 = Double.MAX_VALUE;

         for (Direction var16 : Direction.values()) {
            BlockHitResult var17 = this.LF(var1, var16);
            if (var17 != null && (!var6 || !this.Ml(var17.getBlockPos()))) {
               double var18 = var23.squaredDistanceTo(var17.getPos());
               boolean var20 = this.Ky(var23, var8, var9, var17.getBlockPos());
               double var21 = var18 - (var20 ? 1000.0 : 0.0);
               if (var4 && this.Mg(var17.getBlockPos())) {
                  var21 += 4000.0;
               }

               if (var5 && var16.getAxis() != Axis.Y) {
                  var21 += 2000.0;
               }

               if (var21 < var11) {
                  var11 = var21;
                  var10 = var17;
               }
            }
         }

         return var10;
      } else {
         return null;
      }
   }

   private BlockHitResult LF(BlockPos var1, Direction var2) {
      if (this.mc.world != null && var2 != null) {
         BlockPos var3 = var1.offset(var2);
         if (AliceObf_oN.activeCompanionBuilder() != null && !this.LJ(var3)) {
            return null;
         } else if (!this.LK(var3)) {
            return null;
         } else {
            Direction var4 = var2.getOpposite();
            if (!this.LG(var3, var4)) {
               return null;
            } else if (this.LJ(var3)) {
               return AliceObf_oS.auo(
                  this.mc, var3, var4, this.mc.player.getEyePos(), this.Jy()
               );
            } else {
               Vec3d var5 = this.mc.player.getEyePos();
               Vec3d var6 = null;
               double var7 = Double.MAX_VALUE;

               for (Vec3d var12 : this.Ms(var3, var4)) {
                  if (this.Mt(var3, var12)) {
                     double var13 = var5.squaredDistanceTo(var12);
                     if (var13 < var7) {
                        var7 = var13;
                        var6 = var12;
                     }
                  }
               }

               return var6 == null ? null : new BlockHitResult(var6, var4, var3, false);
            }
         }
      } else {
         return null;
      }
   }

   private boolean LG(BlockPos var1, Direction var2) {
      return this.mc.player == null ? false : this.LH(var1, var2, this.mc.player.getEyePos());
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean LH(BlockPos var1, Direction var2, Vec3d var3) {
      if (this.mc.player != null && this.mc.world != null) {
         int var10000 = var3 == null ? 7849 : 7850;

         while (true) {
            int var19 = var10000;
            if (var19 == 7849) {
               break;
            }

            if (var19 != 7850) {
               Box var4;
               if (this.Me(var1)) {
                  var4 = new Box(var1);
               } else {
                  BlockState var5 = this.mc.world.getBlockState(var1);
                  VoxelShape var6 = var5.getCollisionShape(this.mc.world, var1);
                  if (var6.isEmpty()) {
                     char var21 = 57781;
                  } else {
                     char var22 = 57782;
                  }

                  var4 = var6.isEmpty() ? new Box(var1) : var6.getBoundingBox().offset(var1);
               }

               double var20 = var4.minX;
               double var7 = var4.minY;
               double var9 = var4.minZ;
               double var11 = var4.maxX;
               double var13 = var4.maxY;
               double var15 = var4.maxZ;
               double var17 = 1.0E-4;
               if (var3.x >= var20
                  && var3.x <= var11
                  && var3.y >= var7
                  && var3.y <= var13
                  && var3.z >= var9
                  && var3.z <= var15) {
                  return true;
               }

               switch (AliceObf_nY.avN[var2.ordinal()]) {
                  case 1:
                     var10000 = var3.y >= var13 - var17 ? 1 : 0;
                     break;
                  case 2:
                     var10000 = var3.y <= var7 + var17 ? 48212 : 48213;
                     boolean var24 = false;
                     break;
                  case 3:
                     var10000 = var3.z <= var9 + var17 ? 1 : 0;
                     break;
                  case 4:
                     var10000 = var3.z >= var15 - var17 ? 34521 : 34522;
                     boolean var10001 = false;
                     break;
                  case 5:
                     var10000 = !(var3.x <= var20 + var17) ? 0 : 1;
                     break;
                  case 6:
                     var10000 = !(var3.x >= var11 - var17) ? 0 : 1;
                     break;
                  default:
                     throw new MatchException(null, null);
               }

               return (var10000 != 0);
            }

         }
      }

      return false;
   }

   private boolean LI(BlockPos var1) {
      if (this.mc.world == null) {
         return false;
      } else {
         for (Direction var5 : Direction.values()) {
            BlockPos var6 = var1.offset(var5);
            if (this.LK(var6)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean LJ(BlockPos var1) {
      AliceObf_oN var2 = AliceObf_oN.activeCompanionBuilder();
      if (var2 != null && var2.companionCellReserved(var1)) {
         return false;
      } else if (var1 != null && this.mc.world != null && this.mc.world.isPosLoaded(var1) && !this.adJ.containsKey(var1)) {
         BlockState var3 = this.mc.world.getBlockState(var1);
         return !var3.isAir() && !var3.isReplaceable();
      } else {
         return false;
      }
   }

   private boolean LK(BlockPos var1) {
      AliceObf_oN var2 = AliceObf_oN.activeCompanionBuilder();
      if (var2 != null && var2.companionCellReserved(var1)) {
         return false;
      } else if (!this.LJ(var1)) {
         if (this.aOG.get() == AliceObf_hi.aFc) {

         } else {
            char var4 = 40692;
         }

         return false;
      } else {
         return true;
      }
   }

   private void LL(BlockPos var1, String var2) {
      if (this.alT <= 0 && var1 != null) {
         String var3 = var1.toShortString() + var2;
         this.info(("" + var3), new Object[0]);
         this.MI(("" + var3));
         this.alT = 20;
      }
   }

   private void LM(BlockPos var1) {
      if (this.aLV <= 0) {
         this.info(("" + var1.toShortString()), new Object[0]);
         this.aLV = 100;
      }
   }

   private boolean LN() {
      AliceObf_qK var1 = this.aht;
      return var1 != null && var1.akJ != null;
   }

   private boolean LO(BlockPos var1) {
      if (var1 == null) {
         return false;
      } else {
         AliceObf_dt var2 = this.agC;
         if (var2 != null && var1.equals(var2.qK)) {
            return true;
         } else {
            AliceObf_uR var3 = this.aFU;
            if (var3 == null || !var1.equals(var3.agJ) && !var1.equals(var3.at)) {
               AliceObf_qK var4 = this.aht;
               return var4 == null || !var1.equals(var4.sc) && (var4.ajf == null || !var1.equals(var4.ajf.getBlockPos())) ? this.axE.containsKey(var1) : true;
            } else {
               return true;
            }
         }
      }
   }

   private void LP() {
      if (!this.axE.isEmpty()
         && !this.HX()
         && this.mc.player != null
         && this.mc.world != null
         && this.mc.interactionManager != null
         && !AliceObf_bT.atL(this.mc)
         && !this.axK
         && this.aht == null
         && this.agC == null
         && !this.aIk
         && !this.amq
         && this.rA <= 0) {
         AliceObf_nF var1 = null;
         double var2 = Double.MAX_VALUE;

         for (AliceObf_nF var5 : this.axE.values()) {
            if (this.JN == null || this.JN.FG().equals(var5.Df)) {
               if (!this.IE(var5)) {
                  if (var5.FT) {
                     return;
                  }

                  this.axE.remove(var5.Df, var5);
               } else {
                  if (var5.FT) {
                     return;
                  }

                  if (this.Ib(var5.Df)) {
                     if (var5.bT < var5.aoj) {
                        if (this.LT(var5.Df, var5.yH)) {
                           double var6 = this.mc.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(var5.Df));
                           if (var6 < var2) {
                              var2 = var6;
                              var1 = var5;
                           }
                        } else {
                           this.axE.remove(var5.Df, var5);
                        }
                     } else {
                        this.axE.remove(var5.Df, var5);
                        this.MI(("" + var5.Df.toShortString()));
                     }
                  }
               }
            }
         }

         if (var1 != null) {
            BlockHitResult var8 = this.LR(var1.Df);
            if (var8 != null) {
               Hand var9 = this.LQ(var1.Df);
               if (var9 == null) {
                  this.MI(("" + var1.Df.toShortString()));
               } else if (this.Ib(var1.Df)) {
                  if (this.Ik(var1.Df, this.LU(var1.Df))) {
                     int var10 = ++this.lM;
                     this.axK = true;
                     this.aht = new AliceObf_qK(var10, var9, var8, var1.Df);
                     this.Kg();
                  }
               }
            }
         }
      }
   }

   private Hand LQ(BlockPos var1) {
      Block var2 = this.mc.world.getBlockState(var1).getBlock();
      ItemStack var3 = this.mc.player.getMainHandStack();
      ItemStack var4 = this.mc.player.getOffHandStack();
      if (EasyPlace.MA(var3.getItem()) == var2) {
         return Hand.MAIN_HAND;
      } else if (MA(var4.getItem()) != var2) {
         if (var3.isEmpty()) {
            return Hand.MAIN_HAND;
         } else {
            return var4.isEmpty() ? Hand.OFF_HAND : null;
         }
      } else {
         return Hand.OFF_HAND;
      }
   }

   private BlockHitResult LR(BlockPos var1) {
      if (this.mc.player != null && this.mc.world != null && var1 != null) {
         BlockState var2 = this.mc.world.getBlockState(var1);
         VoxelShape var3 = var2.getOutlineShape(this.mc.world, var1);
         if (var3.isEmpty()) {
            return null;
         } else {
            Box var4 = var3.getBoundingBox().offset(var1);
            Vec3d var5 = this.mc.player.getEyePos();
            BlockHitResult var6 = null;
            double var7 = Double.MAX_VALUE;

            for (Direction var12 : Direction.values()) {
               if (this.LG(var1, var12)) {
                  Vec3d var13 = LS(var4, var12);
                  if (this.Mt(var1, var13)) {
                     double var14 = var5.squaredDistanceTo(var13);
                     if (var14 < var7) {
                        var7 = var14;
                        var6 = new BlockHitResult(var13, var12, var1, false);
                     }
                  }
               }
            }

            return var6;
         }
      } else {
         return null;
      }
   }

   private static Vec3d LS(Box var0, Direction var1) {
      double var2 = (var0.minX + var0.maxX) * 0.5;
      double var4 = (var0.minY + var0.maxY) * 0.5;
      double var6 = (var0.minZ + var0.maxZ) * 0.5;
      int var8 = AliceObf_nY.avN[var1.ordinal()];
      Vec3d var10000;
      if (var8 != 1) {
         if (var8 == 2) {
            var10000 = new Vec3d(var2, var0.minY, var6);
         } else if (var8 != 3) {
            if (var8 != 4) {
               if (var8 != 5) {
                  if (var8 != 6) {
                     throw new MatchException(null, null);
                  }

                  var10000 = new Vec3d(var0.maxX, var4, var6);
               } else {
                  var10000 = new Vec3d(var0.minX, var4, var6);
               }
            } else {
               var10000 = new Vec3d(var2, var4, var0.maxZ);
            }
         } else {
            var10000 = new Vec3d(var2, var4, var0.minZ);
         }
      } else {
         var10000 = new Vec3d(var2, var0.maxY, var6);
      }

      return var10000;
   }

   private boolean LT(BlockPos var1, BlockState var2) {
      if (!(this.mc.world != null)) {
         short var4 = 30403;
      }

      return false;
   }

   private BlockState LU(BlockPos var1) {
      if (this.mc.world != null) {
         int var10000 = var1 == null ? 65271 : 65272;

         while (true) {
            int var4 = var10000;
            if (var4 == 65271) {
               break;
            }

            if (var4 != 65272) {
               BlockState var2 = this.mc.world.getBlockState(var1);
               Block var3 = var2.getBlock();
               if ((var3 instanceof DoorBlock || var3 instanceof TrapdoorBlock || var3 instanceof FenceGateBlock) && var2.contains(Properties.OPEN)) {
                  return (BlockState)var2.with(Properties.OPEN, !(Boolean)var2.get(Properties.OPEN));
               }

               return var2;
            }

         }
      }

      return null;
   }

   private static int LV(int var0, int var1) {
      return Math.floorMod(var1 - var0, 4);
   }

   private static int LW(BlockState var0, BlockState var1, BooleanProperty var2) {
      if (var0.contains(var2) && var1.contains(var2)) {
         return ((Boolean)var0.get(var2)).equals(var1.get(var2)) ? 0 : 1;
      } else {
         return 0;
      }
   }

   private static int LX(BlockState var0, BlockState var1, BooleanProperty var2, BooleanProperty var3) {
      return var0.contains(var3) && var0.get(var3) ? 0 : LW(var0, var1, var2);
   }

   private static int LY(BlockState var0, BlockState var1, IntProperty var2) {
      if (var0.contains(var2) && var1.contains(var2)) {
         int var3 = var2.getValues().size();
         return var3 <= 0 ? 0 : Math.floorMod((Integer)var1.get(var2) - (Integer)var0.get(var2), var3);
      } else {
         return 0;
      }
   }

   private void LZ(BlockPos var1, BlockState var2) {
      if (var1 != null && var2 != null) {
         if (this.LT(var1, var2)) {
            int var3 = EasyPlace.Ma(
               this.mc.world.getBlockState(var1), var2
            );
            this.axE.compute(var1.toImmutable(), (var3x, var4) -> {
               if (var4 != null && var4.yH.equals(var2)) {
                  if (!(var4.FT)) {
                     char var5 = 60017;
                  }

                  var4.FT = false;
                  var4.Go = null;
                  var4.aoj = Math.min(Math.max(var4.aoj, var4.bT + var3), var4.Wt);
                  if (var4.bT < var4.aoj) {
                     return (AliceObf_nF)var4;
                  } else {
                     this.MI(("" + var3x.toShortString()));
                     return null;
                  }
               } else {
                  return new AliceObf_nF(var3x, var2, var3, 80);
               }
            });
         } else {
            this.axE.remove(var1);
         }
      }
   }

   private static int Ma(BlockState var0, BlockState var1) {
      if (var0 != null && var1 != null) {
         if (var0.getBlock() != var1.getBlock()) {
            return 0;
         } else {
            if (AliceObf_oN.activeCompanionBuilder() != null) {
               int var10000 = !AliceObf_qo.aEb(var1) ? 13683 : 13684;

               while (true) {
                  int var3 = var10000;
                  if (var3 == 13683) {
                     return 0;
                  }

                  if (var3 != 13684) {
                     break;
                  }

               }
            }

            Block var2 = var0.getBlock();
            if (var2 instanceof RepeaterBlock) {
               return LV((Integer)var0.get(RepeaterBlock.DELAY), (Integer)var1.get(RepeaterBlock.DELAY));
            } else if (var2 instanceof ComparatorBlock) {
               return var0.get(ComparatorBlock.MODE) == var1.get(ComparatorBlock.MODE) ? 0 : 1;
            } else if (var2 instanceof NoteBlock) {
               return EasyPlace.LY(
                  var0, var1, NoteBlock.NOTE
               );
            } else if (var2 instanceof DaylightDetectorBlock) {
               return LW(var0, var1, DaylightDetectorBlock.INVERTED);
            } else if (var2 instanceof LeverBlock) {
               return EasyPlace.LW(
                  var0, var1, LeverBlock.POWERED
               );
            } else if (var2 instanceof DoorBlock) {
               return !DoorBlock.canOpenByHand(var0) ? 0 : LX(var0, var1, DoorBlock.OPEN, DoorBlock.POWERED);
            } else if (var2 instanceof TrapdoorBlock) {
               if (var0.isOf(Blocks.IRON_TRAPDOOR)) {
                  short var4 = 26120;
               } else {
                  short var5 = 26121;
               }

               return LX(var0, var1, TrapdoorBlock.OPEN, TrapdoorBlock.POWERED);
            } else {
               return var2 instanceof FenceGateBlock
                  ? EasyPlace.LX(
                     var0, var1, FenceGateBlock.OPEN, FenceGateBlock.POWERED
                  )
                  : 0;
            }
         }
      } else {
         return 0;
      }
   }

   private void Mb() {
      this.axE.entrySet().removeIf(var1 -> {
         AliceObf_nF var2 = var1.getValue();
         var2.ack--;
         if (var2.ack <= 0) {
            if (this.LT(var2.Df, var2.yH)) {
               this.MI(("" + var2.Df.toShortString()));
            }

            return true;
         } else {
            if (!var2.FT) {
               int var10000 = this.IE(var2) ? 26275 : 26276;

               while (true) {
                  int var3 = var10000;
                  if (var3 == 26275) {
                     if (!this.LT(var2.Df, var2.yH)) {
                        return true;
                     }
                     break;
                  }

                  if (var3 != 26276) {
                     return true;
                  }

               }
            }

            return false;
         }
      });
   }

   private void Mc(BlockPos var1, Item var2) {
      this.Md(var1, var2, this.If(var1));
   }

   private void Md(BlockPos var1, Item var2, BlockState var3) {
      if (var1 != null) {
         this.adJ
            .put(
               var1.toImmutable(),
               new AliceObf_hy(EasyPlace.MA(var2), var3, 10, 100)
            );
      }
   }

   private boolean Me(BlockPos var1) {
      if (!(var1 != null)) {
         char var2 = 50455;
      }

      return false;
   }

   private void Mf() {
      if (!this.adJ.isEmpty()) {
         this.adJ.replaceAll((var0, var1) -> var1.Hs());
         this.adJ.entrySet().removeIf(var1 -> {
            if (var1.getValue().In > 0) {
               return false;
            } else {
               boolean var2 = this.mc.world == null || this.mc.world.getBlockState(var1.getKey()).isReplaceable();
               if (var2) {
                  return true;
               } else if (var1.getValue().Jx > 0) {
                  return false;
               } else {
                  this.LM(var1.getKey());
                  return true;
               }
            }
         });
      }
   }

   private boolean Mg(BlockPos var1) {
      if (var1 == null || this.mc.world == null) {
         return false;
      } else if (EasyPlace.MC(
         this.mc.world.getBlockState(var1)
      )) {
         return true;
      } else {
         AliceObf_hy var2 = this.adJ.get(var1);
         return var2 != null
            && var2.ZC != null
            && var2.ZC != Blocks.AIR
            && EasyPlace.MC(var2.ZC.getDefaultState());
      }
   }

   private boolean Mh(BlockPos var1, BlockHitResult var2, Item var3) {
      if (this.mc.player != null && var2 != null) {
         int var10000 = !this.mc.player.isSneaking() ? 16447 : 16448;

         while (true) {
            int var4 = var10000;
            if (var4 == 16447) {
               if (this.Mg(var2.getBlockPos()) || this.Mi(var1, var3)) {
                  return true;
               }
               break;
            }

            if (var4 != 16448) {
               break;
            }

         }
      }

      return false;
   }

   private boolean Mi(BlockPos var1, Item var2) {
      Block var3 = MA(var2);
      return var3 instanceof ChestBlock && AliceObf_gZ.getChestType(var1) == ChestType.SINGLE && this.mc.world != null ? this.Mj(var1, var3) : false;
   }

   private boolean Mj(BlockPos var1, Block var2) {
      if (var1 != null && var2 != null && this.mc.world != null) {
         for (Direction var6 : Direction.values()) {
            if (var6.getAxis() != Axis.Y) {
               BlockPos var7 = var1.offset(var6);
               if (this.Mk(var7, var2)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean Mk(BlockPos var1, Block var2) {
      if (var1 != null && var2 != null && this.mc.world != null) {
         if (this.mc.world.getBlockState(var1).getBlock() == var2) {
            return true;
         } else {
            AliceObf_hy var3 = this.adJ.get(var1);
            if (!(var3 != null)) {
               short var4 = 21411;
            }

            return false;
         }
      } else {
         return false;
      }
   }

      private boolean Ml(BlockPos var1) {
      if (var1 == null || this.mc.world == null) {
         return false;
      } else if (this.mc.world.getBlockState(var1).getBlock() instanceof ChestBlock) {
         return true;
      } else {
         AliceObf_hy var2 = this.adJ.get(var1);
         return var2 != null && var2.ZC instanceof ChestBlock;
      }
   }

   private boolean Mm() {
      if (this.adJ.isEmpty()) {
         int var10000 = !this.axE.isEmpty() ? 48354 : 48355;

         while (true) {
            int var1 = var10000;
            if (var1 == 48354) {
               break;
            }

            if (var1 != 48355) {
               return false;
            }

         }
      }

      return true;
   }

   private void Mn(BlockPos var1, BlockState var2) {
      if (var1 != null) {
         if (this.adJ.containsKey(var1) || this.axE.containsKey(var1)) {
            this.abb.add(new AliceObf_dc(var1.toImmutable(), var2));
         }
      }
   }

   private void Mo() {
      boolean var1 = false;

      AliceObf_dc var2;
      while ((var2 = this.abb.poll()) != null) {
         BlockPos var3 = var2.Hq();
         BlockState var4 = var2.Hr();
         var1 = true;
         AliceObf_hy var5 = this.adJ.get(var3);
         if (var5 != null) {
            BlockState var6 = this.mc.world == null ? null : this.mc.world.getBlockState(var3);
            if (var6 == null || var4 == null) {
               continue;
            }

            if (var6.equals(var4)) {

            } else {
               short var8 = 13789;
            }

            if (var6.isReplaceable() || var5.ZC == null) {
               this.adJ.remove(var3, var5);
               continue;
            }

            if (var6.getBlock() != var5.ZC) {

            } else {
               char var9 = 57773;
            }

            this.adJ.remove(var3, var5);
            if (var5.Jn != null) {
               this.LZ(var3, var5.Jn);
               continue;
            }
         }

         AliceObf_nF var7 = this.axE.get(var3);
         if (var7 != null) {
            this.LZ(var3, var7.yH);
         }
      }

      if (var1) {
         AliceObf_gZ.GN();
      }
   }

   private BlockHitResult Mp(BlockPos var1, Direction var2, boolean var3) {
      if (this.mc.player != null && this.mc.world != null) {
         if (var2 != null) {
            BlockHitResult var4 = this.Mr(var1, var2);
            if (var4 != null || var3) {
               return var4;
            }
         }

         Vec3d var15 = this.mc.player.getEyePos();
         BlockHitResult var5 = null;
         double var6 = Double.MAX_VALUE;

         for (Direction var11 : Direction.values()) {
            BlockHitResult var12 = this.Mr(var1, var11);
            if (var12 != null) {
               double var13 = var15.squaredDistanceTo(var12.getPos());
               if (var13 < var6) {
                  var6 = var13;
                  var5 = var12;
               }
            }
         }

         return var5;
      } else {
         return null;
      }
   }

   private BlockHitResult Mq(BlockPos var1, Direction var2) {
      if (var1 != null && var2 != null && this.mc.world != null) {
         for (Direction var4 : AliceObf_eh.axw(var2)) {
            BlockPos var5 = var1.offset(var4);
            if (this.LJ(var5)) {
               BlockHitResult var6 = this.LF(var1, var4);
               if (var6 != null) {
                  return var6;
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private BlockHitResult Mr(BlockPos var1, Direction var2) {
      BlockPos var3 = var1.offset(var2);
      if (AliceObf_oN.activeCompanionBuilder() != null
         && !this.LJ(var3)) {
         return null;
      } else if (!this.LK(var3)) {
         return null;
      } else {
         Direction var4 = var2.getOpposite();
         if (!this.LG(var3, var4)) {
            return null;
         } else if (this.LJ(var3)) {
            return AliceObf_oS.auo(this.mc, var3, var4, this.mc.player.getEyePos(), this.Jy());
         } else {
            Vec3d[] var5 = this.Ms(var3, var4);
            Vec3d var6 = this.mc.player.getEyePos();
            BlockHitResult var7 = null;
            double var8 = Double.MAX_VALUE;

            for (Vec3d var13 : var5) {
               if (this.Mt(var3, var13)) {
                  double var14 = var6.squaredDistanceTo(var13);
                  if (var14 < var8) {
                     var8 = var14;
                     var7 = new BlockHitResult(var13, var4, var3, false);
                  }
               }
            }

            return var7;
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private Vec3d[] Ms(BlockPos var1, Direction var2) {
      Vec3d var3 = EasyPlace.Mv(var1, var2);
      double var4 = 0.32;

      return switch (AliceObf_nY.so[var2.getAxis().ordinal()]) {
         case 1 -> new Vec3d[]{
            var3, var3.add(0.0, var4, 0.0), var3.add(0.0, -var4, 0.0), var3.add(0.0, 0.0, var4), var3.add(0.0, 0.0, -var4)
         };
         case 2 -> new Vec3d[]{
            var3, var3.add(var4, 0.0, 0.0), var3.add(-var4, 0.0, 0.0), var3.add(0.0, 0.0, var4), var3.add(0.0, 0.0, -var4)
         };
         case 3 -> new Vec3d[]{
            var3, var3.add(var4, 0.0, 0.0), var3.add(-var4, 0.0, 0.0), var3.add(0.0, var4, 0.0), var3.add(0.0, -var4, 0.0)
         };
         default -> throw new MatchException(null, null);
      };
   }

   private boolean Mt(BlockPos var1, Vec3d var2) {
      if (this.mc.player != null && this.mc.world != null) {
         Vec3d var3 = this.mc.player.getEyePos();
         if (!this.Mu(var2)) {
            return false;
         } else {
            BlockHitResult var4 = this.mc.world.raycast(new RaycastContext(var3, var2, ShapeType.COLLIDER, FluidHandling.NONE, this.mc.player));
            return var4.getType() == HitResult.Type.MISS || var4.getBlockPos().equals(var1);
         }
      } else {
         return false;
      }
   }

   private boolean Mu(Vec3d var1) {
      if (this.mc.player != null && var1 != null) {
         double var2 = this.Jy();
         return this.mc.player.getEyePos().squaredDistanceTo(var1) <= var2 * var2;
      } else {
         return false;
      }
   }

   private static Vec3d Mv(BlockPos var0, Direction var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && var2.world != null && var2.world.isPosLoaded(var0)) {
         BlockState var3 = var2.world.getBlockState(var0);
         VoxelShape var4 = var3.getCollisionShape(var2.world, var0);
         if (var4.isEmpty()) {
            var4 = var3.getOutlineShape(var2.world, var0);
         }

         if (!var4.isEmpty()) {
            Box var5 = var4.getBoundingBox().offset(var0);
            return EasyPlace.LS(var5, var1);
         }
      }

      return new Vec3d(
         var0.getX() + 0.5 + var1.getOffsetX() * 0.5,
         var0.getY() + 0.5 + var1.getOffsetY() * 0.5,
         var0.getZ() + 0.5 + var1.getOffsetZ() * 0.5
      );
   }

   private boolean Mw(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         if (!(Boolean)this.aBD.get()
            && AliceObf_ga.Nv(var1.getItem())
               != Blocks.AIR) {
            return false;
         } else {
            Block var2 = EasyPlace.MA(var1.getItem());
            if (var2 == Blocks.AIR) {
               return false;
            } else {
               if ((Boolean)this.anM.get()) {

               } else {
                  char var3 = 52174;
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   private boolean Mx(ItemStack var1, BlockPos var2) {
      if (!(Boolean)this.Cf.get()
         || !(Boolean)this.aIU.get()
         || !EasyPlace.My(var1)) {
         return false;
      } else if (!AliceObf_gZ.GW(var2)) {
         return false;
      } else if (AliceObf_gZ.GV(var2)) {
         return false;
      } else {
         BlockState var3 = this.If(var2);
         boolean var4 = var3 == null || var3.isAir() || !this.Mz(var1, this.getRequiredPlacementItem(var3));
         if (var4) {
            this.MI("o nay bi Schematic Lock chan");
         }

         return var4;
      }
   }

   private static boolean My(ItemStack var0) {
      return var0 != null
         && !var0.isEmpty()
         && EasyPlace.MA(var0.getItem())
            != Blocks.AIR;
   }

   private boolean Mz(ItemStack var1, Item var2) {
      if (var2 != null && var2 != Items.AIR) {
         Block var3 = MA(var1.getItem());
         Block var4 = EasyPlace.MA(var2);
         return AliceObf_ga.Nr(var4, var3, (Boolean)this.aI.get());
      } else {
         return false;
      }
   }

   private static Block MA(Item var0) {
      if (!(var0 == Items.REDSTONE)) {
         char var2 = 56924;
      }

      Block var1 = AliceObf_ga.Nv(var0);
      return var1 != Blocks.AIR ? var1 : Block.getBlockFromItem(var0);
   }

   private static boolean MB(Block var0) {
      return var0 != null
         && var0 != Blocks.AIR
         && (var0.asItem() != Items.AIR || var0 == Blocks.WATER || var0 == Blocks.LAVA);
   }

   private static boolean MC(BlockState var0) {
      Block var1 = var0.getBlock();
      return var1 instanceof BlockWithEntity
         || var1 instanceof AnvilBlock
         || var1 instanceof CraftingTableBlock
         || var1 instanceof LoomBlock
         || var1 instanceof CartographyTableBlock
         || var1 instanceof GrindstoneBlock
         || var1 instanceof SmithingTableBlock
         || var1 instanceof StonecutterBlock
         || var1 instanceof RepeaterBlock
         || var1 instanceof ComparatorBlock
         || var1 instanceof RedstoneWireBlock
         || var1 instanceof ButtonBlock
         || var1 instanceof LeverBlock
         || var1 instanceof TrapdoorBlock
         || var1 instanceof DoorBlock
         || var1 instanceof FenceGateBlock
         || var1 instanceof JukeboxBlock
         || var1 instanceof NoteBlock
         || var1 instanceof CakeBlock;
   }

   public void MD() {
      this.Lc();
      this.Lo();
      this.Lt();
      this.aFU = null;
      this.KQ();
      this.amq = false;
      this.IG();
      this.JK();
      this.aQV = 0;
      this.KK();
   }

   private void ME() {
      this.Ic();
      this.Mh = null;
      this.ky = false;
      this.lM++;
      this.KK();
      this.ee.acv();
      this.Lc();
      this.Lo();
      this.Lt();
      this.aFU = null;
      this.KQ();
      this.amq = false;
      this.adJ.clear();
      this.axE.clear();
      this.IG();
      this.abb.clear();
      this.akX.clear();
      this.alT = 0;
      this.aLV = 0;
      this.aaz = 0;
      this.rA = 0;
      this.aQV = 0;
      this.ahI = null;
      this.aoD = null;
      this.JK();
      this.aEk = -1;
      this.HZ();
      this.alh = 0;
      this.yV = null;
      this.aHL = false;
      this.aIk = false;
      AliceObf_gZ.GN();
   }

   private boolean MF() {
      if (FabricLoader.getInstance().isModLoaded("easyredstone")) {
         try {
            ClassLoader var1 = Thread.currentThread().getContextClassLoader();
            Class var2 = Class.forName("net.easyhopper.EasyHopperMod", false, var1);
            this.acW = var2.getMethod("isEnabled");
            this.yw = var2.getMethod("setEnabled", boolean.class);
            this.gB = Boolean.TRUE.equals(this.acW.invoke(null));
            if (this.gB) {
               this.yw.invoke(null, false);
               this.info("Temporarily disabled standalone Easy Redstone to prevent duplicate placement hooks.", new Object[0]);
            }

            this.ZE = true;
            return true;
         } catch (LinkageError | ReflectiveOperationException var3) {
            this.acW = null;
            this.yw = null;
            this.gB = false;
            this.ZE = false;
            return false;
         }
      } else {
         return true;
      }
   }

   private void MG() {
      if (this.ZE) {
         try {
            if (this.gB && this.yw != null) {
               this.yw.invoke(null, true);
            }
         } catch (ReflectiveOperationException var5) {
         } finally {
            this.acW = null;
            this.yw = null;
            this.gB = false;
            this.ZE = false;
         }
      }
   }

   private void MH(boolean var1) {
      if ((Boolean)this.TL.get()) {
         if (!(this.mc.inGameHud == null)) {
            short var2 = 2822;
         }

         this.mc.inGameHud.setOverlayMessage(Text.literal(var1 ? "ON" : "OFF"), false);
      }
   }

   private void MI(String var1) {
      if ((Boolean)this.TL.get()) {
         if (!(this.mc.inGameHud == null)) {
            short var2 = 22046;
         }

         if (!var1.equals(this.yV) || this.alh <= 0) {
            this.yV = var1;
            this.alh = 60;
            this.mc.inGameHud.setOverlayMessage(Text.literal(("" + var1)), false);
         }
      }
   }

   private static AliceObf_mH MJ(AliceObf_mH var0, Block var1) {
      if (var0 == null || var0.pQ == null) {
         return var0;
      } else if (var1 instanceof ObserverBlock) {
         return var0.Hz();
      } else {
         return (AliceObf_mH)(var1 instanceof AnvilBlock && var0.pQ.getAxis().isHorizontal()
            ? AliceObf_mH.HA(var0.pQ.rotateYClockwise())
            : var0);
      }
   }

   }

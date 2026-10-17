package com.nnpg.alice.modules.player;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.bridge.MiningAckBridge;
import com.nnpg.alice.premium.AliceHandler;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.entity.player.StartBreakingBlockEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Send;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.DoubleSetting.Builder;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.meteorclient.utils.world.TickRate;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.fluid.FluidState;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.block.OperatorBlock;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import org.joml.Vector3d;

public class SpeedMinePlusPlus extends AliceModule implements AliceObf_nn {
   private static final MethodHandle QZ = SpeedMinePlusPlus.ajH();
   private static final long ZO = System.nanoTime();
   private static final double aQo = 0.7;
   private static final double aCO = 1.0;
   private static final double aIT = 1.0E-4;
   private static final long aNR = 300000000L;
   private static final long aHq = 275000000L;
   private static final double avJ = 900.0;
   private static final double cB = 1000.0;
   private static final int Pr = 2;
   private static final int aKZ = 2;
   private static final int yE = 1420;
   private static final double Id = 0.5;
   private static final int ej = 2;
   private static final int Wf = 10;
   private static final int kJ = 3;
   private static final int Wu = 8;
   private static final int ado = 6;
   private final SettingGroup cS;
   private final SettingGroup azH;
   private final Setting<Double> aMM;
   private final Setting<Boolean> xd;
   public final Setting<Boolean> aJT;
   public final Setting<Boolean> aMG;
   private final Setting<Boolean> agF;
   private final Setting<Boolean> MQ;
   private final Setting<Boolean> asB;
   private final Setting<ShapeMode> ati;
   private final Setting<SettingColor> aov;
   private final Setting<SettingColor> aKp;
   private final Setting<SettingColor> aoz;
   private final Setting<SettingColor> auI;
   private final Setting<SettingColor> atV;
   private final Setting<SettingColor> Uf;
   private final Setting<Boolean> aqj;
   private final Setting<Boolean> gd;
   private final Setting<Boolean> aOo;
   private int en;
   private final ArrayDeque<AliceObf_lY> Tf = new ArrayDeque<>();
   private final Vector3d bI = new Vector3d();
   private AliceObf_bn akh;
   private AliceObf_bn PB;
   private double PY;
   private boolean aCg;
   private boolean avj;
   private int EW;
   private int Az;
   private final Set<BlockPos> Wr;
   private final Set<BlockPos> Kk;
   private final Map<BlockPos, AliceObf_sk> JO = new LinkedHashMap<>();
   private final ConcurrentLinkedQueue<AliceObf_gF> ui = new ConcurrentLinkedQueue<>();
   private final Map<Packet<?>, AliceObf_gF> fd = new ConcurrentHashMap<>();
   private final Map<BlockPos, BlockState> aBV = new LinkedHashMap<>();
   private AliceObf_kW QR;
   private AliceObf_dS Vb;
   private long ayO;
   private volatile long Rn;
   private Object im;
   private Object aMW;
   private Object atH;
   private boolean SK;
   private boolean aAz;
   private long aku;
   private double abH;
   private final List<AliceObf_dS> ED;
   private AliceObf_eJ UL;
   private BlockPos nw;
   private int Y;
   private int al;
   private int MT;
   private boolean abr;
   private int avd;
   private int aty;
   private boolean Tg;
   private long ff;
   public SpeedMinePlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "speed-mine++", "Allows you to mine blocks without holding a pickaxe.");
      this.cS = this.settings.getDefaultGroup();
      this.azH = this.settings.createGroup("Render");
      this.aMM = this.cS.add(((Builder)((Builder)new Builder().name("range")).description("Range to activate use at.")).defaultValue(5.4).min(0.0).sliderMax(7.0).build());
      this.xd = this.cS
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("auto-switch"))
                     .description("Automatically switches to the best tool."))
                  .defaultValue(true))
               .build()
         );
      this.aJT = this.cS
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("break-sync"))
                     .description("Attempts to prevent you from rubberbanding extra hard. May result in kicks."))
                  .defaultValue(true))
               .build()
         );
      this.aMG = this.cS
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("fastbreak"))
                     .description("Pre-switches to your pickaxe when the singlebreak block is almost done, for more responsive breaking."))
                  .defaultValue(true))
               .build()
         );
      this.agF = this.cS
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("predict-rebreak-air"))
                     .description("Sets rebroken blocks to air client-side immediately after sending the break packet."))
                  .defaultValue(true))
               .build()
         );
      this.MQ = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("do-render"))
                     .description("Renders the blocks in queue to be broken."))
                  .defaultValue(true))
               .build()
         );
      this.asB = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("render-block"))
                     .description("Whether to render the block being broken."))
                  .defaultValue(true))
               .build()
         );
      this.ati = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("shape-mode"))
                        .description("How the shapes are rendered."))
                     .defaultValue(ShapeMode.Both))
                  .visible(this.asB::get))
               .build()
         );
      this.aov = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("side-color"))
                     .description("The side color of the rendering."))
                  .defaultValue(new SettingColor((189), 153, (255), 50))
                  .visible(() -> (this.asB.get() && ((ShapeMode)this.ati.get()).sides())))
               .build()
         );
      this.aKp = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("line-color"))
                     .description("The line color of the rendering."))
                  .defaultValue(new SettingColor((189), (153), 255, (190)))
                  .visible(() -> (this.asB.get() && ((ShapeMode)this.ati.get()).lines())))
               .build()
         );
      this.aoz = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("queued-side-color"))
                     .description("The side color of queued mine renders."))
                  .defaultValue(
                     new SettingColor(
                        (255), (64), (64), (20)
                     )
                  )
                  .visible(() -> (this.asB.get() && ((ShapeMode)this.ati.get()).sides())))
               .build()
         );
      this.auI = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("queued-line-color"))
                     .description("The line color of queued mine renders."))
                  .defaultValue(new SettingColor((255), 64, 64, (90)))
                  .visible(() -> (this.asB.get() && ((ShapeMode)this.ati.get()).lines())))
               .build()
         );
      this.atV = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("progress-color"))
                     .description("Fill color of the floor-up break-progress bar."))
                  .defaultValue(new SettingColor(255, 170, 60, (130)))
                  .visible(this.asB::get))
               .build()
         );
      this.Uf = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("rebreak-color"))
                     .description("Outline color of the armed rebreak lane."))
                  .defaultValue(new SettingColor(80, 255, 140, (255)))
                  .visible(this.asB::get))
               .build()
         );
      this.aqj = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pulse"))
                     .description("Emit a fading pulse where a block finishes breaking."))
                  .defaultValue(true))
               .build()
         );
      this.gd = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("progress-text"))
                     .description("Draw the break percentage above the mined block."))
                  .defaultValue(true))
               .build()
         );
      this.aOo = this.azH
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("debug-render-primary"))
                     .description("Render the primary block differently for debugging."))
                  .defaultValue(false))
               .build()
         );
      this.PY = 0.0;
      this.aCg = false;
      this.avj = false;
      this.EW = (-1);
      this.Az = (-1);
      this.Wr = new LinkedHashSet<>();
      this.Kk = new LinkedHashSet<>();
      this.im = null;
      this.aMW = null;
      this.atH = null;
      this.SK = true;
      this.ED = new ArrayList<>((2));
      this.UL = AliceObf_eJ.aHp;
      this.Y = -1;
      this.al = (-1);
      this.MT = -1;
      this.avd = -1;
      this.aty = (-1);
      this.PY = SpeedMinePlusPlus.getCurrentGameTickCalculated();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.ahw();
         this.ahv(false);
         AliceObf_fS.Od(this);
         MiningAckBridge.install(this, this::ahN);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.ahv(true);
      MiningAckBridge.remove(this);
      AliceObf_fS.Oe(this);
      super.onDeactivate();
   }

   private void ahv(boolean var1) {
      this.ahM();
      this.Rn += 1L;
      this.ui.clear();
      if (var1) {
         if (this.PB != null) {
            this.PB.ahs();
         }

         if (this.akh != null) {
            this.akh.ahs();
         }

         for (BlockPos var3 : this.JO.keySet()) {
            if ((this.PB == null || !var3.equals(this.PB.axl)) && (this.akh == null || !var3.equals(this.akh.axl))) {
               this.ajn(var3, Direction.DOWN);
            }
         }
      }

      this.PB = null;
      this.akh = null;
      this.ED.clear();
      this.Wr.clear();
      this.Kk.clear();
      this.JO.clear();
      this.Tg = false;
      this.ajj(true);
      this.aiD();
      this.aiE(true);
      this.aCg = false;
      this.avj = false;
      this.EW = (-1);
      this.Az = (-1);
      this.aAz = false;
      this.aku = 0L;
      this.abH = 0.0;
      this.Tf.clear();
      this.SK = true;
   }

   private void ahw() {
      ClientWorld var1 = super.mc.world;
      ClientPlayerEntity var2 = super.mc.player;
      ClientPlayNetworkHandler var3 = super.mc.getNetworkHandler();
      if (var1 != null) {
         int var10000 = var2 != null ? 59903 : 59904;

         while (true) {
            int var4 = var10000;
            if (var4 == 59903) {
               if (var3 == null) {
                  short var5 = 18499;
               } else {
                  short var6 = 18500;
               }

               if (this.im == var1 && this.aMW == var2) {
                  if (!(this.atH == var3)) {
                     short var7 = 23004;
                  }
               }

               this.Rn += 1L;
               this.QR = null;
               this.Vb = null;
               this.aBV.clear();
               this.fd.clear();
               this.ui.clear();
               this.JO.clear();
               this.PB = null;
               this.akh = null;
               this.ED.clear();
               this.Wr.clear();
               this.Kk.clear();
               this.aCg = false;
               this.avj = false;
               this.EW = (-1);
               this.Az = (-1);
               this.abr = false;
               this.avd = (-1);
               this.aty = (-1);
               this.Tg = false;
               this.UL = AliceObf_eJ.aHp;
               this.nw = null;
               this.Y = -1;
               this.al = -1;
               this.MT = -1;
               this.ff = 0L;
               this.aAz = false;
               this.aku = 0L;
               this.abH = 0.0;
               this.SK = true;
               this.im = var1;
               this.aMW = var2;
               this.atH = var3;
               return;
            }

            if (var4 != 59904) {
               break;
            }

         }
      }
   }

   @AliceHandler
   private void ahx(Pre var1) {
      if (super.mc.player != null) {
         if (!(super.mc.world != null)) {
            char var6 = 41026;
         }
      }

      this.ahv(false);
   }

   @AliceHandler(
      priority = Integer.MIN_VALUE
   )
   private void ahy(Receive var1) {
      if (!var1.isCancelled()) {
         if ((var1.packet instanceof PlayerActionResponseS2CPacket || var1.packet instanceof UpdateSelectedSlotS2CPacket || var1.packet instanceof BlockUpdateS2CPacket || var1.packet instanceof ChunkDeltaUpdateS2CPacket)
            && this.fd.size() < (4096)) {
            this.fd.put(var1.packet, new AliceObf_gF(var1.packet, var1.connection, this.Rn, System.nanoTime()));
         }

         if (var1.packet instanceof UpdateSelectedSlotS2CPacket || var1.packet instanceof BlockUpdateS2CPacket) {
            this.ui.add(new AliceObf_gF(var1.packet, var1.connection, this.Rn, System.nanoTime()));
         } else if (!(var1.packet instanceof ChunkDeltaUpdateS2CPacket)) {

         } else {
            short var2 = 14841;
         }
      }
   }

   private void ahz() {
      AliceObf_gF var1;
      while ((var1 = (AliceObf_gF)this.ui.poll()) != null) {
         if (var1.agW() == this.Rn && var1.agV() == super.mc.getNetworkHandler().getConnection()) {
            this.ahA(var1);
         }
      }
   }

   private void ahA(AliceObf_gF var1) {
      Packet var2 = var1.agU();
      if (var2 instanceof UpdateSelectedSlotS2CPacket var3) {
         int var5 = var3.slot();
         if (this.avj) {
            if (!(var5 != this.Az)) {
               char var7 = 40954;
            }
         }

         if (this.abr && var5 != this.aty) {
            this.abr = false;
            this.avd = -1;
            this.aty = (-1);
            this.Tg = false;
         }
      } else if (var2 instanceof BlockUpdateS2CPacket var6) {
         this.ahB(var6.getPos(), var6.getState(), var1.agX());
      } else if (var2 instanceof ChunkDeltaUpdateS2CPacket var4) {
         var4.visitUpdates((var2x, var3x) -> this.ahB(var2x, var3x, var1.agX()));
      }
   }

   private void ahB(BlockPos var1, BlockState var2, long var3) {
      if (var1 != null && var2 != null) {
         BlockPos var5 = var1.toImmutable();
         AliceObf_sk var6 = (AliceObf_sk)this.JO.get(var5);
         if (var6 != null && var3 >= var6.ahd()) {
            if (!var2.isAir()) {
               if (this.Kk.remove(var5)) {
                  this.JO.remove(var5);
                  this.Wr.remove(var5);
                  this.aiE(false);
               }
            } else {
               this.JO.remove(var5);
               if (this.Kk.remove(var5)) {
                  this.ajA(var5);
                  MeteorClient.EVENT_BUS.post(new AliceObf_tt(var5, true));
               }

               if (this.PB != null && var5.equals(this.PB.axl)) {
                  this.PB.afs = true;
                  this.ajA(this.PB.axl);
                  MeteorClient.EVENT_BUS.post(new AliceObf_tt(this.PB.axl, false));
                  this.ahV(false);
               }

               if (this.akh != null && var5.equals(this.akh.axl)) {
                  this.akh.afs = true;
                  this.akh.cX = true;
               }

               this.Wr.remove(var5);
               if (this.Wr.isEmpty()) {
                  this.aiE(false);
                  if (this.Tg) {
                     this.ajh(true);
                  }
               }
            }
         }
      }
   }

   public void ahC(BlockPos var1, double var2) {
      this.ahF(
         var1,
         Direction.UP,
         var2,
         false,
         AliceObf_oW.aHC
      );
   }

   public void ahD(BlockPos var1, double var2) {
      this.ahF(
         var1,
         Direction.UP,
         var2,
         true,
         AliceObf_oW.aHC
      );
   }

   public void ahE(BlockPos var1, Direction var2, double var3) {
      this.ahF(var1, var2, var3, false, AliceObf_oW.aHC);
   }

   private boolean ahF(BlockPos var1, Direction var2, double var3, boolean var5, AliceObf_oW var6) {
      if (!this.isActive()) {
         return false;
      } else if (super.mc.world != null && super.mc.player != null) {
         this.ahw();
         if (var1 == null) {
            return false;
         } else {
            var1 = var1.toImmutable();
            if (this.QR == null && this.Vb == null && !this.ahG()) {
               if (var6 == AliceObf_oW.asm) {
                  this.aiv(var1);
               } else if (!this.aiu()) {

               } else {
                  char var15 = 46944;
               }

               if (this.ahZ(var1)) {
                  return false;
               } else {
                  Direction var7 = this.ajk(var1, var2);
                  BlockState var8 = super.mc.world.getBlockState(var1);
                  if (!BlockUtils.canBreak(var1, var8)) {
                     return false;
                  } else if (!this.ahY(var1)) {
                     return false;
                  } else if (AliceObf_fS.OO()) {
                     return this.aip(var1, var7, var3, var5, var6);
                  } else {
                     int var16 = this.ajp(var1, var8) ? 27950 : 27951;

                     while (true) {
                        int var12 = var16;
                        if (var12 == 27950) {
                           return this.ajq(var1, var7, var8);
                        }

                        if (var12 != 27951) {
                           if (this.UL != AliceObf_eJ.aHp) {
                              return this.aiT(var1, var7, var3, var5, var6);
                           }

                           if (this.aiQ()) {
                              return this.aip(var1, var7, var3, var5, var6);
                           }

                           if (!this.ahT()) {
                              int var9 = this.akh != null && !this.ahX() ? (1) : 0;
                              if (var9 != 0 && !this.aix(this.akh, var3, var6)) {
                                 return false;
                              }

                              this.PY -= 0.1;
                              this.PB = new AliceObf_bn(this, var1, var7, var3, var5, var6);
                              int var11 = this.aiS(var1, var8, var5);
                              if (var11 >= 0) {
                                 this.PB.pK = true;
                                 this.PB.Vv = var11;
                                 this.UL = AliceObf_eJ.aEW;
                                 this.nw = var1;
                                 this.Y = var11;
                                 this.MT = this.ajG();
                              }

                              if (!this.PB.ahn(true)) {
                                 this.PB = null;
                                 this.aji(true, true);
                                 return false;
                              }

                              if (var9 != 0 && !this.aiF()) {
                                 this.akh.ahn(false);
                              }
                           }

                           if (this.ahZ(var1)) {
                              return true;
                           }

                           if (this.akh != null && this.PB != null && (this.aix(this.akh, var3, var6) || this.ahX())) {
                              var16 = this.PB.getBreakProgress() <= 0.8 ? 5258 : 5259;

                              while (true) {
                                 int var13 = var16;
                                 if (var13 == 5258) {
                                    this.akh = null;
                                    break;
                                 }

                                 if (var13 != 5259) {
                                    break;
                                 }

                                 var16 = 5257;
                              }
                           }

                           if (this.akh == null) {
                              if (!this.aiQ()) {
                                 this.akh = new AliceObf_bn(this, var1, var7, var3, var5, var6);
                                 if (!this.akh.ahn(false)) {
                                    this.akh = null;
                                    return false;
                                 }

                                 return true;
                              }

                              return this.aip(var1, var7, var3, var5, var6);
                           }

                           return false;
                        }

                        var16 = 27949;
                     }
                  }
               }
            } else {
               return this.ahH(var1, var2, var3, var5, var6);
            }
         }
      } else {
         return false;
      }
   }

   private boolean ahG() {
      return (AliceObf_fS.Og()
            && this.PB == null
            && (this.akh == null || this.akh.cX)
            && this.UL == AliceObf_eJ.aHp
            && this.JO.isEmpty()
            && !this.avj
            && !this.aCg
            && !this.abr);
   }

   private boolean ahH(BlockPos var1, Direction var2, double var3, boolean var5, AliceObf_oW var6) {
      if (this.ain(var1)) {
         int var10000 = this.aBV.size() < (64) ? 38784 : 38783;

         while (true) {
            int var8 = var10000;
            if (var8 == 38783) {
               break;
            }

            if (var8 != 38784) {
               AliceObf_dS var7 = new AliceObf_dS(var1.toImmutable(), this.ajk(var1, var2), var3, var5, var6);
               if (this.QR != null && this.QR.arQ.agY().equals(var1)) {
                  if (var6 == AliceObf_oW.asm) {
                     this.QR.arQ = var7;
                  }

                  return true;
               }

               if (var6 == AliceObf_oW.aHC && this.aiu()) {
                  return false;
               }

               if (this.QR == null) {
                  this.QR = new AliceObf_kW(
                     this.ayO += 1L,
                     var7,
                     super.mc.world.getBlockState(var1),
                     new AliceObf_vw(System.nanoTime(), this.aiK())
                  );
                  return true;
               }

               if (var6 == AliceObf_oW.asm
                  && this.QR.arQ.ahc() == AliceObf_oW.aHC) {
                  this.QR.aGV.Zz(System.nanoTime());
               }

               if (this.Vb != null) {
                  short var9 = 20152;
               } else {
                  short var10 = 20153;
               }

               this.Vb = var7;
               return true;
            }

         }
      }

      return false;
   }

   private int ahI(AliceObf_kW var1) {
      return !this.xd.get() ? super.mc.player.getInventory().getSelectedSlot() : this.ajf(var1.arQ.agY(), var1.aGf);
   }

   private double ahJ(BlockPos var1, BlockState var2, int var3) {
      if (var3 >= 0) {
         int var10000 = var3 > (8) ? 9816 : 9815;

         while (true) {
            int var11 = var10000;
            if (var11 == 9815) {
               if (var2.isAir()) {
                  short var14 = 20860;
               } else {
                  short var15 = 20861;
               }

               float var4 = var2.getHardness(super.mc.world, var1);
               if (!(var4 < 0.0F) && Float.isFinite(var4)) {
                  if (var4 == 0.0F) {
                     return 1.0;
                  }

                  ItemStack var5 = super.mc.player.getInventory().getStack(var3);
                  double var6 = var5.getMiningSpeedMultiplier(var2);
                  int var10001 = var6 > 1.0 ? 57724 : 57725;

                  while (true) {
                     int var12 = var10001;
                     if (var12 == 57724) {
                        var6 += this.ahK(EntityAttributes.MINING_EFFICIENCY, var5);
                        break;
                     }

                     if (var12 != 57725) {
                        break;
                     }

                  }

                  if (StatusEffectUtil.hasHaste(super.mc.player)) {
                     var6 *= 1.0 + (StatusEffectUtil.getHasteAmplifier(super.mc.player) + 1) * 0.2;
                  }

                  if (super.mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
                     var6 *= switch (super.mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                        case 0 -> 0.3;
                        case 1 -> 0.09;
                        case 2 -> 0.0027;
                        default -> 8.1E-4;
                     };
                  }

                  var6 *= this.ahK(EntityAttributes.BLOCK_BREAK_SPEED, var5);
                  if (super.mc.player.isSubmergedIn(FluidTags.WATER)) {
                     char var16 = 57410;
                  } else {
                     char var17 = 57411;
                  }

                  if (!super.mc.player.isOnGround()) {
                     char var18 = 36541;
                  } else {
                     char var19 = 36542;
                  }

                  int var8 = var2.isToolRequired() && !var5.isSuitableFor(var2) ? (0) : (1);
                  double var9 = var6 / var4 / (var8 != 0 ? 30.0 : 100.0);
                  return Double.isFinite(var9) && var9 > 0.0 ? var9 : 0.0;
               }

               return 0.0;
            }

            if (var11 != 9816) {
               break;
            }

         }
      }

      return 0.0;
   }

   private double ahK(RegistryEntry<EntityAttribute> var1, ItemStack var2) {
      EntityAttributeInstance var3 = super.mc.player.getAttributeInstance(var1);
      if (var3 == null) {
         return 0.0;
      } else {
         EntityAttributeInstance var4 = new EntityAttributeInstance(var1, var0 -> {});
         var4.setFrom(var3);

         for (int var5 = 0; var5 < (9); var5++) {
            super.mc.player.getInventory().getStack(var5).applyAttributeModifiers(EquipmentSlot.MAINHAND, (var2x, var3x) -> {
               if (var2x.equals(var1)) {
                  var4.removeModifier(var3x.id());
               }
            });
         }

         double var7 = var4.getValue();
         var2.applyAttributeModifiers(EquipmentSlot.MAINHAND, (var2x, var3x) -> {
            if (var2x.equals(var1)) {
               var4.updateModifier(var3x);
            }
         });
         return Math.max(0.0, Math.min(var3.getValue(), Math.min(var7, var4.getValue())));
      }
   }

   private void ahL() {
      long var1 = System.nanoTime();
      if (this.akh != null && this.akh.cX) {
         this.akh = null;
      }

      AliceObf_kW var3 = this.QR;
      if (!(var3 != null)) {
         char var10 = 48289;
      }

      if (this.QR == null) {
         AliceObf_dS var4 = this.Vb;
         this.Vb = null;
         int var10001 = var4 == null ? 19412 : 19413;

         while (true) {
            int var9 = var10001;
            if (var9 == 19412) {
               if (!this.ED.isEmpty()) {
                  var4 = (AliceObf_dS)this.ED.remove(0);
               }
               break;
            }

            if (var9 != 19413) {
               break;
            }

         }

         if (var4 != null) {
            this.ahH(var4.agY(), var4.agZ(), var4.aha(), var4.ahb(), var4.ahc());
         }
      }
   }

   private void ahM() {
      if (this.QR != null && this.QR.aGV.Zm() != AliceObf_eG.afc) {
         this.aBV.put(this.QR.arQ.agY(), this.QR.aGf);
         if (!this.QR.aGV.Zo()) {

         } else {
            char var1 = 64900;
         }
      }

      this.QR = null;
      this.Vb = null;
      this.fd.clear();
      AliceObf_fS.Om(
         this, System.nanoTime() + (500000000)
      );
   }

   private void ahN(ClientPlayNetworkHandler var1, Packet<?> var2) {
      AliceObf_gF var3 = (AliceObf_gF)this.fd.remove(var2);
      if (var3 != null
         && this.isActive()
         && super.mc.isOnThread()
         && var1 == this.atH
         && var1 == super.mc.getNetworkHandler()
         && super.mc.world == this.im
         && super.mc.player == this.aMW
         && var3.agW() == this.Rn) {
         if (var3.agV() == var1.getConnection()) {

         } else {
            short var7 = 1083;
         }

         if (var2 instanceof PlayerActionResponseS2CPacket var4) {
            if (this.QR != null && var3.agX() >= this.QR.oy) {
               this.QR.aGV.Zt(var4.sequence(), System.nanoTime());
            }
         } else {
            if (!(var2 instanceof BlockUpdateS2CPacket)) {
               char var8 = 59412;
            }

            if (var2 instanceof ChunkDeltaUpdateS2CPacket var6) {
               var6.visitUpdates((var2x, var3x) -> this.ahO(var2x, var3x, var3.agX()));
            } else if (var2 instanceof UpdateSelectedSlotS2CPacket && this.QR != null) {
               this.miningPreempted();
               AliceObf_fS.Om(this, System.nanoTime() + this.aiK());
            }
         }
      }
   }

   private void ahO(BlockPos var1, BlockState var2, long var3) {
      if (super.mc.world.isChunkLoaded(var1)) {
         if (super.mc.world.getBlockState(var1).equals(var2)) {
            BlockState var5 = (BlockState)this.aBV.get(var1);
            if (var5 != null && !var5.equals(var2)) {
               this.aBV.remove(var1);
            }

            AliceObf_kW var6 = this.QR;
            if (var6 != null) {
               if (var6.arQ.agY().equals(var1)) {

               } else {
                  char var7 = 33292;
               }
            }
         }
      }
   }

   public boolean miningWindowReady() {
      if (this.isActive() && this.QR != null && this.QR.aGV.Zq() && super.mc.player != null && !super.mc.player.isUsingItem()) {
         int var10000 = this.QR.aGV.Zl() == AliceObf_jj.CH
            ? 11605
            : 11606;

         while (true) {
            int var1 = var10000;
            if (var1 == 11605) {
               if (!this.aiF()) {
                  return true;
               }
               break;
            }

            if (var1 != 11606) {
               return true;
            }

         }
      }

      return false;
   }

   public AliceObf_y prepareCombatIntent() {
      if (!this.miningWindowReady()) {
         return null;
      } else {
         AliceObf_kW var1 = this.QR;
         long var2 = this.Rn;
         return new AliceObf_oa(this, var1, var2);
      }
   }

   public long miningCommandId() {
      return this.QR == null ? -1L : this.QR.JE;
   }

   public boolean miningActionAllowed(PlayerActionC2SPacket var1) {
      AliceObf_kW var2 = this.QR;
      if (var2 != null
         && this.miningWindowReady()
         && var2.arQ.agY().equals(var1.getPos())
         && super.mc.world == this.im
         && super.mc.player == this.aMW
         && super.mc.getNetworkHandler() == this.atH) {
         long var3 = System.nanoTime();
         int var5 = this.ahI(var2);
         double var6 = this.ahJ(var2.arQ.agY(), var2.aGf, var5);
         var2.aGV.Zw(var3, var6, this.aiM());
         if (var1.getAction() == Action.ABORT_DESTROY_BLOCK) {
            return (var2.aGV.Zl() == AliceObf_jj.ajy);
         } else {
            if (super.mc.world.isChunkLoaded(var2.arQ.agY())) {

            } else {
               char var8 = 41145;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void ahP(AliceObf_kW var1) {
      if (this.miningWindowReady() && super.mc.world == this.im && super.mc.player == this.aMW) {
         long var2 = System.nanoTime();
         int var4 = this.ahI(var1);
         double var5 = this.ahJ(var1.arQ.agY(), var1.aGf, var4);
         var1.aGV.Zw(var2, var5, this.aiM());
         AliceObf_jj var7 = var1.aGV.Zl();
         if (var1.aGV.Zq()) {
            if (var7 == AliceObf_jj.ajy
               || var4 >= 0
                  && Double.isFinite(var5)
                  && !(var5 <= 0.0)
                  && super.mc.world.getBlockState(var1.arQ.agY()).equals(var1.aGf)
                  && this.ahY(var1.arQ.agY())) {
               AliceObf_jU var8 = AliceObf_fS.Oi(
                  this
               );
               if (var8 != null) {
                  boolean var13 = false /* VF: Semaphore variable */;

                  try {
                     var13 = true;
                     this.ahQ(var1, var8, var7, var4, var5, var2);
                     var13 = false;
                  } catch (Error | RuntimeException var14) {
                     var1.aGV.Zz(System.nanoTime());
                     throw var14;
                  } finally {
                     if (var13) {
                        if (var1.aGV.Zl() != AliceObf_jj.aMK) {
                           AliceObf_fS.Om(
                              this, System.nanoTime() + this.aiK()
                           );
                        }
                     }
                  }

                  if (var1.aGV.Zl() != AliceObf_jj.aMK) {
                     AliceObf_fS.Om(
                        this, System.nanoTime() + this.aiK()
                     );
                  }
               }
            } else {
               var1.aGV.Zz(var2);
            }
         }
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void ahQ(AliceObf_kW var1, AliceObf_jU var2, AliceObf_jj var3, int var4, double var5, long var7) {
      if (var3 != AliceObf_jj.ajy
         && !AliceObf_fS.Oj(var2, var4)) {
         var1.aGV.Zy(var7);
         AliceObf_fS.Om(this, var7 + this.aiK());
      } else {
         if (!(var3 != AliceObf_jj.CH)) {
            char var21 = 41203;
         }

         if (!(var3 == AliceObf_jj.aHt)) {
            short var22 = 11403;
         }

         Action var9 = Action.ABORT_DESTROY_BLOCK;

         int var10;
         try {
            var10 = this.getSeq();
         } catch (Throwable var18) {
            var1.aGV.Zy(var7);
            AliceObf_fS.Om(this, var7 + this.aiK());
            return;
         }

         if (var3 == AliceObf_jj.aHt) {
            MeteorClient.EVENT_BUS.post(new AliceObf_cw(var1.arQ.agY(), false));
         }

         PlayerActionC2SPacket var11 = new PlayerActionC2SPacket(var9, var1.arQ.agY(), var1.arQ.agZ(), var10);
         this.aAz = true;
         boolean var16 = false /* VF: Semaphore variable */;

         boolean var12;
         try {
            var16 = true;
            var12 = AliceObf_fS.Ok(
               var2, var11, () -> super.mc.getNetworkHandler().sendPacket(var11)
            );
            var16 = false;
         } finally {
            if (var16) {
               this.aAz = false;
            }
         }

         this.aAz = false;
         if (!(!var12)) {
            short var23 = 15187;
         }

         if (var3 == AliceObf_jj.CH) {
            var1.oy = var2.Zt;
            var1.aGV.Zs(var10, var2.Zt, var5, this.aiM());
         } else if (var3 != AliceObf_jj.aHt) {
            var1.aGV.ZA();
         } else {
            var1.oy = var2.Zt;
            var1.aGV.Zx(var10, var2.Zt);
            this.aku = var2.Zt;
         }

         if (var12) {
            if (var1.aGV.Zl() == AliceObf_jj.aMK) {

            } else {
               short var24 = 3264;
            }
         }

         AliceObf_fS.Om(this, var7 + this.aiK());
      }
   }

   public void miningPreempted() {
      if (this.QR != null) {
         this.QR.aGV.Zz(System.nanoTime());
      }
   }

   public void suspendForItemUse() {
      this.miningPreempted();
   }

   public boolean canYieldAfterExecution() {
      return true;
   }

   public int combatPriority() {
      return (-2147483648);
   }

   public boolean yieldToManualInput() {
      if (this.QR != null && this.QR.arQ.ahc() == AliceObf_oW.asm
         )
       {
         return false;
      } else {
         this.miningPreempted();
         return true;
      }
   }

   @AliceHandler
   public void ahR(StartBreakingBlockEvent var1) {
      var1.cancel();
      this.ahF(
         var1.blockPos,
         var1.direction,
         100.0,
         false,
         AliceObf_oW.asm
      );
   }

   public boolean ahS() {
      int var1 = !this.aCg && !this.avj ? (0) : 1;
      if (this.ahT() && this.PB.ahj(false)) {
         var1 = (0);
      }

      return (var1 != 0);
   }

   public boolean ahT() {
      return (this.PB != null);
   }

   public boolean ahU() {
      return (this.akh != null && !this.akh.cX);
   }

   public void ahV(boolean var1) {
      if (this.ahT()) {
         if (!(var1)) {
            char var2 = 38615;
         }

         this.PB = null;
      }
   }

   public BlockPos getDelayedDestroyBlockPos() {
      int var10000 = this.PB == null ? 40987 : 40988;

      while (true) {
         int var1 = var10000;
         if (var1 == 40987) {
            return null;
         }

         if (var1 != 40988) {
            return this.PB.axl;
         }

      }
   }

   public double getDelayedDestroyProgress() {
      return this.PB == null ? 0.0 : this.PB.getBreakProgress();
   }

   public BlockPos getRebreakBlockPos() {
      return this.akh == null ? null : this.akh.axl;
   }

   public double getRebreakBlockProgress() {
      return this.akh == null ? 0.0 : this.akh.getBreakProgress();
   }

   public AliceObf_uH ahW(boolean var1) {
      if (this.QR != null) {
         if (var1) {
            return null;
         } else {
            AliceObf_kW var11 = this.QR;
            float var3 = (float)var11.aGV.ZD(System.nanoTime());
            return new AliceObf_uH(var11.arQ.agY(), !var11.aGV.Zo(), var3, 0, var3 >= 1.0F ? 0 : Integer.MAX_VALUE);
         }
      } else {
         AliceObf_bn var2 = var1 ? this.akh : this.PB;
         if (this.isActive()) {
            if (!(var2 != null)) {
               char var12 = 40304;
            }
         }

         return null;
      }
   }

   public boolean ahX() {
      return (boolean)(this.SK && this.akh != null ? this.akh.cX : 0);
   }

   public void setAllowRebreakLoop(boolean var1) {
      this.SK = var1;
      if (!var1 && this.akh != null) {
         if (!(this.akh.cX)) {
            char var2 = 49322;
         }
      }
   }

   public boolean ahY(BlockPos var1) {
      return (new Box(var1).squaredMagnitude(super.mc.player.getEyePos()) <= this.aMM.get() * this.aMM.get());
   }

   public boolean ahZ(BlockPos var1) {
      if (var1 == null) {
         return false;
      } else {
         if (this.QR != null) {
            int var10000 = !var1.equals(this.QR.arQ.agY()) ? 34493 : 34494;

            while (true) {
               int var2 = var10000;
               if (var2 == 34493) {
                  break;
               }

               if (var2 != 34494) {
                  return true;
               }

            }
         }

         return !((this.Vb == null || !var1.equals(this.Vb.agY()))
               && !this.Kk.contains(var1)
               && !this.JO.containsKey(var1)
               && (this.akh == null || !var1.equals(this.akh.axl))
               && (this.PB == null || !var1.equals(this.PB.axl))
               && !this.ait(var1));
      }
   }

   public int getImmediateSlots() {
      if (!this.isActive() || super.mc.player == null || super.mc.world == null) {
         return (0);
      } else if (this.QR != null || this.ahG()) {
         return this.QR == null ? 1 : 0;
      } else if (this.aiQ()) {
         return (0);
      } else if (!this.ahT()) {
         if (this.akh != null) {
            if (this.ahX()) {

            } else {
               short var1 = 30565;
            }

            return (1);
         } else {
            return (2);
         }
      } else {
         return this.akh != null && !this.ahX() ? 0 : (1);
      }
   }

   public boolean aia(BlockPos var1, Direction var2, BlockPos var3, Direction var4, double var5, boolean var7) {
      if (this.isActive() && super.mc.player != null && super.mc.world != null && var1 != null && !this.aiu()) {
         BlockPos var8 = var1.toImmutable();
         BlockPos var9 = var3 != null && !var3.equals(var8) ? var3.toImmutable() : null;
         this.ahw();
         if (this.QR == null && this.Vb == null && !this.ahG()) {
            if (!this.ain(var8)) {
               return false;
            } else {
               if (var9 != null) {
                  short var16 = 10967;
               } else {
                  short var17 = 10968;
               }

               BlockPos var14 = this.aim();
               if (!var7 && var14 != null && !var14.equals(var8) && !var14.equals(var9)) {
                  return false;
               } else {
                  if (var7) {
                     this.aio(var8, var9);
                  }

                  Direction var11 = this.ajk(var8, var2);
                  int var12 = !this.ahZ(var8)
                        && !this.ahF(
                           var8,
                           var11,
                           var5,
                           false,
                           AliceObf_oW.aHC
                        )
                     ? (0)
                     : (1);
                  if (var12 != 0) {
                     if (var9 != null) {
                        Direction var13 = this.ajk(var9, var4);
                        this.ahF(
                           var9,
                           var13,
                           var5 - 0.01,
                           false,
                           AliceObf_oW.aHC
                        );
                     }

                     return true;
                  } else {
                     return false;
                  }
               }
            }
         } else if (!this.ain(var8)) {
            return false;
         } else {
            if (var7
               && this.QR != null
               && this.QR.arQ.ahc() == AliceObf_oW.aHC
               && !this.QR.arQ.agY().equals(var8)) {
               if (!this.QR.arQ.agY().equals(var9)) {

               } else {
                  char var15 = 63532;
               }
            }

            boolean var10 = this.ahH(
               var8, var2, var5, false, AliceObf_oW.aHC
            );
            if (var10 && var9 != null && this.ain(var9)) {
               this.ahH(
                  var9,
                  var4,
                  var5 - 0.01,
                  false,
                  AliceObf_oW.aHC
               );
            }

            return var10;
         }
      } else {
         return false;
      }
   }

   public void aib() {
      if (this.Vb != null && this.Vb.ahc() == AliceObf_oW.aHC) {
         this.Vb = null;
      }

      this.ED
         .removeIf(
            var0 -> {
               if (var0 != null) {
                  int var10000 = var0.ahc() != AliceObf_oW.aHC
                     ? 58456
                     : 58455;

                  while (true) {
                     int var1 = var10000;
                     if (var1 == 58455) {
                        return true;
                     }

                     if (var1 != 58456) {
                        break;
                     }

                  }
               }

               return false;
            }
         );
   }

   public BlockPos aic() {
      if (this.QR != null) {
         if (this.QR.arQ.ahc() != AliceObf_oW.aHC) {

         } else {
            short var2 = 30012;
         }
      }

      BlockPos var1 = this.aim();
      if (var1 != null) {
         short var3 = 6703;
      } else {
         short var4 = 6704;
      }

      if (this.PB != null && this.PB.aAO == AliceObf_oW.aHC) {
         return this.PB.axl;
      } else {
         return this.akh != null
               && this.akh.aAO == AliceObf_oW.aHC
            ? this.akh.axl
            : null;
      }
   }

   public BlockPos aid() {
      if (this.Vb != null && this.Vb.ahc() == AliceObf_oW.aHC) {
         return this.Vb.agY();
      } else {
         BlockPos var1 = this.aic();
         if (this.PB != null
            && this.PB.aAO == AliceObf_oW.aHC
            && !this.PB.axl.equals(var1)) {
            return this.PB.axl;
         } else {
            if (this.akh != null
               && this.akh.aAO == AliceObf_oW.aHC) {
               if (!this.akh.axl.equals(var1)) {

               } else {
                  char var4 = 48825;
               }
            }

            for (AliceObf_dS var3 : this.ED) {
               if (var3 != null
                  && var3.ahc() == AliceObf_oW.aHC
                  && !var3.agY().equals(var1)) {
                  return var3.agY();
               }
            }

            return null;
         }
      }
   }

   public boolean aie() {
      return !(!this.aih() && this.UL == AliceObf_eJ.aHp);
   }

   public boolean aif(BlockPos var1, BlockPos var2) {
      if (this.QR != null && this.QR.arQ.ahc() == AliceObf_oW.aHC
         )
       {
         int var3 = !this.QR.arQ.agY().equals(var1) && (this.Vb == null || !this.Vb.agY().equals(var1)) ? 0 : (1);
         if (var2 != null) {
            if (this.QR.arQ.agY().equals(var2)) {

            } else {
               char var5 = 46039;
            }
         }

         byte var4 = 1;
         return (var3 != 0 && var4 != 0);
      } else {
         return false;
      }
   }

   public boolean aig() {
      if (this.QR == null) {
         return false;
      } else {
         AliceObf_jj var1 = this.QR.aGV.Zl();
         return (boolean)(var1 != AliceObf_jj.CH
               && var1 != AliceObf_jj.aou);
      }
   }

   public boolean aih() {
      if ((
            this.QR == null
               || this.QR.arQ.ahc() != AliceObf_oW.aHC
         )
         && (this.Vb == null || this.Vb.ahc() != AliceObf_oW.aHC)
         )
       {
         if (this.PB != null && this.PB.aAO == AliceObf_oW.aHC) {
            return true;
         } else {
            if (!(this.akh != null)) {
               char var4 = 44607;
            }

            for (AliceObf_dS var2 : this.ED) {
               if (var2 != null) {
                  int var10001 = var2.ahc() == AliceObf_oW.aHC
                     ? 50188
                     : 50189;

                  while (true) {
                     int var3 = var10001;
                     if (var3 == 50188) {
                        return true;
                     }

                     if (var3 != 50189) {
                        break;
                     }

                  }
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }

   public boolean aii() {
      if (this.akh != null && this.akh.aAO == AliceObf_oW.aHC) {
         if (!(!this.akh.cX)) {
            char var1 = 51944;
         }
      }

      return false;
   }

   public double aij() {
      return (Double)this.aMM.get();
   }

   public boolean aik() {
      return this.isActive();
   }

   public boolean ail(BlockPos var1) {
      return this.ain(var1);
   }

   private BlockPos aim() {
      if (!(super.mc.world == null)) {
         short var2 = 1726;
      }

      if (this.PB != null
         && this.PB.aAO == AliceObf_oW.aHC
         && !super.mc.world.isAir(this.PB.axl)) {
         return this.PB.axl;
      } else {
         int var10001 = this.akh != null ? 40229 : 40230;

         while (true) {
            int var1 = var10001;
            if (var1 == 40229) {
               if (this.akh.aAO == AliceObf_oW.aHC
                  && !super.mc.world.isAir(this.akh.axl)) {
                  return this.akh.axl;
               }
               break;
            }

            if (var1 != 40230) {
               break;
            }

         }

         return null;
      }
   }

   private boolean ain(BlockPos var1) {
      if (var1 != null && super.mc.world != null && super.mc.player != null) {
         BlockState var2 = super.mc.world.getBlockState(var1);
         if (var2.equals(this.aBV.get(var1))) {
            return false;
         } else {
            return (BlockUtils.canBreak(var1, var2) && this.ahY(var1));
         }
      } else {
         return false;
      }
   }

   private void aio(BlockPos var1, BlockPos var2) {
      this.ED
         .removeIf(
            var2x -> {
               if (var2x != null
                  && var2x.ahc() == AliceObf_oW.aHC
                  && !var2x.agY().equals(var1)) {
                  if (var2x.agY().equals(var2)) {

                  } else {
                     short var3x = 22128;
                  }
               }

               return false;
            }
         );
      if (this.PB != null && this.PB.aAO == AliceObf_oW.aHC) {
         if (!this.PB.axl.equals(var1)) {

         } else {
            char var3 = 47130;
         }
      }

      if (this.akh != null
         && this.akh.aAO == AliceObf_oW.aHC
         && !this.akh.axl.equals(var1)
         && !this.akh.axl.equals(var2)) {
         this.akh.ahs();
         this.akh = null;
      }

      if (this.UL != AliceObf_eJ.aHp && this.aiw()) {
         this.aji(true, true);
      }
   }

   private boolean aip(BlockPos var1, Direction var2, double var3, boolean var5, AliceObf_oW var6) {
      if (var1 != null && var2 != null) {
         BlockPos var7 = var1.toImmutable();

         for (int var8 = 0; var8 < this.ED.size(); var8++) {
            AliceObf_dS var9 = (AliceObf_dS)this.ED.get(var8);
            if (var9 != null) {
               if (var7.equals(var9.agY())) {

               } else {
                  short var10 = 24701;
               }

               if (this.aiy(var9, var3, var6)) {
                  this.ED.set(var8, new AliceObf_dS(var7, var2, var3, var5, var6));
                  return true;
               }

               return false;
            }
         }

         if (this.ED.size() >= (2)) {
            char var11 = 54791;
         } else {
            char var12 = 54792;
         }

         this.ED.add(new AliceObf_dS(var7, var2, var3, var5, var6));
         return true;
      } else {
         return false;
      }
   }

   private void aiq() {
      if (!this.ED.isEmpty()) {
         if (!(super.mc.player != null)) {
            short var3 = 4710;
         }
      }
   }

   private void air() {
      if (super.mc.world == null) {
         this.ED.clear();
      } else {
         this.ED.removeIf(var1 -> {
            if (var1 != null && !this.ais(var1.agY())) {
               BlockState var2 = super.mc.world.getBlockState(var1.agY());
               return !(BlockUtils.canBreak(var1.agY(), var2) && this.ahY(var1.agY()));
            } else {
               return false;
            }
         });
      }
   }

   private boolean ais(BlockPos var1) {
      if (var1 == null) {
         return false;
      } else {
         return ((this.akh == null || !var1.equals(this.akh.axl)) && (this.PB == null || !var1.equals(this.PB.axl)));
      }
   }

   private boolean ait(BlockPos var1) {
      if (var1 == null) {
         return false;
      } else {
         for (AliceObf_dS var3 : this.ED) {
            if (var3 != null && var1.equals(var3.agY())) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean aiu() {
      if (this.QR != null) {
         if (this.QR.arQ.ahc() != AliceObf_oW.asm) {
            short var4 = 14703;
         } else {
            short var5 = 14704;
         }
      } else if (this.Vb == null
         || this.Vb.ahc() != AliceObf_oW.asm) {
         if (this.PB != null && this.PB.aAO == AliceObf_oW.asm) {
            return true;
         }

         if (this.akh != null && this.akh.aAO == AliceObf_oW.asm) {
            return true;
         }

         Iterator var1 = this.ED.iterator();
         if (var1.hasNext()) {

         } else {
            short var3 = 17141;
         }

         return false;
      }

      return true;
   }

   private void aiv(BlockPos var1) {
      if (var1 != null) {
         this.ED
            .removeIf(
               var0 -> !(var0 != null
                     && var0.ahc() == AliceObf_oW.aHC)
            );
         if (this.UL != AliceObf_eJ.aHp) {
            if (this.aiw()) {

            } else {
               short var2 = 24444;
            }
         }

         if (this.PB != null && this.PB.aAO == AliceObf_oW.aHC) {
            if (var1.equals(this.PB.axl) && !this.PB.pK) {
               this.PB.aAO = AliceObf_oW.asm;
            } else {
               this.PB.ahs();
               this.PB = null;
            }
         }

         if (this.akh != null && this.akh.aAO == AliceObf_oW.aHC) {
            if (var1.equals(this.akh.axl) && !this.akh.pK) {
               this.akh.aAO = AliceObf_oW.asm;
            } else {
               this.akh.ahs();
               this.akh = null;
            }
         }
      }
   }

   private boolean aiw() {
      return (boolean)((
               this.PB == null
                  || !this.PB.pK
                  || this.PB.aAO != AliceObf_oW.aHC
            )
            && (
               this.akh == null
                  || !this.akh.pK
                  || this.akh.aAO != AliceObf_oW.aHC
            ));
   }

   private boolean aix(AliceObf_bn var1, double var2, AliceObf_oW var4) {
      if (var1 == null) {
         return true;
      } else if (var4 == AliceObf_oW.asm
         && var1.aAO == AliceObf_oW.aHC) {
         return true;
      } else if (var4 == AliceObf_oW.aHC
         && var1.aAO == AliceObf_oW.asm) {
         return false;
      } else {
         return (var2 >= var1.UW);
      }
   }

   private boolean aiy(AliceObf_dS var1, double var2, AliceObf_oW var4) {
      if (var1 == null) {
         return true;
      } else if (var4 == AliceObf_oW.asm
         && var1.ahc() == AliceObf_oW.aHC) {
         return true;
      } else if (var4 == AliceObf_oW.aHC
         && var1.ahc() == AliceObf_oW.asm) {
         return false;
      } else {
         return !(!(var2 >= var1.aha()));
      }
   }

   private boolean aiz(FindItemResult var1, boolean var2, BlockPos var3) {
      if ((Boolean)this.xd.get() && var1 != null) {
         if (!var1.found()) {

         } else {
            short var5 = 21209;
         }

         if (this.avj && AliceObf_fS.OL() != this.Az) {
            this.aiE(false);
         }

         if (this.abr && AliceObf_fS.OL() != this.aty) {
            this.ajh(false);
         }

         if (!this.aiB(var1.slot())) {
            if (var2) {
               if (!this.aCg) {
                  char var7 = 38867;
               } else {
                  char var8 = 38868;
               }

               return false;
            } else {
               int var4 = AliceObf_fS.OL();
               if (!(var4 < 0)) {
                  char var6 = 58479;
               }

               return false;
            }
         } else {
            if ((this.avj || this.abr) && var3 != null) {
               this.Wr.add(var3.toImmutable());
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private boolean aiA(FindItemResult var1) {
      if ((Boolean)this.xd.get()) {

      } else {
         char var2 = 49363;
      }

      return true;
   }

   private boolean aiB(int var1) {
      return (var1 >= 0
            && var1 <= (8)
            && AliceObf_fS.OL() == var1);
   }

   private boolean aiC(int var1) {
      if (var1 < 0 || var1 > (8) || super.mc.getNetworkHandler() == null) {
         return false;
      } else if (AliceObf_fS.OL() == var1) {
         return true;
      } else if (AliceObf_fS.OO()) {
         return false;
      } else {
         super.mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(var1));
         return !(AliceObf_fS.OL() != var1);
      }
   }

   private void aiD() {
      this.aiE(false);
   }

   private void aiE(boolean var1) {
      if (this.avj) {
         int var2 = AliceObf_fS.OL();
         if (var2 < 0 || var2 > (8)) {
            return;
         }

         if (var2 == this.Az) {
            if (!var1 && !this.Wr.isEmpty()) {
               return;
            }

            if (this.EW >= 0 && this.EW <= 8 && (var2 == this.EW || this.aiC(this.EW))) {
               this.avj = false;
               this.EW = (-1);
               this.Az = -1;
               this.Wr.clear();
            }
         } else {
            this.avj = false;
            this.EW = -1;
            this.Az = (-1);
            this.Wr.clear();
         }
      }

      if (this.aCg && super.mc.player != null && super.mc.getNetworkHandler() != null) {
         if (!InvUtils.swapBack()) {
            return;
         }

         this.aCg = false;
      }
   }

   private boolean aiF() {
      return (!this.aiG(2, System.nanoTime()));
   }

   private boolean aiG(int var1, long var2) {
      double var4 = this.abH;

      for (int var6 = (0); var6 < var1; var6++) {
         var4 = this.aiH(var4, var2);
      }

      return (var4 <= 900.0);
   }

   private double aiH(double var1, long var3) {
      if (this.aku != (0) && var3 - this.aku < (275000000)) {
         double var5 = (var3 - this.aku) / 1000000.0;
         double var7 = var1 + (300.0 - var5);
         return MathHelper.clamp(var7, -1000.0, 1000.0);
      } else {
         return var1 * 0.9;
      }
   }

   private void aiI(Action var1, long var2) {
      if (var1 == Action.START_DESTROY_BLOCK) {
         this.abH = this.aiH(this.abH, var2);
      } else if (var1 == Action.STOP_DESTROY_BLOCK) {
         this.aku = var2;
      }
   }

   private int aiJ(BlockState var1) {
      if ((Boolean)this.xd.get()) {
         FindItemResult var2 = InvUtils.findFastestTool(var1);
         if (var2.found() && var2.slot() >= 0 && var2.slot() <= (8)) {
            return var2.slot();
         }
      }

      return AliceObf_fS.OL();
   }

   private long aiK() {
      PlayerListEntry var1 = super.mc.getNetworkHandler().getPlayerListEntry(super.mc.player.getUuid());
      int var2 = var1 == null ? (100) : var1.getLatency();
      return SpeedMinePlusPlus.aiL(var2, this.aiM());
   }

   private static long aiL(int var0, double var1) {
      long var3 = Math.max(0, Math.min((5000), var0));
      double var5 = Double.isFinite(var1) ? Math.max(50.0, Math.min(1000.0, var1)) : 50.0;
      return TimeUnit.MILLISECONDS.toNanos((long)Math.ceil(Math.max(500.0, var3 * 2.0 + var5 * 4.0)));
   }

   private double aiM() {
      float var1 = TickRate.INSTANCE.getTickRate();
      return Float.isFinite(var1) && var1 > 0.0F ? 1000.0 / Math.min(20.0, (double)var1) : 50.0;
   }

   private void aiN(BlockPos var1, double var2, boolean var4, long var5) {
      long var7 = System.nanoTime();
      long var9 = this.aiK();
      long var11 = TimeUnit.MILLISECONDS.toNanos((long)Math.ceil(Math.max(0.0, var2) * this.aiM()));
      AliceObf_sk var13 = (AliceObf_sk)this.JO.get(var1);
      this.JO
         .put(
            var1.toImmutable(),
            new AliceObf_sk(
               var13 == null ? var5 : var13.ahd(),
               var7 + var11 + (var4 ? var9 : 0L),
               var13 == null ? var7 + var11 + (3) * var9 : var13.ahf(),
               (var13 == null ? (0) : var13.ahg()) + (var4 ? 1 : (0))
            )
         );
   }

   private boolean aiO(BlockPos var1) {
      AliceObf_sk var2 = (AliceObf_sk)this.JO.get(var1);
      long var3 = System.nanoTime();
      return !(var2 != null && (var2.ahg() >= (3) || var3 < var2.ahe() || var3 >= var2.ahf()));
   }

   private void aiP() {
      long var1 = System.nanoTime();

      for (BlockPos var4 : new ArrayList<>(this.JO.keySet())) {
         AliceObf_sk var5 = (AliceObf_sk)this.JO.get(var4);
         if (var5 != null) {
            if (var1 < var5.ahf()) {
               if (this.ahY(var4)) {

               } else {
                  short var6 = 26346;
               }
            }

            if (this.ajn(var4, Direction.DOWN)) {
               this.JO.remove(var4);
               this.Kk.remove(var4);
               this.Wr.remove(var4);
               if (this.PB != null && var4.equals(this.PB.axl)) {
                  this.PB = null;
               }

               if (this.akh != null) {
                  if (!var4.equals(this.akh.axl)) {
                     short var7 = 28267;
                  } else {
                     short var8 = 28266;
                  }
               }

               if (var4.equals(this.nw)) {
                  this.ajj(true);
               }
            }
         }
      }

      if (this.Wr.isEmpty()) {
         this.aiE(true);
         if (this.Tg) {
            this.ajh(true);
         }
      }
   }

   private boolean aiQ() {
      return this.aiF();
   }

   private boolean aiR() {
      if (!this.ahT()) {
         return true;
      } else {
         return (boolean)(this.akh != null && !this.ahX());
      }
   }

   private int aiS(BlockPos var1, BlockState var2, boolean var3) {
      if (!var3) {
         if (this.UL != AliceObf_eJ.aHp
            || this.ahT()
            || this.akh != null
            || !this.ED.isEmpty()) {
            return -1;
         } else if (!this.aCg && !this.avj && !this.abr) {
            int var4 = this.ajf(var1, var2);
            if (var4 >= 0 && this.aja(var1, var2, var4)) {
               int var5 = this.ajb(var1, var2, var4);
               if (var5 < 0) {
                  return -1;
               } else {
                  return this.ajd(var1, var2, var4, -1, var5) >= 0 ? var4 : (-1);
               }
            } else {
               return -1;
            }
         } else {
            return (-1);
         }
      } else {
         return -1;
      }
   }

   private boolean aiT(BlockPos var1, Direction var2, double var3, boolean var5, AliceObf_oW var6) {
      if (var5 || var1 == null || var2 == null || super.mc.world == null) {
         return false;
      } else if (this.UL != AliceObf_eJ.aEW
         && this.UL != AliceObf_eJ.rE
         && this.UL != AliceObf_eJ.aKv) {
         return false;
      } else if (this.PB != null && !this.aix(this.PB, var3, var6)) {
         return false;
      } else if (this.aiV() && !var1.equals(this.nw)) {
         BlockState var7 = super.mc.world.getBlockState(var1);
         int var8 = this.ajf(var1, var7);
         if (var8 >= 0 && this.aja(var1, var7, var8)) {
            BlockState var9 = super.mc.world.getBlockState(this.nw);
            int var10 = this.ajb(this.nw, var9, this.Y);
            if (var10 < 0) {
               return false;
            } else if (this.ajd(this.nw, var9, this.Y, var8, Math.max((double)var10, this.PY - this.PB.Qe)) >= 0) {
               if (this.aip(var1, var2, var3, false, var6)) {
                  if (this.UL == AliceObf_eJ.aEW) {
                     this.UL = AliceObf_eJ.rE;
                  }

                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean aiU(boolean var1) {
      if (this.UL == AliceObf_eJ.aHp) {
         return false;
      } else if (super.mc.player != null && super.mc.world != null && !var1) {
         if (!(this.UL == AliceObf_eJ.aEW)) {
            char var12 = 46234;
         }

         if (this.UL != AliceObf_eJ.rE) {
            if (this.UL == AliceObf_eJ.aKv) {
               if (this.aiW() && !this.ED.isEmpty()) {
                  if (System.nanoTime() >= this.ff) {
                     if (!this.aiF()) {
                        short var16 = 26360;
                     } else {
                        short var17 = 26359;
                     }

                     AliceObf_dS var11 = this.aiY();
                     if (var11 != null) {
                        if (this.aiZ(var11.agY())) {

                        } else {
                           short var18 = 7846;
                        }
                     }

                     this.aji(true, true);
                     return false;
                  } else {
                     return true;
                  }
               } else {
                  this.aji(true, false);
                  return false;
               }
            } else {
               if (this.UL == AliceObf_eJ.Mq) {
                  short var14 = 13635;
               } else {
                  short var15 = 13636;
               }

               return false;
            }
         } else if (this.aiV() && !this.ED.isEmpty()) {
            double var2 = Math.max(0.0, this.PY - this.PB.Qe);
            int var4 = this.PB.getGrimRequiredTicks();
            if (var4 >= 0) {
               if (var2 + 1.0E-4 < var4) {
                  return true;
               } else {
                  AliceObf_dS var5 = this.aiY();
                  if (var5 != null) {
                     if (this.aiZ(var5.agY())) {

                     } else {
                        short var13 = 9957;
                     }
                  }

                  this.aji(true, true);
                  return false;
               }
            } else {
               this.aji(true, true);
               return false;
            }
         } else {
            this.aji(true, false);
            return false;
         }
      } else {
         this.aji(true, true);
         return false;
      }
   }

   private boolean aiV() {
      return (this.PB != null
            && this.PB.pK
            && !this.PB.Rc
            && this.nw != null
            && this.nw.equals(this.PB.axl)
            && this.aiZ(this.PB.axl)
            && this.ahY(this.PB.axl));
   }

   private boolean aiW() {
      return (this.PB != null
            && !this.PB.pK
            && this.PB.Rc
            && this.nw != null
            && this.nw.equals(this.PB.axl)
            && this.aiZ(this.PB.axl)
            && this.ahY(this.PB.axl));
   }

   private boolean aiX(int var1) {
      if (this.PB == null || super.mc.player == null) {
         return false;
      } else if (!this.ajg(var1, this.PB.axl)) {
         this.aji(true, true);
         return false;
      } else {
         this.al = var1;
         this.PB.pK = false;
         this.PB.aho();
         if (this.PB.Rc) {
            this.UL = AliceObf_eJ.aKv;
            this.ff = System.nanoTime() + (300000000);
            return true;
         } else {
            this.aji(true, true);
            return false;
         }
      }
   }

   private AliceObf_dS aiY() {
      Iterator var1 = this.ED.iterator();
      if (!var1.hasNext()) {

      } else {
         short var3 = 31005;
      }

      return null;
   }

   private boolean aiZ(BlockPos var1) {
      return (boolean)(var1 != null && super.mc.world != null && BlockUtils.canBreak(var1, super.mc.world.getBlockState(var1)));
   }

   private boolean aja(BlockPos var1, BlockState var2, int var3) {
      if (var1 != null && var2 != null) {
         if (var2.isAir()) {

         } else {
            char var7 = 62435;
         }
      }

      return false;
   }

   private int ajb(BlockPos var1, BlockState var2, int var3) {
      if (var1 != null && var2 != null && var3 >= 0 && var3 <= (8)) {
         double var4 = this.getBreakingDeltaForSlot(var1, var2, var3);
         if (Double.isFinite(var4) && !(var4 <= 1.0E-4) && !(var4 >= 0.9999)) {
            int var6 = this.ajc(var4);
            return var6 >= 0 ? Math.max(2, var6) : (-1);
         } else {
            return (-1);
         }
      } else {
         return (-1);
      }
   }

   private int ajc(double var1) {
      if (Double.isFinite(var1) && !(var1 <= 0.0)) {
         double var3 = Math.ceil(1.0 / var1);
         return Double.isFinite(var3) && !(var3 > 2.147483647E9) ? Math.max(1, (int)var3) : (-1);
      } else {
         return -1;
      }
   }

   private int ajd(BlockPos var1, BlockState var2, int var3, int var4, double var5) {
      if (var1 != null && var2 != null && super.mc.player != null) {
         int var7 = this.ajb(var1, var2, var3);
         if (var7 < 0) {
            return -1;
         } else {
            double var8 = Math.max((double)var7, var5) + 1.0;
            double var10 = var8 + this.aje() + 3.0;
            int var12 = this.ajG();
            int var13 = -1;
            double var14 = Double.POSITIVE_INFINITY;

            for (int var16 = (0); var16 < 9; var16++) {
               int var10000 = var16 != var3 ? 29134 : 29135;

               while (true) {
                  int var19 = var10000;
                  if (var19 == 29134) {
                     double var17;
                     if (var16 != var4 && !((var17 = this.getBreakingDeltaForSlot(var1, var2, var16)) * var8 >= 0.6999) && !(var17 * var10 >= 0.9999)) {
                        if (!(var17 < var14 - 1.0E-4)) {
                           short var20 = 18540;
                        } else {
                           short var21 = 18541;
                        }

                        var13 = var16;
                        var14 = var17;
                     }
                     break;
                  }

                  if (var19 != 29135) {
                     break;
                  }

               }
            }

            return var13;
         }
      } else {
         return (-1);
      }
   }

   private int aje() {
      return (int)Math.ceil(6.0);
   }

   private int ajf(BlockPos var1, BlockState var2) {
      if (super.mc.player != null && var2 != null && !var2.isAir()) {
         int var3 = -1;
         double var4 = 0.0;

         for (int var6 = (0); var6 < (9); var6++) {
            double var7 = this.getBreakingDeltaForSlot(var1, var2, var6);
            if (var7 > var4) {
               var4 = var7;
               var3 = var6;
            }
         }

         return var3;
      } else {
         return -1;
      }
   }

   private double getBreakingDeltaForSlot(BlockPos var1, BlockState var2, int var3) {
      return var3 >= 0 && var3 <= 8 && var2 != null && !var2.isAir()
         ? this.getBreakDelta(this.getBlockBreakingSpeed(var3, var2), var2, var1, var3)
         : 0.0;
   }

   private boolean ajg(int var1, BlockPos var2) {
      if (super.mc.player != null && super.mc.getNetworkHandler() != null && var1 >= 0 && var1 <= 8) {
         int var3 = AliceObf_fS.OL();
         if (var3 >= 0 && var3 <= (8)) {
            if (this.abr && var3 != this.aty) {
               this.ajh(false);
            }

            int var4 = this.abr ? this.avd : var3;
            if (var3 != var1) {
               if (!this.aiC(var1)) {

               } else {
                  short var6 = 1500;
               }
            }

            this.avd = var4;
            this.aty = var1;
            this.abr = !(AliceObf_fS.OL() == var4
                  && !this.abr);
            int var7 = var2 != null ? 8163 : 8164;

            while (true) {
               int var5 = var7;
               if (var5 == 8163) {
                  this.Wr.add(var2.toImmutable());
                  break;
               }

               if (var5 != 8164) {
                  break;
               }

               var7 = 8162;
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void ajh(boolean var1) {
      if (this.abr) {
         int var2 = AliceObf_fS.OL();
         if (var2 >= 0 && var2 <= (8)) {
            if (var2 != this.aty) {
               this.abr = false;
               this.avd = -1;
               this.aty = -1;
               this.Tg = false;
            } else if (var1 && !this.Wr.isEmpty()) {
               this.Tg = true;
            } else {
               if (var1 && this.avd >= 0 && this.avd <= (8)) {
                  if (super.mc.getNetworkHandler() != null) {

                  } else {
                     char var3 = 39745;
                  }
               }

               this.abr = false;
               this.avd = -1;
               this.aty = (-1);
               this.Tg = false;
            }
         }
      }
   }

   private void aji(boolean var1, boolean var2) {
      if (var2) {
         this.ED.clear();
      }

      this.ajj(var1);
   }

   private void ajj(boolean var1) {
      if (var1) {
         this.ajh(true);
      }

      this.UL = AliceObf_eJ.aHp;
      this.nw = null;
      this.Y = (-1);
      this.al = (-1);
      this.MT = (-1);
      this.ff = 0L;
   }

   private Direction ajk(BlockPos var1, Direction var2) {
      if (super.mc.player != null && var1 != null) {
         return AliceObf_rZ.agB(
            super.mc.player.getEyePos(), var1
         );
      } else if (var2 != null) {
         return var2;
      } else if (super.mc.player != null && var1 != null) {
         Vec3d var3 = Vec3d.ofCenter(var1);
         Vec3d var4 = super.mc.player.getEyePos();
         Direction var5 = Direction.getFacing(var4.x - var3.x, var4.y - var3.y, var4.z - var3.z);
         return var5 == null ? Direction.UP : var5;
      } else {
         return Direction.UP;
      }
   }

   private boolean ajl(Action var1, BlockPos var2, Direction var3) {
      return this.ajm(var1, var2, var3, true);
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean ajm(Action var1, BlockPos var2, Direction var3, boolean var4) {
      if (super.mc.getNetworkHandler() == null || super.mc.world == null || var2 == null) {
         return false;
      } else if (!AliceObf_fS.OO()) {
         int var5;
         try {
            var5 = this.getSeq();
         } catch (Throwable var14) {
            return false;
         }

         Direction var6 = var3 == null ? Direction.DOWN : var3;
         PlayerActionC2SPacket var7 = new PlayerActionC2SPacket(var1, var2, var6, var5);
         this.aAz = true;
         boolean var12 = false /* VF: Semaphore variable */;

         boolean var8;
         try {
            var12 = true;
            var8 = AliceObf_fS.PF(
               var7, () -> super.mc.getNetworkHandler().sendPacket(var7)
            );
            var12 = false;
         } finally {
            if (var12) {
               this.aAz = false;
            }
         }

         this.aAz = false;
         if (!var8) {
            return false;
         } else {
            this.aiI(var1, System.nanoTime());
            return true;
         }
      } else {
         return false;
      }
   }

   private boolean ajn(BlockPos var1, Direction var2) {
      if (super.mc.getNetworkHandler() == null || var1 == null) {
         return false;
      } else if (AliceObf_fS.OO()) {
         return false;
      } else {
         PlayerActionC2SPacket var3 = new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, var1, Direction.DOWN);
         return AliceObf_fS.PF(
            var3, () -> super.mc.getNetworkHandler().sendPacket(var3)
         );
      }
   }

   private boolean ajo(BlockPos var1, Direction var2) {
      Direction var3 = this.ajk(var1, var2);
      BlockPos var4 = var1.up((1420));
      long var5 = System.nanoTime();
      if (!this.ajl(Action.START_DESTROY_BLOCK, var1, var3)) {
         return false;
      } else if (this.ajl(Action.START_DESTROY_BLOCK, var4, Direction.DOWN) && this.ajl(Action.STOP_DESTROY_BLOCK, var1, var3)) {
         return true;
      } else {
         if (!this.ajn(var1, var3)) {
            this.JO.put(var1.toImmutable(), new AliceObf_sk(var5, var5, var5, (3)));
         } else {
            this.JO.remove(var1);
         }

         return false;
      }
   }

   private boolean ajp(BlockPos var1, BlockState var2) {
      return (this.getBestBreakingDelta(var1, var2) >= 0.9999);
   }

   private double getBestBreakingDelta(BlockPos var1, BlockState var2) {
      if (super.mc.player != null && super.mc.world != null && var1 != null && var2 != null) {
         if (!var2.isAir()) {

         } else {
            short var5 = 6617;
         }

         FindItemResult var3 = InvUtils.findFastestTool(var2);
         int var4 = this.xd.get() && var3.found()
            ? var3.slot()
            : AliceObf_fS.OL();
         return var4 >= 0 && var4 <= 8 ? this.getBreakDelta(this.getBlockBreakingSpeed(var4, var2), var2, var1, var4) : 0.0;
      } else {
         return 0.0;
      }
   }

   private boolean ajq(BlockPos var1, Direction var2, BlockState var3) {
      if (var1 != null && var3 != null && BlockUtils.canBreak(var1, var3) && this.ahY(var1)) {
         FindItemResult var4 = InvUtils.findFastestTool(var3);
         this.aiz(var4, true, var1);
         int var10000 = this.aiA(var4) ? 34849 : 34848;

         while (true) {
            int var8 = var10000;
            if (var8 == 34848) {
               return false;
            }

            if (var8 != 34849) {
               MeteorClient.EVENT_BUS.post(new AliceObf_cw(var1, true));
               long var5 = System.nanoTime();
               boolean var7 = this.ajm(Action.START_DESTROY_BLOCK, var1, this.ajk(var1, var2), false);
               if (!var7) {
                  this.Wr.remove(var1.toImmutable());
               } else {
                  this.Kk.add(var1.toImmutable());
                  this.aiN(var1, 0.0, true, var5);
                  this.ajr(var1, AliceObf_fS.OL());
               }

               this.aiE(false);
               return var7;
            }

         }
      } else {
         return false;
      }
   }

   private void ajr(BlockPos var1, int var2) {
      if (!(var1 == null)) {
         short var9 = 32521;
      }

      if (!super.mc.isOnThread()) {
         BlockPos var8 = var1.toImmutable();
         super.mc.execute(() -> this.ajr(var8, var2));
      } else if (super.mc.player != null && super.mc.world != null) {
         BlockState var4 = super.mc.world.getBlockState(var1);
         if (!var4.isAir()) {
            ItemStack var3 = var2 >= 0 && var2 <= (8)
               ? super.mc.player.getInventory().getStack(var2)
               : super.mc.player.getMainHandStack();
            if (!var4.isToolRequired() || var3.isSuitableFor(var4)) {
               Block var6 = var4.getBlock();
               if (!(var6 instanceof OperatorBlock) || super.mc.player.isCreativeLevelTwoOp()) {
                  var6.onBreak(super.mc.world, var1, var4, super.mc.player);
                  FluidState var7 = super.mc.world.getFluidState(var1);
                  if (super.mc.world.setBlockState(var1, var7.getBlockState(), (11))) {
                     var6.onBroken(super.mc.world, var1, var4);
                     this.ajt(var1, var4);
                  }
               }
            }
         }
      }
   }

   private boolean ajs(BlockPos var1) {
      if (var1 == null) {
         return false;
      } else if (!super.mc.isOnThread()) {
         BlockPos var5 = var1.toImmutable();
         super.mc.execute(() -> this.ajs(var5));
         return true;
      } else if (super.mc.world == null) {
         return false;
      } else {
         BlockState var2 = super.mc.world.getBlockState(var1);
         if (var2.isAir()) {
            return true;
         } else {
            BlockState var3 = super.mc.world.getFluidState(var1).getBlockState();
            boolean var4 = super.mc.world.setBlockState(var1, var3, 11);
            if (var4) {
               this.ajt(var1, var2);
            }

            return var4;
         }
      }
   }

   private void ajt(BlockPos var1, BlockState var2) {
      if (super.mc.world != null && var1 != null) {
         if (!(var2 == null)) {
            short var6 = 30906;
         }

         if (this.aju(var2) && var2.contains(Properties.FACING)) {
            if (!var2.contains(Properties.EXTENDED) || (Boolean)var2.get(Properties.EXTENDED)) {
               BlockPos var3 = var1.offset((Direction)var2.get(Properties.FACING));
               BlockState var4 = super.mc.world.getBlockState(var3);
               if (var4.isOf(Blocks.PISTON_HEAD) || var4.isOf(Blocks.MOVING_PISTON)) {
                  BlockState var5 = super.mc.world.getFluidState(var3).getBlockState();
                  super.mc.world.setBlockState(var3, var5, 11);
               }
            }
         }
      }
   }

   private boolean aju(BlockState var1) {
      return (boolean)(var1 == null
            || !var1.isOf(Blocks.PISTON) && !var1.isOf(Blocks.STICKY_PISTON) && !var1.isOf(Blocks.MOVING_PISTON));
   }

   @AliceHandler
   private void ajv(Render3DEvent var1) {
      int var10000 = !this.MQ.get() ? 61052 : 61053;

      while (true) {
         int var7 = var10000;
         if (var7 == 61052) {
            return;
         }

         if (var7 != 61053) {
            double var2 = SpeedMinePlusPlus.getCurrentGameTickCalculated();
            if ((Boolean)this.asB.get()) {
               short var9 = 15858;
            } else {
               short var10 = 15859;
            }

            this.ajw(var1);
            return;
         }

      }
   }

   private void ajw(Render3DEvent var1) {
      if ((Boolean)this.aqj.get() && !this.Tf.isEmpty()) {
         Color var2 = (Color)this.aov.get();
         Color var3 = (Color)this.aKp.get();

         for (AliceObf_lY var5 : this.Tf) {
            double var6 = this.ajC(var5, var1.tickDelta);
            if (!(var6 <= 0.0)) {
               double var8 = (1.0 - var6) * 0.16;
               Box var10 = new Box(var5.ahh()).expand(var8);
               var1.renderer
                  .box(
                     var10,
                     SpeedMinePlusPlus.ajE(var2, var6),
                     SpeedMinePlusPlus.ajE(var3, var6),
                     (ShapeMode)this.ati.get(),
                     (0)
                  );
            }
         }
      }
   }

   @AliceHandler
   private void ajx(Render2DEvent var1) {
      if ((Boolean)this.MQ.get() && (Boolean)this.gd.get()) {
         if (!(super.mc.world == null)) {
            char var3 = 51938;
         }

         if (this.QR != null) {
            this.ajz(this.QR.arQ.agY(), this.QR.aGV.ZD(System.nanoTime()));
         } else {
            AliceObf_bn var2 = this.PB;
            if (var2 != null && var2.axl != null && !super.mc.world.getBlockState(var2.axl).isAir()) {
               this.ajy(var2);
            }
         }
      }
   }

   private void ajy(AliceObf_bn var1) {
      this.ajz(var1.axl, var1.getBreakProgress());
   }

   private void ajz(BlockPos var1, double var2) {
      Vec3d var4 = Vec3d.ofCenter(var1);
      this.bI.set(var4.x, var4.y, var4.z);
      if (NametagUtils.to2D(this.bI, 1.0)) {
         TextRenderer var5 = TextRenderer.get();
         if (var5 != null) {
            int var6 = (int)Math.round(MathHelper.clamp(var2, 0.0, 1.0) * 100.0);
            String var7 = var6 + "";
            Color var8 = SpeedMinePlusPlus.ajD((Color)this.atV.get());
            NametagUtils.begin(this.bI);
            var5.begin(1.0, false, true);
            double var9 = var5.getWidth(var7, true);
            double var11 = var5.getHeight(true);
            var5.render(var7, -var9 / 2.0, -var11 / 2.0, var8, true);
            var5.end();
            NametagUtils.end();
         }
      }
   }

   private void ajA(BlockPos var1) {
      if ((Boolean)this.aqj.get() && var1 != null) {
         AliceObf_lY var2 = (AliceObf_lY)this.Tf.peekLast();
         if (var2 == null || var2.ahi() != this.en || !var2.ahh().equals(var1)) {
            while (this.Tf.size() >= (8)) {
               this.Tf.removeFirst();
            }

            this.Tf.addLast(new AliceObf_lY(var1.toImmutable(), this.en));
         }
      }
   }

   private void ajB() {
      if (!(Boolean)this.aqj.get()) {

      } else {
         char var1 = 40882;
      }

      this.Tf.removeIf(var1x -> !(this.en - var1x.ahi() <= (6)));
   }

   private double ajC(AliceObf_lY var1, float var2) {
      double var3 = Math.max(0.0, (double)(this.en - var1.ahi()) + var2);
      return MathHelper.clamp(1.0 - var3 / 6.0, 0.0, 1.0);
   }

   private static Color ajD(Color var0) {
      return var0.a >= (255) ? var0 : new Color(var0.r, var0.g, var0.b, (255));
   }

   private static Color ajE(Color var0, double var1) {
      return !(var1 >= 0.999)
         ? new Color(
            var0.r, var0.g, var0.b, MathHelper.clamp((int)Math.round(var0.a * var1), (0), (255))
         )
         : var0;
   }

   @AliceHandler
   private void ajF(Send var1) {
      Packet var3 = var1.packet;
      PlayerActionC2SPacket var2;
      if (var3 instanceof PlayerActionC2SPacket
         && (var2 = (PlayerActionC2SPacket)var3).getAction() == Action.STOP_DESTROY_BLOCK
         && (Boolean)this.aJT.get()
         && !this.aAz
         && (var2.getPos().equals(this.getRebreakBlockPos()) || var2.getPos().equals(this.getDelayedDestroyBlockPos()))) {
         this.ajn(var2.getPos(), Direction.DOWN);
      }
   }

   private static double getCurrentGameTickCalculated() {
      return (double)(System.nanoTime() - SpeedMinePlusPlus.ZO)
         / TimeUnit.MILLISECONDS.toNanos(50L);
   }

   private double getBreakDelta(double var1, BlockState var3, BlockPos var4, int var5) {
      float var6 = var3.getHardness(super.mc.world, var4);
      if (var6 == -1.0F) {
         return 0.0;
      } else {
         int var7 = var3.isToolRequired() && !super.mc.player.getInventory().getStack(var5).isSuitableFor(var3)
            ? (0)
            : (1);
         return var1 / var6 / (var7 == 0 ? 100.0 : 30.0);
      }
   }

   private double getBlockBreakingSpeed(int var1, BlockState var2) {
      double var5 = super.mc.player.getInventory().getStack(var1).getMiningSpeedMultiplier(var2);
      ItemStack var3;
      int var4;
      if (var5 > 1.0
         && (var4 = Utils.getEnchantmentLevel(var3 = super.mc.player.getInventory().getStack(var1), Enchantments.EFFICIENCY)) > 0
         && !var3.isEmpty()) {
         var5 += var4 * var4 + 1;
      }

      if (StatusEffectUtil.hasHaste(super.mc.player)) {
         var5 *= 1.0F + (StatusEffectUtil.getHasteAmplifier(super.mc.player) + 1) * 0.2F;
      }

      if (super.mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
         float var7 = switch (super.mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
            case 0 -> 0.3F;
            case 1 -> 0.09F;
            case 2 -> 0.0027F;
            default -> 8.1E-4F;
         };
         var5 *= var7;
      }

      if (super.mc.player.isSubmergedIn(FluidTags.WATER)) {
         var5 *= super.mc.player.getAttributeValue(EntityAttributes.SUBMERGED_MINING_SPEED);
      }

      if (!super.mc.player.isOnGround()) {
         var5 /= 5.0;
      }

      return var5;
   }

   private int ajG() {
      return super.mc.player.getInventory().getSelectedSlot();
   }

   private int getSeq() throws Throwable {
      if (SpeedMinePlusPlus.QZ != null && super.mc.world != null) {
         PendingUpdateManager var1 = (PendingUpdateManager)SpeedMinePlusPlus.QZ
            .invoke((ClientWorld)super.mc.world);
         return var1.incrementSequence().getSequence();
      } else {
         throw new IllegalStateException(
            "Pending update manager is unavailable"
         );
      }
   }

   private static MethodHandle ajH() {
      Method[] var0 = ClientWorld.class.getDeclaredMethods();
      int var1 = var0.length;

      for (int var2 = (0); var2 < var1; var2++) {
         Method var3 = var0[var2];
         Class[] var4 = var3.getParameterTypes();
         if (var3.getReturnType() != PendingUpdateManager.class) {

         } else {
            char var6 = 35595;
         }
      }

      return null;
   }

   }

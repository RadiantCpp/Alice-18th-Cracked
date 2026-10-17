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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.BlockUpdateEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.gui.screens.ModuleScreen;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Direction.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BaseDiggerPlusPlus extends AliceModule {
   private static final Logger Aj = LoggerFactory.getLogger("alice/base-digger-plus-plus");
   private static final long aCs = 650L;
   private static final SettingColor aQM = new SettingColor((48), 54, (216), 255);
   private static final SettingColor iZ = new SettingColor(224, (79), (225), 255);
   private static final int vx = 3;
   private static final int aiW = 200;
   private static final int xN = -1;
   private final SettingGroup FF = this.settings.getDefaultGroup();
   private final SettingGroup ht;
   private final SettingGroup aww;
   private final SettingGroup azI;
   private final Setting<AliceObf_uu> aaw;
   private final Setting<Integer> apa;
   private final Setting<Integer> lP;
   private final Setting<Integer> sN;
   private final Setting<Keybind> aPs;
   private final Setting<Boolean> qo;
   private final Setting<Boolean> AF;
   private final Setting<Double> dJ;
   private final Setting<Double> asg;
   private final Setting<Boolean> arh;
   private final Setting<ShapeMode> abs;
   private final Setting<SettingColor> SN;
   private final Setting<SettingColor> aFT;
   private final Setting<Boolean> aFe;
   private final Setting<Integer> aqK;
   private BlockPos ahR;
   private ClientWorld UZ;
   private boolean Td;
   private AliceObf_hn aJG;
   private ClientWorld abE;
   private Entity lC;
   private Perspective agf;
   private boolean Dk;
   private boolean kO;
   private double air;
   private long bJ;
   private float ng;
   private long UR;
   private float iT;
   private float aQy;
   private final AliceObf_sq wK;
   private Boolean aFK;
   private Boolean Et;
   private Boolean aMw;
   private BlockPos aaS;
   private BlockPos amK;
   private Direction ES;
   private AliceObf_kJ IR;
   private List<AliceObf_ow> sR;
   private List<BlockPos> aMn;
   private List<AliceObf_qt> aFX;
   private BlockPos qW;
   private List<BlockPos> arO;
   private AliceObf_eL apW;
   private int tF;
   private int bS;
   private int Op;
   private int asZ;
   private BlockPos Yk;
   private int GP;
   private int dv;
   private final AliceObf_eu aEN;
   private final AliceObf_eu aik;
   private int Sv;
   private boolean iL;
   private boolean ahA;
   private int PX;
   private int Bj;
   private BlockPos aAU;
   private AliceObf_kJ Cm;
   private List<BlockPos> yb;
   private int aaZ;
   private boolean fO;
   private int c;
   private int xy;
   private int ayN;
   private AliceObf_iY acE;
   private boolean Kz;
   public BaseDiggerPlusPlus() {
      super(AliceAddon.CATEGORY, "BaseDigger++", "Xem truoc vung roi dung Baritone de dao - port tu astatine BaseDigger, fix 4 loi ton tai.");
      this.ht = this.settings.createGroup("Camera");
      this.aww = this.settings.createGroup("Render");
      this.azI = this.settings.createGroup("Fixes");
      this.aaw = this.FF
         .add(((Builder)((Builder)((Builder)new Builder().name("pickaxe-mode")).description("Che do dao: 1x1 ziczac (Baritone chi di goal tung o), 3x3 tunnel + snake route.")).defaultValue(AliceObf_uu.aLZ)).build());
      this.apa = this.FF
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("size-x"))
                     .description("Chieu rong X cua box."))
                  .defaultValue((9)))
               .min(1)
               .max(300)
               .sliderRange(1, (64))
               .build()
         );
      this.lP = this.FF
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("size-y"))
                     .description("Chieu cao Y cua box."))
                  .defaultValue((6)))
               .min(1)
               .max((64))
               .sliderRange((1), 32)
               .build()
         );
      this.sN = this.FF
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("size-z"))
                     .description("Chieu dai Z cua box."))
                  .defaultValue((9)))
               .min(1)
               .max(300)
               .sliderRange((1), 64)
               .build()
         );
      this.aPs = this.FF
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("reset-position"))
                     .description("Bam de reset vi tri neo cua box ve player hien tai."))
                  .defaultValue(Keybind.none()))
               .action(this::qc)
               .build()
         );
      this.qo = this.FF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("reset-clear"))
                        .description("Bam de dat lai vung tai player va xoa tien trinh dao. Tu tat sau khi bam."))
                     .defaultValue(false))
                  .onChanged(var1 -> {
                     int var10000 = var1 ? 30438 : 30439;

                     while (true) {
                        int var2 = var10000;
                        if (var2 == 30438) {
                           this.qc();
                           break;
                        }

                        if (var2 != 30439) {
                           break;
                        }

                     }
                  }))
               .build()
         );
      this.AF = this.ht
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("auto-orbit"))
                     .description("Tu dong xoay camera quanh box khi preview."))
                  .defaultValue(true))
               .build()
         );
      this.dJ = this.ht
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("orbit-speed"))
                     .description("Toc do xoay camera (do/giay)."))
                  .defaultValue(22.0)
                  .min(2.0)
                  .max(90.0)
                  .sliderRange(2.0, 60.0)
                  .visible(this.AF::get))
               .build()
         );
      this.asg = this.ht
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("camera-distance"))
                  .description("Khoang cach camera toi tam box."))
               .defaultValue(14.0)
               .min(4.0)
               .max(64.0)
               .sliderRange(4.0, 32.0)
               .build()
         );
      this.arh = this.aww
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("render-box"))
                     .description("Hien thi box dao."))
                  .defaultValue(true))
               .build()
         );
      this.abs = this.aww
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("shape-mode")).description("Kieu render box."))
                     .defaultValue(ShapeMode.Both))
                  .visible(this.arh::get))
               .build()
         );
      this.SN = this.aww
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("side-color"))
                     .description("Mau fill cua box."))
                  .defaultValue(new SettingColor((48), (54), 216, (45)))
                  .visible(() -> (this.arh.get() && ((ShapeMode)this.abs.get()).sides())))
               .build()
         );
      this.aFT = this.aww
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("line-color"))
                     .description("Mau vien cua box."))
                  .defaultValue(new SettingColor((224), (79), (225), 255))
                  .visible(() -> (this.arh.get() && ((ShapeMode)this.abs.get()).lines())))
               .build()
         );
      this.aFe = this.azI
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("handle-gravel"))
                     .description("Xu ly cat, soi va block trong luc roi lap duong; doi block roi on dinh truoc khi ket thuc tang."))
                  .defaultValue(true))
               .build()
         );
      this.aqK = this.azI
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("durability-threshold"))
                     .description("Dung dao khi do ben cup con lai <= gia tri nay (de sua bang Mending)."))
                  .defaultValue(30))
               .min(1)
               .max((150))
               .sliderRange(1, (100))
               .build()
         );
      this.wK = new AliceObf_sq();
      this.sR = List.of();
      this.aMn = List.of();
      this.aFX = List.of();
      this.arO = List.of();
      this.apW = AliceObf_eL.dC;
      this.aEN = new AliceObf_eu();
      this.aik = new AliceObf_eu();
      this.yb = List.of();
      this.acE = AliceObf_iY.Pn;
      MeteorClient.EVENT_BUS.subscribe(new AliceObf_sz(this));
      MeteorClient.EVENT_BUS.subscribe(new AliceObf_aH(this));
   }

   private boolean pV(Screen var1) {
      if (!(var1 instanceof ModuleScreen)) {
         return false;
      } else {
         try {
            Field var2 = ModuleScreen.class
               .getDeclaredField(
                  "module"
               );
            var2.setAccessible(true);
            return !(var2.get(var1) == this);
         } catch (ReflectiveOperationException var4) {
            String var3 = var1.getTitle().getString();
            return (boolean)(!var3.contains("Base Digger")
                  && !var3.contains("BaseDigger"));
         }
      }
   }

   public void onActivate() {
      int var10000 = !PremiumAccessGate.isAllowed() ? 37464 : 37465;

      while (true) {
         int var1 = var10000;
         if (var1 == 37464) {
            return;
         }

         if (var1 != 37465) {
            if (super.mc.player != null) {
               var10000 = super.mc.world == null ? 2436 : 2437;

               while (true) {
                  int var2 = var10000;
                  if (var2 == 2436) {
                     break;
                  }

                  if (var2 != 2437) {
                     if (this.ahR == null || this.UZ != super.mc.world) {
                        this.qb();
                     }

                     this.Td = true;
                     super.onActivate();
                     this.qj();
                     return;
                  }

               }
            }

            this.toggle();
            return;
         }

      }
   }

   public void onDeactivate() {
      super.onDeactivate();
      this.rs();
      this.Kz = false;
      this.rv();
   }

   public static void pW(BaseDiggerPlusPlus var0) {
      if (!(var0 == null)) {
         char var2 = 34712;
      }

      MinecraftClient var1 = var0.mc;
      if (var1.player != null && var1.world != null) {
         if (var0.ahR != null) {
            if (!(var0.UZ != var1.world)) {
               char var3 = 44261;
            }
         } else {
            var0.qb();
         }

         var0.Td = true;
         var0.kO = false;
         if (var0.aJG != null && var0.abE == var1.world) {
            var1.options.setPerspective(Perspective.FIRST_PERSON);
            var1.setCameraEntity(var0.aJG);
         } else {
            var0.rv();
            var0.lC = var1.getCameraEntity();
            var0.agf = var1.options.getPerspective();
            var0.abE = var1.world;
            var0.aJG = new AliceObf_hn(var1.world, var1.player.getGameProfile());
            var0.aJG.copyPositionAndRotation(var1.player);
            var0.aJG.setHeadYaw(var1.player.getHeadYaw());
            var1.world.addEntity(var0.aJG);
            var1.options.setPerspective(Perspective.FIRST_PERSON);
            var1.setCameraEntity(var0.aJG);
            var0.qd(0.0F);
         }

         var0.air = var1.player.getYaw() + 135.0;
         var0.bJ = System.nanoTime();
         var0.Dk = true;
         var0.qf(1.0F);
      }
   }

   public static void pX(BaseDiggerPlusPlus var0) {
      if (var0 != null && var0.Dk) {
         var0.kO = true;
         var0.qf(0.0F);
      }
   }

   public static void pY(BaseDiggerPlusPlus var0) {
      if (var0 != null && var0.Dk) {
         var0.ru();
      }
   }

   public static void pZ(BaseDiggerPlusPlus var0) {
      if (var0 != null) {
         var0.rv();
      }
   }

   public static boolean qa(BaseDiggerPlusPlus var0) {
      if (var0 != null) {
         if (!(!var0.Dk)) {
            char var1 = 42011;
         }
      }

      return false;
   }

   private void qb() {
      this.ahR = super.mc.player.getBlockPos().toImmutable();
      this.UZ = super.mc.world;
   }

   private void qc() {
      if (this.qo != null && (Boolean)this.qo.get()) {
         this.qo.set(false);
      }

      if (super.mc.player != null && super.mc.world != null) {
         this.rs();
         this.qb();
         this.Td = true;
         if (this.isActive()) {
            this.qj();
         }
      }
   }

   private void qd(float var1) {
      this.ng = MathHelper.clamp(var1, 0.0F, 1.0F);
      this.iT = this.ng;
      this.aQy = this.ng;
      this.UR = System.nanoTime();
   }

   private void qe() {
      if (this.Dk) {
         this.kO = true;
         this.qf(0.0F);
      }
   }

   private void qf(float var1) {
      this.iT = this.ng;
      this.aQy = MathHelper.clamp(var1, 0.0F, 1.0F);
      this.UR = System.nanoTime();
   }

   private float qg() {
      long var1 = System.nanoTime() - this.UR;
      float var3 = MathHelper.clamp((float)var1 / 6.5E8F, 0.0F, 1.0F);
      float var4 = 1.0F - (1.0F - var3) * (1.0F - var3) * (1.0F - var3);
      float var5 = MathHelper.lerp(var4, this.iT, this.aQy);
      this.ng = var5;
      return var5;
   }

   @AliceHandler
   private void qh(Pre var1) {
      if (super.mc.player != null) {
         if (!(super.mc.world != null)) {
            short var5 = 24054;
         }
      }
   }

   private void qi(Render3DEvent var1) {
      if ((Boolean)this.arh.get() && super.mc.world != null) {
         if (this.Td || this.isActive()) {
            if (this.UZ == super.mc.world) {
               AliceObf_kJ var2 = this.rt();
               if (var2 != null) {
                  BlockPos var3 = var2.pC();
                  BlockPos var4 = var2.pD();
                  Box var5 = new Box(
                     var3.getX(),
                     var3.getY(),
                     var3.getZ(),
                     var4.getX() + 1.0,
                     var4.getY() + 1.0,
                     var4.getZ() + 1.0
                  );
                  var1.renderer.box(var5, (Color)this.SN.get(), (Color)this.aFT.get(), (ShapeMode)this.abs.get(), (0));
               }
            }
         }
      }
   }

   private void qj() {
      AliceObf_kJ var1 = this.rt();
      if (super.mc.player != null && super.mc.world != null && var1 != null && this.UZ == super.mc.world) {
         if (this.aaw.get() == AliceObf_uu.hV) {
            this.rs();
            this.qk(var1, this.rr(super.mc.world, var1));
         } else if (this.wK.pm()) {
            this.rs();
            this.qI(var1);
         } else {
            this.warning(
               "Baritone khong san - cai Baritone compatible.",
               new Object[(0)]
            );
            this.rs();
            if (this.isActive()) {
               this.toggle();
            }
         }
      }
   }

   private void qk(AliceObf_kJ var1, List<AliceObf_tu> var2) {
      this.IR = var1;
      this.sR = this.rq(var1, var2);
      this.Op = 0;
      if (super.mc.player != null) {
         this.aaS = this.qV(super.mc.player.getBlockPos(), var1);
         this.ES = this.qW(this.aaS, var1);
         this.aMn = this.qX(this.aaS, this.ES, var1);
         this.aFX = List.of();
         this.tF = (0);
         this.bS = 0;
         this.Yk = null;
         this.GP = (0);
         this.apW = AliceObf_eL.axZ;
         if (this.rg()) {
            try {
               this.Et = this.wK
                  .getSettingBoolean("autoTool");
               this.aMw = this.wK
                  .getSettingBoolean(
                     "allowInventory"
                  );
               this.wK
                  .setSettingBoolean(
                     "autoTool",
                     false
                  );
               this.wK
                  .setSettingBoolean(
                     "allowInventory",
                     false
                  );
               this.wK.pr();
               if (!this.wK.setGoalAndPath(this.aaS)) {
                  throw new RuntimeException("Baritone setGoalAndPath failed");
               }

               this.amK = this.aaS.toImmutable();
               BaseDiggerPlusPlus.Aj
                  .info("Moving to 3x3 staging {} outside top edge", this.aaS);
            } catch (RuntimeException var4) {
               BaseDiggerPlusPlus.Aj
                  .error("Unable to move to 3x3 staging", var4);
               if (this.isActive()) {
                  this.toggle();
               }
            }
         } else {
            this.warning(
               "Khong tim thay cup thuong trong hotbar (can 1 cup khong phai 3x3).",
               new Object[(0)]
            );
            BaseDiggerPlusPlus.Aj
               .warn("Hotbar does not contain a normal pickaxe");
            this.toggle();
         }
      }
   }

   private void ql() {
      if (this.aaS != null) {
         if (this.rg()) {

         } else {
            short var2 = 1068;
         }

         if (this.qz(this.aaS)) {
            this.wK.po();
            BlockPos var1 = !this.aMn.isEmpty() ? (BlockPos)this.aMn.getFirst() : this.aaS.offset(this.ES);
            this.re(var1);
            this.info(
               "Bat dau dao...",
               new Object[0]
            );
            this.qm();
         }
      }
   }

   private void qm() {
      if (!this.aMn.isEmpty()) {
         this.re((BlockPos)this.aMn.getFirst());
      }

      int var10000 = !this.qn() ? 36195 : 36196;

      while (true) {
         int var1 = var10000;
         if (var1 == 36195) {
            this.qB();
            return;
         }

         if (var1 != 36196) {
            if (this.aMn.isEmpty()) {
               this.qB();
               return;
            }

            this.rf();
            this.apW = AliceObf_eL.atU;
            return;
         }

      }
   }

   private boolean qn() {
      if (this.IR != null) {
         int var10000 = this.sR.isEmpty() ? 4068 : 4069;

         while (true) {
            int var4 = var10000;
            if (var4 == 4068) {
               break;
            }

            if (var4 != 4069) {
               AliceObf_ow var1 = (AliceObf_ow)this.sR.get(this.Op);
               int var2 = this.IR.pD().getX() - this.IR.pC().getX() + (1);
               int var3 = this.IR.pD().getZ() - this.IR.pC().getZ() + 1;
               return (var1.pF() - var1.pG() + 1 == (3) && var2 >= 3 && var3 >= (3));
            }

         }
      }

      return false;
   }

   private void qo() {
      while (this.tF < this.aMn.size() && super.mc.world.getBlockState((BlockPos)this.aMn.get(this.tF)).isAir()) {
         this.tF++;
      }

      if (this.tF < this.aMn.size()) {
         this.rf();
         BlockPos var3 = (BlockPos)this.aMn.get(this.tF);
         if (this.qu(var3, this.ES.getOpposite())) {
            this.qB();
         }
      } else {
         this.qv();
         if (this.aMn.isEmpty()) {
            this.qB();
         } else {
            int var1 = Math.min((2), this.aMn.size());
            BlockPos var2 = this.aaS.offset(this.ES, var1);
            this.aFX = this.qY(var2);
            this.bS = (0);
            this.aEN.rR();
            this.apW = AliceObf_eL.MK;
         }
      }
   }

   private void qp() {
      int var1 = Math.min((2), this.aMn.size());
      BlockPos var2 = this.aaS.offset(this.ES, var1);
      if (this.qz(var2)) {
         this.bS = (0);
         this.apW = AliceObf_eL.Zh;
         BaseDiggerPlusPlus.Aj
            .info("Entered center of first 3x3x3 volume at {}", var2);
      }
   }

   private void qq() {
      int var1 = this.aEN
         .rQ(this.bS, this.aFX.size(), var1x -> (this.qr((AliceObf_qt)this.aFX.get(var1x)) != null));
      if (var1 == (-2)) {
         this.qA();
      } else if (var1 == -1) {
         this.qB();
      } else {
         AliceObf_qt var2 = (AliceObf_qt)this.aFX.get(var1);
         BlockPos var3 = this.qr(var2);
         if (var3 != null) {
            if (this.qt(var3, this.IR, var2.pP().getY())) {
               this.bS = var1;
               if (!var3.equals(var2.pP().up())) {
                  if (!this.rg()) {
                     this.toggle();
                     return;
                  }
               } else {
                  this.rf();
               }

               if (this.qu(var3, this.qU(var3))) {
                  this.qB();
               }
            }
         }
      }
   }

   private BlockPos qr(AliceObf_qt var1) {
      BlockPos var2 = var1.pP();
      if (this.qs(var2.up())) {
         return var2.up();
      } else if (!this.qs(var2)) {
         if (this.qs(var2.up((2)))) {

         } else {
            char var3 = 39223;
         }

         return null;
      } else {
         return var2;
      }
   }

   private boolean qs(BlockPos var1) {
      return (super.mc.world != null && !super.mc.world.getBlockState(var1).isAir());
   }

   private boolean qt(BlockPos var1, AliceObf_kJ var2, int var3) {
      if (super.mc.player.canInteractWithBlockAt(var1, 0.0)) {
         this.Bj = 0;
         this.aAU = null;
         this.qA();
         return true;
      } else {
         BlockPos var4 = super.mc.player.getBlockPos();
         if (var4.equals(this.aAU)) {
            this.Bj += (1);
         } else {
            this.Bj = (0);
         }

         this.aAU = var4.toImmutable();
         if (this.Bj >= 200) {
            this.warning(("" + var1.toShortString()), new Object[0]);
            this.toggle();
            return false;
         } else {
            BlockPos var5 = null;
            double var6 = Double.POSITIVE_INFINITY;

            for (Direction var9 : Type.HORIZONTAL) {
               BlockPos var10 = new BlockPos(var1.getX(), var3, var1.getZ()).offset(var9);
               if (this.rd(var10, var2)
                  && super.mc.world.getBlockState(var10).isAir()
                  && super.mc.world.getBlockState(var10.up()).isAir()) {
                  double var11 = var10.getSquaredDistance(var4);
                  if (var11 < var6) {
                     var5 = var10;
                     var6 = var11;
                  }
               }
            }

            int var13 = var1.getX() - var4.getX();
            int var14 = var1.getZ() - var4.getZ();
            BlockPos var15 = null;
            if (var13 != 0 || var14 != 0) {
               Direction var16 = Math.abs(var13) <= Math.abs(var14)
                  ? (var14 > 0 ? Direction.SOUTH : Direction.NORTH)
                  : (var13 > 0 ? Direction.EAST : Direction.WEST);
               BlockPos var12 = new BlockPos(var4.getX(), var3, var4.getZ()).offset(var16);
               if (this.rd(var12, var2)) {
                  var15 = var12;
               }
            }

            if (var15 != null) {
               if (this.qs(var15)) {

               } else {
                  char var19 = 56417;
               }

               BlockPos var17 = this.qs(var15.up()) ? var15.up() : null;
               if (var17 != null && super.mc.player.canInteractWithBlockAt(var17, 0.0)) {
                  if (!this.rg() || this.qu(var17, this.qU(var17))) {
                     this.warning(
                        "Khong the mo duong toi muc tieu - dung dao.",
                        new Object[0]
                     );
                     this.toggle();
                  }

                  return false;
               }
            }

            BlockPos var18 = var5 != null ? var5 : var15;
            if (var18 != null) {
               this.qv();
               this.qz(var18);
            }

            return false;
         }
      }
   }

   private boolean qu(BlockPos var1, Direction var2) {
      this.qA();
      if (!super.mc.player.canInteractWithBlockAt(var1, 0.0)) {
         return true;
      } else {
         int var10000 = !var1.equals(this.Yk) ? 7223 : 7224;

         while (true) {
            int var3 = var10000;
            if (var3 == 7223) {
               this.qv();
               this.Yk = var1.toImmutable();
               break;
            }

            if (var3 != 7224) {
               break;
            }

         }

         this.re(var1);
         if (this.GP == 0) {
            super.mc.interactionManager.attackBlock(var1, var2);
         } else {
            super.mc.interactionManager.updateBlockBreakingProgress(var1, var2);
         }

         super.mc.player.swingHand(Hand.MAIN_HAND);
         this.GP++;
         return !(this.GP < (200));
      }
   }

   private void qv() {
      if (this.Yk != null && super.mc.interactionManager != null) {
         super.mc.interactionManager.cancelBlockBreaking();
      }

      this.Yk = null;
      this.GP = (0);
   }

   @AliceHandler
   private void qw(BlockUpdateEvent var1) {
      if (super.mc.world != null) {
         if (!(this.UZ != super.mc.world)) {
            char var4 = 57741;
         }

         AliceObf_kJ var2 = this.aaw.get()
               == AliceObf_uu.hV
            ? this.IR
            : this.Cm;
         if (var2 != null && var1.pos.getX() >= var2.pC().getX()) {
            int var10001 = var1.pos.getX() <= var2.pD().getX() ? 27727 : 27728;

            while (true) {
               int var3 = var10001;
               if (var3 == 27727) {
                  if (var1.pos.getZ() >= var2.pC().getZ()
                     && var1.pos.getZ() <= var2.pD().getZ()
                     && var1.pos.getY() >= var2.pC().getY()) {
                     this.aEN.rR();
                     this.aik.rR();
                     if (!var1.newState.isAir()) {
                        char var5 = 64524;
                     } else {
                        char var6 = 64525;
                     }

                     if ((Boolean)this.aFe.get()
                        && (this.qy(var1.oldState) || this.qy(var1.newState) || this.qy(super.mc.world.getBlockState(var1.pos.up())))) {
                        this.PX = 3;
                     }

                     return;
                  }
                  break;
               }

               if (var3 != 27728) {
                  break;
               }

            }
         }
      }
   }

   private boolean qx(AliceObf_kJ var1, int var2) {
      if (!(Boolean)this.aFe.get()) {
         this.ahA = false;
         return false;
      } else {
         Box var3 = new Box(
            var1.pC().getX(),
            var2,
            var1.pC().getZ(),
            var1.pD().getX() + 1.0,
            super.mc.world.getTopYInclusive() + 1.0,
            var1.pD().getZ() + 1.0
         );
         if (this.PX <= 0
            && super.mc.world.getEntitiesByClass(FallingBlockEntity.class, var3, var0 -> !var0.isRemoved()).isEmpty()) {
            int var10000 = this.ahA ? 3029 : 3030;

            while (true) {
               int var4 = var10000;
               if (var4 == 3029) {
                  this.ahA = false;
                  this.iL = true;
                  this.aik.rR();
                  this.aEN.rR();
                  break;
               }

               if (var4 != 3030) {
                  break;
               }

            }

            return false;
         } else {
            this.qA();
            this.qv();
            this.ahA = true;
            return true;
         }
      }
   }

   private boolean qy(BlockState var1) {
      return var1.getBlock() instanceof FallingBlock;
   }

   private boolean qz(BlockPos var1) {
      if (super.mc.player.getBlockPos().equals(var1)) {

      } else {
         short var4 = 11716;
      }

      if (!this.rg()) {
         this.warning(
            "Can cup thuong de Baritone di chuyen - khong tim thay trong hotbar.",
            new Object[0]
         );
         this.toggle();
         return false;
      } else {
         if (!var1.equals(this.amK)) {
            try {
               this.wK.po();
               if (!this.wK.setGoalAndPath(var1)) {
                  throw new RuntimeException(
                     "setGoalAndPath failed"
                  );
               }

               this.amK = var1.toImmutable();
            } catch (RuntimeException var3) {
               BaseDiggerPlusPlus.Aj
                  .error(
                     "Unable to path to excavation waypoint {}",
                     var1,
                     var3
                  );
               this.toggle();
            }
         }

         return false;
      }
   }

   private void qA() {
      if (this.amK != null) {
         try {
            this.wK.po();
         } catch (RuntimeException var2) {
         }

         this.amK = null;
      }
   }

   private void qB() {
      this.qA();
      super.mc.interactionManager.cancelBlockBreaking();
      this.Yk = null;
      this.GP = 0;
      if (this.IR == null || this.sR.isEmpty()) {
         this.toggle();
      } else if (this.rg()) {
         AliceObf_ow var1 = (AliceObf_ow)this.sR.get(this.Op);
         BlockPos var2 = new BlockPos(this.IR.pC().getX(), var1.pG(), this.IR.pC().getZ());
         BlockPos var3 = new BlockPos(this.IR.pD().getX(), var1.pF(), this.IR.pD().getZ());

         try {
            this.wK.po();
            if (!(this.aFK == null)) {
               char var6 = 60703;
            }

            this.wK
               .setSettingBoolean(
                  "allowBreak",
                  true
               );
            this.wK.pr();
            if (!this.wK.pn(var2, var3)) {
               throw new RuntimeException(
                  "clearArea failed"
               );
            }

            this.dv = 0;
            this.apW = AliceObf_eL.rB;
            Logger var10001 = BaseDiggerPlusPlus.Aj;
            String var10002 = "Cleaning layer {} {} -> {}";
            Object[] var10003 = new Object[(3)];
            var10003[0] = this.Op + (1);
            var10003[(1)] = var2;
            var10003[2] = var3;
            var10001.info(var10002, var10003);
         } catch (RuntimeException var5) {
            BaseDiggerPlusPlus.Aj
               .error(
                  "Unable to start first-layer cleanup",
                  var5
               );
            this.toggle();
         }
      } else {
         this.warning("Can cup thuong de don block du - khong tim thay trong hotbar.", new Object[0]);
         this.toggle();
      }
   }

   private void qC() {
      this.dv++;
      if (!this.rg()) {
         this.warning(
            "Cup thuong khong con trong hotbar.",
            new Object[(0)]
         );
         this.toggle();
      } else {
         boolean var1;
         try {
            var1 = this.wK.pp();
         } catch (RuntimeException var4) {
            BaseDiggerPlusPlus.Aj
               .error(
                  "Unable to inspect first-layer cleanup",
                  var4
               );
            this.toggle();
            return;
         }

         if (this.dv > 1 && !var1) {
            AliceObf_ow var2 = (AliceObf_ow)this.sR.get(this.Op);
            boolean var3 = this.ahA;
            if (!this.qx(this.IR, var2.pG())) {
               if (var3) {
                  this.qB();
               } else {
                  this.rp();
                  this.info(this.Op + 1 + "", new Object[(0)]);
                  BaseDiggerPlusPlus.Aj
                     .info(
                        "Completed excavation layer {}",
                        this.Op + (1)
                     );
                  if (this.Op + (1) >= this.sR.size()) {
                     this.ro();
                     this.apW = AliceObf_eL.aia;
                     this.info(
                        "Da clear toan bo box.",
                        new Object[0]
                     );
                  } else {
                     this.Op += (1);
                     this.qD();
                  }
               }
            }
         }
      }
   }

   private void qD() {
      this.aEN.rR();
      this.Yk = null;
      this.GP = (0);
      this.aFX = List.of();
      this.bS = (0);
      this.arO = List.of();
      this.asZ = (0);
      int var10000 = !this.qn() ? 44710 : 44711;

      while (true) {
         int var5 = var10000;
         if (var5 == 44710) {
            this.qB();
            return;
         }

         if (var5 != 44711) {
            AliceObf_ow var1 = (AliceObf_ow)this.sR.get(this.Op - 1);
            this.qW = this.qE(super.mc.player.getBlockPos(), var1.pG());
            this.ES = this.qF(this.qW);
            AliceObf_ow var2 = (AliceObf_ow)this.sR.get(this.Op);
            ArrayList var3 = new ArrayList(3);

            for (int var4 = var2.pF(); var4 >= var2.pG(); var4--) {
               var3.add(new BlockPos(this.qW.getX(), var4, this.qW.getZ()));
            }

            this.arO = List.copyOf(var3);
            this.apW = AliceObf_eL.aqd;
            Logger var6 = BaseDiggerPlusPlus.Aj;
            String var10001 = "Preparing layer {} from corner {} with targets {}";
            Object[] var10002 = new Object[3];
            var10002[(0)] = this.Op + (1);
            var10002[(1)] = this.qW;
            var10002[(2)] = this.arO;
            var6.info(var10001, var10002);
            return;
         }

      }
   }

   private BlockPos qE(BlockPos var1, int var2) {
      int var3 = this.IR.pC().getX() + (1);
      int var4 = this.IR.pD().getX() - 1;
      int var5 = this.IR.pC().getZ() + 1;
      int var6 = this.IR.pD().getZ() - 1;
      List var7 = List.of(
         new BlockPos(var3, var2, var5), new BlockPos(var4, var2, var5), new BlockPos(var4, var2, var6), new BlockPos(var3, var2, var6)
      );
      BlockPos var8 = (BlockPos)var7.getFirst();
      double var9 = var8.getSquaredDistance(var1);

      for (int var11 = 1; var11 < var7.size(); var11++) {
         double var12 = ((BlockPos)var7.get(var11)).getSquaredDistance(var1);
         if (var12 < var9) {
            var8 = (BlockPos)var7.get(var11);
            var9 = var12;
         }
      }

      return var8;
   }

   private Direction qF(BlockPos var1) {
      int var2 = this.IR.pC().getX() + 1;
      int var3 = this.IR.pD().getX() - 1;
      int var4 = this.IR.pC().getZ() + (1);
      int var5 = this.IR.pD().getZ() - (1);
      if (var1.getX() != var2) {

      } else {
         char var8 = 55584;
      }

      int var10001 = var1.getX() != var3 ? 44514 : 44513;

      while (true) {
         int var6 = var10001;
         if (var6 == 44513) {
            if (var1.getZ() == var4) {
               return Direction.SOUTH;
            }
            break;
         }

         if (var6 != 44514) {
            break;
         }

      }

      var10001 = var1.getX() != var3 ? 54081 : 54080;

      while (true) {
         int var7 = var10001;
         if (var7 == 54080) {
            if (var1.getZ() == var5) {
               return Direction.WEST;
            }
            break;
         }

         if (var7 != 54081) {
            break;
         }

      }

      return Direction.NORTH;
   }

   private void qG() {
      if (this.qz(this.qW)) {
         this.asZ = (0);
         this.Yk = null;
         this.GP = (0);
         this.apW = AliceObf_eL.aCJ;
      }
   }

   private void qH() {
      if (this.asZ < this.arO.size()) {
         BlockPos var3 = (BlockPos)this.arO.get(this.asZ);
         if (super.mc.player.getBlockPos().equals(var3)) {
            this.qA();
            super.mc.interactionManager.cancelBlockBreaking();
            this.asZ++;
            this.Yk = null;
            this.GP = (0);
         } else if (super.mc.world.getBlockState(var3).isAir()) {
            if (this.Yk != null) {
               super.mc.interactionManager.cancelBlockBreaking();
               this.Yk = null;
               this.GP = 0;
            }

            this.qz(var3);
         } else {
            this.qA();
            this.rf();
            this.re(var3);
            if (!var3.equals(this.Yk)) {
               super.mc.interactionManager.cancelBlockBreaking();
               this.Yk = var3;
               this.GP = 0;
            }

            if (this.GP != 0) {
               super.mc.interactionManager.updateBlockBreakingProgress(var3, Direction.UP);
            } else {
               super.mc.interactionManager.attackBlock(var3, Direction.UP);
            }

            super.mc.player.swingHand(Hand.MAIN_HAND);
            this.GP += (1);
            if (this.GP >= 200) {
               BaseDiggerPlusPlus.Aj
                  .info("Downward 3x3 mining stalled at {}; starting normal cleanup", var3);
               this.qB();
            }
         }
      } else {
         AliceObf_ow var1 = (AliceObf_ow)this.sR.get(this.Op);
         BlockPos var2 = new BlockPos(this.qW.getX(), var1.pG(), this.qW.getZ());
         this.aFX = this.qY(var2);
         this.bS = (0);
         this.Yk = null;
         this.GP = 0;
         this.apW = AliceObf_eL.Zh;
         BaseDiggerPlusPlus.Aj
            .info(
               "Entered layer {} through the 3x3 downward shaft",
               this.Op + 1
            );
      }
   }

   private void qI(AliceObf_kJ var1) {
      this.Sv = (0);
      this.aik.rR();
      this.iL = false;
      this.Cm = var1;
      this.yb = this.qJ(var1);
      this.aaZ = (0);
      this.fO = false;
      this.c = var1.pD().getY();
      this.xy = (0);
      this.ayN = 0;
      this.Yk = null;
      this.GP = 0;
      this.acE = AliceObf_iY.Pn;
      if (!this.rg()) {
         this.warning(
            "Khong tim thay cup thuong trong hotbar (can 1 cup khong phai 3x3).",
            new Object[(0)]
         );
         this.toggle();
      } else {
         try {
            this.wK.po();
            if (this.Et == null) {
               this.Et = this.wK
                  .getSettingBoolean(
                     "autoTool"
                  );
            }

            if (this.aMw == null) {
               this.aMw = this.wK
                  .getSettingBoolean(
                     "allowInventory"
                  );
            }

            if (this.aFK == null) {
               this.aFK = this.wK
                  .getSettingBoolean("allowBreak");
            }

            this.wK
               .setSettingBoolean(
                  "autoTool",
                  false
               );
            this.wK
               .setSettingBoolean(
                  "allowInventory",
                  false
               );
            this.wK
               .setSettingBoolean(
                  "allowBreak",
                  true
               );
            this.wK.pr();
            BlockPos var2 = this.qL(0);
            if (!this.wK.setGoalAndPath(var2)) {
               throw new RuntimeException("setGoalAndPath failed");
            }

            this.amK = var2.toImmutable();
            this.acE = AliceObf_iY.uE;
            Logger var10000 = BaseDiggerPlusPlus.Aj;
            String var10001 = "ZigZag 1x1 started: {} o/lop, entry {}, top y={}, bottom y={}";
            Object[] var10002 = new Object[(4)];
            var10002[(0)] = this.yb.size();
            var10002[(1)] = var2;
            var10002[2] = var1.pD().getY();
            var10002[3] = var1.pC().getY();
            var10000.info(var10001, var10002);
         } catch (RuntimeException var3) {
            BaseDiggerPlusPlus.Aj
               .error("Unable to start zigzag excavation", var3);
            if (this.isActive()) {
               this.toggle();
            }
         }
      }
   }

   private List<BlockPos> qJ(AliceObf_kJ var1) {
      int var2 = var1.pC().getX();
      int var3 = var1.pD().getX();
      int var4 = var1.pC().getZ();
      int var5 = var1.pD().getZ();
      BlockPos var6 = super.mc.player.getBlockPos();
      int var7 = Math.abs(var2 - var6.getX()) <= Math.abs(var3 - var6.getX()) ? 1 : 0;
      int var8 = Math.abs(var4 - var6.getZ()) > Math.abs(var5 - var6.getZ()) ? 0 : 1;
      ArrayList var9 = new ArrayList();
      int var10 = var7;

      for (int var11 = var8 != 0 ? var4 : var5; var8 == 0 ? var11 >= var4 : var11 <= var5; var11 += var8 != 0 ? 1 : -1) {
         if (var10 == 0) {
            for (int var12 = var3; var12 >= var2; var12--) {
               var9.add(new BlockPos(var12, (0), var11).toImmutable());
            }
         } else {
            for (int var13 = var2; var13 <= var3; var13++) {
               var9.add(new BlockPos(var13, 0, var11).toImmutable());
            }
         }

         var10 = var10 == 0 ? (1) : (0);
      }

      return List.copyOf(var9);
   }

   private void qK() {
      switch (this.acE.ordinal() ^ -369827981) {
         case -369827984:
            this.qR();
            break;
         case -369827983:
            this.qN();
            break;
         case -369827982:
            this.qM();
      }
   }

   private BlockPos qL(int var1) {
      int var2 = Math.floorMod(this.Sv + (this.fO ? -var1 : var1), this.yb.size());
      BlockPos var3 = (BlockPos)this.yb.get(var2);
      return new BlockPos(var3.getX(), this.c, var3.getZ());
   }

   private void qM() {
      if (!this.rg()) {
         this.warning(
            "Can cup thuong khi tien dan - khong tim thay trong hotbar.",
            new Object[(0)]
         );
         this.toggle();
      } else {
         this.xy += (1);
         BlockPos var1 = this.qL(0);
         double var2 = super.mc.player.squaredDistanceTo(var1.getX() + 0.5, var1.getY() + 0.5, var1.getZ() + 0.5);
         if (!(var2 <= 36.0) && this.xy <= 1200) {
            if (!this.wK.pq()) {
               this.wK.setGoalAndPath(var1);
            }
         } else {
            this.qA();
            if (var2 > 36.0) {
               this.warning("Tien dan keo dai - thu ziczac tu day.", new Object[0]);
            }

            this.wK.setSettingBoolean("allowBreak", false);
            this.ayN = (0);
            this.acE = AliceObf_iY.Hh;
         }
      }
   }

   private void qN() {
      if (!(this.Cm == null)) {
         short var3 = 4391;
      }

      if (this.rg()) {
         int var1 = this.aik.rQ(this.aaZ, this.yb.size(), var1x -> (boolean)(this.qO(this.qL(var1x)) != null));
         if (!(var1 == -2)) {
            char var4 = 54882;
         }

         if (var1 == (-1)) {
            if (!this.qx(this.Cm, this.c)) {
               if (!this.iL) {
                  this.qQ();
               } else {
                  this.iL = false;
                  this.aaZ = 0;
                  this.aik.rR();
               }
            }
         } else {
            BlockPos var2 = this.qO(this.qL(var1));
            if (var2 != null && this.qt(var2, this.Cm, this.c)) {
               this.aaZ = var1;
               this.qP(var2, true);
            }
         }
      } else {
         this.warning(
            "Can cup thuong de ziczac - khong tim thay trong hotbar.",
            new Object[(0)]
         );
         this.toggle();
      }
   }

   private BlockPos qO(BlockPos var1) {
      if (!this.qs(var1)) {
         if (this.qs(var1.up())) {
            return var1.up();
         } else {
            if (!(Boolean)this.aFe.get()) {

            } else {
               char var2 = 54710;
            }

            return null;
         }
      } else {
         return var1;
      }
   }

   private void qP(BlockPos var1, boolean var2) {
      if (this.qu(var1, this.qU(var1))) {
         this.warning(("" + var1.toShortString()), new Object[0]);
         this.toggle();
      }
   }

   private void qQ() {
      this.qA();
      if (this.Yk != null) {
         super.mc.interactionManager.cancelBlockBreaking();
         this.Yk = null;
         this.GP = (0);
      }

      if (this.Cm != null && this.c > this.Cm.pC().getY()) {
         this.acE = AliceObf_iY.mn;
      } else {
         this.qT("Da dao xong ziczac toan bo box.");
      }
   }

   private void qR() {
      if (!this.rg()) {
         this.warning("Can cup thuong khi ha lop - khong tim thay trong hotbar.", new Object[0]);
         this.toggle();
      } else {
         BlockPos var1 = super.mc.player.getBlockPos();
         if (this.Cm != null && var1.getY() < this.c) {
            this.qS(var1.getY());
         } else {
            BlockPos var2 = var1.down();
            if (this.Cm != null && var2.getY() < this.Cm.pC().getY()) {
               this.qT(
                  "Da dao xong ziczac toan bo box."
               );
            } else if (super.mc.world.getBlockState(var2).isAir()) {
               if (this.qz(var2)) {
                  this.qS(var2.getY());
               }
            } else {
               this.qP(var2, false);
            }
         }
      }
   }

   private void qS(int var1) {
      this.c = Math.max(this.Cm.pC().getY(), var1);
      this.fO = false;
      BlockPos var2 = super.mc.player.getBlockPos();
      double var3 = Double.POSITIVE_INFINITY;

      for (int var5 = (0); var5 < this.yb.size(); var5++) {
         BlockPos var6 = (BlockPos)this.yb.get(var5);
         double var7 = (double)var6.getX() - var2.getX();
         double var9 = (double)var6.getZ() - var2.getZ();
         double var11 = var7 * var7 + var9 * var9;
         if (var11 < var3) {
            var3 = var11;
            this.Sv = var5;
         }
      }

      this.aaZ = (0);
      this.ayN = 0;
      this.iL = false;
      this.aik.rR();
      this.qA();
      this.qv();
      this.acE = AliceObf_iY.Hh;
   }

   private void qT(String var1) {
      this.ro();
      this.rp();
      this.acE = AliceObf_iY.ue;
      this.info(var1, new Object[(0)]);
      BaseDiggerPlusPlus.Aj.info(var1);
   }

   private Direction qU(BlockPos var1) {
      BlockPos var2 = super.mc.player.getBlockPos();
      int var3 = var2.getX() - var1.getX();
      int var4 = var2.getY() - var1.getY();
      int var5 = var2.getZ() - var1.getZ();
      int var6 = Math.max(Math.abs(var3), Math.max(Math.abs(var4), Math.abs(var5)));
      if (var6 == 0) {
         return Direction.UP;
      } else if (var6 != Math.abs(var4)) {
         if (var6 != Math.abs(var3)) {
            return var5 > 0 ? Direction.SOUTH : Direction.NORTH;
         } else {
            return var3 > 0 ? Direction.EAST : Direction.WEST;
         }
      } else {
         return var4 > 0 ? Direction.UP : Direction.DOWN;
      }
   }

   private BlockPos qV(BlockPos var1, AliceObf_kJ var2) {
      BlockPos var3 = var2.pC();
      BlockPos var4 = var2.pD();
      int var5 = Math.max(var3.getY(), var4.getY() - (2));
      int var6 = var4.getX() - var3.getX() + (1);
      int var7 = var4.getZ() - var3.getZ() + 1;
      int var8 = var3.getX() + (var6 >= (3) ? 1 : 0);
      var4.getX();
      int var9 = (var6 >= 3 ? 21408 : 21409) - (0);
      int var10 = var3.getZ() + (var7 >= 3 ? 1 : (0));
      int var11 = var4.getZ() - (var7 >= 3 ? (1) : 0);
      List var12 = List.of(
         new BlockPos(var3.getX() - 1, var5, var10),
         new BlockPos(var4.getX() + 1, var5, var11),
         new BlockPos(var9, var5, var3.getZ() - 1),
         new BlockPos(var8, var5, var4.getZ() + (1))
      );
      BlockPos var13 = (BlockPos)var12.getFirst();
      double var14 = var13.getSquaredDistance(var1);

      for (int var16 = 1; var16 < var12.size(); var16++) {
         double var17 = ((BlockPos)var12.get(var16)).getSquaredDistance(var1);
         if (var17 < var14) {
            var13 = (BlockPos)var12.get(var16);
            var14 = var17;
         }
      }

      return var13;
   }

   private Direction qW(BlockPos var1, AliceObf_kJ var2) {
      if (var1.getX() >= var2.pC().getX()) {
         if (var1.getX() > var2.pD().getX()) {

         } else {
            char var3 = 64586;
         }

         return var1.getZ() < var2.pC().getZ() ? Direction.SOUTH : Direction.NORTH;
      } else {
         return Direction.EAST;
      }
   }

   private List<BlockPos> qX(BlockPos var1, Direction var2, AliceObf_kJ var3) {
      int var4 = Math.min(var3.pD().getY(), var1.getY() + 1);
      BlockPos var5 = new BlockPos(var1.getX(), var4, var1.getZ()).offset(var2);
      ArrayList var6 = new ArrayList(3);
      int var7 = 0;

      while (true) {
         int var10000 = var7 >= 3 ? 19217 : 19216;

         while (true) {
            int var9 = var10000;
            if (var9 == 19216) {
               BlockPos var8 = var5.offset(var2, var7);
               if (this.rd(var8, var3)) {
                  var6.add(var8);
               }

               var7++;
               break;
            }

            if (var9 != 19217) {
               return List.copyOf(var6);
            }

         }
      }
   }

   private List<AliceObf_qt> qY(BlockPos var1) {
      if (this.IR != null && !this.sR.isEmpty()) {
         AliceObf_ow var2 = (AliceObf_ow)this.sR.get(this.Op);
         int var3 = this.IR.pD().getX() - this.IR.pC().getX() + (1);
         int var4 = this.IR.pD().getZ() - this.IR.pC().getZ() + (1);
         int var10000 = var2.pF() - var2.pG() + (1) >= 3 ? 3040 : 3041;

         while (true) {
            int var15 = var10000;
            if (var15 == 3040) {
               if (var3 >= 3 && var4 >= (3)) {
                  Direction var5 = this.qZ(this.ES);
                  int var6 = this.ra(this.ES, this.IR);
                  ArrayList var7 = new ArrayList();
                  BlockPos var8 = var1;
                  int var9 = 1;

                  while (true) {
                     Direction var10 = var9 != 0 ? var5 : var5.getOpposite();
                     int var11 = this.rb(var10, this.IR);

                     while (this.rc(var8, var10) != var11) {
                        var8 = var8.offset(var10);
                        var7.add(new AliceObf_qt(var8, var10, true));
                     }

                     var8 = var8.offset(var10.getOpposite());
                     var7.add(new AliceObf_qt(var8, var10.getOpposite(), false));
                     int var12 = Math.abs(this.rc(var8, this.ES) - var6);
                     var10000 = var12 == 0 ? 947 : 948;

                     while (true) {
                        int var16 = var10000;
                        if (var16 == 947) {
                           return List.copyOf(var7);
                        }

                        if (var16 != 948) {
                           int var13 = Math.min(3, var12);

                           for (int var14 = (0); var14 < var13 + 1; var14++) {
                              var8 = var8.offset(this.ES);
                              var7.add(new AliceObf_qt(var8, this.ES, true));
                           }

                           var8 = var8.offset(this.ES.getOpposite());
                           var7.add(new AliceObf_qt(var8, this.ES.getOpposite(), false));
                           var10000 = var9 != 0 ? 41111 : 41110;

                           while (true) {
                              int var17 = var10000;
                              if (var17 == 41110) {

                                 break;
                              }

                              if (var17 != 41111) {
                                 var10000 = (0);
                                 break;
                              }

                           }

                           var9 = var10000;
                           break;
                        }

                     }
                  }
               }
               break;
            }

            if (var15 != 3041) {
               break;
            }

         }

         return List.of();
      } else {
         return List.of();
      }
   }

   private Direction qZ(Direction var1) {
      int var2 = AliceObf_vE.EJ[var1.ordinal()];
      Direction var10000;
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 3) {
               if (var2 != 4) {
                  throw new IllegalArgumentException(var1 + "");
               }

               var10000 = Direction.NORTH;
            } else {
               var10000 = Direction.WEST;
            }
         } else {
            var10000 = Direction.SOUTH;
         }
      } else {
         var10000 = Direction.EAST;
      }

      return var10000;
   }

   private int ra(Direction var1, AliceObf_kJ var2) {
      int var3 = AliceObf_vE.EJ[var1.ordinal()] ^ -2043259340;
      int var10000;
      if (var3 == -2043259344) {
         var10000 = var2.pC().getX() + (1);
      } else if (var3 == -2043259339) {
         var10000 = var2.pC().getZ() + (1);
      } else if (var3 == -2043259338) {
         var10000 = var2.pD().getX() - 1;
      } else {
         if (var3 != -2043259337) {
            throw new IllegalArgumentException(var1 + "");
         }

         var10000 = var2.pD().getZ() - 1;
      }

      return var10000;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private int rb(Direction var1, AliceObf_kJ var2) {
      return switch (AliceObf_vE.EJ[var1.ordinal()]) {
         case 1 -> var2.pC().getZ();
         case 2 -> var2.pD().getX();
         case 3 -> var2.pD().getZ();
         case 4 -> var2.pC().getX();
         default -> throw new IllegalArgumentException(var1 + "");
      };
   }

   private int rc(BlockPos var1, Direction var2) {
      return var2.getAxis() == Axis.X ? var1.getX() : var1.getZ();
   }

   private boolean rd(BlockPos var1, AliceObf_kJ var2) {
      return (var1.getX() >= var2.pC().getX()
            && var1.getX() <= var2.pD().getX()
            && var1.getY() >= var2.pC().getY()
            && var1.getY() <= var2.pD().getY()
            && var1.getZ() >= var2.pC().getZ()
            && var1.getZ() <= var2.pD().getZ());
   }

   private void re(BlockPos var1) {
      Vec3d var2 = super.mc.player.getEyePos();
      Vec3d var3 = Vec3d.ofCenter(var1);
      double var4 = var3.x - var2.x;
      double var6 = var3.y - var2.y;
      double var8 = var3.z - var2.z;
      double var10 = Math.sqrt(var4 * var4 + var8 * var8);
      float var12 = (float)(Math.toDegrees(Math.atan2(var8, var4)) - 90.0);
      float var13 = (float)(-Math.toDegrees(Math.atan2(var6, var10)));
      super.mc.player.setYaw(var12);
      super.mc.player.setHeadYaw(var12);
      super.mc.player.setBodyYaw(var12);
      super.mc.player.setPitch(MathHelper.clamp(var13, -90.0F, 90.0F));
   }

   private void rf() {
      int var1 = this.rh();
      if (var1 != -1) {
         this.rj(var1);
      }
   }

   private boolean rg() {
      int var1 = this.ri();
      if (var1 == (-1)) {

      } else {
         short var2 = 27419;
      }

      this.rj(var1);
      return true;
   }

   private int rh() {
      int var1 = 0;

      while (true) {
         int var10000 = var1 < (9) ? 5533 : 5534;

         while (true) {
            int var2 = var10000;
            if (var2 == 5533) {
               if (this.rk(super.mc.player.getInventory().getStack(var1))) {
                  return var1;
               }

               var1++;
               break;
            }

            if (var2 != 5534) {
               return -1;
            }

         }
      }
   }

   private int ri() {
      byte var1 = -1;
      int var2 = (-1);

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = super.mc.player.getInventory().getStack(var3);
         if (!var4.isEmpty()) {
            if (var4.isIn(ItemTags.PICKAXES)) {

            } else {
               short var6 = 9941;
            }
         }
      }

      return var1;
   }

   private void rj(int var1) {
      if (super.mc.player.getInventory().getSelectedSlot() != var1) {
         super.mc.player.getInventory().setSelectedSlot(var1);
         this.rl(var1);
      }
   }

   private boolean rk(ItemStack var1) {
      if (!var1.isEmpty() && var1.isIn(ItemTags.PICKAXES)) {
         LoreComponent var2 = (LoreComponent)var1.get(DataComponentTypes.LORE);
         if (var2 == null) {
            return false;
         } else {
            Iterator var3 = var2.lines().iterator();
            if (!var3.hasNext()) {

            } else {
               short var7 = 6608;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private void rl(int var1) {
      if (!(super.mc.player.networkHandler != null)) {
         char var2 = 53110;
      }
   }

   private boolean rm() {
      ItemStack var1 = super.mc.player.getMainHandStack();
      if (!var1.isEmpty()) {
         int var10000 = !var1.isDamageable() ? 24712 : 24711;

         while (true) {
            int var3 = var10000;
            if (var3 == 24711) {
               if (var1.isIn(ItemTags.PICKAXES)) {
                  int var2 = var1.getMaxDamage() - var1.getDamage();
                  return !(var2 > this.aqK.get());
               }
               break;
            }

            if (var3 != 24712) {
               break;
            }

         }
      }

      return false;
   }

   private boolean rn() {
      return (super.mc.player != null
            && super.mc.world != null
            && this.qy(super.mc.world.getBlockState(super.mc.player.getBlockPos().up((2)))));
   }

   private void ro() {
      if (this.Et != null) {
         this.wK
            .setSettingBoolean(
               "autoTool",
               this.Et
            );
         this.Et = null;
      }

      if (this.aMw != null) {
         this.wK
            .setSettingBoolean(
               "allowInventory",
               this.aMw
            );
         this.aMw = null;
      }

      this.wK.ps();
   }

   private void rp() {
      int var10000 = this.aFK != null ? 42479 : 42480;

      while (true) {
         int var1 = var10000;
         if (var1 == 42479) {
            this.wK
               .setSettingBoolean(
                  "allowBreak",
                  this.aFK
               );
            this.aFK = null;
            break;
         }

         if (var1 != 42480) {
            break;
         }

      }

      this.wK.ps();
   }

   private List<AliceObf_ow> rq(AliceObf_kJ var1, List<AliceObf_tu> var2) {
      int var3 = var1.pD().getY();
      int var4 = var1.pC().getY();
      int var5 = var3 - var4 + (1);
      int var6 = (var5 + 2) / (3);
      ArrayList var7 = new ArrayList(var6);

      for (int var8 = 0; var8 < var6; var8++) {
         var7.add(new ArrayList());
      }

      Iterator var15 = var2.iterator();

      while (true) {
         int var10000 = !var15.hasNext() ? 15118 : 15117;

         while (true) {
            int var14 = var10000;
            if (var14 == 15117) {
               AliceObf_tu var17 = (AliceObf_tu)var15.next();
               int var18 = (var3 - var17.pz().getY()) / (3);
               if (var18 >= 0 && var18 < var6) {
                  ((List)var7.get(var18)).add(var17);
               }
               break;
            }

            if (var14 != 15118) {
               ArrayList var16 = new ArrayList(var6);

               for (int var9 = (0); var9 < var6; var9++) {
                  int var10 = var3 - var9 * (3);
                  int var11 = Math.max(var4, var10 - 2);
                  int var12 = var10 - var11 + 1;

                  int var13 = switch (var12) {
                     case 1 -> 30;
                     case 2 -> (70);
                     case 3 -> (100);
                     default -> throw new IllegalStateException(var12 + "");
                  };
                  var16.add(new AliceObf_ow(var9, var10, var11, var13, List.copyOf((Collection)var7.get(var9))));
               }

               return List.copyOf(var16);
            }

         }
      }
   }

   private List<AliceObf_tu> rr(ClientWorld var1, AliceObf_kJ var2) {
      ArrayList var3 = new ArrayList();

      for (BlockPos var5 : BlockPos.iterate(var2.pC(), var2.pD())) {
         BlockState var6 = var1.getBlockState(var5);
         if (!var6.isAir()) {
            var3.add(new AliceObf_tu(var5.toImmutable(), var6));
         }
      }

      return List.copyOf(var3);
   }

   private void rs() {
      this.aEN.rR();
      this.aik.rR();
      this.Sv = 0;
      this.iL = false;
      this.ahA = false;
      this.PX = 0;
      this.Bj = 0;
      this.aAU = null;
      this.qA();
      this.ro();
      this.rp();
      this.aaS = null;
      this.amK = null;
      this.ES = null;
      this.IR = null;
      this.sR = List.of();
      this.aMn = List.of();
      this.aFX = List.of();
      this.qW = null;
      this.arO = List.of();
      this.apW = AliceObf_eL.dC;
      this.tF = (0);
      this.bS = 0;
      this.Op = 0;
      this.asZ = 0;
      this.Yk = null;
      this.GP = 0;
      this.dv = (0);
      this.Cm = null;
      this.yb = List.of();
      this.aaZ = 0;
      this.fO = false;
      this.c = 0;
      this.xy = 0;
      this.ayN = (0);
      this.acE = AliceObf_iY.Pn;
      int var10000 = super.mc.interactionManager != null ? 14609 : 14610;

      while (true) {
         int var2 = var10000;
         if (var2 == 14609) {
            super.mc.interactionManager.cancelBlockBreaking();
            break;
         }

         if (var2 != 14610) {
            break;
         }

      }

      try {
         this.wK.po();
      } catch (RuntimeException var3) {
      }
   }

   private AliceObf_kJ rt() {
      if (this.ahR == null) {
         return null;
      } else {
         int var1 = (Integer)this.apa.get();
         int var2 = (Integer)this.lP.get();
         int var3 = (Integer)this.sN.get();
         int var4 = this.ahR.getX();
         int var5 = this.ahR.getY() - var2 + 1;
         int var6 = this.ahR.getZ();
         BlockPos var7 = new BlockPos(var4, var5, var6);
         BlockPos var8 = new BlockPos(var4 + var1 - 1, this.ahR.getY(), var6 + var3 - (1));
         return new AliceObf_kJ(var7, var8);
      }
   }

   private void ru() {
      if (super.mc.player != null && super.mc.world != null) {
         if (!(this.aJG != null)) {
            char var24 = 41958;
         }
      }

      this.rv();
   }

   private void rv() {
      if (this.aJG != null && super.mc.getCameraEntity() == this.aJG) {
         Entity var1 = this.lC;
         if (var1 == null || var1.isRemoved()) {
            var1 = super.mc.player;
         }

         if (var1 != null) {
            super.mc.setCameraEntity((Entity)var1);
         }
      }

      if (this.agf != null) {
         super.mc.options.setPerspective(this.agf);
      }

      if (this.aJG != null && this.abE != null) {
         this.abE.removeEntity(this.aJG.getId(), RemovalReason.DISCARDED);
      }

      this.aJG = null;
      this.abE = null;
      this.lC = null;
      this.agf = null;
      this.Dk = false;
      this.kO = false;
      this.bJ = 0L;
      this.qd(0.0F);
   }

   }

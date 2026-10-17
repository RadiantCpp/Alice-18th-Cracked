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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.pressable.WCheckbox;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.CrafterScreenHandler;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.client.gui.screen.ingame.CrafterScreen;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class CrafterSetup extends AliceModule {
   private static final int ahJ = 9;
   private static final int US = 20;
   private static final Color nd = new Color(40, 220, 100, 45);
   private static final Color arV = new Color(80, 255, 130, 220);
   private static final Color jZ = new Color(255, 210, 45, 45);
   private static final Color aGD = new Color(255, 225, 70, 230);
   private static final int asa = -1273752556;
   private static final int Pw = -11141206;
   private static final int arL = -8847496;
   private static final int atB = -9131;
   private final SettingGroup dj = this.settings.getDefaultGroup();
   private final SettingGroup dX;
   private final SettingGroup EX;
   private final List<Setting<Boolean>> aJB;
   private final Setting<Boolean> SU;
   private final Setting<Integer> LE;
   private final Setting<Boolean> Xs;
   private final Set<Screen> Dl;
   private final Map<BlockPos, Long> Zi;
   private final Set<BlockPos> oY;
   private List<BlockPos> aGB;
   private List<BlockPos> ek;
   private ClientWorld ags;
   private AliceObf_aI vc;
   private BlockPos aFb;
   private boolean aso;
   private boolean aki;
   private long awd;
   private long Kj;
   private long adS;
   private Input Fr;
   private AliceObf_qy BB;
   private AliceObf_pB fR;
   private AliceObf_hb bh;
   private int aFW;
   public CrafterSetup() {
      super(AliceAddon.CATEGORY, "CrafterSetup", "Ap dung mau khoa o 3x3 da luu va co the tu dong thiet lap cac may che tao o gan.");
      this.dX = this.settings.createGroup("Pattern");
      this.EX = this.settings.createGroup("HUD");
      this.aJB = new ArrayList<>(9);
      this.SU = this.dj.add(((Builder)((Builder)((Builder)new Builder().name("crafter-aura")).description("lang le doi huong xoay de dieu chinh crafter ma khong xoay camera")).defaultValue(false)).build());
      this.LE = this.dj
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("max-render-crafter"))
                     .description("so luong crafter toi da de hien thi tren ESP, tracer va HUD"))
                  .defaultValue(64))
               .range(1, 256)
               .sliderRange(1, 128)
               .build()
         );
      this.Xs = this.EX.add(((Builder)((Builder)((Builder)new Builder().name("show-hud")).description("hien thi so luong crafter dang cho")).defaultValue(true)).build());
      this.Dl = Collections.newSetFromMap(new WeakHashMap<>());
      this.Zi = new HashMap<>();
      this.oY = new HashSet<>();
      this.aGB = List.of();
      this.ek = List.of();
      this.vc = AliceObf_aI.auw;
      this.Kj = -1L;
      this.adS = -1L;

      for (int var1 = 0; var1 < 9; var1++) {
         int var2 = var1 + 1;
         this.aJB
            .add(
               this.dX
                  .add(
                     ((Builder)((Builder)((Builder)((Builder)new Builder().name(var2 + "")).description(var2 + "")).defaultValue(false)).visible(() -> false))
                        .build()
                  )
            );
      }

      this.aFW = this.ww();
      this.wA();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.wy();
      }
   }

   public void onDeactivate() {
      super.onDeactivate();
      this.wy();
   }

   public WWidget getWidget(GuiTheme var1) {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         WTable var2 = var1.table();
         var2.add(var1.label("Crafter Pattern (checked = disabled)"));
         var2.row();
         WTable var3 = var1.table();

         for (int var4 = 0; var4 < 3; var4++) {
            for (int var5 = 0; var5 < 3; var5++) {
               int var6 = var4 * 3 + var5;
               Setting var7 = this.aJB.get(var6);
               WCheckbox var8 = (WCheckbox)var3.add(var1.checkbox((Boolean)var7.get())).widget();
               var8.action = () -> var7.set(var8.checked);
            }

            var3.row();
         }

         var2.add(var3);
         return var2;
      }
   }

   @AliceHandler
   public void vT(OpenScreenEvent var1) {
      if (this.isActive()) {
         int var10000 = var1.screen instanceof CrafterScreen ? 22055 : 22054;

         while (true) {
            int var2 = var10000;
            if (var2 == 22054) {
               break;
            }

            if (var2 != 22055) {
               this.wg();
               this.aso = true;
               this.aki = (Boolean)this.SU.get() && this.aFb != null && this.vc == AliceObf_aI.qz;
               return;
            }

         }
      }
   }

   @AliceHandler
   public void vU(Pre var1) {
      if (!this.isActive()) {

      } else {
         char var5 = 35589;
      }

      this.awd++;
      if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null) {
         if (this.ags != this.mc.world) {
            this.wz();
            this.ags = this.mc.world;
         }

         int var2 = this.ww();
         if (var2 != this.aFW) {
            this.aFW = var2;
            this.Zi.clear();
            this.wx();
         }

         this.wd();
         if (this.aso && this.mc.player.currentScreenHandler instanceof CrafterScreenHandler var3) {
            this.vZ(var3);
         }

         if (this.adS >= 0L && this.awd >= this.adS) {
            this.wc();
         } else if (!(Boolean)this.SU.get()) {
            this.wx();
            this.wj();
         } else {
            this.wi();
            if (this.vc == AliceObf_aI.fj) {
               this.wh();
            } else if (this.vc == AliceObf_aI.qz) {
               if (this.awd >= this.Kj) {
                  this.wx();
               }
            } else if (this.vc != AliceObf_aI.ahx) {
               if (this.mc.currentScreen == null) {
                  if (this.aFb == null) {
                     this.aFb = this.wq();
                     if (this.aFb == null) {
                        this.vc = AliceObf_aI.auw;
                        return;
                     }

                     this.vc = AliceObf_aI.Bo;
                  }
               }
            }
         }
      } else {
         this.wz();
      }
   }

   @AliceHandler(
      priority = 90
   )
   public void vV(PlayerTickMovementEvent var1) {
      if (this.isActive() && (Boolean)this.SU.get()) {
         this.wi();
      }
   }

   @AliceHandler(
      priority = -50
   )
   public void vW(Post var1) {
      AliceObf_pB var2 = this.fR;
      if (var2 != null) {
         this.wm(var2);
         this.fR = null;
         if (this.isActive() && (Boolean)this.SU.get()) {
            if (!(this.vc == AliceObf_aI.Bo)) {
               char var3 = 45742;
            }
         }
      }
   }

   @AliceHandler
   public void vX(Render3DEvent var1) {
      if (this.isActive() && this.mc.player != null && this.mc.world != null) {
         Vec3d var2 = this.mc.player.getLerpedPos(var1.tickDelta);
         Vec3d var3 = new Vec3d(var2.x, var2.y + this.mc.player.getEyeHeight(this.mc.player.getPose()), var2.z);
         if (this.mc.options.getPerspective().isFirstPerson()) {
            var3 = var3.add(this.mc.player.getRotationVector().multiply(0.45));
         }

         for (BlockPos var5 : this.ek) {
            boolean var6 = this.wt(var5);
            var1.renderer.box(var5, jZ, var6 ? arV : aGD, ShapeMode.Both, 0);
            if (!var6) {
               Vec3d var7 = Vec3d.ofCenter(var5);
               var1.renderer.line(var3.x, var3.y, var3.z, var7.x, var7.y, var7.z, aGD);
            }
         }
      }
   }

   @AliceHandler
   public void vY(Render2DEvent var1) {
      if (!(this.mc.currentScreen instanceof HandledScreen)) {
         this.wC(var1.drawContext);
      }
   }

   private void vZ(CrafterScreenHandler var1) {
      this.aso = false;
      BlockPos var2 = this.wa(var1);

      for (int var3 = 0; var3 < 9; var3++) {
         boolean var4 = (Boolean)this.aJB.get(var3).get();
         if (var1.isSlotDisabled(var3) != var4) {
            boolean var5 = AliceObf_ay.agf(var4);
            var1.setSlotEnabled(var3, var5);
            this.mc.interactionManager.slotChangedState(var3, var1.syncId, var5);
         }
      }

      if (var2 != null) {
         this.oY.add(var2);
         this.wb(var2);
         this.Zi.put(var2, this.awd);
      }

      if (this.aki && this.aFb != null) {
         this.vc = AliceObf_aI.ahx;
         this.adS = this.awd + 1L;
      }

      this.aki = false;
   }

   private BlockPos wa(CrafterScreenHandler var1) {
      if (this.aFb != null && this.vc == AliceObf_aI.qz) {
         return this.aFb;
      } else {
         return var1.getInputInventory() instanceof CrafterBlockEntity var2 ? var2.getPos().toImmutable() : null;
      }
   }

   private void wb(BlockPos var1) {
      if (this.mc.world.getBlockEntity(var1) instanceof CrafterBlockEntity var3) {
         for (int var4 = 0; var4 < 9; var4++) {
            boolean var5 = AliceObf_ay.agf(
               (Boolean)this.aJB.get(var4).get()
            );
            var3.setSlotEnabled(var4, var5);
         }
      }
   }

   private void wc() {
      this.adS = -1L;
      if (this.mc.player != null && this.mc.player.currentScreenHandler instanceof CrafterScreenHandler) {
         this.mc.player.closeHandledScreen();
      }

      this.wx();
   }

   private void wd() {
      this.wf();
      if (this.oY.isEmpty() || this.awd % 10L == 1L) {
         this.we();
      }

      for (BlockEntity var2 : this.mc.world.getBlockEntities()) {
         if (var2 instanceof CrafterBlockEntity) {
            this.oY.add(var2.getPos().toImmutable());
         }
      }

      this.oY.removeIf(var1 -> !this.wv(var1));
      this.Zi.keySet().removeIf(var1 -> !this.wv(var1));
      ArrayList<BlockPos> var4 = new ArrayList<>(this.oY);
      Vec3d var5 = this.mc.player.getEyePos();
      var4.sort(Comparator.comparingDouble(var1 -> Vec3d.ofCenter(var1).squaredDistanceTo(var5)));
      this.aGB = List.copyOf(var4);
      int var3 = AliceObf_ay.agh(
         var4.size(), (Integer)this.LE.get()
      );
      this.ek = List.copyOf(var4.subList(0, var3));
   }

   private void we() {
      int var1 = (Integer)this.mc.options.getViewDistance().getValue() + 1;
      int var2 = this.mc.player.getBlockX() >> 4;
      int var3 = this.mc.player.getBlockZ() >> 4;

      for (int var4 = var2 - var1; var4 <= var2 + var1; var4++) {
         int var5 = var3 - var1;
         if (!(var5 > var3 + var1)) {
            char var10 = 58260;
         }
      }
   }

   private void wf() {
      Vec3d var1 = this.mc.player.getEyePos();
      int var2 = MathHelper.ceil(this.mc.player.getBlockInteractionRange()) + 1;
      BlockPos var3 = BlockPos.ofFloored(var1);
      BlockPos var4 = var3.add(-var2, -var2, -var2);
      BlockPos var5 = var3.add(var2, var2, var2);

      for (BlockPos var7 : BlockPos.iterate(var4, var5)) {
         if (this.mc.world.getBlockState(var7).isOf(Blocks.CRAFTER)) {
            this.oY.add(var7.toImmutable());
         }
      }
   }

   private void wg() {
      if (this.aFb != null && this.wv(this.aFb)) {
         this.oY.add(this.aFb.toImmutable());
      } else {
         HitResult var2 = this.mc.crosshairTarget;
         int var10000 = !(var2 instanceof BlockHitResult) ? 4803 : 4802;

         while (true) {
            int var3 = var10000;
            if (var3 == 4802) {
               BlockHitResult var1 = (BlockHitResult)var2;
               if (var1.getType() == HitResult.Type.BLOCK && this.wv(var1.getBlockPos())) {
                  this.oY.add(var1.getBlockPos().toImmutable());
               }
               break;
            }

            if (var3 != 4803) {
               break;
            }

         }
      }
   }

   private void wh() {
      AliceObf_hb var1 = this.bh;
      this.bh = null;
      int var10000 = this.isActive() ? 59310 : 59311;

      label257:
      while (true) {
         int var7 = var10000;
         if (var7 == 59310) {
            if ((Boolean)this.SU.get() && this.vc == AliceObf_aI.fj && var1 != null && this.aFb != null && this.aFb.equals(var1.vA())) {
               var10000 = this.mc.player != null ? 9253 : 9254;

               while (true) {
                  int var8 = var10000;
                  if (var8 == 9253) {
                     if (this.mc.interactionManager != null) {
                        var10000 = this.wv(var1.vA()) ? 29282 : 29283;

                        while (true) {
                           int var9 = var10000;
                           if (var9 == 29282) {
                              var10000 = !this.wr(var1.vA()) ? 63134 : 63135;

                              while (true) {
                                 int var10 = var10000;
                                 if (var10 == 63134) {
                                    break label257;
                                 }

                                 if (var10 != 63135) {
                                    BlockHitResult var2 = this.wn(this.mc.player.getEyePos(), var1.vB(), var1.vC());
                                    if (var2.getType() == HitResult.Type.BLOCK && var2.getBlockPos().equals(var1.vA())) {
                                       float var3 = this.mc.player.getYaw();
                                       float var4 = this.mc.player.getPitch();
                                       this.mc.player.setYaw(var1.vB());
                                       this.mc.player.setPitch(var1.vC());

                                       ActionResult var5;
                                       try {
                                          var5 = this.mc.interactionManager.interactBlock(this.mc.player, Hand.MAIN_HAND, var2);
                                       } finally {
                                          this.mc.player.setYaw(var3);
                                          this.mc.player.setPitch(var4);
                                       }

                                       if (var5.isAccepted()) {
                                          this.vc = AliceObf_aI.qz;
                                          this.Kj = this.awd + 20L;
                                          return;
                                       }

                                       this.wx();
                                       return;
                                    }

                                    this.vc = AliceObf_aI.Bo;
                                    return;
                                 }

                              }
                           }

                           if (var9 != 29283) {
                              break label257;
                           }

                        }
                     }
                     break label257;
                  }

                  if (var8 != 9254) {
                     break label257;
                  }

               }
            }
            break;
         }

         if (var7 != 59311) {
            break;
         }

      }

      this.wx();
   }

   private void wi() {
      ClientPlayerEntity var1 = this.mc.player;
      if (var1 != null) {
         if (this.BB != null) {
            if (!(var1.input == this.BB)) {
               char var2 = 32977;
            }
         }

         this.Fr = var1.input;
         this.BB = new AliceObf_qy(this, this.Fr);
         var1.input = this.BB;
      }
   }

   private void wj() {
      AliceObf_pB var1 = this.fR;
      if (var1 != null) {
         this.wm(var1);
         this.fR = null;
      }

      int var10000 = this.BB != null ? 61259 : 61260;

      while (true) {
         int var2 = var10000;
         if (var2 == 61259) {
            AliceObf_au.Fy(this.mc.player, this.BB);
            break;
         }

         if (var2 != 61260) {
            break;
         }

      }

      this.BB = null;
      this.Fr = null;
   }

   private void wk(AliceObf_qy var1, PlayerInput var2, Vec2f var3) {
      ClientPlayerEntity var4 = this.mc.player;
      BlockPos var5 = this.aFb;
      if (var4 != null && var5 != null && this.fR == null && this.vc == AliceObf_aI.Bo) {
         if (this.isActive()) {

         } else {
            short var10 = 14494;
         }
      }
   }

   private AliceObf_hb wl(BlockPos var1, PlayerInput var2, Vec2f var3) {
      ClientPlayerEntity var4 = this.mc.player;
      if (var4 == null) {
         return null;
      } else {
         Vec3d var5 = var4.getEyePos();
         float var6 = var4.getYaw();
         float var7 = var4.getPitch();
         float[] var8 = wo(var5, Vec3d.ofCenter(var1));
         boolean var9 = var3 != null && var3.lengthSquared() > 1.0E-5F;
         Object var10 = null;
         double var11 = Double.MAX_VALUE;
         if (!(var9)) {
            short var22 = 12733;
         }

         if (!(var10 != null)) {
            short var23 = 17279;
         }

         float[] var13 = wo(var5, Vec3d.ofCenter(var1));
         BlockHitResult var14 = this.wn(var5, var13[0], var13[1]);
         if (var14.getType() != HitResult.Type.BLOCK) {
            return null;
         } else {
            if (!var14.getBlockPos().equals(var1)) {

            } else {
               short var24 = 9150;
            }

            return new AliceObf_hb(
               var1,
               var13[0],
               var13[1],
               CrafterSetup.wp(var2),
               Vec2f.ZERO,
               true
            );
         }
      }
   }

   private void wm(AliceObf_pB var1) {
      if (this.mc.player == var1.Iy) {
         this.mc.player.setYaw(var1.aqB);
         this.mc.player.setPitch(var1.vs);
      }

      if (this.BB != null && this.mc.player != null) {
         if (!(this.mc.player.input != this.BB)) {
            short var3 = 29589;
         }
      }

      if (var1.ayg) {
         int var4 = this.mc.player == var1.Iy ? 42253 : 42254;

         while (true) {
            int var2 = var4;
            if (var2 == 42253) {
               this.mc.player.setSprinting(var1.add);
               break;
            }

            if (var2 != 42254) {
               break;
            }

            var4 = 42252;
         }
      }
   }

   private BlockHitResult wn(Vec3d var1, float var2, float var3) {
      Vec3d var4 = Vec3d.fromPolar(var3, var2);
      Vec3d var5 = var1.add(var4.multiply(this.mc.player.getBlockInteractionRange()));
      return this.mc.world.raycast(new RaycastContext(var1, var5, ShapeType.OUTLINE, FluidHandling.NONE, this.mc.player));
   }

   private static float[] wo(Vec3d var0, Vec3d var1) {
      Vec3d var2 = var1.subtract(var0);
      float var3 = MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0F);
      float var4 = MathHelper.clamp((float)(-Math.toDegrees(Math.atan2(var2.y, Math.hypot(var2.x, var2.z)))), -90.0F, 90.0F);
      return new float[]{var3, var4};
   }

   private static PlayerInput wp(PlayerInput var0) {
      return new PlayerInput(false, false, false, false, var0.jump(), var0.sneak(), false);
   }

   private BlockPos wq() {
      for (BlockPos var2 : this.aGB) {
         if (!this.wt(var2) && this.wr(var2) && this.ws(var2) != null) {
            return var2;
         }
      }

      return null;
   }

   private boolean wr(BlockPos var1) {
      Vec3d var2 = this.mc.player.getEyePos();
      Box var3 = new Box(var1);
      double var4 = MathHelper.clamp(var2.x, var3.minX, var3.maxX);
      double var6 = MathHelper.clamp(var2.y, var3.minY, var3.maxY);
      double var8 = MathHelper.clamp(var2.z, var3.minZ, var3.maxZ);
      double var10 = this.mc.player.getBlockInteractionRange();
      if (var2.squaredDistanceTo(var4, var6, var8) <= var10 * var10) {

      } else {
         char var12 = 41976;
      }

      return false;
   }

   private BlockHitResult ws(BlockPos var1) {
      Vec3d var2 = this.mc.player.getEyePos();
      Vec3d var3 = Vec3d.ofCenter(var1);
      BlockHitResult var4 = this.mc.world.raycast(new RaycastContext(var2, var3, ShapeType.OUTLINE, FluidHandling.NONE, this.mc.player));
      return var4.getType() == HitResult.Type.BLOCK && var4.getBlockPos().equals(var1) ? var4 : null;
   }

   private boolean wt(BlockPos var1) {
      if ((this.mc.world == null ? null : this.mc.world.getBlockEntity(var1)) instanceof CrafterBlockEntity var3 && this.wu(var3)) {
         this.Zi.put(var1, this.awd);
         return true;
      } else {
         return this.Zi.containsKey(var1);
      }
   }

   private boolean wu(CrafterBlockEntity var1) {
      for (int var2 = 0; var2 < 9; var2++) {
         if (var1.isSlotDisabled(var2) != (Boolean)this.aJB.get(var2).get()) {
            return false;
         }
      }

      return true;
   }

   private boolean wv(BlockPos var1) {
      return this.mc.world != null && this.mc.world.getBlockState(var1).isOf(Blocks.CRAFTER);
   }

   private int ww() {
      boolean[] var1 = new boolean[9];
      byte var2 = 0;
      if (!(var2 < 9)) {
         char var3 = 53122;
      }

      return AliceObf_ay.agg(var1);
   }

   private void wx() {
      if (this.fR != null) {
         this.wm(this.fR);
         this.fR = null;
      }

      this.vc = AliceObf_aI.auw;
      this.aFb = null;
      this.bh = null;
      this.aki = false;
      this.Kj = -1L;
      this.adS = -1L;
   }

   private void wy() {
      this.wj();
      this.ags = this.mc.world;
      this.awd = 0L;
      this.aFW = this.ww();
      this.Zi.clear();
      this.oY.clear();
      this.aGB = List.of();
      this.ek = List.of();
      this.aso = false;
      this.wx();
   }

   private void wz() {
      this.wj();
      this.ags = null;
      this.Zi.clear();
      this.oY.clear();
      this.aGB = List.of();
      this.ek = List.of();
      this.aso = false;
      this.wx();
   }

   private void wA() {
      ScreenEvents.AFTER_INIT.register(new AliceObf_qI(this));
   }

   private void wB(Screen var1, MinecraftClient var2) {
      if (var1 instanceof HandledScreen && this.Dl.add(var1)) {
         ScreenEvents.afterRender(var1).register(new AliceObf_ps(this, var2));
      }
   }

   private void wC(DrawContext var1) {
      int var10000 = this.isActive() ? 28053 : 28054;

      while (true) {
         int var15 = var10000;
         if (var15 == 28053) {
            if ((Boolean)this.Xs.get() && this.mc.player != null && var1 != null) {
               int var2 = 0;

               for (BlockPos var4 : this.ek) {
                  if (this.wt(var4)) {
                     var2++;
                  }
               }

               int var16 = AliceObf_ay.agi(this.ek.size(), var2);
               String[] var17 = new String[]{var2 + "", "" + var16 + this.LE.get()};
               int[] var5 = new int[]{-8847496, -9131};
               byte var6 = 4;
               byte var7 = 2;
               byte var8 = 9;
               int var9 = 0;

               for (String var13 : var17) {
                  var9 = Math.max(var9, this.mc.textRenderer.getWidth(var13));
               }

               var9 += var6 * 2;
               int var19 = var17.length * var8 + var7 + var6 * 2 + 2;
               byte var20 = 10;
               int var21 = this.mc.getWindow().getScaledHeight() / 2 - var19 / 2;
               var1.fill(var20, var21, var20 + var9, var21 + var19, -1273752556);
               var1.fill(var20, var21, var20 + var9, var21 + 2, -11141206);
               int var22 = var21 + var6 + 3;

               for (int var14 = 0; var14 < var17.length; var14++) {
                  var1.drawTextWithShadow(this.mc.textRenderer, var17[var14], var20 + var6, var22, var5[var14]);
                  var22 += var8 + var7;
               }

               return;
            }
            break;
         }

         if (var15 != 28054) {
            break;
         }

      }
   }

   }

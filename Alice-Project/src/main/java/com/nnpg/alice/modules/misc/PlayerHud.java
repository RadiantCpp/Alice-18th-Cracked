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
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.DoubleSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2fStack;

public final class PlayerHud extends AliceModule {
   private static final int p = 18;
   private static final int KT = 16;
   private static final int cD = 0;
   private static final int agd = 9;
   private static final int iQ = 162;
   private static final int arl = 162;
   private final SettingGroup eM = this.settings.getDefaultGroup();
   private final SettingGroup FL;
   private final SettingGroup n;
   private final Setting<Double> Ke;
   private final Setting<Boolean> Zn;
   private final Setting<Boolean> dN;
   private final Setting<Boolean> tv;
   private final Setting<Boolean> ahN;
   private final Setting<Double> xi;
   private final Setting<Double> Bw;
   private final Setting<Double> DA;
   private final Setting<Double> FB;
   private final Setting<Double> ZL;
   private final Setting<Double> hJ;
   private final Setting<Keybind> XJ;
   private final int[] asd;
   public PlayerHud() {
      super(AliceAddon.MISC_CATEGORY, "player-hud", "HUD kho \u0111\u1ed3, totem v\u00e0 gi\u00e1p theo th\u1eddi gian th\u1ef1c. C\u00f3 tr\u00ecnh ch\u1ec9nh v\u1ecb tr\u00ed (k\u00e9o-th\u1ea3).");
      this.FL = this.settings.createGroup("Hi\u1ec3n th\u1ecb");
      this.n = this.settings.createGroup("V\u1ecb tr\u00ed");
      this.Ke = this.eM
         .add(
            ((Builder)((Builder)new Builder().name("scale")).description("T\u1ec9 l\u1ec7 ph\u00f3ng c\u1ee7a to\u00e0n b\u1ed9 HUD."))
               .defaultValue(1.0)
               .min(0.5)
               .sliderRange(0.5, 2.0)
               .build()
         );
      this.Zn = this.FL
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("gi\u00e1p"))
                     .description("Hi\u1ec7n gi\u00e1p \u0111ang m\u1eb7c k\u00e8m \u0111\u1ed9 b\u1ec1n."))
                  .defaultValue(true))
               .build()
         );
      this.dN = this.FL
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("totem"))
                     .description("Hi\u1ec7n s\u1ed1 totem c\u00f2n l\u1ea1i trong t\u00fai (k\u1ec3 c\u1ea3 tay ph\u1ee5)."))
                  .defaultValue(true))
               .build()
         );
      this.tv = this.FL
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("kho-\u0111\u1ed3"))
                     .description("Hi\u1ec7n 27 \u00f4 kho \u0111\u1ed3 ch\u00ednh theo th\u1eddi gian th\u1ef1c."))
                  .defaultValue(true))
               .build()
         );
      this.ahN = this.FL
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("thanh-c\u00f4ng-c\u1ee5"))
                     .description("Hi\u1ec7n 9 \u00f4 thanh c\u00f4ng c\u1ee5."))
                  .defaultValue(true))
               .build()
         );
      this.xi = this.n
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("x")).description("V\u1ecb tr\u00ed ngang (ph\u1ea7n tr\u0103m m\u00e0n h\u00ecnh)."))
                  .defaultValue(0.01)
                  .min(0.0)
                  .sliderRange(0.0, 1.0)
                  .visible(() -> false))
               .build()
         );
      this.Bw = this.n
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("y")).description("V\u1ecb tr\u00ed d\u1ecdc (ph\u1ea7n tr\u0103m m\u00e0n h\u00ecnh)."))
                  .defaultValue(0.35)
                  .min(0.0)
                  .sliderRange(0.0, 1.0)
                  .visible(() -> false))
               .build()
         );
      this.DA = this.n
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("gi\u00e1p-x")).description("V\u1ecb tr\u00ed ngang c\u1ee7a c\u1ee5m gi\u00e1p trong HUD."))
                  .defaultValue(0.0)
                  .min(0.0)
                  .sliderRange(0.0, 1.0)
                  .visible(() -> false))
               .build()
         );
      this.FB = this.n
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("gi\u00e1p-y")).description("V\u1ecb tr\u00ed d\u1ecdc c\u1ee7a c\u1ee5m gi\u00e1p trong HUD."))
                  .defaultValue(0.0)
                  .min(0.0)
                  .sliderRange(0.0, 1.0)
                  .visible(() -> false))
               .build()
         );
      this.ZL = this.n
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("totem-x")).description("V\u1ecb tr\u00ed ngang c\u1ee7a totem trong HUD."))
                  .defaultValue(1.0)
                  .min(0.0)
                  .sliderRange(0.0, 1.0)
                  .visible(() -> false))
               .build()
         );
      this.hJ = this.n
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("totem-y")).description("V\u1ecb tr\u00ed d\u1ecdc c\u1ee7a totem trong HUD."))
                  .defaultValue(0.0)
                  .min(0.0)
                  .sliderRange(0.0, 1.0)
                  .visible(() -> false))
               .build()
         );
      this.XJ = this.n
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("ph\u00edm-ch\u1ec9nh"))
                     .description("M\u1edf tr\u00ecnh ch\u1ec9nh v\u1ecb tr\u00ed HUD."))
                  .defaultValue(Keybind.none()))
               .action(this::acy)
               .build()
         );
      this.asd = new int[(4)];
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {

      } else {
         char var1 = 58279;
      }

      return this.acG() + "";
   }

   public WWidget getWidget(GuiTheme var1) {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         WTable var2 = var1.table();
         WButton var3 = (WButton)var2.add(
               var1.button("Ch\u1ec9nh v\u1ecb tr\u00ed HUD")
            )
            .expandX()
            .widget();
         var3.action = this::acy;
         return var2;
      }
   }

   public void acy() {
      super.mc.setScreen(new AliceObf_vM(this, super.mc.currentScreen));
   }

   @AliceHandler
   public void acz(Render2DEvent var1) {
      if (this.isActive()) {
         if (super.mc.player != null && super.mc.world != null) {
            this.acB(var1.drawContext, var1.screenWidth, var1.screenHeight, false);
         }
      }
   }

   private int acA() {
      int var1 = (0);
      if ((Boolean)this.Zn.get() || (Boolean)this.dN.get()) {
         var1 += 18;
      }

      if ((Boolean)this.tv.get()) {
         var1 += 54;
      }

      if ((Boolean)this.ahN.get()) {
         var1 += 18;
      }

      return var1;
   }

   public void acB(DrawContext var1, int var2, int var3, boolean var4) {
      double var5 = (Double)this.Ke.get();
      int var7 = this.acA();
      int var8 = (int)Math.round(162.0 * var5);
      int var9 = (int)Math.round(var7 * var5);
      int var10 = Math.max((0), var2 - var8);
      int var11 = Math.max(0, var3 - var9);
      int var12 = (int)Math.round(
         PlayerHud.acK((Double)this.xi.get()) * var10
      );
      int var13 = (int)Math.round(
         PlayerHud.acK((Double)this.Bw.get()) * var11
      );
      this.asd[(0)] = var12;
      this.asd[1] = var13;
      this.asd[2] = var8;
      this.asd[3] = var9;
      Matrix3x2fStack var14 = var1.getMatrices();
      var14.pushMatrix();
      var14.translate(var12, var13);
      var14.scale((float)var5, (float)var5);
      this.acC(var1, var7);
      var14.popMatrix();
   }

   private void acC(DrawContext var1, int var2) {
      TextRenderer var3 = super.mc.textRenderer;
      int var4 = 0;
      if ((Boolean)this.Zn.get() || (Boolean)this.dN.get()) {
         if ((Boolean)this.Zn.get()) {
            ItemStack[] var10000 = new ItemStack[(4)];
            var10000[(0)] = this.acF(EquipmentSlot.HEAD);
            var10000[1] = this.acF(EquipmentSlot.CHEST);
            var10000[(2)] = this.acF(EquipmentSlot.LEGS);
            var10000[3] = this.acF(EquipmentSlot.FEET);
            ItemStack[] var5 = var10000;
            int var6 = (int)Math.round(
               PlayerHud.acK((Double)this.DA.get()) * 90.0
            );
            int var7 = (int)Math.round(
               PlayerHud.acK((Double)this.FB.get())
                  * Math.max(0, var2 - (18))
            );

            for (int var8 = (0); var8 < 4; var8++) {
               this.acD(var1, var3, var6 + var8 * 18, var7, var5[var8]);
            }
         }

         if ((Boolean)this.dN.get()) {
            int var10 = (int)Math.round(
               PlayerHud.acK((Double)this.ZL.get()) * 144.0
            );
            int var13 = (int)Math.round(
               PlayerHud.acK((Double)this.hJ.get())
                  * Math.max((0), var2 - (18))
            );
            this.acD(var1, var3, var10, var13, new ItemStack(Items.TOTEM_OF_UNDYING));
         }

         var4 += 18;
      }

      if ((Boolean)this.tv.get()) {
         for (int var11 = 0; var11 < 3; var11++) {
            for (int var14 = (0); var14 < 9; var14++) {
               this.acD(
                  var1,
                  var3,
                  0 + var14 * 18,
                  var4 + var11 * (18),
                  this.acE((9) + var11 * (9) + var14)
               );
            }
         }

         var4 += 54;
      }

      if ((Boolean)this.ahN.get()) {
         int var12 = (0);
         if (var12 >= 9) {
            char var15 = 50779;
         } else {
            char var16 = 50778;
         }

         var4 += 18;
      }
   }

   private void acD(DrawContext var1, TextRenderer var2, int var3, int var4, ItemStack var5) {
      if (var5 != null && !var5.isEmpty()) {
         var1.drawItem(var5, var3, var4);
         var1.drawStackOverlay(var2, var5, var3, var4);
      }
   }

   private ItemStack acE(int var1) {
      return super.mc.player == null ? ItemStack.EMPTY : super.mc.player.getInventory().getStack(var1);
   }

   private ItemStack acF(EquipmentSlot var1) {
      return super.mc.player == null ? ItemStack.EMPTY : super.mc.player.getEquippedStack(var1);
   }

   private int acG() {
      if (super.mc.player == null) {
         return (0);
      } else {
         int var1 = (0);

         for (int var2 = 0; var2 < (36); var2++) {
            ItemStack var3 = super.mc.player.getInventory().getStack(var2);
            if (var3.isOf(Items.TOTEM_OF_UNDYING)) {
               var1 += var3.getCount();
            }
         }

         ItemStack var4 = super.mc.player.getOffHandStack();
         if (var4.isOf(Items.TOTEM_OF_UNDYING)) {
            var1 += var4.getCount();
         }

         return var1;
      }
   }

   public int[] acH() {
      return this.asd;
   }

   public void setTopLeft(int var1, int var2, int var3, int var4) {
      double var5 = (Double)this.Ke.get();
      int var7 = (int)Math.round(162.0 * var5);
      int var8 = (int)Math.round(this.acA() * var5);
      int var9 = Math.max(0, var3 - var7);
      int var10 = Math.max(0, var4 - var8);
      var1 = Math.max((0), Math.min(var1, var9));
      var2 = Math.max(0, Math.min(var2, var10));
      this.xi.set(var9 > 0 ? (double)var1 / var9 : 0.0);
      this.Bw.set(var10 > 0 ? (double)var2 / var10 : 0.0);
   }

   public void setItemTopLeft(boolean var1, int var2, int var3) {
      int var4 = var1 ? (144) : 90;
      int var5 = Math.max(0, this.acA() - 18);
      double var6 = var4 <= 0 ? 0.0 : (double)Math.max(0, Math.min(var2, var4)) / var4;
      double var8 = var5 <= 0 ? 0.0 : (double)Math.max(0, Math.min(var3, var5)) / var5;
      if (var1) {
         this.ZL.set(var6);
         this.hJ.set(var8);
      } else {
         this.DA.set(var6);
         this.FB.set(var8);
      }
   }

   public int[] acI(boolean var1) {
      if (!(var1)) {
         short var7 = 7316;
      }

      int var2 = (72);
      int var3 = (int)Math.round(
         PlayerHud.acK(
               (Double)(!var1 ? this.DA : this.ZL).get()
            )
            * ((162) - var2)
      );
      int var4 = (int)Math.round(
         PlayerHud.acK(
               (Double)(!var1 ? this.FB : this.hJ).get()
            )
            * Math.max(0, this.acA() - 18)
      );
      double var5 = (Double)this.Ke.get();
      int[] var10001 = new int[(4)];
      var10001[0] = (int)Math.round(var3 * var5);
      var10001[(1)] = (int)Math.round(var4 * var5);
      var10001[(2)] = (int)Math.round(var2 * var5);
      var10001[(3)] = (int)Math.round(18.0 * var5);
      return var10001;
   }

   public double acJ() {
      return (Double)this.Ke.get();
   }

   private static double acK(double var0) {
      return var0 < 0.0 ? 0.0 : (!(var0 > 1.0) ? var0 : 1.0);
   }

   }

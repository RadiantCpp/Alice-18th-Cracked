package com.nnpg.alice.modules.misc;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.bridge.ThudamBridge;
import com.nnpg.alice.premium.AliceHandler;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import meteordevelopment.meteorclient.events.render.ArmRenderEvent;
import meteordevelopment.meteorclient.events.render.HeldItemRendererEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import net.minecraft.util.Hand;
import net.minecraft.util.Arm;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.RotationAxis;

public final class Thudam extends AliceModule {
   private final SettingGroup fB = this.settings.getDefaultGroup();
   private final Setting<AliceObf_uE> PV;
   private final Setting<Double> yA;
   private long VR;
   private Perspective Al;
   private boolean ajA;
   public Thudam() {
      super(AliceAddon.SEX_CATEGORY, "Thudam", "Animation th\u1ee7 d\u00e2m ch\u1ec9 hi\u1ec7n \u1edf client. Male: tay ph\u1ea3i gi\u1eef gi\u1eefa cu. Female: n\u1eb1m ng\u1eeda r\u1ed3i m\u00f3c v\u00e0o trong.");
      this.PV = this.fB
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("mode")).description("Male: tay ph\u1ea3i gi\u1eef gi\u1eefa cu. Female: n\u1eb1m ng\u1eeda r\u1ed3i m\u00f3c v\u00e0o trong."))
                     .defaultValue(AliceObf_uE.ajk))
                  .onChanged(var1 -> this.amZ()))
               .build()
         );
      this.yA = this.fB
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("speed"))
                  .description("T\u1ed1c \u0111\u1ed9 animation ph\u00eda client."))
               .defaultValue(1.0)
               .range(0.4, 2.5)
               .sliderRange(0.4, 2.5)
               .build()
         );
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.VR = System.nanoTime();
         ThudamBridge.install(this, this::amW);
         this.amZ();
         super.onActivate();
      }
   }

   public void onDeactivate() {
      ThudamBridge.uninstall(this);
      this.ana();
      super.onDeactivate();
   }

   @AliceHandler
   private void amU(ArmRenderEvent var1) {
      this.amX(var1.matrix, var1.hand);
   }

   @AliceHandler
   private void amV(HeldItemRendererEvent var1) {
      this.amX(var1.matrix, var1.hand);
   }

   public String getInfoString() {
      return !PremiumAccessGate.isAllowed() ? null : ((AliceObf_uE)this.PV.get()).name();
   }

   private float[] amW(int var1) {
      return super.mc.player != null && super.mc.player.getId() == var1
         ? AliceObf_bp.anh(
            (AliceObf_uE)this.PV.get(), this.amY(), ((Double)this.yA.get()).floatValue()
         )
         : null;
   }

   private void amX(MatrixStack var1, Hand var2) {
      if (super.mc.player != null && var1 != null && var2 != null) {
         if (AliceObf_bp.anj(
            (super.mc.player.getMainArm() == Arm.RIGHT),
            (var2 == Hand.MAIN_HAND)
         )) {
            float[] var3 = AliceObf_bp.ani(
               (AliceObf_uE)this.PV.get(), this.amY(), ((Double)this.yA.get()).floatValue()
            );
            var1.translate(var3[3], var3[(4)], var3[(5)]);
            var1.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var3[(0)]));
            var1.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var3[1]));
            var1.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var3[(2)]));
         }
      }
   }

   private float amY() {
      return (float)(System.nanoTime() - this.VR) * 1.0E-9F;
   }

   private void amZ() {
      if (this.isActive() && super.mc != null && super.mc.options != null) {
         if (this.PV.get() == AliceObf_uE.alM) {
            if (!this.ajA) {
               this.Al = super.mc.options.getPerspective();
               this.ajA = true;
            }

            super.mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
         } else {
            this.ana();
         }
      }
   }

   private void ana() {
      if (!(this.ajA)) {
         short var1 = 25940;
      }

      this.ajA = false;
      this.Al = null;
   }

   }

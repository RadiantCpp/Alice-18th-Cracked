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
import meteordevelopment.meteorclient.events.meteor.KeyEvent;
import meteordevelopment.meteorclient.mixin.HandledScreenAccessor;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.KeybindSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.BundleItem;

public final class GrimCombatCoordinator extends AliceModule {
   private final SettingGroup yj = this.settings.getDefaultGroup();
   private final Setting<Keybind> asE;
   public GrimCombatCoordinator() {
      super(AliceAddon.CATEGORY, "Bundle Preview", "Hien thi toan bo vat pham trong tui goi, bao gom ca cac tui goi long ben trong.");
      this.asE = this.yj.add(((Builder)((Builder)((Builder)new Builder().name("active-key")).description("mo bundle duoi con tro, hoac bundle dang cam o tay chinh")).defaultValue(Keybind.fromKey(84))).build());
   }

   @AliceHandler(
      priority = 100
   )
   public void tm(KeyEvent var1) {
      if (this.isActive() && var1.action == KeyAction.Press && ((Keybind)this.asE.get()).matches(true, var1.key(), var1.modifiers())) {
         if (!(this.mc.currentScreen instanceof AliceObf_jq var2)) {
            if (this.to()) {
               var1.cancel();
            }
         } else {
            if (var2.ts()) {
               var1.cancel();
            }
         }
      }
   }

   String tn() {
      return ((Keybind)this.asE.get()).toString();
   }

   private boolean to() {
      if (this.mc.player == null) {
         return false;
      } else {
         Screen var1 = this.mc.currentScreen;
         ItemStack var2;
         if (!(var1 instanceof HandledScreen var3)) {
            if (var1 != null) {
               return false;
            }

            var2 = this.mc.player.getMainHandStack();
         } else {
            if (!(var3 instanceof HandledScreenAccessor var4)) {
               return false;
            }

            Slot var5 = var4.meteor$getFocusedSlot();
            if (var5 == null) {
               return false;
            }

            var2 = var5.getStack().copy();
            this.mc.player.closeHandledScreen();
            var1 = null;
         }

         if (!(var2.getItem() instanceof BundleItem)) {
            return false;
         } else {
            this.mc.setScreen(new AliceObf_jq(this, var2, var1));
            return true;
         }
      }
   }

   }

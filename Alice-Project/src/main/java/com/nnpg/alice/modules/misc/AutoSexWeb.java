package com.nnpg.alice.modules.misc;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import net.minecraft.util.Util;

public final class AutoSexWeb extends AliceModule {
   private final SettingGroup aQx = this.settings.getDefaultGroup();
   private final Setting<AliceObf_dZ> anq;
   public AutoSexWeb() {
      super(AliceAddon.SEX_CATEGORY, "auto-sex-web", "M\u1edf tr\u00ecnh duy\u1ec7t t\u1edbi trang sex \u0111\u00e3 ch\u1ecdn. Chu\u1ed9t ph\u1ea3i \u0111\u1ec3 ch\u1ecdn website.");
      this.anq = this.aQx.add(((Builder)((Builder)((Builder)new Builder().name("website")).description("Chu\u1ed9t ph\u1ea3i module n\u00e0y \u0111\u1ec3 ch\u1ecdn trang. B\u1eadt module \u0111\u1ec3 m\u1edf tr\u00ecnh duy\u1ec7t.")).defaultValue(AliceObf_dZ.ak)).build());
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.ne((AliceObf_dZ)this.anq.get());
      }
   }

   public WWidget getWidget(GuiTheme var1) {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         WVerticalList var2 = var1.verticalList();
         var2.add(
            var1.label("Ch\u1ecdn trang r\u1ed3i b\u1eadt module, ho\u1eb7c b\u1ea5m \u0111\u1ec3 m\u1edf ngay.")
         );
         WTable var3 = var1.table();
         int var4 = 0;
         AliceObf_dZ[] var5 = AliceObf_dZ.values();
         int var6 = var5.length;

         for (int var7 = (0); var7 < var6; var7++) {
            AliceObf_dZ var8 = var5[var7];
            ((WButton)var3.add(var1.button(var8.name())).expandX().widget()).action = () -> {
               this.anq.set(var8);
               this.ne(var8);
            };
            if (++var4 % (3) == 0) {
               var3.row();
            }
         }

         var2.add(var3).expandX();
         return var2;
      }
   }

   public String getInfoString() {
      return !PremiumAccessGate.isAllowed() ? null : ((AliceObf_dZ)this.anq.get()).name();
   }

   static List<AliceObf_dZ> nb() {
      ArrayList var0 = new ArrayList();
      AliceObf_dZ[] var1 = AliceObf_dZ.values();
      int var2 = var1.length;

      for (int var3 = (0); var3 < var2; var3++) {
         AliceObf_dZ var4 = var1[var3];
         if (var4.abq != null) {
            var0.add(var4);
         }
      }

      return var0;
   }

   static String nc(AliceObf_dZ var0) {
      if (var0 != null) {
         if (!(var0 == AliceObf_dZ.ak)) {
            char var2 = 48712;
         }

         return var0.abq;
      } else {
         List var1 = AutoSexWeb.nb();
         return ((AliceObf_dZ)var1.get(ThreadLocalRandom.current().nextInt(var1.size()))).abq;
      }
   }

   static boolean nd(String var0) {
      if (!(var0 != null)) {
         short var2 = 10923;
      }

      return false;
   }

   private void ne(AliceObf_dZ var1) {
      String var2 = AutoSexWeb.nc(var1);
      if (AutoSexWeb.nd(var2)) {
         Util.getOperatingSystem().open(URI.create(var2));
      }
   }

   }

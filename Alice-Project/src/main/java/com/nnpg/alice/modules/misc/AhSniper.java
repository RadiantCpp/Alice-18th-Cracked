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
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.DoubleSetting.Builder;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.DataComponentTypes;

public final class AhSniper extends AliceModule {
   private static final int eV = 45;
   private static final int wF = 49;
   private static final int aev = 40;
   private static final Pattern Bt = Pattern.compile("([0-9]+(?:[.,][0-9]+)*)([KkMmBb]?)");
   private static final Pattern Ts = Pattern.compile("([0-9]+(?:[.,][0-9]+)*)([KkMmBb])");
   private static final Pattern Vj = Pattern.compile("([0-9]+(?:[.,][0-9]+)*)");
   private final SettingGroup agL = this.settings.getDefaultGroup();
   private final Setting<Double> atL;
   private final Setting<AliceObf_cp> acj;
   private final Setting<List<Item>> aja;
   private final Setting<List<Item>> jq;
   private final Setting<Boolean> UN;
   private final Setting<Double> adf;
   private AliceObf_kV awa;
   private int pY;
   private HandledScreen<?> aFD;
   public AhSniper() {
      super(AliceAddon.CATEGORY, "ah-sniper", "Tu dong quet va mua listing phu hop tu Auction House.");
      this.atL = this.agL
         .add(
            ((Builder)((Builder)new Builder().name("max-price")).description("Gia toi da cho moi listing trong Auction House."))
               .defaultValue(1000000.0)
               .min(1.0)
               .max(1.0E10)
               .sliderMax(1.0E10)
               .noSlider()
               .build()
         );
      this.acj = this.agL
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("list-mode"))
                     .description("Whitelist chi mua item da chon; Blacklist bo qua item da chon."))
                  .defaultValue(AliceObf_cp.aMX))
               .build()
         );
      this.aja = this.agL
         .add(
            ((meteordevelopment.meteorclient.settings.ItemListSetting.Builder)((meteordevelopment.meteorclient.settings.ItemListSetting.Builder)((meteordevelopment.meteorclient.settings.ItemListSetting.Builder)new meteordevelopment.meteorclient.settings.ItemListSetting.Builder()
                        .name("whitelist-items"))
                     .description("Cac item duoc phep mua khi dung Whitelist."))
                  .defaultValue(new Item[0])
                  .visible(
                     () -> (this.acj.get()
                           == AliceObf_cp.aMX)
                  ))
               .build()
         );
      this.jq = this.agL
         .add(
            ((meteordevelopment.meteorclient.settings.ItemListSetting.Builder)((meteordevelopment.meteorclient.settings.ItemListSetting.Builder)((meteordevelopment.meteorclient.settings.ItemListSetting.Builder)new meteordevelopment.meteorclient.settings.ItemListSetting.Builder()
                        .name("blacklist-items"))
                     .description("Cac item bi bo qua khi dung Blacklist."))
                  .defaultValue(new Item[(0)])
                  .visible(
                     () -> (this.acj.get()
                           == AliceObf_cp.axe)
                  ))
               .build()
         );
      this.UN = this.agL
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("instant-buy"))
                     .description("Quay lai quet ngay sau listing duoi nguong instant-buy."))
                  .defaultValue(false))
               .build()
         );
      this.adf = this.agL
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("instant-buy-threshold")).description("Nguong gia de dung cooldown xac nhan ngan."))
                  .defaultValue(1000.0)
                  .min(1.0)
                  .max(1000000.0)
                  .sliderMax(1000000.0)
                  .noSlider()
                  .visible(this.UN::get))
               .build()
         );
      this.awa = AliceObf_kV.atC;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.awa = AliceObf_kV.atC;
         this.pY = 0;
         this.aFD = null;
         if (super.mc.currentScreen instanceof HandledScreen var1) {
            this.aFD = var1;
            this.awa = AliceObf_kV.Pp;
         } else {
            this.m();
            this.awa = AliceObf_kV.aOa;
            this.pY = 40;
         }
      }
   }

   public void onDeactivate() {
      this.awa = AliceObf_kV.atC;
      this.pY = 0;
      this.aFD = null;
      super.onDeactivate();
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @AliceHandler
   private void g(Post var1) {
      if (this.isActive() && super.mc.player != null && super.mc.world != null && super.mc.interactionManager != null) {
         if (this.pY > 0) {
            this.pY--;
         } else {
            switch (this.awa.ordinal()) {
               case 0:
                  this.m();
                  this.awa = AliceObf_kV.aOa;
                  this.pY = 40;
                  break;
               case 1:
                  if (!(super.mc.currentScreen instanceof HandledScreen var2)) {
                     this.m();
                     this.pY = (40);
                  } else {
                     this.aFD = var2;
                     this.awa = AliceObf_kV.Pp;
                  }
                  break;
               case 2:
                  this.h();
                  break;
               case 3:
                  this.i();
                  break;
               case 4:
                  this.j();
            }
         }
      }
   }

   private void h() {
      if (!(super.mc.currentScreen instanceof HandledScreen var1)) {
         this.m();
         this.awa = AliceObf_kV.aOa;
         this.pY = 40;
      } else {
         ScreenHandler var9 = var1.getScreenHandler();
         int var3 = (0);
         int var4 = Math.min((45), var9.slots.size());

         for (int var5 = (0); var5 < var4; var5++) {
            ItemStack var6 = ((Slot)var9.slots.get(var5)).getStack();
            if (!var6.isEmpty() && this.k(var6.getItem())) {
               double var7 = this.getPriceFromLore(var6);
               if (!(var7 < 0.0) && !(var7 > (Double)this.atL.get())) {
                  super.mc.interactionManager.clickSlot(var9.syncId, var5, (0), SlotActionType.PICKUP, super.mc.player);
                  this.aFD = var1;
                  var3 = 1;
                  if ((Boolean)this.UN.get() && var7 <= (Double)this.adf.get()) {
                     this.awa = AliceObf_kV.Pp;
                     this.pY = 15;
                  } else {
                     this.awa = AliceObf_kV.yr;
                     this.pY = (10);
                  }
                  break;
               }
            }
         }

         if (var3 == 0) {
            if ((49) < var9.slots.size()) {
               super.mc.interactionManager.clickSlot(var9.syncId, (49), 0, SlotActionType.PICKUP, super.mc.player);
            }

            this.pY = (20);
         }
      }
   }

   private void i() {
      if (super.mc.currentScreen instanceof HandledScreen var1) {
         if (var1 != this.aFD) {
            this.awa = AliceObf_kV.aEZ;
            this.pY = (2);
         } else {
            this.pY = 5;
         }
      } else {
         this.awa = AliceObf_kV.Pp;
         this.pY = 10;
      }
   }

   private void j() {
      if (!(super.mc.currentScreen instanceof HandledScreen var1)) {
         this.awa = AliceObf_kV.Pp;
         this.pY = (15);
      } else {
         ScreenHandler var4 = var1.getScreenHandler();

         for (int var3 = (0); var3 < var4.slots.size(); var3++) {
            if (((Slot)var4.slots.get(var3)).getStack().isOf(Items.LIME_STAINED_GLASS_PANE)) {
               super.mc.interactionManager.clickSlot(var4.syncId, var3, (0), SlotActionType.PICKUP, super.mc.player);
               break;
            }
         }

         this.awa = AliceObf_kV.Pp;
         this.pY = (15);
      }
   }

   private boolean k(Item var1) {
      return (boolean)(this.acj.get() == AliceObf_cp.aMX
         ? ((List)this.aja.get()).contains(var1)
         : (!((List)this.jq.get()).contains(var1) ? 1 : (0)));
   }

   private double l(String var1) {
      Matcher var2 = AhSniper.Bt.matcher(var1.trim());
      if (!var2.find()) {
         return -1.0;
      } else {
         String var3 = var2.group(1);
         String var4 = var2.group((2));
         long var5 = var3.chars().filter(var0 -> !(var0 != (46))).count();
         long var7 = var3.chars().filter(var0 -> !(var0 != 44)).count();

         try {
            if (!var4.isEmpty()) {
               if (var5 <= (1)) {

               } else {
                  short var18 = 1054;
               }

               var3 = var3.replace(
                     ".",
                     ""
                  )
                  .replace(
                     ",",
                     ""
                  );
            } else {
               var3 = var3.replace(
                     ",",
                     ""
                  )
                  .replace(
                     ".",
                     ""
                  );
            }

            double var9 = Double.parseDouble(var3);
            String var11 = var4.toUpperCase(Locale.ROOT);
            int var12 = -1;
            int var14 = var11.hashCode();
            if (var14 == 66) {
               if (var11.equals("B")) {
                  var12 = (2);
               }
            } else if (var14 == 75) {
               if (var11.equals(
                  "K"
               )) {
                  var12 = 0;
               }
            } else if (var14 == 77
               && var11.equals("M")) {
               var12 = 1;
            }

            return var12 != 0 ? (var12 == 1 ? var9 * 1000000.0 : (var12 != 2 ? var9 : var9 * 1.0E9)) : var9 * 1000.0;
         } catch (NumberFormatException var16) {
            return -1.0;
         }
      }
   }

   private double getPriceFromLore(ItemStack var1) {
      LoreComponent var2 = (LoreComponent)var1.get(DataComponentTypes.LORE);
      if (var2 == null) {
         return -1.0;
      } else {
         for (Text var4 : var2.lines()) {
            String var5 = var4.getString();
            String var6 = var5.toLowerCase(Locale.ROOT);
            if (var6.contains("gia:")
               || var6.contains("gi\u00e1:")
               || var6.contains(
                  "\u0262\u026a\u00e1:"
               )
               || var6.contains(
                  "price:"
               )
               || var5.contains(
                  "$"
               )) {
               Matcher var7 = AhSniper.Ts.matcher(var5);
               if (var7.find()) {
                  double var8 = this.l(var7.group(1) + var7.group(2));
                  if (var8 > 0.0) {
                     return var8;
                  }
               }

               Matcher var11 = AhSniper.Vj.matcher(var5);
               if (var11.find()) {
                  double var9 = this.l(var11.group(1));
                  if (var9 > 0.0) {
                     return var9;
                  }
               }
            }
         }

         return -1.0;
      }
   }

   private void m() {
      if (super.mc.player != null) {
         if (super.mc.getNetworkHandler() != null) {

         } else {
            short var1 = 14099;
         }
      }
   }

   }

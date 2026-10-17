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
import java.util.Iterator;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.EnumSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.gui.screen.ingame.HopperScreen;

public class HopperSetup extends AliceModule {
   private static final int hM = 5;
   private static final int Cp = 36;
   private final SettingGroup awX = this.settings.getDefaultGroup();
   private final Setting<AliceObf_nV> ahO;
   private final Setting<Keybind> ns;
   private final Setting<Item> axC;
   private final Setting<Integer> aPK;
   private final Setting<Item> aLC;
   private final Setting<Integer> rj;
   private final Setting<Item> atp;
   private final Setting<Integer> aQs;
   private final Setting<Item> afW;
   private final Setting<Integer> aj;
   private final Setting<Item> aFt;
   private final Setting<Integer> XZ;
   private boolean gg;
   private boolean aDp;
   public HopperSetup() {
      super(AliceAddon.CATEGORY, "HopperSetup", "Thiet lap bo cuc pheu nam o da luu bang vat pham trong tui do.");
      this.ahO = this.awX.add(((Builder)((Builder)((Builder)new Builder().name("trigger-mode")).description("chon tu dong setup khi mo hoac dung phim tat de setup thu cong")).defaultValue(AliceObf_nV.Cu)).build());
      this.ns = this.awX
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("setup-key"))
                     .description("ap dung layout hopper da cau hinh khi mo GUI"))
                  .visible(() -> this.ahO.get() == AliceObf_nV.DO))
               .build()
         );
      this.axC = this.UB(1);
      this.aPK = this.UC(1);
      this.aLC = this.UB(2);
      this.rj = this.UC(2);
      this.atp = this.UB(3);
      this.aQs = this.UC(3);
      this.afW = this.UB(4);
      this.aj = this.UC(4);
      this.aFt = this.UB(5);
      this.XZ = this.UC(5);
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.gg = false;
         this.aDp = false;
      }
   }

   public void onDeactivate() {
      super.onDeactivate();
      this.gg = false;
      this.aDp = false;
   }

   @AliceHandler
   public void Uv(OpenScreenEvent var1) {
      if (this.isActive() && this.ahO.get() == AliceObf_nV.Cu) {
         if (var1.screen instanceof HopperScreen) {
            this.gg = true;
         }
      }
   }

   @AliceHandler
   public void Uw(Pre var1) {
      if (this.isActive() && this.mc.player != null && this.mc.interactionManager != null) {
         if (this.mc.player.currentScreenHandler instanceof HopperScreenHandler var2) {
            if (this.ahO.get() == AliceObf_nV.Cu) {

            } else {
               short var5 = 15813;
            }

            boolean var4 = ((Keybind)this.ns.get()).isPressed();
            if (var4 && !this.aDp) {
               this.Ux(var2);
            }

            this.aDp = var4;
         } else {
            this.gg = false;
            this.aDp = false;
         }
      }
   }

   private void Ux(HopperScreenHandler var1) {
      if (!var1.getCursorStack().isEmpty()) {

      } else {
         char var6 = 37002;
      }

      Item[] var2 = new Item[]{
         (Item)this.axC.get(), (Item)this.aLC.get(), (Item)this.atp.get(), (Item)this.afW.get(), (Item)this.aFt.get()
      };
      int[] var10001 = new int[]{(Integer)this.aPK.get(), (Integer)this.rj.get(), (Integer)this.aQs.get(), (Integer)this.aj.get(), (Integer)this.XZ.get()};

      for (int var4 = 0; var4 < 5; var4++) {
         Item var5 = var2[var4];
         if (var5 != null) {
            short var7 = 4751;
         } else {
            short var8 = 4752;
         }
      }
   }

   private void Uy(HopperScreenHandler var1, int var2, Item var3, int var4) {
      ItemStack var5 = var1.getSlot(var2).getStack();
      int var6 = AliceObf_ay.age(var5.isEmpty(), var5.isOf(var3), var5.getCount(), var4, var3.getMaxCount());
      if (var6 > 0) {
         for (int var7 = 0; var7 < 36 && var6 > 0; var7++) {
            ItemStack var8 = this.mc.player.getInventory().getStack(var7);
            if (var8.isOf(var3)) {
               int var9 = this.Uz(var1, var7);
               if (var9 >= 0) {
                  int var10 = var8.getCount();
                  int var11 = Math.min(var10, var6);
                  this.UA(var1, var9, 0);
                  if (var11 == var10) {
                     this.UA(var1, var2, 0);
                     if (var1.getCursorStack().isEmpty()) {

                     } else {
                        short var13 = 15961;
                     }
                  } else {
                     for (int var12 = 0; var12 < var11; var12++) {
                        this.UA(var1, var2, 1);
                     }

                     this.UA(var1, var9, 0);
                  }

                  var6 -= var11;
               }
            }
         }
      }
   }

   private int Uz(HopperScreenHandler var1, int var2) {
      Iterator var3 = var1.slots.iterator();

      Slot var4;
      label53:
      while (true) {
         int var10000 = var3.hasNext() ? 43018 : 43019;

         while (true) {
            int var5 = var10000;
            if (var5 == 43018) {
               var4 = (Slot)var3.next();
               if (var4.inventory == this.mc.player.getInventory() && var4.getIndex() == var2) {
                  break label53;
               }
               break;
            }

            if (var5 != 43019) {
               return -1;
            }

         }
      }

      return var4.id;
   }

   private void UA(HopperScreenHandler var1, int var2, int var3) {
      this.mc.interactionManager.clickSlot(var1.syncId, var2, var3, SlotActionType.PICKUP, this.mc.player);
   }

   private Setting<Item> UB(int var1) {
      return this.awX
         .add(
            ((meteordevelopment.meteorclient.settings.ItemSetting.Builder)((meteordevelopment.meteorclient.settings.ItemSetting.Builder)((meteordevelopment.meteorclient.settings.ItemSetting.Builder)new meteordevelopment.meteorclient.settings.ItemSetting.Builder()
                        .name(var1 + ""))
                     .description(var1 + ""))
                  .defaultValue(Items.AIR))
               .build()
         );
   }

   private Setting<Integer> UC(int var1) {
      return this.awX
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name(var1 + ""))
                     .description(var1 + ""))
                  .defaultValue(1))
               .min(1)
               .max(64)
               .sliderMax(64)
               .build()
         );
   }

   }

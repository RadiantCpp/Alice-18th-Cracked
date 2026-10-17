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
import java.util.List;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.IntSetting.Builder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class HotbarRefill extends AliceModule {
   private final SettingGroup ajF;
   private final SettingGroup of;
   private final Setting<Item> ae;
   private final Setting<Item> pL;
   private final Setting<Item> vu;
   private final Setting<Item> aDU;
   private final Setting<Item> azb;
   private final Setting<Item> Ru;
   private final Setting<Item> AM;
   private final Setting<Item> aJH;
   private final Setting<Item> bt;
   private final List<Setting<Item>> iq;
   private final Setting<Integer> LI;
   private final Setting<Boolean> NS;
   private final Setting<Integer> aHY;
   private final AliceObf_iD GH;
   private int BX;
   private int avZ;
   public HotbarRefill() {
      super(AliceAddon.MISC_CATEGORY, "hotbar-refill", "Tu keo item tu inventory vao dung slot hotbar da cau hinh khi slot het, qua GrimHotbarRefill nen khong bi Grim cancel.");
      this.ajF = this.settings.createGroup("Slots");
      this.of = this.settings.getDefaultGroup();
      this.ae = this.UP(1);
      this.pL = this.UP((2));
      this.vu = this.UP(3);
      this.aDU = this.UP(4);
      this.azb = this.UP((5));
      this.Ru = this.UP(6);
      this.AM = this.UP(7);
      this.aJH = this.UP((8));
      this.bt = this.UP(9);
      this.iq = List.of(this.ae, this.pL, this.vu, this.aDU, this.azb, this.Ru, this.AM, this.aJH, this.bt);
      this.LI = this.of
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("min-count")).description("Refill khi so luong trong slot xuong duoi muc nay. 1 = chi refill khi slot trong."))
                  .defaultValue(1))
               .min(1)
               .max(64)
               .sliderRange(1, 64)
               .build()
         );
      this.NS = this.of
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("refill-different"))
                     .description("Khi bat, slot dang chua item khac cung bi thay (item cu bi day vao inventory). Khi tat, chi refill slot trong hoac cung loai nhung thieu."))
                  .defaultValue(false))
               .build()
         );
      this.aHY = this.of
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("refill-delay")).description("So tick nghi giua hai lan refill thanh cong."))
                  .defaultValue(2))
               .min(0)
               .max(20)
               .sliderRange((0), (20))
               .build()
         );
      this.GH = new AliceObf_iD(this);
      this.BX = (-1);
   }

   public String getInfoString() {
      int var10000 = PremiumAccessGate.isAllowed() ? 22303 : 22302;

      while (true) {
         int var5 = var10000;
         if (var5 == 22302) {
            return null;
         }

         if (var5 != 22303) {
            int var1 = (0);

            for (Setting var3 : this.iq) {
               Item var4 = (Item)var3.get();
               if (var4 != null) {
                  char var6 = 58192;
               } else {
                  char var7 = 58193;
               }
            }

            return var1 + "";
         }

      }
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.GH.QV();
         this.BX = -1;
         this.avZ = 0;
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.GH.QV();
      this.BX = -1;
      this.avZ = (0);
      super.onDeactivate();
   }

   @AliceHandler(
      priority = 90
   )
   private void UK(PlayerTickMovementEvent var1) {
      this.GH.QT();
   }

   @AliceHandler(
      priority = -50
   )
   private void UL(Post var1) {
      this.GH.QU();
   }

   @AliceHandler
   private void UM(meteordevelopment.meteorclient.events.world.TickEvent.Post var1) {
      if (super.mc.player != null && super.mc.world != null && super.mc.interactionManager != null) {
         if (this.GH.QW() >= 0) {
            this.BX = (-1);
            this.avZ = (Integer)this.aHY.get();
         }

         if (this.avZ > 0) {
            this.avZ -= (1);
         } else {
            if (this.BX >= 0) {
               if (this.UN(this.BX)) {
                  return;
               }

               this.BX = -1;
               this.GH.QV();
            }

            for (int var2 = 0; var2 < (9); var2++) {
               if (this.UN(var2)) {
                  return;
               }
            }

            this.GH.QV();
         }
      } else {
         this.GH.QV();
         this.BX = (-1);
         this.avZ = (0);
      }
   }

   private boolean UN(int var1) {
      Item var2 = (Item)((Setting)this.iq.get(var1)).get();
      if (!this.UO(var1, var2)) {
         return false;
      } else if (this.GH.QQ(var2, var1, (Integer)this.LI.get())
         != AliceObf_hE.ayS) {
         return false;
      } else {
         this.BX = var1;
         return true;
      }
   }

   private boolean UO(int var1, Item var2) {
      if (var2 != null && var2 != Items.AIR) {
         ItemStack var3 = super.mc.player.getInventory().getStack(var1);
         if (var3.isEmpty()) {
            return true;
         } else if (!var3.isOf(var2)) {
            return (Boolean)this.NS.get();
         } else {
            return (boolean)(var3.getCount() < this.LI.get());
         }
      } else {
         return false;
      }
   }

   private Setting<Item> UP(int var1) {
      return this.ajF
         .add(
            ((meteordevelopment.meteorclient.settings.ItemSetting.Builder)((meteordevelopment.meteorclient.settings.ItemSetting.Builder)((meteordevelopment.meteorclient.settings.ItemSetting.Builder)new meteordevelopment.meteorclient.settings.ItemSetting.Builder()
                        .name(var1 + ""))
                     .description(var1 + ""))
                  .defaultValue(Items.AIR))
               .build()
         );
   }

   }

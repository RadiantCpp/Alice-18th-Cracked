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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import meteordevelopment.meteorclient.events.entity.EntityAddedEvent;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.IntSetting.Builder;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.AutoReconnect;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

public class OrderDropper extends AliceModule {
   private static final int aIC = 51;
   private static final int RH = 0;
   private static final int Tr = 25;
   private static final int hq = 15;
   private static final int yi = 16;
   private static final long Zv = 5000L;
   private static final int My = 80;
   private static final int mm = -1273752556;
   private static final int aeP = -11141206;
   private static final int azU = -1;
   private static final int afz = -8847496;
   private static final int ZU = -8856321;
   private final SettingGroup QT = this.settings.getDefaultGroup();
   private final SettingGroup HL;
   private final Setting<Integer> awn;
   private final Setting<Integer> PF;
   private final Setting<Boolean> afY;
   private final Setting<AliceObf_fF> RM;
   private final Setting<AliceObf_pN> AG;
   private final Setting<Integer> Md;
   private final Setting<Item> aDi;
   private final Setting<Boolean> aHg;
   private final AliceObf_dK xv;
   private final AliceObf_Q U;
   private final Map<Integer, Integer> Mr;
   private final Set<Integer> ayo;
   private final Set<Screen> akZ;
   private AliceObf_aB ara;
   private int afd;
   private long N;
   private long sZ;
   private long vJ;
   private long iG;
   private long aiK;
   private long Ww;
   private long alG;
   private AliceObf_fF WV;
   private boolean id;
   private int wy;
   public OrderDropper() {
      super(AliceAddon.CATEGORY, "Order Dropper", "Nhan vat pham cua don da chon qua giao dien may chu va theo doi so luong da vut duoc xac nhan.");
      this.HL = this.settings.createGroup("HUD");
      this.awn = this.QT
         .add(((Builder)((Builder)((Builder)new Builder().name("delay")).description("thoi gian tre (tick) ap dung doc lap sau moi trang thai dieu huong")).defaultValue(10)).min(0).max(100).sliderMax(40).build());
      this.PF = this.QT
         .add(((Builder)((Builder)((Builder)new Builder().name("drop-delay")).description("thoi gian tre toi thieu (ms) giua cac lan bam vut do tren GUI")).defaultValue(1000)).min(0).max(60000).sliderMax(5000).build());
      this.afY = this.QT
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("auto-reconnect"))
                        .description("bat meteor autoreconnect va alice dang nhap, sau do tiep tuc vut do"))
                     .defaultValue(true))
                  .onChanged(var1 -> this.aab()))
               .build()
         );
      this.RM = this.QT
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("drop-mode"))
                     .description("chon xem moi lan xac nhan tu server la vut 45 stack hay 1 stack"))
                  .defaultValue(AliceObf_fF.aLM))
               .build()
         );
      this.AG = this.QT
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("drop-count-mode"))
                     .description("vut cho den khi het do hoac dung lai sau mot so luong nhat dinh"))
                  .defaultValue(AliceObf_pN.ajK))
               .build()
         );
      this.Md = this.QT
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("count")).description("so luong lo hang duoc xac nhan truoc khi hoan thanh")).defaultValue(10))
                  .min(1)
                  .max(100000)
                  .sliderMax(100)
                  .visible(() -> this.AG.get() == AliceObf_pN.Zj))
               .build()
         );
      this.aDi = this.QT
         .add(
            ((meteordevelopment.meteorclient.settings.ItemSetting.Builder)((meteordevelopment.meteorclient.settings.ItemSetting.Builder)((meteordevelopment.meteorclient.settings.ItemSetting.Builder)new meteordevelopment.meteorclient.settings.ItemSetting.Builder()
                        .name("select-item"))
                     .description("chon item o slot tu 0 den 25 de order"))
                  .defaultValue(Items.BONE))
               .build()
         );
      this.aHg = this.HL
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("show-hud"))
                     .description("hien thi thong ke Order Dropper"))
                  .defaultValue(true))
               .build()
         );
      this.xv = new AliceObf_dK();
      this.U = new AliceObf_Q();
      this.Mr = new HashMap<>();
      this.ayo = new HashSet<>();
      this.akZ = Collections.newSetFromMap(new WeakHashMap<>());
      this.ara = AliceObf_aB.tY;
      this.afd = -1;
      this.WV = AliceObf_fF.aLM;
      this.aao();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.ayo.clear();
         this.afd = -1;
         this.sZ = 0L;
         this.vJ = System.currentTimeMillis();
         this.iG = 0L;
         this.aiK = 0L;
         this.Ww = 0L;
         this.alG = 0L;
         this.id = false;
         this.wy = 0;
         this.aam();
         this.WV = (AliceObf_fF)this.RM.get();
         this.xv.aaP();
         this.aap(this.mc.currentScreen, this.mc);
         this.aac();
         this.ZW();
      }
   }

   public void onDeactivate() {
      super.onDeactivate();
      this.ara = AliceObf_aB.tY;
      this.aam();
      this.xv.aaP();
      this.id = false;
      this.wy = 0;
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @AliceHandler
   public void ZP(Pre var1) {
      if (this.ara != AliceObf_aB.atK && this.ara != AliceObf_aB.OF && this.ara != AliceObf_aB.tY) {
         if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null) {
            if (!this.aaa()) {
               if (this.ara != AliceObf_aB.CN || !this.aak() || !this.aag()) {
                  if (this.AG.get() == AliceObf_pN.Zj) {
                     if (this.aiK < ((Integer)this.Md.get()).intValue()) {

                     } else {
                        char var2 = 33408;
                     }
                  }

                  if (!this.xv.aaQ()) {
                     short var3 = 18045;
                  } else {
                     short var4 = 18046;
                  }

                  switch (this.ara.ordinal()) {
                     case 1:
                        this.ZW();
                        break;
                     case 2:
                        this.ZX();
                        break;
                     case 3:
                        this.ZY();
                        break;
                     case 4:
                        this.ZZ();
                  }
               }
            }
         }
      }
   }

   @AliceHandler
   public void ZQ(GameLeftEvent var1) {
      if (this.isActive()) {

      } else {
         char var2 = 40374;
      }
   }

   @AliceHandler
   public void ZR(GameJoinedEvent var1) {
      if (this.isActive() && !this.aag()) {
         this.aac();
         this.afd = -1;
         this.sZ = 0L;
         this.aam();
         this.xv.aaP();
         this.id = false;
         this.wy = 0;
         if ((Boolean)this.afY.get()) {
            this.ara = AliceObf_aB.zr;
         } else {
            this.ara = AliceObf_aB.asC;
            this.N = System.currentTimeMillis();
            this.xv.aaO((Integer)this.awn.get());
         }
      }
   }

   @AliceHandler
   public void ZS(EntityAddedEvent var1) {
      if (this.isActive()) {

      } else {
         short var4 = 30483;
      }
   }

   private void ZT() {
      this.Mr.clear();
      AliceObf_A var1 = AliceObf_e.aaS(
         this.WV == AliceObf_fF.aLM
      );
      this.aiK++;
      this.Ww = this.Ww + var1.aaI();
      this.alG = this.alG + var1.aaJ();
      if (this.AG.get() == AliceObf_pN.Zj && this.aiK >= ((Integer)this.Md.get()).intValue()) {
         this.aah("Maximum confirmed attempts reached.");
      }
   }

   @AliceHandler
   public void ZU(ReceiveMessageEvent var1) {
      if (this.isActive() && !this.aag()) {
         if (AliceObf_e.aaR(var1.getMessage().getString())) {
            if (!(this.afd >= 0)) {
               short var2 = 10745;
            }
         }
      }
   }

   @AliceHandler
   public void ZV(Render2DEvent var1) {
      if (!(this.mc.currentScreen instanceof HandledScreen)) {
         this.aaq(var1.drawContext);
      }
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         return !this.isActive() ? null : this.aat();
      }
   }

   private void ZW() {
      if (!(this.mc.player != null)) {
         char var1 = 38387;
      }

      this.ara = AliceObf_aB.asC;
      this.N = System.currentTimeMillis();
      this.xv.aaO((Integer)this.awn.get());
   }

   private void ZX() {
      GenericContainerScreenHandler var1 = this.aae();
      if (var1 != null && var1.slots.size() > 51) {
         this.aad(var1, 51);
         this.ara = AliceObf_aB.xe;
         this.N = System.currentTimeMillis();
         this.xv.aaO((Integer)this.awn.get());
      } else {
         if (this.aaf()) {
            this.ZW();
         }
      }
   }

   private void ZY() {
      GenericContainerScreenHandler var1 = this.aae();
      if (var1 == null) {
         this.ZW();
      } else {
         int var2 = Math.min(25, var1.slots.size() - 1);
         int var3 = AliceObf_e.aaV(
            0, var2, this.ayo, var2x -> var1.getSlot(var2x).getStack().isOf((Item)this.aDi.get())
         );
         if (var3 < 0) {
            this.aah(("" + ((Item)this.aDi.get()).getName().getString()));
         } else {
            this.afd = var3;
            this.aad(var1, var3);
            this.ara = AliceObf_aB.CN;
            this.N = System.currentTimeMillis();
            this.sZ = 0L;
            this.aam();
            this.xv.aaO((Integer)this.awn.get());
         }
      }
   }

   private void ZZ() {
      GenericContainerScreenHandler var1 = this.aae();
      if (var1 == null) {
         this.ZW();
      } else {
         AliceObf_fF var2 = (AliceObf_fF)this.RM.get();
         int var3 = var2 == AliceObf_fF.aLM ? 15 : 16;
         if (var1.slots.size() <= var3) {

         } else {
            short var6 = 29690;
         }

         long var4 = System.currentTimeMillis();
         if (this.sZ <= 0L || var4 - this.sZ >= ((Integer)this.PF.get()).intValue()) {
            this.WV = var2;
            this.aaj();
            this.U.aaK();
            this.aad(var1, var3);
            this.sZ = var4;
            this.N = var4;
         }
      }
   }

   private boolean aaa() {
      if (!(Boolean)this.afY.get()) {
         int var10000 = this.ara != AliceObf_aB.zr ? 55906 : 55905;

         while (true) {
            int var2 = var10000;
            if (var2 == 55905) {
               this.id = false;
               this.wy = 0;
               this.ZW();
               return true;
            }

            if (var2 != 55906) {
               this.id = false;
               this.wy = 0;
               return false;
            }

         }
      } else {
         AliceAutoReconnect var1 = (AliceAutoReconnect)Modules.get().get(AliceAutoReconnect.class);
         if (var1 != null && !var1.isIdle()) {
            if (this.ara != AliceObf_aB.zr) {
               this.aai();
               this.aam();
               this.xv.aaP();
            }

            this.ara = AliceObf_aB.zr;
            this.id = true;
            this.wy = 0;
            return true;
         } else if (this.ara != AliceObf_aB.zr) {
            return false;
         } else {
            this.wy++;
            if (!this.id && this.wy < 80) {
               return true;
            } else {
               this.id = false;
               this.wy = 0;
               this.ZW();
               return true;
            }
         }
      }
   }

   private void aab() {
      if (this.isActive()) {
         if (!(Boolean)this.afY.get()) {
            if (this.ara == AliceObf_aB.zr) {
               this.id = false;
               this.wy = 0;
               this.ara = AliceObf_aB.asC;
               this.N = System.currentTimeMillis();
               this.xv.aaP();
            }
         } else {
            this.aac();
         }
      }
   }

   private void aac() {
      if (this.isActive() && (Boolean)this.afY.get()) {
         Module var1 = Modules.get().get(AutoReconnect.class);
         if (var1 != null && !var1.isActive()) {
            var1.toggle();
         }

         AliceAutoReconnect var2 = (AliceAutoReconnect)Modules.get().get(AliceAutoReconnect.class);
         if (var2 != null && !var2.isActive()) {
            var2.toggle();
         }
      }
   }

   private void aad(GenericContainerScreenHandler var1, int var2) {
      this.mc.interactionManager.clickSlot(var1.syncId, var2, 0, SlotActionType.PICKUP, this.mc.player);
   }

   private GenericContainerScreenHandler aae() {
      return this.mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var1 ? var1 : null;
   }

   private boolean aaf() {
      return System.currentTimeMillis() - this.N >= 5000L;
   }

   private boolean aag() {
      return this.ara == AliceObf_aB.atK || this.ara == AliceObf_aB.OF || this.ara == AliceObf_aB.tY;
   }

   private void aah(String var1) {
      this.ara = AliceObf_aB.atK;
      this.iG = System.currentTimeMillis();
      this.aam();
      this.xv.aaP();
      this.aai();
      this.info(("" + var1), new Object[0]);
   }

   private void aai() {
      if (this.mc.currentScreen != null) {
         if (this.mc.player != null && this.mc.player.currentScreenHandler != this.mc.player.playerScreenHandler) {
            this.mc.player.closeHandledScreen();
         } else {
            this.mc.setScreen(null);
         }
      }
   }

   private void aaj() {
      this.Mr.clear();
      this.Mr.putAll(this.aal());
   }

   private boolean aak() {
      if (this.U.aaN()) {
         Map var1 = this.aal();
         if (AliceObf_e.aaW(this.Mr, var1)) {
            if (!this.U.aaM(true, true)) {
               return false;
            } else {
               this.ZT();
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private Map<Integer, Integer> aal() {
      HashMap var1 = new HashMap();
      if (this.mc.player != null && this.mc.world != null) {
         Item var2 = (Item)this.aDi.get();

         for (Entity var4 : this.mc.world.getEntities()) {
            if (var4 instanceof ItemEntity var5
               && var5.getStack().isOf(var2)
               && AliceObf_e.aaT(
                  var5.squaredDistanceTo(this.mc.player)
               )) {
               var1.put(var5.getId(), var5.getStack().getCount());
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   private void aam() {
      this.U.aaL();
      this.Mr.clear();
   }

   private long aan() {
      if (!(this.vJ <= 0L)) {
         short var3 = 21579;
      }

      long var1 = this.iG > 0L ? this.iG : System.currentTimeMillis();
      return Math.max(0L, var1 - this.vJ);
   }

   private void aao() {
      ScreenEvents.AFTER_INIT.register(new AliceObf_jx(this));
   }

   private void aap(Screen var1, MinecraftClient var2) {
      if (var1 instanceof HandledScreen && this.akZ.add(var1)) {
         ScreenEvents.afterRender(var1).register(new AliceObf_hY(this, var2));
      }
   }

   private void aaq(DrawContext var1) {
      if (this.isActive() && (Boolean)this.aHg.get() && this.mc.player != null && var1 != null) {
         String[] var2 = new String[]{
            ("" + this.aas(this.aan())),
            this.Ww + "",
            this.alG + "",
            ("" + this.aar())
         };
         int[] var3 = new int[]{-1, -8847496, -8847496, -8856321};
         byte var4 = 4;
         byte var5 = 2;
         byte var6 = 9;
         int var7 = 0;

         for (String var11 : var2) {
            var7 = Math.max(var7, this.mc.textRenderer.getWidth(var11));
         }

         var7 += var4 * 2;
         int var14 = var2.length * var6 + (var2.length - 1) * var5 + var4 * 2 + 2;
         int var15 = this.mc.getWindow().getScaledWidth() / 2 - var7 - 10;
         int var16 = this.mc.getWindow().getScaledHeight() / 2 - var14 - 10;
         var1.fill(var15, var16, var15 + var7, var16 + var14, -1273752556);
         var1.fill(var15, var16, var15 + var7, var16 + 2, -11141206);
         int var17 = var16 + var4 + 3;

         for (int var12 = 0; var12 < var2.length; var12++) {
            var1.drawTextWithShadow(this.mc.textRenderer, var2[var12], var15 + var4, var17, var3[var12]);
            var17 += var6 + var5;
         }
      }
   }

   private String aar() {
      String var1 = ("" + this.aat());
      if (this.AG.get() == AliceObf_pN.ajK) {
         String var7 = this.ara == AliceObf_aB.atK ? "Completed - " : "";
         return var7 + this.aiK + var1;
      } else {
         long var2 = ((Integer)this.Md.get()).intValue();
         double var4 = AliceObf_e.aaU(this.aiK, var2);
         String var6 = this.ara == AliceObf_aB.atK ? "Completed - " : "";
         return String.format(Locale.ROOT, "%s%d / %d (%.1f%%)%s", var6, this.aiK, var2, var4, var1);
      }
   }

   private String aas(long var1) {
      long var3 = var1 / 1000L;
      long var5 = var3 / 3600L;
      long var7 = var3 % 3600L / 60L;
      long var9 = var3 % 60L;
      int var10000 = var5 > 0L ? 60005 : 60006;

      while (true) {
         int var11 = var10000;
         if (var11 == 60005) {
            return String.format(Locale.ROOT, "%dh %dm %ds", var5, var7, var9);
         }

         if (var11 != 60006) {
            var10000 = var7 > 0L ? 57556 : 57557;

            while (true) {
               int var12 = var10000;
               if (var12 == 57556) {
                  return String.format(Locale.ROOT, "%dm %ds", var7, var9);
               }

               if (var12 != 57557) {
                  return var9 + "";
               }

            }
         }

      }
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private String aat() {
      return switch (this.ara.ordinal()) {
         case 0 -> "Inactive";
         case 1 -> "Sending /order";
         case 2 -> "Opening Orders";
         case 3 -> "Selecting Item";
         case 4 -> !this.U.aaN() ? "Dropping" : "Waiting Confirmation";
         case 5 -> "Auto Reconnect";
         case 6 -> "Completed";
         case 7 -> "Error";
         default -> throw new MatchException(null, null);
      };
   }

   }

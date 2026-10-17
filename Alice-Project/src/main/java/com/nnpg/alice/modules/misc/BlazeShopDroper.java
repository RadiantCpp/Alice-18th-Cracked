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
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
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
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

public class BlazeShopDroper extends AliceModule {
   private static final int aDy = 36;
   private static final int RS = 13;
   private static final int agb = 17;
   private static final int aiV = 23;
   private static final int aOx = 80;
   private static final int Ny = -1273752556;
   private static final int aJf = -11141206;
   private static final int az = -1;
   private static final int SD = -8847496;
   private static final int aBL = -8856321;
   private final SettingGroup aQv = this.settings.getDefaultGroup();
   private final SettingGroup aHm;
   private final Setting<Integer> afx;
   private final Setting<Integer> aNv;
   private final Setting<Integer> oW;
   private final Setting<Boolean> WB;
   private final Setting<AliceObf_pI> aH;
   private final Setting<Integer> LU;
   private final Setting<Double> ty;
   private final Setting<Boolean> aQj;
   private final AliceObf_nh ain;
   private final AliceObf_nh aDZ;
   private final Set<Screen> alj;
   private AliceObf_U Gj;
   private AliceObf_U bl;
   private long aEo;
   private long jo;
   private long aEP;
   private long alF;
   private boolean aOF;
   private boolean aqb;
   private int oU;
   public BlazeShopDroper() {
      super(AliceAddon.CATEGORY, "BlazeShopDroper", "Tu dong mua que quy lua, vut vat pham khi day tui va theo doi phien hoat dong.");
      this.aHm = this.settings.createGroup("HUD");
      this.afx = this.aQv
         .add(((Builder)((Builder)((Builder)new Builder().name("delay")).description("thoi gian tre (tick) khi dieu huong shop va mua lien tuc o slot 23")).defaultValue(10)).min(1).max(100).sliderMax(40).build());
      this.aNv = this.aQv
         .add(((Builder)((Builder)((Builder)new Builder().name("click-buy-delay")).description("thoi gian tre (tick) sau khi click vao nut chuan bi mua o slot 17")).defaultValue(4)).min(0).max(100).sliderMax(40).build());
      this.oW = this.aQv
         .add(((Builder)((Builder)((Builder)new Builder().name("drop-delay")).description("thoi gian tre 1 lan sau khi tui day roi moi vut het blaze rod")).defaultValue(1)).min(0).max(100).sliderMax(40).build());
      this.WB = this.aQv
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("auto-reconnect"))
                        .description("bat meteor autoreconnect va alice dang nhap, sau do tiep tuc mua"))
                     .defaultValue(true))
                  .onChanged(var1 -> this.sl()))
               .build()
         );
      this.aH = this.aQv
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("limit-mode"))
                     .description("chon gioi han so stack roi, gioi han tien, hoac mua khong gioi han"))
                  .defaultValue(AliceObf_pI.wS))
               .build()
         );
      this.LU = this.aQv
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("max-stack-amount")).description("so stack blaze rod roi ra toi da")).defaultValue(36))
                  .min(1)
                  .max(3600)
                  .sliderMax(360)
                  .visible(() -> this.aH.get() == AliceObf_pI.wS))
               .build()
         );
      this.ty = this.aQv
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("max-money"))
                     .description("so tien toi da de xai, don vi trieu. vd: 0.5 la 500k"))
                  .defaultValue(1.0)
                  .min(0.001)
                  .max(1000.0)
                  .sliderMax(100.0)
                  .visible(() -> this.aH.get() == AliceObf_pI.eP))
               .build()
         );
      this.aQj = this.aHm
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("show-hud"))
                     .description("hien thi thong ke cua BlazeShopDroper"))
                  .defaultValue(true))
               .build()
         );
      this.ain = new AliceObf_nh();
      this.aDZ = new AliceObf_nh();
      this.alj = Collections.newSetFromMap(new WeakHashMap<>());
      this.Gj = AliceObf_U.aqq;
      this.bl = AliceObf_U.GR;
      this.sB();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.Gj = AliceObf_U.GR;
         this.bl = AliceObf_U.GR;
         this.aEo = System.currentTimeMillis();
         this.jo = 0L;
         this.aEP = 0L;
         this.alF = 0L;
         this.aOF = false;
         this.aqb = false;
         this.oU = 0;
         this.ain.tc();
         this.aDZ.tc();
         this.sC(this.mc.currentScreen, this.mc);
         this.sm();
      }
   }

   public void onDeactivate() {
      super.onDeactivate();
      this.Gj = AliceObf_U.aqq;
      this.ain.tc();
      this.aDZ.tc();
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @AliceHandler
   public void rY(Pre var1) {
      if (this.Gj != AliceObf_U.hu && this.Gj != AliceObf_U.qA && this.Gj != AliceObf_U.aqq) {
         if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null) {
            if (!this.si()) {
               this.ss();
               if (this.Gj == AliceObf_U.aDX) {
                  this.sh();
               } else if (this.Gj == AliceObf_U.aPo) {
                  this.sj();
               } else if (this.st()) {
                  this.bl = this.Gj;
                  this.Gj = AliceObf_U.aDX;
                  this.aDZ.tb((Integer)this.oW.get());
               } else if (this.ain.td()) {
                  GenericContainerScreenHandler var2 = this.so();
                  if (this.Gj != AliceObf_U.GR && var2 == null) {
                     this.info("Shop GUI closed. BlazeShopDroper stopped.", new Object[0]);
                     this.toggle();
                  } else {
                     switch (this.Gj.ordinal()) {
                        case 1:
                           this.sd(var2);
                           break;
                        case 2:
                           this.se(var2);
                           break;
                        case 3:
                           this.sf(var2);
                           break;
                        case 4:
                           this.sg(var2);
                     }
                  }
               }
            }
         }
      }
   }

   @AliceHandler
   public void rZ(GameLeftEvent var1) {
      if (this.isActive() && !this.sv() && (Boolean)this.WB.get()) {
         this.Gj = AliceObf_U.Yl;
         this.aqb = false;
         this.oU = 0;
         this.ain.tc();
         this.aDZ.tc();
      }
   }

   @AliceHandler
   public void sa(GameJoinedEvent var1) {
      if (this.isActive() && !this.sv()) {
         this.sm();
         this.bl = AliceObf_U.GR;
         this.ain.tc();
         this.aDZ.tc();
         this.aqb = false;
         this.oU = 0;
         this.Gj = !this.WB.get() ? AliceObf_U.GR : AliceObf_U.Yl;
      }
   }

   @AliceHandler
   public void sb(ReceiveMessageEvent var1) {
      if (this.isActive()) {

      } else {
         char var2 = 59905;
      }
   }

   @AliceHandler
   public void sc(Render2DEvent var1) {
      if (!(this.mc.currentScreen instanceof HandledScreen)) {
         this.sD(var1.drawContext);
      }
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         return this.isActive() ? this.sI() : null;
      }
   }

   private void sd(GenericContainerScreenHandler var1) {
      if (var1 == null) {
         this.mc.player.networkHandler.sendChatCommand("shop");
         this.ain.tb((Integer)this.afx.get());
      } else {
         int var2 = this.sp(var1, Items.NETHERRACK);
         if (var2 < 0) {
            this.ain.tb((Integer)this.afx.get());
         } else {
            this.sn(var1, var2);
            this.Gj = AliceObf_U.CR;
            this.ain.tb((Integer)this.afx.get());
         }
      }
   }

   private void se(GenericContainerScreenHandler var1) {
      int var2 = this.sp(var1, Items.BLAZE_ROD);
      if (!(var2 < 0)) {
         char var3 = 62977;
      }

      this.sn(var1, var2);
      this.Gj = AliceObf_U.qp;
      this.ain.tb((Integer)this.afx.get());
   }

   private void sf(GenericContainerScreenHandler var1) {
      if (this.sr(var1)) {
         if (var1.slots.size() <= 17) {
            this.sx("Shop GUI does not contain slot 17.");
         } else {
            this.sn(var1, 17);
            this.Gj = AliceObf_U.mc;
            this.ain.tb((Integer)this.aNv.get());
         }
      } else {
         this.sk();
      }
   }

   private void sg(GenericContainerScreenHandler var1) {
      if (this.sr(var1)) {
         if (var1.slots.size() <= 23) {
            this.sx("Shop GUI does not contain slot 23.");
         } else {
            this.sn(var1, 23);
            this.ain.tb((Integer)this.afx.get());
         }
      } else {
         this.sk();
      }
   }

   private void sh() {
      if (this.st()) {
         if (this.aDZ.td()) {
            this.Gj = AliceObf_U.aPo;
            this.sj();
         }
      } else {
         this.Gj = this.bl;
         this.aDZ.tc();
      }
   }

   private boolean si() {
      if ((Boolean)this.WB.get()) {
         AliceAutoReconnect var1 = (AliceAutoReconnect)Modules.get().get(AliceAutoReconnect.class);
         if (var1 != null && !var1.isIdle()) {
            this.Gj = AliceObf_U.Yl;
            this.aqb = true;
            this.oU = 0;
            this.ain.tc();
            this.aDZ.tc();
            return true;
         } else if (this.Gj == AliceObf_U.Yl) {
            this.oU++;
            if (!this.aqb && this.oU < 80) {
               return true;
            } else {
               this.Gj = AliceObf_U.GR;
               this.bl = AliceObf_U.GR;
               this.aqb = false;
               this.oU = 0;
               this.ain.tb((Integer)this.afx.get());
               return true;
            }
         } else {
            return false;
         }
      } else {
         if (this.Gj == AliceObf_U.Yl) {
            this.Gj = AliceObf_U.GR;
         }

         this.aqb = false;
         this.oU = 0;
         return false;
      }
   }

   private void sj() {
      ScreenHandler var1 = this.mc.player.currentScreenHandler;
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < 36; var4++) {
         ItemStack var5 = this.mc.player.getInventory().getStack(var4);
         if (var5.isOf(Items.BLAZE_ROD)) {
            int var6 = this.sq(var1, var4);
            if (var6 >= 0) {
               var2.add(var6);
               var3.add(var5.getCount());
            }
         }
      }

      if (var2.isEmpty()) {
         if (this.aOF) {
            this.sw("Out of money.");
         } else {
            this.sx("Inventory is full but contains no Blaze Rods to drop.");
         }
      } else {
         int[] var8 = new int[var3.size()];

         for (int var9 = 0; var9 < var3.size(); var9++) {
            var8[var9] = (Integer)var3.get(var9);
         }

         AliceObf_cH var10 = AliceObf_lU.tj(
            var8
         );

         for (int var7 : (List<Integer>)(List<?>)var2) {
            this.mc.interactionManager.clickSlot(var1.syncId, var7, 1, SlotActionType.THROW, this.mc.player);
         }

         this.aEP = this.aEP + var10.sY();
         this.alF = this.alF + var10.sZ();
         if (this.aOF) {
            this.sw("Out of money.");
         } else if (this.su()) {
            this.sw("Buy limit reached.");
         } else {
            this.Gj = this.bl;
            this.ain.tb((Integer)this.afx.get());
         }
      }
   }

   private void sk() {
      this.Gj = AliceObf_U.GR;
      this.ain.tb((Integer)this.afx.get());
   }

   private void sl() {
      if (this.isActive()) {
         if ((Boolean)this.WB.get()) {
            this.sm();
         } else {
            if (this.Gj == AliceObf_U.Yl) {
               this.Gj = AliceObf_U.GR;
            }

            this.aqb = false;
            this.oU = 0;
         }
      }
   }

   private void sm() {
      if (this.isActive() && (Boolean)this.WB.get()) {
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

   private void sn(GenericContainerScreenHandler var1, int var2) {
      this.mc.interactionManager.clickSlot(var1.syncId, var2, 0, SlotActionType.PICKUP, this.mc.player);
   }

   private GenericContainerScreenHandler so() {
      return !(this.mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var1) ? null : var1;
   }

   private int sp(GenericContainerScreenHandler var1, Item var2) {
      int var3 = Math.min(var1.getRows() * 9, var1.slots.size());
      byte var4 = 0;
      if (!(var4 >= var3)) {
         short var5 = 16567;
      }

      return -1;
   }

   private int sq(ScreenHandler var1, int var2) {
      Iterator var3 = var1.slots.iterator();

      Slot var4;
      label66:
      while (true) {
         int var10000 = var3.hasNext() ? 20709 : 20710;

         while (true) {
            int var5 = var10000;
            if (var5 == 20709) {
               var4 = (Slot)var3.next();
               if (var4.inventory == this.mc.player.getInventory() && var4.getIndex() == var2) {
                  break label66;
               }
               break;
            }

            if (var5 != 20710) {
               return -1;
            }

         }
      }

      return var4.id;
   }

   private boolean sr(GenericContainerScreenHandler var1) {
      return var1.slots.size() > 13 && var1.getSlot(13).getStack().isOf(Items.BLAZE_ROD);
   }

   private void ss() {
      int var1 = this.mc.player.getInventory().getSelectedSlot();
      if (!this.mc.player.getInventory().getStack(var1).isEmpty()) {
         for (int var2 = 0; var2 < 9; var2++) {
            if (this.mc.player.getInventory().getStack(var2).isEmpty()) {

            } else {
               short var3 = 26338;
            }
         }
      }
   }

   private boolean st() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.mc.player.getInventory().getStack(var1).isEmpty()) {

         } else {
            short var2 = 18578;
         }
      }

      return true;
   }

   private boolean su() {
      return switch (((AliceObf_pI)this.aH.get()).ordinal() ^ 1002100114) {
         case 1002100112 -> false;
         case 1002100114 -> this.aEP >= ((Integer)this.LU.get()).intValue();
         case 1002100115 -> this.sy() >= this.sz();
         default -> throw new MatchException(null, null);
      };
   }

   private boolean sv() {
      return this.Gj == AliceObf_U.hu || this.Gj == AliceObf_U.qA || this.Gj == AliceObf_U.aqq;
   }

   private void sw(String var1) {
      this.Gj = AliceObf_U.hu;
      this.jo = System.currentTimeMillis();
      this.ain.tc();
      this.aDZ.tc();
      this.info(("" + var1), new Object[0]);
   }

   private void sx(String var1) {
      this.Gj = AliceObf_U.qA;
      this.jo = System.currentTimeMillis();
      this.ain.tc();
      this.aDZ.tc();
      this.error(var1, new Object[0]);
   }

   private long sy() {
      return AliceObf_lU.th(this.alF);
   }

   private long sz() {
      return AliceObf_lU.tg((Double)this.ty.get());
   }

   private long sA() {
      if (this.aEo <= 0L) {
         return 0L;
      } else {
         long var1 = this.jo > 0L ? this.jo : System.currentTimeMillis();
         return Math.max(0L, var1 - this.aEo);
      }
   }

   private void sB() {
      ScreenEvents.AFTER_INIT.register(new AliceObf_en(this));
   }

   private void sC(Screen var1, MinecraftClient var2) {
      if (var1 instanceof HandledScreen && this.alj.add(var1)) {
         ScreenEvents.afterRender(var1).register(new AliceObf_eI(this, var2));
      }
   }

   private void sD(DrawContext var1) {
      if (this.isActive()) {

      } else {
         short var13 = 388;
      }
   }

   private String sE() {
      String var1 = ("" + this.sI());
      if (this.aH.get() == AliceObf_pI.aIr) {
         return ("" + var1);
      } else {
         long var2;
         long var4;
         String var6;
         if (this.aH.get() == AliceObf_pI.wS) {
            var2 = this.aEP;
            var4 = ((Integer)this.LU.get()).intValue();
            var6 = " stacks";
         } else {
            var2 = this.sy();
            var4 = this.sz();
            var6 = " money";
         }

         double var7 = AliceObf_lU.ti(var2, var4);
         String var9 = this.Gj == AliceObf_U.hu ? "Completed - " : (this.Gj == AliceObf_U.qA ? "Error - " : "");
         return String.format(Locale.ROOT, "%s%.1f%% (%s / %s%s)%s", var9, var7, this.sF(var2), this.sF(var4), var6, var1);
      }
   }

   private String sF(long var1) {
      if (this.aH.get() == AliceObf_pI.eP) {

      } else {
         short var3 = 27934;
      }

      return Long.toString(var1);
   }

   private String sG(long var1) {
      if (var1 >= 1000000000L) {
         return String.format(Locale.ROOT, "%.2fB", var1 / 1.0E9);
      } else if (var1 >= 1000000L) {
         return String.format(Locale.ROOT, "%.2fM", var1 / 1000000.0);
      } else {
         return var1 >= 1000L ? String.format(Locale.ROOT, "%.2fK", var1 / 1000.0) : Long.toString(var1);
      }
   }

   private String sH(long var1) {
      long var3 = var1 / 1000L;
      long var5 = var3 / 3600L;
      long var7 = var3 % 3600L / 60L;
      long var9 = var3 % 60L;
      if (var5 <= 0L) {
         return var7 > 0L ? String.format(Locale.ROOT, "%dm %ds", var7, var9) : var9 + "";
      } else {
         return String.format(Locale.ROOT, "%dh %dm %ds", var5, var7, var9);
      }
   }

   private String sI() {
      int var1 = this.Gj.ordinal();
      String var10000;
      if (var1 == 0) {
         var10000 = "Inactive";
      } else if (var1 != 1) {
         if (var1 == 2) {
            var10000 = "Selecting Blaze Rod";
         } else if (var1 != 3) {
            if (var1 == 4) {
               var10000 = "Buying";
            } else if (var1 != 5) {
               if (var1 != 6) {
                  if (var1 == 7) {
                     var10000 = "Auto Reconnect";
                  } else if (var1 == 8) {
                     var10000 = "Completed";
                  } else {
                     if (var1 != 9) {
                        throw new MatchException(null, null);
                     }

                     var10000 = "Error";
                  }
               } else {
                  var10000 = "Dropping";
               }
            } else {
               var10000 = "Waiting To Drop";
            }
         } else {
            var10000 = "Preparing Buy";
         }
      } else {
         var10000 = "Opening Shop";
      }

      return var10000;
   }

   }

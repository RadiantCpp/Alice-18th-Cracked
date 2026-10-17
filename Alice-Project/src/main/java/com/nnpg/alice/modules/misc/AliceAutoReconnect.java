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
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting.Builder;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.AutoReconnect;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;

public class AliceAutoReconnect extends AliceModule {
   private static final long vL = 8000L;
   private static final long akm = 20000L;
   private static final long dH = 1500L;
   private static final long akM = 2000L;
   private static final long aQd = 180000L;
   private final MinecraftClient KS = MinecraftClient.getInstance();
   private final SettingGroup aPZ = this.settings.getDefaultGroup();
   private final Setting<String> aJr;
   private final Setting<Integer> gs;
   private final Setting<Boolean> Sm;
   private AliceObf_kf aAl;
   private boolean amB;
   private long ayE;
   private long aNj;
   private long GT;
   private long ZP;
   private long aIF;
   private long aLm;
   private static boolean Pi = false;
   public AliceAutoReconnect() {
      super(AliceAddon.CATEGORY, "Auto Login & Antibot & Auto Join", "Nhan dien yeu cau dang nhap, tu dong dang nhap, xu ly kiem tra va tu dong vao server.");
      this.aJr = this.aPZ.add(((Builder)((Builder)((Builder)new Builder().name("login-password")).description("mat khau cho lenh /l")).defaultValue("")).build());
      this.gs = this.aPZ
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("click-delay-ticks"))
                     .description("thoi gian tre giua moi lenh va click"))
                  .defaultValue(15))
               .min(15)
               .max(40)
               .build()
         );
      this.Sm = this.aPZ
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("notifications"))
                     .description("hien thi thong bao"))
                  .defaultValue(true))
               .build()
         );
      this.aAl = AliceObf_kf.zu;
      this.amB = false;
      this.ayE = 0L;
      this.aNj = 0L;
      this.GT = 0L;
      this.ZP = 0L;
      this.aIF = 0L;
      this.aLm = 0L;
   }

   public boolean w() {
      return this.isActive() && this.aAl != AliceObf_kf.zu;
   }

   public boolean isIdle() {
      return !this.isActive() || this.aAl == AliceObf_kf.zu;
   }

   public boolean hasLoginPassword() {
      return this.aJr.get() != null && !((String)this.aJr.get()).trim().isEmpty();
   }

   public String getStateLabel() {
      if (!this.isActive()) {
         return "OFF";
      } else {
         return this.aAl == AliceObf_kf.pa ? "WAITING LOGIN" : this.aAl.name().replace("_", " ");
      }
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.P();
         Module var1 = Modules.get().get(AutoReconnect.class);
         if (var1 != null && !var1.isActive()) {
            var1.toggle();
         }
      }
   }

   public void onDeactivate() {
      super.onDeactivate();
      this.P();
   }

   @AliceHandler
   public void x(ReceiveMessageEvent var1) {
      if (this.isActive()) {
         String var2 = this.Q(var1.getMessage().getString()).toLowerCase(Locale.ROOT);
         if (this.L(var2)) {
            this.H(var2);
         } else if (this.aAl == AliceObf_kf.aFv && var2.contains("xac minh thanh cong")) {
            this.amB = true;
            this.aAl = AliceObf_kf.awZ;
            this.ayE = System.currentTimeMillis();
            this.R("[AutoLogin] X\u00e1c minh antibot th\u00e0nh c\u00f4ng.");
         } else {
            if (this.aAl == AliceObf_kf.pa) {
               int var10000 = var2.contains("/dn") ? 20519 : 20520;

               while (true) {
                  int var3 = var10000;
                  if (var3 == 20519) {
                     if (this.N() && System.currentTimeMillis() - this.aNj >= Math.max(1500L, this.getActionDelayMs())) {
                        this.I();
                        this.R("[AutoLogin] /dn xu\u1ea5t hi\u1ec7n l\u1ea1i, \u0111\u00e3 g\u1eedi l\u1ea1i /l.");
                     }

                     return;
                  }

                  if (var3 != 20520) {
                     break;
                  }

               }
            }

            if (this.aAl == AliceObf_kf.zu) {
               if (var2.contains("dang nhap bang lenh") && var2.contains("/dn")) {
                  this.z();
               }
            }
         }
      }
   }

   @AliceHandler
   public void y(Pre var1) {
      if (this.isActive()) {
         if (this.aAl != AliceObf_kf.zu && this.aLm > 0L && System.currentTimeMillis() - this.aLm >= 180000L) {
            this.S("[AutoLogin] Flow timeout (3 min)! Returning to IDLE.");
            if (this.KS.currentScreen != null && this.KS.player != null) {
               this.KS.player.closeHandledScreen();
            }

            this.P();
         } else {
            switch (this.aAl.ordinal() ^ -1099836461) {
               case -1099836464:
                  this.B();
               case -1099836463:
               case -1099836461:
               default:
                  break;
               case -1099836462:
                  this.A();
                  break;
               case -1099836460:
                  this.F();
                  break;
               case -1099836459:
                  this.E();
                  break;
               case -1099836458:
                  this.D();
                  break;
               case -1099836457:
                  this.C();
                  break;
               case -1099836453:
                  this.G();
            }
         }
      }
   }

   private void z() {
      this.amB = false;
      this.ZP = 0L;
      this.aLm = System.currentTimeMillis();
      this.aAl = AliceObf_kf.aaM;
      this.ayE = System.currentTimeMillis();
      this.R("[AutoLogin] \u00d0\u00e3 ph\u00e1t hi\u1ec7n /dn. \u00d0ang ch\u1edd g\u1eedi /l.");
   }

   private void A() {
      if (this.N()) {
         if (System.currentTimeMillis() - this.ayE >= this.getActionDelayMs()) {
            this.I();
            this.aAl = AliceObf_kf.pa;
            this.ayE = this.aNj;
            this.R("[AutoLogin] \u00d0\u00e3 g\u1eedi /l sau khi v\u00e0o game.");
         }
      }
   }

   private void B() {
      if (this.N()) {
         if (System.currentTimeMillis() - this.ayE < 2500L) {

         } else {
            char var1 = 59417;
         }

         ChatUtils.sendPlayerMsg("/menu");
         this.amB = false;
         this.GT = System.currentTimeMillis();
         this.ZP = 0L;
         this.aAl = AliceObf_kf.acT;
         this.ayE = this.GT;
         this.R("[AutoLogin] \u00d0\u00e3 g\u1eedi /menu.");
      }
   }

   private void C() {
      if (this.KS.currentScreen instanceof GenericContainerScreen var1) {
         if (this.J(var1)) {
            this.aAl = AliceObf_kf.aFv;
            this.ayE = System.currentTimeMillis();
            this.ZP = 0L;
            this.R("[AutoLogin] Ph\u00e1t hi\u1ec7n antibot, b\u1eaft \u0111\u1ea7u x\u00e1c minh.");
         } else {
            this.aAl = AliceObf_kf.NQ;
            this.ayE = System.currentTimeMillis();
            this.ZP = 0L;
         }
      } else {
         if (System.currentTimeMillis() - this.GT >= 8000L) {
            this.aAl = AliceObf_kf.bv;
            this.ayE = System.currentTimeMillis();
         }
      }
   }

   private void D() {
      if (this.KS.currentScreen instanceof GenericContainerScreen var1) {
         if (this.amB) {
            this.aAl = AliceObf_kf.awZ;
            this.ayE = System.currentTimeMillis();
         } else {
            if (System.currentTimeMillis() - this.ayE >= 20000L) {
               char var6 = 59954;
            } else {
               char var7 = 59955;
            }

            if (this.O()) {
               if (System.currentTimeMillis() - this.ZP >= this.getClickDelayMs()) {
                  ScreenHandler var5 = var1.getScreenHandler();
                  int var3 = this.K(var5);
                  if (var3 != -1) {
                     this.KS.interactionManager.clickSlot(var5.syncId, var3, 0, SlotActionType.PICKUP, this.KS.player);
                     this.ZP = System.currentTimeMillis();
                  }
               }
            }
         }
      } else {
         int var10000 = System.currentTimeMillis() - this.ayE >= 8000L ? 49239 : 49240;

         while (true) {
            int var4 = var10000;
            if (var4 == 49239) {
               this.aAl = AliceObf_kf.bv;
               this.ayE = System.currentTimeMillis();
               break;
            }

            if (var4 != 49240) {
               break;
            }

         }
      }
   }

   private void E() {
      if (System.currentTimeMillis() - this.ayE >= 1500L) {
         this.aAl = AliceObf_kf.NQ;
         this.ayE = System.currentTimeMillis();
         this.ZP = 0L;
      }
   }

   private void F() {
      Screen var2 = this.KS.currentScreen;
      if (!(var2 instanceof GenericContainerScreen)) {
         char var3 = 34715;
      }

      if (System.currentTimeMillis() - this.ayE >= 8000L) {
         this.aAl = AliceObf_kf.bv;
         this.ayE = System.currentTimeMillis();
      }
   }

   private void G() {
      if (System.currentTimeMillis() >= this.aIF) {
         if (this.KS.currentScreen instanceof GenericContainerScreen) {
            if (this.KS.player != null && System.currentTimeMillis() - this.ayE >= 8000L) {
               this.KS.player.closeHandledScreen();
            }
         } else if (this.N()) {
            this.aAl = AliceObf_kf.zu;
            this.ayE = System.currentTimeMillis();
            this.R("[AutoLogin] Ho\u00e0n t\u1ea5t flow \u0111\u0103ng nh\u1eadp.");
         }
      }
   }

   private void H(String var1) {
      if (this.M(this.aAl)) {

      } else {
         short var2 = 12843;
      }

      this.amB = false;
      this.ZP = 0L;
      this.aAl = AliceObf_kf.bv;
      this.ayE = System.currentTimeMillis();
      this.R(("" + this.getTriggerLabel(var1)));
   }

   private void I() {
      String var1 = ((String)this.aJr.get()).trim();
      if (var1.isBlank()) {
         this.S("[AutoLogin] Ch\u01b0a nh\u1eadp m\u1eadt kh\u1ea9u! H\u00e3y \u0111i\u1ec1n v\u00e0o setting 'login-password'.");
         this.aAl = AliceObf_kf.zu;
         this.ayE = System.currentTimeMillis();
      } else {
         ChatUtils.sendPlayerMsg(("" + var1));
         this.aNj = System.currentTimeMillis();
      }
   }

   private boolean J(GenericContainerScreen var1) {
      ScreenHandler var2 = var1.getScreenHandler();

      for (int var3 = 0; var3 < Math.min(var2.slots.size(), 54); var3++) {
         ItemStack var4 = var2.getSlot(var3).getStack();
         if (!var4.isEmpty()) {
            String var5 = this.Q(var4.getName().getString()).toLowerCase(Locale.ROOT);
            if (var5.contains("xac minh") || var5.contains("la nguoi") || var5.contains("verify")) {
               return true;
            }
         }
      }

      return false;
   }

   private int K(ScreenHandler var1) {
      for (int var2 = 0; var2 < Math.min(var1.slots.size(), 54); var2++) {
         ItemStack var3 = var1.getSlot(var2).getStack();
         if (!var3.isEmpty()) {
            String var4 = this.Q(var3.getName().getString()).toLowerCase(Locale.ROOT);
            if (!var4.contains("xac minh")) {

            } else {
               char var5 = 37300;
            }

            return var2;
         }
      }

      return -1;
   }

   private boolean L(String var1) {
      return var1.contains("dang nhap thanh cong, neu chua tao ma pin, hay vao") || var1.contains("server con moi nen neu co") || var1.contains("khong duoc phep ban cac vat pham");
   }

   private String getTriggerLabel(String var1) {
      if (var1.contains("dang nhap thanh cong, neu chua tao ma pin, hay vao")) {
         return "login-success";
      } else if (!var1.contains("server con moi nen neu co")) {
         return var1.contains("khong duoc phep ban cac vat pham") ? "khong-duoc-phep-ban" : "unknown";
      } else {
         return "server-moi";
      }
   }

   private boolean M(AliceObf_kf var1) {
      if (!(var1 != AliceObf_kf.bv)) {
         char var2 = 35495;
      }

      return true;
   }

   private long getClickDelayMs() {
      return ((Integer)this.gs.get()).intValue() * 50L;
   }

   private long getActionDelayMs() {
      return ((Integer)this.gs.get()).intValue() * 50L;
   }

   private boolean N() {
      return this.KS.player != null && this.KS.world != null;
   }

   private boolean O() {
      return this.N() && this.KS.interactionManager != null;
   }

   private void P() {
      this.aAl = AliceObf_kf.zu;
      this.amB = false;
      this.ayE = 0L;
      this.aNj = 0L;
      this.GT = 0L;
      this.ZP = 0L;
      this.aIF = 0L;
      this.aLm = 0L;
   }

   private String Q(String var1) {
      return var1 == null ? "" : Normalizer.normalize(var1, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replace("\u0111", "d").replace("\u0110", "D");
   }

   private void R(String var1) {
   }

   private void S(String var1) {
   }

   public String getInfoString() {
      if (PremiumAccessGate.isAllowed()) {
         return this.isActive() ? this.getStateLabel() : null;
      } else {
         return null;
      }
   }

   public static void init() {
      if (!Pi) {
         Pi = true;
      }
   }

   }

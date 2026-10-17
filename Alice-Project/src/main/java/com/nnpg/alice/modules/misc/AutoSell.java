package com.nnpg.alice.modules.misc;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.bridge.NoPushBridge;
import com.nnpg.alice.premium.AliceHandler;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.AutoReconnect;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.registry.RegistryKey;
import org.lwjgl.glfw.GLFW;

public class AutoSell extends AliceModule {
   private static final long ahf = 5000L;
   private static final int GV = 3;
   private static final long aB = 4000L;
   private static final long Kt = 500L;
   private static final int aJo = 80;
   private static final int cZ = 80;
   private static final Pattern VI = Pattern.compile("(?i)(?:\\+\\s*)?\\$[ \\t]*([0-9]+(?:\\.[0-9]+)?)\\s*([KMB]?)");
   private static final Color Sp = new Color(255, 255, 255, 255);
   private static final Color aIg = new Color(120, 255, 120, 255);
   private static final Color xj = new Color(120, 220, 255, 255);
   private final SettingGroup apd = this.settings.getDefaultGroup();
   private final SettingGroup akf;
   private final SettingGroup ET;
   private final SettingGroup Ot;
   private final SettingGroup LS;
   private final Setting<Boolean> aBc;
   private final Setting<Boolean> aef;
   private final Setting<AliceObf_kS> Mk;
   private final Setting<List<Item>> pZ;
   private final Setting<Integer> aIR;
   private final Setting<Integer> ahL;
   private final Setting<Boolean> aNZ;
   private final Setting<Boolean> XE;
   private final Setting<Boolean> axo;
   private final Setting<Boolean> bp;
   private final Setting<Boolean> aBK;
   private final Setting<Boolean> awR;
   private final Setting<List<String>> NE;
   private final Setting<Boolean> pk;
   private final Setting<String> Ba;
   private final Setting<Integer> aDj;
   private final Setting<Boolean> akC;
   private AliceObf_tY aoM;
   private long Jh;
   private long vM;
   private int Ve;
   private int asG;
   private int Am;
   private int xE;
   private double bw;
   private long ao;
   private Vec3d tO;
   private RegistryKey<World> Oa;
   private String abw;
   private String ip;
   private double atR;
   private boolean aCx;
   private boolean ang;
   private int hI;
   private final Set<UUID> amG;
   private boolean TV;
   private boolean JX;
   private boolean axA;
   private int ajx;
   private boolean LL;
   private boolean Zc;
   private boolean aqR;
   private boolean fa;
   private long abD;
   private long Ic;
   private long gt;
   private boolean DY;
   private boolean azi;
   private boolean aPa;
   private boolean oT;
   private boolean cu;
   private long Ox;
   private final HttpClient Wq;
   private final AliceObf_pe aKD;
   private static Method Hq;
   private static boolean aib;
   public static java.lang.invoke.MethodHandles.Lookup getLookup() {
      return java.lang.invoke.MethodHandles.lookup();
   }

   public AutoSell() {
      super(AliceAddon.CATEGORY, "auto-sell", "Tu dong dien va xac nhan giao dien /sell cua may chu, kem tuy chon kiem tra an toan.");
      this.akf = this.settings.createGroup("Selling");
      this.ET = this.settings.createGroup("Auto Return");
      this.Ot = this.settings.createGroup("Player Detection");
      this.LS = this.settings.createGroup("Discord Webhook");
      this.aBc = this.apd.add(((Builder)((Builder)((Builder)new Builder().name("show-hud")).description("hien thi thoi gian on, tien, tong so da ban, trung binh va trang thai")).defaultValue(true)).build());
      this.aef = this.apd.add(((Builder)((Builder)((Builder)new Builder().name("notifications")).description("hien thi tin nhan trang thai cua auto sell tren chat")).defaultValue(true)).build());
      this.Mk = this.akf
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("filter-mode"))
                     .description("whitelist chi ban cac mon da chon, blacklist khong ban cac mon da chon"))
                  .defaultValue(AliceObf_kS.UP))
               .build()
         );
      this.pZ = this.akf
         .add(
            ((meteordevelopment.meteorclient.settings.ItemListSetting.Builder)((meteordevelopment.meteorclient.settings.ItemListSetting.Builder)new meteordevelopment.meteorclient.settings.ItemListSetting.Builder()
                     .name("items-to-sell"))
                  .description("cac item duoc bao gom hoac loai tru theo Filter Mode"))
               .defaultValue(new Item[]{Items.ELYTRA})
               .build()
         );
      this.aIR = this.akf
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("move-delay"))
                     .description("tick giua cac dot chuyen item binh thuong"))
                  .defaultValue(1))
               .min(0)
               .max(100)
               .sliderMax(20)
               .build()
         );
      this.ahL = this.akf
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("sell-delay"))
                     .description("tick cho sau khi bam slot xac nhan ban"))
                  .defaultValue(4))
               .min(0)
               .max(100)
               .sliderMax(20)
               .build()
         );
      this.aNZ = this.ET
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("auto-return")).description("quay ve vi tri kich hoat khi gap dieu kien da chon")).defaultValue(false)).onChanged(var1 -> this.lb()))
               .build()
         );
      this.XE = this.ET
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("return-on-push")).description("quay ve sau khi bi day khoi vi tri kich hoat")).defaultValue(true)).visible(this.aNZ::get)).build());
      this.axo = this.ET.add(((Builder)((Builder)((Builder)new Builder().name("no-push")).description("chi chan luc day cua nuoc/dong chay khi duoc bat")).defaultValue(false)).build());
      this.bp = this.ET
         .add(
            ((Builder)((Builder)((Builder)((Builder)((Builder)new Builder().name("return-on-disconnect")).description("quay ve sau khi ket noi lai server")).defaultValue(true)).visible(this.aNZ::get))
                  .onChanged(var1 -> this.lb()))
               .build()
         );
      this.aBK = this.Ot.add(((Builder)((Builder)((Builder)new Builder().name("player-detection")).description("phat hien bat ky nguoi choi nao duoc load ma khong co trong whitelist")).defaultValue(false)).build());
      this.awR = this.Ot
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("disconnect-on-player")).description("tu dong thoat khi phat hien nguoi choi o gan")).defaultValue(true)).visible(this.aBK::get)).build());
      this.NE = this.Ot
         .add(
            ((meteordevelopment.meteorclient.settings.StringListSetting.Builder)((meteordevelopment.meteorclient.settings.StringListSetting.Builder)((meteordevelopment.meteorclient.settings.StringListSetting.Builder)((meteordevelopment.meteorclient.settings.StringListSetting.Builder)new meteordevelopment.meteorclient.settings.StringListSetting.Builder()
                           .name("whitelisted-players"))
                        .description("ten nguoi choi duoc bo qua"))
                     .defaultValue(new ArrayList()))
                  .visible(this.aBK::get))
               .build()
         );
      this.pk = this.LS.add(((Builder)((Builder)((Builder)new Builder().name("discord-tracking")).description("gui thong bao tinh trang va canh bao qua discord")).defaultValue(false)).build());
      this.Ba = this.LS
         .add(
            ((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)new meteordevelopment.meteorclient.settings.StringSetting.Builder()
                           .name("webhook-url"))
                        .description("dia chi webhook discord"))
                     .defaultValue(""))
                  .visible(this.pk::get))
               .build()
         );
      this.aDj = this.LS
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("status-interval-seconds"))
                        .description("so giay giua cac lan gui webhook"))
                     .defaultValue(60))
                  .min(10)
                  .max(3600)
                  .sliderMax(600)
                  .visible(this.pk::get))
               .build()
         );
      this.akC = this.LS
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("notify-on-player")).description("gui canh bao webhook khi phat hien nguoi choi")).defaultValue(true)).visible(this.pk::get)).build());
      this.aoM = AliceObf_tY.aoq;
      this.Ve = -1;
      this.abw = "";
      this.ip = "";
      this.amG = new HashSet<>();
      this.Wq = HttpClient.newHttpClient();
      this.aKD = new AliceObf_pe();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.lq();
         this.lr();
         this.TV = false;
         this.JX = false;
         this.axA = false;
         this.ajx = 0;
         this.LL = false;
         this.Zc = false;
         this.aqR = false;
         this.fa = false;
         this.abD = 0L;
         this.Ic = 0L;
         this.DY = false;
         this.azi = false;
         this.aPa = false;
         this.oT = false;
         this.kX();
         this.aoM = this.mc.currentScreen instanceof GenericContainerScreen ? AliceObf_tY.alI : AliceObf_tY.C;
         this.cu = false;
         NoPushBridge.install(this, () -> this.isActive() && (Boolean)this.axo.get() && !this.cu);
         this.lb();
         this.ls("Auto Sell enabled.");
      }
   }

   public void onDeactivate() {
      this.cu = false;
      NoPushBridge.uninstall(this);
      this.kV();
      this.aoM = AliceObf_tY.aoq;
      this.tO = null;
      this.Oa = null;
      this.abw = "";
      this.Am = 0;
      this.lp();
      this.ls("Auto Sell disabled.");
      super.onDeactivate();
   }

   public String getInfoString() {
      return PremiumAccessGate.isAllowed() ? this.lw() : null;
   }

   @AliceHandler
   public void kA(Post var1) {
      if (this.isActive() && this.mc.player != null && this.mc.world != null && this.mc.getNetworkHandler() != null) {
         this.kS();
         this.lb();
         this.ld();
         if (this.mc.player.isDead()) {

         } else {
            char var6 = 50394;
         }

         AliceAutoReconnect var2 = this.le();
         boolean var3 = var2 != null && var2.w();
         if (this.JX && var3) {
            this.axA = true;
         }

         boolean var4 = this.kP(var2);
         if (this.mc.getNetworkHandler() != null) {
            if (var3 && !this.aqR) {
               this.aqR = true;
               this.kU();
            } else if (!var3 && this.aqR) {
               this.aqR = false;
               this.Jh = System.currentTimeMillis() + 250L;
            }

            this.lh();
            if (!var4 && !var3 && !this.kT()) {
               if (!this.kW(var2)) {
                  if (this.kK()) {

                  } else {
                     short var7 = 1898;
                  }

                  if (System.currentTimeMillis() >= this.Jh) {
                     int var5 = this.aoM.ordinal() ^ -1094082747;
                     if (var5 == -1094082752) {
                        this.kJ();
                     } else if (var5 == -1094082751) {
                        this.kI();
                     } else if (var5 == -1094082749) {
                        this.aoM = AliceObf_tY.C;
                     } else if (var5 != -1094082748) {
                        if (var5 == -1094082747) {
                           this.aoM = AliceObf_tY.C;
                        } else if (var5 != -1094082746) {
                           if (var5 == -1094082745) {
                              this.kG();
                           }
                        } else {
                           this.kH();
                        }
                     } else {
                        this.kF();
                     }
                  }
               }
            }
         }
      }
   }

   @AliceHandler
   public void kB(ReceiveMessageEvent var1) {
      if (!this.isActive()) {

      } else {
         char var2 = 50234;
      }

      this.lk(var1.getMessage().getString());
   }

   @AliceHandler
   public void kC(GameLeftEvent var1) {
      if (this.isActive()) {
         this.kV();
         this.DY = false;
         this.cu = false;
         if (this.Zc) {
            this.TV = false;
         } else {
            this.TV = this.la() && this.tO != null;
         }

         this.Zc = false;
         this.JX = false;
         this.aoM = AliceObf_tY.aoq;
         this.lr();
      }
   }

   @AliceHandler
   public void kD(GameJoinedEvent var1) {
      if (this.isActive()) {
         boolean var2 = this.LL;
         this.LL = false;
         this.Zc = false;
         this.lb();
         this.lr();
         this.Am = 0;
         this.asG = 0;
         long var10001 = System.currentTimeMillis();
         long var10002 = 1000L;
         this.Jh = var10001 + 1000L;
                   this.JX = this.la();
         this.axA = false;
         this.ajx = 0;
         this.TV = false;
         this.aoM = this.JX ? AliceObf_tY.aoq : AliceObf_tY.C;
      }
   }

   @AliceHandler
   public void kE(Render2DEvent var1) {
      if (this.isActive() && (Boolean)this.aBc.get()) {
         if (!(this.mc.player == null)) {
            char var25 = 36669;
         }

         TextRenderer var2 = TextRenderer.get();
         if (var2 != null) {
            ArrayList<String> var3 = new ArrayList<>();
            ArrayList var4 = new ArrayList();
            var3.add(
               ("" + 
                  AutoSell.lz(this.lx())
               )
            );
            var4.add(Sp);
            var3.add(
               ("" + 
                  AutoSell.lA(this.bw)
               )
            );
            var4.add(aIg);
            var3.add(this.xE + "");
            var4.add(Sp);
            var3.add(("" + lA(this.ly())));
            var4.add(aIg);
            var3.add(("" + this.lw()));
            var4.add(xj);
            var3.add(("" + this.lf()));
            var4.add(Sp);
            if ((Boolean)this.aBK.get()) {
               var3.add(("" + this.lg()));
               var4.add(xj);
            }

            double var5 = 0.0;

            for (String var8 : var3) {
               var5 = Math.max(var5, var2.getWidth(var8, false));
            }

            double var24 = var2.getHeight(false);
            double var9 = 4.0;
            double var11 = 2.0;
            double var13 = var5 + var9 * 2.0;
            double var15 = var3.size() * var24 + (var3.size() - 1) * var11 + var9 * 2.0;
            double var17 = this.mc.getWindow().getScaledWidth() / 2.0 - var13 - 10.0;
            double var19 = this.mc.getWindow().getScaledHeight() / 2.0 - var15 - 10.0;
            Renderer2D.COLOR.begin();
            Renderer2D.COLOR.quad(var17, var19, var13, var15, new Color(20, 20, 20, 180));
            Renderer2D.COLOR.quad(var17, var19, var13, 2.0, new Color(85, 255, 170, 255));
            this.lD(var1);
            var2.begin(1.0, false, true);
            double var21 = var19 + var9 + 3.0;

            for (int var23 = 0; var23 < var3.size(); var23++) {
               var2.render((String)var3.get(var23), var17 + var9, var21, (Color)var4.get(var23), true);
               var21 += var24 + var11;
            }

            var2.end();
         }
      }
   }

   private void kF() {
      GenericContainerScreen var1 = this.ll();
      this.Ve = var1 == null ? -1 : ((GenericContainerScreenHandler)var1.getScreenHandler()).syncId;
      this.mc.player.networkHandler.sendChatCommand("sell");
      this.vM = System.currentTimeMillis() + 5000L;
      this.aoM = AliceObf_tY.auv;
   }

   private void kG() {
      GenericContainerScreen var1 = this.ll();
      if (var1 != null) {
         int var10000 = ((GenericContainerScreenHandler)var1.getScreenHandler()).syncId != this.Ve ? 5322 : 5323;

         while (true) {
            int var2 = var10000;
            if (var2 == 5322) {
               this.asG = 0;
               this.lu(0);
               this.aoM = AliceObf_tY.alI;
               return;
            }

            if (var2 != 5323) {
               break;
            }

         }
      }

      if (System.currentTimeMillis() >= this.vM) {
         this.asG++;
         if (this.asG >= 3) {
            this.asG = 0;
            this.aoM = AliceObf_tY.amF;
            this.Jh = System.currentTimeMillis() + 10000L;
            this.ls("Failed to open the sell GUI after 3 retries.");
         } else {
            this.aoM = AliceObf_tY.C;
         }
      }
   }

   private void kH() {
      GenericContainerScreen var1 = this.ll();
      if (var1 == null) {
         this.aoM = AliceObf_tY.C;
      } else if (this.mc.interactionManager != null) {
         ScreenHandler var2 = var1.getScreenHandler();
         if (var2.slots.size() < 36) {
            this.aoM = AliceObf_tY.C;
         } else if (this.lm(var2) == 0) {
            this.aoM = AliceObf_tY.aOH;
         } else {
            int var3 = var2.slots.size() - 36;
            int var4 = 0;
            int var5 = this.lm(var2);

            for (int var6 = var3; var6 < var2.slots.size() && var4 < var5; var6++) {
               ItemStack var7 = var2.getSlot(var6).getStack();
               if (!var7.isEmpty() && !this.lo(var7)) {
                  int var8 = var7.getCount();
                  this.mc.interactionManager.clickSlot(var2.syncId, var6, 0, SlotActionType.QUICK_MOVE, this.mc.player);
                  this.Am += var8;
                  var4++;
               }
            }

            if (var4 > 0) {
               this.lu((Integer)this.aIR.get());
            } else {
               if (this.Am > 0) {
                  this.aoM = AliceObf_tY.aOH;
               } else {
                  this.lp();
                  this.aoM = AliceObf_tY.amF;
                  this.Jh = System.currentTimeMillis() + 1000L;
               }
            }
         }
      }
   }

   private void kI() {
      GenericContainerScreen var1 = this.ll();
      if (var1 == null) {
         this.aoM = AliceObf_tY.C;
      } else if (this.mc.interactionManager != null) {
         ScreenHandler var2 = var1.getScreenHandler();
         if (this.lm(var2) > 0 && this.ln(var2)) {
            this.aoM = AliceObf_tY.alI;
         } else {
            if (var2.slots.size() > 53) {
               int var10000 = this.Am <= 0 ? 7660 : 7661;

               while (true) {
                  int var4 = var10000;
                  if (var4 == 7660) {
                     break;
                  }

                  if (var4 != 7661) {
                     int var3 = this.Am;
                     this.mc.interactionManager.clickSlot(var2.syncId, 53, 0, SlotActionType.PICKUP, this.mc.player);
                     this.xE += var3;
                     this.Am = 0;
                     this.lu((Integer)this.ahL.get());
                     this.aoM = AliceObf_tY.ww;
                     return;
                  }

               }
            }

            this.lp();
            this.aoM = AliceObf_tY.amF;
            this.Jh = System.currentTimeMillis() + 1000L;
         }
      }
   }

   private void kJ() {
      GenericContainerScreen var1 = this.ll();
      if (!(var1 != null)) {
         short var2 = 25430;
      }

      this.aoM = AliceObf_tY.C;
   }

   private boolean kK() {
      int var10000 = this.tO != null ? 8385 : 8386;

      while (true) {
         int var4 = var10000;
         if (var4 == 8385) {
            if (this.mc.player == null) {
               short var5 = 7977;
            } else {
               short var6 = 7978;
            }

            if (!(Boolean)this.aNZ.get()) {
               if (this.aoM == AliceObf_tY.Mj || this.DY) {
                  this.kO(false);
               }

               this.oT = false;
               return false;
            } else {
               boolean var1 = this.aoM == AliceObf_tY.Mj || this.DY || (Boolean)this.XE.get();
               if (var1 && !this.kY()) {
                  if (!this.oT) {
                     this.oT = true;
                     this.kU();
                     this.ls("Waiting for the saved server and dimension before returning.");
                  }

                  return true;
               } else {
                  this.oT = false;
                  double var2 = this.mc.player.getEntityPos().distanceTo(this.tO);
                  if (!(var2 <= 0.5)) {
                     if (this.aoM == AliceObf_tY.Mj || this.DY || (Boolean)this.XE.get() && var2 > 1.0) {
                        if (this.aoM != AliceObf_tY.Mj) {
                           this.kL("Returning to the saved position.");
                        }

                        this.kM(var2);
                        return true;
                     } else {
                        return false;
                     }
                  } else {
                     if (this.DY || this.aoM == AliceObf_tY.Mj) {
                        this.kO(true);
                     }

                     return false;
                  }
               }
            }
         }

         if (var4 != 8386) {
            return false;
         }

      }
   }

   private void kL(String var1) {
      this.cu = true;
      this.lp();
      this.Am = 0;
      this.aoM = AliceObf_tY.Mj;
      this.gt = 0L;
      if (!this.DY) {
         this.DY = true;
         this.aPa = false;
         this.ls(var1);
      }

      this.kN();
   }

   private void kM(double var1) {
      if ((Boolean)this.aNZ.get()) {

      } else {
         short var14 = 929;
      }

      this.kO(false);
   }

   private void kN() {
      if (this.tO != null && !this.azi) {
         this.azi = this.aKD.km(BlockPos.ofFloored(this.tO));
      }
   }

   private void kO(boolean var1) {
      this.kV();
      boolean var2 = this.DY || this.aoM == AliceObf_tY.Mj;
      this.DY = false;
      this.cu = false;
      this.aPa = false;
      this.gt = 0L;
      this.aoM = AliceObf_tY.C;
      this.Jh = System.currentTimeMillis() + 250L;
      if (var1 && var2) {
         this.ls("Returned to the saved position.");
      }
   }

   private boolean kP(AliceAutoReconnect var1) {
      if (this.mc.player == null || this.mc.world == null) {
         return false;
      } else if (!(Boolean)this.aBK.get()) {
         this.kR();
         this.amG.clear();
         return false;
      } else {
         HashSet var2 = new HashSet();
         ArrayList var3 = new ArrayList();

         for (PlayerEntity var5 : this.mc.world.getPlayers()) {
            if (var5 != this.mc.player) {
               if (var5.getUuid().equals(this.mc.player.getUuid())) {

               } else {
                  short var9 = 22598;
               }
            }
         }

         this.amG.retainAll(var2);
         if (var3.isEmpty()) {
            if (this.aCx || this.ang) {
               this.lt("Detected player left the loaded world. Auto Sell resumed.");
               this.kR();
            }

            return false;
         } else {
            PlayerEntity var7 = (PlayerEntity)var3.getFirst();
            if (!this.aCx && !this.ang) {
               this.aCx = true;
               this.hI = 0;
               this.ip = var7.getName().getString();
               this.atR = this.mc.player.getEntityPos().distanceTo(var7.getEntityPos());
               this.kU();
               this.lt(String.format(Locale.ROOT, "Player detected: %s (%.1f blocks). Confirming for 80 ticks.", this.ip, this.atR));
               if ((Boolean)this.pk.get() && (Boolean)this.akC.get()) {
                  this.li(this.ip, this.atR);
               }
            }

            this.kU();
            if (!this.ang) {
               short var10 = 8667;
            } else {
               short var11 = 8666;
            }

            this.hI++;
            if (this.hI < 80) {
               return true;
            } else if (var1 != null && !var1.isIdle()) {
               for (PlayerEntity var6 : (List<PlayerEntity>)(List<?>)var3) {
                  this.amG.add(var6.getUuid());
               }

               this.lt("Player alert marked as a reconnect/hub false positive.");
               this.kR();
               return true;
            } else {
               this.aCx = false;
               this.ang = true;
               if (!(Boolean)this.awR.get()) {
                  this.lt("Player confirmed, but Disconnect On Player is disabled.");
               } else {
                  this.kQ(("" + this.ip));
               }

               return true;
            }
         }
      }
   }

   private void kQ(String var1) {
      if (!this.Zc && this.mc.getNetworkHandler() != null) {
         this.kU();
         this.lc();
         this.LL = true;
         this.Zc = true;
         this.lt(("" + var1));
         this.mc.getNetworkHandler().getConnection().disconnect(Text.literal(var1));
      }
   }

   private void kR() {
      this.aCx = false;
      this.ang = false;
      this.hI = 0;
      this.ip = "";
      this.atR = 0.0;
   }

   private void kS() {
      boolean var1 = this.mc.getWindow() != null && GLFW.glfwGetKey(this.mc.getWindow().getHandle(), 256) == 1;
      if (var1 && !this.fa) {
         this.abD = System.currentTimeMillis() + 4000L;
         this.kU();
         this.lt("Paused for 4 seconds because ESC was pressed.");
      }

      this.fa = var1;
   }

   private boolean kT() {
      return System.currentTimeMillis() < this.abD;
   }

   private void kU() {
      this.lp();
      this.Am = 0;
      this.kV();
      if (this.aoM != AliceObf_tY.aoq) {
         this.aoM = AliceObf_tY.C;
      }
   }

   private void kV() {
      if (this.azi) {
         this.aKD.kn();
      }

      this.azi = false;
   }

   private boolean kW(AliceAutoReconnect var1) {
      if (!this.JX) {
         return false;
      } else if (var1 != null && !var1.isIdle()) {
         this.axA = true;
         return true;
      } else {
         this.ajx++;
         if (!this.axA && this.ajx < 80) {
            return true;
         } else {
            this.JX = false;
            this.axA = false;
            this.ajx = 0;
            if (this.tO == null) {
               this.kX();
               this.aoM = AliceObf_tY.C;
            } else {
               this.kL("Reconnect flow finished. Returning to the saved position.");
            }

            return false;
         }
      }
   }

   private void kX() {
      if (this.mc.player != null && this.mc.world != null) {
         this.tO = this.mc.player.getEntityPos();
         this.Oa = this.mc.world.getRegistryKey();
         this.abw = this.kZ();
      } else {
         this.tO = null;
         this.Oa = null;
         this.abw = "";
      }
   }

   private boolean kY() {
      return this.mc.world != null && this.Oa != null && this.Oa.equals(this.mc.world.getRegistryKey()) && this.abw.equalsIgnoreCase(this.kZ());
   }

   private String kZ() {
      return this.mc.getCurrentServerEntry() == null ? "singleplayer" : this.mc.getCurrentServerEntry().address;
   }

   private boolean la() {
      if ((Boolean)this.aNZ.get()) {
         if ((Boolean)this.bp.get()) {

         } else {
            short var1 = 16635;
         }
      }

      return false;
   }

   private void lb() {
      if (this.isActive() && this.la() && !this.LL) {
         Module var1 = Modules.get().get(AutoReconnect.class);
         if (var1 != null && !var1.isActive()) {
            var1.toggle();
         }

         AliceAutoReconnect var2 = this.le();
         if (var2 != null && !var2.isActive()) {
            var2.toggle();
         }
      }
   }

   private void lc() {
      Module var1 = Modules.get().get(AutoReconnect.class);
      if (var1 != null && var1.isActive()) {
         var1.toggle();
      }

      AliceAutoReconnect var2 = this.le();
      if (var2 != null) {
         int var10000 = var2.isActive() ? 19454 : 19455;

         while (true) {
            int var3 = var10000;
            if (var3 == 19454) {
               var2.toggle();
               break;
            }

            if (var3 != 19455) {
               break;
            }

         }
      }
   }

   private void ld() {
      int var10000 = this.la() ? 59796 : 59797;

      while (true) {
         int var4 = var10000;
         if (var4 == 59796) {
            if (!this.LL && this.mc.player != null) {
               AliceAutoReconnect var1 = this.le();
               if (var1 != null) {
                  if (var1.hasLoginPassword()) {
                     short var5 = 15952;
                  } else {
                     short var6 = 15953;
                  }

                  long var2 = System.currentTimeMillis();
                  if (var2 < this.Ic) {
                     return;
                  }

                  this.Ic = var2 + 500L;
                  this.lt("Please enter the login password in Auto Login & Antibot & Auto Join.");
                  return;
               }

               this.Ic = 0L;
               return;
            }
            break;
         }

         if (var4 != 59797) {
            break;
         }

      }
   }

   private AliceAutoReconnect le() {
      return (AliceAutoReconnect)Modules.get().get(AliceAutoReconnect.class);
   }

   private String lf() {
      Module var1 = Modules.get().get(AutoReconnect.class);
      if (var1 != null) {
         if (var1.isActive()) {

         } else {
            char var5 = 34861;
         }
      }

      String var2 = "OFF";
      AliceAutoReconnect var3 = this.le();
      if (var3 == null) {
         short var6 = 14853;
      } else {
         short var7 = 14854;
      }

      String var4 = var3.getStateLabel();
      return var2 + var4;
   }

   private String lg() {
      if (!(this.ang)) {
         char var2 = 42658;
      }

      if (this.aCx) {
         String var1 = this.ip.isBlank() ? "" : ("" + this.ip);
         return this.hI + var1;
      } else {
         return !this.amG.isEmpty() ? this.amG.size() + "" : "Monitoring";
      }
   }

   private void lh() {
      if ((Boolean)this.pk.get() && !((String)this.Ba.get()).isBlank() && this.mc.player != null) {
         long var1 = Instant.now().getEpochSecond();
         if (var1 - this.Ox >= ((Integer)this.aDj.get()).intValue()) {
            this.Ox = var1;
            String var3 = AutoSell.lB(
                  "Uptime", AutoSell.lz(this.lx())
               )
               + lB("Money", lA(this.bw))
               + AutoSell.lB("Total Sell", Integer.toString(this.xE))
               + lB(
                  "Average",
                  ("" + 
                     AutoSell.lA(this.ly())
                  )
               );
            this.lj(var3);
         }
      }
   }

   private void li(String var1, double var2) {
      String var4 = lB("Player", var1)
         + AutoSell.lB(
            "Distance", String.format(Locale.ROOT, "%.1f blocks", var2)
         );
      this.lj(var4);
   }

   private void lj(String var1) {
      String var2 = ((String)this.Ba.get()).trim();
      int var10000 = !this.pk.get() ? 17494 : 17493;

      while (true) {
         int var3 = var10000;
         if (var3 == 17493) {
            if (!var2.isBlank()) {
               Thread.ofVirtual().name("session-status-webhook").start(() -> {
                  try {
                     HttpRequest var3x = HttpRequest.newBuilder().uri(URI.create(var2)).header("Content-Type", "application/json").POST(BodyPublishers.ofString(var1)).build();
                     this.Wq.send(var3x, BodyHandlers.discarding());
                  } catch (Exception var4) {
                  }
               });
               return;
            }
            break;
         }

         if (var3 != 17494) {
            break;
         }

      }
   }

   private void lk(String var1) {
      if (var1 != null && !var1.isBlank()) {
         Matcher var2 = VI.matcher(var1);
         if (var2.find()) {
            try {
               double var3 = Double.parseDouble(var2.group(1));
               String var5 = var2.group(2).toUpperCase(Locale.ROOT);
               byte var6 = -1;
               switch (var5.hashCode()) {
                  case 66:
                     if (var5.equals("B")) {
                        var6 = 2;
                     }
                     break;
                  case 75:
                     if (var5.equals("K")) {
                        var6 = 0;
                     }
                     break;
                  case 77:
                     if (var5.equals("M")) {
                        var6 = 1;
                     }
               }

               if (var6 != 0) {
                  if (var6 != 1) {
                     if (var6 == 2) {
                        var3 *= 1.0E9;
                     }
                  } else {
                     var3 *= 1000000.0;
                  }
               } else {
                  var3 *= 1000.0;
               }

               this.bw += var3;
            } catch (NumberFormatException var8) {
            }
         }
      }
   }

   private GenericContainerScreen ll() {
      return !(this.mc.currentScreen instanceof GenericContainerScreen var1) ? null : var1;
   }

   private int lm(ScreenHandler var1) {
      int var2 = Math.min(45, var1.slots.size());
      int var3 = 0;

      for (int var4 = 0; var4 < var2; var4++) {
         if (var1.getSlot(var4).getStack().isEmpty()) {
            var3++;
         }
      }

      return var3;
   }

   private boolean ln(ScreenHandler var1) {
      int var2 = Math.max(0, var1.slots.size() - 36);

      for (int var3 = var2; var3 < var1.slots.size(); var3++) {
         ItemStack var4 = var1.getSlot(var3).getStack();
         if (!var4.isEmpty()) {

         } else {
            short var5 = 30786;
         }
      }

      return false;
   }

   private boolean lo(ItemStack var1) {
      boolean var2 = ((List)this.pZ.get()).contains(var1.getItem());
      return !AliceObf_vH.mf((AliceObf_kS)this.Mk.get(), var2);
   }

   private void lp() {
      if (this.mc.player != null && this.mc.currentScreen instanceof GenericContainerScreen) {
         this.mc.player.closeHandledScreen();
      }
   }

   private void lq() {
      this.xE = 0;
      this.bw = 0.0;
      this.ao = System.currentTimeMillis();
      this.Ox = Instant.now().getEpochSecond();
      this.Am = 0;
      this.asG = 0;
      this.Jh = 0L;
   }

   private void lr() {
      this.kR();
      this.amG.clear();
   }

   private void ls(String var1) {
      if ((Boolean)this.aef.get() && this.mc.player != null) {
         this.mc.player.sendMessage(Text.literal(("" + var1)), false);
      }
   }

   private void lt(String var1) {
      if (this.mc.player != null) {
         this.mc.player.sendMessage(Text.literal(("" + var1)), false);
      }
   }

   private void lu(int var1) {
      this.Jh = System.currentTimeMillis() + var1 * 50L;
   }

   private static boolean lv(List<String> var0, String var1) {
      if (var0 != null && var1 != null) {
         for (String var3 : var0) {
            if (var3 != null && var3.trim().equalsIgnoreCase(var1)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private String lw() {
      if (this.ang) {
         return "Player Confirmed";
      } else if (this.aCx) {
         return this.hI + "";
      } else {
         if (!(this.aqR)) {
            short var3 = 8155;
         }

         if (this.JX) {
            return "Waiting Reconnect";
         } else if (this.kT()) {
            double var1 = Math.max(0L, this.abD - System.currentTimeMillis()) / 1000.0;
            return String.format(Locale.ROOT, "ESC Paused %.1fs", var1);
         } else if (this.oT) {
            return "Waiting Anchor World";
         } else if (this.aoM == AliceObf_tY.Mj) {
            return this.azi ? "Returning (Baritone)" : "Returning (Fallback)";
         } else {
            return switch (this.aoM.ordinal()) {
               case 0 -> "Idle";
               case 1 -> "Sending Command";
               case 2 -> "Waiting GUI";
               case 3 -> "Moving Items";
               case 4 -> "Selling";
               case 5 -> "Waiting";
               case 6 -> "Cooldown";
               case 7 -> "Returning";
               default -> throw new MatchException(null, null);
            };
         }
      }
   }

   private long lx() {
      return this.ao != 0L ? System.currentTimeMillis() - this.ao : 0L;
   }

   private double ly() {
      double var1 = this.lx() / 3600000.0;
      return var1 < 0.001 ? 0.0 : this.bw / var1;
   }

   private static String lz(long var0) {
      long var2 = var0 / 1000L;
      return String.format(Locale.ROOT, "%02d:%02d:%02d", var2 / 3600L, var2 % 3600L / 60L, var2 % 60L);
   }

   private static String lA(double var0) {
      if (!(var0 >= 1.0E9)) {
         if (var0 >= 1000000.0) {
            return String.format(Locale.ROOT, "$%.2fM", var0 / 1000000.0);
         } else {
            return var0 >= 1000.0 ? String.format(Locale.ROOT, "$%.2fK", var0 / 1000.0) : String.format(Locale.ROOT, "$%.0f", var0);
         }
      } else {
         return String.format(Locale.ROOT, "$%.2fB", var0 / 1.0E9);
      }
   }

   private static String lB(String var0, String var1) {
      return lC(var0) + AutoSell.lC(var1);
   }

   private static String lC(String var0) {
      return var0.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n");
   }

   private void lD(Render2DEvent var1) {
      if (!aib) {
         aib = true;

         for (Method var5 : Renderer2D.COLOR.getClass().getMethods()) {
            if (var5.getName().equals("render") && (var5.getParameterCount() == 0 || var5.getParameterCount() == 1)) {
               Hq = var5;
               break;
            }
         }
      }

      if (!(Hq == null)) {
         char var8 = 50350;
      }

      try {
         if (Hq.getParameterCount() == 0) {
            Hq.invoke(Renderer2D.COLOR);
         } else {
            Class var7 = Hq.getParameterTypes()[0];
            Hq.invoke(Renderer2D.COLOR, !var7.getName().contains("DrawContext") ? var1.drawContext.getMatrices() : var1.drawContext);
         }
      } catch (Exception var6) {
      }
   }

   }

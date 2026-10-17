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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.meteor.KeyEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.network.packet.Packet;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.registry.Registries;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.DataComponentTypes;

public final class GuiHelper extends AliceModule {
   private static final DateTimeFormatter lD = DateTimeFormatter.ofPattern("HH:mm:ss");
   private static final int abA = 600;
   private static final String ayc = "[GuiHelper";
   private final SettingGroup Qa = this.settings.getDefaultGroup();
   private final SettingGroup jd;
   private final SettingGroup asO;
   private final Setting<Boolean> apz;
   private final Setting<Boolean> RL;
   private final Setting<Boolean> aGC;
   private final Setting<Boolean> adp;
   private final Setting<Integer> xL;
   private final Setting<Integer> tV;
   private final Setting<Keybind> Cc;
   private final Setting<Boolean> gC;
   private final Setting<Boolean> eI;
   private final Setting<Boolean> BW;
   private final Setting<Boolean> acY;
   private final Setting<Boolean> Xo;
   private Screen alU;
   private String FM;
   private boolean ba;
   private boolean na;
   private long adG;
   private long Ih;
   public GuiHelper() {
      super(AliceAddon.CATEGORY, "gui-helper", "Ghi lai su kien GUI, noi dung container (kem lore/chi so) va lenh player gui di.");
      this.jd = this.settings.createGroup("Packet");
      this.asO = this.settings.createGroup("Output");
      this.apz = this.Qa
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("log-screen")).description("Ghi khi mo/dong GUI (ten screen, title, syncId)."))
                  .defaultValue(true))
               .build()
         );
      this.RL = this.Qa
         .add(((Builder)((Builder)((Builder)new Builder().name("dump-on-open")).description("Tu dong liet ke toan bo item trong container moi khi mo GUI.")).defaultValue(true)).build());
      this.aGC = this.Qa.add(((Builder)((Builder)((Builder)new Builder().name("dump-on-change")).description("Ghi them cac slot doi khac khi noi dung container thay doi (co the spam).")).defaultValue(false)).build());
      this.adp = this.Qa
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("include-empty")).description("Ghi ca slot trong (mac dinh chi ghi slot co item)."))
                  .defaultValue(false))
               .build()
         );
      this.xL = this.Qa
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("max-slots"))
                     .description("So slot toi da ghi trong mot lan dump."))
                  .defaultValue((90)))
               .min(9)
               .max((200))
               .sliderMax(120)
               .build()
         );
      this.tV = this.Qa
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("check-interval-ticks"))
                     .description("Chu ky kiem tra noi dung container (20 tick = 1 giay). Mo/dong GUI van bat ngay lap tuc."))
                  .defaultValue(20))
               .min((5))
               .max(200)
               .sliderMax(100)
               .build()
         );
      this.Cc = this.Qa
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("dump-key"))
                     .description("Phim dump lai container dang mo (de phan tich GUI da mo san)."))
                  .defaultValue(Keybind.none()))
               .build()
         );
      this.gC = this.jd
         .add(((Builder)((Builder)((Builder)new Builder().name("log-slot-click")).description("Ghi ClickSlot / ButtonClick / CloseHandledScreen player gui di.")).defaultValue(true)).build());
      this.eI = this.jd
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("log-command")).description("Ghi lenh va chat player gui di server."))
                  .defaultValue(true))
               .build()
         );
      this.BW = this.jd
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("log-server-message")).description("Ghi tin nhan server gui ve (doi chieu prompt, gia, trang thai)."))
                  .defaultValue(true))
               .build()
         );
      this.acY = this.jd.add(((Builder)((Builder)((Builder)new Builder().name("log-server-inventory")).description("Ghi packet inventory tu server (Inventory/SlotUpdate, co the spam).")).defaultValue(false)).build());
      this.Xo = this.asO
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("log-to-file")).description("Ghi them ra file run/gui-helper.log de copy lam fixture."))
                  .defaultValue(true))
               .build()
         );
      this.FM = "";
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.alU = super.mc.currentScreen;
         this.FM = this.Tj(this.alU);
         if ((Boolean)this.apz.get()) {
            this.Tp("bat dau theo doi GUI. Mo mot container hoac go lenh de xem log.");
         }

         if (!(Boolean)this.RL.get()) {

         } else {
            short var1 = 23662;
         }
      }
   }

   public void onDeactivate() {
      this.alU = null;
      this.FM = "";
      this.na = false;
      this.adG = 0L;
      this.Ih = 0L;
      super.onDeactivate();
   }

   @AliceHandler
   private void SU(OpenScreenEvent var1) {
      if (!this.isActive()) {

      } else {
         short var4 = 4872;
      }

      Screen var2 = super.mc.currentScreen;
      Screen var3 = var1.screen;
      if ((Boolean)this.apz.get()) {
         if (var3 == null) {
            this.Tp(("" + this.Th(var2)));
         } else {
            if (!(var2 != null)) {
               short var5 = 30372;
            }

            this.Tp(("" + this.Th(var3)));
            this.Ti(var3);
         }
      }
   }

   @AliceHandler
   private void SV(Post var1) {
      if (this.isActive() && super.mc.player != null) {
         this.adG += 1L;
         Screen var2 = super.mc.currentScreen;
         if (var2 == this.alU) {
            if ((Boolean)this.aGC.get() && var2 instanceof HandledScreen) {
               if (this.adG - this.Ih < Math.max((1), (Integer)this.tV.get())) {

               } else {
                  short var5 = 28378;
               }
            }
         } else {
            boolean var3 = this.alU instanceof HandledScreen;
            boolean var4 = var2 instanceof HandledScreen;
            if ((Boolean)this.apz.get() && (var3 || var4)) {
               if (var2 == null) {
                  this.Tp(("" + this.Th(this.alU)));
               } else {
                  this.Tp(this.Th(this.alU) + this.Th(var2));
                  this.Ti(var2);
               }
            }

            this.alU = var2;
            this.FM = this.Tj(var2);
            this.Ih = this.adG;
            if (var2 instanceof HandledScreen && (Boolean)this.RL.get()) {
               this.Ta(
                  "open"
               );
            }
         }
      }
   }

   @AliceHandler
   private void SW(KeyEvent var1) {
      if (this.isActive() && var1.action == KeyAction.Press) {
         Keybind var2 = (Keybind)this.Cc.get();
         if (var2 != null) {
            if (var2.matches(true, var1.key(), var1.modifiers())) {
               this.Ta("manual");
               var1.cancel();
            }
         }
      }
   }

   @AliceHandler
   private void SX(Sent var1) {
      if (this.isActive() && super.mc.player != null) {
         Packet var2 = var1.packet;
         if (var2 != null) {
            String var3 = var2.getClass().getSimpleName();
            if ((Boolean)this.gC.get()) {
               int var5 = (-1);
               switch (var3.hashCode() ^ 1080102707) {
                  case -2033706507:
                     if (var3.equals(
                        "ClickSlotC2SPacket"
                     )) {
                        var5 = 0;
                     }
                     break;
                  case -1062335166:
                     if (var3.equals(
                        "SlotChangedStateC2SPacket"
                     )) {
                        var5 = (3);
                     }
                     break;
                  case 794286906:
                     if (var3.equals(
                        "CreativeInventoryActionC2SPacket"
                     )) {
                        var5 = 4;
                     }
                     break;
                  case 1618466245:
                     if (var3.equals(
                        "ButtonClickC2SPacket"
                     )) {
                        var5 = 1;
                     }
                     break;
                  case 1734536847:
                     if (var3.equals("CloseHandledScreenC2SPacket")) {
                        var5 = 2;
                     }
                     break;
                  case 1738327097:
                     if (var3.equals(
                        "UpdateSelectedSlotC2SPacket"
                     )) {
                        var5 = 5;
                     }
               }

               if (var5 == 0) {
                  this.Tp(
                     ("" + 
                        this.Tk(
                           var2,
                           new String[]{
                              "syncId",
                              "slot",
                              "button",
                              "actionType"
                           }
                        )
                     )
                  );
                  return;
               }

               if (var5 == 1) {
                  this.Tp(
                     ("" + 
                        this.Tk(
                           var2,
                           new String[]{
                              "syncId",
                              "buttonId"
                           }
                        )
                     )
                  );
                  return;
               }

               if (var5 == 2) {
                  this.Tp(
                     ("" + 
                        this.Tk(
                           var2,
                           new String[]{
                              "syncId"
                           }
                        )
                     )
                  );
                  return;
               }

               if (var5 == 3 || var5 == 4 || var5 == 5) {
                  this.Tp(("" + this.Tk(var2, new String[0])));
                  return;
               }
            }

            if ((Boolean)this.eI.get()) {
               if (!var3.contains("Chat")) {
                  if (var3.contains(
                     "Command"
                  )) {

                  } else {
                     char var7 = 33735;
                  }
               } else {
                  this.Tp(var3 + this.Tl(var2));
               }
            }
         }
      }
   }

   @AliceHandler
   private void SY(Receive var1) {
      if (this.isActive() && (Boolean)this.acY.get()) {
         Packet var2 = var1.packet;
         if (var2 != null) {
            String var3 = var2.getClass().getSimpleName();
            if (!var3.contains(
               "Inventory"
            )) {
               if (var3.contains(
                  "SlotUpdate"
               )) {

               } else {
                  char var4 = 33042;
               }
            }

            this.Tp(var3 + this.Tk(var2, new String[0]));
         }
      }
   }

   @AliceHandler
   private void SZ(ReceiveMessageEvent var1) {
      if (this.isActive() && (Boolean)this.BW.get()) {
         if (!(this.na)) {
            char var3 = 33868;
         }

         String var2 = var1.getMessage().getString();
         if (!(var2 != null)) {
            char var4 = 58166;
         }
      }
   }

   private void Ta(String var1) {
      if (super.mc.player != null) {
         Screen var2 = super.mc.currentScreen;
         if (!(var2 instanceof HandledScreen var3)) {
            if ("manual"
               .equals(var1)) {
               this.Tp(
                  "khong co container nao dang mo."
               );
            }
         } else {
            ScreenHandler var4 = var3.getScreenHandler();
            this.Tp(var1 + this.Th(var2) + var4.syncId + var4.getRevision() + var4.slots.size());
            int var5 = Math.max(0, var4.slots.size() - (36));
            this.Tp("" + (var5 - 1) + var5 + (var4.slots.size() - (1)));
            int var6 = 0;
            int var7 = (Integer)this.xL.get();

            for (int var8 = 0; var8 < var4.slots.size() && var6 < var7; var8++) {
               ItemStack var9 = ((Slot)var4.slots.get(var8)).getStack();
               if (!var9.isEmpty() || (Boolean)this.adp.get()) {
                  for (String var11 : this.Td(var8, var9)) {
                     this.Tp(var11);
                  }

                  var6++;
               }
            }

            if (var6 >= var7) {
               this.Tp("  ... cat bot, tang max-slots de xem het.");
            }

            this.FM = this.Tj(var2);
         }
      }
   }

   private void Tb(String var1, String var2, Screen var3) {
      if (var3 instanceof HandledScreen var4) {
         this.Tp(("" + this.Th(var3)));
         String[] var5 = var2.split(";");
         int var6 = var5.length;

         for (int var7 = (0); var7 < var6; var7++) {
            String var8 = var5[var7];
            if (!var8.isBlank() && !var1.contains(var8)) {
               this.Tp(("" + var8));
            }
         }

         for (String var10 : this.Tc(var4)) {
            this.Tp(var10);
         }
      }
   }

   private List<String> Tc(HandledScreen<?> var1) {
      ArrayList var2 = new ArrayList();
      ScreenHandler var3 = var1.getScreenHandler();
      int var4 = Math.min((Integer)this.xL.get(), var3.slots.size());

      for (int var5 = (0); var5 < var4; var5++) {
         ItemStack var6 = ((Slot)var3.slots.get(var5)).getStack();
         if (!var6.isEmpty() || (Boolean)this.adp.get()) {
            var2.addAll(this.Td(var5, var6));
         }
      }

      return var2;
   }

   private List<String> Td(int var1, ItemStack var2) {
      ArrayList var3 = new ArrayList();
      if (var2.isEmpty()) {
         var3.add(var1 + "");
         return var3;
      } else {
         String var4 = Registries.ITEM.getId(var2.getItem()).toString();
         String var5 = this.Te(var2);
         StringBuilder var6 = new StringBuilder();
         var6.append(
               "  [slot "
            )
            .append(var1)
            .append("] ")
            .append(var4)
            .append(
               " x"
            )
            .append(var2.getCount());
         if (!var5.isBlank()) {
            var6.append(" ten=\"")
               .append(
                  GuiHelper.To(var5, (120))
               )
               .append(
                  "\""
               );
         }

         if (var2.isDamageable()) {
            var6.append(" ben=")
               .append(var2.getMaxDamage() - var2.getDamage())
               .append("/")
               .append(var2.getMaxDamage());
         }

         var3.add(
            GuiHelper.To(
               var6.toString(), (600)
            )
         );

         for (String var8 : this.Tf(var2)) {
            var3.add(
               GuiHelper.To(
                  ("" + var8), 600
               )
            );
         }

         for (String var10 : this.Tg(var2)) {
            var3.add(
               GuiHelper.To(
                  ("" + var10), 600
               )
            );
         }

         return var3;
      }
   }

   private String Te(ItemStack var1) {
      try {
         Text var2 = (Text)var1.get(DataComponentTypes.CUSTOM_NAME);
         return var2 != null ? var2.getString() : var1.getName().getString();
      } catch (Exception var3) {
         return "";
      }
   }

   private List<String> Tf(ItemStack var1) {
      ArrayList var2 = new ArrayList();

      try {
         LoreComponent var3 = (LoreComponent)var1.get(DataComponentTypes.LORE);
         if (var3 == null) {
            return var2;
         }

         for (Text var5 : var3.lines()) {
            String var6 = var5.getString();
            if (var6 != null) {
               int var10000 = var6.isBlank() ? 16329 : 16328;

               while (true) {
                  int var7 = var10000;
                  if (var7 == 16328) {
                     var2.add(
                        GuiHelper.To(
                           var6.trim(), (250)
                        )
                     );
                     break;
                  }

                  if (var7 != 16329) {
                     break;
                  }

               }
            }
         }
      } catch (Exception var8) {
      }

      return var2;
   }

   private List<String> Tg(ItemStack var1) {
      ArrayList var2 = new ArrayList();

      try {
         ItemEnchantmentsComponent var3 = (ItemEnchantmentsComponent)var1.get(DataComponentTypes.ENCHANTMENTS);
         if (var3 == null || var3.isEmpty()) {
            return var2;
         }

         Iterator var4 = var3.getEnchantmentEntries().iterator();
         if (var4.hasNext()) {

         } else {
            short var8 = 22848;
         }
      } catch (Exception var7) {
      }

      return var2;
   }

   private String Th(Screen var1) {
      if (var1 == null) {
         return "(none)";
      } else {
         String var2 = "";

         try {
            var2 = var1.getTitle().getString();
         } catch (Exception var4) {
         }

         return var1.getClass().getSimpleName()
            + (
               var2.isBlank()
                  ? ""
                  : ("" + 
                     GuiHelper.To(var2, 120)
                  )
            );
      }
   }

   private void Ti(Screen var1) {
      if (var1 instanceof HandledScreen var2) {
         try {
            ScreenHandler var3 = var2.getScreenHandler();
            this.Tp(var3.getClass().getSimpleName() + var3.getType() + var3.syncId + var3.getRevision() + var3.slots.size());
         } catch (Exception var4) {
         }
      }
   }

   private String Tj(Screen var1) {
      int var10000 = var1 instanceof HandledScreen ? 12428 : 12429;

      while (true) {
         int var7 = var10000;
         if (var7 == 12428) {
            HandledScreen var2 = (HandledScreen)var1;
            StringBuilder var3 = new StringBuilder();
            ScreenHandler var4 = var2.getScreenHandler();
            byte var5 = 0;
            if (var5 >= var4.slots.size()) {
               char var8 = 51242;
            } else {
               char var9 = 51241;
            }

            return var3.toString();
         }

         if (var7 != 12429) {
            return "";
         }

      }
   }

   private String Tk(Object var1, String... var2) {
      ArrayList var3 = new ArrayList();
      String[] var4 = var2;
      int var5 = var2.length;

      for (int var6 = (0); var6 < var5; var6++) {
         String var7 = var4[var6];
         Object var8 = this.Tm(var1, var7);
         if (var8 != null) {
            var3.add(
               var7 + GuiHelper.To(String.valueOf(var8), 80)
            );
         }
      }

      if (var3.isEmpty()) {
         String var9 = this.Tn(var1);
         return GuiHelper.To(
            var9, (250)
         );
      } else {
         return String.join(
            " ", var3
         );
      }
   }

   private String Tl(Object var1) {
      String[] var10000 = new String[4];
      var10000[(0)] = "chatMessage";
      var10000[(1)] = "command";
      var10000[2] = "message";
      var10000[(3)] = "getCommand";
      String[] var2 = var10000;
      int var3 = var2.length;

      for (int var4 = 0; var4 < var3; var4++) {
         String var5 = var2[var4];

         try {
            Method var6 = var1.getClass().getMethod(var5);
            Object var7 = var6.invoke(var1);
            if (var7 != null) {
               return GuiHelper.To(
                  String.valueOf(var7), (300)
               );
            }
         } catch (Exception var8) {
         }
      }

      var10000 = new String[(3)];
      var10000[(0)] = "chatMessage";
      var10000[1] = "command";
      var10000[(2)] = "message";
      var2 = var10000;
      var3 = var2.length;

      for (int var11 = 0; var11 < var3; var11++) {
         String var12 = var2[var11];
         Object var13 = this.Tm(var1, var12);
         if (var13 != null) {
            return GuiHelper.To(String.valueOf(var13), 300);
         }
      }

      return GuiHelper.To(
         this.Tn(var1), (300)
      );
   }

   private Object Tm(Object var1, String var2) {
      try {
         Field var3 = var1.getClass().getDeclaredField(var2);
         var3.setAccessible(true);
         return var3.get(var1);
      } catch (Exception var4) {
         return null;
      }
   }

   private String Tn(Object var1) {
      try {
         return String.valueOf(var1);
      } catch (Exception var3) {
         return var1.getClass().getSimpleName();
      }
   }

   private static String To(String var0, int var1) {
      if (var0 == null) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(Math.min(var0.length(), var1 + (16)));
         int var3 = (1);

         for (int var4 = 0; var4 < var0.length(); var4++) {
            char var5 = var0.charAt(var4);
            if (var5 != (10) && var5 != '\r' && !Character.isWhitespace(var5)) {
               var2.append(var5);
               var3 = 0;
            } else {
               if (var3 == 0) {
                  var2.append(' ');
               }

               var3 = (1);
            }
         }

         String var6 = var2.toString().trim();
         if (var6.length() <= var1) {

         } else {
            short var7 = 23707;
         }

         return ("" + var6.substring((0), var1));
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void Tp(String var1) {
      String var2;
      var2 = LocalTime.now().format(GuiHelper.lD) + var1;
      label62:
      if (super.mc.player != null) {
         this.na = true;
         boolean var7 = false /* VF: Semaphore variable */;

         label59: {
            try {
               var7 = true;
               super.mc.player.sendMessage(Text.literal(var2), false);
               var7 = false;
               break label59;
            } catch (Exception var8) {
               var7 = false;
            } finally {
               if (var7) {
                  this.na = false;
               }
            }

            this.na = false;
            break label62;
         }

         this.na = false;
      }

      if ((Boolean)this.Xo.get() && !this.ba) {
         this.Tq(var2);
      }
   }

   private void Tq(String var1) {
      try {
         Path var2 = FabricLoader.getInstance()
            .getGameDir()
            .resolve("gui-helper.log");
         String var3 = var1 + System.lineSeparator();
         Charset var10002 = StandardCharsets.UTF_8;
         OpenOption[] var10003 = new OpenOption[(2)];
         var10003[(0)] = StandardOpenOption.CREATE;
         var10003[1] = StandardOpenOption.APPEND;
         Files.writeString(var2, var3, var10002, var10003);
      } catch (Exception var4) {
         this.ba = true;
      }
   }

   }

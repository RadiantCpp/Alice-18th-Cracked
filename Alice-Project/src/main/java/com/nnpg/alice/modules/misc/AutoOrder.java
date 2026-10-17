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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.ItemSetting.Builder;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.registry.Registries;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.DataComponentTypes;

public final class AutoOrder extends AliceModule {
   private static final int Fh = 200;
   private static final int oX = 20;
   private static final int Ca = -1273752556;
   private static final int L = -11141206;
   private static final int my = -1;
   private static final int ajN = -8847496;
   private static final int aIz = -8856321;
   private final SettingGroup adb = this.settings.getDefaultGroup();
   private final SettingGroup aQn;
   private final SettingGroup aHW;
   private final Setting<Item> OD;
   private final Setting<Boolean> aBn;
   private final Setting<Boolean> Ja;
   private final Setting<String> mv;
   private final Setting<AliceObf_iN> zQ;
   private final Setting<Integer> SX;
   private final Setting<Boolean> aDa;
   private final Set<Screen> pO;
   private final HttpClient hY;
   private final AliceObf_fp awr;
   private final AliceObf_ea hR;
   private AliceObf_vj ayY;
   private int np;
   private int cc;
   private int azG;
   private String VM;
   private long ob;
   private double kX;
   private AliceObf_to agB;
   public AutoOrder() {
      super(AliceAddon.CATEGORY, "Auto-Order", "Tu dong hoan thanh lien tuc don hop le co gia cao nhat cho vat pham da chon.");
      this.aQn = this.settings.createGroup("Webhook");
      this.aHW = this.settings.createGroup("HUD");
      this.OD = this.adb.add(((Builder)((Builder)((Builder)new Builder().name("item")).description("item can hoan thanh. chon AIR de cho ma khong gui lenh")).defaultValue(Items.AIR)).build());
      this.aBn = this.adb
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("click-cauldron"))
                     .description("mo view tim kiem order bang nut cai vac"))
                  .defaultValue(true))
               .build()
         );
      this.Ja = this.aQn
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("webhook"))
                     .description("gui cap nhat session dat hang vao discord"))
                  .defaultValue(false))
               .build()
         );
      this.mv = this.aQn
         .add(
            ((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)new meteordevelopment.meteorclient.settings.StringSetting.Builder()
                           .name("webhook-url"))
                        .description("dia chi webhook discord"))
                     .defaultValue(""))
                  .visible(this.Ja::get))
               .build()
         );
      this.zQ = this.aQn
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("webhook-mode"))
                        .description("gui sau moi don hang hoac sau moi vai phut"))
                     .defaultValue(AliceObf_iN.ahb))
                  .visible(this.Ja::get))
               .build()
         );
      this.SX = this.aQn
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("send-on-minute"))
                        .description("so phut giua cac lan cap nhat webhook"))
                     .defaultValue(5))
                  .min(1)
                  .max(1440)
                  .sliderMax(60)
                  .visible(() -> (Boolean)this.Ja.get() && this.zQ.get() == AliceObf_iN.ahb))
               .build()
         );
      this.aDa = this.aHW
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("show-hud"))
                     .description("hien thi thong ke cua AutoOrder"))
                  .defaultValue(true))
               .build()
         );
      this.pO = Collections.newSetFromMap(new WeakHashMap<>());
      this.hY = HttpClient.newHttpClient();
      this.awr = new AliceObf_fp();
      this.hR = new AliceObf_ea();
      this.ayY = AliceObf_vj.Sd;
      this.VM = "";
      this.ju();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.ob = System.currentTimeMillis();
         this.kX = 0.0;
         this.awr.kd();
         this.agB = null;
         this.azG = 0;
         this.VM = "";
         this.hR.jY(this.ob, (Integer)this.SX.get());
                  AliceObf_vj var2 = this.OD.get() == Items.AIR ? AliceObf_vj.Sd : AliceObf_vj.aFa;
         this.jm(var2, 0);
         this.jv(this.mc.currentScreen, this.mc);
      }
   }

   public void onDeactivate() {
      this.agB = null;
      this.ayY = AliceObf_vj.aJI;
      super.onDeactivate();
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         return !this.isActive() ? null : this.jt();
      }
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @AliceHandler
   private void iP(Post var1) {
      if (this.isActive() && this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null) {
         this.jr();
         if (this.OD.get() == Items.AIR) {
            if (this.ayY != AliceObf_vj.Sd) {
               this.jq();
               this.agB = null;
               this.jm(AliceObf_vj.Sd, 0);
            }
         } else {
            if (this.ayY == AliceObf_vj.Sd) {
               this.jm(AliceObf_vj.aFa, 0);
            }

            if (this.np > 0) {
               this.np--;
            } else {
               this.cc++;
               if (this.cc >= 200) {
                  this.jl();
               } else {
                  switch (this.ayY.ordinal()) {
                     case 2:
                        this.iT();
                        break;
                     case 3:
                        this.iU();
                        break;
                     case 4:
                        this.iV();
                        break;
                     case 5:
                        this.iW();
                        break;
                     case 6:
                        this.iX();
                        break;
                     case 7:
                        this.iY();
                        break;
                     case 8:
                        this.iZ();
                        break;
                     case 9:
                        this.ja();
                        break;
                     case 10:
                        this.jb();
                        break;
                     case 11:
                        this.jc();
                  }
               }
            }
         }
      }
   }

   @AliceHandler
   private void iQ(Receive var1) {
      if (!(this.agB == null)) {
         short var15 = 21186;
      }

      if (!(var1.packet instanceof ScreenHandlerSlotUpdateS2CPacket var5)) {
         if (!(var1.packet instanceof InventoryS2CPacket var4)) {
            return;
         }

         int var2 = var4.syncId();
         int var3 = var4.revision();
         if (var2 == this.agB.Le && var3 >= this.agB.wb) {
            HashMap var13 = new HashMap();

            for (int var8 : this.agB.Ms.jU()) {
               ItemStack var9 = var8 >= 0 && var8 < var4.contents().size() ? (ItemStack)var4.contents().get(var8) : ItemStack.EMPTY;
               var13.put(var8, !var9.isOf((Item)this.OD.get()) ? 0 : var9.getCount());
            }

            this.awr.ka(this.agB.Ms.jS(var13));
         }
      } else {
         int var10 = var5.getSyncId();
         int var11 = var5.getRevision();
         if (var10 == this.agB.Le && var11 >= this.agB.wb && this.agB.Ms.jT(var5.getSlot())) {
            int var14 = !var5.getStack().isOf((Item)this.OD.get()) ? 0 : var5.getStack().getCount();
            this.awr.ka(this.agB.Ms.jR(var5.getSlot(), var14));
         }
      }
   }

   @AliceHandler
   private void iR(ReceiveMessageEvent var1) {
      if (this.isActive()) {
         AliceObf_nG.ki(var1.getMessage().getString())
            .ifPresent(var1x -> {
               this.kX = this.kX + var1x.jX();
               if ((Boolean)this.Ja.get() && this.zQ.get() == AliceObf_iN.apZ) {
                  this.js();
               }

               this.jl();
            });
      }
   }

   @AliceHandler
   private void iS(Render2DEvent var1) {
      if (!(this.mc.currentScreen instanceof HandledScreen)) {
         this.jw(var1.drawContext);
      }
   }

   private void iT() {
      this.jq();
      String var1 = AliceObf_nG.ke(Registries.ITEM.getId((Item)this.OD.get()).toString());
      if (!var1.isBlank() && this.OD.get() != Items.AIR) {
         this.mc.player.networkHandler.sendChatCommand(("" + var1));
         this.azG = 0;
         this.jm(AliceObf_vj.auj, this.jn());
      } else {
         this.jm(AliceObf_vj.Sd, 0);
      }
   }

   private void iU() {
      GenericContainerScreenHandler var1 = this.jo();
      if (var1 != null) {
         if (!(Boolean)this.aBn.get()) {
            this.jm(AliceObf_vj.CK, this.jn());
         } else {
            for (int var2 = 0; var2 < var1.slots.size(); var2++) {
               if (var1.getSlot(var2).getStack().isOf(Items.CAULDRON)) {
                  this.jp(var1, var2, SlotActionType.PICKUP);
                  this.jq();
                  this.jm(AliceObf_vj.ko, this.jn() * 3);
                  return;
               }
            }

            this.jm(AliceObf_vj.CK, this.jn());
         }
      }
   }

   private void iV() {
      if (this.jo() != null) {
         this.jm(AliceObf_vj.CK, this.jn());
      }
   }

   private void iW() {
      GenericContainerScreenHandler var1 = this.jo();
      if (var1 == null) {
         this.jl();
      } else {
         int var2 = this.jd(var1);
         if (var2 >= 0) {
            this.azG = 0;
            this.jp(var1, var2, SlotActionType.PICKUP);
            this.jm(AliceObf_vj.aBT, this.jn());
         } else if (var1.slots.size() > 53 && !var1.getSlot(53).getStack().isEmpty() && this.azG++ < 20) {
            this.jp(var1, 53, SlotActionType.PICKUP);
            this.VM = this.jh();
            this.jm(AliceObf_vj.rV, this.jn());
         } else {
            this.jl();
         }
      }
   }

   private void iX() {
      if (this.mc.currentScreen instanceof GenericContainerScreen var1) {
         String var3 = ji(var1.getTitle().getString());
         if (!var3.equals(this.VM)) {
            this.VM = var3;
            this.jm(AliceObf_vj.CK, 1);
         } else {
            this.np = 1;
         }
      } else {
         this.jl();
      }
   }

   private void iY() {
      if (this.mc.currentScreen instanceof GenericContainerScreen var1) {
         String var10 = ji(var1.getTitle().getString());
         if (!jj(var10)) {
            this.jl();
         } else {
            GenericContainerScreenHandler var3 = (GenericContainerScreenHandler)var1.getScreenHandler();
            boolean var4 = false;
            int var5 = Math.min(54, var3.slots.size());
            byte var6 = 0;
            if (!(var6 >= var5)) {
               short var12 = 28972;
            }

            if (!var4) {
               this.jq();
               this.jm(AliceObf_vj.hx, this.jn() * 5);
            } else {
               Map var11 = this.jf(var3);
               if (var11.isEmpty()) {
                  this.jq();
                  this.jm(AliceObf_vj.hx, this.jn() * 5);
               } else {
                  this.agB = new AliceObf_to(var3.syncId, var3.getRevision(), new AliceObf_kr(var11));

                  for (Slot var8 : var3.slots) {
                     if (var8.inventory == this.mc.player.getInventory() && var8.getStack().isOf((Item)this.OD.get())) {
                        this.mc.interactionManager.clickSlot(var3.syncId, var8.id, 0, SlotActionType.QUICK_MOVE, this.mc.player);
                     }
                  }

                  this.jm(AliceObf_vj.aBT, this.jn() * 2);
               }
            }
         }
      } else {
         this.jl();
      }
   }

   private void iZ() {
      if (this.jo() != null) {
         this.jm(AliceObf_vj.axy, this.jn());
      }
   }

   private void ja() {
      GenericContainerScreenHandler var1 = this.jo();
      if (var1 == null) {
         this.jm(AliceObf_vj.tZ, 0);
      } else if (var1.slots.size() <= 16) {
         this.jm(AliceObf_vj.tZ, 0);
      } else {
         this.jp(var1, 16, SlotActionType.PICKUP);
         this.jm(AliceObf_vj.aLp, this.jn());
      }
   }

   private void jb() {
      if (this.mc.currentScreen instanceof GenericContainerScreen var1) {
         if (this.jg()) {
            String var3 = ji(var1.getTitle().getString());
            if (!AutoOrder.jk(var3)) {
               if (var3.contains("(") && var3.contains("/")) {
                  this.jm(AliceObf_vj.CK, this.jn());
               } else {
                  this.np = this.jn();
               }
            } else {
               this.jm(AliceObf_vj.aBT, this.jn());
            }
         } else {
            this.jq();
            this.jm(AliceObf_vj.tZ, this.jn());
         }
      } else if (this.jg()) {
         this.jm(AliceObf_vj.aFa, this.jn());
      } else {
         this.np = this.jn();
      }
   }

   private void jc() {
      this.jq();
      this.agB = null;
      this.azG = 0;
      this.jm(this.OD.get() == Items.AIR ? AliceObf_vj.Sd : AliceObf_vj.aFa, this.jn());
   }

   private int jd(GenericContainerScreenHandler var1) {
      ArrayList var2 = new ArrayList();
      int var3 = Math.min(45, var1.slots.size());

      for (int var4 = 0; var4 < var3; var4++) {
         ItemStack var5 = var1.getSlot(var4).getStack();
         if (!var5.isEmpty() && var5.isOf((Item)this.OD.get())) {
            List var6 = AutoOrder.je(var5);
            var2.add(
               new AliceObf_sJ(
                  var4, AliceObf_nG.kf(var6), AliceObf_nG.kg(var6)
               )
            );
         }
      }

      return AliceObf_nG.kh(var2);
   }

   private static List<String> je(ItemStack var0) {
      LoreComponent var1 = (LoreComponent)var0.get(DataComponentTypes.LORE);
      if (var1 == null) {
         return Collections.emptyList();
      } else {
         ArrayList var2 = new ArrayList();

         for (Text var4 : var1.lines()) {
            var2.add(var4.getString());
         }

         return var2;
      }
   }

   private Map<Integer, Integer> jf(GenericContainerScreenHandler var1) {
      HashMap var2 = new HashMap();

      for (Slot var4 : var1.slots) {
         if (var4.inventory == this.mc.player.getInventory() && var4.getStack().isOf((Item)this.OD.get())) {
            var2.put(var4.id, var4.getStack().getCount());
         }
      }

      return var2;
   }

   private boolean jg() {
      if (this.mc.player != null) {
         int var10000 = this.OD.get() == Items.AIR ? 57766 : 57767;

         while (true) {
            int var2 = var10000;
            if (var2 == 57766) {
               break;
            }

            if (var2 != 57767) {
               for (int var1 = 0; var1 < this.mc.player.getInventory().size(); var1++) {
                  if (this.mc.player.getInventory().getStack(var1).isOf((Item)this.OD.get())) {
                     return true;
                  }
               }

               return false;
            }

         }
      }

      return false;
   }

   private String jh() {
      return this.mc.currentScreen instanceof GenericContainerScreen var1
         ? AutoOrder.ji(var1.getTitle().getString())
         : "";
   }

   private static String ji(String var0) {
      return var0 == null ? "" : var0.replaceAll("\u00a7.", "");
   }

   private static boolean jj(String var0) {
      return var0 != null && var0.contains("\u0299\u1ecf \u1d20\u1ead\u1d1b \u1d18\u029c\u1ea9\u1d0d \u1d20\u00e0\u1d0f \u0111\u1ec3 \u0262\u026a\u1d00\u1d0f");
   }

   private static boolean jk(String var0) {
      if (var0 != null) {
         if (var0.contains("\u0299\u1ecf \u1d20\u1ead\u1d1b \u1d18\u029c\u1ea9\u1d0d")) {

         } else {
            short var1 = 17597;
         }
      }

      return false;
   }

   private void jl() {
      this.agB = null;
      this.jq();
      this.azG = 0;
      this.jm(this.OD.get() == Items.AIR ? AliceObf_vj.Sd : AliceObf_vj.aFa, this.jn() * 3);
   }

   private void jm(AliceObf_vj var1, int var2) {
      this.ayY = var1;
      this.np = Math.max(0, var2);
      this.cc = 0;
   }

   private int jn() {
      if (this.mc.getNetworkHandler() != null && this.mc.player != null) {
         PlayerListEntry var1 = this.mc.getNetworkHandler().getPlayerListEntry(this.mc.player.getUuid());
         int var2 = var1 == null ? 100 : var1.getLatency();
         if (var2 < 50) {
            return 2;
         } else if (var2 < 100) {
            return 3;
         } else if (var2 < 200) {
            return 5;
         } else {
            return var2 < 300 ? 8 : 12;
         }
      } else {
         return 5;
      }
   }

   private GenericContainerScreenHandler jo() {
      return !(this.mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var1) ? null : var1;
   }

   private void jp(GenericContainerScreenHandler var1, int var2, SlotActionType var3) {
      if (var2 >= 0 && var2 < var1.slots.size()) {
         this.mc.interactionManager.clickSlot(var1.syncId, var2, 0, var3, this.mc.player);
      }
   }

   private void jq() {
      if (this.mc.player != null) {
         if (!(this.mc.currentScreen instanceof GenericContainerScreen)) {
            char var1 = 37506;
         }
      }
   }

   private void jr() {
      if ((Boolean)this.Ja.get() && !((String)this.mv.get()).isBlank() && this.zQ.get() == AliceObf_iN.ahb) {
         if (this.hR.jZ(System.currentTimeMillis(), (Integer)this.SX.get())) {
            this.js();
         }
      }
   }

   private void js() {
      String var1 = ((String)this.mv.get()).trim();
      if ((Boolean)this.Ja.get()) {
         if (var1.isBlank()) {

         } else {
            short var3 = 9015;
         }

         String var2 = AutoOrder.jA("Uptime", jy(this.jx()))
            + AutoOrder.jA("Stacks Ordered", Long.toString(this.awr.kb()))
            + jA("Money Earned", jz(this.kX));
         Thread.ofVirtual().name("order-session-webhook").start(() -> {
            try {
               HttpRequest var3x = HttpRequest.newBuilder().uri(URI.create(var1)).header("Content-Type", "application/json").POST(BodyPublishers.ofString(var2)).build();
               this.hY.send(var3x, BodyHandlers.discarding());
            } catch (Exception var4) {
            }
         });
      }
   }

   private String jt() {
      int var1 = this.ayY.ordinal() ^ -1404560449;
      String var10000;
      if (var1 == -1404560460 || var1 == -1404560459 || var1 == -1404560458 || var1 == -1404560457) {
         var10000 = "Confirming";
      } else if (var1 == -1404560456) {
         var10000 = "Delivering";
      } else if (var1 == -1404560455 || var1 == -1404560454) {
         var10000 = "Searching";
      } else if (var1 == -1404560453 || var1 == -1404560452) {
         var10000 = "Waiting GUI";
      } else if (var1 != -1404560451) {
         if (var1 != -1404560450) {
            if (var1 != -1404560449) {
               throw new MatchException(null, null);
            }

            var10000 = "Inactive";
         } else {
            var10000 = "Select Item";
         }
      } else {
         var10000 = "Sending /order";
      }

      return var10000;
   }

   private void ju() {
      ScreenEvents.AFTER_INIT.register(new AliceObf_hv(this));
   }

   private void jv(Screen var1, MinecraftClient var2) {
      if (var1 instanceof HandledScreen && this.pO.add(var1)) {
         ScreenEvents.afterRender(var1).register(new AliceObf_aF(this, var2));
      }
   }

   private void jw(DrawContext var1) {
      if (this.isActive() && (Boolean)this.aDa.get() && this.mc.player != null && var1 != null) {
         String[] var2 = new String[]{
            this.awr.kb() + "",
            ("" + jz(this.kX)),
            ("" + this.jt()),
            ("" + 
               AutoOrder.jy(this.jx())
            )
         };
         int[] var3 = new int[]{-1, -8847496, -8856321, -1};
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

   private long jx() {
      return this.ob <= 0L ? 0L : Math.max(0L, System.currentTimeMillis() - this.ob);
   }

   private static String jy(long var0) {
      long var2 = Math.max(0L, var0 / 1000L);
      return String.format(Locale.ROOT, "%02d:%02d:%02d", var2 / 3600L, var2 % 3600L / 60L, var2 % 60L);
   }

   private static String jz(double var0) {
      if (var0 >= 1.0E9) {
         return String.format(Locale.ROOT, "$%.2fB", var0 / 1.0E9);
      } else if (!(var0 >= 1000000.0)) {
         return !(var0 >= 1000.0) ? String.format(Locale.ROOT, "$%.0f", var0) : String.format(Locale.ROOT, "$%.2fK", var0 / 1000.0);
      } else {
         return String.format(Locale.ROOT, "$%.2fM", var0 / 1000000.0);
      }
   }

   private static String jA(String var0, String var1) {
      return AutoOrder.jB(var0)
         + AutoOrder.jB(var1);
   }

   private static String jB(String var0) {
      return var0.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n");
   }

   }

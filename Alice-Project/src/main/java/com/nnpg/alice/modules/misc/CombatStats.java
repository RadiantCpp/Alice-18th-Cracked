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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import meteordevelopment.meteorclient.events.entity.EntityAddedEvent;
import meteordevelopment.meteorclient.events.entity.EntityRemovedEvent;
import meteordevelopment.meteorclient.events.entity.player.AttackEntityEvent;
import meteordevelopment.meteorclient.events.entity.player.InteractBlockEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.IntSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.world.TickRate;
import net.minecraft.util.Hand;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.PlayerListEntry;
import org.joml.Matrix3x2fStack;

public final class CombatStats extends AliceModule {
   private static final long avF = 1000000000L;
   private static final long aEh = 100000000L;
   private static final long TS = 2000000000L;
   private static final long LQ = 8000000000L;
   private static final int awz = 512;
   private static final int mN = -1;
   private static final int ql = -3618616;
   private static final int iv = 12;
   private static final int aPc = 360;
   private final SettingGroup aQJ = this.settings.getDefaultGroup();
   private final SettingGroup NO;
   private final SettingGroup WM;
   private final Setting<Integer> aJ;
   private final Setting<AliceObf_ui> akv;
   private final Setting<Double> If;
   private final Setting<Boolean> akc;
   private final Setting<Boolean> KX;
   private final Setting<Boolean> oa;
   private final Setting<Boolean> mB;
   private final Setting<Boolean> aPi;
   private final Setting<Boolean> aoY;
   private final Setting<Boolean> pT;
   private final Setting<Boolean> Dy;
   private final Setting<Boolean> alR;
   private final Setting<Double> De;
   private final Setting<Double> ZS;
   private final Setting<Double> ZQ;
   private final Setting<Keybind> aKJ;
   private final int[] adc;
   private final Deque<AliceObf_kR> SS;
   private final Deque<AliceObf_kR> aNX;
   private final Deque<AliceObf_kR> aAu;
   private final Deque<AliceObf_kR> auf;
   private final Deque<AliceObf_kR> Ub;
   private final Deque<AliceObf_kR> aOC;
   private final Deque<AliceObf_kR> GN;
   private final Deque<AliceObf_kR> Jc;
   private final Deque<AliceObf_ac> xZ;
   private final Deque<AliceObf_qL> ex;
   private final Deque<AliceObf_ch> TA;
   private final Deque<String> asz;
   private final Map<UUID, Long> LC;
   private final Map<Integer, Long> amP;
   private UUID Hy;
   private String qR;
   private long Vx;
   private long ON;
   private long cw;
   private String xK;
   private long a;
   private Object aBy;
   private long BR;
   private double Yf;
   private double oq;
   private double uB;
   private float ayq;
   private float mX;
   private UUID OI;
   private int Rk;
   private int iC;
   private int Ri;
   private int Va;
   private int Hc;
   private boolean aIE;
   public CombatStats() {
      super(AliceAddon.MISC_CATEGORY, "combat-stats", "HUD PvP d\u1ea1ng t\u1eebng d\u00f2ng, \u0111o movement, crystal, totem pop, damage, target, t\u00e0i nguy\u00ean v\u00e0 network.");
      this.NO = this.settings.createGroup("Hi\u1ec3n th\u1ecb");
      this.WM = this.settings.createGroup("HUD");
      this.aJ = this.aQJ
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("c\u1eeda-s\u1ed5-gi\u00e2y")).description("C\u1eeda s\u1ed5 rolling cho t\u1ed1c \u0111\u1ed9, DPS v\u00e0 s\u1ed1 totem pop.")).defaultValue(5))
               .min(3)
               .max(15)
               .sliderRange((3), (15))
               .build()
         );
      this.akv = this.aQJ
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("target-mode"))
                     .description("Locked gi\u1eef m\u1ee5c ti\u00eau cu\u1ed1i c\u00f9ng \u0111\u00e3 \u0111\u00e1nh; Crosshair d\u00f9ng player \u0111ang ng\u1eafm."))
                  .defaultValue(AliceObf_ui.agT))
               .build()
         );
      this.If = this.aQJ
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("target-range"))
                  .description("Kho\u1ea3ng c\u00e1ch t\u1ed1i \u0111a \u0111\u1ec3 gi\u1eef m\u1ed9t m\u1ee5c ti\u00eau trong combat card."))
               .defaultValue(32.0)
               .min(4.0)
               .max(96.0)
               .sliderRange(4.0, 96.0)
               .build()
         );
      this.akc = this.aQJ
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("b\u1ecf-qua-friend"))
                     .description("Kh\u00f4ng t\u00ednh friend v\u00e0o enemy pop v\u00e0 target t\u1ef1 \u0111\u1ed9ng."))
                  .defaultValue(true))
               .build()
         );
      this.KX = this.NO
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("movement"))
                     .description("T\u1ed1c \u0111\u1ed9 hi\u1ec7n t\u1ea1i, trung b\u00ecnh v\u00e0 cao nh\u1ea5t trong c\u1eeda s\u1ed5."))
                  .defaultValue(true))
               .build()
         );
      this.oa = this.NO
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("crystal"))
                     .description("Input CPS, crystal spawn/removal v\u00e0 chu k\u1ef3 quan s\u00e1t \u0111\u01b0\u1ee3c."))
                  .defaultValue(true))
               .build()
         );
      this.mB = this.NO
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("totem"))
                     .description("Totem b\u1ea3n th\u00e2n, target v\u00e0 enemy trong c\u1eeda s\u1ed5 rolling."))
                  .defaultValue(true))
               .build()
         );
      this.aPi = this.NO
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("damage"))
                     .description("Damage nh\u1eadn/g\u00e2y quan s\u00e1t \u0111\u01b0\u1ee3c v\u00e0 attack attempts."))
                  .defaultValue(true))
               .build()
         );
      this.aoY = this.NO
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("t\u00e0i-nguy\u00ean"))
                     .description("Totem, crystal, obsidian v\u00e0 pearl c\u00f2n l\u1ea1i."))
                  .defaultValue(true))
               .build()
         );
      this.pT = this.NO
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("network"))
                     .description("Ping, TPS \u01b0\u1edbc t\u00ednh v\u00e0 FPS."))
                  .defaultValue(true))
               .build()
         );
      this.Dy = this.NO
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("last-event"))
                     .description("S\u1ef1 ki\u1ec7n combat g\u1ea7n nh\u1ea5t v\u00e0 th\u1eddi gian t\u1eeb s\u1ef1 ki\u1ec7n \u0111\u00f3."))
                  .defaultValue(true))
               .build()
         );
      this.alR = this.NO
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("thu-g\u1ecdn-khi-idle"))
                     .description("Khi kh\u00f4ng c\u00f3 combat, \u1ea9n c\u00e1c nh\u00f3m chi ti\u1ebft v\u00e0 gi\u1eef network c\u00f9ng s\u1ef1 ki\u1ec7n cu\u1ed1i."))
                  .defaultValue(false))
               .build()
         );
      this.De = this.WM
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("scale"))
                  .description("T\u1ec9 l\u1ec7 hi\u1ec3n th\u1ecb t\u1eebng d\u00f2ng telemetry."))
               .defaultValue(1.0)
               .min(0.75)
               .max(1.5)
               .sliderRange(0.75, 1.5)
               .build()
         );
      this.ZS = this.WM
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("x"))
                     .description("V\u1ecb tr\u00ed ngang, ch\u1ec9nh ch\u00ednh x\u00e1c b\u1eb1ng edit HUD."))
                  .defaultValue(0.72)
                  .min(0.0)
                  .sliderRange(0.0, 1.0)
                  .visible(() -> false))
               .build()
         );
      this.ZQ = this.WM
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("y"))
                     .description("V\u1ecb tr\u00ed d\u1ecdc, ch\u1ec9nh ch\u00ednh x\u00e1c b\u1eb1ng edit HUD."))
                  .defaultValue(0.05)
                  .min(0.0)
                  .sliderRange(0.0, 1.0)
                  .visible(() -> false))
               .build()
         );
      this.aKJ = this.WM
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("ph\u00edm-ch\u1ec9nh"))
                     .description("M\u1edf tr\u00ecnh ch\u1ec9nh v\u1ecb tr\u00ed d\u00f2ng telemetry."))
                  .defaultValue(Keybind.none()))
               .action(this::tQ)
               .build()
         );
      this.adc = new int[(4)];
      this.SS = new ArrayDeque<>();
      this.aNX = new ArrayDeque<>();
      this.aAu = new ArrayDeque<>();
      this.auf = new ArrayDeque<>();
      this.Ub = new ArrayDeque<>();
      this.aOC = new ArrayDeque<>();
      this.GN = new ArrayDeque<>();
      this.Jc = new ArrayDeque<>();
      this.xZ = new ArrayDeque<>();
      this.ex = new ArrayDeque<>();
      this.TA = new ArrayDeque<>();
      this.asz = new ArrayDeque<>();
      this.LC = new HashMap<>();
      this.amP = new HashMap<>();
      this.qR = "No target";
      this.xK = "Waiting for combat";
      this.ayq = Float.NaN;
      this.mX = Float.NaN;
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         long var1 = CombatStats.uP();
         this.uh(var1);
         int var3 = this.up(var1);
         return var3 + CombatStats.uR(this.uB);
      }
   }

   public WWidget getWidget(GuiTheme var1) {
      if (PremiumAccessGate.isAllowed()) {
         WTable var2 = var1.table();
         WButton var3 = (WButton)var2.add(
               var1.button(
                  "Ch\u1ec9nh v\u1ecb tr\u00ed HUD"
               )
            )
            .expandX()
            .widget();
         var3.action = this::tQ;
         return var2;
      } else {
         return null;
      }
   }

   public void tQ() {
      super.mc.setScreen(new AliceObf_rN(this, super.mc.currentScreen));
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.uK();
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.uK();
      super.onDeactivate();
   }

   @AliceHandler
   private void tR(Post var1) {
      long var2 = CombatStats.uP();
      if (super.mc.player != null && super.mc.world != null && super.mc.getNetworkHandler() != null) {
         if (!super.mc.player.isDead()) {
            if (this.aBy != super.mc.world) {
               this.uK();
               this.aBy = super.mc.world;
            }

            this.aIE = true;
            this.tY(var2);
            this.tZ(var2);
            this.ua(var2);
            this.uh(var2);
         } else {
            this.uK();
            this.aIE = true;
         }
      } else {
         if (this.aIE) {
            this.uK();
         }

         this.aIE = false;
      }
   }

   @AliceHandler(
      priority = 100
   )
   private void tS(AttackEntityEvent var1) {
      if (!var1.isCancelled() && super.mc.player != null && super.mc.world != null) {
         if (var1.entity instanceof PlayerEntity var3) {
            if (!this.ul(var3)) {

            } else {
               char var6 = 59638;
            }

            long var4 = CombatStats.uP();
            this.auf.addLast(new AliceObf_kR(var4, 1.0));
            this.Va++;
            this.Hy = var3.getUuid();
            this.qR = var3.getName().getString();
            this.Vx = var4;
            this.uI(
               var4,
               ("" + 
                  CombatStats.uQ(this.qR)
               )
            );
         }
      }
   }

   @AliceHandler
   private void tT(InteractBlockEvent var1) {
      if (super.mc.player != null && super.mc.world != null && var1.result != null) {
         if (var1.hand == Hand.MAIN_HAND || var1.hand == Hand.OFF_HAND) {
            if (this.un(var1.hand)) {
               BlockHitResult var2 = var1.result;
               if (var2.getType() == HitResult.Type.BLOCK) {
                  long var3 = CombatStats.uP();
                  this.Ub.addLast(new AliceObf_kR(var3, 1.0));
                  this.TA.addLast(new AliceObf_ch(var3, var2.getBlockPos().toImmutable()));
                  this.uI(var3, "Crystal input");
               }
            }
         }
      }
   }

   @AliceHandler(
      priority = 100
   )
   private void tU(EntityAddedEvent var1) {
      if (var1.entity instanceof EndCrystalEntity var2 && super.mc.player != null && super.mc.world != null) {
         long var8 = CombatStats.uP();
         this.aOC.addLast(new AliceObf_kR(var8, 1.0));
         this.amP.put(var2.getId(), var8);
         this.Ri++;
         AliceObf_ch var5 = this.uj(
            var2.getBlockPos().down(), var8
         );
         if (var5 != null) {
            long var6 = Math.max(0L, (var8 - var5.tE()) / (1000000));
            if (var6 <= (2000)) {

            } else {
               short var9 = 5680;
            }

            this.uJ(var6 + "");
         } else {
            this.uJ(
               "Crystal observed"
            );
         }

         this.uI(var8, null);
      }
   }

   @AliceHandler
   private void tV(EntityRemovedEvent var1) {
      if (var1.entity instanceof EndCrystalEntity var2) {
         long var6 = CombatStats.uP();
         this.GN.addLast(new AliceObf_kR(var6, 1.0));
         Long var5 = (Long)this.amP.remove(var2.getId());
         if (var5 != null && var6 >= var5 && var6 - var5 <= (2115098112)) {
            this.ex.addLast(new AliceObf_qL(var6, (var6 - var5) / (1000000)));
         }

         this.uI(
            var6, "Crystal removed"
         );
      }
   }

   @AliceHandler
   private void tW(Receive var1) {
      if (var1.packet instanceof EntityStatusS2CPacket var2 && var2.getStatus() == 35) {
         long var6 = this.BR;
         ClientWorld var5 = super.mc.world;
         super.mc.execute(() -> {
            if (this.isActive() && super.mc.world != null && super.mc.world == var5 && super.mc.player != null && var6 == this.BR) {
               if (var2.getEntity(super.mc.world) instanceof PlayerEntity var6x && this.um(var6x)) {
                  this.ub(var6x, CombatStats.uP());
               }
            }
         });
      }
   }

   @AliceHandler
   private void tX(Render2DEvent var1) {
      if (this.isActive() && super.mc.player != null && super.mc.world != null && super.mc.currentScreen == null) {
         this.uc(var1.drawContext, var1.screenWidth, var1.screenHeight, false);
      }
   }

   private void tY(long var1) {
      PlayerEntity var3 = null;
      if (this.akv.get() != AliceObf_ui.aET) {
         if (this.Hy != null && super.mc.world != null) {
            PlayerEntity var4 = super.mc.world.getPlayerByUuid(this.Hy);
            if (var4 != null && this.ul(var4) && var4.distanceTo(super.mc.player) <= (Double)this.If.get()) {
               var3 = var4;
            }
         }
      } else if (super.mc.targetedEntity instanceof PlayerEntity var6 && this.ul(var6)) {
         if (var6.distanceTo(super.mc.player) <= (Double)this.If.get()) {

         } else {
            short var7 = 17521;
         }
      }

      if (var3 == null) {
         short var8 = 12135;
      } else {
         short var9 = 12136;
      }

      if (var3 != null) {
         this.Hy = var3.getUuid();
         this.qR = var3.getName().getString();
         this.Vx = var1;
      } else if (this.Hy != null && var1 - this.Vx > (-589934592)) {
         this.Hy = null;
         this.qR = "No target";
         this.mX = Float.NaN;
         this.OI = null;
      }
   }

   private void tZ(long var1) {
      if (this.a != (0)) {
         double var3 = (var1 - this.a) / 1.0E9;
         double var5 = super.mc.player.getX() - this.Yf;
         double var7 = super.mc.player.getZ() - this.oq;
         double var9 = Math.sqrt(var5 * var5 + var7 * var7);
         if (var3 > 0.005 && var3 <= 1.0) {
            if (!(var9 <= 8.0)) {
               char var11 = 59494;
            }
         }

         if (var9 > 8.0 || !Double.isFinite(var9)) {
            this.uB = 0.0;
         }
      }

      this.a = var1;
      this.Yf = super.mc.player.getX();
      this.oq = super.mc.player.getZ();
   }

   private void ua(long var1) {
      float var3 = CombatStats.uL(super.mc.player);
      if (Float.isFinite(this.ayq) && this.ayq - var3 > 0.05F) {
         double var4 = CombatStats.uM(this.ayq - var3);
         this.aAu.addLast(new AliceObf_kR(var1, var4));
         this.uI(
            var1,
            ("" + 
               CombatStats.uR(var4)
            )
         );
      }

      this.ayq = var3;
      PlayerEntity var8 = this.uk();
      if (var8 == null) {
         this.mX = Float.NaN;
         this.OI = null;
      } else {
         float var5 = CombatStats.uL(var8);
         if (!var8.getUuid().equals(this.OI)) {
            this.OI = var8.getUuid();
            this.mX = var5;
         } else {
            if (Float.isFinite(this.mX) && this.mX - var5 > 0.05F) {
               double var6 = CombatStats.uM(this.mX - var5);
               this.aNX.addLast(new AliceObf_kR(var1, var6));
               this.Hc++;
               this.uI(
                  var1,
                  ("" + 
                     CombatStats.uR(var6)
                  )
               );
            }

            this.mX = var5;
         }
      }
   }

   private void ub(PlayerEntity var1, long var2) {
      UUID var4 = var1.getUuid();
      long var5 = this.LC.getOrDefault(var4, 0L);
      if (var2 - var5 >= (100000000)) {
         this.LC.put(var4, var2);
         int var7 = var1 == super.mc.player ? 1 : (0);
         this.xZ.addLast(new AliceObf_ac(var2, var1.getUuid(), (var7 != 0)));
         if (var7 != 0) {
            this.Rk += (1);
         } else {
            this.iC++;
         }

         String var8 = var7 != 0
            ? "Self"
            : CombatStats.uQ(var1.getName().getString());
         this.uI(var2, ("" + var8));
      }
   }

   public void uc(DrawContext var1, int var2, int var3, boolean var4) {
      long var5 = CombatStats.uP();
      this.uh(var5);
      List var7 = this.ue(var5);
      int var8 = 14 + Math.max(1, var7.size()) * 12;
      double var9 = CombatStats.uN(
         (Double)this.De.get(), 0.75, 1.5
      );
      int var11 = Math.max((1), (int)Math.round(360.0 * var9));
      int var12 = Math.max(1, (int)Math.round(var8 * var9));
      int var13 = Math.max((0), var2 - var11);
      int var14 = Math.max((0), var3 - var12);
      int var15 = (int)Math.round(
         CombatStats.uO((Double)this.ZS.get()) * var13
      );
      int var16 = (int)Math.round(
         CombatStats.uO((Double)this.ZQ.get()) * var14
      );
      this.adc[0] = var15;
      this.adc[(1)] = var16;
      this.adc[(2)] = var11;
      this.adc[3] = var12;
      Matrix3x2fStack var17 = var1.getMatrices();
      var17.pushMatrix();

      try {
         var17.translate(var15, var16);
         var17.scale((float)var9, (float)var9);
         this.ud(var1, var7);
      } finally {
         var17.popMatrix();
      }
   }

   private void ud(DrawContext var1, List<AliceObf_iJ> var2) {
      TextRenderer var3 = super.mc.textRenderer;
      int var4 = 0;
      var1.drawTextWithShadow(
         var3,
         "ALICE  //  COMBAT STATS",
         0,
         var4,
         -1
      );
      var4 += 14;
      int var5 = (0);

      for (AliceObf_iJ var7 : var2) {
         var5 = Math.max(var5, var3.getWidth(var7.tJ()));
      }

      for (AliceObf_iJ var11 : var2) {
         var1.drawTextWithShadow(var3, var11.tJ(), 0, var4, (-1));
         String var8 = var3.trimToWidth(var11.tK(), Math.max((20), 360 - var5 - 8));
         var1.drawTextWithShadow(var3, var8, var5 + 8, var4, -3618616);
         var4 += 12;
      }
   }

   private List<AliceObf_iJ> ue(long var1) {
      int var3 = var1 - this.ON > (-589934592) ? (1) : 0;
      ArrayList var4 = new ArrayList();
      PlayerEntity var5 = this.uk();
      if (var5 != null) {
         var4.add(
            new AliceObf_iJ(
               "TARGET",
               CombatStats.uQ(this.qR)
                  + CombatStats.uR(
                     CombatStats.uL(var5)
                  )
                  + CombatStats.uR(
                     var5.distanceTo(super.mc.player)
                  )
                  + CombatStats.uD(var5)
            )
         );
      } else if (!(Boolean)this.alR.get() || var3 == 0) {
         var4.add(
            new AliceObf_iJ(
               "TARGET",
               "\u2014"
            )
         );
      }

      if (!(Boolean)this.alR.get() || var3 == 0) {
         if ((Boolean)this.KX.get()) {
            var4.add(
               new AliceObf_iJ(
                  "MOVE",
                  (
                        this.SS.isEmpty()
                           ? "\u2014"
                           : ("" + 
                              CombatStats.uR(this.uB)
                           )
                     )
                     + CombatStats.uA(
                        this.ur(this.SS, var1), this.SS.isEmpty()
                     )
                     + CombatStats.uA(
                        this.us(this.SS), this.SS.isEmpty()
                     )
               )
            );
         }

         if ((Boolean)this.oa.get()) {
            double var6 = this.ux(this.Ub, var1);
            double var8 = this.ux(this.aOC, var1);
            double var10 = this.ux(this.GN, var1);
            double var12 = this.uu(this.Jc, 0.5);
            double var14 = this.ut(0.5);
            String var16 = !(var12 >= 0.0)
               ? "P \u2014"
               : ("" + 
                  CombatStats.uS(var12)
               );
            if (var14 >= 0.0) {
               var16 = var16 + CombatStats.uS(var14);
            }

            var4.add(
               new AliceObf_iJ(
                  "CRYSTAL",
                  this.uy(this.aOC, var8) + this.uy(this.Ub, var6) + this.uy(this.GN, var10) + var16 + this.Ri
               )
            );
         }

         if ((Boolean)this.mB.get()) {

         } else {
            short var17 = 12003;
         }

         if ((Boolean)this.aPi.get()) {
            var4.add(
               new AliceObf_iJ(
                  "DAMAGE",
                  this.uz(this.aNX) + this.uz(this.aAu) + this.uy(this.auf, this.ux(this.auf, var1)) + this.Hc + this.Va
               )
            );
         }
      }

      if ((Boolean)this.aoY.get()) {
         var4.add(
            new AliceObf_iJ(
               "STOCK",
               "" + this.uC(Items.TOTEM_OF_UNDYING) + this.uC(Items.END_CRYSTAL) + this.uC(Items.OBSIDIAN) + this.uC(Items.ENDER_PEARL)
            )
         );
      }

      if ((Boolean)this.pT.get()) {
         var4.add(
            new AliceObf_iJ(
               "NETWORK",
               this.uF() + this.uG() + this.uH()
            )
         );
      }

      if ((Boolean)this.Dy.get() && !this.xK.isEmpty() && this.cw > (0)) {
         var4.add(
            new AliceObf_iJ(
               "LAST",
               this.xK + CombatStats.uT(var1 - this.cw)
            )
         );
      }

      return var4;
   }

   public int[] uf() {
      return this.adc;
   }

   public void setTopLeft(int var1, int var2, int var3, int var4) {
      double var5 = (Double)this.De.get();
      int var7 = Math.max((1), (int)Math.round(360.0 * var5));
      int var8 = Math.max(
         1,
         (int)Math.round(
            (
                  (14)
                     + Math.max(1, this.ue(CombatStats.uP()).size())
                        * 12
               )
               * var5
         )
      );
      int var9 = Math.max(0, var3 - var7);
      int var10 = Math.max((0), var4 - var8);
      var1 = Math.max((0), Math.min(var1, var9));
      var2 = Math.max((0), Math.min(var2, var10));
      this.ZS.set(var9 > 0 ? (double)var1 / var9 : 0.0);
      this.ZQ.set(var10 > 0 ? (double)var2 / var10 : 0.0);
   }

   public double ug() {
      return (Double)this.De.get();
   }

   private void uh(long var1) {
      long var3 = var1 - ((Integer)this.aJ.get()).intValue() * (1000000000);
      this.ui(this.SS, var3);
      this.ui(this.aNX, var3);
      this.ui(this.aAu, var3);
      this.ui(this.auf, var3);
      this.ui(this.Ub, var3);
      this.ui(this.aOC, var3);
      this.ui(this.GN, var3);
      this.ui(this.Jc, var3);

      while (!this.xZ.isEmpty() && ((AliceObf_ac)this.xZ.peekFirst()).tG() < var3) {
         this.xZ.removeFirst();
      }

      while (!this.ex.isEmpty() && ((AliceObf_qL)this.ex.peekFirst()).tC() < var3) {
         this.ex.removeFirst();
      }

      while (!this.TA.isEmpty() && var1 - ((AliceObf_ch)this.TA.peekFirst()).tE() > (2000000000)) {
         this.TA.removeFirst();
      }

      Iterator<Map.Entry<Integer, Long>> var5 = this.amP.entrySet().iterator();

      while (var5.hasNext() && var1 - var5.next().getValue() > 2115098112L) {
         var5.remove();
      }

      while (this.amP.size() > 512) {
         this.amP.remove(this.amP.keySet().iterator().next());
      }

      while (this.LC.size() > (256)) {
         this.LC.remove(this.LC.keySet().iterator().next());
      }
   }

   private void ui(Deque<AliceObf_kR> var1, long var2) {
      while (!var1.isEmpty() && ((AliceObf_kR)var1.peekFirst()).tL() < var2) {
         var1.removeFirst();
      }

      while (var1.size() > 512) {
         var1.removeFirst();
      }
   }

   private AliceObf_ch uj(BlockPos var1, long var2) {
      AliceObf_ch var4 = null;

      for (AliceObf_ch var6 : this.TA) {
         if (var2 - var6.tE() <= (2000000000) && var6.tF().equals(var1) && (var4 == null || var6.tE() < var4.tE())) {
            var4 = var6;
         }
      }

      if (var4 != null) {
         this.TA.remove(var4);
      }

      return var4;
   }

   private PlayerEntity uk() {
      if (this.Hy != null && super.mc.world != null) {
         PlayerEntity var1 = super.mc.world.getPlayerByUuid(this.Hy);
         if (var1 != null) {
            if (this.ul(var1)) {

            } else {
               short var2 = 6322;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private boolean ul(PlayerEntity var1) {
      if (var1 != null && super.mc.player != null && var1 != super.mc.player && !var1.isSpectator()) {
         int var10000 = this.akc.get() ? 33728 : 33729;

         while (true) {
            int var2 = var10000;
            if (var2 == 33728) {
               if (!Friends.get().shouldAttack(var1)) {
                  var10000 = (0);
                  break;
               }
            } else if (var2 == 33729) {

               continue;
            }

            break;
         }

         return (var10000 != 0);
      } else {
         return true;
      }
   }

   private boolean um(PlayerEntity var1) {
      return (boolean)(var1 != super.mc.player && !this.ul(var1));
   }

   private boolean un(Hand var1) {
      return (var1 == Hand.MAIN_HAND ? super.mc.player.getMainHandStack() : super.mc.player.getOffHandStack()).isOf(Items.END_CRYSTAL);
   }

   private int uo(long var1) {
      byte var3 = 0;

      for (AliceObf_ac var5 : this.xZ) {
         if (var5.tI()) {

         } else {
            char var6 = 47649;
         }
      }

      return var3;
   }

   private int up(long var1) {
      if (this.Hy == null) {
         return 0;
      } else {
         int var3 = (0);

         for (AliceObf_ac var5 : this.xZ) {
            if (!var5.tI() && this.Hy.equals(var5.tH())) {
               var3++;
            }
         }

         return var3;
      }
   }

   private int uq(long var1) {
      int var3 = (0);

      for (AliceObf_ac var5 : this.xZ) {
         if (!var5.tI()) {
            var3++;
         }
      }

      return var3;
   }

   private double ur(Deque<AliceObf_kR> var1, long var2) {
      if (var1.isEmpty()) {
         return 0.0;
      } else {
         double var4 = 0.0;
         Iterator var6 = var1.iterator();
         if (var6.hasNext()) {

         } else {
            char var8 = 50179;
         }

         return var4 / var1.size();
      }
   }

   private double us(Deque<AliceObf_kR> var1) {
      double var2 = 0.0;

      for (AliceObf_kR var5 : var1) {
         var2 = Math.max(var2, var5.tM());
      }

      return var2;
   }

   private double ut(double var1) {
      if (this.ex.isEmpty()) {
         return -1.0;
      } else {
         ArrayList var3 = new ArrayList(this.ex.size());

         for (AliceObf_qL var5 : this.ex) {
            var3.add(var5.tD());
         }

         return CombatStats.uv(var3, var1);
      }
   }

   private double uu(Deque<AliceObf_kR> var1, double var2) {
      if (var1.isEmpty()) {
         return -1.0;
      } else {
         ArrayList var4 = new ArrayList(var1.size());

         for (AliceObf_kR var6 : var1) {
            var4.add(Math.round(var6.tM()));
         }

         return CombatStats.uv(var4, var2);
      }
   }

   private static double uv(List<Long> var0, double var1) {
      if (var0.isEmpty()) {
         return -1.0;
      } else {
         var0.sort(Long::compareTo);
         double var3 = CombatStats.uN(var1, 0.0, 1.0)
            * (var0.size() - (1));
         int var5 = (int)Math.floor(var3);
         int var6 = (int)Math.ceil(var3);
         if (var5 != var6) {
            double var7 = var3 - var5;
            return ((Long)var0.get(var5)).longValue() + ((Long)var0.get(var6) - (Long)var0.get(var5)) * var7;
         } else {
            return ((Long)var0.get(var5)).longValue();
         }
      }
   }

   private double uw(Deque<AliceObf_kR> var1, long var2) {
      double var4 = 0.0;

      for (AliceObf_kR var7 : var1) {
         var4 += var7.tM();
      }

      return Math.min(1000000.0, var4);
   }

   private double ux(Deque<AliceObf_kR> var1, long var2) {
      double var4 = Math.max(0.25, (double)((Integer)this.aJ.get()).intValue());
      return Math.min(1000.0, var1.size() / var4);
   }

   private String uy(Deque<AliceObf_kR> var1, double var2) {
      return var1.isEmpty()
         ? "\u2014"
         : ("" + 
            CombatStats.uR(var2)
         );
   }

   private String uz(Deque<AliceObf_kR> var1) {
      return var1.isEmpty()
         ? "\u2014"
         : ("" + 
            CombatStats.uR(this.uB(var1))
         );
   }

   private static String uA(double var0, boolean var2) {
      return var2
         ? "\u2014"
         : CombatStats.uR(var0);
   }

   private double uB(Deque<AliceObf_kR> var1) {
      double var2 = Math.max(0.25, (double)((Integer)this.aJ.get()).intValue());
      return Math.min(
         1000.0, this.uw(var1, CombatStats.uP()) / var2
      );
   }

   private int uC(Item var1) {
      if (super.mc.player == null) {
         return (0);
      } else {
         int var2 = (0);

         for (int var3 = (0); var3 < (36); var3++) {
            ItemStack var4 = super.mc.player.getInventory().getStack(var3);
            int var10000 = var4.isOf(var1) ? 17239 : 17240;

            while (true) {
               int var5 = var10000;
               if (var5 == 17239) {
                  var2 += var4.getCount();
                  break;
               }

               if (var5 != 17240) {
                  break;
               }

            }
         }

         ItemStack var6 = super.mc.player.getOffHandStack();
         if (var6.isOf(var1)) {
            var2 += var6.getCount();
         }

         return var2;
      }
   }

   private static int uD(PlayerEntity var0) {
      int var1 = (100);
      byte var2 = 0;
      EquipmentSlot[] var10000 = new EquipmentSlot[4];
      var10000[(0)] = EquipmentSlot.HEAD;
      var10000[1] = EquipmentSlot.CHEST;
      var10000[2] = EquipmentSlot.LEGS;
      var10000[3] = EquipmentSlot.FEET;
      EquipmentSlot[] var3 = var10000;
      int var4 = var3.length;

      for (int var5 = (0); var5 < var4; var5++) {
         EquipmentSlot var6 = var3[var5];
         ItemStack var7 = var0.getEquippedStack(var6);
         if (var7.isDamageable()) {
            int var10 = var7.getMaxDamage() <= 0 ? 5856 : 5857;

            while (true) {
               int var9 = var10;
               if (var9 == 5856) {
                  break;
               }

               if (var9 != 5857) {
                  var2 = 1;
                  int var8 = Math.max((0), var7.getMaxDamage() - var7.getDamage());
                  var1 = Math.min(var1, var8 * (100) / var7.getMaxDamage());
                  break;
               }

               var10 = 5855;
            }
         }
      }

      return var2 != 0 ? var1 : (0);
   }

   private int uE() {
      if (super.mc.player != null && super.mc.getNetworkHandler() != null) {
         PlayerListEntry var1 = super.mc.getNetworkHandler().getPlayerListEntry(super.mc.player.getUuid());
         return var1 == null ? (-1) : Math.max(0, var1.getLatency());
      } else {
         return -1;
      }
   }

   private String uF() {
      int var1 = this.uE();
      return var1 < 0
         ? "\u2014"
         : Integer.toString(var1);
   }

   private String uG() {
      float var1 = TickRate.INSTANCE.getTickRate();
      return Float.isFinite(var1) && var1 > 0.0F
         ? CombatStats.uR(
            MathHelper.clamp(var1, 0.0F, 20.0F)
         )
         : "\u2014";
   }

   private String uH() {
      int var1 = super.mc.getCurrentFps();
      if (!(var1 > 0)) {
         char var2 = 61885;
      }

      return Integer.toString(var1);
   }

   private void uI(long var1, String var3) {
      this.ON = var1;
      if (var3 != null) {
         this.uJ(var3);
      }
   }

   private void uJ(String var1) {
      this.xK = var1;
      this.cw = CombatStats.uP();
      this.asz.addLast(var1);
      if (this.asz.size() > (5)) {

      } else {
         char var2 = 61845;
      }
   }

   private void uK() {
      this.aBy = null;
      this.BR += 1L;
      this.SS.clear();
      this.aNX.clear();
      this.aAu.clear();
      this.auf.clear();
      this.Ub.clear();
      this.aOC.clear();
      this.GN.clear();
      this.Jc.clear();
      this.xZ.clear();
      this.ex.clear();
      this.TA.clear();
      this.asz.clear();
      this.LC.clear();
      this.amP.clear();
      this.Hy = null;
      this.qR = "No target";
      this.Vx = 0L;
      this.ON = 0L;
      this.cw = 0L;
      this.xK = "Waiting for combat";
      this.a = 0L;
      this.ayq = Float.NaN;
      this.mX = Float.NaN;
      this.OI = null;
      this.uB = 0.0;
      this.Rk = (0);
      this.iC = 0;
      this.Ri = (0);
      this.Va = 0;
      this.Hc = (0);
   }

   private static float uL(PlayerEntity var0) {
      return var0 == null ? Float.NaN : var0.getHealth() + var0.getAbsorptionAmount();
   }

   private static double uM(double var0) {
      return !Double.isFinite(var0) ? 0.0 : MathHelper.clamp(var0, 0.0, 1000.0);
   }

   private static double uN(double var0, double var2, double var4) {
      return Double.isFinite(var0) ? Math.max(var2, Math.min(var4, var0)) : var2;
   }

   private static double uO(double var0) {
      return Double.isFinite(var0) && !(var0 < 0.0) ? Math.min(1.0, var0) : 0.0;
   }

   private static long uP() {
      return System.nanoTime();
   }

   private static String uQ(String var0) {
      if (var0 != null && !var0.isBlank()) {
         return var0.length() > (14)
            ? ("" + var0.substring((0), (13)))
            : var0;
      } else {
         return "Unknown";
      }
   }

   private static String uR(double var0) {
      return !Double.isFinite(var0)
         ? "\u2014"
         : String.format(Locale.ROOT, "%.1f", var0);
   }

   private static String uS(double var0) {
      if (Double.isFinite(var0)) {
         if (!(var0 < 0.0)) {

         } else {
            short var2 = 24273;
         }

         return var0 >= 1000.0
            ? ("" + 
               CombatStats.uR(var0 / 1000.0)
            )
            : Math.round(var0) + "";
      } else {
         return "\u2014";
      }
   }

   private static String uT(long var0) {
      if (var0 < (0)) {
         return "\u2014";
      } else {
         double var2 = var0 / 1.0E9;
         if (!(var2 < 10.0)) {
            short var4 = 481;
         }

         return Math.round(var2) + "";
      }
   }

   }

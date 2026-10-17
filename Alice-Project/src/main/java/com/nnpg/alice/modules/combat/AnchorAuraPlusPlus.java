package com.nnpg.alice.modules.combat;

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.DoubleSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.entity.DamageUtils;
import meteordevelopment.meteorclient.utils.entity.DamageUtils.RaycastFactory;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ClientPlayerEntity;
import org.joml.Vector3d;

public final class AnchorAuraPlusPlus extends AliceModule implements AliceObf_jQ, AliceObf_pl {
   private static final int qg = 140;
   private static final int aar = 12;
   private static final int yQ = 24;
   private static final double IB = 6.0;
   private static final double yL = 0.03;
   private static final double yz = 0.0;
   private final SettingGroup aHh = this.settings.getDefaultGroup();
   private final SettingGroup awG;
   private final SettingGroup Qn;
   private final SettingGroup MX;
   private final SettingGroup pB;
   private final Setting<Double> acI;
   private final Setting<Double> acF;
   private final Setting<Double> afy;
   private final Setting<Double> anA;
   private final Setting<Double> aph;
   private final Setting<Boolean> bu;
   private final Setting<AliceObf_qe> TC;
   private final Setting<Double> Zk;
   private final Setting<Boolean> anW;
   private final Setting<Double> aIZ;
   private final Setting<AliceObf_lh> bk;
   private final Setting<AliceObf_po> aKW;
   private final Setting<AliceObf_lh> aNk;
   private final Setting<Integer> vl;
   private final Setting<Boolean> Sq;
   private final Setting<Integer> aJp;
   private final Setting<Integer> iU;
   private final Setting<Integer> vG;
   private final Setting<Double> arM;
   private final Setting<Boolean> afu;
   private final Setting<Boolean> sM;
   private final Setting<Boolean> NI;
   private final Setting<ShapeMode> QH;
   private final Setting<SettingColor> acX;
   private final Setting<SettingColor> alb;
   private final Setting<SettingColor> aiv;
   private final Setting<SettingColor> Yq;
   private final Setting<Boolean> ZK;
   private final Setting<Boolean> aom;
   private final Setting<Double> sF;
   private final Setting<SettingColor> Nq;
   private final Setting<Integer> yc;
   private final AliceObf_hu LF;
   private final List<BlockPos> kN;
   private final List<AliceObf_li> uf;
   private final List<AliceObf_c> aNI;
   private final Vector3d SH;
   private AliceObf_dv aHE;
   private AliceObf_il acm;
   private boolean Ge;
   private int ahh;
   private int aHw;
   private int wl;
   private int azs;
   private long JU;
   private int ayG;
   private int Wg;
   private boolean dn;
   private boolean amo;
   private boolean FU;
   private float awN;
   private float avs;
   private boolean abQ;
   private AliceObf_hQ aao;
   private AliceObf_aC ih;
   private AliceObf_lB aFC;
   private ClientWorld aQK;
   private ClientPlayerEntity atQ;
   private boolean gM;
   private int ei;
   private int aS;
   private int aqM;
   private final AliceObf_mF apc;
   public AnchorAuraPlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "anchor-aura++", "Anchor Aura damage-first, zero-tick chain va packet order danh cho Grim.");
      this.awG = this.settings.createGroup("Safety");
      this.Qn = this.settings.createGroup("Grim");
      this.MX = this.settings.createGroup("Burst");
      this.pB = this.settings.createGroup("ESP");
      this.acI = this.aHh
         .add(
            ((Builder)((Builder)new Builder().name("target-range")).description("Tam tim nguoi choi de toi uu damage."))
               .defaultValue(10.0)
               .range(1.0, 16.0)
               .sliderRange(1.0, 16.0)
               .build()
         );
      this.acF = this.aHh
         .add(
            ((Builder)((Builder)new Builder().name("action-range")).description("Tam dat, nap va kich no. 4.5 la vanilla/Grim safe."))
               .defaultValue(4.5)
               .range(1.0, 6.0)
               .sliderRange(1.0, 6.0)
               .build()
         );
      this.afy = this.aHh
         .add(((Builder)((Builder)new Builder().name("min-damage")).description("Damage toi thieu len muc tieu chinh.")).defaultValue(5.0).range(0.0, 36.0).sliderRange(0.0, 20.0).build());
      this.anA = this.aHh
         .add(
            ((Builder)((Builder)new Builder().name("face-place-health")).description("Duoi muc mau nay, cho phep damage toi thieu 1 de ket lieu."))
               .defaultValue(8.0)
               .range(0.0, 36.0)
               .sliderRange(0.0, 20.0)
               .build()
         );
      this.aph = this.aHh
         .add(((Builder)((Builder)new Builder().name("min-advantage")).description("Chenh le target-self toi thieu khi safety-mode la Strict.")).defaultValue(-2.0).range(-36.0, 36.0).sliderRange(-12.0, 12.0).build());
      this.bu = this.aHh
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("predict-movement"))
                     .description("Lay damage xau hon giua vi tri hien tai va tick chuyen dong ke tiep."))
                  .defaultValue(true))
               .build()
         );
      this.TC = this.awG
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("safety-mode"))
                     .description("Adaptive: damage-first, tu cho phep trade ngoai hole neu ban song; Strict: luon ep max-self/min-advantage; Off: bo hai gioi han do."))
                  .defaultValue(AliceObf_qe.aLY))
               .build()
         );
      this.Zk = this.awG
         .add(
            ((Builder)((Builder)new Builder().name("max-self-damage")).description("Tran Strict; voi Adaptive day la moc toi thieu truoc khi so damage trade."))
               .defaultValue(12.0)
               .range(0.0, 72.0)
               .sliderRange(0.0, 36.0)
               .build()
         );
      this.anW = this.awG
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("anti-suicide"))
                     .description("Khong no neu mau sau damage se cham safety-reserve."))
                  .defaultValue(true))
               .build()
         );
      this.aIZ = this.awG
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("safety-reserve")).description("Luong mau toi thieu phai con lai sau vu no."))
                  .defaultValue(1.0)
                  .range(0.0, 20.0)
                  .sliderRange(0.0, 10.0)
                  .visible(this.anW::get))
               .build()
         );
      this.bk = this.Qn
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("timing"))
                     .description("Vanilla: moi tick mot tuong tac theo nhip vanilla; Sequential: tiep tuc buoc ke tiep khi client da ap prediction. Grim: giu pipeline movement-safe hien tai. ZeroTick: dat+nap+no ngay tick do, nhanh nhat nhung experimental MultiPlace co the log."))
                  .defaultValue(AliceObf_lh.XL))
               .build()
         );
      this.aKW = this.MX
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("mode"))
                     .description("Sequential giu nhip mot cycle an toan. Burst gui cac lan place lien tuc theo burst-delay-ms, khong doi server ACK; co the flag Grim."))
                  .defaultValue(AliceObf_po.po))
               .build()
         );
      this.aNk = this.MX
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("burst-timing"))
                        .description("Timing rieng cho Burst: Vanilla giu mot place moi tick; Sequential cho phep noi tiep cac buoc ngay sau packet da gui. Chi nen dung Vanilla/Sequential trong Burst."))
                     .defaultValue(AliceObf_lh.aml))
                  .visible(this::bM))
               .build()
         );
      this.vl = this.MX
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("burst-delay-ms"))
                        .description("Khoang toi thieu giua cac dot place Burst; khong phai thoi gian cho ACK."))
                     .defaultValue(0))
                  .range(0, (1000))
                  .sliderRange((0), (500))
                  .visible(this::bM))
               .build()
         );
      this.Sq = this.MX
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("crystal-idle-spam"))
                     .description("Chi spam anchor khi CrystalAura++ dang bat nhung khong co plan dat/pha hop le. Van can muc tieu anchor."))
                  .defaultValue(false))
               .build()
         );
      this.aJp = this.MX
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("idle-spam-limit"))
                        .description("So cycle da gui toi da trong cua so truot, tinh ca place khong duoc server chap nhan."))
                     .defaultValue(3))
                  .range((1), 20)
                  .sliderRange(1, (10))
                  .visible(this.Sq::get))
               .build()
         );
      this.iU = this.MX
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("idle-spam-window-ms"))
                        .description("Cua so gioi han spam; pause hoac doi target khong reset so da gui."))
                     .defaultValue(1000))
                  .range(100, (10000))
                  .sliderRange((100), 5000)
                  .visible(this.Sq::get))
               .build()
         );
      this.vG = this.MX
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("idle-spam-delay-ms"))
                        .description("Khoang toi thieu giua hai cycle fallback; van ton trong burst-delay-ms."))
                     .defaultValue(150))
                  .range(0, (5000))
                  .sliderRange((0), (1000))
                  .visible(this.Sq::get))
               .build()
         );
      this.arM = this.Qn
         .add(
            ((Builder)((Builder)new Builder().name("aim-jitter")).description("Lech goc rat nho de khong lap chinh xac rotation delta cua DuplicateRotPlace."))
               .defaultValue(0.08)
               .range(0.0, 0.5)
               .sliderRange(0.0, 0.5)
               .build()
         );
      this.afu = this.Qn
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pause-on-use"))
                     .description("Tam dung khi dang an, block hoac dung item."))
                  .defaultValue(true))
               .build()
         );
      this.sM = this.Qn
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pause-on-mine"))
                     .description("Tam dung khi giu nut dao. Tat mac dinh de Speed Mine++ co the chay song song."))
                  .defaultValue(false))
               .build()
         );
      this.NI = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("render"))
                     .description("Hien thi anchor toi uu, damage va lich su vu no."))
                  .defaultValue(true))
               .build()
         );
      this.QH = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("shape-mode"))
                        .description("Kieu box ESP."))
                     .defaultValue(ShapeMode.Both))
                  .visible(this.NI::get))
               .build()
         );
      this.acX = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("anchor-side-color"))
                     .description("Mau nen anchor dang duoc toi uu."))
                  .defaultValue(new SettingColor((85), 210, 255, 45))
                  .visible(() -> (this.NI.get() && ((ShapeMode)this.QH.get()).sides())))
               .build()
         );
      this.alb = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("anchor-line-color"))
                     .description("Mau vien anchor dang duoc toi uu."))
                  .defaultValue(new SettingColor(100, (225), (255), (235)))
                  .visible(() -> (this.NI.get() && ((ShapeMode)this.QH.get()).lines())))
               .build()
         );
      this.aiv = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("ready-side-color"))
                     .description("Mau nen anchor da san sang kich no."))
                  .defaultValue(new SettingColor((255), 80, 105, (55)))
                  .visible(() -> (this.NI.get() && ((ShapeMode)this.QH.get()).sides())))
               .build()
         );
      this.Yq = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("ready-line-color"))
                     .description("Mau vien anchor da san sang kich no."))
                  .defaultValue(new SettingColor(255, 95, 120, (245)))
                  .visible(() -> (this.NI.get() && ((ShapeMode)this.QH.get()).lines())))
               .build()
         );
      this.ZK = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("target-line"))
                        .description("Noi anchor toi muc tieu chinh bang mot duong damage."))
                     .defaultValue(true))
                  .visible(this.NI::get))
               .build()
         );
      this.aom = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("damage-label"))
                        .description("Hien target damage va self damage tren anchor."))
                     .defaultValue(true))
                  .visible(this.NI::get))
               .build()
         );
      this.sF = this.pB
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("damage-scale")).description("Ty le nhan damage trong the gioi."))
                  .defaultValue(1.0)
                  .range(0.5, 3.0)
                  .sliderRange(0.5, 2.0)
                  .visible(() -> (boolean)(this.NI.get() && this.aom.get())))
               .build()
         );
      this.Nq = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("damage-color"))
                     .description("Mau nhan damage va target line."))
                  .defaultValue(new SettingColor((255), (255), 255, (255)))
                  .visible(() -> {
                     if (!(Boolean)this.NI.get()) {

                     } else {
                        short var1 = 13434;
                     }

                     return false;
                  }))
               .build()
         );
      this.yc = this.pB
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("fade-time"))
                        .description("So tick giu box vu no roi mo dan."))
                     .defaultValue(10))
                  .range((1), (40))
                  .sliderRange(1, (20))
                  .visible(this.NI::get))
               .build()
         );
      this.LF = new AliceObf_hu(this);
      this.kN = new ArrayList<>(1);
      this.uf = new ArrayList<>();
      this.aNI = new ArrayList<>();
      this.SH = new Vector3d();
      this.aHE = AliceObf_dv.gF;
      this.ayG = (-1);
      this.Wg = (-1);
      this.aS = (-1);
      this.aqM = (-1);
      this.apc = new AliceObf_mF();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.bp();
         AliceObf_fS.Od(this);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      AliceObf_fS.Oe(this);
      this.bp();
      super.onDeactivate();
   }

   private void bp() {
      this.bX();
      this.aao = null;
      this.aFC = null;
      this.LF.RL();
      this.kN.clear();
      this.uf.clear();
      this.aNI.clear();
      this.aHE = AliceObf_dv.gF;
      this.acm = null;
      this.Ge = false;
      this.ahh = (0);
      this.aHw = (0);
      this.wl = (0);
      this.azs = (0);
      this.JU = 0L;
      this.ayG = (-1);
      this.Wg = -1;
      this.dn = false;
      this.amo = false;
      this.FU = false;
      this.awN = 0.0F;
      this.avs = 0.0F;
      this.abQ = false;
      this.aQK = super.mc.world;
      this.atQ = super.mc.player;
      this.gM = false;
      this.ei = (0);
      this.aS = (-1);
      this.aqM = -1;
      this.apc.bl();
   }

   @AliceHandler(
      priority = 100
   )
   private void bq(Pre var1) {
      if (this.aQK != super.mc.world || this.atQ != super.mc.player) {
         this.bp();
      }

      this.wl += (1);
      this.ck();
      this.cl();
      if (!this.bv() || !this.bP()) {
         this.bY(true);
      } else if (this.LF.RP()) {
         if (!(this.acm != null)) {
            char var2 = 44832;
         }

         if (this.acm == null) {
            this.bw();
         }

         if (this.acm != null) {
            if (!this.bZ()) {
               this.kN.clear();
               this.aHE = AliceObf_dv.gF;
            } else {
               this.bG();
            }
         }
      }
   }

   @AliceHandler(
      priority = 90
   )
   private void br(PlayerTickMovementEvent var1) {
      if (this.bv() && this.bP()) {
         this.LF.RQ();
         this.bW();
      } else {
         this.bY(true);
      }
   }

   @AliceHandler(
      priority = 50
   )
   private void bs(meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Pre var1) {
      if (this.bv() && this.bP()) {
         this.LF.RR();
      } else {
         this.bY(true);
      }
   }

   @AliceHandler(
      priority = -50
   )
   private void bt(Post var1) {
      this.LF.RS();
      this.bX();
   }

   @AliceHandler(
      priority = -1000
   )
   private void bu(Sent var1) {
      this.LF.RT(var1);
      if (var1.packet instanceof PlayerMoveC2SPacket var2 && var2.changesLook()) {
         this.awN = var2.getYaw(this.awN);
         this.avs = var2.getPitch(this.avs);
         this.abQ = true;
         if (this.ih != null && this.ih.Lg) {
            AliceObf_fS.Oz(this, this.awN, this.avs);
         }
      }
   }

   private boolean bv() {
      if (!(super.mc.player != null)) {
         short var4 = 1602;
      }

      return false;
   }

   private void bw() {
      List var1 = this.bx();
      if (!var1.isEmpty()) {
         AliceObf_il var2 = this.by(var1);
         if (var2 != null) {
            this.acm = var2;
            this.gM = false;
            this.ei = var2.H;
            this.Ge = var2.bK;
            this.ahh = this.wl;
            this.aHw = (0);
            this.aFC = null;
         }
      }
   }

   private List<PlayerEntity> bx() {
      double var1 = (Double)this.acI.get() * (Double)this.acI.get();
      ArrayList var3 = new ArrayList();

      for (PlayerEntity var5 : super.mc.world.getPlayers()) {
         if (var5 != super.mc.player && var5.isAlive()) {
            if (!var5.isCreative()) {

            } else {
               short var6 = 11873;
            }
         }
      }

      var3.sort(Comparator.<PlayerEntity>comparingDouble(var1x -> super.mc.player.squaredDistanceTo(var1x)).thenComparingInt(Entity::getId));
      return var3.size() > (4) ? new ArrayList<>(var3.subList(0, 4)) : var3;
   }

   private AliceObf_il by(List<PlayerEntity> var1) {
      int var2 = MathHelper.ceil((Double)this.acF.get()) + (1);
      Vec3d var3 = super.mc.player.getEyePos();
      double var4 = (Double)this.acF.get() * (Double)this.acF.get();
      ArrayList<AliceObf_il> var6 = new ArrayList<>();
      Iterator var7 = BlockPos.iterateOutwards(super.mc.player.getBlockPos(), var2, var2, var2).iterator();
      if (var7.hasNext()) {

      } else {
         short var19 = 22205;
      }

      var6.sort(Comparator.comparingDouble(AliceObf_il::aB).reversed().thenComparing(var0 -> var0.aCb.asLong()));
      AliceObf_il var18 = null;

      for (AliceObf_il var9 : var6) {
         int var10001 = !this.bD(var9.aDH) ? 11391 : 11392;

         while (true) {
            int var17 = var10001;
            if (var17 == 11391) {
               break;
            }

            if (var17 != 11392) {
               if (var18 == null || this.bA(var9, var18)) {
                  var18 = var9;
               }
               break;
            }

         }
      }

      return var18;
   }

   private boolean bz(BlockPos var1) {
      if (var1.getY() >= super.mc.world.getBottomY()
         && var1.getY() < super.mc.world.getBottomY() + super.mc.world.getHeight()) {
         if (super.mc.world.getBlockState(var1).isReplaceable()) {
            Direction[] var2 = Direction.values();
            int var3 = var2.length;

            for (int var4 = (0); var4 < var3; var4++) {
               Direction var5 = var2[var4];
               BlockState var6 = super.mc.world.getBlockState(var1.offset(var5));
               if (!var6.isAir() && !var6.isReplaceable() && !BlockUtils.isClickable(var6.getBlock())) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean bA(AliceObf_il var1, AliceObf_il var2) {
      double var3 = var1.hO - var2.hO;
      if (Math.abs(var3) > 0.01) {

      } else {
         char var5 = 58675;
      }

      if (var1.aDH.adv != var2.aDH.adv) {
         return (var1.aDH.adv < var2.aDH.adv);
      } else {
         if (var1.aCb.asLong() < var2.aCb.asLong()) {

         } else {
            short var6 = 843;
         }

         return false;
      }
   }

   private AliceObf_pk bB(Vec3d var1, List<PlayerEntity> var2) {
      BlockPos var3 = BlockPos.ofFloored(var1);
      RaycastFactory var4 = DamageUtils.HIT_FACTORY;
      RaycastFactory var5 = (var2x, var3x) -> var3x.equals(var3) ? null : (BlockHitResult)var4.apply(var2x, var3x);
      float var6 = this.bC(super.mc.player, var1, var5, false);
      if (!Float.isFinite(var6)) {
         return null;
      } else {
         PlayerEntity var7 = null;
         float var8 = 0.0F;
         double var9 = 0.0;
         int var11 = (0);

         for (PlayerEntity var13 : var2) {
            float var14 = this.bC(var13, var1, var5, true);
            if (Float.isFinite(var14)) {
               int var15 = var14 >= AnchorAuraPlusPlus.cv(var13)
                  ? (1)
                  : 0;
               double var16 = AnchorAuraPlusPlus.cv(var13)
                     <= this.anA.get()
                  ? Math.min((Double)this.afy.get(), 1.0)
                  : (Double)this.afy.get();
               if (!(var14 < var16) && this.bE(var14, var6)) {
                  var9 += Math.min(
                     (double)var14, AnchorAuraPlusPlus.cv(var13)
                  );
                  if (var7 != null && (var15 == 0 || var11 != 0)) {
                     if (!(var15 == var11)) {
                        short var19 = 6998;
                     }
                  } else {
                     var7 = var13;
                     var8 = var14;
                     var11 = var15;
                  }
               }
            }
         }

         if (var7 == null) {
            return null;
         } else {
            double var18 = var8 * 12.0 + var9 * 2.0 - var6 * 3.0;
            if (var11 != 0) {
               var18 += 1000.0;
            }

            return new AliceObf_pk(var7, var8, var6, var18, (var11 != 0));
         }
      }
   }

   private float bC(PlayerEntity var1, Vec3d var2, RaycastFactory var3, boolean var4) {
      float var5 = DamageUtils.anchorDamage(var1, var1.getEntityPos(), var1.getBoundingBox(), var2, var3);
      if (!(Boolean)this.bu.get()) {
         return var5;
      } else {
         Vec3d var6 = var1.getVelocity();
         float var7 = DamageUtils.anchorDamage(var1, var1.getEntityPos().add(var6), var1.getBoundingBox().offset(var6), var2, var3);
         return var4 ? Math.min(var5, var7) : Math.max(var5, var7);
      }
   }

   private boolean bD(AliceObf_pk var1) {
      if (var1 != null && Float.isFinite(var1.alH)) {
         if (Float.isFinite(var1.adv)) {

         } else {
            char var2 = 64449;
         }
      }

      return false;
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      private boolean bE(float var1, float var2) {
      return switch (((AliceObf_qe)this.TC.get()).ordinal()) {
         case 0 -> true;
         case 1 -> var1 - var2 >= (Double)this.aph.get();
         case 2 -> false;
         default -> throw new MatchException(null, null);
      };
   }

   private boolean bF() {
      AliceObf_il var1 = this.acm;
      if (var1 != null && var1.aDH.aze != null) {
         PlayerEntity var2 = var1.aDH.aze;
         if (!var2.isAlive()
            || var2.isCreative()
            || var2.isSpectator()
            || !Friends.get().shouldAttack(var2)
            || super.mc.player.squaredDistanceTo(var2) > (Double)this.acI.get() * (Double)this.acI.get()) {
            return false;
         } else if (AnchorAuraPlusPlus.cu(
               super.mc.player.getEyePos(), new Box(var1.aCb)
            )
            > (Double)this.acF.get() * (Double)this.acF.get()) {
            return false;
         } else {
            BlockState var3 = super.mc.world.getBlockState(var1.aCb);
            if (!var3.isOf(Blocks.RESPAWN_ANCHOR)) {

            } else {
               short var4 = 13783;
            }

            if (this.Ge) {
               if (!(this.aHw > 0)) {
                  char var5 = 54111;
               }

               return false;
            } else if (!var3.isReplaceable()) {
               return false;
            } else {
               return !(this.wl - this.ahh > (60));
            }
         }
      } else {
         return false;
      }
   }

   private void bG() {
      this.kN.clear();
      this.aHE = AliceObf_dv.gF;
      AliceObf_il var1 = this.acm;
      if (var1 != null) {
         if (!this.Ge) {
            this.aHE = AliceObf_dv.Dp;
            this.kN.add(var1.aCb);
         }
      }
   }

   public AliceObf_y prepareCombatIntent() {
      if (this.bv() && this.bP()) {
         AliceObf_il var1 = this.acm;
         if (var1 == null) {
            return null;
         } else {
            if (!this.kN.isEmpty()) {

            } else {
               short var8 = 12532;
            }

            BlockState var2 = super.mc.world.getBlockState(var1.aCb);
            if (!var2.isOf(Blocks.RESPAWN_ANCHOR)) {

            } else {
               short var9 = 6515;
            }

            int var3 = var2.isOf(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.get(RespawnAnchorBlock.CHARGES) : this.ei;
            AliceObf_pk var4 = this.bB(
               var1.aCb.toCenterPos(), List.of(var1.aDH.aze)
            );
            if (var4 != null && this.bD(var4)) {
               this.acm = new AliceObf_il(var1.aCb, var4, var3 > 0, (int)var4.ahe, 5.0);
               BlockHitResult var5 = this.bS(var1.aCb);
               if (var5 != null) {
                  char var10 = 51240;
               } else {
                  char var11 = 51241;
               }

               return null;
            } else {
               this.bR();
               return null;
            }
         }
      } else {
         return null;
      }
   }

   private void bH(BlockHitResult var1, boolean var2) {
      AliceObf_il var3 = this.acm;
      if (var3 != null && this.bv() && this.bP()) {
         if (!var3.aCb.equals(var1.getBlockPos())) {

         } else {
            short var19 = 24804;
         }

         if (this.bN() != AliceObf_lh.tC || this.aS != this.wl) {
            BlockState var4 = super.mc.world.getBlockState(var3.aCb);
            boolean var5 = var4.isOf(Blocks.RESPAWN_ANCHOR);
            if (var5 || var2 && this.Ge && var4.isReplaceable() || this.bO(var4)) {
               AliceObf_pk var6 = this.bB(
                  var3.aCb.toCenterPos(), List.of(var3.aDH.aze)
               );
               if (var6 != null && this.bD(var6)) {
                  if (!var2) {
                     if (!AliceObf_fS.OE(this)) {
                        return;
                     }

                     if (!this.bV(var1.getBlockPos())) {
                        AliceObf_lB var18 = this.bT(var1);
                        if (var18 == null) {
                           return;
                        }

                        this.bU(var18);
                        return;
                     }
                  }

                  int var7 = var5 ? (Integer)var4.get(RespawnAnchorBlock.CHARGES) : this.ei;
                  int var10001 = !this.bM() ? 516 : 515;

                  while (true) {
                     int var15 = var10001;
                     if (var15 == 515) {
                        var7 = Math.max(var7, this.ei);
                        break;
                     }

                     if (var15 != 516) {
                        break;
                     }

                  }

                  int var8 = this.cb();
                  if (var8 >= 0) {
                     int var9 = var7;
                     int var10 = var2 ? 1 : 0;

                     try {
                        ItemStack var11 = super.mc.player.getOffHandStack();
                        if (var11.isOf(Items.GLOWSTONE) && var9 < (4)) {
                           int var12 = Math.min((4) - var9, var11.getCount());

                           for (int var13 = 0; var13 < var12; var13++) {
                              if (var10 != 0
                                 && !AliceObf_fS.Ph(this)) {
                                 return;
                              }

                              if (!this.bJ(Hand.OFF_HAND, var1)) {
                                 return;
                              }

                              var10 = 1;
                              this.ei = ++var9;
                              if (this.bN() == AliceObf_lh.tC) {
                                 return;
                              }
                           }
                        } else if (var9 == 0) {
                           if (this.Wg < 0 || !this.bI(this.Wg)) {
                              return;
                           }

                           if (var10 != 0 && !AliceObf_fS.Ph(this)) {
                              return;
                           }

                           if (!this.bJ(Hand.MAIN_HAND, var1)) {
                              return;
                           }

                           var10 = (1);
                           var9 = 1;
                           this.ei = var9;
                           if (this.bN() == AliceObf_lh.tC) {
                              return;
                           }
                        }

                        if (var8 >= 0 && this.bI(var8)) {
                           if (var9 > 0) {
                              if (var10 != 0
                                 && !AliceObf_fS.Ph(this)) {
                                 return;
                              }

                              if (!this.bJ(Hand.MAIN_HAND, var1)) {
                                 return;
                              }
                           }

                           this.bK(var6);
                           this.bL();
                        }
                     } finally {
                        AliceObf_fS.Ov(this);
                     }
                  }
               } else {
                  this.bR();
               }
            }
         }
      }
   }

      private boolean bI(int var1) {
      boolean var2 = var1 >= 0 && var1 <= 8 && AliceObf_fS.Of(this, var1);
      if (var2) {
         this.aqM = var1;
      }
      return var2;
   }

   private boolean bJ(Hand var1, BlockHitResult var2) {
      int var3 = AliceObf_fS.PD();
      int var4 = super.mc.player.getInventory().getSelectedSlot();
      ItemStack var5 = super.mc.player.getInventory().getStack(var4).copy();
      int var6 = var1 == Hand.MAIN_HAND && this.aqM >= 0 && this.aqM != var4 ? (1) : 0;
      int var10000 = var6 != 0 ? 16672 : 16673;

      while (true) {
         int var8 = var10000;
         if (var8 == 16672) {
            super.mc.player.getInventory().setStack(var4, super.mc.player.getInventory().getStack(this.aqM).copy());
            break;
         }

         if (var8 != 16673) {
            break;
         }

      }

      try {
         super.mc.interactionManager.interactBlock(super.mc.player, var1, var2);
      } finally {
         if (var6 != 0) {
            if (AliceObf_fS.PD() > var3) {
               super.mc.player.getInventory().setStack(this.aqM, super.mc.player.getInventory().getStack(var4).copy());
            }

            super.mc.player.getInventory().setStack(var4, var5);
         }
      }

      if (AliceObf_fS.PD() != var3) {
         this.aS = this.wl;
         this.bQ();
         super.mc.player.swingHand(var1);
         return true;
      } else {
         return false;
      }
   }

   private void bK(AliceObf_pk var1) {
      AliceObf_il var2 = this.acm;
      if (var2 != null) {
         this.aNI.removeIf(var1x -> var1x.afh.equals(var2.aCb));
         this.aNI.add(new AliceObf_c(var2.aCb, this.wl));
         this.uf
            .add(
               new AliceObf_li(
                  new AliceObf_my(
                     var2.aCb,
                     var1.aze.getEntityPos(),
                     AnchorAuraPlusPlus.cn(var1),
                     AliceObf_lg.fJ
                  ),
                  this.wl
               )
            );

         while (this.uf.size() > 24) {
            this.uf.removeFirst();
         }
      }
   }

   private void bL() {
      this.azs = this.bM() ? this.wl : this.wl + 2;
      this.bR();
   }

      private boolean bM() {
      return this.aKW.get() == AliceObf_po.aPP || (Boolean)this.Sq.get();
   }

   private AliceObf_lh bN() {
      return !this.bM() ? (AliceObf_lh)this.bk.get() : (AliceObf_lh)this.aNk.get();
   }

   private boolean bO(BlockState var1) {
      return (this.bM() && this.Ge && this.aHw > 0 && this.wl - this.aHw <= (12) && var1.isReplaceable());
   }

   private boolean bP() {
      if (!this.gM && this.bM() && !this.apc.bi(System.nanoTime() / (1000000), (Integer)this.vl.get())) {
         return false;
      } else if (!(Boolean)this.Sq.get()) {
         return true;
      } else {
         CrystalAuraPlusPlus var1 = (CrystalAuraPlusPlus)Modules.get().get(CrystalAuraPlusPlus.class);
         if (var1 != null) {
            if (!var1.isIdleForAnchorFallback()) {

            } else {
               char var2 = 32848;
            }

            return !(!this.gM
                  && !this.apc
                     .bj(
                        System.nanoTime() / (1000000),
                        (Integer)this.aJp.get(),
                        (Integer)this.iU.get(),
                        (Integer)this.vG.get()
                     ));
         } else {
            return false;
         }
      }
   }

   private void bQ() {
      if (!this.gM) {
         this.gM = true;
         this.apc.bk(System.nanoTime() / (1000000));
      }
   }

   private void bR() {
      this.kN.clear();
      this.aHE = AliceObf_dv.gF;
      this.acm = null;
      this.gM = false;
      this.ei = 0;
      this.Ge = false;
      this.ahh = (0);
      this.aHw = (0);
      this.aFC = null;
      this.aao = null;
   }

   private BlockHitResult bS(BlockPos var1) {
      Direction var2 = AnchorAuraPlusPlus.cp(
         var1, super.mc.player.getEyePos()
      );
      return var2 == null
         ? null
         : new BlockHitResult(
            AnchorAuraPlusPlus.cr(var1, var2), var2, var1, false
         );
   }

   private AliceObf_lB bT(BlockHitResult var1) {
      Vec3d var2 = super.mc.player.getEyePos();
      AliceObf_lB var3 = this.aFC;
      if (var3 != null
         && var3.aDJ.equals(var1.getBlockPos())
         && var3.aJK == var1.getSide()
         && AnchorAuraPlusPlus.cq(
            var1.getBlockPos(), var1.getSide(), var2
         )
         && AnchorAuraPlusPlus.ct(
            var2, var3.iw, var3.amJ, (Double)this.acF.get(), new Box(var1.getBlockPos())
         )) {
         return var3;
      } else {
         double[] var4 = AnchorAuraPlusPlus.cs(
            var1.getBlockPos().toCenterPos()
         );
         double var5 = (Double)this.arM.get();
         long var7 = var1.getBlockPos().asLong()
            ^ (this.JU += 1L) * (2135587861);
         double var9 = ((var7 & (1)) == (0) ? -1.0 : 1.0) * var5;
         double var11 = ((var7 & (2)) != (0) ? 0.5 : -0.5) * var5;
         AliceObf_lB var13 = new AliceObf_lB(
            var1.getBlockPos().toImmutable(),
            var1.getSide(),
            (float)MathHelper.wrapDegrees(var4[0] + var9),
            (float)MathHelper.clamp(var4[1] + var11, -90.0, 90.0)
         );
         if (!AnchorAuraPlusPlus.ct(
            var2, var13.iw, var13.amJ, (Double)this.acF.get(), new Box(var1.getBlockPos())
         )) {
            var13 = new AliceObf_lB(
               var1.getBlockPos().toImmutable(),
               var1.getSide(),
               (float)MathHelper.wrapDegrees(var4[0]),
               (float)MathHelper.clamp(var4[(1)], -90.0, 90.0)
            );
         }

         this.aFC = var13;
         return var13;
      }
   }

   private void bU(AliceObf_lB var1) {
      this.aao = new AliceObf_hQ(var1.iw, var1.amJ);
   }

   private boolean bV(BlockPos var1) {
      if (!this.abQ && !AliceObf_fS.OK()) {
         return false;
      } else {
         float var2 = !this.abQ ? AliceObf_fS.OI() : this.awN;
         float var3 = !this.abQ ? AliceObf_fS.OJ() : this.avs;
         return AnchorAuraPlusPlus.ct(
            super.mc.player.getEyePos(), var2, var3, (Double)this.acF.get(), new Box(var1)
         );
      }
   }

   private void bW() {
      AliceObf_hQ var1 = this.aao;
      this.aao = null;
      ClientPlayerEntity var2 = super.mc.player;
      if (!(var1 != null)) {
         short var3 = 5436;
      }
   }

   private void bX() {
      AliceObf_aC var1 = this.ih;
      if (!(var1 == null)) {
         short var2 = 17063;
      }

      var1.aG();
      AliceObf_au.Fy(super.mc.player, var1);
      this.ih = null;
   }

   private void bY(boolean var1) {
      this.bX();
      this.aao = null;
      this.aFC = null;
      this.LF.RW();
      this.kN.clear();
      this.aHE = AliceObf_dv.gF;
      if (var1) {
         this.bR();
      }
   }

   private boolean bZ() {
      AliceObf_il var1 = this.acm;
      if (var1 == null) {
         return false;
      } else {
         BlockState var2 = super.mc.world.getBlockState(var1.aCb);
         int var3 = !this.Ge ? (1) : (0);
         int var8;
         if (this.Ge) {
            if (var2.isOf(Blocks.RESPAWN_ANCHOR)) {
               if ((Integer)var2.get(RespawnAnchorBlock.CHARGES) == 0) {

               } else {
                  short var7 = 21839;
               }
            }

            var8 = (0);
         } else {
            var8 = (1);
         }

         int var4 = var8;
         boolean var5 = super.mc.player.getOffHandStack().isOf(Items.GLOWSTONE);
         int var6 = var4 != 0 && !var5 ? 1 : 0;
         this.ayG = this.ca(Items.RESPAWN_ANCHOR);
         this.Wg = this.ca(Items.GLOWSTONE);
         if (var3 != 0 && this.ayG < 0) {
            this.cd(Items.RESPAWN_ANCHOR);
            return false;
         } else if (var6 != 0 && this.Wg < 0) {
            this.cd(Items.GLOWSTONE);
            return false;
         } else {
            this.dn = false;
            this.amo = false;
            return true;
         }
      }
   }

   private int ca(Item var1) {
      if (super.mc.player == null) {
         return -1;
      } else {
         for (int var2 = 0; var2 < 9; var2++) {
            if (super.mc.player.getInventory().getStack(var2).isOf(var1)) {
               return var2;
            }
         }

         return (-1);
      }
   }

   private int cb() {
      if (super.mc.player == null) {
         return (-1);
      } else {
         int var1 = super.mc.player.getInventory().getSelectedSlot();
         int var10000 = !this.cc(super.mc.player.getInventory().getStack(var1)) ? 661 : 660;

         while (true) {
            int var3 = var10000;
            if (var3 == 660) {
               this.FU = false;
               return var1;
            }

            if (var3 != 661) {
               for (int var2 = 0; var2 < 9; var2++) {
                  if (this.cc(super.mc.player.getInventory().getStack(var2))) {
                     this.FU = false;
                     return var2;
                  }
               }

               if (!this.FU) {
                  this.FU = true;
                  this.warning(
                     "Can mot hotbar item khong phai Glowstone de kich no anchor.",
                     new Object[(0)]
                  );
               }

               return -1;
            }

         }
      }
   }

   private boolean cc(ItemStack var1) {
      return (!var1.isOf(Items.GLOWSTONE));
   }

   private void cd(Item var1) {
      if (var1 == Items.RESPAWN_ANCHOR) {
         if (this.dn) {
            return;
         }

         this.dn = true;
         this.error("Khong co Respawn Anchor tren hotbar.", new Object[0]);
      } else {
         if (this.amo) {
            return;
         }

         this.amo = true;
         this.error(
            "Khong co Glowstone tren hotbar hoac tay phu.",
            new Object[(0)]
         );
      }
   }

   public boolean canYieldEpoch() {
      if (!(this.wl >= this.azs)) {
         short var1 = 5093;
      }

      return false;
   }

   public boolean reservesLowerPriorityEpochs() {
      return false;
   }

   public void yieldToHigherPriority() {
      this.bY(false);
   }

   public int combatPriority() {
      return 140;
   }

   public void suspendForItemUse() {
      this.bY(false);
   }

   public AliceObf_jQ participant() {
      return this;
   }

   public List<BlockPos> placementPlan() {
      return this.kN;
   }

   public List<Block> allowedBlocks() {
      return List.of(Blocks.RESPAWN_ANCHOR);
   }

   public boolean keepCamera() {
      return false;
   }

   public boolean safeTiming() {
      return !(this.bN() != AliceObf_lh.XL
            && this.bN() != AliceObf_lh.tC);
   }

   public boolean strictPlaceCheck() {
      return true;
   }

   public int blocksPerTick() {
      return (1);
   }

   public int placeDelayTicks() {
      return 0;
   }

   public long placeDelayMs() {
      return this.bM() ? ((Integer)this.vl.get()).longValue() : 0L;
   }

   public void onPlaced(BlockPos var1) {
      AliceObf_il var2 = this.acm;
      if (var2 != null) {
         if (this.aHE == AliceObf_dv.Dp && var2.aCb.equals(var1)) {
            this.bQ();
            this.aS = this.wl;
            this.Ge = true;
            this.aHw = this.wl;
            this.kN.clear();
            this.aHE = AliceObf_dv.gF;
            if (this.bN() == AliceObf_lh.rX
               || this.bN() == AliceObf_lh.aml) {
               BlockHitResult var3 = this.bS(var1);
               if (!(var3 != null)) {
                  char var4 = 56899;
               }
            }
         }
      }
   }

   @AliceHandler
   private void ce(Render3DEvent var1) {
      if ((Boolean)this.NI.get() && super.mc.player != null && super.mc.world != null) {
         for (AliceObf_li var3 : this.uf) {
            double var4 = this.cj(var3, var1.tickDelta);
            if (var4 > 0.0) {
               this.ch(var1, var3.CD, var4);
            }
         }

         AliceObf_my var6 = this.cg();
         if (var6 != null) {
            this.ch(var1, var6, 1.0);
         }
      }
   }

   @AliceHandler
   private void cf(Render2DEvent var1) {
      if ((Boolean)this.NI.get()) {
         if ((Boolean)this.aom.get()) {

         } else {
            char var7 = 61749;
         }
      }
   }

   private AliceObf_my cg() {
      AliceObf_il var1 = this.acm;
      if (var1 == null) {
         return null;
      } else {
         AliceObf_lg var2 = super.mc
               .world
               .getBlockState(var1.aCb)
               .isOf(Blocks.RESPAWN_ANCHOR)
            ? AliceObf_lg.aeZ
            : AliceObf_lg.aBb;
         return new AliceObf_my(
            var1.aCb,
            var1.aDH.aze.getEntityPos(),
            AnchorAuraPlusPlus.cn(var1.aDH),
            var2
         );
      }
   }

   private void ch(Render3DEvent var1, AliceObf_my var2, double var3) {
      SettingColor var5 = var2.aLz != AliceObf_lg.aeZ
            && var2.aLz != AliceObf_lg.fJ
         ? (SettingColor)this.acX.get()
         : (SettingColor)this.aiv.get();
      SettingColor var6 = var2.aLz != AliceObf_lg.aeZ
            && var2.aLz != AliceObf_lg.fJ
         ? (SettingColor)this.alb.get()
         : (SettingColor)this.Yq.get();
      var1.renderer
         .box(
            new Box(var2.IP),
            AnchorAuraPlusPlus.co(var5, var3),
            AnchorAuraPlusPlus.co(var6, var3),
            (ShapeMode)this.QH.get(),
            (0)
         );
      if ((Boolean)this.ZK.get() && var2.aeG != null) {
         Vec3d var7 = var2.IP.toCenterPos();
         var1.renderer
            .line(
               var7.x,
               var7.y,
               var7.z,
               var2.aeG.x,
               var2.aeG.y + 0.9,
               var2.aeG.z,
               AnchorAuraPlusPlus.co((Color)this.Nq.get(), var3 * 0.8)
            );
      }
   }

   private void ci(AliceObf_my var1, double var2) {
      Vec3d var4 = var1.IP.toCenterPos().add(0.0, 0.85, 0.0);
      this.SH.set(var4.x, var4.y, var4.z);
      if (NametagUtils.to2D(this.SH, (Double)this.sF.get())) {
         TextRenderer var5 = TextRenderer.get();
         if (var5 != null) {
            NametagUtils.begin(this.SH);
            var5.begin(1.0, true, true);
            double var6 = var5.getWidth(var1.HG, true);
            double var8 = var5.getHeight(true);
            var5.render(
               var1.HG,
               -var6 / 2.0,
               -var8 / 2.0,
               AnchorAuraPlusPlus.co((Color)this.Nq.get(), var2),
               true
            );
            var5.end();
            NametagUtils.end();
         }
      }
   }

   private double cj(AliceObf_li var1, float var2) {
      double var3 = Math.max(0.0, (double)(this.wl - var1.auR + var2));
      return MathHelper.clamp(1.0 - var3 / ((Integer)this.yc.get()).intValue(), 0.0, 1.0);
   }

   private void ck() {
      this.uf.removeIf(var1 -> !(this.wl - var1.auR <= this.yc.get()));
   }

   private void cl() {
      if (super.mc.world == null) {
         this.aNI.clear();
      } else {
         this.aNI.removeIf(var1 -> !(super.mc.world.getBlockState(var1.afh).isOf(Blocks.RESPAWN_ANCHOR) && this.wl - var1.atj <= 12));
      }
   }

   private boolean cm(BlockPos var1) {
      for (AliceObf_c var3 : this.aNI) {
         if (var3.afh.equals(var1)) {
            return !(this.bM() && this.wl > var3.atj);
         }
      }

      return false;
   }

   private static String cn(AliceObf_pk var0) {
      Locale var10000 = Locale.ROOT;
      String var10001 = "%s %.1f | Self %.1f";
      Object[] var10002 = new Object[]{var0.aze.getName().getString(), null, null};
      var10002[(1)] = var0.alH;
      var10002[(2)] = var0.adv;
      return String.format(var10000, var10001, var10002);
   }

   private static Color co(Color var0, double var1) {
      return !(var1 >= 0.999)
         ? new Color(
            var0.r, var0.g, var0.b, MathHelper.clamp((int)Math.round(var0.a * var1), (0), (255))
         )
         : var0;
   }

   private static Direction cp(BlockPos var0, Vec3d var1) {
      Direction var2 = null;
      double var3 = Double.NEGATIVE_INFINITY;

      for (Direction var8 : Direction.values()) {
         if (AnchorAuraPlusPlus.cq(var0, var8, var1)) {
            int var11 = AliceObf_tL.axG[var8.ordinal()] ^ 930258552;
            double var10000;
            if (var11 != 930258553) {
               if (var11 != 930258554) {
                  if (var11 == 930258555) {
                     var10000 = var0.getZ() - var1.z;
                  } else if (var11 != 930258556) {
                     if (var11 == 930258557) {
                        var10000 = var0.getX() - var1.x;
                     } else {
                        if (var11 != 930258558) {
                           throw new MatchException(null, null);
                        }

                        var10000 = var1.x - (var0.getX() + 1.0);
                     }
                  } else {
                     var10000 = var1.z - (var0.getZ() + 1.0);
                  }
               } else {
                  var10000 = var1.y - (var0.getY() + 1.0);
               }
            } else {
               var10000 = var0.getY() - var1.y;
            }

            double var9 = var10000;
            if (var9 > var3) {
               var3 = var9;
               var2 = var8;
            }
         }
      }

      return var2;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static boolean cq(BlockPos var0, Direction var1, Vec3d var2) {
      int var10000;
      switch (AliceObf_tL.axG[var1.ordinal()]) {
         case 1:
            var10000 = var2.y <= var0.getY() + 0.03 ? (1) : 0;
            break;
         case 2:
            var10000 = var2.y >= var0.getY() + 1.0 - 0.03 ? 1 : (0);
            break;
         case 3:
            var10000 = var2.z <= var0.getZ() + 0.03 ? 1 : (0);
            break;
         case 4:
            var10000 = !(var2.z >= var0.getZ() + 1.0 - 0.03) ? 0 : 1;
            break;
         case 5:
            var10000 = var2.x <= var0.getX() + 0.03 ? 21074 : 21075;
            byte var10001 = 0;
            break;
         case 6:
            var10000 = var2.x >= var0.getX() + 1.0 - 0.03 ? 1 : (0);
            break;
         default:
            throw new MatchException(null, null);
      }

      return (var10000 != 0);
   }

   private static Vec3d cr(BlockPos var0, Direction var1) {
      return new Vec3d(
         var0.getX() + 0.5 + var1.getOffsetX() * 0.5,
         var0.getY() + 0.5 + var1.getOffsetY() * 0.5,
         var0.getZ() + 0.5 + var1.getOffsetZ() * 0.5
      );
   }

   private static double[] cs(Vec3d var0) {
      Vec3d var1 = MinecraftClient.getInstance().player.getEyePos();
      double var2 = var0.x - var1.x;
      double var4 = var0.y - var1.y;
      double var6 = var0.z - var1.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      return new double[]{Math.toDegrees(Math.atan2(var6, var2)) - 90.0, -Math.toDegrees(Math.atan2(var4, var8))};
   }

   private static boolean ct(Vec3d var0, float var1, float var2, double var3, Box var5) {
      Vec3d var6 = var0.add(Vec3d.fromPolar(var2, var1).multiply(var3));
      return !(!var5.contains(var0) && !var5.raycast(var0, var6).isPresent());
   }

   private static double cu(Vec3d var0, Box var1) {
      double var2 = Math.max(var1.minX - var0.x, Math.max(0.0, var0.x - var1.maxX));
      double var4 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var6 = Math.max(var1.minZ - var0.z, Math.max(0.0, var0.z - var1.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   private static double cv(PlayerEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         AliceObf_il var1 = this.acm;
         if (var1 == null) {
            return null;
         } else {
            Locale var10000 = Locale.ROOT;
            String var10001 = "%s %.1f";
            Object[] var10002 = new Object[2];
            var10002[(0)] = var1.aDH.aze.getName().getString();
            var10002[(1)] = var1.aDH.alH;
            return String.format(var10000, var10001, var10002);
         }
      }
   }

   private static Vec2f cw(PlayerInput var0) {
      float var1 = (var0.left() ? 1.0F : 0.0F) - (var0.right() ? 1.0F : 0.0F);
      if (var0.forward()) {
         float var10000 = 1.0F;
      } else {
         float var3 = 0.0F;
      }

      float var2 = (var0.backward() ? 18594 : 18595) - 0.0F;
      return new Vec2f(var1, var2).normalize();
   }

   }

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
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import meteordevelopment.meteorclient.events.entity.EntityAddedEvent;
import meteordevelopment.meteorclient.events.entity.EntityRemovedEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.entity.DamageUtils;
import meteordevelopment.meteorclient.utils.entity.DamageUtils.RaycastFactory;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.WireframeEntityRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.network.packet.c2s.play.ClientTickEndC2SPacket;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.Vector3d;

public final class CrystalAuraPlusPlus extends AliceModule implements AliceObf_jQ {
   private static final int aKk = 1;
   private static final int rY = 10;
   private static final double awo = 144.0;
   private static final int[] aMT = new int[0];
   private static final long avz = 100L;
   private static final double Eo = 0.0;
   private static final double Ti = 0.03;
   private static final double cI = 2.0;
   private static final double Zw = 2.0;
   private static final int fw = 20;
   private static final int EK = 40;
   private static final int acs = 10;
   private static final double[] hF = new double[]{0.0, -0.3, 0.3, 0.0, 0.0, -0.3, -0.3, 0.3, 0.3};
   private static final double[] Nd = new double[]{0.0, 0.0, 0.0, -0.3, 0.3, -0.3, 0.3, -0.3, 0.3};
   private static final MethodHandle aEU = CrystalAuraPlusPlus.Bk();
   private final SettingGroup VO = this.settings.getDefaultGroup();
   private final SettingGroup Ks;
   private final SettingGroup oB;
   private final SettingGroup LH;
   private final SettingGroup bz;
   private final SettingGroup x;
   private final SettingGroup ml;
   private final SettingGroup EZ;
   private final Setting<Boolean> ZW;
   private final Setting<Integer> ayx;
   private final Setting<Integer> aeK;
   private final Setting<Double> aHF;
   private final Setting<Integer> Iu;
   private final Setting<Double> pq;
   private final Setting<Double> ajY;
   private final Setting<Double> aQq;
   private final Setting<Double> Qq;
   private final Setting<Boolean> aji;
   private final Setting<Double> atE;
   private final Setting<Boolean> aQr;
   private final Setting<AliceObf_aX> KJ;
   private final Setting<AliceObf_id> aKA;
   private final Setting<Boolean> QW;
   private final Setting<AliceObf_id> aei;
   private final Setting<Integer> zf;
   private final Setting<Integer> iV;
   private final Setting<AliceObf_ko> vm;
   private final Setting<AliceObf_jY> alB;
   private final Setting<Boolean> ayK;
   private final Setting<AliceObf_lJ> So;
   private final Setting<Double> acb;
   private final Setting<Integer> G;
   private final Setting<Integer> aKm;
   private final Setting<Boolean> bQ;
   private final Setting<Boolean> xM;
   private final Setting<Integer> aPE;
   private final Setting<Boolean> su;
   private final Setting<Boolean> Lu;
   private final Setting<Double> Ld;
   private final Setting<Integer> GC;
   private final Setting<Integer> wk;
   private final Setting<Integer> aEn;
   private final Setting<Integer> aN;
   private final Setting<Boolean> tn;
   private final Setting<Integer> zw;
   private final Setting<Boolean> aBm;
   private final Setting<Integer> alN;
   private final Setting<Integer> aOS;
   private final Setting<Integer> sO;
   private final Setting<Integer> anO;
   private final Setting<Integer> dM;
   private final Setting<Boolean> HD;
   private final Setting<Integer> aOW;
   private final Setting<Boolean> zl;
   private final Setting<Boolean> aCh;
   private final Setting<Integer> zd;
   private final Setting<AliceObf_sZ> Yp;
   private final Setting<Double> apM;
   private final Setting<Boolean> anb;
   private final Setting<Boolean> aNq;
   private final Setting<Double> br;
   private final Setting<Double> auL;
   private final Setting<Boolean> DP;
   private final Setting<Boolean> Fi;
   private final Setting<Double> aMI;
   private final Setting<Boolean> atY;
   private final Setting<Boolean> rc;
   private final Setting<Double> hG;
   private final Setting<Double> JJ;
   private final Setting<ShapeMode> aMm;
   private final Setting<SettingColor> HB;
   private final Setting<SettingColor> ev;
   private final Setting<Boolean> aOR;
   private final Setting<Boolean> QC;
   private final Setting<Boolean> ur;
   private final Setting<Boolean> aOP;
   private final Setting<Boolean> akE;
   private final Setting<Integer> aHu;
   private final Setting<ShapeMode> hb;
   private final Setting<SettingColor> aDC;
   private final Setting<SettingColor> aPR;
   private final Setting<SettingColor> vk;
   private final Setting<SettingColor> tx;
   private final Setting<SettingColor> aoU;
   private final Setting<SettingColor> ow;
   private final Setting<SettingColor> anL;
   private final Setting<Double> aws;
   private final AliceObf_pn Zx;
   private final AliceObf_iD WR;
   private static CrystalAuraPlusPlus EU;
   private final AliceObf_tl Nv;
   private final AtomicReference<AliceObf_no> aFn;
   private final AtomicReference<AliceObf_bi> zk;
   private volatile AliceObf_oE BI;
   private BlockPos Cq;
   private Vec3d xx;
   private BlockPos ahS;
   private float EB;
   private int LJ;
   private BlockPos amW;
   private BlockPos Wj;
   private boolean arT;
   private Item auK;
   private int atJ;
   private int aLP;
   private final Map<Integer, AliceObf_rn> azw;
   private final Map<Integer, Long> aAg;
   private final Map<Integer, Long> Dm;
   private final Queue<Vec3d> TP;
   private AliceObf_lG qn;
   private long bL;
   private long oP;
   private long rp;
   private boolean aga;
   private final List<EndCrystalEntity> Da;
   private boolean bX;
   private int alx;
   private int aPD;
   private int[] kY;
   private int tT;
   private int Vw;
   private long aFm;
   private int Dr;
   private int OB;
   private int[] jY;
   private int KL;
   private int OC;
   private long aKq;
   private long eA;
   private AliceObf_aG Fb;
   private BlockPos Iq;
   private BlockPos aln;
   private int Lq;
   private BlockPos eG;
   private final List<AliceObf_dB> aeI;
   private final Map<Integer, Integer> auc;
   private int afe;
   private ClientWorld aJm;
   private BlockPos CE;
   private int DI;
   private long aNE;
   private long ge;
   private BlockPos abG;
   private BlockPos acQ;
   private long ug;
   private AliceObf_lu aDq;
   private AliceObf_lq aIP;
   private EndCrystalEntity iE;
   private final AliceObf_qw du;
   private boolean aEc;
   private final List<AliceObf_vd> qJ;
   private int apy;
   private AliceObf_bP AP;
   private final List<AliceObf_rD> ayt;
   private final Vector3d aec;
   private float fp;
   private float WI;
   private boolean azF;
   private AliceObf_np aDB;
   private AliceObf_sl aBl;
   private boolean EQ;
   private final AliceObf_iG NW;
   private float jG;
   private float aII;
   private boolean cl;
   private final Map<BlockPos, AliceObf_vl> yd;
   private final Map<Integer, BlockPos> ny;
   private BlockPos azO;
   private long HT;
   private int aQZ;
   private ClientWorld aaO;
   private boolean td;
   private int pt;
   public CrystalAuraPlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "crystal-aura++", "Tu dong dat va pha pha le voi tinh toan sat thuong chinh xac cung goc xoay on dinh.");
      this.Ks = this.settings.createGroup("Burst");
      this.oB = this.settings.createGroup("Place");
      this.LH = this.settings.createGroup("Break");
      this.bz = this.settings.createGroup("Rotation");
      this.x = this.settings.createGroup("Pause");
      this.ml = this.settings.createGroup("Render");
      this.EZ = this.settings.createGroup("DTAP");
      this.ZW = this.EZ.add(((Builder)((Builder)((Builder)new Builder().name("dtap")).description("Phoi hop Kill Aura++: sword, knockback, base va crystal. Cho knockback khong lam cham nhip kiem.")).defaultValue(false)).build());
      this.ayx = this.EZ
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("hit-window-ticks"))
                        .description("Cua so quan sat damage va dich chuyen sau moi hit sword; Kill Aura van danh binh thuong."))
                     .defaultValue(12))
                  .range(1, 40)
                  .sliderRange(1, 40)
                  .visible(this.ZW::get))
               .build()
         );
      this.aeK = this.EZ
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("crystal-window-ticks"))
                        .description("Gioi han luot base/crystal truoc khi tra quyen cho Kill Aura. Khong thay delay place/break."))
                     .defaultValue(12))
                  .range(1, 40)
                  .sliderRange(1, 40)
                  .visible(this.ZW::get))
               .build()
         );
      this.aHF = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("target-range"))
                  .description("tam toi da toi muc tieu"))
               .defaultValue(10.0)
               .range(1.0, 16.0)
               .sliderRange(1.0, 16.0)
               .build()
         );
      this.Iu = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("max-targets"))
                     .description("so luong muc tieu danh cung luc"))
                  .defaultValue(2))
               .range(1, 8)
               .sliderRange(1, 8)
               .build()
         );
      this.pq = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("min-damage"))
                  .description("damage toi thieu de danh muc tieu"))
               .defaultValue(4.0)
               .range(0.0, 36.0)
               .sliderRange(0.0, 20.0)
               .build()
         );
      this.ajY = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("face-place-health"))
                  .description("HP thap nhat cua ke dich de crystal lien tuc"))
               .defaultValue(9.0)
               .range(0.0, 36.0)
               .sliderRange(0.0, 20.0)
               .build()
         );
      this.aQq = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("max-self-damage"))
                  .description("sat thuong toi da ban than co the nhan khi danh muc tieu"))
               .defaultValue(5.0)
               .range(0.0, 36.0)
               .sliderRange(0.0, 20.0)
               .build()
         );
      this.Qq = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("min-advantage"))
                  .description("loi the = sat thuong len muc tieu - sat thuong ban than"))
               .defaultValue(1.0)
               .range(-20.0, 36.0)
               .sliderRange(-5.0, 12.0)
               .build()
         );
      this.aji = this.VO.add(((Builder)((Builder)((Builder)new Builder().name("anti-suicide")).description("bao ve ban than khoi viec danh chinh minh")).defaultValue(false)).build());
      this.atE = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("safety-reserve"))
                     .description("luong HP muon giu lai"))
                  .defaultValue(1.0)
                  .range(0.0, 20.0)
                  .sliderRange(0.0, 10.0)
                  .visible(this.aji::get))
               .build()
         );
      this.aQr = this.VO.add(((Builder)((Builder)((Builder)new Builder().name("predict-movement")).description("du doan chuyen dong cua dich")).defaultValue(true)).build());
      this.KJ = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("combat-mode"))
                     .description("Sync: du doan mot lan dat lai sau attack da gui. Speed: du doan theo nhip client. Danger: them nhieu packet va du doan entity ID khi timing la Sequential."))
                  .defaultValue(AliceObf_aX.pe))
               .build()
         );
      this.aKA = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("timing"))
                     .description("Vanilla: dat/pha theo tick, moi epoch mot action. Sequential: phan ung khi spawn va noi tiep dat/pha khi du dieu kien."))
                  .defaultValue(AliceObf_id.uI))
               .build()
         );
      this.QW = this.Ks.add(((Builder)((Builder)((Builder)new Builder().name("burst-enabled")).description("cho phep burst toc do cao trong cua so ngan de ket lieu autototem 1-tick")).defaultValue(true)).build());
      this.aei = this.Ks
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("burst-timing"))
                        .description("Timing rieng khi burst dang chay: Vanilla theo tick; Sequential noi tiep dat/pha. Het burst se dung lai timing thuong."))
                     .defaultValue(AliceObf_id.uI))
                  .visible(this.QW::get))
               .build()
         );
      this.zf = this.Ks
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("burst-duration-ms"))
                        .description("thoi gian burst chay speed/danger voi delay 0ms"))
                     .defaultValue(3000))
                  .range(500, 5000)
                  .sliderRange(500, 5000)
                  .visible(this.QW::get))
               .build()
         );
      this.iV = this.Ks
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("burst-cooldown-ms"))
                        .description("thoi gian nghi ve sync sau burst truoc khi duoc burst lai"))
                     .defaultValue(5000))
                  .range(2000, 15000)
                  .sliderRange(2000, 15000)
                  .visible(this.QW::get))
               .build()
         );
      this.vm = this.Ks
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("burst-trigger"))
                        .description("dieu kien kich hoat burst"))
                     .defaultValue(AliceObf_ko.Ev))
                  .visible(this.QW::get))
               .build()
         );
      this.alB = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("pick-item"))
                     .description("chon item theo mode"))
                  .defaultValue(AliceObf_jY.Ab))
               .build()
         );
      this.ayK = this.oB.add(((Builder)((Builder)((Builder)new Builder().name("place")).description("dat crystal")).defaultValue(true)).build());
      this.So = this.VO
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("raycast-mode"))
                     .description("chon mode raycast"))
                  .defaultValue(AliceObf_lJ.amg))
               .build()
         );
      this.acb = this.oB
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("place-range"))
                  .description("toi da tam dat crystal"))
               .defaultValue(4.5)
               .range(1.0, 6.0)
               .sliderRange(1.0, 6.0)
               .build()
         );
      this.G = this.oB
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("min-place-delay-ms"))
                     .description("so mili-giay toi thieu giua cac goi place va support-place"))
                  .defaultValue(0))
               .range(0, 1000)
               .sliderRange(0, 500)
               .build()
         );
      this.aKm = this.oB
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("max-place-delay-ms"))
                     .description("so mili-giay toi da giua cac goi place va support-place"))
                  .defaultValue(0))
               .range(0, 1000)
               .sliderRange(0, 500)
               .build()
         );
      this.bQ = this.oB.add(((Builder)((Builder)((Builder)new Builder().name("multi-point-place")).description("phan tan place qua nhieu base gan tuong duong thay vi lap lien tuc mot diem")).defaultValue(false)).build());
      this.xM = this.oB.add(((Builder)((Builder)((Builder)new Builder().name("support")).description("dat obsidian")).defaultValue(true)).build());
      this.aPE = this.oB
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("danger-place-packets"))
                        .description("Danger: so crystal dat trong cung mot tick. >1 phai quet lai toan bo o place moi tick nen ton CPU. Khong Grim-safe."))
                     .defaultValue(1))
                  .range(1, 4)
                  .sliderRange(1, 4)
                  .visible(() -> this.Cr()))
               .build()
         );
      this.su = this.LH.add(((Builder)((Builder)((Builder)new Builder().name("break")).description("tan cong crystal")).defaultValue(true)).build());
      this.Lu = this.LH.add(((Builder)((Builder)((Builder)new Builder().name("own-crystals-only")).description("chi dap crystal do chinh minh dat (module hoac tay). Ghep theo vi tri spawn voi lan place vua gui trong 2 giay, khong cho them buoc nao. Crystal nguoi khac bi bo qua ke ca khi dap no co loi; co the nham neu ho dat dung o minh vua nham.")).defaultValue(true)).build());
      this.Ld = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("break-range"))
                  .description("tam tan cong crystal"))
               .defaultValue(4.5)
               .range(1.0, 6.0)
               .sliderRange(1.0, 6.0)
               .build()
         );
      this.GC = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("min-break-delay-ms"))
                     .description("so mili-giay toi thieu giua cac goi dap crystal"))
                  .defaultValue(0))
               .range(0, 1000)
               .sliderRange(0, 500)
               .build()
         );
      this.wk = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("max-break-delay-ms"))
                     .description("so mili-giay toi da giua cac goi dap crystal"))
                  .defaultValue(75))
               .range(0, 1000)
               .sliderRange(0, 500)
               .build()
         );
      this.aEn = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("retry-delay"))
                     .description("khoang nghi giua cac lan thu lai tan cong crystal"))
                  .defaultValue(1))
               .range(1, 10)
               .sliderRange(1, 10)
               .build()
         );
      this.aN = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("max-attempts"))
                     .description("so lan thu dap lai cung crystal khi chua vo"))
                  .defaultValue(3))
               .range(1, 5)
               .sliderRange(1, 5)
               .build()
         );
      this.tn = this.LH
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("predicted-id-break")).description("Danger: dap crystal ngay trong tick vua place bang entity ID du doan. Doan sai thi server bo qua packet. Khong Grim-safe.")).defaultValue(true)).visible(() -> this.Cr())).build());
      this.zw = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("predicted-id-attacks"))
                        .description("ban bao nhieu entity ID lien tiep sau ID lon nhat da thay. 1 = doan don le (gan nhu luon truot), 3-5 = phu mot dai ID."))
                     .defaultValue(3))
                  .range(1, 10)
                  .sliderRange(1, 10)
                  .visible(() -> this.Cr() && (Boolean)this.tn.get()))
               .build()
         );
      this.aBm = this.LH
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("predicted-id-gate")).description("Danger: khong ban dai ID du doan khi tin hieu cho thay client dang lac voi ID that server dang cap (vua join, spawn rate cao, ping jitter cao, entity remove chua kip nhan spawn).")).defaultValue(true))
                  .visible(() -> this.Cr() && (Boolean)this.tn.get()))
               .build()
         );
      this.alN = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("predicted-id-join-guard-ticks"))
                        .description("Danger: khong ban dai ID du doan trong X tick dau sau khi join/respawn/doi world. Seed tu snapshot world gan nhu luon cu trong khoang nay."))
                     .defaultValue(40))
                  .range(0, 400)
                  .sliderRange(20, 200)
                  .visible(() -> this.Cr() && (Boolean)this.tn.get() && (Boolean)this.aBm.get()))
               .build()
         );
      this.aOS = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("predicted-id-max-spawn-rate"))
                        .description("Danger: khong ban dai ID du doan khi so entity spawn nhan duoc trong window gan nhat vuot muc nay. Spawn rate cao = server cap ID nhanh hon client theo kip."))
                     .defaultValue(6))
                  .range(1, 100)
                  .sliderRange(2, 30)
                  .visible(() -> this.Cr() && (Boolean)this.tn.get() && (Boolean)this.aBm.get()))
               .build()
         );
      this.sO = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("predicted-id-spawn-window-ticks"))
                        .description("Danger: cua so tick de dem spawn rate o tren. Cang dai cang nhay voi dot spawn dot ngot, cang ngan cang it nhay."))
                     .defaultValue(20))
                  .range(5, 200)
                  .sliderRange(10, 60)
                  .visible(() -> {
                     if (this.Cr()) {
                        if (!(Boolean)this.tn.get()) {

                        } else {
                           short var1 = 13990;
                        }
                     }

                     return false;
                  }))
               .build()
         );
      this.anO = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("predicted-id-max-ping-jitter"))
                        .description("Danger: khong ban dai ID du doan khi do lech chuan cua ping (ms) vuot muc nay. Ping troi = spawn packet toi cham khong deu -> ID da thay cu nhanh hon binh thuong."))
                     .defaultValue(80))
                  .range(0, 500)
                  .sliderRange(20, 200)
                  .visible(() -> this.Cr() && (Boolean)this.tn.get() && (Boolean)this.aBm.get()))
               .build()
         );
      this.dM = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("predicted-id-max-pending-removals"))
                        .description("Danger: khong ban dai ID du doan khi co qua X entity bi remove trong tick truoc do ma chua thay spawn kem theo. Removal chua kem spawn = server via cap ID cho entity khac."))
                     .defaultValue(1))
                  .range(0, 20)
                  .sliderRange(0, 8)
                  .visible(() -> this.Cr() && (Boolean)this.tn.get() && (Boolean)this.aBm.get()))
               .build()
         );
      this.HD = this.LH
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("predicted-id-gate-on-tracked-count")).description("Danger: khong ban dai ID du doan khi so entity client dang track vuot muc duoi. Nhieu entity = server quan ly nhieu cho khac ngoai tracking cua minh.")).defaultValue(false))
                  .visible(() -> this.Cr() && (Boolean)this.tn.get() && (Boolean)this.aBm.get()))
               .build()
         );
      this.aOW = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("predicted-id-max-tracked-entities"))
                        .description("Danger: nguong entity tracked khi bat gate tren."))
                     .defaultValue(300))
                  .range(20, 2000)
                  .sliderRange(50, 800)
                  .visible(() -> {
                     if (this.Cr() && (Boolean)this.tn.get()) {
                        if ((Boolean)this.aBm.get()) {

                        } else {
                           short var1 = 17721;
                        }
                     }

                     return false;
                  }))
               .build()
         );
      this.zl = this.LH.add(((Builder)((Builder)((Builder)new Builder().name("explosion-inhibit")).description("coi moi crystal gan mot vu no vua nhan la da chet trong ping*2 ms, de khong dap vao crystal ma server da xoa va khong bi chan cho khi place lai.")).defaultValue(true)).build());
      this.aCh = this.LH.add(((Builder)((Builder)((Builder)new Builder().name("anti-weakness")).description("dung vu khi trong hotbar co the pha crystal khi dang bi weakness")).defaultValue(true)).build());
      this.zd = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("danger-break-packets"))
                        .description("Danger: so crystal dap trong cung mot pass, xep theo sat thuong giam dan. Tong self-damage cua ca pass van bi chan boi max-self-damage. Khong Grim-safe."))
                     .defaultValue(2))
                  .range(1, 6)
                  .sliderRange(1, 6)
                  .visible(() -> this.Cr()))
               .build()
         );
      this.Yp = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("danger-logic"))
                        .description("Danger: dap truoc roi dat, hay dat truoc roi dap, trong cung mot epoch."))
                     .defaultValue(AliceObf_sZ.afD))
                  .visible(() -> this.Cr()))
               .build()
         );
      this.apM = this.LH
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("danger-instant-min-damage"))
                     .description("Danger: khong dap-ngay bang ID du doan neu crystal vua dat gay it hon muc nay cho muc tieu. Giu lai crystal face-place sat thuong thap lam vat chan."))
                  .defaultValue(0.0)
                  .range(0.0, 20.0)
                  .sliderRange(0.0, 12.0)
                  .visible(() -> {
                     if (this.Cr()) {
                        int var10000 = this.tn.get() ? 54931 : 54932;

                        while (true) {
                           int var1 = var10000;
                           if (var1 == 54931) {
                              return true;
                           }

                           if (var1 != 54932) {
                              break;
                           }

                        }
                     }

                     return false;
                  }))
               .build()
         );
      this.anb = this.LH
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("sound-confirm")).description("coi mot tieng no BLOCKS la xac nhan crystal quanh do da vo, cho server khong gui ExplosionS2CPacket. Tieng no TNT/giuong gan do cung tinh.")).defaultValue(true)).visible(() -> (Boolean)this.zl.get()))
               .build()
         );
      this.aNq = this.bz.add(((Builder)((Builder)((Builder)new Builder().name("rotation-hold")).description("giu movefix va lookpov vao base crystal sap dat khi con muc tieu. Chi tra camera khi het target.")).defaultValue(true)).build());
      this.br = this.bz
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("yaw-step"))
                  .description("goc yaw thay doi toi da tren server moi chu ky"))
               .defaultValue(180.0)
               .range(1.0, 180.0)
               .sliderRange(1.0, 180.0)
               .build()
         );
      this.auL = this.bz
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("pitch-step"))
                  .description("goc pitch thay doi toi da tren server moi chu ky. 180 = quay tuc thi nhu yaw-step; Danger khong bi anh huong vi no ban ngay voi goc da queue."))
               .defaultValue(90.0)
               .range(1.0, 180.0)
               .sliderRange(1.0, 180.0)
               .build()
         );
      this.DP = this.x.add(((Builder)((Builder)((Builder)new Builder().name("pause-on-use")).description("tam dung khi ban dang dung item de tranh xung dot")).defaultValue(false)).build());
      this.Fi = this.x.add(((Builder)((Builder)((Builder)new Builder().name("pause-on-mine")).description("tam dung khi giu nut danh de tranh xung dot")).defaultValue(true)).build());
      this.aMI = this.x
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("pause-health"))
                  .description("tam dung khi mau thap hon muc nay"))
               .defaultValue(0.0)
               .range(0.0, 36.0)
               .sliderRange(0.0, 20.0)
               .build()
         );
      this.atY = this.ml.add(((Builder)((Builder)((Builder)new Builder().name("render")).description("cong tac chinh cho moi overlay va nhan sat thuong cua Crystal Aura++")).defaultValue(true)).build());
      this.rc = this.ml
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("crystal-chams")).description("hien thi model crystal that xuyen tuong ma khong gay anh huong den server")).defaultValue(true)).visible(this.atY::get)).build());
      this.hG = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("chams-range"))
                     .description("tam toi da de render crystal xuyen tuong"))
                  .defaultValue(16.0)
                  .range(4.0, 64.0)
                  .sliderRange(4.0, 32.0)
                  .visible(() -> {
                     if ((Boolean)this.atY.get()) {
                        if ((Boolean)this.rc.get()) {

                        } else {
                           short var1 = 30243;
                        }
                     }

                     return false;
                  }))
               .build()
         );
      this.JJ = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("chams-scale"))
                     .description("ty le cua model crystal xuyen tuong"))
                  .defaultValue(1.0)
                  .range(0.25, 2.0)
                  .sliderRange(0.5, 1.5)
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.rc.get()))
               .build()
         );
      this.aMm = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("chams-shape-mode"))
                        .description("ve cac mat, canh hoac ca hai cua model crystal xuyen tuong"))
                     .defaultValue(ShapeMode.Both))
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.rc.get()))
               .build()
         );
      this.HB = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("chams-side-color"))
                     .description("mau nen cua model crystal xuyen tuong"))
                  .defaultValue(new SettingColor(190, 90, 255, 45))
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.rc.get() && ((ShapeMode)this.aMm.get()).sides()))
               .build()
         );
      this.ev = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("chams-line-color"))
                     .description("mau vien cua model crystal xuyen tuong"))
                  .defaultValue(new SettingColor(220, 150, 255, 220))
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.rc.get() && ((ShapeMode)this.aMm.get()).lines()))
               .build()
         );
      this.aOR = this.ml
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("render-place")).description("hien thi khoi be do da duoc xac nhan")).defaultValue(true)).visible(this.atY::get)).build());
      this.QC = this.ml
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("render-break")).description("hien thi hitbox cua crystal muc tieu da duoc xac nhan")).defaultValue(true)).visible(this.atY::get)).build());
      this.ur = this.ml
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("render-pending")).description("hien thi cac hanh dong dang cho server cap nhat")).defaultValue(true)).visible(this.atY::get)).build());
      this.aOP = this.ml
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("render-damage")).description("hien thi sat thuong muc tieu va ban than")).defaultValue(true)).visible(this.atY::get)).build());
      this.akE = this.ml
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("render-fade")).description("giu overlay dat/pha da xong roi mo dan cho muot")).defaultValue(true)).visible(this.atY::get)).build());
      this.aHu = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("fade-time"))
                        .description("thoi gian (tick) de mo dan hanh dong da hoan thanh"))
                     .defaultValue(8))
                  .range(1, 40)
                  .sliderRange(1, 20)
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.akE.get()))
               .build()
         );
      this.hb = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                           .name("shape-mode"))
                        .description("ve cac mat, vien hoac ca hai cua overlay"))
                     .defaultValue(ShapeMode.Both))
                  .visible(this.atY::get))
               .build()
         );
      this.aDC = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("place-side-color"))
                     .description("mau nen cua overlay hien thi vi tri dat"))
                  .defaultValue(new SettingColor(70, 255, 130, 45))
                  .visible(() -> (Boolean)this.atY.get() && ((ShapeMode)this.hb.get()).sides()))
               .build()
         );
      this.aPR = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("place-line-color"))
                     .description("mau vien cho lop phu dat"))
                  .defaultValue(new SettingColor(70, 255, 130, 220))
                  .visible(() -> {
                     if ((Boolean)this.atY.get()) {
                        if (((ShapeMode)this.hb.get()).lines()) {

                        } else {
                           char var1 = 42929;
                        }
                     }

                     return false;
                  }))
               .build()
         );
      this.vk = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("break-side-color"))
                     .description("mau nen cua overlay hien thi vi tri dap"))
                  .defaultValue(new SettingColor(255, 70, 90, 45))
                  .visible(() -> (Boolean)this.atY.get() && ((ShapeMode)this.hb.get()).sides()))
               .build()
         );
      this.tx = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("break-line-color"))
                     .description("mau vien cho lop phu dap"))
                  .defaultValue(new SettingColor(255, 70, 90, 220))
                  .visible(() -> {
                     if ((Boolean)this.atY.get()) {
                        int var10000 = ((ShapeMode)this.hb.get()).lines() ? 15362 : 15363;

                        while (true) {
                           int var1 = var10000;
                           if (var1 == 15362) {
                              return true;
                           }

                           if (var1 != 15363) {
                              break;
                           }

                        }
                     }

                     return false;
                  }))
               .build()
         );
      this.aoU = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("pending-side-color"))
                     .description("mau nen cho cac hanh dong dang cho server xac nhan"))
                  .defaultValue(new SettingColor(255, 185, 45, 35))
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.ur.get() && ((ShapeMode)this.hb.get()).sides()))
               .build()
         );
      this.ow = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("pending-line-color"))
                     .description("mau vien cho cac thao tac dang cho server"))
                  .defaultValue(new SettingColor(255, 185, 45, 220))
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.ur.get() && ((ShapeMode)this.hb.get()).lines()))
               .build()
         );
      this.anL = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                        .name("damage-color"))
                     .description("mau cua nhan hien thi sat thuong len muc tieu/ban than"))
                  .defaultValue(new SettingColor(255, 255, 255, 255))
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.aOP.get()))
               .build()
         );
      this.aws = this.ml
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("damage-scale"))
                     .description("ty le scale cho nhan hien thi sat thuong the gioi"))
                  .defaultValue(1.0)
                  .range(0.5, 3.0)
                  .sliderRange(0.5, 2.0)
                  .visible(() -> (Boolean)this.atY.get() && (Boolean)this.aOP.get()))
               .build()
         );
      this.Zx = new AliceObf_pn();
      this.WR = new AliceObf_iD(this);
      this.Nv = new AliceObf_tl();
      this.aFn = new AtomicReference<>();
      this.zk = new AtomicReference<>();
      this.LJ = -1;
      this.atJ = -1;
      this.aLP = -1;
      this.azw = new HashMap<>();
      this.aAg = new HashMap<>();
      this.Dm = new ConcurrentHashMap<>();
      this.TP = new ConcurrentLinkedQueue<>();
      this.qn = AliceObf_lG.cT;
      this.Da = new ArrayList<>();
      this.alx = -1;
      this.aPD = 0;
      this.kY = new int[0];
      this.tT = 0;
      this.Vw = 0;
      this.aFm = 0L;
      this.Dr = 0;
      this.OB = -1;
      this.jY = new int[0];
      this.KL = 0;
      this.OC = 0;
      this.aKq = 0L;
      this.eA = 0L;
      this.aeI = new ArrayList<>();
      this.auc = new HashMap<>();
      this.du = new AliceObf_qw();
      this.qJ = new ArrayList<>();
      this.ayt = new ArrayList<>();
      this.aec = new Vector3d();
      this.NW = new AliceObf_iG();
      this.yd = new HashMap<>();
      this.ny = new HashMap<>();
      this.azO = null;
      this.HT = 0L;
      this.aQZ = -1;
      this.pt = -1;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.zi();
         EU = this;
         AliceObf_fS.Od(this);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.BZ();
      AliceObf_fS.Oe(this);
      if (!(EU == this)) {
         short var1 = 26351;
      }

      this.zi();
      super.onDeactivate();
   }

   private void zi() {
      this.zt();
      this.arT = false;
      this.aQZ = -1;
      this.aaO = null;
      this.td = false;
      this.EQ = false;
      this.aJm = this.mc.world;
      this.CE = null;
      this.azw.clear();
      this.Dm.clear();
      this.TP.clear();
      this.aAg.clear();
      this.qn = AliceObf_lG.cT;
      this.bL = 0L;
      this.oP = 0L;
      this.rp = 0L;
      this.aga = false;
      this.Da.clear();
      this.bX = false;
      this.alx = -1;
      this.aPD = this.afe;
      this.kY = new int[0];
      this.tT = 0;
      this.Vw = 0;
      this.aFm = 0L;
      this.pt = -1;
      this.Dr = 0;
      this.OB = -1;
      this.jY = new int[0];
      this.KL = 0;
      this.OC = 0;
      this.aKq = 0L;
      this.eA = 0L;
      this.Fb = null;
      this.Iq = null;
      this.LJ = -1;
      this.amW = null;
      this.Wj = null;
      this.aln = null;
      this.Lq = 0;
      this.eG = null;
      this.aeI.clear();
      this.auc.clear();
      this.aDq = null;
      this.aIP = null;
      this.aEc = false;
      this.qJ.clear();
      this.iE = null;
      this.du.EM();
      this.apy = 0;
      this.Zx.Rn();
      this.WR.QV();
      this.auK = null;
      this.atJ = -1;
      this.aLP = -1;
      this.yd.clear();
      this.ny.clear();
      this.azO = null;
      this.HT = 0L;
      this.BZ();
      this.afe = 0;
      this.aNE = 0L;
      this.ge = 0L;
      this.abG = null;
      this.acQ = null;
      this.ug = 0L;
      this.AP = null;
      this.ayt.clear();
      this.fp = 0.0F;
      this.WI = 0.0F;
      this.azF = false;
      this.aDB = null;
      this.jG = 0.0F;
      this.aII = 0.0F;
      this.cl = false;
      this.NW.VP();
   }

   @AliceHandler(
      priority = 1000001
   )
   private void zj(Pre var1) {
      if (this.aJm != this.mc.world) {
         this.BZ();
         this.zi();
      }

      if (this.mc.player != null && this.mc.world != null) {
         this.afe++;
         this.bX = false;
         this.apy = 0;
         this.Ch();
         this.Ci();
         if (!AliceObf_fS.Oh(this)) {

         } else {
            short var2 = 22728;
         }

         this.zl();
         this.zk();
      }
   }

   private void zk() {
      if (!(this.OB == this.afe)) {
         char var1 = 62496;
      }
   }

   private void zl() {
      if ((Boolean)this.aBm.get()) {
         int var1 = this.zV();
         byte var2 = 40;
         if (this.jY.length != var2) {
            this.jY = new int[var2];
            this.KL = 0;
            this.OC = 0;
            this.aKq = 0L;
            this.eA = 0L;
         }

         if (this.OC == var2) {
            int var3 = this.jY[this.KL];
            this.aKq -= var3;
            this.eA -= (long)var3 * var3;
         } else {
            this.OC++;
         }

         this.jY[this.KL] = var1;
         this.KL = (this.KL + 1) % var2;
         this.aKq += var1;
         this.eA += (long)var1 * var1;
      }
   }

   private double zm() {
      if (this.OC >= 2) {
         double var1 = this.OC;
         double var3 = this.aKq / var1;
         double var5 = (this.eA - var1 * var3 * var3) / (var1 - 1.0);
         return !(var5 > 0.0) ? 0.0 : Math.sqrt(var5);
      } else {
         return 0.0;
      }
   }

   static boolean zn() {
      CrystalAuraPlusPlus var0 = EU;
      return var0 != null && var0.zs();
   }

   static boolean zo() {
      CrystalAuraPlusPlus var0 = EU;
      if (!(var0 != null)) {
         short var1 = 1848;
      }

      return false;
   }

   static void zp() {
      CrystalAuraPlusPlus var0 = EU;
      if (var0 != null) {
         var0.zu();
      }
   }

   static void zq(PlayerEntity var0) {
      CrystalAuraPlusPlus var1 = EU;
      if (!(var1 != null)) {
         short var4 = 20464;
      }
   }

   static void zr(PlayerEntity var0) {
      CrystalAuraPlusPlus var1 = EU;
      if (var1 != null && var1.zs() && var1.AE() && !var1.Nv.EA()) {
         int var2 = AliceObf_fS.OL();
         if (var2 >= 0 && var2 <= 8 && var1.mc.player.getInventory().getStack(var2).isIn(ItemTags.SWORDS)) {
            Vec3d var3 = var1.zA(var0);
            var1.aFn.set(null);
            var1.zk.set(null);
            var1.EB = DamageUtils.getAttackDamage(var1.mc.player, var0, var1.mc.player.getInventory().getStack(var2));
            var1.ahS = var1.zz(var0);
            var1.xx = null;
            var1.BI = var1.Nv
               .Ep(
                  var1.mc.world,
                  var1.mc.player,
                  var0,
                  var0.getId(),
                  var1.mc.player.getId(),
                  var1.afe,
                  var3.x,
                  var3.y,
                  var3.z,
                  (Integer)var1.ayx.get()
               );
         } else {
            var1.zu();
         }
      }
   }

   private boolean zs() {
      if (this.isActive() && (Boolean)this.ZW.get()) {
         KillAuraPlusPlus var1 = (KillAuraPlusPlus)Modules.get().get(KillAuraPlusPlus.class);
         return var1 != null && var1.isActive();
      } else {
         return false;
      }
   }

   private void zt() {
      this.Nv.EE();
      this.BI = null;
      this.aFn.set(null);
      this.zk.set(null);
      this.Cq = null;
      this.xx = null;
      this.ahS = null;
      this.EB = 0.0F;
      this.LJ = -1;
      this.amW = null;
      this.Wj = null;
   }

   private void zu() {
      boolean var1 = this.Nv.EA();
      this.zt();
      if (var1) {
         this.aDq = null;
         this.Fb = null;
         this.zM(null);
         this.suspendForItemUse();
      }
   }

   private boolean zv(PlayerEntity var1) {
      return this.mc.world != null
         && this.mc.player != null
         && var1 != null
         && this.mc.world.getEntityById(var1.getId()) == var1
         && var1 != this.mc.player
         && var1.isAlive()
         && !var1.isRemoved()
         && !var1.isCreative()
         && !var1.isSpectator()
         && Friends.get().shouldAttack(var1)
         && this.mc.player.squaredDistanceTo(var1) <= (Double)this.aHF.get() * (Double)this.aHF.get();
   }

   private boolean zw(Item var1) {
      int var10000 = !this.mc.player.getOffHandStack().isOf(var1) ? 13699 : 13698;

      while (true) {
         int var2 = var10000;
         if (var2 == 13698) {
            return true;
         }

         if (var2 != 13699) {
            return this.AO(var1);
         }

      }
   }

   private void zx() {
      boolean var1 = this.zs();
      if (var1 != this.arT) {
         this.zt();
         this.aDq = null;
         this.Fb = null;
         this.zM(null);
         this.suspendForItemUse();
         this.arT = var1;
      }

      if (!(!var1)) {
         char var15 = 46427;
      }

      AliceObf_oE var2 = this.Nv.Ez();
      if (var2 != null) {
         PlayerEntity var3 = (PlayerEntity)var2.Eh();
         boolean var4 = this.Nv.EA();
         if (!this.Nv
            .Ev(this.mc.world, this.mc.player, var3, this.AE() && (Boolean)this.ayK.get() && (Boolean)this.su.get() && this.zv(var3), this.afe)) {
            this.zt();
            if (var4) {
               this.aDq = null;
               this.suspendForItemUse();
            }
         } else {
            AliceObf_no var5 = this.aFn.getAndSet(null);
            if (var5 != null) {
               this.Nv.Eq(var5.pR, var5.aBP, var5.Pq, var5.Xx);
            }

            Vec3d var6 = this.zA(var3);
            AliceObf_bi var7 = this.zk.getAndSet(null);
            if (var7 != null && var7.oH * var7.oH + var7.WA * var7.WA >= 0.01) {
               this.Nv.Er(var7.aNY, var7.kt, var7.oH, var7.iA, var7.WA, this.afe, var6.x, var6.z);
            }

            this.Nv.Es(this.afe, var6.x, var6.z);
            this.zE(var3, var6);
            List var8 = List.of(var3);
            if (!this.Nv.EA()) {
               Vec3d var13 = var3.getEntityPos();
               if (this.Nv.Ew(this.afe, var13.x, var13.y, var13.z) && this.zw(Items.END_CRYSTAL)) {
                  Object var14 = this.zy(var8, false);
                  if (var14 == null && (Boolean)this.xM.get() && this.zw(Items.OBSIDIAN)) {
                     var14 = this.zy(var8, true);
                  }

                  if (var14 != null && this.CD(AliceObf_up.wD, ((AliceObf_aG)var14).aQN)) {
                     if (this.Nv.Ex(this.afe, this.zC(), true, var13.x, var13.y, var13.z)) {
                        this.Cq = ((AliceObf_aG)var14).aQN;
                        this.aDq = null;
                        this.LJ = -1;
                        this.amW = null;
                        this.Wj = null;
                        this.CE = null;
                        this.Fb = null;
                        this.aIP = null;
                        this.iE = null;
                     }
                  }
               }
            } else {
               AliceObf_lu var9 = this.aDq;
               if (var9 != null && var9.ajH.equals(this.Cq) && !this.Ag(this.Cq)) {
                  if (this.afe - var9.ca > this.zC()) {
                     this.zu();
                  }
               } else if (this.Cq == null || !this.Ah(this.Cq)) {
                  if (this.amW == null || this.Cq == null || !this.amW.equals(this.Cq)) {
                     AliceObf_sR var10 = this.AG(var8);
                     Object var11 = this.AU(this.Cq, false, var8);
                     if (var11 == null && (Boolean)this.xM.get()) {
                        var11 = this.AU(this.Cq, true, var8);
                     }

                     int var10001 = var11 == null ? 12465 : 12466;

                     while (true) {
                        int var12 = var10001;
                        if (var12 == 12465) {
                           if (!this.Cp() && this.aDq != null) {
                              var11 = this.AX(this.Cq, var8);
                           }
                           break;
                        }

                        if (var12 != 12466) {
                           break;
                        }

                     }

                     if ((var10 == null || this.Bm(var10.ajn) == Integer.MIN_VALUE)
                        && (
                           var11 == null
                              || !this.AN((AliceObf_aG)var11)
                        )) {
                        this.zu();
                     }
                  }
               }
            }
         }
      }
   }

   private AliceObf_aG zy(List<PlayerEntity> var1, boolean var2) {
      AliceObf_aG var3 = null;
      double var4 = Double.NEGATIVE_INFINITY;
      BlockPos var6 = this.xx != null ? BlockPos.ofFloored(this.xx).down() : ((PlayerEntity)var1.getFirst()).getBlockPos().down();

      for (BlockPos var8 : BlockPos.iterateOutwards(var6, 3, 1, 3)) {
         BlockPos var9 = var8.toImmutable();
         if (this.zI(var9)) {
            int var10000 = !this.Ah(var9) ? 40110 : 40109;

            while (true) {
               int var13 = var10000;
               if (var13 == 40109) {
                  break;
               }

               if (var13 != 40110) {
                  AliceObf_aG var10 = this.AU(var9, var2, var1);
                  if (var10 != null) {
                     double var11 = var10.FY.dm + (!var9.equals(this.ahS) ? 0.0 : 20.0);
                     if (var3 == null || var11 > var4) {
                        var3 = var10;
                        var4 = var11;
                     }
                  }
                  break;
               }

            }
         }
      }

      return var3;
   }

   private BlockPos zz(PlayerEntity var1) {
      KnockbackDirection var2 = (KnockbackDirection)Modules.get().get(KnockbackDirection.class);
      if (var2 != null && var2.isActive() && this.mc.world != null) {
         Setting var3 = var2.settings.get("destination");
         Setting var4 = var2.settings.get("base-range");
         if (var3 != null && var3.get() == AliceObf_bv.aMp && var4 != null && var4.get() instanceof Double var5) {
            int var22 = (int)Math.ceil(var5);
            BlockPos var7 = var1.getBlockPos();
            BlockPos var8 = null;
            double var9 = Double.NEGATIVE_INFINITY;

            for (BlockPos var12 : BlockPos.iterate(var7.add(-var22, -2, -var22), var7.add(var22, 0, var22))) {
               if (this.mc.world.isChunkLoaded(var12.getX() >> 4, var12.getZ() >> 4)) {
                  BlockState var13 = this.mc.world.getBlockState(var12);
                  if (var13.isOf(Blocks.OBSIDIAN)) {

                  } else {
                     short var23 = 21393;
                  }

                  if (this.mc.world.isAir(var12.up()) && !(var12.getY() + 1 > var1.getY() + 0.1)) {
                     Vec3d var14 = new Vec3d(var12.getX() + 0.5, var12.getY() + 1, var12.getZ() + 0.5);
                     double var15 = var1.getEntityPos().squaredDistanceTo(var14);
                     if (!(var15 > var5 * var5)) {
                        Box var17 = new Box(
                           var12.getX(),
                           var12.getY() + 1,
                           var12.getZ(),
                           var12.getX() + 1,
                           var12.getY() + 3,
                           var12.getZ() + 1
                        );
                        if (this.mc.world.getOtherEntities(null, var17, var0 -> !var0.isRemoved() && !(var0 instanceof EndCrystalEntity)).isEmpty()) {
                           Vec3d var18 = new Vec3d(var14.x - var1.getX(), 0.0, var14.z - var1.getZ());
                           if (!(var18.lengthSquared() < 0.0625)) {
                              int var10001 = !this.mc
                                    .world
                                    .getBlockCollisions(var1, var1.getBoundingBox().contract(0.01).stretch(var18))
                                    .iterator()
                                    .hasNext()
                                 ? 42519
                                 : 42518;

                              while (true) {
                                 int var21 = var10001;
                                 if (var21 == 42518) {
                                    break;
                                 }

                                 if (var21 != 42519) {
                                    double var19 = AliceObf_sx.YX(
                                       DamageUtils.crystalDamage(var1, var14), DamageUtils.crystalDamage(this.mc.player, var14), var15
                                    );
                                    if (var19 > var9) {
                                       var9 = var19;
                                       var8 = var12.toImmutable();
                                    }
                                    break;
                                 }

                              }
                           }
                        }
                     }
                  }
               }
            }

            return var8;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private Vec3d zA(PlayerEntity var1) {
      Vec3d var2 = var1.getTrackedPosition().getPos();
      return var2 != null && Double.isFinite(var2.x) && Double.isFinite(var2.y) && Double.isFinite(var2.z)
         ? var2
         : var1.getEntityPos();
   }

   private int zB() {
      return MathHelper.ceil(this.zV() * 2 / 50.0);
   }

   private int zC() {
      return Math.max((Integer)this.aeK.get(), this.zB() + 4);
   }

   private int zD() {
      if (!this.Nv.EA()) {
         return this.tn.get() ? 1 : this.zB() + 1;
      } else {
         return 0;
      }
   }

   private void zE(PlayerEntity var1, Vec3d var2) {
      double[] var3 = this.Nv.Et(this.afe, this.zD());
      this.xx = var2.add(var3[0], var3[1], var3[2]);
   }

   private Vec3d zF(PlayerEntity var1) {
      AliceObf_oE var2 = this.Nv.Ez();
      if (!(var2 != null)) {
         short var3 = 10230;
      }

      return null;
   }

   private float zG(float var1) {
      AliceObf_oE var2 = this.Nv.Ez();
      if (var2 == null) {
         return var1;
      } else {
         int var3 = this.afe - var2.Ek() + this.zD();
         return var3 >= 10 ? var1 : Math.max(0.0F, var1 - this.EB);
      }
   }

   private void zH() {
      if (this.du.EK() && this.isActive() && this.aJm == this.mc.world && this.AE()) {
         List var1 = this.AF();
         if (!var1.isEmpty()) {
            AliceObf_aG var2 = this.Ai(var1);
            if (var2 != null && this.Ce(var2.ayi)) {
               AliceObf_fS.Pi(this, () -> this.AK(var2, false));
            }
         }
      }
   }

   private boolean zI(BlockPos var1) {
      return var1 != null && this.mc.world != null ? this.mc.world.getOtherEntities(null, CJ(var1)).isEmpty() : false;
   }

   @AliceHandler(
      priority = 100
   )
   private void zJ(PlayerTickMovementEvent var1) {
      if (!AliceObf_fS.OA(this)) {
         if (!AliceObf_fS.OD(this)) {
            this.suspendForItemUse();
         }
      } else {
         this.bX = false;
         this.maintainLookHold();
         this.BY();
         this.Ct();
         this.WR.QT();
      }
   }

   private void maintainLookHold() {
      if (this.aDB == null) {
         if ((Boolean)this.aNq.get()) {

         } else {
            char var1 = 54670;
         }

         this.Ca(true);
      }
   }

      private boolean zK() {
      if (!this.isActive() || this.mc.player == null || this.mc.world == null || this.mc.getNetworkHandler() == null || !this.AE()) {
         return false;
      }
      return !this.AF().isEmpty();
   }

   private void zL() {
      BlockPos var1 = this.zN();
      if (var1 == null) {
         var1 = this.AM();
      }

      if (!(var1 != null)) {
         char var3 = 63602;
      }

      if (this.zO() || this.azF) {
         this.BX();
      }
   }

   private void zM(BlockPos var1) {
      this.eG = var1 == null ? null : var1.toImmutable();
   }

   private BlockPos zN() {
      return this.eG;
   }

   private boolean zO() {
      BlockPos var1 = this.AM();
      if (var1 != null
         && CU(this.mc.player.getEyePos(), new Box(var1)) <= (Double)this.acb.get() * (Double)this.acb.get()
         && this.mc.world.isAir(var1.up())) {
         return true;
      } else {
         for (EndCrystalEntity var3 : this.zP()) {
            if (var3.isAlive() && this.Aa(var3)) {
               if (this.zW(var3.getId())) {

               } else {
                  short var4 = 29999;
               }

               if (this.Br(var3.getBoundingBox())) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private List<EndCrystalEntity> zP() {
      if (!this.bX) {
         this.Da.clear();
         if (this.mc.world != null) {
            for (Entity var2 : this.mc.world.getEntities()) {
               if (var2 instanceof EndCrystalEntity var3 && var3.isAlive()) {
                  this.Da.add(var3);
               }
            }
         }

         this.bX = true;
      }

      return this.Da;
   }

   @AliceHandler(
      priority = -200
   )
   private void zQ(Post var1) {
      int var3;
      label54: {
         this.du.EI();
         if ((Boolean)this.aNq.get()) {
            var3 = !this.zK() ? 16416 : 16417;

            while (true) {
               int var2 = var3;
               if (var2 == 16416) {
                  break;
               }

               if (var2 != 16417) {
                  var3 = 0;
                  break label54;
               }

               var3 = 16415;
            }
         }

         var3 = 1;
      }

      this.Ca((var3 != 0));
      this.Zx.Rk();
      this.WR.QU();
   }

   private void zR() {
      if (this.du.EL()) {
         AliceObf_lq var1 = this.aIP;
         this.aIP = null;
         if (var1 != null && this.isActive() && this.aJm == this.mc.world && this.AE()) {
            if (this.AF().contains(var1.yn().FY.aqF)) {
               if (this.Ce(var1.yp())) {
                  AliceObf_fS.Pi(
                     this, () -> this.AK(var1.yn(), var1.yo())
                  );
               }
            }
         }
      }
   }

   @AliceHandler
   private void zS(Receive var1) {
      if (var1.packet instanceof EntityDamageS2CPacket var2) {
         AliceObf_oE var9 = this.BI;
         if (var9 != null && var9.Ei() == var2.entityId() && var9.Ej() == var2.sourceCauseId() && var9.Ej() == var2.sourceDirectId()) {
            this.aFn.set(new AliceObf_no(var9, var2.entityId(), var2.sourceCauseId(), var2.sourceDirectId()));
         }
      }

      Packet var10 = var1.packet;
      if (!(var10 instanceof EntityVelocityUpdateS2CPacket)) {

      } else {
         short var15 = 17397;
      }

      if (!(var1.packet instanceof ExplosionS2CPacket var6)) {
         if (!(var1.packet instanceof PlaySoundS2CPacket var7)) {
            if (var1.packet instanceof EntityStatusS2CPacket var8) {
               if (var8.getStatus() == 35) {
                  if (var8.getEntity(this.mc.world) instanceof PlayerEntity var4) {
                     if (var4 != this.mc.player) {
                        if (Friends.get().shouldAttack(var4)) {
                           this.aAg.put(var4.getId(), System.currentTimeMillis());
                        }
                     }
                  }
               }
            }
         } else {
            if ((Boolean)this.zl.get() && (Boolean)this.anb.get() && this.TP.size() < 64) {
               int var10001 = var7.getCategory() == SoundCategory.BLOCKS ? 57904 : 57905;

               while (true) {
                  int var5 = var10001;
                  if (var5 == 57904) {
                     if (((SoundEvent)var7.getSound().value()).equals(SoundEvents.ENTITY_GENERIC_EXPLODE.value())) {
                        this.TP.add(new Vec3d(var7.getX(), var7.getY(), var7.getZ()));
                        return;
                     }
                     break;
                  }

                  if (var5 != 57905) {
                     break;
                  }

               }
            }
         }
      } else {
         if ((Boolean)this.zl.get() && this.TP.size() < 64) {
            this.TP.add(var6.center());
         }
      }
   }

   private void zT() {
      if (!this.TP.isEmpty()) {
         if (this.mc.world != null && (Boolean)this.zl.get()) {
            long var1 = System.currentTimeMillis();
            if (this.TP.poll() != null) {

            } else {
               char var6 = 38015;
            }
         } else {
            this.TP.clear();
         }
      }
   }

   private long zU() {
      return Math.max(50L, this.zV() * 2L);
   }

   private int zV() {
      if (this.mc.getNetworkHandler() != null) {
         if (!(this.mc.player == null)) {
            char var2 = 65027;
         }

         PlayerListEntry var1 = this.mc.getNetworkHandler().getPlayerListEntry(this.mc.player.getUuid());
         return var1 == null ? 100 : Math.max(0, var1.getLatency());
      } else {
         return 100;
      }
   }

   private boolean zW(int var1) {
      Long var2 = this.Dm.get(var1);
      if (!(var2 == null)) {
         char var3 = 42804;
      }

      return System.currentTimeMillis() - var2 <= this.zU();
   }

   @AliceHandler(
      priority = 200
   )
   private void zX(EntityAddedEvent var1) {
      if (this.mc.player != null && this.mc.world != null && this.aJm == this.mc.world) {
         this.bX = false;
         if (var1.entity.getId() > this.alx) {
            this.alx = var1.entity.getId();
         }

         this.BP();
         if (var1.entity instanceof EndCrystalEntity var2) {
            BlockPos var11 = var2.getBlockPos().down();
            this.ny.put(var2.getId(), var11);
            if (this.zZ(var2)) {
               this.auc.put(var2.getId(), this.afe);
            }

            if (this.Nv.EA()
               && this.amW != null
               && CrystalAuraPlusPlus.CL(var2, this.amW)) {
               this.amW = null;
               this.Nv.Ey(this.afe, this.zC());
            }

            AliceObf_vd var4 = this.Ab(var2);
            if (var4 != null) {
               for (int var8 : var4.yG()) {
                  if (var8 != var2.getId()) {
                     this.azw.remove(var8);
                  }
               }

               if (BU(var4.yG(), var2.getId())) {
                  if (this.Nv.EA() && this.Cq != null && CL(var2, this.Cq)) {
                     this.amW = null;
                     this.LJ = var2.getId();
                  }

                  return;
               }
            }

            int var10000 = !this.Co() ? 21089 : 21090;

            while (true) {
               int var9 = var10000;
               if (var9 == 21089) {
                  return;
               }

               if (var9 != 21090) {
                  if (this.AE() && (Boolean)this.su.get() && this.Aa(var2) && this.CD(AliceObf_up.aOU, var11) && this.Bq(var2)) {
                     List var12 = this.AF();
                     var10000 = !var12.isEmpty() ? 281 : 282;

                     while (true) {
                        int var10 = var10000;
                        if (var10 == 281) {
                           AliceObf_sR var13 = this.AH(var2, var12);
                           if (var13 != null) {
                              char var15 = 42570;
                           } else {
                              char var16 = 42571;
                           }
                           break;
                        }

                        if (var10 != 282) {
                           break;
                        }

                     }
                  }

                  return;
               }

            }
         }
      }
   }

   private void zY(BlockPos var1) {
      if (this.aeI.size() >= 16) {
         this.aeI.remove(0);
      }

      this.aeI.add(new AliceObf_dB(var1.toImmutable(), this.afe));
   }

   private boolean zZ(EndCrystalEntity var1) {
      for (AliceObf_dB var3 : this.aeI) {
         if (this.afe - var3.aG <= 40) {
            int var10000 = CrystalAuraPlusPlus.CL(var1, var3.aHX)
               ? 11769
               : 11770;

            while (true) {
               int var4 = var10000;
               if (var4 == 11769) {
                  return true;
               }

               if (var4 != 11770) {
                  break;
               }

            }
         }
      }

      return false;
   }

   private boolean Aa(EndCrystalEntity var1) {
      return !(Boolean)this.Lu.get() || this.auc.containsKey(var1.getId());
   }

   private AliceObf_vd Ab(EndCrystalEntity var1) {
      for (int var2 = 0; var2 < this.qJ.size(); var2++) {
         AliceObf_vd var3 = this.qJ.get(var2);
         int var10000 = CrystalAuraPlusPlus.CL(var1, var3.WE)
            ? 22115
            : 22116;

         while (true) {
            int var4 = var10000;
            if (var4 == 22115) {
               return this.qJ.remove(var2);
            }

            if (var4 != 22116) {
               break;
            }

         }
      }

      return null;
   }

   @AliceHandler
   private void Ac(EntityRemovedEvent var1) {
      this.bX = false;
      this.BR();
      if (var1.entity instanceof EndCrystalEntity) {
         int var2 = var1.entity.getId();
         BlockPos var3 = this.ny.get(var2);
         if (var3 == null) {
            var3 = var1.entity.getBlockPos().down();
         }

         if (var2 == this.LJ && this.Nv.EA() && this.Cq != null && this.Cq.equals(var3)) {
            this.LJ = -1;
            this.Wj = var3.toImmutable();
            this.Nv.Ey(this.afe, this.zC());
         }

         this.azw.remove(var2);
         this.Dm.remove(var2);
         this.ny.remove(var2);
         this.auc.remove(var1.entity.getId());
         if (this.Wj != null && this.Nv.EA()) {
            this.zH();
         }
      }
   }

   @AliceHandler(
      priority = -1000
   )
   private void Ad(Sent var1) {
      if (var1.packet instanceof ClientTickEndC2SPacket) {
         this.du.EJ();
      }

      if (var1.packet instanceof PlayerMoveC2SPacket) {
         this.du.EH();
      }

      if (!(var1.packet instanceof PlayerInteractBlockC2SPacket var2)) {
         if (var1.packet instanceof PlayerMoveC2SPacket var4 && var4.changesLook()) {
            this.fp = var4.getYaw(this.fp);
            this.WI = var4.getPitch(this.WI);
            this.azF = true;
            if (this.aBl != null && this.aBl.aza) {
               AliceObf_fS.Oz(this, this.fp, this.WI);
            }
         }
      } else {
         if (this.mc.player != null && this.mc.player.getStackInHand(var2.getHand()).isOf(Items.END_CRYSTAL)) {
            this.zY(var2.getBlockHitResult().getBlockPos());
         }
      }
   }

   public AliceObf_y prepareCombatIntent() {
      this.EQ = false;
      this.du.EG();
      this.iE = null;
      this.Ca(false);
      this.aDB = null;
      this.aIP = null;
      this.aEc = false;
      this.AP = null;
      if (this.alB.get() != AliceObf_jY.Ab && AliceObf_fS.Oy(this)) {
         return this.Au();
      } else {
         if (!this.AE()) {

         } else {
            char var12 = 62839;
         }

         List var1 = this.AF();
         if (var1.isEmpty()) {
            this.Zx.Rn();
            this.zM(null);
            this.Ca(true);
            this.Bc();
            return AliceObf_fS.Oy(this) ? this.Au() : null;
         } else if (this.Nv.EA() && this.aDq != null && this.aDq.ajH.equals(this.Cq) && !this.Ag(this.Cq)) {
            this.EQ = true;
            this.zM(this.Cq);
            return AliceObf_fS.Oy(this) ? this.Au() : null;
         } else {
            if (this.Nv.EA()) {

            } else {
               short var13 = 560;
            }

            if (this.Nv.EA()) {
               int var10002 = this.Cq != null ? 21054 : 21055;

               while (true) {
                  int var6 = var10002;
                  if (var6 == 21054) {
                     if (this.amW != null && this.amW.equals(this.Cq)) {
                        this.EQ = true;
                        this.zM(this.Cq);
                        return AliceObf_fS.Oy(this)
                           ? this.Au()
                           : null;
                     }
                     break;
                  }

                  if (var6 != 21055) {
                     break;
                  }

               }
            }

            AliceObf_aG var2 = this.Ai(var1);
            if (var2 != null) {
               this.EQ = true;
               this.zM(var2.aQN);
               return this.At(var2, false);
            } else {
               this.EQ = false;
               AliceObf_aG var3 = this.Af(var1);
               int var10003 = var3 != null ? 7760 : 7761;

               while (true) {
                  int var7 = var10003;
                  if (var7 == 7760) {
                     this.EQ = true;
                     return this.At(var3, false);
                  }

                  if (var7 != 7761) {
                     if ((Boolean)this.su.get()) {
                        AliceObf_sR var4 = this.AG(var1);
                        if (var4 != null) {
                           this.zM(var4.II);
                           this.EQ = this.EQ | this.Bm(var4.ajn) != Integer.MIN_VALUE;
                        }

                        if (var4 != null && this.CD(AliceObf_up.aOU, var4.II)) {
                           if (this.Bm(var4.ajn) == Integer.MIN_VALUE) {
                              char var14 = 47402;
                           } else {
                              char var15 = 47401;
                           }
                        }
                     }

                     if ((Boolean)this.ayK.get()) {
                        AliceObf_lu var10 = this.aDq;
                        if (!this.Cp() && var10 != null) {
                           if (this.afe > var10.ca) {
                              AliceObf_aG var5 = this.AX(
                                 var10.ajH, var1
                              );
                              if (var5 != null && this.AO(Items.END_CRYSTAL)) {
                                 this.EQ = true;
                                 this.zM(var5.aQN);
                                 if (!this.CD(AliceObf_up.wD, var5.aQN)) {
                                    return AliceObf_fS.Oy(this) ? this.Au() : null;
                                 }

                                 return this.At(var5, true);
                              }
                           }

                           this.aDq = null;
                        }

                        this.Fb = null;
                        Object var11 = this.AT(var1, false);
                        if (var11 == null && (Boolean)this.xM.get()) {
                           var11 = this.AT(var1, true);
                        }

                        if (var11 != null) {
                           this.zM(((AliceObf_aG)var11).aQN);
                        }

                        if (var11 != null
                           && this.AN((AliceObf_aG)var11)) {
                           this.EQ = true;
                           if (!this.CD(AliceObf_up.wD, ((AliceObf_aG)var11).aQN)) {
                              return AliceObf_fS.Oy(this)
                                 ? this.Au()
                                 : null;
                           }

                           if (this.Cq() || this.Cs()) {
                              this.Iq = ((AliceObf_aG)var11).aQN;
                           }

                           return this.At((AliceObf_aG)var11, false);
                        }

                        return AliceObf_fS.Oy(this)
                           ? this.Au()
                           : null;
                     }

                     this.Zx.Rn();
                     this.Bc();
                     return !AliceObf_fS.Oy(this)
                        ? null
                        : this.Au();
                  }

               }
            }
         }
      }
   }

   public int combatPriority() {
      return 100;
   }

   public boolean canYieldAfterExecution() {
      return this.aDB == null && this.aIP == null;
   }

   public boolean usesExclusiveCombatTurn() {
      return false;
   }

   public boolean sharesParallelCombatEpoch() {
      return true;
   }

   public boolean hasExclusiveCombatWork() {
      return this.EQ;
   }

   boolean isIdleForAnchorFallback() {
      if (this.isActive() && this.AE() && this.mc.player != null && this.mc.world != null) {
         if (this.aDB == null && this.aIP == null) {
            if (!(this.aDq == null)) {
               char var3 = 62770;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean Ae(List<PlayerEntity> var1) {
      if ((Boolean)this.su.get() && this.AG(var1) != null) {
         return true;
      } else {
         if ((Boolean)this.ayK.get()) {
            if (this.AT(var1, false) != null) {
               return true;
            }

            if ((Boolean)this.xM.get() && this.AT(var1, true) != null) {
               return true;
            }
         }

         return false;
      }
   }

   private AliceObf_aG Af(List<PlayerEntity> var1) {
      if (!(this.CE != null)) {
         char var3 = 52474;
      }

      return null;
   }

   private boolean Ag(BlockPos var1) {
      if (var1 != null && this.mc.world != null) {
         BlockState var2 = this.mc.world.getBlockState(var1);
         if (!var2.isOf(Blocks.OBSIDIAN)) {

         } else {
            char var3 = 53492;
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean Ah(BlockPos var1) {
      if (!(var1 == null)) {
         char var4 = 48198;
      }

      for (AliceObf_vd var3 : this.qJ) {
         if (var1.equals(var3.WE)) {
            return true;
         }
      }

      return false;
   }

   private AliceObf_aG Ai(List<PlayerEntity> var1) {
      if (this.Nv.EA()) {
         if (!(this.Wj != null)) {
            char var3 = 59200;
         }
      }

      return null;
   }

   static void Aj() {
      CrystalAuraPlusPlus var0 = EU;
      int var10000 = var0 != null ? 17291 : 17292;

      while (true) {
         int var1 = var10000;
         if (var1 == 17291) {
            var0.zR();
            var0.Ak();
            var0.Am();
            break;
         }

         if (var1 != 17292) {
            break;
         }

      }
   }

   private void Ak() {
      if (this.du.EK()) {
         EndCrystalEntity var1 = this.iE;
         this.iE = null;
         if (var1 != null
            && this.isActive()
            && this.aJm == this.mc.world
            && this.AE()
            && (Boolean)this.su.get()
            && this.Co()
            && this.mc.world.getEntityById(var1.getId()) == var1
            && this.Bq(var1)
            && this.Aa(var1)) {
            List var2 = this.AF();
            if (!var2.isEmpty()) {
               AliceObf_sR var3 = this.AH(var1, var2);
               if (var3 != null) {
                  AliceObf_fS.Pi(this, () -> this.BV(var3));
               }
            }
         }
      }
   }

   static boolean Al(int var0) {
      CrystalAuraPlusPlus var1 = EU;
      return var1 != null && var1.isActive() && var1.auc.containsKey(var0);
   }

   private void Am() {
      if (this.Co()) {
         if (this.isActive() && this.aJm == this.mc.world && this.AE()) {
            if (this.CE != null && !this.Cr()) {
               List var1 = this.AF();
               if (!var1.isEmpty()) {
                  AliceObf_aG var2 = this.Af(var1);
                  if (var2 != null && this.Ce(var2.ayi)) {
                     AliceObf_fS.Pi(this, () -> this.AK(var2, false));
                  }
               }
            }
         }
      }
   }

   private void An(AliceObf_sR var1, AliceObf_rn var2) {
      var2.anU.EN(this.afe);
      if (!this.Cr() && (Boolean)this.ayK.get() && !this.Nv.EA()) {
         this.CE = var1.II.toImmutable();
         this.DI = this.afe;
      }
   }

   private void Ao() {
      if (this.Co()) {
         if (!this.Cr() && !this.Nv.EA() && this.isActive()) {
            if (!(this.aJm == this.mc.world)) {
               short var3 = 11955;
            }
         }
      }
   }

   public boolean canFollowEmergencyOffhandSwap() {
      return true;
   }

   private AliceObf_y Ap(List<AliceObf_sR> var1, List<AliceObf_aG> var2) {
      return new AliceObf_td(this, var2, var1);
   }

   private void Aq(List<AliceObf_sR> var1) {
      for (AliceObf_sR var3 : var1) {
         if (var3.ajn.isAlive()) {
            int var10000 = this.zW(var3.ajn.getId()) ? 53956 : 53957;

            while (true) {
               int var4 = var10000;
               if (var4 == 53956) {
                  break;
               }

               if (var4 != 53957) {
                  this.AJ(var3);
                  break;
               }

            }
         }
      }
   }

   private void Ar(List<AliceObf_aG> var1) {
      for (AliceObf_aG var3 : var1) {
         if (!this.AN(var3)) {

         } else {
            char var4 = 59127;
         }

         this.Iq = var3.aQN;
         this.AK(var3, false);
         if (this.aIP != null) {
            return;
         }
      }
   }

   private AliceObf_y As(AliceObf_sR var1) {
      return new AliceObf_ad(this, var1);
   }

   private AliceObf_y At(AliceObf_aG var1, boolean var2) {
      return new AliceObf_tP(this, var1, var2);
   }

   private AliceObf_y Au() {
      return new AliceObf_kx(this);
   }

   @AliceHandler
   private void Av(Render3DEvent var1) {
      if ((Boolean)this.atY.get() && this.mc.player != null && this.mc.world != null) {
         if ((Boolean)this.rc.get()) {
            this.Aw(var1);
         }

         if ((Boolean)this.akE.get()) {
            for (AliceObf_rD var3 : this.ayt) {
               if (!this.AB(var3.abZ) && this.AA(var3.abZ.aeY)) {
                  double var4 = this.AC(var3, var1.tickDelta);
                  if (var4 > 0.0) {
                     this.Ay(var1, var3.abZ, false, var4);
                  }
               }
            }
         }

         AliceObf_bP var7 = this.AP;
         if (var7 != null && !this.AB(var7) && this.AA(var7.aeY)) {
            this.Ay(var1, var7, false, 1.0);
         }

         if ((Boolean)this.ur.get()) {
            AliceObf_lu var8 = this.aDq;
            int var10000 = var8 != null ? 46736 : 46737;

            while (true) {
               int var6 = var10000;
               if (var6 == 46736) {
                  this.Ay(var1, var8.aCc, true, 1.0);
                  break;
               }

               if (var6 != 46737) {
                  break;
               }

            }

            for (AliceObf_vd var5 : this.qJ) {
               this.Ay(var1, var5.Fj, true, 1.0);
            }

            for (AliceObf_rn var11 : this.azw.values()) {
               if (var11.ayC != null) {
                  this.Ay(var1, var11.ayC, true, 1.0);
               }
            }
         }
      }
   }

   private void Aw(Render3DEvent var1) {
      double var2 = (Double)this.hG.get() * (Double)this.hG.get();

      for (Entity var5 : this.mc.world.getEntities()) {
         if (var5 instanceof EndCrystalEntity var6 && var6.isAlive() && !(this.mc.player.squaredDistanceTo(var6) > var2)) {
            WireframeEntityRenderer.render(var1, var6, (Double)this.JJ.get(), (Color)this.HB.get(), (Color)this.ev.get(), (ShapeMode)this.aMm.get());
         }
      }
   }

   @AliceHandler
   private void Ax(Render2DEvent var1) {
      if ((Boolean)this.atY.get() && (Boolean)this.aOP.get() && this.mc.player != null && this.mc.world != null) {
         if ((Boolean)this.akE.get()) {
            for (AliceObf_rD var3 : this.ayt) {
               if (!this.AB(var3.abZ) && this.AA(var3.abZ.aeY)) {
                  double var4 = this.AC(var3, var1.tickDelta);
                  if (var4 > 0.0) {
                     this.Az(var3.abZ, var4);
                  }
               }
            }
         }

         AliceObf_bP var7 = this.AP;
         if (var7 != null && !this.AB(var7) && this.AA(var7.aeY)) {
            this.Az(var7, 1.0);
         }

         if ((Boolean)this.ur.get()) {
            AliceObf_lu var8 = this.aDq;
            if (var8 != null) {
               this.Az(var8.aCc, 1.0);
            }

            Iterator var9 = this.qJ.iterator();

            while (true) {
               int var10000 = var9.hasNext() ? 59605 : 59606;

               while (true) {
                  int var6 = var10000;
                  if (var6 == 59605) {
                     AliceObf_vd var11 = (AliceObf_vd)var9.next();
                     this.Az(var11.Fj, 1.0);
                     break;
                  }

                  if (var6 != 59606) {
                     for (AliceObf_rn var5 : this.azw.values()) {
                        if (var5.ayC != null) {
                           this.Az(var5.ayC, 1.0);
                        }
                     }

                     return;
                  }

               }
            }
         }
      }
   }

   private void Ay(Render3DEvent var1, AliceObf_bP var2, boolean var3, double var4) {
      SettingColor var6 = var3 ? (SettingColor)this.aoU.get() : (var2.aeY == AliceObf_kC.aFj ? (SettingColor)this.aDC.get() : (SettingColor)this.vk.get());
      SettingColor var7 = !var3 ? (var2.aeY == AliceObf_kC.aFj ? (SettingColor)this.aPR.get() : (SettingColor)this.tx.get()) : (SettingColor)this.ow.get();
      var1.renderer
         .box(
            var2.UH,
            CrystalAuraPlusPlus.AD(var6, var4),
            AD(var7, var4),
            (ShapeMode)this.hb.get(),
            0
         );
   }

   private void Az(AliceObf_bP var1, double var2) {
      this.aec.set(var1.uA.x, var1.uA.y, var1.uA.z);
      if (NametagUtils.to2D(this.aec, (Double)this.aws.get())) {
         String var4 = var1.vY;
         TextRenderer var5 = TextRenderer.get();
         if (var5 != null) {
            NametagUtils.begin(this.aec);
            var5.begin(1.0, false, true);
            double var6 = var5.getWidth(var4, true);
            double var8 = var5.getHeight(true);
            var5.render(
               var4,
               -var6 / 2.0,
               -var8 / 2.0,
               CrystalAuraPlusPlus.AD((Color)this.anL.get(), var2),
               true
            );
            var5.end();
            NametagUtils.end();
         }
      }
   }

   private boolean AA(AliceObf_kC var1) {
      return var1 == AliceObf_kC.aFj ? (Boolean)this.aOR.get() : (Boolean)this.QC.get();
   }

   private boolean AB(AliceObf_bP var1) {
      int var10000 = var1 == null ? 46248 : 46249;

      while (true) {
         int var5 = var10000;
         if (var5 == 46248) {
            return false;
         }

         if (var5 != 46249) {
            AliceObf_lu var2 = this.aDq;
            if (var2 != null && var2.aCc == var1) {
               return true;
            }

            for (AliceObf_vd var4 : this.qJ) {
               if (var4.Fj == var1) {
                  return true;
               }
            }

            for (AliceObf_rn var7 : this.azw.values()) {
               if (var7.ayC == var1) {
                  return true;
               }
            }

            return false;
         }

      }
   }

   private double AC(AliceObf_rD var1, float var2) {
      double var3 = Math.max(0.0, (double)(this.afe - var1.wh + var2));
      return MathHelper.clamp(1.0 - var3 / ((Integer)this.aHu.get()).intValue(), 0.0, 1.0);
   }

   private static Color AD(Color var0, double var1) {
      return !(var1 >= 0.999) ? new Color(var0.r, var0.g, var0.b, MathHelper.clamp((int)Math.round(var0.a * var1), 0, 255)) : var0;
   }

   private boolean AE() {
      if (this.mc.player != null && this.mc.world != null) {
         int var10000 = this.mc.interactionManager != null ? 46353 : 46354;

         while (true) {
            int var1 = var10000;
            if (var1 == 46353) {
               if (this.mc.getNetworkHandler() != null
                  && this.mc.player.isAlive()
                  && !this.mc.player.isCreative()
                  && !this.mc.player.isSpectator()
                  && !this.mc.player.hasVehicle()
                  && !this.mc.player.isGliding()
                  && !this.mc.player.getAbilities().flying) {
                  if (CV(this.mc.player) < (Double)this.aMI.get()) {
                     return false;
                  }

                  if ((Boolean)this.DP.get()) {
                     if (this.mc.player.isUsingItem()) {
                        return false;
                     }

                     if (this.mc.currentScreen == null) {
                        var10000 = this.mc.options.useKey.isPressed() ? 17515 : 17516;

                        while (true) {
                           int var2 = var10000;
                           if (var2 == 17515) {
                              return false;
                           }

                           if (var2 != 17516) {
                              break;
                           }

                        }
                     }
                  }

                  if ((Boolean)this.Fi.get()) {
                     if (this.mc.currentScreen == null) {
                        short var4 = 32608;
                     } else {
                        short var5 = 32609;
                     }
                  }

                  return true;
               }
               break;
            }

            if (var1 != 46354) {
               break;
            }

         }
      }

      return false;
   }

   private List<PlayerEntity> AF() {
      if (this.zs()) {
         AliceObf_oE var1 = this.Nv.Ez();
         if (var1 != null) {
            PlayerEntity var2 = (PlayerEntity)var1.Eh();
            if (this.zv(var2)) {
               return List.of(var2);
            }
         }
      }

      double var6 = (Double)this.aHF.get() * (Double)this.aHF.get();
      ArrayList var3 = new ArrayList();

      for (PlayerEntity var5 : this.mc.world.getPlayers()) {
         if (var5 != this.mc.player
            && var5.isAlive()
            && !var5.isCreative()
            && !var5.isSpectator()
            && Friends.get().shouldAttack(var5)
            && !(this.mc.player.squaredDistanceTo(var5) > var6)) {
            var3.add(var5);
         }
      }

      var3.sort(Comparator.<PlayerEntity>comparingDouble(var1x -> this.mc.player.squaredDistanceTo(var1x)).thenComparingInt(Entity::getId));
      return var3.size() <= this.Iu.get() ? var3 : new ArrayList<>(var3.subList(0, (Integer)this.Iu.get()));
   }

   private AliceObf_sR AG(List<PlayerEntity> var1) {
      AliceObf_sR var2 = null;

      for (EndCrystalEntity var4 : this.zP()) {
         AliceObf_sR var5 = this.AH(var4, var1);
         if (var5 != null) {
            if (var2 == null) {
               var2 = var5;
            } else {
               double var6 = var5.aLu.yj() - var2.aLu.yj();
               if (Math.abs(var6) > 0.5) {
                  if (var6 > 0.0) {
                     var2 = var5;
                  }
               } else if (var5.aIj > var2.aIj
                  || CrystalAuraPlusPlus.CB(var5.aIj, var2.aIj)
                     && var5.ajn.getId() < var2.ajn.getId()) {
                  var2 = var5;
               }
            }
         }
      }

      return var2;
   }

   private AliceObf_sR AH(EndCrystalEntity var1, List<PlayerEntity> var2) {
      if (!this.zs()
         || !this.Nv.EA()
         || this.Cq != null && CrystalAuraPlusPlus.CL(var1, this.Cq)) {
         if (!var1.isAlive() || !this.Bq(var1)) {
            return null;
         } else if (!this.Aa(var1)) {
            return null;
         } else if (this.zW(var1.getId())) {
            return null;
         } else {
            AliceObf_rn var3 = this.azw.get(var1.getId());
            if (var3 != null) {
               if (var3.ahr >= (Integer)this.aN.get()) {
                  return null;
               }

               if (this.afe - var3.Gg < (Integer)this.aEn.get()) {
                  return null;
               }
            }

            BlockPos var4 = this.ny.get(var1.getId());
            if (var4 == null) {
               var4 = var1.getBlockPos().down();
            }

            Vec3d var5 = this.BH(var1, var4);
            if (var5 == null) {
               return null;
            } else if (!CrystalAuraPlusPlus.CS(
               this.mc.player.getEyePos(), var5, var1.getBoundingBox(), this.Bt()
            )) {
               return null;
            } else {
               AliceObf_lI var6 = this.Bn(
                  var1.getEntityPos(), null, var2
               );
               if (var6 == null) {
                  return null;
               } else {
                  double var7 = var6.yj();
                  return new AliceObf_sR(var1, var4, var5, var6, var7);
               }
            }
         }
      } else {
         return null;
      }
   }

   private List<AliceObf_sR> AI(AliceObf_sR var1, List<PlayerEntity> var2) {
      ArrayList<AliceObf_sR> var3 = new ArrayList<>();
      var3.add(var1);
      int var4 = (Integer)this.zd.get();
      if (var4 <= 1) {
         return var3;
      } else {
         double var5 = (Double)this.aQq.get();
         if ((Boolean)this.aji.get()) {

         } else {
            char var14 = 62657;
         }

         double var7 = var5 - var1.aLu.aNU;
         if (var7 <= 0.0) {
            return var3;
         } else {
            ArrayList var9 = new ArrayList();

            for (EndCrystalEntity var11 : this.zP()) {
               if (var11.getId() != var1.ajn.getId()) {
                  AliceObf_sR var12 = this.AH(var11, var2);
                  if (var12 != null && this.CD(AliceObf_up.aOU, var12.II) && this.Bm(var12.ajn) != Integer.MIN_VALUE) {
                     var9.add(var12);
                  }
               }
            }

            var9.sort(Comparator.<AliceObf_sR>comparingDouble(var0 -> -var0.aLu.yj()).thenComparingInt(var0 -> var0.ajn.getId()));
            Iterator var13 = var9.iterator();
            if (var13.hasNext()) {

            } else {
               char var15 = 44285;
            }

            return var3;
         }
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void AJ(AliceObf_sR var1) {
      if (this.du.EK()) {
         int var2 = this.Bm(var1.ajn);
         if (var2 != Integer.MIN_VALUE) {
            if (this.Bg(var2)) {
               if (this.Bq(var1.ajn) && this.Bo(var1.ajn.getEntityPos(), null, var1.aLu.aqF)) {
                  this.AP = CrystalAuraPlusPlus.Cm(var1);
                  double[] var3 = CrystalAuraPlusPlus.CT(var1.aEy);
                  if (this.BM(var1.ajn, var3[0], var3[1])) {
                     if (this.Cg(AliceObf_up.aOU, var1.II)) {
                        int var4 = AliceObf_fS.PD();
                        boolean var7 = false /* VF: Semaphore variable */;

                        try {
                           var7 = true;
                           this.mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(var1.ajn, this.mc.player.isSneaking()));
                           var7 = false;
                        } finally {
                           if (var7) {
                              AliceObf_fS.OG(this);
                           }
                        }

                        AliceObf_fS.OG(this);
                        if (AliceObf_fS.PD() != var4) {
                           this.mc.player.swingHand(Hand.MAIN_HAND);
                           this.CE(AliceObf_up.aOU, var1.II);
                           this.AQ(var1.II);
                           AliceObf_rn var5 = this.azw.computeIfAbsent(var1.ajn.getId(), var0 -> new AliceObf_rn());
                           var5.ayC = this.AP;
                           var5.ahr++;
                           var5.Gg = this.afe;
                           this.An(var1, var5);
                           if (this.Nv.EA() && this.Cq != null && this.Cq.equals(var1.II)) {
                              this.LJ = var1.ajn.getId();
                              this.Wj = null;
                           }

                           this.Cj(this.AP);
                           this.Ao();
                        }
                     }
                  }
               } else {
                  this.Zx.Rn();
               }
            }
         } else {
            this.Zx.Rn();
            this.Bi();
         }
      }
   }

   private void AK(AliceObf_aG var1, boolean var2) {
      if (this.du.EK()) {
         this.AL(var1.aQN);
         if (var1.auH) {
            this.AZ(var1);
         } else if (this.AY(var1, var2)) {

         } else {
            short var3 = 7408;
         }
      }
   }

   private void AL(BlockPos var1) {
      this.aln = var1.toImmutable();
      this.Lq = this.afe;
   }

   private BlockPos AM() {
      if (this.aln == null) {
         return null;
      } else if (this.afe - this.Lq > 20) {
         this.aln = null;
         return null;
      } else {
         return this.aln;
      }
   }

   private boolean AN(AliceObf_aG var1) {
      return var1.auH ? this.AO(Items.OBSIDIAN) && this.AO(Items.END_CRYSTAL) : this.AO(Items.END_CRYSTAL);
   }

   private boolean AO(Item var1) {
      if (this.mc.player.getOffHandStack().isOf(var1)) {
         return true;
      } else {
         int var2 = this.mc.player.getInventory().getSelectedSlot();
         return this.alB.get() == AliceObf_jY.LN
            ? this.mc.player.getInventory().getStack(var2).isOf(var1)
            : InvUtils.find(new Item[]{var1}).found();
      }
   }

   private void AP(BlockPos var1) {
      if ((Boolean)this.bQ.get()) {
         AliceObf_vl var2 = this.yd.computeIfAbsent(var1, AliceObf_vl::new);
         var2.zd(System.currentTimeMillis());
         if (this.azO != null && this.azO.equals(var1)) {
            var2.oC++;
         } else {
            if (this.azO != null) {
               AliceObf_vl var3 = this.yd.get(this.azO);
               if (var3 != null) {
                  var3.oC = 0;
               }
            }

            var2.oC = 1;
            this.azO = var1;
         }
      }
   }

   private void AQ(BlockPos var1) {
      if ((Boolean)this.bQ.get() && var1 != null) {
         AliceObf_vl var2 = this.yd.computeIfAbsent(var1, AliceObf_vl::new);
         var2.ze(System.currentTimeMillis());
      }
   }

   private double AR(double var1, BlockPos var3) {
      if (!(Boolean)this.bQ.get()) {
         return var1;
      } else {
         AliceObf_vl var4 = this.yd.get(var3);
         if (var4 == null) {
            return var1;
         } else {
            long var5 = System.currentTimeMillis();
            int var7 = var4.zf(var5, 500L);
            int var8 = var4.zg(var5, 500L);
            double var9 = var7 * 0.1 + var8 * 0.1 + var4.oC * 0.05;
            return var1 - Math.min(0.5, var9);
         }
      }
   }

   private void AS() {
      if (!(Boolean)this.bQ.get()) {
         this.yd.clear();
         this.azO = null;
      } else {
         long var1 = System.currentTimeMillis();
         if (var1 - this.HT > 1000L) {
            this.yd.values().removeIf(var2 -> var1 - var2.zh() > 2000L);
            this.HT = var1;
         }
      }
   }

   private AliceObf_aG AT(List<PlayerEntity> var1, boolean var2) {
      if (this.zs() && this.Nv.EA() && this.Cq != null) {
         return this.AU(this.Cq, var2, var1);
      } else {
         this.AS();
         int var3 = MathHelper.ceil((Double)this.acb.get()) + 1;
         Vec3d var4 = this.mc.player.getEyePos();
         double var5 = (Double)this.acb.get() * (Double)this.acb.get();
         Object var7 = null;
         Iterator var8 = BlockPos.iterateOutwards(this.mc.player.getBlockPos(), var3, var3, var3).iterator();
         if (var8.hasNext()) {

         } else {
            char var16 = 41421;
         }

         return (AliceObf_aG)var7;
      }
   }

   private AliceObf_aG AU(BlockPos var1, boolean var2, List<PlayerEntity> var3) {
      if (this.zs()) {
         int var10000 = !this.Nv.EA() ? 5484 : 5483;

         while (true) {
            int var9 = var10000;
            if (var9 == 5483) {
               if (!var1.equals(this.Cq)) {
                  return null;
               }
               break;
            }

            if (var9 != 5484) {
               break;
            }

         }
      }

      if (!var2) {
         if (!this.Bu(var1)) {
            return null;
         }
      } else if (!this.Bv(var1)) {
         return null;
      }

      Vec3d var4 = CrystalAuraPlusPlus.CI(var1);
      AliceObf_lI var5 = this.Bn(var4, var2 ? var1 : null, var3);
      if (var5 == null) {
         return null;
      } else {
         BlockHitResult var6 = !var2 ? this.BC(var1) : this.BA(var1);
         if (var6 == null) {
            return null;
         } else {
            double var7 = this.AR(var5.dm, var1);
            return new AliceObf_aG(var1, var6, var2, var5, var7);
         }
      }
   }

   private List<AliceObf_aG> AV(AliceObf_aG var1, List<PlayerEntity> var2) {
      ArrayList<AliceObf_aG> var3 = new ArrayList<>();
      var3.add(var1);
      int var4 = (Integer)this.aPE.get();
      if (var4 > 1 && !var1.auH) {
         int var5 = MathHelper.ceil((Double)this.acb.get()) + 1;
         Vec3d var6 = this.mc.player.getEyePos();
         double var7 = (Double)this.acb.get() * (Double)this.acb.get();
         ArrayList<AliceObf_aG> var9 = new ArrayList<>();

         for (BlockPos var11 : BlockPos.iterateOutwards(this.mc.player.getBlockPos(), var5, var5, var5)) {
            double var12 = CU(var6, new Box(var11));
            if (!(var12 > var7) && this.mc.player.canInteractWithBlockAt(var11, 0.0)) {
               BlockPos var14 = var11.toImmutable();
               if (!var14.equals(var1.aQN)) {
                  AliceObf_aG var15 = this.AU(var14, false, var2);
                  if (var15 != null) {
                     var9.add(var15);
                  }
               }
            }
         }

         var9.sort((AliceObf_aG var0, AliceObf_aG var1x) -> {
            int var2x = Double.compare(var1x.Kb, var0.Kb);
            return var2x != 0 ? var2x : CC(var0.aQN, var1x.aQN);
         });

         for (AliceObf_aG var18 : (List<AliceObf_aG>)(List<?>)var9) {
            int var10000 = var3.size() >= var4 ? 21126 : 21127;

            while (true) {
               int var16 = var10000;
               if (var16 == 21126) {
                  return var3;
               }

               if (var16 != 21127) {
                  if (this.CD(AliceObf_up.wD, var18.aQN)
                     && !CrystalAuraPlusPlus.AW(var3, var18.aQN)) {
                     var3.add(var18);
                  }
                  break;
               }

            }
         }

         return var3;
      } else {
         return var3;
      }
   }

   private static boolean AW(List<AliceObf_aG> var0, BlockPos var1) {
      Box var2 = CJ(var1);

      for (AliceObf_aG var4 : var0) {
         if (CrystalAuraPlusPlus.CJ(var4.aQN).intersects(var2)) {
            return true;
         }
      }

      return false;
   }

   private AliceObf_aG AX(BlockPos var1, List<PlayerEntity> var2) {
      if (!this.Bw(var1)) {
         return null;
      } else {
         AliceObf_lI var3 = this.Bn(
            CrystalAuraPlusPlus.CI(var1), var1, var2
         );
         if (var3 == null) {
            return null;
         } else {
            BlockHitResult var4 = this.BB(var1, Direction.UP);
            if (var4 == null) {
               return null;
            } else {
               double var5 = this.AR(var3.dm, var1);
               return new AliceObf_aG(var1, var4, false, var3, var5);
            }
         }
      }
   }

   private boolean AY(AliceObf_aG var1, boolean var2) {
      AliceObf_mp var3 = this.Ba(Items.END_CRYSTAL, this.Cy());
      if (var3 == null) {
         return false;
      } else {
         boolean var4 = var2 ? this.Bw(var1.aQN) : this.Bu(var1.aQN);
         if (var4 && this.Bo(CI(var1.aQN), !var2 ? null : var1.aQN, var1.FY.aqF)) {
            BlockHitResult var5 = this.BB(var1.aQN, var1.ayi.getSide());
            if (var5 == null) {
               this.Zx.Rn();
               return false;
            } else {
               this.AP = CrystalAuraPlusPlus.Cl(var1);
               double[] var6 = CT(var5.getPos());
               if (!this.BL(var5, var6[0], var6[1])) {
                  if (this.aEc) {
                     this.aIP = new AliceObf_lq(var1, var2, var5);
                  }

                  return false;
               } else if (!this.Cg(AliceObf_up.wD, var1.aQN)) {
                  return false;
               } else if (!this.Bj(var3, var5)) {
                  return false;
               } else {
                  this.Bz(var1.aQN);
                  this.mc.player.swingHand(var3.Hr);
                  this.CE(AliceObf_up.wD, var1.aQN);
                  this.ug++;
                  this.zY(var1.aQN);
                  int[] var7 = aMT;
                  if ((this.Cr() || this.Nv.EA()) && (Boolean)this.tn.get() && (Boolean)this.su.get() && (this.Nv.EA() || var1.FY.wW >= (Double)this.apM.get())
                     )
                   {
                     var7 = this.BN(var1.aQN);
                  }

                  if (this.qJ.size() >= 8) {
                     this.qJ.remove(0);
                  }

                  this.qJ.add(new AliceObf_vd(var1.aQN, this.afe, this.AP, var7));
                  if (this.Nv.EA() && this.Cq != null && this.Cq.equals(var1.aQN)) {
                     this.amW = var1.aQN.toImmutable();
                  }

                  this.apy++;
                  this.AP(var1.aQN);
                  this.Cj(this.AP);
                  return true;
               }
            }
         } else {
            this.Zx.Rn();
            return false;
         }
      }
   }

   private void AZ(AliceObf_aG var1) {
      AliceObf_mp var2 = this.Ba(Items.OBSIDIAN, this.Cy());
      if (var2 != null) {
         if (this.Bv(var1.aQN)
            && this.Bo(
               CrystalAuraPlusPlus.CI(var1.aQN), var1.aQN, var1.FY.aqF
            )) {
            BlockHitResult var3 = this.BA(var1.aQN);
            if (var3 == null) {
               this.Zx.Rn();
            } else {
               this.AP = CrystalAuraPlusPlus.Cl(var1);
               double[] var4 = CT(var3.getPos());
               if (!this.BL(var3, var4[0], var4[1])) {
                  if (this.aEc) {
                     this.aIP = new AliceObf_lq(var1, false, var3);
                  }
               } else if (this.Cg(AliceObf_up.wD, var1.aQN)) {
                  if (this.Bj(var2, var3)) {
                     this.mc.player.swingHand(var2.Hr);
                     this.CE(AliceObf_up.wD, var1.aQN);
                     this.ug++;
                     this.aDq = new AliceObf_lu(var1.aQN, this.afe, this.AP);
                     this.AP(var1.aQN);
                     this.Cj(this.AP);
                  }
               }
            }
         } else {
            this.Zx.Rn();
         }
      }
   }

   private AliceObf_mp Ba(Item var1, int var2) {
      if (this.mc.player.getOffHandStack().isOf(var1)) {
         this.Bc();
         this.Bi();
         return new AliceObf_mp(Hand.OFF_HAND, -1);
      } else {
         int var3 = this.mc.player.getInventory().getSelectedSlot();
         if (this.alB.get() != AliceObf_jY.LN) {
            int var4 = this.Cx(var1);
            if (var4 < 0) {
               int var6;
               if (var1 == Items.END_CRYSTAL) {
                  if (this.aLP >= 0) {
                     if (this.aLP > 8) {
                        byte var10002 = 93;
                     } else {
                        byte var5 = 92;
                     }
                  }

                  var6 = this.Cy();
               } else {
                  var6 = var2;
               }

               this.Bb(var1, var6);
               this.Zx.Rn();
               return null;
            } else {
               this.Bc();
               this.Bf(var1, var4);
               return !this.Bh(var4) ? null : new AliceObf_mp(Hand.MAIN_HAND, var4);
            }
         } else {
            this.Bc();
            this.Bi();
            if (!this.mc.player.getInventory().getStack(var3).isOf(var1)) {
               return null;
            } else {
               this.Bf(var1, var3);
               return new AliceObf_mp(Hand.MAIN_HAND, var3);
            }
         }
      }
   }

   private void Bb(Item var1, int var2) {
      if (var1 != null && var2 >= 0 && var2 <= 8) {
         this.auK = var1;
         this.atJ = var2;
      } else {
         this.Bc();
      }
   }

   private void Bc() {
      this.auK = null;
      this.atJ = -1;
      this.WR.QV();
   }

   static void Bd() {
      CrystalAuraPlusPlus var0 = EU;
      if (var0 != null) {
         var0.Be();
      }
   }

   private void Be() {
      if (!AliceObf_fS.Oh(this)) {
         Item var1 = this.auK;
         if (!(var1 == null)) {
            short var4 = 26553;
         }

         if (this.isActive() && this.mc.player != null && this.mc.interactionManager != null) {
            if (!this.mc.player.getOffHandStack().isOf(var1) && this.Cx(var1) < 0) {
               AliceObf_hE var2 = this.WR.QM(var1, this.atJ);
               if (var2 instanceof AliceObf_bf var3) {
                  this.Bf(var1, var3.QK());
                  this.Bc();
               } else if (var2 == AliceObf_hE.th) {
                  this.Bc();
               }
            } else {
               this.Bc();
            }
         } else {
            this.Bc();
         }
      }
   }

   private void Bf(Item var1, int var2) {
      if (var1 == Items.END_CRYSTAL && var2 >= 0 && var2 <= 8) {
         this.aLP = var2;
      }
   }

   private boolean Bg(int var1) {
      if (var1 < 0) {
         if (this.alB.get() == AliceObf_jY.Ab) {
            this.Bi();
         }

         return true;
      } else if (this.alB.get() == AliceObf_jY.LN) {
         this.Bi();
         return this.mc.player.getInventory().getSelectedSlot() == var1;
      } else {
         return this.Bh(var1);
      }
   }

   private boolean Bh(int var1) {
      if (var1 >= 0 && var1 <= 8) {
         if (this.alB.get() == AliceObf_jY.Ab) {
            return AliceObf_fS.Of(this, var1);
         } else {
            this.Bi();
            return this.mc.player.getInventory().getSelectedSlot() == var1 || InvUtils.swap(var1, false);
         }
      } else {
         return false;
      }
   }

   private void Bi() {
      AliceObf_fS.Ov(this);
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean Bj(AliceObf_mp var1, BlockHitResult var2) {
      if (aEU == null) {
         AliceObf_fS.OG(this);
         return false;
      } else {
         SequencedPacketCreator var3 = var2x -> new PlayerInteractBlockC2SPacket(var1.Hr, var2, var2x);
         boolean var9 = false /* VF: Semaphore variable */;

         boolean var12;
         label57: {
            try {
               var9 = true;
               int var4 = AliceObf_fS.PD();
               aEU.invoke((ClientPlayerInteractionManager)this.mc.interactionManager, (ClientWorld)this.mc.world, (SequencedPacketCreator)var3);
               var12 = AliceObf_fS.PD() != var4;
               var9 = false;
               break label57;
            } catch (Throwable var10) {
               var12 = false;
               var9 = false;
            } finally {
               if (var9) {
                  AliceObf_fS.OG(this);
               }
            }

            AliceObf_fS.OG(this);
            return var12;
         }

         AliceObf_fS.OG(this);
         return var12;
      }
   }

   private static MethodHandle Bk() {
      for (Method var3 : ClientPlayerInteractionManager.class.getDeclaredMethods()) {
         Class[] var4 = var3.getParameterTypes();
         if (var3.getReturnType() == void.class && var4.length == 2 && var4[0] == ClientWorld.class && var4[1] == SequencedPacketCreator.class) {
            try {
               var3.setAccessible(true);
               return MethodHandles.lookup().unreflect(var3);
            } catch (RuntimeException | IllegalAccessException var6) {
               return null;
            }
         }
      }

      return null;
   }

   static boolean Bl() {
      if (!(aEU != null)) {
         char var0 = 57698;
      }

      return false;
   }

   private int Bm(EndCrystalEntity var1) {
      if ((Boolean)this.aCh.get()) {
         if (!this.mc.player.hasStatusEffect(StatusEffects.WEAKNESS)) {

         } else {
            short var9 = 3189;
         }

         if (this.alB.get() != AliceObf_jY.LN) {
            int var7 = -1;
            float var8 = 0.0F;

            for (int var4 = 0; var4 < 9; var4++) {
               ItemStack var5 = this.mc.player.getInventory().getStack(var4);
               float var6 = DamageUtils.getAttackDamage(this.mc.player, var1, var5);
               if (var6 > var8) {
                  var8 = var6;
                  var7 = var4;
               }
            }

            return var7 < 0 ? Integer.MIN_VALUE : var7;
         } else {
            int var2 = this.mc.player.getInventory().getSelectedSlot();
            ItemStack var3 = this.mc.player.getInventory().getStack(var2);
            return DamageUtils.getAttackDamage(this.mc.player, var1, var3) > 0.0F ? var2 : Integer.MIN_VALUE;
         }
      } else {
         return -1;
      }
   }

   private AliceObf_lI Bn(Vec3d var1, BlockPos var2, List<PlayerEntity> var3) {
      float var4 = Float.NaN;
      AliceObf_lI var5 = null;

      label168:
      for (PlayerEntity var7 : var3) {
         float var8 = this.Bp(var7, var1, var2, true);
         double var9 = CV(var7) <= this.ajY.get() ? Math.min((Double)this.pq.get(), 1.0) : (Double)this.pq.get();
         if (!(var8 < var9)) {
            if (Float.isNaN(var4)) {
               var4 = this.Bp(this.mc.player, var1, var2, false);
               int var10000 = var4 > this.aQq.get() ? 9752 : 9753;

               while (true) {
                  int var14 = var10000;
                  if (var14 == 9752) {
                     return null;
                  }

                  if (var14 != 9753) {
                     if ((Boolean)this.aji.get()
                        && CrystalAuraPlusPlus.CV(this.mc.player)
                              - var4
                           <= (Double)this.atE.get()) {
                        return null;
                     }
                     break;
                  }

               }
            }

            if (!(var8 - var4 < (Double)this.Qq.get())) {
               double var11 = var8 * 10.0 - var4;
               if (var8 >= CV(var7)) {
                  var11 += 1000.0;
               }

               AliceObf_lI var13 = new AliceObf_lI(var7, var8, var4, var11);
               if (var5 != null) {
                  int var16 = Cz(var13, var5) ? 50309 : 50310;

                  while (true) {
                     int var15 = var16;
                     if (var15 == 50309) {
                        break;
                     }

                     if (var15 != 50310) {
                        continue label168;
                     }

                     var16 = 50308;
                  }
               }

               var5 = var13;
            }
         }
      }

      return var5;
   }

   private boolean Bo(Vec3d var1, BlockPos var2, PlayerEntity var3) {
      if (var3 != null && var3.isAlive()) {
         if (!Friends.get().shouldAttack(var3)) {

         } else {
            short var5 = 18176;
         }

         if (!var3.isCreative()) {
            int var10001 = !var3.isSpectator() ? 4154 : 4155;

            while (true) {
               int var4 = var10001;
               if (var4 == 4154) {
                  if (!(this.mc.player.squaredDistanceTo(var3) > (Double)this.aHF.get() * (Double)this.aHF.get())) {
                     return this.Bn(var1, var2, List.of(var3)) != null;
                  }
                  break;
               }

               if (var4 != 4155) {
                  break;
               }

            }
         }

         return false;
      } else {
         return false;
      }
   }

   private float Bp(PlayerEntity var1, Vec3d var2, BlockPos var3, boolean var4) {
      RaycastFactory var5 = (var2x, var3x) -> {
         BlockState var4x = var3 != null && var3x.equals(var3) ? Blocks.OBSIDIAN.getDefaultState() : this.mc.world.getBlockState(var3x);
         return var4x.getCollisionShape(this.mc.world, var3x).raycast(var2x.start(), var2x.end(), var3x);
      };
      float var6 = DamageUtils.crystalDamage(var1, var1.getEntityPos(), var1.getBoundingBox(), var2, var5);
      Vec3d var7 = var4 ? this.zF(var1) : null;
      int var10000 = var7 != null ? 21543 : 21544;

      while (true) {
         int var10 = var10000;
         if (var10 == 21543) {
            Vec3d var11 = var7.subtract(var1.getEntityPos());
            float var12 = DamageUtils.crystalDamage(var1, var7, var1.getBoundingBox().offset(var11), var2, var5);
            return this.zG(var12);
         }

         if (var10 != 21544) {
            if ((Boolean)this.aQr.get()) {
               Vec3d var8 = var1.getVelocity();
               float var9 = DamageUtils.crystalDamage(var1, var1.getEntityPos().add(var8), var1.getBoundingBox().offset(var8), var2, var5);
               return !var4 ? Math.max(var6, var9) : Math.min(var6, var9);
            }

            return var6;
         }

      }
   }

   private boolean Bq(EndCrystalEntity var1) {
      return this.Br(var1.getBoundingBox());
   }

   private boolean Br(Box var1) {
      if (!this.mc.player.canAttackEntityIn(var1, 0.0)) {
         return false;
      } else {
         double var2 = this.Bs();
         return CrystalAuraPlusPlus.CU(
               this.mc.player.getEyePos(), var1
            )
            <= var2 * var2;
      }
   }

   private double Bs() {
      return (Double)this.Ld.get();
   }

   private double Bt() {
      return Math.min(this.Bs(), this.mc.player.getEntityInteractionRange());
   }

   private boolean Bu(BlockPos var1) {
      BlockState var2 = this.mc.world.getBlockState(var1);
      if (!var2.isOf(Blocks.OBSIDIAN) && !var2.isOf(Blocks.BEDROCK)) {
         return false;
      } else {
         int var10000 = !this.mc.world.isAir(var1.up()) ? 37238 : 37239;

         while (true) {
            int var3 = var10000;
            if (var3 == 37238) {
               return false;
            }

            if (var3 != 37239) {
               if (!this.mc.player.canInteractWithBlockAt(var1, 0.0)) {
                  return false;
               }

               if (CU(this.mc.player.getEyePos(), new Box(var1)) > (Double)this.acb.get() * (Double)this.acb.get()) {
                  return false;
               }

               return this.Bx(var1);
            }

         }
      }
   }

   private boolean Bv(BlockPos var1) {
      if (this.zs() && !this.mc.world.getOtherEntities(null, new Box(var1)).isEmpty()) {
         return false;
      } else {
         return this.mc.world.getBlockState(var1).isReplaceable()
               && this.mc.world.isAir(var1.up())
               && Blocks.OBSIDIAN.getDefaultState().canPlaceAt(this.mc.world, var1)
               && this.Bx(var1)
            ? this.BA(var1) != null
            : false;
      }
   }

   private boolean Bw(BlockPos var1) {
      if (this.mc.player.canInteractWithBlockAt(var1, 0.0)) {
         return CrystalAuraPlusPlus.CU(
                  this.mc.player.getEyePos(), new Box(var1)
               )
               > this.acb.get() * this.acb.get()
            ? false
            : this.mc.world.isAir(var1.up()) && this.Bx(var1);
      } else {
         return false;
      }
   }

   private boolean Bx(BlockPos var1) {
      Iterator var2 = this.mc.world.getOtherEntities(null, CJ(var1)).iterator();
      if (var2.hasNext()) {
         Entity var3 = (Entity)var2.next();
         if (var3 instanceof EndCrystalEntity var4) {
            if (this.By(var4)) {

            } else {
               short var5 = 27070;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean By(EndCrystalEntity var1) {
      if (!this.zW(var1.getId())) {
         AliceObf_rn var2 = this.azw.get(var1.getId());
         if (var2 == null) {
            return false;
         } else if (this.Cp()) {
            return var2.anU.EO(this.afe, 10);
         } else {
            int var10000 = var2.ahr < this.aN.get() ? 56967 : 56966;

            while (true) {
               int var3 = var10000;
               if (var3 == 56966) {
                  return true;
               }

               if (var3 != 56967) {
                  if (this.afe - var2.Gg > 10) {
                     char var4 = 46987;
                  } else {
                     char var5 = 46986;
                  }

                  return false;
               }

            }
         }
      } else {
         return true;
      }
   }

   private void Bz(BlockPos var1) {
      if (var1.equals(this.CE)) {
         this.CE = null;
      }

      if (this.Cp()) {
         for (Entity var3 : this.mc
            .world
            .getOtherEntities(null, CrystalAuraPlusPlus.CJ(var1))) {
            if (var3 instanceof EndCrystalEntity) {
               AliceObf_rn var4 = this.azw.get(var3.getId());
               if (var4 != null) {
                  var4.anU.EP();
               }
            }
         }
      }
   }

   private BlockHitResult BA(BlockPos var1) {
      Direction[] var2 = Direction.values();
      int var3 = var2.length;
      int var4 = 0;

      while (true) {
         int var10000 = var4 < var3 ? 60289 : 60290;

         while (true) {
            int var9 = var10000;
            if (var9 == 60289) {
               Direction var5 = var2[var4];
               BlockPos var6 = var1.offset(var5.getOpposite());
               BlockState var7 = this.mc.world.getBlockState(var6);
               if (!var7.isAir()
                  && !var7.isReplaceable()
                  && this.mc.player.canInteractWithBlockAt(var6, 0.0)
                  && !(
                     CrystalAuraPlusPlus.CU(
                           this.mc.player.getEyePos(), new Box(var6)
                        )
                        > (Double)this.acb.get() * (Double)this.acb.get()
                  )) {
                  BlockHitResult var8 = this.BB(var6, var5);
                  if (var8 != null) {
                     return var8;
                  }
               }

               var4++;
               break;
            }

            if (var9 != 60290) {
               return null;
            }

         }
      }
   }

   private BlockHitResult BB(BlockPos var1, Direction var2) {
      BlockHitResult var3 = this.BD(var1, var2);
      if (var3 != null) {
         return var3;
      } else if (this.So.get() == AliceObf_lJ.apY) {
         return null;
      } else {
         Direction var4 = var2 == null
            ? CrystalAuraPlusPlus.CO(
               var1, this.mc.player.getEyePos()
            )
            : var2;
         if (var4 != null
            && CrystalAuraPlusPlus.CP(
               var1, var4, this.mc.player.getEyePos()
            )) {
            Vec3d var5 = this.BE(var1, var4, 0);
            return new BlockHitResult(var5, var4, var1, false);
         } else {
            return null;
         }
      }
   }

   private BlockHitResult BC(BlockPos var1) {
      BlockHitResult var2 = this.BB(var1, Direction.UP);
      return var2 != null ? var2 : this.BB(var1, null);
   }

   private BlockHitResult BD(BlockPos var1, Direction var2) {
      Vec3d var3 = this.mc.player.getEyePos();
      Direction[] var4 = var2 == null ? Direction.values() : new Direction[]{var2};

      for (Direction var8 : var4) {
         for (int var9 = 0; var9 < hF.length; var9++) {
            Vec3d var10 = this.BE(var1, var8, var9);
            Vec3d var11 = var10.subtract(var8.getOffsetX() * 0.001, var8.getOffsetY() * 0.001, var8.getOffsetZ() * 0.001);
            BlockHitResult var12 = this.mc.world.raycast(new RaycastContext(var3, var11, ShapeType.COLLIDER, FluidHandling.NONE, this.mc.player));
            if (var12.getType() == HitResult.Type.BLOCK && var12.getBlockPos().equals(var1) && (var2 == null || var12.getSide() == var2)) {
               return var12;
            }
         }
      }

      return null;
   }

   private Vec3d BE(BlockPos var1, Direction var2, int var3) {
      int var4 = CrystalAuraPlusPlus.BF(var1, var2, this.ug, var3);
      return CrystalAuraPlusPlus.CN(var1, var2, hF[var4], Nd[var4]);
   }

   static int BF(BlockPos var0, Direction var1, long var2, int var4) {
      long var5 = (var0.asLong() ^ var1.ordinal() * -4417276706812531889L) + var2;
      int var7 = Math.floorMod(var5, hF.length);
      return Math.floorMod(var7 + var4, hF.length);
   }

   private Vec3d BG(EndCrystalEntity var1) {
      return this.BJ(var1.getBoundingBox());
   }

   private Vec3d BH(EndCrystalEntity var1, BlockPos var2) {
      Vec3d var3 = this.BI(var1, var2);
      return var3 != null ? var3 : this.BG(var1);
   }

   private Vec3d BI(EndCrystalEntity var1, BlockPos var2) {
      if (var1 != null && var2 != null && this.mc.player != null && this.mc.world != null) {
         Box var3 = var1.getBoundingBox();
         Vec3d var4 = this.mc.player.getEyePos();
         Vec3d var5 = CrystalAuraPlusPlus.CM(
            var2, Direction.UP
         );
         if (this.So.get() != AliceObf_lJ.amg && !this.BK(var5)) {
            return null;
         } else {
            return CrystalAuraPlusPlus.CS(var4, var5, var3, this.Bt())
               ? var5
               : null;
         }
      } else {
         return null;
      }
   }

   private Vec3d BJ(Box var1) {
      if (this.So.get() == AliceObf_lJ.amg) {
         return var1.getCenter();
      } else {
         double var2 = (var1.minX + var1.maxX) * 0.5;
         double var4 = (var1.minY + var1.maxY) * 0.5;
         double var6 = (var1.minZ + var1.maxZ) * 0.5;
         double var8 = Math.min(0.35, (var1.maxX - var1.minX) * 0.35);
         double var10 = Math.min(0.65, (var1.maxY - var1.minY) * 0.35);
         double var12 = Math.min(0.35, (var1.maxZ - var1.minZ) * 0.35);

         for (double var17 : new double[]{var4, var4 - var10, var4 + var10}) {
            for (double var22 : new double[]{var2, var2 - var8, var2 + var8}) {
               for (double var27 : new double[]{var6, var6 - var12, var6 + var12}) {
                  Vec3d var29 = new Vec3d(var22, var17, var27);
                  if (this.BK(var29)) {
                     return var29;
                  }
               }
            }
         }

         return null;
      }
   }

   private boolean BK(Vec3d var1) {
      BlockHitResult var2 = this.mc
         .world
         .raycast(new RaycastContext(this.mc.player.getEyePos(), var1, ShapeType.COLLIDER, FluidHandling.NONE, this.mc.player));
      if (var2.getType() != HitResult.Type.MISS) {
         double var3 = this.mc.player.getEyePos().squaredDistanceTo(var1);
         return this.mc.player.getEyePos().squaredDistanceTo(var2.getPos()) + 1.0E-4 >= var3;
      } else {
         return true;
      }
   }

   private boolean BL(BlockHitResult var1, double var2, double var4) {
      this.aEc = false;
      if (!this.Ce(var1)) {
         boolean var6 = this.BW(var2, var4);
         if (!var6) {
            return false;
         } else {
            this.aEc = true;
            this.Zx.Rn();
            return false;
         }
      } else {
         this.maintainSentLook();
         return true;
      }
   }

   private boolean BM(EndCrystalEntity var1, double var2, double var4) {
      if (this.Cf(var1)) {
         this.maintainSentLook();
         return true;
      } else {
         double var6 = !this.azF
            ? (
               AliceObf_fS.OK()
                  ? AliceObf_fS.OI()
                  : Rotations.serverYaw
            )
            : this.fp;
         double var8 = this.azF ? this.WI : (AliceObf_fS.OK() ? AliceObf_fS.OJ() : Rotations.serverPitch);
         if (Math.abs(MathHelper.wrapDegrees(var2 - var6)) < 0.5 && Math.abs(var4 - var8) < 0.5) {
            this.maintainSentLook();
            return true;
         } else {
            boolean var10 = this.BW(var2, var4);
            if (this.Co()) {
               this.Zx.Rn();
               return true;
            } else if (!var10) {
               return false;
            } else {
               this.Zx.Rn();
               return true;
            }
         }
      }
   }

   private int[] BN(BlockPos var1) {
      if (!this.Co()) {
         return aMT;
      } else if (this.mc.player != null && !this.mc.player.isOnGround()) {
         return aMT;
      } else {
         if (this.BS()) {

         } else {
            char var14 = 42418;
         }

         if (this.alx < 0) {
            this.BO();
         }

         if (this.alx < 0) {
            return aMT;
         } else {
            Vec3d var2 = CI(var1);
            EndCrystalEntity var3 = new EndCrystalEntity(this.mc.world, var2.x, var2.y, var2.z);
            int var4 = this.Bm(var3);
            if (var4 == Integer.MIN_VALUE) {
               return aMT;
            } else if (!this.Bg(var4)) {
               return aMT;
            } else {
               double[] var5 = CT(
                  CrystalAuraPlusPlus.Cw(var3.getBoundingBox())
               );
               this.BW(var5[0], var5[1]);
               int var6 = (Integer)this.zw.get();
               int[] var7 = new int[var6];
               int var8 = 0;

               for (int var9 = 1; var9 <= var6; var9++) {
                  int var10 = this.alx + this.apy + var9;
                  Entity var11 = this.mc.world.getEntityById(var10);
                  if (var11 == null || var11 instanceof EndCrystalEntity) {
                     if (!AliceObf_fS.Ph(this)) {
                        break;
                     }

                     var3.setId(var10);
                     int var12 = AliceObf_fS.PD();
                     this.mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(var3, this.mc.player.isSneaking()));
                     if (AliceObf_fS.PD() != var12) {

                     } else {
                        char var15 = 59114;
                     }

                     this.mc.player.swingHand(Hand.MAIN_HAND);
                     var7[var8++] = var10;
                     AliceObf_rn var13 = this.azw.computeIfAbsent(var10, var0 -> new AliceObf_rn());
                     var13.ayC = this.AP;
                     var13.ahr++;
                     var13.Gg = this.afe;
                  }
               }

               if (var8 == 0) {
                  return aMT;
               } else {
                  this.CE(AliceObf_up.aOU, var1);
                  this.AQ(var1);
                  return var8 != var7.length ? Arrays.copyOf(var7, var8) : var7;
               }
            }
         }
      }
   }

   private void BO() {
      if (this.mc.world != null) {
         for (Entity var2 : this.mc.world.getEntities()) {
            int var10000 = var2.getId() > this.alx ? 58211 : 58212;

            while (true) {
               int var3 = var10000;
               if (var3 == 58211) {
                  this.alx = var2.getId();
                  break;
               }

               if (var3 != 58212) {
                  break;
               }

            }
         }
      }
   }

   private void BP() {
      if ((Boolean)this.aBm.get()) {
         int var1 = (Integer)this.sO.get();
         if (var1 > 0) {
            if (this.kY.length != var1) {
               this.kY = new int[var1];
               this.tT = 0;
               this.Vw = 0;
               this.aFm = 0L;
               this.pt = -1;
            }

            if (this.pt != this.afe) {
               if (this.Vw == this.kY.length) {
                  int var3 = this.kY[this.tT];
                  this.aFm -= var3;
                  this.tT = (this.tT + 1) % this.kY.length;
                  this.Vw--;
               }

               int var4 = (this.tT + this.Vw) % this.kY.length;
               this.kY[var4] = 1;
               this.Vw++;
               this.aFm++;
               this.pt = this.afe;
            } else {
               int var2 = (this.tT + this.Vw - 1) % this.kY.length;
               this.kY[var2]++;
               this.aFm++;
            }
         }
      }
   }

   private long BQ() {
      return this.aFm;
   }

   private void BR() {
      if ((Boolean)this.aBm.get()) {
         if (this.OB != this.afe) {
            this.OB = this.afe;
            this.Dr = 0;
         }

         this.Dr++;
      }
   }

   private boolean BS() {
      if (!(Boolean)this.aBm.get()) {
         return false;
      } else if (this.afe - this.aPD < (Integer)this.alN.get()) {
         return true;
      } else if (this.BQ() > ((Integer)this.aOS.get()).intValue()) {
         return true;
      } else {
         int var10000 = this.zm() > ((Integer)this.anO.get()).intValue() ? 50318 : 50319;

         while (true) {
            int var1 = var10000;
            if (var1 == 50318) {
               return true;
            }

            if (var1 != 50319) {
               if (this.Dr <= (Integer)this.dM.get()) {
                  if ((Boolean)this.HD.get() && this.mc.world != null && this.BT() > (Integer)this.aOW.get()) {
                     return true;
                  }

                  return false;
               }

               return true;
            }

         }
      }
   }

   private int BT() {
      if (!(this.mc.world == null)) {
         short var4 = 11820;
      }

      int var1 = 0;

      for (Entity var3 : this.mc.world.getEntities()) {
         var1++;
      }

      return var1;
   }

   private static boolean BU(int[] var0, int var1) {
      if (var0 == null) {
         return false;
      } else {
         for (int var5 : var0) {
            if (var5 == var1) {
               return true;
            }
         }

         return false;
      }
   }

   private void BV(AliceObf_sR var1) {
      if (!this.Co()) {

      } else {
         char var9 = 62677;
      }

      if (this.mc.player != null && this.mc.world != null) {
         if (this.AE()) {
            if (!this.du.EK()) {
               if (this.iE == null) {
                  this.iE = var1.ajn;
               }
            } else if (this.CD(AliceObf_up.aOU, var1.II)) {
               if (this.Cr() || this.Cf(var1.ajn)) {
                  int var2 = this.Bm(var1.ajn);
                  if (var2 != Integer.MIN_VALUE) {
                     if (this.Bg(var2)) {
                        Vec3d var3 = this.BH(var1.ajn, var1.II);
                        if (var3 != null) {
                           if (this.Cr()) {
                              double[] var4 = CT(var3);
                              this.BW(var4[0], var4[1]);
                           }

                           if (this.Cg(AliceObf_up.aOU, var1.II)) {
                              int var8 = AliceObf_fS.PD();

                              try {
                                 this.mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(var1.ajn, this.mc.player.isSneaking()));
                              } finally {
                                 AliceObf_fS.OG(this);
                              }

                              if (AliceObf_fS.PD() != var8) {
                                 this.mc.player.swingHand(Hand.MAIN_HAND);
                                 this.CE(AliceObf_up.aOU, var1.II);
                                 AliceObf_rn var5 = this.azw.computeIfAbsent(var1.ajn.getId(), var0 -> new AliceObf_rn());
                                 var5.ahr++;
                                 var5.Gg = this.afe;
                                 this.An(var1, var5);
                                 if (this.Nv.EA()) {

                                 } else {
                                    short var10 = 27791;
                                 }

                                 this.Ao();
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean BW(double var1, double var3) {
      double var5 = this.azF ? this.fp : (!AliceObf_fS.OK() ? Rotations.serverYaw : AliceObf_fS.OI());
      double var7 = this.azF
         ? this.WI
         : (
            AliceObf_fS.OK()
               ? AliceObf_fS.OJ()
               : Rotations.serverPitch
         );
      double var9 = var3 - var7;
      double var11 = CrystalAuraPlusPlus.CQ(
         var5, var1, (Double)this.br.get()
      );
      double var13 = MathHelper.clamp(var7 + MathHelper.clamp(var9, -(Double)this.auL.get(), (Double)this.auL.get()), -90.0, 90.0);
      boolean var15 = Math.abs(MathHelper.wrapDegrees(var1 - var11)) <= 1.0E-4 && Math.abs(MathHelper.clamp(var3, -90.0, 90.0) - var13) <= 1.0E-4;
      this.aDB = new AliceObf_np((float)var11, (float)var13);
      return var15;
   }

   private void BX() {
      this.aDB = new AliceObf_np(this.fp, this.WI);
   }

   private void maintainSentLook() {
      int var10000 = this.aNq.get() ? 60156 : 60157;

      while (true) {
         int var1 = var10000;
         if (var1 == 60156) {
            if (this.azF) {
               if (AliceObf_fS.OA(this)) {
                  this.BX();
               }

               return;
            }
            break;
         }

         if (var1 != 60157) {
            break;
         }

      }
   }

   private void BY() {
      AliceObf_np var1 = this.aDB;
      this.aDB = null;
      ClientPlayerEntity var2 = this.mc.player;
      if (var1 != null) {
         if (!(var2 != null)) {
            short var3 = 7835;
         }
      }
   }

   private void BZ() {
      this.Ca(true);
   }

   private void Ca(boolean var1) {
      AliceObf_sl var2 = this.aBl;
      if (var2 != null) {
         var2.xO(var1);
         AliceObf_au.Fy(this.mc.player, var2);
         this.aBl = null;
      } else if (var1) {
         this.Cb();
      }
   }

   private void Cb() {
      if (this.cl && this.mc.player != null) {
         this.mc.player.setYaw(this.jG);
         this.mc.player.setPitch(this.aII);
      }

      this.cl = false;
      this.NW.VP();
   }

   public void suspendForItemUse() {
      this.EQ = false;
      this.aIP = null;
      this.iE = null;
      this.aEc = false;
      this.WR.QV();
      this.Bc();
      this.CE = null;
      this.BZ();
      this.aDB = null;
      this.NW.VP();
      this.Zx.Rn();
   }

   public boolean supportsMiningHandoff() {
      return true;
   }

   public boolean canYieldForMining() {
      return !this.Nv.EA() && this.auK == null;
   }

   public void yieldForMining() {
      this.EQ = false;
      this.aIP = null;
      this.iE = null;
      this.aEc = false;
      this.CE = null;
      this.Fb = null;
      this.BZ();
      this.aDB = null;
      this.NW.VP();
      this.Zx.Rn();
   }

   public void yieldToHigherPriority() {
      this.zu();
      this.suspendForItemUse();
   }

   static PlayerInput Cc(PlayerInput var0, Vec2f var1, float var2, float var3) {
      PlayerInput var4 = var0 == null ? PlayerInput.DEFAULT : var0;
      Vec2f var5 = var1 == null ? Vec2f.ZERO : var1;
      if (var5.lengthSquared() <= 1.0E-5F) {
         return new PlayerInput(false, false, false, false, var4.jump(), var4.sneak(), false);
      } else {
         float var6 = (var2 - var3) * (float) (Math.PI / 180.0);
         float var7 = MathHelper.cos(var6);
         float var8 = MathHelper.sin(var6);
         int var9 = MathHelper.clamp(Math.round(var5.x * var7 - var5.y * var8), -1, 1);
         int var10 = MathHelper.clamp(Math.round(var5.y * var7 + var5.x * var8), -1, 1);
         return new PlayerInput(var10 > 0, var10 < 0, var9 > 0, var9 < 0, var4.jump(), var4.sneak(), var4.sprint());
      }
   }

   private static Vec2f Cd(PlayerInput var0) {
      return AliceObf_lO.Fp(var0);
   }

   private boolean Ce(BlockHitResult var1) {
      if (this.azF && var1 != null && this.mc.player != null) {
         Vec3d var2 = this.mc.player.getEyePos();
         double var3 = (Double)this.acb.get();
         if (!CR(var2, this.fp, this.WI, var3, new Box(var1.getBlockPos()))) {
            return false;
         } else if (this.So.get() == AliceObf_lJ.amg) {
            return true;
         } else {
            Vec3d var5 = var2.add(Vec3d.fromPolar(this.WI, this.fp).multiply(var3));
            BlockHitResult var6 = this.mc.world.raycast(new RaycastContext(var2, var5, ShapeType.COLLIDER, FluidHandling.NONE, this.mc.player));
            return var6.getType() == HitResult.Type.BLOCK && var6.getBlockPos().equals(var1.getBlockPos()) && var6.getSide() == var1.getSide();
         }
      } else {
         return false;
      }
   }

   private boolean Cf(EndCrystalEntity var1) {
      return this.azF && var1 != null && this.mc.player != null && CR(this.mc.player.getEyePos(), this.fp, this.WI, this.Bt(), var1.getBoundingBox());
   }

   private boolean Cg(AliceObf_up var1, BlockPos var2) {
      if (!this.du.EK()) {
         return false;
      } else {
         if ((Boolean)this.DP.get()) {

         } else {
            char var4 = 38155;
         }

         if ((Boolean)this.Fi.get()) {
            int var10001 = this.mc.currentScreen == null ? 20172 : 20173;

            while (true) {
               int var3 = var10001;
               if (var3 == 20172) {
                  if (this.mc.options.attackKey.isPressed()) {
                     return false;
                  }
                  break;
               }

               if (var3 != 20173) {
                  break;
               }

            }
         }

         if (!this.Co()) {
            return this.CD(var1, var2) && AliceObf_fS.OE(this);
         } else {
            if (!this.Cr()) {
               char var5 = 43249;
            } else {
               char var6 = 43248;
            }

            return this.CD(var1, var2) && AliceObf_fS.OF(this);
         }
      }
   }

   private void Ch() {
      this.zT();
      long var1 = this.zU();
      long var3 = System.currentTimeMillis();
      this.Dm.entrySet().removeIf(var4 -> var3 - var4.getValue() > var1);
      this.azw.entrySet().removeIf(var1x -> {
         if (this.afe - var1x.getValue().Gg <= 10) {
            return false;
         } else {
                        Entity var2 = this.mc.world == null ? null : this.mc.world.getEntityById(var1x.getKey());
            return !(var2 instanceof EndCrystalEntity) || !var2.isAlive();
         }
      });
      this.qJ.removeIf(var1x -> this.afe - var1x.aye > 1);
      this.aeI.removeIf(var1x -> this.afe - var1x.aG > 40);
      this.auc.entrySet().removeIf(var1x -> {
         if (this.afe - var1x.getValue() <= 10) {
            return false;
         } else {
            Entity var2 = this.mc.world == null ? null : this.mc.world.getEntityById(var1x.getKey());
            if (!(var2 instanceof EndCrystalEntity)) {

            } else {
               short var3x = 26735;
            }

            return true;
         }
      });
   }

   private void Ci() {
      if ((Boolean)this.atY.get()) {

      } else {
         short var1 = 30763;
      }

      this.ayt.clear();
   }

   private void Cj(AliceObf_bP var1) {
      if (var1 != null && (Boolean)this.atY.get() && (Boolean)this.akE.get()) {
         this.ayt
            .removeIf(var1x -> CrystalAuraPlusPlus.Ck(var1x.abZ, var1));
         if (this.ayt.size() >= 64) {
            this.ayt.remove(0);
         }

         this.ayt.add(new AliceObf_rD(var1, this.afe));
      }
   }

   private static boolean Ck(AliceObf_bP var0, AliceObf_bP var1) {
      if (var0.aeY != var1.aeY) {
         return false;
      } else {
         Vec3d var2 = Cw(var0.UH);
         Vec3d var3 = CrystalAuraPlusPlus.Cw(var1.UH);
         return var2.squaredDistanceTo(var3) < 1.0E-4;
      }
   }

   private static AliceObf_bP Cl(AliceObf_aG var0) {
      BlockPos var1 = var0.aQN;
      return new AliceObf_bP(
         AliceObf_kC.aFj,
         new Box(var1),
         new Vec3d(var1.getX() + 0.5, var1.getY() + 1.25, var1.getZ() + 0.5),
         CrystalAuraPlusPlus.Cn(var0.FY)
      );
   }

   private static AliceObf_bP Cm(AliceObf_sR var0) {
      Box var1 = var0.ajn.getBoundingBox();
      Box var2 = new Box(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ);
      Vec3d var3 = CrystalAuraPlusPlus.Cw(var2);
      return new AliceObf_bP(
         AliceObf_kC.lA,
         var2,
         new Vec3d(var3.x, var2.maxY + 0.2, var3.z),
         CrystalAuraPlusPlus.Cn(var0.aLu)
      );
   }

   private static String Cn(AliceObf_lI var0) {
      return String.format(Locale.ROOT, "T %.1f | S %.1f", var0.wW, var0.aNU);
   }

   private boolean Co() {
      return this.Nv.EA() || AliceObf_ss.ET((AliceObf_id)this.aKA.get(), (AliceObf_id)this.aei.get(), this.Cs());
   }

   private boolean Cp() {
      return this.KJ.get() == AliceObf_aX.pe;
   }

   private boolean Cq() {
      return this.KJ.get() != AliceObf_aX.pe;
   }

   private boolean Cr() {
      if (this.KJ.get() == AliceObf_aX.Jw) {

      } else {
         char var1 = 41259;
      }

      return false;
   }

   private boolean Cs() {
      return (Boolean)this.QW.get() && this.qn == AliceObf_lG.tl;
   }

   private void Ct() {
      if (!(Boolean)this.QW.get()) {
         if (this.qn != AliceObf_lG.cT) {
            this.qn = AliceObf_lG.cT;
            this.oP = System.currentTimeMillis();
         }

         this.aAg.entrySet().removeIf(var0 -> System.currentTimeMillis() - var0.getValue() > 10000L);
      } else {
         long var1 = System.currentTimeMillis();
         this.aAg.entrySet().removeIf(var2 -> var1 - var2.getValue() > 10000L);
         if (this.qn == AliceObf_lG.tl) {
            if (var1 - this.bL >= ((Integer)this.zf.get()).intValue()) {
               this.qn = AliceObf_lG.all;
               this.oP = var1;
            }
         } else if (this.qn != AliceObf_lG.all) {
            if (this.mc.player != null && this.mc.world != null) {
               if (var1 - this.rp >= 100L) {
                  this.rp = var1;
                  this.aga = this.Cu();
               }

               if (!(this.aga)) {
                  char var3 = 50702;
               }
            }
         } else {
            if (var1 - this.oP >= ((Integer)this.iV.get()).intValue()) {
               this.qn = AliceObf_lG.cT;
            }
         }
      }
   }

   private boolean Cu() {
      if ((Boolean)this.QW.get() && this.qn == AliceObf_lG.cT) {
         if (this.mc.player != null && this.mc.world != null) {
            List var1 = this.AF();
            if (var1.isEmpty()) {
               return false;
            } else {
               AliceObf_ko var2 = (AliceObf_ko)this.vm.get();
               if (var2 == AliceObf_ko.Ec) {
                  return true;
               } else {
                  long var3 = System.currentTimeMillis();
                  boolean var5 = this.aAg.values().stream().anyMatch(var2x -> {
                     if (!(var3 - var2x < 2500L)) {
                        short var3x = 28362;
                     }

                     return false;
                  });
                  if (var5) {
                     if (!(var2 != AliceObf_ko.AV)) {
                        short var15 = 9621;
                     }
                  }

                  for (PlayerEntity var7 : (List<PlayerEntity>)(List<?>)var1) {
                     boolean var8 = CV(var7) <= (Double)this.ajY.get() + 0.001;
                     if (var2 == AliceObf_ko.amw && var8) {
                        return true;
                     }

                     boolean var9 = this.aAg.containsKey(var7.getId()) && var3 - this.aAg.get(var7.getId()) < 2500L;
                     if (var9) {
                        if (var2 != AliceObf_ko.AV) {
                           char var16 = 60465;
                        } else {
                           char var17 = 60466;
                        }
                     }

                     Object var10 = null;

                     for (EndCrystalEntity var12 : this.zP()) {
                        AliceObf_lI var13 = this.Cv(var12, var7);
                        if (var13 != null && (var10 == null || var13.dm > ((AliceObf_lI)var10).dm)) {
                           var10 = var13;
                        }
                     }

                     if (var10 == null) {
                        AliceObf_aG var14 = this.AT(List.of(var7), false);
                        if (var14 == null && (Boolean)this.xM.get()) {
                           var14 = this.AT(List.of(var7), true);
                        }

                        if (var14 != null) {
                           var10 = var14.yN();
                        }
                     }

                     if (var10 != null && ((AliceObf_lI)var10).wW >= CV(var7) - 0.001) {
                        return true;
                     }
                  }

                  return false;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private AliceObf_lI Cv(EndCrystalEntity var1, PlayerEntity var2) {
      if (!var1.isAlive() || !this.Bq(var1)) {
         return null;
      } else if (!this.Aa(var1)) {
         return null;
      } else {
         BlockPos var3 = this.ny.get(var1.getId());
         if (var3 == null) {
            var3 = var1.getBlockPos().down();
         }

         Vec3d var4 = this.BH(var1, var3);
         if (var4 == null) {
            return null;
         } else {
            return CS(this.mc.player.getEyePos(), var4, var1.getBoundingBox(), this.Bt()) ? this.Bn(var1.getEntityPos(), null, List.of(var2)) : null;
         }
      }
   }

   private static Vec3d Cw(Box var0) {
      return new Vec3d((var0.minX + var0.maxX) * 0.5, (var0.minY + var0.maxY) * 0.5, (var0.minZ + var0.maxZ) * 0.5);
   }

   private int Cx(Item var1) {
      for (int var2 = 0; var2 < 9; var2++) {
         if (this.mc.player.getInventory().getStack(var2).isOf(var1)) {
            return var2;
         }
      }

      return -1;
   }

   private int Cy() {
      int var1 = this.mc.player.getInventory().getSelectedSlot();
      if (this.mc.player.getInventory().getStack(var1).isEmpty()) {
         return var1;
      } else {
         for (int var2 = 0; var2 < 9; var2++) {
            if (this.mc.player.getInventory().getStack(var2).isEmpty()) {
               return var2;
            }
         }

         return -1;
      }
   }

   private static boolean Cz(AliceObf_lI var0, AliceObf_lI var1) {
      if (Math.abs(var0.dm - var1.dm) > 1.0E-4) {
         if (!(var0.dm > var1.dm)) {

         } else {
            short var2 = 7533;
         }

         return false;
      } else if (Math.abs(var0.wW - var1.wW) > 1.0E-4) {
         return var0.wW > var1.wW;
      } else {
         return Math.abs(var0.aNU - var1.aNU) > 1.0E-4 ? var0.aNU < var1.aNU : var0.aqF.getId() < var1.aqF.getId();
      }
   }

   private static boolean CA(AliceObf_lI var0, AliceObf_lI var1) {
      return Math.abs(var0.dm - var1.dm) <= 1.0E-4;
   }

   private static boolean CB(double var0, double var2) {
      return Math.abs(var0 - var2) <= 1.0E-4;
   }

   private static int CC(BlockPos var0, BlockPos var1) {
      int var2 = Integer.compare(var0.getY(), var1.getY());
      if (var2 != 0) {
         return var2;
      } else {
         int var3 = Integer.compare(var0.getX(), var1.getX());
         return var3 != 0 ? var3 : Integer.compare(var0.getZ(), var1.getZ());
      }
   }

   private boolean CD(AliceObf_up var1, BlockPos var2) {
      if (!this.Cs()) {
         long var3 = System.nanoTime();
         long var5 = var1 == AliceObf_up.wD ? this.aNE : this.ge;
         BlockPos var7 = var1 != AliceObf_up.wD ? this.acQ : this.abG;
         return CrystalAuraPlusPlus.CF(var7, var2, var5, var3);
      } else {
         return true;
      }
   }

   private void CE(AliceObf_up var1, BlockPos var2) {
      long var3 = System.nanoTime();
      if (var1 == AliceObf_up.wD) {
         this.abG = var2.toImmutable();
         this.aNE = var3
            + CrystalAuraPlusPlus.CH(
               (Integer)this.G.get(), (Integer)this.aKm.get()
            );
      } else {
         this.acQ = var2.toImmutable();
         this.ge = var3
            + CrystalAuraPlusPlus.CH(
               (Integer)this.GC.get(), (Integer)this.wk.get()
            );
      }
   }

   static boolean CF(BlockPos var0, BlockPos var1, long var2, long var4) {
      if (!(var1 != null)) {
         short var6 = 15021;
      }

      return false;
   }

   private static boolean CG(long var0, long var2) {
      if (!(var0 != 0L)) {
         short var4 = 7364;
      }

      return true;
   }

   static long CH(int var0, int var1) {
      int var2 = Math.max(0, Math.min(var0, var1));
      int var3 = Math.max(0, Math.max(var0, var1));
      long var4 = var2 == var3 ? var2 : ThreadLocalRandom.current().nextLong(var2, var3 + 1L);
      return var4 * 1000000L;
   }

   private static Vec3d CI(BlockPos var0) {
      return new Vec3d(var0.getX() + 0.5, var0.getY() + 1.0, var0.getZ() + 0.5);
   }

   private static Box CJ(BlockPos var0) {
      BlockPos var1 = var0.up();
      return new Box(
         var1.getX(), var1.getY(), var1.getZ(), var1.getX() + 1.0, var1.getY() + 2.0, var1.getZ() + 1.0
      );
   }

   static Box CK(BlockPos var0) {
      Vec3d var1 = CrystalAuraPlusPlus.CI(var0);
      double var2 = 1.0;
      return new Box(
         var1.x - var2, var1.y, var1.z - var2, var1.x + var2, var1.y + 2.0, var1.z + var2
      );
   }

   private static boolean CL(EndCrystalEntity var0, BlockPos var1) {
      Vec3d var2 = CrystalAuraPlusPlus.CI(var1);
      return var0.getEntityPos().squaredDistanceTo(var2) < 0.26;
   }

   private static Vec3d CM(BlockPos var0, Direction var1) {
      return new Vec3d(
         var0.getX() + 0.5 + var1.getOffsetX() * 0.5,
         var0.getY() + 0.5 + var1.getOffsetY() * 0.5,
         var0.getZ() + 0.5 + var1.getOffsetZ() * 0.5
      );
   }

   private static Vec3d CN(BlockPos var0, Direction var1, double var2, double var4) {
      Vec3d var6 = CrystalAuraPlusPlus.CM(var0, var1);
      int var7 = AliceObf_bV.Kp[var1.getAxis().ordinal()];
      Vec3d var10000;
      if (var7 == 1) {
         var10000 = var6.add(var2, 0.0, var4);
      } else if (var7 == 2) {
         var10000 = var6.add(var2, var4, 0.0);
      } else {
         if (var7 != 3) {
            throw new MatchException(null, null);
         }

         var10000 = var6.add(0.0, var4, var2);
      }

      return var10000;
   }

   public static Direction CO(BlockPos var0, Vec3d var1) {
      Object var2 = null;
      double var3 = Double.NEGATIVE_INFINITY;
      Direction[] var5 = Direction.values();
      int var6 = var5.length;
      byte var7 = 0;
      if (!(var7 < var6)) {
         char var12 = 48227;
      }

      return (Direction)var2;
   }

   static boolean CP(BlockPos var0, Direction var1, Vec3d var2) {
      int var3 = AliceObf_bV.aBW[var1.ordinal()] ^ -989545307;
      int var10000;
      if (var3 == -989545312) {
         var10000 = var2.x <= var0.getX() + 0.03 ? 1 : 0;
      } else if (var3 == -989545311) {
         var10000 = !(var2.z >= var0.getZ() + 1.0 - 0.03) ? 0 : 1;
      } else if (var3 != -989545309) {
         if (var3 != -989545308) {
            if (var3 == -989545306) {
               var10000 = var2.z <= var0.getZ() + 0.03 ? 12906 : 12907;
               boolean var10001 = false;
            } else {
               if (var3 != -989545305) {
                  throw new MatchException(null, null);
               }

               var10000 = var2.y >= var0.getY() + 1.0 - 0.03 ? 1 : 0;
            }
         } else {
            var10000 = !(var2.y <= var0.getY() + 0.03) ? 0 : 1;
         }
      } else {
         var10000 = var2.x >= var0.getX() + 1.0 - 0.03 ? 1 : 0;
      }

      return (var10000 != 0);
   }

   static double CQ(double var0, double var2, double var4) {
      return var0 + MathHelper.clamp(MathHelper.wrapDegrees(var2 - var0), -var4, var4);
   }

      static boolean CR(Vec3d var0, float var1, float var2, double var3, Box var5) {
      if (var5.contains(var0)) {
         return true;
      } else {
         Vec3d var6 = var0.add(Vec3d.fromPolar(var2, var1).multiply(var3));
         return var5.raycast(var0, var6).isPresent();
      }
   }

   static boolean CS(Vec3d var0, Vec3d var1, Box var2, double var3) {
      if (!var2.contains(var0)) {
         Vec3d var5 = var1.subtract(var0);
         double var6 = var5.length();
         if (!(var6 < 1.0E-6)) {
            Vec3d var8 = var0.add(var5.multiply(var3 / var6));
            return var2.raycast(var0, var8).isPresent();
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private static double[] CT(Vec3d var0) {
      Vec3d var1 = MinecraftClient.getInstance().player.getEyePos();
      double var2 = var0.x - var1.x;
      double var4 = var0.y - var1.y;
      double var6 = var0.z - var1.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      return new double[]{Math.toDegrees(Math.atan2(var6, var2)) - 90.0, -Math.toDegrees(Math.atan2(var4, var8))};
   }

   private static double CU(Vec3d var0, Box var1) {
      double var2 = Math.max(var1.minX - var0.x, Math.max(0.0, var0.x - var1.maxX));
      double var4 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var6 = Math.max(var1.minZ - var0.z, Math.max(0.0, var0.z - var1.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   private static double CV(PlayerEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   }

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
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Pre;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.KeybindSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.player.Rotations;
import net.minecraft.util.Hand;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.network.SequencedPacketCreator;

public final class AutoExpPlusPlus extends AliceModule implements AliceObf_jQ {
   private static final int bb = 200;
   private static final int Sb = 10;
   private static final MethodHandle jj = AutoExpPlusPlus.hh();
   private final SettingGroup RA = this.settings.getDefaultGroup();
   private final SettingGroup jF;
   private final SettingGroup ja;
   private final Setting<Keybind> hT;
   private final Setting<AliceObf_aS> adu;
   private final Setting<Integer> kc;
   private final Setting<Integer> axT;
   private final Setting<Boolean> bc;
   private final Setting<Boolean> nB;
   private final Setting<Integer> JY;
   private final Setting<Boolean> asr;
   private final Setting<Integer> aos;
   private final Setting<Integer> Bm;
   private final Setting<Boolean> ea;
   private final Setting<Boolean> OV;
   private final Setting<Integer> Ck;
   private final Setting<Integer> are;
   private final Setting<Boolean> ym;
   private final Setting<Boolean> Hd;
   private final Setting<Boolean> arS;
   private final Setting<Boolean> xC;
   private final Setting<Boolean> aCq;
   private final Setting<Boolean> Wx;
   private final Setting<Boolean> aCz;
   private final Setting<Boolean> ajV;
   private final AliceObf_iD aot;
   private final AliceObf_nv Sn;
   private boolean zM;
   private boolean Rg;
   private boolean rl;
   private boolean AU;
   private static AutoExpPlusPlus yY;
   private boolean lu;
   private long Ef;
   public AutoExpPlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "auto-exp++", "Tu dong nem binh kinh nghiem de Mending hoi giap ngay khi giap yeu, va dung lai khi giap da du.");
      this.jF = this.settings.createGroup("Trigger");
      this.ja = this.settings.createGroup("Safety");
      this.hT = this.RA
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("activate-key")).description("Phim tu kich hoat nem exp, khong can cho giap yeu.")).defaultValue(Keybind.none())).build()
         );
      this.adu = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("key-mode"))
                     .description("Toggle: nhan mot lan roi nem den khi du nguong. Hold: chi nem khi dang giu phim."))
                  .defaultValue(AliceObf_aS.Ii))
               .build()
         );
      this.kc = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("throws-per-tick"))
                     .description("So binh toi da trong mot tick; delay-ms va burst-sequential co the gioi han toc do."))
                  .defaultValue((4)))
               .min(1)
               .max((10))
               .sliderRange(1, (10))
               .build()
         );
      this.axT = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("delay-ms"))
                     .description("Khoang nghi toi thieu giua hai lan nem (mili-giay)."))
                  .defaultValue(0))
               .min(0)
               .max(1000)
               .sliderRange(0, (250))
               .build()
         );
      this.bc = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("burst-sequential"))
                     .description("Gui burst theo thu tu, ton trong delay-ms giua tung binh."))
                  .defaultValue(false))
               .build()
         );
      this.nB = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("look-down"))
                     .description("Nem xuong chan ma khong xoay camera; tu ho tro huong nem tren giao thuc cu."))
                  .defaultValue(true))
               .build()
         );
      this.JY = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("throw-pitch"))
                        .description("Pitch dung cho goi nem khi look-down bat (90 = thang xuong chan)."))
                     .defaultValue((90)))
                  .min(0)
                  .max(90)
                  .sliderRange((0), 90)
                  .visible(this.nB::get))
               .build()
         );
      this.asr = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("refill-hotbar"))
                     .description("Tu keo exp bottle tu inventory xuong hotbar khi hotbar het."))
                  .defaultValue(true))
               .build()
         );
      this.aos = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("refill-slot"))
                        .description("Slot hotbar (-8) danh cho exp bottle."))
                     .defaultValue(7))
                  .min(1)
                  .max(9)
                  .sliderRange((1), (9))
                  .visible(this.asr::get))
               .build()
         );
      this.Bm = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("keep-bottles"))
                     .description("Giu lai it nhat bao nhieu binh, khong nem het."))
                  .defaultValue(0))
               .min(0)
               .sliderRange((0), 64)
               .build()
         );
      this.ea = this.RA
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("chat-feedback"))
                     .description("Bao trong chat khi bat dau, khi dung, hoac khi het binh."))
                  .defaultValue(false))
               .build()
         );
      this.OV = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("auto-trigger"))
                     .description("Tu kich hoat khi do ben giap thap hon start-durability."))
                  .defaultValue(true))
               .build()
         );
      this.Ck = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("start-durability"))
                        .description("Do ben (%) thap nhat cua giap ma bat dau nem."))
                     .defaultValue((45)))
                  .min(1)
                  .max(99)
                  .sliderRange((1), 99)
                  .visible(this.OV::get))
               .build()
         );
      this.are = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("stop-durability"))
                     .description("Dung nem khi do ben (%) thap nhat da dat nguong nay. 100 = cho hoi day."))
                  .defaultValue((100)))
               .min(2)
               .max((100))
               .sliderRange(2, (100))
               .build()
         );
      this.ym = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("mending-only"))
                     .description("Chi tinh nhung mon co Mending; mon khong co Mending thi exp khong the va."))
                  .defaultValue(true))
               .build()
         );
      this.Hd = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("watch-helmet"))
                     .description("Theo doi do ben cua mu."))
                  .defaultValue(true))
               .build()
         );
      this.arS = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("watch-chestplate"))
                     .description("Theo doi do ben cua ao giap."))
                  .defaultValue(true))
               .build()
         );
      this.xC = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("watch-leggings"))
                     .description("Theo doi do ben cua quan giap."))
                  .defaultValue(true))
               .build()
         );
      this.aCq = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("watch-boots"))
                     .description("Theo doi do ben cua giay."))
                  .defaultValue(true))
               .build()
         );
      this.Wx = this.jF
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("watch-hands"))
                     .description("Tinh ca do ben cua item tren hai tay."))
                  .defaultValue(false))
               .build()
         );
      this.aCz = this.ja
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pause-on-screen"))
                     .description("Khong nem khi dang mo GUI."))
                  .defaultValue(true))
               .build()
         );
      this.ajV = this.ja
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("pause-while-using"))
                     .description("Khong nem khi dang an, dang gio khien hoac dang dung item khac."))
                  .defaultValue(true))
               .build()
         );
      this.aot = new AliceObf_iD(this);
      this.Sn = new AliceObf_nv();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.hf();
         if (AutoExpPlusPlus.jj == null) {
            this.error(
               "Khong tim thay sequenced packet sender cho ban Minecraft nay.",
               new Object[(0)]
            );
            this.toggle();
         } else {
            AliceObf_fS.Od(this);
            AutoExpPlusPlus.yY = this;
            super.onActivate();
         }
      }
   }

   public void onDeactivate() {
      if (AutoExpPlusPlus.yY == this) {
         AutoExpPlusPlus.yY = null;
      }

      AliceObf_fS.Oe(this);
      this.hf();
      super.onDeactivate();
   }

   @AliceHandler(
      priority = 90
   )
   private void gI(PlayerTickMovementEvent var1) {
      if (!this.gK()) {
         this.lu = false;
         this.aot.QV();
      }

      this.aot.QT();
   }

   static void gJ() {
      AutoExpPlusPlus var0 = AutoExpPlusPlus.yY;
      if (var0 != null && var0.lu) {
         if (!var0.gK()) {
            var0.lu = false;
            var0.aot.QV();
         } else {
            var0.hc();
         }
      }
   }

   private boolean gK() {
      if (this.isActive()) {
         int var10000 = super.mc.player != null ? 28125 : 28126;

         while (true) {
            int var2 = var10000;
            if (var2 == 28125) {
               if (super.mc.world != null) {
                  var10000 = super.mc.interactionManager != null ? 64884 : 64885;

                  while (true) {
                     int var3 = var10000;
                     if (var3 == 64884) {
                        if (super.mc.player.isAlive()
                           && (Boolean)this.asr.get()
                           && (this.zM || this.Rg)
                           && super.mc.currentScreen == null
                           && (!(Boolean)this.ajV.get() || !super.mc.player.isUsingItem())
                           && !AliceObf_fS.OZ()
                           && !super.mc.player.getOffHandStack().isOf(Items.EXPERIENCE_BOTTLE)
                           && this.ha() < 0
                           && this.gZ() > 0) {
                           if (this.zM
                              && !this.Rg
                              && this.adu.get() == AliceObf_aS.UF) {
                              if (((Keybind)this.hT.get()).isPressed()) {
                                 char var5 = 46604;
                              } else {
                                 char var6 = 46603;
                              }
                           }

                           int var1 = this.gW();
                           return !(var1 >= 0 && var1 >= this.are.get());
                        }

                        return false;
                     }

                     if (var3 != 64885) {
                        return false;
                     }

                  }
               }
               break;
            }

            if (var2 != 28126) {
               break;
            }

         }
      }

      return false;
   }

   @AliceHandler(
      priority = -10000
   )
   private void gL(Pre var1) {
      this.Sn.hs();
      if ((Boolean)this.nB.get()
         && AliceObf_nv.hn()
         && super.mc.player != null
         && super.mc.world != null
         && super.mc.interactionManager != null
         && (this.zM || this.Rg)
         && (!(Boolean)this.aCz.get() || super.mc.currentScreen == null)
         && (!(Boolean)this.ajV.get() || !super.mc.player.isUsingItem())
         && !Rotations.rotating
         && AliceObf_fS.OA(this)) {
         int var10000 = this.gZ() > 0 ? 54692 : 54693;

         while (true) {
            int var3 = var10000;
            if (var3 == 54692) {
               if (!super.mc.player.getOffHandStack().isOf(Items.EXPERIENCE_BOTTLE) && this.ha() < 0) {
                  break;
               }

               int var2 = this.gW();
               if (var2 >= 0 && var2 >= (Integer)this.are.get()) {
                  return;
               }

               this.Sn.ho(super.mc, ((Integer)this.JY.get()).intValue());
               return;
            }

            if (var3 != 54693) {
               break;
            }

         }
      }
   }

   @AliceHandler(
      priority = 10000
   )
   private void gM(Post var1) {
      this.Sn.hp(super.mc);
   }

   @AliceHandler(
      priority = 10000
   )
   private void gN(Render3DEvent var1) {
      this.Sn.hs();
   }

   @AliceHandler
   private void gO(GameLeftEvent var1) {
      this.hf();
   }

   @AliceHandler(
      priority = -50
   )
   private void gP(Post var1) {
      this.aot.QU();
   }

   @AliceHandler
   private void gQ(meteordevelopment.meteorclient.events.world.TickEvent.Post var1) {
      boolean var2 = this.Sn.hq(super.mc, ((Integer)this.JY.get()).intValue());
      this.Sn.hs();
      if (super.mc.player != null && super.mc.world != null && super.mc.interactionManager != null) {
         this.gT();
         int var3 = this.gW();
         this.gU(var3);
         if (var3 >= 0 && var3 >= (Integer)this.are.get()) {
            this.gV(
               "giap da du nguong, dung nem"
            );
         }

         if (!this.zM && !this.Rg) {
            this.aot.QV();
         } else if ((!(Boolean)this.aCz.get() || super.mc.currentScreen == null)
            && (!(Boolean)this.ajV.get() || !super.mc.player.isUsingItem())
            && !AliceObf_fS.OZ()) {
            long var4 = System.nanoTime();
            if ((Integer)this.axT.get() <= 0 || var4 >= this.Ef) {
               int var6 = this.gZ();
               if (var6 <= 0) {
                  this.gV(
                     "het binh kinh nghiem"
                  );
                  this.aot.QV();
               } else {
                  this.AU = false;
                  Hand var7 = Hand.MAIN_HAND;
                  int var8 = (-1);
                  if (super.mc.player.getOffHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                     var7 = Hand.OFF_HAND;
                  } else {
                     var8 = this.ha();
                     if (var8 < 0) {
                        if ((Boolean)this.asr.get()) {
                           this.hc();
                        }

                        return;
                     }
                  }

                  this.lu = false;
                  this.aot.QV();
                  int var9 = Math.min(var6, this.hb(var7, var8));
                  if (var9 > 0) {
                     if (!(Boolean)this.nB.get()
                        || !AliceObf_nv.hn()
                        || var2) {
                        Hand var10 = var7;
                        int var11 = var8;
                        int var12 = Math.min(var9, (Integer)this.kc.get());
                        AliceObf_fS.Pi(
                           this, () -> this.gR(var10, var11, var12)
                        );
                     }
                  }
               }
            }
         } else {
            this.lu = false;
            this.aot.QV();
         }
      } else {
         this.hf();
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void gR(Hand var1, int var2, int var3) {
      if (super.mc.player != null && super.mc.world != null) {
         byte var4 = 0;
         if (var1 == Hand.MAIN_HAND) {
            if (!AliceObf_fS.Of(this, var2)) {
               return;
            }

            var4 = 1;
         }

         boolean var9 = false /* VF: Semaphore variable */;

         label204: {
            label205: {
               try {
                  var9 = true;
                  int var5 = 0;

                  while (true) {
                     if (var5 >= var3) {
                        var9 = false;
                        break label204;
                     }

                     if (var5 > 0 && !AliceObf_fS.Ph(this)) {
                        var9 = false;
                        break label205;
                     }

                     if (!this.gS(var1)) {
                        var9 = false;
                        break;
                     }

                     int var10000 = this.axT.get() > 0 ? 7851 : 7852;

                     while (true) {
                        int var7 = var10000;
                        if (var7 == 7851) {
                           this.Ef = System.nanoTime() + ((Integer)this.axT.get()).intValue() * (1000000);
                           if ((Boolean)this.bc.get()) {
                              char var15 = 48705;
                           } else {
                              char var16 = 48706;
                           }
                        } else if (var7 == 7852) {

                           continue;
                        }

                        var5++;
                        break;
                     }
                  }
               } finally {
                  if (var9) {
                     if (var4 != 0) {
                        AliceObf_fS.Ov(this);
                     }
                  }
               }

               if (var4 != 0) {
                  AliceObf_fS.Ov(this);
               }

               return;
            }

            if (var4 != 0) {
               AliceObf_fS.Ov(this);
            }

            return;
         }

         if (var4 != 0) {
            AliceObf_fS.Ov(this);
         }
      }
   }

   private boolean gS(Hand var1) {
      if (AutoExpPlusPlus.jj != null
         && super.mc.player != null
         && super.mc.world != null) {
         float var2 = super.mc.player.getYaw();
         float var3 = this.nB.get() ? ((Integer)this.JY.get()).intValue() : super.mc.player.getPitch();
         if ((Boolean)this.nB.get()
            && AliceObf_nv.hn()
            && !AliceObf_nv.hr(var3)) {
            return false;
         } else {
            SequencedPacketCreator var4 = var3x -> new PlayerInteractItemC2SPacket(var1, var3x, var2, var3);
            int var5 = AliceObf_fS.PD();

            try {
               AutoExpPlusPlus.jj
                  .invoke((ClientPlayerInteractionManager)super.mc.interactionManager, (ClientWorld)super.mc.world, (SequencedPacketCreator)var4);
            } catch (Throwable var7) {
               return true;
            }

            return (AliceObf_fS.PD() <= var5);
         }
      } else {
         return false;
      }
   }

      private void gT() {
      boolean var1 = super.mc.currentScreen != null;
      boolean var2 = ((Keybind)this.hT.get()).isPressed();
      boolean var3 = !var1 && var2;
      if (this.adu.get() == AliceObf_aS.UF) {
         this.zM = var3;
      } else if (AutoExpPlusPlus.hg(var1, var2, this.rl)) {
         this.zM = !this.zM;
         this.hd(
            this.zM
               ? "bat bang phim"
               : "tat bang phim"
         );
      }

      this.rl = var2;
   }

   private void gU(int var1) {
      int var10000 = this.OV.get() ? 44479 : 44480;

      while (true) {
         int var2 = var10000;
         if (var2 == 44479) {
            if (var1 >= 0) {
               if (!this.Rg && var1 <= (Integer)this.Ck.get() && var1 < (Integer)this.are.get()) {
                  this.Rg = true;
                  this.hd(var1 + "");
               }

               return;
            }
            break;
         }

         if (var2 != 44480) {
            break;
         }

      }

      this.Rg = false;
   }

   private void gV(String var1) {
      int var2 = !this.zM && !this.Rg ? (0) : 1;
      this.Rg = false;
      if (this.adu.get() == AliceObf_aS.Ii) {
         this.zM = false;
      }

      if (var2 != 0 && !this.zM) {
         if (!(!this.Rg)) {
            char var3 = 57122;
         }
      }
   }

   private int gW() {
      if (super.mc.player == null) {
         return (-1);
      } else {
         int var1 = (-1);
         if ((Boolean)this.Hd.get()) {
            var1 = this.gX(var1, EquipmentSlot.HEAD);
         }

         if ((Boolean)this.arS.get()) {
            var1 = this.gX(var1, EquipmentSlot.CHEST);
         }

         if ((Boolean)this.xC.get()) {
            var1 = this.gX(var1, EquipmentSlot.LEGS);
         }

         if ((Boolean)this.aCq.get()) {
            var1 = this.gX(var1, EquipmentSlot.FEET);
         }

         if ((Boolean)this.Wx.get()) {
            var1 = this.gX(var1, EquipmentSlot.MAINHAND);
            var1 = this.gX(var1, EquipmentSlot.OFFHAND);
         }

         return var1;
      }
   }

   private int gX(int var1, EquipmentSlot var2) {
      ItemStack var3 = super.mc.player.getEquippedStack(var2);
      if (var3.isEmpty() || !var3.isDamageable() || var3.getMaxDamage() <= 0) {
         return var1;
      } else if ((Boolean)this.ym.get() && !this.gY(var3)) {
         return var1;
      } else {
         int var4 = (var3.getMaxDamage() - var3.getDamage()) * 100 / var3.getMaxDamage();
         return var1 < 0 ? var4 : Math.min(var1, var4);
      }
   }

   private boolean gY(ItemStack var1) {
      for (RegistryEntry var3 : EnchantmentHelper.getEnchantments(var1).getEnchantments()) {
         if (var3.matchesKey(Enchantments.MENDING)) {
            return true;
         }
      }

      return false;
   }

   private int gZ() {
      if (super.mc.player == null) {
         return 0;
      } else {
         int var1 = 0;

         for (int var2 = 0; var2 < super.mc.player.getInventory().size(); var2++) {
            ItemStack var3 = super.mc.player.getInventory().getStack(var2);
            if (var3.isOf(Items.EXPERIENCE_BOTTLE)) {
               var1 += var3.getCount();
            }
         }

         return var1 - (Integer)this.Bm.get();
      }
   }

   private int ha() {
      if (super.mc.player == null) {
         return (-1);
      } else {
         int var1 = (Integer)this.aos.get() - 1;
         if (super.mc.player.getInventory().getStack(var1).isOf(Items.EXPERIENCE_BOTTLE)) {
            return var1;
         } else {
            for (int var2 = 0; var2 < (9); var2++) {
               if (super.mc.player.getInventory().getStack(var2).isOf(Items.EXPERIENCE_BOTTLE)) {
                  return var2;
               }
            }

            return (-1);
         }
      }
   }

   private int hb(Hand var1, int var2) {
      if (super.mc.player == null) {
         return (0);
      } else {
         ItemStack var3 = var1 == Hand.OFF_HAND ? super.mc.player.getOffHandStack() : super.mc.player.getInventory().getStack(var2);
         return var3.isOf(Items.EXPERIENCE_BOTTLE) ? var3.getCount() : 0;
      }
   }

   private void hc() {
      this.lu = true;
      AliceObf_hE var1 = this.aot
         .QP(Items.EXPERIENCE_BOTTLE, (Integer)this.aos.get() - (1));
      if (var1 != AliceObf_hE.ayS) {
         this.lu = false;
      }

      if (!(var1 == AliceObf_hE.th)) {
         short var2 = 23662;
      }
   }

   private void hd(String var1) {
      if ((Boolean)this.ea.get()) {
         this.info(var1, new Object[0]);
      }
   }

   private void he(String var1) {
      if (!this.AU) {
         this.AU = true;
         this.hd(var1);
      }
   }

   private void hf() {
      this.lu = false;
      this.Ef = 0L;
      this.Sn.ht();
      this.zM = false;
      this.Rg = false;
      this.rl = false;
      this.AU = false;
      this.aot.QV();
   }

   static boolean hg(boolean var0, boolean var1, boolean var2) {
      if (!var0) {
         if (!(!var1)) {
            short var4 = 10970;
         }
      }

      return false;
   }

   public AliceObf_y prepareCombatIntent() {
      return null;
   }

   public boolean canYieldEpoch() {
      return true;
   }

   public boolean reservesLowerPriorityEpochs() {
      return false;
   }

   public int combatPriority() {
      return 200;
   }

   public void suspendForItemUse() {
      this.lu = false;
      this.aot.QV();
   }

   public void yieldToHigherPriority() {
      this.lu = false;
      this.aot.QV();
   }

   public boolean yieldToManualInput() {
      this.lu = false;
      this.aot.QV();
      return true;
   }

   private static MethodHandle hh() {
      Method[] var0 = ClientPlayerInteractionManager.class.getDeclaredMethods();
      int var1 = var0.length;

      for (int var2 = (0); var2 < var1; var2++) {
         Method var3 = var0[var2];
         Class[] var4 = var3.getParameterTypes();
         if (var3.getReturnType() == void.class) {
            if (var4.length != (2)) {

            } else {
               short var6 = 12310;
            }
         }
      }

      return null;
   }

   }

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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.IntSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.util.PlayerInput;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Vec2f;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;

public final class AutoShop extends AliceModule {
   private static final int ig = 13;
   private static final int ahQ = 10;
   private static final int pN = 16;
   private static final int arx = 9;
   private static final int aHV = 14;
   private static final int fs = 15;
   private static final int aeH = 16;
   private static final int jN = 23;
   private static final int aNJ = 100;
   private final SettingGroup aFL = this.settings.getDefaultGroup();
   private final Setting<Integer> Ym;
   private final Setting<Keybind> b;
   private final Setting<Integer> ahY;
   private final Setting<Keybind> aFr;
   private final Setting<Integer> aDz;
   private final Setting<Keybind> aMB;
   private final Setting<Integer> zj;
   private final Setting<Keybind> Qx;
   private final Setting<Integer> aNp;
   private final Setting<Keybind> aOf;
   private final Setting<Integer> akK;
   private final Setting<Boolean> aKf;
   private final Setting<AliceObf_jo> on;
   private final Queue<AliceObf_qR> ML;
   private AliceObf_rJ Vf;
   private AliceObf_qR AY;
   private int jr;
   private int jE;
   private boolean aPW;
   private AliceObf_N aPO;
   private ClientPlayerEntity PP;
   private AliceObf_pW Ze;
   public AutoShop() {
      super(AliceAddon.CATEGORY, "AutoShop", "Tu dong mua totem, crystal, exp, obsidian va ender pearl tu /shop open gear.");
      this.Ym = this.aFL
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("totem-amount")).description("So totem mua moi lan nhan phim.")).defaultValue(1))
               .min(1)
               .max(10)
               .sliderMax(10)
               .build()
         );
      this.b = this.aFL
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("totem-key"))
                     .description("Phim tu dong mua totem."))
                  .defaultValue(Keybind.none()))
               .action(
                  () -> this.nx(
                     AliceObf_bB.mV, (Integer)this.Ym.get()
                  )
               )
               .build()
         );
      this.ahY = this.aFL
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("crystal-stacks")).description("So stack end crystal mua moi lan nhan phim.")).defaultValue(1))
               .min(1)
               .max((10))
               .sliderMax((10))
               .build()
         );
      this.aFr = this.aFL
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("crystal-key"))
                     .description("Phim tu dong mua end crystal."))
                  .defaultValue(Keybind.none()))
               .action(
                  () -> this.nx(
                     AliceObf_bB.aQw, (Integer)this.ahY.get()
                  )
               )
               .build()
         );
      this.aDz = this.aFL
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("exp-stacks")).description("So stack exp bottle mua moi lan nhan phim."))
                  .defaultValue(1))
               .min(1)
               .max(10)
               .sliderMax((10))
               .build()
         );
      this.aMB = this.aFL
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("exp-key"))
                     .description("Phim tu dong mua exp bottle."))
                  .defaultValue(Keybind.none()))
               .action(
                  () -> this.nx(
                     AliceObf_bB.awt, (Integer)this.aDz.get()
                  )
               )
               .build()
         );
      this.zj = this.aFL
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("obsidian-stacks")).description("So stack obsidian mua moi lan nhan phim."))
                  .defaultValue(1))
               .min(1)
               .max((10))
               .sliderMax(10)
               .build()
         );
      this.Qx = this.aFL
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("obsidian-key"))
                     .description("Phim tu dong mua obsidian."))
                  .defaultValue(Keybind.none()))
               .action(
                  () -> this.nx(
                     AliceObf_bB.pC, (Integer)this.zj.get()
                  )
               )
               .build()
         );
      this.aNp = this.aFL
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("ender-pearl-stacks")).description("So stack ender pearl mua moi lan nhan phim."))
                  .defaultValue(1))
               .min(1)
               .max((10))
               .sliderMax(10)
               .build()
         );
      this.aOf = this.aFL
         .add(
            ((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)((meteordevelopment.meteorclient.settings.KeybindSetting.Builder)new meteordevelopment.meteorclient.settings.KeybindSetting.Builder()
                        .name("ender-pearl-key"))
                     .description("Phim tu dong mua ender pearl."))
                  .defaultValue(Keybind.none()))
               .action(
                  () -> this.nx(
                     AliceObf_bB.JG, (Integer)this.aNp.get()
                  )
               )
               .build()
         );
      this.akK = this.aFL
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("action-delay")).description("So tick giua cac lan click shop.")).defaultValue(4))
               .min(0)
               .max(20)
               .sliderMax(20)
               .build()
         );
      this.aKf = this.aFL
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("silent"))
                     .description("An GUI shop khi mua nhung van giu container de gui click."))
                  .defaultValue(false))
               .build()
         );
      this.on = this.aFL
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("buy-movement-mode"))
                     .description("StopMovingToBuy: tam dung input di chuyen mot chu ky de mua, sau do phuc hoi. WaitStopMovingToBuy: doi input nguoi choi dung yen roi moi mua."))
                  .defaultValue(AliceObf_jo.Fx))
               .build()
         );
      this.ML = new ConcurrentLinkedQueue<>();
      this.Vf = AliceObf_rJ.aAD;
      this.Ze = AliceObf_pW.lT;
      ScreenEvents.AFTER_INIT.register(new AliceObf_hJ(this));
   }

   public void onActivate() {
      int var10000 = !PremiumAccessGate.isAllowed() ? 22721 : 22722;

      while (true) {
         int var1 = var10000;
         if (var1 == 22721) {
            return;
         }

         if (var1 != 22722) {
            super.onActivate();
            this.nQ(false);
            return;
         }

      }
   }

   public void onDeactivate() {
      this.nJ();
      this.nQ(true);
      super.onDeactivate();
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @AliceHandler
   private void nu(Pre var1) {
      if (this.Vf != AliceObf_rJ.aAD) {
         if (super.mc.player != null && super.mc.world != null && super.mc.interactionManager != null) {
            int var10000 = super.mc.getNetworkHandler() == null ? 29818 : 29819;

            while (true) {
               int var3 = var10000;
               if (var3 == 29818) {
                  break;
               }

               if (var3 != 29819) {
                  if (this.Ze == AliceObf_pW.QO) {
                     this.nJ();
                     this.Ze = AliceObf_pW.RP;
                  }

                  this.nN();
                  if (this.nF() && !this.nG()) {
                     return;
                  }

                  if (this.jr < (Integer)this.akK.get()) {
                     this.jr += (1);
                     return;
                  }

                  this.jr = 0;
                  GenericContainerScreenHandler var2 = this.nM();
                  if (this.Vf != AliceObf_rJ.aL && var2 == null) {
                     this.nP("Shop GUI closed unexpectedly.");
                     return;
                  }

                  switch (this.Vf.ordinal()) {
                     case 0:
                     default:
                        break;
                     case 1:
                        this.nA(var2);
                        break;
                     case 2:
                        this.nB(var2);
                        break;
                     case 3:
                        this.nC();
                        break;
                     case 4:
                        this.setQuantity(var2);
                        break;
                     case 5:
                        this.nD(var2);
                  }

                  return;
               }

            }
         }

         this.nP("Player left world.");
      }
   }

   @AliceHandler
   private void nv(GameLeftEvent var1) {
      this.nQ(false);
   }

   public boolean nw() {
      return (this.Vf != AliceObf_rJ.aAD);
   }

   public synchronized void nx(AliceObf_bB var1, int var2) {
      if (this.isActive() && var1 != null && var2 > 0 && super.mc.player != null && super.mc.getNetworkHandler() != null) {
         if (this.Vf != AliceObf_rJ.aAD
            || super.mc.currentScreen == null) {
            this.ML.add(new AliceObf_qR(var1, var2));
            if (this.Vf == AliceObf_rJ.aAD) {
               this.ny();
            }
         }
      }
   }

   private void ny() {
      this.AY = (AliceObf_qR)this.ML.poll();
      int var10000 = this.AY == null ? 59177 : 59178;

      while (true) {
         int var1 = var10000;
         if (var1 == 59177) {
            this.Vf = AliceObf_rJ.aAD;
            return;
         }

         if (var1 != 59178) {
            this.jr = 0;
            this.jE = 0;
            this.aPW = false;
            this.nz();
            this.Vf = AliceObf_rJ.aL;
            return;
         }

      }
   }

   private void nz() {
      if (super.mc.player != null && super.mc.getNetworkHandler() != null) {
         super.mc
            .player
            .networkHandler
            .sendChatCommand("shop open gear");
      }
   }

   private void nA(GenericContainerScreenHandler var1) {
      if (var1 != null) {
         this.Vf = AliceObf_rJ.aoO;
         this.jE = (0);
      } else {
         this.jE++;
         if (this.jE % (20) == 0) {
            this.nz();
         }

         if (this.jE > 100) {
            this.nP(
               "Shop GUI never opened."
            );
         }
      }
   }

   private void nB(GenericContainerScreenHandler var1) {
      if (this.AY == null) {
         this.Vf = AliceObf_rJ.acc;
      } else {
         int var2 = AutoShop.nR(this.AY.awH);
         if (AutoShop.nS(var1, var2)) {
            this.nE(var1, var2);
            this.Vf = AliceObf_rJ.acc;
         } else {
            this.nP(var2 + "");
         }
      }
   }

   private void nC() {
      if (!(this.AY != null)) {
         char var1 = 45141;
      }

      if (this.ML.isEmpty()) {
         this.nO();
      } else {
         this.ny();
      }
   }

   private void setQuantity(GenericContainerScreenHandler var1) {
      if (this.AY == null) {
         this.Vf = AliceObf_rJ.acc;
      } else {
         int var2 = this.AY.awH != AliceObf_bB.JG ? 16 : 15;
         if (!AutoShop.nS(var1, var2)) {
            this.nP(var2 + "");
         } else {
            this.nE(var1, var2);
            this.aPW = true;
            this.Vf = AliceObf_rJ.eC;
         }
      }
   }

   private void nD(GenericContainerScreenHandler var1) {
      if (!AutoShop.nS(var1, (23))) {
         this.nP("Shop GUI does not contain confirm slot 23.");
      } else {
         this.nE(var1, (23));
         if (this.AY != null) {
            this.AY.aGZ -= (1);
         }

         this.Vf = AliceObf_rJ.acc;
      }
   }

   private void nE(GenericContainerScreenHandler var1, int var2) {
      super.mc.interactionManager.clickSlot(var1.syncId, var2, 0, SlotActionType.PICKUP, super.mc.player);
   }

   private boolean nF() {
      return !(this.Vf != AliceObf_rJ.aoO
            && this.Vf != AliceObf_rJ.lV
            && this.Vf != AliceObf_rJ.eC);
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean nG() {
      if (this.on.get() == AliceObf_jo.aQX) {
         return !(this.nH());
      } else {
         switch (this.Ze.ordinal()) {
            case 0:
               this.nI();
               this.Ze = AliceObf_pW.yB;
               return false;
            case 1:
            case 2:
               return false;
            case 3:
               this.Ze = AliceObf_pW.lT;
               return true;
            default:
               throw new IllegalStateException(
                  "Unknown buy movement phase"
               );
         }
      }
   }

   private boolean nH() {
      int var10000 = super.mc.player != null ? 44356 : 44357;

      while (true) {
         int var4 = var10000;
         if (var4 == 44356) {
            if (super.mc.player.input != null) {
               Input var1 = super.mc.player.input;
               Vec2f var2 = var1.getMovementInput();
               if (var2 != null && var2.lengthSquared() > 1.0E-5F) {
                  return true;
               }

               PlayerInput var3 = var1.playerInput;
               if (var3 != null) {
                  char var5 = 60500;
               } else {
                  char var6 = 60501;
               }

               return false;
            }
            break;
         }

         if (var4 != 44357) {
            break;
         }

      }

      return false;
   }

   private void nI() {
      ClientPlayerEntity var1 = super.mc.player;
      if (var1 != null && var1.input != null) {
         if (this.aPO == null || var1.input != this.aPO) {
            this.nJ();
            this.PP = var1;
            this.aPO = new AliceObf_N(this, var1.input);
            var1.input = this.aPO;
         }
      }
   }

   private void nJ() {
      if (this.aPO != null) {
         AliceObf_au.Fy(this.PP, this.aPO);
      }

      this.aPO = null;
      this.PP = null;
   }

   @AliceHandler(
      priority = -10000
   )
   private void nK(PlayerTickMovementEvent var1) {
      if (this.Ze == AliceObf_pW.yB) {
         this.nI();
      }
   }

   @AliceHandler(
      priority = 10000
   )
   private void nL(Post var1) {
      if (this.Ze == AliceObf_pW.QO) {
         this.nJ();
         this.Ze = AliceObf_pW.RP;
      }
   }

   private GenericContainerScreenHandler nM() {
      return super.mc.player != null && super.mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var1 ? var1 : null;
   }

   private void nN() {
      if ((Boolean)this.aKf.get() && super.mc.currentScreen != null && this.nM() != null) {
         int var10000 = super.mc.currentScreen instanceof HandledScreen ? 38847 : 38848;

         while (true) {
            int var1 = var10000;
            if (var1 == 38847) {
               super.mc.setScreen(null);
               break;
            }

            if (var1 != 38848) {
               break;
            }

         }
      }
   }

   private void nO() {
      this.nQ(true);
      this.info(
         "Done.",
         new Object[(0)]
      );
   }

   private void nP(String var1) {
      this.nQ(true);
      this.error(("" + var1), new Object[(0)]);
   }

   private void nQ(boolean var1) {
      this.nJ();
      this.Vf = AliceObf_rJ.aAD;
      this.ML.clear();
      this.AY = null;
      this.jr = (0);
      this.jE = (0);
      this.aPW = false;
      this.Ze = AliceObf_pW.lT;
      if (var1 && super.mc.player != null) {
         if (super.mc.player.currentScreenHandler == super.mc.player.playerScreenHandler) {
            if (super.mc.currentScreen != null) {
               super.mc.setScreen(null);
            }
         } else {
            super.mc.player.closeHandledScreen();
         }
      }
   }

   private static int nR(AliceObf_bB var0) {
      return switch (var0.ordinal() ^ 847783179) {
         case 847783176 -> 9;
         case 847783177 -> (16);
         case 847783178 -> 10;
         case 847783179 -> 13;
         case 847783183 -> (14);
         default -> throw new MatchException(null, null);
      };
   }

   private static boolean nS(GenericContainerScreenHandler var0, int var1) {
      return (var0 != null && var1 >= 0 && var1 < var0.slots.size());
   }

   public String getInfoString() {
      if (PremiumAccessGate.isAllowed()) {
         return this.AY == null
            ? null
            : this.AY.awH.name()
               + this.AY.aGZ
               + (
                  this.ML.isEmpty()
                     ? ""
                     : this.ML.size() + ""
               );
      } else {
         return null;
      }
   }

   }

package com.nnpg.alice.modules.movement;

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
import java.util.ArrayDeque;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Send;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import net.minecraft.network.packet.Packet;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

public final class GuiMovePlusPlus extends AliceModule {
   private static final int axQ = 6;
   private final SettingGroup asR = this.settings.getDefaultGroup();
   private final SettingGroup aOu;
   private final Setting<Boolean> agp;
   private final Setting<Boolean> aFS;
   private final Setting<Boolean> ayh;
   private final Setting<Boolean> lp;
   private final Setting<Boolean> aDA;
   private final ArrayDeque<Packet<?>> app;
   private int aCU;
   private Packet<?> ajg;
   private boolean aLn;
   private AliceObf_aN sl;
   private AliceObf_tX aPw;
   private ClientPlayerEntity aPb;
   private int Xc;
   public GuiMovePlusPlus() {
      super(AliceAddon.CATEGORY, "gui-move++", "Di chuyen / sprint khi mo GUI; click va vut do trong GUI ep dung 1 nhip de khong bi Grim huy.");
      this.aOu = this.settings.createGroup("Grim");
      this.agp = this.asR
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("move")).description("Giu phim di chuyen WASD khi mo GUI."))
                  .defaultValue(true))
               .build()
         );
      this.aFS = this.asR
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("jump")).description("Giu phim nhay (Space) khi mo GUI."))
                  .defaultValue(true))
               .build()
         );
      this.ayh = this.asR.add(((Builder)((Builder)((Builder)new Builder().name("sneak")).description("Giu phim sneak khi mo GUI.")).defaultValue(true)).build());
      this.lp = this.asR
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("sprint")).description("Giu sprint khi mo GUI (mo container khong ngat sprint)."))
                  .defaultValue(true))
               .build()
         );
      this.aDA = this.aOu
         .add(((Builder)((Builder)((Builder)new Builder().name("neutral-click")).description("Click / vut do (Q) trong GUI luc dang chay: giu click lai, ep dung 1 tick input trung tinh (nhu AutoTotem) roi gui click vao epoch sach sau CLIENT_TICK_END. Tranh Grim MultiActions C/D huy click. Dung yen thi click di thang.")).defaultValue(true)).build());
      this.app = new ArrayDeque<>();
      this.aCU = -1;
      this.sl = AliceObf_aN.aiG;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.Ud();
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.Ub(true);
      this.Ud();
      this.Uk();
      super.onDeactivate();
   }

   @AliceHandler
   private void TV(Pre var1) {
      if (!this.isActive() || super.mc.player == null || super.mc.world == null || super.mc.getWindow() == null) {
         this.Ud();
      } else if (!(super.mc.currentScreen instanceof HandledScreen)) {
         this.Ud();
      } else {
         if (this.aCU >= 0 && super.mc.player.currentScreenHandler != null && super.mc.player.currentScreenHandler.syncId != this.aCU) {
            this.Ud();
         }

         if ((Boolean)this.agp.get()) {
            this.Uj(super.mc.options.forwardKey);
            this.Uj(super.mc.options.backKey);
            this.Uj(super.mc.options.leftKey);
            this.Uj(super.mc.options.rightKey);
         }

         if ((Boolean)this.aFS.get()) {
            this.Uj(super.mc.options.jumpKey);
         }

         if ((Boolean)this.ayh.get()) {
            this.Uj(super.mc.options.sneakKey);
         }

         if ((Boolean)this.lp.get()) {
            this.Uj(super.mc.options.sprintKey);
         }

         if (!this.app.isEmpty()) {
            if (this.sl == AliceObf_aN.aKX && !this.Ub(false)) {
               this.sl = AliceObf_aN.aiG;
            }

            if (!this.app.isEmpty()) {

            } else {
               char var2 = 50801;
            }
         }

         if (this.sl != AliceObf_aN.aua) {
            this.Ui();
         }
      }
   }

   @AliceHandler
   private void TW(Post var1) {
      if (this.isActive()
         && super.mc.player != null
         && this.sl == AliceObf_aN.aKX) {
         if (super.mc.currentScreen instanceof HandledScreen) {
            if (this.app.isEmpty()) {
               this.sl = AliceObf_aN.aiG;
            } else {
               if (!this.Ub(false)) {
                  this.sl = AliceObf_aN.aiG;
               }
            }
         }
      }
   }

   @AliceHandler(
      priority = -10000
   )
   private void TX(PlayerTickMovementEvent var1) {
      if (this.sl == AliceObf_aN.aua) {
         this.Ue();
      }
   }

   @AliceHandler(
      priority = 10000
   )
   private void TY(meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post var1) {
      if (this.sl == AliceObf_aN.aEg) {
         if (this.Uh()) {
            if (!(super.mc.player != null)) {
               short var3 = 23864;
            }
         }

         byte var2 = 0;
         this.Uf();
         this.aPb = null;
         if (var2 != 0) {
            this.sl = AliceObf_aN.aKX;
         } else {
            this.sl = AliceObf_aN.aiG;
            this.Xc += (1);
         }
      } else if (this.sl == AliceObf_aN.aua) {
         this.Uf();
         this.aPb = null;
         this.sl = AliceObf_aN.aiG;
         this.Xc += (1);
      }
   }

   @AliceHandler
   private void TZ(Send var1) {
      if (var1.packet != this.ajg) {
         if (!this.isActive()) {

         } else {
            short var3 = 16836;
         }
      }
   }

   @AliceHandler(
      priority = -1000001
   )
   private void Ua(Sent var1) {
      if (var1.packet == this.ajg) {
         this.aLn = true;
      }
   }

   private boolean Ub(boolean var1) {
      if (!this.app.isEmpty() && super.mc.player != null && super.mc.getNetworkHandler() != null) {
         boolean var2 = AliceObf_pG.UV(super.mc.player);
         int var3 = (0);
         if (!var1 && !var2) {
            int var10 = 0;
            int var10000 = !AliceObf_fS.OS() ? 51617 : 51618;

            label222: {
               while (true) {
                  int var7 = var10000;
                  if (var7 == 51617) {
                     if (!AliceObf_fS.OV()) {
                        var10 = 0;
                        break label222;
                     }
                     break;
                  }

                  if (var7 != 51618) {
                     break;
                  }

               }

               var10 = 1;
            }

            var3 = var10;
            if (var3 == 0) {
               return false;
            }
         }

         int var4 = 0;

         try {
            while (!this.app.isEmpty()) {
               Packet var5 = (Packet)this.app.peekFirst();
               if (!this.Uc(var5)) {
                  char var11 = 35695;
               } else {
                  char var12 = 35696;
               }

               this.app.pollFirst();
               var4 = (1);
            }
         } finally {
            if (var3 != 0 && var4 == 0) {
               AliceObf_fS.OU();
            }
         }

         if (!this.app.isEmpty()) {
            this.sl = AliceObf_aN.aiG;
         } else {
            this.aCU = -1;
            this.Xc = (0);
            this.sl = AliceObf_aN.aiG;
         }

         return (var4 != 0);
      } else {
         return false;
      }
   }

   private boolean Uc(Packet<?> var1) {
      this.ajg = var1;
      this.aLn = false;

      byte var3;
      try {
         super.mc.getNetworkHandler().sendPacket(var1);
         return this.aLn;
      } catch (Throwable var7) {
         var3 = 0;
      } finally {
         this.ajg = null;
      }

      return (var3 != 0);
   }

   private void Ud() {
      this.app.clear();
      this.aCU = (-1);
      this.ajg = null;
      this.aLn = false;
      this.Uf();
      this.aPb = null;
      this.Xc = 0;
      this.sl = AliceObf_aN.aiG;
   }

   private void Ue() {
      ClientPlayerEntity var1 = super.mc.player;
      if (var1 != null && !var1.hasVehicle()) {
         if (this.aPw == null || var1.input != this.aPw) {
            this.aPw = new AliceObf_tX(this, var1.input);
            var1.input = this.aPw;
         }
      }
   }

   private void Uf() {
      int var10000 = this.aPw != null ? 36630 : 36631;

      while (true) {
         int var1 = var10000;
         if (var1 == 36630) {
            AliceObf_au.Fy(super.mc.player, this.aPw);
            break;
         }

         if (var1 != 36631) {
            break;
         }

      }

      this.aPw = null;
   }

   private boolean Ug() {
      ClientPlayerEntity var1 = super.mc.player;
      if (var1 != null && !var1.isSprinting()) {
         PlayerInput var2 = var1.getLastPlayerInput();
         return (boolean)(var2 == null || !var2.forward() && !var2.backward() && !var2.left() && !var2.right() && !var2.jump()
            ? this.Uh()
            : (0));
      } else {
         return false;
      }
   }

   private boolean Uh() {
      ClientPlayerEntity var1 = super.mc.player;
      if (var1 != null && !var1.isSprinting()) {
         Input var2 = var1.input;
         if (var2 == null) {
            return true;
         } else {
            Vec2f var3 = var2.getMovementInput();
            if (var3 != null) {
               int var10000 = !(var3.lengthSquared() > 1.0E-5F) ? 13256 : 13255;

               while (true) {
                  int var5 = var10000;
                  if (var5 == 13255) {
                     return false;
                  }

                  if (var5 != 13256) {
                     break;
                  }

               }
            }

            PlayerInput var4 = var2.playerInput;
            return !(var4 != null && (var4.forward() || var4.backward() || var4.left() || var4.right() || var4.jump()));
         }
      } else {
         return false;
      }
   }

   private void Ui() {
      if ((Boolean)this.lp.get() && (Boolean)this.agp.get() && super.mc.player != null) {
         if (super.mc.options.forwardKey.isPressed() && !super.mc.options.backKey.isPressed()) {
            if (!super.mc.player.isSneaking() && !super.mc.options.sneakKey.isPressed()) {
               if (super.mc.player.isUsingItem()) {

               } else {
                  char var1 = 53790;
               }

               if (!super.mc.player.hasVehicle() && !super.mc.player.isGliding()) {
                  if (super.mc.player.getAbilities().allowFlying || super.mc.player.getHungerManager().getFoodLevel() > 6) {
                     if (!super.mc.player.horizontalCollision) {
                        if (!super.mc.player.isSprinting()) {
                           super.mc.player.setSprinting(true);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void Uj(KeyBinding var1) {
      if (var1 != null) {
         boolean var2 = this.Ul(var1);
         if (var1.isPressed() != var2) {
            var1.setPressed(var2);
         }
      }
   }

   private void Uk() {
      if (super.mc != null && super.mc.getWindow() != null && super.mc.options != null) {
         try {
            this.Uj(super.mc.options.forwardKey);
            this.Uj(super.mc.options.backKey);
            this.Uj(super.mc.options.leftKey);
            this.Uj(super.mc.options.rightKey);
            this.Uj(super.mc.options.jumpKey);
            this.Uj(super.mc.options.sneakKey);
            this.Uj(super.mc.options.sprintKey);
         } catch (Throwable var2) {
         }
      }
   }

   private boolean Ul(KeyBinding var1) {
      if (var1 == null) {
         return false;
      } else {
         try {
            String var2 = var1.getBoundKeyTranslationKey();
            if (var2 == null) {
               return var1.isPressed();
            } else {
               long var3 = super.mc.getWindow().getHandle();
               if (var2.startsWith(
                  "key.mouse."
               )) {
                  int var7 = InputUtil.fromTranslationKey(var2).getCode();
                  return !(GLFW.glfwGetMouseButton(var3, var7) != 1);
               } else {
                  int var5 = InputUtil.fromTranslationKey(var2).getCode();
                  return (boolean)(var5 <= 0 ? 0 : InputUtil.isKeyPressed(super.mc.getWindow(), var5));
               }
            }
         } catch (Throwable var6) {
            return var1.isPressed();
         }
      }
   }

   public String getInfoString() {
      if (PremiumAccessGate.isAllowed()) {
         if (super.mc.player == null || !(super.mc.currentScreen instanceof HandledScreen)) {
            return null;
         } else {
            return this.app.isEmpty()
                  && this.sl == AliceObf_aN.aiG
               ? "move"
               : "neutral";
         }
      } else {
         return null;
      }
   }

   }

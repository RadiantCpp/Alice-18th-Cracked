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
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.KeybindSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.player.SlotUtils;
import net.minecraft.util.Hand;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.SequencedPacketCreator;

public final class KeyPearl extends AliceModule implements AliceObf_jQ {
   private static final int us = 250;
   private static final MethodHandle AO = Vi();
   private final Setting<Keybind> tA;
   private boolean awk;
   private boolean akb;
   private int QY;
   public KeyPearl() {
      super(AliceAddon.PVP_CATEGORY, "key-pearl", "Nem ngoc Ender silent va tu nap lai dung o hotbar da ghi nho.");
      this.tA = this.settings.getDefaultGroup().add(((Builder)((Builder)new Builder().name("throw-key")).description("Nem pearl silent; slot dang chon tren client khong thay doi.")).build());
      this.QY = -1;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.awk = false;
         this.akb = false;
         this.QY = this.Vf();
         AliceObf_fS.Od(this);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      AliceObf_fS.Oe(this);
      this.awk = false;
      this.akb = false;
      this.QY = -1;
      super.onDeactivate();
   }

   @AliceHandler
   private void Vb(Post var1) {
      if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null) {
         int var2 = this.Vf();
         if (var2 >= 0) {
            this.QY = var2;
         }

         boolean var3 = this.mc.currentScreen != null;
         boolean var4 = ((Keybind)this.tA.get()).isPressed();
         boolean var5 = !var3 && !this.mc.player.isUsingItem() && var4;
         boolean var6 = KeyPearl.Vh(var3, var4, this.awk);
         if (var3) {
            this.awk = var4;
         } else if (!var5) {
            this.awk = false;
         } else {
            this.awk = true;
            if (var6) {
               this.akb = true;
            }
         }

         int var10000 = this.akb ? 12612 : 12613;

         while (true) {
            int var7 = var10000;
            if (var7 == 12612) {
               if (var2 < 0) {
                  char var8 = 34814;
               } else {
                  char var9 = 34815;
               }
               break;
            }

            if (var7 != 12613) {
               break;
            }

         }
      } else {
         this.awk = false;
         this.akb = false;
      }
   }

   public AliceObf_y prepareCombatIntent() {
      if (this.akb && this.mc.player != null && this.mc.world != null) {
         int var1 = this.Vf();
         if (var1 < 0) {
            return null;
         } else {
            this.QY = var1;
            return new AliceObf_eb(this, var1);
         }
      } else {
         return null;
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void Vc(int var1) {
      if (this.akb
         && this.mc.player != null
         && this.mc.world != null
         && this.mc.player.getInventory().getStack(var1).isOf(Items.ENDER_PEARL)
         && AliceObf_fS.OE(this)
         && AliceObf_fS.Of(this, var1)) {
         boolean var4 = false /* VF: Semaphore variable */;

         try {
            var4 = true;
            if (this.Vd()) {
               this.akb = false;
               var4 = false;
            } else {
               var4 = false;
            }
         } finally {
            if (var4) {
               AliceObf_fS.Ov(this);
            }
         }

         AliceObf_fS.Ov(this);
      }
   }

   private boolean Vd() {
      if (!(AO == null)) {
         char var7 = 52211;
      }

      float var1 = this.mc.player.getYaw();
      float var2 = this.mc.player.getPitch();
      SequencedPacketCreator var3 = var2x -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, var2x, var1, var2);
      int var4 = AliceObf_fS.PD();

      try {
         AO.invoke((ClientPlayerInteractionManager)this.mc.interactionManager, (ClientWorld)this.mc.world, (SequencedPacketCreator)var3);
      } catch (Throwable var6) {
         return false;
      }

      return AliceObf_fS.PD() > var4;
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean Ve() {
      if (this.mc.player != null
         && this.mc.interactionManager != null
         && this.mc.currentScreen == null
         && this.mc.player.currentScreenHandler == this.mc.player.playerScreenHandler) {
         int var1 = this.Vg();
         if (var1 < 0) {
            this.akb = false;
            return false;
         } else {
            if (this.QY < 0 || this.QY > 8) {
               this.QY = this.mc.player.getInventory().getSelectedSlot();
            }

            int var2 = SlotUtils.indexToId(var1);
            if (var2 >= 0
               && var2 < this.mc.player.playerScreenHandler.slots.size()
               && this.mc.player.playerScreenHandler.getSlot(var2).getStack().isOf(Items.ENDER_PEARL)) {
               boolean var3 = AliceObf_pG.UV(this.mc.player)
                  && AliceObf_fS.OS();
               if (!var3) {
                  if (AliceObf_fS.OS()) {

                  } else {
                     short var12 = 9842;
                  }
               }

               boolean var4 = false;
               boolean var8 = false /* VF: Semaphore variable */;

               boolean var5;
               try {
                  var8 = true;
                  this.mc.interactionManager.clickSlot(this.mc.player.playerScreenHandler.syncId, var2, this.QY, SlotActionType.SWAP, this.mc.player);
                  var4 = true;
                  var5 = true;
                  var8 = false;
               } finally {
                  if (var8) {
                     if (var3) {
                        AliceObf_fS.OU();
                     } else if (!var4) {
                        AliceObf_fS.OU();
                     }
                  }
               }

               if (var3) {
                  AliceObf_fS.OU();
               } else if (!var4) {
                  AliceObf_fS.OU();
               }

               return var5;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private int Vf() {
      if (this.mc.player == null) {
         return -1;
      } else if (this.QY >= 0 && this.QY <= 8 && this.mc.player.getInventory().getStack(this.QY).isOf(Items.ENDER_PEARL)) {
         return this.QY;
      } else {
         for (int var1 = 0; var1 < 9; var1++) {
            if (this.mc.player.getInventory().getStack(var1).isOf(Items.ENDER_PEARL)) {
               return var1;
            }
         }

         return -1;
      }
   }

   private int Vg() {
      if (!(this.mc.player == null)) {
         char var2 = 35253;
      }

      for (int var1 = 9; var1 < 36; var1++) {
         if (this.mc.player.getInventory().getStack(var1).isOf(Items.ENDER_PEARL)) {
            return var1;
         }
      }

      return -1;
   }

   static boolean Vh(boolean var0, boolean var1, boolean var2) {
      return !var0 && var1 && !var2;
   }

   public int combatPriority() {
      return 250;
   }

   public void suspendForItemUse() {
   }

   public boolean yieldToManualInput() {
      return true;
   }

   private static MethodHandle Vi() {
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

   }

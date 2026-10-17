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
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import net.minecraft.client.network.ClientPlayerEntity;

public final class FreezePlayer extends AliceModule {
   private ClientPlayerEntity Ue;
   private double FC;
   private double kQ;
   private double aee;
   public FreezePlayer() {
      super(AliceAddon.PVP_CATEGORY, "Freeze", "Giu nguoi choi tai vi tri hien tai khi module duoc bat.");
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.Ue = null;
         super.onActivate();
      }
   }

   public void onDeactivate() {
      if (this.mc.player != null) {
         this.mc.player.setVelocity(0.0, 0.0, 0.0);
      }

      this.Ue = null;
      super.onDeactivate();
   }

   @AliceHandler
   private void Nx(Pre var1) {
      ClientPlayerEntity var2 = this.mc.player;
      if (var2 == null || this.mc.world == null) {
         this.Ue = null;
      } else if (this.Ue == var2) {
         var2.setVelocity(0.0, 0.0, 0.0);
         var2.setPosition(this.FC, this.kQ, this.aee);
      } else {
         this.Ue = var2;
         this.FC = var2.getX();
         this.kQ = var2.getY();
         this.aee = var2.getZ();
      }
   }

   }

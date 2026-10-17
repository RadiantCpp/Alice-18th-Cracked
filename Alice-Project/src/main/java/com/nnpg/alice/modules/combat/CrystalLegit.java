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
import java.util.Random;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.hit.BlockHitResult;
public final class CrystalLegit extends AliceModule implements AliceObf_jQ {
   private static final int nG = 210;
   private final SettingGroup oi = this.settings.getDefaultGroup();
   private final Setting<Boolean> aGN;
   private final Setting<Double> aMc;
   private final Setting<Double> aIo;
   private final Setting<Boolean> T;
   private final Setting<Boolean> aLv;
   private final Setting<Boolean> rR;
   private static final double aIl = 10.0;
   private static final double Tw = 25.0;
   private final Random NZ;
   private int akF;
   private int Hu;
   private int YJ;
   private boolean azo;
   private boolean hB;
   private long nE;
   private final AliceObf_iD fx;
   private int api;
   public CrystalLegit() {
      super(AliceAddon.PVP_CATEGORY, "Crystal Macro", "dat va dap crystal khi ban giu chuot phai");
      this.aGN = this.oi.add(((Builder)((Builder)((Builder)new Builder().name("sync")).description("cho server xac nhan truoc khi dat/pha")).defaultValue(false)).build());
      this.aMc = this.oi
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("min-cps"))
                  .description("toc do pha cham nhat"))
               .defaultValue(8.0)
               .min(1.0)
               .max(20.0)
               .sliderRange(1.0, 20.0)
               .build()
         );
      this.aIo = this.oi
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("max-cps"))
                  .description("toc do pha nhanh nhat"))
               .defaultValue(12.0)
               .min(1.0)
               .max(20.0)
               .sliderRange(1.0, 20.0)
               .build()
         );
      this.T = this.oi.add(((Builder)((Builder)((Builder)new Builder().name("place-obsidian")).description("dat obsidian truoc khi ban khong nhin vao obsidian hoac bedrock")).defaultValue(false)).build());
      this.aLv = this.oi.add(((Builder)((Builder)((Builder)new Builder().name("stop-on-kill")).description("tam dung khi co nguoi chet gan do de tranh lam no do")).defaultValue(true)).build());
      this.rR = this.oi.add(((Builder)((Builder)((Builder)new Builder().name("auto-refill-crystal")).description("tu dong bom crystal vao hotbar")).defaultValue(false)).build());
      this.NZ = new Random();
      this.fx = new AliceObf_iD(this);
      this.api = -1;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         AliceObf_fS.Od(this);
         super.onActivate();
         this.EV();
         this.EW();
      }
   }

   public void onDeactivate() {
      AliceObf_fS.Oe(this);
      super.onDeactivate();
      this.EV();
   }

   private void EV() {
      this.akF = 0;
      this.Hu = 0;
      this.azo = false;
      this.hB = false;
      this.nE = 0L;
      this.fx.QV();
      this.api = -1;
   }

   private void EW() {
      double var1 = (Double)this.aMc.get();
      double var3 = (Double)this.aIo.get();
      if (var1 > var3) {
         double var5 = var1;
         var1 = var3;
         var3 = var5;
      }

      double var7 = var1 + (var3 - var1) * this.NZ.nextDouble();
      this.YJ = Math.max(1, (int)(20.0 / var7));
   }

   @AliceHandler(
      priority = 100
   )
   private void EX(PlayerTickMovementEvent var1) {
      if (!AliceObf_fS.OA(this)) {

      } else {
         char var2 = 43214;
      }

      this.fx.QT();
   }

   @AliceHandler(
      priority = -200
   )
   private void EY(Post var1) {
      this.fx.QU();
   }

   @AliceHandler
   private void EZ(Receive var1) {
      if ((Boolean)this.aLv.get()) {
         if (var1.packet instanceof EntityStatusS2CPacket var2) {
            if (var2.getStatus() == 3) {
               if (this.mc.world != null && this.mc.player != null) {
                  Entity var4 = var2.getEntity(this.mc.world);
                  if (var4 != null && var4 != this.mc.player) {
                     if (!(var4.distanceTo(this.mc.player) > 10.0)) {
                        this.nE = System.currentTimeMillis() + 1500L;
                     }
                  }
               }
            }
         }
      }
   }

   @AliceHandler
   private void Fa(Pre var1) {
      if (this.mc.player != null && this.mc.currentScreen == null) {
         this.Ff();
         if (!this.mc.options.useKey.isPressed()) {
            this.azo = false;
            this.Hu = 0;
            this.fx.QV();
         } else {
            if (!this.azo) {
               this.azo = true;
               this.Hu = 0;
            }
         }
      } else {
         this.azo = false;
         this.Hu = 0;
         this.fx.QV();
      }
   }

   public AliceObf_y prepareCombatIntent() {
      this.hB = false;
      if (this.Fb()) {
         this.hB = true;
         int var1 = this.Fm(Items.END_CRYSTAL);
         if (var1 >= 0) {
            this.api = var1;
         }

         if ((Boolean)this.rR.get()) {
            if (var1 >= 0) {
               this.fx.QV();
            } else if (this.api >= 0) {
               AliceObf_hE var3 = this.fx.QM(Items.END_CRYSTAL, this.api);
               if (var3 == AliceObf_hE.ayS) {
                  return null;
               }

               if (var3 instanceof AliceObf_bf var4) {
                  this.api = var4.QK();
                  InvUtils.swap(var4.QK(), false);
               }
            } else {
               this.fx.QV();
            }
         } else {
            this.fx.QV();
         }

         if (this.mc.player.getOffHandStack().isOf(Items.END_CRYSTAL)) {

         } else {
            short var5 = 22402;
         }

         if (this.mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
            Hand var2 = Hand.MAIN_HAND;
            return this.Fc(var2, this.mc.crosshairTarget);
         } else {
            return null;
         }
      } else {
         this.fx.QV();
         return null;
      }
   }

   private boolean Fb() {
      if (this.mc.player != null && this.mc.currentScreen == null) {
         if (!(!this.azo)) {
            short var1 = 3781;
         }
      }

      return false;
   }

   private AliceObf_y Fc(Hand var1, HitResult var2) {
      return new AliceObf_ob(this, var2, var1);
   }

   private boolean Fd(boolean var1) {
      return var1
         ? AliceObf_fS.Ph(this)
         : AliceObf_fS.OE(this);
   }

   private EndCrystalEntity Fe() {
      if (this.mc.world != null && this.mc.player != null) {
         EndCrystalEntity var1 = null;
         double var2 = Double.MAX_VALUE;

         for (Entity var5 : this.mc.world.getEntities()) {
            if (var5 instanceof EndCrystalEntity var6) {
               if (var6.isRemoved()) {

               } else {
                  char var9 = 50187;
               }

               double var7 = this.mc.player.squaredDistanceTo(var6);
               if (var7 <= 25.0 && var7 < var2) {
                  var1 = var6;
                  var2 = var7;
               }
            }
         }

         return var1;
      } else {
         return null;
      }
   }

   private void Ff() {
      if (this.akF > 0) {
         this.akF--;
      }

      if (this.Hu > 0) {
         this.Hu--;
      }
   }

   private void Fg(BlockHitResult var1, Hand var2, boolean var3) {
      if (var1.getType() == HitResult.Type.BLOCK) {
         BlockPos var4 = var1.getBlockPos();
         BlockState var5 = this.mc.world.getBlockState(var4);
         boolean var6 = var5.isOf(Blocks.OBSIDIAN) || var5.isOf(Blocks.BEDROCK);
         if (!(!var6)) {
            char var7 = 48076;
         }

         if (this.Fk(var4)) {
            if (!this.Fl(var4.up())) {
               if (this.Fd(var3)) {
                  if (this.Fj(var2, var1)) {
                     this.mc.player.swingHand(var2);
                  }
               }
            }
         }
      }
   }

   private void Fh(BlockHitResult var1, Hand var2, boolean var3) {
      if (!(this.mc.player != null)) {
         char var8 = 53883;
      }
   }

   private boolean Fi(Entity var1, boolean var2) {
      if (this.akF > 0) {
         return false;
      } else if (this.mc.player == null || this.mc.interactionManager == null) {
         return false;
      } else if (var1 == null || var1.isRemoved() || !var1.isAlive()) {
         return false;
      } else if (!(var1 instanceof EndCrystalEntity) && !(var1 instanceof SlimeEntity)) {
         return false;
      } else if (this.Fl(var1.getBlockPos())) {
         return false;
      } else if (this.Fd(var2)) {
         this.mc.interactionManager.attackEntity(this.mc.player, var1);
         this.mc.player.swingHand(Hand.MAIN_HAND);
         this.akF = this.YJ;
         this.EW();
         return true;
      } else {
         return false;
      }
   }

   private boolean Fj(Hand var1, BlockHitResult var2) {
      if (this.mc.player != null && this.mc.interactionManager != null) {
         int var3 = AliceObf_fS.PD();
         ActionResult var4 = this.mc.interactionManager.interactBlock(this.mc.player, var1, var2);
         return var4.isAccepted() && AliceObf_fS.PD() > var3;
      } else {
         return false;
      }
   }

   private boolean Fk(BlockPos var1) {
      if (this.mc.world == null) {
         return false;
      } else {
         BlockPos var2 = var1.up();
         if (!this.mc.world.isAir(var2)) {
            return false;
         } else {
            if (!(Boolean)this.aGN.get()) {

            } else {
               short var6 = 24168;
            }

            int var3 = var2.getX();
            int var4 = var2.getY();
            int var5 = var2.getZ();
            return this.mc.world.getOtherEntities(null, new Box(var3, var4, var5, var3 + 1.0, var4 + 2.0, var5 + 1.0)).isEmpty();
         }
      }
   }

   private boolean Fl(BlockPos var1) {
      if (this.mc.world != null) {
         int var10000 = var1 == null ? 14925 : 14926;

         while (true) {
            int var7 = var10000;
            if (var7 == 14925) {
               break;
            }

            if (var7 != 14926) {
               Box var2 = new Box(
                  var1.getX() - 10.0,
                  var1.getY() - 10.0,
                  var1.getZ() - 10.0,
                  var1.getX() + 10.0,
                  var1.getY() + 10.0,
                  var1.getZ() + 10.0
               );

               for (Entity var4 : this.mc.world.getOtherEntities(null, var2)) {
                  if (var4 instanceof ItemEntity var5) {
                     ItemStack var6 = var5.getStack();
                     if (!var6.isEmpty()) {
                        if (var6.isOf(Items.ELYTRA)) {
                           return true;
                        }

                        if (var6.isIn(ItemTags.SWORDS)) {
                           return true;
                        }

                        if (!var6.isIn(ItemTags.HEAD_ARMOR)) {
                           if (!var6.isIn(ItemTags.CHEST_ARMOR)) {
                              char var8 = 38793;
                           } else {
                              char var9 = 38794;
                           }
                        }

                        return true;
                     }
                  }
               }

               return false;
            }

         }
      }

      return false;
   }

   public int combatPriority() {
      return 210;
   }

   public boolean canFollowEmergencyOffhandSwap() {
      return true;
   }

   public void suspendForItemUse() {
      this.hB = false;
      this.fx.QV();
   }

   public boolean usesExclusiveCombatTurn() {
      return true;
   }

   public boolean hasExclusiveCombatWork() {
      return this.hB;
   }

   public void yieldToHigherPriority() {
      this.fx.QV();
   }

   public boolean yieldToManualInput() {
      if (this.mc.currentScreen == null) {
         int var10000 = !this.mc.options.useKey.isPressed() ? 64195 : 64194;

         while (true) {
            int var1 = var10000;
            if (var1 == 64194) {
               if (this.mc.options.attackKey.isPressed()) {
                  char var2 = 33878;
               } else {
                  char var3 = 33879;
               }

               return false;
            }

            if (var1 != 64195) {
               break;
            }

         }
      }

      this.fx.QV();
      return true;
   }

   private int Fm(Item var1) {
      if (this.mc.player == null) {
         return -1;
      } else {
         for (int var2 = 0; var2 < 9; var2++) {
            if (this.mc.player.getInventory().getStack(var2).isOf(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   }

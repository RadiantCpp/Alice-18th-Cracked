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
import meteordevelopment.meteorclient.events.entity.player.DoItemUseEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.KeybindSetting.Builder;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import org.lwjgl.glfw.GLFW;

public final class AnchorMacro extends AliceModule implements AliceObf_jQ {
   private final SettingGroup amm = this.settings.getDefaultGroup();
   private final Setting<Keybind> JQ;
   private final Setting<Integer> yG;
   private final Setting<Integer> qE;
   private final Setting<Integer> anc;
   private final Setting<Integer> BG;
   private final Setting<Boolean> NG;
   private boolean TR;
   private AliceObf_tG iu;
   private ClientWorld mK;
   private BlockPos jb;
   private BlockHitResult Xy;
   private int UO;
   private boolean aEX;
   private int apv;
   private int Cb;
   private boolean cf;
   public AnchorMacro() {
      super(AliceAddon.PVP_CATEGORY, "anchor-macro", "Tu dong thuc hien chuoi thao tac voi mo neo hoi sinh theo do tre da thiet lap.");
      this.JQ = this.amm.add(((Builder)((Builder)((Builder)new Builder().name("active-key")).description("Phim kich hoat anchor 1 vong lap.")).defaultValue(Keybind.none())).build());
      this.yG = this.amm
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("explode-slot"))
                     .description("Slot hotbar (-8) de switch khi kich no anchor."))
                  .defaultValue(1))
               .min(1)
               .max(9)
               .sliderMax(9)
               .build()
         );
      this.qE = this.amm
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("place-delay"))
                     .description("So tick cho truoc khi dat anchor."))
                  .defaultValue(0))
               .min(0)
               .sliderRange(0, 20)
               .build()
         );
      this.anc = this.amm
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("charge-delay"))
                     .description("So tick cho sau khi dat va truoc khi nap glowstone."))
                  .defaultValue(0))
               .min(0)
               .sliderRange(0, 20)
               .build()
         );
      this.BG = this.amm
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("explode-delay"))
                     .description("So tick cho sau khi nap va truoc khi kich no anchor."))
                  .defaultValue(0))
               .min(0)
               .sliderRange(0, 20)
               .build()
         );
      this.NG = this.amm
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("switch-back"))
                     .description("Tu dong switch ve anchor sau khi no."))
                  .defaultValue(false))
               .build()
         );
      this.Cb = -1;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.resetMacroState();
         AliceObf_fS.Od(this);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      AliceObf_fS.Oe(this);
      this.resetMacroState();
      super.onDeactivate();
   }

   @AliceHandler
   private void cR(Pre var1) {
      this.cf = false;
      boolean var2 = ((Keybind)this.JQ.get()).isPressed();
      boolean var3 = this.dk();
      this.cU();
      if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null) {
         if (this.TR) {
            if (!(this.mK != this.mc.world)) {
               char var6 = 48299;
            }
         }

         if (this.mc.currentScreen == null && !this.dj()) {
            if (!var3) {
               char var7 = 57794;
            } else {
               char var8 = 57793;
            }

            boolean var4 = false;
            if (this.TR || var2 || var4) {
               AliceObf_fS.Pj(this);
               int var10001 = !this.TR ? 13390 : 13391;

               while (true) {
                  int var5 = var10001;
                  if (var5 == 13390) {
                     if (!var2 && !var4) {
                        break;
                     }
                  } else if (var5 == 13391) {

                     continue;
                  }

                  this.cS();
                  break;
               }

               if (!this.TR) {
                  this.cV(true);
               }

               if (this.TR) {
                  this.cW();
               }
            }
         } else {
            this.dm();
         }
      } else {
         this.dm();
      }
   }

   private void cS() {
      this.cf = true;
      this.mc.options.useKey.setPressed(false);

      while (this.mc.options.useKey.wasPressed()) {
      }
   }

   @AliceHandler
   private void cT(DoItemUseEvent var1) {
      if (!(this.cf)) {
         char var2 = 47036;
      }
   }

   private void cU() {
      if (this.aEX) {
         if (this.apv > 0) {
            this.apv--;
            if (this.apv > 0) {
               return;
            }
         }

         this.aEX = false;
         if (this.mc.player == null) {
            this.Cb = -1;
         } else {
            int var1 = this.Cb;
            this.Cb = -1;
            if (var1 < 0 || var1 > 8 || !this.mc.player.getInventory().getStack(var1).isOf(Items.RESPAWN_ANCHOR)) {
               var1 = this.dl(Items.RESPAWN_ANCHOR);
            }

            if (var1 >= 0) {
               this.mc.player.getInventory().setSelectedSlot(var1);
            }
         }
      }
   }

   private void cV(boolean var1) {
      if (this.mc.crosshairTarget instanceof BlockHitResult var2 && var2.getType() == HitResult.Type.BLOCK && this.dh(var2.getPos())) {
         BlockPos var6 = var2.getBlockPos();
         BlockState var4 = this.mc.world.getBlockState(var6);
         this.mK = this.mc.world;
         this.Xy = null;
         if (var4.isOf(Blocks.RESPAWN_ANCHOR)) {
            this.jb = var6.toImmutable();
            int var5 = (Integer)var4.get(RespawnAnchorBlock.CHARGES);
            this.cX(var5 > 0 ? AliceObf_tG.TO : AliceObf_tG.aIX);
         } else if (var1) {
            BlockPos var7 = this.dg(var2);
            if (var7 != null) {
               if (this.mc.world.getBlockState(var7).isReplaceable()) {

               } else {
                  char var8 = 52374;
               }

               this.jb = var7.toImmutable();
               this.Xy = this.di(var2);
               this.cX(AliceObf_tG.Ix);
            } else {
               this.dn();
            }
         } else {
            this.dn();
         }
      }
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void cW() {
      if (this.UO > 0) {
         this.UO--;
      } else {
         for (int var1 = 0; var1 < 3 && this.TR; var1++) {
            AliceObf_tG var2 = this.iu;
            int var3 = this.cZ(var2);
            if (var3 < 0) {
               this.dm();
               return;
            }

            this.mc.player.getInventory().setSelectedSlot(var3);
            BlockHitResult var4 = this.dc(var2);
            if (var4 == null) {
               this.dm();
               return;
            }

            int var5 = AliceObf_fS.PD();
            ActionResult var6 = this.mc.interactionManager.interactBlock(this.mc.player, Hand.MAIN_HAND, var4);
            boolean var7 = AliceObf_fS.PD() > var5;
            if (!var7 || !var6.isAccepted()) {
               this.dm();
               return;
            }

            this.mc.player.swingHand(Hand.MAIN_HAND);
            switch (var2.ordinal()) {
               case 0:
                  this.cX(AliceObf_tG.aIX);
                  break;
               case 1:
                  this.cX(AliceObf_tG.TO);
                  break;
               case 2:
                  this.cY();
            }

            if (this.UO > 0) {
               return;
            }
         }

         return;
      }
   }

   public AliceObf_y prepareCombatIntent() {
      return null;
   }

   private void cX(AliceObf_tG var1) {
      this.TR = true;
      this.iu = var1;
      int var2 = var1.ordinal();
      int var10001;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 2) {
               throw new MatchException(null, null);
            }

            var10001 = (Integer)this.BG.get();
         } else {
            var10001 = (Integer)this.anc.get();
         }
      } else {
         var10001 = (Integer)this.qE.get();
      }

      this.UO = var10001;
   }

   private void cY() {
      if ((Boolean)this.NG.get()) {
         this.Cb = this.dl(Items.RESPAWN_ANCHOR);
         this.apv = 1;
         this.aEX = true;
      }

      this.dn();
   }

   private int cZ(AliceObf_tG var1) {
      int var2 = var1.ordinal() ^ 475875729;
      int var10000;
      if (var2 != 475875728) {
         if (var2 != 475875729) {
            if (var2 != 475875731) {
               throw new MatchException(null, null);
            }

            var10000 = this.da();
         } else {
            var10000 = this.dl(Items.RESPAWN_ANCHOR);
         }
      } else {
         var10000 = this.dl(Items.GLOWSTONE);
      }

      return var10000;
   }

   private int da() {
      if (this.mc.player == null) {
         return -1;
      } else {
         int var1 = Math.max(0, Math.min(8, (Integer)this.yG.get() - 1));
         ItemStack var2 = this.mc.player.getInventory().getStack(var1);
         int var10000 = !var2.isEmpty() ? 54659 : 54660;

         while (true) {
            int var3 = var10000;
            if (var3 == 54659) {
               if (!var2.isOf(Items.GLOWSTONE) && !var2.isOf(Items.RESPAWN_ANCHOR)) {
                  return var1;
               }
               break;
            }

            if (var3 != 54660) {
               break;
            }

         }

         return this.db();
      }
   }

   private int db() {
      if (this.mc.player == null) {
         return -1;
      } else {
         for (int var1 = 0; var1 < 9; var1++) {
            ItemStack var2 = this.mc.player.getInventory().getStack(var1);
            if (!var2.isEmpty()) {
               int var10000 = !var2.isOf(Items.GLOWSTONE) ? 36835 : 36836;

               while (true) {
                  int var3 = var10000;
                  if (var3 == 36835) {
                     if (!var2.isOf(Items.RESPAWN_ANCHOR)) {
                        return var1;
                     }
                     break;
                  }

                  if (var3 != 36836) {
                     break;
                  }

               }
            }
         }

         return -1;
      }
   }

   private BlockHitResult dc(AliceObf_tG var1) {
      if (this.jb == null || this.mc.player == null) {
         return null;
      } else {
         return var1 == AliceObf_tG.Ix ? this.dd() : this.de(this.jb);
      }
   }

   private BlockHitResult dd() {
      if (!(this.mc.player.raycast(this.mc.player.getBlockInteractionRange(), 1.0F, false) instanceof BlockHitResult var1)) {
         return null;
      } else {
         if (var1.getType() == HitResult.Type.BLOCK && this.dh(var1.getPos())) {
            int var10000 = this.jb.equals(this.dg(var1)) ? 4764 : 4763;

            while (true) {
               int var3 = var10000;
               if (var3 == 4763) {
                  break;
               }

               if (var3 != 4764) {
                  this.Xy = this.di(var1);
                  return this.Xy;
               }

            }
         }

         return null;
      }
   }

   private BlockHitResult de(BlockPos var1) {
      Vec3d var2 = this.mc.player.getEyePos();
      double var3 = this.mc.player.getBlockInteractionRange();
      Vec3d var5 = var2.add(this.mc.player.getRotationVec(1.0F).multiply(var3));
      Vec3d var6 = (Vec3d)new Box(var1).raycast(var2, var5).orElse(null);
      return var6 != null && this.dh(var6) ? new BlockHitResult(var6, this.df(var1, var6), var1, false) : null;
   }

   private Direction df(BlockPos var1, Vec3d var2) {
      double var3 = Math.abs(var2.x - var1.getX());
      double var5 = Math.abs(var2.x - (var1.getX() + 1.0));
      double var7 = Math.abs(var2.y - var1.getY());
      double var9 = Math.abs(var2.y - (var1.getY() + 1.0));
      double var11 = Math.abs(var2.z - var1.getZ());
      double var13 = Math.abs(var2.z - (var1.getZ() + 1.0));
      double var15 = var3;
      Direction var17 = Direction.WEST;
      if (var5 < var3) {
         var15 = var5;
         var17 = Direction.EAST;
      }

      if (var7 < var15) {
         var15 = var7;
         var17 = Direction.DOWN;
      }

      if (var9 < var15) {
         var15 = var9;
         var17 = Direction.UP;
      }

      if (var11 < var15) {
         var15 = var11;
         var17 = Direction.NORTH;
      }

      if (var13 < var15) {
         var17 = Direction.SOUTH;
      }

      return var17;
   }

   private BlockPos dg(BlockHitResult var1) {
      if (!(this.mc.player != null)) {
         char var3 = 51421;
      }

      return null;
   }

   private boolean dh(Vec3d var1) {
      double var2 = this.mc.player.getBlockInteractionRange();
      return this.mc.player.getEyePos().squaredDistanceTo(var1) <= var2 * var2;
   }

   private BlockHitResult di(BlockHitResult var1) {
      return new BlockHitResult(var1.getPos(), var1.getSide(), var1.getBlockPos().toImmutable(), var1.isInsideBlock());
   }

   private boolean dj() {
      int var4;
      label97: {
         if (!this.mc.player.getMainHandStack().getItem().getComponents().contains(DataComponentTypes.FOOD)) {
            var4 = this.mc.player.getOffHandStack().getItem().getComponents().contains(DataComponentTypes.FOOD) ? 28990 : 28991;

            while (true) {
               int var3 = var4;
               if (var3 == 28990) {
                  break;
               }

               if (var3 != 28991) {
                  var4 = 0;
                  break label97;
               }

               var4 = 28989;
            }
         }

         var4 = 1;
      }

      boolean var1 = (var4 != 0);
      boolean var2 = this.mc.player.getMainHandStack().getItem() instanceof ShieldItem
         || this.mc.player.getOffHandStack().getItem() instanceof ShieldItem;
      return (var1 || var2) && this.mc.player.isUsingItem();
   }

   private boolean dk() {
      return this.mc.getWindow() == null ? false : GLFW.glfwGetMouseButton(this.mc.getWindow().getHandle(), 1) == 1;
   }

   private int dl(Item var1) {
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

   public boolean canYieldEpoch() {
      return true;
   }

   public boolean reservesLowerPriorityEpochs() {
      return false;
   }

   public void yieldToHigherPriority() {
   }

   public int combatPriority() {
      return 300;
   }

   public void suspendForItemUse() {
      this.dm();
   }

   private void dm() {
      this.dn();
   }

   private void dn() {
      this.TR = false;
      this.iu = null;
      this.mK = null;
      this.jb = null;
      this.Xy = null;
      this.UO = 0;
   }

   private void resetMacroState() {
      this.dn();
      this.aEX = false;
      this.apv = 0;
      this.Cb = -1;
      this.cf = false;
   }

   }

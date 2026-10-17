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
import java.util.Optional;
import meteordevelopment.meteorclient.events.entity.player.DoItemUseEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.KeybindSetting.Builder;
import meteordevelopment.meteorclient.utils.entity.DamageUtils;
import meteordevelopment.meteorclient.utils.entity.DamageUtils.RaycastFactory;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.item.ItemPlacementContext;
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
import net.minecraft.state.property.IntProperty;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import org.lwjgl.glfw.GLFW;

public final class AnchorSafe extends AliceModule implements AliceObf_jQ {
   private static final int aQU = 1;
   private final SettingGroup aHx = this.settings.getDefaultGroup();
   private final Setting<Keybind> MF;
   private final Setting<Integer> yF;
   private final Setting<Integer> qb;
   private final Setting<Integer> aEG;
   private final Setting<Integer> GW;
   private final Setting<Integer> SJ;
   private final Setting<Boolean> ape;
   private final Setting<Boolean> adr;
   private final Setting<Boolean> arm;
   private boolean aCN;
   private AliceObf_aQ Fa;
   private ClientWorld HA;
   private BlockPos Ps;
   private BlockHitResult RN;
   private BlockPos GZ;
   private BlockHitResult ajr;
   private boolean aKw;
   private final AliceObf_nW asQ;
   private boolean VL;
   private int Wn;
   private int avw;
   private int dz;
   private boolean zN;
   private boolean Ne;
   private int sb;
   private int vA;
   private boolean qi;
   public AnchorSafe() {
      super(AliceAddon.PVP_CATEGORY, "anchor-safe", "Dat, nap va kich no respawn anchor voi glowstone chan no giam sat thuong.");
      this.MF = this.aHx
         .add(((Builder)((Builder)((Builder)new Builder().name("active-key")).description("Phim kich hoat anchor safe burst 1 vong lap.")).defaultValue(Keybind.none())).build());
      this.yF = this.aHx
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("explode-slot"))
                     .description("Slot hotbar (-8) uu tien khi kich no anchor."))
                  .defaultValue(1))
               .min(1)
               .max(9)
               .sliderMax((9))
               .build()
         );
      this.qb = this.aHx
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("place-delay"))
                     .description("So tick cho truoc khi dat anchor."))
                  .defaultValue(0))
               .min(0)
               .sliderRange((0), 20)
               .build()
         );
      this.aEG = this.aHx
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("charge-delay"))
                     .description("So tick cho sau khi dat anchor va truoc khi nap."))
                  .defaultValue(0))
               .min(0)
               .sliderRange((0), (20))
               .build()
         );
      this.GW = this.aHx
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("explode-delay"))
                     .description("So tick cho sau khi dat glowstone bao ve va truoc khi kich no."))
                  .defaultValue(1))
               .min(0)
               .sliderRange(0, (20))
               .build()
         );
      this.SJ = this.aHx
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("shield-delay"))
                     .description("So tick cho sau khi nap va truoc khi dat glowstone bao ve."))
                  .defaultValue(0))
               .min(0)
               .sliderRange(0, 20)
               .build()
         );
      this.ape = this.aHx
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("switch-back"))
                     .description("Tu dong switch ve slot anchor sau khi kich no."))
                  .defaultValue(false))
               .build()
         );
      this.adr = this.aHx
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("require-visible"))
                     .description("Chi dat glowstone khi no nam giua nguoi choi va anchor de chan vu no."))
                  .defaultValue(true))
               .build()
         );
      this.arm = this.aHx
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("damage-check"))
                     .description("Chi dat glowstone khi no thuc su giam self damage."))
                  .defaultValue(true))
               .build()
         );
      this.asQ = new AliceObf_nW();
      this.dz = -1;
      this.vA = (-1);
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.ei();
         AliceObf_fS.Od(this);
         this.asQ.eo();
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.asQ.ep();
      AliceObf_fS.Oe(this);
      this.ei();
      super.onDeactivate();
   }

   @AliceHandler
   private void dy(Pre var1) {
      this.qi = false;
      boolean var2 = ((Keybind)this.MF.get()).isPressed();
      boolean var3 = this.ed();
      this.dK();
      if (super.mc.player != null) {
         int var10000 = super.mc.world != null ? 48125 : 48126;

         while (true) {
            int var6 = var10000;
            if (var6 == 48125) {
               if (super.mc.interactionManager != null) {
                  if (this.aCN && this.HA != super.mc.world) {
                     this.eh();
                  }

                  if (super.mc.currentScreen == null && !this.eg()) {
                     int var4 = var3 && super.mc.player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR)
                        ? (1)
                        : (0);
                     int var5 = var3 && this.ec() ? 1 : 0;
                     if (var2 || var4 != 0 || var5 != 0 || this.aCN) {
                        this.dz();
                     }

                     if (this.aCN) {
                        AliceObf_fS.Pj(this);
                        this.dC();
                        return;
                     }

                     if (!var2 && var4 == 0 && var5 == 0) {
                        return;
                     }

                     AliceObf_fS.Pj(this);
                     this.dB(true);
                     if (this.aCN) {
                        this.dC();
                     }

                     return;
                  }

                  this.eh();
                  return;
               }
               break;
            }

            if (var6 != 48126) {
               break;
            }

         }
      }

      this.eh();
   }

   private void dz() {
      this.qi = true;
      super.mc.options.useKey.setPressed(false);

      while (super.mc.options.useKey.wasPressed()) {
      }
   }

   @AliceHandler
   private void dA(DoItemUseEvent var1) {
      if (this.qi) {
         var1.setCancelled(true);
      }
   }

   private void dB(boolean var1) {
      if (super.mc.crosshairTarget instanceof BlockHitResult var2 && var2.getType() == HitResult.Type.BLOCK && this.eb(var2.getPos())) {
         BlockPos var6 = var2.getBlockPos();
         BlockState var4 = super.mc.world.getBlockState(var6);
         this.HA = super.mc.world;
         this.RN = null;
         this.GZ = null;
         this.ajr = null;
         this.aKw = false;
         this.dz = super.mc.player.getInventory().getSelectedSlot();
         if (var4.isOf(Blocks.RESPAWN_ANCHOR)) {
            this.Ps = var6.toImmutable();
            int var7 = this.getAnchorCharges(var4);
            this.dH(
               var7 > 0
                  ? AliceObf_aQ.uq
                  : AliceObf_aQ.aew
            );
         } else if (!var1) {
            this.ej();
         } else {
            BlockPos var5 = this.dX(var2);
            if (var5 != null && super.mc.world.getBlockState(var5).isReplaceable()) {
               this.Ps = var5.toImmutable();
               this.RN = this.ek(var2);
               this.dH(AliceObf_aQ.tI);
            } else {
               this.ej();
            }
         }
      }
   }

   private void dC() {
      if (this.avw > 0) {
         this.avw -= (1);
      } else {
         int var1 = (0);
         if (var1 < (4)) {

         } else {
            char var2 = 36302;
         }
      }
   }

   private boolean dD() {
      AliceObf_aQ var1 = this.Fa;
      if (var1 == AliceObf_aQ.uq) {
         return this.dE();
      } else if (!this.asQ.eq()) {
         int var2 = this.dM(var1);
         BlockHitResult var3 = this.dO(var1);
         if (var2 >= 0 && var3 != null) {
            super.mc.player.getInventory().setSelectedSlot(var2);
            if (!this.dT(var3, false, false)) {
               this.eh();
               return false;
            } else {
               int var4 = var1.ordinal();
               if (var4 != 0) {
                  if (var4 == 1) {
                     this.dH(AliceObf_aQ.uq);
                  } else if (var4 != 2 && var4 == 3) {
                     this.dI();
                  }
               } else {
                  this.dH(AliceObf_aQ.aew);
               }

               return true;
            }
         } else {
            this.eh();
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean dE() {
      if (!this.dQ()) {
         return this.dF();
      } else if (this.GZ != null && super.mc.world.getBlockState(this.GZ).isOf(Blocks.GLOWSTONE)) {
         return this.dF();
      } else {
         int var1 = this.dM(AliceObf_aQ.uq);
         BlockHitResult var2 = this.ajr;
         if (var1 >= 0 && var2 != null) {
            super.mc.player.getInventory().setSelectedSlot(var1);
            if (this.aKw && !this.asQ.er(var1)) {
               if ((this.Wn += (1)) > (1)) {
                  return this.dF();
               } else {
                  this.VL = true;
                  return false;
               }
            } else {
               this.VL = false;
               this.dT(var2, true, this.aKw);
               return this.dF();
            }
         } else {
            return this.dF();
         }
      }
   }

   private boolean dF() {
      this.VL = false;
      this.dH(AliceObf_aQ.aDE);
      return true;
   }

   @AliceHandler(
      priority = -2000001
   )
   private void dG(Post var1) {
      if (this.VL
         && this.aCN
         && this.Fa == AliceObf_aQ.uq
         && super.mc.world == this.HA
         && super.mc.player != null
         && super.mc.currentScreen == null) {
         int var2 = this.dM(AliceObf_aQ.uq);
         if (var2 >= 0 && super.mc.player.getInventory().getSelectedSlot() == var2) {
            this.asQ.es(var2);
         }
      }
   }

   public AliceObf_y prepareCombatIntent() {
      return null;
   }

   private void dH(AliceObf_aQ var1) {
      this.aCN = true;
      this.Fa = var1;
      this.VL = false;
      this.Wn = 0;

      this.avw = switch (var1.ordinal() ^ 1834219154) {
         case 1834219152 -> this.SJ.get();
         case 1834219153 -> this.GW.get();
         case 1834219154 -> this.qb.get();
         case 1834219155 -> this.aEG.get();
         default -> throw new MatchException(null, null);
      };
   }

   private void dI() {
      int var1 = this.dz;
      this.ej();
      this.dz = (-1);
      this.dJ(var1, (Boolean)this.ape.get());
   }

   private void dJ(int var1, boolean var2) {
      int var10000 = var1 >= 0 ? 22925 : 22926;

      while (true) {
         int var3 = var10000;
         if (var3 == 22925) {
            if (var1 <= (8)) {
               this.zN = true;
               this.Ne = var2;
               this.sb = (1);
               this.vA = var1;
               return;
            }
            break;
         }

         if (var3 != 22926) {
            break;
         }

      }
   }

   private void dK() {
      if (this.zN) {
         if (this.sb <= 0 || --this.sb <= 0) {
            int var1 = this.vA;
            boolean var2 = this.Ne;
            this.dL();
            if (super.mc.player != null) {
               if (var2) {
                  int var3 = this.ee(Items.RESPAWN_ANCHOR);
                  if (var3 >= 0) {
                     var1 = var3;
                  }
               }

               if (!(var1 < 0)) {
                  short var5 = 9245;
               }
            }
         }
      }
   }

   private void dL() {
      this.zN = false;
      this.Ne = false;
      this.sb = 0;
      this.vA = (-1);
   }

   // $VF: Unable to simplify switch-on-enum, as the enum class was not able to be found.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private int dM(AliceObf_aQ var1) {
      return switch (var1.ordinal()) {
         case 0 -> this.ee(Items.RESPAWN_ANCHOR);
         case 1, 2 -> this.ee(Items.GLOWSTONE);
         case 3 -> this.dN();
         default -> throw new MatchException(null, null);
      };
   }

   private int dN() {
      if (super.mc.player == null) {
         return (-1);
      } else {
         int var1 = Math.max(0, Math.min(8, (Integer)this.yF.get() - (1)));
         ItemStack var2 = super.mc.player.getInventory().getStack(var1);
         if (!var2.isEmpty()) {
            if (!var2.isOf(Items.GLOWSTONE)) {

            } else {
               char var3 = 56585;
            }
         }

         return this.ef();
      }
   }

   private BlockHitResult dO(AliceObf_aQ var1) {
      if (this.Ps != null && super.mc.player != null) {
         int var2 = var1.ordinal() ^ -1301837115;
         if (var2 != -1301837116) {
            if (var2 == -1301837115) {
               return this.dP();
            }

            if (var2 != -1301837114) {
               if (var2 != -1301837113) {
                  throw new MatchException(null, null);
               }

               return this.ajr;
            }
         }

         return this.dW(this.Ps);
      } else {
         return null;
      }
   }

   private BlockHitResult dP() {
      if (super.mc.player.raycast(super.mc.player.getBlockInteractionRange(), 1.0F, false) instanceof BlockHitResult var1) {
         if (var1.getType() == HitResult.Type.BLOCK && this.eb(var1.getPos()) && this.Ps.equals(this.dX(var1))) {
            this.RN = this.ek(var1);
            return this.RN;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private boolean dQ() {
      if (this.Ps != null && super.mc.world != null) {
         BlockHitResult var1 = this.dW(this.Ps);
         int var10000 = var1 == null ? 25450 : 25451;

         while (true) {
            int var7 = var10000;
            if (var7 == 25450) {
               return false;
            }

            if (var7 != 25451) {
               for (Direction var5 : Direction.values()) {
                  BlockPos var6 = this.Ps.offset(var5);
                  if (super.mc.world.getBlockState(var6).isOf(Blocks.GLOWSTONE) && this.dZ(var1.getPos(), var6)) {
                     this.GZ = var6.toImmutable();
                     this.ajr = null;
                     this.aKw = false;
                     return true;
                  }
               }

               AliceObf_fM var8 = this.dU(this.Ps, var1);
               if (var8 == null) {
                  return false;
               }

               this.GZ = var8.du().toImmutable();
               this.ajr = var8.dv();
               this.aKw = var8.dw();
               return true;
            }

         }
      } else {
         return false;
      }
   }

   private AliceObf_fM dR(BlockPos var1) {
      AliceObf_fM var2 = null;

      for (Direction var6 : Direction.values()) {
         BlockPos var7 = var1.offset(var6.getOpposite());
         if (var7.offset(var6).equals(var1)) {
            BlockState var8 = super.mc.world.getBlockState(var7);
            if (!var8.isAir() && !var8.isReplaceable()) {
               BlockHitResult var9 = this.dS(var7, var6);
               if (!(var9 == null)) {
                  short var12 = 8905;
               }

               boolean var10 = var7.equals(this.Ps);
               AliceObf_fM var11 = new AliceObf_fM(var1, var9, var10, 0.0F);
               if (!var10) {
                  return var11;
               }

               if (var2 == null) {
                  var2 = var11;
               }
            }
         }
      }

      return var2;
   }

   private BlockHitResult dS(BlockPos var1, Direction var2) {
      Vec3d var3 = Vec3d.ofCenter(var1).add(var2.getOffsetX() * 0.5, var2.getOffsetY() * 0.5, var2.getOffsetZ() * 0.5);
      return !this.eb(var3) ? null : new BlockHitResult(var3, var2, var1, false);
   }

   private boolean dT(BlockHitResult var1, boolean var2, boolean var3) {
      ClientPlayerEntity var4 = super.mc.player;
      if (var4 != null && var4.input != null && super.mc.interactionManager != null) {
         if (var3 && !this.asQ.er(var4.getInventory().getSelectedSlot())) {
            return false;
         } else {
            Input var5 = var4.input;
            PlayerInput var6 = var5.playerInput;

            int var10;
            try {
               if (var3) {
                  var5.playerInput = AliceObf_nW.et(var6);
               }

               int var7 = AliceObf_fS.PD();
               ActionResult var8 = super.mc.interactionManager.interactBlock(var4, Hand.MAIN_HAND, var1);
               int var9 = AliceObf_fS.PD() <= var7
                  ? (0)
                  : (1);
               if (var9 != 0 && var8.isAccepted()) {
                  var4.swingHand(Hand.MAIN_HAND);
               }

               var10 = var9 == 0
                     || !var8.isAccepted()
                     || var2 && (this.GZ == null || !super.mc.world.getBlockState(this.GZ).isOf(Blocks.GLOWSTONE))
                  ? (0)
                  : (1);
            } finally {
               var5.playerInput = var6;
            }

            return (var10 != 0);
         }
      } else {
         return false;
      }
   }

   private AliceObf_fM dU(BlockPos var1, BlockHitResult var2) {
      Vec3d var3 = super.mc.player.getEyePos();
      double var4 = super.mc.player.getBlockInteractionRange() * super.mc.player.getBlockInteractionRange();
      boolean var6 = (Boolean)this.arm.get();
      Float var7 = null;
      if (var6) {
         try {
            var7 = DamageUtils.anchorDamage(
               super.mc.player, super.mc.player.getEntityPos(), super.mc.player.getBoundingBox(), var1.toCenterPos(), this.dV(var1, null)
            );
         } catch (Throwable var20) {
            return null;
         }

         if (!Float.isFinite(var7)) {
            return null;
         }
      }

      AliceObf_fM var8 = null;

      label309:
      for (Direction var12 : Direction.values()) {
         BlockPos var13 = var1.offset(var12);
         if (var13.getY() >= super.mc.world.getBottomY()
            && var13.getY() < super.mc.world.getBottomY() + super.mc.world.getHeight()
            && super.mc.world.getBlockState(var13).isReplaceable()) {
            BlockState var14 = Blocks.GLOWSTONE.getDefaultState();
            if (var14.canPlaceAt(super.mc.world, var13)
               && super.mc.world.canPlace(var14, var13, ShapeContext.ofPlacement(super.mc.player))
               && !new Box(var13).intersects(super.mc.player.getBoundingBox())
               && !(this.ea(var3, new Box(var13)) > var4)
               && super.mc.player.canInteractWithBlockAt(var13, 0.0)) {
               AliceObf_fM var15 = this.dR(var13);
               int var10000 = var15 == null ? 6533 : 6534;

               while (true) {
                  int var18 = var10000;
                  if (var18 == 6533) {
                     break;
                  }

                  if (var18 != 6534) {
                     if ((Boolean)this.adr.get() && !this.dZ(var2.getPos(), var13)) {
                        break;
                     }

                     if (var6) {
                        float var16;
                        try {
                           RaycastFactory var17 = this.dV(var1, var13);
                           var16 = DamageUtils.anchorDamage(
                              super.mc.player, super.mc.player.getEntityPos(), super.mc.player.getBoundingBox(), var1.toCenterPos(), var17
                           );
                        } catch (Throwable var21) {
                           break;
                        }

                        if (!Float.isFinite(var16) || var16 >= var7 - 0.05F) {
                           break;
                        }

                        if (var8 != null) {
                           var10000 = !(var16 < var8.dx()) ? 24292 : 24291;

                           while (true) {
                              int var19 = var10000;
                              if (var19 == 24291) {
                                 break;
                              }

                              if (var19 != 24292) {
                                 continue label309;
                              }

                           }
                        }

                        var8 = new AliceObf_fM(var13, var15.dv(), var15.dw(), var16);
                        break;
                     }

                     if (var8 == null) {
                        var8 = var15;
                     }
                     break;
                  }

               }
            }
         }
      }

      return (AliceObf_fM)var8;
   }

   private RaycastFactory dV(BlockPos var1, BlockPos var2) {
      ClientWorld var3 = super.mc.world;
      return (var3x, var4) -> {
         if (var4.equals(var1)) {
            return null;
         } else {
            BlockState var5 = var4.equals(var2) ? Blocks.GLOWSTONE.getDefaultState() : var3.getBlockState(var4);
            return var5.getCollisionShape(var3, var4).raycast(var3x.start(), var3x.end(), var4);
         }
      };
   }

   private BlockHitResult dW(BlockPos var1) {
      Vec3d var2 = super.mc.player.getEyePos();
      double var3 = super.mc.player.getBlockInteractionRange();
      Vec3d var5 = var2.add(super.mc.player.getRotationVec(1.0F).multiply(var3));
      Vec3d var6 = (Vec3d)new Box(var1).raycast(var2, var5).orElse(null);
      return var6 != null && this.eb(var6) ? new BlockHitResult(var6, this.dY(var1, var6), var1, false) : null;
   }

   private BlockPos dX(BlockHitResult var1) {
      if (super.mc.player != null) {
         if (!(var1 == null)) {
            char var3 = 57241;
         }

         ItemPlacementContext var2 = new ItemPlacementContext(super.mc.player, Hand.MAIN_HAND, new ItemStack(Items.RESPAWN_ANCHOR), var1);
         return var2.getBlockPos();
      } else {
         return null;
      }
   }

   private Direction dY(BlockPos var1, Vec3d var2) {
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

   private boolean dZ(Vec3d var1, BlockPos var2) {
      Vec3d var3 = super.mc.player.getEyePos();
      Optional var4 = new Box(var2).raycast(var3, var1);
      if (!var4.isEmpty()) {
         Vec3d var5 = (Vec3d)var4.get();
         return !(!(var3.squaredDistanceTo(var5) + 1.0E-4 < var3.squaredDistanceTo(var1)));
      } else {
         return false;
      }
   }

   private double ea(Vec3d var1, Box var2) {
      double var3 = Math.max(var2.minX - var1.x, Math.max(0.0, var1.x - var2.maxX));
      double var5 = Math.max(var2.minY - var1.y, Math.max(0.0, var1.y - var2.maxY));
      double var7 = Math.max(var2.minZ - var1.z, Math.max(0.0, var1.z - var2.maxZ));
      return var3 * var3 + var5 * var5 + var7 * var7;
   }

   private boolean eb(Vec3d var1) {
      double var2 = super.mc.player.getBlockInteractionRange();
      return !(!(super.mc.player.getEyePos().squaredDistanceTo(var1) <= var2 * var2));
   }

   private boolean ec() {
      return (boolean)(super.mc.crosshairTarget instanceof BlockHitResult var1 && var1.getType() == HitResult.Type.BLOCK
         ? super.mc.world.getBlockState(var1.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)
         : 0);
   }

   private int getAnchorCharges(BlockState var1) {
      try {
         IntProperty var2 = RespawnAnchorBlock.CHARGES;
         return (Integer)var1.get(var2);
      } catch (Exception var3) {
         return 0;
      }
   }

   private boolean ed() {
      if (super.mc.getWindow() == null) {
         return false;
      } else {
         return (GLFW.glfwGetMouseButton(super.mc.getWindow().getHandle(), 1) == 1);
      }
   }

   private int ee(Item var1) {
      if (super.mc.player == null) {
         return (-1);
      } else {
         for (int var2 = (0); var2 < 9; var2++) {
            if (super.mc.player.getInventory().getStack(var2).isOf(var1)) {
               return var2;
            }
         }

         return (-1);
      }
   }

   private int ef() {
      if (super.mc.player == null) {
         return (-1);
      } else {
         for (int var1 = (0); var1 < (9); var1++) {
            ItemStack var2 = super.mc.player.getInventory().getStack(var1);
            int var10000 = !var2.isEmpty() ? 40883 : 40884;

            while (true) {
               int var3 = var10000;
               if (var3 == 40883) {
                  if (!var2.isOf(Items.GLOWSTONE) && !var2.isOf(Items.RESPAWN_ANCHOR)) {
                     return var1;
                  }
                  break;
               }

               if (var3 != 40884) {
                  break;
               }

            }
         }

         return -1;
      }
   }

   private boolean eg() {
      int var1 = !super.mc.player.getMainHandStack().getItem().getComponents().contains(DataComponentTypes.FOOD)
            && !super.mc.player.getOffHandStack().getItem().getComponents().contains(DataComponentTypes.FOOD)
         ? 0
         : 1;
      int var2 = !(super.mc.player.getMainHandStack().getItem() instanceof ShieldItem)
            && !(super.mc.player.getOffHandStack().getItem() instanceof ShieldItem)
         ? (0)
         : 1;
      if (var1 != 0 || var2 != 0) {
         if (super.mc.player.isUsingItem()) {

         } else {
            short var3 = 14974;
         }
      }

      return false;
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
      return 310;
   }

   public void suspendForItemUse() {
      this.eh();
   }

   private void eh() {
      int var1 = this.aCN ? this.dz : -1;
      this.ej();
      this.dz = (-1);
      this.dJ(var1, false);
   }

   private void ei() {
      this.qi = false;
      this.ej();
      this.dz = -1;
      this.dL();
   }

   private void ej() {
      this.aCN = false;
      this.Fa = null;
      this.HA = null;
      this.Ps = null;
      this.RN = null;
      this.GZ = null;
      this.ajr = null;
      this.aKw = false;
      this.VL = false;
      this.Wn = (0);
      this.avw = 0;
   }

   private BlockHitResult ek(BlockHitResult var1) {
      return new BlockHitResult(var1.getPos(), var1.getSide(), var1.getBlockPos().toImmutable(), var1.isInsideBlock());
   }

   }

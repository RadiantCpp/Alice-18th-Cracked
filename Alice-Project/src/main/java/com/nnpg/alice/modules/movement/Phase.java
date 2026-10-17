package com.nnpg.alice.modules.movement;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class Phase extends AliceModule {
   private final SettingGroup agw = this.settings.getDefaultGroup();
   private final Setting<String> aNQ;
   private final Setting<String> aac;
   private final Setting<Boolean> abX;
   private final Setting<Boolean> JH;
   private final Setting<Boolean> cm;
   private final Setting<String> GL;
   private final Setting<String> aFE;
   private final Setting<Boolean> avD;
   private static final double rw = 0.5;
   private static final double fV = 0.5;
   private int anp;
   public Phase() {
      super(AliceAddon.PVP_CATEGORY, "Phase", "Throws an ender pearl to phase into blocks. KingMC Crawl mode requires crawling away from the target block before throwing.");
      this.aNQ = this.agw
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("Logic")).description("Phase bypass targeting logic: KingMC (bedrock = Grim behavior, crawl requires crawling away from block before throwing), Grim, or NCP."))
                  .defaultValue("KingMC"))
               .supplier(
                  () -> {
                     String[] var10000 = new String[3];
                     var10000[(0)] = "KingMC";
                     var10000[(1)] = "Grim";
                     var10000[2] = "NCP";
                     return var10000;
                  }
               )
               .build()
         );
      this.aac = this.agw
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("Swap")).description("The mode that will be used for switching to the pearl before throwing it.")).defaultValue("Silent"))
               .supplier(() -> AliceObf_uv.w)
               .build()
         );
      this.abX = this.agw
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("Crawl"))
                     .description("Crawl phase mode. In KingMC mode, crawl away from the target block before throwing."))
                  .defaultValue(false))
               .build()
         );
      this.JH = this.agw
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("AntiVoid"))
                     .description("Prevents phasing into or through blocks that lead directly into the void."))
                  .defaultValue(false))
               .build()
         );
      this.cm = this.agw
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("Debug"))
                     .description("Chat-prints pos/yaw/pitch/target for every throw, so a failed (popped-up) attempt can be matched back to its exact numbers."))
                  .defaultValue(false))
               .build()
         );
      this.GL = this.agw
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("NCP-Mode")).description("Item used to bypass pearl distance checks on NCP."))
                     .defaultValue("FireCharge"))
                  .supplier(
                     () -> {
                        String[] var10000 = new String[3];
                        var10000[(0)] = "FireCharge";
                        var10000[(1)] = "FlintAndSteel";
                        var10000[2] = "Cobweb";
                        return var10000;
                     }
                  )
                  .visible(
                     () -> ((String)this.aNQ.get())
                        .equalsIgnoreCase("NCP")
                  ))
               .build()
         );
      this.aFE = this.agw
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("Switch")).description("The mode that will be used for switching to the bypass item."))
                     .defaultValue("Silent"))
                  .supplier(
                     () -> {
                        String[] var10000 = new String[]{
                           "Normal",
                           null,
                           null,
                           null
                        };
                        var10000[(1)] = "Silent";
                        var10000[2] = "AltPickup";
                        var10000[(3)] = "AltSwap";
                        return var10000;
                     }
                  )
                  .visible(
                     () -> ((String)this.aNQ.get())
                        .equalsIgnoreCase(
                           "NCP"
                        )
                  ))
               .build()
         );
      this.avD = this.agw
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("AutoRemove"))
                        .description("Automatically removes the bypass block/fire once you have phased."))
                     .defaultValue(true))
                  .visible(
                     () -> ((String)this.aNQ.get())
                        .equalsIgnoreCase("NCP")
                  ))
               .build()
         );
      this.anp = 0;
   }

   private boolean abF(Entity var1) {
      if (var1.getBlockStateAtPos().isOf(Blocks.COBWEB)) {
         return true;
      } else {
         for (BlockPos var3 : BlockPos.iterate(var1.getBoundingBox())) {
            if (AliceObf_iE.ari
               .world
               .getBlockState(var3)
               .isOf(Blocks.COBWEB)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean abG(Entity var1) {
      if (!var1.getBlockStateAtPos().isOf(Blocks.LADDER)) {
         for (BlockPos var3 : BlockPos.iterate(var1.getBoundingBox())) {
            if (AliceObf_iE.ari
               .world
               .getBlockState(var3)
               .isOf(Blocks.LADDER)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean abH() {
      if (AliceObf_iE.ari.player == null) {
         return false;
      } else {
         return (boolean)(AliceObf_iE.ari.player.getPose()
                  != EntityPose.SWIMMING
               && !(
                  AliceObf_iE.ari
                        .player
                        .getEyeHeight(
                           AliceObf_iE.ari
                              .player
                              .getPose()
                        )
                     <= 1.0F
               ));
      }
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         if (AliceObf_iE.ari.player != null
            && AliceObf_iE.ari.world != null) {
            BlockPos[] var10000 = new BlockPos[(2)];
            var10000[(0)] = AliceObf_iE.ari
               .player
               .getBlockPos();
            var10000[1] = AliceObf_iE.ari
               .player
               .getBlockPos()
               .down();
            BlockPos[] var1 = var10000;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; var3++) {
               BlockPos var4 = var1[var3];
               if (AliceObf_iE.ari
                  .world
                  .getBlockState(var4)
                  .isOf(Blocks.SCAFFOLDING)) {
                  AliceObf_iE.aLk(
                     var1x -> new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, var4, Direction.UP, var1x)
                  );
                  AliceObf_iE.aLk(
                     var1x -> new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, var4, Direction.UP, var1x)
                  );
                  AliceObf_iE.aLj(
                     new HandSwingC2SPacket(Hand.MAIN_HAND)
                  );
                  AliceObf_iE.ari
                     .world
                     .removeBlock(var4, false);
               }
            }

            boolean var28 = ((String)this.aNQ.get())
               .equalsIgnoreCase("KingMC");
            boolean var29 = this.abH();
            if (!(Boolean)this.abX.get() && var29 && !var28) {
               this.disable();
            } else {
               int var30 = this.abX.get() && var29 ? (1) : 0;
               int var31 = var30 != 0 && var29 ? (1) : 0;
               boolean var5 = this.abF(AliceObf_iE.ari.player);
               boolean var6 = this.abG(AliceObf_iE.ari.player);
               if (var31 == 0
                  && !var5
                  && !var6
                  && !AliceObf_iE.ari
                     .world
                     .getBlockState(
                        AliceObf_iE.ari.player.getBlockPos()
                     )
                     .isReplaceable()) {
                  this.disable();
               } else {
                  if (!AliceObf_iE.ari.player.isSneaking()) {
                     short var33 = 28685;
                  } else {
                     short var34 = 28684;
                  }

                  if (AliceObf_iE.ari
                     .player
                     .getItemCooldownManager()
                     .isCoolingDown(new ItemStack(Items.ENDER_PEARL))) {
                     this.disable();
                  } else {
                     String var7 = (String)this.aac.get();
                     int var8 = AliceObf_uv.aKZ(
                        Items.ENDER_PEARL,
                        (0),
                        !var7.equalsIgnoreCase(
                                 "AltSwap"
                              )
                              && !var7.equalsIgnoreCase(
                                 "AltPickup"
                              )
                           ? 8
                           : 35
                     );
                     int var9 = AliceObf_iE.ari
                        .player
                        .getInventory()
                        .getSelectedSlot();
                     int var10 = 0;
                     if (var8 == -1) {
                        this.info(
                           "No pearls could be found in your hotbar.",
                           new Object[(0)]
                        );
                        this.disable();
                     } else {
                        BlockPos var11 = AliceObf_iE.ari
                           .player
                           .getBlockPos()
                           .down();
                        float var12 = MathHelper.wrapDegrees(this.getPhaseYaw());
                        float var13 = (var12 % 90.0F + 90.0F) % 90.0F;
                        int var14 = !(Math.min(var13, 90.0F - var13) < 22.5F) ? 0 : (1);
                        boolean var15 = this.abI();
                        boolean var16 = ((String)this.aNQ.get())
                           .equalsIgnoreCase(
                              "Grim"
                           );
                        Vec3d var17;
                        if (var28 && !var15) {
                           var17 = var30 != 0 ? this.abK(var12) : this.abJ(var12);
                        } else if (var16) {
                           var17 = this.abN();
                        } else {
                           var17 = var14 != 0 ? this.abO(var12) : this.abL();
                        }

                        if ((Boolean)this.JH.get() && this.abT(var17, var12)) {
                           this.info(
                              "Phase cancelled to prevent falling into the void.",
                              new Object[(0)]
                           );
                           if (AliceObf_iE.ari.getSoundManager()
                              != null) {
                              AliceObf_iE.ari
                                 .getSoundManager()
                                 .play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_PLING, 1.5F));
                           }

                           this.disable();
                        } else {
                           float var18 = this.abP(var17);
                           float var19 = this.abQ(var17);
                           if ((Boolean)this.cm.get()) {
                              this.anp += (1);
                              String var10002 = "#%d [%s%s] pos=(%.4f, %.4f, %.4f) yaw=%.2f pitch=%.2f target=(%.4f, %.4f, %.4f) sector=%s";
                              Object[] var10003 = new Object[12];
                              var10003[(0)] = this.anp;
                              var10003[1] = this.aNQ.get();
                              if (var28) {
                                 if (!(!var15)) {
                                    short var37 = 27105;
                                 }
                              }

                              var10003[2] = var28 && var30 != 0
                                 ? "/Crawl"
                                 : (
                                    var28
                                       ? "/Standing"
                                       : ""
                                 );
                              var10003[3] = AliceObf_iE.ari
                                 .player
                                 .getX();
                              var10003[(4)] = AliceObf_iE.ari
                                 .player
                                 .getY();
                              var10003[5] = AliceObf_iE.ari
                                 .player
                                 .getZ();
                              var10003[6] = var18;
                              var10003[(7)] = var19;
                              var10003[8] = var17.x;
                              var10003[9] = var17.y;
                              var10003[10] = var17.z;
                              var10003[11] = var14 != 0
                                 ? "straight"
                                 : "corner";
                              this.info(String.format(var10002, var10003), new Object[0]);
                           }

                           if (((String)this.aNQ.get())
                              .equalsIgnoreCase(
                                 "NCP"
                              )) {
                              if (!AliceObf_iE.ari
                                 .world
                                 .getBlockState(
                                    AliceObf_iE.ari
                                       .player
                                       .getBlockPos()
                                 )
                                 .isAir()) {

                              } else {
                                 char var35 = 34365;
                              }

                              if (!AliceObf_iE.ari
                                 .world
                                 .getBlockState(var11)
                                 .isReplaceable()) {
                                 String var21 = (String)this.GL.get();
                                 int var22 = (-1);
                                 int var24 = var21.hashCode() ^ 93957666;
                                 if (var24 != 2083762367) {
                                    if (var24 == 2097583132
                                       && var21.equals(
                                          "Cobweb"
                                       )) {
                                       var22 = (1);
                                    }
                                 } else {
                                    int var36 = var21.equals(
                                          "FlintAndSteel"
                                       )
                                       ? 7885
                                       : 7886;

                                    while (true) {
                                       int var25 = var36;
                                       if (var25 == 7885) {
                                          var22 = (0);
                                          break;
                                       }

                                       if (var25 != 7886) {
                                          break;
                                       }

                                       var36 = 7884;
                                    }
                                 }

                                 switch (var22) {
                                    case 0:
                                    case 1:
                                 }

                                 int var32 = AliceObf_uv.aKZ(null,
                                    ((String)this.aFE.get())
                                          .equalsIgnoreCase(
                                             "AltSwap"
                                          )
                                       ? 3933
                                       : 3932,
                                    (35)
                                 );
                                 if (var32 != (-1)
                                    && AliceObf_uv.aKQ(
                                       (String)this.aFE.get(), var32, var9
                                    )) {
                                    try {
                                       AliceObf_fq.aMY(
                                          var18, 90.0F, true
                                       );
                                       AliceObf_iE.aLk(
                                          var1x -> new PlayerInteractBlockC2SPacket(
                                             Hand.MAIN_HAND,
                                             new BlockHitResult(Vec3d.ofCenter(var11).add(0.0, 1.0, 0.0), Direction.UP, var11, false),
                                             var1x
                                          )
                                       );
                                       AliceObf_iE.aLj(
                                          new HandSwingC2SPacket(Hand.MAIN_HAND)
                                       );
                                       var10 = (1);
                                    } finally {
                                       AliceObf_uv.aKS(
                                          (String)this.aFE.get(), var32, var9
                                       );
                                    }
                                 }
                              }
                           }

                           AliceObf_fq.aMY(var18, var19, true);
                           if (AliceObf_uv.aKQ(var7, var8, var9)) {
                              AliceObf_iE.aLk(
                                 var2x -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, var2x, var18, var19)
                              );
                              AliceObf_iE.aLj(
                                 new HandSwingC2SPacket(Hand.MAIN_HAND)
                              );
                              AliceObf_uv.aKS(var7, var8, var9);
                           }

                           if (var10 != 0 && (Boolean)this.avD.get()) {
                              AliceObf_iE.aLk(
                                 var0 -> new PlayerActionC2SPacket(
                                    Action.START_DESTROY_BLOCK,
                                    AliceObf_iE.ari
                                       .player
                                       .getBlockPos(),
                                    Direction.UP,
                                    var0
                                 )
                              );
                              AliceObf_iE.aLk(
                                 var0 -> new PlayerActionC2SPacket(
                                    Action.STOP_DESTROY_BLOCK,
                                    AliceObf_iE.ari
                                       .player
                                       .getBlockPos(),
                                    Direction.UP,
                                    var0
                                 )
                              );
                              AliceObf_iE.aLj(
                                 new HandSwingC2SPacket(Hand.MAIN_HAND)
                              );
                           }

                           this.disable();
                        }
                     }
                  }
               }
            }
         } else {
            this.disable();
         }
      }
   }

   private float getPhaseYaw() {
      if (AliceObf_iE.ari.player == null) {
         return 0.0F;
      } else {
         float var1 = AliceObf_iE.ari.player.getYaw();
         int var2 = (
               !AliceObf_iE.ari.options.rightKey.isPressed()
                  ? 0
                  : (1)
            )
            - (
               !AliceObf_iE.ari.options.leftKey.isPressed()
                  ? 0
                  : (1)
            );
         int var3 = (
               AliceObf_iE.ari.options.forwardKey.isPressed()
                  ? 1
                  : 0
            )
            - (
               !AliceObf_iE.ari.options.backKey.isPressed()
                  ? (0)
                  : 1
            );
         if (var2 == 0 && var3 == 0) {
            return var1;
         } else {
            float var4 = (float)Math.toDegrees(Math.atan2(var2, var3));
            return MathHelper.wrapDegrees(var1 + var4);
         }
      }
   }

   private boolean abI() {
      if (AliceObf_iE.ari.player != null
         && AliceObf_iE.ari.world != null) {
         double var1 = AliceObf_iE.ari.player.getBoundingBox().minY;
         BlockPos var3 = BlockPos.ofFloored(
            AliceObf_iE.ari.player.getX(),
            var1 - 0.1,
            AliceObf_iE.ari.player.getZ()
         );
         if (AliceObf_iE.ari
            .world
            .getBlockState(var3)
            .isOf(Blocks.BEDROCK)) {
            return true;
         } else {
            return (boolean)(AliceObf_iE.ari
                  .world
                  .getBlockState(
                     AliceObf_iE.ari
                        .player
                        .getBlockPos()
                        .down()
                  )
                  .isOf(Blocks.BEDROCK)
               ? (1)
               : AliceObf_iE.ari
                  .world
                  .getBlockState(
                     AliceObf_iE.ari.player.getBlockPos()
                  )
                  .isOf(Blocks.BEDROCK));
         }
      } else {
         return false;
      }
   }

   private Vec3d abJ(float var1) {
      double var2 = AliceObf_iE.ari.player.getX();
      double var4 = AliceObf_iE.ari.player.getY();
      double var6 = AliceObf_iE.ari.player.getZ();
      double var8 = Math.toRadians(var1);
      Vec3d var10 = new Vec3d(-Math.sin(var8), 0.0, Math.cos(var8)).normalize();
      float var11 = (var1 % 90.0F + 90.0F) % 90.0F;
      int var12 = Math.min(var11, 90.0F - var11) < 22.5F ? 1 : 0;
      double var13 = 0.165;
      if (var12 != 0) {
         int var23 = Math.abs(var10.z) > Math.abs(var10.x) ? 1 : 0;
         if (var23 != 0) {
            double var24 = var10.z > 0.0 ? Math.floor(var6) + 1.0 : Math.floor(var6);
            double var25 = !(var10.z > 0.0) ? var24 + var13 : var24 - var13;
            return new Vec3d(var2, var4, var25);
         } else {
            double var16 = var10.x > 0.0 ? Math.floor(var2) + 1.0 : Math.floor(var2);
            double var18 = var10.x > 0.0 ? var16 - var13 : var16 + var13;
            return new Vec3d(var18, var4, var6);
         }
      } else {
         double var15 = var10.x > 0.0 ? Math.floor(var2) + 1.0 : Math.floor(var2);
         double var17 = var10.z > 0.0 ? Math.floor(var6) + 1.0 : Math.floor(var6);
         double var19 = !(var10.x > 0.0) ? var15 + var13 : var15 - var13;
         double var21 = !(var10.z > 0.0) ? var17 + var13 : var17 - var13;
         return new Vec3d(var19, var4, var21);
      }
   }

   private Vec3d abK(float var1) {
      Vec3d var12 = this.getEffectiveEyePos();
      float var8 = AliceObf_iE.ari.player.getYaw();
      double var9;
      BlockHitResult var13 = AliceObf_iE.ari
         .world
         .raycast(
            new RaycastContext(
               var12,
               var12.add(new Vec3d(-Math.sin(var9 = Math.toRadians(var8)), 0.0, Math.cos(var9)).normalize().multiply(4.0)),
               ShapeType.COLLIDER,
               FluidHandling.NONE,
               AliceObf_iE.ari.player
            )
         );
      if (var13 != null) {
         if (var13.getType() == HitResult.Type.BLOCK) {

         } else {
            char var30 = 50612;
         }
      }

      double var14 = Math.toRadians(var1);
      Vec3d var7 = new Vec3d(-Math.sin(var14), 0.0, Math.cos(var14)).normalize();
      var13 = AliceObf_iE.ari
         .world
         .raycast(
            new RaycastContext(
               var12,
               var12.add(var7.multiply(4.0)),
               ShapeType.COLLIDER,
               FluidHandling.NONE,
               AliceObf_iE.ari.player
            )
         );
      Object var26 = null;
      Object var15 = null;
      Object var16 = null;
      if (var13 != null) {
         if (var13.getType() == HitResult.Type.BLOCK) {
            char var31 = 53752;
         } else {
            char var32 = 53753;
         }
      }

      Direction var17 = Direction.fromHorizontalDegrees(var1);
      BlockPos var27 = AliceObf_iE.ari
         .player
         .getBlockPos()
         .offset(var17, 1);
      var15 = var17.getOpposite();
      double var29 = var27.getY() + 0.52;
      float var19 = (var1 % 90.0F + 90.0F) % 90.0F;
      int var6 = Math.min(var19, 90.0F - var19) < 22.5F ? 1 : 0;
      if (var6 != 0) {
         short var33 = 1930;
      } else {
         short var34 = 1931;
      }

      double var2;
      double var4;
      if (var16 != null) {
         var4 = Math.round(((Vec3d)var16).x);
         var2 = Math.round(((Vec3d)var16).z);
      } else {
         var4 = Math.round(
            AliceObf_iE.ari.player.getX()
               + var7.x
         );
         var2 = Math.round(
            AliceObf_iE.ari.player.getZ()
               + var7.z
         );
      }

      return new Vec3d(var4, var29, var2);
   }

   private Vec3d abL() {
      double var1 = AliceObf_iE.ari.player.getX();
      double var3 = AliceObf_iE.ari.player.getZ();
      double var5 = this.abX.get() && this.abH()
         ? AliceObf_iE.ari
            .player
            .getBlockPos()
            .down()
            .getY()
         : AliceObf_iE.ari.player.getY() - 0.5;
      double var7 = Math.round(var1);
      double var9 = Math.round(var3);
      double var11 = var7 - var1;
      double var13 = var9 - var3;
      if (Math.abs(var11) <= 0.5 && Math.abs(var13) <= 0.5) {
         return new Vec3d(var1 + MathHelper.clamp(var11, -0.5, 0.5), var5, var3 + MathHelper.clamp(var13, -0.5, 0.5));
      } else {
         double var15 = Math.PI / 13;
         double var17 = Math.PI / 4;
         double var19 = var1 + MathHelper.clamp(this.abM(var1, Math.floor(var1) + var15, Math.floor(var1) + var17) - var1, -0.2, 0.2);
         double var21 = var3 + MathHelper.clamp(this.abM(var3, Math.floor(var3) + var15, Math.floor(var3) + var17) - var3, -0.2, 0.2);
         return new Vec3d(var19, var5, var21);
      }
   }

   private double abM(double var1, double var3, double var5) {
      return var1 - var3 > var5 - var1 ? var5 : var3;
   }

   private Vec3d abN() {
      double var3 = AliceObf_iE.ari.player.getX();
      double var5 = AliceObf_iE.ari.player.getZ();
      double var7 = this.abX.get() && this.abH()
         ? AliceObf_iE.ari
            .player
            .getBlockPos()
            .down()
            .getY()
         : AliceObf_iE.ari.player.getY() - 0.5;
      double var9 = Math.floor(var3);
      double var11 = Math.floor(var5);
      double var13 = var11 + 0.5;
      double var1;
      double var15 = Math.toDegrees(Math.atan2(var5 - var13, var3 - (var1 = var9 + 0.5)));
      int var10000 = !(var15 < 0.0) ? 34417 : 34416;

      while (true) {
         int var22 = var10000;
         if (var22 == 34416) {
            var15 += 360.0;
            break;
         }

         if (var22 != 34417) {
            break;
         }

      }

      int var17 = (int)Math.floor((var15 + 22.5) / 45.0) & (7);
      double var18 = var9 + 1.0;
      double var20 = var11 + 1.0;
      int var23 = var17 ^ 1344726824;
      return var23 != 1344726824
         ? (
            var23 == 1344726825
               ? new Vec3d(var18, var7, var20)
               : (
                  var23 != 1344726826
                     ? (
                        var23 == 1344726827
                           ? new Vec3d(var9, var7, var20)
                           : (
                              var23 != 1344726828
                                 ? (
                                    var23 == 1344726829
                                       ? new Vec3d(var9, var7, var11)
                                       : (var23 == 1344726830 ? new Vec3d(var1, var7, var11) : new Vec3d(var18, var7, var11))
                                 )
                                 : new Vec3d(var9, var7, var13)
                           )
                     )
                     : new Vec3d(var1, var7, var20)
               )
         )
         : new Vec3d(var18, var7, var13);
   }

   private Vec3d abO(float var1) {
      double var2 = AliceObf_iE.ari.player.getX();
      double var4 = AliceObf_iE.ari.player.getZ();
      double var6 = this.abX.get() && this.abH()
         ? AliceObf_iE.ari
            .player
            .getBlockPos()
            .down()
            .getY()
         : AliceObf_iE.ari.player.getY() - 0.5;
      double var8 = Math.toRadians(var1);
      Vec3d var10 = new Vec3d(-Math.sin(var8), 0.0, Math.cos(var8)).normalize();
      int var11 = Math.abs(var10.z) > Math.abs(var10.x) ? 1 : 0;
      if (var11 != 0) {
         double var14 = var10.z > 0.0 ? Math.floor(var4) + 1.0 : Math.floor(var4);
         return new Vec3d(var2, var6, var14);
      } else {
         double var12 = !(var10.x > 0.0) ? Math.floor(var2) : Math.floor(var2) + 1.0;
         return new Vec3d(var12, var6, var4);
      }
   }

   private Vec3d getEffectiveEyePos() {
      int var10000 = AliceObf_iE.ari.player == null
         ? 1665
         : 1666;

      while (true) {
         int var3 = var10000;
         if (var3 == 1665) {
            return Vec3d.ZERO;
         }

         if (var3 != 1666) {
            double var1 = AliceObf_iE.ari.player.getEyeY()
               - 0.1;
            return new Vec3d(
               AliceObf_iE.ari.player.getX(),
               var1,
               AliceObf_iE.ari.player.getZ()
            );
         }

      }
   }

   private float abP(Vec3d var1) {
      Vec3d var2 = this.getEffectiveEyePos();
      Vec3d var3 = var1.subtract(var2);
      return (float)Math.toDegrees(Math.atan2(-var3.x, var3.z));
   }

   private float abQ(Vec3d var1) {
      Vec3d var2 = this.getEffectiveEyePos();
      double var3 = Math.hypot(var1.x - var2.x, var1.z - var2.z);
      double var5 = var1.y - var2.y;
      if (var3 < 1.0E-4) {
         return var5 < 0.0 ? 85.0F : -85.0F;
      } else {
         double var7 = -85.0;
         double var9 = 89.9;

         for (int var11 = (0); var11 < (30); var11++) {
            double var12 = (var7 + var9) * 0.5;
            double var14 = Phase.abR(var12, var3);
            if (var14 < var5) {
               var9 = var12;
            } else {
               var7 = var12;
            }
         }

         return (float)((var7 + var9) * 0.5);
      }
   }

   private static double abR(double var0, double var2) {
      double var4 = Math.toRadians(var0);
      double var6 = 1.5 * Math.cos(var4);
      double var8 = -1.5 * Math.sin(var4);
      double var10 = 0.0;
      double var12 = 0.0;

      for (int var14 = (0); var14 < 100; var14++) {
         var10 += var6;
         var12 += var8;
         if (!(var10 >= var2)) {

         } else {
            short var21 = 24571;
         }

         var6 *= 0.99;
         var8 = var8 * 0.99 - 0.03;
      }

      return var12;
   }

   private boolean abS(double var1, double var3, double var5) {
      if (AliceObf_iE.ari.world == null) {
         return false;
      } else {
         int var7 = AliceObf_iE.ari.world.getBottomY();
         int var8 = (int)Math.floor(var3);
         if (var8 < var7) {
            return true;
         } else {
            for (int var9 = var8; var9 >= var7; var9--) {
               BlockPos var10 = BlockPos.ofFloored(var1, var9, var5);
               BlockState var11 = AliceObf_iE.ari
                  .world
                  .getBlockState(var10);
               if (!var11.isAir()
                  && !var11.getCollisionShape(
                        AliceObf_iE.ari.world, var10
                     )
                     .isEmpty()) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private boolean abT(Vec3d var1, float var2) {
      if (AliceObf_iE.ari.player != null
         && AliceObf_iE.ari.world != null) {
         double var3 = AliceObf_iE.ari.player.getX();
         double var5 = AliceObf_iE.ari.player.getY();
         double var7 = AliceObf_iE.ari.player.getZ();
         int var9 = AliceObf_iE.ari.world.getBottomY();
         if (var5 < var9) {
            return true;
         } else {
            double var10 = Math.toRadians(var2);
            Vec3d var12 = new Vec3d(-Math.sin(var10), 0.0, Math.cos(var10)).normalize();
            BlockPos var13 = BlockPos.ofFloored(var1.x, var1.y, var1.z);
            BlockPos var14 = BlockPos.ofFloored(var3 + var12.x * 0.5, var5, var7 + var12.z * 0.5);
            int var15 = !this.abS(var13.getX() + 0.5, var1.y, var13.getZ() + 0.5)
               ? (1)
               : (0);
            int var16 = this.abS(var14.getX() + 0.5, var5, var14.getZ() + 0.5) ? (0) : 1;
            return (var15 == 0 && var16 == 0);
         }
      } else {
         return false;
      }
   }

   }

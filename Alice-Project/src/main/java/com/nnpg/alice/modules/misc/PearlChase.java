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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import meteordevelopment.meteorclient.events.entity.EntityAddedEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class PearlChase extends AliceModule {
   private final SettingGroup auz = this.settings.getDefaultGroup();
   private final Setting<String> anR;
   private final Setting<Boolean> ax;
   private final Setting<String> un;
   private final Setting<String> Ls;
   private final Setting<Double> avP;
   private final Setting<Double> ga;
   private final Setting<String> amn;
   private final Setting<Boolean> adz;
   private final Setting<Double> Bi;
   private final Setting<Integer> JV;
   private final Setting<Boolean> Po;
   private final Setting<Boolean> apG;
   private final Setting<Boolean> dx;
   private final Setting<Double> tz;
   private final Set<Integer> aHD;
   private Object Zf;
   private AliceObf_by anj;
   private long agt;
   private Vec3d Hi;
   private List<Vec3d> aT;
   public PearlChase() {
      super(AliceAddon.PVP_CATEGORY, "PearlChase", "Automatically throws an Ender Pearl to chase enemy pearl throws with trajectory prediction and obstacle avoidance.");
      this.anR = this.auz
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("Target")).description("Priority used to select which enemy to chase."))
                  .defaultValue("Nearest"))
               .supplier(
                  () -> {
                     String[] var10000 = new String[3];
                     var10000[(0)] = "Lowest";
                     var10000[1] = "Furthest";
                     var10000[(2)] = "Nearest";
                     return var10000;
                  }
               )
               .build()
         );
      this.ax = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("IgnoreNaked"))
                     .description("Ignore naked players, even if wearing only elytra."))
                  .defaultValue(false))
               .build()
         );
      this.un = this.auz
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("Rotate")).description("Rotation mode used before throwing the pearl.")).defaultValue("Silent"))
               .supplier(
                  () -> {
                     String[] var10000 = new String[3];
                     var10000[(0)] = "None";
                     var10000[1] = "Normal";
                     var10000[(2)] = "Silent";
                     return var10000;
                  }
               )
               .build()
         );
      this.Ls = this.auz
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("Switch")).description("The mode that will be used for automatically switching to ender pearls."))
                  .defaultValue("Silent"))
               .supplier(() -> AliceObf_uv.w)
               .build()
         );
      this.avP = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("Offset"))
                  .description("Distance offset relative to target landing position along flight path (positive = ahead, negative = behind)."))
               .defaultValue(0.0)
               .min(-10.0)
               .max(10.0)
               .build()
         );
      this.ga = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("Distance"))
                  .description("Minimum horizontal travel distance that enemy will move for pearl to be chased."))
               .defaultValue(10.0)
               .min(0.0)
               .max(50.0)
               .build()
         );
      this.amn = this.auz
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("ArcMode")).description("Trajectory selection mode: Smart (low if clear, high if blocked), Low (fast direct), High (maximum lob).")).defaultValue("Smart"))
               .supplier(
                  () -> {
                     String[] var10000 = new String[(3)];
                     var10000[0] = "Smart";
                     var10000[(1)] = "Low";
                     var10000[2] = "High";
                     return var10000;
                  }
               )
               .build()
         );
      this.adz = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("AvoidObstacles"))
                     .description("Calculates alternate high arcs or yaw offsets if direct path is blocked by terrain."))
                  .defaultValue(true))
               .build()
         );
      this.Bi = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("EnemyRange"))
                  .description("Maximum range to enemy player throwing pearl."))
               .defaultValue(64.0)
               .min(8.0)
               .max(128.0)
               .build()
         );
      this.JV = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("Delay"))
                     .description("Delay in ticks before throwing the pearl."))
                  .defaultValue(0))
               .min(0)
               .max(20)
               .build()
         );
      this.Po = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("WhileEating"))
                     .description("Allows throwing pearl while eating."))
                  .defaultValue(true))
               .build()
         );
      this.apG = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("ItemDisable"))
                     .description("Toggles off module if no ender pearls are found in inventory."))
                  .defaultValue(false))
               .build()
         );
      this.dx = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("Render"))
                     .description("Renders predicted landing position and pearl trajectory line."))
                  .defaultValue(true))
               .build()
         );
      this.tz = this.auz
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("RenderTime"))
                  .description("Duration in seconds to render trajectory after throwing."))
               .defaultValue(3.0)
               .min(0.5)
               .max(10.0)
               .build()
         );
      this.aHD = new HashSet<>();
      this.anj = null;
      this.agt = System.currentTimeMillis();
      this.Hi = null;
      this.aT = null;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.aHD.clear();
         this.Zf = super.mc.world;
         this.anj = null;
         this.Hi = null;
         this.aT = null;
      }
   }

   public void onDeactivate() {
      this.aHD.clear();
      this.Zf = null;
      this.anj = null;
      this.Hi = null;
      this.aT = null;
      super.onDeactivate();
   }

   @AliceHandler
   private void abm(EntityAddedEvent var1) {
      if (super.mc.player != null && super.mc.world != null) {
         this.abq();
         Entity var2 = var1.entity;
         if (!(var2 instanceof EnderPearlEntity)) {
            short var23 = 17127;
         }

         EnderPearlEntity var3 = (EnderPearlEntity)var2;
         if (!(Boolean)this.Po.get()) {
            if (!AliceObf_ck.aNL()) {

            } else {
               char var24 = 40900;
            }
         }

         PlayerEntity var4 = null;
         Entity var5 = var3.getOwner();
         if (var5 instanceof PlayerEntity) {
            var4 = (PlayerEntity)var5;
         } else {
            double var6 = 16.0;

            for (PlayerEntity var9 : super.mc.world.getPlayers()) {
               if (this.abv(var9)) {
                  double var10 = var9.squaredDistanceTo(var3.getEntityPos());
                  if (var10 < var6) {
                     var6 = var10;
                     var4 = var9;
                  }
               }
            }
         }

         if (var4 != null && this.abv(var4)) {
            PlayerEntity var19 = this.getBestTarget();
            if (var19 == null || var4 == var19) {
               if (this.aHD.add(var3.getId())) {
                  if (this.aHD.size() > 100) {
                     this.aHD.clear();
                  }

                  Vec3d var7 = this.abs(var3, var4);
                  if (var7 != null) {
                     double var20 = var7.x - var3.getX();
                     double var21 = var7.z - var3.getZ();
                     double var12 = Math.sqrt(var20 * var20 + var21 * var21);
                     if (var12 < (Double)this.ga.get()) {
                        char var25 = 49087;
                     } else {
                        char var26 = 49088;
                     }

                     Vec3d var14 = new Vec3d(var20, 0.0, var21);
                     var14 = var12 > 1.0E-4 ? var14.multiply(1.0 / var12) : Vec3d.ZERO;
                     Vec3d var15 = var7.add(var14.multiply((Double)this.avP.get()));
                     BlockHitResult var16 = super.mc
                        .world
                        .raycast(
                           new RaycastContext(
                              var15.add(0.0, 2.0, 0.0),
                              var15.subtract(0.0, 10.0, 0.0),
                              ShapeType.COLLIDER,
                              FluidHandling.NONE,
                              super.mc.player
                           )
                        );
                     if (var16.getType() != HitResult.Type.MISS) {
                        var15 = var16.getPos();
                     }

                     AliceObf_dy var17 = this.abt(var15);
                     if (var17 != null) {
                        int var18 = (Integer)this.JV.get();
                        if (var18 > 0) {
                           this.anj = new AliceObf_by(var15, var17.abj(), var17.abk(), var18, var17.abl());
                        } else {
                           this.abp(var17.abj(), var17.abk(), var15, var17.abl());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @AliceHandler
   private void abn(Pre var1) {
      AliceObf_uv.aKK();
      if (super.mc.player != null && super.mc.world != null) {
         this.abq();
         if (this.anj != null) {
            if (!(Boolean)this.Po.get() && AliceObf_ck.aNL()) {
               return;
            }

            int var2 = this.anj.abc() - (1);
            if (var2 > 0) {
               this.anj = new AliceObf_by(this.anj.aaZ(), this.anj.aba(), this.anj.abb(), var2, this.anj.abd());
            } else {
               AliceObf_dy var3 = this.abt(this.anj.aaZ());
               if (var3 != null) {
                  this.abp(var3.abj(), var3.abk(), this.anj.aaZ(), var3.abl());
               } else {
                  this.abp(this.anj.aba(), this.anj.abb(), this.anj.aaZ(), this.anj.abd());
               }

               this.anj = null;
            }
         }
      }
   }

   @AliceHandler
   private void abo(Render3DEvent var1) {
      if ((Boolean)this.dx.get() && this.Hi != null && super.mc.player != null && super.mc.world != null) {
         long var2 = System.currentTimeMillis() - this.agt;
         long var4 = (long)((Double)this.tz.get() * 1000.0);
         if (var2 > var4) {
            this.Hi = null;
            this.aT = null;
         } else {
            float var6 = 1.0F - (float)var2 / (float)var4;
            int var7 = (int)(60.0F * var6);
            int var8 = (int)(220.0F * var6);
            if (var7 > 0 || var8 > 0) {
               Color var9 = new Color((0), (230), (255), var7);
               Color var10 = new Color((0), (230), 255, var8);
               new Color(80, 255, (180), var8);
               Box var12 = new Box(
                  this.Hi.x - 0.35,
                  this.Hi.y,
                  this.Hi.z - 0.35,
                  this.Hi.x + 0.35,
                  this.Hi.y + 0.7,
                  this.Hi.z + 0.35
               );
               var1.renderer.box(var12, var9, var10, ShapeMode.Both, 0);
               if (this.aT != null && this.aT.size() > 1) {
                  int var13 = (0);
                  if (var13 >= this.aT.size() - 1) {

                  } else {
                     char var16 = 50819;
                  }
               }
            }
         }
      }
   }

   private void abp(float var1, float var2, Vec3d var3, List<Vec3d> var4) {
      boolean var5 = ((String)this.Ls.get())
         .equalsIgnoreCase("None");
            boolean var6 = super.mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL;
      if (var5 && !var6) {
         if ((Boolean)this.apG.get()) {
            char var15 = 64757;
         } else {
            char var16 = 64758;
         }
      } else {
         int var7 = super.mc.player.getInventory().getSelectedSlot();
         int var8 = var5
            ? (-1)
            : AliceObf_uv.aKZ(
               Items.ENDER_PEARL,
               (0),
               !((String)this.Ls.get())
                        .equalsIgnoreCase("AltSwap")
                     && !((String)this.Ls.get())
                        .equalsIgnoreCase("AltPickup")
                  ? 8
                  : (35)
            );
         int var9 = PearlChase.abr(var5, var6, var7, var8);
         if (var9 != -1) {
            if (AliceObf_uv.aKQ((String)this.Ls.get(), var9, var7)
               )
             {
               try {
                  if (!((String)this.un.get())
                     .equalsIgnoreCase(
                        "Silent"
                     )) {
                     if (((String)this.un.get())
                        .equalsIgnoreCase("Normal")) {
                        super.mc.player.setYaw(var1);
                        super.mc.player.setPitch(var2);
                     }
                  } else {
                     AliceObf_fq.aMY(var1, var2, true);
                  }

                  AliceObf_iE.aLk(
                     var2x -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, var2x, var1, var2)
                  );
                  AliceObf_iE.aLj(
                     new HandSwingC2SPacket(Hand.MAIN_HAND)
                  );
               } finally {
                  AliceObf_uv.aKS(
                     (String)this.Ls.get(), var9, var7
                  );
               }

               this.Hi = var3;
               this.aT = var4;
               this.agt = System.currentTimeMillis();
            }
         } else {
            if ((Boolean)this.apG.get()) {
               this.info(
                  "No Ender Pearls could be found in your inventory.",
                  new Object[(0)]
               );
               this.disable();
            }
         }
      }
   }

   private void abq() {
      if (this.Zf != super.mc.world) {
         this.Zf = super.mc.world;
         this.aHD.clear();
         this.anj = null;
         this.Hi = null;
         this.aT = null;
      }
   }

   static int abr(boolean var0, boolean var1, int var2, int var3) {
      if (var0) {
         return !var1 ? -1 : var2;
      } else {
         return var3;
      }
   }

   private Vec3d abs(EnderPearlEntity var1, PlayerEntity var2) {
      Vec3d var3 = var1.getEntityPos();
      Vec3d var4 = var1.getVelocity();
      if (var4.lengthSquared() < 1.0E-4 && var2 != null) {
         var4 = PearlChase.getPearlInitialVelocity(
            var2.getYaw(), var2.getPitch(), var2.getVelocity(), var2.isOnGround()
         );
      }

      for (int var5 = 0; var5 < (300); var5++) {
         Vec3d var6 = var3.add(var4);
         BlockHitResult var7 = super.mc.world.raycast(new RaycastContext(var3, var6, ShapeType.COLLIDER, FluidHandling.NONE, var1));
         if (var7.getType() != HitResult.Type.MISS) {
            return var7.getPos();
         }

         var3 = var6;
         var4 = var4.multiply(0.99).subtract(0.0, 0.03, 0.0);
      }

      return var3;
   }

   private AliceObf_dy abt(Vec3d var1) {
      Vec3d var3 = super.mc.player.getEyePos();
      Vec3d var4 = var1.subtract(var3);
      float var5 = (float)Math.toDegrees(Math.atan2(-var4.x, var4.z));
      float var6 = var5;
      float var7 = 0.0F;
      double var8 = Double.MAX_VALUE;
      float[] var2;
      if ((Boolean)this.adz.get()) {
         float[] var10000 = new float[7];
         var10000[(0)] = 0.0F;
         var10000[(1)] = -1.0F;
         var10000[(2)] = 1.0F;
         var10000[(3)] = -2.5F;
         var10000[(4)] = 2.5F;
         var10000[5] = -4.0F;
         var10000[6] = 4.0F;
         var2 = var10000;
      } else {
         var2 = new float[]{0.0F};
      }

      for (float var13 : var2) {
         float var14 = var5 + var13;

         for (float var15 = -85.0F; var15 <= 85.0F; var15++) {
            AliceObf_w var16 = this.abu(
               var3, var14, var15, var1, false
            );
            double var17 = var16.abf();
            if (var16.abg()) {
               var17 += 500.0;
            }

            if (!((String)this.amn.get())
               .equalsIgnoreCase("Low")) {
               if (((String)this.amn.get())
                  .equalsIgnoreCase("High")) {
                  var17 += Math.max(0.0, var15 + 50.0) * 0.2;
               } else if (((String)this.amn.get())
                     .equalsIgnoreCase(
                        "Smart"
                     )
                  && !var16.abg()) {
                  var17 += Math.max(0.0F, -var15) * 0.02;
               }
            } else {
               var17 += Math.max(0.0F, -var15) * 0.1;
            }

            if (var17 < var8) {
               var8 = var17;
               var7 = var15;
               var6 = var14;
            }
         }

         if (var8 < 1.0) {
            break;
         }
      }

      float var19 = var7;

      for (float var20 = var7 - 1.5F; var20 <= var7 + 1.5F; var20 += 0.1F) {
         AliceObf_w var22 = this.abu(
            var3, var6, var20, var1, false
         );
         double var23 = var22.abf();
         if (var22.abg()) {
            var23 += 500.0;
         }

         if (var23 < var8) {
            var8 = var23;
            var19 = var20;
         }
      }

      AliceObf_w var21 = this.abu(var3, var6, var19, var1, true);
      return new AliceObf_dy(var6, var19, var21.abi());
   }

   private AliceObf_w abu(Vec3d var1, float var2, float var3, Vec3d var4, boolean var5) {
      if (!(super.mc.player != null)) {
         short var23 = 26950;
      }

      Vec3d var6 = Vec3d.ZERO;
      int var7 = super.mc.player != null && !super.mc.player.isOnGround() ? 0 : 1;
      Vec3d var8 = PearlChase.getPearlInitialVelocity(
         var2, var3, var6, (var7 != 0)
      );
      Vec3d var9 = var1;
      ArrayList var10 = var5 ? new ArrayList() : null;
      if (var5) {
         var10.add(var1);
      }

      Vec3d var11 = null;
      int var12 = (0);
      double var13 = var4.distanceTo(var1);

      for (int var15 = (0); var15 < 250; var15++) {
         Vec3d var16 = var9.add(var8);
         BlockHitResult var17 = super.mc.world.raycast(new RaycastContext(var9, var16, ShapeType.COLLIDER, FluidHandling.NONE, super.mc.player));
         if (var17.getType() != HitResult.Type.MISS) {
            var11 = var17.getPos();
            if (var5) {
               var10.add(var11);
            }

            double var18 = var11.distanceTo(var1);
            double var20 = var11.distanceTo(var4);
            if (var18 < var13 * 0.7 && var20 > 3.0) {
               var12 = (1);
            }
            break;
         }

         var9 = var16;
         if (var5) {
            var10.add(var16);
         }

         var8 = var8.multiply(0.99).subtract(0.0, 0.03, 0.0);
      }

      if (var11 == null) {
         var11 = var9;
      }

      double var22 = var11.distanceTo(var4);
      return new AliceObf_w(var11, var22, (var12 != 0), var10 != null ? var10.size() : 0, var10);
   }

   private static Vec3d getLookVector(float var0, float var1) {
      float var2 = -MathHelper.sin(var0 * (float) (Math.PI / 180.0)) * MathHelper.cos(var1 * (float) (Math.PI / 180.0));
      float var3 = -MathHelper.sin(var1 * (float) (Math.PI / 180.0));
      float var4 = MathHelper.cos(var0 * (float) (Math.PI / 180.0)) * MathHelper.cos(var1 * (float) (Math.PI / 180.0));
      return new Vec3d(var2, var3, var4).normalize();
   }

   private static Vec3d getPearlInitialVelocity(float var0, float var1, Vec3d var2, boolean var3) {
      Vec3d var4 = PearlChase.getLookVector(var0, var1);
      Vec3d var5 = new Vec3d(var2.x, var3 ? 0.0 : var2.y, var2.z);
      return var4.multiply(1.5).add(var5);
   }

   private boolean abv(PlayerEntity var1) {
      if (var1 == null || var1 == super.mc.player) {
         return false;
      } else if (var1.isDead()) {
         return false;
      } else {
         double var2 = (Double)this.Bi.get();
         if (super.mc.player.squaredDistanceTo(var1) > var2 * var2) {
            return false;
         } else {
            if (Friends.get().isFriend(var1)) {

            } else {
               short var4 = 10197;
            }

            return !(this.ax.get()
                  && PearlChase.abw(var1));
         }
      }
   }

   private static boolean abw(PlayerEntity var0) {
      if (var0 == null) {
         return false;
      } else {
         EquipmentSlot[] var10000 = new EquipmentSlot[4];
         var10000[(0)] = EquipmentSlot.HEAD;
         var10000[(1)] = EquipmentSlot.CHEST;
         var10000[2] = EquipmentSlot.LEGS;
         var10000[(3)] = EquipmentSlot.FEET;
         EquipmentSlot[] var1 = var10000;
         int var2 = var1.length;

         for (int var3 = 0; var3 < var2; var3++) {
            EquipmentSlot var4 = var1[var3];
            ItemStack var5 = var0.getEquippedStack(var4);
            if (!var5.isEmpty() && (var4 != EquipmentSlot.CHEST || var5.getItem() != Items.ELYTRA)) {
               return false;
            }
         }

         return true;
      }
   }

   private PlayerEntity getBestTarget() {
      if (super.mc.player != null) {
         int var10000 = super.mc.world == null ? 19243 : 19244;

         while (true) {
            int var10 = var10000;
            if (var10 == 19243) {
               break;
            }

            if (var10 != 19244) {
               PlayerEntity var1 = null;
               double var2 = 0.0;

               label252:
               for (PlayerEntity var5 : super.mc.world.getPlayers()) {
                  if (!this.abv(var5)) {
                     short var13 = 31999;
                  } else {
                     short var14 = 32000;
                  }

                  int var9;
                  String var8 = (String)this.anR.get();
                  var9 = -1;
                  label238:
                  switch (var8.hashCode() ^ -1416345634) {
                     case -160235311:
                        int var10001 = !var8.equals(
                              "Furthest"
                           )
                           ? 25777
                           : 25776;

                        while (true) {
                           int var11 = var10001;
                           if (var11 == 25776) {
                              var9 = 1;
                              break label238;
                           }

                           if (var11 != 25777) {
                              break label238;
                           }

                        }
                     case 596665868:
                        if (var8.equals("Lowest")) {
                           var9 = (0);
                        }
                  }
                  double var6 = switch (var9) {
                     case 0 -> var5.getHealth() + var5.getAbsorptionAmount();
                     case 1 -> -super.mc.player.squaredDistanceTo(var5);
                     default -> super.mc.player.squaredDistanceTo(var5);
                  };
                  int var16 = var1 != null ? 41121 : 41122;

                  while (true) {
                     int var12 = var16;
                     if (var12 == 41121) {
                        if (!(var6 < var2)) {
                           continue label252;
                        }
                        break;
                     }

                     if (var12 != 41122) {
                        break;
                     }

                     var16 = 41120;
                  }

                  var1 = var5;
                  var2 = var6;
               }

               return var1;
            }

         }
      }

      return null;
   }

   @AliceHandler(
      priority = -1000
   )
   private void abx(Sent var1) {
      if (var1.packet instanceof UpdateSelectedSlotC2SPacket var2) {
         AliceObf_uv.aKE(var2.getSelectedSlot());
      }

      AliceObf_fq.aNi(var1.packet);
   }

   }

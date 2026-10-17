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
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Hand;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.TypeFilter;

public class SelfTrap extends AliceModule {
   private final SettingGroup aqt = this.settings.getDefaultGroup();
   private final AliceObf_fN aEY;
   private final Setting<Boolean> cU;
   private final Setting<String> aBO;
   private final Setting<String> JA;
   private final Setting<Boolean> wZ;
   private final Setting<Integer> pJ;
   private final Setting<Boolean> afG;
   private final Setting<Boolean> A;
   private final Setting<String> BO;
   private final Setting<Boolean> Ma;
   private final Setting<Boolean> Ep;
   private final Setting<Boolean> kC;
   private final Setting<Boolean> ahD;
   private final Setting<Double> tG;
   private final Setting<Integer> aDo;
   private final Setting<Boolean> hr;
   private final Setting<Boolean> aOv;
   private final Setting<Boolean> aaU;
   private final Setting<Boolean> Bh;
   private final Setting<Boolean> vK;
   private final Setting<String> Ly;
   private final Setting<Boolean> axs;
   private final Setting<Boolean> Jf;
   private final Setting<Boolean> aix;
   private int Xj;
   private boolean vb;
   private volatile List<BlockPos> ct;
   private BlockPos adw;
   private boolean ag;
   private int oE;
   private final Set<Integer> arB;
   public SelfTrap() {
      super(AliceAddon.PVP_CATEGORY, "SelfTrap", "Automatically places blocks around you to prevent other people from getting inside your hole.");
      this.aEY = new AliceObf_fN("SelfTrap");
      this.cU = this.aqt
         .add(((Builder)((Builder)((Builder)((Builder)new Builder().name("Diagnostics")).description("Records the next activation for up to 15 seconds in logs/alice-placement. Keeps three rotating files; includes coordinates. Does not change placement.")).defaultValue(false)).onChanged(var1 -> {
            if (!var1) {
               this.aEY.aMt("setting-off");
            }
         })).build());
      this.aBO = this.aqt
         .add(
            ((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)new meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder()
                        .name("Switch"))
                     .description("The mode that will be used for automatically switching to necessary items."))
                  .defaultValue("Silent"))
               .supplier(() -> AliceObf_uv.w)
               .build()
         );
      this.JA = this.aqt
         .add(
            ((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)new meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder()
                        .name("Mode"))
                     .description("The offsets that will be used when trapping."))
                  .defaultValue("Full"))
               .supplier(
                  () -> new String[]{
                     "Partial",
                     "Full"
                  }
               )
               .build()
         );
      this.wZ = this.aqt
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("Head")).description("Whether or not to cover the block on the players head.")).defaultValue(true))
                  .visible(
                     () -> ((String)this.JA.get())
                        .equalsIgnoreCase("Full")
                  ))
               .build()
         );
      this.pJ = this.aqt
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("BPT"))
                     .description("The maximum number of blocks to place per tick. Each place sends its own tick packet plus interact, so higher values burst packets and risk kicks."))
                  .defaultValue(4))
               .min(1)
               .max(8)
               .build()
         );
      this.afG = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("Sync")).description("Only places against blocks the client has already registered. Does not use blocks sent earlier in the same burst as supports.")).defaultValue(false)).build());
      this.A = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("RetryPlace")).description("Retries targets that failed during the burst once more after it finishes. A missing face can appear once the burst's own supports exist.")).defaultValue(false)).build());
      this.BO = this.aqt
         .add(
            ((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)new meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder()
                        .name("Rotate"))
                     .description("The rotation mode when placing blocks. Normal and Grim both send silent rotations (identical on the wire); None sends no rotation and validates against your real look."))
                  .defaultValue("Grim"))
               .supplier(
                  () -> new String[]{
                     "None",
                     "Normal",
                     "Grim"
                  }
               )
               .build()
         );
      this.Ma = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("StrictDirection")).description("Only places using directions that face you.")).defaultValue(false)).build());
      this.Ep = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("AirPlace")).description("Places blocks in the air without needing neighboring blocks.")).defaultValue(false)).build());
      this.kC = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("Attack")).description("Attacks any crystal that interferes with your trap.")).defaultValue(true)).build());
      this.ahD = this.aqt
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("AttackRotate")).description("Rotates toward the crystal before attacking it.")).defaultValue(true))
                  .visible(() -> (Boolean)this.kC.get()))
               .build()
         );
      this.tG = this.aqt
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("AttackRange"))
                     .description("The maximum range at which crystals will be attacked."))
                  .defaultValue(3.0)
                  .min(1.0)
                  .max(6.0)
                  .visible(() -> (Boolean)this.kC.get()))
               .build()
         );
      this.aDo = this.aqt
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("AttackAge"))
                        .description("The minimum age (in ticks) a crystal must reach before being attacked."))
                     .defaultValue(5))
                  .min(0)
                  .max(20)
                  .visible(() -> (Boolean)this.kC.get()))
               .build()
         );
      this.hr = this.aqt
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("AttackMultiTask")).description("Allows attacking while an item is already in use.")).defaultValue(true))
                  .visible(() -> (Boolean)this.kC.get()))
               .build()
         );
      this.aOv = this.aqt
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("AttackSwing")).description("Sends a swing packet whenever attacking a crystal.")).defaultValue(true))
                  .visible(() -> (Boolean)this.kC.get()))
               .build()
         );
      this.aaU = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("AntiStep")).description("Adds additional blocks that prevent anyone from stepping out of the hole.")).defaultValue(false)).build());
      this.Bh = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("AntiBomb")).description("Places an extra block above your head to prevent you from getting bombed.")).defaultValue(false)).build());
      this.vK = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("HoleCheck")).description("Only self traps whenever you are in a hole.")).defaultValue(false)).build());
      this.Ly = this.aqt
         .add(
            ((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)new meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder()
                        .name("AutoJump"))
                     .description("Automatically jumps to place high blocks when on-ground eye height is too low for StrictDirection."))
                  .defaultValue("Grim"))
               .supplier(
                  () -> new String[]{
                     "None",
                     "NCP",
                     "Grim"
                  }
               )
               .build()
         );
      this.axs = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("WhileEating")).description("Places blocks normally while eating.")).defaultValue(true)).build());
      this.Jf = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("SelfDisable")).description("Toggles off the module once it is finished with placing.")).defaultValue(true)).build());
      this.aix = this.aqt.add(((Builder)((Builder)((Builder)new Builder().name("Render")).description("Whether or not to render the place position.")).defaultValue(true)).build());
      this.Xj = 0;
      this.vb = false;
      this.ct = new ArrayList<>();
      this.adw = null;
      this.ag = false;
      this.oE = 0;
      this.arB = new HashSet<>();
   }

   public String getInfoString() {
      return PremiumAccessGate.isAllowed()
         ? this.Xj
            + (
               this.cU.get()
                  ? ("" + this.aEY.aMs())
                  : ""
            )
         : null;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.aEY
            .aMr(
               (Boolean)this.cU.get(),
               (String)this.aBO.get()
                  + this.pJ.get()
                  + (String)this.BO.get()
                  + this.afG.get()
                  + this.A.get()
                  + this.Ep.get()
                  + this.Ma.get()
                  + (String)this.JA.get()
                  + this.kC.get()
                  + this.ahD.get()
                  + (String)this.Ly.get()
                  + this.vK.get()
                  + this.axs.get()
                  + this.Jf.get()
                  + this.wZ.get()
                  + this.aaU.get()
                  + this.Bh.get()
            );
         if (super.mc.player != null && super.mc.world != null) {
            this.Xj = 0;
            this.ct.clear();
            this.adw = super.mc.player.getBlockPos();
            this.ag = false;
         }
      }
   }

   public void onDeactivate() {
      this.aEY.aMu();
      AliceObf_nA.aNq();
      this.ct = new ArrayList();
      this.adw = null;
      this.ag = false;
      super.onDeactivate();
   }

   @AliceHandler
   private void afs(Pre var1) {
      AliceObf_kE var2 = this.aEY
         .aMv("tick-pre");

      try {
         this.aft();
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void aft() {
      AliceObf_uv.aKK();
      if (super.mc.player != null) {
         if (!(super.mc.world == null)) {
            char var35 = 55308;
         }

         if (!(Boolean)this.axs.get()) {

         } else {
            char var36 = 37818;
         }

         try {
            this.oE = 0;
            this.arB.clear();
            ItemStack var1 = super.mc.player.getMainHandStack();
            if (((String)this.aBO.get())
                  .equalsIgnoreCase("None")
               && (!(var1.getItem() instanceof BlockItem var2) || !this.afB(var2.getBlock()))) {
               this.info("het block kia thang ngu", new Object[0]);
               AliceObf_fN.aMA(
                  "held-item-rejected", (String)this.aBO.get(), -1
               );
               this.disable();
               this.ct = new ArrayList();
               return;
            }

            if (super.mc.player.isOnGround()) {
               this.adw = super.mc.player.getBlockPos();
               this.ag = false;
            }

            if (this.afA(this.adw)) {
               AliceObf_fN.aMx(
                  "SKIP",
                  "trap-complete"
               );
               int var44 = this.Jf.get() ? 31759 : 31760;

               while (true) {
                  int var12 = var44;
                  if (var12 == 31759) {
                     if (super.mc.player.isOnGround() && !this.ag) {
                        this.disable();
                     }
                     break;
                  }

                  if (var12 != 31760) {
                     break;
                  }

                  var44 = 31758;
               }

               return;
            }

            if ((Boolean)this.vK.get()
               && !AliceObf_jO.aKx(super.mc.player)
               && (
                  ((String)this.Ly.get())
                        .equalsIgnoreCase("None")
                     || super.mc.player.isOnGround()
               )) {
               AliceObf_fN.aMx(
                  "SKIP",
                  "hole-check"
               );
               return;
            }

            int var20 = this.afC(
               0,
               !((String)this.aBO.get())
                        .equalsIgnoreCase("AltSwap")
                     && !((String)this.aBO.get())
                        .equalsIgnoreCase("AltPickup")
                  ? 8
                  : 35
            );
            int var21 = super.mc.player.getInventory().getSelectedSlot();
            if (var20 == -1) {
               AliceObf_fN.aMA(
                  "lookup-returned-minus-one",
                  (String)this.aBO.get(),
                  var20
               );
               this.info("het block kia thang ngu", new Object[0]);
               this.disable();
               this.ct = new ArrayList();
               return;
            }

            this.ct = AliceObf_jO.getTrapPositions(
                  super.mc.player,
                  this.adw,
                  ((String)this.JA.get())
                     .equalsIgnoreCase("Partial"),
                  (Boolean)this.wZ.get(),
                  (Boolean)this.aaU.get(),
                  (Boolean)this.Bh.get(),
                  (Boolean)this.Ma.get(),
                  (Boolean)this.Ep.get()
               )
               .stream()
               .filter(var0 -> AliceObf_ck.aNB(var0, true))
               .sorted(Comparator.comparingInt(Vec3i::getY))
               .toList();
            if ((Boolean)this.kC.get()) {
               this.afw();
            }

            int var10002 = !this.ct.isEmpty() ? 55806 : 55805;

            while (true) {
               int var13 = var10002;
               if (var13 == 55805) {
                  AliceObf_fN.aMx(
                     "SKIP",
                     "no-placeable-targets"
                  );
                  var10002 = this.Jf.get() ? 34906 : 34907;

                  while (true) {
                     int var14 = var10002;
                     if (var14 == 34906) {
                        if (super.mc.player.isOnGround() && !this.ag) {
                           if (!this.afA(this.adw)) {
                              short var42 = 31795;
                           } else {
                              short var43 = 31794;
                           }
                        }
                        break;
                     }

                     if (var14 != 34907) {
                        break;
                     }

                  }

                  return;
               }

               if (var13 != 55806) {
                  if (!((String)this.Ly.get())
                        .equalsIgnoreCase("None")
                     && (Boolean)this.Ma.get()
                     && super.mc.player.isOnGround()
                     && !(Boolean)this.Ep.get()) {
                     boolean var4 = false;
                     Iterator var5 = this.ct.iterator();

                     while (true) {
                        if (var5.hasNext()) {
                           BlockPos var6 = (BlockPos)var5.next();
                           if (var6.getY() < super.mc.player.getBlockY() + 2) {
                              continue;
                           }

                           if (AliceObf_ck.getDirection(
                                 var6, (Boolean)this.Ma.get()
                              )
                              != null) {
                              short var37 = 7073;
                           } else {
                              short var38 = 7074;
                           }

                           var4 = true;
                        }

                        if (var4) {
                           short var39 = 26691;
                        } else {
                           short var40 = 26692;
                        }
                        break;
                     }
                  }

                  this.vb = true;
                  AliceObf_fq.aNc();
                  boolean var17 = false /* VF: Semaphore variable */;

                  label794: {
                     label795: {
                        try {
                           var17 = true;
                           if (this.oE >= (Integer)this.pJ.get()) {
                              var17 = false;
                              break label794;
                           }

                           if (AliceObf_uv.aKQ(
                              (String)this.aBO.get(), var20, var21
                           )) {
                              ArrayList var22 = new ArrayList();
                              ArrayList<BlockPos> var23 = new ArrayList<>();

                              for (BlockPos var7 : this.ct) {
                                 if (this.oE >= (Integer)this.pJ.get()) {
                                    break;
                                 }

                                 if ((Boolean)this.kC.get()) {
                                    for (EndCrystalEntity var9 : super.mc
                                       .world
                                       .getEntitiesByType(TypeFilter.instanceOf(EndCrystalEntity.class), new Box(var7).expand(0.5), var0 -> true)) {
                                       if (var9.isAlive() && !var9.isRemoved()) {
                                          if ((Boolean)this.ahD.get()) {
                                             float[] var10 = AliceObf_iI.getExactRotations(
                                                super.mc.player, var9.getBoundingBox().getCenter()
                                             );
                                             AliceObf_fq.aNb(
                                                var10[0], var10[1], true
                                             );
                                          }

                                          this.afv(var9);
                                       }
                                    }
                                 }

                                 if (this.afz(var7, var22)) {
                                    this.oE++;
                                 } else if ((Boolean)this.A.get()) {
                                    var23.add(var7);
                                 }
                              }

                              if ((Boolean)this.A.get() && !var23.isEmpty()) {
                                 for (BlockPos var26 : var23) {
                                    if (this.oE >= (Integer)this.pJ.get()) {
                                       break;
                                    }

                                    if (this.afz(var26, var22)) {
                                       this.oE++;
                                    }
                                 }
                              }

                              if (!super.mc.player.isOnGround()
                                 && ((String)this.Ly.get())
                                    .equalsIgnoreCase(
                                       "NCP"
                                    )) {
                                 super.mc
                                    .execute(
                                       () -> {
                                          if (super.mc.player != null && !super.mc.player.isOnGround()) {
                                             super.mc
                                                .player
                                                .setVelocity(
                                                   new Vec3d(
                                                      super.mc.player.getVelocity().x, -0.42, super.mc.player.getVelocity().z
                                                   )
                                                );
                                          }
                                       }
                                    );
                              }

                              AliceObf_uv.aKS(
                                 (String)this.aBO.get(), var20, var21
                              );
                              if ((Boolean)this.Jf.get()) {
                                 if (super.mc.player.isOnGround()) {
                                    if (!this.ag) {
                                       if (this.afA(this.adw)) {
                                          this.disable();
                                          var17 = false;
                                       } else {
                                          var17 = false;
                                       }
                                    } else {
                                       var17 = false;
                                    }
                                 } else {
                                    var17 = false;
                                 }
                              } else {
                                 var17 = false;
                              }
                              break label795;
                           }

                           var17 = false;
                        } finally {
                           if (var17) {
                              AliceObf_fq.aNe();
                              if (AliceObf_fq.aMU()) {
                                 AliceObf_fq.aNg();
                                 AliceObf_fq.setSilentSyncRequired(
                                    false
                                 );
                              }

                              this.vb = false;
                           }
                        }

                        AliceObf_fq.aNe();
                        if (AliceObf_fq.aMU()) {
                           AliceObf_fq.aNg();
                           AliceObf_fq.setSilentSyncRequired(false);
                        }

                        this.vb = false;
                        return;
                     }

                     AliceObf_fq.aNe();
                     if (AliceObf_fq.aMU()) {
                        AliceObf_fq.aNg();
                        AliceObf_fq.setSilentSyncRequired(false);
                     }

                     this.vb = false;
                     break;
                  }

                  AliceObf_fq.aNe();
                  if (AliceObf_fq.aMU()) {
                     AliceObf_fq.aNg();
                     AliceObf_fq.setSilentSyncRequired(false);
                  }

                  this.vb = false;
                  return;
               }

            }
         } catch (Exception var19) {
            if (AliceObf_fN.aMw()) {
               AliceObf_fN.aMx(
                  "MODULE_EXCEPTION", var19.getClass().getName()
               );
            }
         }
      }
   }

   @AliceHandler
   private void afu(Receive var1) {
      Packet var3 = var1.packet;
      if (!(var3 instanceof EntitySpawnS2CPacket)) {
         char var11 = 48096;
      }
   }

   private void afv(EndCrystalEntity var1) {
      if (!(var1 != null)) {
         short var2 = 27614;
      }
   }

   private void afw() {
      for (EndCrystalEntity var2 : super.mc
         .world
         .getEntitiesByType(
            TypeFilter.instanceOf(EndCrystalEntity.class),
            new Box(super.mc.player.getBlockPos()).expand((Double)this.tG.get() + 3.0),
            var0 -> true
         )) {
         if (var2.isAlive()) {

         } else {
            char var7 = 38311;
         }
      }
   }

   @AliceHandler
   private void afx(Render3DEvent var1) {
      if ((Boolean)this.aix.get() && super.mc.player != null && super.mc.world != null) {
         List<BlockPos> var2 = this.ct;
         if (var2 != null && !var2.isEmpty()) {
            Color var3 = new Color(0, 230, 255, 60);
            Color var4 = new Color(0, 230, 255, 220);

            for (BlockPos var6 : var2) {
               var1.renderer.box(new Box(var6), var3, var4, ShapeMode.Both, 0);
            }
         }
      }
   }

   private List<BlockPos> afy(List<BlockPos> var1) {
      return !this.afG.get() ? var1 : List.of();
   }

   private boolean afz(BlockPos var1, List<BlockPos> var2) {
      if (AliceObf_fN.aMw()) {
         AliceObf_fN.aMy(
            "TARGET",
            var1,
            "" + this.oE + this.pJ.get() + var2
         );
      }

      if ((Boolean)this.afG.get() && var2.contains(var1)) {
         return AliceObf_fN.aMz(
            var1, "sync-already-attempted", false
         );
      } else {
         List var3 = this.afy(var2);
         Direction var4 = AliceObf_ck.getDirection(
            var1, var3, (Boolean)this.Ma.get()
         );
         if (var4 == null) {
            if (!(Boolean)this.Ep.get()) {
               BlockPos var5 = AliceObf_ck.aNE(
                  var1, var3, (Boolean)this.Ma.get(), 2
               );
               if (var5 != null && (Boolean)this.afG.get() && var2.contains(var5)) {
                  return AliceObf_fN.aMz(
                     var1, "support-already-attempted", false
                  );
               } else {
                  Direction var6 = var5 == null
                     ? null
                     : AliceObf_ck.getDirection(
                        var5, var3, (Boolean)this.Ma.get()
                     );
                  if (var5 != null
                     && var6 != null
                     && AliceObf_ck.aNy(
                        var5, var6, Hand.MAIN_HAND, (String)this.BO.get(), true, (Boolean)this.aix.get()
                     )) {
                     var2.add(var5);
                     return true;
                  } else {
                     return AliceObf_fN.aMz(
                        var1, "support-unavailable-or-place-failed", false
                     );
                  }
               }
            } else {
               AliceObf_ck.aNz(
                  var1, Hand.MAIN_HAND, (String)this.BO.get(), (Boolean)this.aix.get()
               );
               var2.add(var1);
               return true;
            }
         } else if (!AliceObf_ck.aNy(
            var1, var4, Hand.MAIN_HAND, (String)this.BO.get(), true, (Boolean)this.aix.get()
         )) {
            return false;
         } else {
            var2.add(var1);
            return true;
         }
      }
   }

   private boolean afA(BlockPos var1) {
      if (super.mc.world != null && var1 != null) {
         if (((String)this.JA.get())
            .equalsIgnoreCase("Partial")) {
            BlockPos var5 = var1.up(2);
            return !super.mc.world.getBlockState(var5).isReplaceable();
         } else {
            for (BlockPos var4 : AliceObf_jO.getTrapPositions(
               super.mc.player,
               var1,
               false,
               (Boolean)this.wZ.get(),
               (Boolean)this.aaU.get(),
               (Boolean)this.Bh.get(),
               (Boolean)this.Ma.get(),
               (Boolean)this.Ep.get()
            )) {
               if (super.mc.world.getBlockState(var4).isReplaceable()) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private boolean afB(Block var1) {
      if (var1 != Blocks.OBSIDIAN && var1 != Blocks.ENDER_CHEST && var1 != Blocks.CRYING_OBSIDIAN) {
         if (!(var1 != Blocks.NETHERITE_BLOCK)) {
            char var3 = 56798;
         }
      }

      return true;
   }

   private int afC(int var1, int var2) {
      float var3 = -1.0F;
      int var4 = -1;

      for (int var5 = var1; var5 <= var2; var5++) {
         ItemStack var6 = super.mc.player.getInventory().getStack(var5);
         if (AliceObf_fN.aMw()) {
            AliceObf_fN.aMx(
               "SLOT_SCAN",
               "" + var5 + var6.getCount() + (var6.getItem() instanceof BlockItem var7 && this.afB(var7.getBlock()))
            );
         }

         if (var6.getItem() instanceof BlockItem var9 && this.afB(var9.getBlock())) {
            float var11 = var9.getBlock().getHardness();
            if (var11 == -1.0F) {
               return var5;
            }

            if (var11 > var3) {
               var3 = var11;
               var4 = var5;
            }
         }
      }

      AliceObf_fN.aMA(
         "lookup-result", (String)this.aBO.get(), var4
      );
      return var4;
   }

   @AliceHandler(
      priority = 90
   )
   private void afD(PlayerTickMovementEvent var1) {
      if (AliceObf_lx.aMP()) {
         AliceObf_nA.aNo();
      }
   }

   @AliceHandler(
      priority = -50
   )
   private void afE(Post var1) {
      if (AliceObf_lx.aMP()) {

      } else {
         short var2 = 19880;
      }

      AliceObf_nA.aNp();
   }

   @AliceHandler(
      priority = -1000
   )
   private void afF(Sent var1) {
      if (this.vb) {
         this.Xj++;
      }

      if (var1.packet instanceof UpdateSelectedSlotC2SPacket var2) {
         AliceObf_uv.aKE(var2.getSelectedSlot());
      }

      AliceObf_fq.aNi(var1.packet);
   }

   }

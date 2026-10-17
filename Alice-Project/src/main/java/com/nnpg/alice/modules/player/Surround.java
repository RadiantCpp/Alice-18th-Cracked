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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Sent;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.util.Hand;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.util.TypeFilter;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public class Surround extends AliceModule {
   private final SettingGroup ce = this.settings.getDefaultGroup();
   private final AliceObf_fN ayw;
   private final Setting<Boolean> wG;
   private final Setting<String> aaL;
   private final Setting<String> awU;
   private final Setting<Integer> aOY;
   private final Setting<Double> axS;
   private final Setting<Boolean> aAj;
   private final Setting<String> uC;
   private final Setting<Boolean> Rl;
   private final Setting<Boolean> YW;
   private final Setting<Boolean> Co;
   private final Setting<Boolean> zE;
   private final Setting<Double> QD;
   private final Setting<Integer> aon;
   private final Setting<Boolean> asj;
   private final Setting<Boolean> aOc;
   private final Setting<Boolean> OZ;
   private final Setting<Boolean> Rx;
   private final Setting<Boolean> iB;
   private final Setting<Boolean> azC;
   private final Setting<Boolean> FZ;
   private final Setting<Boolean> atm;
   private final Setting<Boolean> IN;
   private final Setting<Boolean> aJY;
   private final Setting<Boolean> aJA;
   private final Setting<Double> vH;
   private final Setting<Boolean> dd;
   private final Setting<Boolean> aJQ;
   private final Setting<Boolean> yW;
   private final Setting<Boolean> afI;
   private final Setting<Boolean> LB;
   private final Setting<Boolean> ayf;
   private final Setting<Boolean> aMR;
   private final Setting<Boolean> eY;
   private final Setting<Boolean> gN;
   private final Setting<Boolean> aPg;
   private final Setting<Boolean> EH;
   private final Setting<Boolean> axx;
   private final Setting<Boolean> aey;
   private final Setting<Boolean> LD;
   private int uK;
   private boolean nV;
   private Set<BlockPos> Yu;
   private BlockPos aqH;
   private int pS;
   private boolean aCk;
   private final Map<Integer, AliceObf_vv> tJ;
   private static final Direction[] uQ = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
   private long auF;
   public Surround() {
      super(AliceAddon.PVP_CATEGORY, "Surround", "Automatically places blocks at your feet to prevent crystal damage.");
      this.ayw = new AliceObf_fN("Surround");
      this.wG = this.ce.add(((Builder)((Builder)((Builder)((Builder)new Builder().name("Diagnostics")).description("Records the next activation for up to 15 seconds in logs/alice-placement. Keeps three rotating files; includes coordinates. Does not change placement.")).defaultValue(false)).onChanged(var1 -> {
         if (!var1) {
            this.ayw.aMt("setting-off");
         }
      })).build());
      this.aaL = this.ce
         .add(
            ((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)new meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder()
                        .name("Switch"))
                     .description("The mode that will be used for automatically switching to necessary items."))
                  .defaultValue("Silent"))
               .supplier(() -> AliceObf_uv.w)
               .build()
         );
      this.awU = this.ce
         .add(
            ((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)new meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder()
                        .name("Timing"))
                     .description("The timing that will be used in replacing broken surround blocks."))
                  .defaultValue("Sequential"))
               .supplier(
                  () -> new String[]{
                     "Vanilla",
                     "Sequential"
                  }
               )
               .build()
         );
      this.aOY = this.ce
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                        .name("BPT"))
                     .description("The maximum number of blocks to place per tick."))
                  .defaultValue(4))
               .min(1)
               .max(8)
               .build()
         );
      this.axS = this.ce
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                     .name("Range"))
                  .description("The maximum range at which the blocks will be placed at."))
               .defaultValue(5.0)
               .min(0.0)
               .max(12.0)
               .build()
         );
      this.aAj = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("Sync")).description("Only places against blocks the client has already registered. Does not use blocks sent earlier in the same burst as supports.")).defaultValue(false)).build());
      this.uC = this.ce
         .add(
            ((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)((meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder)new meteordevelopment.meteorclient.settings.ProvidedStringSetting.Builder()
                        .name("Rotate"))
                     .description("The rotation mode when placing surround blocks."))
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
      this.Rl = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("StrictDirection")).description("Only places using directions that face you.")).defaultValue(false)).build());
      this.YW = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("AirPlace")).description("Places blocks in the air without needing neighboring blocks.")).defaultValue(false)).build());
      this.Co = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("Attack")).description("Attacks any crystal that interferes with your surround.")).defaultValue(false)).build());
      this.zE = this.ce
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("AttackRotate")).description("Rotates toward the crystal before attacking it.")).defaultValue(true))
                  .visible(() -> (Boolean)this.Co.get()))
               .build()
         );
      this.QD = this.ce
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("AttackRange"))
                     .description("The maximum range at which crystals will be attacked."))
                  .defaultValue(3.0)
                  .min(1.0)
                  .max(6.0)
                  .visible(() -> (Boolean)this.Co.get()))
               .build()
         );
      this.aon = this.ce
         .add(
            ((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)((meteordevelopment.meteorclient.settings.IntSetting.Builder)new meteordevelopment.meteorclient.settings.IntSetting.Builder()
                           .name("AttackAge"))
                        .description("The minimum age (in ticks) a crystal must reach before being attacked."))
                     .defaultValue(5))
                  .min(0)
                  .max(20)
                  .visible(() -> (Boolean)this.Co.get()))
               .build()
         );
      this.asj = this.ce
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("AttackMultiTask")).description("Allows attacking while an item is already in use.")).defaultValue(true)).visible(() -> (Boolean)this.Co.get()))
               .build()
         );
      this.aOc = this.ce
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("AttackSwing")).description("Sends a swing packet whenever attacking a crystal.")).defaultValue(true))
                  .visible(() -> (Boolean)this.Co.get()))
               .build()
         );
      this.OZ = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("Center")).description("Puts you in the center of the block when you surround.")).defaultValue(false)).build());
      this.Rx = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("Floor")).description("Places blocks under your feet as well.")).defaultValue(true)).build());
      this.iB = this.ce
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("UnderFeet")).description("Replaces the block directly under your feet when it breaks.")).defaultValue(true))
                  .visible(() -> (Boolean)this.Rx.get()))
               .build()
         );
      this.azC = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("Extension")).description("Extends the surround if there are entities obstructing block placement.")).defaultValue(true)).build());
      this.FZ = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("Blocker")).description("Pre-places extra blocks where an enemy is digging toward you.")).defaultValue(true)).build());
      this.atm = this.ce
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("Up")).description("Blocker: places above/below the edge being mined through.")).defaultValue(true)).visible(() -> (Boolean)this.FZ.get()))
               .build()
         );
      this.IN = this.ce
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("Corner")).description("Blocker: places the 2 corner cells left/right of the edge being mined through.")).defaultValue(true)).visible(() -> (Boolean)this.FZ.get()))
               .build()
         );
      this.aJY = this.ce
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("Edge")).description("Blocker: places the cell straight out in front of the edge being mined through.")).defaultValue(true)).visible(() -> (Boolean)this.FZ.get()))
               .build()
         );
      this.aJA = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("AdvoidHelp")).description("Skips placements that would also wall in a nearby enemy.")).defaultValue(true)).build());
      this.vH = this.ce
         .add(
            ((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)((meteordevelopment.meteorclient.settings.DoubleSetting.Builder)new meteordevelopment.meteorclient.settings.DoubleSetting.Builder()
                        .name("AdvoidHelpHealth"))
                     .description("Below this health, place every block regardless -- ignores AdvoidHelp entirely."))
                  .defaultValue(10.0)
                  .min(0.0)
                  .max(20.0)
                  .visible(() -> (Boolean)this.aJA.get()))
               .build()
         );
      this.dd = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("WhileEating")).description("Places blocks normally while eating.")).defaultValue(true)).build());
      this.aJQ = this.ce
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("Advanced")).description("While WhileEating is off, keeps placing through an eat pause as long as your surround still has an open gap (stops once sealed, or if you're stuck inside a block).")).defaultValue(false))
                  .visible(() -> !(Boolean)this.dd.get()))
               .build()
         );
      this.yW = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("CenterOnTP")).description("Centers you if you have just teleported to surround against crystals easier.")).defaultValue(true)).build());
      this.afI = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("Predict")).description("Replaces a surround block instantly the moment we see the server's own break packet for it, instead of waiting for the next normal placement cycle.")).defaultValue(true)).build());
      this.LB = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("SelfDisable")).description("Toggles off the module once it is finished with placing.")).defaultValue(false)).build());
      this.ayf = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("ItemDisable")).description("Toggles off the module when no valid items are found in your hotbar/inventory.")).defaultValue(true)).build());
      this.aMR = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("JumpDisable")).description("Toggles off the module whenever your Y level changes.")).defaultValue(true)).build());
      this.eY = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("PearlDisable")).description("Toggles off surround whenever you throw or land an ender pearl.")).defaultValue(false)).build());
      this.gN = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("ChorusDisable")).description("Toggles off surround whenever you eat a chorus fruit.")).defaultValue(false)).build());
      this.aPg = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("RubberbandDisable")).description("Toggles off surround whenever you get rubberbanded/setback by the server.")).defaultValue(false)).build());
      this.EH = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("StepToggle")).description("Toggles the step module when you surround.")).defaultValue(false)).build());
      this.axx = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("SpeedToggle")).description("Toggles the speed module when you surround.")).defaultValue(false)).build());
      this.aey = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("TickShiftToggle")).description("Toggles the TickShift module when you surround.")).defaultValue(false)).build());
      this.LD = this.ce.add(((Builder)((Builder)((Builder)new Builder().name("Render")).description("Whether or not to render the place position.")).defaultValue(true)).build());
      this.uK = 0;
      this.nV = false;
      this.Yu = new HashSet<>();
      this.aqH = null;
      this.pS = 0;
      this.aCk = false;
      this.tJ = new HashMap<>();
      this.auF = 0L;
   }

   public String getInfoString() {
      return !PremiumAccessGate.isAllowed()
         ? null
         : this.uK
            + (
               this.wG.get()
                  ? ("" + this.ayw.aMs())
                  : ""
            );
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.ayw
            .aMr(
               (Boolean)this.wG.get(),
               (String)this.aaL.get()
                  + this.aOY.get()
                  + (String)this.uC.get()
                  + this.aAj.get()
                  + this.YW.get()
                  + this.Rl.get()
                  + (String)this.awU.get()
                  + this.afI.get()
                  + this.axS.get()
                  + this.Co.get()
                  + this.aMR.get()
                  + this.OZ.get()
                  + this.dd.get()
                  + this.LB.get()
                  + this.ayf.get()
                  + this.aPg.get()
            );
         if (!(super.mc.player != null)) {
            short var2 = 5401;
         }
      }
   }

   public void onDeactivate() {
      this.ayw.aMu();
      AliceObf_nA.aNq();
      this.Yu.clear();
      this.aqH = null;
      this.nV = false;
      this.aCk = false;
      this.tJ.clear();
      this.pS = 0;
      super.onDeactivate();
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @AliceHandler
   private void ala(Pre var1) {
      try {
         AliceObf_kE var2 = this.ayw
            .aMv("tick-pre");

         label1168: {
            label1169: {
               label1170: {
                  label1171: {
                     label1172: {
                        label1215: {
                           label1173: {
                              label1174: {
                                 try {
                                    AliceObf_uv.aKK();
                                    if (super.mc.player != null && super.mc.world != null) {
                                       if (this.aCk) {
                                          BlockPos var3 = AliceObf_iT.getFlooredPosition(
                                             super.mc.player
                                          );
                                          super.mc
                                             .player
                                             .setPosition(var3.getX() + 0.5, super.mc.player.getY(), var3.getZ() + 0.5);
                                          this.aqH = var3;
                                          this.aCk = false;
                                       }

                                       if ((Boolean)this.aMR.get()) {
                                          Module var35 = Modules.get()
                                             .get("Step");
                                          Module var4 = Modules.get()
                                             .get("Speed");
                                          if (!(var35 != null)) {
                                             short var49 = 26301;
                                          }

                                          boolean var5 = false;
                                          boolean var6 = var4 != null && var4.isActive();
                                          if (super.mc.player.fallDistance > 2.0
                                             || (var5 || var6)
                                                && (
                                                   this.aqH == null
                                                      || this.aqH.getY()
                                                         != AliceObf_iT.getFlooredPosition(
                                                               super.mc.player
                                                            )
                                                            .getY()
                                                )) {
                                             AliceObf_fN.aMx(
                                                "SKIP",
                                                "jump-disable"
                                             );
                                             this.disable();
                                             break label1215;
                                          }
                                       }

                                       if (!(Boolean)this.dd.get()
                                          && AliceObf_ck.aNL()
                                          && this.alp()) {
                                          AliceObf_fN.aMx(
                                             "SKIP",
                                             "while-eating"
                                          );
                                          break label1172;
                                       }

                                       label1210: {
                                          this.pS = 0;
                                          ItemStack var36 = super.mc.player.getMainHandStack();
                                          if (((String)this.aaL.get())
                                             .equalsIgnoreCase(
                                                "None"
                                             )) {
                                             if (!(var36.getItem() instanceof BlockItem var37)) {
                                                break label1210;
                                             }

                                             int var50 = !this.alq(var37.getBlock()) ? 27451 : 27452;

                                             while (true) {
                                                int var21 = var50;
                                                if (var21 == 27451) {
                                                   break label1210;
                                                }

                                                if (var21 != 27452) {
                                                   break;
                                                }

                                                var50 = 27450;
                                             }
                                          }

                                          int var38 = this.alr(
                                             0,
                                             !((String)this.aaL.get())
                                                      .equalsIgnoreCase(
                                                         "AltSwap"
                                                      )
                                                   && !((String)this.aaL.get())
                                                      .equalsIgnoreCase(
                                                         "AltPickup"
                                                      )
                                                ? 8
                                                : 35
                                          );
                                          int var40 = super.mc.player.getInventory().getSelectedSlot();
                                          if (var38 == -1) {
                                             AliceObf_fN.aMA(
                                                "lookup-returned-minus-one",
                                                (String)this.aaL.get(),
                                                var38
                                             );
                                             if ((Boolean)this.ayf.get()) {
                                                this.info(
                                                   "het block kia thang ngu",
                                                   new Object[0]
                                                );
                                                this.disable();
                                             }

                                             this.Yu.clear();
                                             break label1170;
                                          }

                                          this.Yu = AliceObf_jO.getFeetPositions(
                                             super.mc.player, (Boolean)this.azC.get(), (Boolean)this.Rx.get(), (Boolean)this.iB.get(), false
                                          );
                                          if ((Boolean)this.FZ.get()) {
                                             this.alh();
                                          }

                                          if (!(Boolean)this.Co.get()) {
                                             char var51 = 41164;
                                          } else {
                                             char var52 = 41163;
                                          }

                                          Set var41 = this.aJA.get() ? this.ali() : Set.of();
                                          double var7 = (Double)this.axS.get() * (Double)this.axS.get();
                                                                                    this.Yu
                                             .stream()
                                             .filter(var3x -> super.mc.player.squaredDistanceTo(Vec3d.ofCenter(var3x)) <= var7)
                                             .filter(
                                                var0 -> !AliceObf_ck.aNA(
                                                      var0
                                                   )
                                                   && AliceObf_ck.aNC(
                                                      var0
                                                   )
                                             )
                                             .forEach(
                                                var0 -> AliceObf_ck.aND(
                                                   var0, Direction.UP
                                                )
                                             );
                                          ArrayList<BlockPos> var9 = new ArrayList<>(
                                             this.Yu
                                                .stream()
                                                .filter(
                                                   var3x -> AliceObf_fN.aMz(
                                                      var3x,
                                                      "range-filter",
                                                      super.mc.player.squaredDistanceTo(Vec3d.ofCenter(var3x)) <= var7
                                                   )
                                                )
                                                .filter(
                                                   var0 -> AliceObf_ck.aNB(
                                                      var0, false
                                                   )
                                                )
                                                .filter(
                                                   var1x -> AliceObf_fN.aMz(
                                                      var1x,
                                                      "avoid-help-filter",
                                                      !var41.contains(var1x)
                                                   )
                                                )
                                                .toList()
                                          );
                                          Vec3d var10 = super.mc.player.getEntityPos().add(super.mc.player.getVelocity().multiply(0.5));
                                          var9.sort(
                                             (var1x, var2x) -> {
                                                int var3x = Integer.compare(var1x.getY(), var2x.getY());
                                                if (var3x != 0) {
                                                   return var3x;
                                                } else {
                                                   boolean var4x = AliceObf_ck.getDirection(
                                                         var1x, false
                                                      )
                                                      != null;
                                                   boolean var5x = AliceObf_ck.getDirection(
                                                         var2x, false
                                                      )
                                                      != null;
                                                   if (var4x != var5x) {
                                                      return var4x ? -1 : 1;
                                                   } else {
                                                      return Double.compare(
                                                         Vec3d.ofCenter(var1x).squaredDistanceTo(var10), Vec3d.ofCenter(var2x).squaredDistanceTo(var10)
                                                      );
                                                   }
                                                }
                                             }
                                          );
                                          if ((Boolean)this.eY.get()
                                             && super.mc.player.isUsingItem()
                                             && super.mc.player.getActiveItem().isOf(Items.ENDER_PEARL)) {
                                             this.disable();
                                             break label1169;
                                          }

                                          if (!(Boolean)this.gN.get()) {

                                          } else {
                                             char var53 = 49562;
                                          }

                                          if (!var9.isEmpty()) {
                                             this.nV = true;
                                             boolean var11 = ((String)this.uC.get())
                                                .equalsIgnoreCase(
                                                   "Grim"
                                                );
                                             if (var11) {
                                                AliceObf_fq.aNc();
                                             }

                                             boolean var30 = false /* VF: Semaphore variable */;

                                             label1182: {
                                                try {
                                                   var30 = true;
                                                   if (!AliceObf_uv.aKQ(
                                                      (String)this.aaL.get(), var38, var40
                                                   )) {
                                                      var30 = false;
                                                      break label1182;
                                                   }

                                                   ArrayList var12 = new ArrayList();

                                                   label1084:
                                                   for (BlockPos var14 : var9) {
                                                      if (AliceObf_fN.aMw()
                                                         )
                                                       {
                                                         AliceObf_fN.aMy(
                                                            "TARGET",
                                                            var14,
                                                            "" + this.pS + this.aOY.get() + var12
                                                         );
                                                      }

                                                      if (this.pS >= (Integer)this.aOY.get()) {
                                                         break;
                                                      }

                                                      if ((Boolean)this.Co.get()) {

                                                      } else {
                                                         short var54 = 5624;
                                                      }

                                                      if (!(Boolean)this.aAj.get() || !var12.contains(var14)) {
                                                         List var15 = this.alb(var12);
                                                         List<Direction> var16 = AliceObf_ck.getDirections(
                                                            var14, var15, (Boolean)this.Rl.get()
                                                         );
                                                         if (!var16.isEmpty()) {
                                                            boolean var42 = false;

                                                            for (Direction var19 : var16) {
                                                               if (AliceObf_ck.aNy(
                                                                  var14, var19, Hand.MAIN_HAND, (String)this.uC.get(), true, (Boolean)this.LD.get()
                                                               )) {
                                                                  var12.add(var14);
                                                                  this.auF = System.currentTimeMillis();
                                                                  this.pS++;
                                                                  var42 = true;
                                                                  break;
                                                               }
                                                            }

                                                            if (!var42) {
                                                               if ((Boolean)this.YW.get() && !(Boolean)this.Rl.get()) {
                                                                  this.alc(var14);
                                                                  var12.add(var14);
                                                                  this.auF = System.currentTimeMillis();
                                                                  this.pS++;
                                                               } else {
                                                                  BlockPos var44 = AliceObf_ck.aNE(
                                                                     var14, var15, (Boolean)this.Rl.get(), 2
                                                                  );
                                                                  if (var44 != null) {
                                                                     int var57 = !this.aAj.get() ? 17620 : 17619;

                                                                     while (true) {
                                                                        int var23 = var57;
                                                                        if (var23 == 17619) {
                                                                           if (var12.contains(var44)) {
                                                                              continue label1084;
                                                                           }
                                                                           break;
                                                                        }

                                                                        if (var23 != 17620) {
                                                                           break;
                                                                        }

                                                                        var57 = 17618;
                                                                     }
                                                                  }

                                                                                                                                    Direction var45 = var44 == null ? null : AliceObf_ck.getDirection(
                                                                     var44, var15, (Boolean)this.Rl.get()
                                                                  );
                                                                  if (var44 != null && var45 != null) {
                                                                     if (AliceObf_ck.aNy(
                                                                        var44,
                                                                        var45,
                                                                        Hand.MAIN_HAND,
                                                                        (String)this.uC.get(),
                                                                        true,
                                                                        (Boolean)this.LD.get()
                                                                     )) {
                                                                        var12.add(var44);
                                                                        this.auF = System.currentTimeMillis();
                                                                        this.pS++;
                                                                     }
                                                                  }
                                                               }
                                                            }
                                                         } else if ((Boolean)this.YW.get() && !(Boolean)this.Rl.get()) {
                                                            this.alc(var14);
                                                            var12.add(var14);
                                                            this.auF = System.currentTimeMillis();
                                                            this.pS++;
                                                         } else {
                                                            BlockPos var17 = AliceObf_ck.aNE(
                                                               var14, var15, (Boolean)this.Rl.get(), 2
                                                            );
                                                            if (var17 != null) {
                                                               int var10003 = !this.aAj.get() ? 61453 : 61452;

                                                               while (true) {
                                                                  int var22 = var10003;
                                                                  if (var22 == 61452) {
                                                                     if (var12.contains(var17)) {
                                                                        continue label1084;
                                                                     }
                                                                     break;
                                                                  }

                                                                  if (var22 != 61453) {
                                                                     break;
                                                                  }

                                                               }
                                                            }

                                                            Direction var18 = var17 == null
                                                               ? null
                                                               : AliceObf_ck.getDirection(
                                                                  var17, var15, (Boolean)this.Rl.get()
                                                               );
                                                            if (var17 != null && var18 != null) {
                                                               if (!AliceObf_ck.aNy(
                                                                  var17, var18, Hand.MAIN_HAND, (String)this.uC.get(), true, (Boolean)this.LD.get()
                                                               )) {
                                                                  short var55 = 13760;
                                                               } else {
                                                                  short var56 = 13761;
                                                               }

                                                               var12.add(var17);
                                                               this.auF = System.currentTimeMillis();
                                                               this.pS++;
                                                            }
                                                         }
                                                      }
                                                   }

                                                   AliceObf_uv.aKS(
                                                      (String)this.aaL.get(), var38, var40
                                                   );
                                                   var30 = false;
                                                } finally {
                                                   if (var30) {
                                                      if (var11) {
                                                         AliceObf_fq.aNe();
                                                      }

                                                      this.nV = false;
                                                   }
                                                }

                                                if (var11) {
                                                   AliceObf_fq.aNe();
                                                }

                                                this.nV = false;
                                                break label1173;
                                             }

                                             if (var11) {
                                                AliceObf_fq.aNe();
                                             }

                                             this.nV = false;
                                             break label1168;
                                          }

                                          AliceObf_fN.aMx(
                                             "SKIP",
                                             "no-placeable-targets"
                                          );
                                          if ((Boolean)this.LB.get()) {
                                             this.disable();
                                          }
                                          break label1174;
                                       }

                                       AliceObf_fN.aMA(
                                          "held-item-rejected",
                                          (String)this.aaL.get(),
                                          -1
                                       );
                                       if ((Boolean)this.ayf.get()) {
                                          this.info(
                                             "het block kia thang ngu",
                                             new Object[0]
                                          );
                                          this.disable();
                                       }

                                       this.Yu.clear();
                                       break label1171;
                                    }
                                 } catch (Throwable var33) {
                                    if (var2 != null) {
                                       try {
                                          var2.close();
                                       } catch (Throwable var31) {
                                          var33.addSuppressed(var31);
                                       }
                                    }

                                    throw var33;
                                 }

                                 if (var2 != null) {
                                    var2.close();
                                 }

                                 return;
                              }

                              if (var2 != null) {
                                 var2.close();
                              }

                              return;
                           }

                           if (var2 != null) {
                              var2.close();
                           }

                           return;
                        }

                        if (var2 != null) {
                           var2.close();
                        }

                        return;
                     }

                     if (var2 != null) {
                        var2.close();
                     }

                     return;
                  }

                  if (var2 != null) {
                     var2.close();
                  }

                  return;
               }

               if (var2 != null) {
                  var2.close();
               }

               return;
            }

            if (var2 != null) {
               var2.close();
            }

            return;
         }

         if (var2 != null) {
            var2.close();
         }
      } catch (Exception var34) {
      }
   }

   private List<BlockPos> alb(List<BlockPos> var1) {
      return this.aAj.get() ? List.of() : var1;
   }

   private void alc(BlockPos var1) {
      AliceObf_ck.aNz(
         var1, Hand.MAIN_HAND, (String)this.uC.get(), (Boolean)this.LD.get()
      );
   }

   @AliceHandler
   private void ald(Receive var1) {
      try {
         AliceObf_kE var2 = this.ayw
            .aMv("packet-receive");

         label397: {
            label398: {
               try {
                  if (super.mc.player == null || super.mc.world == null) {
                     break label398;
                  }

                  Packet var3 = var1.packet;
                  if (!(var3 instanceof OpenScreenS2CPacket)) {
                     char var22 = 54616;
                  }

                  if (!(var3 instanceof PlayerPositionLookS2CPacket)) {
                     char var23 = 32872;
                  }

                  if ((Boolean)this.Co.get() && var3 instanceof EntitySpawnS2CPacket var4 && var4.getEntityType() == EntityType.END_CRYSTAL) {
                     Vec3d var5 = new Vec3d(var4.getX(), var4.getY(), var4.getZ());
                     Box var6 = new Box(
                        var5.x - 1.0, var5.y, var5.z - 1.0, var5.x + 1.0, var5.y + 2.0, var5.z + 1.0
                     );
                     boolean var7 = false;
                     Iterator var8 = this.Yu.iterator();
                     if (var8.hasNext()) {
                        BlockPos var9 = (BlockPos)var8.next();
                        if (!var6.intersects(new Box(var9))) {

                        } else {
                           short var24 = 29779;
                        }

                        var7 = true;
                     }

                     if (var7
                        && super.mc.player.getEyePos().squaredDistanceTo(var5) <= (Double)this.QD.get() * (Double)this.QD.get()
                        && ((Boolean)this.asj.get() || !super.mc.player.isUsingItem())) {
                        EndCrystalEntity var21 = new EndCrystalEntity(super.mc.world, var5.x, var5.y, var5.z);
                        var21.setId(var4.getEntityId());
                        AliceObf_iE.aLm(var21);
                        AliceObf_iE.aLn((Boolean)this.aOc.get());
                     }
                  }

                  if (!((String)this.awU.get())
                     .equalsIgnoreCase("Sequential")) {
                     break label397;
                  }

                  if ((Boolean)this.afI.get() && var3 instanceof BlockUpdateS2CPacket var15 && var15.getState().isAir()) {
                     if (this.Yu.contains(var15.getPos())) {
                        short var25 = 6247;
                     } else {
                        short var26 = 6248;
                     }
                  }

                  if ((Boolean)this.FZ.get() && var3 instanceof BlockBreakingProgressS2CPacket var16 && var16.getEntityId() != super.mc.player.getId()) {
                     BlockPos var17 = var16.getPos();
                     boolean var18 = false;
                     Iterator var19 = this.alj().iterator();
                     if (var19.hasNext()) {
                        char var27 = 42882;
                     } else {
                        char var28 = 42883;
                     }

                     if (var18) {
                        AliceObf_vv var20 = (AliceObf_vv)this.tJ.get(var16.getEntityId());
                        if (var20 != null && var20.Yr.equals(var17)) {
                           var20.aEL = System.currentTimeMillis();
                        } else {
                           this.tJ.put(var16.getEntityId(), new AliceObf_vv(var17));
                        }
                     }
                  }
               } catch (Throwable var13) {
                  if (var2 != null) {
                     try {
                        var2.close();
                     } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                     }
                  }

                  throw var13;
               }

               int var29 = var2 != null ? 5506 : 5507;

               while (true) {
                  int var11 = var29;
                  if (var11 == 5506) {
                     var2.close();
                     return;
                  }

                  if (var11 != 5507) {
                     return;
                  }

                  var29 = 5505;
               }
            }

            if (var2 != null) {
               var2.close();
            }

            return;
         }

         if (var2 != null) {
            short var30 = 22357;
         } else {
            short var31 = 22358;
         }
      } catch (Exception var14) {
      }
   }

   private void ale(BlockUpdateS2CPacket var1) {
      if (this.pS < (Integer)this.aOY.get()) {
         if ((Boolean)this.dd.get()
            || !AliceObf_ck.aNL()
            || !this.alp()) {
            boolean var2 = false;
            if (AliceObf_ck.aNB(var1.getPos(), var2)) {
               int var3 = this.alr(
                  0,
                  !((String)this.aaL.get())
                           .equalsIgnoreCase("AltSwap")
                        && !((String)this.aaL.get())
                           .equalsIgnoreCase("AltPickup")
                     ? 8
                     : 35
               );
               int var4 = super.mc.player.getInventory().getSelectedSlot();
               if (var3 != -1) {
                  Direction var5 = AliceObf_ck.getDirection(
                     var1.getPos(), (Boolean)this.Rl.get()
                  );
                  if (var5 == null) {
                     if ((Boolean)this.YW.get()) {

                     } else {
                        char var10 = 55440;
                     }
                  } else {
                     AliceObf_fq.aNc();

                     try {
                        if (AliceObf_uv.aKQ(
                           (String)this.aaL.get(), var3, var4
                        )) {
                           AliceObf_ck.aNy(
                              var1.getPos(), var5, Hand.MAIN_HAND, (String)this.uC.get(), true, (Boolean)this.LD.get()
                           );
                           this.pS++;
                           this.auF = System.currentTimeMillis();
                           AliceObf_uv.aKS(
                              (String)this.aaL.get(), var3, var4
                           );
                           return;
                        }
                     } finally {
                        AliceObf_fq.aNe();
                     }
                  }
               }
            }
         }
      }
   }

   private void alf(BlockPos var1) {
      if (super.mc.player != null && super.mc.world != null) {
         if (this.pS < (Integer)this.aOY.get()) {

         } else {
            char var16 = 53715;
         }

         int var10001 = !this.dd.get() ? 59211 : 59212;

         while (true) {
            int var8 = var10001;
            if (var8 == 59211) {
               if (AliceObf_ck.aNL()) {
                  if (this.alp()) {
                     char var17 = 58230;
                  } else {
                     char var18 = 58231;
                  }
               }
               break;
            }

            if (var8 != 59212) {
               break;
            }

         }

         boolean var2 = false;
         if (AliceObf_ck.aNB(var1, var2)) {
            int var3 = this.alr(
                  ((String)this.aaL.get())
                        .equalsIgnoreCase("AltSwap")
                     ? 17336
                     : 17335,
                  35
               );
            int var4 = super.mc.player.getInventory().getSelectedSlot();
            if (var3 != -1) {
               Direction var5 = AliceObf_ck.getDirection(
                  var1, (Boolean)this.Rl.get()
               );
               if (var5 == null) {
                  int var10002 = this.YW.get() ? 62508 : 62509;

                  while (true) {
                     int var9 = var10002;
                     if (var9 == 62508) {
                        if ((Boolean)this.Rl.get()) {
                           break;
                        }

                        AliceObf_fq.aNc();

                        try {
                           if (AliceObf_uv.aKQ(
                              (String)this.aaL.get(), var3, var4
                           )) {
                              this.alc(var1);
                              this.pS++;
                              this.auF = System.currentTimeMillis();
                              AliceObf_uv.aKS(
                                 (String)this.aaL.get(), var3, var4
                              );
                              break;
                           }
                        } finally {
                           AliceObf_fq.aNe();
                        }

                        return;
                     }

                     if (var9 != 62509) {
                        break;
                     }

                  }
               } else {
                  AliceObf_fq.aNc();

                  try {
                     if (AliceObf_uv.aKQ(
                        (String)this.aaL.get(), var3, var4
                     )) {
                        AliceObf_ck.aNy(
                           var1, var5, Hand.MAIN_HAND, (String)this.uC.get(), true, (Boolean)this.LD.get()
                        );
                        this.pS++;
                        this.auF = System.currentTimeMillis();
                        AliceObf_uv.aKS(
                           (String)this.aaL.get(), var3, var4
                        );
                        return;
                     }
                  } finally {
                     AliceObf_fq.aNe();
                  }
               }
            }
         }
      }
   }

   private void alg() {
      AliceObf_fq.aNc();

      for (EndCrystalEntity var2 : super.mc
         .world
         .getEntitiesByType(
            TypeFilter.instanceOf(EndCrystalEntity.class),
            new Box(super.mc.player.getBlockPos()).expand((Double)this.QD.get() + 3.0),
            var0 -> true
         )) {
         if (var2.isAlive() && !var2.isRemoved()) {
            if (var2.age < (Integer)this.aon.get()) {

            } else {
               char var8 = 52829;
            }

            Box var3 = var2.getBoundingBox();
            boolean var4 = false;

            for (BlockPos var6 : this.Yu) {
               if (super.mc.world.getBlockState(var6).isReplaceable() && new Box(var6).intersects(var3)) {
                  var4 = true;
                  break;
               }
            }

            if (var4) {
               if (!(
                  AliceObf_ck.aNN(
                        var3, super.mc.player.getEyePos()
                     )
                     > (Double)this.QD.get() * (Double)this.QD.get()
               )) {

               } else {
                  char var9 = 57087;
               }
            }
         }
      }

      AliceObf_fq.aNe();
   }

   private void alh() {
      this.tJ
         .entrySet()
         .removeIf(
            var1 -> {
               AliceObf_vv var2x = (AliceObf_vv)var1.getValue();
               return super.mc.world.getEntityById((Integer)var1.getKey()) == null
                  || super.mc.world.getBlockState(var2x.Yr).isReplaceable()
                  || System.currentTimeMillis() - var2x.aEL > 1000L;
            }
         );

      for (AliceObf_vv var2 : this.tJ.values()) {
         var2.alv++;
         if (var2.alv < 2) {
            if (System.currentTimeMillis() - var2.aPB < 100L) {

            } else {
               char var8 = 36699;
            }
         }

         BlockPos var3 = var2.Yr;

         for (BlockPos var5 : this.alj()) {
            if (var3.getY() == var5.getY()) {
               int var6 = var3.getX() - var5.getX();
               int var7 = var3.getZ() - var5.getZ();
               if (Math.abs(var6) + Math.abs(var7) == 1) {
                  if ((Boolean)this.atm.get()) {
                     this.Yu.add(var3.up());
                     this.Yu.add(var3.down());
                  }

                  if ((Boolean)this.IN.get()) {
                     if (var6 != 0) {
                        this.Yu.add(var5.add(var6, 0, 1));
                        this.Yu.add(var5.add(var6, 0, -1));
                     } else {
                        this.Yu.add(var5.add(1, 0, var7));
                        this.Yu.add(var5.add(-1, 0, var7));
                     }
                  }

                  if ((Boolean)this.aJY.get()) {
                     if (var6 != 0) {
                        this.Yu.add(var5.add(var6 * 2, 0, 0));
                     } else {
                        this.Yu.add(var5.add(0, 0, var7 * 2));
                     }
                  }
               }
            }
         }
      }
   }

   private Set<BlockPos> ali() {
      Set<BlockPos> var1 = this.alj();
      if (super.mc.player.getHealth() < ((Double)this.vH.get()).floatValue()) {

      } else {
         char var20 = 37084;
      }

      HashSet<BlockPos> var2 = new HashSet<>();

      for (AbstractClientPlayerEntity var4 : super.mc.world.getPlayers()) {
         if (var4 != super.mc.player) {
            int var10001 = !Friends.get().isFriend(var4) ? 31356 : 31357;

            while (true) {
               int var13 = var10001;
               if (var13 == 31356) {
                  if (!(super.mc.player.squaredDistanceTo(var4) > 25.0) && super.mc.world.getBlockState(var4.getBlockPos()).isReplaceable()) {
                     Set<BlockPos> var5 = this.alk(var4);

                     for (BlockPos var7 : var5) {
                        for (Direction var11 : Surround.uQ) {
                           BlockPos var12 = var7.offset(var11);
                           if (!var5.contains(var12)) {
                              var2.add(var12);
                           }
                        }

                        var2.add(var7.down());
                        var2.add(var7);
                     }
                  }
                  break;
               }

               if (var13 != 31357) {
                  break;
               }

            }
         }
      }

      if (var2.isEmpty()) {
         return Set.of();
      } else {
         HashSet<BlockPos> var14 = new HashSet<>();

         for (BlockPos var16 : var2) {
            boolean var17 = false;
            Iterator var18 = var1.iterator();
            if (var18.hasNext()) {
               BlockPos var19 = (BlockPos)var18.next();
               if (!var16.equals(var19) && !var16.equals(var19.up()) && !var16.equals(var19.down())) {
                  if (!var16.equals(var19.offset(Direction.NORTH))) {
                     char var21 = 35814;
                  } else {
                     char var22 = 35815;
                  }
               }

               var17 = true;
            }

            if (!var17) {
               var14.add(var16);
            }
         }

         return var14;
      }
   }

   private Set<BlockPos> alj() {
      return this.alk(super.mc.player);
   }

   private Set<BlockPos> alk(PlayerEntity var1) {
      HashSet<BlockPos> var2 = new HashSet<>();
      Box var3 = var1.getBoundingBox();
      int var4 = var1.getBlockPos().getY();

      for (int var5 = (int)Math.floor(var3.minX); var5 < Math.ceil(var3.maxX); var5++) {
         for (int var6 = (int)Math.floor(var3.minZ); var6 < Math.ceil(var3.maxZ); var6++) {
            var2.add(new BlockPos(var5, var4, var6));
         }
      }

      return var2;
   }

   @AliceHandler(
      priority = 90
   )
   private void all(PlayerTickMovementEvent var1) {
      if (AliceObf_lx.aMP()) {
         AliceObf_nA.aNo();
      }
   }

   @AliceHandler(
      priority = -50
   )
   private void alm(Post var1) {
      if (AliceObf_lx.aMP()) {
         AliceObf_nA.aNp();
      }
   }

   @AliceHandler(
      priority = -1000
   )
   private void aln(Sent var1) {
      if (this.nV) {
         this.uK++;
      }

      if (var1.packet instanceof UpdateSelectedSlotC2SPacket var2) {
         AliceObf_uv.aKE(var2.getSelectedSlot());
      }

      AliceObf_fq.aNi(var1.packet);
   }

   @AliceHandler
   private void alo(GameLeftEvent var1) {
      this.disable();
   }

   private boolean alp() {
      if (!(Boolean)this.aJQ.get()) {
         return true;
      } else if (!AliceObf_ck.aNK(super.mc.player)) {
         Set<BlockPos> var1 = AliceObf_jO.getFeetPositions(
            super.mc.player, (Boolean)this.azC.get(), (Boolean)this.Rx.get(), (Boolean)this.iB.get(), false
         );
         boolean var2 = var1.stream().filter(AliceObf_ck::aNA).anyMatch(var1x -> {
            boolean var10001 = (Boolean)this.Rl.get();
            if (AliceObf_ck.getDirection(var1x, var10001) == null) {
               if (!(Boolean)this.YW.get()) {

               } else {
                  short var2x = 26330;
               }

               var10001 = false;
            } else {
               boolean var3 = true;
            }

            return var10001;
         });
         return !var2;
      } else {
         return true;
      }
   }

   private boolean alq(Block var1) {
      return var1 == Blocks.OBSIDIAN
         || var1 == Blocks.ENDER_CHEST
         || var1 == Blocks.CRYING_OBSIDIAN
         || var1 == Blocks.NETHERITE_BLOCK
         || var1 == Blocks.RESPAWN_ANCHOR
         || var1 == Blocks.ANCIENT_DEBRIS
         || var1 == Blocks.ENCHANTING_TABLE
         || var1 == Blocks.ANVIL
         || var1 == Blocks.CHIPPED_ANVIL
         || var1 == Blocks.DAMAGED_ANVIL;
   }

   private int alr(int var1, int var2) {
      float var3 = -1.0F;
      int var4 = -1;
      int var5 = var1;

      while (true) {
         int var10000 = var5 <= var2 ? 29166 : 29167;

         while (true) {
            int var9 = var10000;
            if (var9 == 29166) {
               ItemStack var6 = super.mc.player.getInventory().getStack(var5);
               if (AliceObf_fN.aMw()) {
                  String var12 = "SLOT_SCAN";
                  Item var8 = var6.getItem();
                  AliceObf_fN.aMx(
                     String.valueOf(var5), "" + var6.getCount() + (var8 instanceof BlockItem ? 7318 : 7319) + false
                  );
               }

               if (var6.getItem() instanceof BlockItem var7 && this.alq(var7.getBlock())) {
                  float var11 = var7.getBlock().getHardness();
                  if (var11 == -1.0F) {
                     return var5;
                  }

                  if (!(var11 > var3)) {
                     short var13 = 32496;
                  } else {
                     short var14 = 32497;
                  }

                  var3 = var11;
                  var4 = var5;
               }

               var5++;
               break;
            }

            if (var9 != 29167) {
               AliceObf_fN.aMA(
                  "lookup-result",
                  (String)this.aaL.get(),
                  var4
               );
               return var4;
            }

         }
      }
   }

   }

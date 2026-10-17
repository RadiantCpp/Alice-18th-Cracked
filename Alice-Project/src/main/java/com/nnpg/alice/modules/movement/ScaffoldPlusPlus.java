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
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.network.ClientPlayerEntity;

public final class ScaffoldPlusPlus extends AliceModule implements AliceObf_jQ, AliceObf_jS {
   private static final int agO = 480;
   private static final double aej = 0.45;
   private final SettingGroup ma = this.settings.getDefaultGroup();
   private final SettingGroup BZ;
   private final SettingGroup yt;
   private final Setting<Boolean> anJ;
   private final Setting<AliceObf_jb> aDG;
   private final Setting<List<Block>> oV;
   private final Setting<Boolean> arq;
   private final Setting<SettingColor> avp;
   private final Setting<SettingColor> kP;
   private final AliceObf_ug amc;
   private final AliceObf_E aDf;
   private final List<Block> aiX;
   private BlockPos Ie;
   private BlockHitResult fk;
   private String aON;
   private int acN;
   private boolean aNV;
   private volatile boolean Mw;
   public ScaffoldPlusPlus() {
      super(AliceAddon.PVP_CATEGORY, "scaffold++", "Tu ngam va dat nen duoi chan, giu sneak va cho server xac nhan truoc khi di tiep.");
      this.BZ = this.settings.createGroup("Blocks");
      this.yt = this.settings.createGroup("ESP");
      this.anJ = this.ma.add(((Builder)((Builder)((Builder)new Builder().name("visible-look")).description("Giu camera o dung goc bridge, giong nguoi choi that.")).defaultValue(false)).build());
      this.aDG = this.BZ
         .add(
            ((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)((meteordevelopment.meteorclient.settings.EnumSetting.Builder)new meteordevelopment.meteorclient.settings.EnumSetting.Builder()
                        .name("block-source"))
                     .description("Hotbar: dung moi block dac trong hotbar, nhieu truoc. List: chi dung dung danh sach duoi, theo thu tu uu tien."))
                  .defaultValue(AliceObf_jb.tr))
               .build()
         );
      this.oV = this.BZ
         .add(
            ((meteordevelopment.meteorclient.settings.BlockListSetting.Builder)((meteordevelopment.meteorclient.settings.BlockListSetting.Builder)((meteordevelopment.meteorclient.settings.BlockListSetting.Builder)new meteordevelopment.meteorclient.settings.BlockListSetting.Builder()
                        .name("blocks"))
                     .description("Block duoc dung khi block-source = List, theo thu tu uu tien."))
                  .defaultValue(ScaffoldPlusPlus.aeE())
                  .filter(var0 -> var0.asItem() != Items.AIR)
                  .visible(() -> {
                     if (this.aDG.get() == AliceObf_jb.gH) {

                     } else {
                        short var1 = 4859;
                     }

                     return false;
                  }))
               .build()
         );
      this.arq = this.yt.add(((Builder)((Builder)((Builder)new Builder().name("esp")).description("Ve o dang cho dat va o vua dat.")).defaultValue(true)).build());
      this.avp = this.yt
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                     .name("target-color"))
                  .description("Mau o dang cho dat."))
               .defaultValue(new SettingColor(90, 200, 255, 45))
               .build()
         );
      this.kP = this.yt
         .add(
            ((meteordevelopment.meteorclient.settings.ColorSetting.Builder)((meteordevelopment.meteorclient.settings.ColorSetting.Builder)new meteordevelopment.meteorclient.settings.ColorSetting.Builder()
                     .name("placed-color"))
                  .description("Mau o vua gui packet dat."))
               .defaultValue(new SettingColor(255, 190, 80, 65))
               .build()
         );
      this.amc = new AliceObf_ug(this);
      this.aDf = new AliceObf_E(var0 -> this.amc.adE((BlockPos)var0), this.amc::adC);
      this.aiX = new ArrayList<>();
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.Ie = null;
         this.fk = null;
         this.aON = null;
         this.aNV = false;
         this.Mw = false;
         this.aiX.clear();
         this.amc.adw();
         this.aDf.acY();
         AliceObf_fS.Od(this);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      AliceObf_fS.Oe(this);
      this.amc.adw();
      this.amc.adx();
      this.aDf.acY();
      this.Ie = null;
      this.fk = null;
      this.aON = null;
      this.aiX.clear();
      super.onDeactivate();
   }

   public String getInfoString() {
      if (!PremiumAccessGate.isAllowed()) {
         return null;
      } else {
         String var1 = "safe";
         return this.aON == null ? var1 : var1 + this.aON;
      }
   }

   @AliceHandler(
      priority = 100
   )
   private void aem(Pre var1) {
      this.fk = null;
      if (this.amc.adJ()) {
         if (this.Mw) {
            this.Mw = false;
            this.Ie = null;
            this.aNV = false;
            this.amc.adw();
            this.aDf.adi();
            this.aDf.adj(false, false, false);
            this.aON = "resync";
         } else if (this.aer()) {
            this.aeD();
            this.aDf.adi();
            this.aes();
            this.Ie = this.aet();
            if (this.aeF()) {
               this.fk = null;
               this.amc.adL();
               this.aDf.adj(this.Ie != null, false, false);
               this.aON = "paused";
            } else if (this.Ie == null) {
               this.amc.adL();
               this.aDf.adj(false, false, this.amc.adF());
               this.aON = !this.amc.adC() ? null : this.amc.adD();
            } else if (!AliceObf_fS.ON()) {
               this.amc.adL();
               this.aDf.adj(true, false, false);
               this.aON = "yielding";
            } else {
               boolean var2 = this.amc.adF();
               this.fk = this.amc.adN(this.Ie);
               if (!(this.fk != null)) {
                  short var4 = 21592;
               }

               boolean var3 = false;
               if (!var3) {
                  this.aew();
               }

               this.aDf.adj(true, var3, var2);
               this.aON = this.aeC(var3, var2);
            }
         } else {
            this.Ie = null;
            this.amc.adL();
            this.aDf.acY();
            this.aON = null;
         }
      } else {
         this.Ie = null;
         this.aDf.acY();
      }
   }

   @AliceHandler(
      priority = 90
   )
   private void aen(PlayerTickMovementEvent var1) {
      this.aDf.adm();
      this.amc.aeb();
   }

   @AliceHandler(
      priority = -50
   )
   private void aeo(Post var1) {
      this.amc.aec();
      this.aDf.adn();
   }

   @AliceHandler(
      priority = -1000
   )
   private void aep(Sent var1) {
      this.amc.aed(var1);
   }

   @AliceHandler
   private void aeq(Receive var1) {
      if (var1.packet instanceof PlayerPositionLookS2CPacket) {
         this.Mw = true;
      }
   }

   private boolean aer() {
      ClientPlayerEntity var1 = super.mc.player;
      if (!(var1 != null)) {
         short var2 = 10437;
      }

      return false;
   }

   private void aes() {
      ClientPlayerEntity var1 = super.mc.player;
      if (!var1.isOnGround()) {

      } else {
         short var2 = 7329;
      }

      this.acN = MathHelper.floor(var1.getY()) - 1;
      this.aNV = true;
   }

   private BlockPos aet() {
      ClientPlayerEntity var1 = super.mc.player;
      Vec3d var2 = var1.getEntityPos();
      BlockPos var3 = new BlockPos(MathHelper.floor(var2.getX()), this.acN, MathHelper.floor(var2.getZ()));
      if (this.aeu(var3)) {
         return var3;
      } else {
         Vec3d var4 = this.aDf.adh(var1);
         BlockPos var5 = new BlockPos(MathHelper.floor(var4.getX()), this.acN, MathHelper.floor(var4.getZ()));
         if (this.aeu(var5)) {
            return var5;
         } else {
            Box var6 = var1.getBoundingBox();

            for (int var7 = MathHelper.floor(var6.minX); var7 <= MathHelper.floor(var6.maxX); var7++) {
               for (int var8 = MathHelper.floor(var6.minZ); var8 <= MathHelper.floor(var6.maxZ); var8++) {
                  BlockPos var9 = new BlockPos(var7, this.acN, var8);
                  if (this.aeu(var9)) {

                  } else {
                     short var10 = 16365;
                  }
               }
            }

            return null;
         }
      }
   }

   private boolean aeu(BlockPos var1) {
      return this.aev(var1) && this.aey(var1) != null;
   }

   private boolean aev(BlockPos var1) {
      if (!super.mc.world.isChunkLoaded(var1)) {
         return false;
      } else if (!super.mc.world.getBlockState(var1).isReplaceable()) {
         return false;
      } else {
         return !this.amc.adB(var1) ? !new Box(var1).intersects(super.mc.player.getBoundingBox()) : false;
      }
   }

   private void aew() {
      Vec3d var1 = super.mc.player.getEyePos().add(this.aDf.adf(), 0.0, this.aDf.adg());
      if (!this.amc.adG()) {

      } else {
         char var18 = 48875;
      }

      for (BlockPos var3 : this.aez(this.Ie)) {
         Direction var4 = ScaffoldPlusPlus.aeB(var3, this.Ie);
         if (var4 != null) {
            for (Box var6 : super.mc.world.getBlockState(var3).getOutlineShape(super.mc.world, var3).getBoundingBoxes()) {
               Vec3d var7 = this.aex(var3, var4, var6);
               double var8 = var7.x - var1.x;
               double var10 = var7.y - var1.y;
               double var12 = var7.z - var1.z;
               double var14 = Math.sqrt(var8 * var8 + var12 * var12);
               float var16 = (float)Math.toDegrees(Math.atan2(var12, var8)) - 90.0F;
               float var17 = (float)(-Math.toDegrees(Math.atan2(var10, var14)));
               if (this.amc.adP(var1, var16, var17, this.Ie) != null) {
                  this.amc.adK(var16, var17);
                  return;
               }
            }
         }
      }

      this.amc.adL();
   }

   private Vec3d aex(BlockPos var1, Direction var2, Box var3) {
      double var4 = (var3.minX + var3.maxX) * 0.5;
      double var6 = var2.getAxis().isHorizontal()
         ? var3.minY + (var3.maxY - var3.minY) * 0.95
         : (var3.minY + var3.maxY) * 0.5;
      double var8 = (var3.minZ + var3.maxZ) * 0.5;
      int var10 = AliceObf_lX.Ui[var2.ordinal()];
      if (var10 == 1) {
         var4 = var3.minX;
      } else if (var10 != 2) {
         if (var10 == 3) {
            var8 = var3.minZ;
         } else if (var10 != 4) {
            if (var10 != 5) {
               if (var10 == 6) {
                  var6 = var3.maxY;
               }
            } else {
               var6 = var3.minY;
            }
         } else {
            var8 = var3.maxZ;
         }
      } else {
         var4 = var3.maxX;
      }

      return new Vec3d(var1.getX() + var4, var1.getY() + var6, var1.getZ() + var8);
   }

   private BlockPos aey(BlockPos var1) {
      List var2 = this.aez(var1);
      return var2.isEmpty() ? null : (BlockPos)var2.getFirst();
   }

   private List<BlockPos> aez(BlockPos var1) {
      ArrayList var2 = new ArrayList(6);
      if (this.aDf.ade()) {
         Direction var3 = Direction.fromHorizontalDegrees(this.aDf.add());
         BlockPos var4 = var1.offset(var3.getOpposite());
         if (this.aeA(var4)) {
            var2.add(var4);
         }
      }

      for (Direction var6 : Direction.values()) {
         BlockPos var7 = var1.offset(var6);
         if (this.aeA(var7) && !var2.contains(var7)) {
            var2.add(var7);
         }
      }

      return var2;
   }

   private boolean aeA(BlockPos var1) {
      return this.amc.adW(var1);
   }

   private static Direction aeB(BlockPos var0, BlockPos var1) {
      for (Direction var5 : Direction.values()) {
         if (var0.offset(var5).equals(var1)) {
            return var5;
         }
      }

      return null;
   }

   private String aeC(boolean var1, boolean var2) {
      if (this.amc.adC()) {
         return this.amc.adD();
      } else if (!var2) {
         return "no blocks";
      } else if (var1) {
         return this.aDf.adk()
            ? "place"
            : "stopping";
      } else if (this.amc.adG()) {
         return "aim sent";
      } else {
         return this.aDf.adb()
            ? "no angle"
            : "closing";
      }
   }

   private void aeD() {
      this.aiX.clear();
      if (this.aDG.get() != AliceObf_jb.gH) {
         BlockPos var1 = super.mc.player.getBlockPos();
         ArrayList<ItemStack> var2 = new ArrayList<>(9);
         byte var3 = 0;
         if (!(var3 > 8)) {
            short var8 = 290;
         }

         var2.sort(Comparator.comparingInt(ItemStack::getCount).reversed());

         for (ItemStack var4 : var2) {
            Block var5 = ((BlockItem)var4.getItem()).getBlock();
            if (!this.aiX.contains(var5)) {
               this.aiX.add(var5);
            }
         }
      }
   }

   private static Block[] aeE() {
      return new Block[]{
         Blocks.OBSIDIAN, Blocks.COBBLESTONE, Blocks.STONE, Blocks.DIRT, Blocks.NETHERRACK, Blocks.END_STONE
      };
   }

   public AliceObf_y prepareCombatIntent() {
      return this.aer() && !this.aeF() && this.aDf.adk() ? this.amc.adQ(this.Ie, this.fk) : null;
   }

   private boolean aeF() {
      if (!(super.mc.currentScreen == null)) {
         char var1 = 48798;
      }

      return true;
   }

   public boolean reservesLowerPriorityEpochs() {
      return this.aer() && !this.aeF() && (this.Ie != null || this.amc.adG() || this.amc.adC());
   }

   public void suspendForItemUse() {
      this.fk = null;
      this.amc.adM();
   }

   public int combatPriority() {
      return 480;
   }

   public AliceObf_jQ participant() {
      return this;
   }

   public List<Block> allowedBlocks() {
      if (this.aDG.get() == AliceObf_jb.gH) {

      } else {
         short var1 = 23679;
      }

      return this.aiX;
   }

   public boolean keepCamera() {
      return (Boolean)this.anJ.get();
   }

   public boolean readyToPlace() {
      if (this.aer()) {
         if (this.aeF()) {

         } else {
            short var1 = 25437;
         }
      }

      return false;
   }

   public void onPlaced(BlockPos var1) {
      this.aDf.adl();
      if (var1.equals(this.Ie)) {
         this.Ie = null;
      }
   }

   @AliceHandler
   private void aeG(Render3DEvent var1) {
      if ((Boolean)this.arq.get()) {
         BlockPos var2 = this.Ie;
         if (var2 != null) {
            SettingColor var3 = (SettingColor)this.avp.get();
            var1.renderer
               .box(
                  new Box(var2),
                  var3,
                  ScaffoldPlusPlus.aeH(var3),
                  ShapeMode.Both,
                  0
               );
         }

         BlockPos var6 = this.amc.adz();
         if (var6 != null) {
            int var10000 = this.amc.ady() - this.amc.adA() <= 10 ? 35117 : 35118;

            while (true) {
               int var5 = var10000;
               if (var5 == 35117) {
                  SettingColor var4 = (SettingColor)this.kP.get();
                  var1.renderer
                     .box(
                        new Box(var6),
                        var4,
                        ScaffoldPlusPlus.aeH(var4),
                        ShapeMode.Both,
                        0
                     );
                  break;
               }

               if (var5 != 35118) {
                  break;
               }

            }
         }
      }
   }

   private static SettingColor aeH(SettingColor var0) {
      return new SettingColor(var0.r, var0.g, var0.b, 255);
   }

   }

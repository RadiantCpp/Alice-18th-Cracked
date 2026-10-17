package com.nnpg.alice.modules.combat;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.mojang.datafixers.util.Pair;
import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.premium.AliceHandler;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import java.util.ArrayList;
import java.util.List;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent.Post;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.events.world.TickEvent.Pre;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.IntSetting.Builder;
import net.minecraft.util.PlayerInput;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec2f;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.SetPlayerInventoryS2CPacket;

public class AutoTotem extends AliceModule implements AliceObf_jQ {
   private static final int apL = 40;
   private static final int gp = 45;
   private static final int aue = 40;
   private static final int aje = 4;
   private static final int bD = 6;
   private static final int azM = 20;
   private static final int oF = 2;
   private static AutoTotem aeh;
   private final SettingGroup acx = this.settings.getDefaultGroup();
   private final Setting<Integer> Ni;
   private final Setting<Boolean> It;
   private final Setting<Integer> Rj;
   private final Setting<Boolean> ayV;
   private final Setting<Boolean> TG;
   private final AliceObf_iD iJ;
   private int aLB;
   private int aBs;
   private boolean RZ;
   private boolean aU;
   private boolean anG;
   private boolean Sg;
   private AliceObf_ji aPH;
   private ClientPlayerEntity vz;
   private Object aLt;
   private ClientPlayerEntity aNh;
   private AliceObf_bH op;
   private boolean it;
   private boolean AD;
   private boolean IA;
   private boolean alO;
   private boolean ahW;
   private int avv;
   private long aeW;
   private long eZ;
   private int nf;
   private int WL;
   private int aff;
   public AutoTotem() {
      super(AliceAddon.PVP_CATEGORY, "auto-totem++", "Tu dong bao ve bang cau noi tay chinh va bo sung Totem vao tay phu an toan.");
      this.Ni = this.acx.add(((Builder)((Builder)((Builder)new Builder().name("delay")).description("so tick cho giua moi lan swap sang offhand")).defaultValue(0)).min(0).sliderMax(20).build());
      this.It = this.acx
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("plus"))
                     .description("giu totem mainhand phia server trong luc refill offhand; khong doi slot hien thi"))
                  .defaultValue(true))
               .build()
         );
      this.Rj = this.acx
         .add(
            ((Builder)((Builder)((Builder)((Builder)new Builder().name("plus-slot")).description("hotbar slot (-8) du tru totem cho mainhand bridge")).defaultValue(9))
                  .min(1)
                  .max(9)
                  .sliderMax(9)
                  .visible(this.It::get))
               .build()
         );
      this.ayV = this.acx
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("sync"))
                        .description("giu totem mainhand phia server den khi offhand duoc server xac nhan"))
                     .defaultValue(false))
                  .visible(this.It::get))
               .build()
         );
      this.TG = this.acx
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                           .name("fill-mainhand"))
                        .description("tu dong refill totem cho plus-slot tu inventory"))
                     .defaultValue(true))
                  .visible(this.It::get))
               .build()
         );
      this.iJ = new AliceObf_iD(this);
      this.aPH = AliceObf_ji.aHG;
      this.aeW = 1L;
      this.nf = -1;
   }

   static void om() {
      AutoTotem var0 = aeh;
      if (var0 != null) {
         if (!var0.oF()) {
            var0.oA();
         }
      }
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         aeh = this;
         this.aLB = 0;
         this.aBs = 0;
         this.RZ = false;
         this.aU = false;
         this.anG = false;
         this.Sg = false;
         this.aPH = AliceObf_ji.aHG;
         this.vz = null;
         this.it = false;
         this.AD = false;
         this.IA = false;
         this.alO = false;
         this.ahW = false;
         this.avv = 0;
         this.aeW = 1L;
         this.eZ = 0L;
         this.nf = -1;
         this.WL = 0;
         this.aff = 0;
         this.oY();
         this.iJ.QV();
         AliceObf_fS.Od(this);
         super.onActivate();
      }
   }

   public void onDeactivate() {
      AliceObf_fS.Oe(this);
      if (aeh == this) {
         aeh = null;
      }

      this.aLB = 0;
      this.aBs = 0;
      this.RZ = false;
      this.oW();
      this.os();
      super.onDeactivate();
   }

   @AliceHandler(
      priority = -10000
   )
   private void on(PlayerTickMovementEvent var1) {
      if (this.aPH != AliceObf_ji.aHG && !this.oK()) {
         this.aPH = AliceObf_ji.aHG;
         this.oY();
         this.vz = null;
         this.aLt = null;
      }

      if (this.aPH == AliceObf_ji.Og) {
         this.aPH = AliceObf_ji.We;
         this.vz = null;
         this.aLt = null;
      }

      if (this.aPH == AliceObf_ji.We) {
         this.or();
         AliceObf_iD.QL();
         this.oX();
      } else {
         this.iJ.QT();
      }
   }

   @AliceHandler(
      priority = 10000
   )
   private void oo(Post var1) {
      boolean var6 = false;
      int var10000 = this.aPH != AliceObf_ji.PU ? 39169 : 39168;

      while (true) {
         int var3 = var10000;
         if (var3 == 39168) {
            label94: {
               if (this.oZ() && this.mc.player == this.vz && this.mc.world == this.aLt) {
                  var10000 = this.mc.player != null ? 36126 : 36127;

                  while (true) {
                     int var4 = var10000;
                     if (var4 == 36126) {
                        if (this.mc.player.input == this.op) {
                           var6 = true;
                           break label94;
                        }
                        break;
                     }

                     if (var4 != 36127) {
                        break;
                     }

                  }
               }

               var6 = false;
            }

            boolean var2 = var6;
            this.oY();
            this.aPH = var2 ? AliceObf_ji.Og : AliceObf_ji.aHG;
            break;
         }

         if (var3 != 39169) {
            break;
         }

      }

      this.iJ.QU();
   }

   @AliceHandler(
      priority = -10000
   )
   private void op(meteordevelopment.meteorclient.events.world.TickEvent.Post var1) {
      boolean var5 = false;
      int var6 = 0;
      int var10000 = this.anG ? 17455 : 17456;

      while (true) {
         int var4 = var10000;
         if (var4 != 17455) {
            if (var4 == 17456) {

               continue;
            }
         } else if (this.oK()) {
            var5 = true;
            break;
         }

         var5 = false;
         break;
      }

      boolean var2 = var5;
      if (var2) {
         var6 = 1;
      } else {
         if (this.Sg) {
            if (this.mc.player != null) {
               var6 = 18200;
            } else {
               var6 = 18201;
            }
         }

         var6 = 0;
      }

      boolean var3 = (var6 != 0);
      this.os();
      if (var3) {
         this.aBs = 2;
         this.RZ = true;
         this.aU = var2;
      }
   }

   private void oq() {
      if (!this.aU) {
         this.aBs = 0;
         this.RZ = false;
      }
   }

   private void or() {
      this.iJ.QV();
      this.Sg = false;
   }

   private void os() {
      this.anG = false;
      this.aPH = AliceObf_ji.aHG;
      this.oY();
      this.vz = null;
      this.aLt = null;
      this.or();
   }

   @AliceHandler
   private void ot(Receive var1) {
      if (var1.packet instanceof EntityStatusS2CPacket var10) {
         if (var10.getStatus() == 35) {
            this.mc.execute(() -> this.ov(var10));
         }
      } else if (var1.packet instanceof SetPlayerInventoryS2CPacket var9) {
         if (var9.slot() == 40) {
            ItemStack var18 = var9.contents();
            this.mc.execute(() -> this.ow(var18));
         }
      } else if (var1.packet instanceof ScreenHandlerSlotUpdateS2CPacket var8) {
         int var17 = var8.getSyncId();
         int var21 = var8.getSlot();
         ItemStack var23 = var8.getStack();
         this.mc.execute(() -> {
            if (this.ou()) {
               if (this.oN(var17, var21)) {
                  this.oG(var23);
               }
            }
         });
      } else if (var1.packet instanceof InventoryS2CPacket var7) {
         int var16 = var7.syncId();
         List var20 = List.copyOf(var7.contents());
         this.mc.execute(() -> {
            if (this.ou()) {
               int var3 = this.oO(var16);
               if (var3 >= 0 && var20.size() > var3) {
                  this.oG((ItemStack)var20.get(var3));
               }
            }
         });
      } else {
         Packet var14 = var1.packet;
         int var10000 = var14 instanceof EntityEquipmentUpdateS2CPacket ? 19036 : 19037;

         while (true) {
            int var6 = var10000;
            if (var6 == 19036) {
               EntityEquipmentUpdateS2CPacket var2 = (EntityEquipmentUpdateS2CPacket)var14;
               var14 = null;

               for (Pair var5 : var2.getEquipmentList()) {
                  if (var5.getFirst() == EquipmentSlot.OFFHAND) {
                     char var24 = 35615;
                  } else {
                     char var25 = 35616;
                  }
               }

               if (var14 == null) {
                  return;
               }

               int var19 = var2.getEntityId();
               Object var22 = var14;
               this.mc.execute(() -> {
                  if (this.ou()) {
                     if (var19 != this.mc.player.getId()) {

                     } else {
                        char var3 = 46765;
                     }
                  }
               });
               break;
            }

            if (var6 != 19037) {
               break;
            }

         }
      }
   }

   private boolean ou() {
      if (this.isActive()) {
         if (!(this.mc.player != null)) {
            char var1 = 53341;
         }
      }

      return false;
   }

   private void ov(EntityStatusS2CPacket var1) {
      if (this.ou()) {
         if (var1.getEntity(this.mc.world) == this.mc.player) {
            if ((Boolean)this.It.get()) {
               this.oB();
            }
         }
      }
   }

   private void ow(ItemStack var1) {
      if (this.ou()) {
         this.oG(var1);
      }
   }

   @AliceHandler
   private void ox(Pre var1) {
      this.RZ = this.aBs > 0;
      if (this.RZ) {
         this.aBs--;
      }

      if (this.mc.player != null
         && this.mc.world != null
         && this.mc.interactionManager != null
         && !this.mc.player.isCreative()
         && !this.mc.player.isSpectator()) {
         int var2 = this.oU();
         boolean var3 = this.mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING);
         if (!var3) {
            this.or();
            this.oq();
         } else {
            this.aPH = AliceObf_ji.aHG;
            this.oY();
            this.vz = null;
            this.aLt = null;
         }

         if (this.it) {
            this.aff++;
            if (this.aff > 20) {
               this.oW();
            }
         }

         if (this.IA) {
            if (AliceObf_fS.Pg(this.eZ)) {

            } else {
               char var7 = 49600;
            }
         }

         if (this.it
            && this.IA
            && !this.ahW
            && this.avv == 0
            && (this.nf < 0 || this.nf > 8 || !this.mc.player.getInventory().getStack(this.nf).isOf(Items.TOTEM_OF_UNDYING))) {
            AliceObf_fS.Pf(this.eZ);
            this.IA = false;
            this.eZ = 0L;
            this.nf = -1;
            this.AD = true;
         }

         if (this.it && this.ahW) {
            this.WL++;
            if (this.WL >= 6) {
               this.oI();
            }
         } else if (this.it && this.AD && this.oR() >= 0) {
            this.oF();
         } else if (var3) {
            this.aLB = 0;
            if (!this.it || this.alO || !this.AD && !this.IA) {
               if (this.it) {
                  this.oW();
               }

               if ((Boolean)this.It.get() && (Boolean)this.TG.get() && !this.mc.player.getInventory().getStack(var2).isOf(Items.TOTEM_OF_UNDYING)
                  )
                {
                  this.oV(var2);
               } else {
                  int var4 = this.iJ.QW();
                  if (var4 < 0) {
                     this.iJ.QV();
                  }
               }
            }
         } else {
            int var8 = !this.it ? 2329 : 2328;

            while (true) {
               int var5 = var8;
               if (var5 == 2328) {
                  if (this.avv >= 4) {
                     this.oW();
                  }
                  break;
               }

               if (var5 != 2329) {
                  break;
               }

               var8 = 2327;
            }

            if (this.oT() < 0) {
               this.aPH = AliceObf_ji.aHG;
               this.oY();
               this.vz = null;
               var8 = !this.it ? 7005 : 7004;

               while (true) {
                  int var6 = var8;
                  if (var6 == 7004) {
                     this.oW();
                     break;
                  }

                  if (var6 != 7005) {
                     break;
                  }

                  var8 = 7003;
               }
            } else if (this.aLB > 0 && !this.it) {
               this.aLB--;
            } else if (!this.RZ) {
               if (this.aPH != AliceObf_ji.We && this.aPH != AliceObf_ji.PU) {
                  if (this.aPH != AliceObf_ji.Og) {
                     this.oy();
                  } else {
                     this.oz();
                  }
               }
            }
         }
      } else {
         this.aLB = 0;
         this.aBs = 0;
         this.RZ = false;
         this.oW();
         this.os();
      }
   }

   private void oy() {
      this.oq();
      if (!this.RZ) {
         this.anG = true;
         this.or();
         AliceObf_iD.QL();
         if (this.aPH == AliceObf_ji.aHG) {
            this.aPH = AliceObf_ji.We;
         }
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean oz() {
      if (!this.RZ && this.aPH == AliceObf_ji.Og && this.vz == this.mc.player) {
         int var10000 = this.aLt != this.mc.world ? 53868 : 53869;

         while (true) {
            int var6 = var10000;
            if (var6 == 53868) {
               break;
            }

            if (var6 != 53869) {
               int var1 = this.oT();
               if (var1 >= 0 && this.oK()) {
                  boolean var2 = this.it && this.IA && this.eZ > 0L && AliceObf_fS.Pg(this.eZ);
                  boolean var3 = var2
                     ? AliceObf_fS.Pb(this.eZ)
                     : (
                        this.it
                           ? AliceObf_fS.OT()
                           : AliceObf_fS.OS() || AliceObf_fS.OV()
                     );
                  if (var3) {
                     boolean var4 = false;
                     boolean var9 = false /* VF: Semaphore variable */;

                     try {
                        var9 = true;
                        var4 = this.oL(var1);
                        if (var4 && var2) {
                           var4 = AliceObf_fS.Pc(this.eZ, true);
                           if (var4) {
                              this.AD = false;
                              this.alO = true;
                              this.ahW = (Boolean)this.ayV.get();
                              this.WL = 0;
                              this.IA = AliceObf_fS.Pg(this.eZ);
                              if (!this.IA) {
                                 this.eZ = 0L;
                                 this.nf = -1;
                              }
                           }
                        }

                        if (var4) {
                           this.aPH = AliceObf_ji.aHG;
                           this.aLB = (Integer)this.Ni.get();
                           var9 = false;
                        } else {
                           var9 = false;
                        }
                     } finally {
                        if (var9) {
                           if (!var4) {
                              if (var2) {
                                 short var16 = 14844;
                              } else {
                                 short var17 = 14845;
                              }

                              AliceObf_fS.OU();
                           }
                        }
                     }

                     if (!var4) {
                        if (!var2) {
                           short var18 = 4481;
                        } else {
                           short var19 = 4480;
                        }

                        AliceObf_fS.OU();
                     }

                     return var4;
                  }

                  return false;
               }

               this.aPH = AliceObf_ji.aHG;
               return false;
            }

         }
      }

      return false;
   }

   private void oA() {
      if (this.isActive()) {

      } else {
         short var2 = 3814;
      }
   }

   private void oB() {
      this.aBs = 0;
      this.RZ = false;
      boolean var1 = this.IA && this.eZ > 0L && this.oE() && AliceObf_fS.Pe(this.eZ);
      if (!var1 && this.IA && this.eZ > 0L) {
         if (this.ahW) {
            AliceObf_fS.Pd(this.eZ);
         } else {
            AliceObf_fS.Pf(this.eZ);
         }
      }

      this.os();
      if (var1) {
         this.oD();
      } else {
         this.oC();
      }

      if (!var1 && !this.oF() && this.AD && this.oR() >= 0) {
         AliceObf_fS.OX();
      }
   }

   private void oC() {
      this.it = true;
      this.AD = true;
      this.IA = false;
      this.alO = false;
      this.ahW = false;
      this.avv = 0;
      this.eZ = 0L;
      this.nf = -1;
      this.WL = 0;
      this.aff = 0;
   }

   private void oD() {
      this.it = true;
      this.AD = false;
      this.IA = true;
      this.alO = false;
      this.ahW = false;
      this.avv = 0;
      this.WL = 0;
      this.aff = 0;
   }

   private boolean oE() {
      return this.mc.player != null
         && this.nf >= 0
         && this.nf <= 8
         && this.mc.player.getInventory().getStack(this.nf).isOf(Items.TOTEM_OF_UNDYING);
   }

   private boolean oF() {
      if (!this.AD || this.IA || this.avv >= 4) {
         return false;
      } else if (this.mc.player == null || this.mc.world == null) {
         return false;
      } else if (!this.mc.player.isCreative() && !this.mc.player.isSpectator()) {
         int var1 = this.oR();
         if (var1 < 0) {
            return false;
         } else {
            long var2 = this.oS();
            if (!AliceObf_fS.Pa(var1, var2)) {
               byte var10000 = 50;
            } else {
               byte var4 = 51;
            }

            this.AD = false;
            this.IA = true;
            this.alO = false;
            this.eZ = var2;
            this.nf = var1;
            this.WL = 0;
            this.aPH = AliceObf_ji.aHG;
            this.oY();
            this.vz = null;
            return true;
         }
      } else {
         return false;
      }
   }

   private void oG(ItemStack var1) {
      if (!this.it) {
         if (!var1.isOf(Items.TOTEM_OF_UNDYING) && this.oQ() >= 0) {
            this.oy();
         }
      } else {
         this.oH(var1);
      }
   }

   private void oH(ItemStack var1) {
      if (var1.isOf(Items.TOTEM_OF_UNDYING)) {
         if (this.eZ > 0L) {
            AliceObf_fS.Pd(this.eZ);
         }

         this.ahW = false;
         this.WL = 0;
         this.AD = false;
      } else if (!this.ahW) {
         if (this.AD) {
            if (!this.oF()) {
               if (this.oR() < 0 && this.oT() >= 0) {
                  this.AD = false;
                  this.oy();
               }
            }
         } else {
            this.iJ.QV();
            if (this.avv >= 4) {
               this.oW();
            } else if (!(this.mc.player != null)) {
               short var2 = 4837;
            }
         }
      }
   }

   private void oI() {
      if (this.eZ > 0L) {
         AliceObf_fS.Pf(this.eZ);
      }

      this.ahW = false;
      this.IA = false;
      this.alO = false;
      this.eZ = 0L;
      this.nf = -1;
      this.WL = 0;
      if (this.mc.player != null && !this.mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING) && this.oT() >= 0) {
         this.oy();
      }
   }

   private boolean oJ() {
      return this.it && (this.AD || this.IA || this.ahW || this.avv > 0);
   }

   boolean oK() {
      if (this.mc.player == null) {
         return false;
      } else {
         return this.mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING) ? false : this.isActive();
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean oL(int var1) {
      if (this.mc.player != null
         && this.mc.interactionManager != null
         && var1 >= 0
         && var1 < 36
         && this.mc.player.getInventory().getStack(var1).isOf(Items.TOTEM_OF_UNDYING)) {
         ScreenHandler var2 = this.mc.player.currentScreenHandler;
         if (var2 == null) {
            return false;
         } else {
            int var3 = this.oM(var1);
            int var10000 = var3 < 0 ? 45836 : 45835;

            while (true) {
               int var13 = var10000;
               if (var13 == 45835) {
                  var10000 = var3 >= var2.slots.size() ? 4595 : 4596;

                  while (true) {
                     int var14 = var10000;
                     if (var14 == 4595) {
                        return false;
                     }

                     if (var14 != 4596) {
                        Slot var4 = var2.getSlot(var3);
                        if (var4.inventory == this.mc.player.getInventory()
                           && var4.getIndex() == var1
                           && var4.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                           if (this.it) {
                              this.avv++;
                           }

                           int var5 = var2.getRevision();
                           ClientPlayerEntity var6 = this.mc.player;
                           ItemStack var7 = var6.getOffHandStack().copy();
                           ArrayList var8 = new ArrayList(var2.slots.size());

                           for (Slot var10 : var2.slots) {
                              var8.add(var10.getStack().copy());
                           }

                           ItemStack var18 = var2.getCursorStack().copy();
                           int var19 = AliceObf_fS.PE();
                           boolean var16 = false /* VF: Semaphore variable */;

                           boolean var11;
                           try {
                              var16 = true;
                              this.mc.interactionManager.clickSlot(var2.syncId, var3, 40, SlotActionType.SWAP, this.mc.player);
                              var11 = AliceObf_fS.PE() > var19;
                              var16 = false;
                           } finally {
                              if (var16) {
                                 if (AliceObf_fS.PE() == var19) {
                                    short var24 = 14251;
                                 } else {
                                    short var23 = 14252;
                                 }
                              }
                           }

                           if (AliceObf_fS.PE() == var19) {
                              var2.updateSlotStacks(var5, var8, var18);
                              var6.getInventory().setStack(40, var7);
                           }

                           return var11;
                        }

                        return false;
                     }

                  }
               }

               if (var13 != 45836) {
                  return false;
               }

            }
         }
      } else {
         return false;
      }
   }

   private int oM(int var1) {
      if (this.mc.player == null) {
         return -1;
      } else {
         ScreenHandler var2 = this.mc.player.currentScreenHandler;
         if (var2 == null) {
            return -1;
         } else {
            int var3 = 0;

            while (true) {
               int var10000 = var3 >= var2.slots.size() ? 48960 : 48959;

               while (true) {
                  int var5 = var10000;
                  if (var5 == 48959) {
                     Slot var4 = var2.getSlot(var3);
                     if (var4.inventory instanceof PlayerInventory && var4.getIndex() == var1) {
                        return var3;
                     }

                     var3++;
                     break;
                  }

                  if (var5 != 48960) {
                     return -1;
                  }

               }
            }
         }
      }
   }

   private boolean oN(int var1, int var2) {
      if (var2 < 0) {
         return false;
      } else {
         ScreenHandler var3 = this.oP(var1);
         if (var3 != null && var2 < var3.slots.size()) {
            Slot var4 = var3.getSlot(var2);
            if (var4.inventory == this.mc.player.getInventory()) {

            } else {
               short var5 = 27472;
            }

            return false;
         } else {
            return false;
         }
      }
   }

   private int oO(int var1) {
      ScreenHandler var2 = this.oP(var1);
      if (var2 == null) {
         return -1;
      } else {
         if (var2 == this.mc.player.playerScreenHandler && 45 < var2.slots.size()) {
            Slot var3 = var2.getSlot(45);
            if (var3.inventory == this.mc.player.getInventory() && var3.getIndex() == 40) {
               return 45;
            }
         }

         byte var5 = 0;
         if (var5 >= var2.slots.size()) {

         } else {
            char var6 = 33142;
         }

         return -1;
      }
   }

   private ScreenHandler oP(int var1) {
      if (this.mc.player == null) {
         return null;
      } else if (this.mc.player.playerScreenHandler.syncId == var1) {
         return this.mc.player.playerScreenHandler;
      } else {
         ScreenHandler var2 = this.mc.player.currentScreenHandler;
         return var2 != null && var2.syncId == var1 ? var2 : null;
      }
   }

   private int oQ() {
      if (this.mc.player == null) {
         return -1;
      } else {
         int var1 = this.oU();
         if (this.mc.player.getInventory().getStack(var1).isOf(Items.TOTEM_OF_UNDYING)) {
            return var1;
         } else {
            for (int var2 = 0; var2 < 9; var2++) {
               if (var2 != var1 && this.mc.player.getInventory().getStack(var2).isOf(Items.TOTEM_OF_UNDYING)) {
                  return var2;
               }
            }

            for (int var3 = 9; var3 < 36; var3++) {
               if (this.mc.player.getInventory().getStack(var3).isOf(Items.TOTEM_OF_UNDYING)) {
                  return var3;
               }
            }

            return -1;
         }
      }
   }

   private int oR() {
      if (this.mc.player == null) {
         return -1;
      } else {
         int var1 = this.oU();
         if (this.mc.player.getInventory().getStack(var1).isOf(Items.TOTEM_OF_UNDYING)) {
            return var1;
         } else {
            for (int var2 = 0; var2 < 9; var2++) {
               if (!(var2 == var1)) {
                  char var3 = 34008;
               }
            }

            return -1;
         }
      }
   }

   private long oS() {
      long var1 = this.aeW++;
      if (var1 > 0L) {
         return var1;
      } else {
         this.aeW = 2L;
         return 1L;
      }
   }

   private int oT() {
      if (this.it && this.mc.player != null) {
         int var1 = this.oU();

         for (int var2 = 9; var2 < 36; var2++) {
            if (this.mc.player.getInventory().getStack(var2).isOf(Items.TOTEM_OF_UNDYING)) {
               return var2;
            }
         }

         for (int var3 = 0; var3 < 9; var3++) {
            if (var3 != var1 && this.mc.player.getInventory().getStack(var3).isOf(Items.TOTEM_OF_UNDYING)) {
               return var3;
            }
         }

         if (this.mc.player.getInventory().getStack(var1).isOf(Items.TOTEM_OF_UNDYING)) {

         } else {
            short var4 = 2871;
         }

         return -1;
      } else {
         return this.oQ();
      }
   }

   private int oU() {
      return Math.max(0, Math.min(8, (Integer)this.Rj.get() - 1));
   }

   private AliceObf_hE oV(int var1) {
      if (this.mc.player != null && !this.RZ) {
         AliceObf_hE var2 = this.iJ.QP(Items.TOTEM_OF_UNDYING, var1);
         this.Sg = false;
         return var2;
      } else {
         return AliceObf_hE.ayS;
      }
   }

   private void oW() {
      this.it = false;
      this.AD = false;
      this.avv = 0;
      if (this.IA && this.eZ > 0L) {
         AliceObf_fS.Pf(this.eZ);
      }

      this.IA = false;
      this.alO = false;
      this.ahW = false;
      this.eZ = 0L;
      this.nf = -1;
      this.WL = 0;
      this.aff = 0;
   }

   private void oX() {
      ClientPlayerEntity var1 = this.mc.player;
      if (var1 != null && !var1.hasVehicle()) {
         if (this.op == null || var1.input != this.op) {
            this.oY();
            this.aNh = var1;
            this.op = new AliceObf_bH(this, var1.input);
            var1.input = this.op;
         }
      }
   }

   private void oY() {
      if (this.op != null) {
         AliceObf_au.Fy(this.aNh, this.op);
      }

      this.op = null;
      this.aNh = null;
   }

   private boolean oZ() {
      ClientPlayerEntity var1 = this.mc.player;
      if (var1 != null && !var1.isSprinting()) {
         Input var2 = var1.input;
         if (var2 == null) {
            return true;
         } else {
            Vec2f var3 = var2.getMovementInput();
            if (var3 != null && var3.lengthSquared() > 1.0E-5F) {
               return false;
            } else {
               PlayerInput var4 = var2.playerInput;
               return var4 == null || !var4.forward() && !var4.backward() && !var4.left() && !var4.right();
            }
         }
      } else {
         return false;
      }
   }

   public AliceObf_y prepareCombatIntent() {
      if (!this.isActive()) {
         return null;
      } else if (!this.oJ()) {
         return null;
      } else if (this.mc.player == null || this.mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
         return null;
      } else if (!this.AD) {
         return null;
      } else {
         int var1 = this.oR();
         return var1 < 0 ? null : new AliceObf_kH(this);
      }
   }

   public boolean canYieldEpoch() {
      return false;
   }

   public boolean reservesLowerPriorityEpochs() {
      return this.oJ();
   }

   public int combatPriority() {
      return Integer.MAX_VALUE;
   }

   public boolean canFollowEmergencyOffhandSwap() {
      return true;
   }

   public void suspendForItemUse() {
      this.os();
   }

   }

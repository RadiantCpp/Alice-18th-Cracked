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
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.IntSetting.Builder;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.util.hit.EntityHitResult;

public final class ShieldDisable extends AliceModule {
   private final SettingGroup aq = this.settings.getDefaultGroup();
   private final Setting<Integer> Oc;
   private final Setting<Integer> aQA;
   private final Setting<Boolean> azg;
   private final Setting<Boolean> ajZ;
   private final Setting<Boolean> YK;
   private final Setting<Boolean> IZ;
   private final Setting<Boolean> atr;
   private int ast;
   private int IG;
   private int LX;
   private int Up;
   public ShieldDisable() {
      super(AliceAddon.PVP_CATEGORY, "ShieldDisable", "Tu dong chuyen sang riu va tan cong nguoi choi dang gio khien.");
      this.Oc = this.aq.add(((Builder)((Builder)((Builder)new Builder().name("hit-delay")).description("tick giua cac lan danh riu pha khien")).defaultValue(0)).min(0).sliderMax(20).build());
      this.aQA = this.aq.add(((Builder)((Builder)((Builder)new Builder().name("switch-delay")).description("tick cho sau khi chuyen sang riu")).defaultValue(0)).min(0).sliderMax(20).build());
      this.azg = this.aq
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("switch-back"))
                     .description("quay ve slot cu khi muc tieu ngung dung khien"))
                  .defaultValue(true))
               .build()
         );
      this.ajZ = this.aq
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("ignore-friends"))
                     .description("bo qua nguoi choi trong danh sach ban be cua Meteor"))
                  .defaultValue(true))
               .build()
         );
      this.YK = this.aq
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("stun"))
                     .description("danh them 1 phat riu khi pha khien"))
                  .defaultValue(false))
               .build()
         );
      this.IZ = this.aq
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("click-simulation"))
                     .description("chi chuyen sang riu va de cho click vat ly thuc hien cu danh"))
                  .defaultValue(false))
               .build()
         );
      this.atr = this.aq
         .add(
            ((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)((meteordevelopment.meteorclient.settings.BoolSetting.Builder)new meteordevelopment.meteorclient.settings.BoolSetting.Builder()
                        .name("hold-axe"))
                     .description("chi chay khi dang cam riu"))
                  .defaultValue(false))
               .build()
         );
      this.ast = -1;
      this.IG = -1;
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.ast = -1;
         this.IG = -1;
         this.LX = (Integer)this.Oc.get();
         this.Up = (Integer)this.aQA.get();
         super.onActivate();
      }
   }

   public void onDeactivate() {
      this.ags(true);
      super.onDeactivate();
   }

   @AliceHandler
   private void ago(Post var1) {
      if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null && this.mc.currentScreen == null) {
         if (this.mc.player.isSpectator()) {

         } else {
            char var9 = 44803;
         }

         if (!this.agv()) {
            if (this.agt()) {
               if (!(this.mc.crosshairTarget instanceof EntityHitResult var2)) {
                  this.agr();
               } else {
                  if (var2.getEntity() instanceof PlayerEntity var7) {
                     int var10001 = !var7.isBlocking() ? 54976 : 54977;

                     while (true) {
                        int var5 = var10001;
                        if (var5 == 54976) {
                           break;
                        }

                        if (var5 != 54977) {
                           if (!var7.isSpectator() && !var7.isCreative()) {
                              if ((Boolean)this.ajZ.get() && !Friends.get().shouldAttack(var7)) {
                                 this.agr();
                                 return;
                              }

                              if (this.mc.player.isUsingItem()) {
                                 this.agr();
                                 return;
                              }

                              if ((Boolean)this.atr.get() && !(this.mc.player.getMainHandStack().getItem() instanceof AxeItem)) {
                                 return;
                              }

                              if (this.ast == -1) {
                                 this.ast = this.mc.player.getInventory().getSelectedSlot();
                                 this.IG = this.ast;
                              }

                              if (!(this.mc.player.getMainHandStack().getItem() instanceof AxeItem)) {
                                 if (this.Up > 0) {
                                    short var13 = 138;
                                 } else {
                                    short var14 = 139;
                                 }

                                 int var8 = this.agq();
                                 if (var8 != -1) {
                                    if (!InvUtils.swap(var8, false)) {
                                       this.agu();
                                       return;
                                    }

                                    this.IG = var8;
                                    this.Up = (Integer)this.aQA.get();
                                    return;
                                 }

                                 this.agr();
                                 return;
                              }

                              var10001 = this.Up > 0 ? 55676 : 55677;

                              while (true) {
                                 int var6 = var10001;
                                 if (var6 == 55676) {
                                    this.Up--;
                                    return;
                                 }

                                 if (var6 != 55677) {
                                    if (this.LX > 0) {
                                       this.LX--;
                                       return;
                                    }

                                    if ((Boolean)this.IZ.get()) {
                                       short var11 = 23705;
                                    } else {
                                       short var12 = 23706;
                                    }

                                    this.agp(var7);
                                    if ((Boolean)this.YK.get()) {
                                       this.agp(var7);
                                    }

                                    this.LX = (Integer)this.Oc.get();
                                    this.Up = (Integer)this.aQA.get();
                                    return;
                                 }

                              }
                           }

                           this.agr();
                           return;
                        }

                     }
                  }

                  this.agr();
               }
            }
         }
      } else {
         this.agr();
      }
   }

   private void agp(PlayerEntity var1) {
      this.mc.interactionManager.attackEntity(this.mc.player, var1);
      this.mc.player.swingHand(Hand.MAIN_HAND);
   }

   private int agq() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.mc.player.getInventory().getStack(var1).getItem() instanceof AxeItem) {
            return var1;
         }
      }

      return -1;
   }

   private void agr() {
      this.ags(false);
   }

   private void ags(boolean var1) {
      int var10000 = this.ast == -1 ? 49822 : 49821;

      while (true) {
         int var2 = var10000;
         if (var2 == 49821) {
            if (this.mc.player != null && this.mc.interactionManager != null) {
               if (!this.agt()) {
                  return;
               }

               var10000 = !var1 ? 24849 : 24850;

               while (true) {
                  int var3 = var10000;
                  if (var3 == 24849) {
                     if (this.agv()) {
                        return;
                     }
                     break;
                  }

                  if (var3 != 24850) {
                     break;
                  }

               }

               if (!(Boolean)this.azg.get()) {
                  short var5 = 6022;
               } else {
                  short var6 = 6021;
               }
            }
            break;
         }

         if (var2 != 49822) {
            break;
         }

      }

      this.ast = -1;
      this.IG = -1;
      this.LX = (Integer)this.Oc.get();
      this.Up = (Integer)this.aQA.get();
   }

   private boolean agt() {
      if (this.ast != -1 && this.IG != -1 && this.mc.player != null) {
         if (this.mc.player.getInventory().getSelectedSlot() == this.IG) {
            return true;
         } else {
            this.agu();
            return false;
         }
      } else {
         return true;
      }
   }

   private void agu() {
      this.ast = -1;
      this.IG = -1;
      this.LX = (Integer)this.Oc.get();
      this.Up = (Integer)this.aQA.get();
   }

   private boolean agv() {
      if (this.mc.player == null) {
         return false;
      } else {
         int var1 = AliceObf_fS.OL();
         return var1 >= 0 && var1 != this.mc.player.getInventory().getSelectedSlot();
      }
   }

   }

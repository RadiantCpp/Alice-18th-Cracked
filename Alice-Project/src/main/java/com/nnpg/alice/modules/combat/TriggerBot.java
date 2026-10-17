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
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;

public final class TriggerBot extends AliceModule {
   private final SettingGroup aLw = this.settings.getDefaultGroup();
   private final Setting<Boolean> aru;
   private final Setting<Boolean> vy;
   private final Setting<Boolean> apg;
   public TriggerBot() {
      super(AliceAddon.PVP_CATEGORY, "TriggerBot", "Tu dong tan cong muc tieu hop le khi tam ngam nam truc tiep tren muc tieu.");
      this.aru = this.aLw.add(((Builder)((Builder)((Builder)new Builder().name("players-only")).description("chi danh thuc the nguoi choi")).defaultValue(true)).build());
      this.vy = this.aLw.add(((Builder)((Builder)((Builder)new Builder().name("ignore-friends")).description("bo qua nguoi choi trong danh sach ban be cua Meteor")).defaultValue(true)).build());
      this.apg = this.aLw.add(((Builder)((Builder)((Builder)new Builder().name("swing-hand")).description("hien thi hieu ung vung tay sau khi danh")).defaultValue(true)).build());
   }

   @AliceHandler
   private void anv(Post var1) {
      if (this.mc.player != null && this.mc.world != null && this.mc.interactionManager != null && this.mc.currentScreen == null) {
         if (!this.mc.player.isSpectator()) {

         } else {
            char var6 = 50827;
         }
      }
   }

   private boolean anw() {
      int var1 = AliceObf_fS.OL();
      if (var1 >= 0) {
         if (var1 != this.mc.player.getInventory().getSelectedSlot()) {

         } else {
            short var2 = 15630;
         }
      }

      return false;
   }

   }

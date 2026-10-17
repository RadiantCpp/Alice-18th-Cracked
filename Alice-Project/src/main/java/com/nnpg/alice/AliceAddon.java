package com.nnpg.alice;

import com.nnpg.alice.modules.misc.AlicePayloadCoordinator;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AliceAddon extends MeteorAddon {
   public static final Category CATEGORY = new Category("ALICE", new ItemStack(Items.END_CRYSTAL));
   public static final Category ESP_CATEGORY = new Category("ALICE ESP", new ItemStack(Items.ENDER_EYE));
   public static final Category PVP_CATEGORY = new Category("ALICE PVP", new ItemStack(Items.NETHERITE_SWORD));
   public static final Category MISC_CATEGORY = new Category("Misc", new ItemStack(Items.TOTEM_OF_UNDYING));
   public static final Category SEX_CATEGORY = new Category("Sex", new ItemStack(Items.PLAYER_HEAD));
   public static final Category FAKE_CATEGORY = new Category("Fake", new ItemStack(Items.COMMAND_BLOCK));

   public void onInitialize() {
      AlicePayloadCoordinator.init();
   }

   public void onRegisterCategories() {
      Modules.registerCategory(CATEGORY);
      Modules.registerCategory(ESP_CATEGORY);
      Modules.registerCategory(PVP_CATEGORY);
      Modules.registerCategory(MISC_CATEGORY);
      Modules.registerCategory(SEX_CATEGORY);
   }

   public String getPackage() {
      return "com.nnpg.alice";
   }
}

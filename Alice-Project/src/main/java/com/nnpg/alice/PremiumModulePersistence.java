package com.nnpg.alice;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;

public final class PremiumModulePersistence {
   private static final int AUTOSAVE_INTERVAL_TICKS = 20;
   private static PremiumAutosave autosave;

   private PremiumModulePersistence() {
   }

   public static void restore(Modules modules, Module... targetModules) {
      File file = getFile(modules);
      if (file != null && file.isFile()) {
         try {
            NbtCompound tag = NbtIo.read(file.toPath());
            if (tag != null) {
               restoreFromTag(tag, targetModules);
            }
         } catch (RuntimeException | IOException e) {
         }
      }

      installAutosave(modules, targetModules);
   }

   public static int restoreFromTag(NbtCompound tag, Module... targetModules) {
      int restored = 0;
      for (Module module : targetModules) {
         String key = module.name.toLowerCase(Locale.ROOT);
         if (tag.contains(key)) {
            try {
               NbtCompound comp = tag.getCompound(key).orElse(null);
               if (comp != null) {
                  module.fromTag(comp);
                  restored++;
               }
            } catch (Throwable t) {
            }
         }
      }
      return restored;
   }

   public static void save(File file, Module... targetModules) {
      try {
         NbtCompound tag = new NbtCompound();
         for (Module module : targetModules) {
            String key = module.name.toLowerCase(Locale.ROOT);
            tag.put(key, module.toTag());
         }
         if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
         }
         NbtIo.write(tag, file.toPath());
      } catch (Throwable t) {
      }
   }

   private static File getFile(Modules modules) {
      File folder = new File(MeteorClient.FOLDER, "alice");
      if (!folder.exists()) {
         folder.mkdirs();
      }
      return new File(folder, "modules.nbt");
   }

   private static synchronized void installAutosave(Modules modules, Module... targetModules) {
      if (autosave == null) {
         autosave = new PremiumAutosave(modules, targetModules);
         MeteorClient.EVENT_BUS.subscribe(autosave);
      }
   }

   private static class PremiumAutosave {
      private final Modules modules;
      private final Module[] targetModules;
      private int ticks = 0;

      PremiumAutosave(Modules modules, Module[] targetModules) {
         this.modules = modules;
         this.targetModules = targetModules;
      }

      @EventHandler
      private void onTick(TickEvent.Post event) {
         if (++this.ticks >= AUTOSAVE_INTERVAL_TICKS) {
            this.ticks = 0;
            save(getFile(this.modules), this.targetModules);
         }
      }
   }
}

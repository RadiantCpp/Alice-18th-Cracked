package com.nnpg.alice.bridge;

import java.util.UUID;
import net.minecraft.text.Text;

public final class AliceServerBridge {
   private static volatile View aU;
   public static int[] ct = new int[25];

   private AliceServerBridge() {
   }

   public static void install(View view) {
      aU = view;
   }

   public static boolean eV(String msg) {
      View view = aU;
      if (view != null) {
         view.send(msg);
         return true;
      }
      return false;
   }

   public static Text eW(UUID uuid, Text text) {
      View view = aU;
      return view != null ? view.nametag(uuid, text) : text;
   }

   public static Text eX(UUID uuid, Text text) {
      View view = aU;
      return view != null ? view.chat(uuid, text) : text;
   }

   public static int eY(UUID uuid, int color) {
      View view = aU;
      return view != null ? view.color(uuid, color) : color;
   }

   public interface View {
      void send(String msg);
      Text nametag(UUID uuid, Text text);
      Text chat(UUID uuid, Text text);
      int color(UUID uuid, int color);
   }
}

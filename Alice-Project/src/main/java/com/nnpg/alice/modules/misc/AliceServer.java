package com.nnpg.alice.modules.misc;

import com.nnpg.alice.internal.*;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import net.minecraft.util.hit.HitResult;


import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.bridge.AliceServerBridge;
import com.nnpg.alice.bridge.PayloadRuntimeHost;
import com.nnpg.alice.bridge.AliceServerBridge.View;
import com.nnpg.alice.premium.AliceHandler;
import com.nnpg.alice.premium.AliceModule;
import com.nnpg.alice.premium.PremiumAccessGate;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import meteordevelopment.meteorclient.events.world.TickEvent.Post;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting.Builder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.PlayerListEntry;

public final class AliceServer extends AliceModule implements View {
   private static final int Oz = 16034507;
   private static final int apQ = 13350650;
   private static final int jP = 16765286;
   private static final long Wy = 3000000000L;
   private static final long Kh = 30000000000L;
   private final SettingGroup aIa = this.settings.getDefaultGroup();
   private final Setting<Boolean> azr;
   private final Setting<String> aIc;
   private final Setting<Boolean> TD;
   private final Setting<Boolean> ayM;
   private final Setting<Boolean> AQ;
   private final AliceObf_J ace;
   private final Map<UUID, AliceObf_fD> qu;
   private final Set<UUID> aJe;
   private final ThreadPoolExecutor Sz;
   private ClientWorld tm;
   private Future<?> aKh;
   private long sh;
   private long Qi;
   private long atv;
   private int vR;
   private int aLg;
   private boolean aAn;
   public AliceServer() {
      super(AliceAddon.MISC_CATEGORY, "Alice Server", "Chat ri\u00eang /alice, bi\u1ec7t danh v\u00e0 nh\u1eadn di\u1ec7n c\u1ed9ng \u0111\u1ed3ng ALICE \u1edf g\u1ea7n.");
      this.azr = this.aIa
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("hide-chat-name")).description("Ch\u1ec9 che t\u00ean trong chat b\u1eb1ng bi\u1ec7t danh; nametag v\u1eabn l\u00e0 t\u00ean Minecraft."))
                  .defaultValue(false))
               .build()
         );
      this.aIc = this.aIa
         .add(
            ((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)((meteordevelopment.meteorclient.settings.StringSetting.Builder)new meteordevelopment.meteorclient.settings.StringSetting.Builder()
                           .name("nickname"))
                        .description("Bi\u1ec7t danh c\u00f4ng khai trong chat, t\u1ed1i \u0111a 24 k\u00fd t\u1ef1; \u0111\u1ec3 tr\u1ed1ng d\u00f9ng t\u00ean Alice t\u1ef1 sinh."))
                     .defaultValue(""))
                  .visible(this.azr::get))
               .build()
         );
      this.TD = this.aIa.add(((Builder)((Builder)((Builder)new Builder().name("alice-nametags")).description("Nametag pastel v\u00e0 v\u01b0\u01a1ng mi\u1ec7n cho ng\u01b0\u1eddi d\u00f9ng ALICE nh\u00ecn th\u1ea5y \u0111\u01b0\u1ee3c.")).defaultValue(true)).build());
      this.ayM = this.aIa
         .add(
            ((Builder)((Builder)((Builder)new Builder().name("decorate-vanilla-chat")).description("Trang tr\u00ed ng\u01b0\u1eddi g\u1eedi ALICE trong chat Minecraft, kh\u00f4ng chuy\u1ec3n ti\u1ebfp n\u1ed9i dung."))
                  .defaultValue(true))
               .build()
         );
      this.AQ = this.aIa.add(((Builder)((Builder)((Builder)new Builder().name("world-notice")).description("Nh\u1eafc k\u00eanh ALICE v\u00e0 l\u1ec7nh /alice m\u1ed7i khi v\u00e0o world kh\u00e1c.")).defaultValue(true)).build());
      this.ace = new AliceObf_J();
      this.qu = new HashMap<>();
      this.aJe = new HashSet<>();
      this.Sz = new ThreadPoolExecutor(
         (1),
         (1),
         30L,
         TimeUnit.SECONDS,
         new ArrayBlockingQueue<>((2)),
         var0 -> {
            Thread var1 = new Thread(var0, "alice-community");
            var1.setDaemon(true);
            return var1;
         }
      );
      this.Sz.allowCoreThreadTimeOut(true);
      AliceServerBridge.install(this);
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         super.onActivate();
         this.X(true);
         this.tm = null;
         this.atv = 0L;
         this.sh = 0L;
      }
   }

   public void onDeactivate() {
      this.X(true);
      this.tm = null;
      super.onDeactivate();
   }

   private void X(boolean var1) {
      if (this.aKh != null) {
         this.aKh.cancel(true);
      }

      this.aKh = null;
      this.Sz.getQueue().clear();
      this.ace.aKb(var1);
      this.qu.clear();
      this.aJe.clear();
      this.aAn = false;
      this.aLg = (0);
      this.Qi = 0L;
      this.vR = (0);
      if (this.tm != null) {
         String var2 = AliceObf_sP.aJG(
               this.ace.aJV(), this.ace.aJW(), false
            )
            .toString();
         this.Sz.submit(() -> {
            try {
               this.ab(var2);
            } catch (Exception var3) {
               if (var3 instanceof InterruptedException) {
                  Thread.currentThread().interrupt();
               }
            }
         });
      }
   }

   @AliceHandler
   private void Y(Post var1) {
      if (this.isActive()) {
         if (!(this.tm == super.mc.world)) {
            short var13 = 19738;
         }

         if (super.mc.player != null && super.mc.world != null) {
            if (++this.vR >= 10) {
               this.vR = 0;
               this.aJe.clear();

               for (PlayerEntity var3 : this.Z()) {
                  if (!var3.isInvisibleTo(super.mc.player) && super.mc.player.canSee(var3)) {
                     this.aJe.add(var3.getUuid());
                  }
               }

               this.aJe.add(super.mc.player.getUuid());
            } else if (this.aJe.isEmpty()) {

            } else {
               char var14 = 52581;
            }

            long var11 = System.nanoTime();
            if (this.ace.aKd(var11)) {
               this.warning("C\u00f3 tin ALICE ch\u01b0a x\u00e1c nh\u1eadn sau 30 gi\u00e2y; \u0111\u00e3 b\u1ecf kh\u1ecfi h\u00e0ng \u0111\u1ee3i.", new Object[0]);
            }

            if (this.Qi != (0) && var11 - this.Qi >= (-64771072)) {
               this.qu.clear();
               this.aAn = false;
            }

            if (this.aKh == null) {
               if (var11 < this.sh) {
                  char var15 = 34691;
               } else {
                  char var16 = 34692;
               }

               String var4;
               try {
                  String var5 = this.aa();
                  String var6 = AliceObf_sP.aJF(
                     var5, super.mc.world.getRegistryKey().getValue().toString()
                  );
                  LinkedHashSet var7 = new LinkedHashSet();
                  this.Z().forEach(var1x -> var7.add(var1x.getUuid()));
                  if (super.mc.getNetworkHandler() != null) {
                     for (PlayerListEntry var9 : super.mc.getNetworkHandler().getPlayerList()) {
                        if (var7.size() >= (128)) {
                           break;
                        }

                        var7.add(var9.getProfile().id());
                     }
                  }

                  var4 = AliceObf_sP.aJH(
                     this.ace.aJV(),
                     this.ace.aJW(),
                     super.mc.player.getUuid(),
                     super.mc.player.getName().getString(),
                     AliceObf_sP.aJF(
                        var5, ""
                     ),
                     var6,
                     (Boolean)this.azr.get(),
                     this.azr.get()
                        ? (String)this.aIc.get()
                        : "",
                     List.copyOf(var7),
                     this.ace.aJY(),
                     this.ace.aJZ(),
                     this.ace.aKa()
                  );
               } catch (IllegalArgumentException var10) {
                  this.sh = var11 + (-1294967296);
                  this.ae(var10.getMessage());
                  return;
               }

               long var12 = this.ace.aJX();
               this.sh = var11 + (-1294967296);
               this.aKh = this.Sz
                  .submit(
                     () -> {
                        try {
                           AliceObf_rR var4x = AliceObf_sP.aJI(
                              this.ab(var4)
                           );
                           super.mc.execute(() -> this.ac(var12, var4x));
                        } catch (LinkageError | Exception var5x) {
                           if (var5x instanceof InterruptedException) {
                              Thread.currentThread().interrupt();
                           }

                           super.mc.execute(() -> this.ad(var12));
                        }
                     }
                  );
            }
         }
      }
   }

   private List<PlayerEntity> Z() {
      return super.mc
         .world
         .getPlayers()
         .stream()
         .filter(var1 -> var1.squaredDistanceTo(super.mc.player) <= 16384.0)
         .sorted(Comparator.comparingDouble(var1 -> var1.squaredDistanceTo(super.mc.player)))
         .limit(128)
         .map(var0 -> (PlayerEntity)var0)
         .toList();
   }

   private String aa() {
      if (super.mc.getCurrentServerEntry() == null) {
         return ("" + this.ace.aJV());
      } else {
         ServerAddress var1 = ServerAddress.parse(super.mc.getCurrentServerEntry().address);
         return var1.getAddress().toLowerCase(Locale.ROOT) + var1.getPort();
      }
   }

   private String ab(String var1) throws Exception {
      if (!PremiumAccessGate.isAllowed()) {

      } else {
         short var6 = 8069;
      }

      if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
         if (!(AliceServer.class.getClassLoader() instanceof PayloadRuntimeHost var2)) {
            throw new IllegalStateException("ALICE loader does not support community chat");
         } else {
            return var2.communityRequest(var1);
         }
      } else {
         try {
            Class var10001 = Class.forName(
               "com.nnpg.alice.dev.DevCommunityTransport",
               true,
               AliceServer.class.getClassLoader().getParent()
            );
            String var10002 = "request";
            Class[] var10003 = new Class[(1)];
            var10003[0] = String.class;
            Method var9 = var10001.getMethod(var10002, var10003);
            Object[] var10 = new Object[1];
            var10[(0)] = var1;
            return (String)var9.invoke(null, var10);
         } catch (InvocationTargetException var5) {
            Throwable var4 = var5.getCause();
            if (!(var4 instanceof Exception)) {
               char var8 = 58957;
            } else {
               char var7 = 58956;
            }

            throw var5;
         }
      }
   }

   private void ac(long var1, AliceObf_rR var3) {
      if (this.isActive() && PremiumAccessGate.isAllowed()) {
         if (!(this.tm != super.mc.world)) {
            short var6 = 24884;
         }
      }
   }

   private void ad(long var1) {
      int var10000 = var1 != this.ace.aJX() ? 27467 : 27466;

      while (true) {
         int var3 = var10000;
         if (var3 == 27466) {
            if (this.isActive()) {
               if (!PremiumAccessGate.isAllowed()) {
                  char var4 = 55628;
               } else {
                  char var5 = 55629;
               }

               this.aKh = null;
               this.aAn = false;
               this.aLg = Math.min(4, this.aLg + (1));
               this.sh = System.nanoTime()
                  + Math.min(
                        15L, (3) << this.aLg - (1)
                     )
                     * (1000000000);
               return;
            }
            break;
         }

         if (var3 != 27467) {
            break;
         }

      }
   }

   private void ae(String var1) {
      long var2 = System.nanoTime();
      if (var2 >= this.atv) {
         this.atv = var2 + (-129542144);
         this.warning(
            "%s",
            new Object[]{var1}
         );
      }
   }

   public void send(String var1) {
      if (!this.isActive() || !PremiumAccessGate.isAllowed()) {
         this.info(
            "B\u1eadt module Alice Server trong Misc \u0111\u1ec3 d\u00f9ng /alice.",
            new Object[(0)]
         );
      } else if (var1.isEmpty()) {
         this.info(
            "C\u00fa ph\u00e1p: /alice <n\u1ed9i dung>. \u0110\u1ed5i bi\u1ec7t danh \u1edf hide-chat-name / nickname.",
            new Object[0]
         );
      } else {
         if (super.mc.world != null) {
            int var10000 = super.mc.player == null ? 10703 : 10704;

            while (true) {
               int var3 = var10000;
               if (var3 == 10703) {
                  break;
               }

               if (var3 != 10704) {
                  try {
                     if ((Boolean)this.azr.get()) {
                        AliceObf_sP.aJL((String)this.aIc.get());
                     }

                     this.ace.aKc(var1, System.nanoTime());
                     if (this.aAn) {
                        this.sh = 0L;
                     }
                  } catch (IllegalArgumentException var4) {
                     String var10001 = "%s";
                     Object[] var10002 = new Object[1];
                     var10002[(0)] = var4.getMessage();
                     this.warning(var10001, var10002);
                  }

                  return;
               }

            }
         }

         this.info(
            "H\u00e3y v\u00e0o world tr\u01b0\u1edbc khi chat ALICE.",
            new Object[(0)]
         );
      }
   }

   private AliceObf_fD af(UUID var1) {
      return this.isActive()
            && PremiumAccessGate.isAllowed()
            && this.tm == super.mc.world
            && this.Qi != (0)
            && System.nanoTime() - this.Qi < (-64771072)
         ? (AliceObf_fD)this.qu.get(var1)
         : null;
   }

   public Text nametag(UUID var1, Text var2) {
      AliceObf_fD var3 = this.af(var1);
      return (Text)(this.TD.get() && var3 != null && var3.aJy() && this.aJe.contains(var1)
         ? AliceServer.ag(
            var2.getString(), var3.aJx(), false
         )
         : var2);
   }

   public Text chat(UUID var1, Text var2) {
      if (!(Boolean)this.ayM.get()) {
         return var2;
      } else {
         if (!(var1 == null)) {
            short var5 = 3354;
         }

         AliceObf_fD var3 = this.af(var1);
         return (Text)(var3 == null
            ? var2
            : AliceServer.ag(var3.aJw(), var3.aJx(), true));
      }
   }

   public int color(UUID var1, int var2) {
      AliceObf_fD var3 = this.af(var1);
      return !this.TD.get() || var3 == null || !var3.aJy() || !this.aJe.contains(var1) ? var2 : (var3.aJx() ? (16765286) : 16034507);
   }

   private static MutableText ag(String var0, boolean var1, boolean var2) {
      MutableText var3 = Text.literal(
            var2
               ? "[ALICE] "
               : "ALICE \u273f "
         )
         .withColor(13350650);
      if (var1) {
         var3.append(
            Text.literal("\u265b ")
               .withColor(16765286)
         );
      }

      return var3.append(Text.literal(var0).withColor(var1 ? 16765286 : 16034507));
   }

   public String getInfoString() {
      if (PremiumAccessGate.isAllowed()) {
         return this.aAn
            ? this.qu
                  .values()
                  .stream()
                  .filter(AliceObf_fD::aJy)
                  .filter(var1 -> this.aJe.contains(var1.aJu()))
                  .filter(
                     var1 -> !(super.mc.player != null && var1.aJu().equals(super.mc.player.getUuid()))
                  )
                  .count()
               + ""
            : "Offline";
      } else {
         return null;
      }
   }

   }

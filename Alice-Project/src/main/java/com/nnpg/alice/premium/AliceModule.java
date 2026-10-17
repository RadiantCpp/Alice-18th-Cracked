package com.nnpg.alice.premium;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.listeners.IListener;

public abstract class AliceModule extends Module {
   private final List<IListener> managedListeners = new ArrayList<>();

   public AliceModule(Category var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public final void toggle() {
      if (this.isActive() || PremiumAccessGate.isAllowed()) {
         super.toggle();
      }
   }

   public void onActivate() {
      if (PremiumAccessGate.isAllowed()) {
         this.subscribeHandlers();
      }
   }

   public void onDeactivate() {
      this.unsubscribeHandlers();
   }

   private void subscribeHandlers() {
      this.unsubscribeHandlers();

      for (Class<?> var1 = this.getClass(); var1 != null && var1 != AliceModule.class && var1 != Module.class; var1 = var1.getSuperclass()) {
         for (Method var5 : var1.getDeclaredMethods()) {
            if (var5.isAnnotationPresent(AliceHandler.class) && var5.getParameterCount() == 1 && var5.getReturnType() == void.class) {
               Class<?> var6 = var5.getParameterTypes()[0];
               int var7 = ((AliceHandler)var5.getAnnotation(AliceHandler.class)).priority();

               MethodHandle var8;
               try {
                  var8 = MethodHandles.privateLookupIn(var1, MethodHandles.lookup())
                     .unreflect(var5)
                     .bindTo(this)
                     .asType(MethodType.methodType(void.class, Object.class));
               } catch (IllegalAccessException var10) {
                  throw new IllegalStateException("Unable to bind ALICE event handler " + var5, var10);
               }

               IListener var9 = new IListener() {
                  @Override
                  public void call(Object var1x) {
                     if (!PremiumAccessGate.isAllowed()) return;
                     try {
                        var8.invokeExact(var1x);
                     } catch (Throwable t) {
                        if (t instanceof Error) throw (Error) t;
                     }
                  }

                  @Override
                  public Class<?> getTarget() {
                     return var6;
                  }

                  @Override
                  public int getPriority() {
                     return var7;
                  }

                  @Override
                  public boolean isStatic() {
                     return false;
                  }
               };

               this.managedListeners.add(var9);
               MeteorClient.EVENT_BUS.subscribe(var9);
            }
         }
      }
   }

   private void unsubscribeHandlers() {
      for (IListener var2 : this.managedListeners) {
         MeteorClient.EVENT_BUS.unsubscribe(var2);
      }

      this.managedListeners.clear();
   }
}

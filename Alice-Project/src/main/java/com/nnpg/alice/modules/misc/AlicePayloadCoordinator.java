package com.nnpg.alice.modules.misc;

import com.nnpg.alice.AliceAddon;
import com.nnpg.alice.PremiumModulePersistence;
import com.nnpg.alice.modules.combat.*;
import com.nnpg.alice.modules.misc.*;
import com.nnpg.alice.modules.movement.*;
import com.nnpg.alice.modules.player.*;
import java.util.Iterator;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;

public final class AlicePayloadCoordinator {
   private AlicePayloadCoordinator() {
   }

   public static void init() {
      Modules modules = Modules.get();

      Iterator<Category> it = Modules.loopCategories().iterator();
      while (it.hasNext()) {
         if (it.next() == AliceAddon.FAKE_CATEGORY) {
            it.remove();
         }
      }

      AutoOrder autoOrder = new AutoOrder();
      AliceAutoReconnect autoReconnect = new AliceAutoReconnect();
      AliceServer aliceServer = new AliceServer();
      EasyPlace easyPlace = new EasyPlace();
      AutoTotem autoTotem = new AutoTotem();
      AutoSell autoSell = new AutoSell();
      AhSniper ahSniper = new AhSniper();
      BlazeShopDroper blazeShopDroper = new BlazeShopDroper();
      OrderDropper orderDropper = new OrderDropper();
      HopperSetup hopperSetup = new HopperSetup();
      CrafterSetup crafterSetup = new CrafterSetup();
      GrimCombatCoordinator grimCombatCoordinator = new GrimCombatCoordinator();
      GuiHelper guiHelper = new GuiHelper();
      GuiMovePlusPlus guiMove = new GuiMovePlusPlus();
      AnchorMacro anchorMacro = new AnchorMacro();
      AnchorSafe anchorSafe = new AnchorSafe();
      AnchorAuraPlusPlus anchorAura = new AnchorAuraPlusPlus();
      CrystalAuraPlusPlus crystalAura = new CrystalAuraPlusPlus();
      CrystalLegit crystalLegit = new CrystalLegit();
      ShieldDisable shieldDisable = new ShieldDisable();
      TriggerBot triggerBot = new TriggerBot();
      KeyPearl keyPearl = new KeyPearl();
      FreezePlayer freezePlayer = new FreezePlayer();
      VelocityPlusPlus velocity = new VelocityPlusPlus();
      KillAuraPlusPlus killAura = new KillAuraPlusPlus();
      KnockbackDirection knockbackDirection = new KnockbackDirection();
      Criticals criticals = new Criticals();
      TargetStrafe targetStrafe = new TargetStrafe();
      StrafePlusPlus strafe = new StrafePlusPlus();
      SpeedMinePlusPlus speedMine = new SpeedMinePlusPlus();
      AutoMinePlusPlus autoMine = new AutoMinePlusPlus();
      AntiPhasePlusPlus antiPhase = new AntiPhasePlusPlus();
      Phase phase = new Phase();
      AutoShop autoShop = new AutoShop();
      AutoExpPlusPlus autoExp = new AutoExpPlusPlus();
      BaseDiggerPlusPlus baseDigger = new BaseDiggerPlusPlus();
      Surround surround = new Surround();
      SelfTrap selfTrap = new SelfTrap();
      ScaffoldPlusPlus scaffold = new ScaffoldPlusPlus();
      AutoSex autoSex = new AutoSex();
      AutoSexWeb autoSexWeb = new AutoSexWeb();
      Thudam thudam = new Thudam();
      PearlChase pearlChase = new PearlChase();
      PlayerHud playerHud = new PlayerHud();
      CombatStats combatStats = new CombatStats();
      HotbarRefill hotbarRefill = new HotbarRefill();

      modules.add(autoOrder);
      modules.add(autoReconnect);
      modules.add(aliceServer);
      modules.add(easyPlace);
      modules.add(autoTotem);
      modules.add(autoSell);
      modules.add(ahSniper);
      modules.add(blazeShopDroper);
      modules.add(orderDropper);
      modules.add(hopperSetup);
      modules.add(crafterSetup);
      modules.add(grimCombatCoordinator);
      modules.add(guiHelper);
      modules.add(guiMove);
      modules.add(anchorMacro);
      modules.add(anchorSafe);
      modules.add(anchorAura);
      modules.add(crystalAura);
      modules.add(crystalLegit);
      modules.add(shieldDisable);
      modules.add(triggerBot);
      modules.add(keyPearl);
      modules.add(freezePlayer);
      modules.add(velocity);
      modules.add(killAura);
      modules.add(knockbackDirection);
      modules.add(criticals);
      modules.add(targetStrafe);
      modules.add(strafe);
      modules.add(speedMine);
      modules.add(autoMine);
      modules.add(antiPhase);
      modules.add(phase);
      modules.add(autoShop);
      autoShop.toggle();
      modules.add(autoExp);
      modules.add(baseDigger);
      modules.add(surround);
      modules.add(selfTrap);
      modules.add(scaffold);
      modules.add(autoSex);
      modules.add(autoSexWeb);
      modules.add(thudam);
      modules.add(pearlChase);
      modules.add(playerHud);
      if (!playerHud.isActive()) {
         playerHud.toggle();
      }
      modules.add(combatStats);
      modules.add(hotbarRefill);

      PremiumModulePersistence.restore(modules,
         autoOrder, autoReconnect, aliceServer, easyPlace, autoTotem, autoSell,
         ahSniper, blazeShopDroper, orderDropper, hopperSetup, crafterSetup,
         grimCombatCoordinator, guiHelper, guiMove, anchorMacro, anchorSafe,
         anchorAura, crystalAura, crystalLegit, shieldDisable, triggerBot,
         keyPearl, freezePlayer, velocity, killAura, knockbackDirection,
         criticals, targetStrafe, strafe, speedMine, autoMine, antiPhase,
         phase, autoShop, autoExp, baseDigger, surround, selfTrap, scaffold,
         autoSex, autoSexWeb, thudam, pearlChase, playerHud, combatStats, hotbarRefill
      );

      if (!playerHud.isActive()) {
         playerHud.toggle();
      }
   }

   public static void aYQ() {
      init();
   }
}

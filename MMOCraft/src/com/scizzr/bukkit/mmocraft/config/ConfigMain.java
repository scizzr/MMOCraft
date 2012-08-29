package com.scizzr.bukkit.mmocraft.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class ConfigMain extends JavaPlugin {
    static YamlConfiguration config = new YamlConfiguration();
    
    static boolean changed = false;
    
    MMOCraft plugin;
    
    public ConfigMain (MMOCraft plugin) {
        this.plugin = plugin;
    }
    
    public static void main() {
        File file = new File(MMOCraft.fileFolder + MMOCraft.slash + "config.yml");
        
        if (!file.exists()) {
            try {
                file.createNewFile();
                MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("succeedyml", new Object[] {file.getName()}));
            } catch (IOException ex) {
                MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("failedyml", new Object[] {file.getName()}));
                MMOCraft.suicide(ex);
            }
        }
        
        load();
    }
    
    static void load() {
        File file = MMOCraft.fileConfigMain;
        
        try {
            config.load(file);
        } catch (Exception ex) {
            MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("failedload", new Object[] {file.getName()}));
            MMOCraft.suicide(ex);
        }
        
//Config - Main
        checkOption(config, "general.prefix", Config.genPrefix);                            Config.genPrefix = config.getBoolean("general.prefix");
        checkOption(config, "general.stats", Config.genStats);                              Config.genStats = config.getBoolean("general.stats");
        checkOption(config, "general.vercheck", Config.genVerCheck);                        Config.genVerCheck = config.getBoolean("general.vercheck");
        checkOption(config, "general.autoupdate", Config.genAutoUpdate);                    Config.genAutoUpdate = config.getBoolean("general.autoupdate");
        checkOption(config, "general.errorweb", Config.genErrorWeb);                        Config.genErrorWeb = config.getBoolean("general.errorweb");
        checkOption(config, "general.autosave", Config.genAutoSave);                        Config.genAutoSave = config.getBoolean("general.autosave");
        checkOption(config, "general.locale", Config.genLocale);                            Config.genLocale = config.getString("general.locale");
        
        checkOption(config, "economy.enabled", Config.econEnabled);                         Config.econEnabled = config.getBoolean("economy.enabled");
        
        checkOption(config, "permissions.allowops", Config.permAllowOps);                   Config.permAllowOps = config.getBoolean("permissions.allowops");
        
//Config - General
        checkOption(config, "alter.damage", Config.alterDamage);                            Config.alterDamage = config.getBoolean("alter.damage");
        
//Config - Classes
      //Archer
        checkOption(config, "archer.damage.class", Config.arcDmgClass);                     Config.arcDmgClass = config.getInt("archer.damage.class");
        checkOption(config, "archer.damage.other", Config.arcDmgOther);                     Config.arcDmgOther = config.getInt("archer.damage.other");
        checkOption(config, "archer.skills.flamingarrow.lvl", Config.arcSklFlaArrLvl);      Config.arcSklFlaArrLvl = config.getInt("archer.skills.flamingarrow.lvl");
        checkOption(config, "archer.skills.flamingarrow.cd", Config.arcSklFlaArrCd);        Config.arcSklFlaArrCd = config.getInt("archer.skills.flamingarrow.cd");
        checkOption(config, "archer.skills.arrowrain.lvl", Config.arcSklArrRaiLvl);         Config.arcSklArrRaiLvl = config.getInt("archer.skills.arrowrain.lvl");
        checkOption(config, "archer.skills.arrowrain.cd", Config.arcSklArrRaiCd);           Config.arcSklArrRaiCd = config.getInt("archer.skills.arrowrain.cd");
        checkOption(config, "archer.skills.arrowring.lvl", Config.arcSklArrRinLvl);         Config.arcSklArrRinLvl = config.getInt("archer.skills.arrowring.lvl");
        checkOption(config, "archer.skills.arrowring.cd", Config.arcSklArrRinCd);           Config.arcSklArrRinCd = config.getInt("archer.skills.arrowring.cd");
        //checkOption(config, "archer.skills.XXXXXX.lvl", Config.arcSklXXXXXXLvl);            Config.arcSklXXXXXXLvl = config.getInt("archer.skills.XXXXXX.lvl");
        //checkOption(config, "archer.skills.XXXXXX.cd", Config.arcSklXXXXXXCd);              Config.arcSklXXXXXXCd = config.getInt("archer.skills.XXXXXX.cd");
        checkOption(config, "archer.skills.turret.lvl", Config.arcSklTurretLvl);            Config.arcSklTurretLvl = config.getInt("archer.skills.turret.lvl");
        checkOption(config, "archer.skills.turret.cd", Config.arcSklTurretCd);              Config.arcSklTurretCd = config.getInt("archer.skills.turret.cd");
//
        //checkOption(config, "archer.skills.XXXXXX.lvl", Config.arcSklXXXXXXLvl);            Config.arcSklXXXXXXLvl = config.getInt("archer.skills.XXXXXX.lvl");
        //checkOption(config, "archer.skills.XXXXXX.cd", Config.arcSklXXXXXXCd);              Config.arcSklXXXXXXCd = config.getInt("archer.skills.XXXXXX.cd");
        
        
      //Assassin
        checkOption(config, "assassin.damage.class", Config.assDmgClass);                   Config.assDmgClass = config.getInt("assassin.damage.class");
        checkOption(config, "assassin.damage.other", Config.assDmgOther);                   Config.assDmgOther = config.getInt("assassin.damage.other");
        checkOption(config, "assassin.skills.powerup.lvl",  Config.assSklPoweUpLvl);        Config.assSklPoweUpLvl = config.getInt("assassin.skills.powerup.lvl");
        checkOption(config, "assassin.skills.powerup.cd", Config.assSklPoweUpCd);           Config.assSklPoweUpCd = config.getInt("assassin.skills.powerup.cd");
        checkOption(config, "assassin.skills.stealth.lvl", Config.assSklStealtLvl);         Config.assSklStealtLvl = config.getInt("assassin.skills.stealth.lvl");
        checkOption(config, "assassin.skills.stealth.cd", Config.assSklStealtCd);           Config.assSklStealtCd = config.getInt("assassin.skills.stealth.cd");
        checkOption(config, "assassin.skills.shadowstep.lvl", Config.assSklShStepLvl);      Config.assSklShStepLvl = config.getInt("assassin.skills.shadowstep.lvl");
        checkOption(config, "assassin.skills.shadowstep.cd", Config.assSklShStepCd);        Config.assSklShStepCd = config.getInt("assassin.skills.shadowstep.cd");
        checkOption(config, "assassin.skills.stab.lvl", Config.assSklStabLvl);              Config.assSklStabLvl = config.getInt("assassin.skills.stab.lvl");
        checkOption(config, "assassin.skills.stab.cd", Config.assSklStabCd);                Config.assSklStabCd = config.getInt("assassin.skills.stab.cd");
        checkOption(config, "assassin.skills.sigil.lvl", Config.assSklSigilLvl);            Config.assSklSigilLvl = config.getInt("assassin.skills.sigil.lvl");
        checkOption(config, "assassin.skills.sigil.cd", Config.assSklSigilCd);              Config.assSklSigilCd = config.getInt("assassin.skills.sigil.cd");
        checkOption(config, "assassin.skills.sneakattack.lvl", Config.assSklSneAttLvl);     Config.assSklSneAttLvl = config.getInt("assassin.skills.sneakattack.lvl");
        checkOption(config, "assassin.skills.sneakattack.cd", Config.assSklSneAttCd);       Config.assSklSneAttCd = config.getInt("assassin.skills.sneakattack.cd");
        
        
      //Barbarian
        checkOption(config, "barbarian.damage.class", Config.barDmgClass);                  Config.barDmgClass = config.getInt("barbarian.damage.class");
        checkOption(config, "barbarian.damage.other", Config.barDmgOther);                  Config.barDmgOther = config.getInt("barbarian.damage.other");
        checkOption(config, "barbarian.skills.toughskin.lvl", Config.barSklTouSkiLvl);      Config.barSklTouSkiLvl = config.getInt("barbarian.skills.toughskin.lvl");
        checkOption(config, "barbarian.skills.toughskin.cd", Config.barSklTouSkiCd);        Config.barSklTouSkiCd = config.getInt("barbarian.skills.toughskin.cd");
        checkOption(config, "barbarian.skills.leap.lvl", Config.barSklLeapLvl);             Config.barSklLeapLvl = config.getInt("barbarian.skills.leap.lvl");
        checkOption(config, "barbarian.skills.leap.cd", Config.barSklLeapCd);               Config.barSklLeapCd = config.getInt("barbarian.skills.leap.cd");
        checkOption(config, "barbarian.skills.axewhirl.lvl", Config.barSklAxeWhiLvl);       Config.barSklAxeWhiLvl = config.getInt("barbarian.skills.axewhirl.lvl");
        checkOption(config, "barbarian.skills.axewhirl.cd", Config.barSklAxeWhiCd);         Config.barSklAxeWhiCd = config.getInt("barbarian.skills.axewhirl.cd");
//
        //checkOption(config, "barbarian.skills.XXXXXX.lvl", Config.barSklXXXXXXLvl);         Config.barSklXXXXXXLvl = config.getInt("barbarian.skills.XXXXXX.lvl");
        //checkOption(config, "barbarian.skills.XXXXXX.cd", Config.barSklXXXXXXCd);           Config.barSklXXXXXXCd = config.getInt("barbarian.skills.XXXXXX.cd");
        checkOption(config, "barbarian.skills.beacon.lvl", Config.barSklBeaconLvl);         Config.barSklBeaconLvl = config.getInt("barbarian.skills.beacon.lvl");
        checkOption(config, "barbarian.skills.beacon.cd", Config.barSklBeaconCd);           Config.barSklBeaconCd = config.getInt("barbarian.skills.beacon.cd");
//
        //checkOption(config, "barbarian.skills.XXXXXX.lvl", Config.barSklXXXXXXLvl);         Config.barSklXXXXXXLvl = config.getInt("barbarian.skills.XXXXXX.lvl");
        //checkOption(config, "barbarian.skills.XXXXXX.cd", Config.barSklXXXXXXCd);           Config.barSklXXXXXXCd = config.getInt("barbarian.skills.XXXXXX.cd");
        
        
      //Druid
        checkOption(config, "druid.damage.class", Config.druDmgClass);                      Config.druDmgClass = config.getInt("druid.damage.class");
        checkOption(config, "druid.damage.other", Config.druDmgOther);                      Config.druDmgOther = config.getInt("druid.damage.other");
//
        //checkOption(config, "druid.skills.XXXXXX.lvl", Config.druSklXXXXXXLvl);             Config.druSklXXXXXXLvl = config.getInt("druid.skills.XXXXXX.lvl");
        //checkOption(config, "druid.skills.XXXXXX.cd", Config.druSklXXXXXXCd);               Config.druSklXXXXXXCd = config.getInt("druid.skills.XXXXXX.cd");
//
        //checkOption(config, "druid.skills.XXXXXX.lvl", Config.druSklXXXXXXLvl);             Config.druSklXXXXXXLvl = config.getInt("druid.skills.XXXXXX.lvl");
        //checkOption(config, "druid.skills.XXXXXX.cd", Config.druSklXXXXXXCd);               Config.druSklXXXXXXCd = config.getInt("druid.skills.XXXXXX.cd");
//
        //checkOption(config, "druid.skills.XXXXXX.lvl", Config.druSklXXXXXXLvl);             Config.druSklXXXXXXLvl = config.getInt("druid.skills.XXXXXX.lvl");
        //checkOption(config, "druid.skills.XXXXXX.cd", Config.druSklXXXXXXCd);               Config.druSklXXXXXXCd = config.getInt("druid.skills.XXXXXX.cd");
//
        //checkOption(config, "druid.skills.XXXXXX.lvl", Config.druSklXXXXXXLvl);             Config.druSklXXXXXXLvl = config.getInt("druid.skills.XXXXXX.lvl");
        //checkOption(config, "druid.skills.XXXXXX.cd", Config.druSklXXXXXXCd);               Config.druSklXXXXXXCd = config.getInt("druid.skills.XXXXXX.cd");
//
        //checkOption(config, "druid.skills.XXXXXX.lvl", Config.druSklXXXXXXLvl);             Config.druSklXXXXXXLvl = config.getInt("druid.skills.XXXXXX.lvl");
        //checkOption(config, "druid.skills.XXXXXX.cd", Config.druSklXXXXXXCd);               Config.druSklXXXXXXCd = config.getInt("druid.skills.XXXXXX.cd");
//
        //checkOption(config, "druid.skills.XXXXXX.lvl", Config.druSklXXXXXXLvl);             Config.druSklXXXXXXLvl = config.getInt("druid.skills.XXXXXX.lvl");
        //checkOption(config, "druid.skills.XXXXXX.cd", Config.druSklXXXXXXCd);               Config.druSklXXXXXXCd = config.getInt("druid.skills.XXXXXX.cd");
        
        
      //Necromancer
        checkOption(config, "necromancer.damage.class", Config.necDmgClass);                Config.necDmgClass = config.getInt("necromancer.damage.class");
        checkOption(config, "necromancer.damage.other", Config.necDmgOther);                Config.necDmgOther = config.getInt("necromancer.damage.other");
        checkOption(config, "necromancer.skills.toughskin.lvl", Config.necSklLifTapLvl);    Config.necSklLifTapLvl = config.getInt("necromancer.skills.toughskin.lvl");
        checkOption(config, "necromancer.skills.toughskin.cd", Config.necSklLifTapCd);      Config.necSklLifTapCd = config.getInt("necromancer.skills.toughskin.cd");
        checkOption(config, "necromancer.skills.sumpigzombie.lvl", Config.necSklSumPigLvl); Config.necSklSumPigLvl = config.getInt("necromancer.skills.sumpigzombie.lvl");
        checkOption(config, "necromancer.skills.sumpigzombie.cd", Config.necSklSumPigCd);   Config.necSklSumPigCd = config.getInt("necromancer.skills.sumpigzombie.cd");
        checkOption(config, "necromancer.skills.sumspider.lvl", Config.necSklSumSpiLvl);    Config.necSklSumSpiLvl = config.getInt("necromancer.skills.sumspider.lvl");
        checkOption(config, "necromancer.skills.sumspider.cd", Config.necSklSumSpiCd);      Config.necSklSumSpiCd = config.getInt("necromancer.skills.sumspider.cd");
        checkOption(config, "necromancer.skills.sumblaze.lvl", Config.necSklSumBlaLvl);     Config.necSklSumBlaLvl = config.getInt("necromancer.skills.sumblaze.lvl");
        checkOption(config, "necromancer.skills.sumblaze.cd", Config.necSklSumBlaLvl);      Config.necSklSumBlaLvl = config.getInt("necromancer.skills.sumblaze.cd");
        checkOption(config, "necromancer.skills.beacon.lvl", Config.necSklTotemLvl);        Config.necSklTotemLvl = config.getInt("necromancer.skills.beacon.lvl");
        checkOption(config, "necromancer.skills.beacon.cd", Config.necSklTotemCd);          Config.necSklTotemCd = config.getInt("necromancer.skills.beacon.cd");
//
        //checkOption(config, "necromancer.skills.XXXXXX.lvl", Config.necSklXXXXXXLvl);       Config.necSklXXXXXXLvl = config.getInt("necromancer.skills.XXXXXX.lvl");
        //checkOption(config, "necromancer.skills.XXXXXX.cd", Config.necSklXXXXXXCd);         Config.necSklXXXXXXCd = config.getInt("necromancer.skills.XXXXXX.cd");
        
        
      //Wizard
        checkOption(config, "wizard.damage.class", Config.wizDmgClass);                     Config.wizDmgClass = config.getInt("wizard.damage.class");
        checkOption(config, "wizard.damage.other", Config.wizDmgOther);                     Config.wizDmgOther = config.getInt("wizard.damage.other");
        checkOption(config, "wizard.skills.meteor.lvl", Config.wizSklMeteorLvl);            Config.wizSklMeteorLvl = config.getInt("wizard.skills.meteor.lvl");
        checkOption(config, "wizard.skills.meteor.cd", Config.wizSklMeteorCd);              Config.wizSklMeteorCd = config.getInt("wizard.skills.meteor.cd");
        checkOption(config, "wizard.skills.lightning.lvl", Config.wizSklLightnLvl);         Config.wizSklLightnLvl = config.getInt("wizard.skills.lightning.lvl");
        checkOption(config, "wizard.skills.lightning.cd", Config.wizSklLightnCd);           Config.wizSklLightnCd = config.getInt("wizard.skills.lightning.cd");
        checkOption(config, "wizard.skills.fireball.lvl", Config.wizSklFirebaLvl);          Config.wizSklFirebaLvl = config.getInt("wizard.skills.fireball.lvl");
        checkOption(config, "wizard.skills.fireball.cd", Config.wizSklFirebaCd);            Config.wizSklFirebaCd = config.getInt("wizard.skills.fireball.cd");
//
        //checkOption(config, "wizard.skills.XXXXXX.lvl", Config.wizSklXXXXXXLvl);            Config.wizSklXXXXXXLvl = config.getInt("wizard.skills.XXXXXX.lvl");
        //checkOption(config, "wizard.skills.XXXXXX.cd", Config.wizSklXXXXXXCd);              Config.wizSklXXXXXXCd = config.getInt("wizard.skills.XXXXXX.cd");
        checkOption(config, "wizard.skills.trap.lvl", Config.wizSklTrapLvl);                Config.wizSklTrapLvl = config.getInt("wizard.skills.trap.lvl");
        checkOption(config, "wizard.skills.trap.cd", Config.wizSklTrapCd);                  Config.wizSklTrapCd = config.getInt("wizard.skills.trap.cd");
//
        //checkOption(config, "wizard.skills.XXXXXX.lvl", Config.wizSklXXXXXXLvl);            Config.wizSklXXXXXXLvl = config.getInt("wizard.skills.XXXXXX.lvl");
        //checkOption(config, "wizard.skills.XXXXXX.cd", Config.wizSklXXXXXXCd);              Config.wizSklXXXXXXCd = config.getInt("wizard.skills.XXXXXX.cd");
        
        
        
        MMOCraft.prefix = (Config.genPrefix == true) ? MMOCraft.prefixMain : "";
        
        if (changed) {
            config.options().header(
                "Base Configuration - Main"
            );
            try { config.save(file); } catch (Exception ex) { MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("failedsave", new Object[] {file.getName()}));
                MMOCraft.suicide(ex);
            }
        }
    }
    
    static void checkOption(YamlConfiguration config, String node, Object def) {
        if (!config.isSet(node)) {
            config.set(node, def);
            changed = true;
        }
    }
    
    static void editOption(YamlConfiguration config, String nodeOld, String nodeNew) {
        if (config.isSet(nodeOld)) {
            if (nodeNew != null) {
                config.set(nodeNew, config.get(nodeOld));
            }
            config.set(nodeOld, null);
            changed = true;
        }
    }
}

package com.scizzr.bukkit.mmocraft.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.mmocraft.Main;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class ConfigMain extends JavaPlugin {
    static File file = new File(Main.fileFolder + "config.yml");
    static YamlConfiguration config = new YamlConfiguration();
    
    static boolean changed = false;
    
    Main plugin;
    
    public ConfigMain (Main plugin) {
        this.plugin = plugin;
    }
    
    public static void main() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                Main.log.info(Main.prefixConsole + I18n._("succeedyml", new Object[] {file.getName()}));
            } catch (IOException ex) {
                Main.log.info(Main.prefixConsole + I18n._("failedyml", new Object[] {file.getName()}));
                Main.suicide(ex);
            }
        }
        
        load();
    }
    
    static void load() {
        
        File file = Main.fileConfigMain;
        
        try {
            config.load(file);
        } catch (Exception ex) {
            Main.log.info(Main.prefixConsole + I18n._("failedload", new Object[] {file.getName()}));
            Main.suicide(ex);
        }
        
//        editOption(config, "parent.child.grandchild", "parent2.child.grandchild");
//        editOption(config, "parent", null);
        
        checkOption(config, "general.prefix", Config.genPrefix);                Config.genPrefix = config.getBoolean("general.prefix");
        checkOption(config, "general.stats", Config.genStats);                  Config.genStats = config.getBoolean("general.stats");
        checkOption(config, "general.vercheck", Config.genVerCheck);            Config.genVerCheck = config.getBoolean("general.vercheck");
        
        checkOption(config, "economy.enabled", Config.econEnabled);             Config.econEnabled = config.getBoolean("economy.enabled");
        
        checkOption(config, "permissions.allowops", Config.permAllowOps);       Config.permAllowOps = config.getBoolean("permissions.allowops");
        
//XXX TESTING
        checkOption(config, "damage.alter", Config.damageAlter);                Config.damageAlter = config.getBoolean("damage.alter");
        
        Main.prefix = (Config.genPrefix == true) ? Main.prefixMain : "";
        
        if (changed) {
            config.options().header(
                "Base Configuration - Main"
            );
            try { config.save(file); } catch (Exception ex) { Main.log.info(Main.prefixConsole + I18n._("failedsave", new Object[] {file.getName()}));
                Main.suicide(ex);
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

package com.scizzr.bukkit.plugins.mmocraft.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.plugins.mmocraft.Main;

public class ConfigMain extends JavaPlugin {
    File file = new File(getDataFolder() + "configMain.yml");
    
    static boolean changed = false;
    
    Main plugin;
    
    public ConfigMain (Main plugin) {
        this.plugin = plugin;
    }
    
    public static void main() {
        File file = new File(Main.fileConfigMain.getAbsolutePath());
        
        if (!file.exists()) {
            try {
                file.createNewFile();
                Main.log.info(Main.prefixConsole + "Blank configMain.yml created");
            } catch (IOException ex) {
                Main.log.info(Main.prefixConsole + "Failed to make configMain.yml");
                Main.suicide(ex);
            }
        }
        
        load();
    }
    
    static void load() {
        YamlConfiguration config = new YamlConfiguration();
        File file = Main.fileConfigMain;
        
        try {
            config.load(file);
        } catch (Exception ex) {
            Main.log.info(Main.prefixConsole + "There was a problem loading configMain.yml");
            Main.suicide(ex);
        }
        
//        editOption(config, "parent.child.grandchild", "parent2.child.grandchild");
//        editOption(config, "parent", null);
        
        checkOption(config, "general.prefix", Config.genPrefix);                Config.genPrefix = config.getBoolean("general.prefix");
        checkOption(config, "general.stats", Config.genStats);                  Config.genStats = config.getBoolean("general.stats");
        checkOption(config, "general.vercheck", Config.genVerCheck);            Config.genVerCheck = config.getBoolean("general.vercheck");
        
        checkOption(config, "economy.enabled", Config.econEnabled);             Config.econEnabled = config.getBoolean("economy.enabled");
        
        checkOption(config, "permissions.allowops", Config.permAllowOps);       Config.permAllowOps = config.getBoolean("permissions.allowops");
        
        Main.prefix = (Config.genPrefix == true) ? Main.prefixMain : "";
        
        if (changed) {
            config.options().header(
                "Base Configuration - Main"
            );
            try { config.save(file); } catch (Exception ex) { Main.log.info(Main.prefixConsole + "Failed to save configMain.yml");
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

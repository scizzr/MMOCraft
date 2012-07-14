package com.scizzr.bukkit.plugins.mmocraft.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;

public class PlayerData {
    static YamlConfiguration config = new YamlConfiguration();
    
    public static boolean load() {
        if(!Main.filePlayerData.exists()) {
            try {
                Main.filePlayerData.createNewFile();
                Main.log.info(Main.prefixConsole + "Blank playerData.yml created");
                return true;
            } catch (Exception ex) {
                Main.log.info(Main.prefixConsole + "Failed to make playerData.yml");
                Main.suicide(ex);
                return false;
            }
        } else {
            try {
                config.load(Main.filePlayerData);
                return true;
            } catch (Exception ex) { Main.suicide(ex); return false; }
        }
    }
    
    public static void save() {
        try {
            config.save(Main.filePlayerData);
        } catch (Exception ex) { Main.suicide(ex); }
    }
    
    public static void setOpt(Player p, String o, Object v) {
        config.set(p.getName() + "." + o, v);
    }
    
    public static Object getOpt(Player p, String o) {
        String val = config.getString(p.getName() + "." + o);
        
        return val != null ? val : null;
    }
    
    public static void unsetOpt(Player p, String o) {
        config.set(p.getName() + "." + o, null);
    }
    
    public static void checkAll(Player p) {
        try {
            config.load(Main.filePlayerData);
            
/*
            checkOption(config, p, "eff-pot-self", "true");
            checkOption(config, p, "eff-pot-other", "true");
*/
            
            checkOption(config, p, "class", null);
            checkOption(config, p, "experience", 0);
            
            ClassManager.setClass(p, (String)getOpt(p, "class"));
            
            config.save(Main.filePlayerData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    static void checkOption(YamlConfiguration config, Player p, String opt, Object def) {
        if (!config.isSet(p.getName() + "." + opt)) {
            config.set(p.getName() + "." + opt, def);
            try { config.save(Main.filePlayerData); } catch (Exception ex) { Main.log.info(Main.prefixConsole + "Failed to save playerData.yml"); Main.suicide(ex); }
        }
    }
    
    static void editOption(YamlConfiguration config, Player p, String nodeOld, String nodeNew) {
        if (config.isSet(p.getName() + "." + nodeOld)) {
            if (nodeNew != null) {
                config.set(p.getName() + "." + nodeNew, config.get(p.getName() + "." + nodeOld));
            }
            config.set(nodeOld, null);
            try { config.save(Main.filePlayerData); } catch (Exception ex) { p.sendMessage(Main.prefix + "Failed to save playerData.yml"); Main.suicide(ex); }
        }
    }
}

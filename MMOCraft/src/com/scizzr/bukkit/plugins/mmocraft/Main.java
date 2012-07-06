package com.scizzr.bukkit.plugins.mmocraft;

import java.io.File;
import java.util.Calendar;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.plugins.mmocraft.classes.Archer;
import com.scizzr.bukkit.plugins.mmocraft.classes.Wizard;
import com.scizzr.bukkit.plugins.mmocraft.config.Config;
import com.scizzr.bukkit.plugins.mmocraft.config.ConfigMain;
import com.scizzr.bukkit.plugins.mmocraft.config.PlayerData;
import com.scizzr.bukkit.plugins.mmocraft.listeners.Blocks;
import com.scizzr.bukkit.plugins.mmocraft.listeners.Entities;
import com.scizzr.bukkit.plugins.mmocraft.listeners.Players;
import com.scizzr.bukkit.plugins.mmocraft.managers.CheatManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;
import com.scizzr.bukkit.plugins.mmocraft.threads.Errors;
import com.scizzr.bukkit.plugins.mmocraft.threads.Meteor;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreString;
import com.scizzr.bukkit.plugins.mmocraft.util.Vault;
import com.scizzr.bukkit.plugins.mmocraft.threads.Stats;
import com.scizzr.bukkit.plugins.mmocraft.threads.Update;

public class Main extends JavaPlugin {
    public static Logger log = Logger.getLogger("Minecraft");
    public static PluginDescriptionFile info;
    public static PluginManager pm;
    public static Plugin plugin;
    
    public static String prefixConsole, prefixMain, prefix;
    
    static int exTimer = 0;
    
    boolean isScheduled = false;
    int lastTick;
    
    public static File fileFolder, fileConfigMain, filePlayerData;
    
    public static YamlConfiguration config;
    
    public static String osN = System.getProperty("os.name").toLowerCase();
    public static String os = (osN.contains("windows") ? "Windows" :
        (osN.contains("linux")) ? "Linux" :
            (osN.contains("mac")) ? "Macintosh" :
                osN);
    
    public static String slash = os.equalsIgnoreCase("Windows") ? "\\" : "/";
    
    public static File filePlug = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("\\", "/"));
    public static String jar = filePlug.getAbsolutePath().split("/")[filePlug.getAbsolutePath().split("/").length - 1];
    
    public static int calY, calM, calD, calH, calI, calS;
    public static String calA;
    
    boolean noSpeed = false;
    
    Player meteorPlayer = null;
    
    public void onEnable() {
        info = getDescription();
        
        prefixConsole = "[" + info.getName() + "] ";
        prefixMain = ChatColor.LIGHT_PURPLE + "[" + ChatColor.YELLOW + info.getName() + ChatColor.LIGHT_PURPLE + "]" + ChatColor.RESET + " ";
        
        pm = getServer().getPluginManager();
        
        final Blocks listenerBlocks = new Blocks(this); pm.registerEvents(listenerBlocks, this);
        final Entities listenerEntities = new Entities(this); pm.registerEvents(listenerEntities, this);
        final Players listenerPlayers = new Players(this); pm.registerEvents(listenerPlayers, this);
        
        plugin = pm.getPlugin(info.getName());
        
        fileFolder = getDataFolder();
        
        if (!fileFolder.exists()) {
            log.info(prefixConsole + "Creating our plugin's folder.");
            try {
                fileFolder.mkdir();
            } catch (Exception ex) {
                log.info(prefixConsole + "Error making our plugin's folder.");
                suicide(ex);
            }
        }
        
        fileConfigMain = new File(getDataFolder() + slash + "config.yml");
        ConfigMain.main();
        
        filePlayerData = new File(getDataFolder() + slash + "playerData.yml");
        PlayerData.load();
        
        // + Extra initialization stuff
        ClassManager.main(); Wizard.main(); Archer.main();
        // - Extra initialization stuff
        
        Vault.setupPermissions();
        log.info(prefixConsole + "Permissions has been setup.");
        
        Vault.setupEconomy();
        log.info(prefixConsole + "Economy has been setup.");
        
        new Thread(new Stats()).start();
        
        if (Config.genVerCheck == true) {
            new Thread(new Update("check", null, null)).start();
        }
        
        if (!isScheduled) {
            isScheduled = true;
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this,
                    new Runnable() {
                        public void run() {
                            Calendar cal = Calendar.getInstance();
                            calY = cal.get(Calendar.YEAR);
                            calM = cal.get(Calendar.MONTH) + 1;
                            calD = cal.get(Calendar.DAY_OF_MONTH);
                            calH = cal.get(Calendar.HOUR);
                            calI = cal.get(Calendar.MINUTE);
                            calS = cal.get(Calendar.SECOND);
                            calA = cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
                            
                            if (lastTick % 20 == 0) {
                                lastTick = 0;
                                
                                new Thread(new ArrowTimer("countdown", null, null)).start();
                                
                                if (meteorPlayer != null) {
                                    new Thread(new Meteor(meteorPlayer)).start();
                                }
                                
                                if (calS == 0) {
                                    try { CheatManager.resetClicks(); } catch (Exception ex) { /* suicide(ex); */ }
                                }
                                
                                Wizard.flipTraps(); Archer.flipTurrets();
                                
                                if (calS % 2 == 0) {
                                    Archer.fireTurrets();
                                }
                            }
                            
                            if (lastTick % 10 == 0) {
                                
                            }
                            
                            if (lastTick % 10 == 0) {
                                
                            }
                            
                            if (lastTick % 2 == 0) {
                                
                            }
                            
                            if (lastTick % 1 == 0) {
                                try { SkillManager.tickCooldown(); } catch (Exception ex) { /* suicide(ex); */ }
                            }
                            
                            lastTick++;
                        }
                    }, 0L, 1L);
        }
        
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData.checkAll(p);
        }
        
        log.info(prefixConsole + "Plugin Enabled");
    }
    
    public void onDisable() {
        log.info(prefixConsole + "Plugin disabled.");
    }
    
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            PlayerCommand.onCommand((Player)sender, command, commandLabel, args);
        } else {
            SystemCommand.onCommand(sender, command, commandLabel, args);
            
        }
        
        return true;
    }
    
    public static void suicide(Exception ex) {
        int i = 60;
        
        if (Config.genErrorWeb == true) {
            if (exTimer == 0) {
                new Thread(new Errors(MoreString.stackToString(ex))).start();
                log.info(prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                log.info(prefixConsole + "You submitted a stack trace for further review. Thank");
                log.info(prefixConsole + "you for enabling this as it allows me to fix problems.");
                log.info(prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                exTimer = i;
            } else {
                log.info(prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                log.info(prefixConsole + "An error occurred but it was not posted to my website");
                log.info(prefixConsole + "because you recently posted one " + (i-exTimer) + " seconds ago.");
                log.info(prefixConsole + "If errors continue to occur, please post a message on");
                log.info(prefixConsole + "this page: http://dev.bukkit.org/server-mods/" + info.getName().toLowerCase());
                log.info(prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            }
        } else {
            ex.printStackTrace();
        }
    }
}

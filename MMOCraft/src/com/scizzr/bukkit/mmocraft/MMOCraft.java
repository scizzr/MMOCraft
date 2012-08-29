package com.scizzr.bukkit.mmocraft;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.server.EntityTypes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.config.ConfigMain;
import com.scizzr.bukkit.mmocraft.config.Data;
import com.scizzr.bukkit.mmocraft.custommobs.CustomZombie;
import com.scizzr.bukkit.mmocraft.hooks.HookNCP;
import com.scizzr.bukkit.mmocraft.hooks.HookVault;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.listeners.Blocks;
import com.scizzr.bukkit.mmocraft.listeners.Entities;
import com.scizzr.bukkit.mmocraft.listeners.Players;
import com.scizzr.bukkit.mmocraft.managers.AidMgr;
import com.scizzr.bukkit.mmocraft.managers.CmdMgr;
import com.scizzr.bukkit.mmocraft.managers.MobMgr;
import com.scizzr.bukkit.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.threads.Errors;
import com.scizzr.bukkit.mmocraft.threads.Meteor;
import com.scizzr.bukkit.mmocraft.threads.Stats;
import com.scizzr.bukkit.mmocraft.threads.Update;
import com.scizzr.bukkit.mmocraft.timers.ArrowTimer;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class MMOCraft extends JavaPlugin {
    private I18n i18n;
    public static Logger log = Logger.getLogger("Minecraft");
    public static PluginDescriptionFile info;
    public static PluginManager pm;
    public static Plugin plugin;
    
    public static String prefixConsole, prefixMain, prefix;
    
    static int exTimer;
    
    boolean isScheduled = false;
    int lastTick;
    
    public static File fileFolder, fileConfigMain, filePlayerClasses, filePlayerAids, filePlayerPets;
    
    public static YamlConfiguration config;
    
    public static String osN = System.getProperty("os.name").toLowerCase();
    public static String os = (osN.contains("windows") ? "Windows" :
        (osN.contains("linux")) ? "Linux" :
            (osN.contains("mac")) ? "Macintosh" :
                osN);
    
    public static String slash = os.equalsIgnoreCase("Windows") ? "\\" : "/";
    
    public static File filePlug = new File(MMOCraft.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("\\", "/"));
    public static String jar = filePlug.getAbsolutePath().split("/")[filePlug.getAbsolutePath().split("/").length - 1];
    
    public static int calY, calM, calD, calH, calI, calS;
    public static String calA;
    
    boolean noSpeed = false;
    
    Player meteorPlayer = null;
    
    public static String host = "http://www.scizzr.com/";
    
    public void onEnable() {
        info = getDescription();
        
        i18n = new I18n(this); i18n.onEnable();
        
        prefixConsole = "[" + info.getName() + "] ";
        prefixMain = ChatColor.LIGHT_PURPLE + "[" + ChatColor.YELLOW + info.getName() + ChatColor.LIGHT_PURPLE + "]" + ChatColor.RESET + " ";
        
        pm = getServer().getPluginManager();
        plugin = pm.getPlugin(info.getName());
        fileFolder = getDataFolder();
        
        exTimer = 0;
        
/*
        int mon = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        
        if (!(mon <= 8 && day <= 27 && year <= 2012)) {
            log.info(prefixConsole + "-----------------------------------------");
            log.info(prefixConsole + "Plugin disabled. Beta period #2 is over.");
            log.info(prefixConsole + "The beta period ended August 27th, 2012.");
            log.info(prefixConsole + "-----------------------------------------");
            pm.disablePlugin(pm.getPlugin(info.getName())); return;
        }
*/
        
        final Blocks listenerBlocks = new Blocks(this); pm.registerEvents(listenerBlocks, this);
        final Entities listenerEntities = new Entities(this); pm.registerEvents(listenerEntities, this);
        final Players listenerPlayers = new Players(this); pm.registerEvents(listenerPlayers, this);
        final HookNCP hookNCP = new HookNCP(this); pm.registerEvents(hookNCP, this);
        
        if (!fileFolder.exists()) {
            log.info(prefixConsole + I18n._("creatingpluginfolder", new Object[] {}));
            try {
                fileFolder.mkdir();
            } catch (Exception ex) {
                log.info(prefixConsole + I18n._("errormakingfolder", new Object[] {}));
                ex.printStackTrace();
            }
        }
        
        fileConfigMain = new File(getDataFolder() + slash + "config.yml");
        filePlayerClasses = new File(getDataFolder() + slash + "playerClasses.yml");
        filePlayerAids = new File(getDataFolder() + slash + "playerAids.yml");
        filePlayerPets = new File(getDataFolder() + slash + "playerPets.yml");
        
//Unnecessary synchronization??
        //synchronized(this) { 
// + Extra initialization stuff and load things from files
            ConfigMain.main(); Data.load();
//            i18n.updateLocale(Config.locale);
            CmdMgr.main(); AidMgr.main(); PetMgr.main();
            MobMgr.main();
// - Extra initialization stuff and load things from files
        //}
        
        HookVault.setupPermissions();
        log.info(prefixConsole + I18n._("donesetupperms", new Object[] {}));
        
        HookVault.setupEconomy();
        log.info(prefixConsole + I18n._("donesetupecon", new Object[] {}));
        
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
                            
                            if (meteorPlayer != null) {
                                new Thread(new Meteor(meteorPlayer)).start();
                            }
                            
                            RaceMgr.tickStill();
                        }
                        
                        if (lastTick % 10 == 0) {
                            
                        }
                        
                        if (lastTick % 5 == 0) {
                            
                        }
                        
                        if (lastTick % 2 == 0) {
                            
                        }
                        
                        if (lastTick % 1 == 0) {
                            try { 
                                new Thread(new ArrowTimer("countdown", null, null)).start();
                                //new Thread(new FireballTimer("countdown", null, null)).start();
                                SkillMgr.tickCooldown();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            AidMgr.progressAids();
                        }
                        
                        lastTick++;
                    }
                }, 0L, 1L);
            
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this,
                    new Runnable() {
                        public void run() {
                            if (Config.genAutoSave) {
                                Data.save("all");
                            }
                        }
                    }
            , 0L, 300L);
        }
        
        for (Player p : Bukkit.getOnlinePlayers()) {
            Race race = RaceMgr.getRace(p.getName());
            if (race == null) { RaceMgr.setRace(p.getName(), "None"); }
        }
        
        try{
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = Integer.TYPE;
             
            Method a = EntityTypes.class.getDeclaredMethod("a", args);
            
            a.setAccessible(true);
            
            a.invoke(a, new Object[] { CustomZombie.class, "Zombie", Integer.valueOf(54) });
        } catch (Exception ex) {
            ex.printStackTrace();
            this.setEnabled(false);
        }
        
        log.info(prefixConsole + I18n._("pluginenabled", new Object[] {}));
    }
    
    public void onDisable() {
// Force save to pickup any changes that happened
        Data.save("all");
// Remove the pet entities such to prevent stray entities
        List<World> worlds = Bukkit.getWorlds();
        
        synchronized(worlds) {
            Iterator<World> itWorlds = worlds.iterator();
            
            while (itWorlds.hasNext()) {
                World world = itWorlds.next();
                List<Pet> pets = PetMgr.getAllPets();
                
                synchronized (pets) {
                    Iterator<Pet> itPets = pets.iterator();
                    
                    while (itPets.hasNext()) {
                        Pet pet = itPets.next();
                        List<Entity> ents = world.getEntities();
                        
                        synchronized(ents) {
                            Iterator<Entity> itEnts = ents.iterator();
                            
                            while (itEnts.hasNext()) {
                                Entity ent = itEnts.next();
                                
                                if (ent.getUniqueId() == pet.getUUID()) {
                                    ent.remove();
                                }
                            }
                        }
                        
                    }
                }
            }
        }
        
        log.info(prefixConsole + I18n._("plugindisabled", new Object[] {}));
    }
    
    public boolean onCommand(CommandSender s, Command cmd, String cmdLbl, String[] args) {
        CmdMgr.execute(s, cmd, cmdLbl, args); return true;
    }
    
    public static void suicide(Exception ex) {
        int i = 60;
        
        if (Config.genErrorWeb == true) {
            if (exTimer == 0) {
                new Thread(new Errors(Util.stackToString(ex))).start();
                log.info(prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                log.info(prefixConsole + I18n._("stackokA", new Object[] {}));
                log.info(prefixConsole + I18n._("stackokB", new Object[] {}));
                log.info(prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                exTimer = i;
            } else {
                log.info(prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                log.info(prefixConsole + I18n._("stacknotA", new Object[] {}));
                log.info(prefixConsole + I18n._("stacknotB", new Object[] {i-exTimer}));
                log.info(prefixConsole + I18n._("stacknotC", new Object[] {}));
                log.info(prefixConsole + I18n._("stacknotD", new Object[] {info.getName().toLowerCase()}));
                log.info(prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            }
        } else {
            ex.printStackTrace();
        }
    }
}

package com.scizzr.bukkit.mmocraft.managers;

import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.classes.Archer;
import com.scizzr.bukkit.mmocraft.classes.Assassin;
import com.scizzr.bukkit.mmocraft.classes.Barbarian;
import com.scizzr.bukkit.mmocraft.classes.Druid;
import com.scizzr.bukkit.mmocraft.classes.Necromancer;
import com.scizzr.bukkit.mmocraft.classes.None;
import com.scizzr.bukkit.mmocraft.classes.Wizard;
import com.scizzr.bukkit.mmocraft.custommobs.CustomZombie;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.skills.AssassinStealth;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class RaceMgr {
    private static int maxLvl = 50, expMult = 1;
    
    private static CopyOnWriteArrayList<Race> races = new CopyOnWriteArrayList<Race> ();
    
    public static Race getRace(String name) {
        for (Race race : races) {
            if (race.getPlayerName().equals(name)) {
                return race;
            }
        }
        return null;
    }
    
    public static void setRace(String name, String which) {
        Race race = null;
        if (which.equalsIgnoreCase("none")) {        race = new None(); }
        if (which.equalsIgnoreCase("archer")) {      race = new Archer(); }
        if (which.equalsIgnoreCase("assassin")) {    race = new Assassin(); }
        if (which.equalsIgnoreCase("barbarian")) {   race = new Barbarian(); }
        if (which.equalsIgnoreCase("druid")) {       race = new Druid(); }
        if (which.equalsIgnoreCase("necromancer")) { race = new Necromancer(); }
        if (which.equalsIgnoreCase("wizard")) {      race = new Wizard(); }
        if (race != null) {
            race.setPlayerName(name);
            
            //TODO : Config for 'share EXP amongst all classes'
            //Currently ON
            if (getRace(name) != null) {
                race.setExp(getExp(name));
                races.remove(getRace(name));
            }
            //END
            races.add(race);
            
            AidMgr.removeAllPlayerAids(name);
            PetMgr.removeAllPlayerPets(name, true);
        }
    }
    
    public static String getRaceName(String name) {
        Race race = getRace(name);
        return race != null ? race.getName() : null;
    }
    
    public static String getRaceNameColored(String name) {
        Race race = getRace(name);
        return race != null ? race.getColor() + race.getName() + ChatColor.RESET : null;
    }
    
    public static String getRaceNameProper(String name) {
        Race race = getRace(name);
        return race != null ? getArticle(race) + " " + race.getName() : null;
    }
    
    public static String getRaceNameColoredProper(String name) {
        Race race = getRace(name);
        return race != null ? getArticle(race) + " " + race.getColor() + race.getName() + ChatColor.RESET : null;
    }
    
    public static String getArticle(Race race) {
        if (race != null) {
            if (race instanceof Archer|| race instanceof Assassin) { return "an"; }
            return "a";
        }
        return "";
    }
    
    public static void resetRace(String name) {
        if (getRace(name) != null) { races.remove(getRace(name)); }
    }
    
    public static int getExp(String name) {
        return getRace(name).getExp();
    }
    
    public static void addExp(String name, int xp) {
        addExp(name, xp, I18n._("expgained", new Object[] {xp, I18n._("exp", new Object[] {})}));
    }
    
    public static void addExp(String name, int xp, String why) {
        int old = getRace(name).getExp();
        
        if (getLevel(old) >= maxLvl) { return; }
        
        getRace(name).setExp(old+xp);
        
        Player p = EntityMgr.getOnlinePlayer(name);
        if (p != null) {
            if (getLevel(old) != getLevel(old+xp)) {
                if (getLevel(old+xp) == maxLvl) {
                    p.getWorld().strikeLightningEffect(p.getLocation().clone().add(0, 10, 0));
                }
                
                SoundEffects.RANDOM_LEVELUP.play(p, p.getLocation());
                p.setHealth(20); p.setFoodLevel(20);
                
                p.sendMessage(MMOCraft.prefix + ChatColor.AQUA + I18n._("lvlup", new Object[] {getLevel(old+xp)}));
            } else {
                p.sendMessage(MMOCraft.prefix + String.format(why, xp));
            }
        }
    }
    
    public static void setExp(String name, int xp) {
        getRace(name).setExp(xp);
    }
    
    public static int getLevel(int exp) {
        for (int i = 1; i <= maxLvl; i++) {
            int res = (int) (50*(Math.pow(i, 2))) + (100*i);
            if (res > exp) { return i; }
        }
        return maxLvl;
    }
    
    public static int getNextLevel(int exp) {
        for (int i = 1; i <= maxLvl; i++) {
            int res = (int) (50*(Math.pow(i, 2))) + (100*i);
            if (res > exp) { if (i < maxLvl) { return i+1; } else { return maxLvl; } }
        }
        return 0;
    }
    
    public static int getNextExp(int exp) {
        int i = getLevel(exp);
        int res = (int) (50*(Math.pow(i, 2))) + (100*i);
        return (i+1 <= maxLvl ? res - exp : 0);
    }
    
    public static void slayExp(String name, Entity eDead) {
        final Player p = Bukkit.getPlayer(name);
        
        int exp = 0;
    // Passive
        if (eDead instanceof Chicken) {     exp = expMult*  4; }
        if (eDead instanceof Cow) {         exp = expMult* 10; }
        if (eDead instanceof MushroomCow) { exp = expMult* 10; }
        if (eDead instanceof Ocelot) {      exp = expMult* 10; }
        if (eDead instanceof Pig) {         exp = expMult* 10; }
        if (eDead instanceof Sheep) {       exp = expMult*  8; }
        if (eDead instanceof Squid) {       exp = expMult* 10; }
        if (eDead instanceof Villager) {    exp = expMult* 20; }
    // Neutral
        if (eDead instanceof Enderman) {    exp = expMult* 40; }
        if (eDead instanceof Wolf) {        exp = expMult*  8; }
        if (eDead instanceof PigZombie) {   exp = expMult* 20; }
    // Hostile
        if (eDead instanceof Blaze) {       exp = expMult* 20; }
        if (eDead instanceof CaveSpider) {  exp = expMult* 12; }
        if (eDead instanceof Creeper) {     exp = expMult* 20; }
        if (eDead instanceof Ghast) {       exp = expMult* 10; }
        if (eDead instanceof MagmaCube) {   exp = expMult* 16; }
        if (eDead instanceof Silverfish) {  exp = expMult*  8; }
        if (eDead instanceof Skeleton) {    exp = expMult* 20; }
        if (eDead instanceof Slime) {       exp = expMult* 16; }
        if (eDead instanceof Spider) {      exp = expMult* 16; }
        if (eDead instanceof Zombie) {      exp = expMult* 20; }
    // Utility
        if (eDead instanceof Snowman) {     exp = expMult*  6; }
        if (eDead instanceof IronGolem) {   exp = expMult*100; }
    // Bosses & Unused
        if (eDead instanceof Giant) {       exp = expMult*100; }
        if (eDead instanceof EnderDragon) { exp = expMult*200; }
    // Player
        if (eDead instanceof Player) {      exp = expMult*  5; }
    // Custom Mobs
        if (eDead instanceof CustomZombie) {exp = (int)(expMult*1.5); Bukkit.broadcastMessage("CustomZombie"); }
        
        for (int i = 0; i <= exp; i++) {
            final int count = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                public void run() {
                    SoundEffects.RANDOM_CLICK.play(p, p.getLocation(), 0.5f, 0.8f);
                }
            }, count*2);
        }
        
        addExp(name, exp, I18n._("expgainedwhy", new Object[] {exp, I18n._("exp", new Object[] {}), eDead.getType().getName()}));
    }
    
    public static void tickStill() {
        int neededSecs = 10;
        
        for (Race race : races) {
            if (race instanceof Assassin) {
                OfflinePlayer ofp = EntityMgr.getOfflinePlayer(race.getPlayerName());
                if (ofp.isOnline()) {
                    Player p = EntityMgr.getOnlinePlayer(ofp.getName());
                    Location loc = p.getLocation();
                    
                    if (race.hasData("invis")) { continue; }
                    if (race.hasData("lastInvisLoc")) {
                        Location locLast = Util.stringToLocation(race.getData("lastInvisLoc"), ":");
                        if (Util.cmpLoc(loc, locLast)) { continue; }
                    }
                    
                    int still = race.hasData("secondsStill") ? Integer.valueOf(race.getData("secondsStill")) : 0;
                    
                    if (still == 0) {
                        race.setData("secondsStill", 1);
                        race.setData("secondsLoc", Util.locationToString(loc, ":", ":"));
                    } else if (still >= neededSecs) {
                        new AssassinStealth().execute(p, null, 0);
                        race.setData("secondsStill", null);
                        race.setData("lastInvisLoc", Util.locationToString(loc, ":", ":"));
                    } else {
                        Location old = Util.stringToLocation(race.getData("secondsLoc"), ":");
                        if (Util.cmpLoc(loc, old)) {
                            race.setData("secondsStill", still+1);
                        } else {
                            race.setData("secondsStill", 0);
                            race.setData("secondsLoc", Util.locationToString(loc, ":", ":"));
                        }
                    }
                }
            }
        }
    }
    
    public static CopyOnWriteArrayList<Race> get() {
        return races;
    }
    
    public static void put(CopyOnWriteArrayList<Race> racesNew) {
        races = racesNew;
    }
}

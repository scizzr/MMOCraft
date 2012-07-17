package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
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

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.classes.Archer;
import com.scizzr.bukkit.plugins.mmocraft.classes.Assassin;
import com.scizzr.bukkit.plugins.mmocraft.classes.Barbarian;
import com.scizzr.bukkit.plugins.mmocraft.classes.Druid;
import com.scizzr.bukkit.plugins.mmocraft.classes.Necromancer;
import com.scizzr.bukkit.plugins.mmocraft.classes.None;
import com.scizzr.bukkit.plugins.mmocraft.classes.Wizard;
import com.scizzr.bukkit.plugins.mmocraft.enums.Colors;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;

public class RaceMgr {
    private static int maxLvl = 50, expMult = 10;
    
    //TODO : Private!
    public static ConcurrentHashMap<Race, Boolean> players = new ConcurrentHashMap<Race, Boolean> ();
    
    public static Race getRace(Player p) {
        for (Entry<Race, Boolean> entry : players.entrySet()) {
            Race race = entry.getKey();
            if (race.getPlayer().equals(p.getName())) {
                return race;
            }
        }
        return null;
    }
    
    public static void setRace(Player p, String s) {
        Race race = null;
        if (s.equalsIgnoreCase("none")) {        race = new None(); }
        if (s.equalsIgnoreCase("archer")) {      race = new Archer(); }
        if (s.equalsIgnoreCase("assassin")) {    race = new Assassin(); }
        if (s.equalsIgnoreCase("barbarian")) {   race = new Barbarian(); }
        if (s.equalsIgnoreCase("druid")) {       race = new Druid(); }
        if (s.equalsIgnoreCase("necromancer")) { race = new Necromancer(); }
        if (s.equalsIgnoreCase("wizard")) {      race = new Wizard(); }
        if (race != null) {
            race.setPlayer(p.getName());
            //TODO : Config for 'share EXP amongst all classes'
            //Currently ON
            if ("a" == "a" && getRace(p) != null) {
                race.setExp(getExp(p));
                players.remove(getRace(p));
            }
            players.put(race, true);
            p.sendMessage(Main.prefix + Colors.INFO + "Your class has been set to " + RaceMgr.getRaceNameColored(p));
        } else {
            p.sendMessage(Main.prefix + Colors.ERROR + "That is not a valid class.");
        }
    }
    
    public static String getRaceName(Player p) {
        Race race = getRace(p);
        return race != null ? race.getName() : null;
    }
    
    public static String getRaceNameColored(Player p) {
        Race race = getRace(p);
        return race != null ? race.getColor() + race.getName() + ChatColor.RESET : null;
    }
    
    public static String getRaceNameProper(Player p) {
        Race race = getRace(p);
        return race != null ? getArticle(race) + " " + race.getName() : null;
    }
    
    public static String getRaceNameColoredProper(Player p) {
        Race race = getRace(p);
        return race != null ? getArticle(race) + " " + race.getColor() + race.getName() + ChatColor.RESET : null;
    }
    
    public static String getArticle(Race race) {
        if (race != null) {
            if (race instanceof Archer|| race instanceof Assassin) { return "an"; }
            return "a";
        }
        return "";
    }
    
    public static void resetRace(Player p) {
        if (getRace(p) != null) { players.remove(getRace(p)); }
    }
    
    public static int getExp(Player p) {
        return getRace(p).getExp();
    }
    
    public static void addExp(Player p, int xp) {
        addExp(p, xp, String.format("You gained %s XP", xp));
    }
    
    public static void addExp(Player p, int xp, String why) {
        int old = getRace(p).getExp();
        
        if (getLevel(old) >= maxLvl) { return; }
        
        getRace(p).setExp(old+xp);
        
        if (getLevel(old) != getLevel(old+xp)) {
            if (getLevel(old+xp) == maxLvl) {
                p.getWorld().strikeLightningEffect(p.getLocation().clone().add(0, 10, 0));
            }
            
            p.getWorld().playEffect(p.getLocation().clone().add(0.5, 2, 0.5), Effect.POTION_BREAK, 1);
            p.setHealth(20); p.setFoodLevel(20);
            
            p.sendMessage(Main.prefix + Colors.INFO + "Congrats! You are now level " + getLevel(old+xp));
        } else {
            p.sendMessage(Main.prefix + String.format(why, xp));
        }
    }
    
    public static void setExp(Player p, int xp) {
        getRace(p).setExp(xp);
        p.sendMessage(Main.prefix + "New EXP: " + xp);
        p.sendMessage(Main.prefix + "New level: " + getLevel(xp));
    }
    
    public static int getLevel(int exp) {
        for (int i = 1; i <= maxLvl; i++) {
            int res = (int) (50*(Math.pow(i, 2))) + (75*i);
            if (res > exp) { return i; }
        }
        return maxLvl;
    }
    
    public static int getNextLevel(int exp) {
        for (int i = 1; i <= 50; i++) {
            int res = (int) (50*(Math.pow(i, 2))) + (75*i);
            if (res > exp) { if (i < maxLvl) { return i+1; } else { return maxLvl; } }
        }
        return 0;
    }
    
    public static int getNextExp(int exp) {
        int i = getLevel(exp);
        int res = (int) (50*(Math.pow(i, 2))) + (75*i);
        return (i+1 <= maxLvl ? res - exp : 0);
    }
    
    public static void slayExp(Player eKill, Entity eDead) {
        int exp = 0;
    // Passive
        if (eDead instanceof Chicken) {     exp = expMult*4; }
        if (eDead instanceof Cow) {         exp = expMult*10; }
        if (eDead instanceof MushroomCow) { exp = expMult*10; }
        if (eDead instanceof Ocelot) {      exp = expMult*10; }
        if (eDead instanceof Pig) {         exp = expMult*10; }
        if (eDead instanceof Sheep) {       exp = expMult*8; }
        if (eDead instanceof Squid) {       exp = expMult*10; }
        if (eDead instanceof Villager) {    exp = expMult*20; }
    // Neutral
        if (eDead instanceof Enderman) {    exp = expMult*40; }
        if (eDead instanceof Wolf) {        exp = expMult*8; }
        if (eDead instanceof PigZombie) {   exp = expMult*20; }
    // Hostile
        if (eDead instanceof Blaze) {       exp = expMult*20; }
        if (eDead instanceof CaveSpider) {  exp = expMult*12; }
        if (eDead instanceof Creeper) {     exp = expMult*20; }
        if (eDead instanceof Ghast) {       exp = expMult*10; }
        if (eDead instanceof MagmaCube) {   exp = expMult*16; }
        if (eDead instanceof Silverfish) {  exp = expMult*8; }
        if (eDead instanceof Skeleton) {    exp = expMult*20; }
        if (eDead instanceof Slime) {       exp = expMult*16; }
        if (eDead instanceof Spider) {      exp = expMult*16; }
//        if (eDead instanceof Jockey) {      exp = mod*36; }
        if (eDead instanceof Zombie) {      exp = expMult*20; }
    // Utility
        if (eDead instanceof Snowman) {     exp = expMult*6; }
        if (eDead instanceof IronGolem) {   exp = expMult*100; }
    // Bosses & Unused
        if (eDead instanceof Giant) {       exp = expMult*100; }
        if (eDead instanceof EnderDragon) { exp = expMult*200; }
    // Player
        if (eDead instanceof Player) {      exp = expMult*5; }
        
        addExp(eKill, exp, "You earned %s XP for killing a " + eDead.getType().getName());
    }
    
    public static boolean load() {
        File file = new File(Main.filePlayerClasses.getAbsolutePath());
        if (!Main.filePlayerClasses.exists()) {
            try {
                file.createNewFile();
                Main.log.info(Main.prefixConsole + "Blank playerClasses.yml created");
            } catch (Exception ex) {
                Main.log.info(Main.prefixConsole + "Failed to make playerClasses.yml");
                Main.suicide(ex);
            }
        }
        ConcurrentHashMap<Race, Boolean> mapTmp = players;
        try {
            players.clear();
            BufferedReader reader = new BufferedReader(new FileReader(Main.filePlayerClasses));
            String line = reader.readLine();
            
            while (line != null) {
                String[] valueA = line.split(";");
                if (valueA.length != 2) { continue; }
                    String play = valueA[0];
                    String info = valueA[1];
                    String[] valueB = info.split(":");
                    if (valueB.length != 2) { continue; }
                        String name = valueB[0];
                        int exp = Integer.valueOf(valueB[1]);
                
                Race race = null;
                if (name.equalsIgnoreCase("none")) { race = new None(); }
                if (name.equalsIgnoreCase("archer")) { race = new Archer(); }
                if (name.equalsIgnoreCase("assassin")) { race = new Assassin(); }
                if (name.equalsIgnoreCase("barbarian")) { race = new Barbarian(); }
                if (name.equalsIgnoreCase("druid")) { race = new Druid(); }
                if (name.equalsIgnoreCase("necromancer")) { race = new Necromancer(); }
                if (name.equalsIgnoreCase("wizard")) { race = new Wizard(); }
                
                if (race != null) {
                    race.setPlayer(play); race.setExp(exp);
                    players.put(race, true);
                }
                
                line = reader.readLine();
            }
            
            return true;
        } catch (Exception ex) {
            players = mapTmp;
            ex.printStackTrace();
            //Main.suicide(ex);
            return false;
        }
    }
    
    public static boolean save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Main.filePlayerClasses));
            for (Entry<Race, Boolean> entry : players.entrySet()) {
                Race race = entry.getKey();
                String play = race.getPlayer();
                String name = race.getName();
                int exp = race.getExp();
                
                writer.write(play + ";" + name + ":" + exp);
                writer.newLine();
            }
            writer.close();
            return true;
        } catch (Exception ex) {
            //Main.suicide(ex);
            ex.printStackTrace();
            return false;
        }
    }
}

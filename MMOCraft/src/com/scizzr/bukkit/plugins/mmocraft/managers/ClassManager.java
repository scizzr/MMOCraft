package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.HashMap;

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
import com.scizzr.bukkit.plugins.mmocraft.config.PlayerData;

public class ClassManager {
    private static int maxLvl = 50;
    
    private static HashMap<Player, String> classes = new HashMap<Player, String> ();
    private static HashMap<String, String> classColor = new HashMap<String, String> ();
    
    public static void main() {
        classColor.put("archer",        "c");
        classColor.put("assassin",      "8");
        classColor.put("barbarian",     "6");
        classColor.put("druid",         "2");
        classColor.put("necromancer",   "3");
        classColor.put("wizard",        "5");
    }
    
    public static void setClass(Player p, String c) {
        classes.put(p, c);
        PlayerData.setOpt(p, "class", c);
    }
    
    public static void resetClass(Player p) {
        classes.remove(p);
        PlayerData.unsetOpt(p, "class");
    }
    
    public static String getClass(Player p) {
        return classes.containsKey(p) ? classes.get(p) : null;
    }
    
    public static String getClassProper(Player p) {
        String c = classes.containsKey(p) ? classes.get(p) : null;
        
        if (c.equalsIgnoreCase("archer") || c.equalsIgnoreCase("assassin")) {
            return "an";
        } else {
            return "a";
        }
    }

    public static String getClassColored(Player p) {
        String c = classes.containsKey(p) ? classes.get(p) : null;
        String cc = "§";
        
        if (c == null) { return "none"; }
        
        return cc + classColor.get(c) + c + "§r";
    }
    
    public static int getExp(Player p) {
        return Integer.valueOf((String) PlayerData.getOpt(p, "experience"));
    }
    
    public static void addExp(Player p, int xp) {
        addExp(p, xp, "You gained " + xp + " XP");
    }
    
    public static void addExp(Player p, int xp, String why) {
        int old = Integer.valueOf((String) PlayerData.getOpt(p, "experience"));
        PlayerData.setOpt(p, "experience", getExp(p) + xp);
        if (getLevel(old) != getLevel(old+xp)) {
            if (getLevel(old+xp) == maxLvl) {
                p.getWorld().strikeLightningEffect(p.getLocation().clone().add(0, 10, 0));
            } else {
                p.getWorld().playEffect(p.getLocation().clone(), Effect.MOBSPAWNER_FLAMES, 1);
            }
            p.setHealth(20); p.setFoodLevel(20);
            p.sendMessage(Main.prefix + "You are now level " + getLevel(old+xp));
        } else {
            p.sendMessage(Main.prefix + String.format(why, xp));
        }
    }
    
    public static int getLevel(int exp) {
        for (int i = 1; i <= 50; i++) {
            int res = (int) (150*(Math.pow(i, 2))) + (1050*i);
            if (res > exp) { return i; }
        }
        return 50;
    }
    
    public static int getNextLevel(int exp) {
        for (int i = 1; i <= 50; i++) {
            int res = (int) (150*(Math.pow(i, 2))) + (1050*i);
            if (res > exp) { if (i < maxLvl) { return i+1; } else { return maxLvl; } }
        }
        return 0;
    }
    
    public static int getNextExp(int exp) {
        int lvl = getLevel(exp);
        return (lvl+1 <= maxLvl ? (int) (150*(Math.pow(lvl, 2))) + (1050*lvl) - exp : 0);
    }
    
    public static void slayExp(Player eKill, Entity eDead) {
    // EXP modifier
        int mod = 10, exp = 0;
    // Passive
        if (eDead instanceof Chicken) {     exp = mod*4; }
        if (eDead instanceof Cow) {         exp = mod*10; }
        if (eDead instanceof MushroomCow) { exp = mod*10; }
        if (eDead instanceof Ocelot) {      exp = mod*10; }
        if (eDead instanceof Pig) {         exp = mod*10; }
        if (eDead instanceof Sheep) {       exp = mod*8; }
        if (eDead instanceof Squid) {       exp = mod*10; }
        if (eDead instanceof Villager) {    exp = mod*20; }
    // Neutral
        if (eDead instanceof Enderman) {    exp = mod*40; }
        if (eDead instanceof Wolf) {        exp = mod*8; }
        if (eDead instanceof PigZombie) {   exp = mod*20; }
    // Hostile
        if (eDead instanceof Blaze) {       exp = mod*20; }
        if (eDead instanceof CaveSpider) {  exp = mod*12; }
        if (eDead instanceof Creeper) {     exp = mod*20; }
        if (eDead instanceof Ghast) {       exp = mod*10; }
        if (eDead instanceof MagmaCube) {   exp = mod*16; }
        if (eDead instanceof Silverfish) {  exp = mod*8; }
        if (eDead instanceof Skeleton) {    exp = mod*20; }
        if (eDead instanceof Slime) {       exp = mod*16; }
        if (eDead instanceof Spider) {      exp = mod*16; }
//        if (eDead instanceof Jockey) {      exp = mod*20; }
        if (eDead instanceof Zombie) {      exp = mod*20; }
    // Utility
        if (eDead instanceof Snowman) {     exp = mod*6; }
        if (eDead instanceof IronGolem) {   exp = mod*100; }
    // Bosses & Unused
        if (eDead instanceof Giant) {       exp = mod*100; }
        if (eDead instanceof EnderDragon) { exp = mod*200; }
    // Player
        if (eDead instanceof Player) {      exp = mod*5; }
        
        addExp(eKill, exp, "You earned %s XP for killing a " + eDead.getType().getName());
    }
}

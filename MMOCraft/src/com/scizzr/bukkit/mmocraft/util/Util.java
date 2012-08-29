package com.scizzr.bukkit.mmocraft.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.prefs.Preferences;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;

public class Util {
  //Miscellaneous methods
    public static boolean classWep(Race race, int itemID) {
        //Archer
        List<Integer> arc = Arrays.asList(261);
        if (race.getName().equalsIgnoreCase("archer")) {      return(arc.contains(itemID)); }
        
        //Assassin
        List<Integer> ass = Arrays.asList(268,283,272,267,276);
        if (race.getName().equalsIgnoreCase("assassin")) {    return(ass.contains(itemID)); }
        
        //Barbarian
        List<Integer> bar = Arrays.asList(271,286,275,258,279);
        if (race.getName().equalsIgnoreCase("barbarian")) {   return(bar.contains(itemID)); }
        
        //Druid
        List<Integer> dru = Arrays.asList(340);
        if (race.getName().equalsIgnoreCase("druid")) {       return(dru.contains(itemID)); }
        
        //Necromancer
        List<Integer> nec = Arrays.asList(352);
        if (race.getName().equalsIgnoreCase("necromancer")) { return(nec.contains(itemID)); }
        
        //Wizard
        List<Integer> wiz = Arrays.asList(280);
        if (race.getName().equalsIgnoreCase("wizard")) {      return(wiz.contains(itemID)); }
        
        return false;
    }
    
    public static String getUniqID() {
        String path = "com.scizzr.bukkit.shared.UUID";
        
        if (Preferences.userRoot().get(path, null) == null) {
            Preferences.userRoot().put(path, UUID.randomUUID().toString());
        }
        
        return Preferences.userRoot().get(path, null);
    }
    
    public List<Player> getNearbyPlayers(Location loc, int range) {
        return getNearbyPlayers(loc, range, null);
    }
    
    public static List<Player> getNearbyPlayers(Location loc, int range, Entity ignore) {
        CopyOnWriteArrayList<Player> near = new CopyOnWriteArrayList<Player>();
        
        synchronized(near) {
            for (Entity ent : loc.getWorld().getEntities()) {
                if (ent instanceof Player) {
                    Player p = (Player)ent;
                    if (p.getLocation().distance(loc) <= range) {
                        if (ignore != null && ignore == p) {
                            near.add(p);
                        }
                    }
                }
            }
        }
        
        return near;
    }
    
    
    
    
    
    
    
    
    
    
    //Math Methods
    public static boolean between(double num, double low, double hi) {
        return between(num, low, hi, false);
    }
    
    public static boolean between(double num, double low, double hi, boolean inclusive) {
        if (inclusive) {
            if (low <= num && num <= hi) {
                return true;
            }
            return false;
        } else {
            if (low < num && num < hi) {
                return true;
            }
            return false;
        }
    }
    
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static Double setDec(Double d, Integer n) {
        String s = "";
        for (int i = 0; i < n; i++) { s+= "#"; }
        
        DecimalFormat decForm = new DecimalFormat("#." + s);
        Double r = Double.valueOf(decForm.format(d));
        
        return r;
    }
    
    public static int angleToPitch(Integer angle) {
        if ((angle - 90) <= 180) return (angle - 90) * -1;
        else return 180 - (angle + 90);
    }
    
    public static int calcBaseDamage(ItemStack items) {
        int id = items.getTypeId();
        HashMap<Integer, Integer> dmgs = new HashMap<Integer, Integer>();
        //Shovels
        dmgs.put(269, 1); dmgs.put(284, 1); dmgs.put(273, 2); dmgs.put(256, 3); dmgs.put(277, 4);
        //Picks
        dmgs.put(270, 2); dmgs.put(285, 2); dmgs.put(274, 3); dmgs.put(257, 4); dmgs.put(278, 5);
        //Axes
        dmgs.put(271, 3); dmgs.put(286, 3); dmgs.put(275, 4); dmgs.put(258, 5); dmgs.put(279, 6);
        //Swords
        dmgs.put(268, 4); dmgs.put(283, 4); dmgs.put(272, 5); dmgs.put(267, 6); dmgs.put(276, 7);
        //everything else is 1 dmg!
        return dmgs.containsKey(id) ? dmgs.get(id) : 1;
    }
    
    public static int calcDamage(Race race, int dmg2, boolean classWep) {
        int dmg = 1;
        
        if (race.getName().equalsIgnoreCase("archer")) {      dmg = dmg2 + (classWep ? Config.arcDmgClass : Config.arcDmgOther); }
        if (race.getName().equalsIgnoreCase("assassin")) {    dmg = dmg2 + (classWep ? Config.assDmgClass : Config.assDmgOther); }
        if (race.getName().equalsIgnoreCase("barbarian")) {   dmg = dmg2 + (classWep ? Config.barDmgClass : Config.barDmgOther); }
        if (race.getName().equalsIgnoreCase("druid")) {       dmg = dmg2 + (classWep ? Config.druDmgClass : Config.druDmgOther); }
        if (race.getName().equalsIgnoreCase("necromancer")) { dmg = dmg2 + (classWep ? Config.necDmgClass : Config.necDmgOther); }
        if (race.getName().equalsIgnoreCase("wizard")) {      dmg = dmg2 + (classWep ? Config.wizDmgClass : Config.wizDmgOther); }
        
        return (dmg > 0 ? dmg : 1);
    }
    
    
    
    
    
    
    
    
    
    
//STRING METHODS
    public static String stackToString(Throwable th) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement ste : th.getStackTrace()) {
            sb.append(ste.toString() + "\n");
        }
        return sb.toString();
    }
    
    public static String HpFoodBars(int num, ChatColor pre, ChatColor half, ChatColor post, boolean hp, boolean showAll, boolean extra, boolean wholeNums) {
        return HpFoodBars(num, pre, half, post, hp, showAll, extra, wholeNums, true);
    }
    
    public static String HpFoodBars(int num, ChatColor pre, ChatColor half, ChatColor post, boolean hp, boolean showAll, boolean extra, boolean wholeNums, boolean flipFood) {
        if (!hp) {
            ChatColor tmp = post;
            post = pre;
            pre = tmp;
        }
        
        String barChar, str = pre + "";
        //if (hp) { barChar = "\u2588"; } else { barChar = "\u2588"; };
        if (hp) { barChar = "|"; } else { barChar = "|"; if (flipFood) { num = 20-num; } };
        String space = "";
        
        if(showAll) {
            for (int i = 1; i <= 20; i++) {
                str += barChar;
                if (i == num) { str += post; }
                if (extra) { if (i == 10) { str += ChatColor.WHITE + "" + (wholeNums ? (num >= 10 ? num : space + num) : (num == 20 ? (float)num/2 : space + (float)num/2)) + (i < num ? pre : post); } }
            }
        } else {
            for (int i = 1; i <= 20; i += 1) {
                if (i == num) {
                    str += (i % 2 == 0 ? pre : half);
                }
                if (i %2 == 0) {
                    str += barChar;
                }
                if (i == num+1) {
                    str += post;
                }
                if (extra) { if (i == 10) { str += ChatColor.WHITE + "" + (wholeNums ? (num >= 10 ? (hp || !flipFood ? num : 20-num) : space + (hp || !flipFood ? num : 20-num)) : (num == 20 ? (float)(hp || !flipFood ? num : 20 - num)/2 : space + (float)(hp || !flipFood ? num : 20 - num)/2)) + (i < num ? pre : post); } }
            }
        }
        return str + ChatColor.RESET;
    }
    
    public static ItemStack strToItemStack(String itemstr) {
        String[] a = itemstr.split("\\ ");
        String[] b = a[0].split(":");
        int type  = Integer.valueOf(b[0]);
        int amount = (a.length == 2 ? Integer.valueOf(a[1]) : 1);
        short damage = (b.length == 2 ? Short.valueOf(b[1]) : 0);
        return new ItemStack(type, amount, damage);
    }
    
    
    
    
    
    
    
    
    
    
//Location Methods
    public static float getYawFromLocToLoc(Location locSrc, Location locDest) {
        double a = locDest.getBlockX()-locSrc.getBlockX();
        double b = locDest.getBlockZ()-locSrc.getBlockZ();
        //double c = Math.sqrt((a*a) + (b*b));
        
        double t = Math.atan(a/b);
        
        double A = t * (180/Math.PI);
        //double B = 180 - A - 90.0;
        //double C = 90.0;
        
        int add = locSrc.getBlockZ() <= locDest.getBlockZ() ? 360 : 180;
        
        return (add-(float) A);
    }
    
    public static boolean hasSight(LivingEntity look, LivingEntity hide) {
        return (look.getLineOfSight(null, 120).contains(hide) ? true : false);
    }
    
    public static Location stringToLocation(String s, String sep) {
        return stringToLocation(s, sep, false, false);
    }
    
    public static Location stringToLocation(String s, String sep, boolean doPitch, boolean doYaw) {
        String[] spl = s.split(sep);
        if (spl.length != 4) { return null; }
        World w = Bukkit.getWorld(spl[0]);
        double x = Double.parseDouble(spl[1]);
        double y = Double.parseDouble(spl[2]);
        double z = Double.parseDouble(spl[3]);
        float pit = (doPitch ? Float.parseFloat(spl[4]) : 0);
        float yaw = (doYaw ? Float.parseFloat(spl[5]) : 0);
        return new Location(w, x, y, z, pit, yaw);
    }
    
    public static String locationToString(Location loc, String sep) {
        return locationToString(loc, sep, sep, false, false);
    }
    
    public static String locationToString(Location loc, String sep1, String sep2) {
        return locationToString(loc, sep1, sep2, false, false);
    }
    
    public static String locationToString(Location loc, String sep1, String sep2, boolean doPitch, boolean doYaw) {
        return loc.getWorld().getName() + sep1 + loc.getBlockX() + sep2 + loc.getBlockY() + sep2 + loc.getBlockZ() + (doPitch ? sep2 + loc.getPitch() : "") + (doYaw ? sep2 + loc.getYaw() : "");
    }
    
    public static boolean cmpLoc(Location locA, Location locB) {
        return cmpLoc(locA, locB, false, false);
    }
    
    public static String locationToStringColored(ChatColor colWld, ChatColor colPos, Location loc, String sep) {
        return locationToStringColored(colWld, colPos, loc, sep, sep, false, false);
    }
    
    public static String locationToStringColored(ChatColor colWld, ChatColor colPos, Location loc, String sep1, String sep2) {
        return locationToStringColored(colWld, colPos, loc, sep1, sep2, false, false);
    }
    
    public static String locationToStringColored(ChatColor colWld, ChatColor colPos, Location loc, String sep1, String sep2, boolean doPitch, boolean doYaw) {
        return colWld + loc.getWorld().getName() + ChatColor.RESET + sep1 +
                colPos + loc.getBlockX() + ChatColor.RESET + sep2 +
                colPos + loc.getBlockY() + ChatColor.RESET + sep2 +
                colPos + loc.getBlockZ() +
                (doPitch ? ChatColor.RESET + sep2 + colPos + loc.getPitch() : "") +
                (doYaw ? ChatColor.RESET + sep2 + colPos + loc.getYaw() : "") +
                ChatColor.RESET;
    }
    
    public static boolean cmpLoc(Location locA, Location locB, boolean doPitch, boolean doYaw) {
        boolean xyz = (locA.getBlockX() == locB.getBlockX() &&
                       locA.getBlockY() == locB.getBlockY() &&
                       locA.getBlockZ() == locB.getBlockZ());
        boolean pit = (locA.getPitch()  == locB.getPitch());
        boolean yaw = (locA.getYaw()    == locB.getYaw());
        
        return (xyz && (doPitch ? pit : true) && (doYaw ? yaw : true));
    }
    
    public static boolean isBehind(Entity back, Entity frnt) {
        //Voodoo to check if the player is facing the same direction as their target (aka behind) when attacking
        Location locB = frnt.getLocation(); float yawB = locB.getYaw(); if (yawB < 0) { yawB += 360; } yawB %= 360; int dirBack = (int)((yawB+8)/22.5);
        Location locF = back.getLocation(); float yawF = locF.getYaw(); if (yawF < 0) { yawF += 360; } yawF %= 360; int dirFrnt = (int)((yawF+8)/22.5);
        
        return (Util.between(dirBack, dirFrnt-1, dirFrnt+1, true));
    }
    
    
    
    
    
    
    
    
    
//TreeSet methods
    public static void ListRemove(CopyOnWriteArrayList<Aid> aids, Aid aid) {
        Iterator<Aid> it = aids.iterator();
        while (it.hasNext()) {
            if (it.next() == aid) {
                aids.remove(aid);
            }
        }
    }
    
    public static void ListRemove(CopyOnWriteArrayList<Pet> pets, Pet pet) {
        Iterator<Pet> it = pets.iterator();
        while (it.hasNext()) {
            if (it.next() == pet) {
                pets.remove(pet);
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
//Specially formatted messages
    public static String displayAidInfo(Player p, Aid aid) {
        String msg;
        Race race = RaceMgr.getRace(aid.getOwnerName());
        
        if (p.getName().equals(aid.getOwnerName())) {
            msg =
                ChatColor.GRAY + "[" + HpFoodBars(20, ChatColor.DARK_RED, ChatColor.RED, ChatColor.GRAY, true, false, true, true) + ChatColor.GRAY + "] " +
                ChatColor.RESET +  aid.getName();
        } else {
            msg =
                ChatColor.GRAY + "[" + HpFoodBars(20, ChatColor.DARK_RED, ChatColor.RED, ChatColor.GRAY, true, false, true, true) + ChatColor.GRAY + "] " +
                race.getColor() + race.getPlayerName() + ChatColor.RESET + "'s " + aid.getName();
        }
        
        return msg;
    }
    
    public static String displayPetInfo(Player p, Pet pet) {
        String msg;
        Race race = RaceMgr.getRace(pet.getOwnerName());
        
        if (p.getName().equals(pet.getOwnerName())) {
            msg =
                ChatColor.GRAY + "[" + HpFoodBars(pet.getHealth(), ChatColor.DARK_RED, ChatColor.RED, ChatColor.GRAY, true, false, true, true) + ChatColor.GRAY + "] " +
                ChatColor.RESET +  pet.getName();
        } else {
            msg =
                ChatColor.GRAY + "[" + HpFoodBars(pet.getHealth(), ChatColor.DARK_RED, ChatColor.RED, ChatColor.GRAY, true, false, true, true) + ChatColor.GRAY + "] " +
                race.getColor() + race.getPlayerName() + ChatColor.RESET + "'s " + pet.getName();
        }
        
        return msg;
    }
    
    public static String displayPlayerInfo(Player p) {
        Race race = RaceMgr.getRace(p.getName());
        
        String msg =
            ChatColor.GRAY + "[" + HpFoodBars(p.getHealth(), ChatColor.DARK_RED, ChatColor.RED, ChatColor.GRAY, true, false, true, false) + ChatColor.GRAY + "] " +
            ChatColor.GRAY + race.getColor() + p.getName() +
            ChatColor.GRAY + " [" + HpFoodBars(p.getFoodLevel(), ChatColor.DARK_GREEN, ChatColor.GREEN, ChatColor.GRAY, false, false, true, false) + ChatColor.GRAY + "]";
        
        return msg;
    }
    
    public static String displayEntityInfo(Player p, LivingEntity ent) {
        String msg =
            ChatColor.GRAY + "[" + HpFoodBars(ent.getHealth(), ChatColor.DARK_RED, ChatColor.RED, ChatColor.GRAY, true, false, true, true) + ChatColor.GRAY + "] " +
            ChatColor.RESET + ent.getType().getName();
        
        return msg;
    }
}

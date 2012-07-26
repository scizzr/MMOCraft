package com.scizzr.bukkit.mmocraft.managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.mmocraft.Main;
import com.scizzr.bukkit.mmocraft.aids.Beacon;
import com.scizzr.bukkit.mmocraft.aids.Forcefield;
import com.scizzr.bukkit.mmocraft.aids.Sigil;
import com.scizzr.bukkit.mmocraft.aids.Totem;
import com.scizzr.bukkit.mmocraft.aids.Trap;
import com.scizzr.bukkit.mmocraft.aids.Turret;
import com.scizzr.bukkit.mmocraft.classes.Archer;
import com.scizzr.bukkit.mmocraft.classes.Assassin;
import com.scizzr.bukkit.mmocraft.classes.Barbarian;
import com.scizzr.bukkit.mmocraft.classes.Druid;
import com.scizzr.bukkit.mmocraft.classes.Necromancer;
import com.scizzr.bukkit.mmocraft.classes.Wizard;
import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class AidMgr extends JavaPlugin {
    private static CopyOnWriteArrayList<Aid> aids = new CopyOnWriteArrayList<Aid>();
    private static ArrayList<Integer> validBlocks = new ArrayList<Integer>();
    
    public static void main () {
        String blocks = "1,2,3,4,5,14,15,16,17,19,21,22,24,35,41,42,43,47,48,49,56,57,73,74,86,87,88,89,91,97,98,99,100,103,110,112";
        String[] split = blocks.split(",");
        
        for (int i = 0; i < split.length; i++) {
            validBlocks.add(Integer.valueOf(split[i]));
        }
        
        loadAids();
    }
    
    public static void addAid(Player p, Block b) {
        Race race = RaceMgr.getRace(p.getName());
        
        int num = Integer.valueOf(numAids(p));
        if (num >= race.getMaxAids()) {
//TODO: Permission - Bypass max Aid number
            //if (!Vault.hasPermission(p, "")) {
                p.sendMessage(Main.prefix + I18n._("aidhasmax", new Object[] {}));
                return;
            //}
//TODO
        }
        
        Location locClk = b.getLocation();
        Location locAid = locClk.clone().add(0, 1, 0);
        Block b2 = locAid.getBlock();
        
        Aid aid = null;
        if (race instanceof Archer)      { aid = new Turret();     }
        if (race instanceof Assassin)    { aid = new Sigil();      }
        if (race instanceof Barbarian)   { aid = new Beacon();     }
        if (race instanceof Druid)       { aid = new Forcefield(); }
        if (race instanceof Necromancer) { aid = new Totem();      }
        if (race instanceof Wizard)      { aid = new Trap();       }
        
        if (locAid.getBlock().getType() != Material.AIR) {
            p.sendMessage(Main.prefix + I18n._("aidnoroom", new Object[] {aid.getName().toLowerCase()})); return;
        } else if (isAid(locClk.getBlock())) {
            p.sendMessage(Main.prefix + I18n._("aidalready", new Object[] {getAid(locClk.getBlock()).getName().toLowerCase()})); return;
        } else if (isAid(locAid.getBlock())) {
            p.sendMessage(Main.prefix + I18n._("aidalready", new Object[] {getAid(locAid.getBlock()).getName().toLowerCase()})); return;
        } else if (!validBlocks.contains(locClk.getBlock().getTypeId())) {
            p.sendMessage(Main.prefix + I18n._("aidcannot", new Object[] {aid.getName().toLowerCase()})); return;
        }
        
        aid.setLocation(locAid.clone()); aid.setOwnerName(p.getName());
        b2.setTypeId(Integer.valueOf(aid.getBlocks().get(0).split(":")[0]));
        b2.setData(Byte.valueOf(aid.getBlocks().get(0).split(":")[0]));
        
        synchronized(aids) {
            aids.add(aid);
            race.addAid(aid);
        }
        
        locAid.getBlock().setTypeIdAndData(51, (byte) 1, true);
        
        p.sendMessage(Main.prefix + I18n._("aidadd", new Object[] {aid.getName(), locAid.getWorld().getName() + ":" + locAid.getBlockX() + "," + locAid.getBlockY() + "," + locAid.getBlockZ()}));
    }
    
    public static void removeAid(Block b, Entity ent, boolean remove) {
        Location loc = b.getLocation();
        Aid aid = getAid(b);
        
        if (aid != null) {
            String ownername = aid.getOwnerName();
            Race race = RaceMgr.getRace(ownername);
            
            race.removeAid(aid);
            Util.ListRemove(aids, aid);
            
            b.setType(Material.AIR);
            
            OfflinePlayer owner = Bukkit.getOfflinePlayer(ownername);
            
            if (!remove) { return; }
            
            if (ent instanceof Player) {
                Player p = (Player)ent;
                
                if (p != owner) {
                    p.sendMessage(Main.prefix + I18n._("aidremo", new Object[] {RaceMgr.getRace(owner.getName()).getColor() + owner.getName() + ChatColor.RESET, aid.getName(), loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ()}));
                    if (owner.isOnline()) {
                        Bukkit.getPlayer(ownername).sendMessage(Main.prefix + I18n._("aidrem", new Object[] {RaceMgr.getRace(p.getName()).getColor() + p.getName() + ChatColor.RESET, aid.getName(), loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ()}));
                    }
                } else {
                    p.sendMessage(Main.prefix + I18n._("aidrem", new Object[] {aid.getName(), loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ()}));
                }
            } else if (ent == null) {
                if (owner.isOnline()) {
                    Bukkit.getPlayer(ownername).sendMessage(Main.prefix + I18n._("aidrem", new Object[] {aid.getName(), loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ()}));
                }
            }
        }
    }
    
    public static void removeAllPlayerAids(String name) {
        Race race = RaceMgr.getRace(name);
        
        synchronized(aids) {
            Iterator<Aid> it = aids.iterator();
            while (it.hasNext()) {
                synchronized(it) {
                    Aid aid = it.next();
                    
                    if (aid.getOwnerName() == name) {
                        removeAid(aid.getLocation().getBlock(), null, true);
                        race.removeAid(aid);
                    }
                }
            }
        }
    }
    
    public static boolean isAid(Block b) {
        return getAid(b) != null;
    }
    
    public static Aid getAid(Block b) {
        for (Aid aid : aids) {
            if (aid.getLocation().equals(b.getLocation())) { return aid; }
        }
        return null;
    }
    
    public static void listAids(Player p) {
        List<Aid> aidsPlayer = getPlayerAids(p.getName());
        
        if (aidsPlayer.size() == 0) { p.sendMessage(Main.prefix + I18n._("aidhasnone", new Object[] {})); return; }
        
        Iterator<Aid> it = aidsPlayer.iterator();
        while (it.hasNext()) {
            Aid aid = it.next();
            if (aid.getOwnerName().equalsIgnoreCase(p.getName())) {
                p.sendMessage(Main.prefix + Util.displayAidInfo(p, aid));
            }
        }
    }
    
    public static boolean isNearAid(Block b) {
        for (Aid aid : aids) {
            for (int x = b.getX()-1; x <= b.getX()+1; x++) {
                for (int y = b.getY()-1; y <= b.getY()+1; y++) {
                    for (int z = b.getZ()-1; z <= b.getZ()+1; z++) {
                        Location loc = b.getLocation().clone();
                        loc.setX(x); loc.setY(y); loc.setZ(z);
                        
                        if (aid.getLocation() == b.getLocation())
                            return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static void progressAids() {
        for (Aid aid : aids) {
            aid.progress();
        }
    }
    
    public static void springWizardTrap(Entity ent, Block b) {
        if (!(ent instanceof LivingEntity)) { return; }
        
        Aid aid = getAid(b);
        String name = aid.getOwnerName();
        Player owner = Bukkit.getPlayer(name);
        
        if (ent instanceof Player) {
            if ((Player)ent == owner) { return; }
        }
        
        ent.setFireTicks(50);
        removeAid(b, ent, false);
        
        if (owner.isOnline()) {
            Location loc = b.getLocation();
            if (ent instanceof Player) {
                owner.sendMessage(Main.prefix + I18n._("trapstepplay", new Object[] {ent.getType().getName(), loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ()}));
            } else {
                owner.sendMessage(Main.prefix + I18n._("trapstepmob", new Object[] {((Player)ent).getName(), loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ()}));
            }
        }
    }
    
    public static int numAids(Player p) {
        int count = 0;
        synchronized(aids) {
            Iterator<Aid> it = aids.iterator();
            while (it.hasNext()) {
                Aid aidd = it.next();
                if (aidd.getOwnerName().equals(p.getName())) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public static List<Aid> getPlayerAids(String name) {
        Race race = RaceMgr.getRace(name);
        return race.getAids();
    }
    
    public static boolean loadAids() {
        File file = new File(Main.filePlayerAids.getAbsolutePath());
        
        if (!Main.filePlayerAids.exists()) {
            try {
                file.createNewFile();
                Main.log.info(Main.prefixConsole + I18n._("succeedyml", new Object[] {file.getName()}));
            } catch (Exception ex) {
                Main.log.info(Main.prefixConsole + I18n._("failedyml", new Object[] {file.getName()}));
                Main.suicide(ex);
            }
        }
        
        CopyOnWriteArrayList<Aid> setTmp = aids;
        
        try {
            synchronized(aids) {
                aids.clear();
                BufferedReader reader = new BufferedReader(new FileReader(Main.filePlayerAids));
                String line = reader.readLine();
                
                while (line != null) {
                    String[] valueA = line.split(";");
                    if (valueA.length != 4) { continue; }
                    String owner = valueA[0];
                    String name = valueA[1];
                    int count = Integer.valueOf(valueA[2]);
                    String loc = valueA[3];
                        String[] valueB = loc.split(":");
                        if (valueB.length != 4) { continue; }
                        World w = Bukkit.getWorld(valueB[0]);
                        int x = Integer.valueOf(valueB[1]);
                        int y = Integer.valueOf(valueB[2]);
                        int z = Integer.valueOf(valueB[3]);
                        Location locAid = new Location(w, x, y, z);
                    
                    Aid aid = null;
                    if (name.equals("Turret")) {     aid = new Turret(); }
                    if (name.equals("Sigil")) {      aid = new Sigil(); }
                    if (name.equals("Beacon")) {     aid = new Beacon(); }
                    if (name.equals("Forcefield")) { aid = new Forcefield(); }
                    if (name.equals("Totem")) {      aid = new Totem(); }
                    if (name.equals("Trap")) {       aid = new Trap(); }
                    
                    if (aid != null) {
                        aid.setLocation(locAid); aid.setOwnerName(owner); aid.setCount(count);
                        
                        aids.add(aid);
                        
                        RaceMgr.getRace(owner).addAid(aid);
                    }
                    
                    line = reader.readLine();
                }
            }
            return true;
        } catch (Exception ex) {
//XXX : Remove?
            //Main.suicide(ex);
            aids = setTmp;
            return false;
        }
    }
    
    public static boolean saveAids() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Main.filePlayerAids));
            synchronized(aids) {
                Iterator<Aid> it = aids.iterator();
                
                while (it.hasNext()) {
                    Aid aid = it.next();
                    String owner = aid.getOwnerName();
                    String name = aid.getName();
                    int count = aid.getCount();
                    Location loc = aid.getLocation();
                    writer.write(owner + ";" + name + ";" + count + ";" + Util.locationToString(loc, ":"));
                    writer.newLine();
                }
            }
            writer.close();
            return true;
        } catch (Exception ex) {
//XXX : Remove?
            //Main.suicide(ex);
            return false;
        }
    }
}

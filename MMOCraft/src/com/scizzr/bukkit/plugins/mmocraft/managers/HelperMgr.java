package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Helper;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers.Beacon;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers.Forcefield;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers.Sigil;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers.Totem;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers.Trap;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers.Turret;

public class HelperMgr extends JavaPlugin {
    public static ConcurrentHashMap<Helper, Boolean> helpers = new ConcurrentHashMap<Helper, Boolean>();
    private static ArrayList<Integer> validBlocks = new ArrayList<Integer>();
    
    public static void main () {
        String blocks = "1,2,3,4,5,14,15,16,17,19,21,22,24,35,41,42,43,47,48,49,56,57,73,74,86,87,88,89,91,97,98,99,100,103,110,112";
        String[] split = blocks.split(",");
        
        for (int i = 0; i < split.length; i++) {
            validBlocks.add(Integer.valueOf(split[i]));
        }
        
        loadHelpers();
    }
    
    public static void addHelper(Player p, Block b) {
        Location locClk = b.getLocation();
        Location locHelp = locClk.clone().add(0, 1, 0);
        Block b2 = locHelp.getBlock();
        
        Helper help = null;
        if (RaceMgr.getRaceName(p.getName()).equalsIgnoreCase("archer")) { help = new Turret(); }
        if (RaceMgr.getRaceName(p.getName()).equalsIgnoreCase("assassin")) { help = new Sigil(); }
        if (RaceMgr.getRaceName(p.getName()).equalsIgnoreCase("barbarian")) { help = new Beacon(); }
        if (RaceMgr.getRaceName(p.getName()).equalsIgnoreCase("druid"))  { help = new Forcefield(); }
        if (RaceMgr.getRaceName(p.getName()).equalsIgnoreCase("wizard")) { help = new Trap(); }
        if (RaceMgr.getRaceName(p.getName()).equalsIgnoreCase("necromancer")) { help = new Totem(); }
        
        if (locHelp.getBlock().getType() != Material.AIR) {
            p.sendMessage(Main.prefix + "You don't have enough room to build a " + help.getName().toLowerCase() + " there."); return;
        } else if (isHelper(locClk.getBlock())) {
            p.sendMessage(Main.prefix + "There's already a " + getHelper(locClk.getBlock()).getName().toLowerCase() + " there."); return;
        } else if (isHelper(locHelp.getBlock())) {
            p.sendMessage(Main.prefix + "There's already a " + getHelper(locHelp.getBlock()).getName().toLowerCase() + " there."); return;
        } else if (!validBlocks.contains(locClk.getBlock().getTypeId())) {
            p.sendMessage(Main.prefix + "You can't build a " + help.getName().toLowerCase() + " on that block."); return;
        }
        
        help.setLocation(locHelp.clone()); help.setPlayerName(p.getName());
        b2.setTypeId(Integer.valueOf(help.getBlocks().get(0).split(":")[0]));
        b2.setData(Byte.valueOf(help.getBlocks().get(0).split(":")[0]));
        
        helpers.put(help, true);
        locHelp.getBlock().setTypeIdAndData(51, (byte) 1, true);
        p.sendMessage(Main.prefix + help.getName() + " added @ [" + locHelp.getWorld().getName() + ":" + locHelp.getBlockX() + "," + locHelp.getBlockY() + "," + locHelp.getBlockZ() + "]");
    }
    
    public static void removeHelper(Block b, Entity ent) {
        Location loc = b.getLocation();
        Helper help = getHelper(b);
        if (help != null) {
            String ownername = help.getPlayerName();
            helpers.remove(help);
            b.setType(Material.AIR);
            
            OfflinePlayer owner = Bukkit.getOfflinePlayer(ownername);
            
            if (ent instanceof Player) {
                Player p = (Player)ent;
                
                if (p != owner) {
                    p.sendMessage(Main.prefix + "Removed " + RaceMgr.getRace(owner.getName()).getColor() + RaceMgr.getRace(owner.getName()).getColor() + owner.getName() + ChatColor.RESET + "'s " + help.getName() + " @ [" + loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "]");
                    if (owner.isOnline()) {
                        Bukkit.getPlayer(ownername).sendMessage(Main.prefix + RaceMgr.getRace(p.getName()).getColor() + p.getName() + ChatColor.RESET + " removed your " + help.getName() + " @ [" + loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "]");
                    }
                } else {
                    p.sendMessage(Main.prefix + help.getName() + " removed @ [" + loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "]");
                }
            } else if (ent == null) {
                if (owner.isOnline()) {
                    Bukkit.getPlayer(ownername).sendMessage(Main.prefix + help.getName() + " removed @ [" + loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "]");
                }
            }
        }
    }
    
    public static Helper getHelper(Block b) {
        for (Entry<Helper, Boolean> entry : helpers.entrySet()) {
            Helper help = entry.getKey();
            if (help.getLocation().equals(b.getLocation())) { return help; }
        }
        return null;
    }
    
    public static boolean isHelper(Block b) {
        return getHelper(b) != null;
    }
    
    public static boolean isNearHelper(Block b) {
        for (Entry<Helper, Boolean> entry : helpers.entrySet()) {
            Helper help = entry.getKey();
            for (int x = b.getX()-1; x <= b.getX()+1; x++) {
                for (int y = b.getY()-1; y <= b.getY()+1; y++) {
                    for (int z = b.getZ()-1; z <= b.getZ()+1; z++) {
                        Location loc = b.getLocation().clone();
                        loc.setX(x); loc.setY(y); loc.setZ(z);
                        
                        if (help.getLocation() == b.getLocation())
                            return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static void progressHelpers() {
        for (Entry<Helper, Boolean> entry : helpers.entrySet()) {
            Helper help = entry.getKey();
            help.progress();
        }
    }
    
    public static void springWizardTrap(Entity ent, Block b) {
        if (!(ent instanceof LivingEntity)) { return; }
        
        Helper help = getHelper(b);
        String name = help.getPlayerName();
        Player owner = Bukkit.getPlayer(name);
        
        if (ent instanceof Player) {
            if ((Player)ent == owner) { return; }
        }
        
        ent.setFireTicks(50);
        removeHelper(b, ent);
        
        if (owner.isOnline()) {
            Location loc = b.getLocation();
            owner.sendMessage(Main.prefix + help.getName() + " sprung [" + loc.getWorld().getName() + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "] by " + ((ent instanceof Player) ? ((Player)ent).getName() : ent.getType().getName()));
        }
    }
    
    public static boolean loadHelpers() {
        File file = new File(Main.filePlayerHelpers.getAbsolutePath());
        if (!Main.filePlayerHelpers.exists()) {
            try {
                file.createNewFile();
                Main.log.info(Main.prefixConsole + "Blank playerHelpers.yml created");
            } catch (Exception ex) {
                Main.log.info(Main.prefixConsole + "Failed to make playerHelpers.yml");
                Main.suicide(ex);
            }
        }
        ConcurrentHashMap<Helper, Boolean> mapTmp = helpers;
        try {
            helpers.clear();
            BufferedReader reader = new BufferedReader(new FileReader(Main.filePlayerHelpers));
            String line = reader.readLine();
            
            while (line != null) {
                String[] valueA = line.split(";");
                if (valueA.length != 4) { continue; }
                String loc = valueA[0];
                    String[] valueB = loc.split(":");
                    if (valueB.length != 4) { continue; }
                    World w = Bukkit.getWorld(valueB[0]);
                    int x = Integer.valueOf(valueB[1]);
                    int y = Integer.valueOf(valueB[2]);
                    int z = Integer.valueOf(valueB[3]);
                    Location locHelp = new Location(w, x, y, z);
                String name = valueA[1];
                int count = Integer.valueOf(valueA[2]);
                String ownername = valueA[3];
                
                Helper help = null;
                if (name.equals("Turret")) { help = new Turret(); }
                if (name.equals("Sigil")) { help = new Sigil(); }
                if (name.equals("Beacon")) { help = new Beacon(); }
                if (name.equals("Forcefield")) { help = new Forcefield(); }
                if (name.equals("Totem")) { help = new Totem(); }
                if (name.equals("Trap")) { help = new Trap(); }
                
                if (help != null) {
                    help.setLocation(locHelp); help.setPlayerName(ownername); help.setCount(count);
                    helpers.put(help, true);
                }
                
                line = reader.readLine();
            }
            return true;
        } catch (Exception ex) {
            helpers = mapTmp;
            return false;
        }
    }
    
    public static boolean saveHelpers() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Main.filePlayerHelpers));
            for (Entry<Helper, Boolean> entry : helpers.entrySet()) {
                Helper help = entry.getKey();
                    Location loc = help.getLocation();
                        String world = loc.getWorld().getName();
                        int x = loc.getBlockX();
                        int y = loc.getBlockY();
                        int z = loc.getBlockZ();
                    String name = help.getName();
                    int count = help.getCount();
                    String owner = help.getPlayerName();
                
                writer.write(world + ":" + x + ":" + y + ":" + z + ";" + name + ";" + count + ";" + owner);
                writer.newLine();
            }
            writer.close();
            return true;
        } catch (Exception ex) {
            Main.suicide(ex);
            return false;
        }
    }
}

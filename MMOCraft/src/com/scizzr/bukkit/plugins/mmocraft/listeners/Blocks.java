package com.scizzr.bukkit.plugins.mmocraft.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperMgr;

public class Blocks implements Listener {
    Main plugin;
    
    public Blocks(Main instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Block b2 = e.getBlock().getLocation().clone().add(0, 1, 0).getBlock();
        
        if (HelperMgr.isHelper(b)) { HelperMgr.removeHelper(b, p); }
        if (HelperMgr.isHelper(b2)) { HelperMgr.removeHelper(b2, p); }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPhysics(final BlockPhysicsEvent e) {
        if (HelperMgr.isHelper(e.getBlock())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPistonExtend(final BlockPistonExtendEvent e) {
        Block b = e.getBlock();
        BlockFace bf = e.getDirection();
        
        String dir = bf.name();
        
        Location loc = b.getLocation().clone(); loc.add(bf.getModX(), bf.getModY(), bf.getModZ());
        Location diff = new Location(loc.getWorld(), 0, 0, 0);
        
        if (     dir.equalsIgnoreCase("NORTH")) diff.add(-1,  0,  0);
        else if (dir.equalsIgnoreCase("SOUTH")) diff.add(+1,  0,  0);
        else if (dir.equalsIgnoreCase("DOWN"))  diff.add( 0, -1,  0);
        else if (dir.equalsIgnoreCase("UP"))    diff.add( 0, +1,  0);
        else if (dir.equalsIgnoreCase("EAST"))  diff.add( 0,  0, -1);
        else if (dir.equalsIgnoreCase("WEST"))  diff.add( 0,  0, +1);
        
        Location bot = loc.clone();
        Location top = bot.clone(); top.add(0, +1, 0);
        
        for (int i = 0; i <= 12; i ++) {
            //if (HelperMgr.isHelper(b)) { HelperMgr.remove(b, p); }
            if (HelperMgr.isHelper(bot.getBlock()) || HelperMgr.isHelper(top.getBlock())) {
                e.setCancelled(true);
                return;
            }
            
            bot.add(diff); top.add(diff);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPistonRetract(final BlockPistonRetractEvent e) {
        Block b = e.getBlock();
        BlockFace bf = e.getDirection();
        
        String dir = bf.name();
        
        Location loc = b.getLocation().clone(); loc.add(bf.getModX(), bf.getModY(), bf.getModZ());
        Location diff = new Location(loc.getWorld(), 0, 0, 0);
        
        if (     dir.equalsIgnoreCase("NORTH")) diff.add(-1,  0,  0);
        else if (dir.equalsIgnoreCase("SOUTH")) diff.add(+1,  0,  0);
        else if (dir.equalsIgnoreCase("DOWN"))  diff.add( 0, -1,  0);
        else if (dir.equalsIgnoreCase("UP"))    diff.add( 0, +1,  0);
        else if (dir.equalsIgnoreCase("EAST"))  diff.add( 0,  0, -1);
        else if (dir.equalsIgnoreCase("WEST"))  diff.add( 0,  0, +1);
        
        Location bot = loc.clone();
        Location top = bot.clone(); top.add(0, +1, 0);
        
        for (int i = 0; i <= 12; i ++) {
            //if (HelperMgr.isHelper(b)) { HelperMgr.remove(b, p); }
            if (HelperMgr.isHelper(bot.getBlock()) || HelperMgr.isHelper(top.getBlock())) {
                e.setCancelled(true);
                return;
            }
            
            bot.add(diff); top.add(diff);
        }
    }
}

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

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperManager;

public class Blocks implements Listener {
    Main plugin;
    
    public Blocks(Main instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockBreak(final BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Block b2 = e.getBlock().getLocation().clone().add(0, 1, 0).getBlock();
        
        if (HelperManager.isHelper(b)) { HelperManager.removeHelper(b, p); }
        if (HelperManager.isHelper(b2)) { HelperManager.removeHelper(b2, p); }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPhysics(final BlockPhysicsEvent e) {
        if (HelperManager.isHelper(e.getBlock())) {
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
            //if (HelperManager.isHelper(b)) { HelperManager.remove(b, p); }
            if (HelperManager.isHelper(bot.getBlock()) || HelperManager.isHelper(top.getBlock())) {
                e.setCancelled(true);
                return;
            }
            
            bot.add(diff); top.add(diff);
        }
    }
}

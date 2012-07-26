package com.scizzr.bukkit.mmocraft.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import com.scizzr.bukkit.mmocraft.Main;
import com.scizzr.bukkit.mmocraft.managers.AidMgr;

public class Blocks implements Listener {
    Main plugin;
    
    public Blocks(Main instance) {
        plugin = instance;
    }
    
/* Remove Me! I'm just for Testing @ http://forums.bukkit.org/threads/making-a-block-immune-to-explosion.87818/
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(final EntityExplodeEvent e) {
        String[] keep = "1,2,3,4,5:0,5:1,5:2,5:3,20".split(",");
        List<Block> blocks = e.blockList();
        synchronized (blocks) {
            Iterator<Block> it = blocks.iterator();
            while (it.hasNext()) {
                Block block = it.next();
                for (int i = 0; i < keep.length; i++) {
                    String[] info = keep[i].split(":");
                    int idRem = Integer.valueOf(info[0]);
                    int valRem = (info.length == 2 ? Integer.valueOf(info[1]) : 0);
                    
                    int idBlk = block.getTypeId();
                    int valBlk = (int)block.getData();
                    
                    if (idRem == idBlk && valRem == valBlk) { it.remove(); }
                }
            }
        }
    }
*/
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Block b2 = e.getBlock().getLocation().clone().add(0, 1, 0).getBlock();
        
        if (AidMgr.isAid(b)) { AidMgr.removeAid(b, p, true); }
        if (AidMgr.isAid(b2)) { AidMgr.removeAid(b2, p, true); }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockIgnite(final BlockIgniteEvent e) {
        if (e.getCause()== IgniteCause.FIREBALL) {
//TODO : Config - disable Blaze fireball fire
            //if () {
                e.setCancelled(true);
            //}
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPhysics(final BlockPhysicsEvent e) {
        if (AidMgr.isAid(e.getBlock())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
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
            //if (AidMgr.isAid(b)) { AidMgr.remove(b, p); }
            if (AidMgr.isAid(bot.getBlock()) || AidMgr.isAid(top.getBlock())) {
                e.setCancelled(true);
                return;
            }
            
            bot.add(diff); top.add(diff);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
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
            //if (AidMgr.isAid(b)) { AidMgr.remove(b, p); }
            if (AidMgr.isAid(bot.getBlock()) || AidMgr.isAid(top.getBlock())) {
                e.setCancelled(true);
                return;
            }
            
            bot.add(diff); top.add(diff);
        }
    }
}

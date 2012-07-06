package com.scizzr.bukkit.plugins.mmocraft.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.classes.Archer;
import com.scizzr.bukkit.plugins.mmocraft.classes.Wizard;

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
        
        //p.sendMessage(b.toString());
        //p.sendMessage(b2.toString());
        
        if (Wizard.isTrap(b)) { Wizard.removeTrap(b, p); } else if (Wizard.isTrap(b2)) { Wizard.removeTrap(b2, p); }
        if (Archer.isTurret(b)) { Archer.removeTurret(b, p); } else if (Archer.isTurret(b2)) { Archer.removeTurret(b2, p); }
    }
}

package com.scizzr.bukkit.mmocraft.hooks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.mmocraft.Main;

import fr.neatmonster.nocheatplus.checks.moving.RunningCheck.RunningCheckEvent;

public class HookNCP extends JavaPlugin implements Listener {
    Main plugin;
    
    public HookNCP(Main instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRunFlyCheck(final RunningCheckEvent e) {
        //
    }
}

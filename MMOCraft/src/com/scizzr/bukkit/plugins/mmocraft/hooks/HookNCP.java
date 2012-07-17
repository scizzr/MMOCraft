package com.scizzr.bukkit.plugins.mmocraft.hooks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.plugins.mmocraft.Main;

import fr.neatmonster.nocheatplus.checks.moving.RunningCheck.RunningCheckEvent;

public class HookNCP extends JavaPlugin implements Listener {
    Main plugin;
    
    public HookNCP(Main instance) {
        plugin = instance;
    }
    
    @Override
    public void onEnable() {
        Main.pm.registerEvents(this, this);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRunFlyCheck(final RunningCheckEvent e) {
        //
    }
}

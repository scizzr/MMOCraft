package com.scizzr.bukkit.mmocraft.hooks;

//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.scizzr.bukkit.mmocraft.MMOCraft;

//import fr.neatmonster.nocheatplus.hooks.NCPHook;

public class HookNCP extends JavaPlugin implements Listener {
    MMOCraft plugin;
    
    public HookNCP(MMOCraft instance) {
        plugin = instance;
    }
    /*
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRunFlyCheck(final RunningCheckEvent e) {
        //
    }
    */
}

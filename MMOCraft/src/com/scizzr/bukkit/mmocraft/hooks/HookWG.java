package com.scizzr.bukkit.mmocraft.hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class HookWG {
    public static boolean canBuild(Player p, Block b) {
        Plugin plug = MMOCraft.pm.getPlugin("WorldGuard");
        if (plug instanceof WorldGuardPlugin) {
            WorldGuardPlugin wg = (WorldGuardPlugin) MMOCraft.pm.getPlugin("WorldGuard");
            if (wg.canBuild(p, b)) {
                return true;
            } else return false;
        } else return true;
    }
    
    public static boolean canEnter(Player p, Block b) {
        /*
        Plugin plug = Main.pm.getPlugin("WorldGuard");
        if (plug instanceof WorldGuardPlugin) {
            WorldGuardPlugin wg = (WorldGuardPlugin) Main.pm.getPlugin("WorldGuard");
            RegionManager rm = wg.getRegionManager(p.getWorld());
            ApplicableRegionSet ars = rm.getApplicableRegions(b.getLocation());
            
            if(ars.allows(DefaultFlag.ENTRY)) {
                return true;
            } else return false;
        } else return true;
        */
        return true;
    }
}

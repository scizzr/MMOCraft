package com.scizzr.bukkit.plugins.mmocraft.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;
import com.scizzr.bukkit.plugins.mmocraft.timers.SpinTimer;

public class Barbarian {
    static int lvlLeap  = 10;
    static int lvlWhirl = 20;
    
    public static void attackLeft(Player p, Entity t) {
        
    }
    
    public static void attackRight(Player p, Action a) {
        int exp = ClassManager.getExp(p);
        int lvl = ClassManager.getLevel(exp);
        if (p.getItemInHand().getType() == Material.WOOD_AXE || p.getItemInHand().getType() == Material.IRON_AXE || p.getItemInHand().getType() == Material.GOLD_AXE || p.getItemInHand().getType() == Material.DIAMOND_AXE) {
            if (p.getLocation().getPitch() <= -60) {
                if (lvl >= lvlWhirl) { axeWhirl(p); }
            } else if (p.getLocation().getPitch() >= 60) {
                if (a == Action.RIGHT_CLICK_BLOCK) {
                    if (lvl >= lvlLeap) { leapAttack(p); }
                }
            } else {
                //clicking normal height
            }
        }
    }
    
    
    
    public static void axeWhirl(Player p) {
       SpinTimer.spin(p);
       for (Entity en : p.getNearbyEntities(3, 3, 3)) {
           if (en instanceof Player) {
               ((Player) en).damage(5);
           }
       }
    }
    
    public static void leapAttack(Player p) {
        //if (p.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            Location loc = p.getLocation().clone(); loc.setPitch(-90);
            p.setVelocity(loc.getDirection()); p.setFallDistance(-5);
        //}
    }
}

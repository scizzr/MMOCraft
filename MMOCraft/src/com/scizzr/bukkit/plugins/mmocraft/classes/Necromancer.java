package com.scizzr.bukkit.plugins.mmocraft.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class Necromancer {
    public static void attackLeft(Player p, Action a) {
        
    }
    
    public static void attackRight(Player p, Action a) {
        if (a == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand().getType() == Material.ROTTEN_FLESH) {
                Location loc = p.getTargetBlock(null, 120).getLocation().add(0, 1, 0);
                loc.getWorld().spawnCreature(loc, EntityType.ZOMBIE);
            } else if (p.getItemInHand().getType() == Material.ARROW) {
                Location loc = p.getTargetBlock(null, 120).getLocation().add(0, 1, 0);
                loc.getWorld().spawnCreature(loc, EntityType.SKELETON);
            } else if (p.getItemInHand().getType() == Material.MAGMA_CREAM) {
                Location loc = p.getTargetBlock(null, 120).getLocation().add(0, 1, 0);
                loc.getWorld().spawnCreature(loc, EntityType.MAGMA_CUBE);
            }
        }
    }
}

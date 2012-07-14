package com.scizzr.bukkit.plugins.mmocraft.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;

public class Druid {
    static int lvlDruidHelper = 30;
    
    public static void attackLeft(Player p, Action a) {
        
    }
    
    public static void attackRight(Player p, Action a) {
        int exp = ClassManager.getExp(p);
        int lvl = ClassManager.getLevel(exp);
        
        if (p.getItemInHand().getType() == Material.BOOK) {
            if (a == Action.RIGHT_CLICK_BLOCK) {
                if (p.isSneaking()) {
                    Location loc = p.getTargetBlock(null, 0).getLocation().clone();
                    Block b = loc.getBlock();
                    if (b.getLocation().clone().getBlock().getType() != Material.AIR) {
                        if (lvl >= lvlDruidHelper) {
                            helper(p, b);
                        }
                    }
                }
            }
        }
    }
    
    
    
    public static void helper(Player p, Block b) {
        if (SkillManager.isCooldown(p, "druid_helper")) { return; } else { SkillManager.addCooldown(p, "druid_helper", 100); }
        HelperManager.addHelper(p, b);
    }
}

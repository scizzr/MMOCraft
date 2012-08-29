package com.scizzr.bukkit.mmocraft.skills;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.timers.ArrowTimer;

public class NoneArrow implements Skill {
    int cooldown =   0;
    int lvlReq   =   0;
    
    public String getName() {
        return "Arrow";
    }
    
    public void execute(Player p, Entity ent, float f) {
        //if (SkillManager.isCooldown(p, getName())) { new ArcherArrow().execute(p, f); return; } else { SkillManager.addCooldown(p, getName(), cooldown); }
        Location eye = p.getEyeLocation().clone();
        
        Vector direction = eye.getDirection();
        Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
        arrow.setVelocity(direction.multiply(f*3)); arrow.setShooter(p);
        
        if (p.getGameMode() == GameMode.SURVIVAL) {
            CraftArrow ca = (CraftArrow)arrow; ca.getHandle().fromPlayer = 1;
            p.getInventory().removeItem(new ItemStack(Material.ARROW, 1));
        }
        
        ArrowTimer.add(arrow, 100);
    }
    
    public boolean isCooldown(Player p) {
        return false;
    }
    
    public boolean isLevel(Player p) {
        Race race = RaceMgr.getRace(p.getName());
        if (race != null) {
            int exp = race.getExp();
            int lvl = RaceMgr.getLevel(exp);
            if (lvl >= lvlReq) {
                return true;
            }
        }
        return false;
    }
}

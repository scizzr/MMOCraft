package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

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
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.timers.ArrowTimer;

public class ArcherFlaming implements Skill {
    int cooldown =  40;
    int lvlReq   =   0;
    
    Random rand = new Random();
    
    public String getName() {
        return "Flaming Arrow";
    }
    
    public void execute(Player p, Entity ent, float f) {
        if (SkillMgr.isCooldown(p, getName())) { new NoneArrow().execute(p, ent, f); return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { new NoneArrow().execute(p, ent, f); return; }
        
        Location eye = p.getEyeLocation().clone();
        
        Vector direction = eye.getDirection().multiply(3.0f);
        Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
        arrow.setVelocity(direction.multiply(f)); arrow.setShooter(p); arrow.setFireTicks(20);
        
        ArrowTimer.add(arrow, 50);
        
        if (p.getGameMode() == GameMode.SURVIVAL) {
            CraftArrow ca = (CraftArrow)arrow; ca.getHandle().fromPlayer = true;
            p.getInventory().removeItem(new ItemStack(Material.ARROW, 1));
        }
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

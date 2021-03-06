package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class ArcherRing implements Skill {
    int cooldown = 100;
    int lvlReq   =  20;
    int itemCost =  10;
    
    Random rand = new Random();
    
    public String getName() {
        return "Arrow Ring";
    }
    
    public void execute(Player p, Entity ent, float f) {
        if (SkillMgr.isCooldown(p, getName())) { new NoneArrow().execute(p, ent, f); return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { new NoneArrow().execute(p, ent, f); return; }
        
        if (p.getGameMode() == GameMode.SURVIVAL) {
            if (!p.getInventory().contains(Material.ARROW, itemCost)) { new NoneArrow().execute(p, ent, f); return; }
            p.getInventory().removeItem(new ItemStack(Material.ARROW, itemCost));
        }
        
        for (int i = 1; i <= 360; i += 1) {
            Location eye = p.getEyeLocation().clone();
            
            eye.setYaw(i-1); eye.setPitch(0 + (rand.nextFloat() * 5) - 2);
            
            Vector direction = eye.getDirection().multiply(2);
            Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
            arrow.setVelocity(direction.multiply(f)); arrow.setShooter(p);// arrow.setFireTicks(200);
            
            ArrowTimer.add(arrow, 50);
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

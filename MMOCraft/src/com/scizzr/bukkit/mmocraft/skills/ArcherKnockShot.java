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

import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.timers.ArrowTimer;

public class ArcherKnockShot implements Skill {
    int cooldown =  Config.arcSklKnShotCd;
    int lvlReq   =  Config.arcSklKnShotLvl;
    int itemCost = 1;
    
    Random rand = new Random();
    
    public String getName() {
        return "Knock Shot";
    }
    
    public void execute(Player p, Entity ent, float f) {
        if (SkillMgr.isCooldown(p, getName())) { /*new NoneArrow().execute(p, ent, f);*/ return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { new NoneArrow().execute(p, ent, f); return; }
        
        Race race = RaceMgr.getRace(p.getName());
        int knocks = race.hasData("knocks") ? Integer.valueOf(race.getData("knocks")) : 1;
        
        if (p.getGameMode() == GameMode.SURVIVAL) {
            if (!p.getInventory().contains(Material.ARROW, itemCost)) { new NoneArrow().execute(p, ent, f); return; }
            p.getInventory().removeItem(new ItemStack(Material.ARROW, itemCost));
        }
        
        for (int i = 1; i <= knocks; i += 1) {
            Location eye = p.getEyeLocation().clone();
            
            eye.setYaw(eye.getYaw()+(rand.nextInt(13)-6)); eye.setPitch(eye.getPitch()+(rand.nextInt(5)-2));
            
            Vector direction = eye.getDirection().multiply(2);
            Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
            arrow.setVelocity(direction.multiply(f)); arrow.setShooter(p);// arrow.setFireTicks(200);
            
            if (p.getGameMode() == GameMode.SURVIVAL) {
                CraftArrow ca = (CraftArrow)arrow; ca.getHandle().fromPlayer = 1;
            }
            
            ArrowTimer.add(arrow, 100);
        }
        
        race.setData("knocks", 1);
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

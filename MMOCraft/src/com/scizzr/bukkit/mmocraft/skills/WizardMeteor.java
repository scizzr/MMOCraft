package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.timers.FireballTimer;
import com.scizzr.bukkit.mmocraft.util.Util;

public class WizardMeteor implements Skill {
    int cooldown =   0;
    int lvlReq   =   0;
    
    Random rand = new Random();
    
    public String getName() {
        return "Meteor";
    }
    
    public void execute(Player p, Entity ent, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        Block block = p.getTargetBlock(null, 120);
        
        Location locP = p.getLocation().clone();
        Location locB = block.getLocation().clone();
        
        double a = locP.distance(locB)+(locB.getBlockY() - locP.getBlockY());   // Distance from player 1 to block
        double b = 10.0;                                                        // Block from player 1 to meteor (up)
        //double c = Math.sqrt((a*a) + (b*b));                                  // Distance from meteor to block
        
        double t = Math.atan(a/b);                                              // Tangent from block to meteor
        
        double A = t * (180/Math.PI);                                           // Block's angle to meteor
        //double B = 180 - A - 90.0;                                            // Angle to fire meteor
        //double C = 90.0;                                                      // Square angle
        
        Location loc = p.getEyeLocation().clone().add(0, b, 0);
        loc.setPitch((float) Util.angleToPitch((int) A));
        
        Vector direction = loc.getDirection().multiply(1);
        Fireball fb = (Fireball)p.getWorld().spawnEntity(loc.add(direction.getX(), direction.getY(), direction.getZ()), EntityType.FIREBALL);
        fb.setShooter(p); fb.setYield(0);
        
        SoundEffects.MOB_GHAST_FIREBALL.playGlobal(p.getLocation(), 0.5f, 1.0f);
        
        FireballTimer.addFireball(fb, 60);
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

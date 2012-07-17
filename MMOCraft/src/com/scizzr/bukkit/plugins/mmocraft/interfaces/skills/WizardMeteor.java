package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.plugins.mmocraft.timers.FireballTimer;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreMath;

public class WizardMeteor implements Skill {
    int cooldown =  40;
    int lvlReq   =   0;
    
    Random rand = new Random();
    
    public String getName() {
        return "Arrow Rain";
    }
    
    public void execute(Player p, float f) {
        if (SkillMgr.isCooldown(p, getName())) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        
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
        loc.setPitch((float) MoreMath.angleToPitch((int) A));
        
        final Vector direction = loc.getDirection().multiply(1);
        Fireball fireball = p.getWorld().spawn(loc.add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
        fireball.setShooter(p); fireball.setYield(0);
        
        FireballTimer.addFireball(fireball, 30);
    }
    
    public boolean isCooldown() {
        return false;
    }
    
    public boolean isLevel(Player p) {
        Race race = RaceMgr.getRace(p);
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

package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;

public class ArcherRain implements Skill {
    int cooldown =   0;
    int lvlReq   =  20;
    
    Random rand = new Random();
    
    public String getName() {
        return "Arrow Rain";
    }
    
    public void execute(final Player p, final float f) {
        if (SkillMgr.isCooldown(p, getName())) { new NoneArrow().execute(p, f); return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        for (int i = 1; i <= 360; i += 1) {
            Location eye = p.getEyeLocation().clone();
            eye.setYaw(i-1); eye.setPitch(-90 + (float)rand.nextInt(1500)/100);
            
            final Vector direction = eye.getDirection().multiply(2);
            final Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
            arrow.setVelocity(direction.multiply(f)); arrow.setShooter(p);// arrow.setFireTicks(200);
            
            ArrowTimer.add(arrow, 150);
        }
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

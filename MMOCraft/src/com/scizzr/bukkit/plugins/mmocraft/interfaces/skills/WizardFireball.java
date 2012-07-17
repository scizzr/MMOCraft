package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.plugins.mmocraft.timers.FireballTimer;

public class WizardFireball implements Skill {
    int cooldown =  40;
    int lvlReq   =  20;
    
    Random rand = new Random();
    
    public String getName() {
        return "Arrow Rain";
    }
    
    public void execute(Player p, float f) {
        if (SkillMgr.isCooldown(p, getName())) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        Location loc = p.getLocation();
        Vector direction = loc.getDirection();
        
        Fireball fb = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
        fb.setVelocity(direction); fb.setShooter(p); fb.setYield(0); fb.setIsIncendiary(false);
        
        FireballTimer.addFireball(fb, 30);
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

package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Location;
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

public class WizardFireball implements Skill {
    int cooldown =  20;
    int lvlReq   =  20;
    
    Random rand = new Random();
    
    public String getName() {
        return "Fireball";
    }
    
    public void execute(Player p, Entity ent, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        Location loc = p.getLocation();
        Vector direction = loc.getDirection();
        
        Fireball fb = (Fireball)p.getWorld().spawnEntity(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), EntityType.FIREBALL);
        fb.setVelocity(direction); fb.setShooter(p); fb.setYield(5); fb.setIsIncendiary(false);
        
        SoundEffects.MOB_GHAST_FIREBALL.playGlobal(p.getLocation(), 0.5f, 1.0f);
        
        FireballTimer.addFireball(fb, 30);
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

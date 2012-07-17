package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;

public class NoneArrow implements Skill {
    int cooldown =   0;
    int lvlReq   =   0;
    
    public String getName() {
        return "Arrow";
    }
    
    public void execute(Player p, float f) {
        //if (SkillManager.isCooldown(p, getName())) { new ArcherArrow().execute(p, f); return; } else { SkillManager.addCooldown(p, getName(), cooldown); }
        Location eye = p.getEyeLocation().clone();
        
        final Vector direction = eye.getDirection();
        final Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
        arrow.setVelocity(direction.multiply(3.0f)); arrow.setShooter(p);
        
        if (p.getGameMode() == GameMode.SURVIVAL) { CraftArrow ca = (CraftArrow)arrow; ca.getHandle().fromPlayer = true; }
        
        ArrowTimer.add(arrow, 100);
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

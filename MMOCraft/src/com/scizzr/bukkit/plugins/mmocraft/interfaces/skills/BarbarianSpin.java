package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.plugins.mmocraft.timers.SpinTimer;

public class BarbarianSpin implements Skill {
    int cooldown =   0;
    int lvlReq   =  20;
    
    Random rand = new Random();
    
    public String getName() {
        return "Axe Whirl";
    }
    
    public void execute(Player p, float f) {
        if (SkillMgr.isCooldown(p, getName())) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        SpinTimer.spin(p);
        for (Entity en : p.getNearbyEntities(3, 3, 3)) {
            if (en instanceof LivingEntity) {
                ((LivingEntity)en).damage((int)f);
            }
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

package com.scizzr.bukkit.mmocraft.skills;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.timers.SpinTimer;

public class BarbAxeWhirl implements Skill {
    int cooldown =  60;
    int lvlReq   =  20;
    
    Random rand = new Random();
    
    public String getName() {
        return "Axe Whirl";
    }
    
    public void execute(Player p, Entity ent, float f) {
        Race race = RaceMgr.getRace(p.getName());
        if (race.hasData("spinning")) { return; }
        
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        SpinTimer.spin(p);
        
        List<Entity> ents = p.getNearbyEntities(3, 3, 3);
        
        int num = ents.size();
        
        for (Entity en : p.getNearbyEntities(3, 3, 3)) {
            if (en instanceof LivingEntity) {
                ((LivingEntity) en).damage((int)(f/num ), p);
            }
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

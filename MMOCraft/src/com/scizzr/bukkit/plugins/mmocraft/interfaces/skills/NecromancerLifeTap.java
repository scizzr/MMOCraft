package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;

public class NecromancerLifeTap implements Skill {
    int cooldown = 200;
    int lvlReq   =   0;
    
    public String getName() {
        return "Life Tap";
    }
    
    public void execute(Player p, float f) {
        //if (SkillManager.isCooldown(p, getName())) { new ArcherArrow().execute(p, f); return; } else { SkillManager.addCooldown(p, getName(), cooldown); }
        //for () {
            //lifetap every enemy in a 3 block radius for 1/2 heart each
        //}
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

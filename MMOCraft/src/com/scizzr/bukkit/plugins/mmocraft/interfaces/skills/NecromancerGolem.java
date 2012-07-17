package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;

public class NecromancerGolem implements Skill {
    int cooldown = 100;
    int lvlReq   =  10;
    
    public String getName() {
        return "Summon Golem";
    }
    
    public void execute(Player p, float f) {
        //if (SkillManager.isCooldown(p, getName())) { new ArcherArrow().execute(p, f); return; } else { SkillManager.addCooldown(p, getName(), cooldown); }
        Location loc = p.getTargetBlock(null, 120).getLocation().add(0, 1, 0);
        loc.getWorld().spawnCreature(loc, EntityType.IRON_GOLEM);
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

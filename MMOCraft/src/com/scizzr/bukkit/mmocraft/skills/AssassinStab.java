package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;

public class AssassinStab implements Skill {
    int cooldown = Config.assSklStabCd;
    int lvlReq   = Config.assSklStabLvl;
    
    Random rand = new Random();
    
    public String getName() {
        return "Stab";
    }
    
    public void execute(Player p, Entity ent, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        //p.sendMessage(Main.prefix + "You stabbed your enemy.");
        
        final Location loc = ent.getLocation();
        
        loc.getWorld().playEffect(loc, Effect.ZOMBIE_DESTROY_DOOR, 5);
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

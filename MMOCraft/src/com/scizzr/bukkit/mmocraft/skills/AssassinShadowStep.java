package com.scizzr.bukkit.mmocraft.skills;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;

public class AssassinShadowStep implements Skill {
    int cooldown = Config.assSklShStepCd;
    int lvlReq   = Config.assSklShStepLvl;
    
    public String getName() {
        return "Shadow Step";
    }
    
    public void execute(final Player p, Entity ent, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        Location to = ent.getLocation().clone(); to.setPitch(p.getLocation().getPitch());
        Location vel = to.clone(); vel.setYaw(to.getYaw()+180);
        p.teleport(to); p.setVelocity(vel.multiply(1).getDirection());
        
        SoundEffects.MOB_COWHURT.play(p, p.getLocation(), 1.0f, 2.0f);
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

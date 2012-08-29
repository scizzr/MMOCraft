package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.threads.Lightning;

public class WizardLightning implements Skill {
    int cooldown =  10;
    int lvlReq   =  10;
    
    Random rand = new Random();
    
    public String getName() {
        return "Lightning";
    }
    
    public void execute(Player p, Entity ent, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        try { new Thread(new Lightning(p, 5)).start(); } catch (Exception ex) { /* No Spam */ }
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

package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import java.util.Random;

import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.threads.Lightning;

public class WizardLightning implements Skill {
    int cooldown =  20;
    int lvlReq   =  10;
    
    Random rand = new Random();
    
    public String getName() {
        return "Arrow Rain";
    }
    
    public void execute(Player p, float f) {
        try { new Thread(new Lightning(p)).start(); } catch (Exception ex) { /* No Spam */ }
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

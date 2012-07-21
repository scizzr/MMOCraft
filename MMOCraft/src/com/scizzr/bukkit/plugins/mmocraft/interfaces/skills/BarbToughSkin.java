package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;

public class BarbToughSkin implements Skill {
    int cooldown =  60;
    int lvlReq   =   0;
    
    Random rand = new Random();
    
    public String getName() {
        return "Tough Skin";
    }
    
    public void execute(final Player p, final float f) {
        if (isCooldown(p)) { new NoneArrow().execute(p, f); return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        
        p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_CHEW_IRON_DOOR, 1);
        
        for (int i = 1; i <= 600; i += 10) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
                public void run() {
                    if (p.isOnline()) {
                        Location loc = p.getLocation();
                        p.getWorld().playEffect(loc.clone().add(0, 1, 0), Effect.STEP_SOUND, 51);
                    } else {
                        return;
                    }
                }
            }, i);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
            public void run() {
                //Race race = RaceMgr.getRace(p);
                //race.removeBuff("ToughSkin");
            }
        }, 600);
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

package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;

public class AssassinSneakAttack implements Skill {
    int cooldown = Config.assSklSneAttCd;
    int lvlReq   = Config.assSklSneAttLvl;
    
    Random rand = new Random();
    
    public String getName() {
        return "Sneak Attack";
    }
    
    public void execute(Player p, Entity ent, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        //p.sendMessage(Main.prefix + "Sneak attack!");
        
        final Location loc = p.getLocation().clone();
        Vector direction = loc.getDirection();
        ent.setVelocity(direction.multiply(2.0F));
        
        for (int i = 1; i <= 5; i ++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                public void run() {
                    loc.getWorld().playEffect(loc, Effect.ZOMBIE_CHEW_WOODEN_DOOR, 1);
                }
            }, (long)i);
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

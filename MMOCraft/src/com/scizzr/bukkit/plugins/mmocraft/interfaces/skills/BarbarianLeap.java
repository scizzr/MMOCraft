package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;

public class BarbarianLeap implements Skill {
    int cooldown =   0;
    int lvlReq   =  20;
    
    Random rand = new Random();
    
    public String getName() {
        return "Leap Attack";
    }
    
    public void execute(final Player p, final float f) {
        if (SkillMgr.isCooldown(p, getName())) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (p.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            Location loc = p.getLocation().clone(); loc.setPitch(-90);
            p.setVelocity(loc.getDirection());
            for (final Entity ent : p.getNearbyEntities(3, 3, 3)) {
                if (ent instanceof LivingEntity) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.pm.getPlugin(Main.info.getName()), new Runnable() {
                        public void run() {
                            Location loc = ent.getLocation().clone(); loc.setPitch(-90);
                            ((LivingEntity)ent).setVelocity(loc.getDirection());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.pm.getPlugin(Main.info.getName()), new Runnable() {
                                public void run() {
                                    EntityMgr.setAttacker(ent, p);
                                    ((LivingEntity)ent).damage((int)f);
                                }
                            }, 30L);
                        }
                    }, 20L);
                }
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

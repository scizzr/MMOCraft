package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;

public class AssassinPowerUp implements Skill {
    int cooldown = 600;
    int lvlReq   =   0;
    
    Random rand = new Random();
    
    public String getName() {
        return "Power Up";
    }
    
    public void execute(final Player p, final float f) {
        if (SkillMgr.isCooldown(p, getName())) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        try {
            float num = 0;
            for (Entity ent : p.getNearbyEntities(3, 3, 3)) {
                if (!(ent instanceof LivingEntity)) { continue; }
                
                LivingEntity lent = (LivingEntity)ent;
                lent.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 1));
                p.getWorld().playEffect(ent.getLocation(), Effect.ENDER_SIGNAL, 1);
                
                num += 1.0f;
                
                EntityMgr.setAttacker(ent, p);
            }
            if (num > 0) {
                final int total = (int)num;
                Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.pm.getPlugin(Main.info.getName()), new Runnable() {
                    public void run () {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, total*10, (total/10) <= 3 ? (total/10) : 3));
                        p.getWorld().playEffect(p.getLocation(), Effect.POTION_BREAK, 1);
                        p.sendMessage(Main.prefix + "Leeched " + total + " enemies");
                    }
                }, 40L);
            }
        } catch (Exception ex) {
            /* No Spam */
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

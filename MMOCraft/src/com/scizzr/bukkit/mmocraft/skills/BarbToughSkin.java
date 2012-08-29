package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class BarbToughSkin implements Skill {
    int cooldown = 600;
    int lvlReq   =   0;
    
    Random rand = new Random();
    
    public String getName() {
        return "Tough Skin";
    }
    
    public void execute(final Player p, Entity ent, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        
        if (RaceMgr.getRace(p.getName()).hasData("toughSkin")) { return; }
        
        for (int i = 1; i <= 3; i++) {
            p.playEffect(p.getLocation(), Effect.ZOMBIE_CHEW_IRON_DOOR, 1);
        }
        
        p.sendMessage(MMOCraft.prefix + I18n._("skillbartoughskinon", new Object[] {}));
        
        int tid = Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
            public void run() {
                for (int i = 1; i <= 600; i += 10) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                        public void run() {
                            if (p.isOnline()) {
                                Location loc = Bukkit.getPlayer(p.getName()).getLocation();
                                p.playEffect(loc.add(0, 1, 0), Effect.STEP_SOUND, 51);
                            } else {
                                return;
                            }
                        }
                    }, i);
                }
            }
        }, 1L);
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
            public void run() {
                Race race = RaceMgr.getRace(p.getName());
                if (p.isOnline()) {
                    p.sendMessage(MMOCraft.prefix + I18n._("skillbartoughskinoff", new Object[] {}));
                }
                race.setData("toughSkin", null);
            }
        }, 600);
        
        Race race = RaceMgr.getRace(p.getName());
        race.setData("toughSkin", tid);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1));
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

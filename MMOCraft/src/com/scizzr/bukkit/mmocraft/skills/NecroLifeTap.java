package com.scizzr.bukkit.mmocraft.skills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class NecroLifeTap implements Skill {
    int cooldown = 300;
    int lvlReq   =   0;
    
    public String getName() {
        return "Life Tap";
    }
    
    public void execute(Player p, Entity unused, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        int total = 0;
        for (Entity ent : p.getNearbyEntities(3, 3, 3)) {
            if (ent instanceof LivingEntity) {
                LivingEntity lent = (LivingEntity)ent;
                int hp = lent.getHealth();
                if (hp > 1) {
                    lent.damage(1, p);
                    total++;
                }
                lent.setHealth(lent.getHealth()-1);
                EntityMgr.setAttacker(ent, p);
            }
        }
        
        int leech = total-(total-(20-p.getHealth()));
        
        p.setHealth(p.getHealth() + leech);
        
        SoundEffects.RANDOM_BREATH.playGlobal(p.getLocation(), 1.0f, 0.5f);
        
        p.sendMessage(MMOCraft.prefix + I18n._("skillnectap", new Object[] {leech, (float)leech/2 + " " + I18n._("heart", new Object[] {}) + (leech > 2 || leech == 0 ? "s" : "")}));
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

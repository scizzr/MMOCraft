package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.hooks.HookVanish;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class AssassinStealth implements Skill {
    int cooldown = Config.assSklStealtCd;
    int lvlReq   = Config.assSklStealtLvl;
    int lasts    = 200;
    
    Random rand = new Random();
    
    public String getName() {
        return "Stealth";
    }
    
    public void execute(final Player p, Entity unused, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        if (RaceMgr.getRace(p.getName()).hasData("invis")) { return; }
        
        for (Player other : Bukkit.getOnlinePlayers()) {
            other.hidePlayer(p);
        }
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
            public void run() {
                RaceMgr.getRace(p.getName()).setData("invis", null);
                
                for (final Player other : Bukkit.getOnlinePlayers()) {
                    Player hide = EntityMgr.getOnlinePlayer(other.getName());
                    Player look = EntityMgr.getOnlinePlayer(p.getName());
                    if (hide != null && look != null) {
                        if (look.equals(hide)) { continue; }
                        if (look.getWorld() == hide.getWorld()) {
                            if (HookVanish.canSee(other, p)) {
                                other.showPlayer(p);
                            }
                        }
                    }
                }
            }
        }, lasts);
        
//XXX: Config - Notify when they go invisible and visible
        //if () { //invis
            p.sendMessage(MMOCraft.prefix + I18n._("skillinvison", new Object[] {}));
            SoundEffects.PORTAL_TRAVEL.play(p, p.getLocation(), 0.3f, 2.0f);
        //}
        //if () { //vis
            int tid = Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                public void run() {
                    if (p != null) {
                        p.sendMessage(MMOCraft.prefix + I18n._("skillinvisoff", new Object[] {}));
                        SoundEffects.PORTAL_TRIGGER.play(p, p.getLocation(), 25f, 2.5f);
                    }
                }
            }, lasts);
            
            RaceMgr.getRace(p.getName()).setData("invis", tid);
            
        //}
//XXX
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

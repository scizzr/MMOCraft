package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.Main;
import com.scizzr.bukkit.mmocraft.hooks.Vanish;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class AssassinStealth implements Skill {
    int cooldown = 600;
    int lvlReq   =  10;
    int lasts    = 200;
    
    Random rand = new Random();
    
    public String getName() {
        return "Stealth";
    }
    
    public void execute(final Player p, Entity unused, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        if (RaceMgr.getRace(p.getName()).hasData("invis")) { return; }
        
        for (Entity ent : p.getNearbyEntities(10, 10, 10)) {
            if (ent instanceof Player) {
                Player other = (Player)ent;
                other.hidePlayer(p);
            }
        }
        for (final Player other : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
                public void run() {
                    Player hide = EntityMgr.getOnlinePlayer(other.getName());
                    Player look = EntityMgr.getOnlinePlayer(p.getName());
                    if (hide != null && look != null) {
                        if (Vanish.canSee(other, p)) {
                            other.showPlayer(p);
                        }
                    }
                }
            }, lasts);
        }
//XXX: Config - Notify when they go invisible and visible
        //if () {
            p.sendMessage(Main.prefix + I18n._("skillinvison", new Object[] {}));
        //}
        //if () {
            int tid = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
                public void run() {
                    Player play = EntityMgr.getOnlinePlayer(p.getName());
                    if (play != null) {
                        play.sendMessage(Main.prefix + I18n._("skillinvisoff", new Object[] {}));
                        RaceMgr.getRace(p.getName()).setData("invis", null);
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

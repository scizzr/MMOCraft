package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.scizzr.bukkit.plugins.mmocraft.classes.Archer;
import com.scizzr.bukkit.plugins.mmocraft.classes.Assassin;
import com.scizzr.bukkit.plugins.mmocraft.classes.Barbarian;
import com.scizzr.bukkit.plugins.mmocraft.classes.Druid;
import com.scizzr.bukkit.plugins.mmocraft.classes.Necromancer;
import com.scizzr.bukkit.plugins.mmocraft.classes.Wizard;

public class SkillManager {
    private static ConcurrentHashMap<String, Integer> cooldowns = new ConcurrentHashMap<String, Integer>();

    public static void doAttackLeft(Player p, Entity t) {
        String c = ClassManager.getClass(p);
        if (c != null) {
            if (c.equalsIgnoreCase("archer")) { Archer.attackLeft(p, t); }
            if (c.equalsIgnoreCase("assassin")) { Assassin.attackLeft(p, t); }
            if (c.equalsIgnoreCase("barbarian")) { Barbarian.attackLeft(p, t); }
            if (c.equalsIgnoreCase("druid")) { Druid.attackLeft(p, t); }
            if (c.equalsIgnoreCase("necromancer")) { Necromancer.attackLeft(p, t); }
            if (c.equalsIgnoreCase("wizard")) { Wizard.attackLeft(p, t); }
        }
    }
    
    public static void doAttackRight(Player p, Action a) {
        String c = ClassManager.getClass(p);
        if (c != null) {
            if (c.equalsIgnoreCase("archer")) { Archer.attackRight(p, a); }
            if (c.equalsIgnoreCase("assassin")) { Assassin.attackRight(p, a); }
            if (c.equalsIgnoreCase("barbarian")) { Barbarian.attackRight(p, a); }
            if (c.equalsIgnoreCase("druid")) { Druid.attackRight(p, a); }
            if (c.equalsIgnoreCase("necromancer")) { Necromancer.attackRight(p, a); }
            if (c.equalsIgnoreCase("wizard")) { Wizard.attackRight(p, a); }
        }
    }
    
    public static void doAttackBow(Player p, EntityShootBowEvent e) {
        String c = ClassManager.getClass(p);
        if (c != null) {
            if (c.equalsIgnoreCase("archer")) { Archer.attackBow((Player) e.getEntity(), e.getForce()); e.setCancelled(true); }
        }
    }
    
    public static void addCooldown(Player p, String skill, Integer dur) {
        //if (!(p.isOp() == false || Vault.hasPermission(p, "bypass.cooldown"))) {
            cooldowns.put(p.getName() + "=" + skill, dur);
        //}
    }
    
    public static boolean isCooldown(Player p, String skill) {
        for (String s : cooldowns.keySet()) {
            String[] split = s.split("=");
            if (split[0].equalsIgnoreCase(p.getName()) && split[1].equalsIgnoreCase(skill)) {
                return true;
            }
        }
        return false;
    }

    public static void tickCooldown() {
        for (String s : cooldowns.keySet()) {
            int i = cooldowns.get(s)-1;
            if (i > 0) { cooldowns.put(s, i); } else { cooldowns.remove(s); }
        }
    }
}

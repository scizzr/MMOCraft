package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.classes.Archer;
import com.scizzr.bukkit.plugins.mmocraft.enums.Colors;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Helper;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.util.Vault;

public class SkillMgr {
    private static ConcurrentHashMap<String, Integer> cooldowns = new ConcurrentHashMap<String, Integer>();
    
    public static void doAttackLeft(Player p, PlayerInteractEvent e) {
        Race race = RaceMgr.getRace(p);
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (HelperMgr.isHelper(b)) {
                Helper help = HelperMgr.getHelper(b);
                //TODO: Permission to allow breaking other player's helpers
                if (!(help.getOwner() == p || p.isOp() || Vault.hasPermission(p, ""))) {
                    p.sendMessage(Main.prefix + Colors.WARN + "You are not the owner of this " + help.getName());
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if (race != null) {
            race.attackLeft(p, e.getAction());
        }
    }
    
    public static void doAttackRight(Player p, PlayerInteractEvent e) {
        Race race = RaceMgr.getRace(p);
        if (race != null) {
            race.attackRight(p, e.getAction());
        }
    }
    
    public static void doAttackBow(Player p, EntityShootBowEvent e) {
        Race race = RaceMgr.getRace(p);
        if (race != null) {
            if (race instanceof Archer) {
                race.attackBow(p, e.getForce());
                e.setCancelled(true);
            }
        }
    }
    
    public static void doAttackEntity(Player p, PlayerInteractEntityEvent e) {
        Race race = RaceMgr.getRace(p);
        if (race != null) {
            race.attackEntity(p, e.getRightClicked());
        }
    }
    
    public static void addCooldown(Player p, String skill, Integer dur) {
        if (!(p.isOp() == false || Vault.hasPermission(p, "bypass.cooldown"))) {
            cooldowns.put(p.getName() + "=" + skill, dur);
        }
    }
    
    public static boolean isCooldown(Player p, String skill) {
        for (String s : cooldowns.keySet()) {
            String[] split = s.split("=");
            if (split[0].equalsIgnoreCase(p.getName()) && split[1].equalsIgnoreCase(skill)) {
                p.sendMessage(Main.prefix + Colors.WARN + split[0] + " is on cooldown for " + split[1] + " more seconds.");
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

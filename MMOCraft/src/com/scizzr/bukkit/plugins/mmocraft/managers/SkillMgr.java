package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Helper;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.classes.Archer;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreString;
import com.scizzr.bukkit.plugins.mmocraft.util.Vault;

public class SkillMgr {
    private static ConcurrentHashMap<String, Integer> cooldowns = new ConcurrentHashMap<String, Integer>();
    
    public static void doAttackLeft(Player p, PlayerInteractEvent e) {
        Race race = RaceMgr.getRace(p.getName());
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (HelperMgr.isHelper(b)) {
                Helper help = HelperMgr.getHelper(b);
                //XXX: Permission to allow breaking other player's helpers
                if (!(help.getPlayerName() == p.getName() ||  Vault.hasPermission(p, ""))) {
                    p.sendMessage(Main.prefix + ChatColor.RED + "You are not the owner of this " + help.getName());
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
        Race race = RaceMgr.getRace(p.getName());
        if (race != null) {
            race.attackRight(p, e.getAction());
        }
    }
    
    public static void doAttackBow(Player p, EntityShootBowEvent e) {
        Race race = RaceMgr.getRace(p.getName());
        if (race != null) {
            if (race instanceof Archer) {
                race.attackBow(p, e.getForce());
                e.setCancelled(true);
            }
        }
    }
    
    public static void doInteractEntity(Player p, PlayerInteractEntityEvent e) {
        Entity ent = e.getRightClicked();
        LivingEntity lent = (LivingEntity)ent;
        
        Race race = RaceMgr.getRace(p.getName());
        
        if (p.isSneaking()) {
            if (PetMgr.isPet(lent)) {
                Pet pet = PetMgr.getPet(lent.getUniqueId());
                if (pet.getOwnerName() == p.getName()) {
                    PetMgr.removePet(pet, true, true);
                    lent.remove();
                    return;
                }
            }
        }
        
        if (lent instanceof Creature) {
            if (PetMgr.isPet(lent)) {
                Pet pet = PetMgr.getPet(lent.getUniqueId());
                if (p.getName() != pet.getOwnerName()) {
                    p.sendMessage(Main.prefix + "[" + MoreString.HpFoodBars(pet.getHealth(), ChatColor.DARK_RED, ChatColor.RED, ChatColor.BLACK, true, false, true, false) + "] " + RaceMgr.getRace(pet.getOwnerName()).getColor() + pet.getOwnerName() + ChatColor.RESET + "'s " + pet.getName());
                } else {
                    p.sendMessage(Main.prefix + "[" + MoreString.HpFoodBars(pet.getHealth(), ChatColor.DARK_RED, ChatColor.RED, ChatColor.BLACK, true, false, true, false) + "] Pet " + pet.getName());
                }
            } else {
                p.sendMessage(Main.prefix + "[" + MoreString.HpFoodBars(lent.getHealth(), ChatColor.DARK_RED, ChatColor.RED, ChatColor.BLACK, true, false, true, false) + "] " + lent.getType().getName() + (lent instanceof Villager ? " (" + ((Villager)lent).getProfession() + ")" : ""));
            }
        } else {
            if (lent instanceof Player) {
                p.sendMessage(Main.prefix + "[" + MoreString.HpFoodBars(lent.getHealth(), ChatColor.DARK_RED, ChatColor.RED, ChatColor.BLACK, true, false, true, false) + "] " + ((Player)lent).getName() + " [" + MoreString.HpFoodBars(((Player) lent).getFoodLevel(), ChatColor.DARK_BLUE, ChatColor.BLUE, ChatColor.BLACK, false, false, true, false) + "]");
            }
        }
        
        if (race != null) {
            race.interactEntity(p, lent);
        }
    }
    
    public static void doAttackEntity(Player p, EntityDamageByEntityEvent e) {
        Race race = RaceMgr.getRace(p.getName());
        if (race != null) {
            race.attackEntity(p, e);
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
                p.sendMessage(Main.prefix + ChatColor.YELLOW + split[0] + " is on cooldown for " + split[1] + " more seconds.");
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

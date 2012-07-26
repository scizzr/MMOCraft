package com.scizzr.bukkit.mmocraft.managers;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.scizzr.bukkit.mmocraft.Main;
import com.scizzr.bukkit.mmocraft.classes.Archer;
import com.scizzr.bukkit.mmocraft.classes.Assassin;
import com.scizzr.bukkit.mmocraft.hooks.Vanish;
import com.scizzr.bukkit.mmocraft.hooks.Vault;
import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class SkillMgr {
    private static ConcurrentHashMap<String, Integer> cooldowns = new ConcurrentHashMap<String, Integer>();
    
    public static void doAttackLeft(Player p, PlayerInteractEvent e) {
        Race race = RaceMgr.getRace(p.getName());
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (AidMgr.isAid(b)) {
                Aid aid = AidMgr.getAid(b);
//XXX: Permission to allow breaking other player's helpers
                if (!(aid.getOwnerName() == p.getName() ||  Vault.hasPermission(p, ""))) {
                    p.sendMessage(Main.prefix + ChatColor.RED + I18n._("aidnotown", new Object[] {aid.getName()}));
                    e.setCancelled(true);
                    return;
                }
            }
        }
        race.attackLeft(p, e.getAction());
    }
    
    public static void doAttackRight(Player p, PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (AidMgr.isAid(b)) {
                Aid aid = AidMgr.getAid(b);
                
                p.sendMessage(Main.prefix + Util.displayAidInfo(p, aid));
            }
        }
        Race race = RaceMgr.getRace(p.getName());
        race.attackRight(p, e.getAction());
    }
    
    public static void doAttackBow(Player p, EntityShootBowEvent e) {
        Race race = RaceMgr.getRace(p.getName());
        if (race instanceof Archer) {
            race.attackBow(p, e.getForce());
            e.setCancelled(true);
        }
    }
    
    public static void doAttackEntity(Player p, EntityDamageByEntityEvent e) {
        Race race = RaceMgr.getRace(p.getName());
        race.attackEntity(p, e);
    }
    
    public static void doInteractEntity(Player p, PlayerInteractEntityEvent e) {
        Entity ent = e.getRightClicked();
        if (!(ent instanceof LivingEntity)) { return; }
        
        LivingEntity lent = (LivingEntity)ent;
        
        Race race = RaceMgr.getRace(p.getName());
        
        if (p.isSneaking()) {
            if (PetMgr.isPet(lent)) {
                Pet pet = PetMgr.getPet(lent.getUniqueId());
                if (pet.getOwnerName().equalsIgnoreCase(p.getName())) {
                    PetMgr.removePet(pet, true, true);
                    lent.remove();
                    return;
                }
            }
        } else {
            if (lent instanceof Creature) {
                if (PetMgr.isPet(lent)) {
                    Pet pet = PetMgr.getPet(lent.getUniqueId());
                    p.sendMessage(Main.prefix + Util.displayPetInfo(p, pet));
                } else {
                    p.sendMessage(Main.prefix + Util.displayEntityInfo(p, lent));
                }
            } else {
                if (lent instanceof Player) {
                    p.sendMessage(Main.prefix + Util.displayPlayerInfo((Player)lent));
                }
            }
        }
        
        race.interactEntity(p, lent);
    }
    
    public static void doSneak(Player p, PlayerToggleSneakEvent e) {
        Race race = RaceMgr.getRace(p.getName());
        if (race != null) {
            race.toggleSneak(p, e);
        }
    }
    
    public static void breakInvis(Player p) {
        Race race = RaceMgr.getRace(p.getName());
        if (race instanceof Assassin) {
            if (race.hasData("invis")) {
                int tid = Integer.valueOf(race.getData("invis"));
                Bukkit.getScheduler().cancelTask(tid);
                race.setData("invis", null);
                p.sendMessage(Main.prefix + I18n._("skillinvisoff", new Object[] {}));
                for (World world : Bukkit.getWorlds()) {
                    for (Entity ent : world.getEntities()) {
                        if (ent instanceof Player) {
                            Player other = (Player)ent;
                            if (Vanish.canSee(other,  p)) {
                                other.showPlayer(p);
                            }
                        }
                    }
                }
            }
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
                p.sendMessage(Main.prefix + ChatColor.YELLOW + I18n._("skillcooldown", new Object[] {split[0], split[1]}));
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

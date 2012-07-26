package com.scizzr.bukkit.mmocraft.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.scizzr.bukkit.mmocraft.Main;
import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.hooks.Vanish;
import com.scizzr.bukkit.mmocraft.hooks.Vault;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.managers.AidMgr;
import com.scizzr.bukkit.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.threads.Update;
import com.scizzr.bukkit.mmocraft.timers.ArrowTimer;

public class Players implements Listener {
    Location src;
    Location dest;
    
    
    Main plugin;
    
    public Players(Main instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            SkillMgr.doAttackLeft(e.getPlayer(), e);
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            SkillMgr.doAttackRight(e.getPlayer(), e);
        } else if (e.getAction() == Action.PHYSICAL) {
            Entity ent = e.getPlayer();
            Location loc = e.getClickedBlock().getLocation().clone();
            Block b = loc.getBlock();
            if (AidMgr.isAid(b)) { AidMgr.springWizardTrap(ent, b); }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
        SkillMgr.doInteractEntity(e.getPlayer(), e);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Player p = e.getPlayer();
        
        if (Config.genVerCheck == true && Vault.hasPermission(p, "newver")) {
            new Thread(new Update("check", p, null)).start();
        }
        
        for (Player pp : Bukkit.getOnlinePlayers()) {
            if (pp.getLocation().distance(p.getLocation()) <= 32) {
                Race race = RaceMgr.getRace(pp.getName());
                if (race.hasData("invis")) {
                    p.hidePlayer(pp);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(final PlayerLoginEvent e) {
        Player p = e.getPlayer();
        Race race = RaceMgr.getRace(p.getName());
        if (race == null) { RaceMgr.setRace(p.getName(), "None"); }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPickupItem(final PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        Item it = e.getItem();
        ItemStack is = it.getItemStack();
        
        if (is.getType() == Material.ARROW) {
            UUID id = it.getUniqueId();
            for (Entity ent : p.getWorld().getEntitiesByClass(Arrow.class)) {
                if (((Arrow)ent).getUniqueId().equals(id)) {
                    ArrowTimer.remove((Arrow)ent); return;
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        Player p = e.getPlayer();
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (Vanish.canSee(other, p)) {
                other.showPlayer(p);
            }
        }
        if (RaceMgr.getRace(p.getName()).hasData("invis")) {
            int tid = Integer.valueOf(RaceMgr.getRace(p.getName()).getData("invis"));
            Bukkit.getScheduler().cancelTask(tid);
            RaceMgr.getRace(p.getName()).setData("invis", null);
        }
        if (RaceMgr.getRace(p.getName()).hasData("toughskin")) {
            int tid = Integer.valueOf(RaceMgr.getRace(p.getName()).getData("toughskin"));
            Bukkit.getScheduler().cancelTask(tid);
            RaceMgr.getRace(p.getName()).setData("toughskin", null);
        }
        PetMgr.removeAllPlayerPets(p.getName(), false);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerToggleSneak(final PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (!p.isSneaking()) {
            SkillMgr.doSneak(p, e);
        }
    }
}

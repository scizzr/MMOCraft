package com.scizzr.bukkit.plugins.mmocraft.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.classes.Wizard;
import com.scizzr.bukkit.plugins.mmocraft.config.Config;
import com.scizzr.bukkit.plugins.mmocraft.config.PlayerData;
import com.scizzr.bukkit.plugins.mmocraft.managers.CheatManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;
import com.scizzr.bukkit.plugins.mmocraft.threads.Website;

public class Players implements Listener {
    Main plugin;
    
    public Players(Main instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(final PlayerLoginEvent e) {
        PlayerData.checkAll(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            CheatManager.addClick(e.getPlayer());
            SkillManager.doAttackRight(e.getPlayer(), e.getAction());
        } else if (e.getAction() == Action.PHYSICAL) {
            Entity ent = e.getPlayer();
            Location loc = e.getClickedBlock().getLocation().clone();
            Block b = loc.getBlock();
            Wizard.stepTrap(ent, b);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
/*
        Player p = e.getPlayer();
        Entity t = e.getRightClicked();
        
        if (p.getItemInHand().getType() == Material.IRON_INGOT) {
            p.sendMessage("Iron");
        }
        
        if (t instanceof Animals) {
            p.sendMessage("Animal");
        }
        
        if (t instanceof Monster) {
            p.sendMessage("Monster");
        }
        
        if (t instanceof Player) {
            p.sendMessage("Player");
        }
*/
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Player p = e.getPlayer();
        
        if (Config.genVerCheck == true) {
            new Thread(new Website("checkVersion", p)).start();
        }
    }
}

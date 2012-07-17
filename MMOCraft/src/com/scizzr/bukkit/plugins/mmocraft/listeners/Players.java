package com.scizzr.bukkit.plugins.mmocraft.listeners;

import java.util.UUID;

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
import org.bukkit.inventory.ItemStack;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.config.Config;
import com.scizzr.bukkit.plugins.mmocraft.config.PlayerData;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.plugins.mmocraft.threads.Update;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;
import com.scizzr.bukkit.plugins.mmocraft.util.Vault;

public class Players implements Listener {
    Location src;
    Location dest;
    
    
    Main plugin;
    
    public Players(Main instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(final PlayerLoginEvent e) {
        PlayerData.checkAll(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Player p = e.getPlayer();
        
        if (Config.genVerCheck == true && Vault.hasPermission(p, "newver")) {
            new Thread(new Update("check", p, null)).start();
        }
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
            if (HelperMgr.isHelper(b)) {
                HelperMgr.springWizardTrap(ent, b);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
        SkillMgr.doAttackEntity(e.getPlayer(), e);
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
}

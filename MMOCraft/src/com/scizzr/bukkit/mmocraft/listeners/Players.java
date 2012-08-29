package com.scizzr.bukkit.mmocraft.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
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

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.hooks.HookVanish;
import com.scizzr.bukkit.mmocraft.hooks.HookVault;
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
    
    MMOCraft plugin;
    
    public Players(MMOCraft instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        Player p = e.getPlayer();
        
        /* Sign Commands -- Keeping this for debugging*/
        Block blk = e.getClickedBlock();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (blk.getType() == Material.SIGN_POST || blk.getType() == Material.WALL_SIGN)) {
            Sign sign = (Sign)blk.getState();
            String l0 = sign.getLine(0);
            
            if (l0.startsWith("/")) {
                String l1 = sign.getLine(1);
                String l2 = sign.getLine(2);
                String l3 = sign.getLine(3);
                
                String cmd = l0 + (l0.endsWith(" ") ? "" : " ") + l1 + (l1.endsWith(" ") ? "" : " ") + l2 + (l2.endsWith(" ") ? "" : " ") + l3;
                
                p.chat(cmd);
            }
        }
        
        /* Some debug code -- Might need this in the future.
        Block blk = e.getClickedBlock();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (blk.getType() == Material.SIGN || blk.getType() == Material.WALL_SIGN)) {
            p.sendMessage(ChatColor.RED + p.getName());
            try {
                YamlConfiguration config = new YamlConfiguration(); config.load(new File(Main.fileFolder + Main.slash + "data.yml"));
                for (String key : config.getConfigurationSection(p.getName().toLowerCase()).getKeys(false)) {
                    if (key.equals("race")) {
                        
                    } else if (key.equals("exp")) {
                        
                    } else if (key.equals("aids")) {
                        for (String subkey : config.getStringList(p.getName().toLowerCase() + "." + key)) {
                            p.sendMessage("    " + ChatColor.YELLOW + subkey);
                        }
                    } else if (key.equals("pets")) {
                        for (String subkey : config.getStringList(p.getName().toLowerCase() + "." + key)) {
                            p.sendMessage("    " + ChatColor.YELLOW + subkey);
                        }
                    }
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        */
        
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            SkillMgr.doAttackLeft(e.getPlayer(), e);
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            SkillMgr.doAttackRight(e.getPlayer(), e);
        } else if (e.getAction() == Action.PHYSICAL) {
            Location loc = e.getClickedBlock().getLocation().clone();
            Block b = loc.getBlock();
            if (AidMgr.isAid(b)) { AidMgr.springWizardTrap(p, b); }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Villager && e.getPlayer().isSneaking()) {
//XXX:Config - disable villager right-clicking for sneaking players?            if () {
                e.setCancelled(true);
//            }
        }
        
        SkillMgr.doInteractEntity(e.getPlayer(), e);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Player p = e.getPlayer();
        
        if (Config.genVerCheck == true && HookVault.hasPermission(p, "newver")) {
            new Thread(new Update("check", p, null)).start();
        }
        
        for (Player pp : Bukkit.getOnlinePlayers()) {
            if (!(pp.getWorld() == p.getWorld())) { continue; }
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
            if (HookVanish.canSee(other, p)) {
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

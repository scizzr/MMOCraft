package com.scizzr.bukkit.mmocraft.hooks;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class HookVault {
    private static Permission permissionHandler = null;
    private static Economy economyHandler = null;
    
    public static boolean setupPermissions() {
        if (MMOCraft.pm.getPlugin("Vault") != null) {
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            
            if (permissionProvider != null) {
                permissionHandler = permissionProvider.getProvider();
            }
            
            return (permissionHandler != null);
        }
        return false;
    }
    
    public static boolean setupEconomy() {
        if (MMOCraft.pm.getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            
            if (economyProvider != null) {
                economyHandler = economyProvider.getProvider();
            }
            
            return (economyHandler) != null;
        }
        return false;
    }
    
    public static boolean hasPermission(Player p, String perm) {
        if (Config.permAllowOps == true && p.isOp()) {
            return true;
        }
        
        if (permissionHandler != null) {
            if (permissionHandler.has(p, "base." + perm)) { return true; } else { return false; }
        } else {
            if (p.hasPermission("base." + perm)) { return true; } else { return false; }
        }
    }
    
    public static boolean hasMoney(Player p, Double amount, Boolean opt, String perm) {
        if (economyHandler != null) {
            if (opt == true) {
                if (!hasPermission(p, perm)) {
                    double balance = economyHandler.getBalance(p.getName());
                    if ( ( (balance - amount) >= 0.0 ) && ( amount > 0.0 ) ) {
                        economyHandler.withdrawPlayer(p.getName(), amount);
                        p.sendMessage(MMOCraft.prefix + I18n._("econpaid", new Object[] {formatPrice(amount, ChatColor.YELLOW)}));
                        return true;
                    } else { return false; }
                } else { return true; }
            } else { return true; }
        } else { return true; }
    }
    
    private static String formatPrice(Double amount, ChatColor color) {
        return color + economyHandler.format(amount) + ChatColor.RESET;
    }
}

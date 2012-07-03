package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.util.Vault;

public class CheatManager {
    private static ConcurrentHashMap<Player, Integer> clicks = new ConcurrentHashMap<Player, Integer>();
    
    public static void kick(Player p, String why) {
        p.kickPlayer(why);
    }
    
    public static void addClick(Player p) {
        if (clicks.containsKey(p)) {
            if (clicks.get(p) >= 300) {
                if (!(p.isOp() || Vault.hasPermission(p, "bypass.clicks"))) {
                    kick(p, "Clicking too rapidly");
                    clicks.remove(p);
                }
            } else {
                clicks.put(p, clicks.get(p) + 1);
            }
        } else {
            clicks.put(p, 1);
        }
    }
    
    public static void resetClicks() {
        clicks.clear();
    }
}

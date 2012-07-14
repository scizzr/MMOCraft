package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityManager {
    private static HashMap<Entity, Player> lastAttacker = new HashMap<Entity, Player>();
    
    public static void setAttacker(Entity def, Player att) {
        lastAttacker.put(def, att);
    }
    
    public static Player getAttacker(Entity def) {
        return lastAttacker.containsKey(def) ? lastAttacker.get(def) : null;
    }
    
    public static void remove(Entity def) {
        if (lastAttacker.containsKey(def)) {
            lastAttacker.remove(def);
        }
    }
    
    public static boolean playerExists(String who) {
        return Bukkit.getPlayer(who) != null;
    }
    
    public static Player getPlayer(String name) {
        for (Player onp : Bukkit.getOnlinePlayers()) {
            if (onp.getName().equalsIgnoreCase(name)) {
                return onp;
            }
        }
        for (Player onp : Bukkit.getOnlinePlayers()) {
            if (onp.getName().startsWith(name)) {
                return onp;
            }
        }
        return null;
    }
}

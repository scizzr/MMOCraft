package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.HashMap;

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
}

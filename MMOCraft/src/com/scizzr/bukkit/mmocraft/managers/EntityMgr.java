package com.scizzr.bukkit.mmocraft.managers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityMgr {
    private static HashMap<UUID, UUID> lastAttacker = new HashMap<UUID, UUID>();
    private static HashMap<UUID, Boolean> spawnerMobs = new HashMap<UUID, Boolean>();
    
    public static void setAttacker(Entity def, Entity att) {
        if (!isSpawnerMob(def.getUniqueId())) { lastAttacker.put(def.getUniqueId(), att.getUniqueId()); }
    }
    
    public static UUID getAttacker(Entity def) {
        return lastAttacker.containsKey(def.getUniqueId()) ? lastAttacker.get(def.getUniqueId()) : null;
    }
    
    public static void remove(Entity def) {
        if (lastAttacker.containsKey(def)) {
            lastAttacker.remove(def);
        }
    }
    
    public static Player getOnlinePlayer(String name) {
        for (Player onp : Bukkit.getOnlinePlayers()) {
            if (onp.getName().equalsIgnoreCase(name)) { return onp; }
        }
        for (Player onp : Bukkit.getOnlinePlayers()) {
            if (onp.getName().startsWith(name)) { return onp; }
        }
        return null;
    }
    
    public static OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer ofp : Bukkit.getOfflinePlayers()) {
            if (ofp.getName().equalsIgnoreCase(name)) { return ofp; }
        }
        for (OfflinePlayer ofp : Bukkit.getOfflinePlayers()) {
            if (ofp.getName().startsWith(name)) { return ofp; }
        }
        return null;
    }
    
    public static Entity getEntityByUUID(UUID id) {
        List<World> worlds = Bukkit.getWorlds();
        for (int i = 0; i < worlds.size(); i++) {
            World world = worlds.get(i);
            for (Entity ent : world.getEntities()) {
                if (ent.getUniqueId() == id) {
                    return ent;
                }
            }
        }
        return null;
    }

    public static boolean isSpawnerMob(UUID id) {
        return spawnerMobs.containsKey(id);
    }
    
    public static void addSpawnerMob(UUID id) {
        spawnerMobs.put(id, true);
    }
    
    public static void removeSpawnerMob(UUID id) {
        spawnerMobs.remove(id);
    }
}

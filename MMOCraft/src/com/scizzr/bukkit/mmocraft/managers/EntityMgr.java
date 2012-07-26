package com.scizzr.bukkit.mmocraft.managers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.util.Util;

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
    
    public static boolean isBehind(Entity back, Entity frnt) {
//Voodoo to check if the player is facing the same direction as their target (aka behind) when attacking
        Location locB = frnt.getLocation(); float yawB = locB.getYaw(); if (yawB < 0) { yawB += 360; } yawB %= 360; int dP = (int)((yawB+8)/22.5);
        Location locF = back.getLocation(); float yawF = locF.getYaw(); if (yawF < 0) { yawF += 360; } yawF %= 360; int dE = (int)((yawF+8)/22.5);
        
        return (Util.between(dP, dE-1, dE+1, true));
    }
}

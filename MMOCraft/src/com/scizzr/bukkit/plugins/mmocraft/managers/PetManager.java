package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

public class PetManager {
    private static HashMap<Creature, Player> pets = new HashMap<Creature, Player> ();
    
    public static void capture(Player p, Creature c) {
        
    }
    
    public static void release(Creature c) {
        
    }
    
    @SuppressWarnings("rawtypes")
    public static Player getMaster(Creature c) {
        if (pets.containsKey(c)) {
            for (Map.Entry entry : pets.entrySet()) {
                if (entry.getValue().equals(c)) {
                    return (Player)entry.getKey();
                }
            }
        }
        return null;
    }
    
    public static boolean hasPet(Player p) {
        return pets.containsValue(p);
    }
    
    public static boolean isPet(Creature c) {
        return pets.containsKey(c);
    }
    
    public static void spawnPet(Player p, EntityType t) {
        
    }
    
    public static void teleportPet(Player p) {
        
    }    
    
    public static boolean isFollowed(Player p) {
        return true;
    }
    
    public static String getPetName(Creature c) {
        return "";
    }
    
    public static EntityType getPetType(Creature c) {
        if (c instanceof Blaze) return EntityType.BLAZE;
        if (c instanceof CaveSpider) return EntityType.CAVE_SPIDER;
        if (c instanceof Enderman) return EntityType.ENDERMAN;
        if (c instanceof Ghast) return EntityType.GHAST;
        if (c instanceof IronGolem) return EntityType.IRON_GOLEM;
        if (c instanceof MagmaCube) return EntityType.MAGMA_CUBE;
        if (c instanceof MushroomCow) return EntityType.MUSHROOM_COW;
        if (c instanceof Ocelot) return EntityType.OCELOT;
        if (c instanceof Silverfish) return EntityType.SILVERFISH;
        if (c instanceof Skeleton) return EntityType.SKELETON;
        if (c instanceof Snowman) return EntityType.SNOWMAN;
        if (c instanceof Villager) return EntityType.VILLAGER;
        if (c instanceof Wolf) return EntityType.WOLF;
        if (c instanceof Chicken) return EntityType.CHICKEN;
        if (c instanceof Cow) return EntityType.COW;
        if (c instanceof Creeper) return EntityType.CREEPER;
        if (c instanceof Giant) return EntityType.GIANT;
        if (c instanceof Pig) return EntityType.PIG;
        if (c instanceof PigZombie) return EntityType.PIG_ZOMBIE;
        if (c instanceof Sheep) return EntityType.SHEEP;
        if (c instanceof Slime) return EntityType.SLIME;
        if (c instanceof Spider) return EntityType.SPIDER;
        if (c instanceof Squid) return EntityType.SQUID;
        if (c instanceof Zombie) return EntityType.ZOMBIE;
        return null;
    }
}

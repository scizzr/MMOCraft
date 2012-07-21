package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.pets.PetBlaze;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.pets.PetPigzombie;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.pets.PetSpider;

public class PetMgr {
    private static ConcurrentHashMap<Pet, Boolean> pets = new ConcurrentHashMap<Pet, Boolean>();
    
    public static void main() {
        //load();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            public void run() {
                fixPets();
                ConcurrentHashMap<Pet, Boolean> petlist = getAllPets();
                for (Entry<Pet, Boolean> entry : petlist.entrySet()) {
                    Pet pet = entry.getKey();
                    String name = pet.getOwnerName();
                    Player p = Bukkit.getPlayerExact(name);
                    if (p != null) {
                        Location locOwn = p.getLocation();
                        Location locPet = pet.getLocation();
                        
                        if (!locOwn.getWorld().equals(locPet.getWorld())) {
                            pet.teleport(locOwn);
                        } else if (locOwn.distance(locPet) > 10) {
                            pet.teleport(p);
                        }
                    }
                }
            }
        }, 0L, (long)20*3);
    }
    
    public static void addPet(String name, Pet pet) {
        Race race = RaceMgr.getRace(name);
        race.addPet(pet);
        pets.put(pet, true);
    }
    
    public static void removePet(Pet pet, boolean release, boolean remove) {
        RaceMgr.getRace(pet.getOwnerName()).removePet(pet);
        if (release) {
            Player p = Bukkit.getPlayerExact(pet.getOwnerName());
            if (p != null) {
                p.sendMessage(pet.getName() + " released");
            }
        }
        if (remove) {
            ((LivingEntity)EntityMgr.getEntityByUUID(pet.getUUID())).remove();
        }
        pets.remove(pet);
    }
    
    public static void removeAllPets(String name) {
        for (Entry<Pet, Boolean> entry : pets.entrySet()) {
            Pet pet = entry.getKey();
            if (pet.getOwnerName() == name) {
                removePet(pet, true, true);
            }
        }
    }
    
    public static boolean isPet(Entity ent) {
        if (ent == null) return false;
        return getPet(ent.getUniqueId()) != null;
    }
    
    public static Pet getPet(UUID id) {
        try {
            for (Entry<Pet, Boolean> entry : pets.entrySet()) {
                Pet pet = entry.getKey();
                if (pet.getUUID() == id) {
                    return pet;
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }
    
    public static void fixPets() {
        for (Entry<Pet, Boolean> entry : pets.entrySet()) {
            Pet pet = entry.getKey();
            if (EntityMgr.getEntityByUUID(pet.getUUID()) == null) {
                String name = pet.getOwnerName();
                Player owner = Bukkit.getPlayerExact(name);
                if (owner != null) {
                    Location loc = owner.getLocation();
                    int food = pet.getFood();
                    int power = pet.getPower();
                    int hp = pet.getHealth();
                    removePet(pet, false, false);
                    
                    Pet newpet = null; Creature mob = null;
                    if (pet instanceof PetPigzombie) { newpet = new PetPigzombie(); mob = loc.getWorld().spawn(loc, PigZombie.class); }
                    if (pet instanceof PetSpider) {    newpet = new PetSpider();    mob = loc.getWorld().spawn(loc, Spider.class);    }
                    if (pet instanceof PetBlaze) {     newpet = new PetBlaze();     mob = loc.getWorld().spawn(loc, Blaze.class);     }
                    
                    if (newpet != null && mob != null) {
                        newpet.setOwnerName(name);
                        newpet.setUUID(mob.getUniqueId());
                        newpet.setHealth(hp);
                        newpet.setFood(food);
                        newpet.setPower(power);
                        addPet(name, newpet);
                    }
                }
            }
        }
    }
    
    public static ConcurrentHashMap<Pet, Boolean> getAllPets() {
        return pets;
    }
    
    public static ConcurrentHashMap<Pet, Boolean> getPlayerPets(String name) {
        Race race = RaceMgr.getRace(name);
        return race.getPets();
    }
}

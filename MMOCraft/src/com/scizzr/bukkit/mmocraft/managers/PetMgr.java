package com.scizzr.bukkit.mmocraft.managers;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.pets.PetBlaze;
import com.scizzr.bukkit.mmocraft.pets.PetPigzombie;
import com.scizzr.bukkit.mmocraft.pets.PetSpider;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;
public class PetMgr {
    private static CopyOnWriteArrayList<Pet> pets = new CopyOnWriteArrayList<Pet>();
    
    public static void main() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(MMOCraft.plugin, new Runnable() {
            public void run() {
                fixPets();
                
                synchronized(pets) {
                    Iterator<Pet> it = pets.iterator();
                    while (it.hasNext()) {
                        Pet pet = it.next();
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
            }
        }, 0L, (long)20*3);
    }
    
    public static void addPet(String name, Pet pet) {
        Race race = RaceMgr.getRace(name);
        race.addPet(pet);
        pets.add(pet);
    }
    
    public static void removePet(Pet pet, boolean really, boolean removeent, boolean isRelease) {
        if (really) {
            String owner = pet.getOwnerName();
            Race race = RaceMgr.getRace(owner);
            
            race.removePet(pet);
            Util.ListRemove(pets, pet);
            
            Player p = Bukkit.getPlayer(pet.getOwnerName());
            
            if (p != null && isRelease) {
                p.sendMessage(MMOCraft.prefix + I18n._("petreleased", new Object[] {pet.getName()}));
            }
        }
        
        if (removeent) {
            ((LivingEntity)EntityMgr.getEntityByUUID(pet.getUUID())).remove();
        }
    }
    
    public static void removeAllPlayerPets(String name, boolean really) {
        Race race = RaceMgr.getRace(name);
        synchronized(pets) {
            Iterator<Pet> it = pets.iterator();
            while (it.hasNext()) {
                Pet pet = it.next();
                if (pet.getOwnerName() == name) {
                    removePet(pet, really, true, false);
                    race.removePet(pet);
                }
            }
        }
    }
    
    public static boolean isPet(Entity ent) {
        return (ent == null) ? false : (getPet(ent.getUniqueId()) != null);
    }
    
    public static Pet getPet(UUID id) {
        synchronized(pets) {
            Iterator<Pet> it = pets.iterator();
            while (it.hasNext()) {
                Pet pet = it.next();
                if (pet.getUUID() == id) {
                    return pet;
                }
            }
        }
        return null;
    }
    
    public static void listPets(Player p) {
        List<Pet> petsPlayer = getPlayerPets(p.getName());
        
        if (petsPlayer.size() == 0) { p.sendMessage(MMOCraft.prefix + I18n._("pethasnone", new Object[] {})); return; }
        
        Iterator<Pet> it = petsPlayer.iterator();
        while (it.hasNext()) {
            Pet pet = it.next();
            p.sendMessage(MMOCraft.prefix + Util.displayPetInfo(p, pet));
        }
    }
    
    public static List<Pet> getAllPets() {
        return pets;
    }
    
    public static int numPets(Player p, Pet pet) {
        int count = 0;
        synchronized(pets) {
            Iterator<Pet> it = pets.iterator();
            while (it.hasNext()) {
                Pet pett = it.next();
                if (pett.getOwnerName().equals(p.getName()) &&
                        pett.getName().equals(pet.getName())) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public static void fixPets() {
        synchronized(pets) {
            Iterator<Pet> it = pets.iterator();
            while (it.hasNext()) {
                Pet pet = it.next();
                if (EntityMgr.getEntityByUUID(pet.getUUID()) == null) {
                    String name = pet.getOwnerName();
                    Player owner = Bukkit.getPlayerExact(name);
                    if (owner != null) {
                        Location loc = owner.getLocation();
                        int food = pet.getFood();
                        int power = pet.getPower();
                        int hp = pet.getHealth();
                        
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
                            removePet(pet, true, false, false);
                        }
                    }
                }
            }
        }
    }
    
    public static List<Pet> getPlayerPets(String name) {
        Race race = RaceMgr.getRace(name);
        return race.getPets();
    }
    
    public static CopyOnWriteArrayList<Pet> get() {
        return pets;
    }
    
    public static void put(CopyOnWriteArrayList<Pet> petsNew) {
        pets = petsNew;
    }
}

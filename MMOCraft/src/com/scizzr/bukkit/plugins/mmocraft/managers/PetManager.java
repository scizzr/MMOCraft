package com.scizzr.bukkit.plugins.mmocraft.managers;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Pet;

public class PetManager {
    public static ConcurrentHashMap<Pet, Boolean> pets = new ConcurrentHashMap<Pet, Boolean>();
    
    public void tick() {
        for (Entry<Pet, Boolean> entry : pets.entrySet()) {
            Pet pet = entry.getKey();
            Player owner = pet.getOwner();
            Location locOwner = owner.getLocation();
            Location locPet = pet.getLocation();
            if (locOwner.distance(locPet) >= 10) {
                pet.teleport(owner);
            }
        }
    }
}

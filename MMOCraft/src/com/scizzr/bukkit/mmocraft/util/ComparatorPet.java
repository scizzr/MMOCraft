package com.scizzr.bukkit.mmocraft.util;

import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.scizzr.bukkit.mmocraft.interfaces.Pet;

public class ComparatorPet implements Comparator<Pet> {

    @Override
    public int compare(Pet pet1, Pet pet2) {
        //String nameA = pet1.getName(); int hpA = pet1.getHealth();
        //String nameB = pet2.getName(); int hpB = pet2.getHealth();
        
        //int scoreA = ((nameA.equals("Blaze") ? 3 : 0) + (nameA.equals("Spider") ? 2 : 0) + (nameA.equals("PigZombie") ? 1 : 0) + (hpA));
        //int scoreB = ((nameB.equals("Blaze") ? 3 : 0) + (nameB.equals("Spider") ? 2 : 0) + (nameB.equals("PigZombie") ? 1 : 0) + (hpB));
        
        //return (scoreA < scoreB ? -1 : (scoreA == scoreB ? 0 : +1));
        
        Location locO = Bukkit.getPlayer(pet1.getOwnerName()).getLocation();
        Location locA = pet1.getLocation();
        Location locB = pet2.getLocation();
        
        Double scoreA = locO.distance(locA);
        Double scoreB = locO.distance(locB);
        
        return (scoreA < scoreB ? -1 : (scoreA == scoreB ? 0 : +1));
    }
}

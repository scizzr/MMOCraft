package com.scizzr.bukkit.plugins.mmocraft.interfaces.pets;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityMgr;

public class PetSpider implements Pet {
    private String owner;
    private int health = 0;
    private int power = 0;
    private int food = 20;
    private UUID uuid;
    
    public String getName() {
        return "Spider";
    }
    
    public UUID getUUID() {
        return uuid;
    }
    
    public void setUUID(UUID id) {
        uuid = id;
    }
    
    public Location getLocation() {
        return EntityMgr.getEntityByUUID(uuid).getLocation();
    }
    
    public void teleport(Location loc) {
        EntityMgr.getEntityByUUID(uuid).teleport(loc);
    }
    
    public void teleport(Entity ent) {
        EntityMgr.getEntityByUUID(uuid).teleport(ent);
    }
    
    public String getOwnerName() {
        return owner;
    }
    
    public void setOwnerName(String own) {
        owner = own;
    }
    
    public Integer getHealth() {
        return health;
    }
    
    public void setHealth(int amt) {
        health = amt;
    }
    
    public Integer getPower() {
        return power;
    }
    
    public void setPower(int amt) {
        power = amt;
    }
    
    public Integer getFood() {
        return food;
    }
    
    public void setFood(int amt) {
        food = amt;
    }
    
    public void fire() {
        
    }
}

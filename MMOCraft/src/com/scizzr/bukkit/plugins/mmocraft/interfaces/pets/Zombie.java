package com.scizzr.bukkit.plugins.mmocraft.interfaces.pets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Pet;

public class Zombie implements Pet {
    private Location location;
    private Player owner;
    private int power = 0;
    private int food = 0;
    private String uuid = "";
    private Entity entity;
    
    public String getName() {
        return "Zombie";
    }
    
    public String getUUID() {
        return uuid;
    }
    
    public void setUUID(String id) {
        uuid = id;
    }
    
    public Entity getRaw() {
        return entity;
    }
    
    public void setRaw(Entity ent) {
        entity = ent;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void teleport(Location loc) {
        entity.teleport(loc);
    }
    
    public void teleport(Entity ent) {
        entity.teleport(ent);
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public void setOwner(Player p) {
        owner = p;
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

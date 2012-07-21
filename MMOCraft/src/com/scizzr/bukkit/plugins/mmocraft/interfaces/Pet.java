package com.scizzr.bukkit.plugins.mmocraft.interfaces;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract interface Pet {
    public abstract String getName();
    public abstract UUID getUUID();
    public abstract void setUUID(UUID uuid);
    public abstract Location getLocation();
    
    public abstract void teleport(Location loc);
    public abstract void teleport(Entity ent);
    public abstract Integer getHealth();
    public abstract void setHealth(int amt);
    public abstract String getOwnerName();
    public abstract void setOwnerName(String s);
    public abstract Integer getPower();
    public abstract void setPower(int amt);
    public abstract Integer getFood();
    public abstract void setFood(int amt);
    
    public abstract void fire();
}

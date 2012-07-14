package com.scizzr.bukkit.plugins.mmocraft.interfaces;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract interface Pet {
    public abstract String getName();
    public abstract String getUUID();
    public abstract void setUUID(String id);
    public abstract Location getLocation();
    public abstract void teleport(Location loc);
    public abstract void teleport(Entity ent);
    public abstract Player getOwner();
    public abstract void setOwner(Player p);
    public abstract Integer getPower();
    public abstract void setPower(int amt);
    public abstract Integer getFood();
    public abstract void setFood(int amt);
    public abstract void fire();
}

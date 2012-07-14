package com.scizzr.bukkit.plugins.mmocraft.interfaces;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract interface Helper {
    public abstract String getName();
    public abstract boolean canUse(Player p);
    public abstract Location getLocation();
    public abstract void setLocation(Location loc);
    public abstract Player getOwner();
    public abstract void setOwner(Player p);
    public abstract Integer getCount();
    public abstract void setCount(int count);
    public abstract ArrayList<String> getBlocks();
    public abstract void count();
    public abstract void flip();
    public abstract void fire();
}

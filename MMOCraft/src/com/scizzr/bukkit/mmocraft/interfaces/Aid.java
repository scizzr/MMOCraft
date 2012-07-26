package com.scizzr.bukkit.mmocraft.interfaces;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract interface Aid extends Comparable<Aid> {
    public abstract String getName();
    public abstract boolean canUse(Player p);
    public abstract Location getLocation();
    public abstract void setLocation(Location loc);
    public abstract String getOwnerName();
    public abstract void setOwnerName(String play);
    public abstract Integer getCount();
    public abstract void setCount(int count);
    public abstract ArrayList<String> getBlocks();
    public abstract ArrayList<String> getBadBlocks();
    public abstract void progress();
    public abstract void flip();
    public abstract void fire();
}

package com.scizzr.bukkit.mmocraft.effects2;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public abstract class Effect {
    private final int range;
    
    public Effect(int range) {
        this.range = range;
    }
    
    public int getRange() {
        return this.range;
    }
    
    public List<Player> getNearbyPlayers(Location location, Entity ignore) {
        List<Player> near = new ArrayList<Player>();
        
        synchronized (near) {
            for (Entity ent : location.getWorld().getEntities()) {
                if (ent instanceof Player) {
                    Player p = (Player)ent;
                    if (p.getLocation().distance(location) <= this.range) {
                        near.add(p);
                    }
                }
            }
        }
        
        return near;
    }
    
    protected static int getMaxRange(Effect[] effects) {
        int range = 0;
        
        for (Effect effect : effects) {
            range = Math.max(range, effect.getRange());
        }
        return range;
    }
    
    public abstract void play(Player player, Location location);
    
    public void play(List<Player> players, Location location) {
        for (Player player : players) {
            this.play(player, location);
        }
    }
    
    public void playGlobal(Location location) {
        this.playGlobal(location, null);
    }
    
    public void playGlobal(Location location, Entity ignore) {
        this.play(getNearbyPlayers(location, ignore), location);
    }
}

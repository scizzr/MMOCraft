package com.scizzr.bukkit.mmocraft.custommobs;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ZombieMoveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    private boolean isCancelled = false;
    private Zombie zombie;
    private Location from;
    private Location to;
    
    public ZombieMoveEvent(Zombie zombie, Location from, Location to) {
        this.zombie = zombie;
        this.from = from;
        this.to = to;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public Zombie getZombie() {
        return this.zombie;
    }
    
    public Location getFrom() {
        return this.from;
    }
    
    public Location getTo() {
        return this.to;
    }
    
    public boolean isCancelled() {
        return this.isCancelled;
    }
    
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}

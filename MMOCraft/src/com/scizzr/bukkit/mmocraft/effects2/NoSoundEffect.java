package com.scizzr.bukkit.mmocraft.effects2;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NoSoundEffect extends SoundEffect {
    public NoSoundEffect() {
        super("none");
    }
    
    public void playAll(Location loc, double range) {
        
    }
    
    public void playOnly(Player p, Location loc, double range) {
        
    }
    
    public void playExcept(Location loc, Player p, double range) {
        
    }
}

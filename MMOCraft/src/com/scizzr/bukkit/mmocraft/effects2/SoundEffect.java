package com.scizzr.bukkit.mmocraft.effects2;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SoundEffect extends Effect {
    private static final int SOUND_RANGE = 16;
    private final String name;
    private final float volume, pitch;
    
    public SoundEffect(SoundEffect sound, float volume, float pitch) {
        this(sound.getName(), volume, pitch, sound.getRange());
    }
    
    public SoundEffect(String name) {
        this(name, 1.0f, 1.0f);
    }
    
    public SoundEffect(String name, int range) {
        this(name, 1.0f, 1.0f, range);
    }
    
    public SoundEffect(String name, float volume, float pitch) {
        this(name, volume, pitch, SOUND_RANGE);
    }
    
    public SoundEffect(String name, float volume, float pitch, int range) {
        super(range);
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public float getDefaultVolume() {
        return this.volume;
    }
    
    public float getDefaultPitch() {
        return this.pitch;
    }
    
    public SoundEffect adjust(float volume, float pitch) {
        return new SoundEffect(this, volume, pitch);
    }
    
    public SoundEffect randomPitch(float amount) {
        return new RandomPitchSoundEffect(this, amount);
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return "Sound{" + this.name + "}";
    }
    
    public List<Player> getNearbyPlayers(Location location, Entity ignore, float volume) {
        int range = this.getRange();
        if (volume > 1.0f) {
            // Multiply range for different volumes
            range *= volume;
        }
        
        List<Player> near = new ArrayList<Player>();
        
        synchronized (near) {
            for (Entity ent : location.getWorld().getEntities()) {
                if (ent instanceof Player) {
                    Player p = (Player)ent;
                    if (p.getLocation().distance(location) <= range) {
                        near.add(p);
                    }
                }
            }
        }
        
        return near;
    }
    
    @Override
    public void play(Player player, Location location) {
        this.play(player, location, this.getDefaultVolume(), this.getDefaultPitch());
    }
    
    public void play(Player player, Location location, float volume, float pitch) {
        ((CraftWorld)player.getWorld()).getHandle().makeSound(location.getX(), location.getY(), location.getZ(), this.getName(), volume, pitch); 
    }
    
    public void play(List<Player> players, Location location, float volume, float pitch) {
        for (Player player : players) {
            this.play(player, location, volume, pitch);
        }
    }
    
    public void playGlobal(Location location, float volume, float pitch) {
        this.playGlobal(location, volume, pitch, null);
    }
    
    public void playGlobal(Location location, float volume, float pitch, Entity ignore) {
        this.play(getNearbyPlayers(location, ignore, volume), location, volume, pitch);
    }
}
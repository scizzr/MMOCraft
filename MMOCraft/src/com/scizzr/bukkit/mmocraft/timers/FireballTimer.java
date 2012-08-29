package com.scizzr.bukkit.mmocraft.timers;

import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.mmocraft.threads.Explosion;

public class FireballTimer implements Runnable {
    public static ConcurrentHashMap<Fireball, Integer> balls = new ConcurrentHashMap<Fireball, Integer> ();
    public static ConcurrentHashMap<Snowball, Integer> sparks = new ConcurrentHashMap<Snowball, Integer> ();
    
    String act; Object p1; Object p2;
    
    public FireballTimer(String act, Object p1, Object p2) {
        this.act = act; this.p1 = p1; this.p2 = p2;
    }
    
    public void run() {
        if (act.equalsIgnoreCase("countdown")) {
            countdown();
        }
    }
    
    public static void countdown() {
        for (Entry<Fireball, Integer> entry : balls.entrySet()) {
            addFireball(entry.getKey(), entry.getValue() - 1);
        }
        for (Entry<Snowball, Integer> entry : sparks.entrySet()) {
            addSnowball(entry.getKey(), entry.getValue() - 1);
        }
    }
    
    public static void addFireball(Fireball fire, Integer time) {
        if (time > 0) {
            synchronized(balls) {
                balls.put(fire, time);
            }
        } else {
            explodeFireball(fire);
        }
    }
    
    public static void addSnowball(Snowball ball, Integer time) {
        if (time > 0) {
            synchronized(sparks) {
                sparks.put(ball, time);
            }
        } else {
            removeSnowball(ball);
        }
    }

    public static void addSnowball2(Fireball ball, Integer time) {
        if (time > 0) {
            synchronized(balls) {
                balls.put(ball, time);
            }
        } else {
            removeFireball(ball);
        }
    }

    public static void removeFireball(Fireball fire) {
        synchronized(balls) {
            balls.remove(fire);
        }
        fire.remove();
    }
    
    public static void removeSnowball(Snowball snow) {
        synchronized(sparks) {
            sparks.remove(snow);
        }
        snow.remove();
    }
    
    public static void explodeFireball(Fireball fire) {
        Random rand = new Random();
        
        Explosion.make(fire.getLocation(), 1.5F, false);
        
        World world = fire.getWorld();
        
        for (int i = 0; i <= 360; i += 5) {
            Location loc = fire.getLocation().clone();
            loc.setPitch(-90 + rand.nextInt(90)); loc.setYaw(i);
            
            Vector direction = loc.getDirection();
            
            Snowball snow = (Snowball) world.spawnEntity(loc, EntityType.SNOWBALL);
            snow.setVelocity(direction); snow.setFireTicks(60);
            
            addSnowball(snow, (rand.nextInt(2)+1)*10);
        }
        
        synchronized(balls) {
            balls.remove(fire);
        }
        fire.remove();
    }
}

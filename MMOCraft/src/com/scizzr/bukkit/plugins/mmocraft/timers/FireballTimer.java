package com.scizzr.bukkit.plugins.mmocraft.timers;

import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.threads.Explosion;

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
            try {
                addFireball(entry.getKey(), entry.getValue() - 1);
            } catch (Exception ex) {
                /* ex.printStackTrace(); */
            }
        }
        for (Entry<Snowball, Integer> entry : sparks.entrySet()) {
            try {
                addSnowball(entry.getKey(), entry.getValue() - 1);
            } catch (Exception ex) {
                /* ex.printStackTrace(); */
            }
        }
    }
    
    public static void addFireball(Fireball fire, Integer time) {
        if (time > 0) {
            balls.put(fire, time);
        } else {
            explodeFireball(fire);
        }
    }
    
    public static void addSnowball(Snowball ball, Integer time) {
        if (time > 0) {
            sparks.put(ball, time);
        } else {
            removeSnowball(ball);
        }
    }

    public static void addSnowball2(Fireball ball, Integer time) {
        if (time > 0) {
            balls.put(ball, time);
        } else {
            removeFireball(ball);
        }
    }

    public static void removeFireball(Fireball fire) {
        balls.remove(fire);
        fire.remove();
    }
    
    public static void removeSnowball(Snowball snow) {
        sparks.remove(snow);
        snow.remove();
    }
    
    public static void explodeFireball(Fireball fire) {
        Random rand = new Random();
        
        new Thread(new Explosion(fire.getLocation(), false)).start();
        
        for (int i = 0; i <= 360; i += 5) {
            Location loc = fire.getLocation().clone();
            loc.setPitch(-90 + rand.nextInt(90)); loc.setYaw(i);
            
            Vector direction = loc.getDirection();
            
            Snowball snow = fire.getWorld().spawn(loc, Snowball.class);
            snow.setVelocity(direction); snow.setFireTicks(60);
            
            addSnowball(snow, (rand.nextInt(2)+1)*10);
        }
        
        balls.remove(fire);
        fire.remove();
    }
}

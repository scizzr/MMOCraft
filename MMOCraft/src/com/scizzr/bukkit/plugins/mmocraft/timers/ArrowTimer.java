package com.scizzr.bukkit.plugins.mmocraft.timers;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.managers.EntityManager;
import com.scizzr.bukkit.plugins.mmocraft.threads.Explosion;

public class ArrowTimer implements Runnable {
    public static ConcurrentHashMap<Arrow, Integer> arrows = new ConcurrentHashMap<Arrow, Integer> ();
    
    String act; Object p1; Object p2;
    
    public ArrowTimer(String act, Object p1, Object p2) {
        this.act = act; this.p1 = p1; this.p2 = p2;
    }
    
    public void run() {
        if (act.equalsIgnoreCase("countdown")) {
            countdown();
        }
    }
    
    public static void countdown() {
        for (Entry<Arrow, Integer> entry : arrows.entrySet()) {
            try{
                add(entry.getKey(), entry.getValue() - 1);
            } catch (Exception ex) {
                /* ex.printStackTrace(); */
            }
        }
    }
    
    public static void add(Arrow arrow, Integer time) {
        if (time > 0) {
            arrows.put(arrow, time);
        } else {
            //explode(arrow);
            remove(arrow);
        }
    }
    
    public static void remove(Arrow arrow) {
        arrows.remove(arrow);
        arrow.remove();
    }
    
    public static void explode(Arrow arrow) {
        Location loc = arrow.getLocation().clone();
        new Thread(new Explosion(loc, false)).start();
        
        for (Entity ent : loc.getWorld().getEntities()) {
            if (ent instanceof LivingEntity && ent.getLocation().clone().distance(arrow.getLocation().clone()) <= 30) {
                LivingEntity lent = (LivingEntity)ent;
                if (ent instanceof Player) {
                    Player p = (Player)ent;
                    if (arrow.getShooter() != p) {
                        EntityManager.setAttacker(ent, (Player)arrow.getShooter());
                        lent.damage(3);
                    }
                } else {
                    EntityManager.setAttacker(ent, (Player)arrow.getShooter());
                    lent.damage(3);
                }
            }
        }
        
        arrows.remove(arrow);
        arrow.remove();
    }
}

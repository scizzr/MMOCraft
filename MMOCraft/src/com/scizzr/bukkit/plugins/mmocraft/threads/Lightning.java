package com.scizzr.bukkit.plugins.mmocraft.threads;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.managers.EntityManager;

public class Lightning implements Runnable {
    Entity e;
    
    public Lightning(Entity e) {
        this.e = e;
    }
    
    public void run() {
        try {
            for (Entity ee : e.getNearbyEntities(5, 5, 5)) {
                Location loc = ee.getLocation();
                loc.getWorld().strikeLightningEffect(loc.clone().add(0, 1, 0)).setFireTicks(0);
                Location l2 = ee.getLocation().clone(); l2.setPitch(-60);
                ee.setVelocity(l2.getDirection().multiply(1.25));
                if (e instanceof Player) {
                    EntityManager.setAttacker(ee, (Player)e);
                }
            }
        } catch (Exception ex) {
            /* No Spam */
        }
    }
}

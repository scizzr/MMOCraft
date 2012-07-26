package com.scizzr.bukkit.mmocraft.threads;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.managers.EntityMgr;

public class Lightning implements Runnable {
    Entity exec; int max;
    
    public Lightning(Entity exec, int max) {
        this.exec = exec; this.max = max;
    }
    
    public void run() {
        try {
            int count = 0;
            for (Entity ee : exec.getNearbyEntities(5, 5, 5)) {
                if (count > max) { return; }
                Location loc = ee.getLocation();
                loc.getWorld().strikeLightningEffect(loc.clone().add(0, 1, 0)).setFireTicks(0);
                Location l2 = ee.getLocation().clone(); l2.setPitch(-60);
                ee.setVelocity(l2.getDirection().multiply(1.25));
                if (exec instanceof Player) {
                    EntityMgr.setAttacker(ee, exec);
                }
                count++;
            }
        } catch (Exception ex) {
            /* No Spam */
        }
    }
}

package com.scizzr.bukkit.plugins.mmocraft.threads;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.timers.FireballTimer;

public class Meteor implements Runnable {
    Player p;

    public Meteor(Player p) {
        this.p = p;
    }

    public void run() {
        Location loc = p.getLocation().clone();
        loc.setPitch(90);
        
        Fireball fb = p.getWorld().spawn(loc.add(0, 20, 0), Fireball.class);
        fb.setVelocity(loc.getDirection().multiply(0.8));
        fb.setYield(0); fb.setIsIncendiary(false); fb.setShooter(p);
        
        FireballTimer.addFireball(fb, 60);
    }
}

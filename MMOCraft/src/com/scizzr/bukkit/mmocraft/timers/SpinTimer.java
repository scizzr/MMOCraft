package com.scizzr.bukkit.mmocraft.timers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;

public class SpinTimer implements Runnable {
    String act; Object p1; Object p2;
    
    public SpinTimer(String act, Object p1, Object p2) {
        this.act = act; this.p1 = p1; this.p2 = p2;
    }
    
    public void run() {
        if (act.equalsIgnoreCase("spin")) {
            spinThread((Player) p1);
        }
    }
    
    public static void spin(Player p) {
        new Thread(new SpinTimer("spin", p, null)).start();
    }
    
    public static void spinThread(Player p) {
        Race race = RaceMgr.getRace(p.getName());
        race.setData("spinning", true);
        
        Location begin = p.getLocation();
        float yaw = p.getLocation().getYaw();
        for (int i = 1; i <= 10; i += 1) {
            Location loc = p.getLocation();
            loc.setYaw(loc.getYaw() + 36);
            p.teleport(loc);
            try { Thread.sleep(50); } catch (Exception ex) { ex.printStackTrace(); }
            if (yaw == p.getLocation().getYaw()) {
                i -= 1;
            } else {
                yaw = p.getLocation().getYaw();
            }
        }
        p.teleport(begin);
        race.setData("spinning", null);
    }
}

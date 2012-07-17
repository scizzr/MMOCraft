package com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Helper;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.ArcherRing;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreMath;

public class Turret implements Helper {
    private Location location;
    private Player owner;
    private int frequency = 40;
    private int counter = 0;
    private int flip = 0;
    
    public String getName() {
        return "Turret";
    }
    
    public ArrayList<String> getBlocks() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("85:0"); al.add("113:0");
        return al;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location loc) {
        location = loc.clone();
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public void setOwner(Player p) {
        owner = p;
    }
    
    public Integer getCount() {
        return counter;
    }
    
    public void setCount(int count) {
        counter = count;
    }
    
    public boolean canUse(Player p) {
        return true;
    }
    
    public void count() {
        counter += 1;
        if (counter == frequency || counter == Math.floor(frequency/2)) {
            flip();
        }
        if (counter >= frequency) {
            fire();
            counter = 0;
        }
    }
    
    public void flip() {
        Block b = location.getBlock();
        
        if (!(getBlocks().contains(b.getTypeId() + ":" + (int)b.getData()) || b.getType() == Material.FIRE)) {
            //HelperMgr.removeHelper(b, null); return;
        }
        
        if (flip == 0) {
            b.setTypeId(Integer.valueOf(getBlocks().get(0).split(":")[0]));
            b.setData(Byte.valueOf(getBlocks().get(0).split(":")[1]));
            flip = 1;
        } else {
            b.setTypeId(Integer.valueOf(getBlocks().get(1).split(":")[0]));
            b.setData(Byte.valueOf(getBlocks().get(1).split(":")[1]));
            flip = 0;
        }
    }
    
    public void fire() {
        try {
            for (Entity ent : location.getWorld().getEntities()) {
                if (!(ent instanceof LivingEntity)) { continue; }
                
                if (ent instanceof Player) { Player p = (Player)ent; if (owner == p || p.isOp() || p.getGameMode() == GameMode.CREATIVE) { continue; } }
                
                Location locTur = location.clone();
                Location locEnt = ent.getLocation();
                
                if (locTur.distance(locEnt) <= 5) {
                    if (locTur.getBlockY() != locEnt.getBlockY()) { continue; }
                    
                    Location loc = locEnt.clone();
                    loc.setPitch(5);
                    loc.setYaw(MoreMath.getYawFromLocToLoc(locTur, locEnt));
                    
                    final Vector direction = loc.getDirection();
                    
                    Arrow arrow = location.getWorld().spawn(locTur.clone().add(0.5, 1.5, 0.5), Arrow.class);
                    arrow.setVelocity(direction); arrow.setShooter(owner); //arrow.setFireTicks(60);
                    
                    locTur.getWorld().playEffect(locTur, Effect.BOW_FIRE, 1);
                    
                    EntityMgr.setAttacker(ent, owner);
                    
                    ArrowTimer.add(arrow, 50);
                }
            }
        } catch (Exception ex) {
            /* No Spam */
        }
    }
    
    public void fireWhole() {
        new ArcherRing().execute(owner, 1);
    }
}

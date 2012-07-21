package com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Helper;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperMgr;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreMath;

public class Forcefield implements Helper {
    private Location location;
    private String owner;
    private int frequency = 100;//40
    private int counter = 0;
    private int flip = 0;
    
    public String getName() {
        return "Forcefield";
    }
    
    public ArrayList<String> getBlocks() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("6:1"); al.add("6:3");
        return al;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location loc) {
        location = loc.clone();
    }
    
    public String getPlayerName() {
        return owner;
    }
    
    public void setPlayerName(String play) {
        owner = play;
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
    
    public void progress() {
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
        Block b2 = location.clone().add(0, -1, 0).getBlock();
        
        if (!(getBlocks().contains(b.getTypeId() + ":" + (int)b.getData()) || b.getType() == Material.FIRE) || !(getBlocks().contains(b2.getTypeId() + ":" + (int)b2.getData()) || b2.getType() == Material.FIRE)) {
            HelperMgr.removeHelper(b, null);
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
        Player player = Bukkit.getPlayerExact(owner);
        if (player != null) {
            try {
                for (Entity ent : location.getWorld().getEntities()) {
                    if (!(ent instanceof LivingEntity)) { continue; }
                    
                    if (ent instanceof Player) { Player p = (Player)ent; if (player == p || p.getGameMode() == GameMode.CREATIVE) { continue; } }
                    
                    Location locHelp = location.clone();
                    Location locEnt = ent.getLocation();
                    
                    if (locHelp.distance(locEnt) <= 5) {
                        //if (locHelp.getBlockY() != locEnt.getBlockY()) { continue; }
                        
                        Location loc = locEnt.clone();
                        loc.setPitch(5);
                        loc.setYaw(MoreMath.getYawFromLocToLoc(locHelp, locEnt));
                        
                        final Vector direction = loc.getDirection();
                        
                        ent.setVelocity(direction);
                        
                        locHelp.getWorld().playEffect(locHelp, Effect.ENDER_SIGNAL, 1);
                        
                        EntityMgr.setAttacker(ent, owner);
                    }
                }
            } catch (Exception ex) {
                /* No Spam */
            }
        }
    }
}

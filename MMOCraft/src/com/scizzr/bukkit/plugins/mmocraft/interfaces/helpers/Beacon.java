package com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Helper;

public class Beacon implements Helper {
    private Location location;
    private Player owner;
    private int frequency = 60;
    private int counter = 0;
    private int flip = 0;
    
    public String getName() {
        return "Beacon";
    }
    
    public ArrayList<String> getBlocks() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("75:0"); al.add("50:0");
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
                
                LivingEntity lent = (LivingEntity)ent;
                
                if (ent instanceof Player) { Player p = (Player)ent; if (owner == p || p.isOp() || p.getGameMode() == GameMode.CREATIVE) { continue; } }
                
                Location locHelp = location.clone();
                Location locEnt = ent.getLocation();
                
                if (locHelp.distance(locEnt) <= 5) {
                    //if (locHelp.getBlockY() != locEnt.getBlockY()) { continue; }
                    
                    lent.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                }
            }
        } catch (Exception ex) {
            /* No Spam */
        }
    }
}

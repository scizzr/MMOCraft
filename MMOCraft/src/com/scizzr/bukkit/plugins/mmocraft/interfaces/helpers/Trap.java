package com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Helper;

public class Trap implements Helper {
    private Location location;
    private Player owner;
    private int frequency = 20;
    private int counter = 0;
    private int flip = 0;
    
    public String getName() {
        return "Trap";
    }
    
    public ArrayList<String> getBlocks() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("72:0"); al.add("70:0");
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
            //HelperManager.removeHelper(b, null); return;
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
        
    }
}

package com.scizzr.bukkit.mmocraft.aids;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class Trap implements Aid {
    private Location location;
    private String owner;
    private int frequency = 100;//20
    private int counter = 0;
    private int flip = 0;
    
    public String getName() {
        return I18n._("trap", new Object[] {});
    }
    
    public ArrayList<String> getBlocks() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("72:0"); al.add("70:0");
        return al;
    }
    
    public ArrayList<String> getBadBlocks() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("0:0"); al.add("8:0"); al.add("9:0"); al.add("10:0"); al.add("11:0");
        return al;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location loc) {
        location = loc.clone();
    }
    
    public String getOwnerName() {
        return owner;
    }
    
    public void setOwnerName(String play) {
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
        /*
        Block b2 = location.clone().add(0, -1, 0).getBlock();
        
        if (!(getBlocks().contains(b.getTypeId() + ":" + (int)b.getData()) || b.getType() == Material.FIRE) || getBadBlocks().contains(b.getTypeId() + ":" + (int)b.getData()) || getBadBlocks().contains(b2.getTypeId() + ":" + (int)b2.getData())) {
            HelperMgr.removeHelper(b, null);
        }
        */
        
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
        //
    }
    
    public int compareTo(Aid aid) {
        OfflinePlayer ofp = Bukkit.getOfflinePlayer(owner);
        return (int)(ofp.isOnline() ? ofp.getPlayer().getLocation().distance(getLocation()) : 0);
    }
}

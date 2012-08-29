package com.scizzr.bukkit.mmocraft.aids;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.mmocraft.timers.ArrowTimer;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class Turret implements Aid {
    private Location location;
    private String owner;
    private int frequency = 40;
    private int counter = 0;
    private int flip = 0;
    
    public String getName() {
        return I18n._("turret", new Object[] {});
    }
    
    public ArrayList<String> getBlocks() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("85:0"); al.add("113:0");
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
        Player player = Bukkit.getPlayerExact(owner);
        if (player != null) {
            try {
                for (Entity ent : location.getWorld().getEntities()) {
                    if (!(ent instanceof LivingEntity)) { continue; }
                    
                    LivingEntity lent = (LivingEntity)ent;
                    
                    if (ent instanceof Player) { Player p = (Player)ent; if (owner.equalsIgnoreCase(p.getName()) || p.getGameMode() == GameMode.CREATIVE) { continue; } }
                    
                    if (PetMgr.isPet(lent)) {
                        Pet pet = PetMgr.getPet(ent.getUniqueId());
                        if (pet.getOwnerName().equals(player.getName())) { continue; }
                    }
                    
                    Location locTur = location.clone();
                    Location locEnt = ent.getLocation();
                    
                    if (locTur.distance(locEnt) <= 25) {
                        if (locTur.getBlockY() != locEnt.getBlockY()) { continue; }
                        
                        Location loc = locEnt.clone();
                        loc.setPitch(0);
                        loc.setYaw(Util.getYawFromLocToLoc(locTur, locEnt));
                        
                        final Vector direction = loc.getDirection().multiply(5f);
                        
                        Arrow arrow = location.getWorld().spawn(locTur.clone().add(0.5, 1.0, 0.5), Arrow.class);
                        
                        arrow.setVelocity(direction); arrow.setShooter(player); //arrow.setFireTicks(60);
                        
                        ((CraftArrow)arrow).getHandle().shake = 0;
                        
                        locTur.getWorld().playEffect(locTur, Effect.BOW_FIRE, 1);
                        
                        EntityMgr.setAttacker(ent, player);
                        
                        ArrowTimer.add(arrow, 50);
                    }
                }
            } catch (Exception ex) {
                /* No Spam */
            }
        }
    }
    
    public int compareTo(Aid aid) {
        OfflinePlayer ofp = Bukkit.getOfflinePlayer(owner);
        return (int)(ofp.isOnline() ? ofp.getPlayer().getLocation().distance(getLocation()) : 0);
    }
}

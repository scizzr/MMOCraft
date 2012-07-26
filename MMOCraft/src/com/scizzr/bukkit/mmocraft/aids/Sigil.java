package com.scizzr.bukkit.mmocraft.aids;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class Sigil implements Aid {
    private Location location;
    private String owner;
    private int frequency = 200;
    private int counter = 0;
    private int flip = 0;
    
    public String getName() {
        return I18n._("sigil", new Object[] {});
    }
    
    public ArrayList<String> getBlocks() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("91:0"); al.add("86:0");
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
                    
                    if (ent instanceof Player) { Player p = (Player)ent; if (player == p || p.getGameMode() == GameMode.CREATIVE) { continue; } }
                    
                    if (PetMgr.isPet(lent)) {
                        Pet pet = PetMgr.getPet(ent.getUniqueId());
                        if (pet.getOwnerName().equals(player.getName())) { continue; }
                    }
                    
                    Location locHelp = location.clone();
                    Location locEnt = ent.getLocation();
                    
                    if (locHelp.distance(locEnt) <= 5) {
                        //if (locHelp.getBlockY() != locEnt.getBlockY()) { continue; }
                        
                        lent.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                        
                        EntityMgr.setAttacker(ent, player);
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

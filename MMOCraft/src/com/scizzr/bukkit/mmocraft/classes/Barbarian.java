package com.scizzr.bukkit.mmocraft.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.managers.AidMgr;
import com.scizzr.bukkit.mmocraft.skills.BarbAxeWhirl;
import com.scizzr.bukkit.mmocraft.skills.BarbLeap;
import com.scizzr.bukkit.mmocraft.skills.BarbToughSkin;
import com.scizzr.bukkit.mmocraft.skills.NoneArrow;
import com.scizzr.bukkit.mmocraft.util.ComparatorPet;
import com.scizzr.bukkit.mmocraft.util.Util;

public class Barbarian implements Race {
    private String player;
    private int experience;
    int maxAids = 2;
    private int dmgClass = -2;
    private int dmgOther = -4;
    private CopyOnWriteArrayList<Aid> aids = new CopyOnWriteArrayList<Aid>();
    private CopyOnWriteArrayList<Pet> pets = new CopyOnWriteArrayList<Pet>();
    private ConcurrentHashMap<String, String> data = new ConcurrentHashMap<String, String>();
    
    public String getName() {
        return "Barbarian";
    }
    
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }
    
    public String getPlayerName() {
        return player;
    }
    
    public void setPlayerName(String play) {
        player = play;
    }
    
    public Integer getExp() {
        return experience;
    }
    
    public void setExp(int i) {
        experience = i;
    }
    
    public List<Pet> getPets() {
        List<Pet> sortme = new ArrayList<Pet>();
        synchronized(pets) {
            Iterator<Pet> it = pets.iterator();
            while (it.hasNext()) {
                Pet pet = it.next();
                sortme.add(pet);
            }
        }
        
        ComparatorPet pc = new ComparatorPet();
        
        Collections.sort(sortme, pc);
        
        return sortme;
    }
    
    public void addPet(Pet pet) {
        pets.add(pet);
    }
    
    public void removePet(Pet pet) {
        Util.ListRemove(pets, pet);
    }
    
    public List<Aid> getAids() {
        return aids;
    }
    
    public void addAid(Aid aid) {
        aids.add(aid);
    }
    
    public void removeAid(Aid aid) {
        Util.ListRemove(aids, aid);
    }
    
    public String getData(String key) {
        return data.get(key);
    }
    
    public void setData(String key, Object val) {
        if (val == null) {
            if (hasData(key)) {
                data.remove(key);
            }
        } else {
            data.put(key, String.valueOf(val));
        }
    }
    
    public boolean hasData(String key) {
        return data.containsKey(key);
    }
    
    public void attackLeft(Player p, Action a) {
        //
    }
    
    public void attackRight(Player p, Action a) {
        boolean classWeapon = false; int dmg = 0;
        if (p.getItemInHand().getType() == Material.WOOD_AXE) {    dmg = 3; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.STONE_AXE) {   dmg = 3; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.IRON_AXE) {    dmg = 4; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.GOLD_AXE) {    dmg = 5; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.DIAMOND_AXE) { dmg = 6; classWeapon = true; }
        if (classWeapon) {
            dmg += (Config.damageAlter == true ? dmgClass : 0);
            if (p.isSneaking()) {
                if (a == Action.RIGHT_CLICK_BLOCK) {
                    AidMgr.addAid(p, p.getTargetBlock(null, 0).getLocation().getBlock());
                    return;
                }
            }
            if (p.getLocation().getPitch() <= -60) {
                new BarbToughSkin().execute(p, null, dmg);
            } else if (p.getLocation().getPitch() >= 60) {
                new BarbLeap().execute(p, null, dmg);
            } else {
                if (a == Action.RIGHT_CLICK_AIR) {
                    new BarbAxeWhirl().execute(p, null, dmg);
                }
            }
        } else {
            dmg += (Config.damageAlter == true ? dmgOther : 0);
        }
    }
    
    public void attackBow(Player p, float f) {
        new NoneArrow().execute(p, null, f);
    }
    
    public void attackEntity(Player p, EntityDamageByEntityEvent e) {
        int dmg = e.getDamage();
        boolean classWeapon = false;
        if (p.getItemInHand().getType() == Material.WOOD_AXE) {    classWeapon = true; }
        if (p.getItemInHand().getType() == Material.STONE_AXE) {   classWeapon = true; }
        if (p.getItemInHand().getType() == Material.IRON_AXE) {    classWeapon = true; }
        if (p.getItemInHand().getType() == Material.GOLD_AXE) {    classWeapon = true; }
        if (p.getItemInHand().getType() == Material.DIAMOND_AXE) { classWeapon = true; }
//TODO: Config - Change damage?
        //if () {
            if (classWeapon) {
                e.setDamage(dmg + dmgClass);
                if (p.getGameMode() == GameMode.SURVIVAL) {
                    p.getItemInHand().setDurability((short) (p.getItemInHand().getDurability() + 1));
                }
            } else {
                if (p.getItemInHand().getType() != Material.AIR) {
                    e.setDamage(dmg + dmgOther);
                }
            }
        //}
    }
    
    public void interactEntity(Player p, Entity ent) {
        //
    }
    
    public void toggleSneak(Player p, PlayerToggleSneakEvent e) {
        //
    }
    
    public Integer getMaxAids() {
        return maxAids;
    }
}

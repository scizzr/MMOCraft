package com.scizzr.bukkit.mmocraft.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.managers.AidMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.skills.NoneArrow;
import com.scizzr.bukkit.mmocraft.skills.WizardFireball;
import com.scizzr.bukkit.mmocraft.skills.WizardLightning;
import com.scizzr.bukkit.mmocraft.skills.WizardMeteor;
import com.scizzr.bukkit.mmocraft.skills.WizardTeleport;
import com.scizzr.bukkit.mmocraft.util.ComparatorPet;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class Wizard implements Race {
    private String player;
    private int experience;
    int maxAids = 2;
    private CopyOnWriteArrayList<Aid> aids = new CopyOnWriteArrayList<Aid>();
    private CopyOnWriteArrayList<Pet> pets = new CopyOnWriteArrayList<Pet>();
    private ConcurrentHashMap<String, String> data = new ConcurrentHashMap<String, String>();
    
    public String getName() {
        return "Wizard";
    }
    
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
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
        
        //pets.clear();
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
    
    public void attackLeft(Player p, PlayerInteractEvent e) {
        Action act = e.getAction();
        
        if (p.getItemInHand().getType() == Material.STICK) {
            if (act == Action.LEFT_CLICK_AIR) {
                if (p.isSneaking()) {
                    new WizardTeleport().execute(p, null, 0);
                } else {
                    new WizardFireball().execute(p, null, 0);
                }
            }
        }
    }
    
    public void attackRight(Player p, Action a) {
        Race race = RaceMgr.getRace(p.getName());
        
        int dmg = 0;
        int dmgClass = Config.alterDamage ? Util.calcDamage(race, dmg, Util.classWep(race, p.getItemInHand().getTypeId())) : dmg;
        dmg = dmgClass;
        
        if (p.getItemInHand().getType() == Material.STICK) {
            if (a == Action.RIGHT_CLICK_BLOCK) {
                if (p.isSneaking()) {
                    AidMgr.addAid(p, p.getTargetBlock(null, 0).getLocation().getBlock());
                    return;
                }
            }
            if (a == Action.RIGHT_CLICK_AIR) {
                if (p.getLocation().getPitch() <= -60) {
                    new WizardLightning().execute(p, null, 0);
                } else if (p.getLocation().getPitch() >= 60) {
                    
                } else {
                    new WizardMeteor().execute(p, null, 0);
                }
            }
        }
    }
    
    public void attackBow(Player p, float f) {
        new NoneArrow().execute(p, null, f);
    }
    
    public void attackEntity(Player p, EntityDamageByEntityEvent e) {
        Race race = RaceMgr.getRace(p.getName());
        
        int dmg = e.getDamage();
        int dmgClass = Config.alterDamage ? Util.calcDamage(race, dmg, Util.classWep(race, p.getItemInHand().getTypeId())) : dmg;
        e.setDamage(dmgClass);
        
        double diff = Util.setDec(p.getLocation().distance(e.getEntity().getLocation()), 2);
        
        if (diff >= 50) {
            RaceMgr.addExp(p.getName(), (int)diff, ChatColor.YELLOW + I18n._("niceshot", new Object[] {diff}));
        }
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

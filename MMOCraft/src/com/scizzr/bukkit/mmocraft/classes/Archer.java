package com.scizzr.bukkit.mmocraft.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.skills.ArcherFlaming;
import com.scizzr.bukkit.mmocraft.skills.ArcherRain;
import com.scizzr.bukkit.mmocraft.skills.ArcherRing;
import com.scizzr.bukkit.mmocraft.skills.NoneArrow;
import com.scizzr.bukkit.mmocraft.util.ComparatorPet;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

@SuppressWarnings("unused")
public class Archer implements Race {
    private String player;
    private int experience;
    int maxAids = 2;
    private int dmgClass = +0;
    private int dmgOther = -4;
    private CopyOnWriteArrayList<Aid> aids = new CopyOnWriteArrayList<Aid>();
    private CopyOnWriteArrayList<Pet> pets = new CopyOnWriteArrayList<Pet>();
    private ConcurrentHashMap<String, String> data = new ConcurrentHashMap<String, String>();
    
    public String getName() {
        return "Archer";
    }
    
    public ChatColor getColor() {
        return ChatColor.RED;
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
    
    public void attackLeft(Player p, Action a) {
        //Punching the air or a block with a bow
    }
    
    public void attackRight(Player p, Action a) {
        if (p.getItemInHand().getType() == Material.BOW) {
            if (p.isSneaking()) {
                if (a == Action.RIGHT_CLICK_BLOCK) {
                    AidMgr.addAid(p, p.getTargetBlock(null, 0).getLocation().getBlock());
                    return;
                }
            }
        }
    }
    
    public void attackBow(Player p, float f) {
        if (p.getLocation().getPitch() <= -60) {
            new ArcherRain().execute(p, null, f);
        } else if (p.getLocation().getPitch() >= 60) {
            new ArcherRing().execute(p, null, f);
        } else {
            new ArcherFlaming().execute(p, null, f);
        }
    }
    
    public void attackEntity(Player p, EntityDamageByEntityEvent e) {
        int dmg = e.getDamage();
        
        double diff = Util.setDec(p.getLocation().distance(e.getEntity().getLocation()), 2);
        
        if (diff >= 50) {
            RaceMgr.addExp(p.getName(), (int)diff, ChatColor.YELLOW + I18n._("niceshot", new Object[] {diff}));
        }
//TODO: Config - Change damage?
        //if () {
            if (p.getItemInHand().getType() != Material.AIR) {
                e.setDamage(dmg - (Config.damageAlter ? dmgOther : 0));
            }
        //}
    }
    
    public void interactEntity(Player p, Entity ent) {
        //
    }
    
    public void toggleSneak(Player p, PlayerToggleSneakEvent e) {
        //
    }
    
    
    
    
    
    public void skillBow(Player p, float f) {
        new NoneArrow().execute(p, null, f);
    }
    
    public void skillH(Player p, Block b) {
        //if (SkillManager.isCooldown(p, "archer_helper")) { return; } else { SkillManager.addCooldown(p, "archer_helper", 100); }
        if (p.isSneaking()) {
            
        }
    }
    
    public Integer getMaxAids() {
        return maxAids;
    }
}

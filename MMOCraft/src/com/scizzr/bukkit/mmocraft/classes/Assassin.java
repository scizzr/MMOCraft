package com.scizzr.bukkit.mmocraft.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
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
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.skills.AssassinPowerUp;
import com.scizzr.bukkit.mmocraft.skills.AssassinShadowStep;
import com.scizzr.bukkit.mmocraft.skills.AssassinSneakAttack;
import com.scizzr.bukkit.mmocraft.skills.AssassinStab;
import com.scizzr.bukkit.mmocraft.skills.NoneArrow;
import com.scizzr.bukkit.mmocraft.util.ComparatorPet;
import com.scizzr.bukkit.mmocraft.util.Util;

public class Assassin implements Race {
    private String player;
    private int experience;
    int maxAids = 2;
    private CopyOnWriteArrayList<Aid> aids = new CopyOnWriteArrayList<Aid>();
    private CopyOnWriteArrayList<Pet> pets = new CopyOnWriteArrayList<Pet>();
    private ConcurrentHashMap<String, String> data = new ConcurrentHashMap<String, String>();
    
    public String getName() {
        return "Assassin";
    }
    
    public ChatColor getColor() {
        return ChatColor.DARK_GRAY;
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
        //Assassins don't attack the air!
    }
    
    public void attackRight(Player p, Action a) {
        boolean classWeapon = false;
        if (p.getItemInHand().getType() == Material.WOOD_SWORD) {    classWeapon = true; }
        if (p.getItemInHand().getType() == Material.GOLD_SWORD) {    classWeapon = true; }
        if (p.getItemInHand().getType() == Material.STONE_SWORD) {   classWeapon = true; }
        if (p.getItemInHand().getType() == Material.IRON_SWORD) {    classWeapon = true; }
        if (p.getItemInHand().getType() == Material.DIAMOND_SWORD) { classWeapon = true; }
        if (classWeapon) {
            if (p.isSneaking()) {
                if (a == Action.RIGHT_CLICK_BLOCK) {
                    AidMgr.addAid(p, p.getTargetBlock(null, 0).getLocation().getBlock());
                    return;
                }
            }
            if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                if (p.getLocation().getPitch() <= -60) {
                    new AssassinPowerUp().execute(p, null, 0);
                    SkillMgr.breakInvis(p);
                } else if (p.getLocation().getPitch() >= 60) {
                    
                } else {
                    
                }
            }
        }
    }
    
    public void attackBow(Player p, float f) {
        new NoneArrow().execute(p, null, f);
        SkillMgr.breakInvis(p);
    }
    
    public void attackEntity(Player p, EntityDamageByEntityEvent e) {
        Bukkit.broadcastMessage("behind?" + Util.isBehind(p, e.getEntity()));
        
        Race race = RaceMgr.getRace(p.getName());
        
        int dmg = e.getDamage();
        int dmgClass = Config.alterDamage ? Util.calcDamage(race, dmg, Util.classWep(race, p.getItemInHand().getTypeId())) : dmg;
        int dmgBehind = Util.isBehind(p, e.getEntity()) ? dmgClass+1 : dmgClass;
        int dmgSkill = (int)(hasData("invis") ? (p.isSneaking() ? dmgBehind*2.0 : dmgBehind*1.5) : dmgBehind);
        
        if (hasData("invis") && p.isSneaking()) {
            new AssassinSneakAttack().execute(p, e.getEntity(), 0);
        } else if (hasData("invis")) {
            new AssassinStab().execute(p, e.getEntity(), 0);
        }
        
        e.setDamage(dmgSkill);
        
        SkillMgr.breakInvis(p);
    }
    
    public void interactEntity(Player p, Entity ent) {
        if (p.isSneaking()) {
            new AssassinShadowStep().execute(p, ent, 0);
        }
    }
    
    public void toggleSneak(Player p, PlayerToggleSneakEvent e) {
        //
    }
    
    public Integer getMaxAids() {
        return maxAids;
    }
}

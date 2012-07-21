package com.scizzr.bukkit.plugins.mmocraft.interfaces.classes;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.NoneArrow;

public class None implements Race {
    private String player;
    private int experience;
    private ConcurrentHashMap<Pet, Boolean> pets = new ConcurrentHashMap<Pet, Boolean>();
    private ConcurrentHashMap<String, String> data = new ConcurrentHashMap<String, String>();
    
    public String getName() {
        return "None";
    }
    
    public ChatColor getColor() {
        return ChatColor.GRAY;
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
    
    public ConcurrentHashMap<Pet, Boolean> getPets() {
        return pets;
    }
    
    public void addPet(Pet pet) {
        pets.put(pet, true);
    }
    
    public void removePet(Pet pet) {
        pets.remove(pet);
    }
    
    public String getData(String key) {
        return data.get(key);
    }
    
    public void setData(String key, String val) {
        data.put(key, val);
    }
    
    public void attackLeft(Player p, Action a) {
        //
    }
    
    public void attackRight(Player p, Action a) {
        //
    }
    
    public void attackEntity(Player p, EntityDamageByEntityEvent e) {
        //
    }
    
    public void interactEntity(Player p, Entity ent) {
        //
    }
    
    public void attackBow(Player p, float f) {
        skillBow(p, f);
    }
    
    public void skillBow(Player p, float f) {
        new NoneArrow().execute(p, f);
    }
    
    public void skill0(Player p, float f) {
        new NoneArrow().execute(p, f);
    }
    
    public void skill1(Player p, float f) {
        //
    }
    
    public void skill2(Player p, float f) {
        //
    }
    
    public void skill3(Player p, float f) {
        //
    }
    
    public void skillH(Player p, Block b) {
        //
    }
}

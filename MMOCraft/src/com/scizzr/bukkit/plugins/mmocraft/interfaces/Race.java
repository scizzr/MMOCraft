package com.scizzr.bukkit.plugins.mmocraft.interfaces;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract interface Race {
    public abstract String getName();
    public abstract ChatColor getColor();
    public abstract String getPlayerName();
    public abstract void setPlayerName(String play);
    public abstract Integer getExp();
    public abstract void setExp(int i);
    public abstract ConcurrentHashMap<Pet, Boolean> getPets();
    public abstract void addPet(Pet pet);
    public abstract void removePet(Pet pet);
    public abstract String getData(String key);
    public abstract void setData(String key, String val);
    
    public abstract void attackLeft(Player p, Action a);
    public abstract void attackRight(Player p, Action a);
    public abstract void attackBow(Player p, float f);
    public abstract void attackEntity(Player p, EntityDamageByEntityEvent e);
    public abstract void interactEntity(Player p, Entity ent);
    
    public abstract void skillBow(Player p, float f);
    public abstract void skill0(Player p, float f);
    public abstract void skill1(Player p, float f);
    public abstract void skill2(Player p, float f);
    public abstract void skill3(Player p, float f);
    public abstract void skillH(Player p, Block b);
}

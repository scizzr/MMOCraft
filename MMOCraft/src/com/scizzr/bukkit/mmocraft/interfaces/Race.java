package com.scizzr.bukkit.mmocraft.interfaces;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public abstract interface Race {
    public abstract String getName();
    public abstract ChatColor getColor();
    public abstract String getPlayerName();
    public abstract void setPlayerName(String play);
    public abstract Integer getExp();
    public abstract void setExp(int i);
    
    public abstract List<Pet> getPets();
    public abstract void addPet(Pet pet);
    public abstract void removePet(Pet pet);
    
    public abstract List<Aid> getAids();
    public abstract void addAid(Aid aid);
    public abstract void removeAid(Aid aid);
    public abstract Integer getMaxAids();
    
    public abstract String getData(String key);
    public abstract void setData(String key, Object val);
    public abstract boolean hasData(String key);
    
    public abstract void attackLeft(Player p, PlayerInteractEvent e);
    public abstract void attackRight(Player p, Action a);
    public abstract void attackBow(Player p, float f);
    public abstract void attackEntity(Player p, EntityDamageByEntityEvent e);
    public abstract void interactEntity(Player p, Entity ent);
    public abstract void toggleSneak(Player p, PlayerToggleSneakEvent e);
}

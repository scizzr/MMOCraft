package com.scizzr.bukkit.plugins.mmocraft.interfaces;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public abstract interface Race {
    public abstract String getName();
    public abstract ChatColor getColor();
    public abstract String getPlayer();
    public abstract void setPlayer(String play);
    public abstract Integer getExp();
    public abstract void setExp(int i);
    
    public abstract void attackLeft(Player p, Action a);
    public abstract void attackRight(Player p, Action a);
    public abstract void attackBow(Player p, float f);
    public abstract void attackEntity(Player p, Entity ent);
    
    public abstract void skillBow(Player p, float f);
    public abstract void skill0(Player p, float f);
    public abstract void skill1(Player p, float f);
    public abstract void skill2(Player p, float f);
    public abstract void skill3(Player p, float f);
    public abstract void skillH(Player p, Block b);
}

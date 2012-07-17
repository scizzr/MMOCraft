package com.scizzr.bukkit.plugins.mmocraft.classes;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.NoneArrow;

public class None implements Race {
    private String player;
    private int experience;
    
    public String getName() {
        return "None";
    }
    
    public ChatColor getColor() {
        return ChatColor.GRAY;
    }
    
    public String getPlayer() {
        return player;
    }
    
    public void setPlayer(String play) {
        player = play;
    }
    
    public Integer getExp() {
        return experience;
    }
    
    public void setExp(int i) {
        experience = i;
    }
    
    public void attackLeft(Player p, Action a) {
        //
    }
    
    public void attackRight(Player p, Action a) {
        
    }
    
    public void attackEntity(Player p, Entity ent) {
        
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

package com.scizzr.bukkit.plugins.mmocraft.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.ArcherFlaming;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.NoneArrow;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.ArcherRain;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.ArcherRing;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperMgr;

public class Archer implements Race {
    private String player;
    private int experience;
    
    public String getName() {
        return "Archer";
    }
    
    public ChatColor getColor() {
        return ChatColor.RED;
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
        //Punching with a bow???
    }
    
    public void attackRight(Player p, Action a) {
        if (p.getItemInHand().getType() == Material.BOW) {
            if (a == Action.RIGHT_CLICK_BLOCK && p.isSneaking()) { skillH(p, p.getTargetBlock(null, 0).getLocation().getBlock()); }
        }
    }
    
    public void attackBow(Player p, float f) {
        if (p.getLocation().getPitch() <= -60) {
            skill1(p, f);
        } else if (p.getLocation().getPitch() >= 60) {
            skill2(p, f);
        } else {
            skill0(p,f);
        }
    }
    
    public void attackEntity(Player p, Entity ent) {
        //
    }
    
    public void skillBow(Player p, float f) {
        new NoneArrow().execute(p, f);
    }
    
    public void skill0(Player p, float f) {
        new ArcherFlaming().execute(p, f);
    }
    
    public void skill1(Player p, float f) {
        new ArcherRain().execute(p, f);
    }
    
    public void skill2(Player p, float f) {
        new ArcherRing().execute(p, f);
    }
    
    public void skill3(Player p, float f) {
        //
    }
    
    public void skillH(Player p, Block b) {
        //if (SkillManager.isCooldown(p, "archer_helper")) { return; } else { SkillManager.addCooldown(p, "archer_helper", 100); }
        if (p.isSneaking()) {
            HelperMgr.addHelper(p, b);
        }
    }
}

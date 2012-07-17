package com.scizzr.bukkit.plugins.mmocraft.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.BarbarianLeap;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.BarbarianSpin;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.NoneArrow;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperMgr;

public class Barbarian implements Race {
    private String player;
    private int experience;
    private float dmg = 0;
    
    public String getName() {
        return "Barbarian";
    }
    
    public ChatColor getColor() {
        return ChatColor.GOLD;
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
        if (p.getItemInHand().getType() == Material.WOOD_AXE) {    dmg = 1.5f; }
        if (p.getItemInHand().getType() == Material.STONE_AXE) {   dmg = 2.0f; }
        if (p.getItemInHand().getType() == Material.IRON_AXE) {    dmg = 2.5f; }
        if (p.getItemInHand().getType() == Material.GOLD_AXE) {    dmg = 1.5f; }
        if (p.getItemInHand().getType() == Material.DIAMOND_AXE) { dmg = 3.0f; }
        if (dmg > 0) {
            if (p.isSneaking()) {
                if (a == Action.RIGHT_CLICK_BLOCK) { skillH(p, p.getTargetBlock(null, 0).getLocation().getBlock()); }
                return;
            }
            if (p.getLocation().getPitch() <= -60) {
                skill1(p, 0);
            } else if (p.getLocation().getPitch() >= 60) {
                skill2(p, 0);
            } else {
                //
            }
        }
    }
    
    public void attackBow(Player p, float f) {
        skillBow(p, f);
    }
    
    public void attackEntity(Player p, Entity ent) {
        //
    }
    
    public void skillBow(Player p, float f) {
        new NoneArrow().execute(p, f);
    }
    
    public void skill0(Player p, float f) {
        //
    }
    
    public void skill1(Player p, float f) {
        new BarbarianSpin().execute(p, f);
    }
    
    public void skill2(Player p, float f) {
        new BarbarianLeap().execute(p, f);
    }
    
    public void skill3(Player p, float f) {
        //
    }
    
    public void skillH(Player p, Block b) {
        //if (SkillManager.isCooldown(p, "archer_helper")) { return; } else { SkillManager.addCooldown(p, "archer_helper", 100); }
        HelperMgr.addHelper(p, b);
    }
}

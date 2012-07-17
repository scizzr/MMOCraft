package com.scizzr.bukkit.plugins.mmocraft.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.AssassinPowerUp;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.skills.NoneArrow;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperMgr;

public class Assassin implements Race {
    private String player;
    private int experience;
    private float dmg = 0;
    
    public String getName() {
        return "Assassin";
    }
    
    public ChatColor getColor() {
        return ChatColor.DARK_GRAY;
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
        boolean classWeapon = false;
        if (p.getItemInHand().getType() == Material.WOOD_SWORD) {    dmg = 1.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.STONE_SWORD) {   dmg = 2.0f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.IRON_SWORD) {    dmg = 2.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.GOLD_SWORD) {    dmg = 1.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.DIAMOND_SWORD) { dmg = 3.0f; classWeapon = true; }
        if (classWeapon == true && a == Action.LEFT_CLICK_AIR) { skill2(p, dmg+2); }
    }
    
    public void attackRight(Player p, Action a) {
        boolean classWeapon = false;
        if (p.getItemInHand().getType() == Material.WOOD_SWORD) {    dmg = 1.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.STONE_SWORD) {   dmg = 2.0f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.IRON_SWORD) {    dmg = 2.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.GOLD_SWORD) {    dmg = 1.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.DIAMOND_SWORD) { dmg = 3.0f; classWeapon = true; }
        if (classWeapon == true) {
            if (p.isSneaking()) {
                if (a == Action.RIGHT_CLICK_BLOCK) { skillH(p, p.getTargetBlock(null, 0).getLocation().getBlock()); }
                return;
            }
            if (a == Action.RIGHT_CLICK_AIR) {
                if (p.getLocation().getPitch() <= -60) {
                    skill0(p, dmg+2);
                } else if (p.getLocation().getPitch() >= 60) {
                    
                } else {
                    skill1(p, dmg+2);
                }
            }
        }
    }
    
    public void attackBow(Player p, float f) {
        skillBow(p, f);
    }
    
    public void attackEntity(Player p, Entity ent) {
        boolean classWeapon = false;
        if (p.getItemInHand().getType() == Material.WOOD_SWORD) {    dmg = 1.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.STONE_SWORD) {   dmg = 2.0f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.IRON_SWORD) {    dmg = 2.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.GOLD_SWORD) {    dmg = 1.5f; classWeapon = true; }
        if (p.getItemInHand().getType() == Material.DIAMOND_SWORD) { dmg = 3.0f; classWeapon = true; }
        if (classWeapon == true) {
            skill0(p, dmg+5);
        }
    }
    
    public void skillBow(Player p, float f) {
        new NoneArrow().execute(p, f);
    }
    
    public void skill0(Player p, float f) {
        new AssassinPowerUp().execute(p, f);
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
        //if (SkillMgr.isCooldown(p, "wizard_helper")) { return; } else { SkillMgr.addCooldown(p, "wizard_helper", 100); }
        HelperMgr.addHelper(p, b);
    }
}

package com.scizzr.bukkit.plugins.mmocraft.classes;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;

public class Archer {
    static int lvlArcherHalo   =  0;
    static int lvlArcherRing   = 10;
    static int lvlArcherRain   = 20;
    static int lvlArcherHelper = 30;
    
    static Random rand = new Random();
    
    public static void attackLeft(Player p, Action a) {
/*
        int exp = ClassManager.getExp(p);
        int lvl = ClassManager.getLevel(exp);
*/
    }
    
    public static void attackRight(Player p, Action a) {
        int exp = ClassManager.getExp(p);
        int lvl = ClassManager.getLevel(exp);
        
        if (p.getItemInHand().getType() == Material.BOW) {
            if (a == Action.RIGHT_CLICK_BLOCK) {
                if (p.isSneaking()) {
                    Location loc = p.getTargetBlock(null, 0).getLocation().clone();
                    Block b = loc.getBlock();
                    if (b.getLocation().clone().getBlock().getType() != Material.AIR) {
                        if (lvl >= lvlArcherHelper) { 
                            helper(p, b);
                        }
                    }
                }
            }
        }
    }
    
    public static void attackBow(Player p, float f) {
        int exp = ClassManager.getExp(p);
        int lvl = ClassManager.getLevel(exp);
        
        if (p.getLocation().getPitch() <= -60) {
            if (lvl >= lvlArcherRain) { arrowRain(p, f); } else arrowNormal(p, f);
        } else if (p.getLocation().getPitch() >= 60) {
            if (lvl >= lvlArcherRing) { arrowRing(p, f); } else arrowNormal(p, f);
        } else {
            arrowNormal(p, f);
        }
    }
    
    
    
    public static void helper(Player p, Block b) {
        if (SkillManager.isCooldown(p, "archer_helper")) { return; } else { SkillManager.addCooldown(p, "archer_helper", 100); }
        HelperManager.addHelper(p, b);
    }
    
    public static void arrowRain(Player p, float f) {
        if (SkillManager.isCooldown(p, "archer_rain")) { arrowNormal(p, f); return; } else { SkillManager.addCooldown(p, "archer_rain", 100); }
        for (int i = 1; i <= 360; i += 1) {
            Location eye = p.getEyeLocation().clone();
            
            eye.setYaw(i-1); eye.setPitch(-90 + rand.nextInt(45));
            
            final Vector direction = eye.getDirection().multiply(2);
            final Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
            arrow.setVelocity(direction.multiply(f)); arrow.setShooter(p);// arrow.setFireTicks(200);
            
            ArrowTimer.add(arrow, 150);
        }
    }
    
    public static void arrowRing(Player p, float f) {
        if (SkillManager.isCooldown(p, "archer_ring")) { arrowNormal(p, f); return; } else { SkillManager.addCooldown(p, "archer_ring", 100); }
        for (int i = 1; i <= 360; i += 1) {
            Location eye = p.getEyeLocation().clone();
            
            eye.setYaw(i-1); eye.setPitch(0 + rand.nextFloat() * 5 - 2);
            
            final Vector direction = eye.getDirection().multiply(2);
            final Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
            arrow.setVelocity(direction.multiply(f)); arrow.setShooter(p);// arrow.setFireTicks(200);
            
            ArrowTimer.add(arrow, 50);
        }
    }
    
    public static void arrowNormal(Player p, float f) {
        Location eye = p.getEyeLocation().clone();
        
        final Vector direction = eye.getDirection();
        final Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
        arrow.setVelocity(direction.multiply(f*2).add(direction.multiply(0.5))); arrow.setShooter(p);
        
        CraftArrow ca = (CraftArrow)arrow; ca.getHandle().fromPlayer = true;
        
        ArrowTimer.add(arrow, 100);
    }
}

package com.scizzr.bukkit.plugins.mmocraft.classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Helper;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.HelperManager;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.helpers.Turret;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;
import com.scizzr.bukkit.plugins.mmocraft.threads.Lightning;
import com.scizzr.bukkit.plugins.mmocraft.timers.FireballTimer;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreMath;

public class Wizard {
    static int lvlWizardMeteor    =  0;
    static int lvlWizardLightning = 10;
    static int lvlWizardFireball  = 20;
    static int lvlWizardTrap      = 30;
    
    public static void attackLeft(Player p, Action a) {
        
    }
    
    public static void attackRight(Player p, Action a) {
        if (p.getItemInHand().getType() == Material.STICK) {
            if (a == Action.LEFT_CLICK_AIR) {
                if (p.isSneaking()) {
                    fireball(p);
                }
            } else if (a == Action.RIGHT_CLICK_AIR) {
                if (p.getLocation().getPitch() <= -60) {
                    if (SkillManager.isCooldown(p, "wizard_lightning")) { return; } else { SkillManager.addCooldown(p, "wizard_lightning", 100); }
                    try { new Thread(new Lightning(p)).start(); } catch (Exception ex) { /* No Spam */ }
                } else if (p.getLocation().getPitch() >= 60) {
                    
                } else {
                    meteor(p);
                }
            } else if (a == Action.RIGHT_CLICK_BLOCK) {
                if (p.isSneaking()) {
                    Location loc = p.getTargetBlock(null, 0).getLocation().clone();
                    Block b = loc.getBlock();
                    if (b.getLocation().clone().getBlock().getType() != Material.AIR) {
                        HelperManager.addHelper(p, b);
                    }
                }
            }
        }
    }
    
    
    
    public static void fireball(Player p) {
        Location loc = p.getLocation();
        Vector direction = loc.getDirection();
        
        Fireball fb = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
        fb.setVelocity(direction); fb.setShooter(p); fb.setYield(0); fb.setIsIncendiary(false);
        
        FireballTimer.addFireball(fb, 30);
    }
    
    public static void meteor(Player p) {
        if (SkillManager.isCooldown(p, "wizard_meteor")) { return; } else { SkillManager.addCooldown(p, "wizard_meteor", 60); }
        
        Block block = p.getTargetBlock(null, 120);
        
        Location locP = p.getLocation().clone();
        Location locB = block.getLocation().clone();
        
        double a = locP.distance(locB)+(locB.getBlockY() - locP.getBlockY());   // Distance from player 1 to block
        double b = 10.0;                                                        // Block from player 1 to meteor (up)
        //double c = Math.sqrt((a*a) + (b*b));                                  // Distance from meteor to block
        
        double t = Math.atan(a/b);                                              // Tangent from block to meteor
        
        double A = t * (180/Math.PI);                                           // Block's angle to meteor
        //double B = 180 - A - 90.0;                                            // Angle to fire meteor
        //double C = 90.0;                                                      // Square angle
        
        Location loc = p.getEyeLocation().clone().add(0, b, 0);
        loc.setPitch((float) MoreMath.angleToPitch((int) A));
        
        final Vector direction = loc.getDirection().multiply(1);
        Fireball fireball = p.getWorld().spawn(loc.add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
        fireball.setShooter(p); fireball.setYield(1);
    }
    
/*
    public static void stepTrap(Entity ent, Block b) {
        if (isTrap(b)) {
            Player own = traps.get(b.getWorld().getName() + "," + b.getX() + "," + b.getY() + "," + b.getZ());
            if (own != ent) {
                EntityManager.setAttacker(ent, own);
                b.setType(Material.AIR);
                traps.remove(b.getWorld().getName() + "," + b.getX() + "," + b.getY() + "," + b.getZ());
                
                if (ent instanceof LivingEntity) { ent.setFireTicks(ent.getFireTicks()+60); }
                
                if (own.isOnline()) {
                    Location loc = b.getLocation();
                    String name = ent instanceof Player ? ((Player)ent).getName() : ent.getType().getName();
                    own.sendMessage(Main.prefix + "Trap at [" + loc.getWorld().getName() + "] " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + " sprung by " + name);
                }
            }
        }
    }
*/
}

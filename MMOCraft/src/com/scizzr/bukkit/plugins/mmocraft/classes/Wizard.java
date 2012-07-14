package com.scizzr.bukkit.plugins.mmocraft.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;
import com.scizzr.bukkit.plugins.mmocraft.threads.Lightning;
import com.scizzr.bukkit.plugins.mmocraft.timers.FireballTimer;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreMath;

public class Wizard {
    static int lvlWizardMeteor    =  0;
    static int lvlWizardLightning = 10;
    static int lvlWizardFireball  = 20;
    static int lvlWizardHelper    = 30;
    
    public static void attackLeft(Player p, Action a) {
        int exp = ClassManager.getExp(p);
        int lvl = ClassManager.getLevel(exp);
        if (a == Action.LEFT_CLICK_AIR) {
            if (lvl >= lvlWizardHelper) {
                fireball(p);
            }
        } 
    }
    
    public static void attackRight(Player p, Action a) {
        int exp = ClassManager.getExp(p);
        int lvl = ClassManager.getLevel(exp);
        
        if (p.getItemInHand().getType() == Material.STICK) {
            if (a == Action.RIGHT_CLICK_AIR) {
                if (p.getLocation().getPitch() <= -60) {
                    if (lvl >= lvlWizardLightning) {
                        try { new Thread(new Lightning(p)).start(); } catch (Exception ex) { /* No Spam */ }
                    }
                } else if (p.getLocation().getPitch() >= 60) {
                    
                } else {
                    if (lvl >= lvlWizardHelper) {
                        meteor(p);
                    }
                }
            } else if (a == Action.RIGHT_CLICK_BLOCK) {
                if (p.isSneaking()) {
                    if (lvl >= lvlWizardHelper) {
                        Location loc = p.getTargetBlock(null, 0).getLocation().clone();
                        Block b = loc.getBlock();
                        if (b.getLocation().clone().getBlock().getType() != Material.AIR) {
                            helper(p, b);
                        }
                    }
                }
            }
        }
    }
    
    
    
    public static void helper(Player p, Block b) {
        if (SkillManager.isCooldown(p, "wizard_helper")) { return; } else { SkillManager.addCooldown(p, "wizard_helper", 100); }
        HelperManager.addHelper(p, b);
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

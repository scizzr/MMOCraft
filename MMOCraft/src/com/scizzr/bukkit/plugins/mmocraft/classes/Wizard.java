package com.scizzr.bukkit.plugins.mmocraft.classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;
import com.scizzr.bukkit.plugins.mmocraft.threads.Lightning;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreMath;

public class Wizard {
    private static HashMap<String, Player> traps = new HashMap<String, Player>();
    private static HashMap<Integer, Boolean> validTrapBlocks = new HashMap<Integer, Boolean>();
    
    public static void main() {
        validTrapBlocks.put(1, true); validTrapBlocks.put(2, true); validTrapBlocks.put(3, true); validTrapBlocks.put(4, true);
        validTrapBlocks.put(5, true); validTrapBlocks.put(14, true); validTrapBlocks.put(15, true); validTrapBlocks.put(16, true);
        validTrapBlocks.put(17, true); validTrapBlocks.put(19, true); validTrapBlocks.put(21, true); validTrapBlocks.put(22, true);
        validTrapBlocks.put(24, true); validTrapBlocks.put(35, true); validTrapBlocks.put(41, true); validTrapBlocks.put(42, true);
        validTrapBlocks.put(43, true); validTrapBlocks.put(47, true); validTrapBlocks.put(48, true); validTrapBlocks.put(49, true);
        validTrapBlocks.put(56, true); validTrapBlocks.put(57, true); validTrapBlocks.put(73, true); validTrapBlocks.put(74, true);
        validTrapBlocks.put(86, true); validTrapBlocks.put(87, true); validTrapBlocks.put(88, true); validTrapBlocks.put(89, true);
        validTrapBlocks.put(91, true); validTrapBlocks.put(97, true); validTrapBlocks.put(98, true); validTrapBlocks.put(99, true);
        validTrapBlocks.put(100, true); validTrapBlocks.put(103, true); validTrapBlocks.put(110, true); validTrapBlocks.put(112, true);
    }
    
    public static void attackLeft(Player p, Entity t) {
        
    }
    
    public static void attackRight(Player p, Action a) {
        if (p.getItemInHand().getType() == Material.STICK) {
            if (a == Action.LEFT_CLICK_AIR) {
                castFireball(p);
            } else if (a == Action.RIGHT_CLICK_AIR) {
                if (p.getLocation().getPitch() <= -60) {
                    if (SkillManager.isCooldown(p, "wizard_lightning")) { return; } else { SkillManager.addCooldown(p, "wizard_lightning", 100); }
                    try { new Thread(new Lightning(p)).start(); } catch (Exception ex) { /* No Spam */ }
                } else if (p.getLocation().getPitch() >= 60) {
                    
                } else {
                    castMeteor(p);
                }
            } else if (a == Action.RIGHT_CLICK_BLOCK) {
                Location loc = p.getTargetBlock(null, 0).getLocation().clone();
                Block b = loc.getBlock();
                if (b.getLocation().clone().getBlock().getType() != Material.AIR) {
                    addTrap(p, b);
                }
            }
        }
    }
    
    
    
    public static void castFireball(Player p) {
        if (SkillManager.isCooldown(p, "wizard_fireball")) { return; } else { SkillManager.addCooldown(p, "wizard_fireball", 40); }
        
        Location loc = p.getEyeLocation().clone();
        
        final Vector direction = loc.getDirection().multiply(2);
        Fireball fireball = p.getWorld().spawn(loc.add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
        fireball.setShooter(p); fireball.setIsIncendiary(false); fireball.setYield(1);
    }
    
    public static void castMeteor(Player p) {
        if (SkillManager.isCooldown(p, "wizard_meteor")) { return; } else { SkillManager.addCooldown(p, "wizard_meteor", 60); }
        
        Block block = p.getTargetBlock(null, 120);
        
        Location locP = p.getLocation().clone();
        Location locB = block.getLocation().clone();
        
        double a = locP.distance(locB)+(locB.getBlockY() - locP.getBlockY());   // Distance from player 1 to block
        double b = 10.0;                                                        // Block from player 1 to meteor (up)
        //double c = Math.sqrt((a*a) + (b*b));                                  // Distance from meteor to block
        
        double t = Math.atan(a/b);                                              // Tangent from block to meteor
        
        Bukkit.broadcastMessage("angle = " + t);
        
        double A = t * (180/Math.PI);                                           // Block's angle to meteor
        //double B = 180 - A - 90.0;                                            // Angle to fire meteor
        //double C = 90.0;                                                      // Square angle
        
        Location loc = p.getEyeLocation().clone().add(0, b, 0);
        loc.setPitch((float) MoreMath.angleToPitch((int) A));
        
        final Vector direction = loc.getDirection().multiply(1);
        Fireball fireball = p.getWorld().spawn(loc.add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
        fireball.setShooter(p); fireball.setYield(1);
    }
    
    public static void addTrap(Player p, Block b) {
        Block bTarg = b.getLocation().clone().add(0, 1, 0).getBlock();
        if (isNearTrap(bTarg)) {
            p.sendMessage(Main.prefix + "There's another trap nearby."); return;
/*
        } else if (isTrap(bTarg)) {
            p.sendMessage(Main.prefix + "There's already a trap there."); return;
*/
        } else if (isNearTrap(bTarg)) {
            p.sendMessage(Main.prefix + "There's another trap nearby."); return;
        } else if (b.getTypeId() == 70) {
            p.sendMessage(Main.prefix + "There's already a plate there."); return;
        } else if (!validTrapBlocks.containsKey(b.getTypeId())) {
            p.sendMessage(Main.prefix + "You can't build a trap on that block."); return;
        }
        
        if (SkillManager.isCooldown(p, "wizard_trap")) { return; } else { SkillManager.addCooldown(p, "wizard_trap", 60); }
        
        bTarg.setType(Material.STONE_PLATE);
        traps.put(bTarg.getX() + "," + bTarg.getY() + "," + bTarg.getZ(), p);
        Location loc = bTarg.getLocation();
        p.sendMessage(Main.prefix + "Trap added at " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
    }
    
    public static void stepTrap(Entity ent, Block b) {
        if (isTrap(b)) {
            Player own = traps.get(b.getX() + "," + b.getY() + "," + b.getZ());
            if (own != ent) {
                EntityManager.setAttacker(ent, own);
                b.setType(Material.AIR);
                traps.remove(b.getX() + "," + b.getY() + "," + b.getZ());
                ent.setFireTicks(ent.getFireTicks()+60);
                if (own.isOnline()) {
                    Location loc = b.getLocation();
                    String name = ent instanceof Player ? ((Player)ent).getName() : ent.getType().getName();
                    own.sendMessage(Main.prefix + "Trap at " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + " sprung by " + name);
                }
            }
        }
    }
    
    public static void removeTrap(Block b, Player p) {
        if (isTrap(b)) {
            Player own = traps.get(b.getX() + "," + b.getY() + "," + b.getZ());
            if (own.isOnline()) {
                Location loc = b.getLocation();
                own.sendMessage(Main.prefix + "Trap at " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + " broken by " + p.getName());
            }
            traps.remove(b.getX() + "," + b.getY() + "," + b.getZ());
            b.setType(Material.AIR);
        }
    }
    
    public static void flipTraps() {
        Block b;
        for (String s : traps.keySet()) {
            String[] pos = s.split(",");
            String w = "world";
            int x = Integer.valueOf(pos[0]);
            int y = Integer.valueOf(pos[1]);
            int z = Integer.valueOf(pos[2]);
            
            b = Bukkit.getWorld(w).getBlockAt(new Location(Bukkit.getWorld(w), x, y, z));
            if (Main.calS % 2 == 0) {
                b.setType(Material.WOOD_PLATE);
            } else {
                b.setType(Material.STONE_PLATE);
            }
        }
    }
    
    public static boolean isNearTrap(Block b) {
        Location loc;
        for (int x = b.getX()-1; x <= b.getX()+1; x++) {
            for (int y = b.getY()-1; y <= b.getY()+1; y++) {
                for (int z = b.getZ()-1; z <= b.getZ()+1; z++) {
                    loc = b.getLocation().clone();
                    loc.setX(x); loc.setY(y); loc.setZ(z);
                    if (isTrap(loc.getBlock())) { return true; }
                }
            }
        }
        return false;
    }
    
    public static boolean isTrap(Block b) {
        return traps.containsKey(b.getX() + "," + b.getY() + "," + b.getZ());
    }
}

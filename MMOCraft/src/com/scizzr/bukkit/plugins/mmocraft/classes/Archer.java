package com.scizzr.bukkit.plugins.mmocraft.classes;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;

public class Archer {
    static int lvlArcherRing =   10;
    static int lvlArcherRain =   20;
    static int lvlArcherTurret = 30;
    
    static Random rand = new Random();
    
    private static HashMap<String, Player> turrets = new HashMap<String, Player>();
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
        loadTurrets();
    }
    
    public static void attackLeft(Player p, Entity t) {
//        int exp = ClassManager.getExp(p);
//        int lvl = ClassManager.getLevel(exp);
    }
    
    public static void attackRight(Player p, Action a) {
        int exp = ClassManager.getExp(p);
        int lvl = ClassManager.getLevel(exp);
        if (p.getItemInHand().getType() == Material.BOW) {
            if (a == Action.RIGHT_CLICK_BLOCK) {
                Location loc = p.getTargetBlock(null, 0).getLocation().clone();
                Block b = loc.getBlock();
                if (b.getLocation().clone().getBlock().getType() != Material.AIR) {
                    if (lvl >= lvlArcherTurret) { 
                        addTurret(p, b);
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
    
    
    
    public static void arrowRain(Player p, float f) {
        if (SkillManager.isCooldown(p, "archer_rain")) { arrowNormal(p, f); return; } else { SkillManager.addCooldown(p, "archer_rain", 100); }
        for (int i = 1; i <= 360; i += 1) {
            Location eye = p.getEyeLocation().clone();
            
            eye.setYaw(i-1); eye.setPitch(-90 + rand.nextInt(45));
            
            final Vector direction = eye.getDirection().multiply(2);
            final Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
            arrow.setVelocity(direction.multiply(f)); arrow.setShooter(p);// arrow.setFireTicks(200);
            
            ArrowTimer.add(arrow, 8);
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
            
            ArrowTimer.add(arrow, 3);
        }
    }
    
    public static void arrowNormal(Player p, float f) {
        Location eye = p.getEyeLocation().clone();
        
        final Vector direction = eye.getDirection();
        final Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
        arrow.setVelocity(direction.multiply(f*2).add(direction.multiply(0.5))); arrow.setShooter(p);
        
        ArrowTimer.add(arrow, 10);
    }
    
    public static void addTurret(Player p, Block b) {
        Block bTarg = b.getLocation().clone().add(0, 1, 0).getBlock();
        if (isNearTurret(bTarg)) {
            p.sendMessage(Main.prefix + "There's another turret nearby."); return;
/*
        } else if (isTrap(bTarg)) {
            p.sendMessage(Main.prefix + "There's already a trap there."); return;
*/
        } else if (isNearTurret(bTarg)) {
            p.sendMessage(Main.prefix + "There's another turret nearby."); return;
        } else if (b.getTypeId() == 70) {
            p.sendMessage(Main.prefix + "There's already a turret there."); return;
        } else if (!validTrapBlocks.containsKey(b.getTypeId())) {
            p.sendMessage(Main.prefix + "You can't build a turret on that block."); return;
        }
        
        if (SkillManager.isCooldown(p, "archer_turret")) { return; } else { SkillManager.addCooldown(p, "archer_turret", 60); }
        
        bTarg.setType(Material.FENCE);
        turrets.put(bTarg.getX() + "," + bTarg.getY() + "," + bTarg.getZ(), p);
        Location loc = bTarg.getLocation();
        p.sendMessage(Main.prefix + "Turret added at " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
    }
    
    public static void removeTurret(Block b, Player p) {
        if (isTurret(b)) {
            Player own = turrets.get(b.getX() + "," + b.getY() + "," + b.getZ());
            if (own.isOnline()) {
                Location loc = b.getLocation();
                own.sendMessage(Main.prefix + "Turret at " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + " broken by " + p.getName());
            }
            turrets.remove(b.getX() + "," + b.getY() + "," + b.getZ());
            b.setType(Material.AIR);
        }
    }
    
    public static void flipTurrets() {
        Block b;
        for (String s : turrets.keySet()) {
            String[] pos = s.split(",");
            String w = "world";
            int x = Integer.valueOf(pos[0]);
            int y = Integer.valueOf(pos[1]);
            int z = Integer.valueOf(pos[2]);
            
            b = Bukkit.getWorld(w).getBlockAt(new Location(Bukkit.getWorld(w), x, y, z));
            if (Main.calS % 2 == 0) {
                b.setType(Material.FENCE);
            } else {
                b.setType(Material.NETHER_FENCE);
            }
        }
    }
    
    public static void fireTurrets() {
        for (String s : turrets.keySet()) {
/*
            String[] pos = s.split(",");
            String w = "world";
            int x = Integer.valueOf(pos[0]);
            int y = Integer.valueOf(pos[1]);
            int z = Integer.valueOf(pos[2]);
            
            Location locTur = new Location(Bukkit.getWorld(w), x+0.5, y+1.5, z+0.5);
            
            for (int i = 1; i <= 360; i += 1) {
                Location eye = locTur.clone();
                
                eye.setYaw(i-1); eye.setPitch(-90 + rand.nextInt(45));
                
                final Vector direction = eye.getDirection().multiply(2);
                final Snowball arrow = Bukkit.getWorld(w).spawn(locTur, Snowball.class);
                arrow.setVelocity(direction.multiply(1)); arrow.setShooter(turrets.get(s)); arrow.setFireTicks(200);
                
                //ArrowTimer.add(arrow, 5);
            }
*/
            String[] pos = s.split(",");
            String w = "world";
            int x = Integer.valueOf(pos[0]);
            int y = Integer.valueOf(pos[1]);
            int z = Integer.valueOf(pos[2]);
            
            Location locT = new Location(Bukkit.getWorld(w), x+0.5, y+1.5, z+0.5);
            
            for (Entity ent : Bukkit.getWorld(w).getEntities()) {
                if (!(ent instanceof Creature || ent instanceof Player)) { continue; }
                if (ent instanceof Player) { Player p = (Player)ent; if (turrets.get(s) == p || p.isOp() || p.getGameMode() == GameMode.CREATIVE) { continue; } }
                
                Location locTur = locT.clone();
                Location locEnt = ent.getLocation();
                
                if (locTur.distance(locEnt) <= 10) {
                    //XXX Bukkit.broadcastMessage(ChatColor.GREEN + "Our [§6TURRET§a] location is " + ChatColor.RESET + locTur.getBlockX() + "," + locTur.getBlockY() + "," + locTur.getBlockZ());
                    //XXX Bukkit.broadcastMessage(ChatColor.RED + "Enemy [" + ChatColor.GOLD + ent.getType().toString() + ChatColor.RED + "] found at " + ChatColor.RESET + locEnt.getBlockX() + "," + locEnt.getBlockY() + "," + locEnt.getBlockZ());
                    
                    double a = locEnt.getBlockX()-locTur.getBlockX();
                    double b = locEnt.getBlockZ()-locTur.getBlockZ();
                    //double c = Math.sqrt((a*a) + (b*b));
                    
                    //XXX Bukkit.broadcastMessage("The difference is " + a + "," + b);
                    
                    double t = Math.atan(a/b);
                    
                    double A = t * (180/Math.PI);
                    //double B = 180 - A - 90.0;
                    //double C = 90.0;
                    
                    int add = locTur.getBlockZ() <= locEnt.getBlockZ() ? 360 : 180;
                    
                    //XXX Bukkit.broadcastMessage("The angle is " + A);
                    
                    Location loc = ent.getLocation().clone();
                    loc.setPitch(5); //loc.setYaw((float) MoreMath.angleToPitch((int) A));
                    loc.setYaw(add-(float) A);
                    
                    final Vector direction = loc.getDirection().multiply(2);
                    
                    Arrow arrow = Bukkit.getWorld(w).spawn(locTur, Arrow.class);
                    arrow.setVelocity(direction); //arrow.setShooter(turrets.get(s)); arrow.setFireTicks(60);
                    
                    ArrowTimer.add(arrow, 3);
                }
            }
        }
    }
    
    public static boolean isNearTurret(Block b) {
        Location loc;
        for (int x = b.getX()-1; x <= b.getX()+1; x++) {
            for (int y = b.getY()-1; y <= b.getY()+1; y++) {
                for (int z = b.getZ()-1; z <= b.getZ()+1; z++) {
                    loc = b.getLocation().clone();
                    loc.setX(x); loc.setY(y); loc.setZ(z);
                    if (isTurret(loc.getBlock())) { return true; }
                }
            }
        }
        return false;
    }
    
    public static boolean isTurret(Block b) {
        return turrets.containsKey(b.getX() + "," + b.getY() + "," + b.getZ());
    }
    
    public static void loadTurrets() {
        
    }

    public static void saveTurrets() {
        
    }
}

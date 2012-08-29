package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.timers.ArrowTimer;

public class ArcherFullAuto implements Skill {
    int cooldown = 900;
    int lvlReq   =  10;
    int itemCost =  10;
    
    Random rand = new Random();
    
    public String getName() {
        return "Full Auto";
    }
    
    public void execute(final Player p, Entity ent, final float f) {
        if (isCooldown(p)) { new NoneArrow().execute(p, ent, f); return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { new NoneArrow().execute(p, ent, f); return; }
        
        if (p.getGameMode() == GameMode.SURVIVAL) {
            if (!p.getInventory().contains(Material.ARROW, itemCost)) { new NoneArrow().execute(p, ent, f); return; }
            p.getInventory().removeItem(new ItemStack(Material.ARROW, itemCost));
        }
        
        for (int i = 5; i < 20; i++) {
            final float count = (float)i/10;
            Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                public void run() {
                    SoundEffects.RANDOM_BREAK.playGlobal(p.getLocation(), 1.0f, count);
                }
            }, (long)i);
        }
        
        
        
        for (int i = 1; i <= 40; i += 1) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                public void run() {
                    Location eye = p.getEyeLocation().clone();
                    //eye.setPitch(-90 + (float)rand.nextInt(1500)/100);
                    
                    Vector direction = eye.getDirection().multiply(2);
                    Arrow arrow = p.getWorld().spawn(eye.add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
                    arrow.setVelocity(direction.multiply(f)); arrow.setShooter(p);// arrow.setFireTicks(200);
                    
                    ArrowTimer.add(arrow, 150);
                }
            }, (long)i*5+(15)+10); //wait until the sounds above are done playing and then 1/2 more second
        }
    }
    
    public boolean isCooldown(Player p) {
        return false;
    }
    
    public boolean isLevel(Player p) {
        Race race = RaceMgr.getRace(p.getName());
        if (race != null) {
            int exp = race.getExp();
            int lvl = RaceMgr.getLevel(exp);
            if (lvl >= lvlReq) {
                return true;
            }
        }
        return false;
    }
}

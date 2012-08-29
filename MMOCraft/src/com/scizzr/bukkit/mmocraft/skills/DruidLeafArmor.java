package com.scizzr.bukkit.mmocraft.skills;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;

public class DruidLeafArmor implements Skill {
    int cooldown =  60;
    int lvlReq   =   0;
    
    Random rand = new Random();
    
    public String getName() {
        return "Leaf Armor";
    }
    
    public void execute(final Player p, Entity ent, float f) {
        if (isCooldown(p)) { new NoneArrow().execute(p, ent, f); return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { new NoneArrow().execute(p, ent, f); return; }
        
        final Location loc = p.getLocation();
        final World world = loc.getWorld();
        
        for (int i = 0; i < 360; i += 5) {
            for (int j = 0; j <= 20; j++) {
                final int addY = j;
                Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                    public void run() {
                        ItemStack items = new ItemStack(Material.LEAVES, 1, (short)1);
                        final Item item = world.dropItem(p.isOnline() ? p.getLocation().add(0, (double)addY/10, 0) : loc.add(0, (double)addY/10, 0), items);
                        
                        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                            public void run() {
                                item.remove();
                            }
                        }, 5L);
                    }
                }, (long)(i));
            }
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

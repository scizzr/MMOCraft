package com.scizzr.bukkit.mmocraft.managers;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.entity.Entity;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.custommobs.CustomZombie;

public class MobMgr {
    static CopyOnWriteArrayList<UUID> mobs = new CopyOnWriteArrayList<UUID>();
    
    public static void main() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(MMOCraft.plugin, new Runnable() {
            public void run() {
                for (UUID id : mobs) {
                    if (EntityMgr.getEntityByUUID(id) == null) {
                        mobs.remove(id);
                    }
                }
                //synchronized(mobs) {
                    //Iterator<UUID> it = mobs.iterator();
                    //while (it.hasNext()) {
                        //if (EntityMgr.getEntityByUUID(it.next()) == null) {
                            //it.remove();
                        //}
                    //}
                //}
            }
        }, 0L, (long)20*3);
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(MMOCraft.plugin, new Runnable() {
            public void run () {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity ent : world.getEntities()) {
                        net.minecraft.server.Entity MCEnt = ((CraftEntity)ent).getHandle();
                        if (MCEnt instanceof CustomZombie) {
                            net.minecraft.server.EntityZombie mcZombie = ((CraftZombie)ent).getHandle();
                            
                            if (MobMgr.has(mcZombie.uniqueId)) {
                                //Location loc = ent.getLocation();
                                //loc.getWorld().playEffect(loc, Effect.SMOKE, 4);
                            }
                        }
                    }
                }
            }
        }, 0L, 20L);
    }
    
    public static void add(UUID id) {
        mobs.add(id);
    }
    
    public static void remove(UUID id) {
        synchronized (mobs) {
            Iterator<UUID> it = mobs.iterator();
            while (it.hasNext()) {
                if (!mobs.contains(it.next())) {
                    it.remove();
                }
            }
        }
    }
    
    public static boolean has(UUID id) {
        return mobs.contains(id);
    }
}

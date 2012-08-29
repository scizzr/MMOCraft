package com.scizzr.bukkit.mmocraft.custommobs;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.PathfinderGoalBreakDoor;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.Plugin;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.managers.MobMgr;

@SuppressWarnings("rawtypes")
public class CustomZombie extends EntityZombie {
    MMOCraft plugin;
    
    boolean special = false;
    
    public CustomZombie(World world) {
        this(world, MMOCraft.plugin);
    }
    
    public CustomZombie(World world, Plugin plugin) {
        super(world);
        
        Random rand = new Random();
        
        special = rand.nextInt(1) == 0;
        
        if ((plugin == null) || (!(plugin instanceof MMOCraft))) {
            this.world.removeEntity(this);
        }
        
        try {
            Field goalA = this.goalSelector.getClass().getDeclaredField("a");
            goalA.setAccessible(true);
            ((List)goalA.get(this.goalSelector)).clear();
            
            Field targetA = this.targetSelector.getClass().getDeclaredField("a");
            targetA.setAccessible(true);
            ((List)targetA.get(this.targetSelector)).clear();
            
            this.goalSelector.a(0, new PathfinderGoalFloat(this));
            this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
            this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, this.bw, false));
            this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityVillager.class, this.bw, true));
            this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, this.bw));
            this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, this.bw, false));
            this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, this.bw));
            this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
            this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
            
            this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
            this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
            this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        if (special) {
            try {
                //extra speed
                Field nav = EntityLiving.class.getDeclaredField("navigation");
                nav.setAccessible(true);
                nav.set(this, new Navigator(this.plugin, this, this.world, 16.0f));
                
                //add to MobMgr
                MobMgr.add(this.uniqueId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    protected String aQ() {
        if (special) {
            SoundEffects.MOB_ZOMBIE.playGlobal(this.getBukkitEntity().getLocation(), 1.0f, 0.5f);
            return "";
        }
        
        return "mob.zombie";
    }
    
    @Override
    protected String aR() {
        if (special) {
            SoundEffects.MOB_ZOMBIEHURT.playGlobal(this.getBukkitEntity().getLocation(), 1.0f, 0.5f);
            return "";
        }
        
        return "mob.zombiehurt";
    }
    
    @Override
    protected String aS() {
        if (special) {
            SoundEffects.MOB_ZOMBIEDEATH.playGlobal(this.getBukkitEntity().getLocation(), 1.0f, 0.5f);
            return "";
        }
        
        return "mob.zombiedeath";
    }
    
    public void h_() {
        Zombie zombie = (Zombie)this.getBukkitEntity();
        
        Location from = new Location(zombie.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
        Location to = new Location(zombie.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        
        ZombieMoveEvent e = new ZombieMoveEvent(zombie, from, to);
        
        //XXX - remove
        if (special) {
            zombie.getWorld().playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
        }
        
        this.world.getServer().getPluginManager().callEvent(e);
        
        if (e.isCancelled() && zombie.isDead() == false) {
            return;
        }
        
        super.h_();
    }
    
    protected Entity findTarget() {
        float distance = 16F;
        EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, distance);
        return (entityhuman != null) && (l(entityhuman)) ? entityhuman : null;
    }
}

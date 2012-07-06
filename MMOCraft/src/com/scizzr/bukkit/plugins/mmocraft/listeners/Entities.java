package com.scizzr.bukkit.plugins.mmocraft.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftFireball;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.classes.Wizard;
import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillManager;
import com.scizzr.bukkit.plugins.mmocraft.threads.Explosion;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreMath;

public class Entities implements Listener {
    Main plugin;
    
    public Entities(Main instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityInteract(final EntityInteractEvent e) {
        Entity ent = e.getEntity();
        Location loc = e.getBlock().getLocation().clone();
        Block b = loc.getBlock();
        Wizard.stepTrap(ent, b);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
        Entity eAtt = e.getDamager();
        Entity eDef = e.getEntity();
        Player pAtt = null;
        
        if (eAtt instanceof Fireball) {
            if (((Fireball)eAtt).getShooter() instanceof Player) {
                pAtt = (Player)((Fireball)eAtt).getShooter();
            }
        } else if (eAtt instanceof SmallFireball) {
            if (((SmallFireball)eAtt).getShooter() instanceof Player) {
                pAtt = (Player)((SmallFireball)eAtt).getShooter();
            }
        } else if (eAtt instanceof Arrow) {
            if (((Arrow)eAtt).getShooter() instanceof Player) {
                pAtt = (Player)((Arrow)eAtt).getShooter();
            }
        } else if (eAtt instanceof Player) {
            pAtt = (Player)eAtt;
        }
        
        if (pAtt != null) {
            EntityManager.setAttacker(eDef, pAtt);
            double diff = MoreMath.setDec(pAtt.getLocation().distance(eDef.getLocation()), 2);
            
            if (diff >= 50) {
                ClassManager.addExp(pAtt, (int)diff, "Nice shot! +%s XP (" + diff + " blocks)");
            }
        }
        
        /*
        if (PetManager.getMaster(mob) == p) {
            LivingEntity len = p.getWorld().spawnCreature(mob.getLocation(), mob.getType());
                len.setHealth(mob.getHealth());
                len.setRemainingAir(mob.getRemainingAir());
                len.setFireTicks(mob.getFireTicks());
                len.setTicksLived(mob.getTicksLived());
            mob.remove();
            e.setCancelled(true);
        }
*/
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onExplosionPrime(final ExplosionPrimeEvent e) {
        Entity exploder = e.getEntity();
        if (exploder instanceof CraftFireball) {
            Fireball fireball = (Fireball)exploder;
            if (fireball.getShooter() instanceof Player) {
                Player p = (Player)fireball.getShooter();
                if (ClassManager.getClass(p).equalsIgnoreCase("wizard")) {
                    Location loc = fireball.getLocation();
                    
                    new Thread(new Explosion(loc)).start();
                    
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent e) {
        Entity eDead = e.getEntity();
        
        if (EntityManager.getAttacker(eDead) != null) {
            Player p = EntityManager.getAttacker(eDead);
            ClassManager.slayExp(p, eDead);
            EntityManager.remove(eDead);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityShootBow(final EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            SkillManager.doAttackBow((Player)e.getEntity(), e);
        }
    }
}

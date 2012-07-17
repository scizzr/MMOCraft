package com.scizzr.bukkit.plugins.mmocraft.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.classes.Barbarian;
import com.scizzr.bukkit.plugins.mmocraft.classes.Wizard;
import com.scizzr.bukkit.plugins.mmocraft.enums.Colors;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.plugins.mmocraft.threads.Explosion;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;
import com.scizzr.bukkit.plugins.mmocraft.timers.FireballTimer;
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
        if (ent instanceof LivingEntity) {
            if (HelperMgr.isHelper(b)) {
                HelperMgr.springWizardTrap(ent, b);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (e.getCause() == DamageCause.FALL) {
                Race race = RaceMgr.getRace(p);
                if (race != null) {
                    if (race instanceof Barbarian) {
                        if (p.getFallDistance() <= 4) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
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
                ArrowTimer.remove((Arrow)eAtt);
                pAtt = (Player)((Arrow)eAtt).getShooter();
            }
        } else if (eAtt instanceof Player) {
            pAtt = (Player)eAtt;
        }
        
        if (pAtt != null) {
            EntityMgr.setAttacker(eDef, pAtt);
            double diff = MoreMath.setDec(pAtt.getLocation().distance(eDef.getLocation()), 2);
            
            if (diff >= 50) {
                RaceMgr.addExp(pAtt, (int)diff, Colors.WARN + "Nice shot! +%s XP (" + diff + " blocks)");
            }
        }
        
        /*
        if (PetMgr.getMaster(mob) == p) {
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
        
        if (exploder instanceof Fireball) {
            Fireball fireball = (Fireball)exploder;
            
            if (fireball.getShooter() instanceof Player) {
                Player p = (Player) fireball.getShooter();
                
                if (FireballTimer.balls.containsKey(fireball)) {
                    FireballTimer.explodeFireball(fireball);
                    e.setCancelled(true);
                    
                    if (RaceMgr.getRace(p) instanceof Wizard) {
                        Location loc = fireball.getLocation();
                        
                        new Thread(new Explosion(loc, false)).start();
                        
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent e) {
        Entity eDead = e.getEntity();
        
        if (EntityMgr.getAttacker(eDead) != null) {
            Player p = EntityMgr.getAttacker(eDead);
            RaceMgr.slayExp(p, eDead);
            EntityMgr.remove(eDead);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityShootBow(final EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            SkillMgr.doAttackBow((Player)e.getEntity(), e);
        }
    }
}

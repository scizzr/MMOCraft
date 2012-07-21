package com.scizzr.bukkit.plugins.mmocraft.listeners;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.classes.Barbarian;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.classes.Wizard;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.HelperMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.plugins.mmocraft.threads.Explosion;
import com.scizzr.bukkit.plugins.mmocraft.timers.ArrowTimer;
import com.scizzr.bukkit.plugins.mmocraft.timers.FireballTimer;

public class Entities implements Listener {
    Main plugin;
    
    public Entities(Main instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreatureSpawn(final CreatureSpawnEvent e) {
        Entity ent = e.getEntity();
        if (e.getSpawnReason() == SpawnReason.SPAWNER) {
            EntityMgr.addSpawnerMob(ent.getUniqueId());
        }
    }
    
    public void onEntityCombust(EntityCombustEvent e) {
        Entity ent = e.getEntity();
        if (PetMgr.isPet(ent)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(final EntityDamageEvent e) {
        Entity ent = e.getEntity();
    
        if (ent instanceof LivingEntity) {
            LivingEntity lent = (LivingEntity)ent;
            
            if (e.getCause() == DamageCause.FALL) {
                if (lent instanceof Player) {
                    Player p = (Player)lent;
                    Race race = RaceMgr.getRace(p.getName());
                    if (race != null) {
                        if (race instanceof Barbarian) {
                            if (p.getFallDistance() <= 5) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
            
            if (PetMgr.isPet(ent)) {
                Pet pet = PetMgr.getPet(lent.getUniqueId());
                pet.setHealth(lent.getHealth());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
        Entity eAtt = e.getDamager();
        Entity eDef = e.getEntity();
        Player pAtt = null;
        
        if (eDef instanceof Blaze) {
            Bukkit.broadcastMessage("Blaze hurt:" + e.getCause().name());
        }
        
        if (eAtt instanceof SmallFireball) {
            LivingEntity shoot = ((SmallFireball)eAtt).getShooter();
            if (shoot instanceof Player) {
                pAtt = (Player)((SmallFireball)eAtt).getShooter();
            } else if (shoot instanceof Monster) {
                Monster mob = (Monster)shoot;
                if (PetMgr.isPet(mob) && eDef instanceof Player) {
                    Pet pet = PetMgr.getPet(mob.getUniqueId());
                    String owner = pet.getOwnerName();
                    if (owner == ((Player)eDef).getName()) {
                        e.setCancelled(true);
                    }
                }
            }
        } else if (eAtt instanceof Fireball) {
            LivingEntity shoot = ((Fireball)eAtt).getShooter();
            if (shoot instanceof Player) {
                pAtt = (Player)((Fireball)eAtt).getShooter();
            }
        } else if (eAtt instanceof Arrow) {
            LivingEntity shoot = ((Arrow)eAtt).getShooter();
            if (shoot instanceof Player) {
                ArrowTimer.remove((Arrow)eAtt);
                pAtt = (Player)((Arrow)eAtt).getShooter();
            }
        } else if (eAtt instanceof Player) {
            pAtt = (Player)eAtt;
        }
        
        if (PetMgr.isPet(eDef) && eAtt instanceof Player) {
            Pet pet = PetMgr.getPet(eDef.getUniqueId());
            
            if (pet.getOwnerName() == pAtt.getName()) {
                e.setCancelled(true); return;
            }
        } else if (PetMgr.isPet(eAtt) && eDef instanceof Player) {
            Pet pet = PetMgr.getPet(eAtt.getUniqueId());
            Player def = (Player)eDef;
            if (pet.getOwnerName() == def.getName()) {
                e.setCancelled(true); return;
            }
        }

        if (pAtt != null) {
            EntityMgr.setAttacker(eDef, pAtt.getName());
            SkillMgr.doAttackEntity(pAtt, e);
            Race race = RaceMgr.getRace(pAtt.getName());
            ConcurrentHashMap<Pet, Boolean> pets = race.getPets();
            for (Entry<Pet, Boolean> entry : pets.entrySet()) {
                Pet pet = entry.getKey();
                Monster mob = (Monster)EntityMgr.getEntityByUUID(pet.getUUID());
                mob.setTarget((LivingEntity)eDef);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(final EntityDeathEvent e) {
        Entity ent = e.getEntity();
        
        if (ent instanceof Blaze) {
            Bukkit.broadcastMessage("Blaze dead");
        }
        
        if (EntityMgr.getAttacker(ent) != null) {
            String s = EntityMgr.getAttacker(ent);
            RaceMgr.slayExp(s, ent);
            EntityMgr.remove(ent);
        }
        
        if (PetMgr.isPet(ent)) {
            Pet pet = PetMgr.getPet(ent.getUniqueId());
            
            Player owner = Bukkit.getPlayerExact(pet.getOwnerName());
            
            if (owner != null) {
                EntityDamageEvent ede = e.getEntity().getLastDamageCause();
                
                if (ede.getCause() == DamageCause.ENTITY_ATTACK) {
                    Entity eAtt = ede.getEntity();
                    if (eAtt instanceof Player) {
                        owner.sendMessage(Main.prefix + ((Player)eAtt).getName() + " killed your pet " + pet.getName());
                    } else {
                        owner.sendMessage(Main.prefix + "A " + eAtt.getType().getName() + " killed your pet " + pet.getName());
                    }
                } else if (ede.getCause() == DamageCause.BLOCK_EXPLOSION || ede.getCause() == DamageCause.ENTITY_EXPLOSION) {
                    owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " exploded");
                } else if (ede.getCause() == DamageCause.FALL) {
                    owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " fell to its death");
                } else if (ede.getCause() == DamageCause.FIRE || ede.getCause() == DamageCause.FIRE_TICK || ede.getCause() == DamageCause.LAVA) {
                    owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " burned to death");
                } else if (ede.getCause() == DamageCause.LIGHTNING) {
                    owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " was stuck by lightning");
                } else if (ede.getCause() == DamageCause.POISON) {
                    owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " was poisoned");
                } else if (ede.getCause() == DamageCause.PROJECTILE) {
                    Entity eAtt = ((EntityDamageByEntityEvent) ede).getDamager();
                    if (eAtt instanceof Projectile) {
                        Projectile proj = (Projectile)eAtt;
                        Entity shoot = proj.getShooter();
                        if (proj instanceof Fireball || proj instanceof SmallFireball) {
                            if (shoot instanceof Player) {
                                owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " was fireballed by " + ((Player)shoot).getName());
                            } else {
                                owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " was fireballed by " + shoot.getType().getName());
                            }
                        } else if (proj instanceof Arrow) {
                            if (shoot instanceof Player) {
                                owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " was shot by " + ((Player)shoot).getName());
                            } else {
                                owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " was shot by " + shoot.getType().getName());
                            }
                        } else {
                            owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " was shot by " + shoot);
                        }
                    }
                } else if (ede.getCause() == DamageCause.SUFFOCATION) {
                    owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " suffocated");
                } else {
                    owner.sendMessage(Main.prefix + "Your pet " + pet.getName() + " died");
                }
            }
            
            
            PetMgr.removePet(pet, false, false);
        }
        
        EntityMgr.removeSpawnerMob(ent.getUniqueId());
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
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityShootBow(final EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            //e.setCancelled(cancel);
            SkillMgr.doAttackBow((Player)e.getEntity(), e);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityTarget(final EntityTargetEvent e) {
        Entity eAtt = e.getEntity();
        Entity eDef = e.getTarget();
        if (eDef instanceof Player) {
            Player p = (Player)eDef;
            if (PetMgr.isPet(eAtt)) {
                Pet pet = PetMgr.getPet(eAtt.getUniqueId());
                if (pet.getOwnerName().equalsIgnoreCase(p.getName())) {
                    e.setCancelled(true);
                }
            }
        }
        if (PetMgr.isPet(eAtt) && PetMgr.isPet(eDef)) {
            Player ownAtt = Bukkit.getPlayer(PetMgr.getPet(eAtt.getUniqueId()).getOwnerName());
            Player ownDef = Bukkit.getPlayer(PetMgr.getPet(eDef.getUniqueId()).getOwnerName());
            if (ownAtt == ownDef) {
                e.setCancelled(true);
            }
        }
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
                    
                    if (RaceMgr.getRace(p.getName()) instanceof Wizard) {
                        Location loc = fireball.getLocation();
                        
                        new Thread(new Explosion(loc, false)).start();
                        
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
    public void onItemSpawn(final ItemSpawnEvent e) {
        if (HelperMgr.isHelper(e.getLocation().getBlock())) {
            Item it = e.getEntity();
            if (it.getItemStack().getType() == Material.SAPLING) {
                e.setCancelled(true);
            }
        }
    }
}

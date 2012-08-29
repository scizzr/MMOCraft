package com.scizzr.bukkit.mmocraft.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
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
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.classes.Assassin;
import com.scizzr.bukkit.mmocraft.classes.Barbarian;
import com.scizzr.bukkit.mmocraft.classes.Wizard;
import com.scizzr.bukkit.mmocraft.custommobs.CustomZombie;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.managers.AidMgr;
import com.scizzr.bukkit.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.threads.Explosion;
import com.scizzr.bukkit.mmocraft.timers.ArrowTimer;
import com.scizzr.bukkit.mmocraft.timers.FireballTimer;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class Entities implements Listener {
    MMOCraft plugin;
    public static ArrayList<UUID> safeTNT = new ArrayList<UUID>();
    
    public Entities(MMOCraft instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(final CreatureSpawnEvent e) {
        Entity ent = e.getEntity();
        Location loc = ent.getLocation();
        
        if (e.getSpawnReason() == SpawnReason.SPAWNER) {
            EntityMgr.addSpawnerMob(ent.getUniqueId());
        }
        
        World world = ent.getWorld();
        CraftWorld cWorld = (CraftWorld)world;
        net.minecraft.server.World mcWorld = cWorld.getHandle();
        
        CraftEntity cEnt = (CraftEntity)ent;
        net.minecraft.server.Entity mcEnt = cEnt.getHandle();
        
        
        if (e.getEntityType() == EntityType.ZOMBIE && !(mcEnt instanceof CustomZombie)) {
            CustomZombie cz = new CustomZombie(mcWorld, plugin);
            cz.setPosition(loc.getX(), loc.getY(), loc.getZ());
            
            mcWorld.removeEntity(mcEnt);
            mcWorld.addEntity(cz, SpawnReason.CUSTOM);
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
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
            
            if (lent instanceof Player) {
                Player p = (Player)lent;
                Race race = RaceMgr.getRace(p.getName());
                
                if (e.getCause() == DamageCause.FALL) {
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
    public static void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
        Entity eAtt = e.getDamager();
        Entity eDef = e.getEntity();
        Player pAtt = null;
        Player pDef = null;
        
        if (eAtt instanceof SmallFireball) {
            LivingEntity shoot = ((SmallFireball)eAtt).getShooter();
            if (shoot instanceof Player) {
                pAtt = (Player)((SmallFireball)eAtt).getShooter();
            } else if (shoot instanceof Monster) {
                Monster mob = (Monster)shoot;
                if (PetMgr.isPet(mob)) {
                    if (eDef instanceof Player) {
// Pet Blaze shooting its owner
                        Pet pet = PetMgr.getPet(mob.getUniqueId());
                        String owner = pet.getOwnerName();
                        if (owner.equalsIgnoreCase(((Player)eDef).getName())) {
                            e.setCancelled(true);
                        }
                    } else if (eDef instanceof Monster) {
                        if (PetMgr.isPet(eDef)) {
                            Pet petA = PetMgr.getPet(mob.getUniqueId());
                            String ownerA = petA.getOwnerName();
                            Pet petB = PetMgr.getPet(mob.getUniqueId());
                            String ownerB = petB.getOwnerName();
                            
                            if (ownerA.equalsIgnoreCase(ownerB)) {
//Pet Blaze shooting another pet; same owner for both
                                e.setCancelled(true);
                            }
                        }
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
        
        if (eDef instanceof Player) {
            pDef = (Player)eDef;
            if (eAtt instanceof CustomZombie) {
                e.setDamage((int)(e.getDamage()*1.5));
                pDef.setVelocity(new Vector(0, 1, 0));
            }
        }
        
        if (PetMgr.isPet(eDef) && eAtt instanceof Player) {
            Pet pet = PetMgr.getPet(eDef.getUniqueId());
            
            if (pet.getOwnerName().equalsIgnoreCase(pAtt.getName())) {
                e.setCancelled(true); return;
            }
        } else if (PetMgr.isPet(eAtt) && eDef instanceof Player) {
            Pet pet = PetMgr.getPet(eAtt.getUniqueId());
            Player def = (Player)eDef;
            if (pet.getOwnerName().equalsIgnoreCase(def.getName())) {
                e.setCancelled(true); return;
            }
        }
        
        if (pAtt != null) {
            EntityMgr.setAttacker(eDef, pAtt);
            SkillMgr.doAttackEntity(pAtt, e);
            Race race = RaceMgr.getRace(pAtt.getName());
            
            List<Pet> pets = race.getPets();
            
            for (Pet pet : pets) {
                Monster mob = (Monster)EntityMgr.getEntityByUUID(pet.getUUID());
                mob.setTarget((LivingEntity)eDef);
            }
        }
        
        if (pDef != null) {
            Race race = RaceMgr.getRace(pDef.getName());
            
            List<Pet> pets = race.getPets();
            
            for (Pet pet : pets) {
                Monster mob = (Monster)EntityMgr.getEntityByUUID(pet.getUUID());
                if (eAtt instanceof Projectile) {
                    mob.setTarget(((Projectile)eAtt).getShooter());
                } else {
                    mob.setTarget((LivingEntity)eAtt);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(final EntityDeathEvent e) {
        Entity ent = e.getEntity();
        
        if (EntityMgr.getAttacker(ent) != null) {
            Player p = (Player)EntityMgr.getEntityByUUID(EntityMgr.getAttacker(ent));
            if (p != null) { RaceMgr.slayExp(p.getName(), ent); }
            EntityMgr.remove(ent);
        }
        
        if (PetMgr.isPet(ent)) {
            Pet pet = PetMgr.getPet(ent.getUniqueId());
            
            Player owner = Bukkit.getPlayerExact(pet.getOwnerName());
            
            if (owner != null) {
                EntityDamageEvent ede = e.getEntity().getLastDamageCause();
                
                if (ede.getCause() == DamageCause.ENTITY_ATTACK) {
                    if (e.getEntity() instanceof EntityDamageByEntityEvent) { return; }
                    
                    EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
                    Entity eAtt = edbee.getDamager();
                    if (eAtt instanceof Player) {
                        Race race = RaceMgr.getRace(((Player)eAtt).getName());
                        owner.sendMessage(MMOCraft.prefix + I18n._("petkillbyplay", new Object[] {pet.getName(), race.getColor() + ((Player)eAtt).getName()}));
                    } else {
                        owner.sendMessage(MMOCraft.prefix + I18n._("petkillbymob", new Object[] {pet.getName(), eAtt.getType().getName()}));
                    }
                } else if (ede.getCause() == DamageCause.BLOCK_EXPLOSION || ede.getCause() == DamageCause.ENTITY_EXPLOSION) {
                    owner.sendMessage(MMOCraft.prefix + I18n._("petexploded", new Object[] {pet.getName()}));
                } else if (ede.getCause() == DamageCause.FALL) {
                    owner.sendMessage(MMOCraft.prefix + I18n._("petfell", new Object[] {pet.getName()}));
                } else if (ede.getCause() == DamageCause.FIRE || ede.getCause() == DamageCause.FIRE_TICK || ede.getCause() == DamageCause.LAVA) {
                    owner.sendMessage(MMOCraft.prefix + I18n._("petburned", new Object[] {pet.getName()}));
                } else if (ede.getCause() == DamageCause.LIGHTNING) {
                    owner.sendMessage(MMOCraft.prefix + I18n._("petstruck", new Object[] {pet.getName()}));
                } else if (ede.getCause() == DamageCause.POISON) {
                    owner.sendMessage(MMOCraft.prefix + I18n._("petpoisoned", new Object[] {pet.getName()}));
                } else if (ede.getCause() == DamageCause.PROJECTILE) {
                    Entity eAtt = ((EntityDamageByEntityEvent) ede).getDamager();
                    if (eAtt instanceof Projectile) {
                        Projectile proj = (Projectile)eAtt;
                        Entity shoot = proj.getShooter();
                        if (proj instanceof Fireball || proj instanceof SmallFireball) {
                            if (shoot instanceof Player) {
                                Race race = RaceMgr.getRace(((Player)eAtt).getName());
                                owner.sendMessage(MMOCraft.prefix + I18n._("petfirebyplay", new Object[] {pet.getName(), race.getColor() + ((Player)shoot).getName()}));
                            } else {
                                owner.sendMessage(MMOCraft.prefix + I18n._("petfirebymob", new Object[] {pet.getName(), shoot.getType().getName()}));
                            }
                        } else if (proj instanceof Arrow) {
                            if (shoot instanceof Player) {
                                Race race = RaceMgr.getRace(((Player)shoot).getName());
                                owner.sendMessage(MMOCraft.prefix + I18n._("petshotbyplay", new Object[] {pet.getName(), race.getColor() + ((Player)shoot).getName()}));
                            } else {
                                owner.sendMessage(MMOCraft.prefix + I18n._("petshotbymob", new Object[] {pet.getName(), shoot.getType().getName()}));
                            }
                        } else {
                            owner.sendMessage(MMOCraft.prefix + I18n._("petshot", new Object[] {pet.getName(), shoot.getType().getName()}));
                        }
                    }
                } else if (ede.getCause() == DamageCause.SUFFOCATION) {
                    owner.sendMessage(MMOCraft.prefix + I18n._("petsuff", new Object[] {pet.getName()}));
                } else {
                    owner.sendMessage(MMOCraft.prefix + I18n._("petdied", new Object[] {pet.getName()}));
                }
            }
            
            PetMgr.removePet(pet, true, false, false);
        }
        
        EntityMgr.removeSpawnerMob(ent.getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(EntityExplodeEvent e) {
        Entity ent = e.getEntity();
        
        if (ent instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed)ent;
            if (safeTNT.contains(tnt.getUniqueId())) {
                safeTNT.remove(tnt.getUniqueId());
                
                List<Block> blocks = e.blockList();
                synchronized(blocks) {
                    Iterator<Block> it = blocks.iterator();
                    while (it.hasNext()) {
                        Block block = it.next(); block.getData();
                        it.remove();
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityInteract(final EntityInteractEvent e) {
        Entity ent = e.getEntity();
        Location loc = e.getBlock().getLocation().clone();
        Block b = loc.getBlock();
        if (ent instanceof LivingEntity) {
            if (AidMgr.isAid(b)) {
                AidMgr.springWizardTrap(ent, b);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityShootBow(final EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            //e.setCancelled(cancel);
            SkillMgr.doAttackBow((Player)e.getEntity(), e);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityTarget(final EntityTargetEvent e) {
        Entity eAtt = e.getEntity();
        Entity eDef = e.getTarget();
        if (eDef instanceof Player) {
            Player p = (Player)eDef;
            Race race = RaceMgr.getRace(p.getName());
            if (race instanceof Assassin) {
                if (race.hasData("invis")) {
                    e.setCancelled(true);
                }
            }
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
                        
                        Explosion.make(loc, 1.5F, false);
                        
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemSpawn(final ItemSpawnEvent e) {
        if (AidMgr.isAid(e.getLocation().getBlock())) {
            Item it = e.getEntity();
            ItemStack is = it.getItemStack();
            if (is.getType() == Material.SAPLING || is.getType() == Material.TORCH || is.getType() == Material.REDSTONE_TORCH_ON) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileHit(final ProjectileHitEvent e) {
        Projectile proj = e.getEntity();
        Location loc = proj.getLocation();
        
        if (!(proj instanceof Arrow || proj instanceof Fireball)) { return; }
        
        Block b = loc.getBlock();
        if (AidMgr.isAid(b)) {
            if (proj.getShooter() instanceof Player) {
                if ((Player)proj.getShooter() != Bukkit.getPlayer(AidMgr.getAid(b).getOwnerName())) { AidMgr.removeAid(b, proj.getShooter(), true); }
            } else AidMgr.removeAid(b, null, true);
        }
        
//TODO: Config - Allow targeted block to count for aid removal
        //if () {
            if (proj.getShooter() != null) {
                Block b2 = proj.getShooter().getTargetBlock(null, 120);
                if (AidMgr.isAid(b2) && b2.getLocation().distance(proj.getShooter().getTargetBlock(null, 120).getLocation()) < 0.5) {
                    if (proj.getShooter() instanceof Player) {
                        if ((Player)proj.getShooter() != Bukkit.getPlayer(AidMgr.getAid(b2).getOwnerName())) { AidMgr.removeAid(b2, proj.getShooter(), true); }
                    } else AidMgr.removeAid(b2, null, true);
                }
            }
        //}
    }
}

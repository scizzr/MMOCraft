package com.scizzr.bukkit.plugins.mmocraft.interfaces.skills;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.scizzr.bukkit.plugins.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Race;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.plugins.mmocraft.interfaces.pets.PetPigzombie;
import com.scizzr.bukkit.plugins.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.plugins.mmocraft.managers.SkillMgr;

public class NecroPetPigzombie implements Skill {
    int cooldown =  20;
    int lvlReq   =  10;
    Material itemType = Material.IRON_INGOT;
    int itemCost = 1;
    
    public String getName() {
        return "Summon PigZombie";
    }
    
    public void execute(Player p, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        if(!(p.getItemInHand().getType() == itemType)) { return; }
        if (p.getGameMode() == GameMode.SURVIVAL) {
            if (!p.getInventory().contains(itemType, itemCost)) { return; }
            p.getInventory().removeItem(new ItemStack(itemType, itemCost));
        }
        
        Location loc = p.getTargetBlock(null, 120).getLocation().add(0, 1, 0);
        
/*XXX Green Villagers
        Villager mob = (Villager)loc.getWorld().spawn(loc, Villager.class);
        ((CraftVillager)mob).getHandle().setProfession(5);
*/
        
        PigZombie mob = (PigZombie)loc.getWorld().spawnCreature(loc, EntityType.PIG_ZOMBIE);
        Pet pet = new PetPigzombie();
        
        pet.setHealth(mob.getHealth()); pet.setUUID(mob.getUniqueId()); pet.setOwnerName(p.getName());
        
        PetMgr.addPet(p.getName(), pet);
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

package com.scizzr.bukkit.mmocraft.skills;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;
import com.scizzr.bukkit.mmocraft.pets.PetBlaze;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class NecroSumBlaze implements Skill {
    int cooldown = 100;
    int lvlReq   =  10;
    Material itemType = Material.DIAMOND;
    int itemCost = 1;
    
    public String getName() {
        return "Summon Blaze";
    }
    
    public void execute(Player p, Entity ent, float f) {
        Pet pet = new PetBlaze();
        
        if (PetMgr.numPets(p, pet) >= 1) { p.sendMessage(MMOCraft.prefix + I18n._("pethasmax", new Object[] {pet.getName()})); return; }
        
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        if(!(p.getItemInHand().getType() == itemType)) { return; }
        if (p.getGameMode() == GameMode.SURVIVAL) {
            if (!p.getInventory().contains(itemType, itemCost)) { return; }
            p.getInventory().removeItem(new ItemStack(itemType, itemCost));
        }
        
        Location loc = p.getTargetBlock(null, 120).getLocation().add(0, 1, 0);
        Blaze mob = (Blaze)loc.getWorld().spawnEntity(loc, EntityType.BLAZE);
        
        pet.setHealth(mob.getHealth()); pet.setUUID(mob.getUniqueId()); pet.setOwnerName(p.getName());
        
        SoundEffects.MOB_BLAZE_BREATHE.playGlobal(p.getLocation());
        
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

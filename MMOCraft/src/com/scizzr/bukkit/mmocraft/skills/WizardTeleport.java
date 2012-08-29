package com.scizzr.bukkit.mmocraft.skills;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.hooks.HookWG;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;

public class WizardTeleport implements Skill {
    int cooldown =  30;
    int lvlReq   =   0;
    
    Random rand = new Random();
    
    private static ArrayList<Integer> validHeadBlocks = new ArrayList<Integer>();
    
    public String getName() {
        return "Teleport";
    }
    
    public void execute(Player p, Entity ent, float f) {
        Block block = p.getTargetBlock(null, 20);
        
        Location from = p.getLocation();
        Location to = block.getLocation(); to.setYaw(from.getYaw()); to.setPitch(from.getPitch()); to.add(0.5, 0.0, 0.5);
        
//TODO : I18n
        if (from.distance(to) > 20 || block.getType() == Material.AIR) { p.sendMessage("That would be impossible."); return; }
        String blocks = "0,6,8,9,10,11,18,20,26,27,28,30,31,32,37,38,39,40,44,50,51,52,53,54,55,59,63,64,65,66,67,68," +
        		        "69,70,71,72,75,76,79,83,85,89,90,92,93,94,96,101,102,104,105,106,107,108,109,111,113,114,115";
        String[] split = blocks.split(",");
        
        for (int i = 0; i < split.length; i++) {
            validHeadBlocks.add(Integer.valueOf(split[i]));
        }
        
//TODO : I18n
        if (to.clone().add(0, 1, 0).getBlock().getType() != Material.AIR) { p.sendMessage("I would suffocate!"); return; }
//TODO : I18n      
        if (!HookWG.canEnter(p, block)) { p.sendMessage(MMOCraft.prefix + "You cannot teleport there."); }
        
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        p.teleport(to.add(0, 1, 0));
        SoundEffects.MOB_ENDERMEN_PORTAL.play(p, p.getLocation(), 1.0f, 0.6f);
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

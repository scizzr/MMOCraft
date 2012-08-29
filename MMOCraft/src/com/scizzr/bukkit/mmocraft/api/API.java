package com.scizzr.bukkit.mmocraft.api;

import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.managers.RaceMgr;

public class API {
    public static int getExperience(Player p) {
        return RaceMgr.getRace(p.getName()).getExp();
    }
    
    public static int getLevel(Player p) {
        return RaceMgr.getLevel(RaceMgr.getRace(p.getName()).getExp());
    }
    
    
}

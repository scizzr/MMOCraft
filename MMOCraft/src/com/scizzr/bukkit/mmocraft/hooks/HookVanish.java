package com.scizzr.bukkit.mmocraft.hooks;

import org.bukkit.entity.Player;
import org.kitteh.vanish.staticaccess.VanishNoPacket;

import com.scizzr.bukkit.mmocraft.MMOCraft;

public class HookVanish {
    public static boolean canSee(Player look, Player hide) {
        if (MMOCraft.pm.getPlugin("VanishNoPacket") != null) {
            try {
                return VanishNoPacket.canSee(hide, look);
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }
}

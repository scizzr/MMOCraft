package com.scizzr.bukkit.mmocraft.hooks;

import org.bukkit.entity.Player;
import org.kitteh.vanish.staticaccess.VanishNoPacket;

import com.scizzr.bukkit.mmocraft.Main;

public class Vanish {
    public static boolean canSee(Player look, Player hide) {
        if (Main.pm.getPlugin("VanishNoPacket") != null) {
            try {
                return VanishNoPacket.canSee(hide, look);
            } catch (Exception ex) {
                Main.suicide(ex);
                return false;
            }
        }
/*
        if (Main.pm.getPlugin("Another") != null) {
            return AnotherVanishPlugin.canSee(hide, look);
        }
*/
        return true;
    }
}

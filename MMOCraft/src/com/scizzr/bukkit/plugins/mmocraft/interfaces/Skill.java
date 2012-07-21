package com.scizzr.bukkit.plugins.mmocraft.interfaces;

import org.bukkit.entity.Player;

public interface Skill {
    public abstract String getName();
    public abstract void execute(Player p, float f);
    public abstract boolean isCooldown(Player p);
    public abstract boolean isLevel(Player p);
}

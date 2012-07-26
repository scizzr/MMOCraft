package com.scizzr.bukkit.mmocraft.interfaces;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface Skill {
    public abstract String getName();
    public abstract void execute(Player p, Entity ent, float f);
    public abstract boolean isCooldown(Player p);
    public abstract boolean isLevel(Player p);
}

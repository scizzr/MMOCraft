package com.scizzr.bukkit.mmocraft.custommobs;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.World;

import com.scizzr.bukkit.mmocraft.MMOCraft;

@SuppressWarnings("unused")
public class Navigator extends Navigation {
    private MMOCraft plugin;
    EntityLiving entity;
    
    public Navigator(MMOCraft plugin, EntityLiving entity, World world, float f) {
        super(entity, world, f);
        
        this.plugin = plugin;
        this.entity = entity;
    }
    
    public boolean a(double d0, double d1, double d2, float f) {
        PathEntity pe = a(Math.floor(d0), (int)d1, Math.floor(d2));
        
        return a(pe, f);
    }
    
    public boolean a(EntityLiving el, float f) {
        PathEntity pe = a(el);
        
        return (pe != null) ? a(pe, f) : false;
    }
    
    public boolean a(PathEntity path, float speed) {
        //XXX
        //speed *= (float)this.plugin.config.getDouble(Config.movementspeedmult)
        speed *= 1.75;
        
        return super.a(path, speed);
    }
}

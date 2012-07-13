package com.scizzr.bukkit.plugins.mmocraft.threads;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.ChunkPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet60Explosion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.TNTPrimed;

public class Explosion implements Runnable {
    Location loc; Boolean real;
    
    public Explosion(Location loc, Boolean real) {
        this.loc = loc;
        this.real = real;
    }
    
    public void run() {
        spawnExplosion(loc, real);
    }
    
    public static void spawnExplosion(Location loc, Boolean real) {
        if (real) {
            realExplosion(loc);
        } else {
            fakeExplosion(loc);
        }
    }
    
    public static void realExplosion(Location loc) {
        Block b = loc.getBlock();
        TNTPrimed tnt = (TNTPrimed)b.getWorld().spawn(b.getLocation().add(0D, 1D, 0D), TNTPrimed.class);
        tnt.setFuseTicks(0); tnt.setYield(2F);
    }
    
    public static void fakeExplosion(Location loc) {
        fakeExplosion(loc, 4);
    }
    
    public static void fakeExplosion(Location loc, int radius) {
        if (loc == null) return;
        World world = loc.getWorld();
        Set<Block> blocks = new HashSet<Block>();
        int r2 = radius * radius;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if(x*x + y*y + z*z + x+y+z + 0.75 > r2) continue;
                    Block toadd = world.getBlockAt((int)(loc.getX()+x+0.5), (int)(loc.getY()+x+0.5), (int)(loc.getZ()+x+0.5));
                    if (toadd == null) continue;
                    if (toadd.getTypeId() != 0) continue;
                    blocks.add(toadd);
                }
            }
        }
        fakeExplosion(loc, blocks);
    }
    
    protected static void fakeExplosion(Location location, Set<Block> blocks) {
        if (blocks == null) return;
        if (blocks.size() == 0) return;
        
        HashSet<ChunkPosition> chunkPositions = new HashSet<ChunkPosition>(blocks.size());
        
        for (Block block : blocks) {
            chunkPositions.add(new ChunkPosition(block.getX(), block.getY(), block.getZ()));
        }
        
        Packet60Explosion packet = new Packet60Explosion(location.getX(),location.getY(), location.getZ(), 0.1f, chunkPositions);
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        MinecraftServer minecraftServer = craftServer.getServer();
        
        minecraftServer.serverConfigurationManager.sendPacketNearby(location.getX(), location.getY(), location.getZ(), 64, ((CraftWorld)location.getWorld()).getHandle().dimension, packet);
    }
}

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

public class Explosion implements Runnable {
    Location loc;
    
    public Explosion(Location loc) {
        this.loc = loc;
    }
    
    public void run() {
        fakeExplosion(loc);
    }
    
    public static void fakeExplosion(Location location) {
        fakeExplosion(location, 4);
    }
    
    public static void fakeExplosion(Location location, int radius) {
        if (location == null) return;
        World world = location.getWorld();
        Set<Block> blocks = new HashSet<Block>();
        int r2 = radius * radius;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if(x*x + y*y + z*z + x+y+z + 0.75 > r2) continue;
                    Block toadd = world.getBlockAt((int)(location.getX()+x+0.5), (int)(location.getY()+x+0.5), (int)(location.getZ()+x+0.5));
                    if (toadd == null) continue;
                    if (toadd.getTypeId() != 0) continue;
                    blocks.add(toadd);
                }
            }
        }
        fakeExplosion(location, blocks);
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

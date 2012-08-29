package com.scizzr.bukkit.mmocraft.threads;

import java.util.ArrayList;
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

import com.scizzr.bukkit.mmocraft.listeners.Entities;

public class Explosion {
    public static void make(Location loc, float size, Boolean real) {
        spawnExplosion(loc.clone(), size, real, false);
    }
    
    public static void make(Location loc, float size, Boolean real, Boolean packet) {
        spawnExplosion(loc.clone(), size, real, packet);
    }
    
    private static void spawnExplosion(Location loc, float size, Boolean real, Boolean packet) {
        if (real) {
            realExplosion(loc, size);
        } else {
            if (packet) {
                fakeExplosionPacket(loc, (int)(Math.floor((double)size)));
            } else {
                fakeExplosion(loc, size);
            }
        }
    }
    
    private static void fakeExplosion(Location loc, float size) {
        Block b = loc.getBlock();
        TNTPrimed tnt = (TNTPrimed)b.getWorld().spawn(b.getLocation().add(0D, 1D, 0D), TNTPrimed.class);
        tnt.setFuseTicks(0); tnt.setYield(size);
        Entities.safeTNT.add(tnt.getUniqueId());
    }
    
    private static void realExplosion(Location loc, float size) {
        Block b = loc.getBlock();
        TNTPrimed tnt = (TNTPrimed)b.getWorld().spawn(b.getLocation().add(0D, 1D, 0D), TNTPrimed.class);
        tnt.setFuseTicks(0); tnt.setYield(size);
    }
    
    private static void fakeExplosionPacket(Location loc, int radius) {
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
        fakeExplosionPacket(loc, blocks);
    }
    
    private static void fakeExplosionPacket(Location location, Set<Block> blocks) {
        if (blocks == null) return;
        if (blocks.size() == 0) return;
        
        ArrayList<ChunkPosition> chunkPositions = new ArrayList<ChunkPosition>(blocks.size());
        
        for (Block block : blocks) {
            chunkPositions.add(new ChunkPosition(block.getX(), block.getY(), block.getZ()));
        }
        
        Packet60Explosion packet = new Packet60Explosion(location.getX(),location.getY(), location.getZ(), 0.1f, chunkPositions, null);
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        MinecraftServer mcSrv = craftServer.getServer();
        
        mcSrv.getServerConfigurationManager().sendPacketNearby(location.getX(), location.getY(), location.getZ(), 64, ((CraftWorld)location.getWorld()).getHandle().dimension, packet);
    }
}

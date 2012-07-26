package com.scizzr.bukkit.mmocraft.skills;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.server.DataWatcher;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet29DestroyEntity;
import net.minecraft.server.Packet38EntityStatus;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.interfaces.Skill;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.managers.SkillMgr;

public class AssassinShadowStep implements Skill {
    int cooldown = 100;
    int lvlReq   =  20;
    
    
    
    
    
    static HashMap<String, Integer> ids = new HashMap<String, Integer>();
    static DataWatcher metadata;
    //static int id = -185554454;
    static int id = 8544;
    
    
    
    
    
    Random rand = new Random();
    
    public String getName() {
        return "Shadow Step";
    }
    
    public void execute(final Player p, Entity ent, float f) {
        if (isCooldown(p)) { return; } else { SkillMgr.addCooldown(p, getName(), cooldown); }
        if (!isLevel(p)) { return; }
        
        //p.sendMessage(Main.prefix + "Whoosh!");
        
        Location to = ent.getLocation().clone(); to.setPitch(p.getLocation().getPitch());
        Location vel = to.clone(); vel.setYaw(to.getYaw()+180);
        p.teleport(to); p.setVelocity(vel.multiply(1).getDirection());
        
/* Play some sort of a sound using packets
        int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            @Override
            public void run() {
                int tempID = getNewId();
                ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(getMobSpawnPacket(p.getLocation(), tempID));
                ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(getMobStatus(tempID));
                ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(getEntityDestroyPacket(tempID));
            }
        }, 0L, 50L);
        
        ids.put(p.getName(), id);
*/
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static Packet38EntityStatus getMobStatus(int mobID)
    {
    
        Packet38EntityStatus packet = new Packet38EntityStatus();
        packet.a = mobID;
       
        try
        {
        
            Field metadataField = packet.getClass().getDeclaredField("b");
            metadataField.setAccessible(true);
            metadataField.set(packet, (byte)2);
            
        } catch (Exception e) {

              e.printStackTrace();

        }
        
        return packet;
        
    }
    
    public static Packet24MobSpawn getMobSpawnPacket(Location loc, int mobID)
    {

        int x = MathHelper.floor(loc.getX() * 32.0D);
        int y = MathHelper.floor(loc.getY() * 32.0D);
        int z = MathHelper.floor(loc.getZ() * 32.0D);
        
        metadata = new DataWatcher();
        metadata.a(0, Byte.valueOf((byte) 0));
        metadata.a(12, Integer.valueOf(0));
        metadata.a(16, Byte.valueOf((byte) 0));
       
        Packet24MobSpawn packet = new Packet24MobSpawn();
        packet.a = mobID;
        //packet.b = (byte)56;
        packet.b = (byte)120;
        packet.c = x;
        packet.d = y;
        packet.e = z;
        packet.f = degreeToByte(loc.getYaw());
        packet.g = degreeToByte(loc.getPitch());
        packet.h = packet.f;
       
        try
        {
        
            Field metadataField = packet.getClass().getDeclaredField("i");
            metadataField.setAccessible(true);
            metadataField.set(packet, metadata);

         } catch (Exception e) {

            e.printStackTrace();

         }
     
        return packet;
    
    }
    
    public static Packet29DestroyEntity getEntityDestroyPacket(int mobID)
    {
     
        id --;
        
        return new Packet29DestroyEntity(mobID);
    
    }
    
    public static byte degreeToByte(float degree)
    {
     
        return (byte)(int)((int)degree * 256.0F / 360.0F);
    
    }
    
    public static int getNewId()
    {
        
        return id ++;
        
    }
    
    
}

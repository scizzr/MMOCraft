package com.scizzr.bukkit.mmocraft.effects;

import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet40EntityMetadata;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;

public class PotionSwirl {
    public static void playPotionEffect(final Player player, final LivingEntity entity, int color, int duration) {
        final DataWatcher dw = new DataWatcher();
        dw.a(8, Integer.valueOf(0));
        dw.watch(8, Integer.valueOf(color));
         
        Packet40EntityMetadata packet = new Packet40EntityMetadata(entity.getEntityId(), dw);
        ((CraftPlayer)player).getHandle().netServerHandler.sendPacket(packet);
         
        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
            public void run() {
                DataWatcher dwReal = ((CraftLivingEntity)entity).getHandle().getDataWatcher();
                dw.watch(8, dwReal.getInt(8));
                Packet40EntityMetadata packet = new Packet40EntityMetadata(entity.getEntityId(), dw);
                ((CraftPlayer)player).getHandle().netServerHandler.sendPacket(packet);
            }
        }, duration);
    }
}

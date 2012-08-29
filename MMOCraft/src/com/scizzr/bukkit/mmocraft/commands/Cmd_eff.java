//XXX: Testing
package com.scizzr.bukkit.mmocraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.effects2.SoundEffects;
import com.scizzr.bukkit.mmocraft.interfaces.Cmd;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class Cmd_eff implements Cmd {
    int tId = 0;
    
    public String getName() {
        return "eff";
    }
    
    public void execute(final Player p, Command cmd, String cmdLbl, String[] args) {
        if (args.length < 1) { return; }
/*
        if (args[0].equals("hide")) {
            Bukkit.getPlayer("Tester").hidePlayer(p);
        } else if (args[0].equals("show")) {
            Bukkit.getPlayer("Tester").showPlayer(p);
        } else if (args[0].equalsIgnoreCase("dragon")) {
            World world = ((CraftWorld)p.getWorld()).getHandle();
            CustomEntityEnderDragon ed = new CustomEntityEnderDragon(world);
            ed.setLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
            ed.world.addEntity(ed);
            return;
        } else 
*/
        if (args[0].equalsIgnoreCase("sound")) {
            StringBuilder argz = new StringBuilder();
            
            if (Util.isDouble(args[args.length-1])) {
                if (Util.isDouble(args[args.length-2])) {
                    for (int i = 1; i < args.length-2; i++) {
                        argz.append(args[i] + " ");
                    }
                    
                    argz.setLength(argz.length()-1);
                    
                    SoundEffects.getByName(argz.toString()).playGlobal(p.getLocation(), Float.parseFloat(args[args.length-1]), Float.parseFloat(args[args.length-2]));
                } else {
                    for (int i = 1; i < args.length-1; i++) {
                        argz.append(args[i] + " ");
                    }
                    
                    argz.setLength(argz.length()-1);
                    
                    SoundEffects.getByName(argz.toString()).playGlobal(p.getLocation(), 1.0f, Float.parseFloat(args[args.length-1]));
                }
            } else {
                for (int i = 1; i < args.length; i++) {
                    argz.append(args[i] + " ");
                }
                
                argz.setLength(argz.length()-1);
                
                SoundEffects.getByName(argz.toString()).playGlobal(p.getLocation());
            }
        } else if (args[0].equalsIgnoreCase("soundloop")) {
            if (args.length > 1) {
                final StringBuilder argz = new StringBuilder();
                final Player pF = p;
                
                for (int i = 2; i < args.length; i++) {
                    argz.append(args[i] + " ");
                }
                
                argz.setLength(argz.length()-1);
                
                if (args[1].equalsIgnoreCase("up")) {
                    for (int i = 5; i <= 20; i++) {
                        final float count = (float)i/10;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                            public void run() {
                                SoundEffects.getByName(argz.toString()).playGlobal(pF.getLocation(), 1.0f, count);
                            }
                        }, (long)i);
                    }
                } else if (args[1].equalsIgnoreCase("down")) {
                    for (int i = 5; i <= 20; i++) {
                        final float count = (float)i/10;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                            public void run() {
                                SoundEffects.getByName(argz.toString()).playGlobal(pF.getLocation(), 1.0f, 2.5f-count);
                            }
                        }, (long)i);
                    }
                } else if (args[1].equalsIgnoreCase("both")) {
                    for (int i = 5; i <= 20; i++) {
                        final float count = (float)i/10;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                            public void run() {
                                SoundEffects.getByName(argz.toString()).playGlobal(pF.getLocation(), 1.0f, count);
                            }
                        }, (long)i);
                    }
                    for (int i = 5; i <= 20; i++) {
                        final float count = (float)i/10;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCraft.plugin, new Runnable() {
                            public void run() {
                                SoundEffects.getByName(argz.toString()).playGlobal(pF.getLocation(), 1.0f, 2.5f-count);
                            }
                        }, (long)i+(15));
                    }
                }
            }
        }
    }
    
    public void execute(CommandSender s, Command cmd, String cmdLbl, String[] args) {
        s.sendMessage(MMOCraft.prefix + I18n._("playersonly", new Object[] {}));
    }
}

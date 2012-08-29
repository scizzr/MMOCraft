package com.scizzr.bukkit.mmocraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.config.ConfigMain;
import com.scizzr.bukkit.mmocraft.config.Data;
import com.scizzr.bukkit.mmocraft.interfaces.Cmd;
import com.scizzr.bukkit.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class Cmd_mmo implements Cmd {
    public String getName() {
        return "mmo";
    }
    
    public void execute(Player p, Command cmd, String cmdLbl, String[] args) {
        if (args.length == 1) {
            if (args[0].startsWith("help")) {
                p.sendMessage(MMOCraft.prefix + head);
                p.sendMessage(MMOCraft.prefix + "");
                p.sendMessage(MMOCraft.prefix + foot);
                return;
            } else if (args[0].startsWith("ver")) {
                p.sendMessage(MMOCraft.prefix + I18n._("", new Object[] {MMOCraft.info.getVersion(), MMOCraft.osN}));
                return;
            } else if (args[0].startsWith("stat")) {
                int xp = RaceMgr.getExp(p.getName());
                p.sendMessage(MMOCraft.prefix + I18n._("statsA", new Object[] {RaceMgr.getRaceNameColored(p.getName())}));
                p.sendMessage(MMOCraft.prefix + I18n._("statsB", new Object[] {ChatColor.GRAY + "" + RaceMgr.getLevel(xp) + ChatColor.RESET, I18n._("exp", new Object[] {}), ChatColor.GRAY + "" + xp + ChatColor.RESET}));
                p.sendMessage(MMOCraft.prefix + I18n._("statsC", new Object[] {ChatColor.GRAY + "" + RaceMgr.getNextLevel(xp) + ChatColor.RESET, ChatColor.GRAY + "" + RaceMgr.getNextExp(xp) + ChatColor.RESET, I18n._("exp", new Object[] {})}));
                return;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setexp")) {
                if (Util.isInt(args[1])) {
                    RaceMgr.setExp(p.getName(), Integer.parseInt(args[1]));
                    p.chat("/mmo stats");
                } else { p.sendMessage(MMOCraft.prefix + I18n._("expinvalid", new Object[] {I18n._("exp", new Object[] {})})); }
                return;
            } else if (args[0].equalsIgnoreCase("reload")) {
//TODO : Permission
                if (args[1].equalsIgnoreCase("config")) {
                    ConfigMain.main();
//TODO : Localization
                    p.sendMessage(MMOCraft.prefix + "Config reloaded.");
                    return;
                } else if (args[1].equalsIgnoreCase("data")) {
//TODO : Permission
                    Data.load();
//TODO : Localization
                    p.sendMessage(MMOCraft.prefix + "Data reloaded.");
                    return;
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setexp")) {
                OfflinePlayer ofp = EntityMgr.getOfflinePlayer(args[1]);
                if (ofp != null) {
                    if (Util.isInt(args[2])) {
                        RaceMgr.setExp(ofp.getName(), Integer.parseInt(args[2]));
                        if (ofp.isOnline()) { EntityMgr.getOnlinePlayer(ofp.getName()).chat("/mmo stats"); }
                        return;
                    } else { p.sendMessage(MMOCraft.prefix + I18n._("expinvalid", new Object[] {})); }
                    return;
                } else { p.sendMessage(MMOCraft.prefix + I18n._("playernotexist", new Object[] {})); }
                return;
            }
        }
        p.chat("/mmo help");
    }
    
    public void execute(CommandSender s, Command cmd, String cmdLbl, String[] args) {
        s.sendMessage(MMOCraft.prefix + I18n._("playersonly", new Object[] {}));
    }
}

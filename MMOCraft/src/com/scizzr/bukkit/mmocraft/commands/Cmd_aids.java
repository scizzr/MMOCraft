package com.scizzr.bukkit.mmocraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.interfaces.Cmd;
import com.scizzr.bukkit.mmocraft.managers.AidMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class Cmd_aids implements Cmd {
    public String getName() {
        return "aids";
    }
    
    public void execute(Player p, Command cmd, String cmdLbl, String[] args) {
        AidMgr.listAids(p);
    }
    
    public void execute(CommandSender s, Command cmd, String cmdLbl, String[] args) {
        s.sendMessage(MMOCraft.prefix + I18n._("playersonly", new Object[] {}));
    }
}

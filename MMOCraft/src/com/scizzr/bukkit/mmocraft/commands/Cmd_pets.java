package com.scizzr.bukkit.mmocraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.Main;
import com.scizzr.bukkit.mmocraft.interfaces.Cmd;
import com.scizzr.bukkit.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class Cmd_pets implements Cmd {
    public String getName() {
        return "pets";
    }
    
    public void execute(Player p, Command cmd, String cmdLbl, String[] args) {
        PetMgr.listPets(p);
    }
    
    public void execute(CommandSender s, Command cmd, String cmdLbl, String[] args) {
        s.sendMessage(Main.prefix + I18n._("playersonly", new Object[] {}));
    }
}

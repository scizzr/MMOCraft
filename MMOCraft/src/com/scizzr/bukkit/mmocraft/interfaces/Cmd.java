package com.scizzr.bukkit.mmocraft.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.Main;

public abstract interface Cmd {
    String lineSml = "--------------";
    String head = lineSml + " " + Main.info.getName() + " " + lineSml;
    String foot = lineSml + lineSml + lineSml;
    
    public abstract String getName();
    public abstract void execute(Player p, Command cmd, String cmdLabel, String[] args);
    public abstract void execute(CommandSender s, Command cmd, String cmdLabel, String[] args);
}

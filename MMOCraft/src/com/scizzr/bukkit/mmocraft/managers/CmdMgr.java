package com.scizzr.bukkit.mmocraft.managers;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.commands.Cmd_aids;
import com.scizzr.bukkit.mmocraft.commands.Cmd_class;
import com.scizzr.bukkit.mmocraft.commands.Cmd_eff;
import com.scizzr.bukkit.mmocraft.commands.Cmd_mmo;
import com.scizzr.bukkit.mmocraft.commands.Cmd_pets;
import com.scizzr.bukkit.mmocraft.interfaces.Cmd;

public class CmdMgr {
    private static ConcurrentHashMap<String, Cmd> cmds = new ConcurrentHashMap<String, Cmd>();
    
    public static void main() {
        cmds.put("aids", new Cmd_aids());
        cmds.put("class", new Cmd_class());
        cmds.put("mmo", new Cmd_mmo());
        cmds.put("pets", new Cmd_pets());
        //XXX: Testing
        cmds.put("eff", new Cmd_eff());
    }
    
    public static void execute(CommandSender s, Command cmd, String lbl, String[] args) {
        if (s instanceof Player) {
            cmds.get(lbl).execute((Player)s, cmd, lbl, args);
        } else {
            cmds.get(lbl).execute(s, cmd, lbl, args);
        }
    }
}

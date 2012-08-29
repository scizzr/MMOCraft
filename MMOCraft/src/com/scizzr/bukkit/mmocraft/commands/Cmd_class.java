package com.scizzr.bukkit.mmocraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.hooks.HookVault;
import com.scizzr.bukkit.mmocraft.interfaces.Cmd;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.managers.EntityMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class Cmd_class implements Cmd {
    public String getName() {
        return "class";
    }
    
    public void execute(Player p, Command cmd, String cmdLbl, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage(MMOCraft.prefix + head);
                p.sendMessage(MMOCraft.prefix + I18n._("classlist", new Object[] {}) + " §carcher§r, §8assassin§r, §6barbarian§r, §2druid§r, §3necromancer§r, and §5wizard");
                p.sendMessage(MMOCraft.prefix + ChatColor.YELLOW + "/class info <class>" + ChatColor.RESET + " : " + I18n._("classlist", new Object[] {}));
                p.sendMessage(MMOCraft.prefix + ChatColor.YELLOW + "/class set [who] <class>" + ChatColor.RESET + " : " + I18n._("classset", new Object[] {}));
                p.sendMessage(MMOCraft.prefix + foot);
                return;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
//TODO: Config - Disable class re-setting after a class has already been set
//TODO: Permission - Override - Allow re-setting own class
                if (!
                    (RaceMgr.
                        getRace(
                            p.getName())
                                .getName()
                                    .equalsIgnoreCase(
                                            "None") || 
                                            HookVault
                                                .hasPermission(
                                                    p, 
                                                    ""))) {
                    
                    
                    p.sendMessage(MMOCraft.prefix + I18n._("classalready", new Object[] {}) + RaceMgr.getRaceNameColoredProper(p.getName()));
                } else { 
                    String classname = "";
                    if      (args[1].startsWith("arc")) { classname = "archer"; }
                    else if (args[1].startsWith("ass")) { classname = "assassin"; }
                    else if (args[1].startsWith("bar")) { classname = "barbarian"; }
                    else if (args[1].startsWith("dru")) { classname = "druid"; }
                    else if (args[1].startsWith("nec")) { classname = "necromancer"; }
                    else if (args[1].startsWith("wiz")) { classname = "wizard"; }
                    else { p.sendMessage(MMOCraft.prefix + I18n._("classinvalid", new Object[] {})); return; }
                    RaceMgr.setRace(p.getName(), classname);
                    p.sendMessage(MMOCraft.prefix + I18n._("classnow", new Object[] {RaceMgr.getRaceNameColoredProper(p.getName())}));
                }
                return;
            } else if (args[0].equalsIgnoreCase("info")) {
                if      (args[1].startsWith("arc")) { p.sendMessage(MMOCraft.prefix + "§cArchers§r " +      I18n._("infoArc", new Object[] {})); }
                else if (args[1].startsWith("ass")) { p.sendMessage(MMOCraft.prefix + "§8Assassins§r " +    I18n._("infoAss", new Object[] {})); }
                else if (args[1].startsWith("bar")) { p.sendMessage(MMOCraft.prefix + "§6Barbarians§r " +   I18n._("infoBar", new Object[] {})); }
                else if (args[1].startsWith("dru")) { p.sendMessage(MMOCraft.prefix + "§2Druids§r " +       I18n._("infoDru", new Object[] {})); }
                else if (args[1].startsWith("nec")) { p.sendMessage(MMOCraft.prefix + "§3Necromancers§r " + I18n._("infoNec", new Object[] {})); }
                else if (args[1].startsWith("wiz")) { p.sendMessage(MMOCraft.prefix + "§5Wizards§r " +      I18n._("infoWiz", new Object[] {})); }
                else p.sendMessage(MMOCraft.prefix + I18n._("classinvalid", new Object[] {}));
                return;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
//TODO: Permission - Setting another player's class
                OfflinePlayer ofp = EntityMgr.getOfflinePlayer(args[1]);
                if (ofp != null) {
                    String classname = "";
                    if      (args[2].startsWith("arc")) { classname = "archer"; }
                    else if (args[2].startsWith("ass")) { classname = "assassin"; }
                    else if (args[2].startsWith("bar")) { classname = "barbarian"; }
                    else if (args[2].startsWith("dru")) { classname = "druid"; }
                    else if (args[2].startsWith("nec")) { classname = "necromancer"; }
                    else if (args[2].startsWith("wiz")) { classname = "wizard"; }
                    else { p.sendMessage(MMOCraft.prefix + I18n._("classinvalid", new Object[] {})); return; }
                    
                    RaceMgr.setRace(ofp.getName(), classname);
                    Race race = RaceMgr.getRace(ofp.getName());
                    p.sendMessage(MMOCraft.prefix + I18n._("classnowother", new Object[] {race.getColor() + ofp.getName() + ChatColor.RESET, RaceMgr.getRaceNameColored(ofp.getName())}));
                    if (ofp.isOnline()) {
                        Race raceOrig = RaceMgr.getRace(p.getName());
                        Bukkit.getPlayer(args[1]).sendMessage(MMOCraft.prefix + I18n._("classnowmine", new Object[] {raceOrig.getColor() + p.getName() + ChatColor.RESET, race.getColor() + race.getName()}));
                    }
                    return;
                } else p.sendMessage(MMOCraft.prefix + I18n._("playernotexist", new Object[] {})); return;
            }
        }
        p.chat("/class help");
    }
    
    public void execute(CommandSender s, Command cmd, String cmdLbl, String[] args) {
        s.sendMessage(MMOCraft.prefix + I18n._("playersonly", new Object[] {}));
    }
}

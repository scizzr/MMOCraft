package com.scizzr.bukkit.plugins.mmocraft;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.managers.HelperManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.ClassManager;
import com.scizzr.bukkit.plugins.mmocraft.managers.EntityManager;
import com.scizzr.bukkit.plugins.mmocraft.util.MoreMath;
import com.scizzr.bukkit.plugins.mmocraft.util.Vault;

public class PlayerCommand {
    public static boolean onCommand(Player p, Command command, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("class")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    p.sendMessage(Main.prefix + "------------ " + Main.info.getName() + " Class List" + " ------------");
                    p.sendMessage(Main.prefix + "Classes are: §carcher§r, §8assassin§r, §6barbarian§r, §2druid§r, §3necromancer§r, and §5wizard");
                    p.sendMessage(Main.prefix + "Type " + ChatColor.YELLOW + "/class info [class]" + ChatColor.RESET + " to learn about a class.");
                    return true;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    ClassManager.resetClass(p);
                    p.sendMessage(Main.prefix + "Your class was reset");
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (Vault.hasPermission(p, "class.set")) {
                        if (args[1].equalsIgnoreCase("archer") || args[1].equalsIgnoreCase("assassin") || args[1].equalsIgnoreCase("barbarian") || args[1].equalsIgnoreCase("druid") || args[1].equalsIgnoreCase("necromancer") || args[1].equalsIgnoreCase("wizard")) {
                            if (ClassManager.getClass(p) == null) {
                                ClassManager.setClass(p, args[1].toLowerCase());
                                p.sendMessage(Main.prefix + "Your class has been set to " + ClassManager.getClassColored(p));
                            } else p.sendMessage(Main.prefix + "You are already " + ClassManager.getClassProper(p) + " " + ClassManager.getClassColored(p));
                        } else p.sendMessage(Main.prefix + "Invalid class.");
                    } else p.sendMessage(Main.prefix + "You don't have permission to do that.");
                    return true;
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (args[1].equalsIgnoreCase("archer")) { p.sendMessage(Main.prefix + "§cArchers§r are highly skilled marksmen able to thin the ranks of enemies in battle by firing volleys of arrows with deadly accuracy."); }
                    else if (args[1].equalsIgnoreCase("archer")) { p.sendMessage(Main.prefix + "§8Assassins§r are masters of confusion, evasion, and quick attacks. The assassin uses devastating attacks and poison to kill their enemy."); }
                    else if (args[1].equalsIgnoreCase("archer")) { p.sendMessage(Main.prefix + "§6Barbarians§r are reckless warriors. Their brute strength and powerful rage makes them well-suited for adventure and combat."); }
                    else if (args[1].equalsIgnoreCase("archer")) { p.sendMessage(Main.prefix + "§2Druids§r are traditional protectors of the forest. They worship and revere all forms of nature and are usually solitary beings."); }
                    else if (args[1].equalsIgnoreCase("archer")) { p.sendMessage(Main.prefix + "§3Necromancers§r summon and control the undead, wielding powerful dark magic to cripple their enemies. They are masters of life and death."); }
                    else if (args[1].equalsIgnoreCase("archer")) { p.sendMessage(Main.prefix + "§5Wizards§r are masters of magic who utilize the elements of fire, ice, light, and dark to combat and crush their eneies."); }
                    else p.sendMessage(Main.prefix + "Invalid class.");
                    return true;
                }
            }
            p.chat("/class help");
        } else if (commandLabel.equalsIgnoreCase("mmo")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("helpers")) {
                    p.sendMessage(HelperManager.helpers.toString());
                } else if (args[0].equalsIgnoreCase("stats")) {
                    int xp = ClassManager.getExp(p);
                    p.sendMessage(Main.prefix + "Class: " + ClassManager.getClassColored(p));
                    p.sendMessage(Main.prefix + "Your level is [" + ClassManager.getLevel(xp) + "] and your XP is [" + xp + "] so far");
                    p.sendMessage(Main.prefix + "Next level is [" + ClassManager.getNextLevel(xp) + "] which requires [" + ClassManager.getNextExp(xp) + "] more XP");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("setexp")) {
                    if (Vault.hasPermission(p, "exp.set.self")) {
                        if (MoreMath.isNum(args[1])) {
                            ClassManager.setExp(p.getName(), Integer.valueOf(args[1]));
                        } else p.sendMessage(Main.prefix + "Invalid EXP amount.");
                    } else p.sendMessage(Main.prefix + "You don't have permission to change your EXP.");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("setexp")) {
                    if (Vault.hasPermission(p, "exp.set.other")) {
                        if (EntityManager.playerExists(args[1])) {
                            if (MoreMath.isNum(args[2])) {
                                ClassManager.setExp(args[1], Integer.valueOf(args[2]));
                            } else p.sendMessage(Main.prefix + "Invalid EXP amount.");
                        } else p.sendMessage(Main.prefix + "That player is not online.");
                    } else p.sendMessage(Main.prefix + "You don't have permission to change their EXP.");
                }
            }
        }
        return true;
    }
}

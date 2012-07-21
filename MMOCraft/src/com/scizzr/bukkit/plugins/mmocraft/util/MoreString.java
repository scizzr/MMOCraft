package com.scizzr.bukkit.plugins.mmocraft.util;

import org.bukkit.ChatColor;

public class MoreString {
    public static String stackToString(Throwable th) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement ste : th.getStackTrace()) {
            sb.append(ste.toString() + "\n");
        }
        return sb.toString();
    }
    
    public static String HpFoodBars(int num, ChatColor pre, ChatColor half, ChatColor post, boolean hp, boolean showAll, boolean extra, boolean wholeNums) {
        String barChar, str = pre + "";
        //if (hp) barChar = "\u2588";
        // else   barChar = "\u2588";
        if (hp) barChar = "|";
         else   barChar = "|";
        String space = "_";
        
        if(showAll) {
            for (int i = 1; i <= 20; i++) {
                str += barChar;
                if (i == num) { str += post; }
                if (extra) { if (i == 10) { str += ChatColor.WHITE + "" + (wholeNums ? (num >= 10 ? num : space + num) : (num == 20 ? (float)num/2 : space + (float)num/2)) + (i < num ? pre : post); } }
            }
        } else {
            for (int i = 1; i <= 20; i += 1) {
                if (i == num) {
                    str += (i % 2 == 0 ? pre : half);
                }
                if (i %2 == 0) {
                    str += barChar;
                }
                if (i == num+1) {
                    str += post;
                }
                if (extra) { if (i == 10) { str += ChatColor.WHITE + "" + (wholeNums ? (num >= 10 ? num : space + num) : (num == 20 ? (float)num/2 : space + (float)num/2)) + (i < num ? pre : post); } }
            }
        }
        return str + ChatColor.RESET;
    }
}

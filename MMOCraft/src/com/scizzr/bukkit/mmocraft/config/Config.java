package com.scizzr.bukkit.mmocraft.config;

import com.scizzr.bukkit.mmocraft.util.Util;

public class Config {
// Config - Main
    public static boolean genPrefix = true;
    public static boolean genStats = true;
    public static String  genUniqID = Util.getUniqID();
    public static boolean genVerCheck = true;
    public static boolean genAutoUpdate = false;
    public static boolean genErrorWeb = true;
    
    public static boolean econEnabled = true;
    
    public static boolean permAllowOps = true;
    
    public static String locale = "en";
    
//XXX TESTING
    public static boolean damageAlter = true;
}

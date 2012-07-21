package com.scizzr.bukkit.plugins.mmocraft.util;

import java.text.DecimalFormat;

import org.bukkit.Location;

public class MoreMath {
    public static boolean between(double num, double d, double e) {
        return between(num, d, e, false);
    }
    
    public static boolean between(double num, double d, double e, boolean inclusive) {
// inclusive means that numbers can be = to the min and max
        if (inclusive) {
            if (d <= num && num <= e) {
                return true;
            }
            return false;
        } else {
            if (d < num && num < e) {
                return true;
            }
            return false;
        }
    }
    
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static Double setDec(Double d, Integer n) {
        String s = "";
        for (int i = 0; i < n; i++) { s+= "#"; }
        
        DecimalFormat decForm = new DecimalFormat("#." + s);
        Double r = Double.valueOf(decForm.format(d));
        
        return r;
    }
    
    public static Integer angleToPitch(Integer angle) {
        if ((angle - 90) <= 180) return (angle - 90) * -1;
        else return 180 - (angle + 90);
    }
    
    public static float getYawFromLocToLoc(Location locSrc, Location locDest) {
        double a = locDest.getBlockX()-locSrc.getBlockX();
        double b = locDest.getBlockZ()-locSrc.getBlockZ();
        //double c = Math.sqrt((a*a) + (b*b));
        
        double t = Math.atan(a/b);
        
        double A = t * (180/Math.PI);
        //double B = 180 - A - 90.0;
        //double C = 90.0;
        
        int add = locSrc.getBlockZ() <= locDest.getBlockZ() ? 360 : 180;
        
        return (add-(float) A);
    }
}

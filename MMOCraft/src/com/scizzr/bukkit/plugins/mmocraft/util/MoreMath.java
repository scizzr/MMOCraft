package com.scizzr.bukkit.plugins.mmocraft.util;

import java.text.DecimalFormat;

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
    
    public static boolean isNum(String s) {
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
}

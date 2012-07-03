package com.scizzr.bukkit.plugins.mmocraft.util;

public class MoreString {
    public static String stackToString(Throwable th) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement ste : th.getStackTrace()) {
            sb.append(ste.toString() + "\n");
        }
        return sb.toString();
    }
}

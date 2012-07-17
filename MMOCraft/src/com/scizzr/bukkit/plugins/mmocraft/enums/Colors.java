package com.scizzr.bukkit.plugins.mmocraft.enums;

import java.util.Map;

import com.google.common.collect.Maps;

public enum Colors {
    INFO('3', 0x3),     // aqua
    WARN('e', 0x1),     // yellow
    ERROR('c', 0xc);    // red
    
    public static final char COLOR_CHAR = '\u00A7';
    
    private final int intCode;
    private final char code;
    private final boolean isFormat;
    private final String toString;
    private final static Map<Integer, Colors> BY_ID = Maps.newHashMap();
    private final static Map<Character, Colors> BY_CHAR = Maps.newHashMap();
    
    private Colors(char code, int intCode) {
        this(code, intCode, false);
    }
    
    private Colors(char code, int intCode, boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[] { COLOR_CHAR, code });
    }
    
    public char getChar() {
        return code;
    }
    
    public String toString() {
        return toString;
    }
    
    public boolean isFormat() {
        return isFormat;
    }
    
    static {
        for (Colors color : values()) {
            BY_ID.put(color.intCode, color);
            BY_CHAR.put(color.code, color);
        }
    }
}

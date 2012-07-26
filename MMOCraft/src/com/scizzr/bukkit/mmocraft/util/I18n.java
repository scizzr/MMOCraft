package com.scizzr.bukkit.mmocraft.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.scizzr.bukkit.mmocraft.Main;

public class I18n {
    private static I18n instance;
    private ResourceBundle bundle;
    private Locale locale;
    private Map<String, MessageFormat> messageFormatCache = new HashMap<String, MessageFormat>();
    
    public I18n(Main plugin) {
        locale = Locale.ENGLISH;
        //bundle = ResourceBundle.getBundle("com.scizzr.bukkit.mmocraft.util.messages", locale);
    }
    
    public static String _(String string, Object[] objects) {
        if (instance == null) { return ""; }
        return string;
/*
        try {
            if (objects.length == 0) {
                return instance.translate(string);
            }
    
            return instance.format(string, objects);
        } catch (Exception ex) {
            return string;
        }
*/
    }
    
    public String format(String string, Object[] objects) {
        String format = translate(string);
        MessageFormat messageFormat = (MessageFormat)messageFormatCache.get(format);
        
        if (messageFormat == null) {
            messageFormat = new MessageFormat(format);
            messageFormatCache.put(format, messageFormat);
        }
        
        return messageFormat.format(objects);
    }
    
    public String translate(String string) {
        return bundle.getString(string);
    }

    public void onEnable() {
        instance = this;
    }
    
    public void onDisable() {
        instance = null;
    }

    public void updateLocale(String localeNew) {
        if ((localeNew == null) || localeNew.isEmpty()) { return; }
        
        String[] split = localeNew.split("[_\\.]");
        
        switch (split.length) {
            case 1:
                locale = new Locale(split[0]);
            case 2:
                locale = new Locale(split[0], split[1]);
            case 3:
                locale = new Locale(split[0], split[1], split[2]);
        }
        
        ResourceBundle.clearCache();
        
        bundle = ResourceBundle.getBundle("com.scizzr.bukkit.mmocraft.util.messages", locale);
    }
}

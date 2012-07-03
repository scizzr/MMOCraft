package com.scizzr.bukkit.plugins.mmocraft.timers;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Arrow;

public class ArrowTimer implements Runnable {
    public static ConcurrentHashMap<Arrow, Integer> arrows = new ConcurrentHashMap<Arrow, Integer> ();
    
    String act; Object p1; Object p2;
    
    public ArrowTimer(String act, Object p1, Object p2) {
        this.act = act; this.p1 = p1; this.p2 = p2;
    }
    
    public void run() {
        if (act.equalsIgnoreCase("countdown")) {
            countdown();
        }
    }
    
    public static void countdown() {
        for (Entry<Arrow, Integer> entry : arrows.entrySet()) {
            add(entry.getKey(), entry.getValue() - 1);
        }
    }
    
    public static void add(Arrow arrow, Integer time) {
        if (time > 0) {
            arrows.put(arrow, time);
        } else {
            arrow.remove();
            arrows.remove(arrow);
        }
    }
    
    public static void remove(Arrow arrow) {
        arrows.remove(arrow);
    }
}

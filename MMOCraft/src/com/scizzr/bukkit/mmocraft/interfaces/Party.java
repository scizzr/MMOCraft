package com.scizzr.bukkit.mmocraft.interfaces;

import java.util.HashMap;

public class Party {
    //type : 0=private, 1=public, 2=passworded
    int type = 0;
    String password = "";
    String owner;
    HashMap<String, Integer> members = new HashMap<String, Integer>();
    //members : name,
    //          0=invited, 1=joined
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String who) {
        owner = who;
    }
    
    public void add(String who) {
        members.put(who, 0);
    }
    
    public void remove(String who) {
        if (members.containsKey(who)) {
            remove(who);
        }
    }
    
    
}

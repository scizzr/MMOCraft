package com.scizzr.bukkit.mmocraft.threads;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class Update implements Runnable {
    public static boolean updated = false;
    public static String verUpd = null;
    
    private String act; Object par1, par2;
    
    public Update(String act, Object par1, Object par2) {
        this.act = act;
        this.par1 = par1;
        this.par2 = par2;
    }
    
    public void run() {
        if (act.equalsIgnoreCase("check")) {
            try { check((Player) par1); } catch(Exception ex) { ex.printStackTrace(); }
        } else if (act.equalsIgnoreCase("update")) {
            try {
                update((Player) par1, (String) par2);
            } catch(Exception ex) {
                //ex.printStackTrace();
                //MMOCraft.suicide(ex);
            }
        }
    }
    
    public static void check(Player p) throws Exception {
        try {
            URL url = new URL(
                String.format(MMOCraft.host + "plugins/version.php?rev=3&plug=" + MMOCraft.info.getName())
            );
            
            URLConnection conn = url.openConnection();
            InputStreamReader stream = new InputStreamReader(conn.getInputStream());
            BufferedReader buff = new BufferedReader(stream);
            String line = buff.readLine();
            
            while (line != null) {
                String[] data = line.split("<br/>");
                if (data.length == 2) {
                    String updURL = data[1];
                    String verNew = data[0];
                    String verCur = MMOCraft.info.getVersion();
                    if (!verNew.equalsIgnoreCase(verCur)) {
                        if (Config.genAutoUpdate == true) {
                            if (updated == false) {
                                new Thread(new Update("update", p, verNew + "@" + updURL)).start();
                            } else {
                                p.sendMessage(MMOCraft.prefix + I18n._("upddone", new Object[] {verUpd}));
                                p.sendMessage(MMOCraft.prefix + I18n._("updready", new Object[] {}));
                            }
                        } else {
                            if (p != null) {
                                p.sendMessage(MMOCraft.prefix + I18n._("updavailA", new Object[] {MMOCraft.info.getName()}));
                                p.sendMessage(MMOCraft.prefix + I18n._("updavailB", new Object[] {verNew}));
                                p.sendMessage(ChatColor.YELLOW + "http://dev.bukkit.org" + updURL);
                            }
                        }
                    }
                    line = buff.readLine();
                }
            }
            stream.close();
        } catch (Exception ex) {
            //ex.printStackTrace();
            //MMOCraft.suicide(ex);
        }
    }
    
    public static void update(Player p, String verAndUrl) throws Exception {
        String[] split = verAndUrl.split("@");
        String ver = split[0];
        String url = "http://dev.bukkit.org" + split[1];
        
        java.io.BufferedInputStream in = new java.io.BufferedInputStream(new URL(url).openStream());
        
        File tgt = MMOCraft.filePlug;
        
        java.io.FileOutputStream fos = new java.io.FileOutputStream(tgt);
        
        java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte[] data = new byte[1024];
        int x=0;
        
        while((x=in.read(data, 0, 1024))>=0) {
            bout.write(data, 0, x);
        }
        
        bout.close();
        in.close();
        
        if (p != null) {
            p.sendMessage(MMOCraft.prefix + I18n._("upddone", new Object[] {ver}));
            p.sendMessage(MMOCraft.prefix + I18n._("updready", new Object[] {}));
        }
        
        updated = true; verUpd = ver;
    }
}

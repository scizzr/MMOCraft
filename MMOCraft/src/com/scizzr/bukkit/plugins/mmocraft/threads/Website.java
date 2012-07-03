package com.scizzr.bukkit.plugins.mmocraft.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.scizzr.bukkit.plugins.mmocraft.Main;
import com.scizzr.bukkit.plugins.mmocraft.util.Vault;

public class Website implements Runnable {
    private String act; Object par2;
    
    public Website(String act, Object par2) {
        this.act = act;
        this.par2 = par2;
    }
    
    public void run() {
        if (act.equalsIgnoreCase("postStats")) {
            postStats();
        } else if (act.equalsIgnoreCase("checkVersion")) {
            checkVersion((Player) par2);
        }
    }
    
    public void postStats() {
        String name = null, sip = "0", port = null, max = null, gm = "UNKNOWN", ver = null;
        
        name = Bukkit.getServerName();
        sip = (Bukkit.getIp() != "" ? Bukkit.getIp() : "");
        port = String.valueOf(Bukkit.getPort());
        max = String.valueOf(Bukkit.getMaxPlayers());
        try { gm = Bukkit.getDefaultGameMode().toString(); } catch (Throwable th) { /*th.printStackTrace();*/ } finally {
            ver = Bukkit.getBukkitVersion();
            
            try {
                URL url = new URL(
                    String.format("http://www.scizzr.com/util/plugins/stats.php?") +
                    String.format("system=" + "%s", URLEncoder.encode(Main.osN, "UTF-8")) +
                    String.format("&plugname=" + "%s", URLEncoder.encode(Main.info.getName(), "UTF-8")) +
                    String.format("&plugver=" + "%s", URLEncoder.encode(Main.info.getVersion(), "UTF-8")) +
                    String.format("&name=" + "%s", URLEncoder.encode(name, "UTF-8")) +
                    String.format("&sip=" + "%s", URLEncoder.encode(sip, "UTF-8")) +
                    String.format("&port=" + "%s", URLEncoder.encode(port, "UTF-8")) +
                    String.format("&max=" + "%s", URLEncoder.encode(max, "UTF-8")) +
                    String.format("&gm=" + "%s", URLEncoder.encode(gm, "UTF-8")) +
                    String.format("&ver=" + "%s", URLEncoder.encode(ver, "UTF-8"))
                );
                
                URLConnection conn = url.openConnection();
                InputStreamReader stream = new InputStreamReader(conn.getInputStream());
                BufferedReader buff = new BufferedReader(stream);
                String line = buff.readLine();
                
                while (line != null) {
                    // do nothing; just post stats
                    line = buff.readLine();
                }
                stream.close();
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }
    
    public static void checkVersion(Player p) {
        try {
            URL url = new URL(
                String.format("http://www.scizzr.com/util/plugins/version.php?plug=" + Main.info.getName())
            );
            
            URLConnection conn = url.openConnection();
            InputStreamReader stream = new InputStreamReader(conn.getInputStream());
            BufferedReader buff = new BufferedReader(stream);
            String line = buff.readLine();
            
            while (line != null) {
                String verNew = line.split("<br/>")[1];
                String verCur = Main.info.getVersion().endsWith("+") ? Main.info.getVersion().substring(0, Main.info.getVersion().length()-1) : Main.info.getVersion();
                if (!verNew.equalsIgnoreCase(verCur)) {
                    if (p != null) {
                        if (Vault.hasPermission(p, "newver")) {
                            p.sendMessage(Main.prefix + "Your version of " + Main.info.getName() + " is out of date.");
                            p.sendMessage("Version " + verNew + " can be downloaded from this link:");
                            p.sendMessage(ChatColor.YELLOW + "http://www.scizzr.com/plugins/" + Main.info.getName() + ".jar");
                        }
                    }
                }
                line = buff.readLine();
            }
            stream.close();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
}

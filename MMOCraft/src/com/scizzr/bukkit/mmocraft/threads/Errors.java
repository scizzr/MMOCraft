package com.scizzr.bukkit.mmocraft.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.bukkit.Bukkit;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.config.Config;
import com.scizzr.bukkit.mmocraft.util.Base64;
import com.scizzr.bukkit.mmocraft.util.I18n;

public class Errors implements Runnable {
    String err;
    
    public Errors(String err) {
        this.err = Base64.encode(err);
    }
    
    public void run() {
        postError();
    }
    
    public void postError() {
        String ver = "UNKNOWN";
        
        try {
            ver = Bukkit.getBukkitVersion();
            
            URL url = new URL(
                    //$uuid && $system && $plugname && $plugver && $ver && $err
                    String.format(MMOCraft.host + "plugins/errors.php")
                );
            
            String data = new String(
                    //$uuid && $system && $plugname && $plugver && $ver && $err
                    String.format("uniqid=" + "%s", URLEncoder.encode(Config.genUniqID, "UTF-8")) + 
                    String.format("&system=" + "%s", URLEncoder.encode(MMOCraft.osN, "UTF-8")) +
                    String.format("&plugname=" + "%s", URLEncoder.encode(MMOCraft.info.getName(), "UTF-8")) +
                    String.format("&plugver=" + "%s", URLEncoder.encode(MMOCraft.info.getVersion(), "UTF-8")) +
                    String.format("&ver=" + "%s", URLEncoder.encode(ver, "UTF-8")) +
                    String.format("&err=" + "%s", URLEncoder.encode(err, "UTF-8"))
                );
            
            URLConnection conn = url.openConnection(); conn.setDoOutput(true);
            
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream()); writer.write(data); writer.flush();
            InputStreamReader stream = new InputStreamReader(conn.getInputStream());
            
            BufferedReader buff = new BufferedReader(stream);
            
            String line = buff.readLine();
            
            while (line != null) {
                // do nothing; just post error
                line = buff.readLine();
            }
            
            stream.close();
        } catch (Exception ex) {
            MMOCraft.log.info(MMOCraft.prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("stackfailA", new Object[] {}));
            MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("stackfailB", new Object[] {}));
            MMOCraft.log.info(MMOCraft.prefixConsole + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }
    }
}

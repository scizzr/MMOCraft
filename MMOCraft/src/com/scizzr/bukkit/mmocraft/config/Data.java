package com.scizzr.bukkit.mmocraft.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import com.scizzr.bukkit.mmocraft.MMOCraft;
import com.scizzr.bukkit.mmocraft.aids.Beacon;
import com.scizzr.bukkit.mmocraft.aids.Forcefield;
import com.scizzr.bukkit.mmocraft.aids.Sigil;
import com.scizzr.bukkit.mmocraft.aids.Totem;
import com.scizzr.bukkit.mmocraft.aids.Trap;
import com.scizzr.bukkit.mmocraft.aids.Turret;
import com.scizzr.bukkit.mmocraft.classes.Archer;
import com.scizzr.bukkit.mmocraft.classes.Assassin;
import com.scizzr.bukkit.mmocraft.classes.Barbarian;
import com.scizzr.bukkit.mmocraft.classes.Druid;
import com.scizzr.bukkit.mmocraft.classes.Necromancer;
import com.scizzr.bukkit.mmocraft.classes.Wizard;
import com.scizzr.bukkit.mmocraft.interfaces.Aid;
import com.scizzr.bukkit.mmocraft.interfaces.Pet;
import com.scizzr.bukkit.mmocraft.interfaces.Race;
import com.scizzr.bukkit.mmocraft.managers.AidMgr;
import com.scizzr.bukkit.mmocraft.managers.PetMgr;
import com.scizzr.bukkit.mmocraft.managers.RaceMgr;
import com.scizzr.bukkit.mmocraft.pets.PetBlaze;
import com.scizzr.bukkit.mmocraft.pets.PetPigzombie;
import com.scizzr.bukkit.mmocraft.pets.PetSpider;
import com.scizzr.bukkit.mmocraft.util.I18n;
import com.scizzr.bukkit.mmocraft.util.Util;

public class Data {
    static File file;
    static YamlConfiguration config;
    private static CopyOnWriteArrayList<Race> races = new CopyOnWriteArrayList<Race>();
    private static CopyOnWriteArrayList<Aid> aids = new CopyOnWriteArrayList<Aid>();
    private static CopyOnWriteArrayList<Pet> pets = new CopyOnWriteArrayList<Pet>();
    
    public static void load() {
        RaceMgr.put(new CopyOnWriteArrayList<Race>());
        AidMgr.put(new CopyOnWriteArrayList<Aid>());
        PetMgr.put(new CopyOnWriteArrayList<Pet>());
        
        file = new File(MMOCraft.fileFolder + MMOCraft.slash + "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("succeedyml", new Object[] {file.getName()}));
            } catch (Exception ex) {
                MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("failedyml", new Object[] {file.getName()}));
                return;
            }
        }
        
        try {
            YamlConfiguration config = new YamlConfiguration(); config.load(file);
            for (String key : config.getKeys(false)) {
                //Race information
                String raceName = config.getString(key + ".race");
                int raceExp = config.getInt(key + ".exp");
                
                Race race = null;
                
                if (raceName.equalsIgnoreCase("archer"))      { race = new Archer();      }
                if (raceName.equalsIgnoreCase("assassin"))    { race = new Assassin();    }
                if (raceName.equalsIgnoreCase("barbarian"))   { race = new Barbarian();   }
                if (raceName.equalsIgnoreCase("druid"))       { race = new Druid();       }
                if (raceName.equalsIgnoreCase("necromancer")) { race = new Necromancer(); }
                if (raceName.equalsIgnoreCase("wizard"))      { race = new Wizard();      }
                
                if (race != null) {
                    race.setPlayerName(key);
                    race.setExp(raceExp);
                    races.add(race);
                }
                
                RaceMgr.put(races);
                
                for (String subkey : config.getConfigurationSection(key).getKeys(false)) {
                    //Aids information
                    if (subkey.equals("aids")) {
                        List<String> aidsData = config.getStringList(key + ".aids");
                        
                        if (aidsData.size() == 0) { continue; }
                        for (int i = 0; i < aidsData.size(); i++) {
                            String[] aidDataA = aidsData.get(i).split(";");
                            String aidOwner = key;
                            int aidCount = Integer.valueOf(aidDataA[1]);
                            Location loc = Util.stringToLocation(aidDataA[2], ":");
                            World w = loc.getWorld();
                            int x = loc.getBlockX();
                            int y = loc.getBlockY();
                            int z = loc.getBlockZ();
                            
                            Aid aid = null;
                            
                            if (aidDataA[0].equalsIgnoreCase("turret"))     { aid = new Turret();     }
                            if (aidDataA[0].equalsIgnoreCase("sigil"))      { aid = new Sigil();      }
                            if (aidDataA[0].equalsIgnoreCase("beacon"))     { aid = new Beacon();     }
                            if (aidDataA[0].equalsIgnoreCase("forcefield")) { aid = new Forcefield(); }
                            if (aidDataA[0].equalsIgnoreCase("totem"))      { aid = new Totem();      }
                            if (aidDataA[0].equalsIgnoreCase("trap"))       { aid = new Trap();       }
                            
                            if (aid != null) {
                                aid.setLocation(new Location(w, x, y, z));
                                aid.setOwnerName(aidOwner);
                                aid.setCount(aidCount);
                                aids.add(aid);
                                race.addAid(aid);
                            }
                        }
                        
                        AidMgr.put(aids);
                        
                    //Pets information
                    } else if (subkey.equals("pets")) {
                        List<String> petsData = config.getStringList(key + ".pets");
                        
                        if (petsData.size() == 0) { continue; }
                        
                        for (int i = 0; i <= petsData.size(); i++) {
                            String[] petData = petsData.get(0).split(";");
                            String petOwner = key;
                            int petHp = Integer.valueOf(petData[1]);
                            
                            Pet pet = null;
                            
                            if (petData[0].equalsIgnoreCase("blaze"))     { pet = new PetBlaze();     }
                            if (petData[0].equalsIgnoreCase("pigzombie")) { pet = new PetPigzombie(); }
                            if (petData[0].equalsIgnoreCase("spider"))    { pet = new PetSpider();    }
                            
                            if (pet != null) {
                                pet.setOwnerName(petOwner);
                                pet.setHealth(petHp);
                                pets.add(pet);
                                race.addPet(pet);
                            }
                        }
                        
                        PetMgr.put(pets);
                    }
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    public static void save(String what) {
        file = new File(MMOCraft.fileFolder + MMOCraft.slash + "data.yml");
        
        if (!file.exists()) {
            try {
                file.createNewFile();
                MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("succeedyml", new Object[] { file.getName() }));
            } catch (Exception ex) {
                MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("failedyml", new Object[] { file.getName() }));
                return;
            }
        }
        try {
            config = new YamlConfiguration();
            
            if (what.equalsIgnoreCase("races") || what.equalsIgnoreCase("all")) {
                synchronized (races) {
                    races = RaceMgr.get();
                    for (Race race : races) {
                        String player = race.getPlayerName();
                        String name = race.getName();
                        int exp = race.getExp();
                        config.set(player + ".race", name);
                        config.set(player + ".exp", exp);
                    }
                }
            }
            if (what.equalsIgnoreCase("aids") || what.equalsIgnoreCase("all")) {
                synchronized(aids) {
                    HashMap<String, List<String>> mapAids = new HashMap<String, List<String>>();
                    List<String> listAids = new ArrayList<String> ();
                    
                    aids = AidMgr.get();
                    
                    for (Aid aid : aids) {
                        String player = aid.getOwnerName();
                        String name = aid.getName();
                        Location loc = aid.getLocation();
                        int count = aid.getCount();
                        
                        if (mapAids.containsKey(player)) {
                            listAids = mapAids.get(player);
                        }
                        
                        listAids.add(name + ";" + count + ";" + Util.locationToString(loc, ":"));
                        
                        mapAids.put(player, listAids);
                    }
                    
                    for (Entry<String, List<String>> entry : mapAids.entrySet()) {
                        String key = entry.getKey();
                        List<String> value = entry.getValue();
                        config.set(key + ".aids", value);
                    }
                }
            }
            
            if (what.equalsIgnoreCase("pets") || what.equalsIgnoreCase("all")) {
                synchronized(pets) {
                    HashMap<String, List<String>> mapPets = new HashMap<String, List<String>>();
                    List<String> listPets = new ArrayList<String> ();
                    
                    pets = PetMgr.get();
                    
                    for (Pet pet : pets) {
                        String player = pet.getOwnerName();
                        String name = pet.getName();
                        int hp = pet.getHealth();
                        
                        if (mapPets.containsKey(player)) {
                            listPets = mapPets.get(player);
                        }
                        
                        listPets.add(name + ";" + hp);
                        
                        mapPets.put(player, listPets);
                    }
                    
                    for (Entry<String, List<String>> entry : mapPets.entrySet()) {
                        String key = entry.getKey();
                        List<String> value = entry.getValue();
                        config.set(key + ".pets", value);
                    }
                }
            }
        } catch (Exception ex) {
            MMOCraft.log.info(MMOCraft.prefixConsole + "Caught@ 221");
            ex.printStackTrace();
        } finally {
            try {
                config.save(file);
            } catch (Exception ex) {
                MMOCraft.log.info(MMOCraft.prefixConsole + I18n._("failedsave", new Object[] {}));
            }
        }
    }
}

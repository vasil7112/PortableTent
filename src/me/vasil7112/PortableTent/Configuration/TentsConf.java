package me.vasil7112.PortableTent.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.vasil7112.PortableTent.PortableTent;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TentsConf {
	private static FileConfiguration customConfig = null;
	private static File customConfigFile = null;
	
	
	public static FileConfiguration getCustomConfig() {
	    if (customConfig == null) {
	        reloadCustomConfig();
	    }
	    return customConfig;
	}
	
	public static void reloadCustomConfig() {
	    if (customConfigFile == null) {
	    customConfigFile = new File(PortableTent.plugin.getDataFolder(), "Tents.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = PortableTent.plugin.getResource("Tents.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	    saveCustomConfig();
	}
	    
    public static void saveCustomConfig() {
	        if (customConfig == null || customConfigFile == null) {
	            return;
	        }
	        try {
	            getCustomConfig().save(customConfigFile);
	        } catch (IOException ex) {
	        	PortableTent.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	        }
    }
    
    public static void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(PortableTent.plugin.getDataFolder(), "Tents.yml");
        }
        if (!customConfigFile.exists()) {            
        	PortableTent.plugin.saveResource("Tents.yml", false);
        }
    }
}

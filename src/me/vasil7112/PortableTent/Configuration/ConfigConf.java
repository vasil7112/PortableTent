package me.vasil7112.PortableTent.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import me.vasil7112.PortableTent.PortableTent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigConf {
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
	    customConfigFile = new File(PortableTent.plugin.getDataFolder(), "Config.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = PortableTent.plugin.getResource("Config.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	    setupConfig();
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
            customConfigFile = new File(PortableTent.plugin.getDataFolder(), "Config.yml");
        }
        if (!customConfigFile.exists()) {            
        	PortableTent.plugin.saveResource("Config.yml", false);
        }
    }
    
    private static void setupConfig(){
    	if(getCustomConfig().get("Premium-User")==null){
    		getCustomConfig().set("Premium-User.Username", String.valueOf(""));	
    		getCustomConfig().set("Premium-User.Password", String.valueOf(""));
    		getCustomConfig().set("Premium-User.Server-IP", String.valueOf(""));
    		getCustomConfig().set("Premium-User.Server-IMG-468x60", String.valueOf(""));
    	}
    	if(getCustomConfig().get("AutoUpdate")==null){
    		getCustomConfig().set("AutoUpdate", Boolean.valueOf(true));
    	}
    	if(getCustomConfig().get("TentBlock")==null){
    		getCustomConfig().set("TentBlock", String.valueOf("WORKBENCH"));
    		if(getCustomConfig().get("TentItem")!=null){
        		getCustomConfig().set("TentItem", "");
        	}
    	}
    	if(getCustomConfig().get("FirstJoinGift.Enabled")==null){
    		getCustomConfig().set("FirstJoinGift.Enabled", Boolean.valueOf(false));
    		getCustomConfig().set("FirstJoinGift.Tents", "None");
    	}
    	if(getCustomConfig().get("LimitedTents.Enabled")==null){
    		getCustomConfig().set("LimitedTents.Enabled", Boolean.valueOf(true));
    	}
    	if(getCustomConfig().get("LimitedTents.Amount")==null){
    		getCustomConfig().set("LimitedTents.Amount", Integer.valueOf(3));
    	}
    	if(getCustomConfig().get("ProtectTents")==null){
    		getCustomConfig().set("ProtectTents", Boolean.valueOf(true));
    	}
    	if(getCustomConfig().get("User-Perms")==null ){
    		getCustomConfig().set("User-Perms", Boolean.valueOf(false));
    	}
    	if(getCustomConfig().get("Per-Tent-Perms")==null ){
    		getCustomConfig().set("Per-Tent-Perms", Boolean.valueOf(false));
    	}
    	if(getCustomConfig().get("Language.No-Perms")==null ){
    		getCustomConfig().set("Language.No-Perms", String.valueOf("Oh No! &4You don't have enough permissions!"));
    	}
    	if(getCustomConfig().get("Language.No-Tent-Perms")==null ){
    		getCustomConfig().set("Language.No-Tent-Perms", String.valueOf("Oh No! &4You don't have enough permissions!"));
    	}
    	if(getCustomConfig().get("Language.No-Build")==null ){
    		getCustomConfig().set("Language.No-Build", String.valueOf("&4You can't build here!"));
    	}
    	if(getCustomConfig().get("Language.Give-No-Perm")==null ){
    		getCustomConfig().set("Language.Give-No-Perm", String.valueOf("&4You don't have enough permissions"));
    	}
    	if(getCustomConfig().get("Language.Give-Not-Online")==null ){
    		getCustomConfig().set("Language.Give-Not-Online", String.valueOf("&4User is not online"));
    	}
    	if(getCustomConfig().get("Language.Give-Not-Exist")==null ){
    		getCustomConfig().set("Language.Give-Not-Exist", String.valueOf("&4This tent doesn't exist!"));
    	}
    }
}

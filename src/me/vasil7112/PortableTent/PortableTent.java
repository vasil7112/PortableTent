package me.vasil7112.PortableTent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import me.vasil7112.PortableTent.Command.PortableTentCMD;
import me.vasil7112.PortableTent.Configuration.ConfigConf;
import me.vasil7112.PortableTent.Configuration.PlayersConf;
import me.vasil7112.PortableTent.Configuration.TentsConf;
import me.vasil7112.PortableTent.Configuration.TentsLocConf;
import me.vasil7112.PortableTent.Listeners.PlayerListener;
import me.vasil7112.PortableTent.Listeners.SignListener;
import me.vasil7112.PortableTent.Utils.Crafting;
import me.vasil7112.PortableTent.Utils.DestroyTent;
import me.vasil7112.PortableTent.Utils.Generator;
import me.vasil7112.PortableTent.Utils.Updater;
import net.milkbowl.vault.economy.Economy;

import com.massivecraft.factions.Factions;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PortableTent extends JavaPlugin implements Listener{
	public static PortableTent plugin;
	public static Plugin vault = null;
	public static WorldGuardPlugin worldguardPlugin = null;
	public static Economy economy = null;
	public static Factions factions = null;
	
	@SuppressWarnings("unused")
	@Override
	public void onEnable(){
		plugin = this;
		setupWorldGuard();
		setupEconomy();
		setupFactions();
		TentsConf.saveDefaultConfig();
		TentsConf.getCustomConfig();
		TentsLocConf.saveDefaultConfig();
		TentsLocConf.getCustomConfig();
		ConfigConf.saveDefaultConfig();
		ConfigConf.getCustomConfig();
		PlayersConf.saveDefaultConfig();
		PlayersConf.getCustomConfig();
		Crafting.addCraftingRecipes();
		Bukkit.getPluginManager().registerEvents(plugin, plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new SignListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new DestroyTent(), plugin);
		getCommand("PortableTent").setExecutor(new PortableTentCMD());
		if(ConfigConf.getCustomConfig().getBoolean("AutoUpdate")){
			Updater updateCheck = new Updater(66728);
		}
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
		}
		
		if((ConfigConf.getCustomConfig().getBoolean("ProtectTents")) && (worldguardPlugin == null)) {
			 Bukkit.getLogger().warning("You are required to have WorldGuard since you have Tents Protection on! Please download it!");
			 Bukkit.getServer().getPluginManager().disablePlugin(plugin);
		}
		if(ConfigConf.getCustomConfig().get("Premium-User")!=null){
			if(ConfigConf.getCustomConfig().getString("Premium-User.Username")!=null && ConfigConf.getCustomConfig().getString("Premium-User.Password")!=null && ConfigConf.getCustomConfig().getString("Premium-User.Server-IP")!=null && ConfigConf.getCustomConfig().getString("Premium-User.Server-IMG-468x60")!=null){
				URL url;
				 
				try {
					url = new URL("http://vasil7112.nodedevs.net/scripts/portabletent.upload.img.script.php?u="+ConfigConf.getCustomConfig().get("Premium-User.Username")+"&p="+ConfigConf.getCustomConfig().get("Premium-User.Password")+"&ip="+ConfigConf.getCustomConfig().get("Premium-User.Server-IP")+"&img="+ConfigConf.getCustomConfig().get("Premium-User.Server-IMG-468x60"));
					URLConnection conn = url.openConnection();
		 
					BufferedReader br = new BufferedReader(
		                               new InputStreamReader(conn.getInputStream()));
		 
					String inputLine;
					
					while ((inputLine = br.readLine()) != null) {
						if(inputLine.contains("not exist!")){
							getLogger().info("This username doesn't exist!");
						}else if(inputLine.contains("not found!")){
							getLogger().info("Your password is wrong!");
						}else if(inputLine.contains("Error3")){
							getLogger().info("You forgot to enter your server IP!");
						}else if(inputLine.contains("Error4")){
							getLogger().info("You forgot to enter your server IMG link.");
						}else if(inputLine.contains("Error5")){
							getLogger().info("The link must end in .png or .jpg!");
						}else if(inputLine.contains("Error6")){
							getLogger().info("The link must start with http:// or https://");
						}else if(inputLine.contains("done!")){
							getLogger().info("Your Premium Features have been activated.");
							
							Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
								@Override
								public void run() {
									URL url2;
									try {
										url2 = new URL("http://vasil7112.nodedevs.net/scripts/portabletent.upload.img.script.php?u="+ConfigConf.getCustomConfig().get("Premium-User.Username")+"&p="+ConfigConf.getCustomConfig().get("Premium-User.Password")+"&ip="+ConfigConf.getCustomConfig().get("Premium-User.Server-IP")+"&img="+ConfigConf.getCustomConfig().get("Premium-User.Server-IMG-468x60"));
										URLConnection conn = url2.openConnection();
										 
										BufferedReader br2 = new BufferedReader(
							                               new InputStreamReader(conn.getInputStream()));
										br2.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
					        }, 0L, 20 * 60 * 10L);
							
						}
					}
					br.close();
		 
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void onDisable(){
		TentsConf.saveCustomConfig();
		ConfigConf.saveCustomConfig();
		plugin = null;
		worldguardPlugin = null;
	}
	
	private void setupWorldGuard() {
		Plugin wg = this.getServer().getPluginManager().getPlugin("WorldGuard");
	    if(wg == null) {
	    	getLogger().info("WorldGuard was not found. Did you know that by using it, players won't be able to place tents on protected areas?");
	    }else{
	    	worldguardPlugin = (WorldGuardPlugin)wg;     
	    }
	}
	
	private boolean setupEconomy(){
		 vault = this.getServer().getPluginManager().getPlugin("Vault");
		 if(vault == null) {
			 getLogger().info("Vault was not found! Did you know that by using vault you can add buy/sell signs?");
		 }else{
			 RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		     if(economyProvider != null) {
		         economy = economyProvider.getProvider();
		     }
		     return (economy != null);   
		 }
		return false;
	}

	private void setupFactions() {
		Plugin fac = this.getServer().getPluginManager().getPlugin("Factions");
	    if(fac != null) {
	    	factions = (Factions)fac;   
	    }
	}
	
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e){
		if(e.getBlockPlaced().getType().equals(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock"))) && e.getItemInHand().hasItemMeta() && TentsConf.getCustomConfig().contains(ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()))){
			e.getBlockPlaced().setType(Material.AIR);
			if(!Generator.GenerateBuilding(ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()), e.getPlayer(), e.getBlockPlaced().getLocation())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockBrakeEvent(BlockBreakEvent e){
		Player player = e.getPlayer();
		if(e.getBlock().getType().equals(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock"))) && e.getBlock().hasMetadata("TENTBLOCK")){
			int blockX = e.getBlock().getLocation().getBlockX();
			int blockY = e.getBlock().getLocation().getBlockY();
			int blockZ = e.getBlock().getLocation().getBlockZ();
			for(int z=1; z<TentsLocConf.getCustomConfig().getConfigurationSection("").getKeys(false).size() + 1; z++){
				if(TentsLocConf.getCustomConfig().getString(z + ".Location").equals(blockX+","+blockY+","+blockZ)){   						
					if(!TentsLocConf.getCustomConfig().getBoolean(z+".Destroyed") && (player.isOp() ||  TentsLocConf.getCustomConfig().getString(z + ".Owner") == player.getName())){
			            e.setCancelled(true);
			            player.sendMessage(ChatColor.GREEN + "You can't destroy this block! To pickup the tent just Shift-Right-Click it!");
				        return;
					}else if(!TentsLocConf.getCustomConfig().getBoolean(z+".Destroyed") && (!player.isOp() ||  TentsLocConf.getCustomConfig().getString(z + ".Owner") != player.getName())){
						e.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You cannot destroy this block! This is property of: "+TentsLocConf.getCustomConfig().getString(z + ".Owner"));
						return;
					}
					return;
				}
			}
		}
	}
	  
}

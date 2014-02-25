package me.vasil7112.PortableTent.Listeners;

import java.util.ArrayList;
import java.util.List;

import me.vasil7112.PortableTent.Configuration.ConfigConf;
import me.vasil7112.PortableTent.Configuration.PlayersConf;
import me.vasil7112.PortableTent.Configuration.TentsConf;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
public class PlayerListener implements Listener{

	public static Location point1;
	public static Location point2;
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
	    if(player.getItemInHand() !=null && player.getItemInHand().getType().equals(Material.DIAMOND_HOE) && player.hasPermission("PortableTent.create") && player.getItemInHand().hasItemMeta() && ChatColor.stripColor(player.getItemInHand().getItemMeta().getDisplayName()).equals("PortableTent Wand!")){
	    	if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
	    		event.setCancelled(true);
	    		Location loc = event.getClickedBlock().getLocation();
	    		player.sendMessage(ChatColor.GREEN+"Point 1 saved at " + loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ());
	    		point1 = loc;
	    	}
	    	if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
	    		event.setCancelled(true);
	    		Location loc = event.getClickedBlock().getLocation();
	    		player.sendMessage(ChatColor.GREEN+"Point 2 saved at " + loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ());
	    		point2 = loc;
	    	}
	    }
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e){
		Player player = e.getPlayer();
		if(ConfigConf.getCustomConfig().getBoolean("FirstJoinGift.Enabled") != false && (PlayersConf.getCustomConfig().contains(player.getName()) || PlayersConf.getCustomConfig().getBoolean(player.getName()+"FirstJoinTentRecieved") == false)){
			PlayersConf.getCustomConfig().set(player.getName()+"FirstJoinTentRecieved", Boolean.valueOf(true));
			if(!ConfigConf.getCustomConfig().getString("FirstJoinGift.Tents").equalsIgnoreCase("none")){
				if(ConfigConf.getCustomConfig().getString("FirstJoinGift.Tents").contains(",")){
					String[] giftsplit = ConfigConf.getCustomConfig().getString("FirstJoinGift.Tents").split(",");
					for(int gsi=0; gsi<giftsplit.length; gsi++){
						ItemStack i;
						i = new ItemStack(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")), 1);
					
						ItemMeta meta = i.getItemMeta();
				    	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TentsConf.getCustomConfig().getString(giftsplit[gsi]+".Name")));
				    	List<String> splitl = (List<String>) TentsConf.getCustomConfig().getList(giftsplit[gsi]+".Description");
				    	ArrayList<String> Lore = new ArrayList<String>();
				    	for(String s : splitl){
				    		Lore.add(ChatColor.translateAlternateColorCodes('&', s));
				    	}
				    	meta.setLore(Lore);
				    	i.setItemMeta(meta);
				    	player.getInventory().addItem(i);
				    	player.updateInventory();
					}
				}else{
					ItemStack i;
					i = new ItemStack(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")), 1);
					
					ItemMeta meta = i.getItemMeta();
				    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TentsConf.getCustomConfig().getString(ConfigConf.getCustomConfig().getString("FirstJoinGift.Tents")+".Name")));
				    List<String> splitl = (List<String>) TentsConf.getCustomConfig().getList(ConfigConf.getCustomConfig().getString("FirstJoinGift.Tents")+".Description");
				    ArrayList<String> Lore = new ArrayList<String>();
				    for(String s : splitl){
				    	Lore.add(ChatColor.translateAlternateColorCodes('&', s));
				    }
				    meta.setLore(Lore);
				    i.setItemMeta(meta);
				    player.getInventory().addItem(i);
				    player.updateInventory();
				}
			}
		}
		if(ConfigConf.getCustomConfig().getBoolean("LimitedTents.Enabled")){
			if(!PlayersConf.getCustomConfig().contains(player.getName()+".TentsAmount")){
				PlayersConf.getCustomConfig().set(player.getName()+".TentsAmount", Integer.valueOf(ConfigConf.getCustomConfig().getInt("LimitedTents.Amount")));
			}
		}
		PlayersConf.saveCustomConfig();
	}
}

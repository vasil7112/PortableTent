package me.vasil7112.PortableTent.Utils;

import java.util.ArrayList;
import java.util.List;

import me.vasil7112.PortableTent.PortableTent;
import me.vasil7112.PortableTent.Configuration.ConfigConf;
import me.vasil7112.PortableTent.Configuration.PlayersConf;
import me.vasil7112.PortableTent.Configuration.TentsConf;
import me.vasil7112.PortableTent.Configuration.TentsLocConf;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;

public class DestroyTent implements Listener{

	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(ConfigConf.getCustomConfig().getBoolean("User-Perms") == true && !player.hasPermission("PortableTent.use")){
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigConf.getCustomConfig().getString("Language.No-Perms")));
		}else if((ConfigConf.getCustomConfig().getBoolean("User-Perms") == true && player.hasPermission("PortableTent.use")) || (ConfigConf.getCustomConfig().getBoolean("User-Perms") == false)){
	    	if(player.isSneaking()){
	    		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
	    			int blockX = e.getClickedBlock().getLocation().getBlockX();
	    			int blockY = e.getClickedBlock().getLocation().getBlockY();
	    			int blockZ = e.getClickedBlock().getLocation().getBlockZ();
	    			for(int z=1; z<TentsLocConf.getCustomConfig().getConfigurationSection("").getKeys(false).size() + 1; z++){
	    				if(TentsLocConf.getCustomConfig().getString(z + ".Location").equals(blockX+","+blockY+","+blockZ)){ 
	    					if(player.getItemInHand().getType().equals(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")))){
	    						player.sendMessage(ChatColor.RED + "Please pickup the tent with a different item!");
	    						return;
	    					}
	    					if(!TentsLocConf.getCustomConfig().getBoolean(z+".Destroyed") && (player.isOp() ||  TentsLocConf.getCustomConfig().getString(z + ".Owner") == player.getName())){
	    			            World world = Bukkit.getWorld(TentsLocConf.getCustomConfig().getString(z + ".World"));
	    			            String Building = TentsLocConf.getCustomConfig().getString(z + ".Tent");
	    			            int count = 0;
	    			            for (int i = 0; i < player.getInventory().getSize(); i++) {
	    			                if (player.getInventory().getContents()[i] != null){
	    			                    count++;
	    			                }
	    			            }
	    			            if(count <= 0){
	    			            	player.sendMessage(ChatColor.RED + "You don't have enough space in your inventory!");
	    			            	return;
	    			            }
	    			            for(int b=0; b<TentsConf.getCustomConfig().getConfigurationSection(Building+".Schema").getKeys(false).size(); b++){
	    							List<String> list = (List<String>) TentsConf.getCustomConfig().getList(Building+".Schema."+b);
	    							for(int i=0; i<list.size();i++) {
	    					            String[] split = list.get(i).split(",");
	    					            for(int a=0; a<split.length;a++){
	    					            	if(!split[a].equals("-")){
	    					            		world.getBlockAt(blockX + a, blockY + b - 1, blockZ + i).setType(Material.AIR);
	    					            	}
	    					            }
	    					        }
	    						}
	    						List<String> list = (List<String>) TentsLocConf.getCustomConfig().getList(z+".Floor");
	    						for(int i1=0; i1<list.size();i1++) {
	    							String[] split = list.get(i1).split(",");
	    					        for(int a1=0; a1<split.length;a1++){
	    					        	world.getBlockAt(blockX + a1, blockY - 1, blockZ + i1).setTypeId(Integer.valueOf(split[a1]));
	    					        }
	    					    }
	    			            TentsLocConf.getCustomConfig().set(z+".Destroyed", Boolean.valueOf(true));
	    			            TentsLocConf.saveCustomConfig();
	    			            
	    			            if (ConfigConf.getCustomConfig().getBoolean("ProtectTents")) {
	    			                PortableTent.worldguardPlugin.getRegionManager(world).removeRegion(z + "_" + TentsLocConf.getCustomConfig().getString(z + ".Owner"));
	    			                PortableTent.worldguardPlugin.saveConfig();
	    			                try {
	    			                  PortableTent.worldguardPlugin.getRegionManager(world).save();
	    			                }
	    			                catch (ProtectionDatabaseException e1) {
	    			                  e1.printStackTrace();
	    			                }
	    			            }
	    			            
	    			            ItemStack i = new ItemStack(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")), 1);
	    				        ItemMeta meta = i.getItemMeta();
	    				        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TentsConf.getCustomConfig().getString(Building +".Name")));
	    				        List<String> splitl = (List<String>) TentsConf.getCustomConfig().getList(Building+".Description");
	    				        ArrayList<String> Lore = new ArrayList<String>();
	    				        for(String s : splitl){
	    				        	Lore.add(ChatColor.translateAlternateColorCodes('&', s));
	    				        }
	    				        meta.setLore(Lore);
	    				        i.setItemMeta(meta);
	    				        player.getInventory().addItem(i);
	    				        player.updateInventory();
	    				        if(ConfigConf.getCustomConfig().getBoolean("LimitedTents.Enabled")){
	    				        	PlayersConf.getCustomConfig().set(player.getName()+".TentsAmount", PlayersConf.getCustomConfig().getInt(player.getName()+".TentsAmount") + 1);
	    				        	PlayersConf.saveCustomConfig();
	    				        }
	    				        player.sendMessage(ChatColor.GREEN + "You have picked up a tent!");
	    				        return;
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	}
}

package me.vasil7112.PortableTent.Command;

import java.util.ArrayList;
import java.util.List;

import me.vasil7112.PortableTent.PortableTent;
import me.vasil7112.PortableTent.Configuration.ConfigConf;
import me.vasil7112.PortableTent.Configuration.PlayersConf;
import me.vasil7112.PortableTent.Configuration.TentsConf;
import me.vasil7112.PortableTent.Configuration.TentsLocConf;
import me.vasil7112.PortableTent.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;

public class PortableTentCMD implements CommandExecutor {
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("PortableTent")){
			if(args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
				Help(sender);
				return true;
			}else if(args[0].equalsIgnoreCase("credits")){
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GREEN+""+ChatColor.STRIKETHROUGH+""+ChatColor.BOLD+"☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰"+"\n"+
								   ChatColor.GOLD+"Vasil7112 "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Leader Developer, Author"+"\n"+
								   ChatColor.GREEN+""+ChatColor.STRIKETHROUGH+""+ChatColor.BOLD+"☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰");
				return true;
			}else if(args[0].equalsIgnoreCase("wand")){
				if(sender.hasPermission("PortableTent.create")){
					ItemStack i = new ItemStack(Material.DIAMOND_HOE, 1);
					ItemMeta meta = i.getItemMeta();
					meta.setDisplayName(ChatColor.YELLOW + "PortableTent Wand!");
					ArrayList<String> Lore = new ArrayList<String>();
					Lore.add(ChatColor.RED+"Use this to create a blueprint tent!");
					Lore.add(ChatColor.GOLD+"Just select the 2 points");
					Lore.add(ChatColor.GOLD+"by Left-Right Clicking, ");
					Lore.add(ChatColor.GOLD+"and use the command ");
					Lore.add(ChatColor.RED+"/portabletent save <TentName>");
					meta.setLore(Lore);
					i.setItemMeta(meta);
					Bukkit.getServer().getPlayerExact(sender.getName()).getInventory().addItem(i);
					return true;
				}else{
					sender.sendMessage(ChatColor.RED + "You don't have enough permissions to obtain a portabletent wand!");
				}
			}else if(args[0].equalsIgnoreCase("reload")){
				if(sender.hasPermission("PortableTent.reload")){
					TentsConf.reloadCustomConfig();
					ConfigConf.reloadCustomConfig();
					TentsLocConf.reloadCustomConfig();
					sender.sendMessage(ChatColor.GREEN + "Reloaded PortableTent!");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("save")){
				if(args.length < 2){
					sender.sendMessage(ChatColor.RED + "Wrong format used! Correct one is:\n" +
							 		   ChatColor.RED + "/portabletent save <TentName>");
					return true;
				}
				if(!sender.hasPermission("PortableTent.create")){
					sender.sendMessage(ChatColor.RED + "You don't have enough permissions to use this command!");
					return true;
				}
				if(PlayerListener.point1 ==null || PlayerListener.point2 ==null){
					sender.sendMessage(ChatColor.RED + "Please setup the points correctly");
					return true;
				}
				if(TentsConf.getCustomConfig().contains(args[1])){
					sender.sendMessage(ChatColor.RED + "Tent already exist");
					return true;
				}
				int point1X = PlayerListener.point1.getBlockX();
		        int point1Y = PlayerListener.point1.getBlockY();
		        int point1Z = PlayerListener.point1.getBlockZ();
		        int point2X = PlayerListener.point2.getBlockX();
		        int point2Y = PlayerListener.point2.getBlockY();
		        int point2Z = PlayerListener.point2.getBlockZ();
		        World world = PlayerListener.point1.getWorld();
		        int width = 0;
		        int height = 0;
		        int length = 0;
		        int X = 0;
		        int Y = 0;
		        int Z = 0;
		        int minX = 0;
		        int minY = 0;
		        int minZ = 0;

		        if(point1X > point2X){
		            width = point1X - point2X;
		            minX = point2X;
		        }else{
		            width = point2X - point1X;
		            minX = point1X;
		        }
		        if (point1Y > point2Y){
		            height = point1Y - point2Y;
		            minY = point2Y;
		        }else{
		            height = point2Y - point1Y;
		            minY = point1Y;
		        }
		        if(point1Z > point2Z){
		            length = point1Z - point2Z;
		            minZ = point2Z;
		        }else{
		            length = point2Z - point1Z;
		            minZ = point1Z;
		        }
		          
		        for(Y = 0; Y <= height; Y++){
		            List<String> blocks = new ArrayList<String>();
		            for(Z = 0; Z <= length; Z++){
		            	String a = "";
		            	for (X = 0; X <= width; X++) {
		            		Location block = new Location(world, minX+X,minY+Y, minZ+Z);
		            		String id = null;
		            		if(block.getBlock().getData() != 0){
		            			id = block.getBlock().getTypeId()+":"+block.getBlock().getData();
		            		}else{
		            			id = block.getBlock().getTypeId()+"";
		            		}
		            		a = a+","+id;
		            	}
		                a = a.substring(1);
		                blocks.add(a);
		            }
		            TentsConf.getCustomConfig().set(args[1]+".Schema."+Y, blocks);
		        }
		        TentsConf.getCustomConfig().set(args[1]+".Name", "&3"+args[1]);
		        ArrayList<String> Lore = new ArrayList<String>();
		        Lore.add("&3A new tent!");
		        Lore.add("&2Created by &4"+sender.getName());
		        TentsConf.getCustomConfig().set(args[1]+".Description", Lore);
		        TentsConf.getCustomConfig().set(args[1]+".Recipe", "NONE");
		        TentsConf.saveCustomConfig();
		        sender.sendMessage(ChatColor.GREEN+args[1]+" blueprint was created and saved!");  
		        return true;
			}else if(args[0].equalsIgnoreCase("delete")){
				if(args.length < 2){
					sender.sendMessage(ChatColor.RED + "Wrong format used! Correct one is:\n" +
							 		   ChatColor.RED + "/portabletent delete <TentName>");
					return true;
				}
				if(!sender.hasPermission("PortableTent.delete")){
					sender.sendMessage(ChatColor.RED + "You don't have enough permissions to use this command!");
					return true;
				}
				if(!TentsConf.getCustomConfig().contains(args[1])){
					sender.sendMessage(ChatColor.RED + "Tent doesn't exist!");
				}
				TentsConf.getCustomConfig().set(args[1], null);
				TentsConf.saveCustomConfig();
				sender.sendMessage(ChatColor.GREEN + args[1] + " was deleted!");
				return true;
			}else if(args[0].equalsIgnoreCase("give")){
				if(args.length < 3){
					sender.sendMessage(ChatColor.RED + "Wrong format used! Correct one is:\n" +
							 		   ChatColor.RED + "/portabletent give <Player> <TentName> <Amount>");
					return true;
				}
				
				if(!(sender instanceof ConsoleCommandSender)){
					if(!sender.hasPermission("PortableTent.give")){
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigConf.getCustomConfig().getString("Language.Give-No-Perm")));
						return true;
					}
				}
				if(Bukkit.getServer().getPlayerExact(args[1])==null){
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigConf.getCustomConfig().getString("Language.Give-Not-Online")));
					return true;
				}
				if(!TentsConf.getCustomConfig().contains(args[2])){
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigConf.getCustomConfig().getString("Language.Give-Not-Exist")));
					return true;
				}
				ItemStack i;
				if(args.length == 3){
					i = new ItemStack(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")), 1);
				}else{
					i = new ItemStack(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")), Integer.valueOf(args[3]));
				}
				ItemMeta meta = i.getItemMeta();
			    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TentsConf.getCustomConfig().getString(args[2]+".Name")));
			    List<String> splitl = (List<String>) TentsConf.getCustomConfig().getList(args[2]+".Description");
			    ArrayList<String> Lore = new ArrayList<String>();
			    for(String s : splitl){
			    	Lore.add(ChatColor.translateAlternateColorCodes('&', s));
			    }
			    meta.setLore(Lore);
			    i.setItemMeta(meta);
			    Bukkit.getServer().getPlayerExact(args[1]).getInventory().addItem(i);
			    Bukkit.getServer().getPlayerExact(args[1]).updateInventory();
			    return true;
			}else if(args[0].equalsIgnoreCase("destroy")){
				if(args.length < 3){
					sender.sendMessage(ChatColor.RED + "Wrong format used! Correct one is:\n" +
							 		   ChatColor.RED + "/portabletent destroy player <PlayerName> <*/TentName>\n"+
									   ChatColor.RED + "/portabletent destroy tent <TentName>");
					return true;
				}
				if(!sender.hasPermission("PortableTent.destroy")){
					sender.sendMessage(ChatColor.RED + "You don't have enough permissions to use this command");
					return true;
				}
				if(args[1].equalsIgnoreCase("player")){
					if(args.length < 4){
						sender.sendMessage(ChatColor.RED + "Wrong format used! Correct one is:\n" +
								 		   ChatColor.RED + "/portabletent destroy player <PlayerName> <*/TentName>");
					}
					if(args[3].equalsIgnoreCase("*")){
						for(int z=1; z<TentsLocConf.getCustomConfig().getConfigurationSection("").getKeys(false).size() + 1; z++){
							if(TentsLocConf.getCustomConfig().getString(z + ".Owner").equals(args[2]) && !TentsLocConf.getCustomConfig().getBoolean(z + ".Destroyed")){
								String Building = TentsLocConf.getCustomConfig().getString(z + ".Tent");
								World world = Bukkit.getWorld(TentsLocConf.getCustomConfig().getString(z + ".World"));
								String[] splita = TentsLocConf.getCustomConfig().getString(z + ".Location").split(",");
								int blockX = Integer.valueOf(splita[0]);
				    			int blockY = Integer.valueOf(splita[1]);
				    			int blockZ = Integer.valueOf(splita[2]);
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
	    			                PortableTent.worldguardPlugin.getRegionManager(world).removeRegion(z + "_" + args[2]);
	    			                PortableTent.worldguardPlugin.saveConfig();
	    			                try {
	    			                  PortableTent.worldguardPlugin.getRegionManager(world).save();
	    			                }
	    			                catch (ProtectionDatabaseException e1) {
	    			                  e1.printStackTrace();
	    			                }
	    			            }
	    			            
	    			            if(ConfigConf.getCustomConfig().getBoolean("LimitedTents.Enabled")){
	    				        	PlayersConf.getCustomConfig().set(args[2]+".TentsAmount", PlayersConf.getCustomConfig().getInt(args[2]+".TentsAmount") + 1);
	    						}
	    			            PlayersConf.saveCustomConfig();
							}
						}
						sender.sendMessage(ChatColor.GREEN+"Cleared "+args[2]+"'s tents!");
						return true;
					}else if(!args[3].equalsIgnoreCase("*")){
						if(TentsConf.getCustomConfig().contains(args[3])){
							for(int z=1; z<TentsLocConf.getCustomConfig().getConfigurationSection("").getKeys(false).size() + 1; z++){
								if(TentsLocConf.getCustomConfig().getString(z + ".Owner").equals(args[2]) && TentsLocConf.getCustomConfig().getString(z + ".Tent").equals(args[3]) && !TentsLocConf.getCustomConfig().getBoolean(z + ".Destroyed")){
									String Building = TentsLocConf.getCustomConfig().getString(z + ".Tent");
									World world = Bukkit.getWorld(TentsLocConf.getCustomConfig().getString(z + ".World"));
									String[] splita = TentsLocConf.getCustomConfig().getString(z + ".Location").split(",");
									int blockX = Integer.valueOf(splita[0]);
				    				int blockY = Integer.valueOf(splita[1]);
				    				int blockZ = Integer.valueOf(splita[2]);
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
	    			                	PortableTent.worldguardPlugin.getRegionManager(world).removeRegion(z + "_" + args[2]);
	    			                	PortableTent.worldguardPlugin.saveConfig();
	    			                	try {
	    			                		PortableTent.worldguardPlugin.getRegionManager(world).save();
	    			                	}catch (ProtectionDatabaseException e1) {
	    			                		e1.printStackTrace();
	    			                	}
	    			            	}
	    			            
	    			            	if(ConfigConf.getCustomConfig().getBoolean("LimitedTents.Enabled")){
	    				        		PlayersConf.getCustomConfig().set(args[2]+".TentsAmount", PlayersConf.getCustomConfig().getInt(args[2]+".TentsAmount") + 1);
	    							}
	    			            	PlayersConf.saveCustomConfig();
								}
							}
							sender.sendMessage(ChatColor.GREEN+"Cleared "+args[2]+"'s "+args[3]+" tents!");
						}else{
							sender.sendMessage(ChatColor.RED+"This tent doesn't exist!");
						}
						return true;
					}
				}else if(args[1].equalsIgnoreCase("tent")){
					if(args.length < 3){
						sender.sendMessage(ChatColor.RED + "Wrong format used! Correct one is:\n" +
								   		   ChatColor.RED + "/portabletent destroy tent <TentName>");
						return true;
					}
					if(TentsConf.getCustomConfig().contains(args[2])){
						for(int z=1; z<TentsLocConf.getCustomConfig().getConfigurationSection("").getKeys(false).size() + 1; z++){
							if(TentsLocConf.getCustomConfig().getString(z + ".Tent").equals(args[2]) && !TentsLocConf.getCustomConfig().getBoolean(z + ".Destroyed")){
								String Building = TentsLocConf.getCustomConfig().getString(z + ".Tent");
								World world = Bukkit.getWorld(TentsLocConf.getCustomConfig().getString(z + ".World"));
								String[] splita = TentsLocConf.getCustomConfig().getString(z + ".Location").split(",");
								int blockX = Integer.valueOf(splita[0]);
			    				int blockY = Integer.valueOf(splita[1]);
			    				int blockZ = Integer.valueOf(splita[2]);
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
    			                	}catch (ProtectionDatabaseException e1) {
    			                		e1.printStackTrace();
    			                	}
    			            	}
    			            
    			            	if(ConfigConf.getCustomConfig().getBoolean("LimitedTents.Enabled")){
    				        		PlayersConf.getCustomConfig().set(TentsLocConf.getCustomConfig().getString(z + ".Owner")+".TentsAmount", PlayersConf.getCustomConfig().getInt(TentsLocConf.getCustomConfig().getString(z + ".Owner")+".TentsAmount") + 1);
    							}
    			            	PlayersConf.saveCustomConfig();
							}
						}
						sender.sendMessage(ChatColor.GREEN+"Cleared "+args[2]+" tents!");
					}else{
						sender.sendMessage(ChatColor.RED+"This tent doesn't exist!");
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private void Help(CommandSender sender){
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GREEN+""+ChatColor.STRIKETHROUGH+""+ChatColor.BOLD+"☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Display Help"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.RED+"give <Player> <TentName> [Amount] "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Give tents to someone"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.RED+"save <TentName> "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Save blueprint of a tent"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.RED+"delete <TentName> "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Delete tent from config file"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.RED+"destroy Player <PlayerName> [*/TentName] "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Destroy already placed tents by a player!"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.RED+"destroy Tent <TentName>"+ChatColor.AQUA+"-"+ChatColor.WHITE+" Destroy already placed tents!"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.RED+"wand "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Get the magic wand"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.RED+"reload "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Reload Configs"+"\n"+
				   		   ChatColor.GOLD+"/PortableTent "+ChatColor.RED+"credits "+ChatColor.AQUA+"-"+ChatColor.WHITE+" Display Credits"+"\n"+
				   		   ChatColor.GREEN+""+ChatColor.STRIKETHROUGH+""+ChatColor.BOLD+"☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰☰");
	}
}

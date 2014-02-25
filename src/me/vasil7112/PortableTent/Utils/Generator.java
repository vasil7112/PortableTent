package me.vasil7112.PortableTent.Utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import me.vasil7112.PortableTent.PortableTent;
import me.vasil7112.PortableTent.Configuration.ConfigConf;
import me.vasil7112.PortableTent.Configuration.PlayersConf;
import me.vasil7112.PortableTent.Configuration.TentsConf;
import me.vasil7112.PortableTent.Configuration.TentsLocConf;

public class Generator {

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static Boolean GenerateBuilding(String Building, Player player, Location bloc){
		if(ConfigConf.getCustomConfig().getBoolean("User-Perms") == true && !player.hasPermission("PortableTent.use")){
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigConf.getCustomConfig().getString("Language.No-Perms")));
			return false;
		}
		if(ConfigConf.getCustomConfig().getBoolean("LimitedTents.Enabled")){
			if(PlayersConf.getCustomConfig().getInt(player.getName()+".TentsAmount") < 0){
				player.sendMessage(ChatColor.RED + "You have exceeded the max tents that you can place!");
				return false;
			}
		}
		if(TentsConf.getCustomConfig().contains(Building)){
			if(TentsConf.getCustomConfig().contains(Building+".Schema.")){
				if(TentsConf.getCustomConfig().getList(Building+".Schema.1") !=null){
					Location ploc = bloc;
					ploc.setX(ploc.getX() + 1);
					World world = player.getWorld();
					if(ConfigConf.getCustomConfig().getBoolean("Per-Tent-Perms") == true && (!player.hasPermission("PortableTent.tent."+Building) || !player.hasPermission("PortableTent.tent.*"))){
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',ConfigConf.getCustomConfig().getString("Language.No-Tent-Perms")));
						return false;
					}
					for(int b=0; b<TentsConf.getCustomConfig().getConfigurationSection(Building+".Schema").getKeys(false).size(); b++){
						List<String> list = (List<String>) TentsConf.getCustomConfig().getList(Building+".Schema."+b);
						for(int i=0; i<list.size();i++) {
				            String[] split = list.get(i).split(",");
				            for(int a=0; a<split.length;a++){
				            	if(b!=0 && world.getBlockAt(ploc.getBlockX() + a, ploc.getBlockY() + b - 1, ploc.getBlockZ() + i).getType() != Material.AIR){
				            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigConf.getCustomConfig().getString("Language.No-Build")));
				            		return false;
				            	}
				            	if(PortableTent.worldguardPlugin !=null){
				            		if(!PortableTent.worldguardPlugin.canBuild(player, new Location(player.getWorld(), ploc.getBlockX() + a, ploc.getBlockY() + b - 1, ploc.getBlockZ() + i))){
				            			return false;
				            		}
				            	}
				            	if(PortableTent.factions !=null){
				            		if(!Factions.canBuild(player, new Location(player.getWorld(), ploc.getBlockX() + a, ploc.getBlockY() + b - 1, ploc.getBlockZ() + i))){
				            			return false;
				            		}
				            	}
				            }
				        }
					}
					int size = TentsLocConf.getCustomConfig().getConfigurationSection("").getKeys(false).size() + 1;
					List<String> list1 = (List<String>) TentsConf.getCustomConfig().getList(Building+".Schema.0");
					List<String> blocks = new ArrayList<String>();
					for(int i1=0; i1<list1.size();i1++) {
						String[] split = list1.get(i1).split(",");
				        String blocks2 = null;
				        for(int a1=0; a1<split.length;a1++){
				        	blocks2 = blocks2 + "," +world.getBlockAt(ploc.getBlockX() + a1, ploc.getBlockY() - 1, ploc.getBlockZ() + i1).getTypeId();
				        }
				        blocks2 = blocks2.substring(5);
				        blocks.add(blocks2);
				     }
					TentsLocConf.getCustomConfig().set(size+".Floor", blocks);
					int x = 0;
					int y = 0;
					for(int b=0; b<TentsConf.getCustomConfig().getConfigurationSection(Building+".Schema").getKeys(false).size(); b++){
						List<String> list = (List<String>) TentsConf.getCustomConfig().getList(Building+".Schema."+b);
						if(list.size() > x){
							x = list.size();
						}
						for(int i=0; i<list.size();i++) {
				            String[] split = list.get(i).split(",");
				            if(split.length > y){
								y = split.length;
							}
				            for(int a=0; a<split.length;a++){
				            	if(!split[a].equals("-")){
				            		if(split[a].contains(":")){
				            			String[] finalsplit = split[a].split(":");
				            			world.getBlockAt(ploc.getBlockX() + a, ploc.getBlockY() + b - 1, ploc.getBlockZ() + i).setTypeIdAndData(Integer.valueOf(finalsplit[0]), Byte.valueOf(finalsplit[1]), true);
				            		}else{
				            			world.getBlockAt(ploc.getBlockX() + a, ploc.getBlockY() + b - 1, ploc.getBlockZ() + i).setTypeId(Integer.valueOf(split[a]));
				            		}
				            	}
				            }
				        }
					}
					
					bloc.getBlock().setType(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")));
					TentsLocConf.getCustomConfig().set(size + ".Location", bloc.getBlockX()+","+bloc.getBlockY()+","+bloc.getBlockZ());
					TentsLocConf.getCustomConfig().set(size + ".Destroyed", Boolean.valueOf(false));
					TentsLocConf.getCustomConfig().set(size + ".Owner", String.valueOf(player.getName()));
					TentsLocConf.getCustomConfig().set(size + ".World", String.valueOf(player.getWorld().getName()));
					TentsLocConf.getCustomConfig().set(size + ".Tent", String.valueOf(Building));
					TentsLocConf.saveCustomConfig();
					Location l1 = new Location(player.getWorld(), bloc.getBlockX(), bloc.getBlockY() - 1, bloc.getBlockZ());
			        Location l2 = new Location(player.getWorld(), bloc.getBlockX() + y, bloc.getBlockY() + TentsConf.getCustomConfig().getConfigurationSection(Building + ".Schema").getKeys(false).size(), bloc.getBlockZ() + x);
			        if(ConfigConf.getCustomConfig().getBoolean("ProtectTents")){
			            ProtectedCuboidRegion pr = new ProtectedCuboidRegion(size + "_" + player.getName(), WorldEdit.convertToSk89qBV(l1), WorldEdit.convertToSk89qBV(l2));
			            pr.setFlag(new StateFlag("build", false), StateFlag.State.DENY);
			            pr.setFlag(new StateFlag("chest-access", true), StateFlag.State.ALLOW);
			            pr.setFlag(new StateFlag("fire-spread", false), StateFlag.State.DENY);
			            pr.setFlag(new StateFlag("lava-fire", false), StateFlag.State.DENY);
			            PortableTent.worldguardPlugin.getRegionManager(player.getWorld()).addRegion(pr);
			            PortableTent.worldguardPlugin.saveConfig();
			            try {
			              PortableTent.worldguardPlugin.getRegionManager(player.getWorld()).save();
			            }
			            catch (ProtectionDatabaseException e) {
			              e.printStackTrace();
			            }
			        }
			        if(ConfigConf.getCustomConfig().getBoolean("LimitedTents.Enabled")){
			        	PlayersConf.getCustomConfig().set(player.getName()+".TentsAmount", PlayersConf.getCustomConfig().getInt(player.getName()+".TentsAmount") - 1);
					}
					return true;
				}
			}else{
				PortableTent.plugin.getLogger().warning(Building + " Schema doesn't exist!");
				return false;
			}
		}else{
			PortableTent.plugin.getLogger().warning("Oh no! Building "+Building+" doesn't exist!");
			return false;
		}
		return false;
	}
}

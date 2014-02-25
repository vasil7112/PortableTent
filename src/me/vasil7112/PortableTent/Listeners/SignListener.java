package me.vasil7112.PortableTent.Listeners;

import java.util.ArrayList;
import java.util.List;

import me.vasil7112.PortableTent.PortableTent;
import me.vasil7112.PortableTent.Configuration.ConfigConf;
import me.vasil7112.PortableTent.Configuration.TentsConf;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SignListener implements Listener{

	@EventHandler
    public void blockPlaced(final SignChangeEvent e) {
        String[] a= e.getLines();
        Player player = e.getPlayer();
        if(a[0].equalsIgnoreCase("[PTENT]")) {
        	if(ConfigConf.getCustomConfig().getBoolean("User-Perms") == true && !player.hasPermission("PortableTent.signs.create")){
    			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigConf.getCustomConfig().getString("Language.No-Perms")));
    			return;
        	}
            if(PortableTent.vault == null){
            	e.setLine(0, "[PTENT]");
            	e.setLine(1, "Please install");
            	e.setLine(2, "Vault for the");
            	e.setLine(3, "signs to work");
            	e.getBlock().getState().update();
            }else if(PortableTent.economy == null){
            	e.setLine(0, "[PTENT]");
            	e.setLine(1, "No economy");
            	e.setLine(2, "plugin was");
            	e.setLine(3, "found!");
            	e.getBlock().getState().update();
            }else{
            	if(!((a[1].equalsIgnoreCase("Buy") || a[1].equalsIgnoreCase("Sell")) && TentsConf.getCustomConfig().contains(a[2]) && a[3] !=null)){
            		e.setLine(0, "[PTENT]");
                	e.setLine(1, "You used");
                	e.setLine(2, "wrong format");
                	e.getBlock().getState().update();
            	}else{
            		e.setLine(0, "[PTENT]");
            		e.getBlock().getState().update();
            	}
            }
        }
    }
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignClick(PlayerInteractEvent e) {
	    Player player = e.getPlayer();
	    if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if(b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
            	Sign sign = (Sign) b.getState();
                String[] lines = sign.getLines();
                if(lines[0].equalsIgnoreCase("[PTENT]")){
	            	if(PortableTent.economy == null){
	            		return;
	            	}
	            	if(lines[1].equalsIgnoreCase("Buy") && player.hasPermission("PortableTent.signs.buy")){
	            		e.setCancelled(true);
	            		EconomyResponse with = PortableTent.economy.withdrawPlayer(player.getName(), Integer.valueOf(lines[3]));
	                    if(with.transactionSuccess()){
	                    	ItemStack i = new ItemStack(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")), 1);
	    			        ItemMeta meta = i.getItemMeta();
	    			        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TentsConf.getCustomConfig().getString(lines[2]+".Name")));
	    			        List<String> splitl = (List<String>) TentsConf.getCustomConfig().getList(lines[2]+".Description");
	    			        ArrayList<String> Lore = new ArrayList<String>();
	    			        for(String s2 : splitl){
	    			        	Lore.add(ChatColor.translateAlternateColorCodes('&', s2));
	    			        }
	    			        meta.setLore(Lore);
	    			        i.setItemMeta(meta);
	    			        player.getInventory().addItem(i);
	    			        player.updateInventory();
	                    	player.sendMessage(ChatColor.GREEN + "" + Integer.valueOf(lines[3]) + " was taken from your account. You now have " + with.balance + " in your account.");
	                    }else{
	                    	player.sendMessage(ChatColor.RED + String.format("%s", new Object[] { with.errorMessage }));
	                    }
	            	}else if(lines[1].equalsIgnoreCase("Sell") && player.hasPermission("PortableTent.signs.sell")){
	            		e.setCancelled(true);
	            		ItemStack i = new ItemStack(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")), 1);
    			        ItemMeta meta = i.getItemMeta();
    			        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TentsConf.getCustomConfig().getString(lines[2]+".Name")));
    			        List<String> splitl = (List<String>) TentsConf.getCustomConfig().getList(lines[2]+".Description");
    			        ArrayList<String> Lore = new ArrayList<String>();
    			        for(String s2 : splitl){
    			        	Lore.add(ChatColor.translateAlternateColorCodes('&', s2));
    			        }
    			        meta.setLore(Lore);
    			        i.setItemMeta(meta);
    			       
    			        if(player.getInventory().contains(i)){
    			        	EconomyResponse depo = PortableTent.economy.depositPlayer(player.getName(), Integer.valueOf(lines[3]));
    			        	if(depo.transactionSuccess()){
    			        		player.getInventory().removeItem(i);
    			        		player.updateInventory();
    			        		player.sendMessage(ChatColor.GREEN + "" + Integer.valueOf(lines[3]) + " was added to your account. You now have " + depo.balance + " in your account.");
		                	}else{
		                		player.sendMessage(ChatColor.RED + String.format("%s", new Object[] { depo.errorMessage }));
		                	}
    			        }else{
    			        	player.sendMessage(ChatColor.RED + "You don't have this tent to sell!");
    			        }
	            	}
	            }
            }
	    }
	}
	
}

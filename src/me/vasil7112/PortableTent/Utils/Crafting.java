package me.vasil7112.PortableTent.Utils;

import java.util.ArrayList;
import java.util.List;

import me.vasil7112.PortableTent.Configuration.ConfigConf;
import me.vasil7112.PortableTent.Configuration.TentsConf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Crafting {

	@SuppressWarnings("unchecked")
	private static void addCraftingRecipe(String Building) { 
		if(TentsConf.getCustomConfig().getString(Building+".Recipe").equalsIgnoreCase("NONE")){
			return;
		}
		String[] split = TentsConf.getCustomConfig().getString(Building+".Recipe").split(",");
        ItemStack i = new ItemStack(Material.getMaterial(ConfigConf.getCustomConfig().getString("TentBlock")), 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TentsConf.getCustomConfig().getString(Building+".Name")));
        List<String> splitl = (List<String>) TentsConf.getCustomConfig().getList(Building+".Description");
        ArrayList<String> Lore = new ArrayList<String>();
        for(String s : splitl){
        	Lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(Lore);
        i.setItemMeta(meta);
        ShapedRecipe g = new ShapedRecipe(i);
        g.shape("ABC", "DEF", "GHI");
        g.setIngredient('A', Material.getMaterial(split[0]));
        g.setIngredient('B', Material.getMaterial(split[1]));
        g.setIngredient('C', Material.getMaterial(split[2]));
        g.setIngredient('D', Material.getMaterial(split[3]));
        g.setIngredient('E', Material.getMaterial(split[4]));
        g.setIngredient('F', Material.getMaterial(split[5]));
        g.setIngredient('G', Material.getMaterial(split[6]));
        g.setIngredient('H', Material.getMaterial(split[7]));
        g.setIngredient('I', Material.getMaterial(split[8]));
        Bukkit.addRecipe(g);
    }
	
	public static void addCraftingRecipes(){
		for(String tent : TentsConf.getCustomConfig().getConfigurationSection("").getKeys(false)){
			addCraftingRecipe(tent);
		}
	}
}

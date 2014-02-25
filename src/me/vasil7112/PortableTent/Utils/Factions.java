package me.vasil7112.PortableTent.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

public class Factions {

	public static Boolean canBuild(Player player, Location location){
		Faction faction = BoardColls.get().getFactionAt(PS.valueOf(location));
		if(!ChatColor.stripColor(faction.getName()).equals("Wilderness")){
			if(!UPlayer.get(player).isInOwnTerritory()){
				return false;
			}
		}
		return true;
	}
}

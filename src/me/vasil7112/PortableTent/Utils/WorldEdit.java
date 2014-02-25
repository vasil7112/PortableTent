package me.vasil7112.PortableTent.Utils;

import org.bukkit.Location;

import com.sk89q.worldedit.BlockVector;

public class WorldEdit {

	public static BlockVector convertToSk89qBV(Location location){
		return new BlockVector(location.getX(), location.getY(), location.getZ());
	}
	
}

package pl.mccode.privateproperty.util;

import org.bukkit.Location;
import pl.mccode.privateproperty.Main;

public class LocationUtils {
	public static String stringFromLocation(Location location){
		return location.getWorld().getName() + ":"
				+ location.getBlockX() + ":"
				+ location.getBlockY() + ":"
				+ location.getBlockZ();
	}

	public static Location LocationFromString(String string){
		String[] args = string.split(":");
		return new Location(
				Main.getInstance().getServer().getWorld(args[0]),
				Double.parseDouble(args[1]),
				Double.parseDouble(args[2]),
				Double.parseDouble(args[3]));
	}
}

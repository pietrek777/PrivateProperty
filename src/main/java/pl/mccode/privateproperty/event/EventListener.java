package pl.mccode.privateproperty.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.mccode.privateproperty.protect.Protection;

public class EventListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent event){
		try{
			Protection.createProtection(event);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_AIR) {
			Protection.checkPermissions(event);
		}
	}
}

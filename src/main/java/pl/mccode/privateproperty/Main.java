package pl.mccode.privateproperty;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mccode.privateproperty.config.ConfigProvider;
import pl.mccode.privateproperty.event.EventListener;
import pl.mccode.privateproperty.protect.ProtectedResource;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
	public static final String RAW_PLUGIN_PREFIX = "[PrivateProperty] ";
	public static final String PLUGIN_PREFIX = ChatColor.translateAlternateColorCodes('&', "&2[PrivateProperty]&f ");



	private static Main instance;
	private static Logger logger = Logger.getLogger("Minecraft");



	public static Logger getLoggerInstance(){
		return logger;
	}



	public static Main getInstance() {
		return instance;
	}
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		logger.info(RAW_PLUGIN_PREFIX + "Plugin has been enabled");
		instance = this;
		createConfigFiles();
		ConfigurationSerialization.registerClass(ProtectedResource.class);
	}
	@Override
	public void onDisable() {
		logger.info(RAW_PLUGIN_PREFIX + "Plugin has been disabled");
	}
	private void createConfigFiles(){
		ConfigProvider.init(this);
	}

	public void printInfo(String message){
		getLogger().info(RAW_PLUGIN_PREFIX + message);
	}
	public void printError(String message){
		getLogger().severe(RAW_PLUGIN_PREFIX + message);
	}
}


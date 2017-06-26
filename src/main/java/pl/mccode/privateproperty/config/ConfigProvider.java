package pl.mccode.privateproperty.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mccode.privateproperty.protect.ProtectedResource;
import pl.mccode.privateproperty.protect.Protection;

import java.io.*;
import java.util.ArrayList;

public class ConfigProvider {
	private static ConfigProvider instance = null;

	public static final String CONFIG_FILE_NAME = "config.yml";
	public static final String PROTECTED_FILE_NAME = "protected.yml";

	public static final String PROTECTED_BLOCKS_KEY = "protected-blocks";

	public static File protectedConfigFile = null;

	private FileConfiguration configFileConfiguration = null, protectedFileConfiguration = null;
	private JavaPlugin plugin;

	public FileConfiguration getConfigFileConfiguration(){
		return configFileConfiguration;
	}
	public FileConfiguration getProtectedFileConfiguration(){
		return protectedFileConfiguration;
	}
	public static void init(JavaPlugin plugin){
		instance = new ConfigProvider(plugin);
	}

	public static ConfigProvider getInstance() {
		return instance;
	}

	private ConfigProvider(JavaPlugin plugin){
		this.plugin = plugin;
		createDefaultConfig();
		createProtectedConfig();

	}
	private void createDefaultConfig(){
		configFileConfiguration = plugin.getConfig();
		//TODO Add defaults
		configFileConfiguration.addDefault("protectable-blocks", Protection.PROTECTABLE);

		configFileConfiguration.options().copyDefaults(true);
		plugin.saveConfig();
	}
	private void createProtectedConfig(){
		protectedConfigFile = new File(plugin.getDataFolder(), PROTECTED_FILE_NAME);
		if (!protectedConfigFile.exists()) {
			try {
				protectedConfigFile.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().severe("An exception occured while saving " + PROTECTED_FILE_NAME);
			}
			protectedFileConfiguration = YamlConfiguration.loadConfiguration(protectedConfigFile);
			protectedFileConfiguration.set(PROTECTED_BLOCKS_KEY, new ArrayList<ProtectedResource>());
			saveProtectedConfig();
		}
	}
	public void saveProtectedConfig(){
		try {
			protectedFileConfiguration.save(protectedConfigFile);
		} catch (IOException e) {
			plugin.getLogger().severe("Unable to save file " + protectedConfigFile.getName());
		}
	}
}

package pl.mccode.privateproperty.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mccode.privateproperty.Main;
import pl.mccode.privateproperty.protect.ProtectedResource;
import pl.mccode.privateproperty.protect.Protection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

		configFileConfiguration.options().copyDefaults(true);
		plugin.saveConfig();
	}
	private void createProtectedConfig(){
		protectedConfigFile = new File(plugin.getDataFolder(), PROTECTED_FILE_NAME);
		boolean justCreated = false;
		if (!protectedConfigFile.exists()) {
			try {
				protectedConfigFile.createNewFile();
				justCreated = true;
			} catch (IOException e) {
				plugin.getLogger().severe("An exception occured while saving " + PROTECTED_FILE_NAME);
				return;
			}
		}
		protectedFileConfiguration = YamlConfiguration.loadConfiguration(protectedConfigFile);
		if(justCreated){
			saveProtectedConfig();
			protectedFileConfiguration.set(PROTECTED_BLOCKS_KEY, new ArrayList<ProtectedResource>());
		}
	}
	public void saveProtectedConfig(){
		try {
			protectedFileConfiguration.save(protectedConfigFile);
		} catch (IOException e) {
			plugin.getLogger().severe("Unable to save file " + protectedConfigFile.getName());
		}
	}
	public List<ProtectedResource> getProtectedResourcesList(){
		FileConfiguration configuration = getProtectedFileConfiguration();
		if(configuration!=null){
			Main.getInstance().printInfo("FileConfiguration instance for protected.yml is not null");
			List<?> list = getProtectedFileConfiguration().getList(PROTECTED_BLOCKS_KEY);
			if(list.size() > 0){
				List<ProtectedResource> resourcesObjects = new ArrayList<>();
				  list.forEach(x -> resourcesObjects.add((ProtectedResource) x));
				return resourcesObjects;
			}
			else {
				return null;
			}
		} else {
			Main.getInstance().printError("FileConfiguration instance for protected.yml is null!");
			return null;
		}


	}
}

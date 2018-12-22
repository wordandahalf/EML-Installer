package io.github.wordandahalf.equilinox.modloader.installer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ConfigurationUtils {
	private static HashMap<String, Properties> loadedConfigs = new HashMap<>();
	
	/**
	 * Loads the provided config into memory
	 * @param configFile The file of the config to load
	 * @param configName The config to load
	 */
	public static void loadConfig(File configFile, String configName) {
		Properties config = new Properties();
		
		try {
			config.load(new FileReader(configFile));
			
			loadedConfigs.put(configName, config);
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static boolean isConfigLoaded(String configName) {
		return loadedConfigs.get(configName) != null;
	}
	
	public static void createConfig(File configFile, String configName) {
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		Properties config = new Properties();
		
		loadedConfigs.put(configName, config);
	}
	
	/**
	 * Saves and unloads the provided config
	 * @param configFile The file of the config to load
	 * @param configName The name of the loaded config
	 */
	public static void saveConfig(File configFile, String configName, boolean unload) {
		Properties config = loadedConfigs.get(configName);
		
		if(config != null) {
			try {
				config.store(new FileOutputStream(configFile), "");
				
				if(unload)
					loadedConfigs.remove(configName);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Sets {@code config}'s {@code key} to {@code value}. The provided config must be loaded by {@link ConfigurationUtils#loadConfig(String)} first
	 * @param configName The name of the loaded config
	 * @param key
	 * @param value
	 */
	public static void setValue(String configName, String key, String value) {
		Properties config = loadedConfigs.get(configName);
		
		if(config != null) {
			config.setProperty(key, value);
		}
	}
	
	/**
	 * Returns with the String representation of the value of the config's provided key.
	 * @param configName The name of the loaded config
	 * @param key
	 * @return
	 */
	public static String getValue(String configName, String key) {
		Properties config = loadedConfigs.get(configName);
		
		if(config != null) {
			return config.getProperty(key);
		}
		
		return null;
	}
}

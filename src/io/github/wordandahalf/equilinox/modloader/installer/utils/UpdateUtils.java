package io.github.wordandahalf.equilinox.modloader.installer.utils;

import java.io.IOException;
import java.util.HashMap;

import io.github.wordandahalf.equilinox.modloader.installer.Main;
import io.github.wordandahalf.equilinox.modloader.installer.web.WebFetcher;

public class UpdateUtils {
	public static final String UPDATE_SERVER = "https://wordandahalf.github.io/eml/";
	public static final String UPDATE_FILE = UPDATE_SERVER + "version.txt";
	
	private static HashMap<String, String> serverVersions = new HashMap<>();
	
	public static String getServerEMLVersion() throws IOException { 
		if(serverVersions.keySet().size() == 0)
			fetchVersions();
		
		return serverVersions.get("eml-version");
	}
	
	public static String getServerWrapperVersion() throws IOException { 
		if(serverVersions.keySet().size() == 0)
			fetchVersions();
		
		return serverVersions.get("wrapper-version");
	}
	
	public static HashMap<String, String> fetchVersions() throws IOException {
		HashMap<String, String> map = new HashMap<>();
		String[] config = WebFetcher.asString(UpdateUtils.UPDATE_FILE);
		
		for(String ln : config) {
			String[] array = ln.split("=");
			
			for(int i = 0; i < array.length; i++) {
				map.put(array[i], array[i+1]);
				i++;
			}
		}
		
		serverVersions = map;
		
		return map;
	}
	
	/**
	 * Checks the update server for the most updated version of EML
	 * @return If the local version is out of date, the most up-to-date version, else an empty string
	 * @throws IOException
	 */
	public static String checkForEMLUpdate() throws IOException {
		if (!ConfigurationUtils.isConfigLoaded("local_config"))
			ConfigurationUtils.loadConfig(Main.APPLICATION_CONFIGURATION, "local_config");

		if(serverVersions.keySet().size() == 0)
			fetchVersions();
		
		String localEMLVersion = ConfigurationUtils.getValue("local_config", "eml-version");

		String serverEMLVersion = serverVersions.get("eml-version");
		
		if (isVersionOutOfDate(localEMLVersion, serverEMLVersion))
			return serverEMLVersion;
		
		return "";
	}
	
	/**
	 * Checks the update server for the most updated version of the wrapper
	 * @return If the local version is out of date, the most up-to-date version, else an empty string
	 * @throws IOException
	 */
	public static String checkForWrapperUpdate() throws IOException {
		if (!ConfigurationUtils.isConfigLoaded("local_config"))
			ConfigurationUtils.loadConfig(Main.APPLICATION_CONFIGURATION, "local_config");

		if(serverVersions.keySet().size() == 0)
			fetchVersions();
		
		String serverWrapperVersion = serverVersions.get("wrapper-version");
		
		if (isVersionOutOfDate(Main.APPLICATION_VERSION, serverWrapperVersion))
			return serverWrapperVersion;
		
		return "";
	}

	public static boolean isVersionOutOfDate(String versionOne, String versionTwo) {
		String[] versionOneValues = versionOne.split("\\.");
		String[] versionTwoValues = versionTwo.split("\\.");

		if (versionOneValues.length == versionTwoValues.length) {
			for (int i = 0; i < versionOneValues.length; i++) {
				int versionOneValue = Integer.parseInt(versionOneValues[i]);
				int versionTwoValue = Integer.parseInt(versionTwoValues[i]);

				if (versionOneValue < versionTwoValue) {
					return true;
				}
			}
		}

		if (versionOneValues.length > versionTwoValues.length) {
			for (int i = 0; i < versionTwoValues.length; i++) {
				int versionOneValue = Integer.parseInt(versionOneValues[i]);
				int versionTwoValue = Integer.parseInt(versionTwoValues[i]);

				if (versionOneValue < versionTwoValue) {
					return true;
				}
			}
		}

		if (versionOneValues.length < versionTwoValues.length) {
			if (versionOneValues[versionOneValues.length - 1].equals(versionTwoValues[versionOneValues.length - 1]))
				return true;
			else
				for (int i = 0; i < versionOneValues.length; i++) {
					int versionOneValue = Integer.parseInt(versionOneValues[i]);
					int versionTwoValue = Integer.parseInt(versionTwoValues[i]);

					if (versionOneValue < versionTwoValue) {
						return true;
					}
				}
		}

		return false;
	}
}

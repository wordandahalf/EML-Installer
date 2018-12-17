package io.github.wordandahalf.equilinox.modloader.installer;

import java.io.IOException;

import io.github.wordandahalf.equilinox.modloader.installer.utils.WindowsUtils;
import io.github.wordandahalf.equilinox.modloader.installer.web.WebFetcher;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("Running on " + System.getProperty("os.name"));
		
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			String steamDirectory = WindowsUtils.readRegistry("HKEY_CURRENT_USER\\Software\\Valve\\Steam", "SteamPath");
		
			System.out.println("Steam is installed at " + steamDirectory);
		}
		
		if(WebFetcher.canConnect())
			System.out.println("The update server is online!");
		
		for(String s : WebFetcher.asString(WebFetcher.UPDATE_FILE)) {
			System.out.println(s);
		}
	}
}

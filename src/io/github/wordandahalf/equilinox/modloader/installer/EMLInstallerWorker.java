package io.github.wordandahalf.equilinox.modloader.installer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.github.wordandahalf.equilinox.modloader.installer.utils.AlertUtils;
import io.github.wordandahalf.equilinox.modloader.installer.web.WebFetcher;
import javafx.scene.control.Alert.AlertType;

public class EMLInstallerWorker implements Runnable {
	public static final String UPDATE_SERVER = "https://wordandahalf.github.io/";
	public static final String UPDATE_FILE = UPDATE_SERVER + "eml/eml-version.txt";
	
	private File installLocation;
	
	private String emlVersion;
	private String installerVersion;
	
	public EMLInstallerWorker(File installLocation) {
		this.installLocation = installLocation;
	}
	
	public static boolean canConnect() {
		URL server = null;
		try {
			server = new URL(UPDATE_SERVER);
		} catch (MalformedURLException e) {
			AlertUtils.runAlert(AlertType.ERROR, "Error", "This error should never happen! The provided update server URL is invalid! (" + UPDATE_SERVER + ")");
			
			return false;
		}
		
		URLConnection conn = null;
		try {
			conn = server.openConnection();
		} catch (IOException e) {
			AlertUtils.runAlert(AlertType.ERROR, "Error", "There was an unexpected error upon connecting to the update server!");

			return false;
		}
		
		try {
			conn.connect();
		} catch(IOException e) {
			AlertUtils.runAlert(AlertType.ERROR, "Error", "Could not connect to update server!");

			return false;
		}
		
		return true;
	}
	
	@Override
	public void run() {
		if(canConnect()) {
			try {
				String[] data = WebFetcher.asString(UPDATE_FILE);
				
				for(String s : data) {
					if(s.contains("eml-version")) {
						this.emlVersion = s.split("=")[1];
					}
					
					if(s.contains("installer-version")) {
						this.installerVersion = s.split("=")[1];
					}
				}
				
				if(Main.APPLICATION_VERSION.equals(this.installerVersion)) {
					System.out.println("No installer update needed.");
				}
			} catch (IOException e) {
				AlertUtils.runAlert(AlertType.ERROR, "Error", e.toString());
			}
			
		}
	}
}

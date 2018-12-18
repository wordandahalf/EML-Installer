package io.github.wordandahalf.equilinox.modloader.installer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.github.wordandahalf.equilinox.modloader.installer.gui.MainInterfaceController;
import io.github.wordandahalf.equilinox.modloader.installer.utils.AlertUtils;
import io.github.wordandahalf.equilinox.modloader.installer.web.WebFetcher;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;

public class EMLInstallerWorker implements Runnable {
	public static final String UPDATE_SERVER = "https://wordandahalf.github.io/eml/";
	public static final String UPDATE_FILE = UPDATE_SERVER + "version.txt";
	
	private File installLocation;
	
	private String emlVersion;
	private String wrapperVersion;
	
	public EMLInstallerWorker(File installLocation) {
		this.installLocation = installLocation;
	}
	
	public static boolean canConnect() {
		URL server = null;
		try {
			server = new URL(UPDATE_SERVER);
		} catch (MalformedURLException e) {
			AlertUtils.runAlert(AlertType.ERROR, "Error", "This error should never happen! The provided update server URL is invalid! (" + UPDATE_SERVER + ")", true);
			
			return false;
		}
		
		URLConnection conn = null;
		try {
			conn = server.openConnection();
		} catch (IOException e) {
			AlertUtils.runAlert(AlertType.ERROR, "Error", "There was an unexpected error upon connecting to the update server!", true);

			return false;
		}
		
		try {
			conn.connect();
		} catch(IOException e) {
			AlertUtils.runAlert(AlertType.ERROR, "Error", "Could not connect to update server!", true);

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
					
					if(s.contains("wrapper-version")) {
						this.wrapperVersion = s.split("=")[1];
					}
				}
				
				if(!Main.APPLICATION_VERSION.equals(this.wrapperVersion)) {
					String wrapperName = "eml-wrapper-" + this.wrapperVersion + ".jar";
					
					WebFetcher.downloadFile(UPDATE_SERVER + wrapperName, wrapperName);
					
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					    	Alert alert = new Alert(AlertType.INFORMATION);
					    	alert.setTitle("Information");
					    	alert.setHeaderText("A more up-to-date version of " + Main.APPLICATION_NAME + " v" + wrapperVersion + " was downloaded as " + wrapperName + ". This installer will now close.");
					    	
					    	alert.showAndWait();
					    	
					    	System.exit(0);
					    }
					});
				}
				
				String emlName = "eml-" + this.emlVersion + ".jar";
				
				MainInterfaceController.instance.progressBar.setTooltip(
						new Tooltip("Downloading " + emlName + " from " + UPDATE_SERVER)
				);
				WebFetcher.downloadFile(UPDATE_SERVER + "eml-" + this.emlVersion + ".jar",
						new File(this.installLocation, emlName).getAbsolutePath());
				MainInterfaceController.instance.progressBar.setProgress(0.3);
				
				MainInterfaceController.instance.progressBar.setTooltip(
						new Tooltip("Downloading " + Main.APPLICATION_FILE + " from " + UPDATE_SERVER)
				);
				WebFetcher.downloadFile(UPDATE_SERVER + "eml-wrapper-" + this.wrapperVersion + ".jar",
						new File(this.installLocation, "eml-wrapper-" + this.wrapperVersion + ".jar").getAbsolutePath());
				MainInterfaceController.instance.progressBar.setProgress(0.6);
				
				MainInterfaceController.instance.progressBar.getTooltip().setText("Creating configuration file");
				if(Main.APPLICATION_CONFIGURATION.exists())
					Main.APPLICATION_CONFIGURATION.delete();
				
				PrintWriter writer = new PrintWriter(Main.APPLICATION_CONFIGURATION);
				writer.println("eml-version=" + this.emlVersion);
				writer.println("wrapper-version=" + Main.APPLICATION_VERSION);
				writer.print("install-location=" + this.installLocation);
				writer.flush();
				writer.close();
				
				MainInterfaceController.instance.progressBar.setProgress(1);
			} catch (IOException e) {
				AlertUtils.runAlert(AlertType.ERROR, "Error", e.toString(), true);
			}
			
		}
	}
}

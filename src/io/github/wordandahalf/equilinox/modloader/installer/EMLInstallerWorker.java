package io.github.wordandahalf.equilinox.modloader.installer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.github.wordandahalf.equilinox.modloader.installer.gui.MainInterfaceController;
import io.github.wordandahalf.equilinox.modloader.installer.utils.AlertUtils;
import io.github.wordandahalf.equilinox.modloader.installer.utils.ConfigurationUtils;
import io.github.wordandahalf.equilinox.modloader.installer.utils.UpdateUtils;
import io.github.wordandahalf.equilinox.modloader.installer.web.WebFetcher;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;

public class EMLInstallerWorker implements Runnable {	
	private File installLocation;

	public EMLInstallerWorker(File installLocation) {
		this.installLocation = installLocation;
	}
	
	public static boolean canConnect() {
		URL server = null;
		try {
			server = new URL(UpdateUtils.UPDATE_SERVER);
		} catch (MalformedURLException e) {
			AlertUtils.runAlert(AlertType.ERROR, "Error", "This error should never happen! The provided update server URL is invalid! (" + UpdateUtils.UPDATE_SERVER + ")", true);
			
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
				String emlVersion = UpdateUtils.getServerEMLVersion();
				String wrapperVersion = UpdateUtils.getServerWrapperVersion();
				
				if(UpdateUtils.isVersionOutOfDate(Main.APPLICATION_VERSION, wrapperVersion)) {
					String wrapperName = "eml-wrapper-" + wrapperVersion + ".jar";
					
					WebFetcher.downloadFile(UpdateUtils.UPDATE_SERVER + wrapperName, wrapperName);
					
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
					
					return;
				}

				String emlName = "eml-" + emlVersion + ".jar";
				
				MainInterfaceController.instance.progressBar.setTooltip(
						new Tooltip("Downloading " + emlName + " from " + UpdateUtils.UPDATE_SERVER)
				);
				WebFetcher.downloadFile(UpdateUtils.UPDATE_SERVER + "eml-" + emlVersion + ".jar",
						new File(this.installLocation, emlName).getAbsolutePath());
				MainInterfaceController.instance.progressBar.setProgress(0.5);
				
				Thread.sleep((long) 10);
				
				MainInterfaceController.instance.progressBar.setTooltip(
						new Tooltip("Downloading " + Main.APPLICATION_FILE + " from " + UpdateUtils.UPDATE_SERVER)
				);
				
				MainInterfaceController.instance.progressBar.getTooltip().setText("Creating configuration file");
				if(Main.APPLICATION_CONFIGURATION.exists())
					Main.APPLICATION_CONFIGURATION.delete();
				
				ConfigurationUtils.createConfig(Main.APPLICATION_CONFIGURATION, "local_config");
				ConfigurationUtils.setValue("local_config", "eml-version", emlVersion);
				ConfigurationUtils.setValue("local_config", "install-location", this.installLocation.getAbsolutePath());
				ConfigurationUtils.saveConfig(Main.APPLICATION_CONFIGURATION, "local_config", true);
				
				Thread.sleep((long) 10);
				
				MainInterfaceController.instance.progressBar.setProgress(1);
				
				AlertUtils.runAlert(AlertType.INFORMATION, 
						"Information", 
						"EML-Wrapper v" + wrapperVersion + " with EML v" + emlVersion + " has successfully been installed.", 
						true);
			
			} catch (Exception e) {
				AlertUtils.runAlert(AlertType.ERROR, "Error", e.toString(), true);
			}
		}
	}
}

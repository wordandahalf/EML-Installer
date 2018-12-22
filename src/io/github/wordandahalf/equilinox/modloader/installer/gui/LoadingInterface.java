package io.github.wordandahalf.equilinox.modloader.installer.gui;

import java.io.File;
import java.io.IOException;

import io.github.wordandahalf.equilinox.modloader.installer.Main;
import io.github.wordandahalf.equilinox.modloader.installer.utils.ConfigurationUtils;
import io.github.wordandahalf.equilinox.modloader.installer.utils.UpdateUtils;
import io.github.wordandahalf.equilinox.modloader.installer.web.WebFetcher;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingInterface {
	public void open(Stage primaryStage) {
		BorderPane root = new BorderPane();

		primaryStage.getIcons().add(new Image(LoadingInterface.class.getResourceAsStream("icon.png")));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(new Scene(root, 0.1, 0.1));
		primaryStage.setResizable(false);
		primaryStage.show();

		Alert alert = new Alert(AlertType.CONFIRMATION);

		alert.setTitle("Confirmation");

		String version = "";
		
		try {
			if (!(version = UpdateUtils.checkForEMLUpdate()).equals("")) {
				alert.setHeaderText("A new version of EML is available! Would you like to download it?");
				
				if (alert.showAndWait().get().equals(ButtonType.OK)) {
					String emlName = "eml-" + version + ".jar";
					String emlDir = ConfigurationUtils.getValue("local_config", "install-location") 
							+ File.separator + emlName;
					
					WebFetcher.downloadFile(UpdateUtils.UPDATE_SERVER + emlName, emlDir);
					
					ConfigurationUtils.setValue("local_config", "eml-version", version);
					ConfigurationUtils.saveConfig(Main.APPLICATION_CONFIGURATION, "local_config", false);
					
					alert.setAlertType(AlertType.INFORMATION);
					alert.setHeaderText("A more up-to-date version of EML" + " v" + version + " was downloaded to " + emlDir + ".");
					alert.showAndWait();
				}
			}
		} catch (IOException e) {
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(e.getMessage());
			alert.showAndWait();
		}

		try {
			if (!(version = UpdateUtils.checkForWrapperUpdate()).equals("")) {
				alert.setHeaderText("A new version of the wrapper is available! Would you like to download it?");

				String wrapperName = "eml-wrapper-" + version + ".jar";
				
				if (alert.showAndWait().get().equals(ButtonType.OK)) {
					WebFetcher.downloadFile(UpdateUtils.UPDATE_SERVER + wrapperName, wrapperName);
				}
				
				alert.setAlertType(AlertType.INFORMATION);
				alert.setHeaderText("A more up-to-date version of " + Main.APPLICATION_NAME + " v" + version + " was downloaded as " + wrapperName + ". Please relaunch with the updated version.");
				alert.showAndWait();
				
				System.exit(0);
			}
		} catch (IOException e) {
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(e.getMessage());
			alert.showAndWait();
		}

		String installLocation = ConfigurationUtils.getValue("local_config", "install-location");
		String emlVersion = ConfigurationUtils.getValue("local_config", "eml-version");

		try {
			Runtime.getRuntime().exec(
					"java -jar \"" + installLocation + File.separator + "eml-" + emlVersion + ".jar\"", null,
					new File(installLocation));
		} catch (IOException e) {

		}

		System.exit(0);
	}
}

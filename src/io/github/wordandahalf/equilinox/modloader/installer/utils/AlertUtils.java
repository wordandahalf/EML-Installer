package io.github.wordandahalf.equilinox.modloader.installer.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtils {
	public static void runAlert(AlertType type, String title, String text, boolean blocking) {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		       Alert alert = new Alert(type);
		       alert.setTitle(title);
		       alert.setHeaderText(text);
		       
		       if(blocking)
		    	   alert.showAndWait();
		       else
		    	   alert.show();
		    }
		});
	}
}

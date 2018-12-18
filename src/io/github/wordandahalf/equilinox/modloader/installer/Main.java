package io.github.wordandahalf.equilinox.modloader.installer;

import io.github.wordandahalf.equilinox.modloader.installer.gui.MainInterfaceController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public static final String APPLICATION_NAME = "EML Installer";
	public static final String APPLICATION_VERSION = "0.1.0";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		new MainInterfaceController().open(primaryStage);
	}
}

package io.github.wordandahalf.equilinox.modloader.installer;

import java.io.File;

import io.github.wordandahalf.equilinox.modloader.installer.gui.LoadingInterface;
import io.github.wordandahalf.equilinox.modloader.installer.gui.MainInterfaceController;
import io.github.wordandahalf.equilinox.modloader.installer.utils.ConfigurationUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	public static final String APPLICATION_NAME = "EML Wrapper";
	public static final String APPLICATION_VERSION = "0.2";
	
	public static final File APPLICATION_CONFIGURATION = new File(System.getProperty("user.home"), ".eml-wrapper");
	
	public static final String APPLICATION_FILE = "eml-wrapper-" + APPLICATION_VERSION + ".jar";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		if(!APPLICATION_CONFIGURATION.exists())
			new MainInterfaceController().open(primaryStage);
		else {
			ConfigurationUtils.loadConfig(APPLICATION_CONFIGURATION, "local_config");
			
			new LoadingInterface().open(primaryStage);
		}
	}
}

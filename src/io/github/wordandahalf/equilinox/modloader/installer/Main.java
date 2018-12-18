package io.github.wordandahalf.equilinox.modloader.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import io.github.wordandahalf.equilinox.modloader.installer.gui.MainInterfaceController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	public static final String APPLICATION_NAME = "EML Wrapper";
	public static final String APPLICATION_VERSION = "0.1.0";
	
	public static final File APPLICATION_CONFIGURATION = new File(System.getProperty("user.home"), ".eml-wrapper");
	
	public static final String APPLICATION_FILE = "eml-wrapper-" + APPLICATION_VERSION + ".jar";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		if(!APPLICATION_CONFIGURATION.exists())
			new MainInterfaceController().open(primaryStage);
		else {
			BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_CONFIGURATION));
		
			String ln = "";
			
			while((ln = reader.readLine()) != null) {
				if(ln.startsWith("install-location")) {
					String path = ln.split("=")[1];
					
					Runtime.getRuntime().exec("java -jar " + path + File.separator + APPLICATION_FILE);
				}
			}
			
			reader.close();
		}
	}
}

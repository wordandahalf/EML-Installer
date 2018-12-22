package io.github.wordandahalf.equilinox.modloader.installer.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import io.github.wordandahalf.equilinox.modloader.installer.EMLInstallerWorker;
import io.github.wordandahalf.equilinox.modloader.installer.Main;
import io.github.wordandahalf.equilinox.modloader.installer.utils.OS;
import io.github.wordandahalf.equilinox.modloader.installer.utils.WindowsUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainInterfaceController {
	public static MainInterfaceController instance;
	
	public Window window;
	
	private File equilinoxDirectory;
	
	private Thread workerThread;
	
	@FXML
	private Button installButton;
	@FXML
	public void installEML(MouseEvent e) {
		if(equilinoxDirectory == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("");
			alert.setHeaderText("Please select a directory to install into.");
			alert.show();
			
			return;
		}
		
		this.workerThread = new Thread(new EMLInstallerWorker(equilinoxDirectory));
		this.workerThread.start();
	}
	
	@FXML
	private TextField directoryField;
	
	@FXML
	private Button browseButton;
	@FXML
	public void openFileSelectionMenu(MouseEvent e) {
		DirectoryChooser chooser = new DirectoryChooser();
		
		chooser.setTitle("Find your Equilinox directory");
		equilinoxDirectory = chooser.showDialog(window);
		
		if(equilinoxDirectory == null)
			return;
		
		if(equilinoxDirectory.listFiles().length > 0) {
			directoryField.setText(equilinoxDirectory.getAbsolutePath());
			directoryField.setTooltip(new Tooltip(directoryField.getText()));
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("");
			alert.setHeaderText("The selected folder doesn't seem to have any files in it. Continue?");
		
			if(!alert.showAndWait().get().equals(ButtonType.OK)) {
				openFileSelectionMenu(e);
				return;
			}
			
			directoryField.setText(equilinoxDirectory.getAbsolutePath());
			directoryField.setTooltip(new Tooltip(directoryField.getText()));
		}
	}
	
	@FXML
	public ProgressBar progressBar;
	
	@FXML
	private Label progressText;
	
	public void open(Stage primaryStage) throws IOException {
		instance = this;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInterface.fxml"));
		loader.setController(this);
		
		Parent root = loader.load();
		
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		double width = size.getWidth() / 4.0;
		double height = (width / 6.0) * 3.0;
		
		primaryStage.getIcons().add(new Image(MainInterfaceController.class.getResourceAsStream("icon.png")));
		primaryStage.setScene(new Scene(root, width, height));
        primaryStage.setResizable(false);
        primaryStage.setTitle(Main.APPLICATION_NAME + " v" + Main.APPLICATION_VERSION);
        primaryStage.show();
		
        window = root.getScene().getWindow();
        
		if(OS.getOS().equals(OS.WINDOWS)) {
			File equilinox = new File(WindowsUtils.findSteam() + File.separator
					+ "steamapps" + File.separator + 
					"common" + File.separator
					+ "Equilinox");
			
			if(equilinox.isDirectory() && equilinox.listFiles().length > 0) {
				directoryField.setText(equilinox.getAbsolutePath());
				directoryField.setTooltip(new Tooltip(directoryField.getText()));
				
				equilinoxDirectory = equilinox;
			}
		}
	}
}

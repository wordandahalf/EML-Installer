package io.github.wordandahalf.equilinox.modloader.installer.web;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WebFetcher {	
	public static void downloadFile(String URL, String path) throws IOException {
		URL website = new URL(URL);
		
		try (InputStream is = website.openStream();
				ReadableByteChannel rbc = Channels.newChannel(is);
				FileOutputStream fos = new FileOutputStream(path)) {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}
	}
	
	public static String[] asString(String URL) throws IOException {
		URL site = null;

		try {
			site = new URL(URL);
		} catch(Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("This error should never happen! The provided download URL is invalid! (" + URL + ")");
			alert.show();
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(site.openStream()));
		ArrayList<String> string = new ArrayList<>();
		
		String ln = "";
		while ((ln = in.readLine()) != null) {
			string.add(ln);
		}
		
		return string.toArray(new String[] {});
	}
}

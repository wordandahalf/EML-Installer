package io.github.wordandahalf.equilinox.modloader.installer.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WebFetcher {
	public static final String UPDATE_SERVER = "https://wordandahalf.github.io/";
	public static final String UPDATE_FILE = UPDATE_SERVER + "eml/eml-version.txt";
	
	public static boolean canConnect() {
		URL server = null;
		try {
			server = new URL(UPDATE_SERVER);
		} catch (MalformedURLException e2) {
			//TODO: User feedback
			System.err.println("This error should never happen! The provided update server URL is invalid!");
			return false;
		}
		
		URLConnection conn = null;
		try {
			conn = server.openConnection();
		} catch (IOException e1) {
			//TODO: User feedback
			System.err.println("There was an unexpected error upon connecting to the update server!");
			return false;
		}
		
		try {
			conn.connect();
		} catch(IOException e) {
			//TODO: User feedback
			System.err.println("Could not connect to update server!");
			return false;
		}
		
		return true;
	}
	
	public static String[] asString(String URL) throws IOException {
		URL site = null;

		try {
			site = new URL(URL);
		} catch(Exception e) {
			//TODO: User feedback
			System.err.println("This error should never happen! The provided download URL is invalid!");
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

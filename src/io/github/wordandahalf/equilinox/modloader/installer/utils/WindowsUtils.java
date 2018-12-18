package io.github.wordandahalf.equilinox.modloader.installer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WindowsUtils {
	public static String findSteam() {
		return WindowsUtils.readRegistry("HKEY_CURRENT_USER\\Software\\Valve\\Steam", "SteamPath");
	}
	
	public static final String readRegistry(String location, String key) {
		try {
			// Run reg query, then read output with StreamReader (internal class)
			Process process = Runtime.getRuntime().exec("reg query " + '"' + location + "\" /v " + key);

			StreamReader reader = new StreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();
			String output = reader.getResult();
			
			// Output has the following format:
			// \n<location>\n\t<key>\t<key type>\t<value>
			if (!output.contains("    ")) {
				return null;
			}

			// Parse out the value
			String[] parsed = output.split("    ");
			return parsed[parsed.length - 1].replace(System.getProperty("line.separator"), "");
		} catch (Exception e) {
			return null;
		}

	}

	static class StreamReader extends Thread {
		private InputStream is;
		private StringWriter sw = new StringWriter();

		public StreamReader(InputStream is) {
			this.is = is;
		}

		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {
			}
		}

		public String getResult() {
			return sw.toString();
		}
	}
}

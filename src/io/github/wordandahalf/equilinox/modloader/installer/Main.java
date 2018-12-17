package io.github.wordandahalf.equilinox.modloader.installer;

import java.io.IOException;

import io.github.wordandahalf.equilinox.modloader.installer.web.WebFetcher;

public class Main {
	public static void main(String[] args) throws IOException {
		if(WebFetcher.canConnect())
			System.out.println("The update server is online!");
		
		for(String s : WebFetcher.asString(WebFetcher.UPDATE_FILE)) {
			System.out.println(s);
		}
	}
}

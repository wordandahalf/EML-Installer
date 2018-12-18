package io.github.wordandahalf.equilinox.modloader.installer.utils;

public enum OS {
	WINDOWS("Windows", "win"),
	LINUX("Linux", "nix", "nux", "aix"),
	MAC_OS("MacOS", "mac"),
	UNSUPPORTED("Unsupported");
	
	private String name;
	private String[] identifiers;
	
	private OS(String name, String... identifiers) {
		this.name = name;
		this.identifiers = identifiers;
	}
	
	public static OS getOS() {
		return getOS(System.getProperty("os.name"));
	}
	
	public static OS getOS(String identifier) {
		identifier = identifier.toLowerCase();
		
		for(OS os : OS.values()) {
			for(String id : os.identifiers) {
				if(identifier.indexOf(id) >= 0)
					return os;
			}
		}
		
		return OS.UNSUPPORTED;
	}
	
	public String getName() { return this.name; }
}
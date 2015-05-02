package com.mengcraft.protect;

import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class DataCompond {

	private final Main p;

	public DataCompond(Main protect) {
		this.p = protect;
	}

	public MetadataValue create(Object obj) {
		return new FixedMetadataValue(p, obj);
	}
	
	public ConfigurationSection config() {
		return p.getConfig().getConfigurationSection("manager.entity");
	}
	
	public Server server() {
		return p.getServer();
	}

}

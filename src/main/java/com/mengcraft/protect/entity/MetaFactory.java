package com.mengcraft.protect.entity;

import org.bukkit.configuration.Configuration;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.mengcraft.protect.Protect;

public class MetaFactory {

	private final Protect p;

	public MetaFactory(Protect protect) {
		this.p = protect;
	}

	public MetadataValue create(Object obj) {
		return new FixedMetadataValue(p, obj);
	}
	
	public Configuration config() {
		return p.getConfig();
	}

}

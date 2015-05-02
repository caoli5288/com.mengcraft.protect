package com.mengcraft.protect;

import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitScheduler;

public class DataCompond {

	private final Main main;

	public DataCompond(Main main) {
		this.main = main;
	}

	public MetadataValue create(Object obj) {
		return new FixedMetadataValue(main, obj);
	}
	
	public ConfigurationSection config() {
		return main.getConfig().getConfigurationSection("manager.");
	}
	
	public Server server() {
		return main.getServer();
	}

    public Main main() {
        return main;
    }

    public BukkitScheduler scheduler() {
        return server().getScheduler();
    }
    
    public void register(Listener listener) {
        server().getPluginManager().registerEvents(listener, main);
    }

    public Player[] onlines() {
        return server().getOnlinePlayers();
    }

}

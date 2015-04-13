package com.mengcraft.protect.task;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mengcraft.protect.Main;

public class SpigotTask implements Runnable {

	private final FileConfiguration config;

	public SpigotTask(Main main) {
		this.config = main.getConfig();
	}

	@Override
	public void run() {
		File file = new File("spigot.yml");
		if (file.exists() != false) {
			spigot(file);
		}
	}
	
	private void spigot(File file) {
		FileConfiguration sp = YamlConfiguration.loadConfiguration(file);
		boolean tc = config.getBoolean("manager.spigot.tab-complete");
		if (sp.isBoolean("commands.tab-complete")) {
			sp.set("commands.tab-complete", tc);
		} else {
			sp.set("commands.tab-complete", tc ? 0 : -1);
		}
		int view = config.getInt("manager.spigot.view-distance");
		if (view < 3 || view > 4) {
			view = 4;
			config.set("manager.spigot.view-distance", 4);
		}
		sp.set("world-settings.default.view-distance", view);
		int range = config.getInt("manager.spigot.entity-activation");
		if (range < 1 || range > 8) {
			range = 4;
			config.set("manager.spigot.entity-activation", 4);
		}
		sp.set("world-settings.default.entity-activation-range.animals",
				range * 3);
		sp.set("world-settings.default.entity-activation-range.monsters",
				range * 6);
		sp.set("world-settings.default.entity-activation-range.misc",
				range * 1);
		try {
			sp.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package com.mengcraft.protect;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.mengcraft.protect.entity.EntityEvent;
import com.mengcraft.protect.entity.MetaFactory;
import com.mengcraft.protect.entity.PlayerEvent;
import com.mengcraft.protect.task.WorldTask;

public class Protect extends JavaPlugin {

	@Override
	public void onEnable() {
		File conf = new File(getDataFolder(), "config.yml");
		try {
			getConfig().save(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Start modify spigot.yml
		File file = new File("spigot.yml");
		if (file.isFile()) {
			spigot(file);
		}
		// End modify spigot.yml
		EntityEvent g = new EntityEvent(new MetaFactory(this));
		getServer().getPluginManager().registerEvents(g, this);
		PlayerEvent f = new PlayerEvent(this);
		getServer().getPluginManager().registerEvents(f, this);
		getServer().getPluginManager().registerEvents(new AntiCheat(), this);
		Runnable t = new WorldTask(this);
		getServer().getScheduler().runTaskTimer(this, t, 6000, 6000);
		try {
			new Metrics(this).start();
		} catch (IOException e) {
			getLogger().warning("Cant connect to mcstats.org!");
		}
	}

	private void spigot(File file) {
		FileConfiguration sp = YamlConfiguration.loadConfiguration(file);
		boolean tc = getConfig().getBoolean("manager.spigot.tab-complete");
		if (sp.isBoolean("commands.tab-complete")) {
			sp.set("commands.tab-complete", tc);
		} else {
			sp.set("commands.tab-complete", tc ? 0 : -1);
		}
		int view = getConfig().getInt("manager.spigot.view-distance");
		if (view < 3 || view > 4) {
			view = 4;
			getConfig().set("manager.spigot.view-distance", 4);
		}
		sp.set("world-settings.default.view-distance", view);
		int range = getConfig().getInt("manager.spigot.entity-activation");
		if (range < 1 || range > 8) {
			range = 4;
			getConfig().set("manager.spigot.entity-activation", 4);
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

	@Override
	public void onDisable() {
		File conf = new File(getDataFolder(), "config.yml");
		try {
			getConfig().save(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

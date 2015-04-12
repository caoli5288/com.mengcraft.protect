package com.mengcraft.protect;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.mengcraft.protect.entity.EntityEvent;
import com.mengcraft.protect.entity.MetaFactory;
import com.mengcraft.protect.entity.PlayerEvent;
import com.mengcraft.protect.task.Spigot;
import com.mengcraft.protect.task.WorldTask;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
		MetaFactory f = new MetaFactory(this);
		EntityEvent g = new EntityEvent(f);
		getServer().getPluginManager().registerEvents(g, this);
		PlayerEvent p = new PlayerEvent(this);
		getServer().getPluginManager().registerEvents(p, this);
		getServer().getPluginManager().registerEvents(new AntiCheat(), this);
		Runnable t = new WorldTask(f);
		getServer().getScheduler().runTaskTimer(this, t, 3600, 3600);
		getServer().getScheduler().runTask(this, new Spigot(this));
		try {
			new Metrics(this).start();
		} catch (IOException e) {
			getLogger().warning("Cant connect to mcstats.org!");
		}
	}

	@Override
	public void onDisable() {
		saveConfig();
	}

}

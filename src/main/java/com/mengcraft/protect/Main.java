package com.mengcraft.protect;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.mengcraft.protect.entity.ChunkEvent;
import com.mengcraft.protect.entity.EntityEvent;
import com.mengcraft.protect.entity.MetaFactory;
import com.mengcraft.protect.entity.PlayerEvent;
import com.mengcraft.protect.entity.RedstoneEvent;
import com.mengcraft.protect.task.RestartTask;
import com.mengcraft.protect.task.SpigotTask;
import com.mengcraft.protect.task.WorldTask;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
		getServer().getScheduler().runTask(this, new SpigotTask(this));
		MetaFactory f = new MetaFactory(this);
		EntityEvent g = new EntityEvent(f);
		getServer().getPluginManager().registerEvents(g, this);
		PlayerEvent p = new PlayerEvent(this);
		getServer().getPluginManager().registerEvents(p, this);
		RedstoneEvent r = new RedstoneEvent(getConfig());
		getServer().getPluginManager().registerEvents(r, this);
		getServer().getScheduler().runTaskTimer(this, r, 20, 20);
		WorldTask t = new WorldTask(f);
		getServer().getScheduler().runTaskTimer(this, t, 3600, 3600);
		RestartTask s = new RestartTask(this);
		long u = getConfig().getLong("manager.restart.daily", 24);
		if (u < 1) {
			u = 24;
			getConfig().set("manager.restart.daily", u);
		}
		u = u * 72000;
		getServer().getScheduler().runTaskTimer(this, s, u, 1200);
		ChunkEvent q = new ChunkEvent(this);
		getServer().getPluginManager().registerEvents(q, this);
		try {
			new Metrics(this).start();
		} catch (IOException e) {
			getLogger().warning("Cant connect to mcstats.org!");
		}
		String[] strings = {
				ChatColor.GREEN + "梦梦家高性能服务器出租店",
				ChatColor.GREEN + "shop105595113.taobao.com"
		};
		getServer().getConsoleSender().sendMessage(strings);
		getLogger().info("Enabled Protect done!");
	}

	@Override
	public void onDisable() {
		getServer().savePlayers();
		for (World w : getServer().getWorlds()) {
			w.save();
		}
		saveConfig();
	}

}

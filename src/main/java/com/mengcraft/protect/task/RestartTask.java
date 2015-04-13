package com.mengcraft.protect.task;

import org.bukkit.Server;

import com.mengcraft.protect.Main;

public class RestartTask implements Runnable {

	private final Server server;
	private final int limit;

	@Override
	public void run() {
		if (server.getOnlinePlayers().length < limit) {
			server.shutdown();
		}
	}

	public RestartTask(Main main) {
		this.server = main.getServer();
		this.limit = main.getConfig().getInt("manager.restart.limit", 5);
	}

}

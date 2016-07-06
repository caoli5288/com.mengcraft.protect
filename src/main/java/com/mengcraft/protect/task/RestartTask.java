package com.mengcraft.protect.task;

import com.mengcraft.protect.Main;

public class RestartTask implements Runnable {

	private final Main main;
	private final int limit;

	@Override
	public void run() {
		if (main.getCurrentOnline().size() < limit) {
			main.getServer().shutdown();
		}
	}

	public RestartTask(Main main) {
		this.main = main;
		this.limit = main.getConfig().getInt("manager.restart.limit", 5);
	}

}

package com.mengcraft.protect.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.mengcraft.protect.Main;

public class PlayerEvent implements Listener {

	private final int max;

	@EventHandler
	public void handle(PlayerLoginEvent event) {
		if (event.getResult().equals(Result.ALLOWED)) {
			Player p = event.getPlayer();
			while (p.getServer().getOnlinePlayers().length > max) {
				kick(p);
			}
		}
	}

	private void kick(Player p) {
		Player[] ps = p.getServer().getOnlinePlayers();
		int i = ps.length;
		while (i-- != 0) {
			Player tp = ps[i];
			if (!tp.hasPermission("essentials.joinfullserver") || i == 0) {
				tp.kickPlayer("服务器人已经满你被挤下线了");
			}
		}
	}

	public PlayerEvent(Main p) {
		this.max = p.getServer().getMaxPlayers();
	}
	
}

package com.mengcraft.protect.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mengcraft.protect.Main;
import com.mengcraft.util.ArrayIttor;

public class PlayerEvent implements Listener {

	private final int limit;
	private final Map<String, Integer> map;
	private final Server server;
	private final List<String> list;
	private final int max;
	private final Main main;

	private static final String PERM_FULL = "essentials.joinfullserver";
	private static final String KICK_FULL = "您被拥有满人进服特权的玩家挤下线了";
	private static final String KICK_ADDR = "您的网络地址的在线数目已经达到上限";

	@EventHandler
	public void handle(PlayerQuitEvent e) {
		String host = getHostAddress(e.getPlayer());
		int count = check(host);
		if (count > 1) {
			map.put(host, count - 1);
		} else if (count > 0) {
			map.remove(host);
		}
	}

	@EventHandler
	public void handle(AsyncPlayerPreLoginEvent e) {
		if (e.getLoginResult() == Result.ALLOWED) {
			String host = e.getAddress().getHostAddress();
			if (list.contains(host)) {
				e.setLoginResult(Result.ALLOWED);
			} else if (check(host) >= limit) {
				e.setLoginResult(Result.KICK_FULL);
			}
		}
	}

	@EventHandler
	public void handle(PlayerJoinEvent e) {
		String host = getHostAddress(e.getPlayer());
		if (list.contains(host)) {
			// In white-list, do nothings.
		} else if (check(host) < limit) {
			map.put(host, check(host) + 1);
		} else {
			server.getScheduler().runTask(main, new KickTask(e));
		}
		while (onlines() > max) {
			select().kickPlayer(KICK_FULL);
		}
	}
	
	private String getHostAddress(Player p) {
		return p.getAddress().getAddress().getHostAddress();
	}

	private Player select() {
		Player[] array = server.getOnlinePlayers();
		ArrayIttor<Player> it = new ArrayIttor<Player>(array);
		while (it.remain() > 1) {
			Player p = it.next();
			if (!p.hasPermission(PERM_FULL)) {
				return p;
			}
		}
		return it.next();
	}

	private int onlines() {
		return server.getOnlinePlayers().length;
	}

	private int check(String host) {
		return map.get(host) != null ? map.get(host) : 0;
	}

	public PlayerEvent(Main p) {
		this.server = p.getServer();
		this.main = p;
		this.map = new HashMap<String, Integer>();
		this.list = p.getConfig().getStringList("manager.player.white-list");
		int limit = p.getConfig().getInt("manager.player.limit-addr", 2);
		if (limit < 1) {
			limit = 2;
			p.getConfig().set("manager.player.limit-addr", limit);
		}
		this.limit = limit;
		this.max = p.getServer().getMaxPlayers();
	}

	private class KickTask implements Runnable {
		
		private Player p;
	
		@Override
		public void run() {
			p.kickPlayer(KICK_ADDR);
		}
		
		public KickTask(PlayerJoinEvent e) {
			this.p = e.getPlayer();
		}
		
	}

}

package com.mengcraft.protect.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mengcraft.protect.Main;
import com.mengcraft.util.ArrayIttor;

public class PlayerEvent implements Listener {

	private final int limit;
	private final Map<String, Integer> map;
	private final Server main;
	private final List<String> list;
	private final int max;

	private static final String PERM_FULL = "essentials.joinfullserver";
	private static final String KICK_FULL = "您被拥有满人进服特权的玩家挤下线了";
	private static final String KICK_ADDR = "您的网络地址的在线数目已经达到上限";

	@EventHandler
	public void handle(PlayerQuitEvent e) {
		map.remove(getHostAddress(e.getPlayer()));
	}

	@EventHandler
	public void handle(PlayerLoginEvent e) {
		if (e.getResult() == Result.ALLOWED) {
			String host = e.getAddress().getHostAddress();
			if (list.contains(host)) {
				e.setResult(Result.ALLOWED);
			} else if (check(host) >= limit) {
				e.disallow(Result.KICK_BANNED, KICK_ADDR);
			}
		}
	}

	@EventHandler
	public void handle(PlayerJoinEvent e) {
		String s = getHostAddress(e.getPlayer());
		if (map.get(s) != null) {
			map.put(s, map.get(s) + 1);
		}
		while (onlines() > max) {
			select().kickPlayer(KICK_FULL);
		}
	}

	private String getHostAddress(Player p) {
		return p.getAddress().getAddress().getHostAddress();
	}

	private Player select() {
		Player[] array = main.getOnlinePlayers();
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
		return main.getOnlinePlayers().length;
	}

	private int check(String host) {
		if (map.get(host) != null) {
			return map.get(host);
		}
		return 0;
	}

	public PlayerEvent(Main p) {
		this.main = p.getServer();
		this.map = new HashMap<String, Integer>();
		this.list = p.getConfig().getStringList("manager.player.white-list");
		this.limit = p.getConfig().getInt("manager.player.limit-addr", 2);
		this.max = p.getServer().getMaxPlayers();
	}

}

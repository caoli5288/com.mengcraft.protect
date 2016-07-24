package com.mengcraft.protect.listener;

import com.mengcraft.protect.DataCompound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static org.bukkit.event.player.PlayerLoginEvent.Result.ALLOWED;
import static org.bukkit.event.player.PlayerLoginEvent.Result.KICK_FULL;

public class PlayerEvent implements Listener {

    private final int limit;
    private final Map<String, Integer> map;
    private final List<String> list;
    private final int max;
    private final DataCompound compound;

    private static final String PERM_FULL = "essentials.joinfullserver";
    private static final String MESS_FULL = "您被拥有满人进服特权的玩家挤下线了";
    private static final String MESS_ADDR = "您的网络地址的在线数目已经达到上限";

    @EventHandler
    public void handle(PlayerQuitEvent e) {
        String host = e.getPlayer().getAddress().getAddress().getHostAddress();
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
                // Allowed in white-list address.
                e.setLoginResult(Result.ALLOWED);
            } else if (check(host) >= limit) {
                e.setLoginResult(Result.KICK_FULL);
            }
        }
    }

    @EventHandler
    public void handle(PlayerLoginEvent e) {
        if (e.getResult() == ALLOWED) {
            String host = e.getAddress().getHostAddress();
            if (list.contains(host)) {
                // Allowed in white-list address.
                e.setResult(ALLOWED);
            } else if (check(host) >= limit) {
                e.setResult(KICK_FULL);
            }
        }
    }

    @EventHandler
    public void handle(PlayerJoinEvent e) {
        String host = e.getPlayer().getAddress().getAddress().getHostAddress();
        if (list.contains(host)) {
            // In white-list, do nothings.
        } else if (check(host) < limit) {
            map.put(host, check(host) + 1);
        } else {
            compound.scheduler().runTask(compound.main(), new KickTask(e));
        }
        while (size() > max) {
            select().kickPlayer(MESS_FULL);
        }
    }

    private Player select() {
        ListIterator<Player> it = compound.online().listIterator();
        while (it.hasNext()) {
            Player p = it.next();
            if (!p.hasPermission(PERM_FULL)) {
                return p;
            }
        }
        return it.previous();
    }

    private int size() {
        return compound.online().size();
    }

    private int check(String host) {
        return map.get(host) != null ? map.get(host) : 0;
    }

    public PlayerEvent(DataCompound compound) {
        this.map = new HashMap<>();
        this.list = compound.config().getStringList("player.white-list");
        int limit = compound.config().getInt("player.limit-addr", 2);
        if (limit < 1) {
            limit = 2;
            compound.config().set("player.limit-addr", limit);
        }
        this.limit = limit;
        this.max = compound.server().getMaxPlayers();
        this.compound = compound;
    }

    private class KickTask implements Runnable {

        private final Player p;

        @Override
        public void run() {
            p.kickPlayer(MESS_ADDR);
        }

        public KickTask(PlayerJoinEvent e) {
            this.p = e.getPlayer();
        }

    }

}

package com.mengcraft.protect;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.mengcraft.protect.debug.Executor;
import com.mengcraft.protect.entity.ChunkEvent;
import com.mengcraft.protect.entity.EntityEvent;
import com.mengcraft.protect.entity.PlayerEvent;
import com.mengcraft.protect.entity.RedstoneEvent;
import com.mengcraft.protect.task.RestartTask;
import com.mengcraft.protect.task.SpigotTask;
import com.mengcraft.protect.task.WorldTask;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        DataCompound compond = new DataCompound(this);
        compond.scheduler().runTask(this, new SpigotTask(this));
        Listener g = new EntityEvent(compond);
        compond.register(g);
        Listener p = new PlayerEvent(compond);
        compond.register(p);
        RedstoneEvent r = new RedstoneEvent(getConfig());
        compond.register(r);
        compond.scheduler().runTaskTimer(this, r, 20, 20);
        Runnable worldTask = new WorldTask(compond);
        compond.scheduler().runTaskTimer(this, worldTask, 3600, 3600);
        Runnable s = new RestartTask(this);
        long u = getConfig().getLong("manager.restart.daily", 24);
        if (u < 1) {
            u = 24;
            getConfig().set("manager.restart.daily", u);
        }
        compond.scheduler().runTaskTimer(this, s, u * 72000, 1200);
        Listener q = new ChunkEvent(compond);
        compond.register(q);
        getCommand("protect").setExecutor(new Executor(compond));
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
        getServer().shutdown();
    }

    @Override
    public void onLoad() {
    	getConfig().options().copyDefaults(true);
    	saveConfig();
    }

}

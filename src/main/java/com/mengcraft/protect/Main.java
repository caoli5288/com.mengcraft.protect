package com.mengcraft.protect;

import com.mengcraft.protect.listener.ChunkEvent;
import com.mengcraft.protect.listener.EntityEvent;
import com.mengcraft.protect.listener.PlayerEvent;
import com.mengcraft.protect.listener.RedstoneEvent;
import com.mengcraft.protect.task.RestartTask;
import com.mengcraft.protect.task.SpigotTask;
import com.mengcraft.protect.task.WorldTask;
import com.mengcraft.protect.task.YumSubscribeTask;
import com.mengcraft.protect.util.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Main extends JavaPlugin {

    private Method a;

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
        if (getConfig().getBoolean("manager.chunk.auto-unload")) {
            compond.register(new ChunkEvent(compond));
        }
        getCommand("protect").setExecutor(new Executor(compond));

        if (getConfig().getBoolean("subscribe.yum")) {
            if (!getServer().getPluginManager().isPluginEnabled("Yum")) {
                getServer().getScheduler().runTaskAsynchronously(this, new YumSubscribeTask(this));
            }
        }
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

    @SuppressWarnings("unchecked")
    public List<Player> getCurrentOnline() {
        if (a == null) {
            try {
                a = getServer().getClass().getMethod("getOnlinePlayers");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        Object result;
        try {
            result = a.invoke(getServer());
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            result = null;
        }
        return (result == null ? new ArrayList<>() : result instanceof Collection ? new ArrayList<>(((Collection) result)) : new ArrayList<>(Arrays.asList(((Player[]) result))));
    }

    @Override
    public void onDisable() {
        getServer().savePlayers();
        for (World w : getServer().getWorlds()) {
            w.save();
        }
        getServer().shutdown();
    }

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}

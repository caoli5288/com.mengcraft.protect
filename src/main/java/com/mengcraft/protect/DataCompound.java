package com.mengcraft.protect;

import com.mengcraft.protect.task.Post;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCompound {

    private final Map<String, Integer> worldEntities;
    private final Main main;

    public DataCompound(Main main) {
        this.main = main;
        this.worldEntities = new HashMap<>();
    }

    public int worldEntities(String name) {
        Integer out = worldEntities.get(name);
        return out != null ? out : 0;
    }

    public void worldEntities(String name, int size) {
        worldEntities.put(name, size);
    }

    public ConfigurationSection config() {
        return main.getConfig().getConfigurationSection("manager.");
    }

    public Server server() {
        return main.getServer();
    }

    public Main main() {
        return main;
    }

    public BukkitScheduler scheduler() {
        return server().getScheduler();
    }

    public void register(Listener listener) {
        server().getPluginManager().registerEvents(listener, main);
    }

    public List<Player> online() {
        return main.getCurrentOnline();
    }

    public List<World> worldSet() {
        return server().getWorlds();
    }

    public World world(String next) {
        return server().getWorld(next);
    }

    public void post(byte[] host) {
        scheduler().runTaskAsynchronously(main, new Post(host));
    }

    public void save() {
        main.saveConfig();
    }

}

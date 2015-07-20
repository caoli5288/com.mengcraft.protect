package com.mengcraft.protect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitScheduler;

public class DataCompound {

    private final Main main;

    private final Map<String, Integer> worldEntities;

    public DataCompound(Main main) {
        this.main = main;
        this.worldEntities = new HashMap<String, Integer>();
    }

    public int worldEntities(String name) {
        Integer out = worldEntities.get(name);
        return out != null ? out : 0;
    }

    public void worldEntities(String name, int size) {
        worldEntities.put(name, size);
    }

    public MetadataValue create(Object obj) {
        return new FixedMetadataValue(main, obj);
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

    public Player[] onlines() {
        return server().getOnlinePlayers();
    }

    public List<World> worlds() {
        return server().getWorlds();
    }

    public World world(String next) {
        return server().getWorld(next);
    }

    public void warn(Object e) {
        main.getLogger().warning(e.toString());
    }

}

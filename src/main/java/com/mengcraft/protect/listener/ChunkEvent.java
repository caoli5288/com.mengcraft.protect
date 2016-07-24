package com.mengcraft.protect.listener;

import com.mengcraft.protect.DataCompound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkEvent implements Listener {

    private final Set<String> set = new HashSet<>();

    @EventHandler
    public void handle(ChunkLoadEvent event) {
        for (Entity e : event.getChunk().getEntities()) {
            check(e);
        }
    }

    private void check(Entity e) {
        if (set.contains(e.getType().name().toUpperCase())) {
            e.remove();
        }
    }

    public ChunkEvent(DataCompound compound) {
        List<String> list = compound.config().getStringList("chunk.purge-on-unload");
        for (String line : list) {
            set.add(line.toUpperCase());
        }
    }

}

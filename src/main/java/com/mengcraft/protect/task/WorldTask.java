package com.mengcraft.protect.task;

import com.mengcraft.protect.DataCompound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldTask implements Runnable {

    private final Map<String, Integer> map = new HashMap<>();
    private final DataCompound compound;

    @Override
    public void run() {
        for (World w : compound.worldSet()) {
            task(w);
        }
    }

    private void task(World world) {
        List<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            task(entity);
        }
        compound.worldEntities(world.getName(), entities.size());
    }

    private void task(Entity e) {
        if (e instanceof LivingEntity && !(e instanceof Player)) {
            String type = e.getType().name();
            if (map.get(type) == null) {
                cache(type, e instanceof Monster);
            }
            check(map.get(type), e);
        }
    }

    private void cache(String type, boolean b) {
        String path = "entity.control." + type.toLowerCase() + ".lifetime";
        int limit = compound.config().getInt(path, -1);
        if (limit < 0) {
            if (b) {
                limit = 12000;
            } else {
                limit = 0;
            }
            compound.config().set(path, limit);
            compound.save();
        }
        map.put(type, limit);
    }

    private void check(int limit, Entity e) {
        if (limit > 0 && e.getTicksLived() > limit) {
            e.remove();
        }
    }

    public WorldTask(DataCompound compound) {
        this.compound = compound;
    }

}

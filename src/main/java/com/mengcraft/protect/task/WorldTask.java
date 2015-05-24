package com.mengcraft.protect.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.mengcraft.protect.DataCompound;

public class WorldTask implements Runnable {

    private final DataCompound compond;
    private final Map<String, Integer> map;

    @Override
    public void run() {
        for (World w : compond.worlds()) {
            task(w);
        }
    }

    private void task(World world) {
        List<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            task(entity);
        }
        compond.worldEntities(world.getName(), entities.size());
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
        int limit = compond.config().getInt(path, -1);
        if (limit < 0) {
            if (b) {
                limit = 12000;
            } else {
                limit = 0;
            }
            compond.config().set(path, limit);
        }
        map.put(type, limit);
    }

    private void check(int limit, Entity e) {
        if (limit > 0 && e.getTicksLived() > limit) {
            e.remove();
        }
    }

    public WorldTask(DataCompound f) {
        this.compond = f;
        this.map = new HashMap<String, Integer>();
    }

}

package com.mengcraft.protect.task;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

import com.mengcraft.protect.entity.MetaFactory;

public class WorldTask implements Runnable {

	private final MetaFactory f;
	private final Map<String, Integer> map;

	@Override
	public void run() {
		for (World w : f.server().getWorlds()) {
			task(w);
		}
	}

	private void task(World w) {
		for (Chunk c : w.getLoadedChunks()) {
			task(c);
		}
	}

	private void task(Chunk c) {
		for (Entity e : c.getEntities()) {
			task(e);
		}
		c.unload(true, true);
	}

	private void task(Entity e) {
		if (e instanceof LivingEntity) {
			String type = e.getType().name();
			if (map.get(type) != null) {
				check(map.get(type), e);
			} else {
				cache(e);
			}
		}
	}

	private void cache(Entity e) {
		String type = e.getType().name();
		String path = "control." + type.toLowerCase() + ".lifetime";
		int limit = f.config().getInt(path, -1);
		if (limit < 0) {
			if (e instanceof Monster) {
				limit = 12000;
			} else {
				limit = 0;
			}
			f.config().set(path, limit);
		}
		map.put(type, limit);
		check(limit, e);
	}

	private void check(int limit, Entity e) {
		if (limit > 0 && e.getTicksLived() > limit) {
			e.remove();
			System.out.println("WorldTask: Remove a " + e.getType() + " because of " + e.getTicksLived() + " lived.");
		}
	}

	public WorldTask(MetaFactory f) {
		this.f = f;
		this.map = new HashMap<String, Integer>();
	}

}
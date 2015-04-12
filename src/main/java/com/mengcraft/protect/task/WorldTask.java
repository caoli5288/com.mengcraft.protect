package com.mengcraft.protect.task;

import static com.mengcraft.protect.entity.EntityEvent.META_LIFE;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import com.mengcraft.protect.Main;

public class WorldTask implements Runnable {

	private final Server s;

	@Override
	public void run() {
		for (World w : s.getWorlds()) {
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
		if (e.hasMetadata(META_LIFE)) {
			List<MetadataValue> list = e.getMetadata(META_LIFE);
			int life = list.get(0).asInt();
			if (life > 0 && life < e.getTicksLived()) {
				e.remove();
			}
		}
	}

	public WorldTask(Main p) {
		this.s = p.getServer();
	}

}

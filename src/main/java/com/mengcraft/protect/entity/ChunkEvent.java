package com.mengcraft.protect.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.mengcraft.protect.DataCompond;

public class ChunkEvent implements Listener {

	private final Set<String> set = new HashSet<String>();
	private final Queue<Chunk> chunks = new LinkedBlockingDeque<Chunk>();
	private boolean unload;
	private final int limit;

	@EventHandler
	public void handle(ChunkLoadEvent event) {
		Chunk c = event.getChunk();
		if (unload) {
			check(c);
		}
		for (Entity e : c.getEntities()) {
			check(e);
		}
	}

	private void check(Chunk c) {
		int i = limit / 20;
		while (chunks.size() > limit && i-- != 0) {
			Chunk d = chunks.poll();
			// Re-add the chunk to the queue only if
			// it is loaded and unload failed.
			// Check after unload to find MCPC's bug
			// and simply disable auto unload chunks.
			if (d.isLoaded() && !d.unload(true, true)) {
				chunks.offer(d);
			} else if (d.isLoaded()) {
				unload = false;
			} 
		}
		chunks.offer(c);
	}

	private void check(Entity e) {
		String name = e.getType().name().toLowerCase();
		if (set.contains(name)) {
			e.remove();
		}
	}

	public ChunkEvent(DataCompond compond) {
		String path = "chunk.purge-on-unload";
		List<String> list = compond.config().getStringList(path);
		for (String line : list) {
			set.add(line.toLowerCase());
		}
		compond.config().set(path, new ArrayList<String>(set));
		unload = compond.config().getBoolean("chunk.auto-unload");
		String s = "chunk.limit-total";
		int i = compond.config().getInt(s);
		if (i < 1500 || i > 10500) {
			i = 6500;
			compond.config().set(s, i);
		}
		limit = i;
	}

}

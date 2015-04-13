package com.mengcraft.protect.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.mengcraft.protect.Main;

public class ChunkEvent implements Listener {

	private final Set<String> set = new HashSet<String>();
	private final Queue<Chunk> chunks = new LinkedBlockingDeque<Chunk>();
	private boolean b;
	private final int limit;

	@EventHandler
	public void handle(ChunkLoadEvent event) {
		Chunk c = event.getChunk();
		if (b) {
			check(c);
		}
		for (Entity e : c.getEntities()) {
			check(e);
		}
		// DEBUG
		int count = 0;
		World w = event.getWorld();
		Item e = w.dropItem(w.getSpawnLocation(), new ItemStack(Material.STONE));
		Server s = e.getServer();
		e.remove();
		for (World line : s.getWorlds()) {
			count += line.getLoadedChunks().length;
		}
		System.out.println("DEBUG ChunkEvent #3 Loaded: " + count + ".");
		// DEBUG
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
				System.out.println("DEBUG ChunkEvent #1");
			} else if (d.isLoaded()) {
				b = false;
				System.out.println("DEBUG ChunkEvent #2 Disable auto unload.");
			} else {
				System.out.println("DEBUG ChunkEvent #4 Unload a chunk.");
			}
		}
		chunks.offer(c);
		System.out.println("DEBUG ChunkEvent #5 Add a chunk to queue.");
		System.out.println("DEBUG ChunkEvent #5 Size: " + chunks.size() + ".");
	}

	private void check(Entity e) {
		String name = e.getType().name().toLowerCase();
		if (set.contains(name)) {
			e.remove();
		}
	}

	public ChunkEvent(Main main) {
		String path = "manager.chunk.purge-on-unload";
		List<String> list = main.getConfig().getStringList(path);
		for (String line : list) {
			set.add(line.toLowerCase());
		}
		main.getConfig().set(path, new ArrayList<String>(set));
		b = main.getConfig().getBoolean("manager.chunk.auto-unload");
		String s = "manager.chunk.limit-total";
		int i = main.getConfig().getInt(s);
//		if (i < 1500 || i > 10500) {
//			i = 6500;
//			main.getConfig().set(s, i);
//		}
		limit = i;
	}

}

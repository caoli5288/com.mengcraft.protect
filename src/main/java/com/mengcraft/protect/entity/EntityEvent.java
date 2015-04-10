package com.mengcraft.protect.entity;

import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntityEvent implements Listener {

	private final Random rand = new Random();
	private final MetaFactory factory;
	private final int world;
	private final int chunk;
	private final int spawn;

	@EventHandler(ignoreCancelled = true)
	public void handle(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SPAWNER && rand.nextInt(100) < spawn) {
			event.setCancelled(true);
		} else if (event.getEntity().getWorld().getEntities().size() > world) {
			event.setCancelled(true);
		} else if (event.getEntity().getNearbyEntities(8, 8, 8).size() > chunk) {
			event.setCancelled(true);
		} else if (!event.getEntity().hasMetadata("protect.lifetime")) {
			Entity entity = event.getEntity();
			String type = entity.getType().name().toLowerCase();
			int life = factory.config().getInt("manager.entity.control."
					+ type + ".lifetime", -999);
			if (life == -999) {
				factory.config().set("manager.entity.control." + type + ".lifetime", -1);
			}
			entity.setMetadata("protect.lifetime", factory.create(life));
		}
	}

	public EntityEvent(MetaFactory factory) {
		world = 1500;
		chunk = 16;
		spawn = 30;
		this.factory = factory;
	}

}

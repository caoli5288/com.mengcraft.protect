package com.mengcraft.protect.entity;

import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntityEvent implements Listener {

	public static final String META_LIFE = "protect.lifetime";

	private final Random rand = new Random();
	private final MetaFactory factory;
	private final int world;
	private final int chunk;
	private final int spawn;

	@EventHandler(ignoreCancelled = true)
	public void handle(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if (event.getSpawnReason() == SPAWNER && rand.nextInt(100) < spawn
				|| entity.getWorld().getEntities().size() > world
				|| entity.getNearbyEntities(8, 8, 8).size() > chunk) {
			event.setCancelled(true);
		} else {
			lifetime(entity);
		}
	}

	public void lifetime(Entity entity) {
		String type = entity.getType().name().toLowerCase();
		int life = factory.config().getInt("control."
				+ type + ".lifetime", -1);
		if (life < 0) {
			life = entity instanceof Monster ? 12000 : 0;
			factory.config().set("control." + type + ".lifetime", life);
		}
		if (life > 0) {
			entity.setMetadata(META_LIFE, factory.create(life));
		}
	}

	public EntityEvent(MetaFactory factory) {
		this.factory = factory;
		world = factory.config().getInt("limit-world");
		chunk = factory.config().getInt("limit-chunk");
		spawn = factory.config().getInt("spawner.chance");
	}

}

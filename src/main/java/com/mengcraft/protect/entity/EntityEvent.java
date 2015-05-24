package com.mengcraft.protect.entity;

import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.mengcraft.protect.DataCompound;

public class EntityEvent implements Listener {

	private final Random rand = new Random();
	private final int world;
	private final int chunk;
	private final int spawn;
	private final DataCompound compound;

	@EventHandler(ignoreCancelled = true)
	public void handle(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if ((event.getSpawnReason() == SPAWNER && rand.nextInt(100) < spawn)
				|| compound.worldEntities(entity.getWorld().getName()) > world
				|| entity.getNearbyEntities(8, 8, 8).size() > chunk) {
			event.setCancelled(true);
		}
	}

	public EntityEvent(DataCompound compound) {
		world = compound.config().getInt("entity.limit-world");
		chunk = compound.config().getInt("entity.limit-chunk");
		spawn = compound.config().getInt("entity.spawner.chance");
		this.compound = compound;
	}

}

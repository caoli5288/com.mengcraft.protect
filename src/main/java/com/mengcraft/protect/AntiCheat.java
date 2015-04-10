package com.mengcraft.protect;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.inventory.InventoryHolder;

public class AntiCheat implements Listener {

	@EventHandler
	public void handle(BlockBreakEvent event) {
		BlockState state = event.getBlock().getState();
		if (state instanceof InventoryHolder) {
			InventoryHolder chest = (InventoryHolder) state;
			if (check(chest) > 0) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "你无法破坏使用中的器具");
			}
		}
	}

	private int check(InventoryHolder chest) {
		return chest.getInventory().getViewers().size();
	}

	@EventHandler
	public void handle(EntityPortalEvent event) {
		String type = event.getEntity().getType().name();
		if (type.contains("MINECART")) {
			event.setCancelled(true);
		}
	}
}

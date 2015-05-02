package com.mengcraft.protect.debug;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.mengcraft.protect.DataCompond;
import com.mengcraft.util.ArrayIttor;

public class Executor implements CommandExecutor {

    private final DataCompond compond;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd
            , String lable, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§6Command argument error!");
            return false;
        }
        ArrayIttor<String> ittor = new ArrayIttor<String>(args);
        String type = ittor.next();
        if (type.equals("entity")) {
            entity(sender);
            return true;
        }
        return false;
    }

    private void entity(CommandSender sender) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (World world : compond.server().getWorlds()) {
            put(map, world);
        }
        for (Entry<String, Integer> en : map.entrySet()) {
            sender.sendMessage(en.getKey() + ": " + en.getValue());
        }
        sender.sendMessage("§4TOTAL: " + s(map.values()));
    }

    private int s(Collection<Integer> values) {
        int i = 0;
        for (int value : values) {
            i += value;
        }
        return i;
    }

    private void put(Map<String, Integer> map, World world) {
        for (Entity entity : world.getEntities()) {
            put(map, entity);
        }
    }

    private void put(Map<String, Integer> map, Entity entity) {
        String name = entity.getType().name();
        if (map.get(name) != null) {
            map.put(name, map.get(name) + 01);
        } else {
            map.put(name, 1);
        }
    }

    public Executor(DataCompond compond) {
        this.compond = compond;
    }

}

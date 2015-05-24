package com.mengcraft.protect.debug;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.mengcraft.protect.DataCompound;
import com.mengcraft.util.ArrayBuilder;
import com.mengcraft.util.ArrayIttor;

public class Executor implements CommandExecutor {

    private final DataCompound compound;

    public Executor(DataCompound compond) {
        this.compound = compond;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd
            , String lable, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§6Command argument error!");
            return false;
        }
        ArrayIttor<String> it = new ArrayIttor<String>(args);
        String type = it.next();
        if (type.equals("entity")) {
            entity(sender);
        } else if (type.equals("chunk")) {
            sender.sendMessage(chunk(it));
        }
        return true;
    }

    private String[] chunk(ArrayIttor<String> it) {
        ArrayBuilder<String> builder = new ArrayBuilder<String>();
        if (it.hasNext()) {
            String act = it.next();
            if (act.equals("unload")) {
                chunkUnload();
                builder.append("§6Perform unload chunks done!");
            } else if (act.equals("clean") && it.hasNext()) {
                chunkClean(it.next());
                builder.append("§6Perform clean a chunks done!");
            }
        } else {
            int j = 0;
            for (World world : compound.worlds()) {
                int i = world.getLoadedChunks().length;
                builder.append("§6" + world.getName() + ": " + i);
                j += i;
            }
            builder.append("§4TOTAL: " + j);
        }
        return builder.build(new String[builder.length()]);
    }

    private void chunkClean(String next) {
        ArrayIttor<String> it = new ArrayIttor<String>(next.split(","));
        if (it.remain() != 3) {
            throw new IllegalArgumentException();
        }
        World world = compound.world(it.next());
        if (world != null) {
            int x = new Integer(it.next());
            int z = new Integer(it.next());
            world.regenerateChunk(x, z);
        }
    }

    private void chunkUnload() {
        for (World world : compound.worlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                chunk.unload(true, true);
            }
        }
    }

    private void entity(CommandSender sender) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (World world : compound.server().getWorlds()) {
            put(map, world);
        }
        for (Entry<String, Integer> en : map.entrySet()) {
            sender.sendMessage(en.getKey() + ": " + en.getValue());
        }
        sender.sendMessage("§4TOTAL: " + entities(map.values()));
    }

    private int entities(Collection<Integer> values) {
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

}

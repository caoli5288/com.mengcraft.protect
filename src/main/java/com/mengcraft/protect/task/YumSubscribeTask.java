package com.mengcraft.protect.task;

import com.mengcraft.protect.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Created on 16-7-24.
 */
public class YumSubscribeTask implements Runnable {

    private final Main main;

    public YumSubscribeTask(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        main.getLogger().info("Ready to subscribe Yum...");
        File target = new File("plugins/Yum.jar");
        try {
            Files.copy(new URL("http://ci.yumc.pw/job/Yum/lastSuccessfulBuild/artifact/target/Yum.jar").openStream(), target.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        main.getLogger().info("Subscribe done! Please restart server to enable it!");
    }

}

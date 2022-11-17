package com.plugin;

import com.plugin.Commands.Creeper;
import com.plugin.Commands.Enderman;
import com.plugin.Commands.Fly;
import com.plugin.Handlers.CreeperEggHandler;
import com.plugin.Handlers.EndermanEggHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class QuarkTestPlugin extends JavaPlugin {

    private GlobalRunnable globalRunnable;

    @Getter
    private static QuarkTestPlugin plugin;

    @Getter
    private static Logger jlogger;

    @Override
    public void onEnable() {

        globalRunnable = new GlobalRunnable();
        globalRunnable.runTaskTimer(this, 0, 20);

        getCommand("fly").setExecutor(new Fly());
        getCommand("creeper").setExecutor(new Creeper());
        getCommand("enderman").setExecutor(new Enderman());

        new CreeperEggHandler(this);
        new EndermanEggHandler(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public GlobalRunnable getRunnable() {
        return globalRunnable;
    }
}

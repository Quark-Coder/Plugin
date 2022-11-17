package com.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEvent implements Listener {

    private QuarkTestPlugin plugin;

    public RespawnEvent(QuarkTestPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (plugin.getRunnable().hasPlayer(event.getPlayer().getUniqueId())) {
            event.getPlayer().setLevel(plugin.getRunnable().getInactiveCooldowns(event.getPlayer().getUniqueId()));
        }
    }
}

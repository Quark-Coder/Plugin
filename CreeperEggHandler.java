package com.plugin.Handlers;

import com.plugin.QuarkTestPlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class CreeperEggHandler implements Listener {

    Player playerUsedAbility;

    QuarkTestPlugin plugin;

    double yaw;
    double pitch;
    double x;
    double y;

    boolean isFlyingByAbility;

    public CreeperEggHandler(QuarkTestPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEggUse(@NotNull PlayerInteractEvent event) {

        Player player = event.getPlayer();
        playerUsedAbility = player;

        if (player == null)
            return;

        if (player.getItemInHand().getType() == Material.CREEPER_SPAWN_EGG && (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {

            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.CREEPER_SPAWN_EGG) {
                event.setCancelled(true);
            }

            if (!plugin.getRunnable().hasPlayer(player.getUniqueId())) {
                plugin.getRunnable().addNewPlayer(player.getUniqueId());
                player.setLevel(plugin.getRunnable().MAX);
            }

            if (!plugin.getRunnable().addNewCooldown(player.getUniqueId())) {
                event.setCancelled(true);
                return;
            }

            player.setLevel(plugin.getRunnable().getInactiveCooldowns(player.getUniqueId()));

            yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;
            pitch = ((player.getLocation().getPitch() + 90) * Math.PI) / 180;

            x = Math.sin(pitch) * Math.cos(yaw);
            y = Math.sin(pitch) * Math.sin(yaw);

            World world = player.getWorld();
            Location loc = player.getLocation();

            if (player.getLevel() != 0) {
                isFlyingByAbility = false;
                world.createExplosion(loc, 2.3F, false, true);
                isFlyingByAbility = true;

            }
        }
    }

  @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (playerUsedAbility != null) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                if (String.valueOf(e.getEntity()).equals("CraftPlayer{name=" + playerUsedAbility.getName() + "}") && String.valueOf(e.getEntity().getType()).equals("PLAYER")) {
                    e.setCancelled(true);
                    e.getEntity().setVelocity(new Vector(x, 1.35, y));
                }
            }
        }

       if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && isFlyingByAbility == true) {
           e.setCancelled(true);
           isFlyingByAbility = false;
       }
    }
}



package com.plugin.Handlers;

import com.plugin.QuarkTestPlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreeperEggHandler implements Listener {

    public List<UUID> cooldowns = new ArrayList<UUID>();

    Player p;

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
    public void onEggUse(@NotNull PlayerInteractEvent e) {

        p = e.getPlayer();

        //Bukkit.broadcastMessage(String.valueOf(p.getUniqueId()));

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && p.getItemInHand().getType() == Material.CREEPER_SPAWN_EGG){
            e.setCancelled(true);
        }

        yaw = ((p.getLocation().getYaw() + 90) * Math.PI) / 180;
        pitch = ((p.getLocation().getPitch() + 90) * Math.PI) / 180;

        x = Math.sin(pitch) * Math.cos(yaw);
        y = Math.sin(pitch) * Math.sin(yaw);

        World world = p.getWorld();
        Location loc = p.getLocation();

        if (p.getLevel() != 0) {
            if (p.getItemInHand().getType() == Material.CREEPER_SPAWN_EGG && (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
                isFlyingByAbility = false;
                world.createExplosion(loc, 2.3F, false, true);
                isFlyingByAbility = true;

                p.setLevel(p.getLevel() - 1);
            }
        }




        if(cooldowns.contains(p.getUniqueId())){
            return;
        }

        if (p == null)
            return;
        if (cooldowns.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            return;
        }
        cooldowns.add(p.getUniqueId());
        new BukkitRunnable() {
            int remainingCooldown = 10;
            @Override
            public void run() {
                remainingCooldown--;
                if (remainingCooldown <= 0) {
                    cooldowns.remove(p.getUniqueId());
                    p.sendMessage(ChatColor.GREEN + "Cooldown reset!");
                    p.setLevel(p.getLevel() + 1);
                    cancel();
                }
                else {
                    p.sendMessage(ChatColor.RED + "Cooldown " + remainingCooldown + " seconds");
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }



  @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            if (String.valueOf(e.getEntity()).equals("CraftPlayer{name=" + p.getName() + "}") && String.valueOf(e.getEntity().getType()).equals("PLAYER")) {
                e.setCancelled(true);
                e.getEntity().setVelocity(new Vector(x, 1.35, y));
            }
        }

       if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && isFlyingByAbility == true) {
           e.setCancelled(true);
           isFlyingByAbility = false;
       }
    }
}



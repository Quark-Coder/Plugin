package com.plugin;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GlobalRunnable extends BukkitRunnable
{

    private HashMap<UUID, int[]> playerCooldowns = new HashMap<UUID, int[]>();
    public final int MAX = 3;
    public final int COOLDOWN_TIMER = 10; // seconds

    public boolean hasPlayer(UUID uuid) {
        return playerCooldowns.containsKey(uuid);
    }

    public void addNewPlayer(UUID uuid) {
        playerCooldowns.put(uuid, new int[MAX]);
    }

    private int getActiveCooldowns(UUID uuid) {
        int count = 0;
        for (int i = 0; i < MAX; i++) {
            if (playerCooldowns.get(uuid)[i] > 0) {
                count++;
            }
        }
        return count;
    }

    public int getInactiveCooldowns(UUID uuid) {
        return MAX - getActiveCooldowns(uuid);
    }

    private int getLeftMostIndex(UUID uuid, boolean active) {
        for (int i = 0; i < MAX; i++) {
            if (active) {
                if (playerCooldowns.get(uuid)[i] > 0) {
                    return i;
                }
            } else {
                if (playerCooldowns.get(uuid)[i] == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean addNewCooldown(UUID uuid) {
        int i = getLeftMostIndex(uuid, false);
        if (i == -1)
            return false;

        playerCooldowns.get(uuid)[i] = COOLDOWN_TIMER;
        return true;
    }

    @Override
    public void run() {
        for (UUID uuid : playerCooldowns.keySet()) {
            int leftMostIndex = getLeftMostIndex(uuid, true);

            if (leftMostIndex == -1)
                continue;

            int newCooldown = playerCooldowns.get(uuid)[leftMostIndex] - 1;
            playerCooldowns.get(uuid)[leftMostIndex] = newCooldown;

            Player player = Bukkit.getPlayer(uuid);

            if (player == null)
                continue;

            if (newCooldown == 0) {
                player.setExp(0f);
                player.setLevel(getInactiveCooldowns(uuid));
                player.sendMessage(ChatColor.GREEN + "Cooldown reset!");
            } else {
                player.setExp(1f - ((float)newCooldown / (float)COOLDOWN_TIMER));
                player.sendMessage(ChatColor.RED + "[Cooldown] " + newCooldown + "(s) remaining!");
            }
        }
    }
}

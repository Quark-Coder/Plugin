package com.plugin.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Creeper implements CommandExecutor {

    public int ammount = 1;
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;
        player.setLevel(3);

        ItemStack creeperEgg = new ItemStack(Material.CREEPER_SPAWN_EGG, ammount);
        ItemMeta creeperEggMeta = creeperEgg.getItemMeta();
        creeperEggMeta.setDisplayName("Creeper");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("Allows you to get power of a CREEPER!");
        creeperEggMeta.setLore(lore);
        creeperEgg.setItemMeta(creeperEggMeta);
        if(player.getInventory().contains(creeperEgg)){
            sender.sendMessage("You already taken ability!");
        } else {
            player.getInventory().addItem(creeperEgg);
        }
        return true;
    }
}

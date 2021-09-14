package me.proiezrush.amongus.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static void clearJustItems(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            player.getInventory().remove(item);
        }
    }

}

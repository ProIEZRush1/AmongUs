package me.proiezrush.amongus.game.arena.player.wand;

import me.proiezrush.amongus.util.items.icon.Icon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WandItem extends Icon {

    public ItemStack build() {
        return super.build("&d&lSELECTOR WAND", Material.DIAMOND_AXE, false, null);
    }
}

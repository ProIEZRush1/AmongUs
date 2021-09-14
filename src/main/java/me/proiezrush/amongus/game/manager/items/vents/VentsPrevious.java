package me.proiezrush.amongus.game.manager.items.vents;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.CustomSkullItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VentsPrevious extends CustomSkullItem {

    private final AmongUs plugin;
    private final Settings items;
    public VentsPrevious(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Impostor.Vents.VentsPrevious.Name");
        List<String> lore = items.getList("Impostor.Vents.VentsPrevious.Lore");


        return super.build(name, "https://textures.minecraft.net/texture/81c96a5c3d13c3199183e1bc7f086f54ca2a6527126303ac8e25d63e16b64ccf", lore != null, lore);
    }
}

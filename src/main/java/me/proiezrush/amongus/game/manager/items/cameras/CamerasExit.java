package me.proiezrush.amongus.game.manager.items.cameras;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CamerasExit extends ItemBuilder {

    private final AmongUs plugin;
    private final Settings items;
    public CamerasExit(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Cameras.CamerasExit.Name");
        String material = items.get(null, "Cameras.CamerasExit.Material");
        List<String> lore = items.getList("Cameras.CamerasExit.Lore");

        return super.build(name, Material.valueOf(material), lore != null, lore);
    }

}

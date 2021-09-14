package me.proiezrush.amongus.game.manager.items.crewmate;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Cameras extends ItemBuilder {

    private final AmongUs plugin;
    private final Settings items;
    public Cameras(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Crewmate.Cameras.Name");
        String material = items.get(null, "Crewmate.Cameras.Material");
        List<String> lore = items.getList("Crewmate.Cameras.Lore");

        return super.build(name, Material.valueOf(material), lore != null, lore);
    }

}

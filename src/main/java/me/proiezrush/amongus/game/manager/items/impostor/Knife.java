package me.proiezrush.amongus.game.manager.items.impostor;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.items.ItemBuilder;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Knife extends ItemBuilder {

    private final AmongUs plugin;
    private final Settings items;
    public Knife(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Impostor.Knife.Name");
        String material = items.get(null, "Impostor.Knife.Material");
        List<String> lore = items.getList("Impostor.Knife.Lore");

        return super.build(name, Material.valueOf(material), lore != null, lore);
    }
}

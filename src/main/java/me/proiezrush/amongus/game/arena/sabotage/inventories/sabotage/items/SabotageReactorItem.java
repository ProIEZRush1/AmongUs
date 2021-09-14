package me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.icon.Icon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SabotageReactorItem extends Icon {

    private final AmongUs plugin;
    private final Settings items;
    public SabotageReactorItem(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Sabotage.Sabotage.Reactor.Name");
        String material = items.get(null, "Sabotage.Sabotage.Reactor.Material");
        List<String> lore = items.getList("Sabotage.Sabotage.Reactor.Lore");

        return super.build(name, Material.getMaterial(material), lore != null, lore);
    }

}

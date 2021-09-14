package me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.icon.Icon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SabotagePaneItem extends Icon {

    private final AmongUs plugin;
    private final Settings items;
    public SabotagePaneItem(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = "";
        String material = items.get(null, "Sabotage.Sabotage.Pane.Material");

        return super.build(name, Material.getMaterial(material), false, null);
    }

}

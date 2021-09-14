package me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.icon.Icon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SabotageDoorsItem extends Icon {

    private final AmongUs plugin;
    private final Settings items;
    public SabotageDoorsItem(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Sabotage.Sabotage.Doors.Name");
        String material = items.get(null, "Sabotage.Sabotage.Doors.Material");
        List<String> lore = items.getList("Sabotage.Sabotage.Doors.Lore");
        
        return super.build(name, Material.getMaterial(material), lore != null, lore);
    }

}

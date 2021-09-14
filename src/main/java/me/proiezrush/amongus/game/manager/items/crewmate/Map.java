package me.proiezrush.amongus.game.manager.items.crewmate;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.MapItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import java.util.List;

public class Map extends MapItemBuilder {

    private final AmongUs plugin;
    private final Settings items;
    public Map(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build(Arena arena) {
        String name = items.get(null, "Crewmate.Map.Name");
        List<String> lore = items.getList("Crewmate.Map.Lore");

        return super.build(arena.getSpawn(), MapView.Scale.CLOSEST, name, lore != null, lore);
    }

}

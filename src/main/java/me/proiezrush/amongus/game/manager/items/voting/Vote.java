package me.proiezrush.amongus.game.manager.items.voting;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Vote extends ItemBuilder {

    private final AmongUs plugin;
    private final Settings items;
    public Vote(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Voting.Vote.Name");
        String material = items.get(null, "Voting.Vote.Material");
        List<String> lore = items.getList("Voting.Vote.Lore");

        return super.build(name, Material.valueOf(material), lore != null, lore);
    }

}

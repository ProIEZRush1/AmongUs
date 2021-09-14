package me.proiezrush.amongus.game.manager.items.cameras;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.CustomSkullItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CamerasPreviousGroup extends CustomSkullItem {

    private final AmongUs plugin;
    private final Settings items;
    public CamerasPreviousGroup(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Cameras.CamerasPreviousGroup.Name");
        List<String> lore = items.getList("Cameras.CamerasPreviousGroup.Lore");


        return super.build(name, "http://textures.minecraft.net/texture/efce475a4384e09e32cd8e91d09c7947adf7827307fdaddbaf2998514948fb6e", lore != null, lore);
    }

}

package me.proiezrush.amongus.game.manager.items.cameras;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.CustomSkullItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CamerasNextGroup extends CustomSkullItem {

    private final AmongUs plugin;
    private final Settings items;
    public CamerasNextGroup(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Cameras.CamerasNextGroup.Name");
        List<String> lore = items.getList("Cameras.CamerasNextGroup.Lore");

        return super.build(name, "http://textures.minecraft.net/texture/c920f0d4b2e010e80153a68b45501d054c43bb248f44bb8373640e3235679ac3", lore != null, lore);
    }

}

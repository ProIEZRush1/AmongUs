package me.proiezrush.amongus.game.manager.items.cameras;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.CustomSkullItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CamerasNext extends CustomSkullItem {

    private final AmongUs plugin;
    private final Settings items;
    public CamerasNext(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public ItemStack build() {
        String name = items.get(null, "Cameras.CamerasNext.Name");
        List<String> lore = items.getList("Cameras.CamerasNext.Lore");


        return super.build(name, "https://textures.minecraft.net/texture/333ae8de7ed079e38d2c82dd42b74cfcbd94b3480348dbb5ecd93da8b81015e3", lore != null, lore);
    }

}

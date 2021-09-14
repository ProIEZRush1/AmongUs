package me.proiezrush.amongus.game.arena.tasks.inventories.download_data.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.icon.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DownloadDataUploadItem extends Icon {

    private final AmongUs plugin;
    private final Settings items;
    private final ASCrewmate asCrewmate;
    public DownloadDataUploadItem(AmongUs plugin, ASCrewmate asCrewmate) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
        this.asCrewmate = asCrewmate;
    }

    public ItemStack build() {
        Player player = asCrewmate.getPlayer();

        String name = items.get(player, "Tasks.DOWNLOAD_DATA.Upload.Name");
        String material = items.get(player, "Tasks.DOWNLOAD_DATA.Upload.Material");
        List<String> lore = items.getList("Tasks.DOWNLOAD_DATA|.Upload.Lore");

        ItemStack item = super.build(name, Material.valueOf(material), lore != null, lore);
        setItem(item);

        return item;
    }
}
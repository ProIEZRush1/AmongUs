package me.proiezrush.amongus.game.arena.tasks.inventories.download_data.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.icon.Icon;
import me.proiezrush.amongus.util.items.icon.IconExtra;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DownloadDataProgressItem extends Icon {

    private final AmongUs plugin;
    private final Settings items;
    private final ASCrewmate asCrewmate;

    private double progress;
    public DownloadDataProgressItem(AmongUs plugin, ASCrewmate asCrewmate) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
        this.asCrewmate = asCrewmate;

        this.addIconExtra(new IconExtra("progress", progress));
    }

    public void setProgress(double progress) {
        this.progress = progress;

        this.getIconExtras().remove(getExtra("progress"));
        this.addIconExtra(new IconExtra("progress", progress));
    }

    public double getProgress() {
        return progress;
    }

    public ItemStack build() {
        Player player = asCrewmate.getPlayer();

        String name = items.get(player, "Tasks.DOWNLOAD_DATA.Progress.Name").replace("%progress%", progress + "%");
        String material = items.get(player, "Tasks.DOWNLOAD_DATA.Progress.Material");
        List<String> lore = items.getList("Tasks.DOWNLOAD_DATA.Progress.Lore");

        return super.build(name, Material.valueOf(material), lore != null, lore);
    }
}

package me.proiezrush.amongus.util.items;

import me.proiezrush.amongus.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapItemBuilder {

    private MapView mapView;
    private ItemStack item;
    private MapMeta meta;

    public MapItemBuilder() {
    }

    public ItemStack build(Location location, MapView.Scale scale, String name, boolean hasLore, List<String> lore) {
        mapView = Bukkit.createMap(Objects.requireNonNull(location.getWorld()));
        mapView.setCenterX((int) location.getX());
        mapView.setCenterZ((int) location.getZ());
        mapView.setScale(scale);

        item = new ItemStack(Material.FILLED_MAP, 1);
        meta = (MapMeta) item.getItemMeta();
        meta.setMapView(mapView);
        this.meta.setDisplayName(ChatUtil.color(name));
        if (hasLore) {
            List<String> newLore = new ArrayList<>();
            lore.forEach(a -> {
                newLore.add(ChatUtil.color(a));
            });
            this.meta.setLore(newLore);
        }
        this.item.setItemMeta(this.meta);
        return item;
    }

}

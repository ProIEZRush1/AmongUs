package me.proiezrush.amongus.util.inventory;

import me.proiezrush.amongus.util.items.icon.Icon;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryBuilder {

    protected Inventory inventory;
    private final List<Icon> icons;

    public InventoryBuilder() {
        this.icons = new ArrayList<>();
    }

    public void setup(String name, int size) {
        this.inventory = Bukkit.createInventory(null, size, name);
    }

    public void addIcon(Icon icon) {
        this.icons.add(icon);
    }

    public void removeIcon(Icon icon) {
        this.icons.remove(icon);
    }

    public Icon getIcon(ItemStack item) {
        for (Icon icon : icons) {
            if (icon.getItem().isSimilar(item)) return icon;
        }
        return null;
    }

    public List<Icon> getInventoryIcons() {
        return icons;
    }

    public void setItem(ItemStack item, int slot) {
        this.inventory.setItem(slot, item);
    }

    protected Inventory build() {
        return this.inventory;
    }

}

package me.proiezrush.amongus.util.items.icon;

import me.proiezrush.amongus.util.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Icon extends ItemBuilder {

    private ItemStack item;
    private final List<IconExtra> iconExtras;
    public Icon() {
        this.iconExtras = new ArrayList<>();
    }

    public Icon(int amount) {
        super(amount);

        this.iconExtras = new ArrayList<>();
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public void addIconExtra(IconExtra iconExtra) {
        this.iconExtras.add(iconExtra);
    }

    public void removeIconExtra(IconExtra iconExtra) {
        this.iconExtras.remove(iconExtra);
    }

    public IconExtra getExtra(String key) {
        for (IconExtra iconExtra : iconExtras) {
            if (iconExtra.getKey().equals(key)) return iconExtra;
        }
        return null;
    }

    public List<IconExtra> getIconExtras() {
        return iconExtras;
    }
}

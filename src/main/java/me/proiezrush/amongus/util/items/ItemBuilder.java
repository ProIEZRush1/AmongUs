package me.proiezrush.amongus.util.items;

import me.proiezrush.amongus.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    private int amount = 1;

    public ItemBuilder() {
    }
    public ItemBuilder(int amount) {
        this.amount = amount;
    }

    public ItemStack build(String name, Material material, boolean hasLore, List<String> lore) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
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

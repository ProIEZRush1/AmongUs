package me.proiezrush.amongus.util.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.proiezrush.amongus.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomSkullItem {

    private ItemStack item;
    private SkullMeta meta;
    private GameProfile gameProfile;

    private int amount;

    public CustomSkullItem() {
        this.amount = 1;
    }

    public CustomSkullItem(int amount) {
        this.amount = amount;
    }

    public ItemStack build(String name, String textureURL, boolean hasLore, List<String> lore) {
        item = new ItemStack(Material.PLAYER_HEAD);
        meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(name);
        if (hasLore) {
            List<String> newLore = new ArrayList<>();
            lore.forEach(a -> {
                newLore.add(ChatUtil.color(a));
            });
            this.meta.setLore(newLore);
        }

        gameProfile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", textureURL).getBytes());
        gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        Field profileField = null;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        item.setItemMeta(meta);

        return item;
    }

    public boolean isSimilar(ItemStack i, ItemStack i1) {
        if (i == null || i.getItemMeta() == null || i1 == null || i1.getItemMeta() == null) {
            return false;
        }

        ItemMeta iM = i.getItemMeta();
        ItemMeta i1M = i1.getItemMeta();

        return iM.getDisplayName().equals(i1M.getDisplayName()) && i.getType() == i1.getType();
    }
}

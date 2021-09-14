package me.proiezrush.amongus.game.manager.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Armor {

    public void armor(Player player, Color color) {
        player.getInventory().setHelmet(this.helmet(color));
        player.getInventory().setChestplate(this.chestplate(color));
        player.getInventory().setLeggings(this.leggings(color));
        player.getInventory().setBoots(this.boots(color));
    }
    
    private ItemStack helmet(Color color) {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setColor(color);
        helmet.setItemMeta(meta);

        return helmet;
    }

    private ItemStack chestplate(Color color) {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setColor(color);
        chestplate.setItemMeta(meta);

        return chestplate;
    }

    private ItemStack leggings(Color color) {
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta = (LeatherArmorMeta) leggings.getItemMeta();
        meta.setColor(color);
        leggings.setItemMeta(meta);

        return leggings;
    }

    private ItemStack boots(Color color) {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
        meta.setColor(color);
        boots.setItemMeta(meta);

        return boots;
    }
}

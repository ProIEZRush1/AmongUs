package me.proiezrush.amongus.game.manager.voting.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.util.ChatUtil;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.icon.Icon;
import me.proiezrush.amongus.util.items.icon.IconExtra;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

public class VotePlayerItem extends Icon {

    private final AmongUs plugin;
    private final Settings items;
    private final ASPlayer as;
    public VotePlayerItem(AmongUs plugin, ASPlayer as) {
        super();

        this.plugin = plugin;
        this.items = plugin.getItems();
        this.as = as;

        this.addIconExtra(new IconExtra("player_name", as.getPlayer().getName()));
    }

    public ItemStack build() {
        List<String> lore = items.getList("Voting.VotePlayer.Lore");

        Color color = as.getColor();
        String hex = ChatUtil.rgbToHex(color.getRed(), color.getGreen(), color.getBlue());
        String name = ChatUtil.hexColor(hex + as.getPlayer().getName());

        ItemStack item = super.build(name, Material.LEATHER_HELMET, lore != null, lore);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);

        this.setItem(item);
        return item;
    }

}

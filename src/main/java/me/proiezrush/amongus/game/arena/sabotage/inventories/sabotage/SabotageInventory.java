package me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.items.SabotageDoorsItem;
import me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.items.SabotageO2Item;
import me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.items.SabotagePaneItem;
import me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.items.SabotageReactorItem;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.inventory.InventoryBuilder;
import org.bukkit.inventory.Inventory;

public class SabotageInventory extends InventoryBuilder {

    private final AmongUs plugin;
    private final Settings config;
    private final String name;
    private final int size;
    public SabotageInventory(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.config = plugin.getC();
        this.name = config.get(null, "Inventories.Sabotage.Name");
        this.size = 27;
    }

    public String getName() {
        return name;
    }

    private SabotageReactorItem sabotageReactorItem;
    private SabotageO2Item sabotageO2Item;
    private SabotageDoorsItem sabotageDoorsItem;

    public Inventory build() {
        this.setup(name, size);

        SabotageReactorItem sabotageReactorItem = new SabotageReactorItem(plugin);
        this.addIcon(sabotageReactorItem);
        this.setItem(sabotageReactorItem.build(), 10);

        SabotageO2Item sabotageO2Item = new SabotageO2Item(plugin);
        this.addIcon(sabotageO2Item);
        this.setItem(sabotageO2Item.build(), 11);

        SabotageDoorsItem sabotageDoorsItem = new SabotageDoorsItem(plugin);
        this.addIcon(sabotageDoorsItem);
        this.setItem(sabotageDoorsItem.build(), 16);

        this.sabotageReactorItem = sabotageReactorItem;
        this.sabotageO2Item = sabotageO2Item;
        this.sabotageDoorsItem = sabotageDoorsItem;

        boolean fillWithPanes = config.getBoolean("Inventories.Sabotage.FillWithPanes");
        if (fillWithPanes) {
            for (int i=0;i<this.size;i++) {
                if (super.build().getItem(i) == null) setItem(new SabotagePaneItem(plugin).build(), i);
            }
        }

        return super.build();
    }

    public SabotageReactorItem getSabotageReactorItem() {
        return sabotageReactorItem;
    }

    public SabotageO2Item getSabotageO2Item() {
        return sabotageO2Item;
    }

    public SabotageDoorsItem getSabotageDoorsItem() {
        return sabotageDoorsItem;
    }
}

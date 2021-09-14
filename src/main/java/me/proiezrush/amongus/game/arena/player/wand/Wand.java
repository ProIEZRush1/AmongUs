package me.proiezrush.amongus.game.arena.player.wand;

import org.bukkit.Location;

public class Wand {

    private Location pos1;
    private Location pos2;
    private final WandItem wandItem;
    public Wand() {
        this.pos1 = null;
        this.pos2 = null;

        this.wandItem = new WandItem();
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public WandItem getWandItem() {
        return wandItem;
    }
}

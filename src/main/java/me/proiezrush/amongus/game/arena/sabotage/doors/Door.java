package me.proiezrush.amongus.game.arena.sabotage.doors;

import me.proiezrush.amongus.game.arena.loc.Loc;

public class Door {

    private final String name;
    private final Loc loc;
    private final Cuboid cuboid;
    public Door(String name, Loc loc, Cuboid cuboid) {
        this.name = name;
        this.loc = loc;
        this.cuboid = cuboid;
    }

    public String getName() {
        return name;
    }

    public Loc getLoc() {
        return loc;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }
}

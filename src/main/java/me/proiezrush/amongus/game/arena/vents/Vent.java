package me.proiezrush.amongus.game.arena.vents;

import me.proiezrush.amongus.game.arena.loc.Loc;
import org.bukkit.Location;

public class Vent {

    private final String name;
    private final Loc loc;
    private final Location location;
    public Vent(String name, Loc loc, Location location) {
        this.name = name;
        this.loc = loc;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Loc getLoc() {
        return loc;
    }

    public Location getLocation() {
        return location;
    }
}

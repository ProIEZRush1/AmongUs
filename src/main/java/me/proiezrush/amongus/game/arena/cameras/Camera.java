package me.proiezrush.amongus.game.arena.cameras;

import me.proiezrush.amongus.game.arena.loc.Loc;
import org.bukkit.Location;

public class Camera {

    private final String name;
    private final Loc loc;
    private final Location location;
    public Camera(String name, Loc loc, Location location) {
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

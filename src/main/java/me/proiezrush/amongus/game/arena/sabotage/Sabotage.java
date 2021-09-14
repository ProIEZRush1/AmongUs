package me.proiezrush.amongus.game.arena.sabotage;

import me.proiezrush.amongus.game.arena.loc.Loc;
import org.bukkit.Location;

public class Sabotage {

    private final String name;
    private final SabotageType type;
    private final Loc loc;
    private final Location location;
    private boolean isActive;
    public Sabotage(String name, SabotageType type, Loc loc, Location location) {
        this.name = name;
        this.type = type;
        this.loc = loc;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public SabotageType getType() {
        return type;
    }

    public Loc getLoc() {
        return loc;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

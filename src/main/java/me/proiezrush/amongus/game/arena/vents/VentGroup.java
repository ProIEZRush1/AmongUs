package me.proiezrush.amongus.game.arena.vents;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class VentGroup {

    private final String name;
    private List<Vent> vents;
    private final boolean random;
    public VentGroup(String name, boolean random) {
        this.name = name;
        this.vents = new ArrayList<>();
        this.random = random;
    }

    public String getName() {
        return name;
    }

    public void addVent(Vent vent) {
        this.vents.add(vent);
    }

    public void removeVent(Vent vent) {
        this.vents.remove(vent);
    }

    public void setVents(List<Vent> vents) {
        this.vents = vents;
    }

    public List<Vent> getVents() {
        return vents;
    }

    public boolean isRandom() {
        return random;
    }

    // Util methods
    public Vent getVent(Location location) {
        for (Vent vent : vents) {
            if (vent.getLocation().equals(location)) {
                return vent;
            }
        }
        return null;
    }
}

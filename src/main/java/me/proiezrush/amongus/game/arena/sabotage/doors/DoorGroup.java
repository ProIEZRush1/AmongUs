package me.proiezrush.amongus.game.arena.sabotage.doors;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DoorGroup {

    private final String name;
    private List<Door> doors;
    public DoorGroup(String name) {
        this.name = name;
        this.doors = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addDoor(Door door) {
        this.doors.add(door);
    }

    public void removeDoor(Door door) {
        this.doors.remove(door);
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public List<Door> getDoors() {
        return doors;
    }

    // Util methods
    public Door getDoor(Block block) {
        for (Door door : doors) {
            Iterator<Block> blocks = door.getCuboid().blockList();
            while (blocks.hasNext()) {
                if (blocks.next().equals(block)) {
                    return door;
                }
            }
        }
        return null;
    }
}

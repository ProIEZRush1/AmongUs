package me.proiezrush.amongus.game.arena.tasks;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.loc.Loc;
import me.proiezrush.amongus.game.arena.tasks.inventories.TaskInventory;
import me.proiezrush.amongus.game.arena.tasks.inventories.download_data.DownloadDataInventory;
import me.proiezrush.amongus.game.arena.tasks.inventories.upload_data.UploadDataInventory;
import org.bukkit.Location;

public class Task {

    private final String name;
    private final TaskType type;
    private final Loc loc;
    private final Location location;
    private boolean isDone;

    // Inventory
    private final TaskInventory taskInventory;
    public Task(AmongUs plugin, String name, TaskType type, Loc loc, Location location) {
        this.name = name;
        this.type = type;
        this.loc = loc;
        this.location = location;

        switch(type) {
            case UPLOAD_DATA:
                taskInventory = new UploadDataInventory(plugin);
                break;
            case DOWNLOAD_DATA:
                taskInventory = new DownloadDataInventory(plugin);
                break;
            default:
                taskInventory = null;
        }
    }

    public String getName() {
        return name;
    }

    public TaskType getType() {
        return type;
    }

    public Loc getLoc() {
        return loc;
    }

    public Location getLocation() {
        return location;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDone() {
        return isDone;
    }

    public TaskInventory getTaskInventory() {
        return taskInventory;
    }
}

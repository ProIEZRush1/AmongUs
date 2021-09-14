package me.proiezrush.amongus.game.manager.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.manager.items.crewmate.Cameras;
import me.proiezrush.amongus.game.manager.items.crewmate.Map;
import me.proiezrush.amongus.game.manager.items.crewmate.Task;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.entity.Player;

public class CrewmateItems {

    // Objects
    private final AmongUs plugin;
    private final Settings items;

    // Crewmates
    private ASCrewmate asCrewmate;

    private Task task;
    private Cameras cameras;
    private Map crewmateMap;

    public CrewmateItems(AmongUs plugin) {
        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public void setup(ASCrewmate asCrewmate) {
        this.asCrewmate = asCrewmate;

        this.task = new Task(plugin, asCrewmate);
        this.cameras = new Cameras(plugin);
        this.crewmateMap = new Map(plugin);
    }

    public void crewmate(Arena arena) {
        /*
         *
         *   Item list:
         *      Task
         *      Map
         *
         */

        Player p = asCrewmate.getPlayer();

        int taskSlot = items.getInt("Crewmate.Tasks.Slot");
        int camerasSlot = items.getInt("Crewmate.Cameras.Slot");
        int mapSlot = items.getInt("Crewmate.Map.Slot");

        p.getInventory().setItem(taskSlot, task.build());
        p.getInventory().setItem(camerasSlot, cameras.build());
        p.getInventory().setItem(mapSlot, crewmateMap.build(arena));
    }

    public void task() {
        Player p = asCrewmate.getPlayer();

        int taskSlot = items.getInt("Crewmate.Tasks.Slot");

        p.getInventory().setItem(taskSlot, task.build());
    }

    public Task getTask() {
        return task;
    }

    public Cameras getCameras() {
        return cameras;
    }

    public Map getMap() {
        return crewmateMap;
    }

}

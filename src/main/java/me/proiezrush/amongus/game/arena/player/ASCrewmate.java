package me.proiezrush.amongus.game.arena.player;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.tasks.Task;
import me.proiezrush.amongus.game.arena.tasks.TaskGroup;
import me.proiezrush.amongus.game.manager.items.CrewmateItems;

import java.util.ArrayList;
import java.util.List;


public class ASCrewmate extends ASPlayer {

    private final AmongUs plugin;

    // Objects
    private CrewmateItems items;
    private TaskGroup assignedTasks;

    public ASCrewmate(AmongUs plugin, AS as) {
        super(plugin, as);

        this.plugin = plugin;

        this.items = new CrewmateItems(plugin);
    }

    public CrewmateItems getCrewmateItems() {
        this.items.setup(this);
        return items;
    }

    public void setAssignedTasks(TaskGroup assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public TaskGroup getAssignedTasks() {
        return assignedTasks;
    }

    /* USED TO GET CURRENT TASK ITEM POSITION */
    private final List<String> itemTasks = new ArrayList<>();

    public void addItemTask(String s) {
        itemTasks.add(s);
    }

    public void removeItemTask(String s) {
        itemTasks.remove(s);
    }

    public List<String> getItemTasks() {
        return itemTasks;
    }

    /* USED TO GET SCOREBOARD TASK STATIC */
    private final List<String> staticScoreboardTasks = new ArrayList<>();

    public void addStaticScoreboardTask(String s) {
        staticScoreboardTasks.add(s);
    }

    public void removeStaticScoreboardTask(String s) {
        staticScoreboardTasks.remove(s);
    }

    public List<String> getStaticScoreboardTasks() {
        return staticScoreboardTasks;
    }

    /* USED TO GET SCOREBOARD TASK SCROLL */
    private final List<String> scrollScoreboardTasks = new ArrayList<>();

    public void addScrollScoreboardTask(String s) {
        scrollScoreboardTasks.add(s);
    }

    public void removeScrollScoreboardTask(String s) {
        scrollScoreboardTasks.remove(s);
    }

    public List<String> getScrollScoreboardTasks() {
        return scrollScoreboardTasks;
    }

    /* USED TO GET THE LAST OPENED TASK */
    private Task lastTask;

    public void setLastTask(Task lastTask) {
        this.lastTask = lastTask;
    }

    public Task getLastTask() {
        return lastTask;
    }
}

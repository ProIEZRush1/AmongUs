package me.proiezrush.amongus.game.arena.tasks;

import me.proiezrush.amongus.game.arena.loc.Loc;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class TaskGroup {

    private final String name;
    private List<Task> tasks;
    public TaskGroup(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    // Util methods
    public Task getTask(Location location) {
        for (Task task : tasks) {
            if (task.getLocation().equals(location)) {
                return task;
            }
        }
        return null;
    }

    public int getTaskNumber(TaskType type) {
        int i = 0;
        for (Task task : tasks) {
            if (!task.isDone()) {
                if (task.getType() == type) {
                    i++;
                }
            }
        }
        return i;
    }

    public List<String> getRealTasks() {
        List<String> realTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isDone()) {
                if (!realTasks.contains(task.getType().toString())) {
                    realTasks.add(task.getType().toString());
                }
            }
        }
        return realTasks;
    }

    public List<Loc> getLocs(TaskType type) {
        List<Loc> locs = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getType().equals(type)) {
                locs.add(task.getLoc());
            }
        }
        return locs;
    }
}

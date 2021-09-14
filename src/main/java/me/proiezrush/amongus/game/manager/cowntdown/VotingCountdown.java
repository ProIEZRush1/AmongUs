package me.proiezrush.amongus.game.manager.cowntdown;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class VotingCountdown implements Countdown {

    private final AmongUs plugin;
    private final Arena arena;
    private final int totalTime;
    private double time;
    private int taskID;
    public VotingCountdown(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;
        this.totalTime = 15 * 20;
        this.time = totalTime;
    }

    public void start() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            if (arena.voting()) {
                if (time == 0) {
                    plugin.getArenaManager().gettArenaManager().play(arena);
                    stop();
                }
                time--;
            }
            else stop();
        }, 0L, 1);
    }

    public void stop() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTask(taskID);
    }

    public int getTotalTime() {
        return totalTime;
    }

    public double getTime() {
        return time;
    }
}

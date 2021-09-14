package me.proiezrush.amongus.game.manager.cowntdown;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.ArenaPhase;
import me.proiezrush.amongus.game.arena.ArenaStatus;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;


public class FinishCountdown implements Countdown {

    private final AmongUs plugin;
    private final Arena arena;
    private final List<ASPlayer> players;
    private int time;
    private int taskID;
    public FinishCountdown(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;
        this.players = new ArrayList<>(arena.getPlayers());
        this.time = 10;
    }

    public void start() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            if (arena.finishing()) {
                if (time == 0) {
                    ScoreboardManager scoreboardManager = arena.getScoreboardManager();

                    for (ASPlayer as : players) {
                        plugin.getArenaManager().gettArenaManager().leave(as, arena);

                        // Remove finishig scoreboard
                        scoreboardManager.removeFinishingScoreboard(as.getPlayer());
                    }
                    arena.setStatus(ArenaStatus.WAITING);
                    arena.setPhase(ArenaPhase.WAITING);
                    stop();
                }
                time--;
            }
            else stop();
        }, 0L, 20L);
    }

    public void stop() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTask(taskID);
    }

}

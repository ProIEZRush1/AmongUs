package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.scoreboard.FinishingScoreboard;
import me.proiezrush.amongus.game.manager.scoreboard.game.GameScoreboard;
import me.proiezrush.amongus.game.manager.scoreboard.Scoreboard;
import me.proiezrush.amongus.game.manager.scoreboard.WaitingScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

public class ScoreboardManager {

    private final AmongUs plugin;
    private final Arena arena;

    private final HashMap<UUID, Scoreboard> waitingScoreboards;
    private final HashMap<UUID, Scoreboard> gameScoreboards;
    private final HashMap<UUID, Scoreboard> finishingScoreboards;

    private final HashMap<UUID, Integer> waitingTasks;
    private final HashMap<UUID, Integer> gameTasks;
    private final HashMap<UUID, Integer> finishingTasks;
    public ScoreboardManager(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;

        this.waitingScoreboards = new HashMap<>();
        this.gameScoreboards = new HashMap<>();
        this.finishingScoreboards = new HashMap<>();

        this.waitingTasks = new HashMap<>();
        this.gameTasks = new HashMap<>();
        this.finishingTasks = new HashMap<>();
    }

    public void setWaitingScoreboard(Player player) {
        WaitingScoreboard waitingScoreboard = new WaitingScoreboard(plugin, arena);
        waitingScoreboards.put(player.getUniqueId(), waitingScoreboard);
        waitingScoreboard.scoreboard(player);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        waitingTasks.put(player.getUniqueId(), scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            waitingScoreboards.get(player.getUniqueId()).scoreboard(player);
        }, 0L, 20L));
    }

    public void removeWaitingScoreboard(Player player) {
        if (waitingScoreboards.get(player.getUniqueId()) != null) {
            waitingScoreboards.get(player.getUniqueId()).remove(player);
            waitingScoreboards.remove(player.getUniqueId());

            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.cancelTask(waitingTasks.get(player.getUniqueId()));
        }
    }

    public void setGameScoreboard(ASPlayer as) {
        Player player = as.getPlayer();

        GameScoreboard gameScoreboard = new GameScoreboard(plugin, arena);
        gameScoreboards.put(player.getUniqueId(), gameScoreboard);
        gameScoreboard.scoreboard(as);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        gameTasks.put(player.getUniqueId(), scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            gameScoreboards.get(player.getUniqueId()).scoreboard(as);
        }, 0L, 20L));
    }

    public void removeGameScoreboard(Player player) {
        if (gameScoreboards.get(player.getUniqueId()) != null) {
            gameScoreboards.get(player.getUniqueId()).remove(player);
            gameScoreboards.remove(player.getUniqueId());

            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.cancelTask(gameTasks.get(player.getUniqueId()));
        }
    }

    public void setFinishingScoreboard(Player player) {
        FinishingScoreboard finishingScoreboard = new FinishingScoreboard(plugin, arena);
        finishingScoreboards.put(player.getUniqueId(), finishingScoreboard);
        finishingScoreboard.scoreboard(player);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        finishingTasks.put(player.getUniqueId(), scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            finishingScoreboards.get(player.getUniqueId()).scoreboard(player);
        }, 0L, 20L));
    }

    public void removeFinishingScoreboard(Player player) {
        if (finishingScoreboards.get(player.getUniqueId()) != null) {
            finishingScoreboards.get(player.getUniqueId()).remove(player);
            finishingScoreboards.remove(player.getUniqueId());

            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.cancelTask(finishingTasks.get(player.getUniqueId()));
        }
    }

}

package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.manager.bossbar.Bossbar;
import me.proiezrush.amongus.game.manager.bossbar.DiscussionBossbar;
import me.proiezrush.amongus.game.manager.bossbar.TasksBossbar;
import me.proiezrush.amongus.game.manager.bossbar.VotingBossbar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class BossbarManager {

    private final AmongUs plugin;
    private final Arena arena;

    private final HashMap<String, Bossbar> bars;
    private int tasksTask;
    private int discussionTask;
    private int votingTask;
    public BossbarManager(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;

        this.bars = new HashMap<>();
    }

    public void startTasksBar() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        tasksTask = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            for (AS as : arena.getPlayers()) {
                Player player = as.getPlayer();
                removeTasksBar(player);
                setTasksBar(player);
            }
        }, 0L, 20L);
    }

    public void stopTasksBar() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTask(tasksTask);

        for (AS as : arena.getPlayers()) {
            removeTasksBar(as.getPlayer());
        }
    }

    public void setTasksBar(Player player) {
        this.bars.putIfAbsent("tasks", new TasksBossbar(plugin, arena));
        this.bars.get("tasks").bossbar().addPlayer(player);
    }

    public void removeTasksBar(Player player) {
        if (this.bars.get("tasks") != null) this.bars.get("tasks").bossbar().removePlayer(player);
    }

    public void startDiscussionBar() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        discussionTask = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            for (AS as : arena.getPlayers()) {
                Player player = as.getPlayer();
                removeDiscussionBar(player);
                setDiscussionBar(player);
            }
        }, 0L, 1);
    }

    public void stopDiscussionBar() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTask(discussionTask);

        for (AS as : arena.getPlayers()) {
            removeDiscussionBar(as.getPlayer());
        }
    }

    public void setDiscussionBar(Player player) {
        this.bars.putIfAbsent("discussion", new DiscussionBossbar(plugin, arena));
        this.bars.get("discussion").bossbar().addPlayer(player);
    }

    public void removeDiscussionBar(Player player) {
        if (this.bars.get("discussion") != null) this.bars.get("discussion").bossbar().removePlayer(player);
    }

    public void startVotingBar() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        votingTask = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            for (AS as : arena.getPlayers()) {
                Player player = as.getPlayer();
                removeVotingBar(player);
                setVotingBar(player);
            }
        }, 0L, 1);
    }

    public void stopVotingBar() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTask(votingTask);

        for (AS as : arena.getPlayers()) {
            removeVotingBar(as.getPlayer());
        }
    }

    public void setVotingBar(Player player) {
        this.bars.putIfAbsent("voting", new VotingBossbar(plugin, arena));
        this.bars.get("voting").bossbar().addPlayer(player);
    }

    public void removeVotingBar(Player player) {
        if (this.bars.get("voting") != null) this.bars.get("voting").bossbar().removePlayer(player);
    }

}

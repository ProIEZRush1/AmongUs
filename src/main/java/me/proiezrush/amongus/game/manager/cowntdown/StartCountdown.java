package me.proiezrush.amongus.game.manager.cowntdown;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class StartCountdown implements Countdown {

    private final AmongUs plugin;
    private final Settings config;

    private final Arena arena;
    private int time;
    private int taskID;
    public StartCountdown(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.config = plugin.getC();

        this.arena = arena;
        this.time = 10;
    }

    public void start() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            if (arena.starting()) {
                if (time == 0) {
                    plugin.getArenaManager().gettArenaManager().start(arena);
                    stop();
                }
                else if (time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    msg();

                    for (AS as : arena.getPlayers()) {
                        Player player = as.getPlayer();

                        // Play start countdown sound
                        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.StartCountdown")));
                    }
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

    private void msg() {
        for (AS as : arena.getPlayers()) {
            Player p = as.getPlayer();
            p.sendMessage(plugin.getMessages().get(p, "GameStarting").replace("%time%", String.valueOf(time)));
        }
    }

}

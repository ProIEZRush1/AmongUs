package me.proiezrush.amongus.game.manager.bossbar;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class TasksBossbar implements Bossbar {

    private final Settings config;
    private final Arena arena;
    private BossBar bossBar;
    public TasksBossbar(AmongUs plugin, Arena arena) {
        this.config = plugin.getC();
        this.arena = arena;
    }

    @Override
    public BossBar bossbar() {
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(config.get(null, "Bossbars.Tasks.Title"), BarColor.GREEN, BarStyle.SOLID);
        }

        double a = arena.getMadeTasks() * 100;
        double b = arena.getTotalTasks();
        double c = b == 0 ? a : a / b;
        double d = c / 100;

        bossBar.setProgress(d);
        return bossBar;
    }
}

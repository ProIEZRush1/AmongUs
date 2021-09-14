package me.proiezrush.amongus.game.manager.bossbar;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.manager.cowntdown.VotingCountdown;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class VotingBossbar implements Bossbar {

    private final Settings config;
    private final Arena arena;
    private BossBar bossBar;
    public VotingBossbar(AmongUs plugin, Arena arena) {
        this.config = plugin.getC();
        this.arena = arena;
    }

    @Override
    public BossBar bossbar() {
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(config.get(null, "Bossbars.Voting.Title"), BarColor.WHITE, BarStyle.SOLID);
        }
        VotingCountdown votingCountdown = (VotingCountdown) arena.getCountdownManager().getCountdown(VotingCountdown.class);

        double a = votingCountdown.getTime() * 100;
        double b = a / votingCountdown.getTotalTime();
        double c = b / 100;
        double d = c < 0 ? 0 : c;

        bossBar.setProgress(d);
        return bossBar;
    }
}
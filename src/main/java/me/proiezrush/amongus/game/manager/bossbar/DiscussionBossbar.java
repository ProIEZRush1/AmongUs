package me.proiezrush.amongus.game.manager.bossbar;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.manager.cowntdown.DiscussionCountdown;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class DiscussionBossbar implements Bossbar {

    private final Settings config;
    private final Arena arena;
    private BossBar bossBar;
    public DiscussionBossbar(AmongUs plugin, Arena arena) {
        this.config = plugin.getC();
        this.arena = arena;
    }

    @Override
    public BossBar bossbar() {
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(config.get(null, "Bossbars.Discussion.Title"), BarColor.WHITE, BarStyle.SOLID);
        }
        DiscussionCountdown discussionCountdown = (DiscussionCountdown) arena.getCountdownManager().getCountdown(DiscussionCountdown.class);

        double a = discussionCountdown.getTime() * 100;
        double b = a / discussionCountdown.getTotalTime();
        double c = b / 100;
        double d = c < 0 ? 0 : c;

        bossBar.setProgress(d);
        return bossBar;
    }

}

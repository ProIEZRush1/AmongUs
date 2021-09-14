package me.proiezrush.amongus.game.arena.cooldown;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class KnifeCooldown implements Cooldown {

    private final AmongUs plugin;
    private final ArenaManager arenaManager;
    private final Settings items;

    private final ASImpostor asImpostor;
    private final int time;
    private boolean isIn;
    public KnifeCooldown(AmongUs plugin, ASImpostor asImpostor, int time) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
        this.items = plugin.getItems();

        this.asImpostor = asImpostor;
        this.time = time;
    }

    @Override
    public void start() {
        isIn = true;

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            isIn = false;

            // Give knife again if valid
            if (arenaManager.getArena(asImpostor) != null) {
                Arena arena = arenaManager.getArena(asImpostor);
                if (arena.game()) {
                    if (!asImpostor.isDeath()) {
                        Player player = asImpostor.getPlayer();
                        int knifeSlot = items.getInt("Impostor.Knife.Slot");
                        player.getInventory().setItem(knifeSlot, asImpostor.getImpostorItems().getKnife().build());
                    }
                }
            }
        }, time * 20L);
    }

    @Override
    public boolean isIn() {
        return isIn;
    }
}

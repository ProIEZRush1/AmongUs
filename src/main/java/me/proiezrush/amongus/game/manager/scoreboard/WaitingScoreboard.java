package me.proiezrush.amongus.game.manager.scoreboard;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.util.ChatUtil;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WaitingScoreboard implements Scoreboard {

    private final Settings config;
    private final Arena arena;
    public WaitingScoreboard(AmongUs plugin, Arena arena) {
        this.config = plugin.getC();
        this.arena = arena;
    }

    @Override
    public BPlayerBoard scoreboard(Player player) {
        BPlayerBoard scoreboard = Netherboard.instance().getBoard(player);
        if (scoreboard == null) scoreboard = Netherboard.instance().createBoard(player, config.get(player, "Scoreboards.Waiting.Title"));

        List<String> lines = config.getList("Scoreboards.Waiting.Lines");
        List<String> newLines = new ArrayList<>();
        for (String s : lines) {
            newLines.add(replaceAll(player, s));
        }
        scoreboard.setAll(newLines.toArray(new String[0]));

        return scoreboard;
    }

    @Override
    public void remove(Player player) {
        BPlayerBoard scoreboard = Netherboard.instance().getBoard(player);
        if (scoreboard != null) scoreboard.delete();
    }

    private String replaceAll(Player player, String s) {
        String text = ChatUtil.parsePlaceholders(player, s);
        text = text.replace("%status%", ChatUtil.color(arena.getStatus().getColorCode() + arena.getStatus().toString()));
        text = text.replace("%players%", String.valueOf(arena.getActualPlayers()));
        text = text.replaceAll("%max_players%", String.valueOf(arena.getMaxPlayers()));

        return text;
    }
}

package me.proiezrush.amongus.game.manager.scoreboard.game;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.scoreboard.Scoreboard;
import me.proiezrush.amongus.util.ChatUtil;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScoreboard implements Scoreboard {

    private final AmongUs plugin;
    private final Settings config;

    private final Arena arena;
    public GameScoreboard(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.config = plugin.getC();

        this.arena = arena;
    }


    @Override
    public BPlayerBoard scoreboard(ASPlayer as) {
        Player player = as.getPlayer();

        BPlayerBoard scoreboard = Netherboard.instance().getBoard(player);
        if (scoreboard == null) scoreboard = Netherboard.instance().createBoard(player, config.get(player, "Scoreboards.Game.Title"));

        List<String> lines = config.getList("Scoreboards.Game.Lines");
        List<String> newLines = new ArrayList<>();
        for (String s : lines) {
            newLines.add(replaceAll(as, s));
        }
        TasksReplacer tasksReplacer = new TasksReplacer(plugin, arena, as);
        newLines = tasksReplacer.replaceTasks(newLines);
        scoreboard.setAll(newLines.toArray(new String[0]));

        return scoreboard;
    }

    @Override
    public void remove(Player player) {
        BPlayerBoard scoreboard = Netherboard.instance().getBoard(player);
        if (scoreboard != null) scoreboard.delete();
    }

    private String replaceAll(ASPlayer as, String s) {
        String text = ChatUtil.parsePlaceholders(as.getPlayer(), s);
        text = text.replace("%type%", as.getType().toString());
        text = text.replace("%players%", String.valueOf(arena.getActualPlayers()));
        text = text.replace("%max_players%", String.valueOf(arena.getMaxPlayers()));

        return text;
    }
    
}
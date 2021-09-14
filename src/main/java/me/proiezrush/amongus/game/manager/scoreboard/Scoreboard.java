package me.proiezrush.amongus.game.manager.scoreboard;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import org.bukkit.entity.Player;

public interface Scoreboard {

    default BPlayerBoard scoreboard(Player player) {
        return null;
    }
    default BPlayerBoard scoreboard(ASPlayer as) {
        return null;
    }

    void remove(Player player);


}

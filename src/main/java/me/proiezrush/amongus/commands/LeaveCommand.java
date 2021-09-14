package me.proiezrush.amongus.commands;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.ASManager;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.util.ChatUtil;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveCommand implements CommandExecutor {

    private final AmongUs plugin;
    private final ASManager asManager;
    private final ArenaManager arenaManager;
    private final Settings messages;
    public LeaveCommand(AmongUs plugin) {
        this.plugin = plugin;
        this.asManager = plugin.getAsManager();
        this.arenaManager = plugin.getArenaManager();
        this.messages = plugin.getMessages();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("leave")) {
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;

                if (p.hasPermission("amongus.leave")) {
                    AS as = asManager.getAS(p);

                    // Check if you are in arena
                    if (arenaManager.getArena(as) == null) {
                        p.sendMessage(messages.get(p, "NotInArena"));
                        return true;
                    }
                    Arena arena = arenaManager.getArena(as);
                    ASPlayer asPlayer = arena.getPlayer(as);

                    arenaManager.gettArenaManager().leave(asPlayer, arena);
                }
                else p.sendMessage(messages.get(p, "NoPermission"));
            }
            else commandSender.sendMessage(messages.get(null, "NotPlayer"));
        }
        return false;
    }
}

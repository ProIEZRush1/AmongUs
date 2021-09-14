package me.proiezrush.amongus.commands;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.manager.ASManager;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.util.ChatUtil;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinCommand implements CommandExecutor {

    private final AmongUs plugin;
    private final ASManager asManager;
    private final ArenaManager arenaManager;
    private final Settings messages;
    public JoinCommand(AmongUs plugin) {
        this.plugin = plugin;
        this.asManager = plugin.getAsManager();
        this.arenaManager = plugin.getArenaManager();
        this.messages = plugin.getMessages();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("join")) {
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;

                if (p.hasPermission("amongus.join")) {
                    AS as = asManager.getAS(p);

                    if (args.length == 1) {
                        String a = args[0];

                        // Check if arena exists
                        if (arenaManager.getArena(a) == null) {
                            p.sendMessage(messages.get(p, "ArenaNotExists"));
                            return true;
                        }
                        // Check if arena is in game
                        if (arenaManager.getArena(a).inGame()) {
                            p.sendMessage(messages.get(p, "ArenaInGame"));
                            return true;
                        }
                        //Check if arena is disabled
                        if (arenaManager.getArena(a).disabled()) {
                            p.sendMessage(messages.get(p, "ArenaNotExists"));
                            return true;
                        }
                        // Check if you are not in arena
                        if (arenaManager.getArena(as) != null) {
                            p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                            return true;
                        }

                        arenaManager.gettArenaManager().join(as, arenaManager.getArena(a));
                    }
                    else help(p);
                }
                else p.sendMessage(messages.get(p, "NoPermission"));
            }
            else commandSender.sendMessage(messages.get(null, "NotPlayer"));
        }
        return false;
    }

    public void help(Player p) {
        List<String> help = messages.getList("JoinCommandHelp");
        help.forEach(a -> {
            p.sendMessage(ChatUtil.parsePlaceholders(p, a));
        });
    }
}

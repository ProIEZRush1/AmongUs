package me.proiezrush.amongus.commands;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AmongUsCommand implements CommandExecutor {

    private final Settings config;
    private final Settings items;
    private final Settings messages;
    private final Settings arenas;
    private final Settings players;
    public AmongUsCommand(AmongUs plugin) {
        this.config = plugin.getC();
        this.items = plugin.getItems();
        this.messages = plugin.getMessages();
        this.arenas = plugin.getArenas();
        this.players = plugin.getPlayers();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("amongus")) {
            config.reload();
            items.reload();
            messages.reload();
            arenas.reload();
            players.reload();

            commandSender.sendMessage(messages.get(null, "PluginReloaded"));
        }
        return false;
    }
}

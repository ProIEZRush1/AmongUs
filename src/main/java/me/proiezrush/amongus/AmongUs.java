package me.proiezrush.amongus;

import me.proiezrush.amongus.commands.AmongUsCommand;
import me.proiezrush.amongus.commands.ArenaCommand;
import me.proiezrush.amongus.commands.JoinCommand;
import me.proiezrush.amongus.commands.LeaveCommand;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.ASManager;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.listeners.in.BasicInListeners;
import me.proiezrush.amongus.listeners.in.InGameInventoryListeners;
import me.proiezrush.amongus.listeners.in.InGameItemListeners;
import me.proiezrush.amongus.listeners.in.InGameListeners;
import me.proiezrush.amongus.listeners.out.OutListeners;
import me.proiezrush.amongus.util.CommandRegisterer;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.nms.reader.PacketReader;
import me.proiezrush.amongus.util.nms.reader.PacketReaderManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;

public class AmongUs extends JavaPlugin {

    public boolean running;

    // Objects
    private Settings config;
    private Settings items;
    private Settings messages;
    private Settings arenas;
    private Settings players;

    private ArenaManager arenaManager;
    private ASManager asManager;

    @Override
    public void onEnable() {
        this.running = true;

        File f = new File("plugins/AmongUs");
        if (!f.exists()) f.mkdir();

        this.config = new Settings(this, "config", false, true);
        this.items = new Settings(this, "items", false, true);
        this.messages = new Settings(this, "messages", false, true);
        this.arenas = new Settings(this, "arenas", false, true);
        this.players = new Settings(this, "players", false, true);

        this.arenaManager = new ArenaManager(this);
        this.arenaManager.deserialize();

        this.asManager = new ASManager(this);

        // Listeners
        registerListener(new OutListeners(this));
        registerListener(new BasicInListeners(this));
        registerListener(new InGameListeners(this));
        registerListener(new InGameItemListeners(this));
        registerListener(new InGameInventoryListeners(this));

        // Commands
        registerCommand(new CommandRegisterer("amongus", new AmongUsCommand(this)));
        registerCommand(new CommandRegisterer("arena", new ArenaCommand(this)));
        registerCommand(new CommandRegisterer("join", new JoinCommand(this)));
        registerCommand(new CommandRegisterer("leave", new LeaveCommand(this)));

        // Fetch all players
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (!players.isEmpty()) {
            for (Player player : players) {
                AS as = asManager.deserialize(player.getName());
                if (as == null) {
                    as = new AS(player);
                    asManager.addAS(as);
                }

                PacketReader packetReader = PacketReaderManager.packetReader();
                as.setPacketReader(packetReader);
                packetReader.inject(this, as);
            }
        }
    }

    @Override
    public void onDisable() {
        this.running = false;

        this.arenaManager.serialize();

        // Save all players
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (!players.isEmpty()) {
            for (Player player : players) {
                AS as = asManager.getAS(player);

                if (arenaManager.getArena(as) != null) {
                    Arena arena = arenaManager.getArena(as);
                    ASPlayer asPlayer = arena.getPlayer(as);

                    arenaManager.gettArenaManager().leave(asPlayer, arena);
                }

                as.getPacketReader().uninject(player);
                asManager.serialize(as);
            }
        }
    }

    private void registerListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    private void registerCommand(CommandRegisterer registerer) {
        this.getCommand(registerer.getCommand()).setExecutor(registerer.getExecutor());
    }

    public Settings getC() {
        return config;
    }

    public Settings getItems() {
        return items;
    }

    public Settings getMessages() {
        return messages;
    }

    public Settings getArenas() {
        return arenas;
    }

    public Settings getPlayers() {
        return players;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public ASManager getAsManager() {
        return asManager;
    }
}

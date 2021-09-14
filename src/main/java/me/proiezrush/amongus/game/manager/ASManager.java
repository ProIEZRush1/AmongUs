package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ASManager {

    private final AmongUs plugin;
    private final Settings config;
    private final Settings players;
    private final Set<AS> playerSet;
    public ASManager(AmongUs plugin) {
        this.plugin = plugin;
        this.config = plugin.getC();
        this.players = plugin.getPlayers();
        this.playerSet = new HashSet<>();
    }

    public AS deserialize(String name) {
        ConfigurationSection a = players.getConfig().getConfigurationSection("Players");
        if (a == null) return null;

        int wins = a.getInt(name + ".Wins");
        int kills = a.getInt(name + ".Kills");
        int played = a.getInt(name + ".Played");
        int impostor = a.getInt(name + ".Impostor");
        int crewmate = a.getInt(name + ".Crewmate");

        AS as = new AS(Bukkit.getPlayer(name));
        as.setWins(wins);
        as.setKills(kills);
        as.setPlayed(played);
        as.setImpostor(impostor);
        as.setCrewmate(crewmate);

        playerSet.add(as);
        return as;
    }

    public void serialize(AS as) {
        if (!playerSet.contains(as)) return;

        players.set("Players." + as.getPlayer().getName() + ".Wins", as.getWins());
        players.set("Players." + as.getPlayer().getName() + ".Kills", as.getKills());
        players.set("Players." + as.getPlayer().getName() + ".Played", as.getPlayed());
        players.set("Players." + as.getPlayer().getName() + ".Impostor", as.getImpostor());
        players.set("Players." + as.getPlayer().getName() + ".Crewmate", as.getCrewmate());
        players.save();
    }

    public AS getAS(Player player) {
        return playerSet.stream().filter(as -> as.getPlayer().equals(player)).findFirst().orElse(null);
    }

    public AS getAS(String name) {
        return playerSet.stream().filter(as -> as.getPlayer().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void addAS(AS as) {
        playerSet.add(as);
    }

    public void removeAS(AS as) {
        playerSet.remove(as);
    }

    public Set<AS> getPlayerList() {
        return playerSet;
    }
}

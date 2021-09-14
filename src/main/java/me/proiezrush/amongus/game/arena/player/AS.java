package me.proiezrush.amongus.game.arena.player;

import me.proiezrush.amongus.game.arena.player.wand.Wand;
import me.proiezrush.amongus.util.ChatUtil;
import me.proiezrush.amongus.util.nms.reader.PacketReader;
import org.bukkit.entity.Player;

public class AS {

    // Objects
    private final Player player;
    private PacketReader packetReader;

    private final ASData asData;

    // Setup objects
    private final Wand wand;

    // Stats Objects
    private int wins;
    private int kills;
    private int played;
    private int impostor;
    private int crewmate;

    public AS(Player player) {
        this.player = player;
        this.asData = new ASData(this);

        this.wand = new Wand();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPacketReader(PacketReader packetReader) {
        this.packetReader = packetReader;
    }

    public PacketReader getPacketReader() {
        return packetReader;
    }

    public ASData getAsData() {
        return asData;
    }

    public Wand getWand() {
        return wand;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getImpostor() {
        return impostor;
    }

    public void setImpostor(int impostor) {
        this.impostor = impostor;
    }

    public int getCrewmate() {
        return crewmate;
    }

    public void setCrewmate(int crewmate) {
        this.crewmate = crewmate;
    }

    // Util methods
    public String getDisplay(boolean a) {
        String display = "";
        if (!a) display += "&k";
        display += this.getPlayer().getName();

        return ChatUtil.color(display);
    }
}

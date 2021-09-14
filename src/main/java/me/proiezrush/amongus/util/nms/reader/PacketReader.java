package me.proiezrush.amongus.util.nms.reader;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.AS;
import org.bukkit.entity.Player;

public interface PacketReader {

    void inject(AmongUs plugin, AS as);
    void uninject(Player player);

}

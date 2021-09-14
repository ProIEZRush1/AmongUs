package me.proiezrush.amongus.util.nms.corpses;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import org.bukkit.Bukkit;

public class CorpseManager {

    public static void spawn(AmongUs plugin, Arena arena, ASPlayer as) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        ICorpse corpse;

        switch (version) {
            case "v1_17_R1":
                corpse = new Corpse_v1_17_R1(plugin, as);
                break;
            default:
                return;
        }

        corpse.spawn(arena.getPlayers());
        arena.getCorpses().add(corpse);
    }

    public static void removeAll(Arena arena) {
        for (ICorpse corpse : arena.getCorpses()) {
            corpse.remove(arena.getPlayers());
        }
        arena.getCorpses().clear();
    }

}

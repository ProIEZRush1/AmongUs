package me.proiezrush.amongus.listeners.in;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.ASManager;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.game.manager.items.ImpostorItems;
import me.proiezrush.amongus.game.manager.items.impostor.Knife;
import me.proiezrush.amongus.util.nms.corpses.events.CorpseClickEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class InGameListeners implements Listener {

    private final AmongUs plugin;
    private final ASManager asManager;
    private final ArenaManager arenaManager;
    public InGameListeners(AmongUs plugin) {
        this.plugin = plugin;
        this.asManager = plugin.getAsManager();
        this.arenaManager = plugin.getArenaManager();
    }

    /* CHAT LISTENERS */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);

        if (arenaManager.getArena(as) == null) return;

        Arena arena = arenaManager.getArena(as);

        if (arena.discussion()) return;

        e.setCancelled(true);
    }

    /* KILL LISTENERS */
    @EventHandler
    public void onKnife(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity en = e.getRightClicked();

        if (!(en instanceof Player)) return;

        Player clicked = (Player) en;
        AS as = asManager.getAS(player);
        AS as1 = asManager.getAS(clicked);

        if (arenaManager.getArena(as) == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isImpostor(as)) return;
        if (!arena.isCrewmate(as1)) return;

        ASImpostor asImpostor = arena.getImpostor(as);
        ASCrewmate asCrewmate = arena.getCrewmate(as1);
        ItemStack item = player.getItemInHand();
        ImpostorItems impostorItems = asImpostor.getImpostorItems();
        Knife knife = impostorItems.getKnife();

        if (!item.isSimilar(knife.build())) return;

        // Kill
        plugin.getArenaManager().gettArenaManager().kill(asImpostor, asCrewmate, arena);
        e.setCancelled(true);
    }

    /* REPORT LISTENERS */
    @EventHandler
    public void onReport(CorpseClickEvent e) {
        Player reporter = e.getPlayer();
        AS asReporter = asManager.getAS(reporter);
        Arena arena = arenaManager.getArena(asReporter);
        ASPlayer asPlayerReporter = arena.getPlayer(asReporter);

        if (asPlayerReporter.isDeath()) return;

        AS asVictim = e.getCorpse().getASPlayer();
        ASPlayer asPlayerVictim = arena.getPlayer(asVictim);

        if (!arena.game()) return;

        arenaManager.gettArenaManager().report(asPlayerReporter, asPlayerVictim, arena);
    }

    /* DEATH LISTENERS */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        AS as = asManager.getAS(player);

        if (arenaManager.getArena(as) == null) return;

        e.setDeathMessage(null);
        e.getDrops().clear();
    }

    /* CAMERA LISTENERS */
    @EventHandler
    public void onMoveWhileCamera(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);

        if (arenaManager.getArena(as) == null) return;

        Arena arena = arenaManager.getArena(as);
        ASPlayer asPlayer = arena.getPlayer(as);

        if (!asPlayer.isCameras()) return;

        e.setCancelled(true);
    }

    /* VENT LISTENERS */
    @EventHandler
    public void onMoveWhileVent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);

        if (arenaManager.getArena(as) == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isImpostor(as)) return;

        ASImpostor asImpostor = arena.getImpostor(as);

        if (!asImpostor.isVents()) return;

        e.setCancelled(true);
    }

}

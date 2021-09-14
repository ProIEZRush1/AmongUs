package me.proiezrush.amongus.listeners.out;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.arena.player.wand.WandItem;
import me.proiezrush.amongus.game.manager.ASManager;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.nms.reader.PacketReader;
import me.proiezrush.amongus.util.nms.reader.PacketReaderManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class OutListeners implements Listener {

    private final AmongUs plugin;
    private final Settings messages;
    private final ASManager asManager;
    private final ArenaManager arenaManager;
    public OutListeners(AmongUs plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.asManager = plugin.getAsManager();
        this.arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        AS as = asManager.deserialize(p.getName());
        if (as == null) {
            as = new AS(p);
            asManager.addAS(as);
        }

        PacketReader packetReader = PacketReaderManager.packetReader();
        as.setPacketReader(packetReader);
        packetReader.inject(plugin, as);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        AS as = asManager.getAS(p);

        as.getPacketReader().uninject(p);
        asManager.serialize(as);

        if (arenaManager.getArena(as) == null) return;

        Arena arena = arenaManager.getArena(as);
        ASPlayer asPlayer = arena.getPlayer(as);

        arenaManager.gettArenaManager().leave(asPlayer, arena);
    }

    // Wand listeners
    @EventHandler
    public void onWand(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        AS as = asManager.getAS(p);
        Block block = e.getClickedBlock();

        if (arenaManager.getArena(as) == null) return;
        if (block == null) return;

        EquipmentSlot hand = e.getHand();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (hand != EquipmentSlot.HAND) return;
        if (!item.isSimilar(as.getWand().getWandItem().build())) return;

        Action action = e.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            as.getWand().setPos2(block.getLocation());

            p.sendMessage(messages.get(p, "Pos2Set"));
        }
        if (action == Action.LEFT_CLICK_BLOCK) {
            as.getWand().setPos1(block.getLocation());

            p.sendMessage(messages.get(p, "Pos1Set"));
        }
        e.setCancelled(true);
    }
}

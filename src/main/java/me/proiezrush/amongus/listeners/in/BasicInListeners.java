package me.proiezrush.amongus.listeners.in;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.manager.ASManager;
import me.proiezrush.amongus.game.manager.ArenaManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class BasicInListeners implements Listener {

    private final AmongUs plugin;
    private final ASManager asManager;
    private final ArenaManager arenaManager;
    public BasicInListeners(AmongUs plugin) {
        this.plugin = plugin;
        this.asManager = plugin.getAsManager();
        this.arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity en = e.getEntity();

        if (!(en instanceof Player)) return;

        Player p = (Player) en;
        AS as = asManager.getAS(p);

        if (arenaManager.getArena(as) == null) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity en = e.getEntity();

        if (!(en instanceof Player)) return;

        Player p = (Player) en;
        AS as = asManager.getAS(p);

        if (arenaManager.getArena(as) == null) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        Entity en = e.getEntity();

        if (!(en instanceof Player)) return;

        Player p = (Player) en;
        AS as = asManager.getAS(p);

        if (arenaManager.getArena(as) == null) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        AS as = asManager.getAS(p);

        if (arenaManager.getArena(as) == null) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        AS as = asManager.getAS(p);

        if (arenaManager.getArena(as) == null) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        AS as = asManager.getAS(p);

        if (arenaManager.getArena(as) == null) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Entity en = e.getWhoClicked();

        if (!(en instanceof Player)) return;

        Player p = (Player) en;
        AS as = asManager.getAS(p);

        if (arenaManager.getArena(as) == null) return;

        Inventory inventory = e.getInventory();

        if (inventory.getHolder() == null) return;
        if (!inventory.getHolder().equals(p.getInventory().getHolder())) return;

        e.setCancelled(true);
    }

}
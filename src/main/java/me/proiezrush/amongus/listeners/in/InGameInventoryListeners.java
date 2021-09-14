package me.proiezrush.amongus.listeners.in;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.arena.tasks.Task;
import me.proiezrush.amongus.game.arena.tasks.inventories.TaskInventory;
import me.proiezrush.amongus.game.arena.tasks.inventories.download_data.DownloadDataInventory;
import me.proiezrush.amongus.game.arena.tasks.inventories.upload_data.UploadDataInventory;
import me.proiezrush.amongus.game.manager.ASManager;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.game.manager.TaskManager;
import me.proiezrush.amongus.game.manager.voting.VotingInventory;
import me.proiezrush.amongus.util.items.icon.Icon;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InGameInventoryListeners implements Listener {

    private final AmongUs plugin;
    private final ASManager asManager;
    private final ArenaManager arenaManager;
    public InGameInventoryListeners(AmongUs plugin) {
        this.plugin = plugin;
        this.asManager = plugin.getAsManager();
        this.arenaManager = plugin.getArenaManager();
    }

    /* VOTING INVENTORY LISTENERS */
    @EventHandler
    public void onVotingInventory(InventoryClickEvent e) {
        HumanEntity he = e.getWhoClicked();
        Inventory inventory = e.getClickedInventory();
        InventoryView inventoryView = e.getView();
        ItemStack item = e.getCurrentItem();

        if (!(he instanceof Player)) return;

        Player voter = (Player) he;
        AS asVoter = asManager.getAS(voter);

        if (arenaManager.getArena(asVoter) == null) return;
        if (inventory == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(asVoter);
        VotingInventory votingInventory = arena.getVotingManager().getVotingInventory();

        if (!inventoryView.getTitle().equals(votingInventory.getName())) return;

        String votedName = (String) votingInventory.getIcon(item).getExtra("player_name").getExtra();
        Player voted = Bukkit.getPlayer(votedName);
        AS asVoted = asManager.getAS(voted);

        ASPlayer asPlayerVoter = arena.getPlayer(asVoter);
        ASPlayer asPlayerVoted = arena.getPlayer(asVoted);

        // Vote player
        arena.getVotingManager().vote(arena, asPlayerVoter, asPlayerVoted);
    }

    /* UPLOAD_DATA TASKS */
    @EventHandler
    public void onUploadDataTask(InventoryClickEvent e) {
        HumanEntity he = e.getWhoClicked();
        Inventory inventory = e.getClickedInventory();
        InventoryView inventoryView = e.getView();
        ItemStack item = e.getCurrentItem();

        if (!(he instanceof Player)) return;

        Player player = (Player) he;
        AS as = asManager.getAS(player);

        if (arenaManager.getArena(as) == null) return;
        if (inventory == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isCrewmate(as)) return;

        ASCrewmate asCrewmate = arena.getCrewmate(as);
        Task lastTask = asCrewmate.getLastTask();

        if (lastTask == null) return;

        TaskInventory taskInventory = lastTask.getTaskInventory();

        if (!isTaskInventory(taskInventory, UploadDataInventory.class)) return;

        UploadDataInventory uploadDataInventory = (UploadDataInventory) taskInventory;

        if (!inventoryView.getTitle().equals(uploadDataInventory.getName())) return;

        Icon filesUploadItem = uploadDataInventory.getFilesUploadItem();

        if (!item.isSimilar(filesUploadItem.getItem())) return;

        uploadDataInventory.start(asCrewmate);
        e.setCancelled(true);

        if (isTaskInventory(taskInventory, DownloadDataInventory.class)) {
            DownloadDataInventory downloadDataInventory = (DownloadDataInventory) taskInventory;
            if (inventoryView.getTitle().equals(downloadDataInventory.getName())) {
                Icon filesDownloadItem = downloadDataInventory.getFilesUploadItem();
                if (item.isSimilar(filesDownloadItem.getItem())) {
                    downloadDataInventory.start(asCrewmate);
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDownloadDataTask(InventoryClickEvent e) {
        HumanEntity he = e.getWhoClicked();
        Inventory inventory = e.getClickedInventory();
        InventoryView inventoryView = e.getView();
        ItemStack item = e.getCurrentItem();

        if (!(he instanceof Player)) return;

        Player player = (Player) he;
        AS as = asManager.getAS(player);

        if (arenaManager.getArena(as) == null) return;
        if (inventory == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isCrewmate(as)) return;

        ASCrewmate asCrewmate = arena.getCrewmate(as);
        Task lastTask = asCrewmate.getLastTask();

        if (lastTask == null) return;

        TaskInventory taskInventory = lastTask.getTaskInventory();

        if (!isTaskInventory(taskInventory, DownloadDataInventory.class)) return;

        DownloadDataInventory downloadDataInventory = (DownloadDataInventory) taskInventory;

        if (!inventoryView.getTitle().equals(downloadDataInventory.getName())) return;

        Icon filesDownloadItem = downloadDataInventory.getFilesUploadItem();

        if (!item.isSimilar(filesDownloadItem.getItem())) return;

        downloadDataInventory.start(asCrewmate);
        e.setCancelled(true);
    }

    /* RESET LAST TASK */
    @EventHandler
    public void onCloseTask(InventoryCloseEvent e) {
        HumanEntity he = e.getPlayer();
        InventoryView inventoryView = e.getView();

        if (!(he instanceof Player)) return;

        Player player = (Player) he;
        AS as = asManager.getAS(player);

        if (arenaManager.getArena(as) == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isCrewmate(as)) return;

        ASCrewmate asCrewmate = arena.getCrewmate(as);
        Task lastTask = asCrewmate.getLastTask();

        if (lastTask == null) return;

        TaskInventory taskInventory = lastTask.getTaskInventory();

        if (!inventoryView.getTitle().equals(taskInventory.getName())) return;

        TaskManager taskManager = arena.getTaskManager();
        taskManager.resetTaskInventory(asCrewmate, lastTask);
    }

    private boolean isTaskInventory(TaskInventory taskInventory, Class<?> cast) {
        return cast.isAssignableFrom(taskInventory.getClass());
    }
}

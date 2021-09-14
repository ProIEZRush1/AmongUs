package me.proiezrush.amongus.game.arena.tasks.inventories.download_data;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.tasks.TaskType;
import me.proiezrush.amongus.game.arena.tasks.inventories.TaskChangeableInventory;
import me.proiezrush.amongus.game.arena.tasks.inventories.download_data.items.DownloadDataProgressItem;
import me.proiezrush.amongus.game.arena.tasks.inventories.download_data.items.DownloadDataUploadItem;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.game.manager.TaskManager;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.inventory.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.atomic.AtomicInteger;

public class DownloadDataInventory extends InventoryBuilder implements TaskChangeableInventory {

    private final AmongUs plugin;
    private final Settings config;
    private final ArenaManager arenaManager;
    private final String name;
    public DownloadDataInventory(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.config = plugin.getC();
        this.arenaManager = plugin.getArenaManager();
        this.name = config.get(null, "Inventories.Tasks.DOWNLOAD_DATA.Name");
    }

    @Override
    public String getName() {
        return name;
    }

    private DownloadDataUploadItem downloadDataUploadItem;
    private DownloadDataProgressItem downloadDataProgressItem;
    public Inventory build(ASCrewmate asCrewmate) {
        if (inventory == null) {
            set(name, 9, asCrewmate);
        }
        return inventory;
    }

    private void set(String name, int size, ASCrewmate asCrewmate) {
        this.setup(name, size);

        DownloadDataUploadItem downloadDataUploadItem = new DownloadDataUploadItem(plugin, asCrewmate);
        this.addIcon(downloadDataUploadItem);
        this.setItem(downloadDataUploadItem.build(), 0);

        this.downloadDataUploadItem = downloadDataUploadItem;
        this.downloadDataProgressItem = null;
    }

    private int taskID;
    @Override
    public void start(ASCrewmate asCrewmate) {
        AtomicInteger slot = new AtomicInteger(1);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            change(asCrewmate, slot);
        }, 0L, 20L);
    }

    private void change(ASCrewmate asCrewmate, AtomicInteger slot) {
        if (slot.get() == 9) {
            stop(asCrewmate, true);
            return;
        }
        if (downloadDataProgressItem == null) {
            this.downloadDataProgressItem = new DownloadDataProgressItem(plugin, asCrewmate);
        }
        this.downloadDataProgressItem.setProgress(this.downloadDataProgressItem.getProgress() + (100D / 8D));
        this.removeIcon(this.downloadDataProgressItem);
        this.addIcon(this.downloadDataProgressItem);
        this.setItem(this.downloadDataProgressItem.build(), slot.get());
        slot.getAndIncrement();
    }

    @Override
    public void stop(ASCrewmate asCrewmate, boolean complete) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTask(taskID);

        if (complete) {
            // Complete task
            Arena arena = arenaManager.getArena(asCrewmate);
            TaskManager taskManager = arena.getTaskManager();
            taskManager.complete(asCrewmate, asCrewmate.getLastTask());
        }
    }

    @Override
    public void reset(ASCrewmate asCrewmate) {
        set(name, 9, asCrewmate);
        stop(asCrewmate, false);
    }

    public DownloadDataUploadItem getFilesUploadItem() {
        return downloadDataUploadItem;
    }

    public DownloadDataProgressItem getFilesProgressItem() {
        return downloadDataProgressItem;
    }

    @Override
    public TaskType getType() {
        return TaskType.DOWNLOAD_DATA;
    }
}

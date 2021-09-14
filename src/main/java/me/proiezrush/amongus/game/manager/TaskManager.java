package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.tasks.Task;
import me.proiezrush.amongus.game.arena.tasks.TaskType;
import me.proiezrush.amongus.game.arena.tasks.inventories.TaskInventory;
import me.proiezrush.amongus.game.arena.tasks.inventories.download_data.DownloadDataInventory;
import me.proiezrush.amongus.game.arena.tasks.inventories.upload_data.UploadDataInventory;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.SoundUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TaskManager {

    private final AmongUs plugin;
    private final ArenaManager arenaManager;
    private final Settings config;

    private final Arena arena;
    public TaskManager(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
        this.config = plugin.getC();

        this.arena = arena;
    }

    public void openTaskInventory(ASCrewmate asCrewmate, Task task) {
        asCrewmate.setLastTask(task);

        Player player = asCrewmate.getPlayer();

        TaskType taskType = task.getType();
        TaskInventory taskInventory = task.getTaskInventory();

        switch (taskType) {
            case UPLOAD_DATA:
                UploadDataInventory uploadDataInventory = (UploadDataInventory) taskInventory;
                player.openInventory(uploadDataInventory.build(asCrewmate));
                break;
            case DOWNLOAD_DATA:
                DownloadDataInventory downloadDataInventory = (DownloadDataInventory) taskInventory;
                player.openInventory(downloadDataInventory.build(asCrewmate));
                break;
        }

        SoundUtil.playSound(player, Sound.valueOf(config.get(player, "Sounds.TaskOpened")));
    }

    public void resetTaskInventory(ASCrewmate asCrewmate, Task task) {
        TaskType taskType = task.getType();
        TaskInventory taskInventory = task.getTaskInventory();

        switch (taskType) {
            case UPLOAD_DATA:
                UploadDataInventory uploadDataInventory = (UploadDataInventory) taskInventory;
                uploadDataInventory.reset(asCrewmate);
                break;
            case DOWNLOAD_DATA:
                DownloadDataInventory downloadDataInventory = (DownloadDataInventory) taskInventory;
                downloadDataInventory.reset(asCrewmate);
        }
    }

    public void complete(ASCrewmate asCrewmate, Task task) {
        task.setDone(true);

        Player player = asCrewmate.getPlayer();
        player.closeInventory();

        // Check if finish arena
        Arena arena = arenaManager.getArena(asCrewmate);
        if (arena.finish()) arenaManager.gettArenaManager().finish(arena, false);
        else {
            // Set task item
            asCrewmate.getCrewmateItems().getTask().recheck();
            asCrewmate.getCrewmateItems().task();
        }

        SoundUtil.playSound(player, Sound.valueOf(config.get(player, "Sounds.TaskCompleted")));
    }
}

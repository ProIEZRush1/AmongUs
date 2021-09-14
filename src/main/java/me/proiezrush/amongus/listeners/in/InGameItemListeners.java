package me.proiezrush.amongus.listeners.in;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.SabotageInventory;
import me.proiezrush.amongus.game.arena.tasks.TaskGroup;
import me.proiezrush.amongus.game.arena.vents.Vent;
import me.proiezrush.amongus.game.arena.vents.VentGroup;
import me.proiezrush.amongus.game.manager.*;
import me.proiezrush.amongus.game.manager.items.CrewmateItems;
import me.proiezrush.amongus.game.manager.items.ImpostorItems;
import me.proiezrush.amongus.game.manager.items.Items;
import me.proiezrush.amongus.game.manager.items.cameras.*;
import me.proiezrush.amongus.game.manager.items.crewmate.Task;
import me.proiezrush.amongus.game.manager.items.impostor.FakeTask;
import me.proiezrush.amongus.game.manager.items.impostor.Sabotage;
import me.proiezrush.amongus.game.manager.items.vents.VentsExit;
import me.proiezrush.amongus.game.manager.items.vents.VentsNext;
import me.proiezrush.amongus.game.manager.items.vents.VentsPrevious;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InGameItemListeners implements Listener {

    private final AmongUs plugin;
    private final ASManager asManager;
    private final ArenaManager arenaManager;

    public InGameItemListeners(AmongUs plugin) {
        this.plugin = plugin;
        this.asManager = plugin.getAsManager();
        this.arenaManager = plugin.getArenaManager();
    }

    /*
     *
     *   GENERAL ITEMS
     *
     */

    /* VOTING ITEM INTERACT */
    @EventHandler
    public void onVotingItem(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(as);
        ASPlayer asPlayer = arena.getPlayer(as);

        if (!item.isSimilar(asPlayer.getVotingItems().getVote().build())) return;

        player.openInventory(arena.getVotingManager().getVotingInventory().build(arena));
        e.setCancelled(true);
    }

    /* CAMERAS ITEMS INTERACT */
    @EventHandler
    public void onCameraItems(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(as);
        ASPlayer asPlayer = arena.getPlayer(as);

        if (!asPlayer.isCameras()) return;

        CameraManager cameraManager = arena.getCameraManager();
        Items items = asPlayer.getItems();

        CamerasNextGroup camerasNextGroup = items.getCamerasNextGroup();
        if (camerasNextGroup.isSimilar(item, camerasNextGroup.build())) {
            cameraManager.nextCameraGroup(asPlayer);
        }

        CamerasNext camerasNext = items.getCamerasNext();
        if (camerasNext.isSimilar(item, camerasNext.build())) {
            cameraManager.nextCamera(asPlayer);
        }

        CamerasExit camerasExit = items.getCamerasExit();
        if (item.isSimilar(camerasExit.build())) {
            cameraManager.exitCameras(asPlayer);
        }

        CamerasPrevious camerasPrevious = items.getCamerasPrevious();
        if (camerasPrevious.isSimilar(item, camerasPrevious.build())) {
            cameraManager.previousCamera(asPlayer);
        }

        CamerasPreviousGroup camerasPreviousGroup = items.getCamerasPreviousGroup();
        if (camerasPreviousGroup.isSimilar(item, camerasPreviousGroup.build())) {
            cameraManager.previousCameraGroup(asPlayer);
        }
    }

    /*
    *
    *   IMPOSTOR ITEMS
    *
     */

    /* FAKE TASK ITEM CHANGE */
    @EventHandler
    public void onFakeItem(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();
        Action action = e.getAction();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;
        if (action != Action.LEFT_CLICK_BLOCK && action != Action.LEFT_CLICK_AIR) return;

        Arena arena = arenaManager.getArena(as);
        if (!arena.isImpostor(as)) return;

        ASImpostor asImpostor = arena.getImpostor(as);
        ImpostorItems impostorItems = asImpostor.getImpostorItems();
        FakeTask fakeTask = impostorItems.getFakeTask();

        if (!item.isSimilar(fakeTask.build())) return;

        fakeTask.change();
        impostorItems.fakeTask();
        e.setCancelled(true);
    }

    /* CAMERAS ITEM INTERACT */
    @EventHandler
    public void onCamerasImpostor(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(as);
        CameraManager cameraManager = arena.getCameraManager();

        if (!arena.isImpostor(as)) return;

        ASImpostor asImpostor = arena.getImpostor(as);
        me.proiezrush.amongus.game.manager.items.impostor.Cameras cameras = asImpostor.getImpostorItems().getCameras();

        if (!item.isSimilar(cameras.build())) return;

        // Enter cameras
        cameraManager.enterCameras(arena.getPlayer(asImpostor));
    }

    /* VENTS ITEM INTERACT */
    @EventHandler
    public void onVents(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();
        Block block = e.getClickedBlock();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;
        if (block == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isImpostor(as)) return;

        ASImpostor asImpostor = arena.getImpostor(as);

        if (asImpostor.isVents()) return;

        VentGroup ventGroup = arena.getVentGroup(block.getLocation());

        if (ventGroup == null) return;

        me.proiezrush.amongus.game.manager.items.impostor.Vent ventItem = asImpostor.getImpostorItems().getVent();

        if (!item.isSimilar(ventItem.build())) return;

        VentManager ventManager = arena.getVentManager();
        Vent vent = ventGroup.getVent(block.getLocation());
        ventManager.enterVents(asImpostor, vent);
    }

    /* VENTS ITEMS INTERACT WHEN VENTS */
    @EventHandler
    public void onVentItems(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isImpostor(as)) return;

        ASImpostor asImpostor = arena.getImpostor(as);

        if (!asImpostor.isVents()) return;

        VentManager ventManager = arena.getVentManager();
        ImpostorItems impostorItems = asImpostor.getImpostorItems();

        VentsNext camerasNext = impostorItems.getVentsNext();
        if (camerasNext.isSimilar(item, camerasNext.build())) {
            ventManager.nextVent(asImpostor);
        }

        VentsExit camerasExit = impostorItems.getVentsExit();
        if (item.isSimilar(camerasExit.build())) {
            ventManager.exitVents(asImpostor);
        }

        VentsPrevious camerasPrevious = impostorItems.getVentsPrevious();
        if (camerasPrevious.isSimilar(item, camerasPrevious.build())) {
            ventManager.previousVent(asImpostor);
        }
    }

    /* SABOTAGE ITEM INTERACT */
    @EventHandler
    public void onSabotageItem(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isImpostor(as)) return;

        ASImpostor asImpostor = arena.getImpostor(as);
        ImpostorItems impostorItems = asImpostor.getImpostorItems();
        Sabotage sabotage = impostorItems.getSabotage();

        if (!item.isSimilar(sabotage.build())) return;

        SabotageInventory sabotageInventory = asImpostor.getSabotageInventory();
        player.openInventory(sabotageInventory.build());
    }

    /*
    *
    *   CREWMATE ITEMS
    *
     */

    /* TASK ITEM CHANGE */
    @EventHandler
    public void onTaskItemChange(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();
        Block block = e.getClickedBlock();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;
        if (block == null) return;

        Arena arena = arenaManager.getArena(as);
        if (!arena.isCrewmate(as)) return;

        ASCrewmate asCrewmate = arena.getCrewmate(as);
        if (asCrewmate.getAssignedTasks().getTask(block.getLocation()) != null) return;

        CrewmateItems crewmateItems = asCrewmate.getCrewmateItems();
        Task task = crewmateItems.getTask();

        if (!item.isSimilar(task.build())) return;

        task.change();
        crewmateItems.task();
        e.setCancelled(true);
    }

    /* TASK ITEM INTERACT */
    @EventHandler
    public void onTaskItem(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();
        Block block = e.getClickedBlock();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;
        if (block == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isCrewmate(as)) return;

        ASCrewmate asCrewmate = arena.getCrewmate(as);
        TaskGroup taskGroup = asCrewmate.getAssignedTasks();

        if (taskGroup.getTask(block.getLocation()) == null) return;

        CrewmateItems crewmateItems = asCrewmate.getCrewmateItems();
        Task taskItem = crewmateItems.getTask();

        if (!item.isSimilar(taskItem.build())) return;

        me.proiezrush.amongus.game.arena.tasks.Task task = taskGroup.getTask(block.getLocation());

        if (taskItem.getType() != task.getType()) return;
        if (task.isDone()) return;

        TaskManager taskManager = arena.getTaskManager();
        taskManager.openTaskInventory(asCrewmate, task);
        e.setCancelled(true);
    }

    /* TASK ITEM INTERACT WHEN DEATH */
    @EventHandler
    public void onTaskItemDeath(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        Block block = e.getClickedBlock();

        if (arenaManager.getArena(as) == null) return;
        if (block == null) return;

        Arena arena = arenaManager.getArena(as);

        if (!arena.isCrewmate(as)) return;

        ASCrewmate asCrewmate = arena.getCrewmate(as);

        if (!asCrewmate.isDeath()) return;

        TaskGroup taskGroup = asCrewmate.getAssignedTasks();

        if (taskGroup.getTask(block.getLocation()) == null) return;

        me.proiezrush.amongus.game.arena.tasks.Task task = taskGroup.getTask(block.getLocation());

        if (task.isDone()) return;

        TaskManager taskManager = arena.getTaskManager();
        taskManager.openTaskInventory(asCrewmate, task);
        e.setCancelled(true);
    }

    /* CAMERAS ITEM LISTENERS */
    @EventHandler
    public void onCamerasCrewmate(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        AS as = asManager.getAS(player);
        ItemStack item = e.getItem();

        if (arenaManager.getArena(as) == null) return;
        if (item == null) return;

        Arena arena = arenaManager.getArena(as);
        CameraManager cameraManager = arena.getCameraManager();

        if (!arena.isCrewmate(as)) return;

        ASCrewmate asCrewmate = arena.getCrewmate(as);
        me.proiezrush.amongus.game.manager.items.crewmate.Cameras cameras = asCrewmate.getCrewmateItems().getCameras();

        if (!item.isSimilar(cameras.build())) return;

        // Enter cameras
        cameraManager.enterCameras(arena.getPlayer(asCrewmate));
    }
}

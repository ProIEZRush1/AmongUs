package me.proiezrush.amongus.game.manager.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.items.cameras.*;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.entity.Player;

public class Items {

    private final AmongUs plugin;
    private final Settings items;

    private ASPlayer asPlayer;

    // Cameras
    private CamerasNextGroup camerasNextGroup;
    private CamerasNext camerasNext;
    private CamerasExit camerasExit;
    private CamerasPrevious camerasPrevious;
    private CamerasPreviousGroup camerasPreviousGroup;

    public Items(AmongUs plugin) {
        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public void setup(ASPlayer asPlayer) {
        this.asPlayer = asPlayer;

        this.camerasNextGroup = new CamerasNextGroup(plugin);
        this.camerasPrevious = new CamerasPrevious(plugin);
        this.camerasNext = new CamerasNext(plugin);
        this.camerasExit = new CamerasExit(plugin);
        this.camerasPreviousGroup = new CamerasPreviousGroup(plugin);
    }

    public void cameras() {
        Player p = asPlayer.getPlayer();

        int camerasNextGroupSlot = items.getInt("Cameras.CamerasNextGroup.Slot");
        int camerasNextSlot = items.getInt("Cameras.CamerasNext.Slot");
        int camerasExitSlot = items.getInt("Cameras.CamerasExit.Slot");
        int camerasPreviousSlot = items.getInt("Cameras.CamerasPrevious.Slot");
        int camerasPreviousGroupSlot = items.getInt("Cameras.CamerasPreviousGroup.Slot");

        p.getInventory().setItem(camerasNextGroupSlot, camerasNextGroup.build());
        p.getInventory().setItem(camerasNextSlot, camerasNext.build());
        p.getInventory().setItem(camerasExitSlot, camerasExit.build());
        p.getInventory().setItem(camerasPreviousSlot, camerasPrevious.build());
        p.getInventory().setItem(camerasPreviousGroupSlot, camerasPreviousGroup.build());
    }

    public void vents() {

    }

    public CamerasNextGroup getCamerasNextGroup() {
        return camerasNextGroup;
    }

    public CamerasPrevious getCamerasPrevious() {
        return camerasPrevious;
    }

    public CamerasNext getCamerasNext() {
        return camerasNext;
    }

    public CamerasExit getCamerasExit() {
        return camerasExit;
    }

    public CamerasPreviousGroup getCamerasPreviousGroup() {
        return camerasPreviousGroup;
    }
}

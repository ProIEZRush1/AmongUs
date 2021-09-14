package me.proiezrush.amongus.game.manager.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.game.manager.items.impostor.*;
import me.proiezrush.amongus.game.manager.items.vents.VentsExit;
import me.proiezrush.amongus.game.manager.items.vents.VentsNext;
import me.proiezrush.amongus.game.manager.items.vents.VentsPrevious;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.entity.Player;

public class ImpostorItems {

    // Objects
    private final AmongUs plugin;
    private final Settings items;

    // Impostor
    private ASImpostor asImpostor;

    private Knife knife;
    private FakeTask fakeTask;
    private Vent vent;
    private Sabotage sabotage;
    private Cameras cameras;
    private Map impostorMap;

    // Vents
    private VentsNext ventsNext;
    private VentsExit ventsExit;
    private VentsPrevious ventsPrevious;

    public ImpostorItems(AmongUs plugin) {
        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public void setup(ASImpostor asImpostor) {
        this.asImpostor = asImpostor;

        this.knife = new Knife(plugin);
        this.fakeTask = new FakeTask(plugin, asImpostor);
        this.vent = new Vent(plugin);
        this.sabotage = new Sabotage(plugin);
        this.cameras = new Cameras(plugin);
        this.impostorMap = new Map(plugin);

        this.ventsNext = new VentsNext(plugin);
        this.ventsExit = new VentsExit(plugin);
        this.ventsPrevious = new VentsPrevious(plugin);
    }

    public void impostor(Arena arena) {
        /*
         *
         *   Item list:
         *       Knife
         *       Fake task
         *       Vent
         *       Map
         *
         */

        Player player = asImpostor.getPlayer();

        int knifeSlot = items.getInt("Impostor.Knife.Slot");
        int fakeTaskSlot = items.getInt("Impostor.FakeTask.Slot");
        int ventSlot = items.getInt("Impostor.Vent.Slot");
        int sabotageSlot = items.getInt("Impostor.Sabotage.Slot");
        int camerasSlot = items.getInt("Impostor.Cameras.Slot");
        int mapSlot = items.getInt("Impostor.Map.Slot");

        if (!asImpostor.getKnifeCooldown().isIn()) player.getInventory().setItem(knifeSlot, knife.build());
        player.getInventory().setItem(fakeTaskSlot, fakeTask.build());
        player.getInventory().setItem(ventSlot, vent.build());
        player.getInventory().setItem(sabotageSlot, sabotage.build());
        player.getInventory().setItem(camerasSlot, cameras.build());
        player.getInventory().setItem(mapSlot, impostorMap.build(arena));
    }

    public void fakeTask() {
        Player player = asImpostor.getPlayer();

        int fakeTaskSlot = items.getInt("Impostor.FakeTask.Slot");

        player.getInventory().setItem(fakeTaskSlot, fakeTask.build());
    }

    public void vents() {
        Player player = asImpostor.getPlayer();

        int ventsNextSlot = items.getInt("Impostor.Vents.VentsNext.Slot");
        int ventsExitSlot = items.getInt("Impostor.Vents.VentsExit.Slot");
        int ventsPreviousSlot = items.getInt("Impostor.Vents.VentsPrevious.Slot");

        player.getInventory().setItem(ventsNextSlot, ventsNext.build());
        player.getInventory().setItem(ventsExitSlot, ventsExit.build());
        player.getInventory().setItem(ventsPreviousSlot, ventsPrevious.build());
    }

    public Knife getKnife() {
        return knife;
    }

    public FakeTask getFakeTask() {
        return fakeTask;
    }

    public Vent getVent() {
        return vent;
    }

    public Sabotage getSabotage() {
        return sabotage;
    }

    public Cameras getCameras() {
        return cameras;
    }

    public Map getMap() {
        return impostorMap;
    }

    public VentsNext getVentsNext() {
        return ventsNext;
    }

    public VentsExit getVentsExit() {
        return ventsExit;
    }

    public VentsPrevious getVentsPrevious() {
        return ventsPrevious;
    }

}

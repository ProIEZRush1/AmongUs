package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.cameras.Camera;
import me.proiezrush.amongus.game.arena.cameras.CameraGroup;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.util.NumUtil;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.SoundUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CameraManager {

    private final AmongUs plugin;
    private final ArenaManager arenaManager;
    private final Settings config;

    private final Arena arena;
    public CameraManager(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
        this.config = plugin.getC();

        this.arena = arena;
    }

    public void enterCameras(ASPlayer asPlayer) {
        Player player = asPlayer.getPlayer();

        asPlayer.setCameras(true);
        asPlayer.setLastLocation(player.getLocation());

        // Give invisibility and remove armor
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 0));
        player.getInventory().clear();

        // Teleport player to camera location
        Camera lastCamera = asPlayer.getLastCamera();
        if (lastCamera == null) {
            Arena arena = arenaManager.getArena(asPlayer);
            lastCamera = new ArrayList<>(arena.getCameraGroups()).get(asPlayer.getCameraGroupIndex()).getCameras().get(asPlayer.getCameraIndex());
        }
        asPlayer.setLastCamera(lastCamera);
        player.teleport(lastCamera.getLocation());

        // Give camera items
        asPlayer.getItems().cameras();

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.EnterCameras")));
    }
    
    public void nextCameraGroup(ASPlayer asPlayer) {
        Player player = asPlayer.getPlayer();

        Arena arena = arenaManager.getArena(asPlayer);

        int cameraGroupIndex = asPlayer.getCameraGroupIndex();
        if (cameraGroupIndex + 1 < arena.getCameraGroups().size()) {
            asPlayer.setCameraGroupIndex(cameraGroupIndex + 1);
            asPlayer.setCameraIndex(0);

            cameraGroupIndex = asPlayer.getCameraGroupIndex();
            List<CameraGroup> cameraGroups = new ArrayList<>(arena.getCameraGroups());
            CameraGroup cameraGroup = cameraGroups.get(cameraGroupIndex);
            Camera camera = cameraGroup.getCameras().get(0);
            asPlayer.setLastCamera(camera);

            player.teleport(camera.getLocation().getBlock().getLocation().clone().add(new Vector(0.5D, 0D, 0.5D)));
        }

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.NextCameraGroup")));
    }

    public void nextCamera(ASPlayer asPlayer) {
        Player player = asPlayer.getPlayer();

        Arena arena = arenaManager.getArena(asPlayer);
        Camera lastCamera = asPlayer.getLastCamera();
        CameraGroup cameraGroup = arena.getCameraGroup(lastCamera.getLocation());

        int cameraIndex = asPlayer.getCameraIndex();
        if (!cameraGroup.isRandom()) {
            if (cameraIndex + 1 < cameraGroup.getCameras().size()) {
                asPlayer.setCameraIndex(cameraIndex + 1);
            }
        }
        else {
            int random = NumUtil.random(0, cameraGroup.getCameras().size());
            while (random == cameraIndex) {
                random = NumUtil.random(0, cameraGroup.getCameras().size());
            }

            asPlayer.setCameraIndex(random);
        }

        asPlayer.setCameraGroupIndex(new ArrayList<>(arena.getCameraGroups()).indexOf(cameraGroup));

        cameraIndex = asPlayer.getCameraIndex();
        lastCamera = cameraGroup.getCameras().get(cameraIndex);
        asPlayer.setLastCamera(lastCamera);

        player.teleport(lastCamera.getLocation().getBlock().getLocation().clone().add(new Vector(0.5D, 0D, 0.5D)));

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.NextCamera")));
    }

    public void exitCameras(ASPlayer asPlayer) {
        Player player = asPlayer.getPlayer();

        asPlayer.setCameras(false);

        // Remove invisibility
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.getInventory().clear();

        // Teleport player to last location
        Location lastLocation = asPlayer.getLastLocation();
        player.teleport(lastLocation);

        // Give armor
        asPlayer.getArmor().armor(player, asPlayer.getColor());

        // Give items
        Arena arena = arenaManager.getArena(asPlayer);
        if (arena.isImpostor(asPlayer)) {
            ASImpostor asImpostor = arena.getImpostor(asPlayer);
            asImpostor.getImpostorItems().impostor(arena);
        }
        if (arena.isCrewmate(asPlayer)) {
            ASCrewmate asCrewmate = arena.getCrewmate(asPlayer);
            asCrewmate.getCrewmateItems().crewmate(arena);
        }

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.ExitCameras")));
    }

    public void previousCamera(ASPlayer asPlayer) {
        Player player = asPlayer.getPlayer();

        Arena arena = arenaManager.getArena(asPlayer);
        Camera lastCamera = asPlayer.getLastCamera();
        CameraGroup cameraGroup = arena.getCameraGroup(lastCamera.getLocation());

        int cameraIndex = asPlayer.getCameraIndex();
        if (!cameraGroup.isRandom()) {
            if (cameraIndex - 1 >= 0) {
                asPlayer.setCameraIndex(cameraIndex - 1);
            }
        }
        else {
            int random = NumUtil.random(0, cameraGroup.getCameras().size());
            while (random == cameraIndex) {
                random = NumUtil.random(0, cameraGroup.getCameras().size());
            }

            asPlayer.setCameraIndex(random);
        }

        asPlayer.setCameraGroupIndex(new ArrayList<>(arena.getCameraGroups()).indexOf(cameraGroup));

        cameraIndex = asPlayer.getCameraIndex();
        lastCamera = cameraGroup.getCameras().get(cameraIndex);
        asPlayer.setLastCamera(lastCamera);

        player.teleport(lastCamera.getLocation().getBlock().getLocation().clone().add(new Vector(0.5D, 0D, 0.5D)));

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.PreviousCamera")));
    }

    public void previousCameraGroup(ASPlayer asPlayer) {
        Player player = asPlayer.getPlayer();

        Arena arena = arenaManager.getArena(asPlayer);

        int cameraGroupIndex = asPlayer.getCameraGroupIndex();
        if (cameraGroupIndex - 1 >= 0) {
            asPlayer.setCameraGroupIndex(cameraGroupIndex - 1);
            asPlayer.setCameraIndex(0);

            cameraGroupIndex = asPlayer.getCameraGroupIndex();
            List<CameraGroup> cameraGroups = new ArrayList<>(arena.getCameraGroups());
            CameraGroup cameraGroup = cameraGroups.get(cameraGroupIndex);
            Camera camera = cameraGroup.getCameras().get(0);
            asPlayer.setLastCamera(camera);

            player.teleport(camera.getLocation().getBlock().getLocation().clone().add(new Vector(0.5D, 0D, 0.5D)));
        }

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.PreviousCameraGroup")));
    }

}
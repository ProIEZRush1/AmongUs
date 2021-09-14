package me.proiezrush.amongus.game.arena.cameras;

import me.proiezrush.amongus.game.arena.tasks.Task;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class CameraGroup {

    private final String name;
    private List<Camera> cameras;
    private final boolean random;
    public CameraGroup(String name, boolean random) {
        this.name = name;
        this.cameras = new ArrayList<>();
        this.random = random;
    }

    public String getName() {
        return name;
    }

    public void addCamera(Camera camera) {
        this.cameras.add(camera);
    }

    public void removeCamera(Camera camera) {
        this.cameras.remove(camera);
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public boolean isRandom() {
        return random;
    }

    // Util methods
    public Camera getCamera(Location location) {
        for (Camera camera : cameras) {
            if (camera.getLocation().equals(location)) {
                return camera;
            }
        }
        return null;
    }
}

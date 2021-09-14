package me.proiezrush.amongus.game.arena.player;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.cameras.Camera;
import me.proiezrush.amongus.game.manager.items.Armor;
import me.proiezrush.amongus.game.manager.items.Items;
import me.proiezrush.amongus.game.manager.items.VotingItems;
import org.bukkit.Color;
import org.bukkit.Location;

public class ASPlayer extends AS {

    // Items
    private final Items items;
    private final Armor armor;
    private final VotingItems votingItems;

    // Type
    private ASType type;

    // Objects
    private Location lastLocation;
    private Camera lastCamera;

    // Booleans
    private boolean isDeath;
    private boolean isVoted;
    private boolean isCameras;

    // Other
    private Color color;
    private int votes;

    public ASPlayer(AmongUs plugin, AS as) {
        super(as.getPlayer());

        this.items = new Items(plugin);
        this.armor = new Armor();
        this.votingItems = new VotingItems(plugin, this);
    }

    public Items getItems() {
        this.items.setup(this);
        return items;
    }

    public Armor getArmor() {
        return armor;
    }

    public VotingItems getVotingItems() {
        return votingItems;
    }

    public void setType(ASType type) {
        this.type = type;
    }

    public ASType getType() {
        return type;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastCamera(Camera lastCamera) {
        this.lastCamera = lastCamera;
    }

    public Camera getLastCamera() {
        return lastCamera;
    }

    public void setDeath(boolean death) {
        this.isDeath = death;
    }

    public boolean isDeath() {
        return isDeath;
    }

    public void setVoted(boolean voted) {
        this.isVoted = voted;
    }

    public boolean isVoted() {
        return isVoted;
    }

    public void setCameras(boolean cameras) {
        isCameras = cameras;
    }

    public boolean isCameras() {
        return isCameras;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    /* USED TO GET CAMERA GROUP INDEX */
    private int cameraGroupIndex;

    public void setCameraGroupIndex(int cameraGroupIndex) {
        this.cameraGroupIndex = cameraGroupIndex;
    }

    public int getCameraGroupIndex() {
        return cameraGroupIndex;
    }

    /* USED TO GET CAMERA INDEX */
    private int cameraIndex;

    public void setCameraIndex(int cameraIndex) {
        this.cameraIndex = cameraIndex;
    }

    public int getCameraIndex() {
        return cameraIndex;
    }
}

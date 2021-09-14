package me.proiezrush.amongus.game.arena.player;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.cooldown.KnifeCooldown;
import me.proiezrush.amongus.game.arena.sabotage.inventories.sabotage.SabotageInventory;
import me.proiezrush.amongus.game.arena.vents.Vent;
import me.proiezrush.amongus.game.manager.items.ImpostorItems;

public class ASImpostor extends ASPlayer {

    private final AmongUs plugin;

    // Objects
    private ImpostorItems items;
    private final KnifeCooldown knifeCooldown;
    private Vent lastVent;

    // Inventories
    private final SabotageInventory sabotageInventory;

    // Booleans
    private boolean isVents;

    public ASImpostor(AmongUs plugin, AS as) {
        super(plugin, as);

        this.plugin = plugin;

        this.items = new ImpostorItems(plugin);
        this.knifeCooldown = new KnifeCooldown(plugin, this, 30);

        this.sabotageInventory = new SabotageInventory(plugin);
    }

    public ImpostorItems getImpostorItems() {
        this.items.setup(this);
        return items;
    }

    public KnifeCooldown getKnifeCooldown() {
        return knifeCooldown;
    }

    public void setLastVent(Vent lastVent) {
        this.lastVent = lastVent;
    }

    public Vent getLastVent() {
        return lastVent;
    }

    public SabotageInventory getSabotageInventory() {
        return sabotageInventory;
    }

    public void setVents(boolean vents) {
        isVents = vents;
    }

    public boolean isVents() {
        return isVents;
    }

    /* USED TO GET FAKE TASK ITEM POSITION */
    private int fakeTaskPosition = 1;

    public void setFakeTaskPosition(int fakeTaskPosition) {
        this.fakeTaskPosition = fakeTaskPosition;
    }

    public int getFakeTaskPosition() {
        return fakeTaskPosition;
    }

    /* USED TO GET VENT GROUP INDEX */
    private int ventGroupIndex;

    public void setVentGroupIndex(int ventGroupIndex) {
        this.ventGroupIndex = ventGroupIndex;
    }

    public int getVentGroupIndex() {
        return ventGroupIndex;
    }

    /* USED TO GET VENT INDEX */
    private int ventIndex;

    public void setVentIndex(int ventIndex) {
        this.ventIndex = ventIndex;
    }

    public int getVentIndex() {
        return ventIndex;
    }
}

package me.proiezrush.amongus.util.nms.corpses.events;

import me.proiezrush.amongus.util.nms.corpses.ICorpse;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CorpseClickEvent extends Event implements Cancellable {

    private final Player player;
    private final ICorpse corpse;
    private boolean isCancelled;
    public CorpseClickEvent(Player player, ICorpse corpse) {
        this.player = player;
        this.corpse = corpse;
    }

    public Player getPlayer() {
        return player;
    }

    public ICorpse getCorpse() {
        return corpse;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

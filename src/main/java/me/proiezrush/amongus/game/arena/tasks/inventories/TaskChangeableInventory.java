package me.proiezrush.amongus.game.arena.tasks.inventories;

import me.proiezrush.amongus.game.arena.player.ASCrewmate;

public interface TaskChangeableInventory extends TaskInventory {

    void start(ASCrewmate asCrewmate);
    void stop(ASCrewmate asCrewmate, boolean complete);
    void reset(ASCrewmate asCrewmate);

}

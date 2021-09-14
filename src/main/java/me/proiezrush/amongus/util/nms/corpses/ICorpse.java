package me.proiezrush.amongus.util.nms.corpses;

import me.proiezrush.amongus.game.arena.player.ASPlayer;

import java.util.Set;

public interface ICorpse {

    void spawn(Set<ASPlayer> players);
    void remove(Set<ASPlayer> players);
    int getId();
    ASPlayer getASPlayer();

}

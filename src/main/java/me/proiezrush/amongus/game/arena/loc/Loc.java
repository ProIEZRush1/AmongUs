package me.proiezrush.amongus.game.arena.loc;

public class Loc {

    private final String name;
    public Loc(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

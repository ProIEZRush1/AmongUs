package me.proiezrush.amongus.game.arena;

public enum ArenaStatus {

    DISABLED("&7"), WAITING("&e"), STARTING("&a"), GAME("&c"), FINISHING("&5");

    private String colorCode;
    ArenaStatus(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}

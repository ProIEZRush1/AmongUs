package me.proiezrush.amongus.game.arena.player;

import me.proiezrush.amongus.util.ChatUtil;

public enum ASType {

    IMPOSTOR, CREWMATE;

    public String getDisplay(boolean a) {
        String display = "";
        if (!a) display += "&k";
        display += this.toString();
        return ChatUtil.color(display);
    }

}

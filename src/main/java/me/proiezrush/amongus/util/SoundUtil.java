package me.proiezrush.amongus.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtil {

    public static void playSound(Player player, Sound sound) {
        playSound(player, sound, 10F, 10F);
    }

    public static void playSound(Player player, Sound sound, float a, float b) {
        player.playSound(player.getLocation(), sound, a, b);
    }

}

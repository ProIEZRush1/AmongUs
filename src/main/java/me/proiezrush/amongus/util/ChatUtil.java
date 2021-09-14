package me.proiezrush.amongus.util;

import me.proiezrush.amongus.dependencies.PAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    private static final PAPI papi;

    static {
        papi = new PAPI();
    }

    public static String parsePlaceholders(Player p, String s) {
        return color(papi.parsePlaceholders(p, s));
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String strip(String s) {
        return ChatColor.stripColor(s);
    }

    public static String rgbToHex(int r, int g, int b) {
        return String.format("#%02x%02x%02x", r, g, b);
    }

    private final static Pattern hexPattern = Pattern.compile("#[a-fA-f0-9]{6}");
    public static String hexColor(String s) {
        Matcher match = hexPattern.matcher(s);
        while (match.find()) {
            String color = s.substring(match.start(), match.end());
            s = s.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            match = hexPattern.matcher(s);
        }
        return color(s);
    }

}

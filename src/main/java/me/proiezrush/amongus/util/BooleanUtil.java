package me.proiezrush.amongus.util;

public class BooleanUtil {

    public static boolean valid(String s) {
        return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("1") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("0") || s.equalsIgnoreCase("no");
    }

    public static boolean getBoolean(String s) {
        if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("1") || s.equalsIgnoreCase("yes")) {
            return true;
        }
        else if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("0") || s.equalsIgnoreCase("no")) {
            return false;
        }
        return false;
    }

}

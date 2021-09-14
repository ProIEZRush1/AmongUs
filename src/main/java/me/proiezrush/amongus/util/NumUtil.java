package me.proiezrush.amongus.util;

public class NumUtil {

    public static int random(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int round(int num, int r) {
        return (int) (Math.ceil(num / (double) r) * r);
    }

}
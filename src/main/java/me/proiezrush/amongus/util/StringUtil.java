package me.proiezrush.amongus.util;

import java.util.List;
import java.util.Random;

public class StringUtil {

    public static final String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String key(int length) {
        StringBuilder key = new StringBuilder();
        for (int i=0;i<length;i++) {
            key.append(getRandomAlphaNum());
        }
        return key.toString();
    }

    private static String getRandomAlphaNum() {
        Random r = new Random();
        int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
        return ALPHANUMERIC_CHARACTERS.substring(offset, offset+1);
    }

    public static String firstCapital(String s) {
        String a = s.toLowerCase();
        String b = a.substring(0, 1).toUpperCase();
        String c = a.substring(1);

        return b + c;
    }

    public static String listToString(List<?> list, String separator) {
        StringBuilder s = new StringBuilder();
        for (int i=0;i<list.size();i++) {
            s.append(list.get(i).toString());
            if (i != list.size()-1) {
                s.append(separator);
            }
        }
        return s.toString();
    }

}

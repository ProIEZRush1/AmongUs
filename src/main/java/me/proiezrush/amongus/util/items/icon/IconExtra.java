package me.proiezrush.amongus.util.items.icon;

public class IconExtra {

    private final String key;
    private final Object extra;
    public IconExtra(String key, Object extra) {
        this.key = key;
        this.extra = extra;
    }

    public String getKey() {
        return key;
    }

    public Object getExtra() {
        return extra;
    }
}

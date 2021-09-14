package me.proiezrush.amongus.game.manager.items.impostor;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.items.ChangeableItem;
import me.proiezrush.amongus.util.items.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FakeTask extends ItemBuilder implements ChangeableItem {

    private final AmongUs plugin;
    private final ASImpostor as;
    private final Settings items;
    public FakeTask(AmongUs plugin, ASImpostor as) {
        super();

        this.plugin = plugin;
        this.as = as;
        this.items = plugin.getItems();
    }

    @Override
    public void change() {
        ConfigurationSection a = items.getConfig().getConfigurationSection("Crewmate.Tasks");
        List<String> b = new ArrayList<>(a.getKeys(false));

        if (as.getFakeTaskPosition() == b.size()-1) as.setFakeTaskPosition(1);
        else as.setFakeTaskPosition(as.getFakeTaskPosition()+1);
    }

    public ItemStack build() {
        ConfigurationSection a = items.getConfig().getConfigurationSection("Crewmate.Tasks");
        List<String> b = new ArrayList<>(a.getKeys(false));

        String name = a.getString(b.get(as.getFakeTaskPosition()) + ".Name");
        String material = a.getString(b.get(as.getFakeTaskPosition()) + ".Material");
        List<String> lore = a.getStringList(b.get(as.getFakeTaskPosition()) + ".Lore");

        return super.build(replaceValues(name), Material.valueOf(material), lore != null, replaceValues(lore));
    }

    private String replaceValues(String s) {
        s = s.replace("%number%", "");
        s = s.replace("%locations%", "");

        if (s.contains("<crewmate>") && s.contains("</crewmate>")) {
            s = s.replace(StringUtils.substringBetween(s, "<crewmate>", "</crewmate>"), "");
            s = s.replace("<crewmate>", "");
            s = s.replace("</crewmate>", "");
        }

        return s;
    }

    private List<String> replaceValues(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String s : list) {
            if (s.contains("<crewmate>") && s.contains("</crewmate>")) {
                s = s.replace(StringUtils.substringBetween(s, "<crewmate>", "</crewmate>"), "");
                s = s.replace("<crewmate>", "");
                s = s.replace("</crewmate>", "");

                if (s.equals("")) {
                    continue;
                }
            }
            newList.add(s);
        }
        
        return newList;
    }
}
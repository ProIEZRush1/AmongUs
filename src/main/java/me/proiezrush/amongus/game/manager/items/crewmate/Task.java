package me.proiezrush.amongus.game.manager.items.crewmate;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.loc.Loc;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.tasks.TaskGroup;
import me.proiezrush.amongus.game.arena.tasks.TaskType;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.StringUtil;
import me.proiezrush.amongus.util.items.ChangeableItem;
import me.proiezrush.amongus.util.items.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Task extends ItemBuilder implements ChangeableItem {
    
    private final AmongUs plugin;
    private final ASCrewmate asCrewmate;
    private final Settings items;

    private TaskType type;
    public Task(AmongUs plugin, ASCrewmate as) {
        super();
        
        this.plugin = plugin;
        this.asCrewmate = as;
        this.items = plugin.getItems();
    }

    @Override
    public void change() {
        List<String> itemTasks = asCrewmate.getItemTasks();
        String itemsFirst = itemTasks.get(0);
        asCrewmate.removeItemTask(itemsFirst);
        asCrewmate.addItemTask(itemsFirst);

        // If scoreboard is in static mode
        List<String> staticScoreboardTasks = asCrewmate.getStaticScoreboardTasks();
        if (!staticScoreboardTasks.isEmpty()) {
            String staticScoreboardFirst = staticScoreboardTasks.get(0);
            asCrewmate.removeStaticScoreboardTask(staticScoreboardFirst);
            asCrewmate.addStaticScoreboardTask(staticScoreboardFirst);
        }
    }

    public void recheck() {
        List<String> realTasks = asCrewmate.getAssignedTasks().getRealTasks();
        List<String> itemTasks = asCrewmate.getItemTasks();

        itemTasks.clear();
        itemTasks.addAll(realTasks);
    }
    
    public ItemStack build() {
        List<String> itemTasks = asCrewmate.getItemTasks();

        if (itemTasks.isEmpty()) recheck();

        ConfigurationSection a = items.getConfig().getConfigurationSection("Crewmate.Tasks");
        String task = itemTasks.get(0);
        String name = a.getString(task + ".Name");
        String material = a.getString(task + ".Material");
        List<String> lore = a.getStringList(task + ".Lore");

        type = TaskType.valueOf(task);
        
        return super.build(replaceTaskValues(name, task), Material.valueOf(material), lore != null, replaceTaskValues(lore, task));
    }

    private String replaceTaskValues(String s, String task) {
        TaskGroup assignedTasks = asCrewmate.getAssignedTasks();
        int number = assignedTasks.getTaskNumber(TaskType.valueOf(task));

        s = s.replace("%number%", String.valueOf(number));

        List<Loc> locs = assignedTasks.getLocs(TaskType.valueOf(task));
        s = s.replace("%location%", StringUtil.listToString(locs, ","));

        if (s.contains("<crewmate>") && s.contains("</crewmate>")) {
            s = s.replace("<crewmate>", "");
            s = s.replace("</crewmate>", "");
        }

        return s;
    }

    private List<String> replaceTaskValues(List<String> list, String task) {
        TaskGroup assignedTasks = asCrewmate.getAssignedTasks();
        int number = assignedTasks.getTaskNumber(TaskType.valueOf(task));

        List<String> newList = new ArrayList<>();
        for (String s : list) {
            if (s.contains("%locations_")) {
                String between = StringUtils.substringBetween(s, "%", "%");
                String[] values = between.split("_");
                String colorCode = values[1];

                List<Loc> locs = assignedTasks.getLocs(TaskType.valueOf(task));
                for (Loc loc : locs) {
                    newList.add("&" + colorCode + loc.toString());
                }
                continue;
            }
            if (s.contains("<crewmate>") && s.contains("</crewmate>")) {
                s = s.replace("<crewmate>", "");
                s = s.replace("</crewmate>", "");
            }
            newList.add(s.replace("%number%", String.valueOf(number)));
        }

        return newList;
    }

    public TaskType getType() {
        return type;
    }
}

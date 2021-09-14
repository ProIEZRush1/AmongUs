package me.proiezrush.amongus.game.manager.scoreboard.game;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.arena.player.ASType;
import me.proiezrush.amongus.game.arena.tasks.TaskGroup;
import me.proiezrush.amongus.game.arena.tasks.TaskType;
import me.proiezrush.amongus.util.ChatUtil;
import me.proiezrush.amongus.util.NumUtil;
import me.proiezrush.amongus.util.Settings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TasksReplacer {

    private final AmongUs plugin;
    private final Settings config;
    private final Settings items;

    private final Arena arena;
    private final ASPlayer as;
    public TasksReplacer(AmongUs plugin, Arena arena, ASPlayer as) {
        this.plugin = plugin;
        this.config = plugin.getC();
        this.items = plugin.getItems();

        this.arena = arena;
        this.as = as;
    }

    public List<String> replaceTasks(List<String> lines) {
        ASType type = as.getType();

        List<String> newLines = new ArrayList<>();
        for (String s : lines) {
            if (type == ASType.IMPOSTOR) {
                if (s.contains("<crewmate>") && s.contains("</crewmate>")) {
                    continue;
                }
                newLines.add(s);
            }
            else if (type == ASType.CREWMATE) {
                ASCrewmate asCrewmate = arena.getCrewmate(as);

                s = s.replace("<crewmate>", "");
                s = s.replace("</crewmate>", "");

                /* GLOBAL VARIABLES */
                ConfigurationSection x = config.getConfig().getConfigurationSection("Tasks");
                TaskGroup assignedTasks = asCrewmate.getAssignedTasks();

                boolean contains = false;

                for (String a : tasksMode) {
                    if (s.contains(a)) {
                        /* REPLACES %tasks_static_NUMBER% WITH STATIC */
                        if (a.equals(tasksMode[0])) {
                            List<String> realTasks = assignedTasks.getRealTasks();
                            List<String> staticScoreboardTasks = asCrewmate.getStaticScoreboardTasks();

                            staticScoreboardTasks.clear();
                            staticScoreboardTasks.addAll(realTasks);

                            String[] between = StringUtils.substringsBetween(s, "%", "%");
                            for (String b : between) {
                                if (b.contains(tasksMode[0].replace("%", ""))) {
                                    String[] values = b.split("_");
                                    int number = Integer.parseInt(values[2]);

                                    if (number > staticScoreboardTasks.size()) {
                                        for (int i=0;i<staticScoreboardTasks.size();i++) {
                                            String task = staticScoreboardTasks.get(i);
                                            String taskDescription = x.getString(task + ".Description");
                                            int taskNumber = assignedTasks.getTaskNumber(TaskType.valueOf(task.toUpperCase()));
                                            newLines.add(ChatUtil.color(taskDescription.replace("%number%", String.valueOf(taskNumber))));
                                        }
                                    }
                                    else {
                                        for (int i=0;i<number;i++) {
                                            if (staticScoreboardTasks.size()-1 >= i) {
                                                String task = staticScoreboardTasks.get(i);
                                                String taskDescription = x.getString(task + ".Description");
                                                int taskNumber = assignedTasks.getTaskNumber(TaskType.valueOf(task));
                                                newLines.add(ChatUtil.color(taskDescription.replace("%number%", String.valueOf(taskNumber))));
                                            }
                                        }
                                    }
                                }
                            }
                            contains = true;
                        }
                        /* REPLACES %tasks_scroll_NUMBER% WITH SCROLL */
                        if (a.equals(tasksMode[1])) {
                            List<String> realTasks = assignedTasks.getRealTasks();
                            List<String> scrollScoreboardTasks = asCrewmate.getScrollScoreboardTasks();

                            scrollScoreboardTasks.clear();
                            scrollScoreboardTasks.addAll(realTasks);

                            String[] between = StringUtils.substringsBetween(s, "%", "%");
                            for (String b : between) {
                                if (b.contains(tasksMode[1].replace("%", ""))) {
                                    String[] values = b.split("_");
                                    int number = Integer.parseInt(values[2]);

                                    if (number > scrollScoreboardTasks.size()) {
                                        for (int i=0;i<scrollScoreboardTasks.size();i++) {
                                            String task = scrollScoreboardTasks.get(i);
                                            String taskDescription = x.getString(task + ".Description");
                                            int taskNumber = assignedTasks.getTaskNumber(TaskType.valueOf(task.toUpperCase()));
                                            newLines.add(ChatUtil.color(taskDescription.replace("%number%", String.valueOf(taskNumber))));
                                        }
                                    }
                                    else {
                                        for (int i=0;i<number;i++) {
                                            if (scrollScoreboardTasks.size()-1 >= i) {
                                                String task = scrollScoreboardTasks.get(i);
                                                String taskDescription = x.getString(task + ".Description");
                                                int taskNumber = assignedTasks.getTaskNumber(TaskType.valueOf(task));
                                                newLines.add(ChatUtil.color(taskDescription.replace("%number%", String.valueOf(taskNumber))));
                                            }
                                        }
                                        /* SET FIRST ELEMENT TO LAST ELEMENT */
                                        String first = scrollScoreboardTasks.get(0);
                                        asCrewmate.removeScrollScoreboardTask(first);
                                        asCrewmate.addScrollScoreboardTask(first);
                                    }
                                }
                            }
                            contains = true;
                        }
                    }
                }
                if (!contains) newLines.add(s);
            }
        }
        return newLines;
    }

    private final String[] tasksMode = {"%tasks_static_", "%tasks_scroll_"};
}

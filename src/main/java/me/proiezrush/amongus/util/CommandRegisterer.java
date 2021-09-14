package me.proiezrush.amongus.util;

import org.bukkit.command.CommandExecutor;

public class CommandRegisterer {

    private final String command;
    private final CommandExecutor executor;
    public CommandRegisterer(String command, CommandExecutor executor) {
        this.command = command;
        this.executor = executor;
    }

    public String getCommand() {
        return command;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }
}

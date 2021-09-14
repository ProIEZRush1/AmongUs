package me.proiezrush.amongus.commands;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.wand.Wand;
import me.proiezrush.amongus.game.arena.player.wand.WandItem;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.ArenaPhase;
import me.proiezrush.amongus.game.arena.ArenaStatus;
import me.proiezrush.amongus.game.arena.cameras.Camera;
import me.proiezrush.amongus.game.arena.cameras.CameraGroup;
import me.proiezrush.amongus.game.arena.sabotage.Sabotage;
import me.proiezrush.amongus.game.arena.sabotage.SabotageType;
import me.proiezrush.amongus.game.arena.sabotage.doors.Cuboid;
import me.proiezrush.amongus.game.arena.sabotage.doors.Door;
import me.proiezrush.amongus.game.arena.sabotage.doors.DoorGroup;
import me.proiezrush.amongus.game.arena.loc.Loc;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.tasks.Task;
import me.proiezrush.amongus.game.arena.tasks.TaskGroup;
import me.proiezrush.amongus.game.arena.tasks.TaskType;
import me.proiezrush.amongus.game.arena.vents.Vent;
import me.proiezrush.amongus.game.arena.vents.VentGroup;
import me.proiezrush.amongus.game.manager.ASManager;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.util.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class ArenaCommand implements CommandExecutor {

    private final AmongUs plugin;
    private final Settings messages;
    private final ArenaManager arenaManager;
    private final ASManager asManager;
    public ArenaCommand(AmongUs plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.arenaManager = plugin.getArenaManager();
        this.asManager = plugin.getAsManager();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("arena")) {
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;

                if (p.hasPermission("amongus.admin")) {
                    AS as = asManager.getAS(p);

                    if (args.length == 1) {
                        String a = args[0];

                        if (a.equalsIgnoreCase("wand")) {
                            Wand wand = as.getWand();
                            WandItem wandItem = wand.getWandItem();
                            p.getInventory().addItem(wandItem.build());

                            p.sendMessage(messages.get(p, "WandReceived"));
                        }
                        else help(p);
                    }
                    else if (args.length == 2) {
                        String a = args[0];
                        String b = args[1];

                        if (a.equalsIgnoreCase("create")) {
                            // Check if arena not exists
                            if (arenaManager.getArena(b) != null) {
                                p.sendMessage(messages.get(p, "ArenaAlreadyExists"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }

                            Arena arena = new Arena(plugin, b);
                            arenaManager.addArena(arena);

                            p.sendMessage(messages.get(p, "ArenaCreated"));
                        } else if (a.equalsIgnoreCase("delete")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            // Check if arena is in game
                            if (arenaManager.getArena(b).inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }

                            Arena arena = arenaManager.getArena(b);
                            arenaManager.removeArena(arena);

                            p.sendMessage(messages.get(p, "ArenaDeleted"));
                        } else if (a.equalsIgnoreCase("waiting")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            // Check if arena is in game
                            if (arenaManager.getArena(b).inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }

                            Arena arena = arenaManager.getArena(b);
                            arena.setWaiting(p.getLocation());

                            p.sendMessage(messages.get(p, "WaitingSet"));
                        } else if (a.equalsIgnoreCase("spawn")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            // Check if arena is in game
                            if (arenaManager.getArena(b).inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }

                            Arena arena = arenaManager.getArena(b);
                            arena.setSpawn(p.getLocation());

                            p.sendMessage(messages.get(p, "SpawnSet"));
                        } else if (a.equalsIgnoreCase("discussion")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            // Check if arena is in game
                            if (arenaManager.getArena(b).inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }

                            Arena arena = arenaManager.getArena(b);
                            arena.setDiscussion(p.getLocation());

                            p.sendMessage(messages.get(p, "DiscussionSet"));
                        } else if (a.equalsIgnoreCase("voting")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            // Check if arena is in game
                            if (arenaManager.getArena(b).inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }

                            Arena arena = arenaManager.getArena(b);
                            arena.setVoting(p.getLocation());

                            p.sendMessage(messages.get(p, "VotingSet"));
                        } else if (a.equalsIgnoreCase("emergency")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            // Check if arena is in game
                            if (arenaManager.getArena(b).inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the block is a button
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            if (!location.getBlock().getType().toString().contains("BUTTON")) {
                                p.sendMessage(messages.get(p, "NeedsToBeButton"));
                                return true;
                            }

                            Arena arena = arenaManager.getArena(b);
                            arena.setEmergencyButton(p.getLocation());

                            p.sendMessage(messages.get(p, "EmergencySet"));
                        } else if (a.equalsIgnoreCase("rmtask")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            // Check if the group exists
                            if (arena.getTaskGroup(location) == null) {
                                p.sendMessage(messages.get(p, "NoTask"));
                                return true;
                            }
                            TaskGroup taskGroup = arena.getTaskGroup(location);
                            // Check if the block has a task
                            if (taskGroup.getTask(location) == null) {
                                p.sendMessage(messages.get(p, "NoTask"));
                                return true;
                            }

                            Task task = taskGroup.getTask(location);
                            taskGroup.removeTask(task);

                            p.sendMessage(messages.get(p, "TaskRemoved"));
                        } else if (a.equalsIgnoreCase("rmvent")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            // Check if the group exists
                            if (arena.getVentGroup(location) == null) {
                                p.sendMessage(messages.get(p, "NoVent"));
                                return true;
                            }
                            // Check if the block has a vent
                            VentGroup ventGroup = arena.getVentGroup(location);
                            if (ventGroup.getVent(location) == null) {
                                p.sendMessage(messages.get(p, "NoVent"));
                                return true;
                            }

                            Vent vent = ventGroup.getVent(location);
                            ventGroup.removeVent(vent);

                            p.sendMessage(messages.get(p, "VentRemoved"));
                        } else if (a.equalsIgnoreCase("rmcamera")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            // Check if the group exists
                            if (arena.getCameraGroup(location) == null) {
                                p.sendMessage(messages.get(p, "NoCamera"));
                                return true;
                            }
                            // Check if the block has a camera
                            CameraGroup cameraGroup = arena.getCameraGroup(location);
                            if (cameraGroup.getCamera(location) == null) {
                                p.sendMessage(messages.get(p, "NoCamera"));
                                return true;
                            }

                            Camera camera = cameraGroup.getCamera(location);
                            cameraGroup.removeCamera(camera);

                            p.sendMessage(messages.get(p, "CameraRemoved"));
                        }
                        else if (a.equalsIgnoreCase("rmsabotage")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            // Check if theres a sabotage in that block
                            if (arena.getSabotage(location) == null) {
                                p.sendMessage(messages.get(p, "NoSabotage"));
                                return true;
                            }

                            Sabotage sabotage = arena.getSabotage(location);
                            arena.getSabotages().remove(sabotage);

                            p.sendMessage(messages.get(p, "SabotageRemoved"));
                        }
                        else if (a.equalsIgnoreCase("rmdoor")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            // Check if the group exists
                            if (arena.getDoorGroup(location.getBlock()) == null) {
                                p.sendMessage(messages.get(p, "NoDoor"));
                                return true;
                            }
                            // Check if the block has a camera
                            DoorGroup doorGroup = arena.getDoorGroup(location.getBlock());
                            if (doorGroup.getDoor(location.getBlock()) == null) {
                                p.sendMessage(messages.get(p, "NoDoor"));
                                return true;
                            }

                            Door door = doorGroup.getDoor(location.getBlock());
                            doorGroup.removeDoor(door);

                            p.sendMessage(messages.get(p, "DoorRemoved"));
                        }
                        else if (a.equalsIgnoreCase("save")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            // Check if arena is in game
                            if (arenaManager.getArena(b).inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if the arena is configured
                            if (arena.getWaiting() == null || arena.getSpawn() == null || arena.getDiscussion() == null ||
                                    arena.getVoting() == null || arena.getEmergencyButton() == null || arena.getTaskGroups().isEmpty() ||
                                    arena.getVentGroups().isEmpty()) {
                                p.sendMessage(messages.get(p, "ConfigurationMissing"));
                                return true;
                            }

                            arena.setStatus(ArenaStatus.WAITING);
                            arena.setPhase(ArenaPhase.WAITING);

                            p.sendMessage(messages.get(p, "ArenaSaved"));
                        }
                        else help(p);
                    }
                    else if (args.length == 3) {
                        String a = args[0];
                        String b = args[1];
                        String c = args[2];

                        if (a.equalsIgnoreCase("loc")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if loc not exists
                            if (arena.getLoc(c) != null) {
                                p.sendMessage(messages.get(p, "LocExists"));
                                return true;
                            }

                            Loc loc = new Loc(c);
                            arena.getLocs().add(loc);

                            p.sendMessage(messages.get(p, "LocAdded"));
                        }
                        else if (a.equalsIgnoreCase("rmloc")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if loc exists
                            if (arena.getLoc(c) == null) {
                                p.sendMessage(messages.get(p, "LocNotExists"));
                                return true;
                            }

                            Loc loc = arena.getLoc(c);
                            arena.getLocs().remove(loc);

                            p.sendMessage(messages.get(p, "LocRemoved"));
                        }
                        else if (a.equalsIgnoreCase("taskgroup")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the taskgroup doesn't exists
                            if (arena.getTaskGroup(c) != null) {
                                p.sendMessage(messages.get(p, "TaskGroupExists"));
                                return true;
                            }

                            TaskGroup taskGroup = new TaskGroup(c);
                            arena.getTaskGroups().add(taskGroup);

                            p.sendMessage(messages.get(p, "TaskGroupAdded"));
                        }
                        else if (a.equalsIgnoreCase("rmtaskgroup")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the taskgroup exists
                            if (arena.getTaskGroup(c) == null) {
                                p.sendMessage(messages.get(p, "TaskGroupNotExists"));
                                return true;
                            }

                            TaskGroup taskGroup = arena.getTaskGroup(c);
                            arena.getTaskGroups().remove(taskGroup);

                            p.sendMessage(messages.get(p, "TaskGroupRemoved"));
                        }
                        else if (a.equalsIgnoreCase("rmventgroup")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the taskgroup exists
                            if (arena.getVentGroup(c) == null) {
                                p.sendMessage(messages.get(p, "VentGroupNotExists"));
                                return true;
                            }

                            VentGroup ventGroup = arena.getVentGroup(c);
                            arena.getVentGroups().remove(ventGroup);

                            p.sendMessage(messages.get(p, "VentGroupRemoved"));
                        }
                        else if (a.equalsIgnoreCase("rmcameragroup")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the taskgroup exists
                            if (arena.getCameraGroup(c) == null) {
                                p.sendMessage(messages.get(p, "CameraGroupNotExists"));
                                return true;
                            }

                            CameraGroup cameraGroup = arena.getCameraGroup(c);
                            arena.getCameraGroups().remove(cameraGroup);

                            p.sendMessage(messages.get(p, "CameraGroupRemoved"));
                        }
                        else if (a.equalsIgnoreCase("doorgroup")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the door group doesn't exists
                            if (arena.getDoorGroup(c) != null) {
                                p.sendMessage(messages.get(p, "DoorGroupExists"));
                                return true;
                            }

                            DoorGroup doorGroup = new DoorGroup(c);
                            arena.getDoorGroups().add(doorGroup);

                            p.sendMessage(messages.get(p, "DoorGroupAdded"));
                        }
                        else if (a.equalsIgnoreCase("rmdoorgroup")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the taskgroup exists
                            if (arena.getDoorGroup(c) == null) {
                                p.sendMessage(messages.get(p, "DoorGroupNotExists"));
                                return true;
                            }

                            DoorGroup doorGroup = arena.getDoorGroup(c);
                            arena.getDoorGroups().remove(doorGroup);

                            p.sendMessage(messages.get(p, "DoorGroupRemoved"));
                        }
                        else help(p);
                    }
                    else if (args.length == 4) {
                        String a = args[0];
                        String b = args[1];
                        String c = args[2];
                        String d = args[3];

                        if (a.equalsIgnoreCase("ventgroup")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if random is valid value
                            if (!BooleanUtil.valid(d)) {
                                p.sendMessage(messages.get(p, "InvalidBoolean"));
                                return true;
                            }
                            // Check if the ventgroup doesn't exists
                            if (arena.getVentGroup(c) != null) {
                                p.sendMessage(messages.get(p, "VentGroupExists"));
                                return true;
                            }

                            VentGroup ventGroup = new VentGroup(c, BooleanUtil.getBoolean(d));
                            arena.getVentGroups().add(ventGroup);

                            p.sendMessage(messages.get(p, "VentGroupAdded"));
                        }
                        else if (a.equalsIgnoreCase("vent")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the group exists
                            if (arena.getVentGroup(c) == null) {
                                p.sendMessage(messages.get(p, "VentGroupNotExists"));
                                return true;
                            }
                            VentGroup ventGroup = arena.getVentGroup(c);
                            // Check if the loc exists
                            if (arena.getLoc(d) == null) {
                                p.sendMessage(messages.get(p, "LocNotExists"));
                                return true;
                            }
                            Loc loc = arena.getLoc(d);
                            // Check if the block is a trapdoor
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            if (location.getBlock().getType() != Material.IRON_TRAPDOOR) {
                                p.sendMessage(messages.get(p, "NeedsToBeTrapdoor"));
                                return true;
                            }
                            // Check if theres no other vent in that block
                            if (ventGroup.getVent(location) != null) {
                                p.sendMessage(messages.get(p, "TheresAlreadyVent"));
                                return true;
                            }

                            Vent vent = new Vent(StringUtil.key(75), loc, location);
                            ventGroup.getVents().add(vent);

                            p.sendMessage(messages.get(p, "VentAdded"));
                        }
                        else if (a.equalsIgnoreCase("cameragroup")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if random is valid value
                            if (!BooleanUtil.valid(d)) {
                                p.sendMessage(messages.get(p, "InvalidBoolean"));
                                return true;
                            }
                            // Check if the ventgroup doesn't exists
                            if (arena.getCameraGroup(c) != null) {
                                p.sendMessage(messages.get(p, "CameraGroupExists"));
                                return true;
                            }

                            CameraGroup ventGroup = new CameraGroup(c, BooleanUtil.getBoolean(d));
                            arena.getCameraGroups().add(ventGroup);

                            p.sendMessage(messages.get(p, "CameraGroupAdded"));
                        }
                        else if (a.equalsIgnoreCase("camera")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the group exists
                            if (arena.getCameraGroup(c) == null) {
                                p.sendMessage(messages.get(p, "CameraGroupNotExists"));
                                return true;
                            }
                            CameraGroup cameraGroup = arena.getCameraGroup(c);
                            // Check if the loc exists
                            if (arena.getLoc(d) == null) {
                                p.sendMessage(messages.get(p, "LocNotExists"));
                                return true;
                            }
                            Loc loc = arena.getLoc(d);
                            Location location = p.getLocation().clone().add(new Vector(0D, 1D, 0D));
                            // Check if the block is air
                            if (location.getBlock().getType() != Material.AIR) {
                                p.sendMessage(messages.get(p, "NeedsToBeAir"));
                                return true;
                            }
                            // Check if theres no other camera in that block
                            if (cameraGroup.getCamera(location) != null) {
                                p.sendMessage(messages.get(p, "TheresAlreadyCamera"));
                                return true;
                            }

                            Camera camera = new Camera(StringUtil.key(75), loc, location);
                            cameraGroup.getCameras().add(camera);

                            // Create the camera block skull
                            Block block = location.getBlock();
                            block.setType(Material.PLAYER_HEAD);
                            Skull skull = (Skull) block.getState();
                            skull.setOwner("SecurityCamera");
                            skull.setRotation(BlockFaceUtil.yawToFace(location.getYaw()));
                            skull.update();

                            p.sendMessage(messages.get(p, "CameraAdded"));
                        }
                        else if (a.equalsIgnoreCase("sabotage")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if sabotage type is valid
                            try {
                                SabotageType type = SabotageType.valueOf(c);
                            }
                            catch (Exception e) {
                                p.sendMessage(messages.get(p, "InvalidSabotage"));
                                return true;
                            }
                            // Check if the loc exists
                            if (arena.getLoc(d) == null) {
                                p.sendMessage(messages.get(p, "LocNotExists"));
                                return true;
                            }
                            Loc loc = arena.getLoc(d);
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            // Check if theres no other sabotage in that block
                            if (arena.getSabotage(location) != null) {
                                p.sendMessage(messages.get(p, "TheresAlreadySabotage"));
                                return true;
                            }

                            SabotageType type = SabotageType.valueOf(c);
                            Sabotage sabotage = new Sabotage(StringUtil.key(75), type, loc, location);
                            arena.getSabotages().add(sabotage);

                            p.sendMessage(messages.get(p, "SabotageAdded"));
                        }
                        else if (a.equalsIgnoreCase("door")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the group exists
                            if (arena.getDoorGroup(c) == null) {
                                p.sendMessage(messages.get(p, "DoorGroupNotExists"));
                                return true;
                            }
                            DoorGroup doorGroup = arena.getDoorGroup(c);
                            // Check if the loc exists
                            if (arena.getLoc(d) == null) {
                                p.sendMessage(messages.get(p, "LocNotExists"));
                                return true;
                            }
                            Loc loc = arena.getLoc(d);
                            // Check if the pos1 and pos2 are set
                            Wand wand = as.getWand();
                            if (wand.getPos1() == null || wand.getPos2() == null) {
                                p.sendMessage(messages.get(p, "WandPositionsNotSet"));
                                return true;
                            }
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            // Check if theres no other door in that block
                            if (doorGroup.getDoor(location.getBlock()) != null) {
                                p.sendMessage(messages.get(p, "TheresAlreadyDoor"));
                                return true;
                            }

                            Door door = new Door(StringUtil.key(75), loc, new Cuboid(wand.getPos1().clone(), wand.getPos2().clone()));
                            doorGroup.addDoor(door);

                            wand.setPos1(null);
                            wand.setPos2(null);

                            p.sendMessage(messages.get(p, "DoorAdded"));
                        }
                        else help(p);
                    }
                    else if (args.length == 5) {
                        String a = args[0];
                        String b = args[1];
                        String c = args[2];
                        String d = args[3];
                        String e = args[4];

                        if (a.equalsIgnoreCase("task")) {
                            // Check if arena exists
                            if (arenaManager.getArena(b) == null) {
                                p.sendMessage(messages.get(p, "ArenaNotExists"));
                                return true;
                            }
                            Arena arena = arenaManager.getArena(b);
                            // Check if arena is in game
                            if (arena.inGame()) {
                                p.sendMessage(messages.get(p, "ArenaInGame"));
                                return true;
                            }
                            // Check if you are not in arena
                            if (arenaManager.getArena(as) != null) {
                                p.sendMessage(messages.get(p, "NeedToLeaveGame"));
                                return true;
                            }
                            // Check if the group exists
                            if (arena.getTaskGroup(c) == null) {
                                p.sendMessage(messages.get(p, "TaskGroupNotExists"));
                                return true;
                            }
                            TaskGroup taskGroup = arena.getTaskGroup(c);
                            // Check if the type is valid
                            try {
                                TaskType.valueOf(d.toUpperCase());
                            }
                            catch (Exception ex) {
                                p.sendMessage(messages.get(p, "InvalidTask"));
                                return true;
                            }
                            // Check if the loc exists
                            if (arena.getLoc(e) == null) {
                                p.sendMessage(messages.get(p, "LocNotExists"));
                                return true;
                            }
                            Loc loc = arena.getLoc(e);
                            Location location = p.getTargetBlock(null, 5).getLocation();
                            // Check if the block is not air
                            if (location.getBlock().getType() == Material.AIR) {
                                location.getBlock();
                                p.sendMessage(messages.get(p, "NeedsToBeBlock"));
                                return true;
                            }
                            // Check if theres no task in that block
                            if (taskGroup.getTask(location) != null) {
                                p.sendMessage(messages.get(p, "TheresAlreadyTask"));
                                return true;
                            }

                            TaskType type = TaskType.valueOf(d.toUpperCase());
                            Task task = new Task(plugin, StringUtil.key(75), type, loc, location);
                            taskGroup.addTask(task);

                            p.sendMessage(messages.get(p, "TaskAdded"));
                        }
                        else help(p);
                    }
                    else help(p);
                }
                else p.sendMessage(messages.get(p, "NoPermission"));
            }
            else commandSender.sendMessage(messages.get(null, "NotPlayer"));
        }
        return false;
    }

    public void help(Player p) {
        List<String> help = messages.getList("ArenaCommandHelp");
        help.forEach(a -> {
            p.sendMessage(ChatUtil.parsePlaceholders(p, a));
        });
    }
}
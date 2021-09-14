package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.cameras.Camera;
import me.proiezrush.amongus.game.arena.cameras.CameraGroup;
import me.proiezrush.amongus.game.arena.sabotage.Sabotage;
import me.proiezrush.amongus.game.arena.sabotage.SabotageType;
import me.proiezrush.amongus.game.arena.sabotage.doors.Cuboid;
import me.proiezrush.amongus.game.arena.sabotage.doors.Door;
import me.proiezrush.amongus.game.arena.sabotage.doors.DoorGroup;
import me.proiezrush.amongus.game.arena.loc.Loc;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.ArenaStatus;
import me.proiezrush.amongus.game.arena.tasks.Task;
import me.proiezrush.amongus.game.arena.tasks.TaskGroup;
import me.proiezrush.amongus.game.arena.tasks.TaskType;
import me.proiezrush.amongus.game.arena.vents.Vent;
import me.proiezrush.amongus.game.arena.vents.VentGroup;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArenaManager {

    private final AmongUs plugin;
    private final Settings arenas;
    private final Set<Arena> arenaSet;

    private final TArenaManager tArenaManager;
    public ArenaManager(AmongUs plugin) {
        this.plugin = plugin;
        this.arenas = plugin.getArenas();
        this.arenaSet = new HashSet<>();

        this.tArenaManager = new TArenaManager(plugin, this);
    }

    public void deserialize() {
        ConfigurationSection a = arenas.getConfig().getConfigurationSection("Arenas");
        if (a == null) return;

        a.getKeys(false).forEach(s -> {
            String name = a.getString(s + ".Name");

            Location waiting = null;
            if (a.getConfigurationSection(s + ".Waiting") != null) {
                ConfigurationSection b = a.getConfigurationSection(s + ".Waiting");

                String world = b.getString("World");
                double x = b.getDouble("X");
                double y = b.getDouble("Y");
                double z = b.getDouble("Z");
                float yaw = (float) b.getDouble("Yaw");
                float pitch = (float) b.getDouble("Pitch");

                waiting = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            }

            Location spawn = null;
            if (a.getConfigurationSection(s + ".Spawn") != null) {
                ConfigurationSection b = a.getConfigurationSection(s + ".Spawn");

                String world = b.getString("World");
                double x = b.getDouble("X");
                double y = b.getDouble("Y");
                double z = b.getDouble("Z");
                float yaw = (float) b.getDouble("Yaw");
                float pitch = (float) b.getDouble("Pitch");

                spawn = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            }

            Location discussion = null;
            if (a.getConfigurationSection(s + ".Discussion") != null) {
                ConfigurationSection b = a.getConfigurationSection(s + ".Discussion");

                String world = b.getString("World");
                double x = b.getDouble("X");
                double y = b.getDouble("Y");
                double z = b.getDouble("Z");
                float yaw = (float) b.getDouble("Yaw");
                float pitch = (float) b.getDouble("Pitch");

                discussion = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            }

            Location voting = null;
            if (a.getConfigurationSection(s + ".Voting") != null) {
                ConfigurationSection b = a.getConfigurationSection(s + ".Voting");

                String world = b.getString("World");
                double x = b.getDouble("X");
                double y = b.getDouble("Y");
                double z = b.getDouble("Z");
                float yaw = (float) b.getDouble("Yaw");
                float pitch = (float) b.getDouble("Pitch");

                voting = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            }

            Location emergencyButton = null;
            if (a.getConfigurationSection(s + ".EmergencyButton") != null) {
                ConfigurationSection b = a.getConfigurationSection(s + ".EmergencyButton");

                String world = b.getString("World");
                double x = b.getDouble("X");
                double y = b.getDouble("Y");
                double z = b.getDouble("Z");

                emergencyButton = new Location(Bukkit.getWorld(world), x, y, z);
            }

            Arena arena = new Arena(plugin, name);

            // LOCS
            ConfigurationSection locs = a.getConfigurationSection(s + ".Locs");
            if (locs != null) {
                locs.getKeys(false).forEach(j -> {
                    Loc loc = new Loc(locs.getString(j + ".Name"));
                    arena.getLocs().add(loc);
                });
            }

            // Task groups
            ConfigurationSection taskGroups = a.getConfigurationSection(s + ".TaskGroups");
            if (taskGroups != null) {
                taskGroups.getKeys(false).forEach(j -> {
                    ConfigurationSection tasks = taskGroups.getConfigurationSection(j + ".Tasks");
                    if (tasks != null) {
                        TaskGroup taskGroup = new TaskGroup(j);
                        tasks.getKeys(false).forEach(l -> {
                            String type = tasks.getString(l + ".Type");
                            String loc = tasks.getString(l + ".Loc");

                            String world = tasks.getString(l + ".Location.World");
                            double x = tasks.getDouble(l + ".Location.X");
                            double y = tasks.getDouble(l + ".Location.Y");
                            double z = tasks.getDouble(l + ".Location.Z");

                            Task task = new Task(plugin, l, TaskType.valueOf(type), arena.getLoc(loc), new Location(Bukkit.getWorld(world), x, y, z));
                            taskGroup.addTask(task);
                        });
                        arena.getTaskGroups().add(taskGroup);
                    }
                });
            }

            // Vent groups
            ConfigurationSection ventGroups = a.getConfigurationSection(s + ".VentGroups");
            if (ventGroups != null) {
                ventGroups.getKeys(false).forEach(j -> {
                    ConfigurationSection vents = ventGroups.getConfigurationSection(j + ".Vents");
                    if (vents != null) {
                        VentGroup ventGroup = new VentGroup(j, ventGroups.getBoolean(j + ".Random"));

                        vents.getKeys(false).forEach(l -> {
                            String loc = vents.getString(l + ".Loc");

                            String world = vents.getString(l + ".Location.World");
                            double x = vents.getDouble(l + ".Location.X");
                            double y = vents.getDouble(l + ".Location.Y");
                            double z = vents.getDouble(l + ".Location.Z");

                            Vent vent = new Vent(l, arena.getLoc(loc), new Location(Bukkit.getWorld(world), x, y, z));
                            ventGroup.addVent(vent);
                        });
                        arena.getVentGroups().add(ventGroup);
                    }
                });
            }
            
            // Camera groups
            ConfigurationSection cameraGroups = a.getConfigurationSection(s + ".CameraGroups");
            if (cameraGroups != null) {
                cameraGroups.getKeys(false).forEach(j -> {
                    ConfigurationSection cameras = cameraGroups.getConfigurationSection(j + ".Cameras");
                    if (cameras != null) {
                        CameraGroup cameraGroup = new CameraGroup(j, cameraGroups.getBoolean(j + ".Random"));

                        cameras.getKeys(false).forEach(l -> {
                            String loc = cameras.getString(l + ".Loc");

                            String world = cameras.getString(l + ".Location.World");
                            double x = cameras.getDouble(l + ".Location.X");
                            double y = cameras.getDouble(l + ".Location.Y");
                            double z = cameras.getDouble(l + ".Location.Z");

                            Camera camera = new Camera(l, arena.getLoc(loc), new Location(Bukkit.getWorld(world), x, y, z));
                            cameraGroup.addCamera(camera);
                        });
                        arena.getCameraGroups().add(cameraGroup);
                    }
                });
            }
            
            // Door groups
            ConfigurationSection doorGroups = a.getConfigurationSection(s + ".DoorGroups");
            if (doorGroups != null) {
                doorGroups.getKeys(false).forEach(j -> {
                    ConfigurationSection doors = doorGroups.getConfigurationSection(j + ".Doors");
                    if (doors != null) {
                        DoorGroup doorGroup = new DoorGroup(j);

                        doors.getKeys(false).forEach(l -> {
                            String loc = doors.getString(l + ".Loc");

                            String world1 = doors.getString(l + ".Location.1.World");
                            double x1 = doors.getDouble(l + ".Location.1.X");
                            double y1 = doors.getDouble(l + ".Location.1.Y");
                            double z1 = doors.getDouble(l + ".Location.1.Z");

                            String world2 = doors.getString(l + ".Location.2.World");
                            double x2 = doors.getDouble(l + ".Location.2.X");
                            double y2 = doors.getDouble(l + ".Location.2.Y");
                            double z2 = doors.getDouble(l + ".Location.2.Z");

                            Door door = new Door(l, arena.getLoc(loc), new Cuboid(new Location(Bukkit.getWorld(world1), x1, y1, z1), new Location(Bukkit.getWorld(world2), x2, y2, z2)));
                            doorGroup.addDoor(door);
                        });
                        arena.getDoorGroups().add(doorGroup);
                    }
                });
            }

            // Sabotages
            ConfigurationSection sabotages = a.getConfigurationSection(s + ".Sabotages");
            if (sabotages != null) {
                sabotages.getKeys(false).forEach(j -> {
                    String type = sabotages.getString(j + ".Type");
                    String loc = sabotages.getString(j + ".Loc");

                    String world = sabotages.getString(j + ".Location.World");
                    double x = sabotages.getDouble(j + ".Location.X");
                    double y = sabotages.getDouble(j + ".Location.Y");
                    double z = sabotages.getDouble(j + ".Location.Z");

                    Sabotage sabotage = new Sabotage(j, SabotageType.valueOf(type), arena.getLoc(loc), new Location(Bukkit.getWorld(world), x, y, z));
                    arena.getSabotages().add(sabotage);
                });
            }

            boolean showNamesOnChat = a.getBoolean(s + ".ShowNamesOnChat");
            boolean showReporterName = a.getBoolean(s + ".ShowReporterName");
            boolean showVictimName = a.getBoolean(s + ".ShowVictimName");
            boolean showVoterName = a.getBoolean(s + ".ShowVoterName");
            boolean showVotedName = a.getBoolean(s + ".ShowVotedName");
            boolean showMostVotedName = a.getBoolean(s + ".ShowMostVotedName");
            boolean showMostVotedType = a.getBoolean(s + ".ShowMostVotedType");

            String status = a.getString(s + ".Status");
            if (status.equalsIgnoreCase("ENABLED")) arena.setStatus(ArenaStatus.WAITING);

            arena.setWaiting(waiting);
            arena.setSpawn(spawn);
            arena.setDiscussion(discussion);
            arena.setVoting(voting);
            arena.setEmergencyButton(emergencyButton);
            arena.setShowNamesOnChat(showNamesOnChat);
            arena.setShowReporterName(showReporterName);
            arena.setShowVictimName(showVictimName);
            arena.setShowVoterName(showVoterName);
            arena.setShowVotedName(showVotedName);
            arena.setShowMostVotedName(showMostVotedName);
            arena.setShowMostVotedType(showMostVotedType);
            arenaSet.add(arena);
        });
    }
    
    public void loadArena(String name) {
        ConfigurationSection a = arenas.getConfig().getConfigurationSection("Arenas." + name);
        if (a == null) return;

        Location waiting = null;
        if (a.getConfigurationSection("Waiting") != null) {
            ConfigurationSection b = a.getConfigurationSection("Waiting");

            String world = b.getString("World");
            double x = b.getDouble("X");
            double y = b.getDouble("Y");
            double z = b.getDouble("Z");
            float yaw = (float) b.getDouble("Yaw");
            float pitch = (float) b.getDouble("Pitch");

            waiting = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        Location spawn = null;
        if (a.getConfigurationSection("Spawn") != null) {
            ConfigurationSection b = a.getConfigurationSection("Spawn");

            String world = b.getString("World");
            double x = b.getDouble("X");
            double y = b.getDouble("Y");
            double z = b.getDouble("Z");
            float yaw = (float) b.getDouble("Yaw");
            float pitch = (float) b.getDouble("Pitch");

            spawn = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        Location discussion = null;
        if (a.getConfigurationSection("Discussion") != null) {
            ConfigurationSection b = a.getConfigurationSection("Discussion");

            String world = b.getString("World");
            double x = b.getDouble("X");
            double y = b.getDouble("Y");
            double z = b.getDouble("Z");
            float yaw = (float) b.getDouble("Yaw");
            float pitch = (float) b.getDouble("Pitch");

            discussion = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        Location voting = null;
        if (a.getConfigurationSection("Voting") != null) {
            ConfigurationSection b = a.getConfigurationSection("Voting");

            String world = b.getString("World");
            double x = b.getDouble("X");
            double y = b.getDouble("Y");
            double z = b.getDouble("Z");
            float yaw = (float) b.getDouble("Yaw");
            float pitch = (float) b.getDouble("Pitch");

            voting = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        Location emergencyButton = null;
        if (a.getConfigurationSection("EmergencyButton") != null) {
            ConfigurationSection b = a.getConfigurationSection("EmergencyButton");

            String world = b.getString("World");
            double x = b.getDouble("X");
            double y = b.getDouble("Y");
            double z = b.getDouble("Z");

            emergencyButton = new Location(Bukkit.getWorld(world), x, y, z);
        }

        Arena arena = new Arena(plugin, name);

        // LOCS
        ConfigurationSection locs = a.getConfigurationSection("Locs");
        if (locs != null) {
            locs.getKeys(false).forEach(j -> {
                Loc loc = new Loc(locs.getString(j + ".Name"));
                arena.getLocs().add(loc);
            });
        }

        // Task groups
        ConfigurationSection taskGroups = a.getConfigurationSection("TaskGroups");
        if (taskGroups != null) {
            taskGroups.getKeys(false).forEach(j -> {
                ConfigurationSection tasks = taskGroups.getConfigurationSection(j + ".Tasks");
                if (tasks != null) {
                    TaskGroup taskGroup = new TaskGroup(j);
                    tasks.getKeys(false).forEach(l -> {
                        String type = tasks.getString(l + ".Type");
                        String loc = tasks.getString(l + ".Loc");

                        String world = tasks.getString(l + ".Location.World");
                        double x = tasks.getDouble(l + ".Location.X");
                        double y = tasks.getDouble(l + ".Location.Y");
                        double z = tasks.getDouble(l + ".Location.Z");

                        Task task = new Task(plugin, l, TaskType.valueOf(type), arena.getLoc(loc), new Location(Bukkit.getWorld(world), x, y, z));
                        taskGroup.addTask(task);
                    });
                    arena.getTaskGroups().add(taskGroup);
                }
            });
        }

        // Vent groups
        ConfigurationSection ventGroups = a.getConfigurationSection("VentGroups");
        if (ventGroups != null) {
            ventGroups.getKeys(false).forEach(j -> {
                ConfigurationSection vents = ventGroups.getConfigurationSection(j + ".Vents");
                if (vents != null) {
                    VentGroup ventGroup = new VentGroup(j, ventGroups.getBoolean(j + ".Random"));

                    vents.getKeys(false).forEach(l -> {
                        String loc = vents.getString(l + ".Loc");

                        String world = vents.getString(l + ".Location.World");
                        double x = vents.getDouble(l + ".Location.X");
                        double y = vents.getDouble(l + ".Location.Y");
                        double z = vents.getDouble(l + ".Location.Z");

                        Vent vent = new Vent(l, arena.getLoc(loc), new Location(Bukkit.getWorld(world), x, y, z));
                        ventGroup.addVent(vent);
                    });
                    arena.getVentGroups().add(ventGroup);
                }
            });
        }

        // Camera groups
        ConfigurationSection cameraGroups = a.getConfigurationSection("CameraGroups");
        if (cameraGroups != null) {
            cameraGroups.getKeys(false).forEach(j -> {
                ConfigurationSection cameras = cameraGroups.getConfigurationSection(j + ".Cameras");
                if (cameras != null) {
                    CameraGroup cameraGroup = new CameraGroup(j, cameraGroups.getBoolean(j + ".Random"));

                    cameras.getKeys(false).forEach(l -> {
                        String loc = cameras.getString(l + ".Loc");

                        String world = cameras.getString(l + ".Location.World");
                        double x = cameras.getDouble(l + ".Location.X");
                        double y = cameras.getDouble(l + ".Location.Y");
                        double z = cameras.getDouble(l + ".Location.Z");

                        Camera camera = new Camera(l, arena.getLoc(loc), new Location(Bukkit.getWorld(world), x, y, z));
                        cameraGroup.addCamera(camera);
                    });
                    arena.getCameraGroups().add(cameraGroup);
                }
            });
        }

        // Sabotages
        ConfigurationSection sabotages = a.getConfigurationSection("Sabotages");
        if (sabotages != null) {
            sabotages.getKeys(false).forEach(j -> {
                String type = sabotages.getString(j + ".Type");
                String loc = sabotages.getString(j + ".Loc");

                String world = sabotages.getString(j + ".Location.World");
                double x = sabotages.getDouble(j + ".Location.X");
                double y = sabotages.getDouble(j + ".Location.Y");
                double z = sabotages.getDouble(j + ".Location.Z");

                Sabotage sabotage = new Sabotage(j, SabotageType.valueOf(type), arena.getLoc(loc), new Location(Bukkit.getWorld(world), x, y, z));
                arena.getSabotages().add(sabotage);
            });
        }

        // Door groups
        ConfigurationSection doorGroups = a.getConfigurationSection("DoorGroups");
        if (doorGroups != null) {
            doorGroups.getKeys(false).forEach(j -> {
                ConfigurationSection doors = doorGroups.getConfigurationSection(j + ".Doors");
                if (doors != null) {
                    DoorGroup doorGroup = new DoorGroup(j);

                    doors.getKeys(false).forEach(l -> {
                        String loc = doors.getString(l + ".Loc");

                        String world1 = doors.getString(l + ".Location.1.World");
                        double x1 = doors.getDouble(l + ".Location.1.X");
                        double y1 = doors.getDouble(l + ".Location.1.Y");
                        double z1 = doors.getDouble(l + ".Location.1.Z");

                        String world2 = doors.getString(l + ".Location.2.World");
                        double x2 = doors.getDouble(l + ".Location.2.X");
                        double y2 = doors.getDouble(l + ".Location.2.Y");
                        double z2 = doors.getDouble(l + ".Location.2.Z");

                        Door door = new Door(l, arena.getLoc(loc), new Cuboid(new Location(Bukkit.getWorld(world1), x1, y1, z1), new Location(Bukkit.getWorld(world2), x2, y2, z2)));
                        doorGroup.addDoor(door);
                    });
                    arena.getDoorGroups().add(doorGroup);
                }
            });
        }

        boolean showNamesOnChat = a.getBoolean("ShowNamesOnChat");
        boolean showReporterName = a.getBoolean("ShowReporterName");
        boolean showVictimName = a.getBoolean("ShowVictimName");
        boolean showVoterName = a.getBoolean("ShowVoterName");
        boolean showVotedName = a.getBoolean("ShowVotedName");
        boolean showMostVotedName = a.getBoolean("ShowMostVotedName");
        boolean showMostVotedType = a.getBoolean("ShowMostVotedType");

        String status = a.getString("Status");
        if (status.equalsIgnoreCase("ENABLED")) arena.setStatus(ArenaStatus.WAITING);

        arena.setWaiting(waiting);
        arena.setSpawn(spawn);
        arena.setDiscussion(discussion);
        arena.setVoting(voting);
        arena.setEmergencyButton(emergencyButton);
        arena.setShowNamesOnChat(showNamesOnChat);
        arena.setShowReporterName(showReporterName);
        arena.setShowVictimName(showVictimName);
        arena.setShowVoterName(showVoterName);
        arena.setShowVotedName(showVotedName);
        arena.setShowMostVotedName(showMostVotedName);
        arena.setShowMostVotedType(showMostVotedType);
        arenaSet.add(arena);
    }

    public void serialize() {
        if (arenaSet.isEmpty()) return;
        arenas.set("Arenas", null);
        arenaSet.forEach(arena -> {
            // Name
            arenas.set("Arenas." + arena.getName() + ".Name", arena.getName());

            // Waiting
            Location waiting = arena.getWaiting();
            if (waiting != null) {
                arenas.set("Arenas." + arena.getName() + ".Waiting.World", waiting.getWorld().getName());
                arenas.set("Arenas." + arena.getName() + ".Waiting.X", waiting.getX());
                arenas.set("Arenas." + arena.getName() + ".Waiting.Y", waiting.getY());
                arenas.set("Arenas." + arena.getName() + ".Waiting.Z", waiting.getZ());
                arenas.set("Arenas." + arena.getName() + ".Waiting.Yaw", waiting.getYaw());
                arenas.set("Arenas." + arena.getName() + ".Waiting.Pitch", waiting.getPitch());
            }

            // Spawn
            Location spawn = arena.getSpawn();
            if (spawn != null) {
                arenas.set("Arenas." + arena.getName() + ".Spawn.World", spawn.getWorld().getName());
                arenas.set("Arenas." + arena.getName() + ".Spawn.X", spawn.getX());
                arenas.set("Arenas." + arena.getName() + ".Spawn.Y", spawn.getY());
                arenas.set("Arenas." + arena.getName() + ".Spawn.Z", spawn.getZ());
                arenas.set("Arenas." + arena.getName() + ".Spawn.Ya-w", spawn.getYaw());
                arenas.set("Arenas." + arena.getName() + ".Spawn.Pitch", spawn.getPitch());
            }

            // Discussion
            Location discussion = arena.getDiscussion();
            if (discussion != null) {
                arenas.set("Arenas." + arena.getName() + ".Discussion.World", discussion.getWorld().getName());
                arenas.set("Arenas." + arena.getName() + ".Discussion.X", discussion.getX());
                arenas.set("Arenas." + arena.getName() + ".Discussion.Y", discussion.getY());
                arenas.set("Arenas." + arena.getName() + ".Discussion.Z", discussion.getZ());
                arenas.set("Arenas." + arena.getName() + ".Discussion.Yaw", discussion.getYaw());
                arenas.set("Arenas." + arena.getName() + ".Discussion.Pitch", discussion.getPitch());
            }

            // Voting
            Location voting = arena.getVoting();
            if (voting != null) {
                arenas.set("Arenas." + arena.getName() + ".Voting.World", voting.getWorld().getName());
                arenas.set("Arenas." + arena.getName() + ".Voting.X", voting.getX());
                arenas.set("Arenas." + arena.getName() + ".Voting.Y", voting.getY());
                arenas.set("Arenas." + arena.getName() + ".Voting.Z", voting.getZ());
                arenas.set("Arenas." + arena.getName() + ".Voting.Yaw", voting.getYaw());
                arenas.set("Arenas." + arena.getName() + ".Voting.Pitch", voting.getPitch());
            }

            // Emergency Button
            Location emergencyButton = arena.getEmergencyButton();
            if (emergencyButton != null) {
                arenas.set("Arenas." + arena.getName() + ".EmergencyButton.World", emergencyButton.getWorld().getName());
                arenas.set("Arenas." + arena.getName() + ".EmergencyButton.X", emergencyButton.getX());
                arenas.set("Arenas." + arena.getName() + ".EmergencyButton.Y", emergencyButton.getY());
                arenas.set("Arenas." + arena.getName() + ".EmergencyButton.Z", emergencyButton.getZ());
                arenas.set("Arenas." + arena.getName() + ".EmergencyButton.Yaw", emergencyButton.getYaw());
                arenas.set("Arenas." + arena.getName() + ".EmergencyButton.Pitch", emergencyButton.getPitch());
            }

            // LOCS
            Set<Loc> locs = arena.getLocs();
            locs.forEach(loc -> {
                arenas.set("Arenas." + arena.getName() + ".Locs." + loc.getName() + ".Name", loc.getName());
            });

            // Task groups
            Set<TaskGroup> taskGroups = arena.getTaskGroups();
            taskGroups.forEach(taskGroup -> {
                List<Task> tasks = taskGroup.getTasks();
                tasks.forEach(task -> {
                    arenas.set("Arenas." + arena.getName() + ".TaskGroups." + taskGroup.getName() + ".Tasks." + task.getName() + ".Type", task.getType().toString());
                    arenas.set("Arenas." + arena.getName() + ".TaskGroups." + taskGroup.getName() + ".Tasks." + task.getName() + ".Loc", task.getLoc().getName());
                    arenas.set("Arenas." + arena.getName() + ".TaskGroups." + taskGroup.getName() + ".Tasks." + task.getName() + ".Location.World", task.getLocation().getWorld().getName());
                    arenas.set("Arenas." + arena.getName() + ".TaskGroups." + taskGroup.getName() + ".Tasks." + task.getName() + ".Location.X", task.getLocation().getX());
                    arenas.set("Arenas." + arena.getName() + ".TaskGroups." + taskGroup.getName() + ".Tasks." + task.getName() + ".Location.Y", task.getLocation().getY());
                    arenas.set("Arenas." + arena.getName() + ".TaskGroups." + taskGroup.getName() + ".Tasks." + task.getName() + ".Location.Z", task.getLocation().getZ());
                });
            });

            // Vent groups
            Set<VentGroup> ventGroups = arena.getVentGroups();
            ventGroups.forEach(ventGroup -> {
                List<Vent> vents = ventGroup.getVents();
                if (!vents.isEmpty()) arenas.set("Arenas." + arena.getName() + ".VentGroups." + ventGroup.getName() + ".Random", ventGroup.isRandom());
                vents.forEach(vent -> {
                    arenas.set("Arenas." + arena.getName() + ".VentGroups." + ventGroup.getName() + ".Vents." + vent.getName() + ".Loc", vent.getLoc().getName());
                    arenas.set("Arenas." + arena.getName() + ".VentGroups." + ventGroup.getName() + ".Vents." + vent.getName() + ".Location.World", vent.getLocation().getWorld().getName());
                    arenas.set("Arenas." + arena.getName() + ".VentGroups." + ventGroup.getName() + ".Vents." + vent.getName() + ".Location.X", vent.getLocation().getX());
                    arenas.set("Arenas." + arena.getName() + ".VentGroups." + ventGroup.getName() + ".Vents." + vent.getName() + ".Location.Y", vent.getLocation().getY());
                    arenas.set("Arenas." + arena.getName() + ".VentGroups." + ventGroup.getName() + ".Vents." + vent.getName() + ".Location.Z", vent.getLocation().getZ());
                });
            });

            // Camera groups
            Set<CameraGroup> cameraGroups = arena.getCameraGroups();
            cameraGroups.forEach(cameraGroup -> {
                List<Camera> cameras = cameraGroup.getCameras();
                if (!cameras.isEmpty()) arenas.set("Arenas." + arena.getName() + ".CameraGroups." + cameraGroup.getName() + ".Random", cameraGroup.isRandom());
                cameras.forEach(camera -> {
                    arenas.set("Arenas." + arena.getName() + ".CameraGroups." + cameraGroup.getName() + ".Cameras." + camera.getName() + ".Loc", camera.getLoc().getName());
                    arenas.set("Arenas." + arena.getName() + ".CameraGroups." + cameraGroup.getName() + ".Cameras." + camera.getName() + ".Location.World", camera.getLocation().getWorld().getName());
                    arenas.set("Arenas." + arena.getName() + ".CameraGroups." + cameraGroup.getName() + ".Cameras." + camera.getName() + ".Location.X", camera.getLocation().getX());
                    arenas.set("Arenas." + arena.getName() + ".CameraGroups." + cameraGroup.getName() + ".Cameras." + camera.getName() + ".Location.Y", camera.getLocation().getY());
                    arenas.set("Arenas." + arena.getName() + ".CameraGroups." + cameraGroup.getName() + ".Cameras." + camera.getName() + ".Location.Z", camera.getLocation().getZ());
                });
            });

            // Sabotages
            Set<Sabotage> sabotages = arena.getSabotages();
            sabotages.forEach(sabotage -> {
                arenas.set("Arenas." + arena.getName() + ".Sabotages" + sabotage.getName() + ".Type", sabotage.getType().toString());
                arenas.set("Arenas." + arena.getName() + ".Sabotages" + sabotage.getName() + ".Loc", sabotage.getLoc().getName());
                arenas.set("Arenas." + arena.getName() + ".Sabotages" + sabotage.getName() + ".Location.World", sabotage.getLocation().getWorld().getName());
                arenas.set("Arenas." + arena.getName() + ".Sabotages" + sabotage.getName() + ".Location.X", sabotage.getLocation().getX());
                arenas.set("Arenas." + arena.getName() + ".Sabotages" + sabotage.getName() + ".Location.Y", sabotage.getLocation().getY());
                arenas.set("Arenas." + arena.getName() + ".Sabotages" + sabotage.getName() + ".Location.Z", sabotage.getLocation().getZ());
            });

            // Door groups
            Set<DoorGroup> doorGroups = arena.getDoorGroups();
            doorGroups.forEach(doorGroup -> {
                List<Door> doors = doorGroup.getDoors();
                doors.forEach(door -> {
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Loc", door.getLoc().getName());
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Location.1.World", door.getCuboid().getPoint1().getWorld().getName());
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Location.1.X", door.getCuboid().getPoint1().getX());
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Location.1.Y", door.getCuboid().getPoint1().getY());
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Location.1.Z", door.getCuboid().getPoint1().getZ());
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Location.2.World", door.getCuboid().getPoint2().getWorld().getName());
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Location.2.X", door.getCuboid().getPoint2().getX());
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Location.2.Y", door.getCuboid().getPoint2().getY());
                    arenas.set("Arenas." + arena.getName() + ".DoorGroups." + doorGroup.getName() + ".Doors." + door.getName() + ".Location.2.Z", door.getCuboid().getPoint2().getZ());
                });
            });

            arenas.set("Arenas." + arena.getName() + ".ShowNamesOnChat", arena.isShowNamesOnChat());
            arenas.set("Arenas." + arena.getName() + ".ShowReporterName", arena.isShowReporterName());
            arenas.set("Arenas." + arena.getName() + ".ShowVictimName", arena.isShowVictimName());
            arenas.set("Arenas." + arena.getName() + ".ShowVoterName", arena.isShowVoterName());
            arenas.set("Arenas." + arena.getName() + ".ShowVotedName", arena.isShowVotedName());
            arenas.set("Arenas." + arena.getName() + ".ShowMostVotedName", arena.isShowMostVotedName());
            arenas.set("Arenas." + arena.getName() + ".ShowMostVotedType", arena.isShowMostVotedType());

            arenas.set("Arenas." + arena.getName() + ".Status", arena.disabled() ? "DISABLED" : "ENABLED");
        });
        arenas.save();
    }

    public Arena getArena(AS as) {
        return arenaSet.stream().filter(arena -> arena.inGame(as)).findFirst().orElse(null);
    }

    public Arena getArena(String name) {
        return arenaSet.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void addArena(Arena arena) {
        this.arenaSet.add(arena);
    }

    public void removeArena(Arena arena) {
        this.arenaSet.remove(arena);
    }

    public Set<Arena> getArenaList() {
        return Collections.unmodifiableSet(arenaSet);
    }

    public TArenaManager gettArenaManager() {
        return tArenaManager;
    }
}

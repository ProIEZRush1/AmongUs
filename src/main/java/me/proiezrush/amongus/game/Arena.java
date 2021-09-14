package me.proiezrush.amongus.game;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.ArenaPhase;
import me.proiezrush.amongus.game.arena.cameras.Camera;
import me.proiezrush.amongus.game.arena.cameras.CameraGroup;
import me.proiezrush.amongus.game.arena.sabotage.Sabotage;
import me.proiezrush.amongus.game.arena.sabotage.doors.Door;
import me.proiezrush.amongus.game.arena.sabotage.doors.DoorGroup;
import me.proiezrush.amongus.game.arena.loc.Loc;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.arena.tasks.TaskGroup;
import me.proiezrush.amongus.game.arena.vents.VentGroup;
import me.proiezrush.amongus.game.manager.*;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.ArenaStatus;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.game.arena.tasks.Task;
import me.proiezrush.amongus.game.arena.vents.Vent;
import me.proiezrush.amongus.util.nms.corpses.ICorpse;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;

public class Arena {

    // General lists
    private final List<ICorpse> corpses;

    // Managers
    private final VotingManager votingManager;
    private final CountdownManager countdownManager;
    private final BossbarManager bossbarManager;
    private final ScoreboardManager scoreboardManager;
    private final TaskManager taskManager;
    private final CameraManager cameraManager;
    private final VentManager ventManager;

    // Objects
    private final String name;
    private Location waiting;
    private Location spawn;
    private Location discussion;
    private Location voting;
    private Location emergencyButton;
    private final Set<Loc> locs;
    private final Set<TaskGroup> taskGroups;
    private final Set<VentGroup> ventGroups;
    private final Set<CameraGroup> cameraGroups;
    private final Set<DoorGroup> doorGroups;
    private final Set<Sabotage> sabotages;

    // Players lists
    private final Set<ASPlayer> players;
    private Set<ASImpostor> impostors;
    private Set<ASCrewmate> crewmates;

    // Booleans
    private boolean showNamesOnChat;
    private boolean showReporterName;
    private boolean showVictimName;
    private boolean showVoterName;
    private boolean showVotedName;
    private boolean showMostVotedName;
    private boolean showMostVotedType;

    // Statuses
    private ArenaStatus status;
    private ArenaPhase phase;

    // Int data
    private int i;
    private int actualPlayers;
    private int minPlayers;
    private int maxPlayers;

    public Arena(AmongUs plugin, String name) {
        this.corpses = new ArrayList<>();

        this.votingManager = new VotingManager(plugin);
        this.countdownManager = new CountdownManager(plugin, this);
        this.bossbarManager = new BossbarManager(plugin, this);
        this.scoreboardManager = new ScoreboardManager(plugin, this);
        this.taskManager = new TaskManager(plugin, this);
        this.cameraManager = new CameraManager(plugin, this);
        this.ventManager = new VentManager(plugin, this);

        this.name = name;
        this.locs = new HashSet<>();
        this.taskGroups = new HashSet<>();
        this.ventGroups = new HashSet<>();
        this.cameraGroups = new HashSet<>();
        this.doorGroups = new HashSet<>();
        this.sabotages = new HashSet<>();

        this.players = new HashSet<>();
        this.impostors = new HashSet<>();
        this.crewmates = new HashSet<>();

        this.showNamesOnChat = true;
        this.showReporterName = true;
        this.showVictimName = true;
        this.showVoterName = true;
        this.showVotedName = true;
        this.showMostVotedName = true;
        this.showMostVotedType = true;

        this.status = ArenaStatus.DISABLED;
        this.phase = ArenaPhase.WAITING;

        this.i = 1;
        this.minPlayers = 2;
        this.maxPlayers = 10;
    }

    public List<ICorpse> getCorpses() {
        return corpses;
    }

    public VotingManager getVotingManager() {
        return votingManager;
    }

    public CountdownManager getCountdownManager() {
        return countdownManager;
    }

    public BossbarManager getBossbarManager() {
        return bossbarManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public VentManager getVentManager() {
        return ventManager;
    }

    public String getName() {
        return name;
    }

    public Location getWaiting() {
        return waiting;
    }

    public void setWaiting(Location waiting) {
        this.waiting = waiting;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getDiscussion() {
        return discussion;
    }

    public void setDiscussion(Location discussion) {
        this.discussion = discussion;
    }

    public Location getVoting() {
        return voting;
    }

    public void setVoting(Location voting) {
        this.voting = voting;
    }

    public Location getEmergencyButton() {
        return emergencyButton;
    }

    public void setEmergencyButton(Location emergencyButton) {
        this.emergencyButton = emergencyButton;
    }

    public Set<Loc> getLocs() {
        return locs;
    }

    public Set<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    public Set<VentGroup> getVentGroups() {
        return ventGroups;
    }

    public Set<CameraGroup> getCameraGroups() {
        return cameraGroups;
    }

    public Set<DoorGroup> getDoorGroups() {
        return doorGroups;
    }

    public Set<Sabotage> getSabotages() {
        return sabotages;
    }

    public Set<ASPlayer> getPlayers() {
        return players;
    }

    public boolean isShowNamesOnChat() {
        return showNamesOnChat;
    }

    public void setShowNamesOnChat(boolean showNamesOnChat) {
        this.showNamesOnChat = showNamesOnChat;
    }

    public boolean isShowReporterName() {
        return showReporterName;
    }

    public void setShowReporterName(boolean showReporterName) {
        this.showReporterName = showReporterName;
    }

    public boolean isShowVictimName() {
        return showVictimName;
    }

    public void setShowVictimName(boolean showVictimName) {
        this.showVictimName = showVictimName;
    }

    public boolean isShowVoterName() {
        return showVoterName;
    }

    public void setShowVoterName(boolean showVoterName) {
        this.showVoterName = showVoterName;
    }

    public boolean isShowVotedName() {
        return showVotedName;
    }

    public void setShowVotedName(boolean showVotedName) {
        this.showVotedName = showVotedName;
    }

    public boolean isShowMostVotedName() {
        return showMostVotedName;
    }

    public void setShowMostVotedName(boolean showMostVotedName) {
        this.showMostVotedName = showMostVotedName;
    }

    public boolean isShowMostVotedType() {
        return showMostVotedType;
    }

    public void setShowMostVotedType(boolean showMostVotedType) {
        this.showMostVotedType = showMostVotedType;
    }

    public void setStatus(ArenaStatus status) {
        this.status = status;
    }

    public ArenaStatus getStatus() {
        return status;
    }

    public void setPhase(ArenaPhase phase) {
        this.phase = phase;
    }

    public ArenaPhase getPhase() {
        return phase;
    }

    public void setImpostors(Set<ASImpostor> impostors) {
        this.impostors = impostors;
    }

    public Set<ASImpostor> getImpostors() {
        return impostors;
    }


    public void setCrewmates(Set<ASCrewmate> crewmates) {
        this.crewmates = crewmates;
    }

    public Set<ASCrewmate> getCrewmates() {
        return crewmates;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public int getActualPlayers() {
        actualPlayers = players.size();
        return actualPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    // Util methods
    public void add(ASPlayer as) {
        players.add(as);
    }

    public void remove(ASPlayer as) {
        players.remove(as);
        if (getImpostor(as) != null) impostors.remove(getImpostor(as));
        if (getCrewmate(as) != null) crewmates.remove(getCrewmate(as));
    }

    public boolean inGame(AS as) {
        return getPlayers().stream().filter(player -> player.getPlayer().getName().equals(as.getPlayer().getName())).findFirst().orElse(null) != null;
    }

    public boolean disabled() {
        return status.equals(ArenaStatus.DISABLED);
    }

    public boolean starting() {
        return status.equals(ArenaStatus.STARTING);
    }

    public boolean finishing() {
        return status.equals(ArenaStatus.FINISHING);
    }

    public boolean inGame() {
        return status.equals(ArenaStatus.GAME) || status.equals(ArenaStatus.FINISHING);
    }

    public boolean discussion() {
        return phase.equals(ArenaPhase.DISCUSSION);
    }

    public boolean voting() {
        return phase.equals(ArenaPhase.VOTING);
    }

    public boolean game() {
        return phase.equals(ArenaPhase.GAME);
    }

    public Loc getLoc(String name) {
        for (Loc loc : locs) {
            if (loc.getName().equals(name)) {
                return loc;
            }
        }
        return null;
    }

    public TaskGroup getTaskGroup(String name) {
        for (TaskGroup taskGroup : taskGroups) {
            if (taskGroup.getName().equals(name)) {
                return taskGroup;
            }
        }
        return null;
    }

    public TaskGroup getTaskGroup(Location taskLocation) {
        for (TaskGroup taskGroup : taskGroups) {
            for (Task task : taskGroup.getTasks()) {
                if (task.getLocation().equals(taskLocation)) {
                    return taskGroup;
                }
            }
        }
        return null;
    }

    public VentGroup getVentGroup(String name) {
        for (VentGroup ventGroup : ventGroups) {
            if (ventGroup.getName().equals(name)) {
                return ventGroup;
            }
        }
        return null;
    }

    public VentGroup getVentGroup(Location ventLocation) {
        for (VentGroup ventGroup : ventGroups) {
            for (Vent vent : ventGroup.getVents()) {
                if (vent.getLocation().equals(ventLocation)) {
                    return ventGroup;
                }
            }
        }
        return null;
    }

    public CameraGroup getCameraGroup(String name) {
        for (CameraGroup cameraGroup : cameraGroups) {
            if (cameraGroup.getName().equals(name)) {
                return cameraGroup;
            }
        }
        return null;
    }

    public CameraGroup getCameraGroup(Location cameraLocation) {
        for (CameraGroup cameraGroup : cameraGroups) {
            for (Camera camera : cameraGroup.getCameras()) {
                if (camera.getLocation().equals(cameraLocation)) {
                    return cameraGroup;
                }
            }
        }
        return null;
    }

    public Sabotage getSabotage(Location sabotageLocation) {
        for (Sabotage sabotage : sabotages) {
            if (sabotage.getLocation().equals(sabotageLocation)) {
                return sabotage;
            }
        }
        return null;
    }

    public DoorGroup getDoorGroup(String name) {
        for (DoorGroup doorGroup : doorGroups) {
            if (doorGroup.getName().equals(name)) {
                return doorGroup;
            }
        }
        return null;
    }

    public DoorGroup getDoorGroup(Block doorBlock) {
        for (DoorGroup doorGroup : doorGroups) {
            for (Door door : doorGroup.getDoors()) {
                Iterator<Block> blocks = door.getCuboid().blockList();
                while (blocks.hasNext()) {
                    if (blocks.next().equals(doorBlock)) {
                        return doorGroup;
                    }
                }
            }
        }
        return null;
    }

    public boolean finish() {
        int impostors = 0;
        int crewmates = 0;

        for (ASImpostor as : this.impostors) if (!as.isDeath()) impostors++;
        for (ASCrewmate as : this.crewmates) if (!as.isDeath()) crewmates++;

        return players.isEmpty();
    }

    public String winner() {
        int impostors = 0;
        int crewmates = 0;

        for (ASImpostor as : this.impostors) if (!as.isDeath()) impostors++;
        for (ASCrewmate as : this.crewmates) if (!as.isDeath()) crewmates++;

        String winner = null;

        if (impostors == crewmates) winner = "Impostors";
        if (impostors == 0) winner = "Crewmates";
        if (crewmates > impostors) winner = "Crewmates";
        if (impostors > crewmates) winner = "Impostors";

        // Check if all tasks were made
        if (getMadeTasks() == getTotalTasks()) winner = "Crewmates";

        return winner;
    }

    public int getMadeTasks() {
        int i = 0;
        for (ASCrewmate crewmate : crewmates) {
            for (Task task : crewmate.getAssignedTasks().getTasks()) {
                if (task.isDone()) {
                    i++;
                }
            }
        }
        return i;
    }

    public int getTotalTasks() {
        int i = 0;
        for (ASCrewmate crewmate : crewmates) {
            i += crewmate.getAssignedTasks().getTasks().size();
        }
        return i;
    }

    public ASPlayer getPlayer(AS as) {
        for (ASPlayer player : players) {
            if (player.getPlayer().equals(as.getPlayer())) {
                return player;
            }
        }
        return null;
    }

    public boolean isImpostor(AS as) {
        return getImpostor(as) != null;
    }

    public ASImpostor getImpostor(AS as) {
        for (ASImpostor asImpostor : impostors) {
            if (asImpostor.getPlayer().equals(as.getPlayer())) {
                return asImpostor;
            }
        }
        return null;
    }

    public boolean isCrewmate(AS as) {
        return getCrewmate(as) != null;
    }

    public ASCrewmate getCrewmate(AS as) {
        for (ASCrewmate asCrewmate : crewmates) {
            if (asCrewmate.getPlayer().equals(as.getPlayer())) {
                return asCrewmate;
            }
        }
        return null;
    }
}

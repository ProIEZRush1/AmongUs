package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.ArenaPhase;
import me.proiezrush.amongus.game.arena.ArenaStatus;
import me.proiezrush.amongus.game.arena.player.*;
import me.proiezrush.amongus.game.arena.tasks.TaskGroup;
import me.proiezrush.amongus.util.*;
import me.proiezrush.amongus.util.nms.corpses.CorpseManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TArenaManager {

    private final AmongUs plugin;
    private final ArenaManager arenaManager;
    private final Settings config;
    private final Settings messages;
    public TArenaManager(AmongUs plugin, ArenaManager arenaManager) {
        this.plugin = plugin;
        this.arenaManager = arenaManager;
        this.config = plugin.getC();
        this.messages = plugin.getMessages();
    }

    public void join(AS as, Arena arena) {
        Player player = as.getPlayer();

        // Add player to arena
        ASPlayer asPlayer = new ASPlayer(plugin, as);
        arena.add(asPlayer);

        // Clear & Save Data
        asPlayer.getAsData().clear();

        // Teleport to waiting
        player.teleport(arena.getWaiting());

        // Set waiting scoreboard
        ScoreboardManager scoreboardManager = arena.getScoreboardManager();
        scoreboardManager.setWaitingScoreboard(player);

        // Play join sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.PlayerJoin")));

        // Join message
        msg(arena, "PlayerJoined", "%player%", player.getName());

        // Checkers
        if (arena.getActualPlayers() == arena.getMinPlayers()) {
            // Change status to starting
            arena.setStatus(ArenaStatus.STARTING);

            // Start start countdown
            CountdownManager countdownManager = arena.getCountdownManager();
            countdownManager.startStartCountdown();
        }
    }

    public void leave(ASPlayer as, Arena arena) {
        Player player = as.getPlayer();

        // Remove player from arena
        arena.remove(as);

        // Restore Data & Teleport back
        as.getAsData().restore();

        // Reset votes
        as.setVoted(false);
        as.setVotes(0);

        // Remove all bossbars
        BossbarManager bossbarManager = arena.getBossbarManager();
        bossbarManager.removeTasksBar(player);
        bossbarManager.removeDiscussionBar(player);
        bossbarManager.removeVotingBar(player);

        // Remove all scoreboards
        ScoreboardManager scoreboardManager = arena.getScoreboardManager();
        scoreboardManager.removeWaitingScoreboard(player);
        scoreboardManager.removeGameScoreboard(player);
        scoreboardManager.removeFinishingScoreboard(player);

        // Play leave sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.PlayerLeave")));

        // Leave message
        msg(arena, "PlayerLeft","%player%", player.getName());

        // Checkers
        if (arena.getActualPlayers() < arena.getMinPlayers()) {
            if (arena.starting()) {
                // Change status to waiting
                arena.setStatus(ArenaStatus.WAITING);

                // Stop start countdown
                CountdownManager countdownManager = arena.getCountdownManager();
                countdownManager.stopStartCountdown();
            }
        }

        if (arena.inGame()) {
            if (arena.finish()) {
                // Finish arena
                finish(arena, !plugin.running);
            }
        }
    }

    public void start(Arena arena) {
        // Change status
        arena.setStatus(ArenaStatus.GAME);
        arena.setPhase(ArenaPhase.GAME);

        // Teleport players to spawn
        for (ASPlayer as : arena.getPlayers()) {
            Player player = as.getPlayer();
            player.teleport(arena.getSpawn());
        }

        // Set Impostors & Crewmates
        setTeams(arena);

        // Give effects to players
        effects(arena);

        // Give color armor & give items
        setColor(arena);
        itemsAndArmor(arena);

        // Set tasks bossbar
        BossbarManager bossbarManager = arena.getBossbarManager();
        bossbarManager.startTasksBar();

        ScoreboardManager scoreboardManager = arena.getScoreboardManager();
        for (ASPlayer as : arena.getPlayers()) {
            Player player = as.getPlayer();

            // Remove waiting scoreboard && set game scoreboard
            scoreboardManager.removeWaitingScoreboard(player);
            scoreboardManager.setGameScoreboard(as);

            // Play start sound
            SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.GameStart")));
        }

        // Titles
        startTitles(arena);

        // Send start message
        msg(arena, "GameStart");
    }

    public void finish(Arena arena, boolean closing) {
        String winner = arena.winner();

        // Change status
        arena.setStatus(ArenaStatus.FINISHING);
        arena.setPhase(ArenaPhase.FINISHING);

        // Remove all corpses
        CorpseManager.removeAll(arena);

        // Stop bars tasks
        BossbarManager bossbarManager = arena.getBossbarManager();
        bossbarManager.stopTasksBar();
        bossbarManager.stopDiscussionBar();
        bossbarManager.stopVotingBar();

        ScoreboardManager scoreboardManager = arena.getScoreboardManager();

        // Teleport players to spawn & Clear effects
        List<ASPlayer> players = new ArrayList<>(arena.getPlayers());
        for (ASPlayer as : players) {
            Player player = as.getPlayer();
            player.teleport(arena.getSpawn());

            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }

            if (closing) {
                leave(as, arena);
            }

            if (!closing) {
                // Remove game scoreboard && Set finishing scoreboard
                scoreboardManager.removeGameScoreboard(player);
                scoreboardManager.setFinishingScoreboard(player);

                // Play finish sounds
                SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.GameFinish")));
            }
        }

        // Stop all countdowns
        CountdownManager countdownManager = arena.getCountdownManager();
        countdownManager.stopAllCountdowns();

        if (!closing) {
            // Send titles
            finishTitles(arena, winner);

            // Finish message
            msg(arena, "GameFinish", "%winner%", winner);

            // Start finish countdown
            countdownManager.startFinishCountdown();
        }

        // Reload arena
        arenaManager.removeArena(arena);
        arenaManager.loadArena(arena.getName());
    }

    public void kill(ASImpostor asImpostor, ASCrewmate asCrewmate, Arena arena) {
        // Spawn corpse
        CorpseManager.spawn(plugin, arena, asCrewmate);

        // Set crewmate death & teleport to impostor
        Player crewmatePlayer = asCrewmate.getPlayer();
        crewmatePlayer.setHealth(0);
        crewmatePlayer.spigot().respawn();
        crewmatePlayer.setGameMode(GameMode.SPECTATOR);
        crewmatePlayer.teleport(asImpostor.getPlayer().getLocation());
        asCrewmate.setDeath(true);

        // Play sounds
        SoundUtil.playSound(asImpostor.getPlayer(), Sound.valueOf(config.get(null, "Sounds.PlayerKill")));
        SoundUtil.playSound(crewmatePlayer, Sound.valueOf(config.get(null, "Sounds.PlayerKilled")));

        // Add impostor to cooldown & remove knife
        asImpostor.getKnifeCooldown().start();
        asImpostor.getPlayer().getInventory().remove(asImpostor.getImpostorItems().getKnife().build());

        // Check if finish the game
        if (arena.finish()) {
            finish(arena, false);
        }
    }

    public void report(ASPlayer reporter, ASPlayer victim, Arena arena) {
        // Set phase to DISCUSSION
        arena.setPhase(ArenaPhase.DISCUSSION);

        // Remove all corpses
        CorpseManager.removeAll(arena);

        // Teleport players to discussion location & Clear inventory & Remove effects
        for (ASPlayer as : arena.getPlayers()) {
            Player player = as.getPlayer();
            player.teleport(arena.getDiscussion());
            InventoryUtils.clearJustItems(player);
            for (PotionEffect potionEffect : player.getActivePotionEffects()) player.removePotionEffect(potionEffect.getType());

            // Play report sound
            SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.PlayerReported")));
        }

        // Send titles
        String reporterName = reporter.getDisplay(arena.isShowReporterName());
        String victimName = victim.getDisplay(arena.isShowVictimName());
        reportTitles(reporterName, victimName, arena);

        // Start discussion countdown
        CountdownManager countdownManager = arena.getCountdownManager();
        countdownManager.startDiscussionCountdown();

        // Start discussion bar
        BossbarManager bossbarManager = arena.getBossbarManager();
        bossbarManager.startDiscussionBar();
    }

    public void vote(Arena arena) {
        // Set phase to VOTING
        arena.setPhase(ArenaPhase.VOTING);

        // Stop discussion bar
        BossbarManager bossbarManager = arena.getBossbarManager();
        bossbarManager.stopDiscussionBar();

        // Teleport players to voting location & give voting items
        for (ASPlayer as : arena.getPlayers()) {
            Player player = as.getPlayer();
            player.teleport(arena.getVoting());

            as.getVotingItems().voting();

            // Play vote phase sounds
            SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.VotePhase")));
        }

        // Send titles
        votingTitles(arena);

        // Start voting countdown
        CountdownManager countdownManager = arena.getCountdownManager();
        countdownManager.startVotingCountdown();

        // Start voting bar
        bossbarManager.startVotingBar();
    }

    public void play(Arena arena) {
        // Set phase to GAME
        arena.setPhase(ArenaPhase.GAME);

        // Stop voting bar
        BossbarManager bossbarManager = arena.getBossbarManager();
        bossbarManager.stopVotingBar();

        // Set voted death and in spectator mode
        ASPlayer voted = arena.getVotingManager().getVoted(arena);
        if (voted != null) {
            Player player = voted.getPlayer();
            voted.setDeath(true);
            player.setGameMode(GameMode.SPECTATOR);

            // Play play phase sounds
            SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.PlayPhase")));
        }

        // Teleport players to spawn
        for (ASPlayer as : arena.getPlayers()) {
            Player player = as.getPlayer();
            player.teleport(arena.getSpawn());

            // Clear player's inventory
            player.getInventory().clear();

            // Reset votes
            as.setVoted(false);
            as.setVotes(0);
        }

        // Send titles
        boolean isNull = voted == null;
        String votedName = !isNull ? voted.getDisplay(arena.isShowMostVotedName()) : null;
        String votedType = !isNull ? voted.getType().getDisplay(arena.isShowMostVotedType()) : "";
        playTitles(votedName, votedType, arena);

        // Give effects to players
        effects(arena);

        // Give items
        itemsAndArmor(arena);
    }

    // Util methods
    private void setTeams(Arena arena) {
        List<ASPlayer> players = new ArrayList<>(arena.getPlayers());
        Set<ASImpostor> impostors = new HashSet<>();
        Set<ASCrewmate> crewmates = new HashSet<>();

        int i = arena.getI(); // Number of impostors
        List<Integer> a = random(i, players.size()-1);

        // Create impostors and add them to the list
        for (int x : a) {
            ASPlayer player = players.get(x);
            ASImpostor impostor = new ASImpostor(plugin, player);
            impostors.add(impostor);
            player.setType(ASType.IMPOSTOR);
            impostor.setType(ASType.IMPOSTOR);
        }

        for (int j=0;j<players.size();j++) {
            if (!a.contains(j)) {
                ASPlayer player = players.get(j);
                ASCrewmate crewmate = new ASCrewmate(plugin, player);

                // Assign tasks to a random task group
                List<TaskGroup> taskGroups = new ArrayList<>(arena.getTaskGroups());
                int random = NumUtil.random(0, taskGroups.size()-1);
                crewmate.setAssignedTasks(taskGroups.get(random));

                crewmates.add(crewmate);
                player.setType(ASType.CREWMATE);
                crewmate.setType(ASType.CREWMATE);
            }
        }

        arena.setImpostors(impostors);
        arena.setCrewmates(crewmates);
    }

    private List<Integer> random(int i, int size) {
        List<Integer> a = new ArrayList<>(); // Number of selected impostors
        for (int j=0;j<i;j++) {
            int random = NumUtil.random(0, size);
            if (a.contains(random)) {
                random(i, size);
            }
            else {
                a.add(random);
            }
        }
        return a;
    }

    private void effects(Arena arena) {
        List<String> effects = config.getList("GameEffects");
        for (ASPlayer as : arena.getPlayers()) {
            for (String s : effects) {
                String[] values = s.split(":");
                String effect = values[0];
                int level = Integer.parseInt(values[1]);

                Player p = as.getPlayer();
                p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), 1000000000, level-1));
            }
        }
    }

    private void setColor(Arena arena) {
        for (ASPlayer as : arena.getPlayers()) {
            Color color = Color.fromRGB(NumUtil.random(0, 255), NumUtil.random(0, 255), NumUtil.random(0, 255));
            as.setColor(color);
        }
    }

    private void itemsAndArmor(Arena arena) {
        for (ASPlayer as : arena.getPlayers()) {
            as.getArmor().armor(as.getPlayer(), as.getColor());
        }

        for (ASImpostor as : arena.getImpostors()) {
            as.getImpostorItems().impostor(arena);
        }

        for (ASCrewmate as : arena.getCrewmates()) {
            as.getCrewmateItems().crewmate(arena);
        }
    }

    private void startTitles(Arena arena) {
        String title = messages.get(null, "GameStartTitle");
        String subtitle = messages.get(null, "GameStartSubtitle");
        for (ASImpostor as : arena.getImpostors()) {
            Player p = as.getPlayer();
            TitleManager.sendTitle(p, 10, 50, 10, title.replace("%type%", as.getType().toString()), subtitle.replace("%type%", as.getType().toString()));
        }

        for (ASCrewmate as : arena.getCrewmates()) {
            Player p = as.getPlayer();
            TitleManager.sendTitle(p, 10, 50, 10, title.replace("%type%", as.getType().toString()), subtitle.replace("%type%", as.getType().toString()));
        }
    }

    private void finishTitles(Arena arena, String winner) {
        String title = messages.get(null, "GameFinishTitle");
        String subtitle = messages.get(null, "GameFinishSubtitle");
        for (ASPlayer as : arena.getPlayers()) {
            Player p = as.getPlayer();
            TitleManager.sendTitle(p, 10, 50, 10, title.replace("%winner%", winner), subtitle.replace("%winner%", winner));
        }
    }

    private void reportTitles(String reporter, String victim, Arena arena) {
        String title = messages.get(null, "ReportTitle");
        String subtitle = messages.get(null, "ReportSubtitle");
        for (ASPlayer as : arena.getPlayers()) {
            Player p = as.getPlayer();
            TitleManager.sendTitle(p, 10, 50, 10, title.replace("%reporter%", reporter).replace("%victim%", victim), subtitle.replace("%reporter%", reporter).replace("%victim%", victim));
        }
    }

    private void votingTitles(Arena arena) {
        String title = messages.get(null, "VotingTitle");
        String subtitle = messages.get(null, "VotingSubtitle");
        for (ASPlayer as : arena.getPlayers()) {
            Player p = as.getPlayer();
            TitleManager.sendTitle(p, 10, 50, 10, title, subtitle);
        }
    }

    private void playTitles(String voted, String type, Arena arena) {
        String title = messages.get(null, "PlayTitle");
        String subtitle = messages.get(null, "PlaySubtitle");
        for (ASPlayer as : arena.getPlayers()) {
            Player p = as.getPlayer();

            String newTitle = title;
            String newSubtitle = subtitle;
            if (newTitle.contains("<voted>") && newTitle.contains("</voted>")) {
                newTitle = voted != null ? title.replace("<voted>", "").replace("</voted>", "").replace("%type%", type) :
                        title.replace(StringUtils.substringBetween(title, "<voted>", "</voted>"), "").replace("<voted></voted>", "");
            }
            if (newSubtitle.contains("<voted>") && newSubtitle.contains("</voted>")) {
                newSubtitle = voted != null ? subtitle.replace("<voted>", "").replace("</voted>", "").replace("%type%", type) :
                        subtitle.replace(StringUtils.substringBetween(subtitle, "<voted>", "</voted>"), "").replace("<voted></voted>", "");
            }

            if (voted == null) voted = "NO ONE";

            TitleManager.sendTitle(p, 10, 50, 10, newTitle.replace("%voted%", voted), newSubtitle.replace("%voted%", voted));
        }
    }

    private void msg(Arena arena, String a, String s, String r) {
        arena.getPlayers().forEach(as -> {
            Player p = as.getPlayer();
            p.sendMessage(messages.get(p, a).replace(s, r));
        });
    }

    private void msg(Arena arena, String a) {
        arena.getPlayers().forEach(as -> {
            Player p = as.getPlayer();
            p.sendMessage(messages.get(p, a));
        });
    }

}

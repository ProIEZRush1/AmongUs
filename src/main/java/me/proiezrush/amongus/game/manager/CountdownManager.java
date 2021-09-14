package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.manager.cowntdown.*;

import java.util.ArrayList;
import java.util.List;

public class CountdownManager {

    private final AmongUs plugin;
    private final Arena arena;

    private final List<Countdown> countdowns;
    public CountdownManager(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;

        this.countdowns = new ArrayList<>();
    }

    public void startStartCountdown() {
        StartCountdown startCountdown = new StartCountdown(plugin, arena);
        countdowns.add(startCountdown);
        startCountdown.start();
    }

    public void stopStartCountdown() {
        StartCountdown startCountdown = (StartCountdown) getCountdown(StartCountdown.class);
        startCountdown.stop();
        countdowns.remove(startCountdown);
    }

    public void startDiscussionCountdown() {
        DiscussionCountdown discussionCountdown = new DiscussionCountdown(plugin, arena);
        countdowns.add(discussionCountdown);
        discussionCountdown.start();
    }

    public void startVotingCountdown() {
        VotingCountdown votingCountdown = new VotingCountdown(plugin, arena);
        countdowns.add(votingCountdown);
        votingCountdown.start();
    }

    public void startFinishCountdown() {
        FinishCountdown finishCountdown = new FinishCountdown(plugin, arena);
        countdowns.add(finishCountdown);
        finishCountdown.start();
    }

    public void stopAllCountdowns() {
        for (Countdown countdown : countdowns) {
            countdown.stop();
        }
        countdowns.clear();
    }

    public Countdown getCountdown(Class<?> t) {
        for (Countdown countdown : countdowns) {
            if (t.isAssignableFrom(countdown.getClass())) {
                return countdown;
            }
        }
        return null;
    }
}

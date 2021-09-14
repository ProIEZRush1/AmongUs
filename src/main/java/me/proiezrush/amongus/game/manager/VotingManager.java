package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASCrewmate;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.voting.VotingInventory;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.SoundUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class VotingManager {

    private final Settings config;
    private final Settings messages;
    private final VotingInventory votingInventory;
    public VotingManager(AmongUs plugin) {
        this.config = plugin.getC();
        this.messages = plugin.getMessages();
        this.votingInventory = new VotingInventory(plugin);
    }

    public void vote(Arena arena, ASPlayer voter, ASPlayer voted) {
        // Add vote to the voted
        voted.setVotes(voted.getVotes() + 1);

        // Set voted to the voter
        voter.setVoted(true);

        // Remove vote item & close inventory
        Player p = voter.getPlayer();
        p.getInventory().remove(voter.getVotingItems().getVote().build());
        p.closeInventory();

        for (AS as : arena.getPlayers()) {
            Player player = as.getPlayer();

            String voterName = as.getDisplay(arena.isShowVoterName());
            String votedName = as.getDisplay(arena.isShowVotedName());
            player.sendMessage(messages.get(p, "VoteGlobal").replace("%voter%", voterName).replace("%voted%", votedName).replace("%votes%", String.valueOf(voted.getVotes())));
        }
        p.sendMessage(messages.get(p, "Vote").replace("%voted%", voted.getPlayer().getName()).replace("%votes%", String.valueOf(voted.getVotes())));

        // Play vote sound
        SoundUtil.playSound(p, Sound.valueOf(config.get(null, "Sounds.PlayerVote")));
    }

    public ASPlayer getVoted(Arena arena) {
        ASPlayer voted = null;

        for (ASPlayer as : arena.getPlayers()) {
            if (voted == null && as.getVotes() > 0) {
                voted = as;
                continue;
            }

            if (voted != null) {
                if (as.getVotes() > voted.getVotes()) voted = as;
            }
        }

        return voted;
    }

    public VotingInventory getVotingInventory() {
        return votingInventory;
    }
}

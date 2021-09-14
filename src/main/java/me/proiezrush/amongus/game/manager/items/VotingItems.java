package me.proiezrush.amongus.game.manager.items;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.manager.items.voting.Vote;
import me.proiezrush.amongus.util.Settings;
import org.bukkit.entity.Player;

public class VotingItems {

    // Objects
    private final Settings items;
    private final AS as;

    private Vote vote;

    public VotingItems(AmongUs plugin, AS as) {
        this.items = plugin.getItems();
        this.as = as;

        this.vote = new Vote(plugin);
    }

    public void voting() {
        Player p = as.getPlayer();

        int voteSlot = items.getInt("Voting.Vote.Slot");

        p.getInventory().setItem(voteSlot, vote.build());
    }

    public Vote getVote() {
        return vote;
    }
}

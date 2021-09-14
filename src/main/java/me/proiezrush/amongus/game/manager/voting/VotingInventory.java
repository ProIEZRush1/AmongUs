package me.proiezrush.amongus.game.manager.voting;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import me.proiezrush.amongus.game.manager.voting.items.VotePlayerItem;
import me.proiezrush.amongus.util.NumUtil;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.inventory.InventoryBuilder;
import org.bukkit.inventory.Inventory;

public class VotingInventory extends InventoryBuilder {

    private final AmongUs plugin;
    private final Settings config;
    private final String name;
    public VotingInventory(AmongUs plugin) {
        super();

        this.plugin = plugin;
        this.config = plugin.getC();
        this.name = config.get(null, "Inventories.Voting.Name");
    }

    public String getName() {
        return name;
    }

    public Inventory build(Arena arena) {
        this.setup(name, NumUtil.round(arena.getActualPlayers(), 9));

        int slot = 0;
        for (ASPlayer as : arena.getPlayers()) {
            if (!as.isDeath()) {
                VotePlayerItem votePlayerItem = new VotePlayerItem(plugin, as);

                this.addIcon(votePlayerItem);
                this.setItem(votePlayerItem.build(), slot);

                slot++;
            }
        }
        return super.build();
    }
}

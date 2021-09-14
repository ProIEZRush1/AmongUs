package me.proiezrush.amongus.game.manager;

import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.ASImpostor;
import me.proiezrush.amongus.game.arena.vents.Vent;
import me.proiezrush.amongus.game.arena.vents.VentGroup;
import me.proiezrush.amongus.util.NumUtil;
import me.proiezrush.amongus.util.Settings;
import me.proiezrush.amongus.util.SoundUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class VentManager {

    private final AmongUs plugin;
    private final ArenaManager arenaManager;
    private final Settings config;

    private final Arena arena;
    public VentManager(AmongUs plugin, Arena arena) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
        this.config = plugin.getC();

        this.arena = arena;
    }

    public void enterVents(ASImpostor asImpostor, Vent vent) {
        Player player = asImpostor.getPlayer();

        asImpostor.setVents(true);
        asImpostor.setLastLocation(player.getLocation());

        // Give invisibility and remove armor
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 0));
        player.getInventory().clear();

        // Play open animation
        Block block = vent.getLocation().getBlock();
        playOpenAnimation(block);

        asImpostor.setLastVent(vent);
        player.teleport(vent.getLocation());

        playCloseAnimation(block);

        // Give vent items
        asImpostor.getImpostorItems().vents();

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.EnterVents")));
    }

    public void nextVent(ASImpostor asImpostor) {
        Player player = asImpostor.getPlayer();

        Arena arena = arenaManager.getArena(asImpostor);
        Vent lastVent = asImpostor.getLastVent();
        VentGroup ventGroup = arena.getVentGroup(lastVent.getLocation());

        int ventIndex = asImpostor.getVentIndex();
        if (!ventGroup.isRandom()) {
            if (ventIndex + 1 < ventGroup.getVents().size()) {
                asImpostor.setVentIndex(ventIndex + 1);
            }
        }
        else {
            int random = NumUtil.random(0, ventGroup.getVents().size());
            while (random == ventIndex) {
                random = NumUtil.random(0, ventGroup.getVents().size());
            }

            asImpostor.setVentIndex(random);
        }

        asImpostor.setVentGroupIndex(new ArrayList<>(arena.getVentGroups()).indexOf(ventGroup));

        ventIndex = asImpostor.getVentIndex();
        lastVent = ventGroup.getVents().get(ventIndex);
        asImpostor.setLastVent(lastVent);

        player.teleport(lastVent.getLocation().clone().add(new Vector(0.5D, 0D, 0.5D)));

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.NextVent")));
    }

    public void exitVents(ASImpostor asImpostor) {
        Player player = asImpostor.getPlayer();

        asImpostor.setVents(false);

        // Remove invisibility
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.getInventory().clear();

        // Teleport player to last location
        Location lastLocation = asImpostor.getLastLocation();
        player.teleport(lastLocation);

        // Give armor
        asImpostor.getArmor().armor(player, asImpostor.getColor());

        // Give items
        asImpostor.getImpostorItems().impostor(arena);

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.ExitVents")));
    }

    public void previousVent(ASImpostor asImpostor) {
        Player player = asImpostor.getPlayer();

        Arena arena = arenaManager.getArena(asImpostor);
        Vent lastVent = asImpostor.getLastVent();
        VentGroup ventGroup = arena.getVentGroup(lastVent.getLocation());

        int ventIndex = asImpostor.getVentIndex();
        if (!ventGroup.isRandom()) {
            if (ventIndex - 1 >= 0) {
                asImpostor.setVentIndex(ventIndex - 1);
            }
        }
        else {
            int random = NumUtil.random(0, ventGroup.getVents().size());
            while (random == ventIndex) {
                random = NumUtil.random(0, ventGroup.getVents().size());
            }

            asImpostor.setVentIndex(random);
        }

        asImpostor.setVentGroupIndex(new ArrayList<>(arena.getVentGroups()).indexOf(ventGroup));

        ventIndex = asImpostor.getVentIndex();
        lastVent = ventGroup.getVents().get(ventIndex);
        asImpostor.setLastVent(lastVent);

        player.teleport(lastVent.getLocation().clone().add(new Vector(0.5D, 0D, 0.5D)));

        // Play sound
        SoundUtil.playSound(player, Sound.valueOf(config.get(null, "Sounds.PreviousVent")));
    }

    private void playOpenAnimation(Block block) {
        BlockData blockData = block.getBlockData();
        TrapDoor trapDoor = (TrapDoor) blockData;
        trapDoor.setOpen(true);
    }

    private void playCloseAnimation(Block block) {
        BlockData blockData = block.getBlockData();
        TrapDoor trapDoor = (TrapDoor) blockData;
        trapDoor.setOpen(false);
    }

}

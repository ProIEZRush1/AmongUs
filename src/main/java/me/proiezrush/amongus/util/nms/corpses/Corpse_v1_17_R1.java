package me.proiezrush.amongus.util.nms.corpses;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.arena.player.ASPlayer;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Corpse_v1_17_R1 implements ICorpse {

    private final AmongUs plugin;

    private final List<Pair<EnumItemSlot, ItemStack>> armorContents;
    private final ASPlayer as;

    private EntityPlayer corpse;

    public Corpse_v1_17_R1(AmongUs plugin, ASPlayer as) {
        this.plugin = plugin;

        this.armorContents = new ArrayList<>();
        this.as = as;
    }

    public void spawn(Set<ASPlayer> players) {
        Player player = as.getPlayer();
        EntityPlayer craftPlayer = ((CraftPlayer) player).getHandle();

        Property textures = (Property) craftPlayer.getProfile().getProperties().get("textures").toArray()[0];
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        gameProfile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));

        this.corpse = new EntityPlayer(
                ((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) player.getWorld()).getHandle(),
                gameProfile
        );
        Location location = player.getLocation();
        corpse.setPosition(location.getX(), location.getY(), location.getZ());

        Location bed = location.add(1, 0, 0);
        corpse.e(new BlockPosition(bed.getX(), bed.getY(), bed.getZ()));

        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), player.getName());
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.b);

        PacketPlayOutScoreboardTeam score = PacketPlayOutScoreboardTeam.a(team);
        PacketPlayOutScoreboardTeam score1 = PacketPlayOutScoreboardTeam.a(team, true);
        PacketPlayOutScoreboardTeam score2 = PacketPlayOutScoreboardTeam.a(team, corpse.getName(), PacketPlayOutScoreboardTeam.a.a);

        corpse.setPose(EntityPose.d);

        DataWatcher watcher = corpse.getDataWatcher();
        byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
        watcher.set(DataWatcherRegistry.a.a(17), b);

        PacketPlayOutEntity.PacketPlayOutRelEntityMove move = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                corpse.getId(), (byte) 0, (byte) ((location.getY() - 1.7 - location.getY()) * 32), (byte) 0, false);

        setArmorContents(player);

        for (ASPlayer as : players) {
            PlayerConnection connection = ((CraftPlayer) as.getPlayer()).getHandle().b;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, corpse));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(corpse));
            connection.sendPacket(score);
            connection.sendPacket(score1);
            connection.sendPacket(score2);
            connection.sendPacket(new PacketPlayOutEntityMetadata(corpse.getId(), watcher, true));
            connection.sendPacket(move);
            for (Pair<EnumItemSlot, ItemStack> pair : armorContents) {
                connection.sendPacket(new PacketPlayOutEntityEquipment(corpse.getId(), Collections.singletonList(pair)));
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, corpse));
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    public void remove(Set<ASPlayer> players) {
        for (ASPlayer as : players) {
            Player p = as.getPlayer();

            PlayerConnection connection = ((CraftPlayer) p).getHandle().b;
            connection.sendPacket(new PacketPlayOutEntityDestroy(corpse.getId()));
        }
    }

    @Override
    public int getId() {
        return corpse.getId();
    }

    @Override
    public ASPlayer getASPlayer() {
        return as;
    }

    // Util methods
    private void setArmorContents(Player player) {
        ItemStack helmet = CraftItemStack.asNMSCopy(player.getInventory().getHelmet());
        ItemStack chestplate = CraftItemStack.asNMSCopy(player.getInventory().getChestplate());
        ItemStack leggings = CraftItemStack.asNMSCopy(player.getInventory().getLeggings());
        ItemStack boots = CraftItemStack.asNMSCopy(player.getInventory().getBoots());

        Pair<EnumItemSlot, ItemStack> pair = new Pair<>(EnumItemSlot.f, helmet);
        Pair<EnumItemSlot, ItemStack> pair2 = new Pair<>(EnumItemSlot.e, chestplate);
        Pair<EnumItemSlot, ItemStack> pair3 = new Pair<>(EnumItemSlot.d, leggings);
        Pair<EnumItemSlot, ItemStack> pair4 = new Pair<>(EnumItemSlot.c, boots);

        armorContents.add(pair);
        armorContents.add(pair2);
        armorContents.add(pair3);
        armorContents.add(pair4);
    }

}

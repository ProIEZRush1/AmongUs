package me.proiezrush.amongus.util.nms.reader;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.proiezrush.amongus.AmongUs;
import me.proiezrush.amongus.game.Arena;
import me.proiezrush.amongus.game.arena.player.AS;
import me.proiezrush.amongus.game.manager.ArenaManager;
import me.proiezrush.amongus.util.nms.corpses.ICorpse;
import me.proiezrush.amongus.util.nms.corpses.events.CorpseClickEvent;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketReader_v1_17_R1 implements PacketReader {

    Channel channel;
    public static Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public void inject(AmongUs plugin, AS as) {
        Player player = as.getPlayer();
        CraftPlayer craftPlayer = (CraftPlayer) player;
        channel = craftPlayer.getHandle().b.a.k;
        channels.put(player.getUniqueId(), channel);

        if (channel.pipeline().get("PacketInjector") != null) return;

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext channel, Packet<?> packet, List<Object> list) throws Exception {
                list.add(packet);
                readPacket(plugin, as, packet);
            }
        });
    }

    @Override
    public void uninject(Player player) {
        channel = channels.get(player.getUniqueId());
        if (channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
    }

    public void readPacket(AmongUs plugin, AS as, Packet<?> packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            int id = (int) getValue(packet, "a");

            ArenaManager arenaManager = plugin.getArenaManager();
            if (arenaManager.getArena(as) != null) {
                Arena arena = arenaManager.getArena(as);
                for (ICorpse corpse : arena.getCorpses()) {
                    if (corpse.getId() == id) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            Bukkit.getPluginManager().callEvent(new CorpseClickEvent(as.getPlayer(), corpse));
                        }, 0);
                    }
                }
            }
        }
    }

    private Object getValue(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

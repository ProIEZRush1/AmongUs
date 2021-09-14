package me.proiezrush.amongus.util.nms.reader;

import org.bukkit.Bukkit;

public class PacketReaderManager {

    public static PacketReader packetReader() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        switch (version) {
            case "v1_17_R1":
                return new PacketReader_v1_17_R1();
        }

        return null;
    }

}

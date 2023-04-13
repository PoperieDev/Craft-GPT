package com.poperie.craftgpt.playerData;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class playerDataMethods {
    static Map<String, playerDataMemory> playerDataMap = new HashMap<>();
    public static playerDataMemory getPlayerMemory(Player player) {
        if (!playerDataMap.containsKey(player.getUniqueId().toString())) {
            playerDataMemory m = new playerDataMemory();
            playerDataMap.put(player.getUniqueId().toString(), m);
            return m;
        }
        return playerDataMap.get(player.getUniqueId().toString());
    }

    public static void setPlayerMemory(Player player, playerDataMemory memory) {
        if (memory == null) {
            playerDataMap.remove(player.getUniqueId().toString());
            return;
        }
        playerDataMap.put(player.getUniqueId().toString(), memory);
    }

    public static String getPlayerMemoryFilePath(Player player) {
        return "plugins/CraftGPT/playerData/" + player.getUniqueId().toString() + ".yml";
    }
}

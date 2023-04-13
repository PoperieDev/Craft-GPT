package com.poperie.craftgpt.playerData;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class playerDataMethods {
    static Map<String, playerDataMemory> playerDataMap = new HashMap<>();
    public static playerDataMemory getPlayerMemory(Player player) {
        String playerUUID = player.getUniqueId().toString();
        if (playerDataMap.containsKey(playerUUID)) {
            return playerDataMap.get(playerUUID);
        }
        playerDataMemory memory = new playerDataMemory();
        File file = new File(getPlayerMemoryFilePath(player));
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            memory.setTokens(config.getInt("tokens"));
        } else {
            memory.setTokens(0);
        }
        return memory;
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

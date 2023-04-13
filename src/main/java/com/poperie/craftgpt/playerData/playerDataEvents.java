package com.poperie.craftgpt.playerData;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class playerDataEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerDataMethods.setPlayerMemory(event.getPlayer(), playerDataMethods.getPlayerMemory(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerJoinEvent event) {
        playerDataMemory memory = playerDataMethods.getPlayerMemory(event.getPlayer());
        File file = new File(playerDataMethods.getPlayerMemoryFilePath(event.getPlayer()));
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("tokens", memory.getTokens());
        playerDataMethods.setPlayerMemory(event.getPlayer(), null);
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.poperie.craftgpt.playerData;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;

import static com.poperie.craftgpt.configLoader.getFreeTokens;
import static com.poperie.craftgpt.playerData.playerDataMethods.getPlayerMemoryFilePath;

public class playerDataEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        playerDataMemory memory = new playerDataMemory();
        File file = new File(getPlayerMemoryFilePath(event.getPlayer()));

        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            memory.setTokens(config.getInt("tokens"));
        } else {
            memory.setTokens(getFreeTokens());
        }
        playerDataMethods.setPlayerMemory(event.getPlayer(), memory);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerDataMemory memory = playerDataMethods.getPlayerMemory(event.getPlayer());
        File file = new File(getPlayerMemoryFilePath(event.getPlayer()));
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("tokens", memory.getTokens());
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playerDataMethods.setPlayerMemory(event.getPlayer(), null);
    }
}

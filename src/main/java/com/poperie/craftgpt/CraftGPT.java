package com.poperie.craftgpt;

import com.poperie.craftgpt.events.chatEvent;
import com.poperie.craftgpt.playerData.playerDataEvents;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CraftGPT extends JavaPlugin {
    public static String key;
    @Override
    public void onEnable() {

        // Commands
        this.getCommand("tokens").setExecutor(new tokensAdminCommand());
        this.getCommand("buy").setExecutor(new buyCommand());

        // Events
        getServer().getPluginManager().registerEvents(new chatEvent(), this);
        getServer().getPluginManager().registerEvents(new playerDataEvents(), this);
        // Get the secret key from the key.yml file
        File file = new File(this.getDataFolder() + "key.yml");
        if (!file.exists()) {
            this.saveResource("key.yml", false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "key.yml"));
        key = config.getString("key");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

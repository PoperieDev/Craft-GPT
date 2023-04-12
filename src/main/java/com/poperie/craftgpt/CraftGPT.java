package com.poperie.craftgpt;

import com.poperie.craftgpt.events.chatEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CraftGPT extends JavaPlugin {
    public static String key;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new chatEvent(), this);
        // Get the secret key from the key.yml file
        this.saveResource("key.yml", true);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "key.yml"));
        key = config.getString("key");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

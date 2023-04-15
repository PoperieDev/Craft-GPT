package com.poperie.craftgpt;

import com.poperie.craftgpt.commands.askCommand;
import com.poperie.craftgpt.commands.buyCommand;
import com.poperie.craftgpt.commands.tokensAdminCommand;
import com.poperie.craftgpt.playerData.playerDataEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static com.poperie.craftgpt.configLoader.getScoreboard;
import static com.poperie.craftgpt.configLoader.loadConfig;
import static com.poperie.craftgpt.repeatedTasks.runRepeatedTasks;
import static com.poperie.craftgpt.scoreboard.createScoreboard;

public final class CraftGPT extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        // Commands
        this.getCommand("tokens").setExecutor(new tokensAdminCommand());
        this.getCommand("buy").setExecutor(new buyCommand());
        this.getCommand("ask").setExecutor(new askCommand());

        // Events
        getServer().getPluginManager().registerEvents(new playerDataEvents(), this);
        getServer().getPluginManager().registerEvents(this, this);
        // Get the secret key from the key.yml file
        File file = new File(this.getDataFolder() + "key.yml");
        if (!file.exists()) {
            this.saveResource("key.yml", false);
        }
        File configFile = new File(this.getDataFolder() + "config.yml");
        if (!configFile.exists()) {
            this.saveResource("config.yml", false);
        }
        loadConfig();
        if (getScoreboard()) {
            runRepeatedTasks();
        }
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getScheduler().runTaskLater(this, () -> createScoreboard(player), 20);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Run the method after 1 second
        Bukkit.getScheduler().runTaskLater(this, () -> createScoreboard(event.getPlayer()), 20);
    }
}

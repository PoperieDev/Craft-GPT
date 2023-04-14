package com.poperie.craftgpt;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.poperie.craftgpt.scoreboard.createScoreboard;

public class repeatedTasks {
    public static void runRepeatedTasks() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("CraftGPT"), new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    createScoreboard(player);
                }
            }
        }, 20, 60);
    }
}

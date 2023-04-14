package com.poperie.craftgpt;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class configLoader {
    private static boolean scoreboard;
    private static boolean buy;
    private static boolean broadcastAnswers;
    private static int freeTokens;

    public static boolean getScoreboard() {
        return scoreboard;
    }
    public static boolean getBuy() {
        return buy;
    }
    public static boolean getBroadcastAnswers() {
        return broadcastAnswers;
    }
    public static int getFreeTokens() {
        return freeTokens;
    }

    public static void loadConfig() {
        File configFile = new File("plugins/CraftGPT/config.yml");
        if (!configFile.exists()) {
            throw new RuntimeException("Config file not found! Please reload the plugin to fix this!");
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        scoreboard = config.getBoolean("config.scoreboard");
        buy = config.getBoolean("config.buy");
        broadcastAnswers = config.getBoolean("config.broadcastAnswers");
        freeTokens = config.getInt("config.freeTokens");
    }
}

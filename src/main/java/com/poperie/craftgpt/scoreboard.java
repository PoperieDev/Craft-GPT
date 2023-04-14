package com.poperie.craftgpt;

import com.poperie.craftgpt.playerData.playerDataMemory;
import com.poperie.craftgpt.playerData.playerDataMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import static com.poperie.craftgpt.configLoader.getScoreboard;

public class scoreboard {
    static int score;

    public static void createScoreboard(Player player) {

        if (!(getScoreboard())) {
            return;
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective(ChatColor.translateAlternateColorCodes('&', "&a&lCRAFT-GPT"), "");
        playerDataMemory memory = playerDataMethods.getPlayerMemory(player);

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        score = 15;

        setScoreBoardLine(board, " ");

        setScoreBoardLine(board, "&b&lSPILLER &f&lINFO");
        setScoreBoardLine(board, "&8- &fTokens: &b" + memory.getTokens());

        setScoreBoardLine(board, "&f");

        setScoreBoardLine(board, "&b&lSERVER &f&lINFO");
        setScoreBoardLine(board, "&8- &fSpillere: &f(&b" + Bukkit.getOnlinePlayers().size() + "&f/&b" + Bukkit.getMaxPlayers()+"&f)");
        setScoreBoardLine(board, "&8- &fVerden: &b" + player.getWorld().getName());
        setScoreBoardLine(board, "&8- §aGratis &b&l50 &aTokens");
        setScoreBoardLine(board, "&f&f");

        player.setScoreboard(board);
    }

    public static void setScoreBoardLine(Scoreboard board, String line) {
        Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
        objective.getScore(ChatColor.translateAlternateColorCodes('&', line)).setScore(score);
        score--;
    }
}

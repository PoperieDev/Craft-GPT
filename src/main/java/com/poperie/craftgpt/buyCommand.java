package com.poperie.craftgpt;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class buyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("§f ");
            player.sendMessage("§8[ §a§lKØB TOKENS §8]");
            player.sendMessage("§f  " + "§a1 EM §b= §a5 tokens");
            player.sendMessage("§f  " + "§aMinimum køb for: §b50 EMs");
            player.sendMessage("§f  " + "§aBrug: §b/buy <mængde tokens>");
            player.sendMessage("§f ");
            player.sendMessage("§8[ §a§lINFO §8]");
            player.sendMessage("§f  " + "§aTokens bliver brugt som betaling til API'en");
            player.sendMessage("§f  " + "§a50 Tokens giver dig et svar på ca. 300-500 Ord");
            player.sendMessage("§f  " + "§aDu mister dine tokens efter du har fået svar, ingen refund");
            player.sendMessage("§f ");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (Exception e) {
            player.sendMessage("§cUgyldig mængde");
            return true;
        }
        if (amount < 250) {
            player.sendMessage("§cMinimum køb er 250 tokens §7(Værdi i ems: 50 EMs)");
            return true;
        }
        if (amount % 5 != 0) {
            player.sendMessage("§cDu kan kun købe i mængder af 5 tokens");
            return true;
        }
        int ems = amount / 5;
        player.sendMessage("§f ");
        player.sendMessage("§8[ §a§lKØB TOKENS §8]");
        player.sendMessage("§a  En staff vil skrive til dig hurtigst muligt");
        player.sendMessage("§f ");
        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            if (loopPlayer.hasPermission("craftgpt.admin")) {
                loopPlayer.sendMessage("§f ");
                loopPlayer.sendMessage("§8[ §a§lKØB §8]");
                loopPlayer.sendMessage("§b  " + player.getName() + " §aVil gerne købe §b" + amount + " tokens §afor §b" + ems + " EMsa§a!");
                loopPlayer.sendMessage("§f ");
            }
        }

        return true;
    }
}

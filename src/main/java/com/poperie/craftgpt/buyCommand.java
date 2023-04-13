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
            player.sendMessage("§aKøb tokens:");
            player.sendMessage("§b5 Token = §a1 EM");
            player.sendMessage("§bMinimum køb for: §a50 EMs");
            player.sendMessage("§bBrug: /buy <mængde tokens>");
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
        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            if (loopPlayer.hasPermission("craftgpt.admin")) {
                loopPlayer.sendMessage("§f ");
                loopPlayer.sendMessage("§a§lKØB");
                loopPlayer.sendMessage("§a" + player.getName() + " §bVil gerne købe §a" + amount + " tokens §bfor §a" + ems + " EMs§b!");
                loopPlayer.sendMessage("§f ");
            }
        }

        return true;
    }
}

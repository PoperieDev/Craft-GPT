package com.poperie.craftgpt;

import com.poperie.craftgpt.playerData.playerDataMemory;
import com.poperie.craftgpt.playerData.playerDataMethods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tokensAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("craftgpt.admin")) {
            playerDataMemory memory = playerDataMethods.getPlayerMemory(player);
            player.sendMessage("§aDu har: §b" + memory.getTokens() + " tokens§a!");
            player.sendMessage("§aDu kan købe tokens via §b/buy");
            return true;
        }
        if (args.length < 3) {
            player.sendMessage("Usage: /tokens <player> <amount> <add|remove|set> [<broadcast? yes>]");
            return true;
        }
        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("Player not found");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage("Invalid amount");
            return true;
        }
        playerDataMemory memory = playerDataMethods.getPlayerMemory(target);
        switch (args[2]) {
            case "add":
                memory.setTokens(memory.getTokens() + amount);
                break;
            case "remove":
                if (memory.getTokens() < amount) {
                    player.sendMessage("§cPlayer doesn't have enough tokens §7(" + memory.getTokens() + "§7)");
                    return true;
                }
                memory.setTokens(memory.getTokens() - amount);
                break;
            case "set":
                memory.setTokens(amount);
                break;
        }
        if (args[3] != null && args[3].equals("yes")) {
            player.getServer().broadcastMessage("§f ");
            player.getServer().broadcastMessage("§a§lKØB");
            player.getServer().broadcastMessage("§a" + player.getName() + " §bHar købt §a" + amount + " Tokens!");
            player.getServer().broadcastMessage("§f ");
        }
        return true;
    }
}

package com.poperie.craftgpt;

import com.poperie.craftgpt.playerData.playerDataMemory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.poperie.craftgpt.playerData.playerDataMethods.getPlayerMemory;
import static com.poperie.craftgpt.scoreboard.createScoreboard;

public class tokensAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("craftgpt.admin")) {
            playerDataMemory memory = getPlayerMemory(player);
            player.sendMessage("§aDu har: §b" + memory.getTokens() + " tokens§a!");
            player.sendMessage("§aDu kan købe tokens via §b/buy");
            createScoreboard(player);
            return true;
        }
        if (args.length < 3) {
            player.sendMessage("§cUsage: /tokens <player> <amount> <add|remove|set> [<broadcast? yes>]");
            return true;
        }
        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cPlayer not found");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage("§cInvalid amount");
            return true;
        }
        playerDataMemory memory = getPlayerMemory(target);
        switch (args[2]) {
            case "add":
                memory.addTokens(amount);
                player.sendMessage("§aAdded §b" + amount + " tokens §ato §b" + target.getName() + "§a! §7(" + memory.getTokens() + "§7)");
                break;
            case "remove":
                if (memory.getTokens() < amount) {
                    player.sendMessage("§cPlayer doesn't have enough tokens §7(" + memory.getTokens() + "§7)");
                    return true;
                }
                memory.removeTokens(amount);
                player.sendMessage("§aRemoved §b" + amount + " tokens §afrom §b" + target.getName() + "§a! §7(" + memory.getTokens() + "§7)");
                break;
            case "set":
                memory.setTokens(amount);
                player.sendMessage("§aSet §b" + target.getName() + "'s §atokens to §b" + amount + "§a! §7(" + memory.getTokens() + "§7)");
                break;
        }
        createScoreboard(target);
        createScoreboard(target);
        try {
            if (args[3] != null && args[3].equals("yes")) {
                player.getServer().broadcastMessage("§f ");
                player.getServer().broadcastMessage("§8[ §a§lKØB §8]");
                player.getServer().broadcastMessage("§b  " + player.getName() + " §aHar købt §b" + amount + " Tokens!");
                player.getServer().broadcastMessage("§f ");
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }
}

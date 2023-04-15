package com.poperie.craftgpt.commands;

import com.poperie.craftgpt.CraftGPT;
import com.poperie.craftgpt.playerData.playerDataMemory;
import com.poperie.craftgpt.playerData.playerDataMethods;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.poperie.craftgpt.configLoader.getBroadcastAnswers;

public class askCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage("§cBrug: /ask <token amount> <question>");
            return true;
        }
        int tokens;
        try {
            tokens = Integer.parseInt(args[0]);
        } catch (Exception e) {
            player.sendMessage("§cBrug: /ask <token amount> <question>");
            return true;
        }
        if (tokens < 1) {
            player.sendMessage("§cDu skal bruge mindst 1 token.");
            return true;
        }
        playerDataMemory memory = playerDataMethods.getPlayerMemory(player);
        if (memory.getTokens() < tokens) {
            player.sendMessage("§cDu har ikke nok tokens.");
            return true;
        }
        memory.setTokens(memory.getTokens() - tokens);
        // Get all arguments past the 1st index
        String question = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        player.sendMessage("§7Generating response...");
        int finalTokens = tokens;
        String finalQuestion = question;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/CraftGPT/key.yml"));
        String key = config.getString("key");

        Bukkit.getScheduler().runTaskAsynchronously(CraftGPT.getPlugin(CraftGPT.class), new BukkitRunnable() {
            @Override
            public void run() {

                player.sendMessage("§7Tokens: §f" + memory.getTokens() + " §7(§f-" + finalTokens + "§7)");

                ChatMessage system = new ChatMessage("system", "Du er en hjælpsom Minecraft Pro-spiller, og du er altid klar til at besvare spørgsmål om Minecraft. Du er en af de bedste Minecraft spiller i verden.");
                ChatMessage user = new ChatMessage("user", finalQuestion);
                List<ChatMessage> messages = Arrays.asList(system, user);

                OpenAiService service = new OpenAiService(key);
                ChatCompletionRequest request = ChatCompletionRequest.builder()
                        .messages(messages)
                        .maxTokens(finalTokens)
                        .model("gpt-3.5-turbo")
                        .temperature(0.9)
                        .topP(1.0)
                        .frequencyPenalty(0.0)
                        .presencePenalty(0.6)
                        .build();
                List<ChatCompletionChoice> choices = service.createChatCompletion(request).getChoices();
                String response = choices.get(0).getMessage().getContent();

                if (getBroadcastAnswers()) {
                    for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                        loopPlayer.sendMessage("§f ");
                        loopPlayer.sendMessage("§a" + player.getName() + "'s Spørgsmål: §b" + finalQuestion);
                        loopPlayer.sendMessage("§aAI Svar: §b" + response);
                        if (Objects.equals(choices.get(0).getFinishReason(), "length")) {
                            loopPlayer.sendMessage("§7Stoppede fordi " + player.getName() + " §7løb tør for tokens");
                        }
                        loopPlayer.sendMessage("§f ");
                    }
                } else {
                    player.sendMessage("§f ");
                    player.sendMessage("§a" + player.getName() + "'s Spørgsmål: §b" + finalQuestion);
                    player.sendMessage("§aAI Svar: §b" + response);
                    if (Objects.equals(choices.get(0).getFinishReason(), "length")) {
                        player.sendMessage("§7Stoppede fordi du løb tør for tokens");
                    }
                    player.sendMessage("§f ");
                }
                System.out.println(response);

                if (Objects.equals(choices.get(0).getFinishReason(), "content_filter")) {
                    player.sendMessage("§cResponse filtered by OpenAI!");
                }
            }
        });
        return true;
    }
}
package com.poperie.craftgpt.events;

import com.poperie.craftgpt.CraftGPT;
import com.poperie.craftgpt.playerData.playerDataMemory;
import com.poperie.craftgpt.playerData.playerDataMethods;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.poperie.craftgpt.configLoader.getBroadcastAnswers;

public class chatEvent implements Listener {
    @EventHandler
    public void onChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        String messageCheck = event.getMessage();
        if (!messageCheck.startsWith("!")) {
            return;
        }
        Player player = event.getPlayer();
        playerDataMemory memory = playerDataMethods.getPlayerMemory(player);

        event.setCancelled(true);

        int tokens;
        if (messageCheck.contains("--t")) {
            // Get the letter after --t
            try {
                String tokenString = messageCheck.substring(messageCheck.indexOf("--t") + 4);
                // Convert the letter to an integer
                tokens = Integer.parseInt(tokenString);
            } catch (Exception e) {
                player.sendMessage("§cDu mangler at skrive den mængde tokens du vil bruge: §f--t <tal>");
                return;
            }
        } else {
            player.sendMessage("§cDu skal skrive: §f--t <tal> §cefter dit spørgsmål, for at bestemme hvor mange tokens du vil bruge.");
            return;
        }

        if (tokens < 1) {
            player.sendMessage("§cDu skal bruge mindst 1 token.");
            return;
        }

        if (memory.getTokens() < tokens) {
            player.sendMessage("§cDu har ikke nok tokens. §7(Du har " + memory.getTokens() + " tokens)");
            return;
        }

        String message = messageCheck.substring(1);
        // Remove the --t <number> from the message
        message = message.replace("--t " + tokens, "");

        // Create an async task to run the OpenAI API
        player.sendMessage("§7Generating response...");
        int finalTokens = tokens;
        String finalMessage = message;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/CraftGPT/key.yml"));
        String key = config.getString("key");
        memory.setTokens(memory.getTokens() - finalTokens);
        Bukkit.getScheduler().runTaskAsynchronously(CraftGPT.getPlugin(CraftGPT.class), new BukkitRunnable() {
            @Override
            public void run() {

            player.sendMessage("§7Tokens: §f" + memory.getTokens() + " §7(§f-" + finalTokens + "§7)");

            ChatMessage system = new ChatMessage("system", "Du er en hjælpsom Minecraft Pro-spiller, og du er altid klar til at besvare spørgsmål om Minecraft. Du er en af de bedste Minecraft spiller i verden.");
            ChatMessage user = new ChatMessage("user", finalMessage);
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
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage("§f ");
                    player.sendMessage("§a" + event.getPlayer().getName() + "'s Spørgsmål: §b" + finalMessage);
                    player.sendMessage("§aAI Svar: §b" + response);
                    if (Objects.equals(choices.get(0).getFinishReason(), "length")) {
                        player.sendMessage("§7Stoppede fordi " + event.getPlayer() + " §7løb tør for tokens");
                    }
                    player.sendMessage("§f ");
                }
            } else {
                player.sendMessage("§f ");
                player.sendMessage("§a" + event.getPlayer().getName() + "'s Spørgsmål: §b" + finalMessage);
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
    }
}

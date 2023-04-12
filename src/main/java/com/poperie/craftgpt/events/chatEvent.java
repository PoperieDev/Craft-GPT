package com.poperie.craftgpt.events;

import com.poperie.craftgpt.CraftGPT;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class chatEvent implements Listener {
    @EventHandler
    public void onChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        String messageCheck = event.getMessage();
        if (!messageCheck.startsWith("!")) {
            return;
        }
        Player player = event.getPlayer();

        event.setCancelled(true);

        int tokens;
        if (messageCheck.contains("--t")) {
            // Get the letter after --t
            String tokenString = messageCheck.substring(messageCheck.indexOf("--t") + 4);
            // Convert the letter to an integer
            tokens = Integer.parseInt(tokenString);
        } else {
            player.sendMessage("§cDu skal skrive: §f--t <tal> §cefter dit spørgsmål, for at bestemme hvor mange tokens du vil bruge.");
            return;
        }
        String message = messageCheck.substring(1);
        // Remove the --t <number> from the message
        message = message.replace("--t " + tokens, "");

        // Create an async task to run the OpenAI API
        player.sendMessage("§7Generating response...");
        int finalTokens = tokens;
        String finalMessage = message;
        Bukkit.getScheduler().runTaskAsynchronously(CraftGPT.getPlugin(CraftGPT.class), new BukkitRunnable() {
            @Override
            public void run() {

                ChatMessage system = new ChatMessage("system", "Du er en hjælpsom Minecraft Pro-spiller, og du er altid klar til at besvare spørgsmål om Minecraft. Du er en af de bedste Minecraft spillere i verden.");
                ChatMessage user = new ChatMessage("user", finalMessage);
                List<ChatMessage> messages = Arrays.asList(system, user);

                OpenAiService service = new OpenAiService(CraftGPT.key);
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

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage("§f ");
                    player.sendMessage("§a" + player.getName() + "'s Spørgsmål: §b" + finalMessage);
                    player.sendMessage("§f ");
                    player.sendMessage("§aAI Svar: §b" + response);
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

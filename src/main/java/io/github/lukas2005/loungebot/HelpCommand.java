package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.MessageSet;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

import java.util.concurrent.CompletableFuture;

public class HelpCommand implements Command {
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			DiscordApi api = e.getApi();

			String serverPrefix = Main.serverPrefixes.getOrDefault(server, Main.defaultPrefix);

			if ((Main.checkForCommand(messageContent, "help", server, api))) {


				CompletableFuture<Message> messageFuture = textChannel.sendMessage(e.getMessage().getAuthor().asUser().get().getMentionTag()+" Check your **DMs**! :mailbox:");
				messageFuture.thenAcceptAsync(message -> {
					try {
						Thread.sleep(5000);
						message.delete();
					} catch (InterruptedException ignored) {
					}
				});

				EmbedBuilder embed = new EmbedBuilder();

				embed.setDescription("Help");
				embed.addField("Current prefix for this server:", serverPrefix);
				embed.addField("Settings:", serverPrefix+"prefix [prefix] // Set the server's prefix");
				embed.addField("Moderation:", serverPrefix+"purge [number] // Remove set amount of messages");

				e.getMessage().getAuthor().asUser().ifPresent(user -> user.sendMessage(embed));
			}
		}
	}
}

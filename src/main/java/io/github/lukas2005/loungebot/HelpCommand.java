package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

import java.util.concurrent.CompletableFuture;

public class HelpCommand implements Command {
	@Override
	public boolean onMessageCreate(MessageCreateEvent e) {
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
				embed.addField("Settings:",
						serverPrefix+"prefix [prefix] // Set the server's prefix\n" +
						""
				);
				embed.addField("Moderation:",
						serverPrefix+"purge [number] // Remove set amount of messages\n" +
						serverPrefix+"serverinfo // Gives info about the server\n" +
						serverPrefix+"userinfo [username] // Gives info about the specific user\n"
				);
				embed.addField("Fun:",
						serverPrefix+"rr add [role name] [amount of steps (how long will it take to get from one color to another one step = 500ms)] [r1] [g1] [b1] [r2] [g2] [b2] // Makes the specified role a rainbow role!\n" +
						serverPrefix+"rr remove [role name] // Changes the specified role back to a normal one\n" +
						serverPrefix+"memedna // prints a dna string made out of lenny faces\n" +
						serverPrefix+"meme // Gets a random meme from reddit"
				);

				e.getMessage().getAuthor().asUser().ifPresent(user -> user.sendMessage(embed));
				return true;
			}
		}
		return false;
	}
}

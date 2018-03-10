package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

public class PrefixCommand implements Command {
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			String[] messageContentSplit = messageContent.split(" ");
			DiscordApi api = e.getApi();

			if ((Main.checkForCommand(messageContent, "prefix", server, api))) {
				if (!messageContentSplit[messageContentSplit.length - 1].contains("prefix")) {

					if (Main.serverPrefixes.containsKey(server)) {
						Main.serverPrefixes.remove(server);
					}

					Main.serverPrefixes.put(server, messageContentSplit[messageContentSplit.length - 1]);
					textChannel.sendMessage("The prefix for this server is now: `" + messageContentSplit[messageContentSplit.length - 1] + "`");
				} else {
					if (!Main.serverPrefixes.containsKey(server)) Main.serverPrefixes.put(server, Main.defaultPrefix);
					textChannel.sendMessage("The prefix for this server is: `" + Main.serverPrefixes.get(server) +"`");

				}
			}
		}
	}
}

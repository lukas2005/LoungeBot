package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.MessageSet;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;
import org.javacord.util.logging.ExceptionLogger;

public class PurgeCommand implements Command {
	@Override
	public boolean onMessageCreate(MessageCreateEvent e) {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			String[] messageContentSplit = messageContent.split(" ");
			DiscordApi api = e.getApi();

			if ((Main.checkForCommand(messageContent, "purge", server, api)) && (e.getMessage().getAuthor().isBotOwner() || e.getMessage().getAuthor().canManageMessagesInTextChannel())) {
				int amount;
				try {
					amount = Integer.parseInt(messageContentSplit[messageContentSplit.length-1]);
				} catch (Exception e1) {
					return false;
				}
				textChannel.getMessages(amount).thenCompose(MessageSet::deleteAll).exceptionally(ExceptionLogger.get());
				return true;
			}
		}
		return false;
	}
}

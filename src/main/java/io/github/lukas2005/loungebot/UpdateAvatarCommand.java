package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

import java.net.MalformedURLException;
import java.net.URL;

public class UpdateAvatarCommand implements Command {
	@Override
	public boolean onMessageCreate(MessageCreateEvent e) throws Exception {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			String[] messageContentSplit = messageContent.split(" ");
			DiscordApi api = e.getApi();

			if ((Main.checkForCommand(messageContent, "avatar", server, api)) || (Main.checkForCommand(messageContent, "av", server, api))) {
				if (e.getMessage().getAuthor().isBotOwner() && e.getMessage().getMentionedUsers().size() == 0) {
					try {
						api.updateAvatar(new URL(messageContentSplit[messageContentSplit.length - 1]));
					} catch (MalformedURLException e1) {
						textChannel.sendMessage("Sorry but you provided a Malformed url");
					}
				}
				return true;
			}
		}
		return false;
	}
}

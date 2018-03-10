package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

public class ServerInfoCommand implements Command {
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			DiscordApi api = e.getApi();
			if (Main.checkForCommand(messageContent, "serverinfo", server, api)) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.addField("Members: ", String.valueOf(server.getMemberCount()));

				textChannel.sendMessage(builder);
			}
		}
	}
}

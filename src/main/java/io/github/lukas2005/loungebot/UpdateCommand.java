package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.ApplicationInfo;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

import java.io.IOException;

public class UpdateCommand implements Command {
	@Override
	public void onMessageCreate(MessageCreateEvent e) throws Exception {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			String[] messageContentSplit = messageContent.split(" ");
			DiscordApi api = e.getApi();

			String serverPrefix = Main.serverPrefixes.getOrDefault(server, Main.defaultPrefix);
			if (Main.checkForCommand(messageContent, "update", server, api) && e.getMessage().getAuthor().isBotOwner()) {
				new Thread(() -> {
					try {
						new ProcessBuilder("screen", "-S", "LoungeBot", "sh").start();
					} catch (IOException e1) {
					}
				}, "Update Thread").setDaemon(true);
				//Runtime.getRuntime().exit(0);
			}
		}
	}
}

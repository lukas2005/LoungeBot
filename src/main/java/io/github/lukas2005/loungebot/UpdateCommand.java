package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

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
				if (OsUtils.isWindows()) {
					//new ProcessBuilder("cmd", "/c", "echo", "test").inheritIO().start();
				} else {
					new ProcessBuilder("/bin/bash", "-c", "screen", "-dm", "../test.sh").inheritIO().start();
				}

				//System.exit(0);
			}
		}
	}
}

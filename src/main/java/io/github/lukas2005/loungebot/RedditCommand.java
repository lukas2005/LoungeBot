package io.github.lukas2005.loungebot;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

import java.util.HashMap;

public class RedditCommand implements Command {

	public static HashMap<ServerTextChannel, String> subredditWatchList = new HashMap<>();

	@Override
	public void onMessageCreate(MessageCreateEvent e) throws Exception {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			String[] messageContentSplit = messageContent.split(" ");
			DiscordApi api = e.getApi();

			String serverPrefix = Main.serverPrefixes.getOrDefault(server, Main.defaultPrefix);

			if ((Main.checkForCommand(messageContent, "youtube", server, api))) {
				if ((Main.checkForCommand(messageContent, "youtube add", server, api))) {
					subredditWatchList.put(textChannel, messageContentSplit[messageContentSplit.length-1]);
					new Thread(() -> {
						String subreddit = messageContentSplit[messageContentSplit.length-1];
						while (!Thread.currentThread().isInterrupted()) {
							try {
								Thread.sleep(10 * 60000);
							} catch (InterruptedException e1) {
								Thread.currentThread().interrupt();
							}
						}
					},"Subreddit " +messageContentSplit[messageContentSplit.length-1]+" Polling Thread");
				}
			}
		}
	}
}

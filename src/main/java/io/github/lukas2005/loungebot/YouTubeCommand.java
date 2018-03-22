package io.github.lukas2005.loungebot;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

import java.util.HashMap;

public class YouTubeCommand implements Command {

	public static final HashMap<ServerTextChannel, Channel> watchedChannels = new HashMap<>();
	public static YouTube youtube;

	public YouTubeCommand() {
		youtube = new YouTube.Builder(
				new NetHttpTransport(),
				new JacksonFactory(),
				request -> {
				}).setApplicationName("LoungeBot").setYouTubeRequestInitializer(new YouTubeRequestInitializer(Main.youtubeApiKey)).build();
	}

	@Override
	public boolean onMessageCreate(MessageCreateEvent e) throws Exception {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			String[] messageContentSplit = messageContent.split(" ");
			DiscordApi api = e.getApi();

			String serverPrefix = Main.serverPrefixes.getOrDefault(server, Main.defaultPrefix);

			if ((Main.checkForCommand(messageContent, "youtube", server, api))) {
				if ((Main.checkForCommand(messageContent, "youtube add", server, api))) {
					YouTube.Channels.List channels = youtube.channels().list("snippet, statistics");
					channels.setId(messageContentSplit[messageContentSplit.length-1]);

					ChannelListResponse channelResponse = channels.execute();

					Channel channel = channelResponse.getItems().get(0);
					watchedChannels.put(textChannel, channel);
				}
				return true;
			}
		}
		return false;
	}
}

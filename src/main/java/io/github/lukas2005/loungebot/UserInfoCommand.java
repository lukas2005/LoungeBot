package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.permission.Role;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.message.MessageCreateEvent;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class UserInfoCommand implements Command {
	@Override
	public boolean onMessageCreate(MessageCreateEvent e) {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			DiscordApi api = e.getApi();

			String serverPrefix = Main.serverPrefixes.getOrDefault(server, Main.defaultPrefix);

			if ((Main.checkForCommand(messageContent, "userinfo", server, api))) {
				EmbedBuilder embed = new EmbedBuilder();

				List<User> mentioned = new ArrayList<>(e.getMessage().getMentionedUsers());
				if (mentioned.size() > 1 && mentioned.contains(api.getYourself())) mentioned.remove(api.getYourself());
				User user = mentioned.get(0);

				embed.setDescription(user.getDiscriminatedName() + " " +user.getNickname(server).map(nick -> "aka "+nick).orElse(""));
				embed.setThumbnail(user.getAvatar());

				embed.addField("User id: ", user.getIdAsString());
				embed.addField("Joined: ", user.getJoinedAtTimestamp(server).get().atZone(ZoneId.of("GMT")).format(Main.dateTimeFormatter));
				embed.addField("Registered: ", user.getCreationTimestamp().atZone(ZoneId.of("GMT")).format(Main.dateTimeFormatter));
				StringBuilder roleString = new StringBuilder();
				for (Role r : user.getRoles(server)) {
					if (!r.isEveryoneRole()) {
						roleString.append(r.getMentionTag()).append(" ");
					}
				}
				embed.addField("Roles: ", roleString.toString());

				textChannel.sendMessage(embed);
				return true;
			}
		}
		return false;
	}
}

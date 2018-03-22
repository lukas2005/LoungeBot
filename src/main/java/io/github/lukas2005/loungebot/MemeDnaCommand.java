package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

public class MemeDnaCommand implements Command {

	public static final String memeDNA = "(   ͡° ͜ʖ ͡° )(   ͡° ͜ʖ ͡° )\n" +
			" (   ͡° ͜ʖ ͡° )  ͡° ͜ʖ ͡° )\n" +
			"  (   ͡° ͜ʖ ͡° )  ͜ʖ ͡° )\n" +
			"   (   ͡° ͜ʖ ͡° )  ͡° )\n" +
			"    (   ͡° ͜ʖ ͡° )° )\n" +
			"     (   ͡° ͜ʖ ͡° )\n" +
			"    ( (   ͡° ͜ʖ ͡° )\n" +
			"   (   (   ͡° ͜ʖ ͡° )\n" +
			"  (   ͡° (   ͡° ͜ʖ ͡° )\n" +
			" (   ͡° ͜ʖ (   ͡° ͜ʖ ͡° )\n" +
			"(   ͡° ͜ʖ ͡° (   ͡° ͜ʖ ͡° )\n" +
			"(   ͡° ͜ʖ ͡° )(   ͡° ͜ʖ ͡° )\n" +
			"(   ͡° ͜ʖ ͡° ) (   ͡° ͜ʖ ͡° )\n" +
			"(   ͡° ͜ʖ ͡° )(   ͡° ͜ʖ ͡° )\n" +
			" (   ͡° ͜ʖ ͡° )  ͡° ͜ʖ ͡° )\n" +
			"  (   ͡° ͜ʖ ͡° )  ͜ʖ ͡° )\n" +
			"   (   ͡° ͜ʖ ͡° )  ͡° )\n" +
			"    (   ͡° ͜ʖ ͡° )° )\n" +
			"     (   ͡° ͜ʖ ͡° ))\n" +
			"     (   ͡° ͜ʖ ͡° )\n" +
			"    ( (   ͡° ͜ʖ ͡° )\n" +
			"   (   (   ͡° ͜ʖ ͡° )\n" +
			"  (   ͡° (   ͡° ͜ʖ ͡° )\n" +
			" (   ͡° ͜ʖ (   ͡° ͜ʖ ͡° )\n" +
			"(   ͡° ͜ʖ ͡° (   ͡° ͜ʖ ͡° )\n" +
			"(   ͡° ͜ʖ ͡° )(   ͡° ͜ʖ ͡° )\n" +
			"(   ͡° ͜ʖ ͡° ) (   ͡° ͜ʖ ͡° )\n";

	@Override
	public boolean onMessageCreate(MessageCreateEvent e) throws Exception {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			DiscordApi api = e.getApi();

			if ((Main.checkForCommand(messageContent, "memedna", server, api))) {
				textChannel.sendMessage(memeDNA);
				return true;
			}
		}
		return false;
	}
}

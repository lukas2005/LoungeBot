package io.github.lukas2005.loungebot.modules;

import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;
import org.javacord.listener.message.MessageCreateListener;

import java.util.ArrayList;

public class Module implements MessageCreateListener {

	ArrayList<Server> enabledServers = new ArrayList<>();
	ArrayList<Command> commands = new ArrayList<>();

	DiscordApi api;

	public Module(DiscordApi api) {
		this.api = api;
		api.addMessageCreateListener(this);
	}

	public void addCommand(Command command) {
		commands.add(command);
	}

	public void enableOnServer(Server server) {
		enabledServers.add(server);
	}

	public void disableOnServer(Server server) {
		enabledServers.remove(server);
	}

	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if (e.getServer().isPresent() && e.getMessage().getAuthor().isUser()) {
			Server server = e.getServer().get();
			//if (enabledServers.contains(server)) {
				for (Command command : commands) {
					try {
						if (command.onMessageCreate(e)) {
							break;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			//}
		}
	}
}

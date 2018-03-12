package io.github.lukas2005.loungebot.modules;

import org.javacord.event.message.MessageCreateEvent;

public interface Command {

	public void onMessageCreate(MessageCreateEvent e) throws Exception;

}

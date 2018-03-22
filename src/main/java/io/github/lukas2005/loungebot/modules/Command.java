package io.github.lukas2005.loungebot.modules;

import org.javacord.event.message.MessageCreateEvent;

public interface Command {

	boolean onMessageCreate(MessageCreateEvent e) throws Exception;

}

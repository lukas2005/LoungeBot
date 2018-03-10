package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Module;
import org.javacord.AccountType;
import org.javacord.DiscordApi;
import org.javacord.DiscordApiBuilder;
import org.javacord.entity.server.Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Main {

	public static DiscordApi api;

	public static String defaultPrefix = "!";

	public static HashMap<Server, String> serverPrefixes = new HashMap<>();

	public static Module settingsModule;
	public static Module toolsModule;
	public static Module moderationModule;

	public static void main(String...args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("discord_token.txt"));

		api = new DiscordApiBuilder().setAccountType(AccountType.BOT).setToken(reader.readLine()).login().join();
		api.addServerJoinListener(serverJoinEvent -> serverPrefixes.put(serverJoinEvent.getServer(), defaultPrefix));

		settingsModule = new Module(api);
		settingsModule.addCommand(new PrefixCommand());

		toolsModule = new Module(api);
		toolsModule.addCommand(new ServerInfoCommand());

		moderationModule = new Module(api);
	}

	public static boolean checkForCommand(String messageContents, String command, Server server, DiscordApi api) {
		return messageContents.contains(api.getYourself().getMentionTag()+" "+command) || messageContents.contains(serverPrefixes.get(server)+command) || messageContents.contains(Main.defaultPrefix+command);
	}

}

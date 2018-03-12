package io.github.lukas2005.loungebot;

import com.github.jreddit.oauth.RedditOAuthAgent;
import com.github.jreddit.oauth.RedditToken;
import com.github.jreddit.oauth.app.RedditScriptApp;
import com.github.jreddit.oauth.client.RedditHttpClient;
import io.github.lukas2005.loungebot.modules.Module;
import org.apache.http.impl.client.HttpClientBuilder;
import org.javacord.AccountType;
import org.javacord.DiscordApi;
import org.javacord.DiscordApiBuilder;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.server.Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

	public static DiscordApi api;

	public static String defaultPrefix = "lb.";

	public static String mentionTag;

	public static HashMap<Server, String> serverPrefixes = new HashMap<>();

	public static Module settingsModule;
	public static Module toolsModule;
	public static Module moderationModule;
	public static Module funModule;

	public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM uuuu hh:mm:ss", Locale.ENGLISH);

	public static List<EmbedBuilder> memes = Arrays.asList(
			new EmbedBuilder().setImage("https://cdn.discordapp.com/attachments/284834903563173889/422019708217196556/unknown.png"),
			new EmbedBuilder().setImage("https://quizizz.zendesk.com/hc/article_attachments/115002501069/1024x1024.jpg")

	);

	public static Random rand = new Random();

	public static BufferedReader secretsReader;

	public static String discordApiKey;
	public static String redditSecret;
	public static String youtubeApiKey;

	static RedditScriptApp redditApp;
	static RedditOAuthAgent redditAgent;
	static RedditHttpClient redditClient;
	static RedditToken redditToken;

	static final String userAgent = "LoungeBot";

	public static void main(String...args) throws Exception {
		secretsReader = new BufferedReader(new FileReader("secrets.txt"));

		discordApiKey = secretsReader.readLine();
		redditSecret = secretsReader.readLine();
		youtubeApiKey = secretsReader.readLine();

		api = new DiscordApiBuilder().setAccountType(AccountType.BOT).setToken(discordApiKey).login().join();
		api.addServerJoinListener(serverJoinEvent -> serverPrefixes.put(serverJoinEvent.getServer(), defaultPrefix));
		mentionTag = api.getYourself().getMentionTag();

		api.updateActivity(defaultPrefix+"help");

		redditApp = new RedditScriptApp("MzlfRrutvYNsRg", Main.redditSecret, "");
		redditAgent = new RedditOAuthAgent(userAgent, redditApp);
		redditClient = new RedditHttpClient(userAgent, HttpClientBuilder.create().build());

		redditToken = redditAgent.tokenAppOnly(true);

		settingsModule = new Module(api);
		settingsModule.addCommand(new PrefixCommand());
		settingsModule.addCommand(new HelpCommand());
		settingsModule.addCommand(new UpdateCommand());

		toolsModule = new Module(api);
		toolsModule.addCommand(new ServerInfoCommand());
		toolsModule.addCommand(new UserInfoCommand());
		toolsModule.addCommand(new YouTubeCommand());
		toolsModule.addCommand(new RedditCommand());

		moderationModule = new Module(api);
		moderationModule.addCommand(new PurgeCommand());

		funModule = new Module(api);
		funModule.addCommand(new MemeCommand());
		funModule.addCommand(new RainbowRolesCommand());
	}

	public static boolean checkForCommand(String messageContents, String command, Server server, DiscordApi api) {
		return ((serverPrefixes.containsKey(server) && messageContents.startsWith(serverPrefixes.get(server))) || messageContents.startsWith(Main.defaultPrefix)) && (messageContents.contains(mentionTag+" "+command) || messageContents.contains(command));
	}

}

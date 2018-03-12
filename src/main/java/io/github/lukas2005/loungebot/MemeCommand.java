package io.github.lukas2005.loungebot;

import com.github.jreddit.parser.entity.Submission;
import com.github.jreddit.parser.listing.SubmissionsListingParser;
import com.github.jreddit.request.retrieval.param.SubmissionSort;
import com.github.jreddit.request.retrieval.submissions.CustomRedditRequest;
import com.github.jreddit.request.retrieval.submissions.SubmissionsOfSubredditRequest;
import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

import java.util.Arrays;
import java.util.List;

public class MemeCommand implements Command {

	public static List<String> subreddits = Arrays.asList("memes");

	public static String redditBaseUrl = "https://api.reddit.com/r/%s/search?";
	public static String redditUrlParams = "count=10&restrict_sr=on&raw_json=true&sort=new&q=%s";

	public static List<String> memeTopics = Arrays.asList("cola", "trump", "memes", "doge");

	public MemeCommand() throws Exception {
	}

	@Override
	public void onMessageCreate(MessageCreateEvent e) throws Exception {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			String[] messageContentSplit = messageContent.split(" ");
			DiscordApi api = e.getApi();

			if ((Main.checkForCommand(messageContent, "meme", server, api))) {
//				URLConnection conn = new URL(String.format(redditBaseUrl+redditUrlParams,
//						subreddits.get(Main.rand.nextInt(subreddits.size())),
//						messageContentSplit[messageContentSplit.length-1].contains("meme") ?
//								memeTopics.get(Main.rand.nextInt(memeTopics.size())) :
//								messageContentSplit[messageContentSplit.length-1])).openConnection();
//				JSONObject obj = new JSONObject((String)conn.getContent());
//				JSONArray arr = obj.getJSONObject("data").getJSONArray("children");
//				textChannel.sendMessage(String.valueOf(arr.length()));
//				JSONObject redditPost = arr.getJSONObject(Main.rand.nextInt(arr.length()));

				String memeTopic = messageContentSplit[messageContentSplit.length-1].contains("meme") ?
						memeTopics.get(Main.rand.nextInt(memeTopics.size())) :
						messageContentSplit[messageContentSplit.length-1];

				SubmissionsOfSubredditRequest search = new CustomRedditRequest(memeTopic, subreddits.get(Main.rand.nextInt(subreddits.size())), SubmissionSort.NEW);
				search.setCount(100);
				search.setLimit(100);

				System.out.println("https://oauth.reddit.com" + search.generateRedditURI());

				SubmissionsListingParser parser = new SubmissionsListingParser();

				List<Submission> submissions = parser.parse(Main.redditClient.get(Main.redditToken, search))/*.stream().filter(submission -> submission.getTitle().contains(memeTopic)).collect(Collectors.toList())*/;

				Submission picked = submissions.get(Main.rand.nextInt(submissions.size()));

//				while (picked.getUpVotes() < 2000) {
//					picked = submissions.get(Main.rand.nextInt(submissions.size()));
//				}

				EmbedBuilder embed = new EmbedBuilder();

				embed.setImage(picked.getURL());
				embed.setTitle(picked.getTitle());
				//embed.setDescription();

				textChannel.sendMessage(embed);
			}
		}
	}
}

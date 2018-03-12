package io.github.lukas2005.loungebot;

import io.github.lukas2005.loungebot.modules.Command;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.permission.Role;
import org.javacord.entity.server.Server;
import org.javacord.event.message.MessageCreateEvent;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class RainbowRolesCommand implements Command {

	public static HashMap<Role, Color[]> rainbowRoles = new HashMap<>();
	public static HashMap<Role, Thread> rainbowRoleThreads = new HashMap<>();

	public static File rolesFile = new File("rainbowRoles.txt");

	public RainbowRolesCommand() throws FileNotFoundException {
		if (rolesFile.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(rolesFile));
			try {
				String line = reader.readLine();
				while (line != null) {

					String[] lineSplit = line.split(":");

					String roleId = lineSplit[0];
					String colors = lineSplit[1];

					String[] colorsSplit = colors.split(",");

					int amountOfSteps = Integer.parseInt(lineSplit[2]);

					Color[] colors1 = new Color[]{new Color(Integer.parseInt(colorsSplit[0])), new Color(Integer.parseInt(colorsSplit[1]))};

					Role role = Main.api.getRoleById(roleId).orElse(null);

					if (role != null) {
						rainbowRoles.put(role, colors1);
						startThread(role, colors1, amountOfSteps);
					}

					line = reader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onMessageCreate(MessageCreateEvent e) throws Exception {
		if (e.getServer().isPresent()) {
			Server server = e.getServer().get();
			ServerTextChannel textChannel = e.getMessage().getServerTextChannel().get();
			String messageContent = e.getMessage().getContent();
			String[] messageContentSplit = messageContent.split(" ");
			DiscordApi api = e.getApi();

			String serverPrefix = Main.serverPrefixes.getOrDefault(server, Main.defaultPrefix);

			if ((Main.checkForCommand(messageContent, "rr", server, api))) {
				if ((Main.checkForCommand(messageContent, "rr add", server, api))) {
					Role role = server.getRolesByName(messageContentSplit[messageContentSplit.length-8]).get(0);

					int amountOfSteps = Integer.parseInt(messageContentSplit[messageContentSplit.length-7]);

					int startR = Integer.parseInt(messageContentSplit[messageContentSplit.length-6]);
					int startG = Integer.parseInt(messageContentSplit[messageContentSplit.length-5]);
					int startB = Integer.parseInt(messageContentSplit[messageContentSplit.length-4]);

					int endR = Integer.parseInt(messageContentSplit[messageContentSplit.length-3]);
					int endG = Integer.parseInt(messageContentSplit[messageContentSplit.length-2]);
					int endB = Integer.parseInt(messageContentSplit[messageContentSplit.length-1]);

					Color[] colors = new Color[]{new Color(startR, startG, startB), new Color(endR, endG, endB)};

					rainbowRoles.put(role, colors);
					startThread(role, colors, amountOfSteps);
					saveRolesFile();
				}
				if ((Main.checkForCommand(messageContent, "rr remove", server, api))) {
					Role role = server.getRolesByName(messageContentSplit[messageContentSplit.length-1]).get(0);

					rainbowRoles.remove(role);
					rainbowRoleThreads.get(role).interrupt();
					rainbowRoleThreads.remove(role);
					saveRolesFile();
				}
			}
		}
	}

	public static void saveRolesFile() throws Exception {
		if (rolesFile.exists()) {
			rolesFile.delete();
			rolesFile.createNewFile();
		} else {
			rolesFile.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(rolesFile));
		for (Role r : rainbowRoles.keySet()) {
			Color[] colors1 = rainbowRoles.get(r);
			writer.append(r.getIdAsString())
					.append(":")
					.append(String.valueOf(colors1[0].getRGB()))
					.append(",")
					.append(String.valueOf(colors1[1].getRGB()));
			writer.newLine();
		}
		writer.close();
	}

	public static void startThread(Role role, Color[] colors, double amountOfSteps) throws IllegalArgumentException {
		if (colors.length != 2) throw new IllegalArgumentException("Colors array needs to be of length = 2");
		Color startColor = colors[0];
		Color endColor = colors[1];

		Thread t = new Thread(() -> {
			double diff = amountOfSteps;

			boolean sswitch = false;
			while (!Thread.currentThread().isInterrupted()) {
				double ratio = diff / amountOfSteps;
				try {
					int r = (int)Math.abs((ratio * startColor.getRed()) + ((1 - ratio) * endColor.getRed()));
					int g = (int)Math.abs((ratio * startColor.getGreen()) + ((1 - ratio) * endColor.getGreen()));
					int b = (int)Math.abs((ratio * startColor.getBlue()) + ((1 - ratio) * endColor.getBlue()));
					role.updateColor(new Color(r, g, b));

					if (diff > 0 && !sswitch) {
						diff--;
						if (diff == 0) {
							sswitch = true;
						}
					} else if (sswitch) {
						diff++;
						if (diff == amountOfSteps) {
							sswitch = false;
						}
					}

					Thread.sleep(500);
				} catch (Exception e1) {
					e1.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		}, "Rainbow Role Thread for Role: "+role.getName());

		t.start();

		rainbowRoleThreads.put(role, t);
	}
}

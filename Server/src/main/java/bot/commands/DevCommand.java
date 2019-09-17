package bot.commands;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by Corey on 15/06/2017.
 */
public class DevCommand implements CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(DevCommand.class);

    private final CommandHandler commandHandler;
    private final DiscordAPI api;

    public DevCommand(CommandHandler commandHandler, DiscordAPI api) {
        this.commandHandler = commandHandler;
        this.api = api;
    }

    @Command(aliases = { "::dev" }, description = "Dev command", usage = "::dev [args]", showInHelpPage = false)
    public String onDevCommand(String[] args, User user, Server server, Message command) {

        List<String> roles = user.getRoles(server).parallelStream().map(Role::getName).collect(Collectors.toList());

        if (roles.contains("Administrator") || roles.contains("Developer") || roles.contains("Bot Master")) {

            if (args.length < 1) {
                return "No args specified";
            }

            args[0] = args[0].toLowerCase();
            switch (args[0]) {
                case "ping":
                    long messageCreated = command.getCreationDate().getTimeInMillis();
                    long currentTime = System.currentTimeMillis();

                    long timeTaken = currentTime - messageCreated;

                    return ":ping_pong:Pong! " + timeTaken + "ms";
                case "servers":
                    if (args.length == 1 || args[1].equalsIgnoreCase("all")) {
                        StringBuilder bldr = new StringBuilder("Servers:\n");
                        for (Server currentServer : api.getServers()) {
                            bldr.append(currentServer.getName()).append(":").append(currentServer.getId()).append("\n");
                        }
                        return bldr.toString();
                    } else {
                        if (args[1].equalsIgnoreCase("this")) {
                            args[1] = server.getId();
                        }

                        String serverId = args[1].trim();
                        Server serverToLookup = api.getServerById(serverId);

                        StringBuilder bldr = new StringBuilder("```xml\n");
                        bldr.append("Server name: ").append(serverToLookup.getName()).append("\n");
                        bldr.append("Server id: ").append(serverToLookup.getId()).append("\n");

                        try {
                            bldr.append("Server owner: ").append(serverToLookup.getOwner().get().getName()).append("\n");
                        } catch (InterruptedException | ExecutionException ex) {
                            log.warn("Failed to lookup server in dev command.", ex);
                        }

                        bldr.append("Server owner id: ").append(serverToLookup.getOwnerId()).append("\n");
                        bldr.append("Server channels:\n");

                        for (Channel channel : serverToLookup.getChannels()) {
                            bldr.append("- ").append(channel.getName()).append(":").append(channel.getId()).append("\n");
                        }

                        if (args.length > 2 && args[2].equalsIgnoreCase("full")) {
                            bldr.append("Users:\n");
                            for (User currentUser : serverToLookup.getMembers()) {
                                bldr.append(currentUser.getName()).append(" - ").append(currentUser.getId()).append("\n");
                            }
                        }

                        bldr.append("```");
                        return bldr.toString();
                    }
                case "channels":
                    if (args.length == 1 || args[1].equalsIgnoreCase("all")) {
                        StringBuilder bldr = new StringBuilder("All Channels:\n");
                        for (Channel channel : api.getChannels()) {
                            bldr.append(channel.getName()).append(":").append(channel.getId()).append("\n");
                        }
                        return bldr.toString();
                    } else {
                        String channelId = args[1].trim();
                        Channel channel = api.getChannelById(channelId);
                        StringBuilder bldr = new StringBuilder("```xml\n");
                        bldr.append("Channel name: ").append(channel.getName()).append("\n");
                        bldr.append("Channel id: ").append(channel.getId()).append("\n");
                        bldr.append("Channel topic: ").append(channel.getTopic()).append("\n");
                        bldr.append("Channel server: ").append(channel.getServer().toString()).append("\n");
                        bldr.append("```");
                        return bldr.toString();
                    }
                case "message":
                    if (args.length < 3) {
                        return "No channel id defined, usage ::dev message channelid message_to_send";
                    }
                    if (args[1].equalsIgnoreCase("this")) {
                        args[1] = command.getChannelReceiver().getId();
                    }

                    String channelId = args[1];
                    Channel channel = api.getChannelById(channelId);
                    StringBuilder bldr = new StringBuilder();

                    for (int i = 2; i < args.length; i++) {
                        bldr.append(args[i]).append(" ");
                    }

                    channel.sendMessage(bldr.toString().trim());
                    return "Sent message to channel " + channelId;

                case "roles":
                    if (args.length < 3) {
                        return "Wrong number of arguments, usage ::dev roles userid serverid";
                    }

                    if (args[1].equalsIgnoreCase("me")) {
                        args[1] = user.getId();
                    }

                    if (args[2].equalsIgnoreCase("this")) {
                        args[2] = server.getId();
                    }

                    String userId = args[1];
                    String serverId = args[2];

                    Server currentServer = api.getServerById(serverId);
                    User currentUser = null;

                    try {
                        currentUser = api.getUserById(userId).get();
                    } catch (InterruptedException | ExecutionException ex) {
                        log.warn("Failed to get user: {}.", userId, ex);
                    }

                    StringBuilder sb = new StringBuilder(currentUser.getName() + "'s roles on " + serverId + "\n```xml\n");

                    for (Role role : currentUser.getRoles(currentServer)) {
                        sb.append(role.getName()).append("\n");
                    }

                    sb.append("\n```");

                    return sb.toString();
                case "disconnect":
                    api.disconnect();
                    return "Disconnecting, bye bye :wave:";
            }
        } else {
            return ":x:`You do not have permission to do this!`";
        }

        return "Invalid argument";
    }

}

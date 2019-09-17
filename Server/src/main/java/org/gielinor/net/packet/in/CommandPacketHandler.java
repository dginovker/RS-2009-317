package org.gielinor.net.packet.in;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.impl.DefaultCommand;
import org.gielinor.game.world.World;
import org.gielinor.game.world.callback.CallBack;
import org.gielinor.mqueue.message.impl.CommandMessage;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles player commands (the ::words).
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 * @author Graham Edgecombe
 */
public class CommandPacketHandler implements IncomingPacket, CallBack {

    private static final Logger log = LoggerFactory.getLogger(CommandPacketHandler.class);

    /**
     * The {@link java.util.List} of command classes.
     */
    private static final List<Command> COMMAND_LIST = new ArrayList<>();

    /**
     * The command mapping.
     */
    private static final Map<String, Command> commands = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void decode(Player player, int opcode, PacketBuilder buffer) {
        final String commandString = ByteBufferUtils.getRS2String(buffer.toByteBuffer());
        final String[] args = commandString.split(" ");
        final String trigger = args[0].toLowerCase();

        if (trigger.isEmpty()) {
            return;
        }

        if (SystemManager.isActive()) {
            World.submit(new CommandMessage(player, args));
        }

        final Command command = commands.get(trigger);

        // Delegate to DefaultCommand if we don't have a more appropiate handler.
        if (command == null) {
            Command defaultCommand = new DefaultCommand();
            defaultCommand.execute(player, args);
            return;
        }

        boolean hasPermissionsRequired = false;

        if (World.getConfiguration().isBetaEnabled() && command.isBeta()) {
            hasPermissionsRequired = true;
        }

        if (player.getRights().ordinal() >= command.getRights().ordinal() && command.canUse(player)) {
            hasPermissionsRequired = true;
        }

        if (hasPermissionsRequired) {
            try {
                command.execute(player, args);
            } catch (Throwable t) {
                log.error("Error executing command [{}] from [{}].", commandString, player.getName(), t);
                player.getActionSender().sendMessage("An error occured. Please report this.");
            }
        } else {
            // It's actually a lack of permissions, but we lie and say the command is unknown.
            player.getActionSender().sendMessage("Unknown command: " + args[0].toLowerCase() + ".");
        }
    }

    @Override
    public boolean call() {
        boolean reload = false;
        if (commands.size() > 0) {
            commands.clear();
            reload = true;
        }
        String packageName = "org.gielinor.game.system.command.impl.";
        File commandDir = new File(Constants.CLASS_DIRECTORY + packageName.replaceAll("\\.", "/"));
        File[] files = commandDir.listFiles();
        assert files != null;
        for (File f : files) {
            if (f == null) {
                continue;
            }
            if (f.getName().contains("$")) {
                continue;
            }
            if (!f.getName().endsWith(".class")) {
                continue;
            }
            String fileName = packageName + f.getName().substring(0, f.getName().length() - 6);
            try {
                Class c = Class.forName(fileName);
                Command commandClass = ((Command) c.newInstance());
                commandClass.init();
                COMMAND_LIST.add(commandClass);
                if (commandClass.getCommands() != null) {
                    for (String cmd : commandClass.getCommands()) {
                        commands.put(cmd, commandClass);
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
                log.error("No command String set for command class: {}.", f.getName(), ex);
            }
        }
        String action = reload ? "Reloaded" : "Loaded";
        log.info("{} {} commands.", action, commands.size());
        return true;
    }

    /**
     * Gets the command list.
     *
     * @return The command class list.
     */
    public static List<Command> getCommandList() {
        return COMMAND_LIST;
    }

    /**
     * Gets the commands mapping.
     *
     * @return The commands mapping.
     */
    public static Map<String, Command> getCommands() {
        return commands;
    }

}

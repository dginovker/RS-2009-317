package org.gielinor.game.system.command;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.player.info.Rights;

/**
 * Represents the description of a {@link org.gielinor.game.system.command.Command}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CommandDescription {

    /**
     * The {@link org.gielinor.game.system.command.CommandDescription}s.
     */
    private static final List<CommandDescription> commandDescriptions = new ArrayList<>();
    /**
     * The command.
     */
    private final String command;
    /**
     * The description.
     */
    private final String description;
    /**
     * The rights.
     */
    private final Rights rights;
    /**
     * The usages.
     */
    private String usages;

    /**
     * Constructs a new {@link org.gielinor.game.system.command.CommandDescription}.
     *
     * @param command     The command.
     * @param description The description.
     * @param usages      The usages.
     */
    public CommandDescription(String command, String description, Rights rights, String usages) {
        this.command = command;
        this.description = description;
        this.rights = rights;
        this.usages = usages;
    }

    /**
     * Constructs a new {@link org.gielinor.game.system.command.CommandDescription}.
     *
     * @param command     The command.
     * @param description The description.
     * @param usages      The usages.
     */
    public CommandDescription(String command, String description, String usages) {
        this(command, description, Rights.REGULAR_PLAYER, usages);
    }

    /**
     * Constructs a new {@link org.gielinor.game.system.command.CommandDescription}.
     *
     * @param command     The command.
     * @param description The description.
     */
    public CommandDescription(String command, String description) {
        this(command, description, Rights.REGULAR_PLAYER, null);
    }

    /**
     * Adds a {@link org.gielinor.game.system.command.CommandDescription}s for this command.
     *
     * @param commandDescription The {@link org.gielinor.game.system.command.CommandDescription}.
     */
    public static void add(CommandDescription commandDescription) {
        commandDescriptions.add(commandDescription);
    }

    /**
     * Gets the {@link org.gielinor.game.system.command.CommandDescription} list.
     *
     * @return The list.
     */
    public static List<CommandDescription> values() {
        return commandDescriptions;
    }

    /**
     * Gets the command.
     *
     * @return The command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Gets the description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the rights.
     *
     * @return The {@link org.gielinor.game.node.entity.player.info.Rights}.
     */
    public Rights getRights() {
        return rights;
    }

    /**
     * Gets the usages.
     *
     * @return The usages.
     */
    public String getUsages() {
        return usages;
    }
}

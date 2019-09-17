package org.gielinor.rs2.model.command;

import java.util.ArrayList;

import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents an {@link java.util.ArrayList} of commands.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Command extends ArrayList<Plugin<Object>> {

    /**
     * Gets the singleton of this class.
     */
    private static final Command command = new Command();

    /**
     * The default constructor.
     */
    public Command() {

    }

    /**
     * Gets the singleton of this class.
     *
     * @return The {@link #command} singleton.
     */
    public static Command getCommand() {
        return command;
    }
}
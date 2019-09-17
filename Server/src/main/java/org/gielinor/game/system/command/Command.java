package org.gielinor.game.system.command;

import java.util.Arrays;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.impl.YellCommand;

/**
 * Represents a single command.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *
 */
public abstract class Command {

    /**
     * Initializes this command.
     */
    public void init() {

    }

    /**
     * The rights required to use this command.
     *
     * @return The rights.
     */
    public abstract Rights getRights();

    /**
     * Additional variables to check before we know if the player can use this
     * command.
     *
     * @return <code>True</code> if so.
     */
    public abstract boolean canUse(Player player);

    /**
     * The strings that this command will work with.
     *
     * @return The strings.
     */
    public abstract String[] getCommands();

    /**
     * Checks if this is a command that can be used in beta mode.
     *
     * @return <code>True</code> if so.
     */
    public boolean isBeta() {
        return false;
    }

    /**
     * Executes the command.
     *
     * @param player
     *            The player using the command.
     * @param params
     *            The parameters in the command.
     */
    public abstract void execute(Player player, String[] params);

    /**
     * Converts parameters at given index to a {@link String},
     * for {@link Command}s such as {@link YellCommand}.
     *
     * @param params
     *      The parameters.
     * @param index
     *      The starting index.
     * @return
     * 		The command's parameters in a string.
     */
    public final String toString(String[] params, int index) {
        if (params.length < index) {
            return "";
        }

        return String.join(" ", Arrays.copyOfRange(params, index, params.length));
    }

}

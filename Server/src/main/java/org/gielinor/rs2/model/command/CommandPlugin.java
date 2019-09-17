package org.gielinor.rs2.model.command;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents a plugin for a player command.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public abstract class CommandPlugin implements Plugin<Object> {

    /**
     * Executes the command.
     *
     * @param player  The player using this command.
     * @param command The command string.
     * @param args    The arguments in the command.
     */
    public abstract void execute(Player player, String command, String[] args);

    /**
     * Additional variables to check before we know if the player can execute this
     * command.
     *
     * @return <code>True</code> if the player can execute this command.
     */
    public abstract boolean canUse(Player player);

    /**
     * Gets the rights required to use this command.
     *
     * @return The rights.
     */
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    /**
     * The strings that this command will work with.
     *
     * @return The strings.
     */
    public abstract Object getCommands();

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Gets the argument line (starting at index 1).
     *
     * @param args The arguments.
     * @return The argument line.
     */
    public String toString(String[] args) {
        return toString(args, 1, args.length);
    }

    /**
     * Gets the argument line from the given arguments.
     *
     * @param args   The arguments.
     * @param offset The start index.
     * @param length The end index.
     * @return the line.
     */
    public String toString(String[] args, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < length; i++) {
            if (i != offset) {
                sb.append(" ");
            }
            sb.append(args[i]);
        }
        return sb.toString();
    }
}

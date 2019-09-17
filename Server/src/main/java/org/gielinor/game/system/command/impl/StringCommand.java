package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;

/**
 * Sends a string on an interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class StringCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "str" };
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length < 3) {
            player.getActionSender().sendMessage("Use as ::str <lt>interface id> <lt>string>");
            return;
        }
        player.getActionSender().sendString(Integer.parseInt(args[1]), toString(args, 2));
    }
}

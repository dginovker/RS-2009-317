package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.rs2.config.ServerVar;

/**
 * Opens the forums for the player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ForumsCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "forum", "forums", "website" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("forums", "Launches the forums website"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        player.getActionSender().sendURL(ServerVar.fetch("forum_url"));
    }
}

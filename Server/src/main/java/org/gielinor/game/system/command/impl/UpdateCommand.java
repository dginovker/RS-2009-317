package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.system.SystemState;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Updates the server.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class UpdateCommand extends Command {

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
        return new String[]{ "update" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("update", "Sends the update process to players online", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 2) {
            int timer = Integer.parseInt(args[1]);
            if (timer == 0) {
                SystemManager.getUpdater().cancel();
                SystemManager.getUpdater().notifyPlayers();
                return;
            }
            SystemManager.getUpdater().setCountdown(timer);
        }
        SystemManager.flag(SystemState.UPDATING);
    }
}

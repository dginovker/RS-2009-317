package org.gielinor.game.system.command.impl;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;

/**
 * Sends string ids to an interface that has been manually entered here.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SendStringsCommand extends Command {

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
        return new String[]{ "strs" };
    }

    @Override
    public void execute(Player player, String[] args) {
        player.getInterfaceState().open(new Component(6308));
        for (int textId = 6309; textId < 10000; textId++) {
            player.getActionSender().sendString("" + textId, textId);
        }
    }
}

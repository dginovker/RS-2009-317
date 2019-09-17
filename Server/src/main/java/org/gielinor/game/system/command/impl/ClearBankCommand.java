package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Clears the player's bank of items.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ClearBankCommand extends Command {

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
        return new String[]{ "clearbank" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("clearbank", "Clears bank items", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        player.getBank().clear();
        player.getBank().getBankData().setTabAmounts(new int[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        player.getBank().updateScroll();
        player.getBank().getBankData().setOpenTab(0);
        player.getBank().update(true);
    }
}

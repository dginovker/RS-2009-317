package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.utilities.string.TextUtils;

/**
 * Gives the player max coins.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class MaxCashCommand extends Command {

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
        return new String[]{ "maxcash", "cash", "coins" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("maxcash",
            "Gives " + TextUtils.getFormattedNumber(Integer.MAX_VALUE) + " coins", getRights(), null));
    }

    @Override
    public void execute(final Player player, String[] args) {
        player.getInventory().add(new Item(Item.COINS, Integer.MAX_VALUE));
    }

}

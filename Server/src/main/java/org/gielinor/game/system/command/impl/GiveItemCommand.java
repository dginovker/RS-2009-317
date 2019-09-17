package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.utilities.string.TextUtils;

/**
 * Gives the given player an item.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GiveItemCommand extends Command {

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
        return new String[]{ "giveitem" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("giveitem", "Gives an item to a player", getRights(),
            "::giveitem <lt>item_id> <lt>count> <lt>player_name><br>Example:<br>::giveitem 4151 1 Gielinor"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.getActionSender().sendMessage("Use as ::giveitem <lt>item id> <lt>count> <lt>player name>");
            return;
        }
        int itemId = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);
        String playerName = toString(args, 3);
        Player otherPlayer = Repository.getPlayerByName(toString(args, 3));
        if (otherPlayer == null) {
            player.getActionSender().sendMessage("Player " + playerName + " is not online.");
            return;
        }
        otherPlayer.getInventory().add(new Item(itemId, count));
        otherPlayer.getActionSender().sendMessage(TextUtils.formatDisplayName(player.getName()) + " has given you "
            + ItemDefinition.forId(itemId).getName() + ".");
    }
}

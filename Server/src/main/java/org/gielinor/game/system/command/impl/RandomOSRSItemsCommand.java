package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Gives the player random items
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RandomOSRSItemsCommand extends Command {

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
        return new String[]{ "randomosrsitems" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("randomitems", "Fills inventory with random items", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        boolean noted = args.length == 2 && args[1].toLowerCase().startsWith("note");
        while (player.getInventory().freeSlots() > 0) {
            player.getInventory().add(new Item(ItemDefinition.getRandomOSRSDefinition(noted).getId(), 1));
        }
    }
}

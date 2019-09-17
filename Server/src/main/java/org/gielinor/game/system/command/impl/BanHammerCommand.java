package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Spawns a Ban Hammer for users with correct permission.
 *
 * @author Corey
 */
public class BanHammerCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.PLAYER_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "bh", "banhammer", "bhammer" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("banhammer", "Spawns the almighty Ban Hammer.", getRights(), "::banhammer<br>::bhammer<br>::bh"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (!player.getInventory().add(new Item(Item.BAN_HAMMER))) {
            player.getActionSender().sendMessage("You do not have enough space in your inventory!");
        } else {
            player.getActionSender().sendMessage("Use it wisely.");
        }
    }
}

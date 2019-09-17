package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Poisons the given player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PoisonCommand extends Command {

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
        return new String[]{ "poison" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("poison", "Poisons a player", getRights(),
            "::poison <lt>amount> <lt>[ player_name]>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length >= 3) {
            Player otherPlayer = Repository.getPlayerByName(toString(args, 2));
            if (otherPlayer == null) {
                player.getActionSender().sendMessage("Player not online.");
                return;
            }
            if (Integer.parseInt(args[1]) < 0) {
                player.getActionSender().sendMessage("Use as ::poison <lt>amount> <lt>[ player_name]>");
                return;
            }
            otherPlayer.getStateManager().register(EntityState.POISONED, true, Integer.parseInt(args[1]), player);
        } else {
            if (!(args.length >= 2)) {
                player.getActionSender().sendMessage("Use as ::poison <lt>amount>");
                return;
            }
            if (Integer.parseInt(args[1]) < 0 || Integer.parseInt(args[1]) > 100) {
                player.getActionSender().sendMessage("Use as ::poison <lt>amount>");
                return;
            }
            player.getStateManager().register(EntityState.POISONED, true, Integer.parseInt(args[1]), player);
        }
    }
}

package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.info.perk.PerkManagement;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Adds, removes, enables or disables a
 * {@link org.gielinor.game.node.entity.player.info.Perk} for the given player
 * (if any).
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PerkCommand extends Command {

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
        return new String[]{ "addperk", "removeperk", "enableperk", "disableperk", "perks" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("addperk", "Adds a perk to a player's list", getRights(),
            "::addperk <lt>perk_id> <lt>[ player_name]><br>Will add perk to your account if player_name is empty"));
        CommandDescription.add(new CommandDescription("removeperk", "Removes a perk to a player's list", getRights(),
            "::removeperk <lt>perk_id> <lt>[ player_name]><br>Will remove perk from your account if player_name is empty"));
        CommandDescription.add(new CommandDescription("enableperk", "Enables a perk for a player", getRights(),
            "::enableperk <lt>perk_id> <lt>[ player_name]><br>Will enable perk on your account if player_name is empty"));
        CommandDescription.add(new CommandDescription("disableperk", "Disables a perk for a player", getRights(),
            "::disableperk <lt>perk_id> <lt>[ player_name]><br>Will disable perk on your account if player_name is empty"));
        CommandDescription.add(new CommandDescription("perks", "Displays available perks", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args[0].toLowerCase().startsWith("perks")) {
            PerkManagement.openPage(player, 0, true);
            return;
        }
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>perk id>-<lt>[ player name]>");
            return;
        }
        if (!StringUtils.isNumeric(args[1])) {
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>perk id>-<lt>[ player name]>");
            return;
        }
        Perk perk = Perk.forId(Integer.parseInt(args[1]));
        if (perk == null) {
            player.getActionSender().sendMessage("Invalid perk id. Type ::perks to see list.");
            return;
        }
        String playerName = null;
        Player otherPlayer = null;
        if (args.length >= 3) {
            playerName = toString(args, 2);
            otherPlayer = Repository.getPlayerByName(playerName);
        } else {
            otherPlayer = player;
        }
        if (otherPlayer == null) {
            player.getActionSender().sendMessage("Player " + playerName + " is not online.");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "addperk":
                otherPlayer.getPerkManager().unlock(perk);
                otherPlayer.getActionSender().sendMessage(
                    "You have been given the perk " + perk.name().toLowerCase().replaceAll("_", " ") + ".");
                player.getActionSender().sendMessage("Gave perk " + perk.name().toLowerCase().replaceAll("_", " ") + " to "
                    + otherPlayer.getName() + ".");
                break;
            case "removeperk":
                otherPlayer.getPerkManager().remove(perk);
                otherPlayer.getActionSender().sendMessage("The perk " + perk.name().toLowerCase().replaceAll("_", " ")
                    + " has been removed from your account.");
                player.getActionSender().sendMessage("Removed perk " + perk.name().toLowerCase().replaceAll("_", " ")
                    + " from " + otherPlayer.getName() + ".");
                break;
            case "enableperk":
                otherPlayer.getPerkManager().enable(perk);
                otherPlayer.getActionSender().sendMessage("The perk " + perk.name().toLowerCase().replaceAll("_", " ")
                    + " has been enabled on your account.");
                player.getActionSender().sendMessage("Enabled perk " + perk.name().toLowerCase().replaceAll("_", " ")
                    + " for " + otherPlayer.getName() + ".");
                break;
            case "disableperk":
                otherPlayer.getPerkManager().disable(perk);
                otherPlayer.getActionSender().sendMessage("The perk " + perk.name().toLowerCase().replaceAll("_", " ")
                    + " has been disabled on your account.");
                player.getActionSender().sendMessage("Disabled perk " + perk.name().toLowerCase().replaceAll("_", " ")
                    + " for " + otherPlayer.getName() + ".");
                break;
        }
    }
}

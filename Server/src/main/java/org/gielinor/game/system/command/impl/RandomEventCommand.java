package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Starts a random event for the player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RandomEventCommand extends Command {

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
        return new String[]{ "randomevent" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("randomevent", "Triggers a random event for a player", getRights(),
                "::randomevent <lt>randomname> <lt>[ player_name]><br>"
                    + "Will trigger on your account if player_name is empty.<br>Possible random events:<br>"
                    + "lostandfound<br>evilchicken<br>rivertroll<br>treespirit<br>swarm<br>genie"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::randomevent <lt>randomname> <lt>[ player_name]>");
            return;
        }
        Player toRandom;
        if (args.length >= 3) {
            String otherPlayer = toString(args, 2);
            toRandom = Repository.getPlayerByName(otherPlayer);
            if (toRandom == null) {
                player.getActionSender().sendMessage("Player " + otherPlayer + " is not online.");
                return;
            }
        } else {
            toRandom = player;
        }
        if (toRandom.getAntiMacroHandler().hasEvent()) {
            player.getActionSender().sendMessage(toRandom.getName() + " already has random event "
                + toRandom.getAntiMacroHandler().getEvent().getName() + " running.");
            return;
        }
        String randomEvent = args[1].replaceAll("_", " ");
        switch (args[1].toLowerCase()) {
            case "evilchicken":
                randomEvent = "Evil chicken";
                break;
            case "rivertroll":
                randomEvent = "River troll";
                break;
            case "treespirit":
                randomEvent = "Tree spirit";
                break;
            case "swarm":
                randomEvent = "Swarm";
                break;
            case "genie":
                randomEvent = "Genie";
                break;
            case "lostandfound":
            case "lostfound":
            case "lost_and_found":
                player.getAntiMacroHandler().fireEvent("Lost and found", player.getLocation());
                return;
        }
        if (!toRandom.getAntiMacroHandler().fireEvent(randomEvent, player)) {
            player.getActionSender().sendMessage("Invalid random event " + randomEvent + "!");
        }
    }

}

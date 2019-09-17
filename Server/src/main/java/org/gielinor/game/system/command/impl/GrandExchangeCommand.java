package org.gielinor.game.system.command.impl;

import org.gielinor.game.content.eco.grandexchange.ResourceManager;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * A Grand Exchange utility command.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GrandExchangeCommand extends Command {

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
        return new String[]{ "clear_resource", "clearresource", "add_resource", "addresource", "kickstarteco",
            "reset_resources" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("clear_resource", "Clears a Grand Exchange resource by id",
            getRights(), "::clear_resource <lt>item_id><br>Example:<br>::clear_resource 4151"));
        CommandDescription.add(new CommandDescription("add_resource", "Adds a Grand Exchange resource by id",
            getRights(),
            "::add_resource <lt>item_id> <lt>item_count>[ <lt>sale>]<br>Example:<br>::add_resource 556 2000<br>::add_resource 1113 100 true"));
        CommandDescription.add(
            new CommandDescription("kickstarteco", "Kick starts the Grand Exchange economy", getRights(), null));
        CommandDescription.add(
            new CommandDescription("reset_resources", "Resets the Grand Exchange resources", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        switch (args[0].toLowerCase()) {
            case "clear_resource":
            case "clearresource":
                if (args.length < 2) {
                    player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>item_id>");
                    break;
                }
                ResourceManager.clearResource(Integer.parseInt(args[1]));
                player.getActionSender().sendConsoleMessage("Cleared Grand Exchange resource " + args[1] + "!");
                break;

            case "add_resource":
            case "addresource":
                if (args.length < 3) {
                    player.getActionSender()
                        .sendMessage("Use as ::" + args[0] + " <lt>item_id> <lt>item_count>[ <lt>sale>]");
                    break;
                }
                boolean sell = !(args.length > 3 && args[3].equals("false"));
                ResourceManager.addResource(Integer.parseInt(args[1]), Integer.parseInt(args[2]), sell);
                player.getActionSender().sendConsoleMessage("Added " + (sell ? "selling" : "buying") + " resource item "
                    + args[1] + " with the count of " + args[2] + "!");
                break;

            case "kickstarteco":
                ResourceManager.kickStartEconomy();
                break;

            case "reset_resources":
                ResourceManager.getStock().clear();
                break;
        }
    }
}

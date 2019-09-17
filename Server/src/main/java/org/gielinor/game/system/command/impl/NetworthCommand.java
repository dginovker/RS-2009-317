package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Displays the player's current networth.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class NetworthCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "networth" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("networth", "Prints your current calculated networth", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        long finalValue = player.getNetworth();
        String stringValue = "";
        if (finalValue >= 1000 && finalValue < 1000000) {
            stringValue = "(" + (finalValue / 1000) + "K).";
        } else if (finalValue >= 1000000) {
            stringValue = "(" + (finalValue / 1000000) + " million).";
        }
        player.getActionSender().sendMessage("Your networth is " + (finalValue + " " + stringValue));
    }
}

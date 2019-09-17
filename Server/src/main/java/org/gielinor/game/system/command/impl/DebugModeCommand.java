package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Turns debug mode on/off for the player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DebugModeCommand extends Command {

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
        return new String[]{ "debug", "dbg", "debugmode", "dbgmode", "dbgbank" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("debug", "Toggles debug mode on or off", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] params) {
        if (params != null && params.length >= 1 && params[0].equalsIgnoreCase("dbgbank")) {
            int slot = 0;
            for (Item item : player.getBank().toArray()) {
                if (item == null) {
                    continue;
                }
                System.out.println(slot + " : " + item.getId());
                slot++;
            }
            return;
        }
        player.toggleDebug();
        player.getActionSender()
            .sendMessage("Debug mode is now " + (player.isDebug() ? "on. Check console for data." : "off."));
        player.getActionSender().sendConsoleMessage("Debug mode is now " + (player.isDebug() ? "on" : "off") + ".");
    }

}

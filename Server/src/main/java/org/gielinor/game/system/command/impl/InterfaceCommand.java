package org.gielinor.game.system.command.impl;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Opens an interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class InterfaceCommand extends Command {

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
        return new String[]{ "interface", "inter" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("interface", "Opens an interface", getRights(),
            "::interface <lt>interface_id> <lt>[ type]><br>Types can be the following:<br />inv - Opens inventory interface<br>chat - Opens chatbox interface"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        switch (args.length) {
            case 3:
                switch (args[2].toLowerCase()) {
                    case "inv":
                    case "inventory":
                        player.getInterfaceState().openTab(Sidebar.INVENTORY_TAB.ordinal(),
                            new Component(Integer.parseInt(args[1])));
                        break;
                    case "c":
                    case "chat":
                    case "chatbox":
                        player.getInterfaceState().openChatbox(Integer.parseInt(args[1]));
                        break;
                    default:
                        player.getActionSender().sendMessage("Use as ::interface <lt>interface_id> <lt> [type]>");
                        break;
                }
                break;
            case 2:
                player.getInterfaceState().open(new Component(Integer.parseInt(args[1])));
                break;
        }
    }
}

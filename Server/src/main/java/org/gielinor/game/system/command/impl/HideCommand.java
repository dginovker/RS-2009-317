package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * (Un)Hides the player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class HideCommand extends Command {

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
        return new String[]{ "setvis" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("setvis", "Sets your visibility status", getRights(),
            "::setvis <lt>level><br>Levels:<br> 0 - Visible<br>1 - Invisible to everyone, including yourself<br>2 - Invisible to everyone, excluding yourself"));
    }

    @Override
    public void execute(Player player, String[] args) {
        // 0 = visible, 1 = invisible to all including, 2 = invisible to all
        // excluding you
        if (args.length != 2 || Integer.parseInt(args[1]) < 0 || Integer.parseInt(args[1]) > 2) {
            player.getActionSender().sendMessage("Use as ::setvis <lt>level>");
            return;
        }
        player.getSavedData().getGlobalData().setVisibility((byte) Integer.parseInt(args[1]));
        player.getAppearance().sync();
        player.getActionSender().sendMessage("vis: " + args[1]);
    }
}

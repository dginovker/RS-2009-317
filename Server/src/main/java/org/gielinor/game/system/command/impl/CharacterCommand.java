package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Opens the player's character appearance editor.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CharacterCommand extends Command {

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
        return new String[]{ "char", "mychar", "app", "appearance" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("char", "Opens the character design interface", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        player.getInterfaceState().openComponent(3559);
    }
}

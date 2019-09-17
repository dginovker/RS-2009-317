package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;

/**
 * Allows the player to set their new password.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ResetCombatStatCommand extends Command {

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
        return new String[]{ "resetstat", "resetcombatstat", "resetcmb", "resetcombatstats", "resetstats" };
    }

    @Override
    public void execute(Player player, String[] args) {
        player.getDialogueInterpreter().open("ResetCombatStatsDialogue");
    }
}

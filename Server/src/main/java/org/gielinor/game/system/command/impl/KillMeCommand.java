package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * The class that represents my goodbye to the world.
 *
 * @author Hayder
 */
public class KillMeCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return false;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "killme" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("killme", "Kills command player", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] params) {
        player.getImpactHandler().handleImpact(player, player.getSkills().getMaximumLifepoints(), CombatStyle.MELEE);
    }
}

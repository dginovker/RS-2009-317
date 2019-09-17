package org.gielinor.game.system.command.impl;

import org.gielinor.content.periodicity.PeriodicityExtensionsKt;
import org.gielinor.content.periodicity.PeriodicityPulseManager;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

public class PeriodicityPulseCommand extends Command {

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
        return new String[]{ "periodicity-pulse" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("periodicity-pulse", "Pulses all the periodic events.", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        PeriodicityExtensionsKt.pulse(PeriodicityPulseManager.Companion.getPULSES());
    }
}

package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.World;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Runs through configurations.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RunConfigCommand extends Command {

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
        return new String[]{ "runcfg", "runconfig" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("runcfg",
            "Runs through configurations from a starting<br>id, sending them to the client", getRights(),
            "::runcfg <lt>start_id>"));
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (player.getAttribute("CONFIG_PULSE") != null) {
            Pulse configPulse = player.getAttribute("CONFIG_PULSE");
            configPulse.stop();
            player.removeAttribute("CONFIG_PULSE");
            player.getActionSender().sendMessage("Stopped.");
            return;
        }
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::runcfg <lt>starting config id>");
            return;
        }
        player.setAttribute("CONFIG_PULSE", new Pulse(2, player) {

            int configId = Integer.parseInt(args[1]);

            @Override
            public boolean pulse() {
                player.getActionSender().sendMessage("Config ID: " + configId);
                player.getConfigManager().set(configId, 1, false);
                configId += 1;
                return false;
            }
        });
        World.submit((Pulse) player.getAttribute("CONFIG_PULSE"));
    }
}

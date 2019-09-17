package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.ai.AIPBuilder;
import org.gielinor.game.node.entity.player.ai.AIPlayer;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles commands concerning artificial players.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AIPCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        if (Constants.isTemp(player.getUsername())) {
            player.getActionSender().sendConsoleMessage("Uh oh... You cannot use this command yet!");
            return false;
        }
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "aip", "aipcopy", "aipclear", "massaip" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("aip", "Adds an artificial player", getRights(), "::aip <lt>username>"));
        CommandDescription.add(new CommandDescription("aipcopy", "Adds an artificial player, copying character",
            getRights(), "::aipcopy <lt>username>"));
        CommandDescription
            .add(new CommandDescription("aipclear", "Clears all online artificial players", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("aipclear")) {
            for (Player ai : Repository.getPlayers()) {
                if (!(ai instanceof AIPlayer)) {
                    continue;
                }
                AIPlayer.deregister(((AIPlayer) ai).getUid());
            }
            return;
        }
        if (args[0].equalsIgnoreCase("massaip")) {
            int amount = (args.length >= 2) ? Integer.parseInt(args[1]) : 30;
            player.getActionSender().sendConsoleMessage("Adding " + amount + " aips.");
            while (amount > 0) {
                String name = RandomStringUtils.randomAlphanumeric(RandomUtil.random(6, 12));
                Location randomLocation = Location
                    .getRandomLocation(ZoneBorders.forRegion(player.getLocation().getRegionId()));
                AIPlayer aiPlayer = AIPBuilder.makeUnique(player, name, randomLocation);
                Repository.getPlayers().add(aiPlayer);
                aiPlayer.init();
                Interaction.sendOption(player, 7, "Control");
                aiPlayer.getWalkingQueue().reset();
                player.getActionSender().sendConsoleMessage("Added AIP " + name + ".");
                amount--;
            }
            return;
        }
        String name = args.length < 2 ? player.getName() : args[1];
        AIPlayer p = args[0].equalsIgnoreCase("aipcopy")
            ? AIPBuilder.copy(player, name, player.getLocation().transform(0, 1, 0))
            : AIPBuilder.makeUnique(player, name, player.getLocation().transform(0, 1, 0));
        Repository.getPlayers().add(p);
        p.init();
        Interaction.sendOption(player, 3, "Control");
    }
}

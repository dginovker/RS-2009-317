package org.gielinor.game.system.command.impl;

import org.gielinor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.repository.Repository;

/**
 * Resets a player's configuration block.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a> TODO
 */
public class ResetConfigCommand extends Command {

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
        return new String[]{ "clearconfig" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("clearconfig", "Clears Farming configuration for a player",
            getRights(), "::clearconfig <lt>player_name>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::clearconfig <lt>player name>");
        }
        final GameObject object = RegionManager.getObject(player.getLocation());
        if (object == null) {
            player.getActionSender().sendMessage("No patch found.");
            return;
        }
        String playerName = toString(args, 1);
        Player otherPlayer = Repository.getPlayerByName(playerName);
        if (otherPlayer == null) {
            player.getActionSender().sendMessage("No player found " + playerName + ".");
            return;
        }
        PatchWrapper wrapper = otherPlayer.getFarmingManager().getPatchWrapper(object.getWrapper().getId());
        wrapper.getCycle().clear(otherPlayer);
        player.getActionSender().sendMessage("Cleared.");
    }
}

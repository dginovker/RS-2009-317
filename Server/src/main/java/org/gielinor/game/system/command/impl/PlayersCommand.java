package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.utilities.string.TextUtils;

/**
 * Shows the online players.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PlayersCommand extends Command {

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
        return new String[]{ "players", "playersonline" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("players", "Displays the count of players online", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        int playersOnline = Repository.getPlayers().size();
        player.getActionSender().sendMessage("There " + (playersOnline == 1 ? "is" : "are") + " currently " + playersOnline + " player" + (playersOnline == 1 ? "" : "s") + " online.");
        if (playersOnline < 100 && player.getRights().isAdministrator()) {
            StringBuilder stringBuilder = new StringBuilder();
            player.getQuestMenuManager().setTitle("Players Online - " + playersOnline);
            for (Player otherPlayer : Repository.getPlayers()) {
                if (otherPlayer == null || !otherPlayer.isActive() || otherPlayer.getSavedData().getGlobalData().getVisibility() > 0) {
                    continue;
                }
                if (otherPlayer.isArtificial()) {
                    continue;
                }
                String online = (TextUtils.formatDisplayName(otherPlayer.getUsername()));
                String gielinorId = "<col=255>" + otherPlayer.getRights().name() + ":" + otherPlayer.getPidn() + "</col>";
                stringBuilder.append(online);
                if (player.getRights().isAdministrator()) {
                    stringBuilder.append(" <col=0E2C41>(").append(gielinorId).append(")");
                }
                stringBuilder.append(", ");
            }
            player.getQuestMenuManager().setLines(stringBuilder.toString().split(", ")).send();
        }
    }
}

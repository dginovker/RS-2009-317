package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Hits the player or the given player with the given amount
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class HitCommand extends Command {

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
        return new String[]{ "hit", "hitp", "hitd" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("hit", "Hits a player for the damage given", getRights(),
            "::hit <lt>amount> <lt>[ player_name]>"));
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args.length >= 3) {
            Player otherPlayer = Repository.getPlayerByName(toString(args, 2));
            if (otherPlayer == null) {
                player.getActionSender().sendMessage("Player not online.");
                return;
            }
            if (Integer.parseInt(args[1]) < 0) {
                player.getActionSender().sendMessage("Use as ::hit <lt>amount> <lt>[ player_name]>");
                return;
            }
            HitsplatType hitsplatType = args[0].equalsIgnoreCase("hit") ? HitsplatType.NORMAL
                : args[0].equalsIgnoreCase("hitp") ? HitsplatType.POISON : HitsplatType.DISEASE;
            otherPlayer.getImpactHandler().manualHit(player, Integer.parseInt(args[1]), hitsplatType);
            if (player.getProperties().getAttackAnimation() != null && !player.getAppearance().isNpc()) {
                player.animate(player.getProperties().getAttackAnimation());
            }
            if (otherPlayer.getProperties().getDefenceAnimation() != null) {
                otherPlayer.animate(otherPlayer.getProperties().getDefenceAnimation());
            }
        } else {
            if (!(args.length >= 2)) {
                player.getActionSender().sendMessage("Use as ::hit <lt>amount>");
                return;
            }
            if (Integer.parseInt(args[1]) < 0 || Integer.parseInt(args[1]) > 100) {
                player.getActionSender().sendMessage("Use as ::hit <lt>amount>");
                return;
            }
            HitsplatType hitsplatType = args[0].equalsIgnoreCase("hit") ? HitsplatType.NORMAL
                : args[0].equalsIgnoreCase("hitp") ? HitsplatType.POISON : HitsplatType.DISEASE;
            player.getImpactHandler().manualHit(player, Integer.parseInt(args[1]), hitsplatType);
            if (player.getProperties().getDefenceAnimation() != null) {
                player.animate(player.getProperties().getDefenceAnimation());
            }
        }
    }
}

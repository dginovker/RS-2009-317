package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.utilities.misc.PlayerLoader;

/**
 * Copies another player's items, appearance, stats, inventory or bank
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CopyCommand extends Command {

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
        return new String[]{ "copy" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("copy", "Copies another player's information", getRights(),
            "::copy <lt>type> <lt>player name><br>Types:<br>inventory<br>bank<br>equipment<br>appearance<br>stats"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length >= 3) {
            boolean clear = false;
            String playerName = toString(args, 2).toLowerCase();
            Player otherPlayer = Repository.getPlayerByName(toString(args, 2));
            if (otherPlayer == null) {
                otherPlayer = PlayerLoader.getPlayerFile(playerName);
                clear = true;
            }
            if (otherPlayer == null) {
                player.getActionSender().sendMessage("Player " + playerName + " could not be found.");
                return;
            }
            switch (args[1].toLowerCase()) {
                case "inventory":
                    player.getInventory().copy(otherPlayer.getInventory());
                    player.getInventory().update(true);
                    player.getInventory().refresh();
                    break;
                case "bankclean":
                    player.getBank().copy(otherPlayer.getBank());
                    player.getBank().update(true);
                    player.getBank().refresh();
                    break;
                case "bank":
                    player.getBank().copy(otherPlayer.getBank());
                    player.getBank().getBankData().setTabAmounts(otherPlayer.getBank().getBankData().getTabAmounts());
                    player.getBank().update(true);
                    player.getBank().getBankData().setOpenTab(0);
                    player.getBank().refresh();
                    break;
                case "equipment":
                    player.getEquipment().copy(otherPlayer.getEquipment());
                    player.getEquipment().update(true);
                    player.getEquipment().refresh();
                    player.getAppearance().sync();
                    break;
                case "appearance":
                    player.getAppearance().copy(otherPlayer.getAppearance());
                    player.getAppearance().sync();
                    break;
                case "stats":
                case "skills":
                    player.getSkills().copy(otherPlayer.getSkills());
                    player.getSkills().updateCombatLevel();
                    player.getSkills().refresh();
                    player.getAppearance().sync();
                    break;
            }
            if (clear) {
                otherPlayer.clear();
            }
        } else {
            player.getActionSender()
                .sendMessage("Use as ::copy <lt>inventory|bank|equipment|appearance|stats> <lt>player name> ");
        }
    }
}

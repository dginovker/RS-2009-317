package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.string.TextUtils;

/**
 * The dialogue plugin for setting a player's rights.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class RightsDialogue extends DialoguePlugin {

    /**
     * The player to set rights for.
     */
    private Player targetPlayer;

    /**
     * Constructs a new {@link RightsDialogue} {@link DialoguePlugin}.
     */
    public RightsDialogue() {
    }

    /**
     * Constructs a new {@link RightsDialogue} {@link DialoguePlugin}.
     *
     * @param player
     *            The player.
     */
    public RightsDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RightsDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (args[0] instanceof Player) {
            targetPlayer = (Player) args[0];
            if (targetPlayer.getRights().isAdministrator() && !player.specialDetails()) {
                end();
                player.getDialogueInterpreter().sendPlaneMessage("You cannot change this player's rights.");
                return true;
            }
            interpreter.sendOptions("Select rights", "Regular Player", "Player Moderator",
                "" + Constants.SERVER_NAME + " Moderator");
            stage = 0;
            return true;
        }
        end();
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        if (targetPlayer.getDetails().getRights() == Rights.GIELINOR_MODERATOR) {
                            targetPlayer.getBank().clear();
                            targetPlayer.getInventory().clear();
                            targetPlayer.getEquipment().clear();
                            targetPlayer.getSkills().reset();
                        }
                        targetPlayer.getDetails().setRights(Rights.REGULAR_PLAYER);
                        targetPlayer.getDetails().save();
                        player.getActionSender()
                            .sendMessage(TextUtils.formatDisplayName(targetPlayer.getDetails().getUsername())
                                + " rights set to Regular Player.");
                        break;
                    case THREE_OPTION_TWO:
                        if (targetPlayer.getDetails().getRights() == Rights.GIELINOR_MODERATOR) {
                            targetPlayer.getBank().clear();
                            targetPlayer.getInventory().clear();
                            targetPlayer.getEquipment().clear();
                            targetPlayer.getSkills().reset();
                        }
                        targetPlayer.getDetails().setRights(Rights.PLAYER_MODERATOR);
                        targetPlayer.getDetails().save();
                        player.getActionSender()
                            .sendMessage(TextUtils.formatDisplayName(targetPlayer.getDetails().getUsername())
                                + " rights set to Player Moderator.");
                        if (targetPlayer.getActionSender() != null && targetPlayer.getSession() != null
                            && targetPlayer.getSession().isActive()) {
                            targetPlayer.getActionSender().sendMessage("Your rights are now Player Moderator.");
                        }
                        break;
                    case THREE_OPTION_THREE:
                        targetPlayer.getDetails().setRights(Rights.GIELINOR_MODERATOR);
                        targetPlayer.getDetails().save();
                        player.getActionSender()
                            .sendMessage(TextUtils.formatDisplayName(targetPlayer.getDetails().getUsername())
                                + " rights set to " + Constants.SERVER_NAME + " Moderator.");
                        if (targetPlayer.getActionSender() != null && targetPlayer.getSession() != null
                            && targetPlayer.getSession().isActive()) {
                            targetPlayer.getActionSender()
                                .sendMessage("Your rights are now " + Constants.SERVER_NAME + " Moderator.");
                        }
                        break;
                }
                end();
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("PlayerRights") };
    }
}

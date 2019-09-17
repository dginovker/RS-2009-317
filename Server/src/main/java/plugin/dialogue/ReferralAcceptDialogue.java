package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.referral.Referred;

import plugin.interaction.item.ReferralTicketPlugin;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for accepting a referral.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ReferralAcceptDialogue extends DialoguePlugin {

    /**
     * The referral request.
     */
    private Referred referred;

    /**
     * Constructs a new <code>ReferralAcceptDialogue</code>.
     */
    public ReferralAcceptDialogue() {

    }

    /**
     * Constructs a new <code>ReferralAcceptDialogue</code>.
     *
     * @param player the player.
     */
    public ReferralAcceptDialogue(final Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ReferralAcceptDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        referred = (Referred) args[0];
        if (referred == null) {
            end();
            return true;
        }
        interpreter.sendPlaneMessage("Are you sure you want to accept this referral", "from the player " + referred.getUsername() + "?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Accept referral?", "Yes.", "No.");
                stage = 1;
                break;
            case 1:
                end();
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player.getReferralManager().accept(referred.getUsername());
                        break;
                    case TWO_OPTION_TWO:
                        ReferralTicketPlugin.openReferralRequests(player);
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("referral-accept") };
    }

}

package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.LoyaltyTitle;
import org.gielinor.game.node.entity.player.info.title.LoyaltyTitleManagement;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for purchasing a {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class LoyaltyTitlePurchaseDialogue extends DialoguePlugin {

    /**
     * The {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle} to purchase.
     */
    private LoyaltyTitle loyaltyTitle;

    /**
     * Constructs a new <code>LoyaltyTitlePurchaseDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public LoyaltyTitlePurchaseDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new <code>LoyaltyTitlePurchaseDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public LoyaltyTitlePurchaseDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new LoyaltyTitlePurchaseDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (!(args[0] instanceof LoyaltyTitle)) {
            return false;
        }
        loyaltyTitle = (LoyaltyTitle) args[0];
        if (!loyaltyTitle.canPurchase(player)) {
            return false;
        }
        if (loyaltyTitle.getCost() == 0) { // Free
            loyaltyTitle.unlock(player);
            interpreter.sendPlaneMessage(false, "You have purchased the title:", loyaltyTitle.getFormattedTitle(player, false, null));
            stage = 100;
            return true;
        }
        interpreter.sendDialogue(
            "Are you sure you want to purchase the title:",
            loyaltyTitle.getFormattedTitle(player, true, "<col=37597E>"),
            "for " + loyaltyTitle.getCostString() + "?"
        );
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                options("Yes.", "No.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (!loyaltyTitle.canPurchase(player)) {
                            end();
                            return true;
                        }
                        String pointsRemaining = loyaltyTitle.purchase(player);
                        if (pointsRemaining != null) {
                            interpreter.sendPlaneMessage(false,
                                "You have purchased the title:",
                                loyaltyTitle.getFormattedTitle(player, false, null),
                                pointsRemaining
                            );
                            stage = 100;
                            return true;
                        }
                        end();
                        return true;
                    case TWO_OPTION_TWO:
                        end();
                        LoyaltyTitleManagement.openPage(player, player.getAttribute("TITLES_PAGE", 0), true);
                        break;
                }
                break;
            case 100:
                interpreter.sendPlaneMessage(false, "To enable this title, go to the title manager and select the", "\"Enable\" option!");
                stage = END;
                break;
            case END:
                end();
                return true;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("loyalty-title-purchase") };
    }
}

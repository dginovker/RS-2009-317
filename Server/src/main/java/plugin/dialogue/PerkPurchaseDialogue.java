package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.entity.player.info.perk.PerkManagement;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for purchasing a
 * {@link org.gielinor.game.node.entity.player.info.Perk}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PerkPurchaseDialogue extends DialoguePlugin {

    /**
     * The {@link org.gielinor.game.node.entity.player.info.Perk} to purchase.
     */
    private Perk perk;

    /**
     * Constructs a new <code>PerkPurchaseDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public PerkPurchaseDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new <code>PerkPurchaseDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public PerkPurchaseDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new PerkPurchaseDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (!(args[0] instanceof Perk)) {
            return false;
        }
        perk = (Perk) args[0];
        if (player.getPerkManager().getPerks().containsKey(perk)) {
            interpreter.sendPlaneMessage(false, "You already own that perk.");
            return false;
        }
        interpreter.sendDialogue(
            "Are you sure you want to purchase the perk:",
            "<col=BF9647><shad=1>" + perk.getFormattedName(),
            "for " + perk.getCost() + " Gielinor tokens?"
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
                        if (player.getPerkManager().getPerks().containsKey(perk)) {
                            interpreter.sendPlaneMessage(false, "You already own that perk.");
                            return true;
                        }
                        if (player.getDonorManager().getGielinorTokens() < perk.getCost()) {
                            interpreter.sendPlaneMessage(false, "You do not have enough Gielinor tokens for that perk.");
                            return true;
                        }
                        String pointsRemaining = perk.purchase(player);
                        if (pointsRemaining != null) {
                            interpreter.sendPlaneMessage(false,
                                "You have purchased the perk:",
                                "<col=BF9647><shad=1>" + perk.getFormattedName(),
                                pointsRemaining
                            );
                            stage = 100;
                            return true;
                        }
                        end();
                        return true;
                    case TWO_OPTION_TWO:
                        end();
                        PerkManagement.openPage(player, player.getAttribute("PERKS_PAGE", 0), true);
                        break;
                }
                break;
            case 100:
                interpreter.sendPlaneMessage(false, "To enable this perk, go to the perk manager and select the", "\"Enable\" option!");
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
        return new int[]{ DialogueInterpreter.getDialogueKey("perk-purchase") };
    }
}

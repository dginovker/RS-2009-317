package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for Dimintheis.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class DimintheisDialogue extends DialoguePlugin {

    int gauntletsToBuy;

    public DimintheisDialogue() {
    }

    public DimintheisDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DimintheisDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Good day! Can I interest you in some gauntlets?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Buy Cooking gauntlets", "Buy Goldsmith gauntlets", "No thank you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        npc("It will cost you 100,000 coins.");
                        gauntletsToBuy = 0;
                        stage = 2;
                        break;
                    case THREE_OPTION_TWO:
                        npc("It will cost you 100,000 coins.");
                        gauntletsToBuy = 1;
                        stage = 2;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
                        stage = 20;
                        break;
                }
                break;
            case 2:
                interpreter.sendOptions("Select an Option", "Pay 100,000 coins.", "Nevermind.");
                stage = 3;
                break;
            case 3:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (!player.getInventory().contains(new Item(Item.COINS, 100000))) {
                            interpreter.sendPlaneMessage("You do not have enough coins.");
                            stage = 20;
                            break;
                        }
                        player.getInventory().remove(new Item(Item.COINS, 100000));
                        int gauntletId = -1;
                        switch (gauntletsToBuy) {
                            case 0:
                                gauntletId = 775;
                                break;
                            case 1:
                                gauntletId = 776;
                                break;
                        }
                        if (gauntletId == -1) {
                            end();
                        }
                        player.getInventory().add(new Item(gauntletId, 1), player);
                        npc("Pleasure doing business with you!");
                        stage = 20;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 20:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 664 };
    }
}

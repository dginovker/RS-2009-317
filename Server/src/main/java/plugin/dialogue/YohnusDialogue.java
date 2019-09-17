package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue for Yohnus.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class YohnusDialogue extends DialoguePlugin {

    private final Item COINS = new Item(Item.COINS, 20);

    public YohnusDialogue() {
    }

    public YohnusDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new YohnusDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello, can I enter this blacksmith?");
        stage = 10;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Yes, for a fee of 20 coins.", "Each time you wish to enter, it will cost you 20 coins.");
                stage = 11;
                break;
            case 11:
                interpreter.sendOptions("Pay 20 coins?", "Yes.", "No.");
                stage = 12;
                break;
            case 12:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        if (!player.getInventory().containsItem(COINS)) {
                            interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough coins.");
                            stage = END;
                            return true;
                        }
                        player.getInventory().remove(COINS);
                        npc("Thanks! You can now enter this one time.");
                        player.getSavedData().getGlobalData().setBlacksmithPaid();
                        stage = END;
                        break;
                    case 2:
                        player("No thank you.");
                        stage = END;
                        break;
                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 513 };
    }
}

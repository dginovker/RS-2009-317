package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the benny npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BennyDialogue extends DialoguePlugin {

    /**
     * Represents the item coins related to benny.
     */
    private static final Item COINS = new Item(Item.COINS, 50);

    /**
     * Represents the newspaper item related to benny.
     */
    private static final Item NEWSPAPER = new Item(11169, 1);

    /**
     * Constructs a new {@code BennyDialogue} {@code Object}.
     */
    public BennyDialogue() {
        /*(
         * empty.
         */
    }

    /**
     * Constructs a new {@code BennyDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public BennyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BennyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendOptions("Select an Option", "Can I have a newspaper, please?", "How much does a paper cost?", "Varrock Herald? Never heard of it.", "Anything interesting in there?");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can I have a newspaper, please?");
                        stage = 10;
                        break;
                    case FOUR_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How much does a paper cost?");
                        stage = 20;
                        break;
                    case FOUR_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Varrock Herald? Never heard of it.");
                        stage = 30;
                        break;
                    case FOUR_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Anything interesting in there?");
                        stage = 40;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Certainly, Guv. That'll be 50 gold pieces, please.");
                stage = 11;
                break;
            case 11:
                interpreter.sendOptions("Select an Option", "Sure, here you go.", "Uh, no thanks, I've changed my mind");
                stage = 12;
                break;
            case 12:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sure, here you go.");
                        stage = 13;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thanks.");
                        stage = 14;
                        break;
                }
                break;
            case 13:
                if (!player.getInventory().contains(Item.COINS, 50)) {
                    end();
                    player.getActionSender().sendMessage("You need 50 gold coins to buy a newspaper.");

                } else if (player.getInventory().freeSlot() == 0) {
                    end();
                    player.getActionSender().sendMessage("You don't have enough inventory space.");
                } else {
                    if (!player.getInventory().containsItem(COINS)) {
                        end();
                        return true;
                    }
                    player.getInventory().remove(COINS);
                    player.getInventory().add(NEWSPAPER);
                    end();
                }
                break;
            case 14:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ok, suit yourself. Plenty more fish in the sea.");
                stage = 100;
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Just 50 gold pieces! An absolute bargain! Want one?");
                stage = 21;
                break;
            case 21:
                interpreter.sendOptions("Select an Option", "Yes, please.", "No, thanks.");
                stage = 22;
                break;
            case 22:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, please.");
                        stage = 13;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thanks.");
                        stage = 14;
                        break;
                }
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "For the illiterate amongst us, I shall elucidate. The", "Varrock Herald is a new newspaper. It is edited, printed", "and published by myself, Benny Gutenberg, and each", "edition promises to enthrall the reader with captivating ");
                stage = 31;
                break;
            case 31:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "material! Now, can I interest you in buying one for a mere", "50 gold?");
                stage = 21;
                break;
            case 40:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Of course there is, mate. Packed full of thought provoking", "insights, contentious interviews and celebrity", "scandalmongering! An excellent read and all for just 50", "coins! Want one?");
                stage = 21;
                break;
            case 100:
                end();
                break;
        }
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5925 };
    }
}

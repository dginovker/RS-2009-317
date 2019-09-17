package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the dward cart conductor.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CartConductorDialogue extends DialoguePlugin {

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 150);

    /**
     * Represents the ticket item.
     */
    private static final Item TICKET = new Item(5020);

    /**
     * Constructs a new {@code CartConductorDialogue} {@code Object}.
     */
    public CartConductorDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code CartConductorDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public CartConductorDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CartConductorDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendOptions("Select an Option", "Who are you?", "Where can you take me?", "I'd like to buy a ticket.", "I have to go.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Who are you?");
                        stage = 10;
                        break;
                    case FOUR_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Where can you take me?");
                        stage = 20;
                        break;
                    case FOUR_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'd like to buy a ticket.");
                        stage = 30;
                        break;
                    case FOUR_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I have to go.");
                        stage = 40;
                        break;
                }
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm an employee of Keldagrim Carts. I make sure the", "carts in this area run on time and that people pay their", "fares.");
                stage = 11;
                break;
            case 11:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I don't think I'm allowed to take you into the city of", "Keldagrim, human. Perhaps when you find another way", "into the city and talk to someone of importance there", "you will be allowed to.");
                stage = 21;
                break;
            case 21:
                end();
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "One ticket to Keldagrim, that's 150 coins then.");
                stage = 31;
                break;
            case 31:
                end();
                if (player.getInventory().containsItem(COINS) && player.getInventory().remove(COINS)) {
                    if (!player.getInventory().add(TICKET)) {
                        GroundItemManager.create(TICKET, player);
                    }
                } else {
                    player.getActionSender().sendMessage("You don't have enough coins.");
                }
                break;
            case 40:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2180 };
    }
}

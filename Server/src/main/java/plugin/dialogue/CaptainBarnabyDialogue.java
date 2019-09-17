package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.travel.ship.Ships;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the captain barnaby dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CaptainBarnabyDialogue extends DialoguePlugin {

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 30);

    /**
     * Constructs a new {@code CaptainBarnabyDialogue} {@code Object}.
     */
    public CaptainBarnabyDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code CaptainBarnabyDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public CaptainBarnabyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CaptainBarnabyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Do you want to go on a trip to Brimhaven?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "The trip will cost you 30 coins.");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select an Option", "Yes please.", "No, thank you.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes please.");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 10:
                if (!player.getInventory().containsItem(COINS) && !player.getInventory().remove(COINS)) {
                    interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough coins.");
                    stage = 220;
                    return true;
                }
                end();
                player.getActionSender().sendMessage("You pay 30 coins and board the ship.");
                Ships.ARDOUGNE_TO_BRIMHAVEN.sail(player);
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 381 };
    }
}

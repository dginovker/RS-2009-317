package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.travel.ship.Ships;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue used to handle the sailing from and to karamja.
 *
 * @author 'Vexia
 * @date 28/12/2013
 */
public class SeamanDialoguePlugin extends DialoguePlugin {

    /**
     * Constructs a new {@code SeamanDialoguePlugin} {@code Object}.
     */
    public SeamanDialoguePlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code SeamanDialoguePlugin} {@code Object}.
     *
     * @param player the player.
     */
    public SeamanDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SeamanDialoguePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length > 1 && player.getQuestRepository().isComplete("Pirate's Treasure")) {
            pay();
            return true;
        } else {
            player.getActionSender().sendMessage("You may only use the Pay-fare option after completing Pirate's Treasure.");
        }
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Do you want to go on a trip to Karamja?");
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
                interpreter.sendOptions("Select an Option", "Yes, please.", "No, thank you.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, please.");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
                        stage = 20;
                        break;

                }
                break;
            case 10:
                pay();
                break;
            case 20:
                end();
                break;
        }
        return true;
    }

    /**
     * Method used to pay the fare.
     */
    public void pay() {
        end();
        if (!player.getInventory().contains(Item.COINS, 30)) {
            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, I don't have enough coins for that.");
            stage = 20;
        } else {
            end();
            player.getInventory().remove(new Item(Item.COINS, 30));
            Ships.PORT_SARIM_TO_KARAMAJA.sail(player);
            player.getActionSender().sendMessage("You pay 30 coins and board the ship.");
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{ 377, 378, 376 };
    }
}

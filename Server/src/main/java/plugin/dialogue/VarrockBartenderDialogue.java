package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * The plugin used to handle the dialogue for the varrock bartender.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class VarrockBartenderDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code VarrockBartenderDialogue} {@code Object}.
     */
    public VarrockBartenderDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code VarrockBartenderDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public VarrockBartenderDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 732, 731 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Good day to you, brave adventurer. Can I get you a", "refreshing beer?");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select an Option", "Yes please!", "No thanks.", "How much?");
                stage = 2;
                break;
            case 2:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes please!");
                        stage = 10;
                        break;

                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thanks.");
                        stage = 20;
                        break;

                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How much?");
                        stage = 30;
                        break;
                }

                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ok then, that's two gold coins please.");
                stage = 11;
                break;
            case 11:
                if (player.getInventory().contains(Item.COINS, 2)) {
                    player.getInventory().remove(new Item(Item.COINS, 2));
                    player.getInventory().add(new Item(1917, 1));
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Cheers!");
                    stage = 12;
                } else {
                    end();
                    player.getActionSender().sendMessage("You need two gold coins to buy a beer.");
                }
                break;
            case 12:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Cheers!");
                stage = 13;
                break;
            case 13:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Let me know if you change your mind.");
                stage = 21;
                break;
            case 21:
                end();
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Two gold pieces a pint. So, what do you say?");
                stage = 31;
                break;
            case 31:
                interpreter.sendOptions("Select an Option", "Yes please!", "No thanks.");
                stage = 32;
                break;
            case 32:

                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes please!");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thanks.");
                        stage = 20;
                        break;
                }

                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new VarrockBartenderDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello.");
        stage = 0;
        return true;
    }
}

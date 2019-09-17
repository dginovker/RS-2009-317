package plugin.dialogue;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for pirate jackie.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class PirateJackieDialogue extends DialoguePlugin {

    /**
     * Represents the interface component.
     */
    private static final Component COMPONENT = new Component(6);

    /**
     * Constructs a new {@code PirateJackieDialogue} {@code Object}.
     */
    public PirateJackieDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code PirateJackieDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public PirateJackieDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new PirateJackieDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ahoy there!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ahoy!");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select an Option", "What is this place?", "What do you do?", "I'd like to trade in my tickets, please.", "See you later.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What is this place?");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What do you do?");
                        stage = 20;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'd like to trade in my tickets, please.");
                        stage = 30;
                        break;
                    case 4:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "See you later.");
                        stage = 40;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Welcome to the Brimhaven Agility Arena!");
                stage = 11;
                break;
            case 11:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "If ye want to know more talk to Cap'n Izzy, he found", "it!");
                stage = 12;
                break;
            case 12:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I be the Jack o' tickets. I exchange the tickets ye", "collect in the Agility Arena for more stuff. Ye can", "obtain more agility experience or some items ye won't", "find anywhere else!");
                stage = 21;
                break;
            case 21:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sounds good!");
                stage = 22;
                break;
            case 22:
                end();
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Aye, ye be on the right track.");
                stage = 31;
                break;
            case 31:
                end();
                player.getInterfaceState().open(COMPONENT);
                break;
            case 40:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1055 };
    }
}

package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the mining tutor dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MiningtutorDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code MiningtutorDialogue} {@code Object}.
     */
    public MiningtutorDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code MiningtutorDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public MiningtutorDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MiningtutorDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendOptions("Select an Option", "Can you teach me the basics of mining please?", "Are there any mining related quests?", "Goodbye.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you teach me the basics of mining please?");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Are there are mining related quests?");
                        stage = 20;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Goodbye.");
                        stage = 30;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "If you want to know what ore's in a rock before you", "mine it, right-click the rock and select prospect from the", "menu, it will take a little time, but you'll find out what's", "in the rock before you mine.");
                stage = 11;
                break;
            case 11:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "You can also tell the ore you'll get from the colour of", "the rock.");
                stage = 12;
                break;
            case 12:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To mine, simply click on the rock to mine it, but make", "sue you have your pick with you.");
                stage = 13;
                break;
            case 13:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "When you have a full inventory, take it to the bank,", "you can find it on the roof of the castle in Lumbridge.");
                stage = 14;
                break;
            case 14:
                interpreter.sendOptions("Select an Option", "Can you teach me the basics of mining please?", "Are there any mining related quests?", "Goodbye.");
                stage = 0;
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Oh yes, if you haven't already, speak to Doric who can", "be found around the anvils north of Falador. I'm sure", "he can help you out.");
                stage = 21;
                break;
            case 21:
                interpreter.sendOptions("Select an Option", "Can you teach me the basics of mining please?", "Are there any mining related quests?", "Goodbye.");
                stage = 0;
                break;
            case 30:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 4902 };
    }
}

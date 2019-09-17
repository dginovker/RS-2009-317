package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the doric npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class DoricDialogue extends DialoguePlugin {

    /**
     * Represents the pickaxe item.
     */
    private static final Item PICKAXE = new Item(1265, 1);

    /**
     * Represents the requirement items.
     */
    private static final Item[] REQUIREMENTS = new Item[]{ new Item(436, 4), new Item(434, 6), new Item(440, 2) };

    /**
     * Constructs a new {@code DoricDialogue} {@code Object}.
     */
    public DoricDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code DoricDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public DoricDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DoricDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello traveller, how is your metalworking coming along?");
        stage = 500;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 500:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Not too bad, Doric.");
                stage = 501;
                break;
            case 501:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Good, the love of metal is a thing close to my heart.");
                stage = 502;
                break;
            case 502:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 284 };
    }
}

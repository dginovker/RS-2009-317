package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for king arthur.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class KingArthurDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code KingArthurDialogue} {@code Object}.
     */
    public KingArthurDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code KingArthurDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public KingArthurDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KingArthurDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Now I am a knight of the round table, do", "you have any more quests for me?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "No, sorry good man.");
                stage = 1;
                break;
            case 1:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 251 };
    }
}

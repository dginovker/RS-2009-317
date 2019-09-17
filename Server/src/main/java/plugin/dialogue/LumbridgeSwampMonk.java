package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the lumbridge swamp monk.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LumbridgeSwampMonk extends DialoguePlugin {

    /**
     * Constructs a new {@code LumbridgeSwampMonk} {@code Object}.
     */
    public LumbridgeSwampMonk() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code LumbridgeSwampMonk} {@code Object}.
     *
     * @param player the player.
     */
    public LumbridgeSwampMonk(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new LumbridgeSwampMonk(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Why are all of you standing around here?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "None of your business. Get lost.");
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
        return new int[]{ 651 };
    }

}

package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the jiminua dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class JiminuaDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code JiminuaDialogue} {@code Object}.
     */
    public JiminuaDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code JiminuaDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public JiminuaDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new JiminuaDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Welcome to the Jungle Store, Can I help you at all?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes please. What are you selling?", "No thanks.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        end();
                        Shops.JIMINUAS_JUNGLE_STORE.open(player);
                        break;
                    case 2:
                        interpreter.sendDialogues(player, null, "No thanks.");
                        stage = 20;
                        break;
                }
                break;
            case 20:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 560 };
    }
}

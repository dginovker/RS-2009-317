package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the camel npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CamelDialoguePlugin extends DialoguePlugin {

    /**
     * Constructs a new {@code CamelDialoguePlugin} {@code Object}.
     */
    public CamelDialoguePlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code CamelDialoguePlugin} {@code Object}.
     *
     * @param player the player.
     */
    public CamelDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CamelDialoguePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "If I go near that camel, it'll probably bite my hand off.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                end();
                player.getActionSender().sendMessage("The camel spits at you, and you jump back hurriedly.");
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2813 };
    }
}

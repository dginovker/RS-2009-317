package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the white knight dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class WhiteKnightDialoguePlugin extends DialoguePlugin {

    /**
     * The NPC ids that use this dialogue plugin.
     */
    private static final int[] NPC_IDS = { 660 };

    /**
     * Constructs a new {@code WhiteKnightDialoguePlugin} {@code Object}.
     */
    public WhiteKnightDialoguePlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code WhiteKnightDialoguePlugin} {@code Object}.
     *
     * @param player the player.
     */
    public WhiteKnightDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (npc != null) {
            npc.faceLocation(npc.getLocation().transform(0, -1, 0));
        }
        interpreter.sendPlaneMessage("He is too busy dancing to talk!");
        return true;
    }

    @Override
    public int[] getIds() {
        return NPC_IDS;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new WhiteKnightDialoguePlugin(player);
    }

}

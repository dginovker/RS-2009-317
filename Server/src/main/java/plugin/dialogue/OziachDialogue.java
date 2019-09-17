package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue used to handle the oziach dialogue.
 *
 * @author 'Vexia
 */
public final class OziachDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code OziachDialogue} {@code Object}.
     */
    public OziachDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code OziachDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public OziachDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new OziachDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Aye, 'tis a fair day my friend.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                options("I'm not your friend.", "Yes, it's a very nice day.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        player("I'm not your friend.");
                        stage = 10;
                        break;
                    case 2:
                        player("Yes, it's a very nice day.");
                        stage = 20;
                        break;
                }
                break;
            case 10:
                npc("I'm surprised if you're anyone's friend with those kind", "of manners.");
                stage = 11;
                break;
            case 11:
                end();
                break;
            case 20:
                npc("Aye, may the gods walk by yer side. Now leave me", "alone.");
                stage = 21;
                break;
            case 21:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 747 };
    }
}

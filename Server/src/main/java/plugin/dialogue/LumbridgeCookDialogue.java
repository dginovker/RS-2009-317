package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the lumbridge cook.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LumbridgeCookDialogue extends DialoguePlugin {

    /**
     * Represents the milk item.
     */
    private static final Item MILK = new Item(1927, 1);

    /**
     * Represents the flour item.
     */
    private static final Item FLOUR = new Item(1933, 1);

    /**
     * Represents the eggs item.
     */
    private static final Item EGG = new Item(1944, 1);

    /**
     * Constructs a new {@code LumbridgeCookDialogue} {@code Object}.
     */
    public LumbridgeCookDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code LumbridgeCookDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public LumbridgeCookDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new LumbridgeCookDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Hello friend, how is the adventuring going?");
        stage = 0;
        return true;
    }


    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                player("I am getting strong and mighty. Grrr");
                stage = 93;
                break;
            case 93:
                npc("Glad to hear it.");
                stage = 94;
                break;
            case 94:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 278 };
    }
}

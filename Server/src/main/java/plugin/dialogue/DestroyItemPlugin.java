package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import plugin.activity.motherloadmine.GemBagPlugin;

/**
 * Represents the destroy item plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class DestroyItemPlugin extends DialoguePlugin {

    /**
     * Represents the sound to send for destroying an item.
     */
    private static final Audio AUDIO = new Audio(4500, 1, 0);

    /**
     * Represents the item parameter.
     */
    private Item item;

    /**
     * Constructs a new {@code DestroyItemPlugin} {@code Object}.
     */
    public DestroyItemPlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code DestroyItemPlugin} {@code Object}.
     *
     * @param player the player.
     */
    public DestroyItemPlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DestroyItemPlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        item = (Item) args[0];
        interpreter.sendDestroyItem(item.getId());
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        int buttonId = optionSelect.getId();
        if (buttonId == 14175) {
            if (player.getInventory().remove(item)) {
                end();
                return true;
            }
        }
        if (buttonId == 14176) {
            end();
            return true;
        }
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("DESTROY_ITEM") };
    }

}

package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for the Rotten Potato on an item.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class RottenPotatoItemEditor extends DialoguePlugin {

    /**
     * The item to edit.
     */
    private Item item;

    /**
     * Constructs a new {@code RottenPotatoItemEditor} {@code Object}.
     */
    public RottenPotatoItemEditor() {

    }

    /**
     * Constructs a new {@code RottenPotatoItemEditor} {@code Object}.
     *
     * @param player the player.
     */
    public RottenPotatoItemEditor(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RottenPotatoItemEditor(player);
    }

    @Override
    public boolean open(Object... args) {
        if (!player.getDetails().getRights().isAdministrator()) {
            return true;
        }
        if (!(args[0] instanceof Item)) {
            return true;
        }
        item = (Item) args[0];
        interpreter.sendOptions("Select an Option", "Edit Bonuses", "Edit Name", "Edit Examine", "Edit Equip Slot", "More...");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect) {
                    case FIVE_OPTION_FIVE:
                        interpreter.sendOptions("Select an Option", "Edit Animations", "Remove Head", "Remove Beard", "Remove Sleeves", "More...");
                        stage = 1;
                        break;
                }
                break;

            case 1:
                switch (optionSelect) {
                    case FIVE_OPTION_FIVE:
                        interpreter.sendOptions("Select an Option", "Two Handed", "Remove Head", "Remove Beard", "Remove Sleeves", "More...");
                        stage = 1;
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("RottenPotatoItemEditor") };
    }

}

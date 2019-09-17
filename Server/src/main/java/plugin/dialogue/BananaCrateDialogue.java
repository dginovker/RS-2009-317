package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Represents the banana crate dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BananaCrateDialogue extends DialoguePlugin {

    /**
     * Represents the dialogue id.
     */
    public static final int ID = 9682749;

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(832);

    /**
     * Represents the banana item.
     */
    private static final Item BANANA = new Item(1963);

    /**
     * Constructs a new {@code BananaCrateDialogue} {@code Object}.
     */
    public BananaCrateDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BananaCrateDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public BananaCrateDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BananaCrateDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendOptions("Do you want to take a banana?", "Yes.", "No.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (player.getInventory().add(BANANA)) {
                            player.animate(ANIMATION);
                            player.getActionSender().sendMessage("You take a banana.");
                        }
                        end();
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ ID };
    }
}

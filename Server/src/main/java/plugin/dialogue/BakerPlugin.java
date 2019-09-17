package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the baker.
 *
 * @author 'Vexia
 */
public final class BakerPlugin extends DialoguePlugin {

    /**
     * Constructs a new {@code BakerPlugin} {@code Object}.
     */
    public BakerPlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BakerPlugin} {@code Object}.
     *
     * @param player the player.
     */
    public BakerPlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BakerPlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Good day, monsieur. Would you like ze nice freshly-baked", "bread? Or perhaps a nice piece of cake?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Let's see what you have.", "No, thank you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        Shops.ARDOUGNE_BAKERS_STALL.open(player);
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
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
        return new int[]{ 571 };
    }
}

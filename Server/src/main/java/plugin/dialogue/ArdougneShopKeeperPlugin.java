package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the arougne shop keepers.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ArdougneShopKeeperPlugin extends DialoguePlugin {

    /**
     * Constructs a new {@code ArdouShopKeeperPlugin} {@code Object}.
     */
    public ArdougneShopKeeperPlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code ArdouShopKeeperPlugin} {@code Object}.
     *
     * @param player the player.
     */
    public ArdougneShopKeeperPlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ArdougneShopKeeperPlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello, you look like a bold adventurer. You've come to the", "right place for adventurers' equipment.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Oh, that sounds interesting.", "How should I use your shop?", "No, sorry, I've come to the wrong place.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        end();
                        Shops.AEMADS_ADVENTURING_SUPPLIES.open(player);
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm glad you ask! You can buy as many of the items", "stocked as you wish. You can also sell most items to the", "shop.");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hmph. Well, perhaps next time you'll need something", "from me?");
                        stage = 30;
                        break;

                }
                break;
            case 20:
                end();
                break;
            case 30:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 590, 591 };
    }
}

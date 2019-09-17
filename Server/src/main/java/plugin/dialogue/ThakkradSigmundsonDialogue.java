package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * The dialogue for Thakkrad Sigmundson.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class ThakkradSigmundsonDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@link ThakkradSigmundsonDialogue} {@link DialoguePlugin}.
     */
    public ThakkradSigmundsonDialogue() {
    }

    /**
     * Constructs a new {@link ThakkradSigmundsonDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public ThakkradSigmundsonDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ThakkradSigmundsonDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (true) {
            end();
            return true;
        }
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Could you cure my Yak hides for me?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                if (player.getInventory().contains(10818)) {
                    npc("Sure! It will cost you 5 gp for the lot.");
                    stage = 1;
                }
                npc("You don't have any hides that need curing.");
                stage = END;
                break;
            case 1:
                options("Pay 5 gp to cure " + player.getInventory().getCount(new Item(10818)) + " yak hides", "No thank you");
                stage = 2;
                break;
            case 2:

                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5506 };
    }
}

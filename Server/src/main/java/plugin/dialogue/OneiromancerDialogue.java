package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * The dialogue plugin for the Oneiromancer.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class OneiromancerDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@link OneiromancerDialogue} {@link DialoguePlugin}.
     */
    public OneiromancerDialogue() {
    }

    /**
     * Constructs a new {@link OneiromancerDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public OneiromancerDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new OneiromancerDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("Why hello, how are you today?");
                stage = 1;
                break;
            case 1:
                player("I'm...");
                stage = 2;
                break;
            case 2:
                npc("Fine.", "Yes I know.");
                stage = 3;
                break;
            case 3:
                player("What? How'd you...");
                stage = 4;
                break;
            case 4:
                npc("By reading your mind.", "Would you like to purchase any Lunar equipment?");
                stage = 5;
                break;
            case 5:
                options("Yes, please.", "No, thank you.");
                stage = 6;
                break;
            case 6:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        end();
                        Shops.LUNAR_EQUIPMENT.open(player);
                        break;
                    case 2:
                        player("No, thank you.");
                        stage = END;
                        break;
                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 4511 };
    }
}

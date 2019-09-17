package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Grace.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GraceDialogue extends DialoguePlugin {

    public GraceDialogue() {

    }

    public GraceDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GraceDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NORMAL, "What can I do for you?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("What would you like to say?", "I don't know. What can you do for me?", "Can I see what you're selling?", "I'm alright, thanks.");
                stage = 1;
                break;

            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "I don't know. What can you do for me?");
                        stage = 2;
                        break;
                    case THREE_OPTION_TWO:
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "Can I see what you're selling?");
                        stage = 6;
                        break;

                    case THREE_OPTION_THREE:
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "I'm alright, thanks.");
                        stage = END;
                        break;
                }
                break;

            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL, "A good question indeed! I'm selling special clothing for", "Agility enthusiasts.");
                stage = 3;
                break;

            case 3:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Sometimes, when you're exploring an Agility course", "you'll find one of my Marks.");
                stage = 4;
                break;

            case 4:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Once you've got enough Marks, I'll exchange them for", "my Graceful clothing. So does that interest you?");
                stage = 5;
                break;

            case 5:
                interpreter.sendOptions("What would you like to say?", "Can I see what you're selling?", "I'm alright, thanks.");
                stage = 1;
                break;

            case 6:
                end();
                Shops.GRACES_GRACEFUL_CLOTHING.open(player);
                break;

            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 8602 };
    }
}

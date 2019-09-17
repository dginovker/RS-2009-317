package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Reprepsents the dialogue plugin used to handle the brian orichard npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BrianORichardDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code BrianORichardDialogue} {@code Object}.
     */
    public BrianORichardDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BrianORichardDialogue}{@code Object}.
     *
     * @param player the player.
     */
    public BrianORichardDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BrianORichardDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hi there, looking for a challenge are you?");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1:
                interpreter.sendOptions("Select an Option", "Yes, actually, what've you got?", "What is this place?", "No thanks.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes actually, what've you got?");
                        stage = 10;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What is this place?");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thanks.");
                        stage = 30;
                        break;
                }
                break;
            case 10:
                if (player.getSkills().getLevel(Skills.THIEVING) < 50) {
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Shame, I don't think I have anything for you. Train up", "your Thieving skill to at least 50 and I might be able to", "help you out.");
                    stage = 11;
                } else {
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Shame, I don't think I have anything for you. Train up", "your Thieving skill to at least 50 and I might be able to", "help you out.");
                    stage = 11;
                }
                break;
            case 11:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ah welcome to my humble home, well actually it belongs", "to mummsie but she's getting on a bit so I look after", "the place for her.");
                stage = 21;
                break;
            case 21:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "So are you interested in a challenge?");
                stage = 22;
                break;
            case 22:
                interpreter.sendOptions("Select an Option", "Yes actually, what've you got?", "No thanks.");
                stage = 23;
                break;
            case 23:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes actually, what've you got?");
                        stage = 24;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thanks.");
                        stage = 25;
                        break;
                }
                break;
            case 24:
                if (player.getSkills().getLevel(Skills.THIEVING) < 50) {
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Shame, I don't think I have anything for you. Train up", "your Thieving skill to at least 50 and I might be able to", "help you out.");
                    stage = 25;
                } else {

                }
                break;
            case 25:
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
        return new int[]{ 2266 };
    }
}

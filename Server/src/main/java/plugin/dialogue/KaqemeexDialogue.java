package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.Skillcape;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the kaqemeex dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class KaqemeexDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code KaqemeexDialogue} {@code Object}.
     */
    public KaqemeexDialogue() {
    }

    /**
     * Constructs a new {@code KaqemeexDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public KaqemeexDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KaqemeexDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NORMAL, "Hello there.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, null, "Hello again. How is the Herblore going?");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(player, null, "Good good!");
                stage = Skillcape.isMaster(player, Skills.HERBLORE) ? 2 : 8;
                break;
            case 2:
                interpreter.sendOptions("Select an Option", "Can I buy a Skillcape of Herblore?", "I'll be on my way now.");
                stage = 3;
                break;
            case 3:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "Can I buy a Skillcape of Herblore?");
                        stage = 4;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.CONFUSED, "Well, I'll be on my way now.");
                        stage = END;
                        break;
                }
                break;
            case 4:
                npc("Certainly! Right when you give me 99000 coins.");
                stage = 5;
                break;
            case 5:
                options("Yes, here you go,", "No, thanks.");
                stage = 6;
                break;
            case 6:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, here you go.");
                        stage = 7;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 7:
                if (Skillcape.purchase(player, Skills.HERBLORE)) {
                    npc("There you go! Enjoy.");
                }
                stage = END;
                break;
            case 8:
                interpreter.sendDialogues(player, FacialExpression.NORMAL, "Well, I'll be on my way now.");
                stage = END;
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 455 };
    }
}

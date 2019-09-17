package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the captain dalbur npc.
 *
 * @author 'Vexia
 * @version 1.0
 * @note complete with gnome city stuff.
 */
public final class CaptainDalburDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code CaptainDalburDialogue} {@code Object}.
     */
    public CaptainDalburDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code CaptainDalburDialogue.java} {@code Object}.
     *
     * @param player the player.
     */
    public CaptainDalburDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CaptainDalburDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length == 2) {
            stage = 1;
            handle(-1, null);
            return true;
        }
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What do you want human?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "May you fly me somewhere on your glider?");
                stage = 1;
                break;
            case 1:
                if (player.getSkills().getStaticLevel(Skills.COOKING) < 99) {
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I only fly those who have mastered the virtue", "of cooking!");
                    stage = END;
                    break;
                }
                player.getInterfaceState().openComponent(802);
                stage = 2;
                break;
            case 2:
                end();
                player.getInterfaceState().openComponent(802);
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3809 };
    }
}

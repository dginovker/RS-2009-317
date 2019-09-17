package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Reprents the dialogue plugin used for the edgevill monk.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class EdgevilleMonkDialogue extends DialoguePlugin {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(710);

    /**
     * Represents the graphics to use.
     */
    private static final Graphics GRAPHIC = new Graphics(84);

    /**
     * Constructs a new {@code EdgevilleMonkDialogue} {@code Object}.
     */
    public EdgevilleMonkDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code EdgevilleMonkDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public EdgevilleMonkDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new EdgevilleMonkDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Greetings traveller.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Can you heal me? I'm injured.", "Isn't this place built a bit out of the way?", "How do I get further into the monastery?");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you heal me? I'm injured.");
                        stage = 10;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Isn't this place built a bit out of the way?");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How do i get farther into the monastery?");
                        stage = 30;
                        break;

                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ok.");
                stage = 11;
                break;
            case 11:
                end();
                npc.animate(ANIMATION);
                npc.graphics(GRAPHIC);
                player.getActionSender().sendMessage("You feel a little better.");
                player.getSkills().heal(((int) (player.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.20)));
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "We like it that way actually! We get disturbed less. We still", "get rather a large amount of travellers looking for", "sanctuary and healing here as it is!");
                stage = 21;
                break;
            case 21:
                end();
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "You'l need to talk to Abbot Langley about that. He's", "usually to be found walking the halls of the monastery.");
                stage = 21;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7727 };
    }
}

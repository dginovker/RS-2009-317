package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Represents the abbot langley dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class AbbotLangleyDialogue extends DialoguePlugin {

    /**
     * Represents the healing animation to use for the npc.
     */
    private static final Animation ANIMATION = new Animation(717);

    /**
     * Represents the healing craphics to use for the npc.
     */
    private static final Graphics GRAPHICS = new Graphics(84);

    /**
     * Constructs a new {@code AbbotLangleyDialogue} {@code Object}.
     */
    public AbbotLangleyDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code AbbotLangleyDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public AbbotLangleyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AbbotLangleyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Greetings traveller.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Can you heal me? I'm injured.", "Isn't this place built a bit out the way?");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Can you heal me? I'm injured.");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        player("Isn't this place built a bit out of the way?");
                        stage = 20;
                        break;
                }
                break;
            case 10:
                npc("Ok.");
                stage = 11;
                break;
            case 11:
                player.getSkills().heal(((int) (player.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.20)));
                npc.animate(ANIMATION);
                npc.graphics(GRAPHICS);
                interpreter.sendPlaneMessage("Abbot Langley places his hands on your head. You feel a little better.");
                stage = 12;
                break;
            case 12:
                end();
                break;
            case 20:
                npc("We like it that way actually! We get disturbed less. We still", "get rather a large amount of travellers looking for", "sanctuary and healing here as it is!");
                stage = 21;
                break;
            case 21:
                end();
                break;

        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 801 };
    }
}

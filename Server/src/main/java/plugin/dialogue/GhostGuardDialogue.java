package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for the ghost guards.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class GhostGuardDialogue extends DialoguePlugin {

    /**
     * Constructs a new <code>GhostGuardDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public GhostGuardDialogue() {
    }

    /**
     * Constructs a new <code>GhostGuardDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public GhostGuardDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GhostGuardDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("All visitors to Port Phasmatys must pay a toll charge of", "2 Ectotokens.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                options("I don't have that many Ectotokens.", "Where can I get Ecototokens?");
                stage = 1;
                break;

            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("I don't have that many Ectotokens.");
                        stage = 2;
                        break;

                    case TWO_OPTION_TWO:
                        player("Where can I get my Ectotokens?");
                        stage = 3;
                        break;
                }
                break;

            case 2:
                npc("In that case, you need to go to the Temple and earn", "some. Talk to the disciples - they will tell you how.");
                stage = END;
                break;

            case 3:
                npc("You need to go to the Temple and earn some.", "Talk to the disciples - they will tell you how.");
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
        return new int[]{ 3155 };
    }
}

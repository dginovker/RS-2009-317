package plugin.dialogue;

import java.security.SecureRandom;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents the Hunting Expert at home {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class HomeHuntingExpert extends DialoguePlugin {

    /**
     * Constructs a new {@code HomeHuntingExpert} {@code Object}.
     */
    public HomeHuntingExpert() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code HomeHuntingExpert} {@code Object}.
     *
     * @param player the player.
     */
    public HomeHuntingExpert(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new HomeHuntingExpert(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hey there, stranger. Would you like to teleport to a", "hunter area? Or perhaps purchase some hunter gear?");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1:
                interpreter.sendOptions("Select an Option", "Teleport to a hunter area.", "Purchase some hunter gear.", "Ahh, leave me alone you crazy killing person.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendOptions("Select an area",
                            "Feldip Hills area (jungle)",
                            "Rellekka Hunter area (polar)",
                            "Piscatoris Hunter area (woodland)",
                            "Uzer Hunter area (desert)", "More...");
                        stage = 3;
                        break;
                    case THREE_OPTION_TWO:
                        player("I'd like to purchase some hunter gear.");
                        stage = 5;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ahh, leave me alone you crazy killing person.");
                        stage = END;
                        break;
                }
                break;
            case 3:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        end();
                        player.getTeleporter().send(new Location(2525, 2915, 0, 2));
                        break;
                    case FIVE_OPTION_TWO:
                        end();
                        player.getTeleporter().send(new Location(2720, 3781, 0, 2));
                        break;
                    case FIVE_OPTION_THREE:
                        end();
                        player.getTeleporter().send(new Location(2336, 3586, 0, 2));
                        break;
                    case FIVE_OPTION_FOUR:
                        end();
                        player.getTeleporter().send(new Location(3404, 3107, 0, 2));
                        break;
                    case FIVE_OPTION_FIVE:
                        interpreter.sendOptions("Select an area",
                            "Swamp lizard area",
                            "Falconry area",
                            "Puro-Puro",
                            "Back",
                            "Never mind");
                        stage = 4;
                        break;
                }
                break;
            case 4:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        end();
                        player.getTeleporter().send(new Location(3536, 3448, 0, 1));
                        break;
                    case FIVE_OPTION_TWO:
                        end();
                        player.getTeleporter().send(new Location(2374, 3605, 0, 3));
                        break;
                    case FIVE_OPTION_THREE:
                        end();
                        player.getTeleporter().send(new Location(2418, 4444 + (new SecureRandom().nextInt(1)), 0));
                        break;
                    case FIVE_OPTION_FOUR:
                        interpreter.sendOptions("Select an area",
                            "Feldip Hills area (jungle)",
                            "Rellekka Hunter area (polar)",
                            "Piscatoris Hunter area (woodland)",
                            "Uzer Hunter area (desert)", "More...");
                        stage = 3;
                        break;
                    case FIVE_OPTION_FIVE:
                        player("Never mind...");
                        stage = END;
                        break;

                }
                break;
            case 5:
                npc("Very well, I'll teleport you to the fancy-dress shop owner!");
                stage = 6;
                break;

            case 6:
                end();
                player.getTeleporter().send(new Location(3272, 3398, 0, 2));
                break;

            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5112 };
    }
}

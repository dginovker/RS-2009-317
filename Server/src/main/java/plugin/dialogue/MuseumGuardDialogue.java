package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;

/**
 * Represents the museum guard dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MuseumGuardDialogue extends DialoguePlugin {

    /**
     * Represents the gate location.
     */
    private static final Location LOCATION = new Location(3261, 3446, 0);

    /**
     * Constructs a new {@code MuseumGuardDialogue} {@code Object}.
     */
    public MuseumGuardDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code MuseumGuardDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public MuseumGuardDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MuseumGuardDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendDialogues(5941, FacialExpression.NO_EXPRESSION, "Welcome! Would you like to go into the Dig Site", "archaeology cleaning area?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes, I'll go in!", "No thanks, I'll take a look around out there.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, I'll go in!");
                        stage = 20;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thanks, I'll take a look around out there.");
                        stage = 3;
                        break;
                }
                break;
            case 3:
                end();
                break;
            case 20:
                end();
                DoorActionHandler.handleAutowalkDoor(player, RegionManager.getObject(LOCATION));
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5941 };
    }

}

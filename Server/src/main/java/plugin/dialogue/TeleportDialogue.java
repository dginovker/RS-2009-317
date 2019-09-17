package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

import plugin.interaction.inter.TeleportsInterfacePlugin;

/**
 * The dialogue plugin for the teleports.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class TeleportDialogue extends DialoguePlugin {

    /**
     * The selected teleport location.
     */
    private Location location;
    /**
     * The warning message (if any).
     */
    private String warningMessage;

    /**
     * Constructs a new {@link TeleportDialogue} {@link DialoguePlugin}.
     */
    public TeleportDialogue() {
    }

    /**
     * Constructs a new {@link TeleportDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public TeleportDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new TeleportDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        stage = (int) args[0];
        String warningMessage = (String) args[2];
        interpreter.sendPlaneMessage(warningMessage.split("<br>"));
        location = (Location) args[3];
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1:
                if (location == null) {
                    player.getInterfaceState().close();
                    player.getDialogueInterpreter().close();
                    return true;
                }
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    TeleportsInterfacePlugin.handleTeleport(player, location.randomize(), null, false);
                    return true;
                }
                end();
                return true;
            case 3:
                interpreter.sendOptions("Teleport to Wilderness?", "Yes.", "No.");
                stage = 1;
                return true;
            case 4:
                interpreter.sendOptions("Teleport?", "Yes.", "No.");
                stage = 1;
                return true;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("TeleportDialogue") };
    }
}

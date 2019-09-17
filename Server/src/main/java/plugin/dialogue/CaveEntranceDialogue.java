package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue for the Mos Le'Harmless cave entrance.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class CaveEntranceDialogue extends DialoguePlugin {

    public CaveEntranceDialogue() {
    }

    public CaveEntranceDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CaveEntranceDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendPlaneMessage("You look in the cave entrance, and can see it is very, very dark. It", "also sounds like there are many large creatures moving around in", "there.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Do you still want to proceed?", "Yes, I think I am fully prepared.", "No, I think I need to go get food, weapons and a light source.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player.setTeleportTarget(player.getLocation().transform(-1, 6400, 0));
                        end();
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("CaveEntrance") };
    }
}

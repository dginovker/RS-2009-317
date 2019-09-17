package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SkeletalWyvernWarning extends DialoguePlugin {

    public SkeletalWyvernWarning() {

    }

    public SkeletalWyvernWarning(final Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SkeletalWyvernWarning(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendPlaneMessage(false, "STOP! The creatures in this cave are VERY dangerous. Are you", "sure you want to enter?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                options("Yes, I'm not afraid of death!", "No thanks, I don't want to die!");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        player.getProperties().setTeleportLocation(Location.create(3056, 9555, 0));
                        player.getActionSender().sendMessage("You venture into the icy cavern.");
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
        return new int[]{ DialogueInterpreter.getDialogueKey("SkeletalWyvernWarning") };
    }

}

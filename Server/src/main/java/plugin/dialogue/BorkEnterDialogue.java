package plugin.dialogue;

import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for entering the Bork zone.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BorkEnterDialogue extends DialoguePlugin {

    public BorkEnterDialogue() {
        /**
         * empty.
         */
    }


    public BorkEnterDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BorkEnterDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendDialogue("<col=8A0808>Warning!", "Bork is not to be taken lightly, agreeing to teleport", "to his lair means you are ready for a challenge.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogue("If you die, you will not be able to retrieve your items.", "", "And not many have survived Bork...");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Teleport?", "Yes, teleport to Bork, I understand the risks.", "No, stay here.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if ((System.currentTimeMillis() - player.getSavedData().getActivityData().getLastBorkBattle())
                            < (12 * 60 * 60_000)) {
                            player.getActionSender().sendMessage("The portal's magic is too weak to teleport you right now.");
                            end();
                            return true;
                        }
                        player.lock(3);
                        player.graphics(Graphics.create(110));
                        if (!player.getSavedData().getActivityData().hasKilledBork()) {
                            player.setAttribute("first-bork", true);
                        }
                        player.getSavedData().getActivityData().setLastBorkBattle(System.currentTimeMillis());
                        ActivityManager.start(player, "Bork cutscene", false);
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;

                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("BorkDialoguePlugin") };
    }

}

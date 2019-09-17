package plugin.dialogue;

import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.config.Constants;

import plugin.cutscene.StarterCutscenePlugin;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for the starter guide.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class StarterGuideDialogue extends DialoguePlugin {

    /**
     * The {@link plugin.cutscene.StarterCutscenePlugin}.
     */
    private StarterCutscenePlugin cutscenePlugin;

    /**
     * Constructs a new {@link plugin.dialogue.StarterGuideDialogue} {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public StarterGuideDialogue() {
    }

    /**
     * Constructs a new {@link plugin.dialogue.StarterGuideDialogue} {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public StarterGuideDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new StarterGuideDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        boolean startCutscene = args.length == 1 && args[0] instanceof Boolean && ((Boolean) args[0]);
        if (startCutscene) {
            interpreter.sendPlaneMessage("Would you like to view the starter cutscene?");
            stage = 11;
            return true;
        }
        this.cutscenePlugin = player.getAttribute("in-cutscene", null);
        if (cutscenePlugin == null) {
            end();
            return true;
        }
        interpreter.sendPlaneMessage(false, "Welcome to " + Constants.SERVER_NAME + ".");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendPlaneMessage("This is the " + Constants.SERVER_NAME + " home.", "Here you can find banking, shops, and more.");
                stage = 1;
                break;
            case 1:
                cutscenePlugin.next();
                interpreter.sendPlaneMessage("When you teleport home, you will see", "these bank booths and various NPCs", "around.");
                stage = 2;
                break;
            case 2:
                interpreter.sendPlaneMessage("These NPCs will help you with the basics, such as", "selecting your title, claiming voting rewards, claiming", "donation rewards, and more.");
                stage = 3;
                break;
            case 3:
                interpreter.sendPlaneMessage("To teleport around you can use spells in your Magic", "book. If you wish to visit places for training,", "minigames, and more, click the world icon on your minimap.");
                stage = 4;
                break;
            case 4:
                cutscenePlugin.next();
                interpreter.sendPlaneMessage("If you have chosen the option, you will be able to find", "iron man help East of the home teleport.", "The NPCs here will supply you with", "your armour, and give you access to");
                stage = 5;
                break;
            case 5:
                interpreter.sendPlaneMessage("various shops dedicated to iron men.");
                stage = 6;
                break;
            case 6:
                cutscenePlugin.next();
                interpreter.sendPlaneMessage("If you are not an iron man, you can find shops", "to the West of the home teleport.", "These shops include basic items to help you", "along on your adventure.");
                stage = 7;
                break;
            case 7:
                cutscenePlugin.next();
                interpreter.sendPlaneMessage("Directly South of shops are altars to change your", "magic books and recharge your Prayer points.", "Along-side them you will find a Slayer master", "who will assign you assignments.");
                stage = 8;
                break;
            case 8:
                cutscenePlugin.next();
                interpreter.sendPlaneMessage("Once you set off, if you need any help you can", "talk to the <col=992323>" + Constants.SERVER_NAME + " Guide</col>, located at home.", "You can also view this cutscene by talking to him.", "If you need additional help, you can ask other");
                stage = 9;
                break;
            case 9:
                interpreter.sendPlaneMessage(
                    "players, type ::help, or join the \"Help\" clan chat.",
                    "If you need additional assistance, or help from staff,",
                    "you can go to our forums by typing ::forums");
                stage = 10;
                break;
            case 10:
                cutscenePlugin.end();
                end();
                return true;

            case 11:
                interpreter.sendOptions("View starter cutscene?", "Yes", "No");
                stage = 12;
                break;

            case 12:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        player.lock();
                        ActivityManager.start(player, "starter cutscene", false);
                        return true;
                    case TWO_OPTION_TWO:
                        interpreter.sendPlaneMessage("If you wish to view the cutscene any time in the", "future, you can talk to the " + Constants.SERVER_NAME + " Guide", "who is located at home.");
                        stage = END;
                        break;
                }
                break;

            case END:
                end();
                return true;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("StarterGuide") };
    }
}

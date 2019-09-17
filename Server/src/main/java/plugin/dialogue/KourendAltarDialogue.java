package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import plugin.interaction.item.ReferralTicketPlugin;
import plugin.zone.kourend.CatacombsOfKourendZone;

/**
 * Created by Stan van der Bend on 13/01/2018.
 * project: Gielinor-Server
 * package: plugin.dialogue
 */
public class KourendAltarDialogue extends DialoguePlugin {

    private final static String
        OPTION_TITLE = "You will lose all of your items dropped if you die!",
        OPTION_ACCEPT = "I know I'm risking everything I have",
        OPTION_DECLINE =  "I need to prepare some more.",

        OPTION_CONFIRMATION_TITLE = "Are you sure?",
        OPTION_CONFIRM = "Yes, I know items dropped in the instance will be lost.",
        OPTION_NEVERMIND = "On second thoughts, better not.";

    @Override
    public DialoguePlugin newInstance(Player player) {
        return null;
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendOptions(OPTION_TITLE, OPTION_ACCEPT, OPTION_DECLINE);
        return true;
    }
    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:

                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendOptions(OPTION_CONFIRMATION_TITLE, OPTION_CONFIRM, OPTION_NEVERMIND);
                        stage = 1;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;

            case 1:
                end();
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        CatacombsOfKourendZone.activateAltar(player);
                        break;
                }
                break;
        }
        return true;
    }
    @Override
    public int[] getIds() {
        return new int[0];
    }
}

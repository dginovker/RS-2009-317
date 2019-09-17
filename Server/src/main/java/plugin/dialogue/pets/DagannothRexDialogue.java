package plugin.dialogue.pets;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * @author Erik
 * @author Harry
 */
public class DagannothRexDialogue extends DialoguePlugin {

    public DagannothRexDialogue() {
    }

    private DagannothRexDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DagannothRexDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];

        if (npc instanceof Familiar) {
            Familiar familiar = (Familiar) npc;
            if (familiar.getOwner() != player) {
                player.getActionSender().sendMessage("It doesn't seem interested in talking to you.");
                return true;
            }
        } else {
            player.getActionSender().sendMessage("It doesn't seem interested in talking to you.");
            return true;
        }

        int roll = RandomUtil.random(30);
        if (roll < 30) {
            player("Do you have any berserker rings?");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("Nope.");
                stage = 1;
                break;

            case 1:
                player("You sure?");
                stage = 2;
                break;
            case 2:
                npc("Yes.");
                stage = 3;
                break;
            case 3:
                player("So, if I tipped you upside down and shook you,", "you'd not drop any berserker rings?");
                stage = 4;
                break;
            case 4:
                npc("Nope.");
                stage = 5;
                break;
            case 5:
                player("What if I endlessly killed your father for", "weeks on end, would I get one then?");
                stage = 6;
                break;
            case 6:
                npc("Been done by someone, nope.");
                stage = 0xBABE;
                break;


            case 0xBABE:
                end();
                break;
        }

        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{
            26630
        };
    }

}

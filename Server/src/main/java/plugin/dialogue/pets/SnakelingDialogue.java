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
public class SnakelingDialogue extends DialoguePlugin {

    public SnakelingDialogue() {
    }

    private SnakelingDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SnakelingDialogue(player);
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
            player("Hey little snake!");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("Soon, Zulrah shall establish dominion over this plane.");
                stage = 1;
                break;

            case 1:
                player("Wanna play fetch?");
                stage = 2;
                break;
            case 2:
                npc("Submit to the almighty Zulrah.");
                stage = 3;
                break;
            case 3:
                player("Walkies? Or slidies...?");
                stage = 4;
                break;
            case 4:
                npc("Zulrah's wilderness as a God will soon be demonstrated.");
                stage = 5;
                break;
            case 5:
                player("I give up...");
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
            22130,
            22131,
            22132
        };
    }

}
